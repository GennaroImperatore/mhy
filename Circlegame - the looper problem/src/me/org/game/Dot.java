package me.org.game;

import java.util.Vector;

import android.graphics.Matrix;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Dot implements Renderable {

	Vector2 position;
	Vector2 pivot;
	Texture dotTexture;
	float startTime= 0.0f;
	float angle =0;
	
	
	
	public Dot (Vector2 initPos, Vector2 piv)
	{
		position = initPos;
		pivot = piv;
		dotTexture = Assets.assetManager.get(Assets.sDOT_TEX, Texture.class);
		
	}
	
	
	public void render(SpriteBatch refBatch) {
		
		
		//position.set(position.x + pivot.x, position.y + pivot.y);
		
		
		refBatch.draw(dotTexture, position.x, position.y);
		
		/*Matrix4 identity = new Matrix4().idt();
		batch.setTransformMatrix(identity); // reset the transform for the other drawing*/
		
		
		/*SpriteBatch customisedBatch = new SpriteBatch();
		
		customisedBatch.begin();
		
		customisedBatch.draw(dotTexture , position.x, position.y);
		
		customisedBatch.end();*/
		
	}

	public boolean update(float elaspedTime) {
		
		
		// do the rotation here !!
		startTime+=elaspedTime;
		
		if(angle>359.0f)
			angle=0.0f;
		
			/*angle += 1.0f*elaspedTime;*/
			
			Matrix3 rot = new Matrix3().idt();//.translate(new Vector3 (-position.x, -position.y, 0)); // transalate to origin	
			/*rot =*/ rot.translate(pivot.x, pivot.y).rotate((360.0f/GameManager.getInstance().getTotalTime())*elaspedTime).translate(-pivot.x,  -pivot.y); // translate back to original position; // rotate by current angle ; // translate to pivot point
			/*position =*/ position.mul(rot);
			
		
		
		
				
		return false;
		
	}

	public void dispose() {
		
		//customisedBatch.dispose();

	}

	public Vector2 getPosition() {
		
		return position;
	}

	public boolean isHittable() {

		return false;
	}

}
