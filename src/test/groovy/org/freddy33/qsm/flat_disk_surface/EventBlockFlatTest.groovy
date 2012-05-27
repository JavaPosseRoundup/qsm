package org.freddy33.qsm.flat_disk_surface

import spock.lang.Specification
import org.freddy33.qsm.flat_disk_surface.calc.EventBlockFlat
import org.freddy33.math.dbl.Point3d
import org.freddy33.math.dbl.SphericalUnitVector2d
import org.freddy33.math.dbl.Vector3d
import org.freddy33.math.dbl.MathUtilsDbl

import static java.lang.Math.PI

import org.freddy33.math.dbl.Point4d

import static org.freddy33.math.bigInt.MathUtilsInt.ONE
import static org.freddy33.math.bigInt.MathUtilsInt.ONE_HALF
import org.freddy33.math.dbl.Line4d

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 8:46 AM
 * To change this template use File | Settings | File Templates.
 */
class EventBlockFlatTest extends Specification {

    public static final BigInteger SIZE = 10G
    public static final BigInteger CREATION_TIME = 7G
    public static final double one = (double) SIZE * ONE
    public static final double oneHalf = (double) SIZE * ONE_HALF

    def "simple flat photon"() {
        def photon = EventBlockFlat.createPhoton(
                new Point3d(0d,0d,0d),
                CREATION_TIME,
                new SphericalUnitVector2d(new Vector3d(1d,0d,0d)),
                0d,
                SIZE
        )

        expect:
        photon.events.length == 4
        photon.triangles.length == 4
        MathUtilsDbl.eq(photon.plane.phi, PI/2d)
        MathUtilsDbl.eq(photon.plane.teta, PI/2d)
        MathUtilsDbl.eq(photon.plane.psi, 0d)
        List<Point4d> pts = photon.getEventPoints()
        pts[0].equals(new Point4d(0d, 0d, 0d, (double)CREATION_TIME))
        pts[1].equals(new Point4d(0d, 0d, one, (double)CREATION_TIME))
        pts[2].equals(new Point4d(0d, oneHalf, -oneHalf, (double)CREATION_TIME))
        pts[3].equals(new Point4d(0d, -oneHalf, -oneHalf, (double)CREATION_TIME))
        BigInteger oneTime = CREATION_TIME + 1G
        List<Line4d>[] wPts1 = photon.getWaitingEvents(oneTime)
        wPts1.length == 4
        wPts1.every { it.size() == 4 }
        wPts1[0].any { it.a.equals(new Point4d(1d,  1d,  0d, (double)oneTime)) }
        wPts1[0].any { it.a.equals(new Point4d(1d, -1d,  0d, (double)oneTime)) }
        wPts1[0].any { it.a.equals(new Point4d(1d,  0d,  1d, (double)oneTime)) }
        wPts1[0].any { it.a.equals(new Point4d(1d,  0d, -1d, (double)oneTime)) }

        wPts1[1].any { it.a.equals(new Point4d(1d,  1d, one, (double)oneTime)) }
        wPts1[1].any { it.a.equals(new Point4d(1d, -1d, one, (double)oneTime)) }
        wPts1[1].any { it.a.equals(new Point4d(1d,  0d, one+1d, (double)oneTime)) }
        wPts1[1].any { it.a.equals(new Point4d(1d,  0d, one-1d, (double)oneTime)) }

        wPts1[2].any { it.a.equals(new Point4d(1d, oneHalf+1d, -oneHalf, (double)oneTime)) }
        wPts1[2].any { it.a.equals(new Point4d(1d, oneHalf-1d, -oneHalf, (double)oneTime)) }
        wPts1[2].any { it.a.equals(new Point4d(1d, oneHalf, -oneHalf+1d, (double)oneTime)) }
        wPts1[2].any { it.a.equals(new Point4d(1d, oneHalf, -oneHalf-1d, (double)oneTime)) }

        wPts1[3].any { it.a.equals(new Point4d(1d, -oneHalf+1d, -oneHalf, (double)oneTime)) }
        wPts1[3].any { it.a.equals(new Point4d(1d, -oneHalf-1d, -oneHalf, (double)oneTime)) }
        wPts1[3].any { it.a.equals(new Point4d(1d,    -oneHalf, -oneHalf+1d, (double)oneTime)) }
        wPts1[3].any { it.a.equals(new Point4d(1d,    -oneHalf, -oneHalf-1d, (double)oneTime)) }

        BigInteger twoTime = CREATION_TIME + 2G
        List<Point4d>[] wPts2 = photon.getWaitingEvents(twoTime)
        wPts2.length == 4
        wPts2.every { it.size() == 8 }
        wPts2[0].any { it.equals(new Point4d(2d,  2d,  0d, (double)twoTime)) }
        wPts2[0].any { it.equals(new Point4d(2d, -2d,  0d, (double)twoTime)) }
        wPts2[0].any { it.equals(new Point4d(2d,  0d,  2d, (double)twoTime)) }
        wPts2[0].any { it.equals(new Point4d(2d,  0d, -2d, (double)twoTime)) }

        wPts2[0].any { it.equals(new Point4d(2d,  1d,  1d, (double)twoTime)) }
        wPts2[0].any { it.equals(new Point4d(2d, -1d,  1d, (double)twoTime)) }
        wPts2[0].any { it.equals(new Point4d(2d,  1d, -1d, (double)twoTime)) }
        wPts2[0].any { it.equals(new Point4d(2d, -1d, -1d, (double)twoTime)) }

        BigInteger twelveTime = CREATION_TIME + 12G
        List<Point4d>[] wPts12 = photon.getWaitingEvents(twelveTime)
        wPts12.length == 4
        wPts12.every { it.size() == 4*12 }
        wPts12[0].any { it.equals(new Point4d(12d,  12d,  0d, (double)twelveTime)) }
        wPts12[0].any { it.equals(new Point4d(12d, -12d,  0d, (double)twelveTime)) }
        wPts12[0].any { it.equals(new Point4d(12d,  0d,  12d, (double)twelveTime)) }
        wPts12[0].any { it.equals(new Point4d(12d,  0d, -12d, (double)twelveTime)) }

        wPts12[0].any { it.equals(new Point4d(12d,  1d,  11d, (double)twelveTime)) }
        wPts12[0].any { it.equals(new Point4d(12d, -1d,  11d, (double)twelveTime)) }
        wPts12[0].any { it.equals(new Point4d(12d,  11d, -1d, (double)twelveTime)) }
        wPts12[0].any { it.equals(new Point4d(12d, -11d, -1d, (double)twelveTime)) }

        wPts12[0].any { it.equals(new Point4d(12d,  6d,   6d, (double)twelveTime)) }
        wPts12[0].any { it.equals(new Point4d(12d, -6d,   6d, (double)twelveTime)) }
        wPts12[0].any { it.equals(new Point4d(12d,  6d,  -6d, (double)twelveTime)) }
        wPts12[0].any { it.equals(new Point4d(12d, -6d,  -6d, (double)twelveTime)) }
    }

