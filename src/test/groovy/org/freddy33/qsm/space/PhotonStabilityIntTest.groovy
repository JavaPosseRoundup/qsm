package org.freddy33.qsm.space

import org.freddy33.math.MathUtils
import org.freddy33.math.Point4i
import spock.lang.Specification

import static org.freddy33.math.SphericalVector3i.ONE
import static org.freddy33.math.SphericalVector3i.ONE_HALF
import static org.freddy33.qsm.space.TriangleIntTest.cos120
import static org.freddy33.qsm.space.TriangleIntTest.sin120
import static org.freddy33.qsm.space.SpaceTimeIntTest.findNextGoodCalc

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
    public static final int ITERATIONS = 10000

    def "test D value photon long time"() {
        def st = new SpaceTimeInt(ratio)
        List<BigInteger> sumMagSq = []
        st.initPhoton(ratio)
        for (int i = 1; i < ITERATIONS; i++) {
            findNextGoodCalc(st, bigDist * i, false)
            def block = st.activeEvents[0].createdByBlock
            println "$i, ${block.deltaTime}, ${block.sumMagSquared}"
            if (i == 1 || i == ITERATIONS-1) sumMagSq.add(block.sumMagSquared)
        }

        expect:
        MathUtils.almostEquals(sumMagSq[0], sumMagSq[1])
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
