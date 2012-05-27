package org.freddy33.math.bigInt

public class SphericalVector3i {
    public static final Map<BigInteger, BigInteger> trigMap = [:]
    public static final Map<BigInteger, BigInteger> invTrigMap = [:]

    public static double CONVERTER = (MathUtilsInt.DIV / (2d * Math.PI))

    static {
        BigInteger lastSinVal = null
        for (i in 0G..MathUtilsInt.D90) {
            BigInteger sinVal = (BigInteger) (MathUtilsInt.ONE * Math.sin(i / CONVERTER))
            trigMap.put(i, sinVal)
            if (!invTrigMap.containsKey(sinVal)) invTrigMap.put(sinVal, i)
            if (lastSinVal != null && (sinVal - 1G) > lastSinVal) {
                // Some holes to fill
                BigInteger holeSize = sinVal - lastSinVal - 1G
                for (h in 1G..holeSize) {
                    invTrigMap.put(lastSinVal + h, i - 1G)
                }
            }
            lastSinVal = sinVal
        }
        println "Trig map found for PI=$MathUtilsInt.D180 ONE=$MathUtilsInt.ONE trigSize=${trigMap.size()} and invTrigSize=${invTrigMap.size()}"
//        println "Trig map=${trigMap}"
//        println "Invers Trig map=${invTrigMap}"
    }

    static BigInteger atan2(BigInteger y, BigInteger x) {
        if (x == 0G) {
            if (y > 0G) {
                return MathUtilsInt.D90
            } else if (y < 0) {
                return -MathUtilsInt.D90
            } else {
                return 0G
            }
        } else {
            BigInteger arctan = asin((BigInteger) (y / Math.sqrt((double) (x * x + y * y))))
            if (x > 0G) {
                return arctan
            } else if (x < 0G) {
                if (arctan == 0G) {
                    return MathUtilsInt.D180
                } else if (y < 0G) {
                    return arctan - MathUtilsInt.D180
                } else {
                    return arctan + MathUtilsInt.D180
                }
            }
        }
    }

    static BigInteger acos(BigInteger cosVal) {
        MathUtilsInt.D90 - asin(cosVal)
    }

    static BigInteger asin(BigInteger sinVal) {
        if (sinVal < -MathUtilsInt.ONE || sinVal > MathUtilsInt.ONE) {
            throw new IllegalArgumentException("A sin value cannot be ${sinVal} above or below +- ${MathUtilsInt.ONE}")
        }
        if (sinVal < 0G) {
            return -invTrigMap.get(-sinVal)
        } else {
            return invTrigMap.get(sinVal)
        }
    }

    static BigInteger sin(BigInteger angle) {
        if (angle >= 0G && angle <= MathUtilsInt.D90) {
            return trigMap[angle]
        } else if (angle < 0G && angle >= -MathUtilsInt.D90) {
            return -trigMap[-angle]
        } else if (angle > MathUtilsInt.D90 && angle <= MathUtilsInt.D180) {
            return trigMap[MathUtilsInt.D180 - angle]
        } else if (angle < -MathUtilsInt.D90 && angle >= -MathUtilsInt.D180) {
            return -trigMap[MathUtilsInt.D180 + angle]
        } else if (angle < -MathUtilsInt.D180) {
            return sin(angle + MathUtilsInt.DIV)
        } else if (angle > MathUtilsInt.D180) {
            return sin(angle - MathUtilsInt.DIV)
        } else {
            throw new RuntimeException("Don't know how to count!")
        }
    }

    static BigInteger cos(BigInteger angle) {
        sin(angle + MathUtilsInt.D90)
    }

    final BigInteger r, teta /* [0, pi] */, phi /* [-pi, pi] */

    /**
     * Create a normalized spherical vector for teta phi
     * @param pteta
     * @param pphi
     */
    public SphericalVector3i(BigInteger pteta, pphi) {
        this(MathUtilsInt.ONE, pteta, pphi)
    }

    public SphericalVector3i(BigInteger pr, pteta, pphi) {
        r = pr
        teta = pteta
        if (pteta == 0G || pteta == MathUtilsInt.D180) {
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
        while (resultTeta > MathUtilsInt.D180) {
            resultTeta -= MathUtilsInt.D180
            resultPhi += MathUtilsInt.D180
        }
        while (resultPhi > MathUtilsInt.D180) resultPhi -= MathUtilsInt.DIV
        while (resultPhi <= -MathUtilsInt.D180) resultPhi += MathUtilsInt.DIV
        new SphericalVector3i(resultTeta, resultPhi)
    }

    private validate() {
        if (r < 0G) throw new IllegalArgumentException("Spherical coord r cannot be neg! For $this")
        if (teta < 0G || teta > MathUtilsInt.D180) throw new IllegalArgumentException("Spherical coord teta between 0 and PI=$MathUtilsInt.D180! For $this")
        if ((teta == 0G || teta == MathUtilsInt.D180) && phi != 0G) throw new IllegalArgumentException("Phi should be zero for teta 0 or PI=$MathUtilsInt.D180! For $this")
        if (phi <= -MathUtilsInt.D180 || phi > MathUtilsInt.D180) throw new IllegalArgumentException("Spherical coord phi between -PI=${-MathUtilsInt.D180} and PI=$MathUtilsInt.D180! For $this")
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
        BigInteger z = MathUtilsInt.ONE * cos(teta)
        new Vector3i((BigInteger) x * r / (MathUtilsInt.ONE * MathUtilsInt.ONE), (BigInteger) y * r / (MathUtilsInt.ONE * MathUtilsInt.ONE), (BigInteger) z * r / (MathUtilsInt.ONE * MathUtilsInt.ONE))
    }

    public BigInteger d() {
        r
    }

    public BigInteger magSquared() {
        r * r
    }

    public SphericalVector3i negative() {
        if (teta == 0G || teta == MathUtilsInt.D180) {
            // Just reverse phi is zero anyway
            return new SphericalVector3i(r, MathUtilsInt.D180 - teta, 0G);
        }
        def nphi = MathUtilsInt.D180 + phi
        if (nphi > MathUtilsInt.D180) nphi -= MathUtilsInt.DIV
        return new SphericalVector3i(r, MathUtilsInt.D180 - teta, nphi)
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
                new SphericalVector3i((BigInteger) (r * d) / MathUtilsInt.ONE, teta, phi)
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
        (BigInteger) (this.r * v.r * ((2G * MathUtilsInt.ONE + cosPhi1MinusPhi2) * cosTeta1MinusTeta2 + (2G * MathUtilsInt.ONE - cosPhi1MinusPhi2) * cosTeta1PlusTeta2) / (MathUtilsInt.ONE * MathUtilsInt.ONE))
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
        BigInteger myZ = cos(teta) * MathUtilsInt.ONE
        new Vector3i(myX, myY, myZ)
    }

    public boolean isNormalized() {
        r == MathUtilsInt.ONE
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
