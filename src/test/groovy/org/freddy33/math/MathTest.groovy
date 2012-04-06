package org.freddy33.math

import spock.lang.Specification

class MathTest extends Specification {
    def "test Coord4d"() {
        expect:
        mult3 == coord * 3
        plusX == coord + new Vector4d(2d, 0d, 0d)
        plusY == coord + new Vector4d(0d, 2d, 0d)
        plusZ == coord + new Vector4d(0d, 0d, 2d)

        where:
        coord << [
                new Coord4d(0d, 0d, 0d),
                new Coord4d(0d, 0d, 0d, 1d),
                new Coord4d(1d, 1d, 1d),
                new Coord4d(-1d, -1d, -1d, 1d)
        ]
        mult3 << [
                new Coord4d(0d, 0d, 0d),
                new Coord4d(0d, 0d, 0d, 1d),
                new Coord4d(3d, 3d, 3d),
                new Coord4d(-3d, -3d, -3d, 1d)
        ]
        plusX << [
                new Coord4d(2d, 0d, 0d),
                new Coord4d(2d, 0d, 0d, 1d),
                new Coord4d(3d, 1d, 1d),
                new Coord4d(1d, -1d, -1d, 1d)
        ]
        plusY << [
                new Coord4d(0d, 2d, 0d),
                new Coord4d(0d, 2d, 0d, 1d),
                new Coord4d(1d, 3d, 1d),
                new Coord4d(-1d, 1d, -1d, 1d)
        ]
        plusZ << [
                new Coord4d(0d, 0d, 2d),
                new Coord4d(0d, 0d, 2d, 1d),
                new Coord4d(1d, 1d, 3d),
                new Coord4d(-1d, -1d, 1d, 1d)
        ]
    }

    def "test Vector4d"() {
        expect:
        mult3 == vect * 3
        plusX == vect + new Vector4d(2d, 0d, 0d)
        plusY == vect + new Vector4d(0d, 2d, 0d)
        plusZ == vect + new Vector4d(0d, 0d, 2d)

        where:
        vect << [
                new Vector4d(0d, 0d, 0d),
                new Vector4d(0d, 0d, 0d, 1d),
                new Vector4d(1d, 1d, 1d),
                new Vector4d(-1d, -1d, -1d, 1d)
        ]
        mult3 << [
                new Vector4d(0d, 0d, 0d),
                new Vector4d(0d, 0d, 0d, 1d),
                new Vector4d(3d, 3d, 3d),
                new Vector4d(-3d, -3d, -3d, 1d)
        ]
        plusX << [
                new Vector4d(2d, 0d, 0d),
                new Vector4d(2d, 0d, 0d, 1d),
                new Vector4d(3d, 1d, 1d),
                new Vector4d(1d, -1d, -1d, 1d)
        ]
        plusY << [
                new Vector4d(0d, 2d, 0d),
                new Vector4d(0d, 2d, 0d, 1d),
                new Vector4d(1d, 3d, 1d),
                new Vector4d(-1d, 1d, -1d, 1d)
        ]
        plusZ << [
                new Vector4d(0d, 0d, 2d),
                new Vector4d(0d, 0d, 2d, 1d),
                new Vector4d(1d, 1d, 3d),
                new Vector4d(-1d, -1d, 1d, 1d)
        ]
    }
}
