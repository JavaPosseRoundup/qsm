package org.freddy33.math.bigInt

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 9:12 PM
 * To change this template use File | Settings | File Templates.
 */
class Vector2i {
    final BigInteger x, y

    public Vector2i(BigInteger px, py) {
        x = px
        y = py
    }

    public Vector2i(Point2i p1, Point2i p2) {
        x = p2.x - p1.x;
        y = p2.y - p1.y;
    }

    public BigInteger mod(Vector2i v) {
        dot(v)
    }

    public BigInteger dot(Vector2i v) {
        x * v.x + y * v.y
    }

    public BigInteger magSquared() {
        (x * x) + (y * y)
    }

    public Vector2i negative() {
        return new Vector2i(-x, -y);
    }

    public Vector2i multiply(BigInteger d) {
        new Vector2i(x * d, y * d)
    }

    Vector2i multiply(double d) {
        new Vector2i((BigInteger) x * d, (BigInteger) y * d)
    }

    Vector2i plus(Vector2i v) {
        new Vector2i(x + v.x, y + v.y)
    }

    Vector2i minus(Vector2i v) {
        new Vector2i(x - v.x, y - v.y)
    }

    @Override
    boolean equals(Object obj) {
        Vector2i v = (Vector2i) obj
        x == v.x && y == v.y
    }

    String toString() {
        "v($x, $y)"
    }
}
