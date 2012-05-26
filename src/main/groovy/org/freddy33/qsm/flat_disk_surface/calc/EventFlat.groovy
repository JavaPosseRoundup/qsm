package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.qsm.sphere_surface_int.calc.Sign
import org.freddy33.math.dbl.Point4d
import org.freddy33.math.dbl.SphericalUnitVector2d
import org.freddy33.math.bigInt.Point4i
import org.freddy33.math.bigInt.Point2i
import org.freddy33.math.dbl.Matrix3d
import org.freddy33.math.dbl.Point3d

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
class EventFlat {
    static final WaitingEventShape shape = WaitingEventShape.SQUARE
    final EventBlockFlat belongsTo
    final Point2i point
    final Sign sign
    BigInteger currentWaitingDist
    List<WaitingEventFlat> waitingEvents

    EventFlat(Point2i point, EventBlockFlat belongs) {
        this.belongsTo = belongs
        this.point = point
        this.currentWaitingDist = 0G
        this.waitingEvents = Collections.emptyList()
    }

    public BigInteger getCurrentWaitingDist() {
        return currentWaitingDist
    }

    def incrementTime() {
        currentWaitingDist++
        setWaitingEventDistance(currentWaitingDist)
    }

    def setWaitingEventDistance(BigInteger waitingEventsDist) {
        waitingEvents.clear()
        if (shape == WaitingEventShape.SQUARE) {
            (-waitingEventsDist..waitingEventsDist).each {
                waitingEvents.add(new WaitingEventFlat(this, new Point2i(it, waitingEventsDist)))
                waitingEvents.add(new WaitingEventFlat(this, new Point2i(it, -waitingEventsDist)))
                if (it != waitingEventsDist || it != -waitingEventsDist) {
                    waitingEvents.add(new WaitingEventFlat(this, new Point2i(waitingEventsDist, it)))
                    waitingEvents.add(new WaitingEventFlat(this, new Point2i(-waitingEventsDist, it)))
                }
            }
        } else if (shape == WaitingEventShape.DIAMOND) {
            (0G..waitingEventsDist).each {
                waitingEvents.add(new WaitingEventFlat(this, new Point2i(it, waitingEventsDist-it)))
                waitingEvents.add(new WaitingEventFlat(this, new Point2i(it-waitingEventsDist, -it)))
                if (it != 0 || it != waitingEventsDist) {
                    waitingEvents.add(new WaitingEventFlat(this, new Point2i(it, it-waitingEventsDist)))
                    waitingEvents.add(new WaitingEventFlat(this, new Point2i(it-waitingEventsDist, it)))
                }
            }
        } else {
            throw new UnsupportedOperationException("Shape $shape not supported!")
        }
    }

    List<Point3d> getAllWaitingEvents(BigInteger currentTime) {
        List<Point3d> result = []
        Matrix3d transform = belongsTo.plane.transform
        BigInteger waitingTime = currentTime - belongsTo.createdTime
        setWaitingEventDistance(waitingTime)
        waitingEvents.each { we ->
            result.add(transform * new Point3d((double)we.pos.x, (double)we.pos.y, (double)waitingTime))
        }
        result
    }
}

enum WaitingEventShape { SQUARE, DIAMOND }

class WaitingEventFlat {
    final EventFlat from
    final Point2i pos

    WaitingEventFlat(EventFlat from, Point2i pos) {
        this.from = from
        this.pos = pos
    }
}
