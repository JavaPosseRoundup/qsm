package org.freddy33.math.dbl

import spock.lang.Specification
import org.freddy33.math.dbl.Coord4d
import org.freddy33.math.dbl.Vector3d

class DoubleMathTest extends Specification {
    def "test Coord4d"() {
        expect:
        mult3 == coord * 3
        plusX == coord + new Vector3d(2d, 0d, 0d)
        plusY == coord + new Vector3d(0d, 2d, 0d)
        plusZ == coord + new Vector3d(0d, 0d, 2d)

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
        plusX == vect + new Vector3d(2d, 0d, 0d)
        plusY == vect + new Vector3d(0d, 2d, 0d)
        plusZ == vect + new Vector3d(0d, 0d, 2d)

        where:
        vect << [
                new Vector3d(0d, 0d, 0d),
                new Vector3d(1d, 1d, 1d),
                new Vector3d(-1d, -1d, -1d)
        ]
        mult3 << [
                new Vector3d(0d, 0d, 0d),
                new Vector3d(3d, 3d, 3d),
                new Vector3d(-3d, -3d, -3d)
        ]
        plusX << [
                new Vector3d(2d, 0d, 0d),
                new Vector3d(3d, 1d, 1d),
                new Vector3d(1d, -1d, -1d)
        ]
        plusY << [
                new Vector3d(0d, 2d, 0d),
                new Vector3d(1d, 3d, 1d),
                new Vector3d(-1d, 1d, -1d)
        ]
        plusZ << [
                new Vector3d(0d, 0d, 2d),
                new Vector3d(1d, 1d, 3d),
                new Vector3d(-1d, -1d, 1d)
        ]
    }
}
