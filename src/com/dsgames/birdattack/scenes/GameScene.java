package com.dsgames.birdattack.scenes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseExponentialOut;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.MotionEvent;

import com.dsgames.birdattack.R;
import com.dsgames.birdattack.advertising.AdvertisingObserver;
import com.dsgames.birdattack.managers.ReservBulletManager;
import com.dsgames.birdattack.managers.ResourcesManager;
import com.dsgames.birdattack.managers.SceneManager;
import com.dsgames.birdattack.pools.BulletPool;
import com.dsgames.birdattack.pools.ExplosionPool;
import com.dsgames.birdattack.scenes.models.BaseScene;
import com.dsgames.birdattack.sprites.Bird;
import com.dsgames.birdattack.sprites.Bullet;
import com.dsgames.birdattack.sprites.Explosion;
import com.dsgames.birdattack.utils.AnimationListenerAdapter;
import com.dsgames.birdattack.utils.Points;
import com.dsgames.birdattack.utils.Utils;

public class GameScene extends BaseScene implements IOnAreaTouchListener{

	// game constants
	public static boolean GAME_IS_PAUSED;
	public static int GAME_LEVEL;
	public static final int GAME_LAST_LEVEL = 50;

	// scene sprites
	private Sprite fundoSprite;
	private Sprite groundSprite;
	private Sprite bulletHudPlateSprite;
	private Sprite pointsHudPlateSprite;
	public Sprite pauseButtonSprite;
	private Sprite engageBullet; // bala do stiling
	private Sprite slingshot_front;
	private Sprite slingshot_back;

	// HUD buttons
	Sprite playButton;
	Sprite restartButton;
	Sprite exitButton;
	Sprite restartGameOverButton;
	Sprite exitGameOverButton;

	// slingshot variables
	public static float shootPosX = 70f;
	public static float shootPosY = 50f;
	public static final float shootAxisX = 110f; // X axis of slingshot rubber
	public static final float shootAxisY = 100f; // y axis of slingshot rubber
	private Line line_01;
	private Line line_02;
	private Line line_03;
	public static final float SHOT_TIME = 0.1f;
	public static boolean SHOOT_IS_PERMITED;
	public static boolean SLINGSHOT_IS_STRETCHED;
	
	// touch variables
	public static double catetoX;
	public static double catetoY;
	public static double hipotenusa;
	public static float angle;

	// birds creation variables
	private static int NUMBER_OF_BIRDS;
	private static int BIRDS_COUNT;
	private float[] birdsPositionsList;
	private Bird[] birdList;
	private int nextBirdPointer;
	private static float TIME_TO_NEXT_BIRD;
	private float timeToNextBirdCounter;
	private static float BIRD_VELOCITY;
	
	// bullets creation variables
	public static final float TIME_TO_NEXT_BULLET = 0.2f;
	public static boolean ENGAGE_BULLET_IS_CHARGED;
	private float timeToNextBulletCounter;
	private List<Bullet> bulletsOnTheAirList;
	private ReservBulletManager reservBulletManager;
	//private Bullet lastBullet = null;

	public GameScene() {
		super();
		setOnAreaTouchListener(this);

		GAME_IS_PAUSED = false;
		GAME_LEVEL = 1;
		NUMBER_OF_BIRDS = 2; // initial number of birds (level up modified)
		BIRDS_COUNT = NUMBER_OF_BIRDS;
		TIME_TO_NEXT_BIRD = 1f; // initial velocity of birds (level up modified)
		BIRD_VELOCITY = 100;
		SHOOT_IS_PERMITED = false;
		SLINGSHOT_IS_STRETCHED = false;
		ENGAGE_BULLET_IS_CHARGED = false;
		
		ResourcesManager.loadGameSceneResources();
		mountScene();
		initBirds(); // inicializa todos os passaros do nível
		initializeBullets(); // initialize bullets and reservBullets lists 
		wait(1f); // wait a moment to launch birds
		
		if(!ResourcesManager.getSharedInstance().gameActivity.TUTORIAL_IS_SHOWED){
			createTutor();
		}
	}

	// --------------------------------------------------------------------------------------------------
	// -- SUPER METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	/** Load all resources of the scene */
	@Override
	public void mountScene() {
		mountSky();
		mountGround();
		mountSlingshot();
		mountHUD();
		sortChildren();
		ResourcesManager.getSharedInstance().gameMusic.play();
	}

