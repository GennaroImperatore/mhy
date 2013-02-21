package me.org.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import me.org.datacapturing.DataForPosting;
import me.org.datacapturing.DataPoster;
import me.org.datacapturing.UserPreferencesManager;
import me.org.datacapturing.UserInfo.SIDE;


import android.content.Context;
import android.graphics.Camera;
import android.net.ConnectivityManager;
import android.os.Looper;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class GameManager {

	
	Context ctx;
	static GameManager instance;
	int score = 0; 
	
	int PADDING = 15; // padding between circles
	final int SCORE_INCREMENT =5; // the basic score increment 
	private float TOTAL_TIME=4.0f; //the life of a circle

	final float TIME_TO_NEXT_LEVEL = 4.0f;
	float countDown = TIME_TO_NEXT_LEVEL;
	List<Renderable> renderables;
	List <GameObject> circles;
	

	
	
	int currentLevel =1; // keep track of the level
	int streakCount = 0; // keep track of the streak to assign bonuses
	
	int bonusMultiplier =1;
	
	int spawnedObjectCount=0; //keep count of spawned object to calculate accuracy 
	
	HitGameText popupGameText /*score*/, levelpopupGameText /*level complete*/; 
	/* these two are used to display score given as circle is hit and new level alert  */

	
	//	HitGameText  bonusAlertGameText; ??
	
	HitGameText hitResultGameText/*displays good, ok , or perfect*/ ;
	
	GameText scoreText, scoreTextVal;
	GameText levelText , levelTextVal;
	GameText livesText, livesTextVal;
	
	
	
	
	//GameText countdownToLevelStart;
	
	
	
	Background background;
	float totalTime=0.0f;
	int popupScoreValue=0;
	
	int hitCount =0; // how many circles have been hit in total during a game session
	
	int maxCircles =0; // the maximum amount of circles for each level 
	final int startCircles =3; // the minimum number of circles at each level reset
	final int spawnLimit =5;
	int spawnedCirclesPartial = 0;
	
	int lives =3; // the chances the player has to miss a circle
	
	boolean isLoading = true;
	
	
	
	
	public static GameManager getInstance() // singleton pattern  
	{if (instance==null) 
		instance = new GameManager();
	return instance; }
	
	
	public void setContext(Context context) {ctx = context;}
	
	private void createPopUpScoreText(Vector2 position)
	{
		popupGameText = new HitGameText(position, String.valueOf(popupScoreValue) , false); 
	}
	
	
	private void manageHit(GameObject objectWhichWasHit, int hitPointX, int hitPointY)
	{
		streakCount++;
		hitCount++;
		
		//we know for sure that the circle was hit
		
		
		int diameter = objectWhichWasHit.getRadius()*2; // the diameter of the object which was hit
		int radius = objectWhichWasHit.getRadius(); // the radius of the object which was hit
		int innerCircleDiameter = objectWhichWasHit.getRadius();
		
		
		int circleX = (int)objectWhichWasHit.getPosition().x;
		int circleY = (int)objectWhichWasHit.getPosition().y;

		int innerCircleX = circleX + (radius - innerCircleDiameter/2);
		int innerCircleY = circleY + (radius - innerCircleDiameter/2);
		
		//the inner circle was hit

		if((hitPointX<= innerCircleX+innerCircleDiameter && hitPointX>=innerCircleX) // in the circle widthwise
				&& //and
		(hitPointY>= innerCircleY && hitPointY <=innerCircleY + innerCircleDiameter)) // in the circle heightwise
		
		
		{
			System.out.println("INNER CIRLCE WAS HIT");
			
			System.out.println(new Vector2(hitPointX, hitPointY).dst(objectWhichWasHit.getCentre()  ));
			if (new Vector2(hitPointX, hitPointY).dst(objectWhichWasHit.getRealCentre()  ) <=5.0f) // we are very close to the centre
			{
				hitResultGameText = new HitGameText(new Vector2(objectWhichWasHit.getPosition().x + objectWhichWasHit.getRadius(), 
						objectWhichWasHit.getPosition().y + objectWhichWasHit.getRadius() ), "PERFECT! pts X3", false);
					hitResultGameText.setColour(Color.GREEN);
					hitResultGameText.setSize(1.5f);
					score+= (SCORE_INCREMENT*3) *bonusMultiplier;
					popupScoreValue = (SCORE_INCREMENT*3) * bonusMultiplier;

		
			}
			else // we are close to the centre 
			{
			hitResultGameText = new HitGameText(new Vector2(objectWhichWasHit.getPosition().x + objectWhichWasHit.getRadius(), 
					objectWhichWasHit.getPosition().y + objectWhichWasHit.getRadius() ), "GOOD! pts X2" ,false);
				hitResultGameText.setColour(Color.YELLOW);
				hitResultGameText.setSize(1.5f);
				score+= (SCORE_INCREMENT*2) *bonusMultiplier;
				popupScoreValue = (SCORE_INCREMENT*2) * bonusMultiplier;
			}
				
		}
		else // the outer circle was hit
		{
			System.out.println("OUTER CIRCLE WAS HIT");
			
			hitResultGameText = new HitGameText(new Vector2(objectWhichWasHit.getPosition().x + objectWhichWasHit.getRadius(), 
												objectWhichWasHit.getPosition().y + objectWhichWasHit.getRadius() ), "OK!" ,false);
			hitResultGameText.setColour(Color.RED);
			hitResultGameText.setSize(1.5f);
			score+=SCORE_INCREMENT*bonusMultiplier;
			popupScoreValue = SCORE_INCREMENT * bonusMultiplier;
		}
		
		
		if(streakCount % 7 == 0 && streakCount!=0)
		{
			bonusMultiplier++;
			increaseTotalTime(); // give more circle life;
			
		/*	gameTimeAlert = new HitGameText(new Vector2(0, Gdx.graphics.getHeight()/2), "Circle Time Increased", false);
			gameTimeAlert.setSize(2.0f);*/
		}
		scoreTextVal.setText(String.valueOf(score));
		
		UserPreferencesManager.getInstance().updateHiScore(score);
	}
	
	
	
	public void spawnNewCircle(int howManyCircles)
	{
		if(spawnedCirclesPartial > spawnLimit || spawnedCirclesPartial > maxCircles)
			return; 
		
		for (int i=0; i<howManyCircles; i++)
		{

			int randSize = new Random().nextInt(2);
			int radius =64;
			GameObjectType type= GameObjectType.OBJ_MEDIUM;
			
			if(currentLevel>= 5) // the game will 
			{
				switch (randSize)
				{
				case 0:
					type = GameObjectType.OBJ_SMALL;
					radius=32;
					break;
				case 1:
					type = GameObjectType.OBJ_MEDIUM;
					radius =64;
					break;
				case 2:
					break;
				}
			}
			
			Vector2 spawningposition = generateRandomPostion(Gdx.graphics.getHeight() - radius*2, radius);
			GameObject dummy = new GameObject((int)spawningposition.x, (int)spawningposition.y, type);
			
			for(GameObject object:circles)// check the position of the other objects on screen
			{
				while (checkCollision(object, dummy))
				{
					spawningposition = generateRandomPostion (Gdx.graphics.getHeight() - radius*2, radius);
					dummy.setPosition(spawningposition); // we update dummy with the new position
				}
				
				// if we got here it means that the pos is good
				for(GameObject recheck:circles) // now we recheck it against the other circles 
				{
					while(checkCollision(recheck, dummy))
					{
						spawningposition = generateRandomPostion (Gdx.graphics.getHeight() - radius*2, radius);
						dummy.setPosition(spawningposition);
					}
				}
				//first we check for a collision the other circles
				// if a good position is found we check it against the other circles again 
				//until a good position is found
			
				
				
				
				
				//we perform collision detection between the new circle and circles that are already present
				//if they collide we generate a new position
				/*while
					(	( (nX + radius*2) >= objX  && nX <= (objX + object.getRadius()*2)   )
							&& // in the circle widthwise
					 ((nY + radius*2) >= objY   &&  (nY <= objY + object.getRadius()*2 ) )  ) // in the circle heightwise 



				{
					spawningposition = generateRandomPostion(Gdx.graphics.getHeight()- radius, radius);
					nX = (int) spawningposition.x;
					nY = (int) spawningposition.y;
				}*/
			}


			dummy.dispose();
			circles.add(new GameObject((int)spawningposition.x, (int)spawningposition.y, type));
			System.out.println("Spawned new circle at " + spawningposition.x + "," + spawningposition.y);
			spawnedObjectCount++;
			spawnedCirclesPartial++;
			
		}

	}
	
	private boolean checkCollision(GameObject circle1, GameObject circle2) // used for checking spawning point
	{
		
		int sumoftheradii= circle1.radius + circle2.radius;
		int distancebetweenCentres = (int)circle1.getCentre().dst(circle2.getCentre());
		
		 return (distancebetweenCentres<=sumoftheradii);
	}
	
	
	public boolean checkHit(int touchx, int touchy)
	{
		GameObject hit=null;
		boolean therewasaHit =false;
		if (Gdx.input.isTouched())
		{
			
			for (GameObject object:circles)
			{
				
					int circleX = (int)object.getPosition().x;
					int circleY = (int)object.getPosition().y;

					//int touchx =Gdx.input.getX();
					//int touchy =Gdx.input.getY();

					System.out.println("X "+ touchx);
					System.out.println("Y "+ touchy);

					if((touchx<= circleX+object.getRadius()*2 && touchx>=circleX) // in the circle widthwise
							&& //and
					(touchy>=circleY && touchy<=circleY + object.getRadius()*2)) // in the circle heightwise
					{
						System.out.println("Hit!!");
						hit = object;
						therewasaHit = true;
						
						manageHit(hit, touchx, touchy); // here we determine which of the concentric circles was hit
						
						int typePost=hit.getRadius(); 
						
						
						//post the data to the server
						if(connectedToInternet())
						{
							
							new DataPoster().execute( new DataForPosting 
									( (int)(hit.getRealCentre().x), 
									(int)(hit.getRealCentre().y), (int)(touchx), (int)(touchy) , 
									typePost , hit.getObjectTime() ) );
							
						}
					}
					else
					{ 

						//accuracyBar.increaseAccuracyBar(spawnedObjectCount, false); // decrease accuracy bar
					}
				}
			

		}
		//remove all hit objects from the list				
		if(therewasaHit)//manage the hit!!!
		{
			createPopUpScoreText(hit.getPosition());
			circles.remove(hit);
			hit.dispose(); // clear the memory of the object
			
			
			
			
			//renderables.add(new Line(hit.getCentre(), new Vector2(touchx, touchy))); // draw a line!;
			
			//spawn new objects!!!!
			
			
			int howMany = new Random().nextInt(2) + 1;
			if(circles.isEmpty())
				spawnNewCircle(howMany);
			
			/*if(circles.isEmpty()) // the player has
				nexLevel(); // all circles have been hit by the player so go to the next level*/
			
			
		}	
		return therewasaHit;
	}
	 
	
	public void  reset()
	{
		renderables.clear();
		circles.clear();
		
		create();
		
		spawnedObjectCount=0; 
		
		score = 0;
		
		currentLevel =1;
		
		
		bonusMultiplier =1;
		streakCount=0;
		hitCount=0;
		
		spawnedCirclesPartial =0;
		maxCircles = startCircles ;
		
		lives = 3 ;
		
		spawnNewCircle(startCircles);
		
		levelTextVal.setText(String.valueOf(currentLevel));
		scoreTextVal.setText(String.valueOf(score));
		livesTextVal.setText(String.valueOf(lives));
		
		//loading is complete
		isLoading=false;
		TOTAL_TIME = 4.0f; // reset the total time
		totalTime = TOTAL_TIME;
		
	}
	
	public void nexLevel()
	{
		System.gc();
		
		levelpopupGameText = new HitGameText(new Vector2(0,Gdx.graphics.getHeight()/2), "NEW LEVEL!",false);
		levelpopupGameText.setColour(Color.BLUE);
		levelpopupGameText.setSize(2.0f);
		
		countDown = TIME_TO_NEXT_LEVEL;
		
		currentLevel++;
		spawnedCirclesPartial=0;
		
		levelTextVal.setText(String.valueOf(currentLevel));

		decreaseTotalTime();

	/*	gameTimeAlert = new HitGameText(new Vector2(0, Gdx.graphics.getHeight()/2), "Circle Time Reduced",false);
		gameTimeAlert.setSize(2f);*/




		
		int lowLimit=1;
		int uplimit=2;
		
		if(currentLevel >5)
		{
			uplimit =spawnLimit;
			lowLimit =1;
		}
		
		
		maxCircles  = new Random().nextInt(uplimit) + lowLimit; 
		
		spawnNewCircle(maxCircles);
		
		
		//update the level info
			}
	
	public void drawScene(SpriteBatch refBatch)
	{
		/*shapeRender.setProjectionMatrix(refBatch.getProjectionMatrix());
		shapeRender.setColor(Color.MAGENTA);
		shapeRender.circle(0, 0, 32);*/
		
		for (Renderable object:renderables) // render all non circles
			object.render(refBatch);
				
				 // render these two before everyone else !!
				if(popupGameText!=null)
				{
					if(!popupGameText.isAnimationOver()) // render till the animation is over
					popupGameText.render(refBatch);
				}
				
				if(levelpopupGameText!=null && !levelpopupGameText.isAnimationOver()) // tell the user that the animation is over
					levelpopupGameText.render(refBatch);
				
				/*if(bonusAlertGameText!=null && ! bonusAlertGameText.isAnimationOver())
					bonusAlertGameText.render(refBatch);*/
				
				/*if (gameTimeAlert!=null && !gameTimeAlert.isAnimationOver())
					gameTimeAlert.render(refBatch);*/
				
				if(hitResultGameText!=null && !hitResultGameText.isAnimationOver())
					hitResultGameText.render(refBatch);
								
				/*if(countDown > 0.0f && currentLevel> 1)
					countdownToLevelStart.render(refBatch);*/
					
				
		
		for (GameObject object:circles)
			object.render(refBatch);
				
		
	}
	public boolean updateScene (float elapsedTime)
	{
		List <Renderable> objRem = new ArrayList<Renderable>();
		
		cleanPopUps();

		for (Renderable rend:renderables)
		{
			if(rend.update(elapsedTime))
				objRem.add(rend);
		}
		
		
				List<Renderable> objectsToRemove = new ArrayList<Renderable>();

				for (Renderable object:circles)
				{
					if(object.update(elapsedTime)) // update all game objects
					{
						objectsToRemove.add(object); // time has run out so remove from screen
						streakCount=0;
						lives -- ;
						livesTextVal.setText(String.valueOf(lives));
						if(lives  == 0) 
							return true; // reduce lives and check if game is over 

					}

				}

				for(Renderable garbage:objectsToRemove)
				{
					garbage.dispose();
					circles.remove(garbage);
				}

						for (Renderable rem:objRem)
						{
							rem.dispose();
							renderables.remove(rem); // remove stuff like lines and other stuff 
						/*if (lives <= 0) // the user has no lives left
							return true;*/
						}


						if (lives > 0 && circles.isEmpty() && maxCircles <= spawnedCirclesPartial ) // the user has lives left, there are no objects left on screen, 
																									//and the max number of object have been spawned for that l// so we can proceed to the next level
						{
							
							

							countDown -= elapsedTime; // wait before starting next level  
								

							if(countDown<=0.0f)
							{
								nexLevel();
							}
						}
						
						if (popupGameText!= null)
							popupGameText.update(elapsedTime);
						
						if(levelpopupGameText!=null && ! levelpopupGameText.isAnimationOver())
							levelpopupGameText.update(elapsedTime);
						
					/*	if(bonusAlertGameText!=null && ! bonusAlertGameText.isAnimationOver())
							bonusAlertGameText.update(elapsedTime);*/
						
						/*if (gameTimeAlert!=null && !gameTimeAlert.isAnimationOver())
							gameTimeAlert.update(elapsedTime);*/
						
						totalTime-=elapsedTime;
						if(totalTime<=0)
							
						{
							totalTime=TOTAL_TIME;
						}
						
						
						
						if(hitResultGameText!=null && !hitResultGameText.isAnimationOver())
							hitResultGameText.update(elapsedTime);
						
						
						
						
						return false;
	}
	
	private Vector2 generateRandomPostion(int maxSize , int radius)
	{
		// from http://stackoverflow.com/questions/5969447/java-random-integer-with-non-uniform-distribution
	    //Get a linearly multiplied random number
		
		//generate y
		int xPos = new Random().nextInt(Gdx.graphics.getWidth()-(radius*2));
		
		
	    int randomMultiplier = maxSize * (maxSize + 1) / 2;
	    Random r=new Random();
	    int randomInt = r.nextInt(randomMultiplier);

	    //Linearly iterate through the possible values to find the correct one
	    
	    int linearRandomNumber = 0;
	    for(int i=maxSize; randomInt >= 0; i--){
	        randomInt -= i;
	        linearRandomNumber++;
	    
	    
	    }

	    return new Vector2(xPos, linearRandomNumber);
		
		
	}
	
	public void cleanUp()
	{
		for(Renderable object:renderables)
			object.dispose(); // we do all the cleaning up
				
		for (GameObject circle:circles)
			circle.dispose();
		
		if(popupGameText!=null)
			popupGameText.dispose();
		
		if(levelpopupGameText!=null)
			levelpopupGameText.dispose();
		
			
	/*	if(bonusAlertGameText!=null)
			bonusAlertGameText.dispose();*/
		
		/*if(gameTimeAlert!=null)
			gameTimeAlert.dispose();*/
		
		if(hitResultGameText!=null)
			hitResultGameText.dispose();
			
	}
	
	private void create()
	{
		renderables.add(background);
		renderables.add(scoreText);
		renderables.add(scoreTextVal);
		renderables.add(levelText);
		renderables.add(levelTextVal);
		renderables.add(livesText);
		renderables.add(livesTextVal);
	}
	
	public void pause()
	{
		
	}
	
	public void resume()
	{
		
	}
	
	
	
	private GameManager()
	{
		
		
		isLoading = true;
		renderables = new ArrayList<Renderable>();
		circles = new ArrayList<GameObject>();
		
		background = new Background();
		scoreText = new GameText(new Vector2(0, Gdx.graphics.getHeight()-50), "Score ");
		scoreText.setColour(Color.YELLOW);
		scoreTextVal = new GameText(
				new Vector2(scoreText.getPosition().x + scoreText.getFontWidth() + 15, scoreText.getPosition().y)
				, String.valueOf(score), false);
		scoreTextVal.setSize(2.3f);
		scoreTextVal.setColour(Color.YELLOW);
		
		
		levelText = new GameText(new Vector2(0, Gdx.graphics.getHeight()-10), "Level ");
		levelText.setColour(Color.YELLOW);
		levelTextVal = new GameText(new Vector2(levelText.getPosition().x + levelText.getFontWidth() + 15, levelText.getPosition().y  ) , 
				       String.valueOf(currentLevel), false);
		levelTextVal.setSize(2.3f);
		levelTextVal.setColour(Color.YELLOW);
		
		
		
		
		livesText = new GameText(new Vector2(Gdx.graphics.getWidth() - 158 , Gdx.graphics.getHeight()) , "Lives ", false);
		livesTextVal = 
				new GameText(new Vector2(livesText.getPosition().x + 80 , Gdx.graphics.getHeight()) , 
						String.valueOf(lives), false);
		livesText.setColour(Color.WHITE);
		livesTextVal.setColour(Color.WHITE);
		livesText.setSize(2.0f);
		livesTextVal.setSize(2.0f);
		
		
		
	}
	
	
	
	public boolean isLoading()
	{
		return isLoading;
	}
	
	private boolean connectedToInternet()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean weHaveMobileConnection =
       		 connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean weHaveWifiConnection =
       		 connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        
       return (weHaveMobileConnection || weHaveWifiConnection);
	}
	
	public void decreaseTotalTime() { 
		if (TOTAL_TIME>=2.0f) // do not make life of circle shorter than 2 secs
		{
			TOTAL_TIME -= (0.250f);
		} // decrease time by ten 10 %
	}
	public void increaseTotalTime ()
	{
		if(TOTAL_TIME<= 4.5f) // DO NOT MAKE LIFE LONGER THAN 4.5 SECS
		{
			TOTAL_TIME += (0.250f);
		}  // increase circle life by ten 10 %
		
	}
	
	public float getTotalTime() // return the circle current life
	{
		return TOTAL_TIME;
	}
	
	public void cleanPopUps()
	{
		if(popupGameText!=null && popupGameText.isAnimationOver())
			popupGameText.dispose();
		
		if(levelpopupGameText!=null && levelpopupGameText.isAnimationOver())
			levelpopupGameText.dispose();
		
			
		/*if(bonusAlertGameText!=null && bonusAlertGameText.isAnimationOver())
			bonusAlertGameText.dispose();*/
		
		/*if(gameTimeAlert!=null)
			gameTimeAlert.dispose();*/
		
		if(hitResultGameText!=null && hitResultGameText.isAnimationOver())
			hitResultGameText.dispose();
	}
	
	
}
