package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.dbl.MathUtilsDbl
import org.freddy33.math.dbl.Point4d
import org.freddy33.math.dbl.Quaternion
import org.freddy33.math.dbl.Vector3d

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 8/4/12
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
class PropagatingEvent {
    final Point4d origin
    final Vector3d moment
    final Vector3d psi
    final Vector3d z

    PropagatingEvent(Point4d origin, Vector3d moment, Vector3d psi) {
        if (!moment.isNormalized() || !psi.isNormalized()) {
            throw new IllegalArgumentException("moment=$moment or psi=$psi are not normalized!")
        }
        this.origin = origin
        this.moment = moment
        this.psi = psi
        this.z = moment.cross(psi)
        if (!z.isNormalized() || MathUtilsDbl.eq(moment.dot(psi), 0d)) {
            throw new IllegalArgumentException("moment=$moment or psi=$psi are not orthogonal!")
        }
    }

    PropagatingEvent[] nextEvents() {
        def de = Math.sqrt(2d / 3d)
        def dd = Math.sqrt(1d / 3d)
        Vector3d[] f = [
                moment * de + psi * dd,
                moment * de - psi * (dd / 2d) + z * 0.5d,
                moment * de - psi * (dd / 2d) - z * 0.5d
        ]
        def teta = Math.PI / 2
        Vector3d[] ds = [
                new Quaternion(f[0], teta).getRotMatrix() * f[0].cross(moment).normalized(),
                new Quaternion(f[1], teta).getRotMatrix() * f[1].cross(moment).normalized(),
                new Quaternion(f[2], teta).getRotMatrix() * f[2].cross(moment).normalized()
        ]
        [
                new PropagatingEvent(origin + f[0], f[0], ds[0]),
                new PropagatingEvent(origin + f[1], f[1], ds[1]),
                new PropagatingEvent(origin + f[2], f[2], ds[2])
        ]
    }

}

class PropagatingEvents {
    final PropagatingEvent first
    List<PropagatingEvent> events

    PropagatingEvents(PropagatingEvent first) {
        events = [ first ]
        this.first = first
    }

    void increment() {
        List<PropagatingEvent> nextEvents = []
        events.each {
            nextEvents.addAll(it.nextEvents())
        }
        events = nextEvents
    }
}