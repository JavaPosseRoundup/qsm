package org.freddy33.qsm.space

import org.freddy33.math.Coord4d
import org.freddy33.math.MathUtils
import org.freddy33.math.Triangle
import org.freddy33.math.Vector4d

/**
 * Date: 12/6/11
 * Time: 11:51 PM
 * @author Fred Simon
 */
public class SpaceTime {
    int initialRatio
    int currentTime = 0
    Map<Integer, Space> spaces = [:]
    List<Event> allEvents = []

    SpaceTime(int ratio) {
        initialRatio = ratio
        init()
    }

    SpaceTime(Closure firstSpace) {
        Space first = new Space(currentTime)
        spaces.put(0, first)
        firstSpace.call(first)
    }

    def init() {
        Space first = new Space(currentTime)
        spaces.put(0, first)
        addPhoton(first,
                new Coord4d(0d, 0d, 0d),
                new Vector4d(1d, 0d, 0d),
                new Vector4d(0d, 0d, 1d),
                (double) initialRatio)
    }

    def static addPhoton(Space space, Coord4d c, Vector4d v, Vector4d p, double size) {
        p = p.normalized()
        v = v.normalized()
        // v and p needs to be perpendicular so cross should be normalized sin(teta)= 1
        Vector4d py = v.cross(p)
        if (!MathUtils.eq(py.magSquared(), 1d)) {
            throw new IllegalArgumentException("Polarization $v and vector $v are not perpendicular!");
        }
        space.addEvent(c, v)
        space.addEvent(c + (p * size), v)
        Vector4d cos120 = p * (size * MathUtils.cos120)
        Vector4d sin120 = py * (size * MathUtils.sin120)
        space.addEvent(c + cos120 + sin120, v)
        space.addEvent(c + cos120 - sin120, v)
    }

    Space addTime() {
        Space previous = spaces[currentTime]
        currentTime++
        Space newSpace = new Space(currentTime)
        spaces.put(currentTime, newSpace)
        if (previous != null && !previous.events.isEmpty()) allEvents.addAll(previous.events)
        return newSpace
    }

    List<Coord4d> currentPoints() {
        return allEvents.findAll { !it.used }.collect { it.point }
    }
}

class Event {
    final Coord4d point
    final Vector4d direction
    int time
    boolean used = false

    Event(double x, double y, double z, int time, Vector4d dir) {
//        this.point = new Coord4d((double)round(x),(double)round(y),(double)round(z))
        this.point = new Coord4d(x, y, z)
        this.time = time
        this.direction = dir.normalized()
    }

    Event(Coord4d point, int time, Vector4d dir) {
        this(point.x, point.y, point.z, time, dir)
    }
}

class Space {
    int time
    List<Event> events = []

    Space(int time) {
        this.time = time
    }

    def addEvent(Coord4d point, Vector4d direction) {
        def res = new Event(point, time, direction)
        events.add(res)
        return res
    }
}

class Calculator {
    static int N = 4

    List<Coord4d> fixedPoints
    SpaceTime spaceTime

    Calculator(int ratio) {
        spaceTime = new SpaceTime(ratio)
        initFixPoints()
    }

    Calculator(SpaceTime spaceTime) {
        this.spaceTime = spaceTime
        initFixPoints()
    }

    def initFixPoints() {
        fixedPoints = [
                new Coord4d(-50d, -50d, -50d, 0d) * ((double) spaceTime.initialRatio),
                new Coord4d(50d, 50d, 50d, 0d) * ((double) spaceTime.initialRatio)
        ]
    }

    def manyCalc(int n) {
        for (int i = 0; i < n; i++) calc()
    }

    def calc() {
        if (spaceTime.currentTime % spaceTime.initialRatio == 0) println "Current time: ${spaceTime.currentTime}"
        print "."
        Space newSpace = spaceTime.addTime()
        // Go back in time one by one and find all group of N
        for (int dt = 1; dt <= spaceTime.currentTime; dt++) {
            List<Event> events = spaceTime.spaces[spaceTime.currentTime - dt]?.events
            if (events == null || events.size() < N) continue
            forAllN(events, 0, []) { List<Event> block ->
                // For all N blocks try to find new events
                Set<List<Event>> subsequences = block.subsequences()
                Set<List<Event>> allPairs = subsequences.findAll { it.size() == 2 }
                // If any 2 points of the blocks could not have dt time to join => toFar
                boolean toFar = allPairs.any { it[0].point.d(it[1].point) > dt }
                // Global dir of the block is the sum of directions vectors
                Vector4d blockDirection = new Vector4d(0d, 0d, 0d)
                block.each { blockDirection.addSelf(it.direction) }
                if (!toFar && !MathUtils.eq(blockDirection.magSquared(), 0d)) {
                    blockDirection = blockDirection.normalized();
                    Set<List<Event>> allTriangles = subsequences.findAll { it.size() == 3 }
                    List<Event> newEvents = []
                    allTriangles.each {
                        // For each triangle find the equidistant ( = dt ) points
                        def tr = new Triangle(it[0].point, it[1].point, it[2].point)
                        Coord4d newPoint = tr.findEvent(dt, blockDirection)
                        if (newPoint != null) newEvents.add(new Event(newPoint, spaceTime.currentTime, tr.finalDir(blockDirection)))
                    }
                    if (newEvents.size() == block.size()) {
                        // Conservation of events good
                        newSpace.events.addAll(newEvents)
                        block.each { it.used = true }
                    }
                }
            }
        }
        // Clean all used events
        spaceTime.spaces.each { dt, Space space ->
            space.events.removeAll { it.used }
        }
        // Clean all used spaces
        spaceTime.spaces = spaceTime.spaces.findAll { !it.value.events.isEmpty() }
    }

    static def forAllN(List<Event> evtList, int idx, List<Event> block, Closure doStuff) {
        evtList.each { Event evt ->
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