	private void mountSky() {
		fundoSprite = new Sprite(450, cameraHeight / 2,
				ResourcesManager.getSharedInstance().gameBackgroundRegion, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		fundoSprite.setCullingEnabled(true);
		attachChild(fundoSprite); // #0
		fundoSprite.setZIndex(0);
		//fundoSprite.registerEntityModifier(new MoveXModifier(100, 500, 300));
	}

	private void mountGround() {
		groundSprite = new Sprite(cameraWidth / 2, 71,
				ResourcesManager.getSharedInstance().gameGroundRegion, vbom);
		attachChild(groundSprite);// #3
		groundSprite.setZIndex(3);
	}
	
	private void mountSlingshot(){
		slingshot_front = new Sprite(122, 59,
				ResourcesManager.getSharedInstance().slingshot_frontRegion,
				vbom);
		slingshot_back = new Sprite(138, 84,
				ResourcesManager.getSharedInstance().slingshot_backRegion, vbom);
		engageBullet = new Sprite(shootPosX, shootPosY, ResourcesManager.getSharedInstance().bulletRegion, vbom);
		engageBullet.setVisible(false);
		line_01 = createSlingshotLine(104, 96, 0, 0,8);
		line_01.setVisible(false);
		line_02 = createSlingshotLine(138, 99, 0, 0,6);
		line_02.setVisible(false);
		line_03 = createSlingshotLine(138, 99, 104, 96,6);
		line_03.setVisible(true);
		
		attachChild(line_01);// #4
		attachChild(line_02);// #5
		attachChild(line_03);// #6
		attachChild(slingshot_back);// #7
		attachChild(engageBullet); // #8
		attachChild(slingshot_front);// #9
		
		line_01.setZIndex(4);
		line_02.setZIndex(5);
		line_03.setZIndex(6);
		slingshot_back.setZIndex(7);
		engageBullet.setZIndex(8);
		slingshot_front.setZIndex(9);
	}

	private void mountHUD() {
		bulletHudPlateSprite = new Sprite(266,455,ResourcesManager.getSharedInstance().gameBulletHudPlateRegion,vbom);
		pointsHudPlateSprite = new Sprite(633,455,ResourcesManager.getSharedInstance().gamePointsHudPlateRegion,vbom);
		pauseButtonSprite = new Sprite(33,447,ResourcesManager.getSharedInstance().gamePauseButtonRegion,vbom);
		registerTouchArea(pauseButtonSprite);
		attachChild(bulletHudPlateSprite);// #12
		attachChild(pointsHudPlateSprite);// #12
		attachChild(pauseButtonSprite);// #13
		attachChild(ResourcesManager.getSharedInstance().gameScoreText);
		attachChild(ResourcesManager.getSharedInstance().gameScoreValText);
		attachChild(ResourcesManager.getSharedInstance().gameLevelText);
		attachChild(ResourcesManager.getSharedInstance().gameLevelValText);
		bulletHudPlateSprite.setZIndex(12);
		pointsHudPlateSprite.setZIndex(12);
		pauseButtonSprite.setZIndex(13);
		ResourcesManager.getSharedInstance().gameLevelText.setZIndex(15);
		ResourcesManager.getSharedInstance().gameLevelValText.setZIndex(15);
		ResourcesManager.getSharedInstance().gameScoreText.setZIndex(15);
		ResourcesManager.getSharedInstance().gameScoreValText.setZIndex(15);
	}

	/** Release all resources of the scene */
	@Override
	public void unmountScene() {
		ResourcesManager.getSharedInstance().gameActivity.hideBannerAdvertise();
		bulletsOnTheAirList = null;
		detachChildren();
		BulletPool.sharedBulletPool().clean();
		ExplosionPool.sharedExplosionPool().clean();
		ResourcesManager.unloadGameSceneResources();
	}

	@Override
	public void gameLoop(float pSecondsElapsed) {
		checkCollisions();
		updateSlinghot(pSecondsElapsed);
		updateBullets(pSecondsElapsed);
		updateBirds(pSecondsElapsed);
	}

	@Override
	public boolean toutchSceneEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		// if games is paused don't needs calculate touch
		if(!GAME_IS_PAUSED){
			
			calculateTouchVariables(pSceneTouchEvent);
			
			switch (pSceneTouchEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				stretchSlingshot();
				if(SHOOT_IS_PERMITED && reservBulletManager.bulletCount()>0){
					showEngagedBullet();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				updateSlingshot();
				updateEngagedBulletPosition();
				break;
			case MotionEvent.ACTION_UP:
				releaseSlingshot();
				if(SHOOT_IS_PERMITED && reservBulletManager.bulletCount()>0){
					hideEngagedBullet();
					shootBullet();
					SHOOT_IS_PERMITED = false;
					timeToNextBulletCounter=0;
				}
				ResourcesManager.getSharedInstance().rubberSound.play();
					
				break;
			}
		}
		return true;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (GAME_IS_PAUSED) {
			super.onManagedUpdate(0);
		} else {
			gameLoop(pSecondsElapsed);
			super.onManagedUpdate(pSecondsElapsed);
		}
	}

	// --------------------------------------------------------------------------------------------------
	// -- UPDATE METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	private void updateBirds(float pSecondsElapsed) {
		timeToNextBirdCounter += pSecondsElapsed;
		if (timeToNextBirdCounter >= TIME_TO_NEXT_BIRD && nextBirdPointer <= NUMBER_OF_BIRDS - 1) {
			releaseNextBird();
			timeToNextBirdCounter = 0;
		}
		moveAllBirds(pSecondsElapsed);
	}
	
