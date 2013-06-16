package sleepygarden;

import java.awt.Point;

import org.newdawn.slick.geom.Line;

public class LDrawer {
	public LPoint[] array;
	public LPoint center;

	public double x, y;

	public double distance;
	public int theta;
	public double heading = 0;

	boolean irisMode = false;
	boolean spinMode = false;

	public int[] primes;

	public LDrawer(LPoint[] p) {
		this.array = p;
	}

	public Line[] getLines() {

		if (array.length > 1) { // one point a line does not make

			Line[] lines = new LLine[array.length]; // if you have an
													// unfinished line it'll
													// get cut off, no
													// worries

			for (int count = 0; count < lines.length; count++) {
				if (!(count == array.length - 1)) {
					lines[count] = new LLine(array[count].x, array[count].y,
							array[count + 1].x, array[count + 1].y);
				}

			}
			return lines;
		} else
			return null;
	}

	public double distance(LPoint a, LPoint b) {
		return (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y);
	}

	public void setTheta(int theta) {
		this.theta = theta;
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
			ret = ret + "Point " + count + ": (" + array[count].x + ", "
					+ array[count].y + ") \n";
		}
		return ret;
	}

	public void move(double x, double y) {
		for (LPoint lp : array) {
			movePoint(lp, x, y);
		}
	}

	public void moveToPoint(LPoint lp, double x, double y) {
		lp.x = x;
		lp.y = y;
	}

	public void scale(double coeff) {
		refreshCenter();
		for (LPoint lp : array) {
			scalePointFromCenter(lp, coeff);
		}

	}

	public void scalePointFromCenter(LPoint lp, double coeff) {
		refreshCenter();
		lp.x = lp.x + (lp.x - center.x) * coeff;
		lp.y = lp.y + (lp.y - center.y) * coeff;
	}

	public void rotateAroundPoint(LPoint lp, LPoint origin, double rads) {
		double s = Math.sin(rads);
		double c = Math.cos(rads);

		// translatelpoint back to origin:
		lp.x -= origin.x;
		lp.y -= origin.y;

		// rotatelpoint
		double xnew = lp.x * c - lp.y * s;
		double ynew = lp.x * s + lp.y * c;

		// translatelpoint back to global coords:
		lp.x = xnew + origin.x;
		lp.y = ynew + origin.y;
	}

	public void rotatePoint(LPoint point, double rads) {
		refreshCenter();
		double s = Math.sin(rads);
		double c = Math.cos(rads);

		point.x -= center.x;
		point.y -= center.y;

		double xnew = point.x * c - point.y * s;
		double ynew = point.x * s + point.y * c;

		point.x = xnew + center.x;
		point.y = ynew + center.y;

	}

	public void rotate(double rads) {
		for (LPoint lp : array) {
			rotatePoint(lp, rads);
		}
	}

	public double getRadius() {
		refreshCenter();
		double r = 0;
		for (LPoint lp : array) {
			if (Math.abs(distance(lp, center)) > r)
				r = Math.abs(distance(lp, center));
		}
		return r;
	}

	public LPoint getCenter() {
		return center;
	}

	public void refreshCenter() {
		LPoint center = new LPoint(0, 0);
		double length = 0; // more precision than array.length
		for (LPoint lp : array) {
			center.x += lp.x;
			center.y += lp.y;
			length++;
		}
		center.x = center.x / length;
		center.y = center.y / length;
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

	public void mutate() {

	}

	public double degToRad(double degrees) {
		return (degrees * (Math.PI / 180.0));
	}

	public double radToDeg(double rad) {
		return (rad * (180.0 / Math.PI));
	}

	public void drawForward() {
		double targetHeading = heading;
		if (irisMode) {
			targetHeading = Math.floor(targetHeading);
		} else if (spinMode) {
			targetHeading = (float) targetHeading;
		}

		LPoint newlp = new LPoint(array[array.length - 1]);
		movePoint(newlp, (distance * Math.cos(targetHeading)),
				(distance * Math.sin(targetHeading)));
		appendPoint(newlp);

	}

	public void movePoint(LPoint lp, double x, double y) {
		lp.x += x;
		lp.y += y;
	}

	public void appendPoint(LPoint lp) {

		// if (array.length >= 10000) { //if array is to large, move every point
		// down the array, forcing out the first item
		// LPoint[] temp = new LPoint[array.length-1];
		// for (int count = 0; count<temp.length; count++){
		// temp[count] = array[count+1];
		// }
		// array = temp;
		//
		// }

		LPoint[] temp = new LPoint[array.length + 1];
		for (int count = 0; count < array.length; count++) {
			temp[count] = array[count];
		}
		int index = temp.length - 1;
		temp[index] = lp;
		array = temp;
		refreshCenter();
		System.out.println("heading "+heading);
	}

}
