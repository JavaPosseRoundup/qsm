package org.freddy33.qsm.space

import org.freddy33.math.Coord4d
import org.freddy33.math.Vector4d

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 4/7/12
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class Event {
    final Coord4d point
    final Vector4d direction
    final Sign sign
    boolean used = false

    Event(double x, double y, double z, double t, Vector4d dir) {
//        this.point = new Coord4d((double)round(x),(double)round(y),(double)round(z))
        this.point = new Coord4d(x, y, z, t)
        this.direction = dir.normalized()
    }

    Event(Coord4d point, Vector4d dir) {
        this(point.x, point.y, point.z, point.t, dir)
    }
}

class EventTriangle {
    Event e1, e2, e3
}

class EventBlock {
    Event e1, e2, e3, e4
}

enum Sign {
    plus, minus, plusI, minusI;

    Sign next(EventTriangle tr) {
        return plus
    }
}