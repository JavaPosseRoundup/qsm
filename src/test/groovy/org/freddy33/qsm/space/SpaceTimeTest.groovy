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
class SpaceTimeTest extends Specification {
    static def ratio = 1000d
    static double bigDist = ratio * sin120 * 2d
    static int nextInt = (int) bigDist
    static double nextX = Math.sqrt((nextInt + 1) * (nextInt + 1) - (ratio * ratio))

    def "test calculator"() {
        def st = new SpaceTime((int) ratio)

        expect:
        st.activeEvents.size() == 4
        st.calc()
        st.currentTime == 1
        st.activeEvents.size() == 4
        st.deadEvents.size() == 0
        for (int i = 2; i <= nextInt; i++) {
            st.calc()
            st.currentTime == i
            st.activeEvents.size() == 4
            st.deadEvents.size() == 0
            st.currentPoints().size() == 4
        }
        st.calc()
        st.currentTime == nextInt + 1
        st.activeEvents.size() == 4
        st.deadEvents.size() == 4
        def points = st.currentPoints()
        points.size() == 4
        points.any { MathUtils.eq(it, new Coord4d(nextX, 0d, -ratio, st.currentTime)) }
        st.calc()
        st.currentTime == nextInt + 2
        st.activeEvents.size() == 4
        st.deadEvents.size() == 4

        for (int i = 3; i <= nextInt + 1; i++) {
            st.calc()
            st.currentTime == i + nextInt
            st.deadEvents.size() == 4
            st.activeEvents.size() == 4
            st.currentPoints().size() == 4
        }
        st.calc()
        st.currentTime == nextInt + nextInt + 2
        st.activeEvents.size() == 4
        st.deadEvents.size() == 8
        st.calc()
        st.currentTime == nextInt + nextInt + 3
        st.activeEvents.size() == 4
        st.deadEvents.size() == 8
        def points2 = st.currentPoints()
        points2.size() == 4
        points2.any { MathUtils.eq(it, new Coord4d(nextX + nextX, 0d, ratio)) }
    }
}
