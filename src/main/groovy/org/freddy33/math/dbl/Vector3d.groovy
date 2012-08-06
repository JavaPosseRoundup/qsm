package org.freddy33.math.dbl

class Vector3d {
    double x, y, z

    public Vector3d(double px, py, pz) {
        x = px
        y = py
        z = pz
    }

    public Vector3d(Point3d p1, Point3d p2) {
        x = p2.x - p1.x;
        y = p2.y - p1.y;
        z = p2.z - p1.z;
    }

    public Vector3d(Point4d p1, Point4d p2) {
        x = p2.x - p1.x;
        y = p2.y - p1.y;
        z = p2.z - p1.z;
    }

    public double mod(Vector3d v) {
        dot(v)
    }

    public double dot(Vector3d v) {
        x * v.x + y * v.y + z * v.z
    }

    public Vector3d xor(Vector3d v) {
        cross(v)
    }

    public Vector3d cross(Vector3d v) {
        new Vector3d(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        )
    }

    /**
     * Compute the d of this vector.
     * @return the d
     */
    public double d() {
        Math.sqrt(magSquared())
    }

    public double magSquared() {
        (x * x) + (y * y) + (z * z)
    }

    public Vector3d negative() {
        return new Vector3d(-x, -y, -z);
    }

    Vector3d multiply(double d) {
        new Vector3d(x * d, y * d, z * d)
    }

    Vector3d div(double d) {
        new Vector3d(x / d, y / d, z / d)
    }

    Vector3d normalized() {
        div(d())
    }

    Vector3d plus(Vector3d v) {
        new Vector3d(x + v.x, y + v.y, z + v.z)
    }

    Vector3d minus(Vector3d v) {
        new Vector3d(x - v.x, y - v.y, z - v.z)
    }

    Vector3d addSelf(Vector3d v) {
        x += v.x
        y += v.y
        z += v.z
        return this
    }

    @Override
    boolean equals(Object obj) {
        MathUtilsDbl.eq(this, (Vector3d) obj)
    }

    String toString() {
        "v($x, $y, $z)"
    }

    boolean isNormalized() {
        MathUtilsDbl.eq(magSquared(), 1d)
    }
}
