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

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 8:46 AM
 * To change this template use File | Settings | File Templates.
 */
class PropagatingEventsTest extends Specification {

    def "first level PropagatingEvent test"() {
        def pe = new PropagatingEvent(Point4d.origin(), Vector3d.X(), Vector3d.Z())
        println "$pe"
        def next = pe.nextEvents()
        println "$next"

        expect:
        pe.origin == Point4d.origin()
        pe.z == -Vector3d.Y()
        next.size() == 3
        MathUtilsDbl.eq(next[0].origin.magSquared(next[1].origin), 1d)
        MathUtilsDbl.eq(next[0].origin.magSquared(next[2].origin), 1d)
        MathUtilsDbl.eq(next[1].origin.magSquared(next[2].origin), 1d)
    }

    def "two level PropagatingEvent test"() {
        def pes = new PropagatingEvents(new PropagatingEvent(Point4d.origin(), Vector3d.X(), Vector3d.Z()))
        def next = pes.first.nextEvents()

        expect:
        pes.events.size() == 1
        pes.first.origin == Point4d.origin()
        pes.increment()
        pes.events.size() == 3
        pes.events.toArray()[2] == next[0]
        pes.increment()
        pes.events.size() == 6
    }
}
