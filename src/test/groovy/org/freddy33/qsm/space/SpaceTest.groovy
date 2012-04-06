package org.freddy33.qsm.space;


import org.freddy33.math.Coord4d
import org.freddy33.math.MathUtils
import org.freddy33.math.Triangle
import org.freddy33.math.Vector4d
import spock.lang.Specification

public class SpaceTest extends Specification {
    static def s22 = Math.sqrt(2d) / 2d
    static def sin120 = Math.sin(2d * Math.PI / 3d)
    static def cos120 = -0.5d
    static def evt1 = new Event(0d, 0d, 0d, 3, new Vector4d(3d, 0d, 0d))
    static def evt2 = new Event(0d, 0d, 1d, 3, new Vector4d(3d, 3d, 0d))
    static def evt3 = new Event(0d, sin120, cos120, 3, evt1.direction)
    static def evt4 = new Event(0d, -sin120, cos120, 3, evt1.direction)

    def "test Event"() {
        expect:
        event.direction == dir

        where:
        event << [
                new Event(0d, 0d, 0d, 3, new Vector4d(3d, 0d, 0d)),
                new Event(0d, 0d, 1d, 3, new Vector4d(3d, 3d, 0d)),
                new Event(0d, sin120, cos120, 3, new Vector4d(3d, 0d, 0d)),

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
        triangle.findEvent(1, evt1.direction) == null
        triangle.findEvent(2, evt1.direction) == eventAt2

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
                new Coord4d(Math.sqrt(3d), 0d, 0d),
                new Coord4d(Math.sqrt(3d), 0d, -1d)
        ]
    }
}
