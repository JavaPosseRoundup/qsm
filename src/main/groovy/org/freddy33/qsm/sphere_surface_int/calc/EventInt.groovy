package org.freddy33.qsm.sphere_surface_int.calc

import org.freddy33.math.bigInt.Point4i
import org.freddy33.math.bigInt.SphericalVector3i
import org.freddy33.math.bigInt.Vector3i

import org.freddy33.math.bigInt.MathUtilsInt
import org.freddy33.math.bigInt.Triangle4i
import org.freddy33.math.bigInt.EventSign

/**
 * User: freds
 * Date: 4/7/12
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class EventInt {
    public final EventBlockInt createdByBlock
    public final EventTriangleInt createdByTriangle
    public final Point4i point
    public final SphericalVector3i dir
    public final EventSign sign
    public boolean used = false

    EventInt(Point4i p, SphericalVector3i v) {
        this(p, v, null, null)
    }

    EventInt(Point4i p, SphericalVector3i v, EventBlockInt from, EventTriangleInt tr) {
        point = p
        if (v.isNormalized()) {
            dir = v
        } else {
            dir = v.normalized()
        }
        createdByBlock = from
        createdByTriangle = tr
    }

    @Override
    public String toString() {
        return "evt($point, $dir, used=$used)";
    }
}


class EventTriangleInt {
    public final EventBlockInt from
    public final EventInt[] e = new EventInt[3]
    public final Vector3i v12, v13, v23, v12v23cross
    public final SphericalVector3i fDir
    public final BigInteger p12p13cross22

    EventTriangleInt(EventInt e1, EventInt e2, EventInt e3, EventBlockInt from) {
        this.e[0] = e1
        this.e[1] = e2
        this.e[2] = e3
        this.from = from
        v12 = new Vector3i(e1.point, e2.point)
        v13 = new Vector3i(e1.point, e3.point)
        v23 = new Vector3i(e2.point, e3.point)
        v12v23cross = v12.cross(v23)
        p12p13cross22 = v12v23cross.magSquared() * 2G
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $p12p13cross22 is too small!
                                                  Cannot find final direction of a flat triangle!""")
        }
        // Final direction (perpendicular to triangle plane) is cross vector
        def fd = new SphericalVector3i(v12v23cross).normalized()
        if (from != null && ((fd % from.blockDir) < 0G)) {
            fDir = fd.negative()
        } else {
            fDir = fd
        }
    }

    EventInt e1() { e[0] }

    EventInt e2() { e[1] }

    EventInt e3() { e[2] }

    boolean isFlat() {
        p12p13cross22 == 0G
    }

    Point4i findEvent() {
        // OK, it's a non flat triangle and dt is big enough => find center
        Point4i center = findCenter()
        center.t += from.deltaTime
        // Then find how much above plane on center need to add
        BigInteger radius2 = radius2()
        if (from.maxMagSquared - radius2 < 1G) {
            // Still too flat
            return null
        } else {
            BigInteger abovePlane = Math.sqrt((double) (from.maxMagSquared - radius2))
            return center + (fDir * abovePlane)
        }
    }

    Point4i findCenter() {
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $p12p13cross22 is too small!
                                                   Cannot find center of a flat triangle!""")
        }
        // From "Barycentric coordinates from cross- and dot-products"
        BigInteger alpha = v23.magSquared() * (v12 % v13)
        BigInteger beta = v13.magSquared() * (-v12 % v23)
        BigInteger gama = v12.magSquared() * (-v13 % -v23)
        new Point4i(
                (BigInteger) ((alpha * e1().point.x + beta * e2().point.x + gama * e3().point.x) / p12p13cross22),
                (BigInteger) ((alpha * e1().point.y + beta * e2().point.y + gama * e3().point.y) / p12p13cross22),
                (BigInteger) ((alpha * e1().point.z + beta * e2().point.z + gama * e3().point.z) / p12p13cross22),
                // TODO: Need to decide on forced plane system => euclidian transformation to block coordinate where all points z=t=0
                e1().point.t
                //(BigInteger) (e1().point.t + e2().point.t + e3().point.t) / 3G
        )
    }

    BigInteger radius2() {
        (BigInteger) ((v12.magSquared() * v13.magSquared() * v23.magSquared()) / (2G * p12p13cross22))
    }

    @Override
    String toString() {
        "tr(${e.collect({it.point}).join(", ")}, ${fDir?.toCartesian()})"
    }
}

class EventBlockInt {
    public final EventInt[] e
    public final EventTriangleInt[] tr = new EventTriangleInt[4]
    public final SphericalVector3i blockDir
    public final BigInteger maxMagSquared
    public final BigInteger maxRadius2
    public final BigInteger totalSurface16Squared
    public final BigInteger deltaTime

    static EventBlockInt createBlock(List<EventInt> es) {
        if (es.size() != 4) {
            println "Managing only block of 4!"
            return null
        }
        // Same plane (in 4D), for now same time plane and then same 3D plane
        def currentTime = es[0].point.t
        if (es.any { it.point.t != currentTime }) {
            // Not same time
            return null
        }
        new EventBlockInt(es)
    }

    EventBlockInt(List<EventInt> es) {
        if (es.size() < 4) {
            // TODO: For 5 there is 12 solutions that create 5 triangles covering it all
            throw new IllegalArgumentException("Cannot make block with less than 4 events")
        }
        this.e = es.toArray(new EventInt[4])
        // Global dir of the block is the barycenter of directions vectors
        this.blockDir = SphericalVector3i.middleMan(es.collect { it.dir })
        this.maxMagSquared = calcMaxMagSquared(es.collect() { it.point })
        this.totalSurface16Squared = calcTotalSurface16Squared(es.collect() { it.point })
        this.deltaTime = (BigInteger) Math.sqrt((double) this.maxMagSquared)

        // For 4 it's all possible triangles
        // Writing the loop was longer ?!?
        tr[0] = new EventTriangleInt(e[0], e[1], e[2], this)
        tr[1] = new EventTriangleInt(e[0], e[1], e[3], this)
        tr[2] = new EventTriangleInt(e[0], e[2], e[3], this)
        tr[3] = new EventTriangleInt(e[1], e[2], e[3], this)

        this.maxRadius2 = MathUtilsInt.max(tr.collect {it.radius2()})
    }

    EventInt e1() { e[0] }

    EventInt e2() { e[1] }

    EventInt e3() { e[2] }

    EventInt e4() { e[3] }

    EventTriangleInt tr1() { tr[0] }

    EventTriangleInt tr2() { tr[1] }

    EventTriangleInt tr3() { tr[2] }

    EventTriangleInt tr4() { tr[3] }

    public boolean isValid(BigInteger timePassedSquared, boolean log) {
        if (maxMagSquared <= timePassedSquared) {
            // Check the radius of all triangle is below the max squared
            if (tr.any { it.radius2() > timePassedSquared }) {
                /*if (log)*/ println "Block $this has a triangle too flat for current time"
                return false
            }
            // Check same 3D plane using diff between fDir of triangles
            if (!MathUtilsInt.almostEquals(tr1().fDir, tr2().fDir) ||
                    !MathUtilsInt.almostEquals(tr1().fDir, tr3().fDir) ||
                    !MathUtilsInt.almostEquals(tr1().fDir, tr4().fDir) ||
                    !MathUtilsInt.almostEquals(tr2().fDir, tr3().fDir) ||
                    !MathUtilsInt.almostEquals(tr2().fDir, tr4().fDir) ||
                    !MathUtilsInt.almostEquals(tr3().fDir, tr4().fDir)
            ) {
                /*if (log)*/ println "Block $this is not making a plane"
                return false
            }
            if (log) println "Found valid block $this"
            return true
        } else {
            return false
        }
    }

    private static BigInteger calcMaxMagSquared(List<Point4i> points) {
        BigInteger result = 0G
        points.eachWithIndex { Point4i one, i ->
            if (i < points.size() - 1) {
                for (j in (i + 1)..(points.size() - 1)) {
                    def m = one.magSquared(points[j])
                    if (m > result) result = m
                }
            }
        }
        result
    }

    private static BigInteger calcTotalSurface16Squared(List<Point4i> points) {
        Triangle4i[] ts = [
         new Triangle4i(points[0], points[1], points[2]),
         new Triangle4i(points[0], points[1], points[3]),
         new Triangle4i(points[0], points[2], points[3]),
         new Triangle4i(points[1], points[2], points[3])
        ]
        BigInteger result = 0G
        ts.each { result += it.s16squared }
        result
    }

    private static def smallestSide(List<Point4i> points) {
        BigInteger smallDist2 = null
        int a = -1, b = -1
        points.eachWithIndex { Point4i one, i ->
            if (i < points.size() - 1) {
                for (j in (i + 1)..(points.size() - 1)) {
                    def vMagSquared = one.magSquared(points[j])
                    if (smallDist2 == null || smallDist2 > vMagSquared) {
                        smallDist2 = vMagSquared
                        a = i
                        b = j
                    }
                }
            }
        }
        [a, b]
    }

    private static def biggestSide(List<Point4i> points) {
        BigInteger bigDist2 = null
        int a = -1, b = -1
        points.eachWithIndex { Point4i one, i ->
            if (i < points.size() - 1) {
                for (j in (i + 1)..(points.size() - 1)) {
                    def vMagSquared = one.magSquared(points[j])
                    if (bigDist2 == null || bigDist2 < vMagSquared) {
                        bigDist2 = vMagSquared
                        a = i
                        b = j
                    }
                }
            }
        }
        [a, b]
    }

    def markUsed() {
        e.each { it.used = true }
    }

    EventBlockInt findNewEvents(boolean log) {
        List<EventInt> newEvents = []
        tr.each { EventTriangleInt t ->
            Point4i newPoint = t.findEvent()
            if (newPoint != null) {
                newEvents.add(new EventInt(newPoint, t.fDir, this, t))
            }
        }

        if (newEvents.size() == 4) {
            // Conservation of events good
            markUsed()
            // Activate conservation of time
            def newBlock = createBlock(newEvents)
            newBlock = newBlock.makeSurfaceEquals(totalSurface16Squared, log)
            return newBlock
        }
        null
    }

    static int NUMBER_OF_SWITCH=100

    EventBlockInt makeSurfaceEquals(BigInteger originalSurface16Squared, boolean log) {
        List<Point4i> points = e.collect() { it.point }
        int numberOfSwitch = 0
        Boolean previousDiffPositive = null
        BigDecimal previousSurface16Squared = null
        double deltaMove = 1d
        double currentAverageDist = 0d
log=true
        if (log) println("Trying to make ${this.totalSurface16Squared} equal to ${originalSurface16Squared} diff=${originalSurface16Squared-this.totalSurface16Squared}")
        double originalAverageDist = Math.sqrt(Math.sqrt((double)originalSurface16Squared/(16d*4d)))
        while (true) {
            BigDecimal currentSurface16Squared = calcTotalSurface16Squared(points)
            if (previousSurface16Squared == currentSurface16Squared) {
                println "We are not moving for delta=$deltaMove currentS162=$currentSurface16Squared"
                break
            }
            BigDecimal surface16SquaredDiff = originalSurface16Squared - currentSurface16Squared
            if (previousDiffPositive == null) {
                currentAverageDist = Math.sqrt(Math.sqrt((double)currentSurface16Squared/(16d*4d)))
                deltaMove = originalAverageDist - currentAverageDist
            }
            // Below Sqrt(3) we can loop by not modifying anything
            if (Math.abs(deltaMove) < 1.8d) {
                if (log) println "Perfectly equal $currentAverageDist == $originalAverageDist"
                break
            } else if (surface16SquaredDiff > 0G) {
                // Need to increase smallest distance
                def (int i, int j) = smallestSide(points)
                if (log) println "Increasing small distance diff=$surface16SquaredDiff delta=$deltaMove currentS162=$currentSurface16Squared using $i, $j"
                points = fillResultingPoints(points, i, j, deltaMove)
                if (previousDiffPositive == null) {
                    previousDiffPositive = true
                } else {
                    if (!previousDiffPositive) {
                        // Switched from - to + => increase number of switch
                        numberOfSwitch++
                        previousDiffPositive = null
                        previousSurface16Squared = null
                    }
                }
            } else if (surface16SquaredDiff < 0d) {
                // Need to decrease biggest distance
                def (int i, int j) = biggestSide(points)
                if (log) println "Decreasing big distance diff=$surface16SquaredDiff delta=$deltaMove currentS162=$currentSurface16Squared using $i, $j"
                points = fillResultingPoints(points, i, j, deltaMove)
                if (previousDiffPositive == null) {
                    previousDiffPositive = false
                } else {
                    if (previousDiffPositive) {
                        // Switched from + to - => increase number of switch
                        numberOfSwitch++
                        previousDiffPositive = null
                        previousSurface16Squared = null
                    }
                }
            }
            // stop at NUMBER_OF_SWITCH switch
            if (numberOfSwitch > NUMBER_OF_SWITCH) break
            previousSurface16Squared = currentSurface16Squared
        }
        new EventBlockInt([
                new EventInt(points[0], e[0].dir, e[0].createdByBlock, e[0].createdByTriangle),
                new EventInt(points[1], e[1].dir, e[1].createdByBlock, e[1].createdByTriangle),
                new EventInt(points[2], e[2].dir, e[2].createdByBlock, e[2].createdByTriangle),
                new EventInt(points[3], e[3].dir, e[3].createdByBlock, e[3].createdByTriangle)
        ])
    }

    public List<Point4i> fillResultingPoints(List<Point4i> points, int i, int j, double delta) {
        List<Point4i> results = []
        Vector3i v = new Vector3i(points[i], points[j])
        double d = delta / Math.sqrt((double) v.magSquared())
        points.eachWithIndex { Point4i p, k ->
            if (k==i) p = p - v * d
            if (k==j) p = p + v * d
            results.add(p)
        }
        results
    }

    @Override
    public String toString() {
        return "EventBlockInt dT=$deltaTime, S=$totalSurface16Squared, dir=$blockDir\n${e.collect({it.point}).join("\n")}"
    }
}



