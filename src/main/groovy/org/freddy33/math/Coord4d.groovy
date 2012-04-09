package org.freddy33.math;

class Coord4d {
    double x, y, z, t;

    public Coord4d(double xi, double yi, double zi) {
        this(xi, yi, zi, 0d)
    }

    public Coord4d(double xi, double yi, double zi, double ti) {
        x = xi
        y = yi
        z = zi
        t = ti
    }

    public Coord4d clone() {
        return new Coord4d(x, y, z, t);
    }

    public Coord4d plus(Vector4d v) {
        return new Coord4d(x + v.x, y + v.y, z + v.z, t + v.t)
    }

    public Coord4d minus(Vector4d v) {
        new Coord4d(x - v.x, y - v.y, z - v.z, t)
    }

    public Coord4d multiply(double m) {
        new Coord4d(x * m, y * m, z * m, t)
    }

    public double magSquared(Coord4d c) {
        new Vector4d(this, c).magSquared()
    }

    public double d(Coord4d c) {
        new Vector4d(this, c).d()
    }

    String toString() {
        "p($x, $y, $z, $t)"
    }

    @Override
    boolean equals(Object obj) {
        MathUtils.eq(this, (Coord4d) obj)
    }
}
