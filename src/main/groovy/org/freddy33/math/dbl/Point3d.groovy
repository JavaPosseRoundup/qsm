package org.freddy33.math.dbl

class Point3d {
    public static ORIGIN = new Point3d(0d,0d,0d)

    public final double x, y, z

    public Point3d(double xi, double yi, double zi) {
        x = xi
        y = yi
        z = zi
    }

    public Point3d clone() {
        return new Point3d(x, y, z);
    }

    public Point3d plus(Vector3d v) {
        return new Point3d(x + v.x, y + v.y, z + v.z)
    }

    public Point3d plus(SphericalUnitVector2d sv) {
        return plus(sv.toCartesian())
    }

    public Point3d minus(Vector3d v) {
        new Point3d(x - v.x, y - v.y, z - v.z)
    }

    public Point3d minus(SphericalUnitVector2d sv) {
        return minus(sv.toCartesian())
    }

    public Point3d multiply(double m) {
        new Point3d(x * m, y * m, z * m)
    }

    public Point3d negative() {
        new Point3d(-x, -y, -z)
    }

    public double magSquared(Point3d c) {
        Math.pow(x - c.x, 2d) * Math.pow(y - c.y, 2d) * Math.pow(z - c.z, 2d)
    }

    String toString() {
        "p($x, $y, $z)"
    }

    @Override
    boolean equals(Object obj) {
        MathUtilsDbl.eq(this, (Point3d) obj)
    }
}
