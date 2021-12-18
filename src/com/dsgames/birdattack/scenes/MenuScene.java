package com.dsgames.birdattack.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseExponentialOut;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.EaseQuadOut;
import org.andengine.util.modifier.ease.IEaseFunction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.MotionEvent;

import com.dsgames.birdattack.GameActivity;
import com.dsgames.birdattack.R;
import com.dsgames.birdattack.advertising.AdvertisingObserver;
import com.dsgames.birdattack.managers.ResourcesManager;
import com.dsgames.birdattack.managers.SceneManager;
import com.dsgames.birdattack.scenes.models.BaseScene;

public class MenuScene extends BaseScene implements IOnAreaTouchListener{
	
	private Sprite fundo;
	private Sprite cloud;
	private Sprite cloud_copy;
	private AnimatedSprite bird_01;
	private AnimatedSprite bird_02;
	private AnimatedSprite bird_03;
	private Sprite letter_a;
	private Sprite letter_t;
	private Sprite letter_t2;
	private Sprite letter_a2;
	private Sprite letter_c;
	private Sprite letter_k;
	private Sprite letter_b;
	private Sprite letter_i;
	private Sprite letter_r;
	private Sprite letter_d;
	private Sprite letter_s;
	private Text playText;
	private Text exitText;
	private Text scoreText;
	
	private final float CLOUD_VELOCITY = 1.5f;
	
	public MenuScene() {
		super();
		setOnAreaTouchListener(this);
		mountScene();
		wait(1f);
		//registerFPSCount();
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- SUPER METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	@Override
	public void mountScene() {
		ResourcesManager.loadMenuSceneResources();
		// initialize sprites
		fundo = new Sprite(cameraWidth/2, cameraHeight/2, ResourcesManager.getSharedInstance().menuFundoRegion, vbom){
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				pGLState.enableDither();
				super.preDraw(pGLState, pCamera);
			}
		};
		cloud = new Sprite(cameraWidth/2, 140, ResourcesManager.getSharedInstance().menuCloudRegion, vbom);
		cloud_copy = new Sprite((cameraWidth/2)+cameraWidth, 140, ResourcesManager.getSharedInstance().menuCloudRegion, vbom);
		bird_01 = new AnimatedSprite(-80, 142, ResourcesManager.getSharedInstance().menuBird_01_Region, vbom);
		bird_01.animate(50,true);
		bird_02 = new AnimatedSprite(-41, 191, ResourcesManager.getSharedInstance().menuBird_02_Region, vbom);
		bird_02.animate(50,true);
		bird_03 = new AnimatedSprite(-26, 240, ResourcesManager.getSharedInstance().menuBird_03_Region, vbom);
		bird_03.animate(50,true);
		letter_a = new Sprite(131, 530, ResourcesManager.getSharedInstance().menuLetter_a_Region, vbom);
		letter_t = new Sprite(182, 530, ResourcesManager.getSharedInstance().menuLetter_t_Region, vbom);
		letter_t2 = new Sprite(236, 530, ResourcesManager.getSharedInstance().menuLetter_t_Region, vbom);
		letter_a2 = new Sprite(286, 530, ResourcesManager.getSharedInstance().menuLetter_a_Region, vbom);
		letter_c = new Sprite(340, 530, ResourcesManager.getSharedInstance().menuLetter_c_Region, vbom);
		letter_k = new Sprite(397, 530, ResourcesManager.getSharedInstance().menuLetter_k_Region, vbom);
		letter_b = new Sprite(479, 530, ResourcesManager.getSharedInstance().menuLetter_b_Region, vbom);
		letter_i = new Sprite(521, 530, ResourcesManager.getSharedInstance().menuLetter_i_Region, vbom);
		letter_r = new Sprite(565, 530, ResourcesManager.getSharedInstance().menuLetter_r_Region, vbom);
		letter_d = new Sprite(623, 530, ResourcesManager.getSharedInstance().menuLetter_d_Region, vbom);
		letter_s = new Sprite(676, 530, ResourcesManager.getSharedInstance().menuLetter_s_Region, vbom);
		
		playText = ResourcesManager.getSharedInstance().menuPlayText;
		playText.setPosition(519, 230);
		registerTouchArea(playText);
		exitText = ResourcesManager.getSharedInstance().menuExitText;
		exitText.setPosition(519, 130);
		registerTouchArea(exitText);
		
		scoreText = ResourcesManager.getSharedInstance().menuScoreValText;
		scoreText.setPosition(cameraWidth/2, 460);
		
		registerModifiers();
		
		attachChild(fundo);
		attachChild(bird_03);
		attachChild(cloud);
		attachChild(cloud_copy);
		attachChild(bird_02);
		attachChild(bird_01);
		attachChild(letter_s);
		attachChild(letter_d);
		attachChild(letter_i);
		attachChild(letter_r);
		attachChild(letter_b);
		attachChild(letter_k);
		attachChild(letter_c);
		attachChild(letter_a2);
		attachChild(letter_t2);
		attachChild(letter_t);
		attachChild(letter_a);
		attachChild(playText);
		attachChild(exitText);
		if(ResourcesManager.getSharedInstance().gameActivity.loadScore().length()>0){
			attachChild(scoreText);
		}
		ResourcesManager.getSharedInstance().menuMusic.play();
	}

