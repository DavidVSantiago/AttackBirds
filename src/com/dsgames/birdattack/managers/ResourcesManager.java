package com.dsgames.birdattack.managers;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.content.res.AssetManager;
import android.graphics.Color;

import com.dsgames.birdattack.GameActivity;
import com.dsgames.birdattack.R;

public class ResourcesManager {

	// -- Manager Variables -- //
	public Engine engine;
	public GameActivity gameActivity;
	public Camera camera;
	public VertexBufferObjectManager vertexBufferObjectManager;
	public static final TextureOptions TEXTURE_QUALITY = TextureOptions.BILINEAR_PREMULTIPLYALPHA;
	private static ResourcesManager instance;
	public static int GAME_SCORE = 0;
	
	// -- LoadScreen Variables -- //
	private Font baseFont;
	public Text loadScreenText;
	
	// -- Menu Variables -- //
	public ITextureRegion menuFundoRegion;
	public ITextureRegion menuCloudRegion;
	public ITiledTextureRegion menuBird_01_Region;
	public ITiledTextureRegion menuBird_02_Region;
	public ITiledTextureRegion menuBird_03_Region;
	public ITextureRegion menuLetter_a_Region;
	public ITextureRegion menuLetter_b_Region;
	public ITextureRegion menuLetter_c_Region;
	public ITextureRegion menuLetter_d_Region;
	public ITextureRegion menuLetter_i_Region;
	public ITextureRegion menuLetter_k_Region;
	public ITextureRegion menuLetter_r_Region;
	public ITextureRegion menuLetter_s_Region;
	public ITextureRegion menuLetter_t_Region;
	private BitmapTextureAtlas menuFundoAtlas;
	private BitmapTextureAtlas menuCloudAtlas;
	private BitmapTextureAtlas menuBird_01_Atlas;
	private BitmapTextureAtlas menuBird_02_Atlas;
	private BitmapTextureAtlas menuBird_03_Atlas;
	private BitmapTextureAtlas menuLetter_a_Atlas;
	private BitmapTextureAtlas menuLetter_b_Atlas;
	private BitmapTextureAtlas menuLetter_c_Atlas;
	private BitmapTextureAtlas menuLetter_d_Atlas;
	private BitmapTextureAtlas menuLetter_i_Atlas;
	private BitmapTextureAtlas menuLetter_k_Atlas;
	private BitmapTextureAtlas menuLetter_r_Atlas;
	private BitmapTextureAtlas menuLetter_s_Atlas;
	private BitmapTextureAtlas menuLetter_t_Atlas;
	private Font menuPlayFont;
	public Text menuPlayText;
	public Text menuExitText;
	public Font menuScoreValFont;
	public Text menuScoreValText;
	public Sound menuClickSound;
	public Music menuMusic;

