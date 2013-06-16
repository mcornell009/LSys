package PrimeNebula;

import PrimeSystem.PPoint;

 /**
  * 
  * @author michaelcornell
  * PrimeNebula, ploting prime numbers through turtle drawing
  *
  */
public class Nebula {
	
	public Point[] points;
	
	int theta = 1; //degrees
	double bearing; //radians
	double distance = 1; //how far you "step"	
	int limit = 100000; //maximum number of points permitted in the array
	
	public static void main(String[] args){
		new Nebula();
	}
	
	public Nebula() {
		System.out.println("Starting nebula gen");
		Point origin = new Point (0,0);
		points = new Point[]{origin};
		bearing = Math.PI / 2 * -1; //bearing starts facing up (12 o'clock)
		
		calculatePoints();
	}
	
	public void calculatePoints(){
		
		System.out.print("Target = "+limit+", calculating...");
		while (points.length < limit) {
			next();
		}
		
		System.out.println("Finished!");
		logNebula();
	}
	
	public double degToRad(double degrees) {
		return (degrees * (Math.PI / 180.0));
	}

	public double radToDeg(double rad) {
		return (rad * (180.0 / Math.PI));
	}
	
	public void logNebula(){
		for (Point p : points){
			System.out.println(p.toString());
		}
	}
	
	public String toString(){
		String ret = "{";
		for (Point p : points){
			ret += p.toString() + ", ";
		}
		ret += "}";
		return ret;
	}

	public void next() {
		
		//turn our bearing
		bearing += degToRad(theta);
		
		//make a copy of the last point in the system
		Point newpoint = new Point(points[points.length - 1]); 
		
		//move the point
		newpoint.x += (distance * Math.cos(bearing));
		newpoint.y += (distance * Math.sin(bearing));
		
		//dialate point away from center. Presumes center is 0,0, and scaling rate it 1.0 per step.
		//TODO maybe try scaling at doubling rate or golden ratio?
		if (newpoint.x>0) newpoint.x += 1;
		else newpoint.x -= 1;	
		if (newpoint.y>0) newpoint.y += 1;
		else newpoint.y -= 1;
		
		//append new point to point array	
		Point[] temp = new Point[points.length + 1];
		for (int count = 0; count < points.length; count++) {
			temp[count] = points[count];
		}
		int index = temp.length - 1;
		temp[index] = newpoint;
		points = temp;

	}
	
	private class Point{
		
		double x;
		double y;
		
		Point(double x, double y){
			this.x = x;
			this.y = y;
		}
		
		Point(Point parent){
			this.x = parent.x;
			this.y = parent.y;
		}
		
		public String toString(){
			return "("+this.x + ", "+this.y+")";
		}
	}

}
