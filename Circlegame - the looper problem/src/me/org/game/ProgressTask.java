package me.org.game;

import com.badlogic.gdx.assets.AssetManager;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

class ProgressTask /*extends AsyncTask<Integer, Integer, Void>*/{
  /*  ProgressDialog progress;
    Context context;
    String title;
    
    public ProgressTask(String title, Context context ) {
        this.context = context;
        this.title = title;
        

    }
    @Override
    protected void onPreExecute() {
        // initialize the progress bar
        // set maximum progress to 100.
    	Assets.loadAssets(context);
    	
    	 progress = new ProgressDialog(context);
    	progress.setMessage(title);
    	progress.setCancelable(false);
    	progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	progress.setProgress(0); // set percentage completed to 0%
    	progress.show();
        progress.setMax(100);
      

    }


    @Override
    protected Void doInBackground(Integer... params) {
        // get the initial starting value
        int start=params[0];
       

        // increment the progress
      
        while (Assets.getProgress()<100)
        {
        	
            try {
                boolean cancelled=isCancelled();
                Assets.update();
                //if async task is not cancelled, update the progress
                if(!cancelled){
                    publishProgress(Assets.getProgress());
                    SystemClock.sleep(100);

                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }
        
        return null;
    }
    //Has direct connection to UI Main thread
    //Called everytime publishProgress(int) is called in doInBackground
    @Override
    protected void onProgressUpdate(Integer... values) {
    	
        progress.setProgress(values[0]);
       
    }


    @Override
    protected void onPostExecute(Void result) {
    	
    		progress.dismiss();
        // async task finished
                    
    }
    @Override
    protected void onCancelled() {
        progress.setMax(0);
    }*/
}