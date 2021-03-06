package org.freddy33.qsm.sphere_surface_int

import spock.lang.Specification

import static org.freddy33.qsm.sphere_surface_int.TriangleIntTest.sin120
import static org.freddy33.qsm.sphere_surface_int.SpaceTimeIntTest.findNextGoodCalc
import org.freddy33.qsm.sphere_surface_int.calc.SpaceTimeInt
import org.freddy33.math.bigInt.MathUtilsInt

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 4/7/12
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
class PhotonStabilityIntTest extends Specification {
    static BigInteger ratio = 100G
    static BigInteger bigDist = ratio * sin120 * 2G
    public static final int ITERATIONS = 100

    def "test D value photon long time"() {
        def st = new SpaceTimeInt(ratio)
        List<BigInteger> sumMagSq = []
        st.initPhoton(ratio)
        for (int i = 1; i < ITERATIONS; i++) {
            findNextGoodCalc(st, bigDist * i, false)
            def block = st.activeEvents[0].createdByBlock
            println "$i, ${block.deltaTime}, ${block.totalSurface16Squared}"
            if (i == 1 || i == ITERATIONS-1) sumMagSq.add(block.totalSurface16Squared)
        }

        expect:
        MathUtilsInt.almostEquals(sumMagSq[0], sumMagSq[1])
    }

/*
    def "test D value single electron long time"() {
        def st = new SpaceTimeInt(ratio)
        st.initElectron(ratio)
        for (int i = 1; i < ITERATIONS; i++) {
            findNextGoodCalc(st, bigDist * i, false)
            def block = st.activeEvents[0].createdByBlock
            println "$i, ${block.deltaTime}, ${block.sumMagSquared}"
        }
    }
*/

}
