package me.org.datacapturing;

public class DataForPosting {

	//this encapsulates the data so we can send different types to the post
	
	private int touchX;
	private int touchY;
	private int circleX;
	private int circleY;
	private int type;
	private float tapTime;
	
	
	public DataForPosting(int cx, int cy, int tx ,int ty, int tp, float taptime)
	{
		touchX =tx;
		touchY = ty;
		circleX =cx;
		circleY =cy;
		type = tp;
		tapTime = taptime;
	}
	
	public int getTouchX() {return touchX;}
	public int getTouchY(){return touchY;}
	public int getCircleX() {return circleX;}
	public int getCircleY() {return circleY;}
	public int getType() {return type;}
	public float getTapTime() {return tapTime;}
}
