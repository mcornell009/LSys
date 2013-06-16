package PrimeSystem;

import org.newdawn.slick.geom.Circle;

public class PPoint extends Circle {

	public PPoint(float centerPointX, float centerPointY) {
		super(centerPointX, centerPointY, 1);
	}
	
	public PPoint (PPoint parent) {
		super(parent.getX(),parent.getY(),parent.getRadius());
	}
	
	public void setX(double x){
		super.setX((float)x);
	}
	public void setY(double y){
		super.setY((float)y);
	}

}
