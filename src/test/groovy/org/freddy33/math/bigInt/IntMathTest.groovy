package org.freddy33.math.bigInt

import spock.lang.Specification

import static MathUtilsInt.almostEquals

class IntMathTest extends Specification {
    def "basic Math"() {
        expect:
        MathUtilsInt.almostEquals(MathUtilsInt.EPSILON_INT - 1G, 0G)
        almostEquals(1G - MathUtilsInt.EPSILON_INT, 0G)
        almostEquals(0G, MathUtilsInt.EPSILON_INT - 1G)
        almostEquals(0G, 1G - MathUtilsInt.EPSILON_INT)

        !almostEquals(MathUtilsInt.EPSILON_INT, 0G)
        !almostEquals(-MathUtilsInt.EPSILON_INT, 0G)
        !almostEquals(MathUtilsInt.EPSILON_INT + 1G, 1G)
        !almostEquals(MathUtilsInt.EPSILON_INT - 1G, -1G)
        !almostEquals(0G, MathUtilsInt.EPSILON_INT)
        !almostEquals(0G, -MathUtilsInt.EPSILON_INT)
        !almostEquals(1G, 1G + MathUtilsInt.EPSILON_INT)
        !almostEquals(-1G, MathUtilsInt.EPSILON_INT - 1G)

        almostEquals(MathUtilsInt.EPSILON_INT, 1G)
        almostEquals(-MathUtilsInt.EPSILON_INT, -1G)
        almostEquals(1G, MathUtilsInt.EPSILON_INT)
        almostEquals(-1G, -MathUtilsInt.EPSILON_INT)
        !almostEquals(-MathUtilsInt.D180, MathUtilsInt.D180)
    }

    def "basic Trig"() {
        expect:
        SphericalVector3i.trigMap[0G] == 0G
        SphericalVector3i.trigMap[MathUtilsInt.D90] == MathUtilsInt.ONE
        SphericalVector3i.invTrigMap[MathUtilsInt.ONE] == MathUtilsInt.D90
        SphericalVector3i.invTrigMap[0G] == 0G
        almostEquals(SphericalVector3i.trigMap[MathUtilsInt.D30], MathUtilsInt.ONE_HALF)
        almostEquals(SphericalVector3i.cos(MathUtilsInt.D120), -MathUtilsInt.ONE_HALF)
        SphericalVector3i.sin(0G) == 0G
        almostEquals(SphericalVector3i.sin(MathUtilsInt.D30), MathUtilsInt.ONE_HALF)
        almostEquals(SphericalVector3i.invTrigMap[MathUtilsInt.ONE_HALF], MathUtilsInt.D30)
        almostEquals(SphericalVector3i.asin(MathUtilsInt.ONE_HALF), MathUtilsInt.D30)
        almostEquals(SphericalVector3i.asin(-MathUtilsInt.ONE_HALF), -MathUtilsInt.D30)
        almostEquals(SphericalVector3i.asin(MathUtilsInt.ONE), MathUtilsInt.D90)
        almostEquals(SphericalVector3i.asin(-MathUtilsInt.ONE), -MathUtilsInt.D90)
    }

    def "test Point4i"() {
        expect:
        mult3 == coord * 3
        plusX == coord + new Vector3i(2G * MathUtilsInt.ONE, 0G, 0G)
        plusY == coord + new Vector3i(0G, 2G * MathUtilsInt.ONE, 0G)
        plusZ == coord + new Vector3i(0G, 0G, 2G * MathUtilsInt.ONE)

        where:
        coord << [
                new Point4i(0G, 0G, 0G),
                new Point4i(0G, 0G, 0G, MathUtilsInt.ONE),
                new Point4i(MathUtilsInt.ONE, MathUtilsInt.ONE, MathUtilsInt.ONE),
                new Point4i(-MathUtilsInt.ONE, -MathUtilsInt.ONE, -MathUtilsInt.ONE, MathUtilsInt.ONE)
        ]
        mult3 << [
                new Point4i(0G, 0G, 0G),
                new Point4i(0G, 0G, 0G, MathUtilsInt.ONE),
                new Point4i(3G * MathUtilsInt.ONE, 3G * MathUtilsInt.ONE, 3G * MathUtilsInt.ONE),
                new Point4i(-3G * MathUtilsInt.ONE, -3G * MathUtilsInt.ONE, -3G * MathUtilsInt.ONE, MathUtilsInt.ONE)
        ]
        plusX << [
                new Point4i(2G * MathUtilsInt.ONE, 0G, 0G),
                new Point4i(2G * MathUtilsInt.ONE, 0G, 0G, MathUtilsInt.ONE),
                new Point4i(3G * MathUtilsInt.ONE, MathUtilsInt.ONE, MathUtilsInt.ONE),
                new Point4i(MathUtilsInt.ONE, -MathUtilsInt.ONE, -MathUtilsInt.ONE, MathUtilsInt.ONE)
        ]
        plusY << [
                new Point4i(0G, 2G * MathUtilsInt.ONE, 0G),
                new Point4i(0G, 2G * MathUtilsInt.ONE, 0G, MathUtilsInt.ONE),
                new Point4i(MathUtilsInt.ONE, 3G * MathUtilsInt.ONE, MathUtilsInt.ONE),
                new Point4i(-MathUtilsInt.ONE, MathUtilsInt.ONE, -MathUtilsInt.ONE, MathUtilsInt.ONE)
        ]
        plusZ << [
                new Point4i(0G, 0G, 2G * MathUtilsInt.ONE),
                new Point4i(0G, 0G, 2G * MathUtilsInt.ONE, MathUtilsInt.ONE),
                new Point4i(MathUtilsInt.ONE, MathUtilsInt.ONE, 3G * MathUtilsInt.ONE),
                new Point4i(-MathUtilsInt.ONE, -MathUtilsInt.ONE, MathUtilsInt.ONE, MathUtilsInt.ONE)
        ]
    }

