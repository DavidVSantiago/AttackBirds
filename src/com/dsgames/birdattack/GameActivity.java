package com.dsgames.birdattack;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.preferences.SecureSharedPreferences;
import org.andengine.util.preferences.SecureSharedPreferences.Editor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.dsgames.birdattack.advertising.AdvertisingObserver;
import com.dsgames.birdattack.managers.ResourcesManager;
import com.dsgames.birdattack.managers.SceneManager;
import com.dsgames.birdattack.scenes.GameScene;
import com.dsgames.birdattack.scenes.LoadScene;
import com.dsgames.birdattack.scenes.MenuScene;
import com.dsgames.birdattack.scenes.SplashScene;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class GameActivity extends BaseGameActivity {

	private Camera camera;
	
	// Score variables
	SecureSharedPreferences preferences;
	Editor editor;
	
	// AdMob Advertise variables
	public InterstitialAd intersticialAd; // intersticial advertising
	public AdView adView; // banner advertising
	private AdRequest intersticialRequest;
	private AdRequest bannerRequest;
	private AdvertisingObserver intersticialAdvertisingObserver;
	private AdvertisingObserver bannerAdvertisingObserver;
	public static boolean BANNER_IS_LOADED = false;
	public static boolean TUTORIAL_IS_SHOWED = false;

	// -------------------------------------------------------------------------------------------------- //
	// -- LIFE_CIRCLE SUPER METHODS -- //
	// -------------------------------------------------------------------------------------------------- //
	
	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		preferences = new SecureSharedPreferences(getSharedPreferences("score", 0), "rasta");
		preprareIntersticialAdvertise();
	}
	
	@Override
	public void onDestroyResources() throws IOException {
		super.onDestroyResources();
		if(isGameLoaded()){
			System.exit(0); // ensures kill activity
		}
	}

	/*@Override
	protected void onStop() {
		if(SceneManager.getInstance().getCurrentScene() instanceof GameScene){
			ResourcesManager.getSharedInstance().gameMusic.pause();
		}else if(SceneManager.getInstance().getCurrentScene() instanceof LoadScene){
			// DO NOTHING
		}else if(SceneManager.getInstance().getCurrentScene() instanceof MenuScene){
			ResourcesManager.getSharedInstance().menuMusic.pause();
		}
		super.onStop();
	}*/
	
	/*@Override
	protected synchronized void onResume() {
		if(SceneManager.getInstance().getCurrentScene() instanceof GameScene){
			ResourcesManager.getSharedInstance().gameMusic.play();
		}else if(SceneManager.getInstance().getCurrentScene() instanceof LoadScene){
			// DO NOTHING
		}else if(SceneManager.getInstance().getCurrentScene() instanceof MenuScene){
			ResourcesManager.getSharedInstance().menuMusic.play();
		}
		super.onResume();
	}*/
	
	@Override
	public void onBackPressed() {
		if(SceneManager.getInstance().getCurrentScene() instanceof GameScene){
			GameScene gameScene = (GameScene) SceneManager.getInstance().getCurrentScene();
			if(!gameScene.GAME_IS_PAUSED){
				gameScene.pauseButtonSprite.setVisible(false);
				gameScene.createPauseMenu();
				gameScene.GAME_IS_PAUSED = true;
			}
		}else if(SceneManager.getInstance().getCurrentScene() instanceof LoadScene){
			// DO NOTHING! don't be able to quit
		}else if(SceneManager.getInstance().getCurrentScene() instanceof SplashScene){
			// DO NOTHING! don't be able to quit
		}else if(SceneManager.getInstance().getCurrentScene() instanceof MenuScene){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AlertDialog.Builder confirm = new AlertDialog.Builder(GameActivity.this);
					confirm.setTitle(R.string.confirm_exit_title);
					confirm.setPositiveButton(R.string.confirm_exit_OK, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									ResourcesManager.unloadMenuSceneResources();
								}
							});
							finish();
						}
					});
					confirm.setNegativeButton(R.string.confirm_exit_Cancel, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					});
					confirm.show();
				}
			});
		}else{
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onSetContentView() {
		// 01 - prepare main layout
		final FrameLayout frameLayout = new FrameLayout(this);
		// 02 - prepare game surface view
		this.mRenderSurfaceView = new RenderSurfaceView(this);
		mRenderSurfaceView.setRenderer(mEngine, this);
		// 03 - prepare banner view
		preprareBannerAdvertise();
		
		// Layouts params (01 main layout / 02 game surface / 03 advertise view)
        final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,Gravity.CENTER);
        final FrameLayout.LayoutParams surfaceViewLayoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        final FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.RIGHT|Gravity.BOTTOM);
 
        // ADD the game surface view and advertise view to the main frame //
        frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
        frameLayout.addView(adView, adViewLayoutParams);
 
        // ADD the views to main frame layout
        this.setContentView(frameLayout, frameLayoutLayoutParams);
	}
	
	// -------------------------------------------------------------------------------------------------- //
	// -- BaseGameActivity SUPER METHODS -- //
	// -------------------------------------------------------------------------------------------------- //
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		Engine engine = new LimitedFPSEngine(pEngineOptions, 60);
		//Engine engine = new Engine(pEngineOptions);
		engine.disableAccelerationSensor(this);
		engine.disableOrientationSensor(this);
		return engine;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, 800, 480);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						800, 480), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getTouchOptions().setNeedsMultiTouch(false);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
		ResourcesManager.prepareManager(mEngine, this, camera,
				getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		SplashScene splashScene = new SplashScene();
		SceneManager.getInstance().setCurrentScene(splashScene);
		//GameScene gameScene = new GameScene();
		//SceneManager.getInstance().setCurrentScene(gameScene);
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	// -------------------------------------------------------------------------------------------------- //
	// -- SCORE METHODS -- //
	// -------------------------------------------------------------------------------------------------- //
	
	public void saveScore(String score){
		if(Integer.parseInt(score)>0){ // testa score válido
			if(loadScore().length()==0){ // testa se já existe registro de score 
				// primeiro registro
				editor = preferences.edit();
				editor.putString("score", score);
				editor.clear();
				editor.commit();
			} else if(Integer.parseInt(score) > Integer.parseInt(loadScore())){ // testa novo melhor score
				// demais registros
				editor = preferences.edit();
				editor.putString("score", score);
				editor.clear();
				editor.commit();
			}
		}
	}
	
	public String loadScore(){
		String score = preferences.getString("score", "");
		return score;
	}
	
	// -------------------------------------------------------------------------------------------------- //
	// -- ADVERTISE METHODS -- //
	// -------------------------------------------------------------------------------------------------- //
	
	// INTERSTICIAL
	public void setIntersticialAdvertiseCaller(AdvertisingObserver advertisingObserver){
		this.intersticialAdvertisingObserver = advertisingObserver;
	}
	
	private void preprareIntersticialAdvertise(){
		// create intersticial AD
		intersticialAd = new InterstitialAd(this);
		intersticialAd.setAdUnitId("ca-app-pub-1938973842309251/3028370534");
		intersticialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				intersticialAdvertisingObserver.advertisingIsLoaded();
			}
			@Override
			public void onAdFailedToLoad(int errorCode){
				switch (errorCode) {
				case AdRequest.ERROR_CODE_INTERNAL_ERROR:
					Log.d("Ads", "Erro - Erro interno!");
					break;
				case AdRequest.ERROR_CODE_INVALID_REQUEST:
					Log.d("Ads", "Erro - Request code inválido!");
					break;
				case AdRequest.ERROR_CODE_NETWORK_ERROR:
					Log.d("Ads", "Erro - Erro de rede!");
					break;
				case AdRequest.ERROR_CODE_NO_FILL:
					Log.d("Ads", "Erro - Não preencheu!");
					break;
				default:
					Log.d("Ads", "Erro - Desconhecido");
					break;
				}
				intersticialAdvertisingObserver.advertisingIsFailed();
			}
			@Override
			public void onAdOpened() {
				intersticialAdvertisingObserver.advertisingIsOpened();
			}
			@Override
			public void onAdClosed() {
				intersticialAdvertisingObserver.advertisingIsClosed();
			}
			@Override
			public void onAdLeftApplication() {
				intersticialAdvertisingObserver.advertisingIsLeftApp();
			}
		});

		// crate AD solicitation
		intersticialRequest = new AdRequest.Builder()
		//.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		//.addTestDevice("1C434FECB77FFFC7FA00BA29372CFC13")
		.build();
	}
	
	public void displayIntersticialAdvertise(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				intersticialAd.show();
			}
		});
	}
	
	public void tryToLoadIntersticialAdvertise(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try{
					intersticialAd.loadAd(intersticialRequest);
				}catch(IllegalStateException exception){
					exception.printStackTrace();
				}
			}
		});
	}
	
	// BANNER
	public void setBannerAdvertiseCaller(AdvertisingObserver advertisingObserver){
		this.bannerAdvertisingObserver = advertisingObserver;
	}
	
	private void preprareBannerAdvertise(){
		adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-1938973842309251/3903001337");
        adView.setAdSize(AdSize.BANNER);
 
        adView.refreshDrawableState();
        adView.setVisibility(AdView.GONE);
        adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				BANNER_IS_LOADED = true;
				bannerAdvertisingObserver.advertisingIsLoaded();
			}
			@Override
			public void onAdFailedToLoad(int errorCode){
				switch (errorCode) {
				case AdRequest.ERROR_CODE_INTERNAL_ERROR:
					Log.d("Ads", "Erro - Erro interno!");
					break;
				case AdRequest.ERROR_CODE_INVALID_REQUEST:
					Log.d("Ads", "Erro - Request code inválido!");
					break;
				case AdRequest.ERROR_CODE_NETWORK_ERROR:
					Log.d("Ads", "Erro - Erro de rede!");
					break;
				case AdRequest.ERROR_CODE_NO_FILL:
					Log.d("Ads", "Erro - Não preencheu!");
					break;
				default:
					Log.d("Ads", "Erro - Desconhecido");
					break;
				}
				bannerAdvertisingObserver.advertisingIsFailed();
			}
			@Override
			public void onAdOpened() {
				bannerAdvertisingObserver.advertisingIsOpened();
			}
			@Override
			public void onAdClosed() {
				mEngine.start();
				bannerAdvertisingObserver.advertisingIsClosed();
			}
			@Override
			public void onAdLeftApplication() {
				bannerAdvertisingObserver.advertisingIsLeftApp();
			}
		});
        
        bannerRequest = new AdRequest.Builder()
		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		//.addTestDevice("1C434FECB77FFFC7FA00BA29372CFC13")
		.build();
	}
	
	public void displayBannerAdvertise(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(AdView.VISIBLE);
			}
		});
	}

	public void hideBannerAdvertise(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(AdView.GONE);
			}
		});
	}
	
	public void tryToLoadBannerAdvertise(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try{
					adView.loadAd(bannerRequest);
				}catch(IllegalStateException exception){
					exception.printStackTrace();
				}
			}
		});
	}
	
}
