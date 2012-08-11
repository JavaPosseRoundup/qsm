package org.freddy33.qsm.flat_disk_surface

import spock.lang.Specification

import org.freddy33.math.dbl.Point3d
import org.freddy33.math.dbl.SphericalUnitVector2d
import org.freddy33.math.dbl.Vector3d
import org.freddy33.math.dbl.MathUtilsDbl

import static java.lang.Math.PI

import org.freddy33.math.dbl.Point4d

import static org.freddy33.math.bigInt.TrigoInt.ONE
import static org.freddy33.math.bigInt.TrigoInt.ONE_HALF
import org.freddy33.math.dbl.Line4d
import org.freddy33.qsm.flat_disk_surface.calc.BlockType
import org.freddy33.qsm.flat_disk_surface.calc.GlobalParams

import static java.lang.Math.sqrt
import org.freddy33.qsm.flat_disk_surface.calc.EventBlockDouble

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 8:46 AM
 * To change this template use File | Settings | File Templates.
 */
class EventBlockDoubleTest extends Specification {

    public static final BigInteger SIZE = 10G
    public static final BigInteger CREATION_TIME = 7G
    public static final double one = (double) SIZE * ONE
    public static final double oneHalf = (double) SIZE * ONE_HALF
    public static final double oneCos60 = (double) SIZE * ONE * MathUtilsDbl.cos60
    public static final double oneSin60 = (double) SIZE * ONE * MathUtilsDbl.sin60

    def "simple flat photon"() {
        def photon = EventBlockDouble.createPhoton(
                new Point3d(0d,0d,0d),
                CREATION_TIME,
                new SphericalUnitVector2d(new Vector3d(1d,0d,0d)),
                0d,
                SIZE
        )

        expect:
        photon.events.length == 4
        photon.triangles.length == 4
        photon.type == BlockType.FlatStar
        MathUtilsDbl.eq(photon.blockPlane.phi, PI/2d)
        MathUtilsDbl.eq(photon.blockPlane.teta, PI/2d)
        MathUtilsDbl.eq(photon.blockPlane.psi, 0d)
        List<Point4d> pts = photon.getEventPoints()
        pts[0].equals(new Point4d(0d, 0d, 0d, (double)CREATION_TIME))
        pts[1].equals(new Point4d(0d, 0d, one, (double)CREATION_TIME))
        pts[2].equals(new Point4d(0d, oneSin60, -oneCos60, (double)CREATION_TIME))
        pts[3].equals(new Point4d(0d, -oneSin60, -oneCos60, (double)CREATION_TIME))
        MathUtilsDbl.eq(new Vector3d(pts[0], pts[1]).d(), one)
        MathUtilsDbl.eq(new Vector3d(pts[0], pts[2]).d(), one)
        MathUtilsDbl.eq(new Vector3d(pts[0], pts[3]).d(), one)
        MathUtilsDbl.eq(new Vector3d(pts[1], pts[2]).d(), one * Math.sqrt(3))
        MathUtilsDbl.eq(new Vector3d(pts[1], pts[3]).d(), one * Math.sqrt(3))
        MathUtilsDbl.eq(new Vector3d(pts[2], pts[3]).d(), one * Math.sqrt(3))
    }

