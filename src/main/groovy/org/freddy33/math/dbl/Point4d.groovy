package org.freddy33.math.dbl

class Point4d {

    public static Point4d origin() { new Point4d(0d,0d,0d,0d) }

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

    public Point4d plus(Point4d v) {
        return new Point4d(x + v.x, y + v.y, z + v.z, t + v.t)
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

    public Point4d div(double m) {
        new Point4d(x / m, y / m, z / m, t / m)
    }

    public double magSquared(Point4d c) {
        (x - c.x)*(x - c.x) + (y - c.y)*(y - c.y) + (z - c.z)*(z - c.z) + (t - c.t)*(t - c.t)
    }

    String toString() {
        "p($x, $y, $z, $t)"
    }

    @Override
    boolean equals(Object obj) {
        MathUtilsDbl.eq(this, (Point4d) obj)
    }
}
