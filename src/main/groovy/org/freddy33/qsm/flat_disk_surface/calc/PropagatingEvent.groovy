package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.dbl.*

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 8/4/12
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
class PropagatingEvent {
    // TODO: Change sign depending on origin color/sign
    public static final double TETA_ROT = Math.PI / 2

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
        if (!z.isNormalized() || !MathUtilsDbl.eq(moment.dot(psi), 0d)) {
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
        Vector3d[] ds = [
                new Quaternion(f[0], TETA_ROT).getRotMatrix() * f[0].cross(moment).normalized(),
                new Quaternion(f[1], TETA_ROT).getRotMatrix() * f[1].cross(moment).normalized(),
                new Quaternion(f[2], TETA_ROT).getRotMatrix() * f[2].cross(moment).normalized()
        ]
        [
                new PropagatingEvent(origin + f[0], f[0], ds[0]),
                new PropagatingEvent(origin + f[1], f[1], ds[1]),
                new PropagatingEvent(origin + f[2], f[2], ds[2])
        ]
    }

    @Override
    String toString() {
        return "pe($origin, $moment, $psi)"
    }
}

class PropagatingEvents implements Transformer3dto4d {
    final PropagatingEvent first
    List<PropagatingEvent> events

    PropagatingEvents(PropagatingEvent first) {
        this.events = [first]
        this.first = first
    }

    void increment() {
        List<PropagatingEvent> nextEvents = []
        events.each {
            nextEvents.addAll(it.nextEvents())
        }
        // Check mergeable set (if |ab| < 1 and |ac| < 1 => |bc| < 1)
        Map<PropagatingEvent, List<PropagatingEvent>> closeEvents = [:]
        nextEvents.each { PropagatingEvent a ->
            List<PropagatingEvent> closeToMe = nextEvents.findAll { it.origin.magSquared(a.origin) < (0.9d-MathUtilsDbl.EPSILON) }
            if (closeToMe.size() > 1) closeEvents.put(a, closeToMe)
        }
        println "Found close events $closeEvents"
        Set<PropagatingEvent> done = new HashSet<PropagatingEvent>()
        List<List<PropagatingEvent>> toMerge = new ArrayList<List<PropagatingEvent>>()
        closeEvents.keySet().each { PropagatingEvent a ->
            if (!done.contains(a)) {
                // All close events, should have the same list
                def closeToAEvents = closeEvents[a]
                closeToAEvents.each { PropagatingEvent b ->
                    def closeToBEvents = closeEvents[b]
                    if (closeToAEvents.size() != closeToBEvents.size() || !closeToAEvents.containsAll(closeToBEvents)) {
                        println "ERROR: Oups needs to thing since ${closeToAEvents} != ${closeToBEvents}"
                    } else {
                        done.addAll(closeToAEvents)
                        toMerge.add(closeToAEvents)
                    }
                }
            }
        }
        nextEvents.removeAll(done)
        toMerge.each { List<PropagatingEvent> merge ->
            Point4d barycenter = (merge.sum(new Point4d(0d,0d,0d,0d)) { it.origin }).div((double)merge.size())
            Vector3d momentAvg = (merge.sum(new Vector3d(0d,0d,0d)) { it.moment }).normalized()
            List<SphericalUnitVector2d> psis = merge.collect() { new SphericalUnitVector2d(it.psi) }
            Vector3d psiCrossAvg = SphericalUnitVector2d.middleMan(psis).toCartesian().cross(momentAvg)
            if (psiCrossAvg.magSquared() < MathUtilsDbl.EPSILON) {
                println "ERROR: What can I do?"
            } else {
                nextEvents << new PropagatingEvent(barycenter, momentAvg, new Quaternion(momentAvg, PropagatingEvent.TETA_ROT).getRotMatrix() * psiCrossAvg.normalized())
            }
        }
        events = nextEvents
    }

    @Override
    Point4d transformToGlobalCoordinates(Point3d p) {
        return new Point4d(p.x, p.y, p.z, 0d)
    }

    List<Line4d> getOriginalMoment(double size) {
        TransformerUtils.getLinesForMoment(
                new Point3d(first.origin.x, first.origin.y, first.origin.z),
                first.moment, size, this)
    }

    List<Line4d> getCurrentMoments(double size) {
        List<Line4d> res = []
        Transformer3dto4d transformer = this
        events.each { PropagatingEvent pe ->
            res.addAll(TransformerUtils.getLinesForMoment(
                    new Point3d(pe.origin.x, pe.origin.y, pe.origin.z),
                    pe.moment, size, transformer))
        }
        res
    }
}