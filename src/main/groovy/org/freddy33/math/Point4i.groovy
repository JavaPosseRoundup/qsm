package org.freddy33.math;

class Point4i {
    BigInteger x, y, z, t;

    public Point4i(BigInteger xi, BigInteger yi, BigInteger zi) {
        this(xi, yi, zi, 0G)
    }

    public Point4i(BigInteger xi, BigInteger yi, BigInteger zi, BigInteger ti) {
        x = xi
        y = yi
        z = zi
        t = ti
    }

    public Point4i clone() {
        return new Point4i(x, y, z, t);
    }

    public Point4i plus(Vector3i v) {
        return new Point4i(x + v.x, y + v.y, z + v.z, t)
    }

    public Point4i plus(SphericalVector3i p) {
        plus(p.toCartesian())
    }

    public Point4i minus(Vector3i v) {
        new Point4i(x - v.x, y - v.y, z - v.z, t)
    }

    public Point4i multiply(BigInteger m) {
        new Point4i(x * m, y * m, z * m, t)
    }

    public BigInteger magSquared(Point4i c) {
        (x - c.x) ** 2G + (y - c.y) ** 2G + (z - c.z) ** 2G + (t - c.t) ** 2G
    }

    String toString() {
        "p($x, $y, $z, $t)"
    }

    @Override
    boolean equals(Object obj) {
        def p = (Point4i) obj
        x == p.x && y == p.y && z == p.z && t == p.t
    }
}
