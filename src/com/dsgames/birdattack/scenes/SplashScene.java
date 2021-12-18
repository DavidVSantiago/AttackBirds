package com.dsgames.birdattack.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;

import android.content.res.AssetManager;
import android.graphics.Color;

import com.dsgames.birdattack.advertising.AdvertisingObserver;
import com.dsgames.birdattack.managers.ResourcesManager;
import com.dsgames.birdattack.managers.SceneManager;
import com.dsgames.birdattack.scenes.models.BaseScene;

public class SplashScene extends BaseScene{
	
	private Font font;
	private Text text;
	
	public SplashScene() {
		setColor(Color.BLACK);
		mountScene();
		registerEntityModifier(new DelayModifier(2f,new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				ResourcesManager.getSharedInstance().gameActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						SceneManager.getInstance().showScene(MenuScene.class);
					}
				});
			}
		}));
	}

	// -------------------------------------------------------------------------------------------------- //
	// -- SUPER METHODS -- //
	// -------------------------------------------------------------------------------------------------- //
	
	@Override
	public void mountScene() {
		FontManager fontManager = ResourcesManager.getSharedInstance().engine.getFontManager();
		TextureManager textureManager = ResourcesManager.getSharedInstance().engine.getTextureManager();
		AssetManager assetManager = ResourcesManager.getSharedInstance().gameActivity.getAssets();
		font = FontFactory.createFromAsset(fontManager, textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA, assetManager, "fonts/luckiestguy.ttf", 100, true, Color.WHITE);
		font.load();
		text = new Text(0, 0, font, "DS Games", ResourcesManager.getSharedInstance().vertexBufferObjectManager);
		text.setPosition(ResourcesManager.getSharedInstance().camera.getWidth()/2,
				ResourcesManager.getSharedInstance().camera.getHeight()/2);
		attachChild(text);
	}

	@Override
	public void unmountScene() {
		font.unload();
		text.clearEntityModifiers();
		text.clearUpdateHandlers();
		text.detachSelf();
		clearEntityModifiers();
		clearUpdateHandlers();
	}

	@Override
	public void gameLoop(float pSecondsElapsed) {}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		gameLoop(pSecondsElapsed);
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	@Override
	public boolean toutchSceneEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return false;
	}
}
