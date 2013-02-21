package me.org.game;

import me.org.datacapturing.UserPreferencesManager;
import me.org.game.SuperGame.screensize;


import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameMenu implements Renderable {

	public enum MENU_SELECTION {NONE, PLAY, HISCORE}
	
	
	GameText gameName, playButton, hiScoreButton, hiScoreVal;
	screensize size;
	
	public GameMenu(screensize sz)
	{
		size = sz;
	System.out.println( "Hiscore " + 	UserPreferencesManager.getInstance().getHiScore());
		gameName = new GameText(new Vector2(0, Gdx.graphics.getHeight()-100), "CircleGame 1.06");
		playButton = new GameText(new Vector2(0, Gdx.graphics.getHeight()-150), "PLAY");
		if(UserPreferencesManager.getInstance()!=null)
			hiScoreButton = new GameText(new Vector2(0, Gdx.graphics.getHeight()-200), "Hi Score "  
					);
		else
			hiScoreButton = new GameText(new Vector2(0, Gdx.graphics.getHeight()-200), "YOUR HI SCORE IS 0" + 
			
					String.valueOf( UserPreferencesManager.getInstance().getHiScore()));
		
		hiScoreVal = new GameText(new Vector2(0, Gdx.graphics.getHeight()-250), " " + 
				String.valueOf(UserPreferencesManager.getInstance().getHiScore()));
		
	
			
	}
	
	
	public void render(SpriteBatch refBatch ) {
		
		
			refBatch.draw(Assets.assetManager.get(Assets.sLoadedCover, Texture.class),0,0);
		
		
		
		//gameName.render(refBatch);
			hiScoreVal.setText(  
			String.valueOf(UserPreferencesManager.getInstance().getHiScore()));
		playButton.render(refBatch);
		hiScoreButton.render(refBatch);
		hiScoreVal.render(refBatch);
		
	}

	public boolean update(float elaspedTime) {
		
		return false;
	}

	public void dispose() {
		if(gameName!=null)
			gameName.dispose();
		if(playButton!=null)
			playButton.dispose();
		if(hiScoreButton!=null)
			hiScoreButton.dispose();
		if(hiScoreVal!=null)
			hiScoreVal.dispose();
		
	}

	public Vector2 getPosition() {
	
		return null;
	}

	public boolean isHittable() {
		
		return false;
	}
	
	public MENU_SELECTION getSelection(int x, int y)
	{
		//check if play has been pressed
		if((x>= playButton.getPosition().x && x<= playButton.getPosition().x + playButton.getFontWidth()*4)
				&&
		  (y<=playButton.getPosition().y && y >=playButton.getPosition().y-playButton.getFontHeight()))
			return MENU_SELECTION.PLAY;
		//check if hi score has been pressed
		if((x>= hiScoreButton.getPosition().x && x<= hiScoreButton.getPosition().x + hiScoreButton.getFontWidth())
				&&
		  (y<=hiScoreButton.getPosition().y && y >=hiScoreButton.getPosition().y-hiScoreButton.getFontHeight()))
			return MENU_SELECTION.HISCORE;
		
		
		return MENU_SELECTION.NONE;
		
	}

	
}
