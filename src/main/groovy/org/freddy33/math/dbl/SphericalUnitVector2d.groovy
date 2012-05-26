package org.freddy33.math.dbl

import org.freddy33.math.bigInt.Point4i
import org.freddy33.math.bigInt.Vector3i

import static java.lang.Math.PI

public class SphericalUnitVector2d {
    final double teta /* [0, pi] */, phi /* [-pi, pi] */

    /**
     * Create a normalized spherical vector for teta phi
     * @param pteta
     * @param pphi
     */
    public SphericalUnitVector2d(double pteta, pphi) {
        teta = pteta
        if (pteta == 0d || pteta == PI) {
            // Force phi zero on z axis
            phi = 0G
        } else {
            phi = pphi
        }
        validate()
    }

    static SphericalUnitVector2d middleMan(List<SphericalUnitVector2d> pv) {
        double resultTeta = 0G;
        double resultPhi = 0G;
        pv.each { resultTeta += it.teta; resultPhi += it.phi; }
        resultTeta = resultTeta / pv.size()
        resultPhi = resultPhi / pv.size()
        while (resultTeta > PI) {
            resultTeta -= PI
            resultPhi += PI
        }
        while (resultPhi > PI) resultPhi -= 2d * PI
        while (resultPhi <= -PI) resultPhi += 2d * PI
        new SphericalUnitVector2d(resultTeta, resultPhi)
    }

    private validate() {
        if (teta < 0G || teta > PI) throw new IllegalArgumentException("Spherical coord teta between 0 and PI! For $this")
        if ((teta == 0G || teta == PI) && phi != 0G) throw new IllegalArgumentException("Phi should be zero for teta 0 or PI! For $this")
        if (phi <= -PI || phi > PI) throw new IllegalArgumentException("Spherical coord phi between -PI and PI! For $this")
    }

    public SphericalUnitVector2d(Point4d p1, Point4d p2) {
        this(new Vector3d(p1, p2))
    }

    public SphericalUnitVector2d(Point4i p1, Point4i p2) {
        this(new Vector3i(p1, p2))
    }

    public SphericalUnitVector2d(Vector3i v) {
        this(new Vector3d((double)v.x, (double)v.y, (double)v.z))
    }

    public SphericalUnitVector2d(Vector3d v) {
        double d2 = v.magSquared()
        if (MathUtilsDbl.eq(d2, 0d)) {
            phi = teta = 0G
        } else {
            def rd = Math.sqrt(d2)
            phi = Math.atan2(v.y, v.x)
            teta = Math.acos(v.z / rd)
        }
        validate()
    }

    public Vector3d toCartesian() {
        double sinTeta = Math.sin(teta)
        double x = Math.cos(phi) * sinTeta
        double y = Math.sin(phi) * sinTeta
        double z = Math.cos(teta)
        new Vector3d(x, y, z)
    }

    public double d() {
        1d
    }

    public double magSquared() {
        1d
    }

    public SphericalUnitVector2d negative() {
        if (teta == 0G || teta == PI) {
            // Just reverse phi is zero anyway
            return new SphericalUnitVector2d(PI - teta, 0d);
        }
        def nphi = PI + phi
        if (nphi > PI) nphi -= 2d * PI
        return new SphericalUnitVector2d(PI - teta, nphi)
    }

    double mod(SphericalUnitVector2d v) {
        dot(v)
    }

    double dot(SphericalUnitVector2d v) {
        // 1/4 * ( (2+cos(phi1-phi2)) * cos(teta1-teta2) + (2-cos(phi1-phi2)) * cos(teta1+teta2) )
        double cosPhi1MinusPhi2 = Math.cos(this.phi - v.phi)
        double cosTeta1MinusTeta2 = Math.cos(this.teta - v.teta)
        double cosTeta1PlusTeta2 = Math.cos(this.teta + v.teta)
        ((2d + cosPhi1MinusPhi2) * cosTeta1MinusTeta2 + (2d - cosPhi1MinusPhi2) * cosTeta1PlusTeta2) / 4d
    }

    SphericalUnitVector2d cross(SphericalUnitVector2d v) {
        new SphericalUnitVector2d(this.toCartesian().cross(v.toCartesian()))
    }

    public boolean isNormalized() {
        true
    }

    @Override
    boolean equals(Object obj) {
        SphericalUnitVector2d v = (SphericalUnitVector2d) obj
        MathUtilsDbl.eq(teta, v.teta) && MathUtilsDbl.eq(phi, v.phi)
    }

    String toString() {
        "suv2d($teta, $phi)"
    }
}
