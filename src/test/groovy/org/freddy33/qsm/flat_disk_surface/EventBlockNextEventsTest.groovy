package org.freddy33.qsm.flat_disk_surface

import org.freddy33.qsm.flat_disk_surface.calc.EventBlockDouble

import spock.lang.Specification
import org.freddy33.math.dbl.*

import static org.freddy33.math.bigInt.TrigoInt.ONE
import static org.freddy33.math.bigInt.TrigoInt.ONE_HALF
import org.freddy33.math.bigInt.MathUtilsInt

import static org.freddy33.math.bigInt.TrigoInt.trigMap

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 8:46 AM
 * To change this template use File | Settings | File Templates.
 */
class EventBlockNextEventsTest extends Specification {

    public static final BigInteger SIZE = 10000G
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
        int t = 0
        while (!MathUtilsDbl.eq(transformedBlock.s,firstBlock.s)) {
            transformedBlock = transformedBlock.elasticConstraint(firstBlock)
            if (transformedBlock == null) break
            t++
        }
        firstBlock.displayForElastic()
        println "$SIZE,$t"
        transformedBlock.displayForElastic()

        expect:
        MathUtilsDbl.eq(firstBlock.s, Math.sqrt(3d)*SIZE*SIZE*ONE*ONE)
        MathUtilsInt.almostEquals(nextBlock.o.createdTime, (BigInteger)(CREATION_TIME + (SIZE*ONE/Math.sqrt(3d))))
        MathUtilsDbl.eq(nextBlock.s, Math.sqrt(3d)*SIZE*SIZE*ONE*ONE/9d)
    }

    def "Function size elastic time"() {
        Map<BigInteger, Integer> elFromSize = getNumberOfStepsToPrecision(MathUtilsDbl.EPSILON)

        expect:
        elFromSize.size() == 20
        elFromSize.values().every { it == 240 }
    }

    def "Function steps per precision elastic"() {
        expect:
        def stepsToPrec = getNumberOfStepsToPrecision(precision)
        stepsToPrec.size() == 20
        stepsToPrec.values().every { it == steps }

        where:
        precision << [1e-4, 1e-5, 1e-6, 1e-7]
        steps << [159, 199, 240, 280]
    }

    private Map<BigInteger, Integer> getNumberOfStepsToPrecision(double precision) {
        Map<BigInteger, Integer> elFromSize = [:]
        (2G..21G).each { BigInteger size ->
            def firstBlock = EventBlockDouble.createElectron(
                    new Point3d(0d, 0d, 0d),
                    0G,
                    new SphericalUnitVector2d(new Vector3d(1d, 0d, 0d)),
                    0d,
                    size * size
            )
            def nextBlock = firstBlock.getImmediateNextBlock()
            def transformedBlock = nextBlock
            int t = 0
            while (!MathUtilsDbl.eq(transformedBlock.s, firstBlock.s, precision)) {
                transformedBlock = transformedBlock.elasticConstraint(firstBlock)
                if (transformedBlock == null) { println "equal"; break; }
                t++
            }
            elFromSize.put(size * size * ONE, t)
        }
        elFromSize
    }
}
