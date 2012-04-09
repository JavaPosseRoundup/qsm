package org.freddy33.qsm.space;


import org.freddy33.math.Coord4d
import org.freddy33.math.MathUtils
import org.freddy33.math.Triangle
import org.freddy33.math.Vector4d
import spock.lang.Specification

import static org.freddy33.math.MathUtils.getCos120
import static org.freddy33.math.MathUtils.getSin120

public class TriangleDoubleTest extends Specification {
    static def s22 = Math.sqrt(2d) / 2d
    static def evt1 = new EventDouble(0d, 0d, 0d, 3, new Vector4d(3d, 0d, 0d))
    static def evt2 = new EventDouble(0d, 0d, 1d, 3, new Vector4d(3d, 3d, 0d))
    static def evt3 = new EventDouble(0d, sin120, cos120, 3, evt1.direction)
    static def evt4 = new EventDouble(0d, -sin120, cos120, 3, evt1.direction)
    static def ratio = 1000d
    static double bigDist = ratio * sin120 * 2d
    static double nextX = Math.sqrt((bigDist * bigDist) - (ratio * ratio))

    def "test Event"() {
        expect:
        event.direction == dir

        where:
        event << [
                new EventDouble(0d, 0d, 0d, 3, new Vector4d(3d, 0d, 0d)),
                new EventDouble(0d, 0d, 1d, 3, new Vector4d(3d, 3d, 0d)),
                new EventDouble(0d, sin120, cos120, 3, new Vector4d(3d, 0d, 0d)),

        ]
        dir << [
                new Vector4d(1d, 0d, 0d),
                new Vector4d(s22, s22, 0d),
                new Vector4d(1d, 0d, 0d)
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
                new Triangle(evt2.point, evt3.point, evt4.point),
                new Triangle(evt1.point, evt3.point, evt4.point)
        ]
        dir << [
                new Vector4d(1d, 0d, 0d),
                new Vector4d(1d, 0d, 0d)
        ]
        radius2 << [
                1d,
                1d
        ]
        center << [
                new Coord4d(0d, 0d, 0d),
                new Coord4d(0d, 0d, -1d)
        ]
        eventAt2 << [
                new Coord4d(Math.sqrt(2d), 0d, 0d),
                new Coord4d(Math.sqrt(2d), 0d, -1d)
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
                new Triangle(evt2.point * ratio, evt3.point * ratio, evt4.point * ratio),
                new Triangle(evt1.point * ratio, evt3.point * ratio, evt4.point * ratio)
        ]
        dir << [
                new Vector4d(1d, 0d, 0d),
                new Vector4d(1d, 0d, 0d)
        ]
        radius2 << [
                ratio * ratio,
                ratio * ratio
        ]
        center << [
                new Coord4d(0d, 0d, 0d),
                new Coord4d(0d, 0d, -ratio)
        ]
        eventAt2 << [
                new Coord4d(nextX, 0d, 0d),
                new Coord4d(nextX, 0d, -ratio)
        ]
    }
}
