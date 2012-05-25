package org.freddy33.qsm.sphere_dbl;


import org.freddy33.math.dbl.Coord4d
import org.freddy33.math.MathUtils

import org.freddy33.math.dbl.Vector3d
import spock.lang.Specification

import static org.freddy33.math.MathUtils.getCos120
import static org.freddy33.math.MathUtils.getSin120
import org.freddy33.math.dbl.TriangleDbl
import org.freddy33.qsm.sphere_surface_int.calc.EventDouble

public class TriangleDoubleTest extends Specification {
    static def s22 = Math.sqrt(2d) / 2d
    static def evt1 = new EventDouble(0d, 0d, 0d, 3d, new Vector3d(3d, 0d, 0d))
    static def evt2 = new EventDouble(0d, 0d, 1d, 3d, new Vector3d(3d, 3d, 0d))
    static def evt3 = new EventDouble(0d, org.freddy33.math.MathUtils.sin120, org.freddy33.math.MathUtils.cos120, 3d, evt1.direction)
    static def evt4 = new EventDouble(0d, -org.freddy33.math.MathUtils.sin120, org.freddy33.math.MathUtils.cos120, 3d, evt1.direction)
    static def ratio = 1000d
    static double bigDist = ratio * org.freddy33.math.MathUtils.sin120 * 2d
    static double nextX = Math.sqrt((bigDist * bigDist) - (ratio * ratio))

    def "test Event"() {
        expect:
        event.direction == dir

        where:
        event << [
                new EventDouble(0d, 0d, 0d, 3d, new Vector3d(3d, 0d, 0d)),
                new EventDouble(0d, 0d, 1d, 3d, new Vector3d(3d, 3d, 0d)),
                new EventDouble(0d, org.freddy33.math.MathUtils.sin120, org.freddy33.math.MathUtils.cos120, 3d, new Vector3d(3d, 0d, 0d)),

        ]
        dir << [
                new Vector3d(1d, 0d, 0d),
                new Vector3d(s22, s22, 0d),
                new Vector3d(1d, 0d, 0d)
        ]
    }

    def "test Triangle"() {
        expect:
        !triangle.isFlat()
        triangle.findCenter() == center
        triangle.finalDir(evt1.direction) == dir
        MathUtils.eq(triangle.radius2(), radius2)
        triangle.findEvent(evt1.direction) == eventAt2

        where:
        triangle << [
                new TriangleDbl(evt2.point, evt3.point, evt4.point),
                new TriangleDbl(evt1.point, evt3.point, evt4.point)
        ]
        dir << [
                new Vector3d(1d, 0d, 0d),
                new Vector3d(1d, 0d, 0d)
        ]
        radius2 << [
                1d,
                1d
        ]
        center << [
                new Coord4d(0d, 0d, 0d, 3d),
                new Coord4d(0d, 0d, -1d, 3d)
        ]
        eventAt2 << [
                new Coord4d(Math.sqrt(2d), 0d, 0d, 4d),
                new Coord4d(Math.sqrt(2d), 0d, -1d, 4d)
        ]
    }

    def "test big triangle"() {

        expect:
        !triangle.isFlat()
        triangle.findCenter() == center
        triangle.finalDir(evt1.direction) == dir
        MathUtils.eq(triangle.radius2(), radius2)
        triangle.findEvent(evt1.direction) == eventAt2

        where:
        triangle << [
                new TriangleDbl(evt2.point * ratio, evt3.point * ratio, evt4.point * ratio),
                new TriangleDbl(evt1.point * ratio, evt3.point * ratio, evt4.point * ratio)
        ]
        dir << [
                new Vector3d(1d, 0d, 0d),
                new Vector3d(1d, 0d, 0d)
        ]
        radius2 << [
                ratio * ratio,
                ratio * ratio
        ]
        center << [
                new Coord4d(0d, 0d, 0d, 3d),
                new Coord4d(0d, 0d, -ratio, 3d)
        ]
        eventAt2 << [
                new Coord4d(nextX, 0d, 0d, 3d + bigDist),
                new Coord4d(nextX, 0d, -ratio, 3d + bigDist)
        ]
    }
}
