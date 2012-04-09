package org.freddy33.qsm.space

import org.freddy33.math.*

/**
 * Date: 12/6/11
 * Time: 11:51 PM
 * @author Fred Simon
 */
public class SpaceTimeInt {
    static int N = 4

    BigInteger initialRatio = PolarVector3i.DIV
    BigInteger currentTime = 0G
    List<Point4i> fixedPoints
    List<EventInt> deadEvents = []
    List<EventInt> activeEvents = []

    SpaceTimeInt(int ratio) {
        initialRatio = ratio
        init()
    }

    def init() {
        fixedPoints = [
                new Point4i(-50G, -50G, -50G, 0G) * initialRatio,
                new Point4i(50G, 50G, 50G, 0G) * initialRatio
        ]
        addPhoton(new Point4i(0G, 0G, 0G, 0G),
                new PolarVector3i(PolarVector3i.DIV, PolarVector3i.D90, 0G),
                new PolarVector3i(PolarVector3i.DIV, 0G, 0G),
                initialRatio)
    }

    def addPhoton(Point4i c, PolarVector3i v, PolarVector3i p, BigInteger size) {
        p = p.normalized()
        v = v.normalized()
        // v and p needs to be perpendicular so cross should be normalized sin(teta)= 1
        PolarVector3i py = v.cross(p)
        if (!py.isNormalized()) {
            throw new IllegalArgumentException("Polarization $v and vector $v are not perpendicular!");
        }
        addEvent(c, v)
        addEvent(c + (p * size), v)
        Vector3i cos120 = (p * (size << 1)).toCartesian()
        Vector3i sin120 = (py * (BigInteger) (size * MathUtils.sin120)).toCartesian()
        addEvent(c + cos120 + sin120, v)
        addEvent(c + cos120 - sin120, v)
    }

    List<Point4i> currentPoints() {
        return activeEvents.findAll { !it.used }.collect { it.point }
    }

    def addEvent(Point4i point, PolarVector3i direction) {
        def res = new EventInt(point, direction)
        activeEvents.add(res)
        return res
    }

    def manyCalc(int n) {
        for (int i = 0; i < n; i++) calc()
    }

    def calc() {
        if (currentTime % initialRatio == 0) println "Current time: ${currentTime}"
        print "."
        currentTime++
        List<EventInt> newActiveEvents = []
        // For all active events find all the events closer than timePassed=(currentTime-evt.t)
        // And from the collection create events blocks of 4
        activeEvents.each { EventInt event ->
            BigInteger timePassed = currentTime - event.point.t
            if (!event.used && timePassed > 0G) {
                BigInteger timePassedSquared = timePassed * timePassed
                List<EventInt> events = activeEvents.findAll {
                    !it.used && it.point.t == event.point.t && it.point.magSquared(event.point) <= timePassedSquared
                }
                // current event part of it
                forAllN(events, 0, []) { List<EventInt> block ->
                    // For all N blocks try to find new events
                    Set<List<EventInt>> subsequences = block.subsequences()
                    Set<List<EventInt>> allPairs = subsequences.findAll { it.size() == 2 }
                    // If any 2 points of the blocks could not have dt time to join => toFar
                    boolean toFar = allPairs.any { it[0].point.magSquared(it[1].point) > timePassedSquared }
                    if (!toFar) {
                        Set<List<EventInt>> allTriangles = subsequences.findAll { it.size() == 3 }
                        List<EventInt> newEvents = []
                        allTriangles.each {
                            // For each triangle find the equidistant ( = dt ) points
                            def tr = new EventTriangleInt(it[0], it[1], it[2])
                            Coord4d newPoint = tr.findEvent()
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

    static def forAllN(List<EventInt> evtList, int idx, List<EventInt> block, Closure doStuff) {
        evtList.each { EventInt evt ->
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
