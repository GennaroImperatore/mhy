package me.org.game;



import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class HitGameText extends GameText {

	final float animationSpeed =5.0f;
	final float animationDuration =3;
	float time =0;
	private boolean animationover=false;
	
	public HitGameText(Vector2 pos, String content) {
		super(pos, content);
		animationover=false;
		
	}
	
	public HitGameText(Vector2 pos, String content, boolean useCustomFont) {
		super(pos, content, useCustomFont);
		animationover=false;
		
	}
	
	public boolean update (float elapsedTime)
	{
		super.update(elapsedTime);
		
		time+=elapsedTime;
		position.y+=animationSpeed*elapsedTime; // slowly move the score up
		
		if(time > animationDuration)
		{
			animationover = true;
			return true;
		}
		
		
		return false;
		
	
	}

	
	@Override
	public void render(SpriteBatch refBatch) {
		
		super.render(refBatch);

		}
public boolean isAnimationOver() {return animationover;}
	
}
