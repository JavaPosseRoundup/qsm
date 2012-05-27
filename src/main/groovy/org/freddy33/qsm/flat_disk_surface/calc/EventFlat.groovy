package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.bigInt.Point2i
import org.freddy33.math.dbl.Matrix3d
import org.freddy33.math.dbl.Point3d
import org.freddy33.qsm.sphere_surface_int.calc.Sign
import org.freddy33.math.dbl.Point4d

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
class EventFlat {
    final EventBlockFlat belongsTo
    final Point2i point
    final Sign sign

    EventFlat(Point2i point, EventBlockFlat belongs) {
        this.belongsTo = belongs
        this.point = point
    }

    static List<Point2i> calcWaitingEvent(BigInteger waitingEventsDist, WaitingEventShape shape) {
        List<Point2i> waitingEvents = []
        switch (shape) {
            case WaitingEventShape.SQUARE:
                (-waitingEventsDist..waitingEventsDist).each {
                    waitingEvents.add(new Point2i(it, waitingEventsDist))
                    waitingEvents.add(new Point2i(it, -waitingEventsDist))
                    if (it != waitingEventsDist || it != -waitingEventsDist) {
                        waitingEvents.add(new Point2i(waitingEventsDist, it))
                        waitingEvents.add(new Point2i(-waitingEventsDist, it))
                    }
                }
                break;
            case WaitingEventShape.DIAMOND:
                (0G..waitingEventsDist).each {
                    waitingEvents.add(new Point2i(it, waitingEventsDist - it))
                    waitingEvents.add(new Point2i(it - waitingEventsDist, -it))
                    if (it != 0 && it != waitingEventsDist) {
                        waitingEvents.add(new Point2i(it, it - waitingEventsDist))
                        waitingEvents.add(new Point2i(it - waitingEventsDist, it))
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Shape $shape not supported!")
        }
        waitingEvents
    }

    List<Point4d> getAllWaitingEvents(BigInteger currentTime, WaitingEventShape shape) {
        List<Point4d> result = []
        Matrix3d transform = belongsTo.plane.traInv
        BigInteger waitingTime = currentTime - belongsTo.createdTime
        List<Point2i> waitingEvents = calcWaitingEvent(waitingTime, shape)
        waitingEvents.each { we ->
            result.add(belongsTo.origin + transform * new Point4d(
                    (double) point.x + we.x,
                    (double) point.y + we.y,
                    (double) waitingTime, // Z=T since all speed of light
                    (double) waitingTime))
        }
        result
    }

    @Override
    String toString() {
        "ef($point)"
    }
}

enum WaitingEventShape {
    SQUARE, DIAMOND
}
