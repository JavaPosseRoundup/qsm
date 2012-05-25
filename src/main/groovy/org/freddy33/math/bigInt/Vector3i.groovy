package org.freddy33.math.bigInt

class Vector3i {
    final BigInteger x, y, z

    public Vector3i(BigInteger px, py, pz) {
        x = px
        y = py
        z = pz
    }

    public Vector3i(Point4i p1, Point4i p2) {
        if (p1.t != p2.t) throw new IllegalArgumentException("Cannot vector in 4D")
        x = p2.x - p1.x;
        y = p2.y - p1.y;
        z = p2.z - p1.z;
    }

    public BigInteger mod(Vector3i v) {
        dot(v)
    }

    public BigInteger dot(Vector3i v) {
        x * v.x + y * v.y + z * v.z
    }

    public Vector3i xor(Vector3i v) {
        cross(v)
    }

    public Vector3i cross(Vector3i v) {
        new Vector3i(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        )
    }

    public BigInteger magSquared() {
        (x * x) + (y * y) + (z * z)
    }

    public Vector3i negative() {
        return new Vector3i(-x, -y, -z);
    }

    Vector3i multiply(BigInteger d) {
        new Vector3i(x * d, y * d, z * d)
    }

    Vector3i multiply(double d) {
        new Vector3i((BigInteger) x * d, (BigInteger) y * d, (BigInteger) z * d)
    }

    Vector3i plus(Vector3i v) {
        new Vector3i(x + v.x, y + v.y, z + v.z)
    }

    Vector3i minus(Vector3i v) {
        new Vector3i(x - v.x, y - v.y, z - v.z)
    }

    @Override
    boolean equals(Object obj) {
        Vector3i v = (Vector3i) obj
        x == v.x && y == v.y && z == v.z
    }

    String toString() {
        "v($x, $y, $z)"
    }
}
