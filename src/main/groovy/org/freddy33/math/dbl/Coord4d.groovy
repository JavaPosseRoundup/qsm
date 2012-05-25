package org.freddy33.math.dbl

import org.freddy33.math.MathUtils;

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

    public Coord4d plus(Vector3d v) {
        return new Coord4d(x + v.x, y + v.y, z + v.z, t)
    }

    public Coord4d minus(Vector3d v) {
        new Coord4d(x - v.x, y - v.y, z - v.z, t)
    }

    public Coord4d multiply(double m) {
        new Coord4d(x * m, y * m, z * m, t)
    }

    public double magSquared(Coord4d c) {
        Math.pow(x - c.x, 2d) * Math.pow(y - c.y, 2d) * Math.pow(z - c.z, 2d) * Math.pow(t - c.t, 2d)
    }

    String toString() {
        "p($x, $y, $z, $t)"
    }

    @Override
    boolean equals(Object obj) {
        MathUtils.eq(this, (Coord4d) obj)
    }
}
