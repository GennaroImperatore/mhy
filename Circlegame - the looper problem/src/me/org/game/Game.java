package me.org.game;



import me.org.game.SuperGame.screensize;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Game implements ApplicationListener , InputProcessor  {

	OrthographicCamera camera;
	 SpriteBatch batch;
	 int width, height;
	 float elapsedTime;
	 GameObject gameObject;
	 
	Vector3 clearColour = new Vector3(0,0,0);
	float animationStartTime = 0.0f;
	boolean animate = false;
	
	
	GameText popupText;
	
	GameMenu menu;
	 Context context;	
	 GameManager manager ;
	 
	 GameState currentState;
	 screensize size;
	 
	
	 
	 boolean gameStarted = false;
	 
	// Context context;
	 
	 float lastRenderingTime = 0.0f;
	 
	 
	public Game (int w, int h, Context ctx, screensize sz)
	{
		height = h;
		width = w;
		size = sz;
		
		context = ctx;
		
	
		System.out.println("Android Screen Size: H= "+ h +  " W= " + w);
		
	}
	
	
	private void setState(GameState state)
	{
		if (currentState == state)
			return;
		currentState = state;
		
		switch (state)
		{
		case GAMESTATE_MENU:
			break;
		case GAMESTATE_HISCORE:
			break;
		case GAMESTATE_PLAY:
			manager = GameManager.getInstance();
			manager.setContext(context);
			manager.reset();
			break;
		}
	}
	
	
	private void screenFlash(float elapsedTime)
	{
		//animation lasts for 1.5 seconds each phase is a third
		
		//flash green
		
			clearColour = new Vector3(0,1,0);
		
	}
	 
	public void create() {
	
		
		Looper.prepare();
		Assets.loadAssets(width, height);
		
		
		camera = new OrthographicCamera();
		 camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		 
		 
		 batch = new SpriteBatch();
		
		 Gdx.input.setInputProcessor(this); 
		 
		 setState(GameState.GAMESTATE_MENU);
		 menu = new GameMenu(size);
		
	
		
		 System.out.println("Gdx Screen Size: H= " + Gdx.graphics.getHeight() + 
				 " W= " + Gdx.graphics.getWidth());
		 
		 popupText = new GameText(new Vector2( 0, Gdx.graphics.getHeight()/2), 
					"Tap to start");
		 
		 
		
	}

	public void dispose() {
		
		if (batch!=null)
		{
			batch.dispose(); // dispose of the spritebatch
		}
		
		if (manager!=null)
			manager.cleanUp();

		if(menu!=null)
			menu.dispose();

		if (popupText!=null)
			popupText.dispose();
		
		
		Assets.disposeAssets();
		
	}

	public void pause() {
	
		
		gameStarted = false; // if the user receives a phone call or something we freeze the game.... 

	}

	public void render() {
		
	//	System.out.println(Gdx.app.getNativeHeap() / (1024*1024f) + ", " + Gdx.app.getJavaHeap() / (1024*1024f));
	
		
			if(!Assets.update()) // wait for thread to load assets before rendering.... 
			{
				return;
			}
		
		Gdx.gl.glClearColor(clearColour.x, clearColour.y, clearColour.z, 1);
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	     
	     camera.update();
	    
	     //draw stuff
	     batch.setProjectionMatrix(camera.combined);  
	    
	     
	    
	     batch.begin();
	     elapsedTime =Gdx.graphics.getDeltaTime();
	     switch (currentState)
	     {
	     
	     case GAMESTATE_MENU:
	     { 
	    	 	 
	     
	     		menu.update(elapsedTime);
	    	 	 menu.render(batch);
	    	 
	    	 break;
	     }

	     case GAMESTATE_PLAY:
	     {
	    	
	    	 if (gameStarted) // only update game if the user has tapped the screen
	    	 {
	    		if( manager.updateScene(elapsedTime))
	    		{
	    			// start a new game
	    			manager.reset();
	    			gameStarted = false;


	    		}
	    			
	    	 }

	    	 manager.drawScene(batch);
	    	 
	    	 if(!gameStarted)
	    		 popupText.render(batch);
	    // cannot do an else otherwise the game does render the tap to start message	 
	     /*if(animate)
	    	 screenFlash(elapsedTime);*/
	     }
	     break;
	    }
    	 
	     batch.end();
	     
	    
	}
	
	public void theUserWantsToLeave () 
	{
		gameStarted=false;
		setState(GameState.GAMESTATE_MENU); // return to the menu
	}

	public void resize(int arg0, int arg1) {
	

	}

	public void resume() {
		
		while (Assets.getProgress()<1)
			Assets.update();

	}


	public boolean keyDown(int arg0) {
		
		return false;
	}


	public boolean keyTyped(char arg0) {
		
		return false;
	}


	public boolean keyUp(int arg0) {
		
		return false;
	}


	public boolean scrolled(int arg0) {
		
		return false;
	}


	public boolean touchDown(int x, int y, int pointer, int button)  {
		
		if(currentState == GameState.GAMESTATE_PLAY )
		{
			if(gameStarted)
			{
			
				if(manager.checkHit(x, (Gdx.graphics.getHeight())-y))
				{
					animate = true;
					return false;// check if the user has hit a circle
				}
				

			}// if gamestarted == false
			else
			{
				gameStarted= true; // tap the screen to start the game

			}

		}
		return false;
	}


	public boolean touchDragged(int arg0, int arg1, int arg2) {
	
		
		return false;
	}


	public boolean touchMoved(int arg0, int arg1) {
		
		return false;
	}


	public boolean touchUp(int x, int y, int pointer, int button) {
		
		if(currentState == GameState.GAMESTATE_MENU)
		{
			GameMenu.MENU_SELECTION selection =  menu.getSelection(x, Gdx.graphics.getHeight()-y);
			
			switch (selection)
			{
			
			case PLAY:
				setState(GameState.GAMESTATE_PLAY);
			}
			
		}
		
		return false;
	}

}