	// -- Game Scene Variables -- //
	public Text gameScoreText;
	public Text gameScoreValText;
	public Text gameLevelText;
	public Text gameLevelValText;
	public Text gameMenuPlayText;
	public Text gameMenuResetText;
	public Text gameMenuQuitText;
	public Text gameOverText;
	public Text gameStageCompleteText;
	public Text gameTutorText;
	private Font gameFont;
	private Font gameValFont;
	private Font gameMenuFont;
	private Font gameOverFont;
	private Font gameStageCompleteFont;
	private Font gameTutorFont;
	public Font gamePointsFont;
	public ITextureRegion gameBackgroundRegion;
	public ITextureRegion gameGroundRegion;
	public ITextureRegion gameBulletHudPlateRegion;
	public ITextureRegion gamePointsHudPlateRegion;
	public ITextureRegion gamePauseButtonRegion;
	public ITextureRegion gameResumeButtonRegion;
	public ITextureRegion gameRestartButtonRegion;
	public ITextureRegion gameExitButtonRegion;
	public ITiledTextureRegion bird_01_Region;
	public ITextureRegion slingshot_frontRegion;
	public ITextureRegion slingshot_backRegion;
	public ITextureRegion bulletRegion;
	public ITextureRegion reserveBulletRegion;
	public ITiledTextureRegion explosionRegion;
	public ITextureRegion tutorRegion;
	private BitmapTextureAtlas gameBackgroundAtlas;
	private BitmapTextureAtlas gameGroundAtlas;
	private BitmapTextureAtlas gameBulletHudPlateAtlas;
	private BitmapTextureAtlas gamePointsHudPlateAtlas;
	private BitmapTextureAtlas gamePauseButtonAtlas;
	private BitmapTextureAtlas gameResumeButtonAtlas;
	private BitmapTextureAtlas gameRestartButtonAtlas;
	private BitmapTextureAtlas gameExitButtonAtlas;
	private BitmapTextureAtlas bird_01_Atlas;
	private BitmapTextureAtlas slingshot_frontAtlas;
	private BitmapTextureAtlas slingshot_backAtlas;
	private BitmapTextureAtlas bulletAtlas;
	private BitmapTextureAtlas reserveBulletAtlas;
	private BitmapTextureAtlas explosionAtlas;
	private BitmapTextureAtlas tutorAtlas;
	public Sound explodeSound;
	public Sound shootSound;
	public Sound rubberSound;
	public Sound gameClickSound;
	public Music gameMusic;

	// -- Game Constants -- //
	public static final float BULLET_CREATION_DELAY = 1f;
	public static final float GRAVITY = 0.9f;
	public static final int LOADING_DURATION = 1000;

	// --------------------------------------------------------------------------------------------------
	// -- RESOURCESMANAGER METHODS -- //
	// --------------------------------------------------------------------------------------------------

	private ResourcesManager() {
	}

	public static ResourcesManager getSharedInstance() {
		if (instance == null) {
			instance = new ResourcesManager();
		}
		return instance;
	}

	public static void prepareManager(Engine engine, GameActivity gameActivity,
			Camera camera, VertexBufferObjectManager vertexBufferObjectManager) {
		getSharedInstance().engine = engine;
		getSharedInstance().gameActivity = gameActivity;
		getSharedInstance().camera = camera;
		getSharedInstance().vertexBufferObjectManager = vertexBufferObjectManager;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		MusicFactory.setAssetBasePath("bgm/");
		SoundFactory.setAssetBasePath("mfx/");
	}

