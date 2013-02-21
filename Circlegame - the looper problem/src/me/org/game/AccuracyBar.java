package me.org.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class AccuracyBar implements Renderable {

	Vector2 position;
	Texture accuracyBarTexture;
	
	int hitCircles;
	
	Vector2 dotPosition;
	Texture circleTex;
	
	final int ACCURACY_BAR_LENGTH =128;
	final int ACCURACY_BAR_HEIGHT =32;
	
	
	public AccuracyBar(float x, float y)
	{
		hitCircles =0;
		
		//create the texture for the accuracy bar itself
		accuracyBarTexture = new Texture(Gdx.files.internal("emptybar.png"));
		
		//create the dot for the accuracy bar
		
		Pixmap pixmap = new Pixmap( 16, 16, Format.RGBA8888 );
		 pixmap.setColor( 1, 0, 0, 1 ); // a red dot
		 pixmap.fillCircle( 8, 8, 8 );
		 circleTex =  new Texture (pixmap);
		 
		 pixmap.dispose();
		 
		 //set the position for the accuracy bar
		 position = new Vector2(x,y - ACCURACY_BAR_HEIGHT);
		 dotPosition = new Vector2(position.x, position.y + ACCURACY_BAR_HEIGHT/2); 
		 // dot appears at the centre of the accuracy bar vertical line
		 
	}
	
	public void render(SpriteBatch refBatch) {
		
		//draw the accuracy bar
		refBatch.draw(accuracyBarTexture, position.x, position.y);
		//draw the dot on the accuracy bar
		refBatch.draw(circleTex, dotPosition.x, dotPosition.y);
	}

	public void increaseAccuracyBar(float totalSpawned, float hitCircles)
	{
		// update the x  only based on the percenteage of hits
		float percenteage = hitCircles/totalSpawned;
		dotPosition.x = ACCURACY_BAR_LENGTH * percenteage + position.x - 16;
	}
	
	public boolean update(float elaspedTime) {
		
		return false;
	}

	public void dispose() {
		if(circleTex!=null)
			circleTex.dispose();
		if(accuracyBarTexture!=null)
			accuracyBarTexture.dispose();

	}

	public Vector2 getPosition() {
		
		return position;
	}

	public boolean isHittable() {
		
		return false;
	}

}
