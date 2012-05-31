package org.freddy33.qsm.sphere_surface_int.calc

import org.freddy33.math.dbl.Point4d
import org.freddy33.math.dbl.Vector3d
import org.freddy33.math.bigInt.EventSign

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 4/7/12
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class EventDouble {
    final Point4d point
    final Vector3d direction
    final EventSign sign
    boolean used = false

    EventDouble(double x, double y, double z, double t, Vector3d dir) {
//        this.point = new Coord4d((double)round(x),(double)round(y),(double)round(z))
        this.point = new Point4d(x, y, z, t)
        this.direction = dir.normalized()
    }

    EventDouble(Point4d point, Vector3d dir) {
        this(point.x, point.y, point.z, point.t, dir)
    }
}

class EventTriangle {
    EventDouble e1, e2, e3
}

class EventBlock {
    EventDouble e1, e2, e3, e4
}

