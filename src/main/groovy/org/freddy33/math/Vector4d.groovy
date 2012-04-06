package org.freddy33.math;

class Vector4d {
    double x, y, z, t

	public Vector4d(double px, py, pz) { this(px,py,pz,0d) }

    public Vector4d(double px, py, pz, pt){
        x = px
        y = py
        z = pz
        t = pt
    }

    public Vector4d(Coord4d p1, Coord4d p2){
		x = p2.x - p1.x;
		y = p2.y - p1.y;
		z = p2.z - p1.z;
		t = p2.t - p1.t;
	}

	public double mod(Vector4d v){
        dot(v)
    }

	public double dot(Vector4d v){
		x*v.x + y*v.y + z*v.z + t*v.t
	}

	public Vector4d xor(Vector4d v){
        cross(v)
    }

	public Vector4d cross(Vector4d v){
		new Vector4d(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        )
	}

	/**
	 * Compute the d of this vector.
	 * @return the d
	 */
	public double d() {
		Math.sqrt(magSquared())
	}

    public double magSquared() {
        (x*x) + (y*y) + (z*z)
    }

    public Vector4d negative() {
        return new Vector4d(-x,-y,-z,-t);
    }

    Vector4d multiply(double d) {
        new Vector4d(x*d,y*d,z*d,t*d)
    }

    Vector4d normalized() {
        double d = d()
        new Vector4d(x/d,y/d,z/d,0d)
    }

    Vector4d plus(Vector4d v) {
        new Vector4d(x+v.x,y+v.y,z+v.z,t+v.t)
    }

    Vector4d minus(Vector4d v) {
        new Vector4d(x-v.x,y-v.y,z-v.z,t-v.t)
    }

    Vector4d addSelf(Vector4d v) {
        x += v.x
        y += v.y
        z += v.z
        t += v.t
        return this
    }

    @Override
    boolean equals(Object obj) {
        MathUtils.eq(this,(Vector4d)obj)
    }

    String toString() {
        "v($x, $y, $z, $t)"
    }
}