    def "simple flat electron"() {
        def electron = EventBlockFlat.createElectron(
                new Point3d(0d,0d,0d),
                CREATION_TIME,
                new SphericalUnitVector2d(new Vector3d(1d,0d,0d)),
                0d,
                SIZE
        )

        expect:
        electron.events.length == 4
        electron.triangles.length == 4
        MathUtilsDbl.eq(electron.plane.phi, PI/2d)
        MathUtilsDbl.eq(electron.plane.teta, PI/2d)
        MathUtilsDbl.eq(electron.plane.psi, 0d)
        List<Point4d> pts = electron.getEventPoints()
        pts[0].equals(new Point4d(0d, 0d, oneHalf, (double)CREATION_TIME))
        pts[1].equals(new Point4d(0d, 0d, -oneHalf, (double)CREATION_TIME))
        pts[2].equals(new Point4d(0d, one, 0d, (double)CREATION_TIME))
        pts[3].equals(new Point4d(0d, -one, 0d, (double)CREATION_TIME))
        BigInteger oneTime = CREATION_TIME + 1G
        List<Point4d>[] wPts1 = electron.getWaitingEvents(oneTime)
        wPts1.length == 4
        wPts1.every { it.size() == 4 }
        wPts1[0].any { it.equals(new Point4d(1d,  1d,    oneHalf, (double)oneTime)) }
        wPts1[0].any { it.equals(new Point4d(1d, -1d,    oneHalf, (double)oneTime)) }
        wPts1[0].any { it.equals(new Point4d(1d,  0d, oneHalf+1d, (double)oneTime)) }
        wPts1[0].any { it.equals(new Point4d(1d,  0d, oneHalf-1d, (double)oneTime)) }

        wPts1[2].any { it.equals(new Point4d(1d, one+1d,  0d, (double)oneTime)) }
        wPts1[2].any { it.equals(new Point4d(1d, one-1d,  0d, (double)oneTime)) }
        wPts1[2].any { it.equals(new Point4d(1d,    one,  1d, (double)oneTime)) }
        wPts1[2].any { it.equals(new Point4d(1d,    one, -1d, (double)oneTime)) }
    }
}
