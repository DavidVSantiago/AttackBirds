package com.dsgames.birdattack.sprites;

import org.andengine.util.adt.color.Color;

import com.dsgames.birdattack.managers.ResourcesManager;
import com.dsgames.birdattack.sprites.models.BaseAnimatedSprite;

public class Bird extends BaseAnimatedSprite {

	private float vel;
	private boolean ENABLED;

	public Bird() {
		super(ResourcesManager.getSharedInstance().bird_01_Region);
		collisionBox.setSize(50, 38);
		collisionBox.setPosition(getWidth()/2-5, getHeight()/2);
		collisionBox.setColor(Color.BLUE);
		collisionBox.setAlpha(0.5f);
		collisionBox.setVisible(false);
	}

	// --------------------------------------------------------------------------------------------------
	// -- PUBLIC METHODS -- //
	// --------------------------------------------------------------------------------------------------

	public void setVelocity(float vel){
		this.vel = vel;
	}
	
	public void move(float pSecondsElapsed){
		setX(getX() - (vel*pSecondsElapsed));
	}
	
	public void enable(){
		setVisible(true);
		setIgnoreUpdate(false);
		ENABLED=true;
		animate(50,true);
	}
	
	public void disable(){
		setVisible(false);
		setIgnoreUpdate(true);
		ENABLED=false;
		stopAnimation();
	}
	
	public boolean isEnable(){
		return ENABLED;
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- INTERFACES -- //
	// --------------------------------------------------------------------------------------------------
}
