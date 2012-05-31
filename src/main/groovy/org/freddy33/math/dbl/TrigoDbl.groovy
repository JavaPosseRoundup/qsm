package org.freddy33.math.dbl

import static org.freddy33.math.bigInt.TrigoInt.CONVERTER
import static org.freddy33.math.bigInt.TrigoInt.D90
import static org.freddy33.math.bigInt.TrigoInt.D180
import static org.freddy33.math.bigInt.TrigoInt.DIV

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/31/12
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
class TrigoDbl {
    public static final Map<BigInteger, Double> trigDblMap = [:]

    static {
        for (i in 0G..D90) {
            trigDblMap.put(i, Math.sin(i / CONVERTER))
        }
        println "Trig Dbk map found for trigSize=${trigDblMap.size()}"
    }

    public static double sin(BigInteger angle) {
        if (angle >= 0G && angle <= D90) {
            return trigDblMap[angle]
        } else if (angle < 0G && angle >= -D90) {
            return -trigDblMap[-angle]
        } else if (angle > D90 && angle <= D180) {
            return trigDblMap[D180 - angle]
        } else if (angle < -D90 && angle >= -D180) {
            return -trigDblMap[D180 + angle]
        } else if (angle < -D180) {
            return sin(angle + DIV)
        } else if (angle > D180) {
            return sin(angle - DIV)
        } else {
            throw new RuntimeException("Don't know how to count!")
        }
    }

    public static double cos(BigInteger angle) {
        sin(angle + D90)
    }
}
