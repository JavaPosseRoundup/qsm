package org.freddy33.math.bigInt

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
class Triangle2i {
    public final Point2i[] p = new Point2i[3]
    final BigInteger s16squared

    Triangle2i(Point2i... p) {
        this.p = p
        // Surface calculated using Heron's formula 16*T^2= ( a^2 + b^2 + c^2 )^2 - 2( a^4 + b^4 + c^4 )
        BigInteger a2 = new Vector2i(p[0], p[1]).magSquared()
        BigInteger b2 = new Vector2i(p[1], p[2]).magSquared()
        BigInteger c2 = new Vector2i(p[2], p[0]).magSquared()
        s16squared = ( a2+b2+c2 ) * ( a2+b2+c2 ) - ( 2G * ( a2*a2 + b2*b2 +c2*c2 ) )
    }
}
