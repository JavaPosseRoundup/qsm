package org.freddy33.math.bigInt

import spock.lang.Specification

import static org.freddy33.math.bigInt.MathUtilsInt.almostEquals
import static org.freddy33.math.bigInt.MathUtilsInt.EPSILON_INT
import static org.freddy33.math.bigInt.TrigoInt.*

class IntMathTest extends Specification {
    def "basic Math"() {
        expect:
        MathUtilsInt.almostEquals(EPSILON_INT - 1G, 0G)
        almostEquals(1G - EPSILON_INT, 0G)
        almostEquals(0G, EPSILON_INT - 1G)
        almostEquals(0G, 1G - EPSILON_INT)

        !almostEquals(EPSILON_INT, 0G)
        !almostEquals(-EPSILON_INT, 0G)
        !almostEquals(EPSILON_INT + 1G, 1G)
        !almostEquals(EPSILON_INT - 1G, -1G)
        !almostEquals(0G, EPSILON_INT)
        !almostEquals(0G, -EPSILON_INT)
        !almostEquals(1G, 1G + EPSILON_INT)
        !almostEquals(-1G, EPSILON_INT - 1G)

        almostEquals(EPSILON_INT, 1G)
        almostEquals(-EPSILON_INT, -1G)
        almostEquals(1G, EPSILON_INT)
        almostEquals(-1G, -EPSILON_INT)
        !almostEquals(-D180, D180)
    }

    def "basic Trig"() {
        expect:
        trigMap[0G] == 0G
        trigMap[D90] == ONE
        invTrigMap[ONE] == D90
        invTrigMap[0G] == 0G
        almostEquals(trigMap[D30], ONE_HALF)
        almostEquals(cos(D120), -ONE_HALF)
        sin(0G) == 0G
        almostEquals(sin(D30), ONE_HALF)
        almostEquals(invTrigMap[ONE_HALF], D30)
        almostEquals(asin(ONE_HALF), D30)
        almostEquals(asin(-ONE_HALF), -D30)
        almostEquals(asin(ONE), D90)
        almostEquals(asin(-ONE), -D90)
    }

    def "test Point4i"() {
        expect:
        mult3 == coord * 3
        plusX == coord + new Vector3i(2G * ONE, 0G, 0G)
        plusY == coord + new Vector3i(0G, 2G * ONE, 0G)
        plusZ == coord + new Vector3i(0G, 0G, 2G * ONE)

        where:
        coord << [
                new Point4i(0G, 0G, 0G),
                new Point4i(0G, 0G, 0G, ONE),
                new Point4i(ONE, ONE, ONE),
                new Point4i(-ONE, -ONE, -ONE, ONE)
        ]
        mult3 << [
                new Point4i(0G, 0G, 0G),
                new Point4i(0G, 0G, 0G, ONE),
                new Point4i(3G * ONE, 3G * ONE, 3G * ONE),
                new Point4i(-3G * ONE, -3G * ONE, -3G * ONE, ONE)
        ]
        plusX << [
                new Point4i(2G * ONE, 0G, 0G),
                new Point4i(2G * ONE, 0G, 0G, ONE),
                new Point4i(3G * ONE, ONE, ONE),
                new Point4i(ONE, -ONE, -ONE, ONE)
        ]
        plusY << [
                new Point4i(0G, 2G * ONE, 0G),
                new Point4i(0G, 2G * ONE, 0G, ONE),
                new Point4i(ONE, 3G * ONE, ONE),
                new Point4i(-ONE, ONE, -ONE, ONE)
        ]
        plusZ << [
                new Point4i(0G, 0G, 2G * ONE),
                new Point4i(0G, 0G, 2G * ONE, ONE),
                new Point4i(ONE, ONE, 3G * ONE),
                new Point4i(-ONE, -ONE, ONE, ONE)
        ]
    }

    def "test Vector3i"() {
        expect:
        mult3 == vect * 3G
        plusX == vect + new Vector3i(2G * ONE, 0G, 0G)
        plusY == vect + new Vector3i(0G, 2G * ONE, 0G)
        plusZ == vect + new Vector3i(0G, 0G, 2G * ONE)

        where:
        vect << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(ONE, ONE, ONE),
                new Vector3i(-ONE, -ONE, -ONE)
        ]
        mult3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(3G * ONE, 3G * ONE, 3G * ONE),
                new Vector3i(-3G * ONE, -3G * ONE, -3G * ONE)
        ]
        plusX << [
                new Vector3i(2G * ONE, 0G, 0G),
                new Vector3i(3G * ONE, ONE, ONE),
                new Vector3i(ONE, -ONE, -ONE)
        ]
        plusY << [
                new Vector3i(0G, 2G * ONE, 0G),
                new Vector3i(ONE, 3G * ONE, ONE),
                new Vector3i(-ONE, ONE, -ONE)
        ]
        plusZ << [
                new Vector3i(0G, 0G, 2G * ONE),
                new Vector3i(ONE, ONE, 3G * ONE),
                new Vector3i(-ONE, -ONE, ONE)
        ]
    }

    def "test PolarVector3i"() {
        expect:
        mult3 == (vect * (3G * ONE)).toCartesian()
        multMinus3 == (vect * (-3G * ONE)).toCartesian()
        mult3 * 5G == (vect * (15G * ONE)).toCartesian()
        multMinus3 * 5G == (vect * (-15G * ONE)).toCartesian()

        where:
        vect << [
                new SphericalVector3i(0G, 0G, 0G),
                new SphericalVector3i(D90, 0G),
                new SphericalVector3i(D90, D90),
                new SphericalVector3i(0G, 0G),
                new SphericalVector3i(D120, 0G),
                new SphericalVector3i(D90, D120)
        ]
        mult3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(3G * ONE, 0G, 0G),
                new Vector3i(0G, 3G * ONE, 0G),
                new Vector3i(0G, 0G, 3G * ONE),
                new Vector3i(3G * sin(D120), 0G, 3G * cos(D120)),
                new Vector3i(3G * cos(D120), 3G * sin(D120), 0G)
        ]
        multMinus3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(-3G * ONE, 0G, 0G),
                new Vector3i(0G, -3G * ONE, 0G),
                new Vector3i(0G, 0G, -3G * ONE),
                new Vector3i(-3G * sin(D120), 0G, -3G * cos(D120)),
                new Vector3i(-3G * cos(D120), -3G * sin(D120), 0G)
        ]
    }
}
