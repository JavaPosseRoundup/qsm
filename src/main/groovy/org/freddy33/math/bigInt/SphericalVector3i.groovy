package org.freddy33.math.bigInt

import static org.freddy33.math.bigInt.TrigoInt.ONE
import static org.freddy33.math.bigInt.TrigoInt.cos
import static org.freddy33.math.bigInt.TrigoInt.sin
import static org.freddy33.math.bigInt.TrigoInt.acos
import static org.freddy33.math.bigInt.TrigoInt.atan2

public class SphericalVector3i {
    final BigInteger r, teta /* [0, pi] */, phi /* [-pi, pi] */

    /**
     * Create a normalized spherical vector for teta phi
     * @param pteta
     * @param pphi
     */
    public SphericalVector3i(BigInteger pteta, pphi) {
        this(ONE, pteta, pphi)
    }

    public SphericalVector3i(BigInteger pr, pteta, pphi) {
        r = pr
        teta = pteta
        if (pteta == 0G || pteta == TrigoInt.D180) {
            // Force phi zero on z axis
            phi = 0G
        } else {
            phi = pphi
        }
        validate()
    }

    static SphericalVector3i middleMan(List<SphericalVector3i> pv) {
        BigInteger resultTeta = 0G;
        BigInteger resultPhi = 0G;
        pv.each { resultTeta += it.teta; resultPhi += it.phi; }
        resultTeta = (BigInteger) resultTeta / pv.size()
        resultPhi = (BigInteger) resultPhi / pv.size()
        while (resultTeta > TrigoInt.D180) {
            resultTeta -= TrigoInt.D180
            resultPhi += TrigoInt.D180
        }
        while (resultPhi > TrigoInt.D180) resultPhi -= TrigoInt.DIV
        while (resultPhi <= -TrigoInt.D180) resultPhi += TrigoInt.DIV
        new SphericalVector3i(resultTeta, resultPhi)
    }

    private validate() {
        if (r < 0G) throw new IllegalArgumentException("Spherical coord r cannot be neg! For $this")
        if (teta < 0G || teta > TrigoInt.D180) throw new IllegalArgumentException("Spherical coord teta between 0 and PI=$TrigoInt.D180! For $this")
        if ((teta == 0G || teta == TrigoInt.D180) && phi != 0G) throw new IllegalArgumentException("Phi should be zero for teta 0 or PI=$TrigoInt.D180! For $this")
        if (phi <= -TrigoInt.D180 || phi > TrigoInt.D180) throw new IllegalArgumentException("Spherical coord phi between -PI=${-TrigoInt.D180} and PI=$TrigoInt.D180! For $this")
    }

    public SphericalVector3i(Point4i p1, Point4i p2) {
        this(new Vector3i(p1, p2))
    }

    public SphericalVector3i(Vector3i v) {
        def d2 = v.magSquared()
        if (d2 == 0G) {
            r = phi = teta = 0G
        } else {
            def rd = Math.sqrt((double) d2)
            r = rd
            phi = atan2(v.y, v.x)
            teta = acos((BigInteger) v.z / rd)
        }
        validate()
    }

    public Vector3i toCartesian() {
        BigInteger sinTeta = sin(teta)
        BigInteger x = cos(phi) * sinTeta
        BigInteger y = sin(phi) * sinTeta
        BigInteger z = ONE * cos(teta)
        new Vector3i((BigInteger) x * r / (ONE * ONE), (BigInteger) y * r / (ONE * ONE), (BigInteger) z * r / (ONE * ONE))
    }

    public BigInteger d() {
        r
    }

    public BigInteger magSquared() {
        r * r
    }

    public SphericalVector3i negative() {
        if (teta == 0G || teta == TrigoInt.D180) {
            // Just reverse phi is zero anyway
            return new SphericalVector3i(r, TrigoInt.D180 - teta, 0G);
        }
        def nphi = TrigoInt.D180 + phi
        if (nphi > TrigoInt.D180) nphi -= TrigoInt.DIV
        return new SphericalVector3i(r, TrigoInt.D180 - teta, nphi)
    }

    SphericalVector3i setR(BigInteger d) {
        if (d > 0G) {
            new SphericalVector3i(d, teta, phi)
        } else if (d == 0G) {
            new SphericalVector3i(0G, 0G, 0G)
        } else {
            negative().setR(-d)
        }
    }

    SphericalVector3i multiply(BigInteger d) {
        if (d < 0G) {
            negative().multiply(-d)
        } else if (d == 0G) {
            new SphericalVector3i(0G, 0G, 0G)
        } else {
            if (isNormalized()) {
                new SphericalVector3i(d, teta, phi)
            } else {
                new SphericalVector3i((BigInteger) (r * d) / ONE, teta, phi)
            }
        }
    }

    SphericalVector3i normalized() {
        // Normalized on DIV
        new SphericalVector3i(teta, phi)
    }

    BigInteger mod(SphericalVector3i v) {
        dot(v)
    }

    BigInteger dot(SphericalVector3i v) {
        // 1/4 * ( (2+cos(phi1-phi2)) * cos(teta1-teta2) + (2-cos(phi1-phi2)) * cos(teta1+teta2) )
        BigInteger cosPhi1MinusPhi2 = cos(this.phi - v.phi)
        BigInteger cosTeta1MinusTeta2 = cos(this.teta - v.teta)
        BigInteger cosTeta1PlusTeta2 = cos(this.teta + v.teta)
        (BigInteger) (this.r * v.r * ((2G * ONE + cosPhi1MinusPhi2) * cosTeta1MinusTeta2 + (2G * ONE - cosPhi1MinusPhi2) * cosTeta1PlusTeta2) / (ONE * ONE))
    }

    SphericalVector3i cross(SphericalVector3i v) {
        // Valid only on normalized v
        def me = normalizedToVectorInt()
        def other = v.normalizedToVectorInt()
        Vector3i result = me.cross(other)
        def rd = Math.sqrt((double) result.magSquared())
        BigInteger resultPhi = atan2(result.y, result.x)
        BigInteger resultTeta = acos((BigInteger) result.z / rd)
        new SphericalVector3i(resultTeta, resultPhi)
    }

    public Vector3i normalizedToVectorInt() {
        if (!isNormalized()) throw new IllegalArgumentException("$this is not a normalized vector")
        BigInteger myX = cos(phi) * sin(teta)
        BigInteger myY = sin(phi) * sin(teta)
        BigInteger myZ = cos(teta) * ONE
        new Vector3i(myX, myY, myZ)
    }

    public boolean isNormalized() {
        r == ONE
    }

    @Override
    boolean equals(Object obj) {
        SphericalVector3i v = (SphericalVector3i) obj
        r == v.r && teta == v.teta && phi == v.phi
    }

    String toString() {
        "vp($r, $teta, $phi)"
    }
}
