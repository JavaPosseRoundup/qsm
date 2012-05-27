package org.freddy33.math.dbl

import static java.lang.Math.cos
import static java.lang.Math.sin
import static java.lang.Math.PI

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
class EulerAngles3d {
    final double phi, teta, psi
    final Matrix3d tra
    final Matrix3d traInv

    EulerAngles3d(SphericalUnitVector2d newZ, double psi) {
        // needs to add PI/2 to phi since z is transformed into Z=newZ
        this(newZ.phi + PI / 2d, newZ.teta, psi)
    }

    EulerAngles3d(double phi, double teta, double psi) {
        while (phi > PI) phi -= 2d * PI
        while (psi > PI) psi -= 2d * PI
        while (phi <= -PI) phi += 2d * PI
        while (psi <= -PI) psi += 2d * PI
        this.phi = phi
        this.teta = teta
        this.psi = psi
        validate()
        this.tra = getTramsformMatrix(teta, psi, phi)
        this.traInv = tra.inverse()
    }

    public static Matrix3d getTramsformMatrix(double teta, double psi, double phi) {
        Matrix3d.rotZ(psi) * Matrix3d.rotX(teta) * Matrix3d.rotZ(phi)
/*
                new Matrix3d(
                cos(teta) * cos(psi), -cos(phi) * sin(psi) + sin(phi) * sin(teta) * cos(psi), sin(phi) * sin(psi) + cos(phi) * sin(teta) * cos(psi),
                cos(teta) * sin(psi), cos(phi) * cos(psi) + sin(phi) * sin(teta) * sin(psi), -sin(phi) * cos(psi) + cos(phi) * sin(teta) * sin(psi),
                -sin(teta), sin(phi) * cos(teta), cos(phi) * cos(teta)
        )
*/
    }

    private validate() {
        if (phi <= -PI || phi > PI) throw new IllegalArgumentException("Euler angle phi not between -PI and PI! For $this")
        if (teta < 0G || teta > PI) throw new IllegalArgumentException("Euler angle teta not between 0 and PI! For $this")
        if (psi <= -PI || psi > PI) throw new IllegalArgumentException("Euler angle psi not between -PI and PI! For $this")
    }

    @Override
    String toString() {
        "ea($phi, $teta, $psi)"
    }

    @Override
    boolean equals(Object obj) {
        EulerAngles3d o =(EulerAngles3d)obj
        MathUtilsDbl.eq(phi, o.phi) && MathUtilsDbl.eq(teta, o.teta) && MathUtilsDbl.eq(psi, o.psi)
    }
}