	// --------------------------------------------------------------------------------------------------
	// -- LOAD GRAPHICS METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	public static void loadLoadSceneResources() {
		FontManager fontManager = getSharedInstance().engine.getFontManager();
		TextureManager textureManager = getSharedInstance().engine.getTextureManager();
		AssetManager assetManager = getSharedInstance().gameActivity.getAssets();
		getSharedInstance().baseFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 50, true, Color.WHITE);
		getSharedInstance().baseFont.load();
		getSharedInstance().loadScreenText = new Text(0, 0, getSharedInstance().baseFont, getSharedInstance().gameActivity.getResources().getText(R.string.loading_scene_loading), getSharedInstance().vertexBufferObjectManager);
	}
	
	public static void unloadLoadSceneResources() {
		getSharedInstance().baseFont.unload();
		getSharedInstance().loadScreenText = null;
	}
	
	public static void loadMenuSceneResources() {
		getSharedInstance().menuFundoAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),(int) getSharedInstance().camera.getWidth(),(int) getSharedInstance().camera.getHeight(),BitmapTextureFormat.RGB_565,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuCloudAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),800,279,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuBird_01_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),1272,97,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuBird_02_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),744,58,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuBird_03_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),448,35,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuLetter_a_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),76,79,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuLetter_b_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),68,83,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuLetter_c_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),62,81,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuLetter_d_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),66,80,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuLetter_i_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),38,78,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuLetter_k_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),73,85,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuLetter_r_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),68,82,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuLetter_s_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),62,83,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuLetter_t_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),65,79,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().menuFundoRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuFundoAtlas,getSharedInstance().gameActivity, "menu/fundo.png", 0,0);
		getSharedInstance().menuCloudRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuCloudAtlas,getSharedInstance().gameActivity, "menu/cloud.png", 0,0);
		getSharedInstance().menuBird_01_Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(getSharedInstance().menuBird_01_Atlas, getSharedInstance().gameActivity, "menu/bird_01.png", 0, 0, 8, 1);
		getSharedInstance().menuBird_02_Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(getSharedInstance().menuBird_02_Atlas,getSharedInstance().gameActivity, "menu/bird_02.png", 0,0,8,1);
		getSharedInstance().menuBird_03_Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(getSharedInstance().menuBird_03_Atlas,getSharedInstance().gameActivity, "menu/bird_03.png", 0,0,8,1);
		getSharedInstance().menuLetter_a_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuLetter_a_Atlas,getSharedInstance().gameActivity, "menu/letter_a.png", 0,0);
		getSharedInstance().menuLetter_b_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuLetter_b_Atlas,getSharedInstance().gameActivity, "menu/letter_b.png", 0,0);
		getSharedInstance().menuLetter_c_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuLetter_c_Atlas,getSharedInstance().gameActivity, "menu/letter_c.png", 0,0);
		getSharedInstance().menuLetter_d_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuLetter_d_Atlas,getSharedInstance().gameActivity, "menu/letter_d.png", 0,0);
		getSharedInstance().menuLetter_i_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuLetter_i_Atlas,getSharedInstance().gameActivity, "menu/letter_i.png", 0,0);
		getSharedInstance().menuLetter_k_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuLetter_k_Atlas,getSharedInstance().gameActivity, "menu/letter_k.png", 0,0);
		getSharedInstance().menuLetter_r_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuLetter_r_Atlas,getSharedInstance().gameActivity, "menu/letter_r.png", 0,0);
		getSharedInstance().menuLetter_s_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuLetter_s_Atlas,getSharedInstance().gameActivity, "menu/letter_s.png", 0,0);
		getSharedInstance().menuLetter_t_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().menuLetter_t_Atlas,getSharedInstance().gameActivity, "menu/letter_t.png", 0,0);
		getSharedInstance().menuFundoAtlas.load();
		getSharedInstance().menuCloudAtlas.load();
		getSharedInstance().menuBird_01_Atlas.load();
		getSharedInstance().menuBird_02_Atlas.load();
		getSharedInstance().menuBird_03_Atlas.load();
		getSharedInstance().menuLetter_a_Atlas.load();
		getSharedInstance().menuLetter_b_Atlas.load();
		getSharedInstance().menuLetter_c_Atlas.load();
		getSharedInstance().menuLetter_d_Atlas.load();
		getSharedInstance().menuLetter_i_Atlas.load();
		getSharedInstance().menuLetter_k_Atlas.load();
		getSharedInstance().menuLetter_r_Atlas.load();
		getSharedInstance().menuLetter_s_Atlas.load();
		getSharedInstance().menuLetter_t_Atlas.load();
		
		FontManager fontManager = getSharedInstance().engine.getFontManager();
		TextureManager textureManager = getSharedInstance().engine.getTextureManager();
		AssetManager assetManager = getSharedInstance().gameActivity.getAssets();
		
		getSharedInstance().menuPlayFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 50, true, Color.WHITE);
		getSharedInstance().menuPlayFont.load();
		getSharedInstance().menuPlayText = new Text(0, 0,getSharedInstance().menuPlayFont, ResourcesManager.getSharedInstance().gameActivity.getResources().getText(R.string.menu_play), getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().menuExitText = new Text(0, 0,getSharedInstance().menuPlayFont, ResourcesManager.getSharedInstance().gameActivity.getResources().getText(R.string.menu_exit), getSharedInstance().vertexBufferObjectManager);
		
		getSharedInstance().menuScoreValFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 25, true, Color.BLACK);
		getSharedInstance().menuScoreValFont.load();
		String bestScore = getSharedInstance().gameActivity.loadScore();
		getSharedInstance().menuScoreValText = new Text(0, 0,getSharedInstance().menuScoreValFont, ResourcesManager.getSharedInstance().gameActivity.getResources().getText(R.string.menu_score)+": "+bestScore, getSharedInstance().vertexBufferObjectManager);
		
		try {
			getSharedInstance().menuClickSound = SoundFactory.createSoundFromAsset(getSharedInstance().engine.getSoundManager(),getSharedInstance().gameActivity,"click.ogg");
			getSharedInstance().menuMusic = MusicFactory.createMusicFromAsset(getSharedInstance().engine.getMusicManager(),getSharedInstance().gameActivity,"menu.ogg");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}finally{
			Debug.d(" - Load Menu Music Resources");
		}
		getSharedInstance().menuMusic.setVolume(1f);
		getSharedInstance().menuMusic.setLooping(true);
	}

	public static void unloadMenuSceneResources() {
		getSharedInstance().menuFundoRegion = null;
		getSharedInstance().menuCloudRegion = null;
		getSharedInstance().menuBird_01_Region = null;
		getSharedInstance().menuBird_02_Region = null;
		getSharedInstance().menuBird_03_Region = null;
		getSharedInstance().menuLetter_a_Region = null;
		getSharedInstance().menuLetter_b_Region = null;
		getSharedInstance().menuLetter_c_Region = null;
		getSharedInstance().menuLetter_d_Region = null;
		getSharedInstance().menuLetter_i_Region = null;
		getSharedInstance().menuLetter_k_Region = null;
		getSharedInstance().menuLetter_r_Region = null;
		getSharedInstance().menuLetter_s_Region = null;
		getSharedInstance().menuLetter_t_Region = null;
		getSharedInstance().menuFundoAtlas.unload();
		getSharedInstance().menuCloudAtlas.unload();
		getSharedInstance().menuBird_01_Atlas.unload();
		getSharedInstance().menuBird_02_Atlas.unload();
		getSharedInstance().menuBird_03_Atlas.unload();
		getSharedInstance().menuLetter_a_Atlas.unload();
		getSharedInstance().menuLetter_b_Atlas.unload();
		getSharedInstance().menuLetter_c_Atlas.unload();
		getSharedInstance().menuLetter_d_Atlas.unload();
		getSharedInstance().menuLetter_i_Atlas.unload();
		getSharedInstance().menuLetter_k_Atlas.unload();
		getSharedInstance().menuLetter_r_Atlas.unload();
		getSharedInstance().menuLetter_s_Atlas.unload();
		getSharedInstance().menuLetter_t_Atlas.unload();
		getSharedInstance().menuPlayFont.unload();
		getSharedInstance().menuScoreValFont.unload();
		getSharedInstance().menuMusic.release();
		getSharedInstance().menuClickSound.release();
		getSharedInstance().menuPlayText = null;
		getSharedInstance().menuExitText = null;
		getSharedInstance().menuScoreValText = null;
	}
	
	public static void loadGameSceneResources() {
		getSharedInstance().gameBackgroundAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),1000,(int) getSharedInstance().camera.getHeight(),BitmapTextureFormat.RGB_565,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().gameGroundAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),(int) getSharedInstance().camera.getWidth(),141,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().gameBulletHudPlateAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),400,42,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().gamePointsHudPlateAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),325,42,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().gamePauseButtonAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),61,61,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().gameResumeButtonAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),61,61,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().gameRestartButtonAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),61,61,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().gameExitButtonAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(),61,61,BitmapTextureFormat.RGBA_8888,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().bird_01_Atlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(), 656, 52,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().slingshot_frontAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(), 57, 96,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().slingshot_backAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(), 12, 42,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().bulletAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(), 35, 35,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().reserveBulletAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(), 30, 30,ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().explosionAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(), 1600,200, ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().tutorAtlas = new BitmapTextureAtlas(getSharedInstance().gameActivity.getTextureManager(), 505,273, ResourcesManager.TEXTURE_QUALITY);
		getSharedInstance().gameBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().gameBackgroundAtlas,getSharedInstance().gameActivity, "game/background.jpg", 0,0);
		getSharedInstance().gameGroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().gameGroundAtlas,getSharedInstance().gameActivity, "game/ground.png", 0,0);
		getSharedInstance().gameBulletHudPlateRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().gameBulletHudPlateAtlas,getSharedInstance().gameActivity, "game/bullet_hud_plate.png", 0,0);
		getSharedInstance().gamePointsHudPlateRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().gamePointsHudPlateAtlas,getSharedInstance().gameActivity, "game/points_hud_plate.png", 0,0);
		getSharedInstance().gamePauseButtonRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().gamePauseButtonAtlas,getSharedInstance().gameActivity, "game/pause_button.png", 0,0);
		getSharedInstance().gameResumeButtonRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().gameResumeButtonAtlas,getSharedInstance().gameActivity, "game/resume_button.png", 0,0);
		getSharedInstance().gameRestartButtonRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().gameRestartButtonAtlas,getSharedInstance().gameActivity, "game/restart_button.png", 0,0);
		getSharedInstance().gameExitButtonRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().gameExitButtonAtlas,getSharedInstance().gameActivity, "game/exit_button.png", 0,0);
		getSharedInstance().bird_01_Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(getSharedInstance().bird_01_Atlas,getSharedInstance().gameActivity, "game/bird_01.png", 0, 0, 8, 1);
		getSharedInstance().slingshot_frontRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().slingshot_frontAtlas,getSharedInstance().gameActivity, "game/slingshot_front.png", 0, 0);
		getSharedInstance().slingshot_backRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().slingshot_backAtlas,getSharedInstance().gameActivity, "game/slingshot_back.png", 0, 0);
		getSharedInstance().bulletRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().bulletAtlas,getSharedInstance().gameActivity, "game/stone.png", 0, 0);
		getSharedInstance().reserveBulletRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().reserveBulletAtlas,getSharedInstance().gameActivity, "game/reserve_stone.png", 0, 0);
		getSharedInstance().explosionRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(getSharedInstance().explosionAtlas,getSharedInstance().gameActivity, "game/explode.png", 0,0, 8, 1);
		getSharedInstance().tutorRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(getSharedInstance().tutorAtlas,getSharedInstance().gameActivity, "game/tutorial_img.png", 0,0);
		getSharedInstance().gameBackgroundAtlas.load();
		getSharedInstance().gameGroundAtlas.load();
		getSharedInstance().gameBulletHudPlateAtlas.load();
		getSharedInstance().gamePointsHudPlateAtlas.load();
		getSharedInstance().gamePauseButtonAtlas.load();
		getSharedInstance().gameResumeButtonAtlas.load();
		getSharedInstance().gameRestartButtonAtlas.load();
		getSharedInstance().gameExitButtonAtlas.load();
		getSharedInstance().bird_01_Atlas.load();
		getSharedInstance().slingshot_frontAtlas.load();
		getSharedInstance().slingshot_backAtlas.load();
		getSharedInstance().bulletAtlas.load();
		getSharedInstance().reserveBulletAtlas.load();
		getSharedInstance().explosionAtlas.load();
		getSharedInstance().tutorAtlas.load();
		
		// Text resources
		FontManager fontManager = getSharedInstance().engine.getFontManager();
		TextureManager textureManager = getSharedInstance().engine.getTextureManager();
		AssetManager assetManager = getSharedInstance().gameActivity.getAssets();
		getSharedInstance().gameFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 25, true, Color.WHITE);
		getSharedInstance().gameFont.load();
		getSharedInstance().gameValFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 35, true, Color.WHITE);
		getSharedInstance().gameValFont.load();
		getSharedInstance().gameMenuFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 35, true, Color.WHITE);
		getSharedInstance().gameMenuFont.load();
		getSharedInstance().gameOverFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 50, true, Color.WHITE);
		getSharedInstance().gameOverFont.load();
		getSharedInstance().gameStageCompleteFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 50, true, Color.WHITE);
		getSharedInstance().gameStageCompleteFont.load();
		getSharedInstance().gamePointsFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 20, true, Color.WHITE);
		getSharedInstance().gamePointsFont.load();
		getSharedInstance().gameTutorFont = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 30, true, Color.WHITE);
		getSharedInstance().gameTutorFont.load();
		getSharedInstance().gameScoreText = new Text(0, 0, getSharedInstance().gameFont, ResourcesManager.getSharedInstance().gameActivity.getText(R.string.game_score), getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().gameScoreText.setPosition(515,450);
		getSharedInstance().gameScoreText.setColor(0.20703125f, 0f, 0.45703125f);
		getSharedInstance().gameScoreValText = new Text(0, 0, getSharedInstance().gameValFont, "00000", getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().gameScoreValText.setPosition(610,450);
		getSharedInstance().gameScoreValText.setColor(0.40234375f, 0.11328125f, 0.74609375f);
		getSharedInstance().gameLevelText = new Text(0, 0, getSharedInstance().gameFont, ResourcesManager.getSharedInstance().gameActivity.getText(R.string.game_level), getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().gameLevelText.setPosition(710,450);
		getSharedInstance().gameLevelText.setColor(0.20703125f, 0f, 0.45703125f);
		getSharedInstance().gameLevelValText = new Text(0, 0, getSharedInstance().gameValFont, "01", getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().gameLevelValText.setPosition(765,450);
		getSharedInstance().gameLevelValText.setColor(0.40234375f, 0.11328125f, 0.74609375f);
		getSharedInstance().gameMenuPlayText = new Text(getSharedInstance().camera.getWidth()/2+50, (getSharedInstance().camera.getHeight()/2)+100, getSharedInstance().gameMenuFont, ResourcesManager.getSharedInstance().gameActivity.getText(R.string.game_pause_play), getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().gameTutorText = new Text(getSharedInstance().camera.getWidth()/2+50, (getSharedInstance().camera.getHeight()/2)+100, getSharedInstance().gameTutorFont, ResourcesManager.getSharedInstance().gameActivity.getText(R.string.game_tutor), getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().gameMenuResetText = new Text(getSharedInstance().camera.getWidth()/2+50, getSharedInstance().camera.getHeight()/2,getSharedInstance().gameTutorFont, ResourcesManager.getSharedInstance().gameActivity.getText(R.string.game_pause_restart), getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().gameMenuQuitText= new Text(getSharedInstance().camera.getWidth()/2+50, (getSharedInstance().camera.getHeight()/2)-100,getSharedInstance().gameMenuFont, ResourcesManager.getSharedInstance().gameActivity.getText(R.string.game_pause_quit), getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().gameOverText= new Text(getSharedInstance().camera.getWidth()/2, (getSharedInstance().camera.getHeight()/2)+100,getSharedInstance().gameOverFont, ResourcesManager.getSharedInstance().gameActivity.getText(R.string.game_pause_game_over), getSharedInstance().vertexBufferObjectManager);
		getSharedInstance().gameStageCompleteText= new Text(getSharedInstance().camera.getWidth()/2, (getSharedInstance().camera.getHeight()/2)+100,getSharedInstance().gameStageCompleteFont, ResourcesManager.getSharedInstance().gameActivity.getText(R.string.game_pause_stage_complete), getSharedInstance().vertexBufferObjectManager);
		
		// load audio resources
		try {
			getSharedInstance().gameClickSound = SoundFactory.createSoundFromAsset(getSharedInstance().engine.getSoundManager(),getSharedInstance().gameActivity,"click.ogg");
			getSharedInstance().explodeSound = SoundFactory.createSoundFromAsset(getSharedInstance().engine.getSoundManager(),getSharedInstance().gameActivity,"pop.ogg");
			getSharedInstance().shootSound = SoundFactory.createSoundFromAsset(getSharedInstance().engine.getSoundManager(),getSharedInstance().gameActivity,"shoot.ogg");
			getSharedInstance().rubberSound = SoundFactory.createSoundFromAsset(getSharedInstance().engine.getSoundManager(),getSharedInstance().gameActivity,"rubber.ogg");
			getSharedInstance().gameMusic = MusicFactory.createMusicFromAsset(getSharedInstance().engine.getMusicManager(),getSharedInstance().gameActivity,"smrforst.mid");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			Debug.d(" - Load GameScene Sound Resources");
		}
		getSharedInstance().explodeSound.setVolume(1f);
		getSharedInstance().shootSound.setVolume(1f);
		getSharedInstance().rubberSound.setVolume(1f);
		getSharedInstance().gameMusic.setVolume(0.5f);
		getSharedInstance().gameMusic.setLooping(true);
	}
	public static void unloadGameSceneResources() {
		getSharedInstance().gameBackgroundRegion = null;
		getSharedInstance().gameGroundRegion = null;
		getSharedInstance().gameBulletHudPlateRegion = null;
		getSharedInstance().gamePauseButtonRegion = null;
		getSharedInstance().gameResumeButtonRegion = null;
		getSharedInstance().gameRestartButtonRegion = null;
		getSharedInstance().gameExitButtonRegion = null;
		getSharedInstance().bird_01_Region = null;
		getSharedInstance().slingshot_frontRegion = null;
		getSharedInstance().slingshot_backRegion = null;
		getSharedInstance().bulletRegion = null;
		getSharedInstance().reserveBulletRegion = null;
		getSharedInstance().explosionRegion = null;
		getSharedInstance().gameScoreText = null;
		getSharedInstance().gameScoreValText = null;
		getSharedInstance().gameMenuPlayText = null;
		getSharedInstance().gameMenuResetText = null;
		getSharedInstance().gameMenuQuitText = null;
		getSharedInstance().gameLevelText = null;
		getSharedInstance().gameLevelValText = null;
		getSharedInstance().gameOverText = null;
		getSharedInstance().gameTutorText = null;
		getSharedInstance().gameStageCompleteText = null;
		getSharedInstance().gameBackgroundAtlas.unload();
		getSharedInstance().gameGroundAtlas.unload();
		getSharedInstance().gameBulletHudPlateAtlas.unload();
		getSharedInstance().gamePointsHudPlateAtlas.unload();
		getSharedInstance().gamePauseButtonAtlas.unload();
		getSharedInstance().gameResumeButtonAtlas.unload();
		getSharedInstance().gameRestartButtonAtlas.unload();
		getSharedInstance().gameExitButtonAtlas.unload();
		getSharedInstance().bird_01_Atlas.unload();
		getSharedInstance().slingshot_frontAtlas.unload();
		getSharedInstance().slingshot_backAtlas.unload();
		getSharedInstance().bulletAtlas.unload();
		getSharedInstance().reserveBulletAtlas.unload();
		getSharedInstance().explosionAtlas.unload();
		getSharedInstance().explodeSound.release();
		getSharedInstance().gameClickSound.release();
		getSharedInstance().shootSound.release();
		getSharedInstance().rubberSound.release();
		getSharedInstance().gameMusic.release();
		getSharedInstance().gameFont.unload();
		getSharedInstance().gameValFont.unload();
		getSharedInstance().gameMenuFont.unload();
		getSharedInstance().gameOverFont.unload();
		getSharedInstance().gameTutorFont.unload();
		getSharedInstance().gameStageCompleteFont.unload();
		getSharedInstance().gamePointsFont.unload();
	}
	
}
