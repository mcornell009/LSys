package PrimeSystem;

import java.awt.Point;

import org.newdawn.slick.geom.Line;

import sleepygarden.LPoint;

public class PDrawer {
	public PPoint[] array;
	public PPoint center;

	public double x, y;

	public double distance;
	public int theta;
	public double heading = 0;

	boolean irisMode = false;
	boolean spinMode = false;

	public PDrawer(PPoint[] p) {
		this.array = p;
	}

	public void setTheta(int theta) {
		this.theta = theta;
	}
	
	public PPoint[] getPoints(){
		return this.array;
	}
	
	public double distance(PPoint a, PPoint b) {
		return (b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY()) * (b.getY() - a.getY());
	}

	public void setDistance(double dist) {
		this.distance = dist;
	}

	public void resetHeading() {
		heading = Math.PI / 2 * -1;
	}

	public String toString() {

		String ret = "";

		for (int count = 0; count < array.length; count++) {
			ret = ret + "Point " + count + ": (" + array[count].getX() + ", "
					+ array[count].getY() + ") \n";
		}
		return ret;
	}

	public void move(double x, double y) {
		for (PPoint point : array) {
			movePoint(point, x, y);
		}
	}

	public void moveToPoint(PPoint point, double x, double y) {
		point.setX(x);
		point.setY(y);
	}

	public void scale(double coeff) {
		refreshCenter();
		for (PPoint point : array) {
			scalePointFromCenter(point, coeff);
		}
	}

	public void scalePointFromCenter(PPoint point, double coeff) {
		refreshCenter();
		point.setX(point.getX() + (point.getX() - center.getX()) * coeff);
		point.setY(point.getY() + (point.getY() - center.getY()) * coeff);
	}

	public void rotateAroundPoint(PPoint point, PPoint origin, double rads) {
		double s = Math.sin(rads);
		double c = Math.cos(rads);

		// translatePPoint back to origin:
		point.setX(point.getX() - origin.getX());
		point.setY(point.getY() - origin.getY());

		// rotatePPoint
		double xnew = point.getX() * c - point.getY() * s;
		double ynew = point.getX() * s + point.getY() * c;

		// translatePPoint back to global coords:
		point.setX(xnew + origin.getX());
		point.setY(ynew + origin.getY());
	}

	public void rotatePoint(PPoint point, double rads) {
		refreshCenter();
		double s = Math.sin(rads);
		double c = Math.cos(rads);

		point.setX(point.getX() - center.getX());
		point.setY(point.getY() - center.getY());

		double xnew = point.getX() * c - point.getY() * s;
		double ynew = point.getX() * s + point.getY() * c;

		point.setX(xnew + center.getX());
		point.setY(ynew + center.getY());

	}

	public void rotate(double rads) {
		for (PPoint point : array) {
			rotatePoint(point, rads);
		}
	}

	public double getRadius() {
		refreshCenter();
		double r = 0;
		for (PPoint point : array) {
			if (Math.abs(distance(point, center)) > r)
				r = Math.abs(distance(point, center));
		}
		return r;
	}

	public PPoint getCenter() {
		return center;
	}

	public void refreshCenter() {
		PPoint center = new PPoint(0, 0);
		double length = 0; // more precision than array.length
		for (PPoint point : array) {
			center.setX(center.getX() + point.getX());
			center.setY(center.getY() + point.getY());
			length++;
		}
		center.setX(center.getX() / length);
		center.setY(center.getY() / length);
		this.center = center;
	}

	public void clear() {
		System.out.println("clearing " + array.length + " points");
		array = null;
		center = null;
		heading = 0;
	}

	public void turnLeft() {
		heading -= degToRad(theta);
	}

	public void turnRight() {
		heading += degToRad(theta);
	}

	public double degToRad(double degrees) {
		return (degrees * (Math.PI / 180.0));
	}

	public double radToDeg(double rad) {
		return (rad * (180.0 / Math.PI));
	}

	public void drawForward() {

		PPoint newpoint = new PPoint(array[array.length - 1]);
		
		movePoint(newpoint, (distance * Math.cos(heading)),
				(distance * Math.sin(heading)));
		appendPoint(newpoint);

	}

	public void movePoint(PPoint point, double x, double y) {
		point.setX(point.getX() + x);
		point.setY(point.getY() + y);
	}

	public void appendPoint(PPoint point) {

		// if (array.length >= 10000) { //if array is to large, move every point
		// down the array, forcing out the first item
		// PPoint[] temp = new PPoint[array.length-1];
		// for (int count = 0; count<temp.length; count++){
		// temp[count] = array[count+1];
		// }
		// array = temp;
		//
		// }

		PPoint[] temp = new PPoint[array.length + 1];
		for (int count = 0; count < array.length; count++) {
			temp[count] = array[count];
		}
		int index = temp.length - 1;
		temp[index] = point;
		array = temp;
		refreshCenter();
		//System.out.println("heading "+heading);
	}

}
