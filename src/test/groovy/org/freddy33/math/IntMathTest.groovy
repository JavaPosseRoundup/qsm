package org.freddy33.math

import spock.lang.Specification

import static SphericalVector3i.DIV

class IntMathTest extends Specification {
    def "basic Trig"() {
        expect:
        SphericalVector3i.trigMap[SphericalVector3i.D30] == SphericalVector3i.D180
        SphericalVector3i.cos(SphericalVector3i.D120) == -SphericalVector3i.D180
        SphericalVector3i.sin(0G) == 0G
        SphericalVector3i.sin(SphericalVector3i.D30) == SphericalVector3i.D180
        SphericalVector3i.invTrigMap[SphericalVector3i.D180] == SphericalVector3i.D30
        SphericalVector3i.asin(SphericalVector3i.D180) == SphericalVector3i.D30
        SphericalVector3i.asin(-SphericalVector3i.D180) == -SphericalVector3i.D30
    }

    def "test Point4i"() {
        expect:
        mult3 == coord * 3
        plusX == coord + new Vector3i(2G * DIV, 0G, 0G)
        plusY == coord + new Vector3i(0G, 2G * DIV, 0G)
        plusZ == coord + new Vector3i(0G, 0G, 2G * DIV)

        where:
        coord << [
                new Point4i(0G, 0G, 0G),
                new Point4i(0G, 0G, 0G, DIV),
                new Point4i(DIV, DIV, DIV),
                new Point4i(-DIV, -DIV, -DIV, DIV)
        ]
        mult3 << [
                new Point4i(0G, 0G, 0G),
                new Point4i(0G, 0G, 0G, DIV),
                new Point4i(3G * DIV, 3G * DIV, 3G * DIV),
                new Point4i(-3G * DIV, -3G * DIV, -3G * DIV, DIV)
        ]
        plusX << [
                new Point4i(2G * DIV, 0G, 0G),
                new Point4i(2G * DIV, 0G, 0G, DIV),
                new Point4i(3G * DIV, DIV, DIV),
                new Point4i(DIV, -DIV, -DIV, DIV)
        ]
        plusY << [
                new Point4i(0G, 2G * DIV, 0G),
                new Point4i(0G, 2G * DIV, 0G, DIV),
                new Point4i(DIV, 3G * DIV, DIV),
                new Point4i(-DIV, DIV, -DIV, DIV)
        ]
        plusZ << [
                new Point4i(0G, 0G, 2G * DIV),
                new Point4i(0G, 0G, 2G * DIV, DIV),
                new Point4i(DIV, DIV, 3G * DIV),
                new Point4i(-DIV, -DIV, DIV, DIV)
        ]
    }

    def "test Vector3i"() {
        expect:
        mult3 == vect * 3G
        plusX == vect + new Vector3i(2G * DIV, 0G, 0G)
        plusY == vect + new Vector3i(0G, 2G * DIV, 0G)
        plusZ == vect + new Vector3i(0G, 0G, 2G * DIV)

        where:
        vect << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(DIV, DIV, DIV),
                new Vector3i(-DIV, -DIV, -DIV)
        ]
        mult3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(3G * DIV, 3G * DIV, 3G * DIV),
                new Vector3i(-3G * DIV, -3G * DIV, -3G * DIV)
        ]
        plusX << [
                new Vector3i(2G * DIV, 0G, 0G),
                new Vector3i(3G * DIV, DIV, DIV),
                new Vector3i(DIV, -DIV, -DIV)
        ]
        plusY << [
                new Vector3i(0G, 2G * DIV, 0G),
                new Vector3i(DIV, 3G * DIV, DIV),
                new Vector3i(-DIV, DIV, -DIV)
        ]
        plusZ << [
                new Vector3i(0G, 0G, 2G * DIV),
                new Vector3i(DIV, DIV, 3G * DIV),
                new Vector3i(-DIV, -DIV, DIV)
        ]
    }

    def "test PolarVector3i"() {
        expect:
        mult3 == (vect * (3G * DIV)).toCartesian()
        multMinus3 == (vect * (-3G * DIV)).toCartesian()
        mult3 * 5G == (vect * (15G * DIV)).toCartesian()
        multMinus3 * 5G == (vect * (-15G * DIV)).toCartesian()

        where:
        vect << [
                new SphericalVector3i(0G, 0G, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, SphericalVector3i.D90),
                new SphericalVector3i(DIV, 0G, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D120, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, SphericalVector3i.D120)
        ]
        mult3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(3G * DIV, 0G, 0G),
                new Vector3i(0G, 3G * DIV, 0G),
                new Vector3i(0G, 0G, 3G * DIV),
                new Vector3i(3G * SphericalVector3i.sin(SphericalVector3i.D120), 0G, 3G * SphericalVector3i.cos(SphericalVector3i.D120)),
                new Vector3i(3G * SphericalVector3i.cos(SphericalVector3i.D120), 3G * SphericalVector3i.sin(SphericalVector3i.D120), 0G)
        ]
        multMinus3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(-3G * DIV, 0G, 0G),
                new Vector3i(0G, -3G * DIV, 0G),
                new Vector3i(0G, 0G, -3G * DIV),
                new Vector3i(-3G * SphericalVector3i.sin(SphericalVector3i.D120), 0G, -3G * SphericalVector3i.cos(SphericalVector3i.D120)),
                new Vector3i(-3G * SphericalVector3i.cos(SphericalVector3i.D120), -3G * SphericalVector3i.sin(SphericalVector3i.D120), 0G)
        ]
    }
}
