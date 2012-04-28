package org.freddy33.qsm.space

import org.freddy33.math.Point4i
import org.freddy33.math.SphericalVector3i
import org.freddy33.math.Vector3i
import org.jzy3d.plot3d.pipelines.NotImplementedException

/**
 * User: freds
 * Date: 4/7/12
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class EventInt {
    final EventBlockInt createdByBlock
    final EventTriangleInt createdByTriangle
    final Point4i point
    final SphericalVector3i dir
    final Sign sign
    boolean used = false

    EventInt(Point4i p, SphericalVector3i v) {
        this(p, v, null, null)
    }

    EventInt(Point4i p, SphericalVector3i v, EventBlockInt from, EventTriangleInt tr) {
        point = p
        dir = v
        createdByBlock = from
        createdByTriangle = tr
    }

    @Override
    public String toString() {
        return "evt($point, $dir, used=$used)";
    }
}

class EventTriangleInt {
    final EventBlockInt from
    final EventInt e1, e2, e3
    final Vector3i v12, v13, v23, v12v23cross
    final SphericalVector3i fDir
    final BigInteger p12p13cross22

    EventTriangleInt(EventInt e1, EventInt e2, EventInt e3, EventBlockInt from) {
        this.e1 = e1
        this.e2 = e2
        this.e3 = e3
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
        if ((fd % from.blockDir) < 0G) {
            fDir = fd.negative()
        } else {
            fDir = fd
        }
    }

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
                (BigInteger) ((alpha * e1.point.x + beta * e2.point.x + gama * e3.point.x) / p12p13cross22),
                (BigInteger) ((alpha * e1.point.y + beta * e2.point.y + gama * e3.point.y) / p12p13cross22),
                (BigInteger) ((alpha * e1.point.z + beta * e2.point.z + gama * e3.point.z) / p12p13cross22),
                e1.point.t
        )
    }

    BigInteger radius2() {
        (BigInteger) ((v12.magSquared() * v13.magSquared() * v23.magSquared()) / (2G * p12p13cross22))
    }

    @Override
    String toString() {
        "tr($e1, $e2, $e3, ${fDir?.toCartesian()})"
    }
}

class EventBlockInt {
    final List<EventInt> es
    final Set<EventTriangleInt> triangles = []
    final SphericalVector3i blockDir
    final BigInteger maxMagSquared
    final BigInteger sumMagSquared
    final BigInteger deltaTime

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
            throw new IllegalArgumentException("Cannot make block with less than 4 events")
        }
        this.es = es
        // Global dir of the block is the barycenter of directions vectors
        this.blockDir = SphericalVector3i.middleMan(es.collect { it.dir })
        this.maxMagSquared = calcMaxMagSquared(es.collect() { it.point })
        this.sumMagSquared = calcSumMagSquared(es.collect() { it.point })
        this.deltaTime = (BigInteger) Math.sqrt((double) this.maxMagSquared)
        fillTriangles()
    }

    public boolean isValid(BigInteger timePassedSquared) {
        if (maxMagSquared <= timePassedSquared) {
            // TODO: Check same 3D plane
            // Check the radius of all triangle is below the max squared
            if (triangles.any { it.radius2() > timePassedSquared }) {
                println "Block $this has a triangle to flat for current time"
                return false
            }
            println "Found valid block $this"
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

    private static BigInteger calcSumMagSquared(List<Point4i> points) {
        BigInteger result = 0G
        points.eachWithIndex { Point4i one, i ->
            if (i < points.size() - 1) {
                for (j in (i + 1)..(points.size() - 1)) {
                    result += one.magSquared(points[j])
                }
            }
        }
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

    private def fillTriangles() {
        // For 4 it's all possible triangles
        // TODO: For 5 there is 12 solutions that create 5 triangles covering it all
        if (es.size() == 4) {
            // Writing the loop was longer ?!?
            triangles.add(new EventTriangleInt(es[0], es[1], es[2], this))
            triangles.add(new EventTriangleInt(es[0], es[1], es[3], this))
            triangles.add(new EventTriangleInt(es[0], es[2], es[3], this))
            triangles.add(new EventTriangleInt(es[1], es[2], es[3], this))
        } else {
            throw new NotImplementedException()
        }
        triangles
    }

    def markUsed() {
        es.each { it.used = true }
    }

    EventBlockInt findNewEvents() {
        List<EventInt> newEvents = []
        triangles.each { EventTriangleInt tr ->
            Point4i newPoint = tr.findEvent()
            if (newPoint != null) newEvents.add(new EventInt(newPoint, tr.fDir, this, tr))
        }
        if (newEvents.size() == es.size()) {
            // Conservation of events good
            markUsed()
            // Activate conservation of time
            def newBlock = createBlock(newEvents)
            if (newBlock.sumMagSquared != sumMagSquared) {
                println "Need to activate cons of time for $newBlock"
                newBlock.makeSizeEqualTo(maxMagSquared)
            }
            return newBlock
        }
        null
    }

    EventBlockInt makeSizeEqualTo(BigInteger newSumMagSquared) {
        List<Point4i> points = es.collect() { it.point }
        List<Vector3i> vectors = [
                new Vector3i(points[0], points[1]),
                new Vector3i(points[0], points[2]),
                new Vector3i(points[0], points[3]),
                new Vector3i(points[1], points[2]),
                new Vector3i(points[1], points[3]),
                new Vector3i(points[2], points[3])
        ]
        List<Double> multipliers = [
                0d,
                0d,
                0d,
                0d,
                0d,
                0d
        ]

        List<Point4i> results = []
        Boolean previousDiffPositive = null

        while (true) {
            results.clear()
            results.add(points[0] - vectors[0] * multipliers[0] - vectors[1] * multipliers[1] - vectors[2] * multipliers[2])
            results.add(points[1] + vectors[0] * multipliers[0] - vectors[3] * multipliers[3] - vectors[4] * multipliers[4])
            results.add(points[2] + vectors[0] * multipliers[0] + vectors[3] * multipliers[3] - vectors[5] * multipliers[5])
            results.add(points[3] + vectors[0] * multipliers[0] + vectors[4] * multipliers[4] + vectors[5] * multipliers[5])

            def diffMagSquared = newSumMagSquared - calcSumMagSquared(results)
            if (diffMagSquared > 0G) {
                // Need to increase smallest distance
                def (int i, int j) = smallestSide(results)
                if (i == 0) {
                    multipliers[j - 1] += 2d / SphericalVector3i.D180
                } else {
                    multipliers[j + i] += 2d / SphericalVector3i.D180
                }
                if (previousDiffPositive == null) {
                    previousDiffPositive = true
                } else {
                    if (!previousDiffPositive) {
                        // Switched from + to - => stop
                        break
                    }
                }
            } else if (diffMagSquared < 0G) {
                // Need to decrease biggest distance
                def (int i, int j) = biggestSide(results)
                if (i == 0) {
                    multipliers[j - 1] -= 2d / SphericalVector3i.D180
                } else {
                    multipliers[j + i] -= 2d / SphericalVector3i.D180
                }
                if (previousDiffPositive == null) {
                    previousDiffPositive = false
                } else {
                    if (previousDiffPositive) {
                        // Switched from + to - => stop
                        break
                    }
                }
            } else {
                println "Perfectly equal $this == $newSumMagSquared"
                break
            }
        }
        null
    }

    @Override
    public String toString() {
        return "EventBlockInt dT=$deltaTime, dir=$blockDir\n${es.join("\n")}\n${triangles.join("\n")}"
    }
}



