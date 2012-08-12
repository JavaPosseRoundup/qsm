package org.freddy33.qsm.flat_disk_surface

import org.freddy33.math.bigInt.MathUtilsInt
import org.freddy33.math.dbl.MathUtilsDbl
import org.freddy33.math.dbl.Point3d
import org.freddy33.math.dbl.SphericalUnitVector2d
import org.freddy33.math.dbl.Vector3d
import org.freddy33.qsm.flat_disk_surface.calc.EventBlockDouble
import spock.lang.Specification

import static org.freddy33.math.bigInt.TrigoInt.ONE
import static org.freddy33.math.bigInt.TrigoInt.ONE_HALF
import org.freddy33.qsm.flat_disk_surface.calc.PropagatingEvents
import org.freddy33.qsm.flat_disk_surface.calc.PropagatingEvent
import org.freddy33.math.dbl.Point4d
import org.freddy33.math.bigInt.EventColor

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 8:46 AM
 * To change this template use File | Settings | File Templates.
 */
class PropagatingEventsTest extends Specification {

    def "first level PropagatingEvent test"() {

        expect:
        def pe = new PropagatingEvent(Point4d.origin(), Vector3d.X(), Vector3d.Z(), color)
        def next = pe.nextEvents()
        pe.origin == Point4d.origin()
        pe.z == -Vector3d.Y()
        next.size() == 3
        MathUtilsDbl.eq(next[0].origin.magSquared(next[1].origin), 1d)
        MathUtilsDbl.eq(next[0].origin.magSquared(next[2].origin), 1d)
        MathUtilsDbl.eq(next[1].origin.magSquared(next[2].origin), 1d)

        where:
        color << [ EventColor.plus_i, EventColor.minus_i ]
    }

    def "two level PropagatingEvent test"() {

        expect:
        def pes = new PropagatingEvents(new PropagatingEvent(Point4d.origin(), Vector3d.X(), Vector3d.Z(), color))
        def next = pes.first.nextEvents()
        pes.events.size() == sizes[0]
        pes.first.origin == Point4d.origin()
        pes.increment()
        pes.events.size() == sizes[1]
        pes.events.any { it == next[0] }
        pes.events.any { it == next[1] }
        pes.events.any { it == next[2] }
        pes.increment()
        pes.events.size() == sizes[2]
        pes.increment()
        pes.events.size() == sizes[3]
        pes.increment()
        pes.events.size() == sizes[4]

        where:
        color << [ EventColor.plus_i, EventColor.minus_i, EventColor.plus_1, EventColor.minus_1 ]
        sizes << [
                [1,3,6,18,28],
                [1,3,9,24,51],
                [1,3,9,21,46],
                [1,3,9,21,44]
        ]
    }
}
