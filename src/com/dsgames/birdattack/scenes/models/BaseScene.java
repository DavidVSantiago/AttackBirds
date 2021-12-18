package com.dsgames.birdattack.scenes.models;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import com.dsgames.birdattack.managers.ResourcesManager;

public abstract class BaseScene extends Scene implements IOnSceneTouchListener {

	protected int cameraWidth;
	protected int cameraHeight;
	protected VertexBufferObjectManager vbom;

	public BaseScene() {
		prepareResourcesManager();
	}

	public BaseScene(Color color) {
		setBackground(new Background(color));
		prepareResourcesManager();
	}

	public BaseScene(float red, float green, float blue) {
		setBackground(new Background(red, green, blue));
		prepareResourcesManager();
	}

	// --------------------------------------------------------------------------------------------------
	// -- ABSTRACT METHODS -- //
	// --------------------------------------------------------------------------------------------------

	public abstract void mountScene();

	public abstract void unmountScene();

	public abstract void gameLoop(float pSecondsElapsed);

	public abstract boolean toutchSceneEvent(Scene pScene,
			TouchEvent pSceneTouchEvent);

	// --------------------------------------------------------------------------------------------------
	// -- SUPER METHODS -- //
	// --------------------------------------------------------------------------------------------------

  /*@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		gameLoop(pSecondsElapsed);
		super.onManagedUpdate(pSecondsElapsed);
	}*/
	
	// --------------------------------------------------------------------------------------------------
	// -- IOnSceneTouchListener SUPER METHODS -- //
	// --------------------------------------------------------------------------------------------------

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return toutchSceneEvent(pScene, pSceneTouchEvent);
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- PRIVATE METHODS -- //
	// --------------------------------------------------------------------------------------------------

	private void prepareResourcesManager() {
		cameraWidth = (int) ResourcesManager.getSharedInstance().camera
				.getWidth();
		cameraHeight = (int) ResourcesManager.getSharedInstance().camera
				.getHeight();
		vbom = ResourcesManager.getSharedInstance().vertexBufferObjectManager;
		setOnSceneTouchListener(this);
	}
}
