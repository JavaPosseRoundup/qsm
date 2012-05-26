package org.freddy33.math.dbl

class Point4d {
    double x, y, z, t;

    public Point4d(double xi, double yi, double zi) {
        this(xi, yi, zi, 0d)
    }

    public Point4d(double xi, double yi, double zi, double ti) {
        x = xi
        y = yi
        z = zi
        t = ti
    }

    public Point4d clone() {
        return new Point4d(x, y, z, t);
    }

    public Point4d plus(Vector3d v) {
        return new Point4d(x + v.x, y + v.y, z + v.z, t)
    }

    public Point4d minus(Vector3d v) {
        new Point4d(x - v.x, y - v.y, z - v.z, t)
    }

    public Point4d multiply(double m) {
        new Point4d(x * m, y * m, z * m, t)
    }

    public double magSquared(Point4d c) {
        Math.pow(x - c.x, 2d) * Math.pow(y - c.y, 2d) * Math.pow(z - c.z, 2d) * Math.pow(t - c.t, 2d)
    }

    String toString() {
        "p($x, $y, $z, $t)"
    }

    @Override
    boolean equals(Object obj) {
        MathUtilsDbl.eq(this, (Point4d) obj)
    }
}
