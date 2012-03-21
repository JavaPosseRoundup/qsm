package org.freddy33.math;

/** A Coord4d stores a 3 dimensional coordinate for cartesian or polar mode,
 * and provide few operators.
 * 
 * Operators allow adding, substracting, multiplying and
 * divising coordinate values, as well as computing the distance between
 * two points, and converting polar and cartesian coordinates.
 * 
 * @author Martin Pernollet
 *
 */
class Coord4d {
    double x, y, z, t;

	/** The origin is a Coord4d having value 0 for each dimension.*/
	public static final Coord4d ORIGIN = new Coord4d()
	/** The origin is a Coord4d having value 1 for each dimension.*/
	public static final Coord4d IDENTITY = new Coord4d(1.0d, 1.0d, 1.0d, 0d);
	/** An invalid Coord4d has value NaN for each dimension.*/
	public static final Coord4d INVALID = new Coord4d(Double.NaN, Double.NaN, Double.NaN);
	
	/** Create a 3d coordinate with value 0 for each dimension.*/
	public Coord4d(){
		x = 0.0d
		y = 0.0d
		z = 0.0d
        t = 0.0d
	}
	
	public static Coord4d noTime(Coord4d c){
        return new Coord4d(c.x, c.y, c.z, 0d)
    }

	public Coord4d(double xi, double yi, double zi){
        this(xi, yi, zi, 0d)
    }

	public Coord4d(double xi, double yi, double zi, double ti){
		x = xi
		y = yi
		z = zi
        t = ti
	}
	
	public Coord4d(double[] c){
		x = c[0]
		y = c[1]
		z = c[2]
        t = c[3]
	}

	public Coord4d clone(){
		return new Coord4d(x,y,z,t);
	}
	
	/**************************************************************/
	
	public Coord4d add(Coord4d c2){
		return new Coord4d(x+c2.x, y+c2.y, z+c2.z)
	}
	
	public Coord4d addSelf(Coord4d c2){
		x+=c2.x;
		y+=c2.y;
		z+=c2.z;
		return this;
	}
	
	/** Add a value to all components of the current Coord and return the result
	 * in a new Coord4d.
	 * @param value
	 * @return the result Coord4d
	 */
	public Coord4d add(float value){
		return new Coord4d(x+value, y+value, z+value);
	}
	
	public Coord4d addSelf(float value){
		x+=value;
		y+=value;
		z+=value;
		return this;
	}
		
	/** Substract a Coord4d to the current one and return the result
	 * in a new Coord4d.
	 * @param c2
	 * @return the result Coord4d
	 */
	public Coord4d sub(Coord4d c2){
		return new Coord4d(x-c2.x, y-c2.y, z-c2.z);
	}
	
	public Coord4d subSelf(Coord4d c2){
		x-=c2.x;
		y-=c2.y;
		z-=c2.z;
		return this;
	}
	
	/** Substract a value to all components of the current Coord and return the result
	 * in a new Coord4d.
	 * @param value
	 * @return the result Coord4d
	 */
	public Coord4d sub(float value){
		return new Coord4d(x-value, y-value, z-value);
	}
	
	public Coord4d subSelf(float value){
		x-=value;
		y-=value;
		z-=value;
		return this;
	}
	
	/** Multiply a Coord4d to the current one and return the result
	 * in a new Coord4d.
	 * @param c2
	 * @return the result Coord4d
	 */
	public Coord4d mul(Coord4d c2){
		return new Coord4d(x*c2.x, y*c2.y, z*c2.z);
	}
	
	/** Multiply all components of the current Coord and return the result
	 * in a new Coord4d.
	 * @param value
	 * @return the result Coord4d
	 */
	public Coord4d mul(float value){
		return new Coord4d(x*value, y*value, z*value);
	}
	
	/** Divise a Coord4d to the current one and return the result
	 * in a new Coord4d.
	 * @param c2
	 * @return the result Coord4d
	 */
	public Coord4d div(Coord4d c2){
		return new Coord4d(x/c2.x, y/c2.y, z/c2.z);
	}
	
	/** Divise all components of the current Coord by the same value and return the result
	 * in a new Coord4d.
	 * @param value
	 * @return the result Coord4d
	 */
	public Coord4d div(float value){
		return new Coord4d(x/value, y/value, z/value);
	}
	
	public Coord4d negative(){
		return new Coord4d(-x, -y, -z);
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
	
	/** Compute the distance between two coordinates.*/
	public double distance(Coord4d c){
		return Math.sqrt( Math.pow(x-c.x, 2) + Math.pow(y-c.y, 2) + Math.pow(z-c.z, 2) );
	}
	
	/**************************************************************/

	public float magSquared() {
		return x * x + y * y + z * z;
	}
	
	public Coord4d getNormalizedTo(float len) {
		return clone().normalizeTo(len);
	}
	
	public Coord4d normalizeTo(float len) {
		float mag = (float) Math.sqrt(x * x + y * y + z * z);
		if (mag > 0) {
			mag = len / mag;
			x *= mag;
			y *= mag;
			z *= mag;
		}
		return this;
	}
	
	public final float dot(Coord4d v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public final Coord4d interpolateTo(Coord4d v, float f) {
		return new Coord4d(x + (v.x - x) * f, y + (v.y - y) * f, z + (v.z - z)
				* f);
	}
	
	/**************************************************************/
	
	/** Return a string representation of this coordinate.*/
	public String toString(){
		return ("x=" + x + " y=" + y + " z=" + z);
	}
	
	/**************************************************************/

	@Override 
	public boolean equals(Object aThat) {
		//check for self-comparison
		if ( this == aThat ) 
			return true;

		//use instanceof instead of getClass here for two reasons
		//1. if need be, it can match any supertype, and not just one class;
		//2. it renders an explict check for "that == null" redundant, since
		//it does the check for null already - "null instanceof [type]" always
		//returns false. (See Effective Java by Joshua Bloch.)
		if ( !(aThat instanceof Coord4d) )
			return false;
		//Alternative to the above line :
		//if ( aThat == null || aThat.getClass() != this.getClass() ) return false;

		//cast to native object is now safe
		Coord4d that = (Coord4d)aThat;

		//now a proper field-by-field evaluation can be made
		return ((this.x==that.x) && (this.y==that.y) && (this.z==that.z));
	}	

	@Override 
	public int hashCode() {
		if ( fHashCode == 0 ) {
			fHashCode = (int)(this.x*100000000.0f+this.y*10000.0f+this.z);
		}
		return fHashCode;
	}
	/**************************************************************/
	
	private int fHashCode;
}
