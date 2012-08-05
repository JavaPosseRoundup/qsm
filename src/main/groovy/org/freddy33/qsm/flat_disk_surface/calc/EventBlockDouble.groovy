package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.dbl.*

import static org.freddy33.math.bigInt.TrigoInt.ONE

public class EventBlockOrigin {
    final Point4d origin
    final BigInteger createdTime
    final EulerAngles3d blockPlane
    final SphericalUnitVector2d moment

    EventBlockOrigin(Point3d o, BigInteger time, SphericalUnitVector2d moment, double psi) {
        this.origin = new Point4d(o.x, o.y, o.z, (double) time)
        this.createdTime = time
        this.blockPlane = new EulerAngles3d(moment, psi)
        this.moment = moment
    }

    EventBlockOrigin(EventBlockOrigin copy, BigInteger newTime) {
        this.origin = new Point4d(copy.origin.x, copy.origin.y, copy.origin.z, (double) newTime)
        this.createdTime = newTime
        this.blockPlane = copy.blockPlane
        this.moment = copy.moment
    }

    @Override
    String toString() {
        "ebo{$origin,$moment}"
    }
}

public class EventBlockDouble implements Transformer3dto4d {
    final EventBlockOrigin o
    final BlockType type
    final EventFlat[] events = new EventFlat[4]
    final Triangle3d[] triangles = new Triangle3d[4]
    double s

    public EventBlockDouble(Point3d o, BlockType type, BigInteger time, SphericalUnitVector2d moment, double psi, double size) {
        this.o = new EventBlockOrigin(o, time, moment, psi)
        this.type = type
        setEvents(type.createEvents(this.o, size))
    }

    public EventBlockDouble(EventBlockDouble from, BigInteger newTime) {
        this.o = new EventBlockOrigin(from.o, newTime)
        this.type = from.type
    }

    private setEvents(def pEvents) {
        for (int i = 0; i < events.length; i++) {
            events[i] = pEvents[i];
        }
        // Keeping circulating triangles t[0] -> p1,p2,p3
        triangles[0] = new Triangle3d(events[1].point, events[2].point, events[3].point)
        triangles[1] = new Triangle3d(events[0].point, events[2].point, events[3].point)
        triangles[2] = new Triangle3d(events[0].point, events[1].point, events[3].point)
        triangles[3] = new Triangle3d(events[0].point, events[1].point, events[2].point)
        s = triangles.sum(0d) { it.s }
    }

    public static EventBlockDouble createPhoton(Point3d origin, BigInteger time, SphericalUnitVector2d k, double psi, BigInteger size) {
        new EventBlockDouble(origin, BlockType.FlatStar, time, k, psi, (double)size*ONE)
    }

    public static EventBlockDouble createElectron(Point3d origin, BigInteger time, SphericalUnitVector2d k, double psi, BigInteger size) {
        new EventBlockDouble(origin, BlockType.Pyramid, time, k, psi, (double)size*ONE)
    }

    List<Point4d> getEventPoints() {
        events.collect {
            transformToGlobalCoordinates(it.point)
        }
    }

    public Point4d transformToGlobalCoordinates(Point3d p) {
        o.origin + (o.blockPlane.traInv * new Point4d(p.x, p.y, p.z, 0d))
    }

    List<Triangle4d> getEventTriangles() {
        triangles.collect {
            new Triangle4d(
                    transformToGlobalCoordinates(it.p[0]),
                    transformToGlobalCoordinates(it.p[1]),
                    transformToGlobalCoordinates(it.p[2]))
        }
    }

    List<Line4d>[] getEventMoments(double mult) {
        List<Line4d>[] result = new List<Line4d>[4]
        for (int i = 0; i < events.length; i++) {
            EventFlat evt = events[i]
            result[i] = TransformerUtils.getLinesForMoment(evt.point, evt.moment, mult, this)
        }
        result
    }

