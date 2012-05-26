package org.freddy33.math.bigInt

class Point2i {
    BigInteger x, y;

    public Point2i(BigInteger xi, BigInteger yi) {
        x = xi
        y = yi
    }

    public Point2i clone() {
        return new Point2i(x, y);
    }

    String toString() {
        "p($x, $y)"
    }

    @Override
    boolean equals(Object obj) {
        def p = (Point2i) obj
        x == p.x && y == p.y
    }
}
