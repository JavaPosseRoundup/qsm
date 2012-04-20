package org.freddy33.math;

public class SphericalVector3i {
    public static final Map<BigInteger, BigInteger> trigMap = [:]
    public static final Map<BigInteger, BigInteger> invTrigMap = [:]
    public static final BigInteger D30 = 3888G
    public static final BigInteger D90 = D30 * 3G
    public static final BigInteger DIV = D90 * 4G
    public static final BigInteger D180 = D90 * 2G
    public static final BigInteger D120 = D30 * 4G

    static double CONVERTER = DIV / (2d * Math.PI)
    static {
        BigInteger lastSinVal
        for (i in 0G..D90) {
            BigInteger sinVal = (BigInteger) (DIV * Math.sin(i / CONVERTER))
            trigMap.put(i, sinVal)
            invTrigMap.put(sinVal, i)
            if (lastSinVal != null && sinVal - 1G > lastSinVal) {
                // Some holes to fill
                BigInteger holeSize = sinVal - lastSinVal - 1G
                for (h in 1G..holeSize) {
                    invTrigMap.put(lastSinVal + h, i - 1G)
                }
            }
            lastSinVal = sinVal
        }
        println "Trig map found ${trigMap.size()} and ${invTrigMap.size()}"
    }

    static BigInteger atan2(BigInteger y, BigInteger x) {
        if (x == 0G) {
            if (y > 0G) {
                return D90
            } else if (y < 0) {
                return -D90
            } else {
                return 0G
            }
        } else {
            BigInteger arctan = asin((BigInteger) (y / Math.sqrt((double) (x * x + y * y))))
            if (x > 0G) {
                return arctan
            } else if (x < 0G) {
                if (arctan == 0G) {
                    return D180
                } else if (y < 0G) {
                    return arctan - D180
                } else {
                    return arctan + D180
                }
            }
        }
    }

    static BigInteger acos(BigInteger cosVal) {
        D90 - asin(cosVal)
    }

    static BigInteger asin(BigInteger sinVal) {
        if (sinVal < -DIV || sinVal > DIV) {
            throw new IllegalArgumentException("A sin value cannot be ${sinVal} above or below +- ${DIV}")
        }
        if (sinVal < 0G) {
            return -invTrigMap.get(-sinVal)
        } else {
            return invTrigMap.get(sinVal)
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

    public SphericalVector3i(BigInteger pr, pteta, pphi) {
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

    static SphericalVector3i middleMan(List<SphericalVector3i> pv) {
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
        new SphericalVector3i(DIV, resultTeta, resultPhi)
    }

    private validate() {
        if (r < 0G) throw new IllegalArgumentException("Spherical coord r cannot be neg! For $this")
        if (teta < 0G || teta > D180) throw new IllegalArgumentException("Spherical coord teta between 0 and PI! For $this")
        if ((teta == 0G || teta == D180) && phi != 0G) throw new IllegalArgumentException("Phi should be zero for teta 0 or PI! For $this")
        if (phi <= -D180 || phi > D180) throw new IllegalArgumentException("Spherical coord phi between -PI and PI! For $this")
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
        BigInteger z = DIV * cos(teta)
        new Vector3i((BigInteger) x * r / (DIV * DIV), (BigInteger) y * r / (DIV * DIV), (BigInteger) z * r / (DIV * DIV))
    }

    public BigInteger d() {
        r
    }

    public BigInteger magSquared() {
        r * r
    }

    public SphericalVector3i negative() {
        if (teta == 0G || teta == D180) {
            // Just reverse phi is zero anyway
            return new SphericalVector3i(r, D180 - teta, 0G);
        }
        def nphi = D180 + phi
        if (nphi > D180) nphi -= DIV
        return new SphericalVector3i(r, D180 - teta, nphi)
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
                new SphericalVector3i((BigInteger) (r * d) / DIV, teta, phi)
            }
        }
    }

    SphericalVector3i normalized() {
        // Normalized on DIV
        new SphericalVector3i(DIV, teta, phi)
    }

    BigInteger mod(SphericalVector3i v) {
        dot(v)
    }

    BigInteger dot(SphericalVector3i v) {
        // 1/4 * ( (2+cos(phi1-phi2)) * cos(teta1-teta2) + (2-cos(phi1-phi2)) * cos(teta1+teta2) )
        BigInteger cosPhi1MinusPhi2 = cos(this.phi - v.phi)
        BigInteger cosTeta1MinusTeta2 = cos(this.teta - v.teta)
        BigInteger cosTeta1PlusTeta2 = cos(this.teta + v.teta)
        (BigInteger) (this.r * v.r * ((2G * DIV + cosPhi1MinusPhi2) * cosTeta1MinusTeta2 + (2G * DIV - cosPhi1MinusPhi2) * cosTeta1PlusTeta2) / (DIV * DIV))
    }

    SphericalVector3i cross(SphericalVector3i v) {
        // Valid only on normalized v
        def me = normalizedToVectorInt()
        def other = v.normalizedToVectorInt()
        Vector3i result = me.cross(other)
        def rd = Math.sqrt((double) result.magSquared())
        BigInteger resultPhi = CONVERTER * Math.atan2((double) result.y, (double) result.x)
        BigInteger resultTeta = CONVERTER * Math.acos((double) result.z / rd)
        new SphericalVector3i(DIV, resultTeta, resultPhi)
    }

    public Vector3i normalizedToVectorInt() {
        if (!isNormalized()) throw new IllegalArgumentException("$this is not a normalized vector")
        BigInteger myX = cos(phi) * sin(teta)
        BigInteger myY = sin(phi) * sin(teta)
        BigInteger myZ = cos(teta) * DIV
        new Vector3i(myX, myY, myZ)
    }

    public boolean isNormalized() {
        r == DIV
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
