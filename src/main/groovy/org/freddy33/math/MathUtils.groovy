package org.freddy33.math

/**
 * Created by IntelliJ IDEA.
 * User: freds
 * Date: 2/10/12
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
class MathUtils {
    static float sin120 = (float) Math.sin(2 * Math.PI / 3)
    static float cos120 = -0.5f
    public static final double EPSILON = 9e-6d

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

    static boolean eq(Coord4d a, Coord4d b) {
        eq(a.x, b.x) && eq(a.y, b.y) && eq(a.z, b.z)
    }

    static boolean eq(Vector4d a, Vector4d b) {
        eq(a.x, b.x) && eq(a.y, b.y) && eq(a.z, b.z)
    }
}
