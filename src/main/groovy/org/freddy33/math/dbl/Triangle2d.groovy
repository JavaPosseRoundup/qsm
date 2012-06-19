package org.freddy33.math.dbl

/**
 * @author  freds
 * Date: 5/25/12
 * Time: 1:43 PM
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
    final Point3d center
    final double s
    final double r
    final Vector3d v12v13cross

    Triangle3d(Point3d... p) {
        this.p = p
        // Surface calculated using Heron's formula 16*T^2= ( a^2 + b^2 + c^2 )^2 - 2( a^4 + b^4 + c^4 )
        def v12 = new Vector3d(p[0], p[1])
        def v13 = new Vector3d(p[0], p[2])
        def v23 = new Vector3d(p[1], p[2])
        this.v12v13cross = v12.cross(v13)

        double v12v13cross22 = v12v13cross.magSquared() * 2d
        if (v12v13cross22 < MathUtilsDbl.EPSILON) {
            throw new IllegalArgumentException("Triangle $p is too flat!")
        }
        double d12 = v12.magSquared()
        double d13 = v13.magSquared()
        double d23 = v23.magSquared()
        double alpha = d23 * v12.dot(v13)
        double beta  = d13 * (-v12).dot(v23)
        double gamma = d12 * (-v13).dot(-v23)
        center = new Point3d(
                ( alpha * p[0].x + beta * p[1].x + gamma * p[2].x ) / v12v13cross22,
                ( alpha * p[0].y + beta * p[1].y + gamma * p[2].y ) / v12v13cross22,
                ( alpha * p[0].z + beta * p[1].z + gamma * p[2].z ) / v12v13cross22
        )
        s = Math.sqrt ( ( d12+d23+d13 ) * ( d12+d23+d13 ) - ( 2d * ( d12*d12 + d23*d23 +d13*d13 ) ) ) / 4d
        r = Math.sqrt( (d12 * d23 * d13) / (v12v13cross22 * 2d) )
    }

    @Override
    public String toString() {
        "Triangle3d{$p, $s, $r, $center}"
    }
}

class Triangle4d {
    public final Point4d[] p = new Point4d[3]

    Triangle4d(Point4d... p) {
        this.p = p
    }
}