    def "test Vector3i"() {
        expect:
        mult3 == vect * 3G
        plusX == vect + new Vector3i(2G * MathUtilsInt.ONE, 0G, 0G)
        plusY == vect + new Vector3i(0G, 2G * MathUtilsInt.ONE, 0G)
        plusZ == vect + new Vector3i(0G, 0G, 2G * MathUtilsInt.ONE)

        where:
        vect << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(MathUtilsInt.ONE, MathUtilsInt.ONE, MathUtilsInt.ONE),
                new Vector3i(-MathUtilsInt.ONE, -MathUtilsInt.ONE, -MathUtilsInt.ONE)
        ]
        mult3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(3G * MathUtilsInt.ONE, 3G * MathUtilsInt.ONE, 3G * MathUtilsInt.ONE),
                new Vector3i(-3G * MathUtilsInt.ONE, -3G * MathUtilsInt.ONE, -3G * MathUtilsInt.ONE)
        ]
        plusX << [
                new Vector3i(2G * MathUtilsInt.ONE, 0G, 0G),
                new Vector3i(3G * MathUtilsInt.ONE, MathUtilsInt.ONE, MathUtilsInt.ONE),
                new Vector3i(MathUtilsInt.ONE, -MathUtilsInt.ONE, -MathUtilsInt.ONE)
        ]
        plusY << [
                new Vector3i(0G, 2G * MathUtilsInt.ONE, 0G),
                new Vector3i(MathUtilsInt.ONE, 3G * MathUtilsInt.ONE, MathUtilsInt.ONE),
                new Vector3i(-MathUtilsInt.ONE, MathUtilsInt.ONE, -MathUtilsInt.ONE)
        ]
        plusZ << [
                new Vector3i(0G, 0G, 2G * MathUtilsInt.ONE),
                new Vector3i(MathUtilsInt.ONE, MathUtilsInt.ONE, 3G * MathUtilsInt.ONE),
                new Vector3i(-MathUtilsInt.ONE, -MathUtilsInt.ONE, MathUtilsInt.ONE)
        ]
    }

    def "test PolarVector3i"() {
        expect:
        mult3 == (vect * (3G * MathUtilsInt.ONE)).toCartesian()
        multMinus3 == (vect * (-3G * MathUtilsInt.ONE)).toCartesian()
        mult3 * 5G == (vect * (15G * MathUtilsInt.ONE)).toCartesian()
        multMinus3 * 5G == (vect * (-15G * MathUtilsInt.ONE)).toCartesian()

        where:
        vect << [
                new SphericalVector3i(0G, 0G, 0G),
                new SphericalVector3i(MathUtilsInt.D90, 0G),
                new SphericalVector3i(MathUtilsInt.D90, MathUtilsInt.D90),
                new SphericalVector3i(0G, 0G),
                new SphericalVector3i(MathUtilsInt.D120, 0G),
                new SphericalVector3i(MathUtilsInt.D90, MathUtilsInt.D120)
        ]
        mult3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(3G * MathUtilsInt.ONE, 0G, 0G),
                new Vector3i(0G, 3G * MathUtilsInt.ONE, 0G),
                new Vector3i(0G, 0G, 3G * MathUtilsInt.ONE),
                new Vector3i(3G * SphericalVector3i.sin(MathUtilsInt.D120), 0G, 3G * SphericalVector3i.cos(MathUtilsInt.D120)),
                new Vector3i(3G * SphericalVector3i.cos(MathUtilsInt.D120), 3G * SphericalVector3i.sin(MathUtilsInt.D120), 0G)
        ]
        multMinus3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(-3G * MathUtilsInt.ONE, 0G, 0G),
                new Vector3i(0G, -3G * MathUtilsInt.ONE, 0G),
                new Vector3i(0G, 0G, -3G * MathUtilsInt.ONE),
                new Vector3i(-3G * SphericalVector3i.sin(MathUtilsInt.D120), 0G, -3G * SphericalVector3i.cos(MathUtilsInt.D120)),
                new Vector3i(-3G * SphericalVector3i.cos(MathUtilsInt.D120), -3G * SphericalVector3i.sin(MathUtilsInt.D120), 0G)
        ]
    }
}
