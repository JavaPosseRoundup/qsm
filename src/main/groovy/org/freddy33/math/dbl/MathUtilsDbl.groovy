package org.freddy33.math.dbl

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
class MathUtilsDbl {
    // The basic block of trigo int

    // Also 1/2
    public static double sin30 = Math.cos(Math.PI / 6d)
    // Also sqrt(2)/2
    public static double sin45 = Math.sin(Math.PI / 4d)
    // Also sqrt(3)/2
    public static double sin60 = Math.sin(Math.PI / 3d)

    // TODO: Should not use theses
    public static double sin120 = Math.sin(2d * Math.PI / 3d)
    public static double cos120 = -0.5d
    public static final double EPSILON = 1e-6d

    static boolean eq(double a, double b) {
        if (b == a) return true
        if (b == 0d) {
            Math.abs(a) < EPSILON
        } else if (a == 0d) {
            Math.abs(b) < EPSILON
        } else {
            Math.abs(1.0d - (a / b)) < EPSILON
        }
    }

    static boolean eq(Point4d a, Point4d b) {
        eq(a.x, b.x) && eq(a.y, b.y) && eq(a.z, b.z) && Math.abs(a.t - b.t) < 1d
    }

    static boolean eq(Point3d a, Point3d b) {
        eq(a.x, b.x) && eq(a.y, b.y) && eq(a.z, b.z)
    }

    static boolean eq(Vector3d a, Vector3d b) {
        eq(a.x, b.x) && eq(a.y, b.y) && eq(a.z, b.z)
    }
}
