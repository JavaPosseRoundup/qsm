package org.freddy33.qsm.space

import org.freddy33.math.MathUtils
import org.freddy33.math.Point4i
import org.freddy33.math.PolarVector3i
import org.freddy33.math.Vector3i

import static org.freddy33.math.PolarVector3i.DIV

/**
 * Date: 12/6/11
 * Time: 11:51 PM
 * @author Fred Simon
 */
public class SpaceTimeInt {
    static int N = 4
    public static final BigInteger fixPointRatio = 12G

    BigInteger initialRatio = 1G
    BigInteger currentTime = 0G
    List<Point4i> fixedPoints
    List<EventInt> deadEvents = []
    List<EventInt> activeEvents = []

    SpaceTimeInt(BigInteger ratio) {
        initialRatio = ratio
        init()
    }

    def init() {
        fixedPoints = [
                new Point4i(-fixPointRatio, -fixPointRatio, -fixPointRatio, 0G) * DIV * initialRatio,
                new Point4i(fixPointRatio, fixPointRatio, fixPointRatio, 0G) * DIV * initialRatio
        ]
        addPhoton(new Point4i(0G, 0G, 0G, 0G),
                new PolarVector3i(DIV, PolarVector3i.D90, 0G),
                new PolarVector3i(DIV, 0G, 0G),
                initialRatio * DIV)
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
        Vector3i cos120 = (p * (BigInteger) (size * MathUtils.cos120)).toCartesian()
        Vector3i sin120 = (py * (BigInteger) (size * MathUtils.sin120)).toCartesian()
        addEvent(c + cos120 + sin120, v)
        addEvent(c + cos120 - sin120, v)
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
        if (currentTime % DIV == 0) {
            print "."
        }
        if (currentTime % (initialRatio * DIV) == 0) {
            println ""
            println "Current time: ${currentTime} Active Events: ${activeEvents.size()} Dead Events: ${deadEvents.size()}"
        }
        currentTime += (BigInteger) DIV / 10G
        if (activeEvents.size() < N) {
            println "Your universe is dull :)"
            return
        }
        BigInteger smallestActiveTime = activeEvents[0].point.t
        BigInteger smallestDistance = activeEvents[0].point.magSquared(activeEvents[1].point)
        activeEvents.eachWithIndex { EventInt one, i ->
            if (one.point.t < smallestActiveTime) smallestActiveTime = one.point.t
            if (i < activeEvents.size() - 1) {
                for (j in (i + 1)..(activeEvents.size() - 1)) {
                    BigInteger d = one.point.magSquared(activeEvents[j].point)
                    if (d < smallestDistance) smallestDistance = d
                }
            }
        }
        if (currentTime < smallestActiveTime) {
            println "Jumping to ${smallestActiveTime} since nothing going on until"
            currentTime = smallestActiveTime
        }
        def maxDistance = currentTime - smallestActiveTime
        def maxDistance2 = maxDistance * maxDistance - smallestDistance
        if (maxDistance2 < 0G) {
            def toAdd = (BigInteger) Math.sqrt((double) -maxDistance2)
            println "Not enough time to communicate! Jumping to ${currentTime + toAdd} since nothing going on until"
            currentTime += toAdd
        }
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
                forAllN(events, 0, []) { List<EventInt> evts ->
                    // For all N blocks try to find new events
                    def block = new EventBlockInt(evts)
                    if (block.maxMagSquared() <= timePassedSquared) {
                        newActiveEvents.addAll(block.findNewEvents())
                    }
                }
            }
        }

        // Clean all used events
        List<EventInt> used = activeEvents.findAll { it.used }
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
