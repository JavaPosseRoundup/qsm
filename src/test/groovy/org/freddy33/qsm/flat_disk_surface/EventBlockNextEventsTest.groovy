package org.freddy33.qsm.flat_disk_surface

import org.freddy33.qsm.flat_disk_surface.calc.EventBlockDouble

import spock.lang.Specification
import org.freddy33.math.dbl.*

import static org.freddy33.math.bigInt.TrigoInt.ONE
import static org.freddy33.math.bigInt.TrigoInt.ONE_HALF
import org.freddy33.math.bigInt.MathUtilsInt

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 8:46 AM
 * To change this template use File | Settings | File Templates.
 */
class EventBlockNextEventsTest extends Specification {

    public static final BigInteger SIZE = 10G
    public static final BigInteger CREATION_TIME = 7G
    public static final double one = (double) SIZE * ONE
    public static final double oneHalf = (double) SIZE * ONE_HALF
    public static final double oneCos60 = (double) SIZE * ONE * MathUtilsDbl.cos60
    public static final double oneSin60 = (double) SIZE * ONE * MathUtilsDbl.sin60

    def "simple electron"() {
        def firstBlock = EventBlockDouble.createElectron(
                new Point3d(0d,0d,0d),
                CREATION_TIME,
                new SphericalUnitVector2d(new Vector3d(1d,0d,0d)),
                0d,
                SIZE
        )
        def nextBlock = firstBlock.getImmediateNextBlock()
        def transformedBlock = nextBlock
        while (!MathUtilsInt.almostEquals((BigInteger)(transformedBlock.s-firstBlock.s), 0G)) {
            transformedBlock = transformedBlock.elasticConstraint(firstBlock)
            println "$transformedBlock"
        }

        expect:
        MathUtilsDbl.eq(firstBlock.s, Math.sqrt(3d)*SIZE*SIZE*ONE*ONE)
        MathUtilsInt.almostEquals(nextBlock.o.createdTime, (BigInteger)(SIZE*ONE/Math.sqrt(3d)))
        MathUtilsDbl.eq(nextBlock.s, Math.sqrt(3d)*SIZE*SIZE*ONE*ONE/9d)
    }
}
