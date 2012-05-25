package org.freddy33.qsm.sphere_surface_int.calc

import org.freddy33.math.bigInt.Point4i
import org.freddy33.math.bigInt.SphericalVector3i
import org.freddy33.math.bigInt.Vector3i

import static SphericalVector3i.ONE
import org.freddy33.math.MathUtils

/**
 * Date: 12/6/11
 * Time: 11:51 PM
 * @author Fred Simon
 */
public class SpaceTimeInt {
    static int N = 4
    public static final BigInteger fixPointRatio = 4G

    BigInteger initialRatio = 1G
    BigInteger currentTime = 0G
    List<Point4i> fixedPoints
    List<EventInt> deadEvents = []
    List<EventInt> activeEvents = []
    BigInteger smallestActiveTime
    BigInteger smallestMagSquared

    SpaceTimeInt(BigInteger ratio) {
        initialRatio = ratio
        fixedPoints = [
                new Point4i(-fixPointRatio, -fixPointRatio, -fixPointRatio, 0G) * ONE * initialRatio,
                new Point4i(fixPointRatio, fixPointRatio, fixPointRatio, 0G) * ONE * initialRatio
        ]
    }

    def initPhoton(BigInteger size) {
        addPhoton(new Point4i(0G, 0G, 0G, 0G),
                new SphericalVector3i(SphericalVector3i.D90, 0G),
                new SphericalVector3i(0G, 0G),
                size)
    }

    def initElectron(BigInteger size) {
        addElectron(new Point4i(0G, 0G, 0G, 0G),
                new SphericalVector3i(SphericalVector3i.D90, 0G),
                new SphericalVector3i(0G, 0G),
                size)
    }

    def addPhoton(Point4i c, SphericalVector3i v, SphericalVector3i p, BigInteger size) {
        if (!p.isNormalized()) {
            p = p.normalized()
        }
        if (!v.isNormalized()) {
            v = v.normalized()
        }
        // v and p needs to be perpendicular so cross should be normalized sin(teta)= 1
        SphericalVector3i py = v.cross(p)
        if (!py.isNormalized()) {
            throw new IllegalArgumentException("Polarization $v and vector $p are not perpendicular!");
        }
        addEvent(c, v)
        //Real size is in div units
        addEvent(c + (p * (size * ONE)), v)
        Vector3i cos120 = (p * (size * SphericalVector3i.cos(SphericalVector3i.D120))).toCartesian()
        Vector3i sin120 = (py * (size * SphericalVector3i.sin(SphericalVector3i.D120))).toCartesian()
        addEvent(c + cos120 + sin120, v)
        addEvent(c + cos120 - sin120, v)
    }

    def addElectron(Point4i c, SphericalVector3i v, SphericalVector3i p, BigInteger size) {
        if (!p.isNormalized()) {
            p = p.normalized()
        }
        if (!v.isNormalized()) {
            v = v.normalized()
        }
        // v and p needs to be perpendicular so cross should be normalized sin(teta)= 1
        SphericalVector3i py = v.cross(p)
        if (!py.isNormalized()) {
            throw new IllegalArgumentException("Polarization $v and vector $p are not perpendicular!");
        }
        // Multiply by D180 is like div by 2
        Vector3i midLine = (p * (size * SphericalVector3i.ONE_HALF)).toCartesian()
        // Sin 120 is sqrt(3)/2
        Vector3i sqrt3div2 = (py * (size * SphericalVector3i.sin(SphericalVector3i.D120))).toCartesian()

        addEvent(c - midLine, v)
        addEvent(c + midLine, v)
        addEvent(c + sqrt3div2, v)
        addEvent(c - sqrt3div2, v)
    }

    def addEvent(Point4i point, SphericalVector3i direction) {
        def res = new EventInt(point, direction)
        activeEvents.add(res)
        return res
    }

    def printDetails() {
        println ""
        println "Current time: ${currentTime} Active Events: ${activeEvents.size()} Dead Events: ${deadEvents.size()}"
        activeEvents.eachWithIndex {EventInt evt, i ->
            println "$i: $evt"
        }
    }

