package org.freddy33.math

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 4/6/12
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
class Triangle {
    Coord4d p1, p2, p3
    Vector4d v12, v13, v23
    Vector4d v12v23cross
    Double p12p13cross22

    Triangle(Coord4d p1, Coord4d p2, Coord4d p3) {
        this.p1 = p1
        this.p2 = p2
        this.p3 = p3
    }

    Vector4d v12() {
        v12 != null ? v12 : (v12 = new Vector4d(p1, p2))
    }

    Vector4d v13() {
        v13 != null ? v13 : (v13 = new Vector4d(p1, p3))
    }

    Vector4d v23() {
        v23 != null ? v23 : (v23 = new Vector4d(p2, p3))
    }

    Vector4d v12v23cross() {
        v12v23cross != null ? v12v23cross : (v12v23cross = v12().cross(v23()))
    }

    double v12v23s2() {
        p12p13cross22 != null ? p12p13cross22 : (p12p13cross22 = v12v23cross().magSquared() * 2d)
    }

    Vector4d finalDir(Vector4d origDirection) {
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $v12v23s2 is too small!
                                                  Cannot find final direction of a flat triangle!""")
        }
        // Final direction (perpendicular to triangle plane) is cross vector
        Vector4d finalDir = v12v23cross().normalized()
        if ((finalDir % origDirection) < 0d) {
            return -finalDir
        }
        return finalDir
    }

    Coord4d findEvent(Vector4d origDirection) {
        if (isFlat()) {
            return null
        }
        double dt2 = Math.max(Math.max(v12().magSquared(), v13().magSquared()), v23().magSquared())
        double dt = Math.sqrt(dt2)
        // OK, it's a non flat triangle and dt is big enough => find center
        Coord4d center = findCenter()
        center.t += dt
        // Then find how much above plane on center need to add
        double radius2 = radius2()
        if (dt2 - radius2 < 1d) {
            // Still too flat
            return null
        } else {
            double abovePlane = Math.sqrt(dt2 - radius2)
            return center + (finalDir(origDirection) * abovePlane)
        }
    }

    double radius2() {
        return (v12().magSquared() * v13().magSquared() * v23().magSquared() / (2d * v12v23s2()))
    }

    boolean isFlat() {
        return MathUtils.eq(v12v23s2(), 0d)
    }

    Coord4d findCenter() {
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $v12v23s2 is too small!
                                                   Cannot find center of a flat triangle!""")
        }
        // From "Barycentric coordinates from cross- and dot-products"
        double alpha = v23().magSquared() * (v12() % v13()) / v12v23s2()
        double beta = v13().magSquared() * (-v12() % v23()) / v12v23s2()
        double gama = v12().magSquared() * (-v13() % -v23()) / v12v23s2()
        new Coord4d(
                alpha * p1.x + beta * p2.x + gama * p3.x,
                alpha * p1.y + beta * p2.y + gama * p3.y,
                alpha * p1.z + beta * p2.z + gama * p3.z,
                (p1.t + p2.t + p3.t) / 3d
        )
    }

    @Override
    public String toString() {
        "Triangle{ $p1, $p2, $p3, v12=$v12, v13=$v13, v23=$v23 }"
    }
}
