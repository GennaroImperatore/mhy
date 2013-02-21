package me.org.datacapturing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import me.org.datacapturing.DataManager.UncorrectUserInfoDataException;
import me.org.datacapturing.UserInfo.SIDE;
import me.org.game.SuperGame;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.badlogic.gdx.utils.JsonReader;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;


import java.util.List;

public class DataPoster extends AsyncTask<DataForPosting, Float, Boolean> {
	
	private static final String X_CIRCLE_POST ="xcircle";
	private static final String Y_CIRCLE_POST ="ycircle";
	private static final String X_TOUCH_POST ="xtouch";
	private static final String Y_TOUCH_POST ="ytouch";
	private static final String HANDEDNESS_POST ="handedness";
	private static final String EYDNESS_POST = "eyedness";
	private static final String CIRCLE_TYPE ="ctype";
	private static final String NOTP_POST = "notp";
	private static final String TAPTIME_POST ="taptime";
	
	private static final String ID_POST ="serial";
	
	private static final String POST_SCRIPT_NAME = "postgamedata.php";
	private static final String POST_ID_SCRIPT_NAME ="idmanager.php";
	
	
	
	
	
	public static boolean postGameData (int cirX, int cirY, int touX, int touY, int type , float taptime)
	{
	
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://192.168.1.1/"+ POST_SCRIPT_NAME);
		
		//load user info from file
		UserPreferencesManager prefs = UserPreferencesManager.getInstance();
		
		//try posting
		try
		{
			
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			
			//our data
			
			//unique id
			nameValuePairs.add(new BasicNameValuePair(ID_POST, SuperGame.getPhoneID()) );
			
			//handeddness
			nameValuePairs.add( new BasicNameValuePair(HANDEDNESS_POST, prefs.getHandedness().toString()));
			
			// eydness
			nameValuePairs.add( new BasicNameValuePair(EYDNESS_POST, prefs.getEyedness().toString()));
			
			//the circle coordinates
			
			nameValuePairs.add( new BasicNameValuePair(X_CIRCLE_POST, String.valueOf(cirX)));
			nameValuePairs.add( new BasicNameValuePair(Y_CIRCLE_POST, String.valueOf(cirY)));
			
			
			//the touch point coordinates
			nameValuePairs.add( new BasicNameValuePair(X_TOUCH_POST, String.valueOf(touX)));
			nameValuePairs.add( new BasicNameValuePair(Y_TOUCH_POST, String.valueOf(touY)));
			
			//the circle type
			nameValuePairs.add(new BasicNameValuePair(CIRCLE_TYPE, String.valueOf(type)));
			
			//notp 
			nameValuePairs.add(new BasicNameValuePair(NOTP_POST, String.valueOf(prefs.getNOTP())));
			
			//time taken to tap
			nameValuePairs.add (new BasicNameValuePair(TAPTIME_POST, String.valueOf(taptime)));
			
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			//post the stuff
			HttpResponse response = client.execute(post);
			
			System.out.println(response.toString());
			
		}
		catch (ClientProtocolException cpex)
		{
			System.out.println("protocol error");
		}
		catch (IOException ioex)
		{
			return false;
		}
			boolean wasPostSuccesful = false;
		
	
		return wasPostSuccesful;
		
	}
	
	/*public static int selectID() 
	{
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://192.168.41.1/"+ POST_SCRIPT_NAME);
		int id=-1;
		try
		{
			
			//call the id script
			HttpResponse response = client.execute(post);
			
			StatusLine statusLine = response.getStatusLine();
		      int statusCode = statusLine.getStatusCode();
		      if (statusCode == 200) { // if we get an OK response we can proceed 
	
		    	  //here we get the current max from the table
		    	  System.out.println("response ok");
		    	  BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		    	  String json = reader.readLine();
		    	  System.out.print(json);
		    	  JSONTokener tokener = new JSONTokener(json);
		    	  JSONArray jsonarray = new JSONArray(tokener);
		    	  id =jsonarray.getInt(0);
		    	  

		    	   // get the id
		    	  System.out.println(id);
	
		      }
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return id;
	}*/

	@Override
	protected Boolean doInBackground(DataForPosting... params) {

		/*(int cirX, int cirY, int touX, int touY, int type , int taptime)*/

		
		postGameData(params[0].getCircleX(), params[0].getCircleY(), 
				params[0].getTouchX(), params[0].getTouchY(), params[0].getType() , 
				params[0].getTapTime() );
		
			
		return true;
		
	}
	
	 protected void onPreExecute() {
	
		
	        super.onPreExecute();
	        
	    }
	 
	 @Override
	protected void onPostExecute(Boolean result) {

		
			 

		super.onPostExecute(result);
	}
	 

	  
	       
	  

}
