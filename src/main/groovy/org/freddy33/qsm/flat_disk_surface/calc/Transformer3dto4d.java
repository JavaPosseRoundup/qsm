package org.freddy33.qsm.flat_disk_surface.calc;

import org.freddy33.math.dbl.Point3d;
import org.freddy33.math.dbl.Point4d;

/**
 * User: freds
 * Date: 8/5/12
 * Time: 6:07 PM
 */
public interface Transformer3dto4d {
    public Point4d transformToGlobalCoordinates(Point3d p);
}
