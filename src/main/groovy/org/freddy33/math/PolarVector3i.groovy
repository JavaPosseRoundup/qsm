package org.freddy33.math;

public class PolarVector3i {
    public static final Map<BigInteger, BigInteger> trigMap = [:]
    public static final BigInteger DIV = 46656G
    static BigInteger D180 = DIV / 2G
    static BigInteger D90 = DIV / 4G
    static BigInteger D120 = DIV / 3G
    static double CONVERTER = DIV / (2d * Math.PI)

    static {
        for (i in 0G..D90) {
            trigMap.put(i, (BigInteger) (DIV * Math.sin(i / CONVERTER)))
        }
    }

    static BigInteger sin(BigInteger angle) {
        if (angle >= 0G && angle <= D90) {
            return trigMap[angle]
        } else if (angle < 0G && angle >= -D90) {
            return -trigMap[-angle]
        } else if (angle > D90 && angle <= D180) {
            return trigMap[D180 - angle]
        } else if (angle < -D90 && angle >= -D180) {
            return -trigMap[D180 + angle]
        } else if (angle < -D180) {
            return sin(angle + DIV)
        } else if (angle > D180) {
            return sin(angle - DIV)
        } else {
            throw new RuntimeException("Don't know how to count!")
        }
    }

    static BigInteger cos(BigInteger angle) {
        sin(angle + D90)
    }

    final BigInteger r, teta /* [0, pi] */, phi /* [-pi, pi] */

    public PolarVector3i(BigInteger pr, pteta, pphi) {
        r = pr
        teta = pteta
        if (pteta == 0G || pteta == D180) {
            // Force phi zero on z axis
            phi = 0G
        } else {
            phi = pphi
        }
        validate()
    }

    static PolarVector3i middleMan(List<PolarVector3i> pv) {
        BigInteger resultTeta = 0G;
        BigInteger resultPhi = 0G;
        pv.each { resultTeta += it.teta; resultPhi += it.phi; }
        resultTeta = (BigInteger) resultTeta / pv.size()
        resultPhi = (BigInteger) resultPhi / pv.size()
        while (resultTeta > D180) {
            resultTeta -= D180
            resultPhi += D180
        }
        while (resultPhi > D180) resultPhi -= DIV
        while (resultPhi <= -D180) resultPhi += DIV
        new PolarVector3i(DIV, resultTeta, resultPhi)
    }

    private validate() {
        if (r < 0G) throw new IllegalArgumentException("Spherical coord r cannot be neg")
        if (teta < 0G || teta > D180) throw new IllegalArgumentException("Spherical coord teta between 0 and PI")
        if ((teta == 0G || teta == D180) && phi != 0G) throw new IllegalArgumentException("Phi should be zero for teta 0 or PI")
        if (phi <= -D180 || phi > D180) throw new IllegalArgumentException("Spherical coord phi between -PI and PI")
    }

    public PolarVector3i(Point4i p1, Point4i p2) {
        this(new Vector3i(p1, p2))
    }

    public PolarVector3i(Vector3i v) {
        def d2 = v.magSquared()
        if (d2 == 0G) {
            r = phi = teta = 0G
        } else {
            def x = (double) v.x;
            def y = (double) v.y;
            def z = (double) v.z;
            def rd = Math.sqrt((double) d2)

            r = rd
            phi = CONVERTER * Math.atan2(y, x)
            teta = CONVERTER * Math.acos(z / rd)
        }
        validate()
    }

    public Vector3i toCartesian() {
        BigInteger sinTeta = sin(teta)
        BigInteger x = cos(phi) * sinTeta
        BigInteger y = sin(phi) * sinTeta
        BigInteger z = DIV * cos(teta)
        new Vector3i((BigInteger) x * r / (DIV * DIV), (BigInteger) y * r / (DIV * DIV), (BigInteger) z * r / (DIV * DIV))
    }

    public BigInteger d() {
        r
    }

    public BigInteger magSquared() {
        r * r
    }

    public PolarVector3i negative() {
        if (teta == 0G || teta == D180) {
            // Just reverse phi is zero anyway
            return new PolarVector3i(r, D180 - teta, 0G);
        }
        def nphi = D180 + phi
        if (nphi > D180) nphi -= DIV
        return new PolarVector3i(r, D180 - teta, nphi)
    }

    PolarVector3i multiply(BigInteger d) {
        if (d < 0G) {
            negative().multiply(-d)
        } else if (d == 0G) {
            new PolarVector3i(0G, 0G, 0G)
        } else {
            if (isNormalized()) {
                new PolarVector3i(d, teta, phi)
            } else {
                new PolarVector3i((BigInteger) (r * d) / DIV, teta, phi)
            }
        }
    }

    PolarVector3i normalized() {
        // Normalized on DIV
        new PolarVector3i(DIV, teta, phi)
    }

    BigInteger mod(PolarVector3i v) {
        dot(v)
    }

    BigInteger dot(PolarVector3i v) {
        // 1/4 * ( (2+cos(phi1-phi2)) * cos(teta1-teta2) + (2-cos(phi1-phi2)) * cos(teta1+teta2) )
        double cosPhi1MinusPhi2 = Math.cos((double) (this.phi - v.phi) / CONVERTER)
        double cosTeta1MinusTeta2 = Math.cos((double) (this.teta - v.teta) / CONVERTER)
        double cosTeta1PlusTeta2 = Math.cos((double) (this.teta + v.teta) / CONVERTER)
        this.r * v.r * (BigInteger) (2d + cosPhi1MinusPhi2) * cosTeta1MinusTeta2 + (2d - cosPhi1MinusPhi2) * cosTeta1PlusTeta2 / 4d
    }

    PolarVector3i cross(PolarVector3i v) {
        // Valid only on normalized v
        def me = normalizedToVectorDouble()
        def other = v.normalizedToVectorDouble()
        Vector3d result = me.cross(other)
        def rd = result.d()
        BigInteger resultPhi = CONVERTER * Math.atan2(result.y, result.x)
        BigInteger resultTeta = CONVERTER * Math.acos(result.z / rd)
        new PolarVector3i((BigInteger) DIV * rd, resultTeta, resultPhi)
    }

    public Vector3d normalizedToVectorDouble() {
        if (!isNormalized()) throw new IllegalArgumentException("$this is not a normalized vector")
        def myTetaD = teta / CONVERTER
        def myPhiD = phi / CONVERTER
        def mySinTeta = Math.sin(myTetaD)
        double myX = Math.cos(myPhiD) * mySinTeta
        double myY = Math.sin(myPhiD) * mySinTeta
        double myZ = Math.cos(myTetaD)
        new Vector3d(myX, myY, myZ)
    }

    public boolean isNormalized() {
        r == DIV
    }

    @Override
    boolean equals(Object obj) {
        PolarVector3i v = (PolarVector3i) obj
        r == v.r && teta == v.teta && phi == v.phi
    }

    String toString() {
        "vp($r, $teta, $phi)"
    }
}
