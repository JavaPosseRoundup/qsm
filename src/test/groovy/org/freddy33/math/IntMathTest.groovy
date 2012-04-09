package org.freddy33.math

import spock.lang.Specification

class IntMathTest extends Specification {
    def "test Point4i"() {
        expect:
        mult3 == coord * 3
        plusX == coord + new Vector3i(2G * PolarVector3i.DIV, 0G, 0G)
        plusY == coord + new Vector3i(0G, 2G * PolarVector3i.DIV, 0G)
        plusZ == coord + new Vector3i(0G, 0G, 2G * PolarVector3i.DIV)

        where:
        coord << [
                new Point4i(0G, 0G, 0G),
                new Point4i(0G, 0G, 0G, PolarVector3i.DIV),
                new Point4i(PolarVector3i.DIV, PolarVector3i.DIV, PolarVector3i.DIV),
                new Point4i(-PolarVector3i.DIV, -PolarVector3i.DIV, -PolarVector3i.DIV, PolarVector3i.DIV)
        ]
        mult3 << [
                new Point4i(0G, 0G, 0G),
                new Point4i(0G, 0G, 0G, PolarVector3i.DIV),
                new Point4i(3G * PolarVector3i.DIV, 3G * PolarVector3i.DIV, 3G * PolarVector3i.DIV),
                new Point4i(-3G * PolarVector3i.DIV, -3G * PolarVector3i.DIV, -3G * PolarVector3i.DIV, PolarVector3i.DIV)
        ]
        plusX << [
                new Point4i(2G * PolarVector3i.DIV, 0G, 0G),
                new Point4i(2G * PolarVector3i.DIV, 0G, 0G, PolarVector3i.DIV),
                new Point4i(3G * PolarVector3i.DIV, PolarVector3i.DIV, PolarVector3i.DIV),
                new Point4i(PolarVector3i.DIV, -PolarVector3i.DIV, -PolarVector3i.DIV, PolarVector3i.DIV)
        ]
        plusY << [
                new Point4i(0G, 2G * PolarVector3i.DIV, 0G),
                new Point4i(0G, 2G * PolarVector3i.DIV, 0G, PolarVector3i.DIV),
                new Point4i(PolarVector3i.DIV, 3G * PolarVector3i.DIV, PolarVector3i.DIV),
                new Point4i(-PolarVector3i.DIV, PolarVector3i.DIV, -PolarVector3i.DIV, PolarVector3i.DIV)
        ]
        plusZ << [
                new Point4i(0G, 0G, 2G * PolarVector3i.DIV),
                new Point4i(0G, 0G, 2G * PolarVector3i.DIV, PolarVector3i.DIV),
                new Point4i(PolarVector3i.DIV, PolarVector3i.DIV, 3G * PolarVector3i.DIV),
                new Point4i(-PolarVector3i.DIV, -PolarVector3i.DIV, PolarVector3i.DIV, PolarVector3i.DIV)
        ]
    }

    def "test Vector3i"() {
        expect:
        mult3 == vect * 3
        plusX == vect + new Vector3i(2G * PolarVector3i.DIV, 0G, 0G)
        plusY == vect + new Vector3i(0G, 2G * PolarVector3i.DIV, 0G)
        plusZ == vect + new Vector3i(0G, 0G, 2G * PolarVector3i.DIV)

        where:
        vect << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(PolarVector3i.DIV, PolarVector3i.DIV, PolarVector3i.DIV),
                new Vector3i(-PolarVector3i.DIV, -PolarVector3i.DIV, -PolarVector3i.DIV)
        ]
        mult3 << [
                new Vector3i(0G, 0G, 0G),
                new Vector3i(3G * PolarVector3i.DIV, 3G * PolarVector3i.DIV, 3G * PolarVector3i.DIV),
                new Vector3i(-3G * PolarVector3i.DIV, -3G * PolarVector3i.DIV, -3G * PolarVector3i.DIV)
        ]
        plusX << [
                new Vector3i(2G * PolarVector3i.DIV, 0G, 0G),
                new Vector3i(3G * PolarVector3i.DIV, PolarVector3i.DIV, PolarVector3i.DIV),
                new Vector3i(PolarVector3i.DIV, -PolarVector3i.DIV, -PolarVector3i.DIV)
        ]
        plusY << [
                new Vector3i(0G, 2G * PolarVector3i.DIV, 0G),
                new Vector3i(PolarVector3i.DIV, 3G * PolarVector3i.DIV, PolarVector3i.DIV),
                new Vector3i(-PolarVector3i.DIV, PolarVector3i.DIV, -PolarVector3i.DIV)
        ]
        plusZ << [
                new Vector3i(0G, 0G, 2G * PolarVector3i.DIV),
                new Vector3i(PolarVector3i.DIV, PolarVector3i.DIV, 3G * PolarVector3i.DIV),
                new Vector3i(-PolarVector3i.DIV, -PolarVector3i.DIV, PolarVector3i.DIV)
        ]
    }
}
