package org.freddy33.math.bigInt

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/29/12
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
class TrigoInt {
    public static final BigInteger D15 = 2G * 3G * 5G
    public static final BigInteger D45 = D15 * 3G
    public static final BigInteger D30 = D15 * 2G
    public static final BigInteger D90 = D30 * 3G
    public static final BigInteger D180 = D90 * 2G
    public static final BigInteger D120 = D30 * 4G
    public static final BigInteger DIV = D90 * 4G
    public static final BigInteger ONE_HALF = (BigInteger) (DIV / (4d*Math.PI))
    public static final BigInteger ONE = ONE_HALF * 2G

    public static final Map<BigInteger, BigInteger> trigMap = [:]
    public static final Map<BigInteger, BigInteger> invTrigMap = [:]

    public static double CONVERTER = ((double)DIV / (2d * Math.PI))

    static {
        BigInteger lastSinVal = null
        for (i in 0G..TrigoInt.D90) {
            BigInteger sinVal = (BigInteger) (TrigoInt.ONE * Math.sin(i / CONVERTER))
            trigMap.put(i, sinVal)
            if (!invTrigMap.containsKey(sinVal)) invTrigMap.put(sinVal, i)
            if (lastSinVal != null && (sinVal - 1G) > lastSinVal) {
                // Some holes to fill
                BigInteger holeSize = sinVal - lastSinVal - 1G
                for (h in 1G..holeSize) {
                    invTrigMap.put(lastSinVal + h, i - 1G)
                }
            }
            lastSinVal = sinVal
        }
        println "Trig map found for PI=$TrigoInt.D180 ONE=$TrigoInt.ONE trigSize=${trigMap.size()} and invTrigSize=${invTrigMap.size()}"
//        println "Trig map=${trigMap}"
//        println "Invers Trig map=${invTrigMap}"
    }

    public static BigInteger atan2(BigInteger y, BigInteger x) {
        if (x == 0G) {
            if (y > 0G) {
                return TrigoInt.D90
            } else if (y < 0) {
                return -TrigoInt.D90
            } else {
                return 0G
            }
        } else {
            BigInteger arctan = asin((BigInteger) (y / Math.sqrt((double) (x * x + y * y))))
            if (x > 0G) {
                return arctan
            } else if (x < 0G) {
                if (arctan == 0G) {
                    return TrigoInt.D180
                } else if (y < 0G) {
                    return arctan - TrigoInt.D180
                } else {
                    return arctan + TrigoInt.D180
                }
            }
        }
    }

    public static BigInteger acos(BigInteger cosVal) {
        TrigoInt.D90 - asin(cosVal)
    }

    public static BigInteger asin(BigInteger sinVal) {
        if (sinVal < -TrigoInt.ONE || sinVal > TrigoInt.ONE) {
            throw new IllegalArgumentException("A sin value cannot be ${sinVal} above or below +- ${TrigoInt.ONE}")
        }
        if (sinVal < 0G) {
            return -invTrigMap.get(-sinVal)
        } else {
            return invTrigMap.get(sinVal)
        }
    }

    public static BigInteger sin(BigInteger angle) {
        if (angle >= 0G && angle <= TrigoInt.D90) {
            return trigMap[angle]
        } else if (angle < 0G && angle >= -TrigoInt.D90) {
            return -trigMap[-angle]
        } else if (angle > TrigoInt.D90 && angle <= TrigoInt.D180) {
            return trigMap[TrigoInt.D180 - angle]
        } else if (angle < -TrigoInt.D90 && angle >= -TrigoInt.D180) {
            return -trigMap[TrigoInt.D180 + angle]
        } else if (angle < -TrigoInt.D180) {
            return sin(angle + TrigoInt.DIV)
        } else if (angle > TrigoInt.D180) {
            return sin(angle - TrigoInt.DIV)
        } else {
            throw new RuntimeException("Don't know how to count!")
        }
    }

    public static BigInteger cos(BigInteger angle) {
        sin(angle + TrigoInt.D90)
    }

}
