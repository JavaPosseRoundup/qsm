package org.freddy33.math.dbl

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 8/4/12
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
class Quaternion {
    final double q1, q2, q3, q4

    Quaternion(def qs) {
        this.q1 = qs[0]
        this.q2 = qs[1]
        this.q3 = qs[2]
        this.q4 = qs[3]
    }

    Quaternion(Vector3d e, double teta) {
        def sinHalfTeta = Math.sin(teta / 2d)
        q1 = e.x* sinHalfTeta
        q2 = e.y* sinHalfTeta
        q3 = e.z* sinHalfTeta
        q4 = Math.cos(teta/2d)
    }

    Matrix3d getRotMatrix() {
        new Matrix3d(
                1d-2d*q2*q2-2d*q3*q3, 2d*(q1*q2-q3*q4),     2d*(q1*q3+q2*q4),
                2d*(q1*q2+q3*q4),     1d-2d*q1*q1-2d*q3*q3, 2d*(q2*q3-q1*q4),
                2d*(q1*q3-q2*q4),     2d*(q1*q4+q2*q3),     1d-2d*q1*q1-2d*q2*q2
        )
    }
}
