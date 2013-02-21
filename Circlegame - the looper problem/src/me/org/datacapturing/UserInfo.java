package me.org.datacapturing;

public class UserInfo {
	
	
	
	public enum SIDE {LEFT, RIGHT, NONE};
	
	private int nOfTimesPlayed=0;
	private SIDE eydness;
	private SIDE handedness;
	private String ID;
	
	
	public UserInfo()
	{
		
	}
	
	public UserInfo (SIDE h, SIDE e, int notp)
	{
		eydness = e;
		handedness = h;
		nOfTimesPlayed = notp;
	}
	
	public SIDE getHandedness() {return handedness;}
	public SIDE getEydness() {return eydness;}
	public int getNOTP() {return nOfTimesPlayed;}
	public String getID() {return ID;}
	
	
	public void setHandedness (SIDE h ) { handedness =h;}
	public void setEyedness (SIDE e ) { eydness = e; }
	public void setNOTP(int n) { nOfTimesPlayed =n; }
	public void setID (String id) {ID = id; }

}
