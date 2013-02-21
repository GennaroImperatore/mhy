package me.org.game;

import me.org.datacapturing.DataManager;
import me.org.datacapturing.DataPoster;
import me.org.datacapturing.DataManager.UncorrectUserInfoDataException;
import me.org.datacapturing.UserInfo;
import me.org.datacapturing.UserInfo.SIDE;
import me.org.datacapturing.UserPreferencesManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;



public class SuperGame extends AndroidApplication {

	Button prevButton , nextButton, submitButton;
	ViewFlipper viewflipper;
	TextView viewCounter;
	ScrollView scrollView;
	EditText editText;
	RadioGroup radiogroupHandedness, radioGroupEyedness;
	int currView = 1;
	static String uid;
	
	public static ProgressDialog dlg;
	
	public enum screensize  {LARGE,SMALL,MEDIUM};
	
	
	UserPreferencesManager userprefs;
	
	Game theGame;
	int total =0;

	 public void onCreate (android.os.Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        
         Display display = getWindowManager().getDefaultDisplay();
 
         cfg.useGL20 = false;
         screensize size = screensize.LARGE;
         
         //DETERMINE THE SCREENSIZE
         if ((getResources().getConfiguration().screenLayout & 
        		    Configuration.SCREENLAYOUT_SIZE_MASK) == 
        		        Configuration.SCREENLAYOUT_SIZE_LARGE) {
        	 size = screensize.LARGE;
         }
         
         if ((getResources().getConfiguration().screenLayout & 
     		    Configuration.SCREENLAYOUT_SIZE_MASK) == 
     		        Configuration.SCREENLAYOUT_SIZE_NORMAL) {
     	 size = screensize.MEDIUM;
      }
         
         if ((getResources().getConfiguration().screenLayout & 
     		    Configuration.SCREENLAYOUT_SIZE_MASK) == 
     		        Configuration.SCREENLAYOUT_SIZE_SMALL) {
     	 size = screensize.SMALL;
      }
	
         System.out.println(size.toString());
      
       

        theGame = new Game(display.getWidth(), display.getHeight(), this, size); 
         
         initialize(theGame, false);
         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // force portrait
         
         
      
         
         
        /* ProgressTask progBar = new ProgressTask("loading...", this);
         progBar.execute(0);*/
         
         UserPreferencesManager.getInstance().setContext(this);
         
        
         
         TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
         uid = telephonyManager.getDeviceId();
         
         ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
         boolean weHaveMobileConnection =
        		 connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
         boolean weHaveWifiConnection =
        		 connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
         
         if(!weHaveMobileConnection && !weHaveWifiConnection)
         {
        	 //display an alert dialog to ask the player to switch on some form of connection
        	 
        	 String title = "Please";
        	 String message ="You have no Internet connection active. You can still play the game, but no data will be sent. Please " +
        	 		"connect to the internet so I can get some data for my PhD research :) ";
        	 
        	 AlertDialog dialog = new AlertDialog.Builder(this).setCancelable(false).create();
        	 dialog.setTitle(title);
        	 dialog.setMessage(message);
        	 dialog.setCancelable(true); // important we don't want people cancelling with the back button
        	 dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
			
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
				}
			});
        	 
        	 
        	 
        	dialog.show(); 
         }
         
         
         
         // DataPoster.postGameData(SIDE.LEFT, SIDE.LEFT, 23, 10, 12, 100);
          /*try {
 		System.out.println(	DataManager.loadUserInfofromFile().getNOTP());
 		} catch (UncorrectUserInfoDataException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
          
          
         
      
         // if it is the first time the user has played we'll ask him for basic info
        userprefs = UserPreferencesManager.getInstance();
       
        
         if (!userprefs.preferencesHaveBeenSet()) 
  
         {

         	final Dialog dialog = new Dialog (this);
         	 
         	 dialog.setContentView(R.layout.firsttimealert);
         	 dialog.setTitle("Welcome");
         	 dialog.setCancelable(false);

         	 //manage the dialog!!
         	 
         	 viewCounter = (TextView) dialog.findViewById(R.id.viewCounter);
         	 viewflipper= (ViewFlipper) dialog.findViewById(R.id.viewFlipper1);
         	 nextButton =  (Button) dialog.findViewById(R.id.nextButton);
         	 prevButton = (Button) dialog.findViewById(R.id.prevButton);
         	 scrollView = (ScrollView) dialog.findViewById(R.id.scrollview1);
         	 submitButton = (Button) dialog.findViewById(R.id.submitButton);
         	 radioGroupEyedness = (RadioGroup) dialog.findViewById(R.id.radioGroupEyedness);
         	 radiogroupHandedness = (RadioGroup) dialog.findViewById(R.id.radioGroupHandedness);
         	
         	 final int total = viewflipper.getChildCount();
         	 
         	 viewCounter.setText(String.valueOf(currView) + "/" + String.valueOf(total));
         	 
         	 nextButton.setOnClickListener(new OnClickListener() {
 				
         		 public void onClick(View v) {

         			 if(currView  < total)
         			 {
         				 currView++;
         				 viewflipper.showNext(); // let the user proceed to the next view 
         				 scrollView.scrollTo(0, scrollView.getTop()); // reset the position of the scrollbar when we flip the view
         				 viewCounter.setText(String.valueOf(currView) + "/" + String.valueOf(total));
         			 
         			}
         		 }
         	 });
         

         	 prevButton.setOnClickListener(new OnClickListener() {

         		 public void onClick(View v) {
         			 if(currView > 1)
         			 {
         				 currView--;
         				 viewflipper.showPrevious();
         				 scrollView.scrollTo(0, scrollView.getTop()); // same here
         				 viewCounter.setText(String.valueOf(currView) + "/" + String.valueOf(total));
         			 }

         		 }
         	 });
         
         	 dialog.show();

         	 submitButton.setOnClickListener(new OnClickListener() {

         		 public void onClick(View v) {

         			 /*gather the data from user input*/
         			 int checkedEyedness = radioGroupEyedness.getCheckedRadioButtonId();
         			 
         			 switch (checkedEyedness)
         			 {
         			 case R.id.radioELeft:
         			  userprefs.setEyedness(SIDE.LEFT);
         				 break;
         			 
         			case R.id.radioERight:
         				userprefs.setEyedness(SIDE.RIGHT);
            			 break;
            			 
         			case R.id.radioENone:
         				userprefs.setEyedness(SIDE.NONE);
            			 break;
            			
         			 }
         			 
         			 int checkedHandedness = radiogroupHandedness.getCheckedRadioButtonId();
         			 
         			 switch (checkedHandedness)
         			 {
         			 case R.id.radioHLeft:
         				 userprefs.setHandedness(SIDE.LEFT);
         				 break;
         			 case R.id.radioHRight:
         				userprefs.setHandedness(SIDE.RIGHT);
         				 break;
         			 case R.id.radioHNone:
         				userprefs.setHandedness(SIDE.NONE);
         				 break;
         			 }
         		 
         		userprefs.setNotp(0);
         		userprefs.preferenceshaveBeensetToTrue();
         			 
         			 //System.out.println(DataPoster.selectID());
         			// DataManager.createUserInfo(info);
         			 dialog.dismiss();

         		 }
         	 });
         } // end of if...
         else // we have prefs
         {
        	 userprefs.increaseNotp();
         }

        
	 }
 
	 
	 public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.optionsmenu, menu);
		    return true;
	 }
	 
	 public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle item selection
		    switch (item.getItemId()) {
		        case R.id.exitoption:
		            if(theGame!=null)
		            	theGame.theUserWantsToLeave();
		            return true;
		        case R.id.resetinfo:
		        	if (userprefs !=null)
		        		userprefs.resetUserPreferences();
		        	
		            return true;
		        default:
		            return super.onOptionsItemSelected(item);
		    }
		}
	

	 
	 
	 public static String getPhoneID() {return uid;}



public void onBackPressed()
{
	//do nothing
}



	 
	 
}
