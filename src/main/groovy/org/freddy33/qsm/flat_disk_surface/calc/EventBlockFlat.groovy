package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.dbl.Point4d
import org.freddy33.math.dbl.SphericalUnitVector2d

import org.freddy33.math.bigInt.Point2i
import org.freddy33.math.bigInt.Triangle2i
import org.freddy33.math.bigInt.SphericalVector3i
import org.freddy33.math.dbl.EulerAngles3d
import org.freddy33.math.dbl.Point3d

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventBlockFlat {
    final Point3d origin
    final BigInteger createdTime
    final SphericalUnitVector2d moment
    final EulerAngles3d plane
    final EventFlat[] events = new EventFlat[4]
    final Triangle2i[] triangles = new Triangle2i[4]
    final BigInteger surfaces16Squared

    public EventBlockFlat(Point3d origin, BigInteger time, SphericalUnitVector2d moment, double psi, Point2i[] points) {
        this.origin = origin
        this.createdTime = time
        this.moment = moment
        this.plane = new EulerAngles3d(moment, psi)
        for (int i = 0; i < points.length; i++) {
            events[i] = new EventFlat(points[i], this)
        }
        triangles[0] = new Triangle2i(points[0], points[1], points[2])
        triangles[1] = new Triangle2i(points[0], points[1], points[3])
        triangles[2] = new Triangle2i(points[0], points[2], points[3])
        triangles[3] = new Triangle2i(points[1], points[2], points[3])
        surfaces16Squared = triangles.sum(0G) { Triangle2i t -> t.s16squared }
    }

    public static EventBlockFlat createPhoton(Point3d origin, BigInteger time, SphericalUnitVector2d k, double psi, BigInteger size) {
        Point2i[] pts = new Point2i[4]
        pts[0] = new Point2i(0G, 0G)
        pts[1] = new Point2i(0G, size * SphericalVector3i.ONE)
        pts[2] = new Point2i(size * SphericalVector3i.sin(SphericalVector3i.D30), -size * SphericalVector3i.ONE_HALF)
        pts[3] = new Point2i(size * SphericalVector3i.sin(SphericalVector3i.D120), -size * SphericalVector3i.ONE_HALF)
        new EventBlockFlat(origin, time, k, psi, pts)
    }

    public static EventBlockFlat createElectron(Point3d origin, BigInteger time, SphericalUnitVector2d k, double psi, BigInteger size) {
        Point2i[] pts = new Point2i[4]
        pts[0] = new Point2i(0G, size * SphericalVector3i.ONE_HALF)
        pts[1] = new Point2i(0G, -size * SphericalVector3i.ONE_HALF)
        // Sin 30 is sqrt(3)/2
        pts[2] = new Point2i(size * SphericalVector3i.sin(SphericalVector3i.D30), 0G)
        pts[3] = new Point2i(-size * SphericalVector3i.sin(SphericalVector3i.D30), 0G)
        new EventBlockFlat(origin, time, k, psi, pts)
    }

    public incrementTime() {
        events.each {
            it.incrementTime()
        }
    }


}
