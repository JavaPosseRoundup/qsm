package org.freddy33.math.dbl

import spock.lang.Specification

import static java.lang.Math.PI

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/27/12
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
class Matrix3dTest extends Specification {

    def "test euler angles z to X"() {
        // Moment on pure X, so z -> x
        def angles3d = new EulerAngles3d(PI/2d, PI/2d, 0d)

        expect:
        ptsOut == angles3d.tra * ptsIn
        ptsIn == angles3d.traInv * ptsOut

        where:
        ptsIn << [
                new Point3d(0d, 0d, 1d),
                new Point3d(1d, 0d, 0d),
                new Point3d(0d, 1d, 0d),
                new Point3d(0d, 1d, -1.5d),
                new Point3d(0d, -1d, -1.5d),
                new Point3d(5d, -1d, 0.5d),
                new Point3d(5d, -1d, -0.5d),
        ]
        ptsOut << [
                new Point3d(0d, 1d, 0d),
                new Point3d(0d, 0d, 1d),
                new Point3d(1d, 0d, 0d),
                new Point3d(1d, -1.5d, 0d),
                new Point3d(-1d, -1.5d, 0d),
                new Point3d(-1d, 0.5d, 5d),
                new Point3d(-1d, -0.5d, 5d),
        ]
    }

    def "test euler angles moment vectors"() {
        expect:

        where:
        fromMomentum << [
                new EulerAngles3d(new SphericalUnitVector2d(new Vector3d(1d, 0d, 0d)), 0d),
                new EulerAngles3d(new SphericalUnitVector2d(new Vector3d(0d, 1d, 0d)), 0d),
                new EulerAngles3d(new SphericalUnitVector2d(new Vector3d(0d, 0d, 1d)), 0d),
        ]
        resulting << [
                new EulerAngles3d(PI/2d, PI/2d, 0d),
                new EulerAngles3d(PI, PI/2d, 0d),
                new EulerAngles3d(0d, 0d, 0d),
        ]
    }
}