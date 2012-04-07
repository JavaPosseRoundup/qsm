package org.freddy33.qsm.space

import org.freddy33.math.Coord4d
import org.freddy33.math.MathUtils
import spock.lang.Specification

import static org.freddy33.math.MathUtils.getSin120

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 4/7/12
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
class CalculatorTest extends Specification {
    static def ratio = 1000d
    static double bigDist = ratio * sin120 * 2d
    static int nextInt = (int) bigDist
    static double nextX = Math.sqrt((nextInt + 1) * (nextInt + 1) - (ratio * ratio))

    def "test calculator"() {
        def st = new SpaceTime((int) ratio)
        Calculator calculator = new Calculator(st)

        expect:
        st.spaces[0].events.size() == 4
        calculator.calc()
        st.currentTime == 1
        st.allEvents.size() == 4
        st.spaces[0].events.size() == 4
        for (int i = 2; i <= nextInt; i++) {
            calculator.calc()
            st.currentTime == i
            st.allEvents.size() == 4
            st.spaces[0].events.size() == 4
            st.spaces[i - 1] == null
            st.currentPoints().size() == 4
        }
        calculator.calc()
        st.currentTime == nextInt + 1
        st.spaces[st.currentTime].events.size() == 4
        st.allEvents.size() == 4
        st.spaces[0] == null
        calculator.calc()
        st.currentTime == nextInt + 2
        st.spaces[nextInt + 1].events.size() == 4
        st.spaces[0] == null
        st.allEvents.size() == 8
        def points = st.currentPoints()
        points.size() == 4
        points.any { MathUtils.eq(it, new Coord4d(nextX, 0d, -ratio)) }

        for (int i = 3; i <= nextInt + 1; i++) {
            calculator.calc()
            st.currentTime == i + nextInt
            st.allEvents.size() == 8
            st.spaces[0] == null
            st.spaces[nextInt + 1].events.size() == 4
            st.currentPoints().size() == 4
        }
        calculator.calc()
        st.currentTime == nextInt + nextInt + 2
        st.spaces[st.currentTime].events.size() == 4
        st.allEvents.size() == 8
        st.spaces[nextInt + 1] == null
        calculator.calc()
        st.currentTime == nextInt + nextInt + 3
        st.spaces[nextInt + nextInt + 2].events.size() == 4
        st.spaces[nextInt + 1] == null
        st.allEvents.size() == 12
        def points2 = st.currentPoints()
        points2.size() == 4
        points2.any { MathUtils.eq(it, new Coord4d(nextX + nextX, 0d, ratio)) }
    }
}
