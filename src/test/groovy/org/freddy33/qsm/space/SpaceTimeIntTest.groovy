package org.freddy33.qsm.space

import org.freddy33.math.MathUtils
import org.freddy33.math.Point4i
import spock.lang.Specification

import static org.freddy33.math.SphericalVector3i.DIV
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

    private def findNextGoodCalc(SpaceTimeInt st, BigInteger around) {
        CalcResult res = st.calc()
        if (res == CalcResult.dullUniverse) {
            return false
        }
        while (res != CalcResult.createdEvents) {
            res = st.calc()
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
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, 0G, ratio * DIV, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, ratio * sin120, ratio * cos120, 0G)) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(0G, -ratio * sin120, ratio * cos120, 0G)) }

        findNextGoodCalc(st, bigDist)
        def firstBigDist = st.currentTime
        def firstNextX = (BigInteger) Math.sqrt((double) (firstBigDist * firstBigDist) - (ratio * ratio * DIV * DIV))
        st.activeEvents.size() == 4
        st.deadEvents.size() == 4
        println "firstNextX=$firstNextX, firstBigDist=$firstBigDist"
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(firstNextX, 0G, 0G, firstBigDist), MathUtils.EPSILON_INT * 50G) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(firstNextX, 0G, -ratio * DIV, firstBigDist), MathUtils.EPSILON_INT * 50G) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(firstNextX, ratio * sin120, -ratio * cos120, firstBigDist), MathUtils.EPSILON_INT * 50G) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(firstNextX, -ratio * sin120, -ratio * cos120, firstBigDist), MathUtils.EPSILON_INT * 50G) }

        findNextGoodCalc(st, firstBigDist + bigDist)
        def secondBigDist = st.currentTime
        def secondNextX = firstNextX + (BigInteger) Math.sqrt((double) (secondBigDist - firstBigDist) * (secondBigDist - firstBigDist) - (ratio * ratio * DIV * DIV))
        println "secondNextX=$secondNextX, secondBigDist=$secondBigDist"
        st.activeEvents.size() == 4
        st.deadEvents.size() == 8
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(secondNextX, 0G, -MathUtils.EPSILON_INT, secondBigDist), MathUtils.EPSILON_INT * 50G) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(secondNextX, 0G, ratio * DIV, secondBigDist), MathUtils.EPSILON_INT * 50G) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(secondNextX, ratio * sin120, ratio * cos120, secondBigDist), MathUtils.EPSILON_INT * 50G) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(secondNextX, -ratio * sin120, ratio * cos120, secondBigDist), MathUtils.EPSILON_INT * 50G) }

        findNextGoodCalc(st, secondBigDist + bigDist)
        def newBigDist = st.currentTime
        def newNextX = secondNextX + (BigInteger) Math.sqrt((double) (newBigDist - secondBigDist) * (newBigDist - secondBigDist) - (ratio * ratio * DIV * DIV))
        println "newNextX=$newNextX, newBigDist=$newBigDist"
        st.activeEvents.size() == 4
        st.deadEvents.size() == 12
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(newNextX, 0G, -MathUtils.EPSILON_INT, newBigDist), MathUtils.EPSILON_INT * 50G) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(newNextX, 0G, -ratio * DIV, newBigDist), MathUtils.EPSILON_INT * 50G) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(newNextX, ratio * sin120, -ratio * cos120, newBigDist), MathUtils.EPSILON_INT * 50G) }
        st.activeEvents.any { MathUtils.almostEquals(it.point, new Point4i(newNextX, -ratio * sin120, -ratio * cos120, newBigDist), MathUtils.EPSILON_INT * 50G) }
    }
}