    CalcResult calc(boolean log) {
        if (log) printDetails()
        if (activeEvents.size() < N) {
            println "Your universe is dull :)"
            return CalcResult.dullUniverse
        }
        CalcResult calcResult = null
        if (smallestActiveTime == null) {
            smallestActiveTime = activeEvents[0].point.t
            smallestMagSquared = activeEvents[0].point.magSquared(activeEvents[1].point)
            activeEvents.eachWithIndex { EventInt one, i ->
                if (one.point.t < smallestActiveTime) smallestActiveTime = one.point.t
                if (i < activeEvents.size() - 1) {
                    for (j in (i + 1)..(activeEvents.size() - 1)) {
                        BigInteger d = one.point.magSquared(activeEvents[j].point)
                        if (d < smallestMagSquared) smallestMagSquared = d
                    }
                }
            }

            if (currentTime < smallestActiveTime) {
                if (log) println "Jumping to ${smallestActiveTime} since no event time until"
                currentTime = smallestActiveTime
                calcResult = CalcResult.changedTime
            }
            if (addTimeToReachSmallestDistance(smallestMagSquared, log)) {
                calcResult = CalcResult.changedTime
            }
        }
        if (calcResult == null) {
            calcResult = CalcResult.increment
            currentTime += 1G
        }

        BigInteger smallestBlockSize = null
        List<EventInt> newActiveEvents = []
        // For all active events find all the events closer than timePassed=(currentTime-evt.t)
        // And from the collection create events blocks of 4
        activeEvents.each { EventInt event ->
            BigInteger timePassed = currentTime - event.point.t
            if (!event.used && timePassed > 0G) {
                BigInteger timePassedSquared = timePassed * timePassed
                List<EventInt> events = activeEvents.findAll {
                    !it.used && it.point.t == event.point.t // && it.point.magSquared(event.point) <= timePassedSquared
                }
                // current event part of it
                forAllN(events, 0, []) { List<EventInt> evts ->
                    // For all N blocks try to find new events
                    def block = EventBlockInt.createBlock(evts)
                    if (block != null) {
                        if (block.isValid(timePassedSquared, log)) {
                            def newBlock = block.findNewEvents(log)
                            if (newBlock) {
                                newActiveEvents.addAll(newBlock.e)
                                calcResult = CalcResult.createdEvents
                            }
                        } else {
                            BigInteger maxTimeForBlock = MathUtils.max(block.maxMagSquared, block.maxRadius2)
                            if (smallestBlockSize == null || maxTimeForBlock < smallestBlockSize) {
                                smallestBlockSize = maxTimeForBlock
                            }
                        }
                    }
                }
            }
        }

        if (calcResult == CalcResult.createdEvents) {
            // Clean all used events
            List<EventInt> used = activeEvents.findAll { it.used }
            activeEvents.removeAll(used)
            deadEvents.addAll(used)
            activeEvents.addAll(newActiveEvents)
            if (log) printDetails()
            smallestActiveTime = null
            smallestMagSquared = null
        } else {
            if (smallestBlockSize != null) {
                if (addTimeToReachSmallestDistance(smallestBlockSize, log)) {
                    calcResult = CalcResult.changedTime
                }
            } else {
                println "We have a problem no active block found!"
                calcResult = CalcResult.dullUniverse
            }
        }
        return calcResult
    }

    private boolean addTimeToReachSmallestDistance(BigInteger foundSmallestMagSquared, boolean log) {
        BigInteger currentSmallestDistance = currentTime - smallestActiveTime
        BigInteger foundSmallestDistance = (BigInteger) Math.sqrt((double) foundSmallestMagSquared)
        if (currentSmallestDistance < foundSmallestDistance - 1G) {
            def toAdd = foundSmallestDistance - currentSmallestDistance - 1G
            if (log) println "Not enough time to communicate! Jumping to ${currentTime + toAdd} since nothing going on until"
            currentTime += toAdd
            true
        }
        false
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

enum CalcResult {
    dullUniverse, increment, changedTime, createdEvents
}