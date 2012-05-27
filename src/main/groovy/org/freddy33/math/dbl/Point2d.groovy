package org.freddy33.math.dbl

class Point2d {
    double x, y;

    public Point2d(double xi, double yi) {
        x = xi
        y = yi
    }

    public Point2d clone() {
        return new Point2d(x, y);
    }

    public Point2d plus(Point2d v) {
        return new Point2d(x + v.x, y + v.y)
    }

    public Point2d minus(Point2d v) {
        new Point2d(x - v.x, y - v.y)
    }

    public Point2d multiply(double m) {
        new Point2d(x * m, y * m)
    }

    public double magSquared(Point2d c) {
        Math.pow(x - c.x, 2d) * Math.pow(y - c.y, 2d)
    }

    String toString() {
        "p($x, $y)"
    }

    @Override
    boolean equals(Object obj) {
        def o = (Point2d) obj
        MathUtilsDbl.eq(this.x, o.x)  && MathUtilsDbl.eq(this.y, o.y)
    }
}
