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
class SpaceTimeDoubleTest extends Specification {
    static def ratio = 1000d
    static double bigDist = ratio * sin120 * 2d
    static int nextInt = (int) bigDist
    static double nextX = Math.sqrt((bigDist * bigDist) - (ratio * ratio))

    def "test calculator"() {
        def st = new SpaceTimeDouble((int) ratio)

        expect:
        st.activeEvents.size() == 4
        st.calc()
        st.currentTime == 1
        st.activeEvents.size() == 4
        st.deadEvents.size() == 4
        def points = st.currentPoints()
        points.size() == 4
        points.any { MathUtils.eq(it, new Coord4d(0d, 0d, 0d, 0d)) }
        points.any { MathUtils.eq(it, new Coord4d(0d, 0d, ratio, 0d)) }
        points.any { MathUtils.eq(it, new Coord4d(0d, ratio * MathUtils.sin120, ratio * MathUtils.cos120, 0d)) }
        points.any { MathUtils.eq(it, new Coord4d(0d, -ratio * MathUtils.sin120, ratio * MathUtils.cos120, 0d)) }
        for (int i = 2; i <= nextInt; i++) {
            st.calc()
            st.currentTime == i
            st.activeEvents.size() == 4
            st.deadEvents.size() == 4
            st.currentPoints().size() == 4
        }
        st.calc()
        st.currentTime == nextInt + 1
        st.activeEvents.size() == 4
        st.deadEvents.size() == 8
        def points1 = st.currentPoints()
        points1.size() == 8
        points1.any { MathUtils.eq(it, new Coord4d(nextX, 0d, -ratio, nextInt)) }
        points1.any { MathUtils.eq(it, new Coord4d(nextX, 0d, 0d, nextInt)) }
        points1.any { MathUtils.eq(it, new Coord4d(nextX, 0d, -ratio, nextInt)) }
        points1.any { MathUtils.eq(it, new Coord4d(nextX, ratio * MathUtils.sin120, -ratio * MathUtils.cos120, nextInt)) }
        points1.any { MathUtils.eq(it, new Coord4d(nextX, -ratio * MathUtils.sin120, -ratio * MathUtils.cos120, nextInt)) }
        st.calc()
        st.currentTime == nextInt + 2
        st.activeEvents.size() == 4
        st.deadEvents.size() == 8

        for (int i = 3; i <= nextInt + 1; i++) {
            st.calc()
            st.currentTime == i + nextInt
            st.deadEvents.size() == 8
            st.activeEvents.size() == 4
            st.currentPoints().size() == 4
        }
        st.calc()
        st.currentTime == nextInt + nextInt + 2
        st.activeEvents.size() == 4
        st.deadEvents.size() == 12
        st.calc()
        st.currentTime == nextInt + nextInt + 3
        st.activeEvents.size() == 4
        st.deadEvents.size() == 12
        def points2 = st.currentPoints()
        points2.size() == 12
        points2.any { MathUtils.eq(it, new Coord4d(2d * nextX, 0d, 0d, 2d * nextInt)) }
        points2.any { MathUtils.eq(it, new Coord4d(2d * nextX, 0d, ratio, 2d * nextInt)) }
        points2.any { MathUtils.eq(it, new Coord4d(2d * nextX, ratio * MathUtils.sin120, ratio * MathUtils.cos120, 2d * nextInt)) }
        points2.any { MathUtils.eq(it, new Coord4d(2d * nextX, -ratio * MathUtils.sin120, ratio * MathUtils.cos120, 2d * nextInt)) }
    }
}
