package org.freddy33.math

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 4/6/12
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
class Triangle {
    List<Coord4d> p
    Vector4d v12, v13, v23
    Vector4d v12v23cross
    Float p12p13cross22

    Triangle(Coord4d p1, Coord4d p2, Coord4d p3) {
        this.p = []
        this.p << p1
        this.p << p2
        this.p << p3
    }

    Vector4d v12() {
        v12 != null ? v12 : (v12 = new Vector4d(p[0], p[1]))
    }

    Vector4d v13() {
        v13 != null ? v13 : (v13 = new Vector4d(p[0], p[2]))
    }

    Vector4d v23() {
        v23 != null ? v23 : (v23 = new Vector4d(p[1], p[2]))
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

    Coord4d findEvent(int dt, Vector4d origDirection) {
        if (isFlat()) {
            return null
        }
        double dt2 = dt * dt
        if (
                (v12().magSquared() > dt2) ||
                        (v13().magSquared() > dt2) ||
                        (v23().magSquared() > dt2)
        ) {
            // too big triangle for dt
            return null
        }
        // OK, it's a non flat triangle and dt is big enough => find center
        Coord4d center = findCenter()
        // Then find how much above plane on center need to add
        double radius2 = radius2()
        if (dt2 - radius2 < 1d) {
            throw new IllegalStateException("Don't know how to do math!")
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
                alpha * p[0].x + beta * p[1].x + gama * p[2].x,
                alpha * p[0].y + beta * p[1].y + gama * p[2].y,
                alpha * p[0].z + beta * p[1].z + gama * p[2].z
        )
    }

    @Override
    public String toString() {
        "Triangle{ p=$p, v12=$v12, v13=$v13, v23=$v23 }"
    }
}