	@Override
	public void unmountScene() {
		ResourcesManager.getSharedInstance().gameActivity.hideBannerAdvertise();
		detachChildren();
		clearEntityModifiers();
		clearUpdateHandlers();
		ResourcesManager.unloadMenuSceneResources();
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		gameLoop(pSecondsElapsed);
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	@Override
	public void gameLoop(float pSecondsElapsed) {
		moveCloud();
	}

	@Override
	public boolean toutchSceneEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return false;
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- PRIVATE METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	private void moveCloud(){
		// check for reposition
		if(cloud.getX()+399<=0){
			cloud.setX(cameraWidth+(cloud.getWidth()/2));
		}else if(cloud_copy.getX()+399<=0){
			cloud_copy.setX(cameraWidth+(cloud_copy.getWidth()/2));
		}
		// move
		cloud.setX(cloud.getX()-CLOUD_VELOCITY);
		cloud_copy.setX(cloud_copy.getX()-CLOUD_VELOCITY);
	}
	
	private void registerModifiers(){
		// birds
		bird_01.registerEntityModifier(new SequenceEntityModifier(mod_bird_01_Delay, mod_bird_01_Arrive, mod_bird_01_Loop));
		bird_02.registerEntityModifier(new SequenceEntityModifier(mod_bird_02_Delay, mod_bird_02_Arrive, mod_bird_02_Loop));
		bird_03.registerEntityModifier(new SequenceEntityModifier(mod_bird_03_Delay, mod_bird_03_Arrive, mod_bird_03_Loop));
		// letters
		letter_a.registerEntityModifier(new SequenceEntityModifier(mod_letter_a_Delay,mod_letter_a_Down));
		letter_t.registerEntityModifier(new SequenceEntityModifier(mod_letter_t_Delay,mod_letter_t_Down));
		letter_t2.registerEntityModifier(new SequenceEntityModifier(mod_letter_t2_Delay,mod_letter_t2_Down));
		letter_a2.registerEntityModifier(new SequenceEntityModifier(mod_letter_a2_Delay,mod_letter_a2_Down));
		letter_c.registerEntityModifier(new SequenceEntityModifier(mod_letter_c_Delay,mod_letter_c_Down));
		letter_k.registerEntityModifier(new SequenceEntityModifier(mod_letter_k_Delay,mod_letter_k_Down));
		letter_b.registerEntityModifier(new SequenceEntityModifier(mod_letter_b_Delay,mod_letter_b_Down));
		letter_i.registerEntityModifier(new SequenceEntityModifier(mod_letter_i_Delay,mod_letter_i_Down));
		letter_r.registerEntityModifier(new SequenceEntityModifier(mod_letter_r_Delay,mod_letter_r_Down));
		letter_d.registerEntityModifier(new SequenceEntityModifier(mod_letter_d_Delay,mod_letter_d_Down));
		letter_s.registerEntityModifier(new SequenceEntityModifier(mod_letter_s_Delay,mod_letter_s_Down));
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
	
	private void goToGame(){
		SceneManager.getInstance().showScene(GameScene.class);
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- IOnAreaTouchListener SUPER METHODS -- //
	// --------------------------------------------------------------------------------------------------

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		switch (pSceneTouchEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(pTouchArea == playText){
				ResourcesManager.getSharedInstance().menuClickSound.play();
			}else if(pTouchArea == exitText){
				ResourcesManager.getSharedInstance().menuClickSound.play();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			if(pTouchArea == playText){
				tryToShowIntersticialAdvertise();
			}else if(pTouchArea == exitText){
				ResourcesManager.getSharedInstance().gameActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AlertDialog.Builder confirm = new AlertDialog.Builder(ResourcesManager.getSharedInstance().gameActivity);
						confirm.setTitle(R.string.confirm_exit_title);
						confirm.setPositiveButton(R.string.confirm_exit_OK, new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ResourcesManager.getSharedInstance().gameActivity.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										ResourcesManager.unloadMenuSceneResources();
									}
								});
								ResourcesManager.getSharedInstance().gameActivity.finish();
							}
						});
						confirm.setNegativeButton(R.string.confirm_exit_Cancel, new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						});
						confirm.show();
					}
				});
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- EntityModifiers -- //
	// --------------------------------------------------------------------------------------------------

	// bird_01
	IEntityModifier mod_bird_01_Delay = new DelayModifier(1.2f);
	IEntityModifier mod_bird_01_Arrive = new MoveXModifier(1.5f, -80, 319,EaseQuadOut.getInstance());
	IEntityModifier mod_bird_01_MoveUp = new MoveYModifier(1f, 142, 162,EaseLinear.getInstance());
	IEntityModifier mod_bird_01_MoveDown = new MoveYModifier(1f, 162, 142,EaseLinear.getInstance());
	IEntityModifier mod_bird_01_Sequence = new SequenceEntityModifier(mod_bird_01_MoveUp,mod_bird_01_MoveDown);
	IEntityModifier mod_bird_01_Loop = new LoopEntityModifier(mod_bird_01_Sequence);
	// bird_02
	IEntityModifier mod_bird_02_Delay = new DelayModifier(0.6f);
	IEntityModifier mod_bird_02_Arrive = new MoveXModifier(1.5f, -41, 186,EaseQuadOut.getInstance());
	IEntityModifier mod_bird_02_MoveUp = new MoveYModifier(1f, 191, 201,EaseLinear.getInstance());
	IEntityModifier mod_bird_02_MoveDown = new MoveYModifier(1f, 201, 191,EaseLinear.getInstance());
	IEntityModifier mod_bird_02_Sequence = new SequenceEntityModifier(mod_bird_02_MoveUp,mod_bird_02_MoveDown);
	IEntityModifier mod_bird_02_Loop = new LoopEntityModifier(mod_bird_02_Sequence);
	// bird_03
	IEntityModifier mod_bird_03_Delay = new DelayModifier(0.1f);
	IEntityModifier mod_bird_03_Arrive = new MoveXModifier(1.5f, -26, 319,EaseQuadOut.getInstance());
	IEntityModifier mod_bird_03_MoveUp = new MoveYModifier(1f, 240, 245,EaseLinear.getInstance());
	IEntityModifier mod_bird_03_MoveDown = new MoveYModifier(1f, 245, 240,EaseLinear.getInstance());
	IEntityModifier mod_bird_03_Sequence = new SequenceEntityModifier(mod_bird_03_MoveUp,mod_bird_03_MoveDown);
	IEntityModifier mod_bird_03_Loop = new LoopEntityModifier(mod_bird_03_Sequence);
	// Letters
	IEaseFunction ease = EaseExponentialOut.getInstance();
	float duration = 0.5f;
	IEntityModifier mod_letter_a_Delay = new DelayModifier(0.1f);
	IEntityModifier mod_letter_a_Down = new MoveYModifier(duration,530,361,ease);
	IEntityModifier mod_letter_t_Delay = new DelayModifier(0.2f);
	IEntityModifier mod_letter_t_Down = new MoveYModifier(duration,530,361,ease);
	IEntityModifier mod_letter_t2_Delay = new DelayModifier(0.3f);
	IEntityModifier mod_letter_t2_Down = new MoveYModifier(duration,530,361,ease);
	IEntityModifier mod_letter_a2_Delay = new DelayModifier(0.4f);
	IEntityModifier mod_letter_a2_Down = new MoveYModifier(duration,530,361,ease);
	IEntityModifier mod_letter_c_Delay = new DelayModifier(0.5f);
	IEntityModifier mod_letter_c_Down = new MoveYModifier(duration,530,361,ease);
	IEntityModifier mod_letter_k_Delay = new DelayModifier(0.6f);
	IEntityModifier mod_letter_k_Down = new MoveYModifier(duration,530,361,ease);
	IEntityModifier mod_letter_b_Delay = new DelayModifier(0.7f);
	IEntityModifier mod_letter_b_Down = new MoveYModifier(duration,530,361,ease);
	IEntityModifier mod_letter_i_Delay = new DelayModifier(0.8f);
	IEntityModifier mod_letter_i_Down = new MoveYModifier(duration,530,361,ease);
	IEntityModifier mod_letter_r_Delay = new DelayModifier(0.9f);
	IEntityModifier mod_letter_r_Down = new MoveYModifier(duration,530,361,ease);
	IEntityModifier mod_letter_d_Delay = new DelayModifier(1f);
	IEntityModifier mod_letter_d_Down = new MoveYModifier(duration,530,360,ease);
	IEntityModifier mod_letter_s_Delay = new DelayModifier(1.1f);
	IEntityModifier mod_letter_s_Down = new MoveYModifier(duration,530,361,ease);
	
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
		ResourcesManager.getSharedInstance().gameActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(ResourcesManager.getSharedInstance().gameActivity.intersticialAd.isLoaded()){
					ResourcesManager.getSharedInstance().gameActivity.displayIntersticialAdvertise();
				}else{
					goToGame();
				}
			}
		});
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
			// DO NOTHING
		}
	};
	
	AdvertisingObserver interstialAdvertisingObserver = new AdvertisingObserver(){
		@Override
		public void advertisingIsLoaded(){
			Debug.d("Interstial is loaded");
		}
		
		@Override
		public void advertisingIsClosed() {
			goToGame();
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
