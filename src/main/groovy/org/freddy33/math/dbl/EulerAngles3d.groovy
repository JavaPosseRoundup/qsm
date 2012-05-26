package org.freddy33.math.dbl

import static java.lang.Math.cos
import static java.lang.Math.sin

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
class EulerAngles3d {
    final double phi, teta, psi
    final Matrix3d transform

    EulerAngles3d(SphericalUnitVector2d moment, double psi) {
        this(moment.phi, moment.teta, psi)
    }

    EulerAngles3d(double phi, double teta, double psi) {
        this.phi = phi
        this.teta = teta
        this.psi = psi
        this.transform = new Matrix3d(
                cos(teta) * cos(psi), -cos(phi) * sin(psi) + sin(phi) * sin(teta) * cos(psi), sin(phi) * sin(psi) + cos(phi) * sin(teta) * cos(psi),
                cos(teta) * sin(psi), cos(phi) * cos(psi) + sin(phi) * sin(teta) * sin(psi), -sin(phi) * cos(psi) + cos(phi) * sin(teta) * sin(psi),
                -sin(teta), sin(phi) * cos(teta), cos(phi) * cos(teta)
        )
    }
}
