package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.dbl.Point4d
import org.freddy33.math.dbl.Point2d

import org.freddy33.math.dbl.Line2d
import org.freddy33.math.dbl.Line4d
import org.freddy33.math.bigInt.EventSign
import org.freddy33.math.dbl.Point3d
import org.freddy33.math.dbl.EulerAngles3d
import org.freddy33.math.dbl.SphericalUnitVector2d
import org.freddy33.math.dbl.Vector3d
import org.freddy33.math.bigInt.TrigoInt
import org.freddy33.math.dbl.TrigoDbl

/**
 * User: freds
 * Date: 5/25/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
class EventFlat {
    final EventBlockFlat belongsTo
    final Point3d point
    final EventSign sign
    final EulerAngles3d plane

    /**
     * Constructor for pyramid like event blocks where internal euler plane is changed for each points
     */
    EventFlat(Point3d point, Point3d anotherPoint, EventBlockFlat belongs, EventSign sign) {
        this.belongsTo = belongs
        this.sign = sign
        this.point = point

        // The plane is dictated for vector Z=(point,origin), Psi=found from Y = XY projection of (point,-anotherPoint)
        this.plane = new EulerAngles3d(
                new SphericalUnitVector2d(new Vector3d(point, Point3d.ORIGIN)),
                new Vector3d(point, -anotherPoint))
    }

    /**
     * Constructor
     */
    EventFlat(Point3d point, EventBlockFlat belongs, EventSign sign) {
        this.belongsTo = belongs
        this.sign = sign
        this.plane = null
        this.point = point
    }

    static List<Line2d> calcWaitingEvent(BigInteger waitingEventsDist) {
        double d = (double)waitingEventsDist
        switch (GlobalParams.SHAPE) {
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
                return circleWithLines(TrigoInt.D30 * 2G, d)
            case WaitingEventShape.CIRCLE:
                return circleWithLines(1G, d)
            default:
                throw new UnsupportedOperationException("Shape ${GlobalParams.SHAPE} not supported!")
        }
    }

    public static Line2d[] circleWithLines(BigInteger angleIncrement, double size) {
        List<Line2d> result = []
        for (BigInteger a = 0G; a < TrigoInt.DIV;) {
            BigInteger b = a + angleIncrement
            result.add(new Line2d(pointOnUnitCircle(a) * size, pointOnUnitCircle(b) * size))
            a = b
        }
        result.toArray(new Line2d[result.size()])
    }

    public static Point2d pointOnUnitCircle(BigInteger angle) {
        BigInteger a
        new Point2d(TrigoDbl.cos(angle), TrigoDbl.sin(angle))
    }

    List<Line4d> getAllWaitingEvents(BigInteger currentTime) {
        List<Line4d> result = []
        BigInteger waitingTime = currentTime - belongsTo.createdTime
        List<Line2d> waitingEvents = calcWaitingEvent(waitingTime)
        waitingEvents.each { we ->
            result.add(new Line4d(
                    transformToGlobalCoord(we.a, waitingTime),
                    transformToGlobalCoord(we.b, waitingTime)))
        }
        result
    }

    Point4d transformToGlobalCoord(Point2d p, BigInteger waitingTime) {
        if (this.plane == null) {
            belongsTo.origin + belongsTo.singlePlane.traInv * new Point4d(
                    point.x + p.x,
                    point.y + p.y,
                    (double) waitingTime * GlobalParams.K,
                    (double) waitingTime)
        } else {
            belongsTo.origin + belongsTo.singlePlane.traInv * (
                new Point4d( point.x, point.y, 0d, 0d ) + (
                plane.traInv * new Point4d(
                    p.x,
                    p.y,
                    (double) waitingTime * GlobalParams.K,
                    (double) waitingTime)
                )
            )
        }
    }

    @Override
    String toString() {
        "ef($point, $sign, $plane)"
    }
}