	private void updateSlinghot(float pSecondsElapsed){
		timeToNextBulletCounter += pSecondsElapsed;
		if(timeToNextBulletCounter>=TIME_TO_NEXT_BULLET){
			SHOOT_IS_PERMITED = true;
			timeToNextBulletCounter = 0;
			if(SLINGSHOT_IS_STRETCHED && reservBulletManager.bulletCount()>0){
				engageBullet.setPosition(shootPosX, shootPosY);
				if(!ENGAGE_BULLET_IS_CHARGED){
					engageBullet.registerEntityModifier(new ScaleModifier(0.5f, 0, 1f, EaseExponentialOut.getInstance()));
				}
				engageBullet.setVisible(true);
			}
		}
		
	}
	
	private void updateBullets(float pSecondsElapsed){
		Iterator<Bullet> iterator = bulletsOnTheAirList.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.move(pSecondsElapsed);
			if (bullet.getX() <= -(bullet.getWidth() / 2)
					|| bullet.getX() >= cameraWidth
							+ (bullet.getWidth() / 2)
					|| bullet.getY() <= -(bullet.getHeight() / 2)
					|| bullet.getY() >= cameraHeight
							+ (bullet.getHeight() / 2)) {
				BulletPool.sharedBulletPool().recyclePoolItem(bullet);
				iterator.remove();
				if(reservBulletManager.bulletCount()<=0 && bulletsOnTheAirList.size()==0){
					GAME_IS_PAUSED = true;
					createGameOver();
				}
			}
		}
	}

	// --------------------------------------------------------------------------------------------------
	// -- BIRDS METHODS -- //
	// --------------------------------------------------------------------------------------------------

	private void releaseNextBird() {
		Bird bird = birdList[nextBirdPointer];
		bird.enable();
		nextBirdPointer++;
	}
	
	private void moveAllBirds(float pSecondsElapsed) {
		for (int i = 0; i < NUMBER_OF_BIRDS; i++) {
			if (birdList[i].isEnable()) {
				birdList[i].move(pSecondsElapsed);
				// verify off screen
				if (birdList[i].getX() + birdList[i].getWidth() / 2 < 0) {
					
					// subtract bird
					birdList[i].disable();
					BIRDS_COUNT--;
					
					// verify last bird off screen (to next Level)
					if (nextBirdPointer == NUMBER_OF_BIRDS && BIRDS_COUNT == 0) {
						if(GAME_LEVEL < GAME_LAST_LEVEL) {
							levelUp();
						} else {
							// stage complete
							GAME_IS_PAUSED = true;
							createStageComplete();
						}
					// verify game over
					}else if(reservBulletManager.bulletCount()<=1){
						GAME_IS_PAUSED = true;
						createGameOver();
					}else{
						//subtractReserveBullet();
					}
				}
			}
		}
	}
	
	private void initBirds() {
		birdsPositionsList = new float[NUMBER_OF_BIRDS];
		birdList = new Bird[NUMBER_OF_BIRDS];
		for (int i = 0; i < NUMBER_OF_BIRDS; i++) {
			int birdPosition = (Utils.getInt(245) + 1) + 155;
			birdsPositionsList[i] = birdPosition;
			birdList[i] = new Bird();
			birdList[i].setPosition(cameraWidth + (birdList[i].getWidth() / 2),
					birdPosition);
			birdList[i].setVelocity(BIRD_VELOCITY);
			birdList[i].disable();
			birdList[i].setZIndex(10);
			attachChild(birdList[i]);
			// birdList[i].setVisible(false);
		}
		nextBirdPointer = 0;
	}
	
	private void makeBirdExplosion(float x, float y) {
		final Explosion explosion = new Explosion();
		explosion.setPosition(x, y);
		attachChild(explosion);
		explosion.setZIndex(11);
		sortChildren();
		explosion.animate(30, false, new AnimationListenerAdapter() {
			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				ResourcesManager.getSharedInstance().engine
						.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								explosion.clearUpdateHandlers();
								explosion.detachSelf();
							}
						});
			}
		});
	}

	// --------------------------------------------------------------------------------------------------
	// -- SLINGSHOT METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	private Line createSlingshotLine(int x1,int y1,int x2,int y2,int strokeWidth){
		Line line = new Line(x1, y1, x2, y2, vbom);
		line.setLineWidth(strokeWidth);
		line.setColor(0.515625f,0.30859375f,0.03515625f);
		line.setBlendingEnabled(true);
		return line;
	}
	
	private void stretchSlingshot(){
		line_01.setPosition(104, 96, shootPosX, shootPosY);
		line_01.setVisible(true);
		line_02.setPosition(138, 99, shootPosX, shootPosY);
		line_02.setVisible(true);
		line_03.setVisible(false);
		SLINGSHOT_IS_STRETCHED = true;
	}
	
	private void releaseSlingshot(){
		line_01.setVisible(false);
		line_02.setVisible(false);
		line_03.setVisible(true);
		SLINGSHOT_IS_STRETCHED = false;
	}
	
	private void updateSlingshot(){
		line_01.setPosition(104, 96, shootPosX, shootPosY);
		line_02.setPosition(138, 99, shootPosX, shootPosY);
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- BULLET METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	private void showEngagedBullet(){
		engageBullet.setVisible(true);
		engageBullet.registerEntityModifier(new ScaleModifier(0.5f, 0, 1f, EaseExponentialOut.getInstance()));
		engageBullet.setPosition(shootPosX, shootPosY);
		ENGAGE_BULLET_IS_CHARGED = true;
	}

	private void hideEngagedBullet(){
		engageBullet.setVisible(false);
		engageBullet.clearEntityModifiers();
		ENGAGE_BULLET_IS_CHARGED = false;
	}
	
	private void updateEngagedBulletPosition(){
		engageBullet.setPosition(shootPosX, shootPosY);
	}
	
	private void shootBullet(){
		
		subtractReserveBullet();
		
		Bullet bullet = BulletPool.sharedBulletPool().obtainPoolItem();
		
		// verify last bullet shooted
		/*if(!reservBulletManager.checkExistsBullet()){
			this.lastBullet = bullet;
		}*/

		// calculates direction velocity
		bullet.velX = (catetoX / hipotenusa) * bullet.accel;
		bullet.velY = (catetoY / hipotenusa) * bullet.accel;
		bullet.rotationVel = (new Random()).nextInt(20);

		bullet.setPosition(shootPosX, shootPosY);
		if(!bullet.isAttached()){
			bullet.setAttached(true);
			attachChild(bullet);
		}
		bulletsOnTheAirList.add(bullet);
		bullet.setZIndex(8);
		//sortChildren();
	}
	
	private void subtractReserveBullet(){
		reservBulletManager.subtractReserveBullet();
	}
	
	public void addReserveBullet(){
		reservBulletManager.addReserveBullet();
	}
	
	private void initializeBullets(){
		bulletsOnTheAirList = new LinkedList<Bullet>(); // init bullets shooted list
		reservBulletManager = new ReservBulletManager();
		for(int i = 0;i<reservBulletManager.NUMBER_OF_BULLETS;i++){
			Sprite reserveBullet = new Sprite(90+(i*32), 455, ResourcesManager.getSharedInstance().reserveBulletRegion, vbom);
			reserveBullet.setZIndex(14);
			reservBulletManager.reserveBulletList[i] = reserveBullet;
			attachChild(reserveBullet);
			reservBulletManager.reservBulletPointer++;
		}
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- LEVEL METHODS -- //
	// --------------------------------------------------------------------------------------------------

	private void levelUp() {
		addReserveBullet();
		//lastBullet = null;
		// Wait a moment to release next wave
		registerEntityModifier(new DelayModifier(1f,
				new IEntityModifierListener() {
					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}
					@Override
					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						ResourcesManager.getSharedInstance().engine.stop();
						NUMBER_OF_BIRDS += 2;
						BIRDS_COUNT = NUMBER_OF_BIRDS;
						BIRD_VELOCITY += 10;
						GAME_LEVEL++;
						initBirds();
						ResourcesManager.getSharedInstance().gameLevelValText
								.setText(String.format("%02d", GAME_LEVEL));
						ResourcesManager.getSharedInstance().engine.start();
					}
				}));
	}

	private void wait(float time) {
		registerEntityModifier(new DelayModifier(time, new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				tryToShowBannerAdvertise();
				tryToLoadIntersticialAdvertise();
			}
		}));
	}

	private void calculateTouchVariables(TouchEvent touchEvent) {
		catetoX = (double) touchEvent.getX() - GameScene.shootPosX;
		catetoY = (double) touchEvent.getY() - GameScene.shootPosY;
		hipotenusa = Math.hypot(catetoX, catetoY);
		angle = (float) Math.toDegrees(Math.atan2(catetoY, catetoX));

		// angular limit correction
		if (angle > 80) {
			angle = 80;
			catetoX = Math.cos(Math.toRadians(angle)) * hipotenusa;
			catetoY = Math.sin(Math.toRadians(angle)) * hipotenusa;
		} else if (angle < 20) {
			angle = 20;
			catetoX = Math.cos(Math.toRadians(angle)) * hipotenusa;
			catetoY = Math.sin(Math.toRadians(angle)) * hipotenusa;
		}
		
		// calculates shoot position
		shootPosX = shootAxisX - ((float) (catetoX / hipotenusa) * 80);
		shootPosY = shootAxisY - ((float) (catetoY / hipotenusa) * 80);
	}
	
	private void checkCollisions() {
		Iterator<Bullet> bulletsIterator = bulletsOnTheAirList.iterator();
		while (bulletsIterator.hasNext()) {
			Bullet bullet = bulletsIterator.next();
			for(int i=0;i<NUMBER_OF_BIRDS;i++){ // search all birds in scene
				Bird bird = birdList[i];
				if (bird.collidesWith(bullet) && bird.isVisible()) {
					
					int increment = (int) (bird.getX()*0.01f);
					attachChild(new Points(bird.getX(),bird.getY(),ResourcesManager.getSharedInstance().gamePointsFont, String.format("%02d",1+increment)));
					
					float explosionPosX = bird.getX();
					float explosionPosY = bird.getY();
					makeBirdExplosion(explosionPosX, explosionPosY);
					
					birdList[i].disable();
					BIRDS_COUNT--;
					
					addReserveBullet();
					
					BulletPool.sharedBulletPool().recyclePoolItem(bullet);
					bulletsIterator.remove();
					
					ResourcesManager.getSharedInstance().explodeSound.play();
					
					ResourcesManager.getSharedInstance().GAME_SCORE+=1+increment;
					ResourcesManager.getSharedInstance().gameScoreValText.setText(String.format("%05d",ResourcesManager.getSharedInstance().GAME_SCORE));
					
					if(BIRDS_COUNT==0){
						levelUp();
					}
				}
			}
		}
	}
	
	private void tryToSaveScore(){
		ResourcesManager.getSharedInstance().gameActivity.saveScore(""+ResourcesManager.getSharedInstance().GAME_SCORE);
		ResourcesManager.getSharedInstance().GAME_SCORE=0;
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- MENU sub Scenes-- // 
	// --------------------------------------------------------------------------------------------------
	
	public void createPauseMenu() {
		Text playText = ResourcesManager.getSharedInstance().gameMenuPlayText;
		Text resetText = ResourcesManager.getSharedInstance().gameMenuResetText;
		Text quitText = ResourcesManager.getSharedInstance().gameMenuQuitText;
		playText.detachSelf();
		resetText.detachSelf();
		quitText.detachSelf();
		Scene menuScene = new MenuScene(
				ResourcesManager.getSharedInstance().camera);
		Rectangle rectShape = new Rectangle(0, 0, cameraWidth, cameraHeight,
				vbom);
		rectShape.setColor(0f, 0f, 0f, 0.5f);
		rectShape.setPosition(cameraWidth / 2, cameraHeight / 2);
		menuScene.attachChild(rectShape);
		playButton = new Sprite(0, 0,
				ResourcesManager.getSharedInstance().gameResumeButtonRegion,
				vbom);
		playButton.setPosition((cameraWidth / 2) - 100,
				(cameraHeight / 2) + 100);
		restartButton = new Sprite(0, 0,
				ResourcesManager.getSharedInstance().gameRestartButtonRegion,
				vbom);
		restartButton.setPosition((cameraWidth / 2) - 100, cameraHeight / 2);
		exitButton = new Sprite(0, 0,
				ResourcesManager.getSharedInstance().gameExitButtonRegion, vbom);
		exitButton.setPosition((cameraWidth / 2) - 100,
				(cameraHeight / 2) - 100);
		menuScene.registerTouchArea(playButton);
		menuScene.registerTouchArea(restartButton);
		menuScene.registerTouchArea(exitButton);
		menuScene.attachChild(playButton);
		menuScene.attachChild(restartButton);
		menuScene.attachChild(exitButton);
		menuScene.attachChild(playText);
		menuScene.attachChild(resetText);
		menuScene.attachChild(quitText);
		menuScene.setOnAreaTouchListener(this);
		menuScene.setBackgroundEnabled(false);
		setChildScene(menuScene);
	}

	public void createGameOver() {
		// scene
		Scene gameOverScene = new MenuScene(
				ResourcesManager.getSharedInstance().camera);
		Rectangle rectShape = new Rectangle(0, 0, cameraWidth, cameraHeight,
				vbom);
		rectShape.setColor(0f, 0f, 0f, 0.5f);
		rectShape.setPosition(cameraWidth / 2, cameraHeight / 2);
		gameOverScene.attachChild(rectShape);

		// texts
		Text gameOverText = ResourcesManager.getSharedInstance().gameOverText;
		Text resetOverText = ResourcesManager.getSharedInstance().gameMenuResetText;
		resetOverText.setY(cameraHeight / 2);
		Text quitOverText = ResourcesManager.getSharedInstance().gameMenuQuitText;
		quitOverText.setY(cameraHeight / 2 - 100);
		gameOverText.detachSelf();
		resetOverText.detachSelf();
		quitOverText.detachSelf();
		gameOverScene.attachChild(gameOverText);
		gameOverScene.attachChild(resetOverText);
		gameOverScene.attachChild(quitOverText);

		// buttons
		restartGameOverButton = new Sprite(0, 0,
				ResourcesManager.getSharedInstance().gameRestartButtonRegion,
				vbom);
		restartGameOverButton.setPosition((cameraWidth / 2) - 100,
				cameraHeight / 2);
		exitGameOverButton = new Sprite(0, 0,
				ResourcesManager.getSharedInstance().gameExitButtonRegion, vbom);
		exitGameOverButton.setPosition((cameraWidth / 2) - 100,
				(cameraHeight / 2) - 100);
		gameOverScene.registerTouchArea(restartGameOverButton);
		gameOverScene.registerTouchArea(exitGameOverButton);
		gameOverScene.attachChild(restartGameOverButton);
		gameOverScene.attachChild(exitGameOverButton);

		gameOverScene.setOnAreaTouchListener(this);
		gameOverScene.setBackgroundEnabled(false);

		tryToSaveScore();
		
		setChildScene(gameOverScene);
	}

	public void createStageComplete() {
		// scene
		Scene gameStageCompleteScene = new MenuScene(
				ResourcesManager.getSharedInstance().camera);
		Rectangle rectShape = new Rectangle(0, 0, cameraWidth, cameraHeight,
				vbom);
		rectShape.setColor(0f, 0f, 0f, 0.5f);
		rectShape.setPosition(cameraWidth / 2, cameraHeight / 2);
		gameStageCompleteScene.attachChild(rectShape);

		// texts
		Text stageCompleteText = ResourcesManager.getSharedInstance().gameStageCompleteText;
		Text resetOverText = ResourcesManager.getSharedInstance().gameMenuResetText;
		resetOverText.setY(cameraHeight / 2);
		Text quitOverText = ResourcesManager.getSharedInstance().gameMenuQuitText;
		quitOverText.setY(cameraHeight / 2 - 100);
		stageCompleteText.detachSelf();
		resetOverText.detachSelf();
		quitOverText.detachSelf();
		gameStageCompleteScene.attachChild(stageCompleteText);
		gameStageCompleteScene.attachChild(resetOverText);
		gameStageCompleteScene.attachChild(quitOverText);

		// buttons
		restartGameOverButton = new Sprite(0, 0,
				ResourcesManager.getSharedInstance().gameRestartButtonRegion,
				vbom);
		restartGameOverButton.setPosition((cameraWidth / 2) - 100,
				cameraHeight / 2);
		exitGameOverButton = new Sprite(0, 0,
				ResourcesManager.getSharedInstance().gameExitButtonRegion, vbom);
		exitGameOverButton.setPosition((cameraWidth / 2) - 100,
				(cameraHeight / 2) - 100);
		gameStageCompleteScene.registerTouchArea(restartGameOverButton);
		gameStageCompleteScene.registerTouchArea(exitGameOverButton);
		gameStageCompleteScene.attachChild(restartGameOverButton);
		gameStageCompleteScene.attachChild(exitGameOverButton);

		gameStageCompleteScene.setOnAreaTouchListener(this);
		gameStageCompleteScene.setBackgroundEnabled(false);
		
		tryToSaveScore();

		setChildScene(gameStageCompleteScene);
	}
	
	public void createTutor() {
		ResourcesManager.getSharedInstance().gameActivity.TUTORIAL_IS_SHOWED = true;
		pauseButtonSprite.setVisible(false);
		setOnSceneTouchListener(null);
		GAME_IS_PAUSED = true;
		
		// scene
		Scene gameTutorScene = new MenuScene(ResourcesManager.getSharedInstance().camera);
		Rectangle rectShape = new Rectangle(0, 0, cameraWidth, cameraHeight,
				vbom);
		rectShape.setColor(0f, 0f, 0f, 0.5f);
		rectShape.setPosition(cameraWidth / 2, cameraHeight / 2);
		gameTutorScene.attachChild(rectShape);
		
		// Sprite
		Sprite tutorSprite = new Sprite(400,288,ResourcesManager.getSharedInstance().tutorRegion,vbom){
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				pGLState.setDitherEnabled(true);
				super.preDraw(pGLState, pCamera);
			}
		};
		gameTutorScene.attachChild(tutorSprite);
		
		// texts
		Text tutorText = ResourcesManager.getSharedInstance().gameTutorText;
		tutorText.setPosition(400,125);
		gameTutorScene.attachChild(tutorText);
		
		// buttons
		playButton = new Sprite(0, 0,ResourcesManager.getSharedInstance().gameResumeButtonRegion,vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		playButton.setPosition(700,288);
		gameTutorScene.registerTouchArea(playButton);
		gameTutorScene.attachChild(playButton);
		
		gameTutorScene.setOnAreaTouchListener(this);
		gameTutorScene.setBackgroundEnabled(false);
		
		setChildScene(gameTutorScene);
	}

	// --------------------------------------------------------------------------------------------------
	// -- IOnAreaTouchListener SUPER METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {

		// pause button
		if (pTouchArea == pauseButtonSprite && GAME_IS_PAUSED == false) {
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				ResourcesManager.getSharedInstance().gameClickSound.play();
				pauseButtonSprite.setVisible(false);
				setOnSceneTouchListener(null);
				GAME_IS_PAUSED = true;
				createPauseMenu();
				return true;
			default:
				return false;
			}
		}
		// play button
		else if (pTouchArea == playButton) {
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				ResourcesManager.getSharedInstance().gameClickSound.play();
				break;
			case TouchEvent.ACTION_UP:
				pauseButtonSprite.setVisible(true);
				clearChildScene();
				GAME_IS_PAUSED = false;
				setOnSceneTouchListener(this);
				return true;
			default:
				return false;
			}
		}
		// restart button
		else if (pTouchArea == restartButton) {
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				ResourcesManager.getSharedInstance().gameClickSound.play();
				break;
			case TouchEvent.ACTION_UP:
				ResourcesManager.getSharedInstance().gameActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								AlertDialog.Builder confirm = new AlertDialog.Builder(ResourcesManager.getSharedInstance().gameActivity);
								confirm.setTitle(R.string.confirm_restart_title);
								confirm.setMessage(R.string.confirm_exit_msg);
								confirm.setPositiveButton(R.string.confirm_exit_OK,new OnClickListener(){
										@Override
										public void onClick(DialogInterface dialog,int which){
											ResourcesManager.getSharedInstance().gameActivity.hideBannerAdvertise();
												SceneManager.getInstance().showScene(GameScene.class);
											}
										});
								confirm.setNegativeButton(R.string.confirm_exit_Cancel,new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,int which) {
											}
										});
								confirm.show();
							}
						});
			}
		}
		// exit button
		else if (pTouchArea == exitButton) {
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				ResourcesManager.getSharedInstance().gameClickSound.play();
				break;
			case TouchEvent.ACTION_UP:
				ResourcesManager.getSharedInstance().gameActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AlertDialog.Builder confirm = new AlertDialog.Builder(
								ResourcesManager.getSharedInstance().gameActivity);
						confirm.setTitle(R.string.confirm_exit_title);
						confirm.setMessage(R.string.confirm_exit_msg);
						confirm.setPositiveButton(R.string.confirm_exit_OK,new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								tryToShowIntersticialAdvertise();
							}
						});
						confirm.setNegativeButton(R.string.confirm_exit_Cancel,new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
							}
						});
						confirm.show();
					}
				});
			}
		}
		// game over restart button
		else if (pTouchArea == restartGameOverButton) {
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				ResourcesManager.getSharedInstance().gameClickSound.play();
				break;
			case TouchEvent.ACTION_UP:
				SceneManager.getInstance().showScene(GameScene.class);
			}
		}
		// game over exit button
		else if (pTouchArea == exitGameOverButton) {
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				ResourcesManager.getSharedInstance().gameClickSound.play();
				break;
			case TouchEvent.ACTION_UP:
				ResourcesManager.getSharedInstance().gameActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tryToShowIntersticialAdvertise();
					}
				});
			}
		}
		return true;
	}

	// -------------------------------------------------------------------------------------------------- //
	// -- ADVERTISE METHODS -- //
	// -------------------------------------------------------------------------------------------------- //

	private void tryToShowBannerAdvertise(){
		ResourcesManager.getSharedInstance().gameActivity.setBannerAdvertiseCaller(bannerAdvertisingObserver);
		if(ResourcesManager.getSharedInstance().gameActivity.BANNER_IS_LOADED){
			ResourcesManager.getSharedInstance().gameActivity.displayBannerAdvertise();
		}else{
			ResourcesManager.getSharedInstance().gameActivity.tryToLoadBannerAdvertise();
		}
	}
	
	private void tryToShowIntersticialAdvertise(){
		if(ResourcesManager.getSharedInstance().gameActivity.intersticialAd.isLoaded()){
			ResourcesManager.getSharedInstance().gameActivity.displayIntersticialAdvertise();
		}else{
			SceneManager.getInstance().showScene(com.dsgames.birdattack.scenes.MenuScene.class);
		}
	}
	
	private void tryToLoadIntersticialAdvertise(){
		ResourcesManager.getSharedInstance().gameActivity.setIntersticialAdvertiseCaller(interstialAdvertisingObserver);
		ResourcesManager.getSharedInstance().gameActivity.tryToLoadIntersticialAdvertise();
	}
	
	// -------------------------------------------------------------------------------------------------- //
	// -- AdvertisingObserver SUPER METHODS -- //
	// -------------------------------------------------------------------------------------------------- //

	AdvertisingObserver bannerAdvertisingObserver = new AdvertisingObserver() {
		@Override
		public void advertisingIsLoaded() {
			ResourcesManager.getSharedInstance().gameActivity.displayBannerAdvertise();
		}
		
		@Override
		public void advertisingIsClosed() {
			// DO NOTHING
		}
		
		@Override
		public void advertisingIsFailed() {
			tryToShowBannerAdvertise();
		}
		
		@Override
		public void advertisingIsOpened() {
			// DO NOTHING
		}
		@Override
		public void advertisingIsLeftApp() {
			if(!GAME_IS_PAUSED){
				pauseButtonSprite.setVisible(false);
				createPauseMenu();
				GAME_IS_PAUSED = true;
			}
		}
	};
	
	AdvertisingObserver interstialAdvertisingObserver = new AdvertisingObserver(){
		@Override
		public void advertisingIsLoaded(){
			Debug.d("Interstial is loaded");
		}
		
		@Override
		public void advertisingIsClosed() {
			SceneManager.getInstance().showScene(com.dsgames.birdattack.scenes.MenuScene.class);
		}
		
		@Override
		public void advertisingIsFailed() {
			ResourcesManager.getSharedInstance().gameActivity.tryToLoadIntersticialAdvertise();
		}
		
		@Override
		public void advertisingIsOpened() {
			// DO NOTHING
		}
		@Override
		public void advertisingIsLeftApp() {
			// DO NOTHING
		}
	};
}
