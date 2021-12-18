package com.dsgames.birdattack.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import com.dsgames.birdattack.managers.ResourcesManager;
import com.dsgames.birdattack.scenes.models.BaseScene;

public class LoadScene extends BaseScene {

	Text text;
	
	public LoadScene() {
		super();
		mountScene();
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- SUPER METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	@Override
	public void mountScene() {
		ResourcesManager.loadLoadSceneResources();
		setColor(Color.BLACK);
		text = ResourcesManager.getSharedInstance().loadScreenText;
		text.setPosition(ResourcesManager.getSharedInstance().camera.getWidth()/2,ResourcesManager.getSharedInstance().camera.getHeight()/2);
		attachChild(text);
	}

	@Override
	public void unmountScene() {
		text = null;
		ResourcesManager.unloadLoadSceneResources();
	}

	@Override
	public void gameLoop(float pSecondsElapsed) {
	}

	@Override
	public boolean toutchSceneEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return false;
	}

}