    List<Line4d>[] getWaitingEvents(BigInteger currentTime) {
        List<Line4d>[] result = new List<Line4d>[4]
        for (int i = 0; i < events.length; i++) {
            EventFlat evt = events[i];
            result[i] = evt.getAllWaitingEvents(currentTime)
        }
        result
    }

    EventBlockDouble getImmediateNextBlock() {
        // Find the biggest triangle radius of the circumscribe circle
        double biggestR = triangles.max { it.r }.r
        def pts = triangles.collect {
            double planeDist = Math.sqrt(biggestR*biggestR - it.r*it.r)
            it.center + it.v12v13cross.normalized() * planeDist
        }
        def res = new EventBlockDouble(this,(BigInteger)(o.createdTime+biggestR))
        res.setEvents(
            [
                    new EventFlat(pts[0],pts[1],o,events[0].sign),
                    new EventFlat(pts[1],pts[2],o,events[1].sign),
                    new EventFlat(pts[2],pts[3],o,events[2].sign),
                    new EventFlat(pts[3],pts[0],o,events[3].sign)
            ]
        )
        res
    }

    def displayForElastic() {
        List<Double> dist = [
                new Vector3d(events[0].point, events[1].point).d(),
                new Vector3d(events[0].point, events[2].point).d(),
                new Vector3d(events[0].point, events[3].point).d(),
                new Vector3d(events[1].point, events[2].point).d(),
                new Vector3d(events[1].point, events[3].point).d(),
                new Vector3d(events[2].point, events[3].point).d()
        ]
        println "$s,${dist.join(",")}"
    }

    EventBlockDouble elasticConstraint(EventBlockDouble original) {
        Vector3d[] vects = [
                new Vector3d(events[0].point, events[1].point),
                new Vector3d(events[0].point, events[2].point),
                new Vector3d(events[0].point, events[3].point),
                new Vector3d(events[1].point, events[2].point),
                new Vector3d(events[1].point, events[3].point),
                new Vector3d(events[2].point, events[3].point)
        ]
        List<Double> ds = vects.collect { it.d() }
        def sumDist = ds.sum(0d)
        def delta = (original.s - s)/(4d*Math.sqrt(3)*sumDist)
        int vectorChanged = -1
        if (delta > MathUtilsDbl.EPSILON) {
            // Find smallest vector
            def value = 0
            for (int i = 0; i < ds.size(); i++) {
                if (vectorChanged == -1 || ds[i] < value) {
                    vectorChanged = i
                    value = ds[i]
                }
            }
        } else if (delta < -MathUtilsDbl.EPSILON) {
            // Find biggest vector
            def value = 0
            for (int i = 0; i < ds.size(); i++) {
                if (vectorChanged == -1 || ds[i] > value) {
                    vectorChanged = i
                    value = ds[i]
                }
            }
        } else {
            return null
        }

        Point3d[] pts = events.collect { it.point }
        def deltaV = vects[vectorChanged].normalized() * delta
        if (vectorChanged < 3) {
            pts[0] = pts[0] - deltaV
            pts[vectorChanged+1] = pts[vectorChanged+1] + deltaV
        } else if (vectorChanged < 5) {
            pts[1] = pts[1] - deltaV
            pts[vectorChanged-1] = pts[vectorChanged-1] + deltaV
        } else {
            pts[2] = pts[2] - deltaV
            pts[3] = pts[3] + deltaV
        }
        def res = new EventBlockDouble(this, o.createdTime+1G)
        res.setEvents(
                [
                        new EventFlat(pts[0],pts[1],o,events[0].sign),
                        new EventFlat(pts[1],pts[2],o,events[1].sign),
                        new EventFlat(pts[2],pts[3],o,events[2].sign),
                        new EventFlat(pts[3],pts[0],o,events[3].sign)
                ]
        )
        res
    }

    List<Point4d> getNextEventPoints() {
        getImmediateNextBlock().getEventPoints()
    }

    @Override
    String toString() {
        "ebf($o, [${events.join(", ")}] )"
    }
}



