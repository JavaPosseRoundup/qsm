package org.freddy33.math.dbl

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
}
