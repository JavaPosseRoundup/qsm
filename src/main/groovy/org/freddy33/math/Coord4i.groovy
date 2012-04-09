package org.freddy33.math;

class Coord4i {
    BigInteger x, y, z, t;

    public Coord4i(BigInteger xi, BigInteger yi, BigInteger zi) {
        this(xi, yi, zi, 0G)
    }

    public Coord4i(BigInteger xi, BigInteger yi, BigInteger zi, BigInteger ti) {
        x = xi
        y = yi
        z = zi
        t = ti
    }

    public Coord4i clone() {
        return new Coord4i(x, y, z, t);
    }

    public Coord4i plus(Vector3i v) {
        return new Coord4i(x + v.x, y + v.y, z + v.z, t + v.t)
    }

    public Coord4i minus(Vector3i v) {
        new Coord4i(x - v.x, y - v.y, z - v.z, t)
    }

    public Coord4i multiply(BigInteger m) {
        new Coord4i(x * m, y * m, z * m, t)
    }

    public BigInteger magSquared(Coord4i c) {
        new Vector3i(this, c).magSquared()
    }

    String toString() {
        "p($x, $y, $z, $t)"
    }

    @Override
    boolean equals(Object obj) {
        MathUtils.eq(this, (Coord4i) obj)
    }
}
