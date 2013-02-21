package me.org.game;


import me.org.game.SuperGame.screensize;
import android.app.ProgressDialog;
import android.content.Context;

import android.test.suitebuilder.annotation.LargeTest;
import android.widget.ProgressBar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {

	private static String internal = "assets/";
	
	//HOLDS NAME OF OBJECTS.... 
	public static String sOBJ_TEX = "game object circle.png";
	public static String sTIME_FRAME_1 = "cooltex.png";
	public static String sTIME_FRAME_2 = "cooltex2.png";
	public static String sTIME_FRAME_3 = "cooltex3.png";
	public static String sTIME_FRAME_4 = "cooltex4.png";
	public static String sOBJ_TEX_MEDIUM = "gameobjectmed.png";
	public static String sBACKGROUND_TEX ="back.png";
	public static String sDOT_TEX = "dot.png";
	public static String sCAPT_FNT = "capt.fnt";
	public static String sCAPT_PNG = "capt.png";
	public static String sCOVER_LARGE_PNG ="largecover.png";
	public static String sCOVER_MEDIUM_PNG ="mediumcover.png";
	public static String sCOVER_SMALL_PNG ="smallcover.png";
	public static BitmapFont BITMAP_FONT;
	
	public static String sLoadedCover;
	
	
	public static BitmapFont CAPT_BITMAP_FONT;
	
	private static boolean loaded = false;
   public static AssetManager assetManager = new AssetManager();
	
	public static boolean loadAssets(int width, int height )
	{
		//Looper.prepare();
		//ProgressDialog progressDlg = new ProgressDialog(ctx);
		//progressDlg.setTitle("Loading Assets");
		//progressDlg.setCancelable(true);
		
		sLoadedCover =sCOVER_LARGE_PNG;
		
		if (width> 239 && width <= 400 && height <321)
		{
			sLoadedCover= sCOVER_SMALL_PNG;
		}
		if (width > 400 && width <=432  && height >321)
		{
			sLoadedCover= sCOVER_MEDIUM_PNG;
		}
		if(width >400 && height>321)
		{
			sLoadedCover =sCOVER_LARGE_PNG;
		}
			
		
			System.out.println("loaded cover... " + sLoadedCover);
			assetManager.load(sLoadedCover, Texture.class);
		
		
		assetManager.load( sOBJ_TEX, Texture.class);
		//progBar.execute((int)assetManager.getProgress());
		
		//progressDlg.setProgress((int)assetManager.getProgress());
		
		assetManager.load(sOBJ_TEX_MEDIUM, Texture.class);
		//progBar.execute((int)assetManager.getProgress());

		
		//progressDlg.setProgress((int)assetManager.getProgress());
		
		assetManager.load(sTIME_FRAME_1, Texture.class);
		//progBar.execute((int)assetManager.getProgress());
		
		//progressDlg.setProgress((int)assetManager.getProgress());
		
		assetManager.load(sTIME_FRAME_2, Texture.class);
		//progBar.execute((int)assetManager.getProgress());
	
		
		//progressDlg.setProgress((int)assetManager.getProgress());
		
		assetManager.load(sTIME_FRAME_3, Texture.class);
		//.execute((int)assetManager.getProgress());
		//progressDlg.setProgress((int)assetManager.getProgress());
		
		assetManager.load(sTIME_FRAME_4, Texture.class);
		//progBar.execute((int)assetManager.getProgress());
		
		//progressDlg.setProgress((int)assetManager.getProgress());
		
		assetManager.load(sDOT_TEX, Texture.class);
		//progBar.execute((int)assetManager.getProgress());
	
		
		//progressDlg.setProgress((int)assetManager.getProgress());
		
		assetManager.load(sBACKGROUND_TEX, Texture.class);
		//progBar.execute((int)assetManager.getProgress());
		CAPT_BITMAP_FONT = new BitmapFont(Gdx.files.internal(sCAPT_FNT), Gdx.files.internal(sCAPT_PNG), false);
		BITMAP_FONT = new BitmapFont(false);
		//progressDlg.setProgress((int)assetManager.getProgress());
		
		
		
		
		//999progressDlg.dismiss();
		
		
		
		
		
		loaded = true;
		return loaded;
	}
	
	public static void disposeAssets()
	{
		if (!loaded)
			return;
		
		assetManager.dispose();
		/*
		 OBJ_TEX.dispose() ;
		 TIME_FRAME_1.dispose()  ;
		 TIME_FRAME_2.dispose() ;
		 TIME_FRAME_3.dispose() ;
		 TIME_FRAME_4.dispose() ;
		 OBJ_TEX_MEDIUM.dispose()  ;
		 BACKGROUND_TEX.dispose() ;
		 DOT_TEX.dispose() ;*/
		CAPT_BITMAP_FONT.dispose();
		BITMAP_FONT.dispose();
		
	}
	
	public static int getProgress () { return (int) (assetManager.getProgress()*100.0f);}
	public static boolean update () {return assetManager.update();}
}
