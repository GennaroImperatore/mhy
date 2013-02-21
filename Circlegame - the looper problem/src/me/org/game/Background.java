package me.org.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Background implements Renderable {

	private Texture backgroundTexture;
	
	public Background()
	{
		backgroundTexture = Assets.assetManager.get(Assets.sBACKGROUND_TEX, Texture.class);
		
	}
	
	
	public void render(SpriteBatch refBatch) {
	
		refBatch.draw (backgroundTexture , 0, 0);
		
	}

	public boolean update(float elaspedTime) {
		
		return false;
	}

	public void dispose() {
		
		
	}

	public Vector2 getPosition() {
		
		return null;
	}

	public boolean isHittable() {
		
		return false;
	}

}
