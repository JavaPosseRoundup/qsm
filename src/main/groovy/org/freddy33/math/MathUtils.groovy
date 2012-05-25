package org.freddy33.math

import org.freddy33.math.dbl.Coord4d
import org.freddy33.math.bigInt.Point4i
import org.freddy33.math.bigInt.SphericalVector3i
import org.freddy33.math.dbl.Vector3d
import org.freddy33.math.bigInt.Vector3i

/**
 * Created by IntelliJ IDEA.
 * User: freds
 * Date: 2/10/12
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
class MathUtils {
    static double sin120 = Math.sin(2d * Math.PI / 3d)
    static double cos120 = -0.5d
    public static final double EPSILON = 1e-6d
    public static final BigInteger EPSILON_INT = 2G

    static boolean eq(double a, double b) {
        if (b == a) return true
        if (b == 0d) {
            Math.abs(a) < EPSILON
        } else if (a == 0d) {
            Math.abs(b) < EPSILON
        } else {
            Math.abs(1.0d - (a / b)) < EPSILON
        }
    }

    static boolean almostEquals(BigInteger a, BigInteger b) {
        almostEquals(a, b, EPSILON_INT)
    }

    static boolean almostEquals(BigInteger a, BigInteger b, BigInteger prec) {
        if (b == a) return true
        if (b == 0G) {
            return isSmallInt(a, prec)
        } else if (a == 0G) {
            return isSmallInt(b, prec)
        } else {
            if (isSmallInt(a - b, prec)) {
                return true
            }
            BigInteger ma = a
            BigInteger mb = b
            BigInteger d = a - b
            if (d < 0G) {
                d = -d
            }
            if (ma < 0G) {
                ma = -ma
            }
            if (mb < 0G) {
                mb = -mb
            }
            if (d >= ma || d >= mb) {
                return false
            }
            return (d / max(ma, mb)) < prec.toBigDecimal()
        }
    }

    public static boolean isSmallInt(BigInteger i, BigInteger prec) {
        if (i < 0G) {
            return -i < prec
        } else {
            return i < prec
        }
    }

    public static boolean isSmallInt(BigInteger i) {
        isSmallInt(i, EPSILON_INT)
    }

    static boolean eq(Coord4d a, Coord4d b) {
        eq(a.x, b.x) && eq(a.y, b.y) && eq(a.z, b.z) && Math.abs(a.t - b.t) < 1d
    }

    static boolean eq(Vector3d a, Vector3d b) {
        eq(a.x, b.x) && eq(a.y, b.y) && eq(a.z, b.z)
    }

    static boolean almostEquals(Vector3i a, Vector3i b) {
        almostEquals(a.x, b.x) && almostEquals(a.y, b.y) && almostEquals(a.z, b.z)
    }

    static boolean almostEquals(Point4i a, Point4i b) {
        almostEquals(a, b, EPSILON_INT)
    }

    static boolean almostEquals(Point4i a, Point4i b, BigInteger precision) {
        almostEquals(a.x, b.x, precision) &&
                almostEquals(a.y, b.y, precision) &&
                almostEquals(a.z, b.z, precision) &&
                almostEquals(a.t, b.t, precision)
    }

    static boolean almostEquals(SphericalVector3i a, SphericalVector3i b) {
        if (!almostEquals(a.r, b.r) || !isSmallInt(a.teta - b.teta)) return false
        if (isSmallInt(a.phi - b.phi)) return true
        // Check when phi +- D180
        if (isSmallInt(SphericalVector3i.D180 - a.phi) && isSmallInt(SphericalVector3i.D180 + b.phi)) {
            // a.phi is almost +D180 and b.phi almost -D180
            return isSmallInt(a.phi + b.phi)
        }
        if (isSmallInt(SphericalVector3i.D180 + a.phi) && isSmallInt(SphericalVector3i.D180 - b.phi)) {
            // a.phi is almost -D180 and b.phi almost D180
            return isSmallInt(a.phi + b.phi)
        }
        return false
    }

    static BigInteger max(List<BigInteger> vals) {
        BigInteger result = 0G
        vals.each {
            def bi = it
            if (bi < 0G) {
                bi = -bi
            }
            if (bi > result) result = bi
        }
        result
    }

    static BigInteger max(BigInteger... vals) {
        BigInteger result = 0G
        vals.each {
            def bi = it
            if (bi < 0G) {
                bi = -bi
            }
            if (bi > result) result = bi
        }
        result
    }
}
