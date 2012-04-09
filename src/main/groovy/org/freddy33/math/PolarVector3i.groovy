package org.freddy33.math;

class PolarVector3i {
    static BigInteger DIV = 46656G
    static BigInteger D180 = DIV / 2G
    static BigInteger D90 = DIV / 4G
    static BigInteger D120 = DIV / 3G
    static double CONVERTER = DIV / (2d * Math.PI)

    final BigInteger r, teta /* [0, pi] */, phi /* [-pi, pi] */

    public PolarVector3i(BigInteger pr, pteta, pphi) {
        r = pr
        teta = pteta
        phi = pphi
        validate()
    }

    private validate() {
        if (r < 0G) throw new IllegalArgumentException("Spherical coord r cannot be neg")
        if (teta < 0G || teta > D180) throw new IllegalArgumentException("Spherical coord teta between 0 and PI")
        if (phi < -D180 || phi > D180) throw new IllegalArgumentException("Spherical coord phi between -PI and PI")
    }

    public PolarVector3i(Coord4i p1, Coord4i p2) {
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
        def tetaD = teta / CONVERTER
        def phiD = phi / CONVERTER
        def rSinTeta = r * Math.sin(tetaD)
        double x = Math.cos(phiD) * rSinTeta
        double y = Math.sin(phiD) * rSinTeta
        double z = r * Math.cos(tetaD)
        new Vector3i((BigInteger) x, (BigInteger) y, (BigInteger) z)
    }

    public BigInteger d() {
        r
    }

    public BigInteger magSquared() {
        r * r
    }

    public PolarVector3i negative() {
        def nphi = D180 + phi
        if (nphi > D180) nphi -= DIV
        return new PolarVector3i(r, D180 - teta, nphi);
    }

    PolarVector3i multiply(BigInteger d) {
        new PolarVector3i(r * d, teta, phi)
    }

    PolarVector3i normalized() {
        // Normalized on DIV
        new PolarVector3i(DIV, teta, phi)
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
