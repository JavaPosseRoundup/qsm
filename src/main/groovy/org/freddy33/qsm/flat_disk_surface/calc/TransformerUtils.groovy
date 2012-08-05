package org.freddy33.qsm.flat_disk_surface.calc

import org.freddy33.math.dbl.Line4d
import org.freddy33.math.dbl.Point3d
import org.freddy33.math.dbl.SphericalUnitVector2d
import org.freddy33.math.dbl.Vector3d
import org.freddy33.math.dbl.Point4d

/**
 * User: freds
 * Date: 8/5/12
 * Time: 6:13 PM
 */
class TransformerUtils {
    public static List<Line4d> getLinesForMoment(Point3d originToDraw, SphericalUnitVector2d momentSpherical, double size, Transformer3dto4d transformer) {
        getLinesForMoments(momentSpherical, momentSpherical.toCartesian(), size, originToDraw, transformer)
    }

    public static List<Line4d> getLinesForMoment(Point3d originToDraw, Vector3d momentCartesian, double size, Transformer3dto4d transformer) {
        getLinesForMoments(new SphericalUnitVector2d(momentCartesian), momentCartesian, size, originToDraw, transformer)
    }

    private static List<Line4d> getLinesForMoments(SphericalUnitVector2d momentSpherical, Vector3d momentCartesian, double size, Point3d originToDraw, Transformer3dto4d transformer) {
        Vector3d m = momentCartesian * size
        Point3d localEndPoint = originToDraw + m
        Point4d endPoint = transformer.transformToGlobalCoordinates(localEndPoint)
        [
                new Line4d(transformer.transformToGlobalCoordinates(originToDraw), endPoint),
                new Line4d(endPoint, transformer.transformToGlobalCoordinates(localEndPoint + (m * -0.2d) + (momentSpherical.plusPiOver2().toCartesian() * (size * 0.2d)))),
                new Line4d(endPoint, transformer.transformToGlobalCoordinates(localEndPoint + (m * -0.2d) + (momentSpherical.plusPiOver2().toCartesian() * (size * -0.2d))))
        ]
    }
}
