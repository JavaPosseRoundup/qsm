package org.freddy33.math.dbl

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
class Triangle2d {
    public final Point2d[] p = new Point2d[3]
    final double s

    Triangle2d(Point2d... p) {
        this.p = p
        // Surface calculated using Heron's formula 16*T^2= ( a^2 + b^2 + c^2 )^2 - 2( a^4 + b^4 + c^4 )
        double a2 = new Vector2d(p[0], p[1]).magSquared()
        double b2 = new Vector2d(p[1], p[2]).magSquared()
        double c2 = new Vector2d(p[2], p[0]).magSquared()
        s = Math.sqrt ( ( a2+b2+c2 ) * ( a2+b2+c2 ) - ( 2G * ( a2*a2 + b2*b2 +c2*c2 ) ) ) / 4d
    }
}

class Triangle3d {
    public final Point3d[] p = new Point3d[3]
    final double s

    Triangle3d(Point3d... p) {
        this.p = p
        // Surface calculated using Heron's formula 16*T^2= ( a^2 + b^2 + c^2 )^2 - 2( a^4 + b^4 + c^4 )
        double a2 = new Vector3d(p[0], p[1]).magSquared()
        double b2 = new Vector3d(p[1], p[2]).magSquared()
        double c2 = new Vector3d(p[2], p[0]).magSquared()
        s = Math.sqrt ( ( a2+b2+c2 ) * ( a2+b2+c2 ) - ( 2G * ( a2*a2 + b2*b2 +c2*c2 ) ) ) / 4d
    }
}

class Triangle4d {
    public final Point4d[] p = new Point4d[3]

    Triangle4d(Point4d... p) {
        this.p = p
    }
}
