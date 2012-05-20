package org.freddy33.qsm.space

import org.freddy33.math.MathUtils
import org.freddy33.math.Point4i
import spock.lang.Specification

import static org.freddy33.math.SphericalVector3i.ONE
import static org.freddy33.math.SphericalVector3i.ONE_HALF
import static org.freddy33.qsm.space.TriangleIntTest.cos120
import static org.freddy33.qsm.space.TriangleIntTest.sin120

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 4/7/12
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
class SpaceTimeIntTest extends Specification {
    static BigInteger ratio = 10G
    static BigInteger bigDist = ratio * sin120 * 2G
    static BigInteger deltaX = (BigInteger) Math.sqrt((double) (bigDist * bigDist) - (ratio * ratio * ONE * ONE))
    public static final BigInteger PRECISION = 500G

    private def findNextGoodCalc(SpaceTimeInt st, BigInteger around, boolean log) {
        CalcResult res = st.calc(log)
        if (res == CalcResult.dullUniverse) {
            return false
        }
        while (res != CalcResult.createdEvents) {
            res = st.calc(log)
            if (res == CalcResult.dullUniverse) {
                return false
            }
        }
        if (!MathUtils.almostEquals(st.currentTime, around)) {
            println "Current time ${st.currentTime} not around $around"
            return false
        }
        return true
    }

    def "test single photon"() {
        def st = new SpaceTimeInt(ratio)
        st.initPhoton(ratio)

        expect:
        st.activeEvents.size() == 4
        st.deadEvents.size() == 0
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, 0G, 0G, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, 0G, ratio * ONE, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, ratio * sin120, ratio * cos120, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, -ratio * sin120, ratio * cos120, 0G)) }

        findNextGoodCalc(st, bigDist, true)
        st.activeEvents.size() == 4
        st.deadEvents.size() == 4
        println "firstNextX=$deltaX, firstBigDist=$bigDist"
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX, 0G, 0G, bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX, 0G, -ratio * ONE, bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX, ratio * sin120, -ratio * cos120, bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX, -ratio * sin120, -ratio * cos120, bigDist), PRECISION) }

        findNextGoodCalc(st, 2G * bigDist, true)
        println "secondNextX=${2G * deltaX}, secondBigDist=${2G * bigDist}"
        st.activeEvents.size() == 4
        st.deadEvents.size() == 8
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(2G * deltaX, 0G, 0G, 2G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(2G * deltaX, 0G, ratio * ONE, 2G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(2G * deltaX, ratio * sin120, ratio * cos120, 2G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(2G * deltaX, -ratio * sin120, ratio * cos120, 2G * bigDist), PRECISION) }

        findNextGoodCalc(st, 3G * bigDist, true)
        println "newNextX=${3G * deltaX}, newBigDist=${3G * bigDist}"
        st.activeEvents.size() == 4
        st.deadEvents.size() == 12
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(3G * deltaX, 0G, 0G, 3G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(3G * deltaX, 0G, -ratio * ONE, 3G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(3G * deltaX, ratio * sin120, -ratio * cos120, 3G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(3G * deltaX, -ratio * sin120, -ratio * cos120, 3G * bigDist), PRECISION) }
    }

    def "test single photon long time"() {
        def st = new SpaceTimeInt(ratio)
        st.initPhoton(ratio)

        expect:
        st.activeEvents.size() == 4
        st.deadEvents.size() == 0
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, 0G, 0G, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, 0G, ratio * ONE, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, ratio * sin120, ratio * cos120, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, -ratio * sin120, ratio * cos120, 0G)) }

        for (int i = 1; i < 1000; i++) {
            findNextGoodCalc(st, bigDist * i, false)
            st.activeEvents.size() == 4
            st.deadEvents.size() == 4 * i
            println "Neg Z i=$i"
            st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX * i, 0G, 0G, bigDist * i), PRECISION) }
            st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX * i, 0G, -ratio * ONE, bigDist * i), PRECISION) }
            st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX * i, ratio * sin120, -ratio * cos120, bigDist * i), PRECISION) }
            st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX * i, -ratio * sin120, -ratio * cos120, bigDist * i), PRECISION) }

            i++
            println "Pos Z i=$i"
            findNextGoodCalc(st, bigDist * i, false)
            st.activeEvents.size() == 4
            st.deadEvents.size() == 4 * i
            st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX * i, 0G, 0G, bigDist * i), PRECISION) }
            st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX * i, 0G, ratio * ONE, bigDist * i), PRECISION) }
            st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX * i, ratio * sin120, ratio * cos120, bigDist * i), PRECISION) }
            st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX * i, -ratio * sin120, ratio * cos120, bigDist * i), PRECISION) }
        }
    }

    def "test single electron"() {
        def st = new SpaceTimeInt(ratio)
        st.initElectron(ratio)

        expect:
        st.activeEvents.size() == 4
        st.deadEvents.size() == 0
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, 0G, ratio * ONE_HALF, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, 0G, -ratio * ONE_HALF, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, ratio * sin120, 0G, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, -ratio * sin120, 0G, 0G)) }

        findNextGoodCalc(st, bigDist, true)
        st.activeEvents.size() == 4
        st.deadEvents.size() == 4
        println "firstNextX=$deltaX, firstBigDist=$bigDist"
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX, 0G, ratio * sin120, bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX, 0G, -ratio * sin120, bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX, ratio * ONE_HALF, 0G, bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(deltaX, -ratio * ONE_HALF, 0G, bigDist), PRECISION) }

        findNextGoodCalc(st, 2G * bigDist, true)
        println "secondNextX=${2G * deltaX}, secondBigDist=${2G * bigDist}"
        st.activeEvents.size() == 4
        st.deadEvents.size() == 8
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(2G * deltaX, 0G, ratio * ONE_HALF, 2G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(2G * deltaX, 0G, -ratio * ONE_HALF, 2G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(2G * deltaX, ratio * sin120, 0G, 2G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(2G * deltaX, -ratio * sin120, 0G, 2G * bigDist), PRECISION) }

        findNextGoodCalc(st, 3G * bigDist, true)
        println "newNextX=${3G * deltaX}, newBigDist=${3G * bigDist}"
        st.activeEvents.size() == 4
        st.deadEvents.size() == 12
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(3G * deltaX, 0G, ratio * sin120, 3G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(3G * deltaX, 0G, -ratio * sin120, 3G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(3G * deltaX, ratio * ONE_HALF, 0G, 3G * bigDist), PRECISION) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(3G * deltaX, -ratio * ONE_HALF, 0G, 3G * bigDist), PRECISION) }
    }

}
