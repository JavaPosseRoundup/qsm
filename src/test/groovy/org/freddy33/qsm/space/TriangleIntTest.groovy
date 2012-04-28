package org.freddy33.qsm.space;


import org.freddy33.math.MathUtils
import org.freddy33.math.Point4i
import org.freddy33.math.SphericalVector3i
import org.freddy33.math.Vector3i
import spock.lang.Specification

import static org.freddy33.math.SphericalVector3i.DIV

public class TriangleIntTest extends Specification {
    public static final BigInteger sin120 = SphericalVector3i.sin(SphericalVector3i.D30 * 4G)
    public static final BigInteger cos120 = SphericalVector3i.cos(SphericalVector3i.D30 * 4G)
    public static final BigInteger sin45 = SphericalVector3i.sin(SphericalVector3i.D45)

    static def evt1 = new EventInt(new Point4i(0G, 0G, 0G, 3G), new SphericalVector3i(3G * DIV, SphericalVector3i.D90, 0G))
    static def evt2 = new EventInt(new Point4i(0G, 0G, DIV, 3G), new SphericalVector3i(3G * DIV, SphericalVector3i.D90, SphericalVector3i.D45))
    static def evt3 = new EventInt(new Point4i(0G, sin120, cos120, 3G), evt1.dir)
    static def evt4 = new EventInt(new Point4i(0G, -sin120, cos120, 3G), evt1.dir)

    static def ratio = 100G
    static def bigEvt2 = new EventInt(evt2.point * ratio, evt2.dir)
    static def bigEvt3 = new EventInt(evt3.point * ratio, evt1.dir)
    static def bigEvt4 = new EventInt(evt4.point * ratio, evt1.dir)

    def "test Event"() {
        expect:
        event.dir.isNormalized()
        event.dir == dir

        where:
        event << [
                evt1,
                evt2,
                evt3,
                new EventInt(new Point4i(0G, 0G, 0G, 3G), new SphericalVector3i(5G * DIV, SphericalVector3i.D45, SphericalVector3i.D45))
        ]
        dir << [
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, SphericalVector3i.D45),
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D45, SphericalVector3i.D45)
        ]
        dirVect << [
                new Vector3i(DIV, 0G, 0G),
                new Vector3i(sin45, sin45, 0G),
                new Vector3i(DIV, 0G, 0G),
                new Vector3i((BigInteger) sin45 * sin45 / DIV, (BigInteger) sin45 * sin45 / DIV, sin45)
        ]
    }

    def "test Normalized DIV Triangles"() {
        expect:
        !triangle.isFlat()
        MathUtils.almostEquals(triangle.findCenter(), center)
        triangle.fDir == dir || triangle.fDir == -dir
        MathUtils.almostEquals(triangle.radius2(), DIV * DIV)

        where:
        triangle << [
                new EventTriangleInt(evt1, evt2, evt3, null),
                new EventTriangleInt(evt1, evt2, evt4, null),
                new EventTriangleInt(evt1, evt3, evt4, null),
                new EventTriangleInt(evt2, evt3, evt4, null)
        ]
        dir << [
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G)
        ]
        center << [
                new Point4i(0G, sin120, -cos120, 3G),
                new Point4i(0G, -sin120, -cos120, 3G),
                new Point4i(0G, 0G, -DIV, 3G),
                new Point4i(0G, 0G, 0G, 3G)
        ]
    }

    def "test Big Triangles"() {
        expect:
        !triangle.isFlat()
        MathUtils.almostEquals(triangle.findCenter(), center)
        triangle.fDir == dir || triangle.fDir == -dir
        MathUtils.almostEquals(triangle.radius2(), DIV * DIV * ratio * ratio)

        where:
        triangle << [
                new EventTriangleInt(evt1, bigEvt2, bigEvt3, null),
                new EventTriangleInt(evt1, bigEvt2, bigEvt4, null),
                new EventTriangleInt(evt1, bigEvt3, bigEvt4, null),
                new EventTriangleInt(bigEvt2, bigEvt3, bigEvt4, null)
        ]
        dir << [
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G),
                new SphericalVector3i(DIV, SphericalVector3i.D90, 0G)
        ]
        center << [
                new Point4i(0G, sin120, -cos120, 3G) * ratio,
                new Point4i(0G, -sin120, -cos120, 3G) * ratio,
                new Point4i(0G, 0G, -DIV, 3G) * ratio,
                new Point4i(0G, 0G, MathUtils.EPSILON_INT, 3G)
        ]
    }
}
