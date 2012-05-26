package org.freddy33.qsm.sphere_surface_int;


import org.freddy33.math.bigInt.Point4i
import org.freddy33.math.bigInt.SphericalVector3i
import org.freddy33.math.bigInt.Vector3i
import spock.lang.Specification

import static org.freddy33.math.bigInt.MathUtilsInt.almostEquals
import static org.freddy33.math.bigInt.SphericalVector3i.*
import org.freddy33.qsm.sphere_surface_int.calc.EventInt
import org.freddy33.qsm.sphere_surface_int.calc.EventTriangleInt

public class TriangleIntTest extends Specification {
    public static final BigInteger sin120 = SphericalVector3i.sin(D120)
    public static final BigInteger cos120 = SphericalVector3i.cos(D120)
    public static final BigInteger sin45 = SphericalVector3i.sin(D45)

    static def evt1 = new EventInt(new Point4i(0G, 0G, 0G, 3G), new SphericalVector3i(3G * ONE, D90, 0G))
    static def evt2 = new EventInt(new Point4i(0G, 0G, ONE, 3G), new SphericalVector3i(3G * ONE, D90, D45))
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
                new EventInt(new Point4i(0G, 0G, 0G, 3G), new SphericalVector3i(5G * ONE, D45, D45))
        ]
        dir << [
                new SphericalVector3i(D90, 0G),
                new SphericalVector3i(D90, D45),
                new SphericalVector3i(D90, 0G),
                new SphericalVector3i(D45, D45)
        ]
        dirVect << [
                new Vector3i(ONE, 0G, 0G),
                new Vector3i(sin45, sin45, 0G),
                new Vector3i(ONE, 0G, 0G),
                new Vector3i((BigInteger) sin45 * sin45 / ONE, (BigInteger) sin45 * sin45 / ONE, sin45)
        ]
    }

    def "test Normalized ONE Triangles"() {
        expect:
        !triangle.isFlat()
        almostEquals(triangle.findCenter(), center)
        triangle.fDir == dir || triangle.fDir == -dir
        almostEquals(triangle.radius2(), ONE * ONE)

        where:
        triangle << [
                new EventTriangleInt(evt1, evt2, evt3, null),
                new EventTriangleInt(evt1, evt2, evt4, null),
                new EventTriangleInt(evt1, evt3, evt4, null),
                new EventTriangleInt(evt2, evt3, evt4, null)
        ]
        dir << [
                new SphericalVector3i(D90, 0G),
                new SphericalVector3i(D90, 0G),
                new SphericalVector3i(D90, 0G),
                new SphericalVector3i(D90, 0G)
        ]
        center << [
                new Point4i(0G, sin120, -cos120, 3G),
                new Point4i(0G, -sin120, -cos120, 3G),
                new Point4i(0G, 0G, -ONE, 3G),
                new Point4i(0G, 0G, 0G, 3G)
        ]
    }

    def "test Big Triangles"() {
        expect:
        !triangle.isFlat()
        almostEquals(triangle.findCenter(), center, 100G)
        triangle.fDir == dir || triangle.fDir == -dir
        almostEquals(triangle.radius2(), ONE * ONE * ratio * ratio)

        where:
        triangle << [
                new EventTriangleInt(evt1, bigEvt2, bigEvt3, null),
                new EventTriangleInt(evt1, bigEvt2, bigEvt4, null),
                new EventTriangleInt(evt1, bigEvt3, bigEvt4, null),
                new EventTriangleInt(bigEvt2, bigEvt3, bigEvt4, null)
        ]
        dir << [
                new SphericalVector3i(D90, 0G),
                new SphericalVector3i(D90, 0G),
                new SphericalVector3i(D90, 0G),
                new SphericalVector3i(D90, 0G)
        ]
        center << [
                new Point4i(0G, sin120, -cos120, 3G) * ratio,
                new Point4i(0G, -sin120, -cos120, 3G) * ratio,
                new Point4i(0G, 0G, -ONE, 3G) * ratio,
                new Point4i(0G, 0G, 0G, 3G) * ratio
        ]
    }
}
