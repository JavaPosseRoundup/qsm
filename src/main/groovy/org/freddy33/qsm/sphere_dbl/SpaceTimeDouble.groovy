package org.freddy33.qsm.sphere_dbl

import org.freddy33.math.dbl.Point4d

import org.freddy33.math.dbl.TriangleDbl
import org.freddy33.math.dbl.Vector3d
import org.freddy33.qsm.sphere_surface_int.calc.EventDouble
import org.freddy33.math.dbl.MathUtilsDbl

/**
 * Date: 12/6/11
 * Time: 11:51 PM
 * @author Fred Simon
 */
public class SpaceTimeDouble {
    static int N = 4

    int fixPointRatio = 20
    int initialRatio
    int currentTime = 0
    List<Point4d> fixedPoints
    List<EventDouble> deadEvents = []
    List<EventDouble> activeEvents = []

    SpaceTimeDouble(int ratio) {
        initialRatio = ratio
        init()
    }

    def init() {
        fixedPoints = [
                new Point4d(-fixPointRatio, -fixPointRatio, -fixPointRatio, 0d) * ((double) initialRatio),
                new Point4d(fixPointRatio, fixPointRatio, fixPointRatio, 0d) * ((double) initialRatio)
        ]
        addPhoton(new Point4d(0d, 0d, 0d),
                new Vector3d(1d, 0d, 0d),
                new Vector3d(0d, 0d, 1d),
                (double) initialRatio)
    }

    def addPhoton(Point4d c, Vector3d v, Vector3d p, double size) {
        p = p.normalized()
        v = v.normalized()
        // v and p needs to be perpendicular so cross should be normalized sin(teta)= 1
        Vector3d py = v.cross(p)
        if (!MathUtilsDbl.eq(py.magSquared(), 1d)) {
            throw new IllegalArgumentException("Polarization $v and vector $v are not perpendicular!");
        }
        addEvent(c, v)
        addEvent(c + (p * size), v)
        Vector3d cos120 = p * (size * MathUtilsDbl.cos120)
        Vector3d sin120 = py * (size * MathUtilsDbl.sin120)
        addEvent(c + cos120 + sin120, v)
        addEvent(c + cos120 - sin120, v)
    }

    List<Point4d> currentPoints() {
        return deadEvents.findAll { it.point.t <= currentTime }.collect { it.point }
    }

    def addEvent(Point4d point, Vector3d direction) {
        def res = new EventDouble(point, direction)
        activeEvents.add(res)
        return res
    }

    def manyCalc(int n) {
        for (int i = 0; i < n; i++) calc()
    }

    def calc() {
        if (currentTime % initialRatio == 0) println "Current time: ${currentTime}, Active Events: ${activeEvents.size()}"
        print "."
        currentTime++
        List<EventDouble> newActiveEvents = []
        // For all active events find all the events closer than timePassed=(currentTime-evt.t)
        // And from the collection create events blocks of 4
        activeEvents.each { EventDouble event ->
            double timePassed = currentTime - event.point.t
            if (!event.used && timePassed > MathUtilsDbl.EPSILON) {
                double timePassedSquared = timePassed ** 2d
                List<EventDouble> events = activeEvents.findAll {
                    !it.used && it.point.t < currentTime && it.point.magSquared(event.point) <= timePassedSquared
                }
                // current event part of it
                forAllN(events, 0, []) { List<EventDouble> block ->
                    // For all N blocks try to find new events
                    Set<List<EventDouble>> subsequences = block.subsequences()
                    Set<List<EventDouble>> allPairs = subsequences.findAll { it.size() == 2 }
                    // If any 2 points of the blocks could not have dt time to join => toFar
                    boolean toFar = allPairs.any { it[0].point.magSquared(it[1].point) > timePassedSquared }
                    // Global dir of the block is the sum of directions vectors
                    Vector3d blockDirection = new Vector3d(0d, 0d, 0d)
                    block.each { blockDirection.addSelf(it.direction) }
                    if (!toFar && !MathUtilsDbl.eq(blockDirection.magSquared(), 0d)) {
                        blockDirection = blockDirection.normalized();
                        Set<List<EventDouble>> allTriangles = subsequences.findAll { it.size() == 3 }
                        List<EventDouble> newEvents = []
                        allTriangles.each {
                            // For each triangle find the equidistant ( = dt ) points
                            def tr = new TriangleDbl(it[0].point, it[1].point, it[2].point)
                            Point4d newPoint = tr.findEvent(blockDirection)
                            if (newPoint != null) newEvents.add(new EventDouble(newPoint, tr.finalDir(blockDirection)))
                        }
                        if (newEvents.size() == block.size()) {
                            // Conservation of events good
                            newActiveEvents.addAll(newEvents)
                            block.each { it.used = true }
                        }
                    }
                }
            }
        }

        // Clean all used events
        List<EventDouble> used = activeEvents.findAll { it.used }
        activeEvents.removeAll(used)
        deadEvents.addAll(used)
        activeEvents.addAll(newActiveEvents)
        true
    }

    static def forAllN(List<EventDouble> evtList, int idx, List<EventDouble> block, Closure doStuff) {
        evtList.each { EventDouble evt ->
            if (!evt.used && !block.contains(evt)) {
                block[idx] = evt
                idx++
                if (idx == N) {
                    doStuff(block)
                } else {
                    forAllN(evtList, idx, block, doStuff)
                }
            }
        }
    }
}
