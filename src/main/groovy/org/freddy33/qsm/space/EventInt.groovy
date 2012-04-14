package org.freddy33.qsm.space

import org.freddy33.math.Point4i
import org.freddy33.math.SphericalVector3i
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
    final SphericalVector3i dir
    final Sign sign
    boolean used = false

    EventInt(Point4i p, SphericalVector3i v) {
        this(p, v, null)
    }

    EventInt(Point4i p, SphericalVector3i v, EventBlockInt from) {
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
    final EventBlockInt from
    final EventInt e1, e2, e3
    final Vector3i v12, v13, v23, v12v23cross
    final SphericalVector3i fDir
    final BigInteger p12p13cross22

    EventTriangleInt(EventInt e1, EventInt e2, EventInt e3, EventBlockInt from) {
        this.e1 = e1
        this.e2 = e2
        this.e3 = e3
        this.from = from
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
        def fd = new SphericalVector3i(v12v23cross).normalized()
        if ((fd % from.blockDir) < 0G) {
            fDir = fd.negative()
        } else {
            fDir = fd
        }
    }

    boolean isFlat() {
        p12p13cross22 == 0G
    }

    Point4i findEvent() {
        // OK, it's a non flat triangle and dt is big enough => find center
        Point4i center = findCenter()
        center.t += from.deltaTime
        // Then find how much above plane on center need to add
        BigInteger radius2 = radius2()
        if (from.maxMagSquared - radius2 < 1G) {
            // Still too flat
            return null
        } else {
            BigInteger abovePlane = Math.sqrt((double) (from.maxMagSquared - radius2))
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
        "tr($e1, $e2, $e3, ${fDir?.toCartesian()})"
    }
}

class EventBlockInt {
    final List<EventInt> es
    final Set<EventTriangleInt> triangles = []
    final SphericalVector3i blockDir
    final BigInteger maxMagSquared
    final BigInteger deltaTime

    static EventBlockInt createValidBlock(List<EventInt> es, BigInteger timePassedSquared) {
        if (es.size() != 4) {
            println "Managing only block of 4!"
            return null
        }
        // Same plane (in 4D), for now same time plane and then same 3D plane
        def currentTime = es[0].point.t
        if (es.any { it.point.t != currentTime }) {
            // Not same time
            return null
        }
        EventBlockInt result = new EventBlockInt(es)
        if (result.maxMagSquared <= timePassedSquared) {
            // TODO: Check same 3D plane
            // Check the radius of all triangle is below the max squared
            if (result.triangles.any { it.radius2() > timePassedSquared }) {
                println "Block $this has a triangle to flat for current time"
                return null
            }
            println "Found valid block $result"
            return result
        } else {
            return null
        }
    }

    EventBlockInt(List<EventInt> es) {
        if (es.size() < 4) {
            throw new IllegalArgumentException("Cannot make block with less than 4 events")
        }
        this.es = es
        // Global dir of the block is the barycenter of directions vectors
        this.blockDir = SphericalVector3i.middleMan(es.collect { it.dir })
        this.maxMagSquared = maxMagSquared()
        this.deltaTime = (BigInteger) Math.sqrt((double) this.maxMagSquared)
        fillTriangles()
    }

    private BigInteger maxMagSquared() {
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

    private def fillTriangles() {
        // For 4 it's all possible triangles
        // TODO: For 5 there is 12 solutions that create 5 triangles covering it all
        if (es.size() == 4) {
            // Writing the loop was longer ?!?
            triangles.add(new EventTriangleInt(es[0], es[1], es[2], this))
            triangles.add(new EventTriangleInt(es[0], es[1], es[3], this))
            triangles.add(new EventTriangleInt(es[0], es[2], es[3], this))
            triangles.add(new EventTriangleInt(es[1], es[2], es[3], this))
        } else {
            throw new NotImplementedException()
        }
        triangles
    }

    def markUsed() {
        es.each { it.used = true }
    }

    List<EventInt> findNewEvents() {
        List<EventInt> newEvents = []
        triangles.each { EventTriangleInt tr ->
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

    @Override
    public String toString() {
        return "EventBlockInt dT=$deltaTime, dir=$blockDir\n${es.join("\n")}\n${triangles.join("\n")}"
    }
}



