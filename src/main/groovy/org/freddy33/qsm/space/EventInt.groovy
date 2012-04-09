package org.freddy33.qsm.space

import org.freddy33.math.MathUtils
import org.freddy33.math.Point4i
import org.freddy33.math.PolarVector3i
import org.freddy33.math.Vector3i

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 4/7/12
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class EventInt {
    final Point4i point
    final PolarVector3i dir
    final Sign sign
    boolean used = false

    EventInt(Point4i p, PolarVector3i v) {
        point = p
        dir = v
    }
}

class EventTriangleInt {
    final EventInt e1, e2, e3
    final PolarVector3i blockDir
    Vector3i v12, v13, v23, v12v23cross
    PolarVector3i finalDir
    BigInteger p12p13cross22

    EventTriangleInt(EventInt e1, EventInt e2, EventInt e3) {
        this.e1 = e1
        this.e2 = e2
        this.e3 = e3
        if (e1.point.t != e2.point.t || e1.point.t != e3.point.t) {
            throw new IllegalArgumentException("All 3 events of a triangle $this should be synchronous!")
        }
        // Global dir of the block is the barycenter of directions vectors
        blockDir = PolarVector3i.middleMan(e1.dir, e2.dir, e3.dir)
    }

    Vector3i v12() {
        v12 != null ? v12 : (v12 = new Vector3i(e1.point, e2.point))
    }

    Vector3i v13() {
        v13 != null ? v13 : (v13 = new Vector3i(e1.point, e3.point))
    }

    Vector3i v23() {
        v23 != null ? v23 : (v23 = new Vector3i(e2.point, e3.point))
    }

    Vector3i v12v23cross() {
        v12v23cross != null ? v12v23cross : (v12v23cross = v12().cross(v23()))
    }

    BigInteger v12v23s2() {
        p12p13cross22 != null ? p12p13cross22 : (p12p13cross22 = v12v23cross().magSquared() * 2G)
    }

    boolean isFlat() {
        v12v23s2() == 0G
    }

    Point4i findEvent() {
        if (isFlat()) {
            return null
        }
        BigInteger dt2 = MathUtils.max(v12().magSquared(), v13().magSquared(), v23().magSquared())
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
            return center + (finalDir() * abovePlane)
        }
    }

    PolarVector3i finalDir() {
        if (finalDir != null) { return finalDir; }
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $p12p13cross22 is too small!
                                                  Cannot find final direction of a flat triangle!""")
        }
        // Final direction (perpendicular to triangle plane) is cross vector
        finalDir = new PolarVector3i(v12v23cross()).normalized()
        // TODO: Easier to check more than 180 on spherical coord
        if ((finalDir % blockDir) < 0G) {
            finalDir = -finalDir
        }
        return finalDir
    }

    Point4i findCenter() {
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $p12p13cross22 is too small!
                                                   Cannot find center of a flat triangle!""")
        }
        // From "Barycentric coordinates from cross- and dot-products"
        BigInteger alpha = v23().magSquared() * (v12() % v13())
        BigInteger beta = v13().magSquared() * (-v12() % v23())
        BigInteger gama = v12().magSquared() * (-v13() % -v23())
        new Point4i(
                (BigInteger) ((alpha * e1.point.x + beta * e2.point.x + gama * e3.point.x) / v12v23s2()),
                (BigInteger) ((alpha * e1.point.y + beta * e2.point.y + gama * e3.point.y) / v12v23s2()),
                (BigInteger) ((alpha * e1.point.z + beta * e2.point.z + gama * e3.point.z) / v12v23s2()),
                e1.point.t
        )
    }

    BigInteger radius2() {
        (BigInteger) ((v12().magSquared() * v13().magSquared() * v23().magSquared()) / (2G * v12v23s2()))
    }

    @Override
    String toString() {
        "tr($e1, $e2, $e3)"
    }
}

class EventBlockInt {
    List<EventInt> es
}



