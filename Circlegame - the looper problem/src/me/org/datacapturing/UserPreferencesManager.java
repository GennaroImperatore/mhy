package me.org.datacapturing;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import me.org.datacapturing.UserInfo.SIDE;

public class UserPreferencesManager {

	private final String PREF_NAME = "USER_INFO";
	private Context ctx;
	private String HAVE_BEEN_SET_KEY ="haveBeenSet";
	private String HANDEDNESS_KEY = "handedness";
	private String EYEDNESS_KEY = "eyedness";
	private String NOTP_KEY = "notp";
	private String HISCORE_KEY ="hiscore";
	
	public static UserPreferencesManager instance;
	
	private UserPreferencesManager()
	{
		
	}
	
	public void setContext(Context ct)
	{
		ctx = ct;
	}
	
	public static UserPreferencesManager getInstance()
	{
		if(instance == null)
			instance = new UserPreferencesManager();
		return instance;
	}

	
	public boolean preferencesHaveBeenSet()
	{
		SharedPreferences prefs;
		prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		Log.d("SHARED_PREFS_CIRCLEGAME", String.valueOf(prefs.contains(HAVE_BEEN_SET_KEY)));
		
		return prefs.contains(HAVE_BEEN_SET_KEY);
	}
	
	public void preferenceshaveBeensetToTrue()
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		Editor editor = prefs.edit();
		editor.putBoolean(HAVE_BEEN_SET_KEY, true);
		
		editor.commit();
	}
	
	public void setHandedness (SIDE h)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
	
		Editor editor = prefs.edit();
		editor.putString(HANDEDNESS_KEY, h.toString());
		editor.commit();
	}
	
	public void setEyedness (SIDE e)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		Editor editor = prefs.edit();
		editor.putString(EYEDNESS_KEY, e.toString());
		editor.commit();
	}
	
	public void increaseNotp ()
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		Editor editor = prefs.edit();
		editor.putInt (NOTP_KEY, getNOTP()+1);
		editor.commit();
	}
	
	public String getHandedness()
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		return prefs.getString(HANDEDNESS_KEY, null);
	}
	
	public String getEyedness()
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		return prefs.getString(EYEDNESS_KEY, null);
	}
	
	public int getNOTP()
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		return prefs.getInt(NOTP_KEY, 0);
	}
	
	public void resetUserPreferences()
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		Editor editor = prefs.edit();
		editor.remove(HAVE_BEEN_SET_KEY);
		editor.commit();
	}
	
	public void setNotp (int notp)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		Editor editor = prefs.edit();
		editor.putInt(NOTP_KEY, notp);
	}
	
	public int getHiScore() 
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		return prefs.getInt(HISCORE_KEY, 0);
	}
	
	public void resetHiScore()
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		Editor editor = prefs.edit();
		editor.putInt(HISCORE_KEY, 0);
		editor.commit();
	}
	
	public void updateHiScore(int currentScore)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, 0);
		
		if (currentScore > prefs.getInt(HISCORE_KEY, 0))
		{

		System.out.println("Attempting to update hiscoe");
		Editor editor = prefs.edit();
		editor.putInt(HISCORE_KEY, currentScore);
		editor.commit();
	
		
		}
	}
	
}
