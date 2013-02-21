package me.org.game;

import java.util.Date;
import java.util.Random;

import android.graphics.YuvImage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


/**
 * Warning the position of the circle is the lower left corner of the texture square!!!
 * 
 * @author Gennaro Imperatore
 * 
 * */


public class GameObject implements Renderable {
	
	
	Circle circle;
	Rectangle timeBar;
	Vector2 position;
	int radius;
	Texture circleTex;
	Texture[] rectangleTex;
	int currentTimeFrame=0;
	
	int timeBarExpiry =3000;
	float startTime=0;
	
	public final int smallRadius = 32;
	public final int mediumRadius = 64;
	public final int largeRadius = 128;
	
	
	GameObjectType type;
	float timeReducer =1;
	float objectTime =0.0f;
	private float TOTAL_TIME =4.0f;
	
	
	Dot dot;
	
	
	
	
public	GameObject(int x, int y, GameObjectType type)  
	{
	
	
		position = new Vector2();
		position.x =x;
		position.y =y;
		rectangleTex = new Texture[4];
		
		//give the correct radius to the circle based on the requested size by manager
		if(type == GameObjectType.OBJ_SMALL)
		{
			radius = smallRadius;
			circleTex = Assets.assetManager.get(Assets.sOBJ_TEX, Texture.class);
		}
		if (type == GameObjectType.OBJ_MEDIUM)
		{
			radius = mediumRadius;
			circleTex = Assets.assetManager.get(Assets.sOBJ_TEX_MEDIUM, Texture.class);
		}
		
		if (type == GameObjectType.OBJ_LARGE)
		{
			//
		}
		
		// prepare the time counter 
		rectangleTex[0] = Assets.assetManager.get(Assets.sTIME_FRAME_1, Texture.class);
		rectangleTex[1] = Assets.assetManager.get(Assets.sTIME_FRAME_2, Texture.class);
		rectangleTex[2] = Assets.assetManager.get(Assets.sTIME_FRAME_3, Texture.class);
		rectangleTex[3] = Assets.assetManager.get(Assets.sTIME_FRAME_4, Texture.class);
		
		timeBar = new Rectangle(x, y, radius*2, 10);
		
		if(type!=GameObjectType.OBJ_SMALL)
			timeBar = new Rectangle(x + (mediumRadius - smallRadius), y + (mediumRadius - smallRadius), radius*2, 10);
		
		
		dot = new Dot(new Vector2(position.x , position.y ) , 
				new Vector2( (position.x + radius) - 10, (position.y + radius) - 10)/*pivot point of the dot is centre of circle */);
		
	startTime =0.0f;
	objectTime =0.0f;
		 
		
		
		
		
		/*
		//create texture for circle
		Pixmap pixmap = new Pixmap( 64, 64, Format.RGBA8888 );
	 	 pixmap.setColor( 0, 1, 0, 1 );
		 pixmap.fillCircle( 32, 32, 32 );
		 circleTex =  new Texture (pixmap);
		 
		 // create texture for rectangle
		 pixmap = new Pixmap ((int)timeBar.getWidth(), (int)timeBar.getHeight(), Format.RGBA8888);
		 pixmap.setColor(1,0,0,1);
		 pixmap.fillRectangle(x, y, (int)timeBar.getWidth(), (int)timeBar.getHeight());
		 rectangleTex = new Texture (pixmap);
		 
		 pixmap.dispose();*/
		
		
		
	}
	
	private boolean updateTimeBar(float elapsedTime) // updates the time bar on top of the circle
	{
			startTime+=elapsedTime;
			
			if (startTime > (TOTAL_TIME/4.0f)) // update the timebar at the appropriate time
			{
				currentTimeFrame++;// shrink the timebar every second
				startTime =0.0f;
				
			}
		
		if (currentTimeFrame>3)
		{
			currentTimeFrame =0; // reset the time frame (temporary)
			return true;
		}
		return false;
			
	}
	
	public void render(SpriteBatch refBatch) //render the object
	{
		
		refBatch.draw (circleTex, position.x, position.y);
		refBatch.draw (rectangleTex[currentTimeFrame], timeBar.x, timeBar.y);
		
		if(dot!=null)
			dot.render(refBatch);
		
		
		//bitmapFont.draw(refBatch,"TEST!!", Gdx.graphics.getWidth()/2, 10);
		
		
	}
	
	public boolean update(float elapsedTime)
	{	
		objectTime+=elapsedTime;
		if(dot!=null)
			dot.update(elapsedTime);
		
		return updateTimeBar(elapsedTime); // remove object when time has run out
		
		
	}
	
	public void dispose()
	{
		// dispose of textures and others....
		
		
	}

	public Vector2 getPosition() {
		
		return position;
	}

	public boolean isHittable() {
		return true;
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public Vector2 getCentre () {return new Vector2 ((position.x + radius) - 10, (position.y + radius) - 10);} // a centre that works better for dot rotations
	public Vector2 getRealCentre () {return new Vector2 ((position.x + radius) , (position.y + radius) );}
 	public void setPosition (Vector2 newpos) {position = newpos;}
	
	public float getTotalTime () {return TOTAL_TIME;} // get the life of the circle
	public float getObjectTime () {return objectTime;}
	

	
	

}
