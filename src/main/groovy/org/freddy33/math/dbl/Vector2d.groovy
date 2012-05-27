package org.freddy33.math.dbl

class Vector2d {
    final double x, y

    public Vector2d(double px, py) {
        x = px
        y = py
    }

    public Vector2d(Point2d p1, Point2d p2) {
        x = p2.x - p1.x;
        y = p2.y - p1.y;
    }

    public double mod(Vector2d v) {
        dot(v)
    }

    public double dot(Vector2d v) {
        x * v.x + y * v.y
    }

    /**
     * Compute the d of this vector.
     * @return the d
     */
    public double d() {
        Math.sqrt(magSquared())
    }

    public double magSquared() {
        (x * x) + (y * y)
    }

    public Vector2d negative() {
        return new Vector2d(-x, -y);
    }

    Vector2d multiply(double d) {
        new Vector2d(x * d, y * d)
    }

    Vector2d normalized() {
        double d = d()
        new Vector2d(x / d, y / d)
    }

    Vector2d plus(Vector2d v) {
        new Vector2d(x + v.x, y + v.y)
    }

    Vector2d minus(Vector2d v) {
        new Vector2d(x - v.x, y - v.y)
    }

    @Override
    boolean equals(Object obj) {
        def o = (Vector2d) obj
        MathUtilsDbl.eq(this.x, o.x) && MathUtilsDbl.eq(this.y, o.y)
    }

    String toString() {
        "v($x, $y)"
    }
}
