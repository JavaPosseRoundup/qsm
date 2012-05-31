package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.dbl.*

import static org.freddy33.math.bigInt.TrigoInt.ONE

public class EventBlockFlat {
    final Point4d origin
    final BlockType type
    final BigInteger createdTime
    final SphericalUnitVector2d moment
    final EulerAngles3d singlePlane
    final EventFlat[] events
    final Triangle3d[] triangles = new Triangle3d[4]
    final double s

    public EventBlockFlat(Point3d o, BlockType type, BigInteger time, SphericalUnitVector2d moment, double psi, double size) {
        this.origin = new Point4d(o.x, o.y, o.z, (double) time)
        this.type = type
        this.createdTime = time
        this.moment = moment
        this.singlePlane = new EulerAngles3d(moment, psi)
        this.events = type.createEvents(this, size)
        triangles[0] = new Triangle3d(events[0].point, events[1].point, events[2].point)
        triangles[1] = new Triangle3d(events[0].point, events[1].point, events[3].point)
        triangles[2] = new Triangle3d(events[0].point, events[2].point, events[3].point)
        triangles[3] = new Triangle3d(events[1].point, events[2].point, events[3].point)
        s = triangles.sum(0d) { it.s }
    }

    public static EventBlockFlat createPhoton(Point3d origin, BigInteger time, SphericalUnitVector2d k, double psi, BigInteger size) {
        new EventBlockFlat(origin, BlockType.FlatStar, time, k, psi, (double)size*ONE)
    }

    public static EventBlockFlat createElectron(Point3d origin, BigInteger time, SphericalUnitVector2d k, double psi, BigInteger size) {
        new EventBlockFlat(origin, BlockType.Pyramid, time, k, psi, (double)size*ONE)
    }

    List<Point4d> getEventPoints() {
        events.collect {
            origin + (singlePlane.traInv * new Point4d(it.point.x, it.point.y, it.point.z, 0d))
        }
    }

    List<Line4d>[] getWaitingEvents(BigInteger currentTime) {
        List<Line4d>[] result = new List<Line4d>[4]
        for (int i = 0; i < events.length; i++) {
            EventFlat evt = events[i];
            result[i] = evt.getAllWaitingEvents(currentTime)
        }
        result
    }

    @Override
    String toString() {
        "ebf($origin, $moment, [${events.join(", ")}] )"
    }
}



