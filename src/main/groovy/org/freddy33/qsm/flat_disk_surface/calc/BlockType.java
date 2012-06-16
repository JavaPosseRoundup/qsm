package org.freddy33.qsm.flat_disk_surface.calc;

import org.freddy33.math.bigInt.EventColor;
import org.freddy33.math.dbl.Point3d;
import org.freddy33.math.dbl.MathUtilsDbl;

/**
 * User: freds
 * Date: 5/31/12
 * Time: 11:12 PM
 */
public enum BlockType implements EventsBuilder {
    FlatStar {
        @Override
        public EventFlat[] createEvents(EventBlockFlat from, double size) {
            return new EventFlat[] {
                    new EventFlat(new Point3d(0d, 0d, 0d), from, EventColor.plus_1),
                    new EventFlat(new Point3d(0d, size, 0d), from, EventColor.minus_1),
                    new EventFlat(new Point3d(size * MathUtilsDbl.sin60, -size * MathUtilsDbl.cos60, 0d), from, EventColor.plus_i),
                    new EventFlat(new Point3d(-size * MathUtilsDbl.sin60, -size * MathUtilsDbl.cos60, 0d), from, EventColor.minus_i)
            };
        }
    },
    Pyramid {
        @Override
        public EventFlat[] createEvents(EventBlockFlat from, double size) {
            Point3d[] point3ds = new Point3d[] {
                    new Point3d(size * MathUtilsDbl.cos60, 0d, size * MathUtilsDbl.sin45 / 2d),
                    new Point3d(-size * MathUtilsDbl.cos60, 0d, size * MathUtilsDbl.sin45 / 2d),
                    new Point3d(0d, size * MathUtilsDbl.cos60, -size * MathUtilsDbl.sin45 / 2d),
                    new Point3d(0d, -size * MathUtilsDbl.cos60, -size * MathUtilsDbl.sin45 / 2d)
            };
            return new EventFlat[] {
                    new EventFlat(point3ds[0], point3ds[1], from, EventColor.plus_1),
                    new EventFlat(point3ds[1], point3ds[2], from, EventColor.minus_1),
                    new EventFlat(point3ds[2], point3ds[3], from, EventColor.plus_i),
                    new EventFlat(point3ds[3], point3ds[0], from, EventColor.minus_i)
            };
        }
    },
    FlatDiamond {
        @Override
        public EventFlat[] createEvents(EventBlockFlat from, double size) {
            return new EventFlat[] {
                    new EventFlat(new Point3d(0d, size/2d, 0d), from, EventColor.plus_1),
                    new EventFlat(new Point3d(0d, -size/2d, 0d), from, EventColor.minus_1),
                    new EventFlat(new Point3d(size * MathUtilsDbl.sin60, 0d, 0d), from, EventColor.plus_i),
                    new EventFlat(new Point3d(-size * MathUtilsDbl.sin60, 0d, 0d), from, EventColor.minus_i)
            };
        }
    }
}