    private boolean verifyByWaitingTime(EventBlockDouble photon, BigInteger waitingTime) {
        BigInteger newTime = CREATION_TIME + waitingTime
        List<Line4d>[] wes = photon.getWaitingEvents(newTime)
        wes.length == 4
        wes.every { it.size() == 4 }
        double nz = GlobalParams.K_BOSON * waitingTime
        double wt = (double)waitingTime
        double nt = (double)newTime

        wes[0].any { it.a.equals(new Point4d(nz, wt,  0d, nt)) }
        wes[0].any { it.a.equals(new Point4d(nz, -wt,  0d, (double)newTime)) }
        wes[0].any { it.a.equals(new Point4d(nz,  0d,  wt, (double)newTime)) }
        wes[0].any { it.a.equals(new Point4d(nz,  0d, -wt, (double)newTime)) }

        wes[1].any { it.a.equals(new Point4d(nz,  1d, one, (double)newTime)) }
        wes[1].any { it.a.equals(new Point4d(1d, -1d, one, (double)newTime)) }
        wes[1].any { it.a.equals(new Point4d(1d,  0d, one+1d, (double)newTime)) }
        wes[1].any { it.a.equals(new Point4d(1d,  0d, one-1d, (double)newTime)) }

        wes[2].any { it.a.equals(new Point4d(1d, oneHalf+1d, -oneHalf, (double)newTime)) }
        wes[2].any { it.a.equals(new Point4d(1d, oneHalf-1d, -oneHalf, (double)newTime)) }
        wes[2].any { it.a.equals(new Point4d(1d, oneHalf, -oneHalf+1d, (double)newTime)) }
        wes[2].any { it.a.equals(new Point4d(1d, oneHalf, -oneHalf-1d, (double)newTime)) }

        wes[3].any { it.a.equals(new Point4d(1d, -oneHalf+1d, -oneHalf, (double)newTime)) }
        wes[3].any { it.a.equals(new Point4d(1d, -oneHalf-1d, -oneHalf, (double)newTime)) }
        wes[3].any { it.a.equals(new Point4d(1d,    -oneHalf, -oneHalf+1d, (double)newTime)) }
        wes[3].any { it.a.equals(new Point4d(1d,    -oneHalf, -oneHalf-1d, (double)newTime)) }
    }

    def "simple flat electron"() {
        def electron = EventBlockDouble.createElectron(
                new Point3d(0d,0d,0d),
                CREATION_TIME,
                new SphericalUnitVector2d(new Vector3d(1d,0d,0d)),
                0d,
                SIZE
        )

        expect:
        electron.events.length == 4
        electron.triangles.length == 4
        MathUtilsDbl.eq(electron.blockPlane.phi, PI/2d)
        MathUtilsDbl.eq(electron.blockPlane.teta, PI/2d)
        MathUtilsDbl.eq(electron.blockPlane.psi, 0d)
        List<Point4d> pts = electron.getEventPoints()
        pts[0].equals(new Point4d(oneHalf * MathUtilsDbl.sin45, oneCos60, 0d, (double)CREATION_TIME))
        pts[1].equals(new Point4d(oneHalf * MathUtilsDbl.sin45, -oneCos60, 0d, (double)CREATION_TIME))
        pts[2].equals(new Point4d(-oneHalf * MathUtilsDbl.sin45, 0d, oneCos60, (double)CREATION_TIME))
        pts[3].equals(new Point4d(-oneHalf * MathUtilsDbl.sin45, 0d, -oneCos60, (double)CREATION_TIME))
        MathUtilsDbl.eq(new Vector3d(pts[0], pts[1]).d(), one)
        MathUtilsDbl.eq(new Vector3d(pts[0], pts[2]).d(), one)
        MathUtilsDbl.eq(new Vector3d(pts[0], pts[3]).d(), one)
        MathUtilsDbl.eq(new Vector3d(pts[1], pts[2]).d(), one)
        MathUtilsDbl.eq(new Vector3d(pts[1], pts[3]).d(), one)
        MathUtilsDbl.eq(new Vector3d(pts[2], pts[3]).d(), one)
        MathUtilsDbl.eq(new Vector3d(pts[0], Point4d.origin()).d(), one*sqrt(3d/8d))
        MathUtilsDbl.eq(new Vector3d(pts[1], Point4d.origin()).d(), one*sqrt(3d/8d))
        MathUtilsDbl.eq(new Vector3d(pts[2], Point4d.origin()).d(), one*sqrt(3d/8d))
        MathUtilsDbl.eq(new Vector3d(pts[3], Point4d.origin()).d(), one*sqrt(3d/8d))
        BigInteger oneTime = CREATION_TIME + 1G
        List<Line4d>[] wPts1 = electron.getWaitingEvents(oneTime)
        wPts1.length == 4
    }
}
