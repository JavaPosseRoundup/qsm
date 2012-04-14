package org.freddy33.qsm.space

import org.freddy33.math.MathUtils
import org.freddy33.math.Point4i
import org.freddy33.math.PolarVector3i
import org.freddy33.math.Vector3i
import org.jzy3d.plot3d.pipelines.NotImplementedException

/**
 * User: freds
 * Date: 4/7/12
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class EventInt {
    final EventBlockInt createdBy
    final Point4i point
    final PolarVector3i dir
    final Sign sign
    boolean used = false

    EventInt(Point4i p, PolarVector3i v) {
        this(p, v, null)
    }

    EventInt(Point4i p, PolarVector3i v, EventBlockInt from) {
        point = p
        dir = v
        createdBy = from
    }

    @Override
    public String toString() {
        return "evt($point, $dir, used=$used)";
    }
}

class EventTriangleInt {
    final EventInt e1, e2, e3
    final PolarVector3i blockDir
    final Vector3i v12, v13, v23, v12v23cross
    final PolarVector3i fDir
    final BigInteger p12p13cross22

    EventTriangleInt(EventInt e1, EventInt e2, EventInt e3, PolarVector3i b) {
        this.e1 = e1
        this.e2 = e2
        this.e3 = e3
        if (e1.point.t != e2.point.t || e1.point.t != e3.point.t) {
            throw new IllegalArgumentException("All 3 events of a triangle $this should be synchronous!")
        }
        blockDir = b.normalized()
        v12 = new Vector3i(e1.point, e2.point)
        v13 = new Vector3i(e1.point, e3.point)
        v23 = new Vector3i(e2.point, e3.point)
        v12v23cross = v12.cross(v23)
        p12p13cross22 = v12v23cross.magSquared() * 2G
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $p12p13cross22 is too small!
                                                  Cannot find final direction of a flat triangle!""")
        }
        // Final direction (perpendicular to triangle plane) is cross vector
        def fd = new PolarVector3i(v12v23cross).normalized()
        if ((fd % blockDir) < 0G) {
            fDir = fd.negative()
        } else {
            fDir = fd
        }
        println "Created triangle $this"
    }

    boolean isFlat() {
        p12p13cross22 == 0G
    }

    Point4i findEvent() {
        BigInteger dt2 = MathUtils.max(v12.magSquared(), v13.magSquared(), v23.magSquared())
        BigInteger dt = Math.sqrt((double) dt2)
        // OK, it's a non flat triangle and dt is big enough => find center
        Point4i center = findCenter()
        center.t += dt
        // Then find how much above plane on center need to add
        BigInteger radius2 = radius2()
        if (dt2 - radius2 < 1G) {
            // Still too flat
            return null
        } else {
            BigInteger abovePlane = Math.sqrt((double) (dt2 - radius2))
            return center + (fDir * abovePlane)
        }
    }

    Point4i findCenter() {
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $p12p13cross22 is too small!
                                                   Cannot find center of a flat triangle!""")
        }
        // From "Barycentric coordinates from cross- and dot-products"
        BigInteger alpha = v23.magSquared() * (v12 % v13)
        BigInteger beta = v13.magSquared() * (-v12 % v23)
        BigInteger gama = v12.magSquared() * (-v13 % -v23)
        new Point4i(
                (BigInteger) ((alpha * e1.point.x + beta * e2.point.x + gama * e3.point.x) / p12p13cross22),
                (BigInteger) ((alpha * e1.point.y + beta * e2.point.y + gama * e3.point.y) / p12p13cross22),
                (BigInteger) ((alpha * e1.point.z + beta * e2.point.z + gama * e3.point.z) / p12p13cross22),
                e1.point.t
        )
    }

    BigInteger radius2() {
        (BigInteger) ((v12.magSquared() * v13.magSquared() * v23.magSquared()) / (2G * p12p13cross22))
    }

    @Override
    String toString() {
        "tr($e1, $e2, $e3, ${fDir?.toCartesian()}, ${blockDir?.toCartesian()})"
    }
}

class EventBlockInt {
    final List<EventInt> es
    final PolarVector3i blockDir

    EventBlockInt(List<EventInt> es) {
        if (es.size() < 4) {
            throw new IllegalArgumentException("Cannot make block with less than 4 events")
        }
        this.es = es
        // Global dir of the block is the barycenter of directions vectors
        this.blockDir = PolarVector3i.middleMan(es.collect { it.dir })
    }

    BigInteger maxMagSquared() {
        BigInteger result = 0G
        es.eachWithIndex { EventInt one, i ->
            if (i < es.size() - 1) {
                for (j in (i + 1)..(es.size() - 1)) {
                    def m = one.point.magSquared(es[j].point)
                    if (m > result) result = m
                }
            }
        }
        result
    }

    Set<EventTriangleInt> allTriangles() {
        // For 4 it's all possible triangles
        // TODO: For 5 there is 12 solutions that create 5 triangles covering it all
        Set<EventTriangleInt> result = []
        if (es.size() == 4) {
            // Writing the loop was longer ?!?
            result.add(new EventTriangleInt(es[0], es[1], es[2], blockDir))
            result.add(new EventTriangleInt(es[0], es[1], es[3], blockDir))
            result.add(new EventTriangleInt(es[0], es[2], es[3], blockDir))
            result.add(new EventTriangleInt(es[1], es[2], es[3], blockDir))
        } else {
            throw new NotImplementedException()
        }
        result
    }

    def markUsed() {
        es.each { it.used = true }
    }

    List<EventInt> findNewEvents() {
        List<EventInt> newEvents = []
        allTriangles().each { EventTriangleInt tr ->
            Point4i newPoint = tr.findEvent()
            if (newPoint != null) newEvents.add(new EventInt(newPoint, tr.fDir, this))
        }
        if (newEvents.size() == es.size()) {
            // Conservation of events good
            markUsed()
        } else {
            // Missed
            newEvents.clear()
        }
        newEvents
    }
}



