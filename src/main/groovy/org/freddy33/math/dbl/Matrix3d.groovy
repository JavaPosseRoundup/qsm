package org.freddy33.math.dbl

import static java.lang.Math.cos
import static java.lang.Math.sin

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 10:40 PM
 * To change this template use File | Settings | File Templates.
 */
class Matrix3d {
    double[][] m = new double[3][3]

    Matrix3d(double... pm) {
        m[0][0] = pm[0]; m[0][1] = pm[1]; m[0][2] = pm[2];
        m[1][0] = pm[3]; m[1][1] = pm[4]; m[1][2] = pm[5];
        m[2][0] = pm[6]; m[2][1] = pm[7]; m[2][2] = pm[8];
    }

    Point4d multiply(Point4d p) {
        new Point4d(
                m[0][0] * p.x + m[0][1] * p.y + m[0][2] * p.z,
                m[1][0] * p.x + m[1][1] * p.y + m[1][2] * p.z,
                m[2][0] * p.x + m[2][1] * p.y + m[2][2] * p.z,
                p.t
        )
    }

    Point3d multiply(Point3d p) {
        new Point3d(
                m[0][0] * p.x + m[0][1] * p.y + m[0][2] * p.z,
                m[1][0] * p.x + m[1][1] * p.y + m[1][2] * p.z,
                m[2][0] * p.x + m[2][1] * p.y + m[2][2] * p.z
        )
    }

    Matrix3d multiply(Matrix3d p) {
        new Matrix3d(
                m[0][0] * p.m[0][0] + m[0][1] * p.m[1][0] + m[0][2] * p.m[2][0],
                m[0][0] * p.m[0][1] + m[0][1] * p.m[1][1] + m[0][2] * p.m[2][1],
                m[0][0] * p.m[0][2] + m[0][1] * p.m[1][2] + m[0][2] * p.m[2][2],

                m[1][0] * p.m[0][0] + m[1][1] * p.m[1][0] + m[1][2] * p.m[2][0],
                m[1][0] * p.m[0][1] + m[1][1] * p.m[1][1] + m[1][2] * p.m[2][1],
                m[1][0] * p.m[0][2] + m[1][1] * p.m[1][2] + m[1][2] * p.m[2][2],

                m[2][0] * p.m[0][0] + m[2][1] * p.m[1][0] + m[2][2] * p.m[2][0],
                m[2][0] * p.m[0][1] + m[2][1] * p.m[1][1] + m[2][2] * p.m[2][1],
                m[2][0] * p.m[0][2] + m[2][1] * p.m[1][2] + m[2][2] * p.m[2][2]
        )
    }

    static Matrix3d rotX(double a) {
        new Matrix3d(
                1d,     0d,      0d,
                0d, cos(a), sin(a),
                0d, -sin(a),  cos(a)
        )
    }

    static Matrix3d rotY(double a) {
        new Matrix3d(
                 cos(a), 0d, -sin(a),
                     0d, 1d,      0d,
                 sin(a), 0d,  cos(a)
        )
    }

    static Matrix3d rotZ(double a) {
        new Matrix3d(
                 cos(a), sin(a), 0d,
                -sin(a), cos(a), 0d,
                     0d,     0d, 1d
        )
    }

    Matrix3d inverse() {
        // TODO: divide by det
        new Matrix3d(
                m[1][1]*m[2][2]-m[1][2]*m[2][1], m[0][2]*m[2][1]-m[0][1]*m[2][2], m[0][1]*m[1][2]-m[0][2]*m[1][1],
                m[1][2]*m[2][0]-m[1][0]*m[2][2], m[0][0]*m[2][2]-m[0][2]*m[2][0], m[0][2]*m[1][0]-m[0][0]*m[1][2],
                m[1][0]*m[2][1]-m[1][1]*m[2][0], m[2][0]*m[0][1]-m[0][0]*m[2][1], m[0][0]*m[1][1]-m[0][1]*m[1][0]
        )
    }

    @Override
    boolean equals(Object obj) {
        Matrix3d o = obj as Matrix3d
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!MathUtilsDbl.eq(m[i][j], o.m[i][j])) return false;
            }
        }
        return true;
    }
}
