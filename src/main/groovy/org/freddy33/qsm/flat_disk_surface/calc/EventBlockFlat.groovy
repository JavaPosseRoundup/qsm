package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.dbl.Point4d
import org.freddy33.math.dbl.SphericalUnitVector2d

import org.freddy33.math.bigInt.Point2i
import org.freddy33.math.bigInt.Triangle2i

import org.freddy33.math.dbl.EulerAngles3d
import org.freddy33.math.dbl.Point3d

import static org.freddy33.math.bigInt.MathUtilsInt.ONE
import static org.freddy33.math.bigInt.MathUtilsInt.ONE_HALF
import org.freddy33.math.dbl.Line4d
import org.freddy33.math.dbl.Point2d
import org.freddy33.math.dbl.Triangle2d
import org.freddy33.math.dbl.MathUtilsDbl

import static org.freddy33.math.dbl.MathUtilsDbl.sin60

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventBlockFlat {
    final Point4d origin
    final BigInteger createdTime
    final SphericalUnitVector2d moment
    final EulerAngles3d plane
    final EventFlat[] events = new EventFlat[4]
    final Triangle2d[] triangles = new Triangle2d[4]
    final double s
    final WaitingEventShape shape = WaitingEventShape.TRIANGLE

    public EventBlockFlat(Point3d o, BigInteger time, SphericalUnitVector2d moment, double psi, Point2d[] points) {
        this.origin = new Point4d(o.x, o.y, o.z, (double)time)
        this.createdTime = time
        this.moment = moment
        this.plane = new EulerAngles3d(moment, psi)
        for (int i = 0; i < points.length; i++) {
            events[i] = new EventFlat(points[i], this)
        }
        triangles[0] = new Triangle2d(points[0], points[1], points[2])
        triangles[1] = new Triangle2d(points[0], points[1], points[3])
        triangles[2] = new Triangle2d(points[0], points[2], points[3])
        triangles[3] = new Triangle2d(points[1], points[2], points[3])
        s = triangles.sum(0d) { Triangle2d t -> t.s }
    }

    public static EventBlockFlat createPhoton(Point3d origin, BigInteger time, SphericalUnitVector2d k, double psi, BigInteger size) {
        Point2d[] pts = new Point2d[4]
        pts[0] = new Point2d(0d, 0d)
        pts[1] = new Point2d(0d, (double)size * ONE)
        pts[2] = new Point2d((double)size * ONE * sin60, (double)-size * ONE_HALF)
        pts[3] = new Point2d((double)-size * ONE * sin60, (double)-size * ONE_HALF)
        new EventBlockFlat(origin, time, k, psi, pts)
    }

    public static EventBlockFlat createElectron(Point3d origin, BigInteger time, SphericalUnitVector2d k, double psi, BigInteger size) {
        Point2d[] pts = new Point2d[4]
        pts[0] = new Point2d(0d, (double)size * ONE_HALF)
        pts[1] = new Point2d(0d, (double)-size * ONE_HALF)
        pts[2] = new Point2d((double)size * ONE * sin60, 0d)
        pts[3] = new Point2d((double)-size * ONE * sin60, 0d)
        new EventBlockFlat(origin, time, k, psi, pts)
    }

    List<Point4d> getEventPoints() {
        events.collect {
            origin + (plane.traInv * new Point4d(it.point.x, it.point.y, 0d, 0d))
        }
    }

    List<Line4d>[] getWaitingEvents(BigInteger currentTime) {
        List<Line4d>[] result = new List<Line4d>[4]
        for (int i = 0; i < events.length; i++) {
            EventFlat evt = events[i];
            result[i] = evt.getAllWaitingEvents(currentTime, shape)
        }
        result
    }

    @Override
    String toString() {
        "ebf($origin, $moment, [${events.join(", ")}] )"
    }
}
