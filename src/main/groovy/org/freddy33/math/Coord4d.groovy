package org.freddy33.math;

class Coord4d {
    double x, y, z, t;

	public Coord4d(double xi, double yi, double zi){
        this(xi, yi, zi, 0d)
    }

	public Coord4d(double xi, double yi, double zi, double ti){
		x = xi
		y = yi
		z = zi
        t = ti
	}

	public Coord4d clone() {
		return new Coord4d(x,y,z,t);
	}
	
	public Coord4d plus(Vector4d v){
		return new Coord4d(x+v.x, y+v.y, z+v.z, t+v.t)
	}

	public Coord4d minus(Vector4d v){
		new Coord4d(x-v.x, y-v.y, z-v.z, t)
	}

    public Coord4d multiply(double m){
        new Coord4d(x*m, y*m, z*m, t)
    }

    public double d(Coord4d c){
        new Vector4d(this, c).d()
    }

    String toString() {
        "p($x, $y, $z, $t)"
    }

    @Override
    boolean equals(Object obj) {
        MathUtils.eq(this,(Coord4d)obj)
    }

    /** Converts the current Coord4d into cartesian coordinates
	 * and return the result in a new Coord4d.
	 * @return the result Coord4d
	 */
	public Coord4d cartesian(){
		return new Coord4d(
				Math.cos(x) * Math.cos(y) * z, // azimuth
				Math.sin(x) * Math.cos(y) * z, // elevation
				Math.sin(y) * z);              // range
		/*return new Coord4d(
				Math.sin(x) * Math.cos(y) * z, // azimuth
				Math.sin(x) * Math.sin(y) * z, // elevation
				Math.cos(x) * z); // range*
		
		return new Coord4d(
                Math.cos(x) * Math.cos(y) * z, // azimuth
                Math.sin(x) * Math.cos(y) * z, // elevation
                Math.sin(y) * z); // range*/
	}
	
	/** Converts the current Coord4d into polar coordinates
	 * and return the result in a new Coord4d.
	 * @return the result Coord4d
	 */
	public Coord4d polar(){
		double a;
		double e;
		double r = Math.sqrt( x*x + y*y + z*z );
		double d = Math.sqrt( x*x + y*y );
		
		// case x=0 and y=0
		if(d==0 && z>0)
			return new Coord4d(0, Math.PI/2, r);
		else if(d==0 && z<=0)
			return new Coord4d(0, -Math.PI/2, r);
		// other cases
		else{
			// classical case for azimuth
			if(Math.abs(x/d)<1)
				a = Math.acos(x/d) * (y>0?1:-1);
			// special on each pole for azimuth
			else if(y==0 && x>0) //y==0
				a = 0;
			else if(y==0 && x<0)
				a = Math.PI;
			else
				a=0;
			
			
			e = Math.atan(z/d);
			
			return new Coord4d(a, e, r);
		}
			
		
		/*return new Coord4d(
				Math.atan(y/x), // azimuth
				Math.acos(z/r), // elevation 
				r); // range*/
	}
}
