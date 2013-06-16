package sleepygarden;


public class LPoint {
	
	public double x;
	public double y;
	
	public LPoint(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public LPoint(LPoint parent) {
		this.x = parent.x;
		this.y = parent.y;
	}
	
	public String toString(){
		return "("+x+", "+y+")";
	}
	

}
