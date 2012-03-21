package org.freddy33.math;

class Vector4d {
    double x, y, z, t

	public Vector4d(Coord4d p1, Coord4d p2){
		x = p2.x - p1.x;
		y = p2.y - p1.y;
		z = p2.z - p1.z;
		t = p2.t - p1.t;
	}

	/**
	 * Compute the dot product between and current and given vector.
	 *
	 * @param v input vector
	 * @return the dot product
	 */
	public float dot(Vector4d v){
		return x*v.x + y*v.y + z*v.z + t*v.t;
	}

	/** Computes the vectorial product of the current and the given vector.
	 * The result is a vector defined as a Coord4d, that is perpendicular to
	 * the plan induced by current vector and vector V.*/
	public Vector4d cross(Vector4d v){
		Coord4d v1 = this.vector();
		Coord4d v2 = v.vector();
		Coord4d v3 = new Coord4d();
								  // V1    V2  =  V3
		v3.x = y * v.z - z * v.y; // x1    x2     x3  <-
		v3.y = z * v.x - x * v.z; // y1 \/ y2     y3
		v3.z = x * v.y - y * v.x; // z1 /\ z2     z3

		return v3; //TODO: should return a vector! Vector4d(V3, Coord4d.ORIGIN)
	}

	/**
	 * Compute the norm of this vector.
	 * @return the norm
	 */
	public float norm(){
		return (float)Math.sqrt( Math.pow(x2-x1,2) + Math.pow(y2-y1,2) + Math.pow(z2-z1,2) );
	}

    /**
     * Reverse the direction of this vector
     * @return a new vector with opposite direction
     */
    public Vector4d neg() {
        return new Vector4d(this.x2,this.y2,this.z2,this.x1,this.y1,this.z1);
    }

	/***********************************************************/

	/** Compute the distance between two coordinates.*/
	public double distance(Coord4d c){
		return getCenter().distance(c);
	}

	/**Return the central point of this segment.*/
	public Coord4d getCenter(){
		float cx  = (x1+x2)/2;
		float cy  = (y1+y2)/2;
		float cz  = (z1+z2)/2;
		return new Coord4d(cx, cy, cz);
	}
		
	/***********************************************************/
	
	private double x1;
	private double x2;
	private double y1;
	private double y2;
	private double z1;
	private double z2;
}
