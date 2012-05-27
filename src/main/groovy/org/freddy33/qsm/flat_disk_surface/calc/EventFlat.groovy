package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.bigInt.Point2i
import org.freddy33.math.dbl.Matrix3d
import org.freddy33.math.dbl.Point3d
import org.freddy33.qsm.sphere_surface_int.calc.Sign
import org.freddy33.math.dbl.Point4d
import org.freddy33.math.dbl.Point2d

import static org.freddy33.math.dbl.MathUtilsDbl.sin60
import org.freddy33.math.dbl.Line3d
import org.freddy33.math.dbl.Line2d
import org.freddy33.math.dbl.Line4d

/**
 * Created with IntelliJ IDEA.
 * User: freds
 * Date: 5/25/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
class EventFlat {
    final EventBlockFlat belongsTo
    final Point2d point
    final Sign sign

    EventFlat(Point2d point, EventBlockFlat belongs) {
        this.belongsTo = belongs
        this.point = point
    }

    static List<Line2d> calcWaitingEvent(BigInteger waitingEventsDist, WaitingEventShape shape) {
        double d = (double)waitingEventsDist
        switch (shape) {
            case WaitingEventShape.SQUARE:
                return [
                        new Line2d(new Point2d(-d,  d), new Point2d( d,  d)),
                        new Line2d(new Point2d( d,  d), new Point2d( d, -d)),
                        new Line2d(new Point2d( d, -d), new Point2d(-d, -d)),
                        new Line2d(new Point2d(-d, -d), new Point2d(-d,  d))
                ]
            case WaitingEventShape.DIAMOND:
                return [
                        new Line2d(new Point2d(-d,  0), new Point2d( 0,  d)),
                        new Line2d(new Point2d( 0,  d), new Point2d( d,  0)),
                        new Line2d(new Point2d( d,  0), new Point2d( 0, -d)),
                        new Line2d(new Point2d( 0, -d), new Point2d(-d,  0))
                ]
            case WaitingEventShape.TRIANGLE:
                return [
                        new Line2d(new Point2d(-d, 0), new Point2d( -d*sin60, d/2d)),
                        new Line2d(new Point2d(-d*sin60, d/2d), new Point2d( -d/2d, d*sin60)),
                        new Line2d(new Point2d(-d/2d, d*sin60), new Point2d( 0, d)),
                        new Line2d(new Point2d( 0, d), new Point2d( d/2d, d*sin60)),
                        new Line2d(new Point2d( d/2d, d*sin60), new Point2d(d*sin60,  d/2)),
                        new Line2d(new Point2d( d*sin60, d/2d), new Point2d(d, 0)),

                        new Line2d(new Point2d(-d, 0), new Point2d( -d*sin60, -d/2d)),
                        new Line2d(new Point2d(-d*sin60, -d/2d), new Point2d( -d/2d, -d*sin60)),
                        new Line2d(new Point2d(-d/2d, -d*sin60), new Point2d( 0, -d)),
                        new Line2d(new Point2d( 0, -d), new Point2d( d/2d, -d*sin60)),
                        new Line2d(new Point2d( d/2d, -d*sin60), new Point2d(d*sin60,  -d/2)),
                        new Line2d(new Point2d( d*sin60, -d/2d), new Point2d(d, 0))
                ]
            default:
                throw new UnsupportedOperationException("Shape $shape not supported!")
        }
    }

    List<Line4d> getAllWaitingEvents(BigInteger currentTime, WaitingEventShape shape) {
        List<Line4d> result = []
        Matrix3d transform = belongsTo.plane.traInv
        BigInteger waitingTime = currentTime - belongsTo.createdTime
        List<Line2d> waitingEvents = calcWaitingEvent(waitingTime, shape)
        waitingEvents.each { we ->
            result.add(new Line4d(
                    belongsTo.origin + transform * new Point4d(
                    (double) point.x + we.a.x,
                    (double) point.y + we.a.y,
                    (double) waitingTime * 2d * sin60, // Z=2*sin60*T for all 60 degree
                    (double) waitingTime),
                    belongsTo.origin + transform * new Point4d(
                    (double) point.x + we.b.x,
                    (double) point.y + we.b.y,
                    (double) waitingTime * 2d * sin60, // Z=T since all speed of light
                    (double) waitingTime)
            ))
        }
        result
    }

    @Override
    String toString() {
        "ef($point)"
    }
}

enum WaitingEventShape {
    SQUARE, DIAMOND, TRIANGLE
}
