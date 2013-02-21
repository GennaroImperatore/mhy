package me.org.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameText implements Renderable { // a class to facilitate displaying text on the screen

	protected String contents;
	protected Vector2 position;
	protected BitmapFont font; // the object responsible for displaying text in libgdx
	protected boolean customisedFont; // are we using a customised font?
	
	private final int  CHAR_WIDTH =15;//px
	private final int CHAR_HEIGHT =30;//px
	
	private Color colour = Color.WHITE;
	private float scale =1.0f;
	
	
	/*public static final String FONT_CHARACTERS = 
			"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" +
			"][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";*/
	
	public GameText (Vector2 pos, String content)
	{
		
		position = pos;
		contents = content;
		customisedFont = true;
		
		
		/*font = TrueTypeFontFactory.createBitmapFont
				(Gdx.files.internal("CAPTL.TTF"), FONT_CHARACTERS, 12.5f, 7.5f, 1.0f, 
				 Gdx.graphics.getWidth(), Gdx.graphics.getHeight());*/
		font= Assets.CAPT_BITMAP_FONT;
		
	}
	
	public GameText (Vector2 pos, String content, boolean useCustomFont)
	{
		position = pos;
		contents = content;
		customisedFont = useCustomFont;
		
		if(useCustomFont)
			font= Assets.CAPT_BITMAP_FONT;
		else
			font = Assets.BITMAP_FONT;

	}
	
	public void render(SpriteBatch refBatch) 
	{
		font.setScale(scale);
		font.setColor(colour);
		font.draw(refBatch, contents, position.x, position.y);
		
		
	}
	
	public void setSize (float size)
	{
		scale = size;
	}
	
	public void setColour(Color col )
	{
		colour = col;
	}

	public boolean update(float elaspedTime) 
	{
		return false;
		
	}

	public void dispose() {
		
		/*if(!customisedFont && font!=null) // we need to dispose of the bitmap font we allocate
			font.dispose();*/
	
		
	}

	public Vector2 getPosition() {
		// TODO Auto-generated method stub
		return position;
	}

	public boolean isHittable() {
		return false;
	}
	
	public int getFontHeight()
	{
		if (customisedFont)
			return CHAR_HEIGHT;
		else
			return (int) font.getScaleY();
	}
	
	public int getFontWidth()
	{
		int width = 0;
		
		if (customisedFont)
			width= CHAR_WIDTH*contents.length();
		else
			width = (int)font.getScaleX() * contents.length();
		
		return width;
	}
	
	public void setText (String text) {contents = text;}

}
