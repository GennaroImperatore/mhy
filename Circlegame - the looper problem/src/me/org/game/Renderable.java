package me.org.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface Renderable {
	
	public void render(SpriteBatch refBatch);
	public boolean update (float elaspedTime);
	public void dispose();
	public Vector2 getPosition();
	public boolean isHittable();
	

}
