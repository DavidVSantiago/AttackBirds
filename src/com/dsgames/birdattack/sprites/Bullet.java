package com.dsgames.birdattack.sprites;

import com.dsgames.birdattack.managers.ResourcesManager;
import com.dsgames.birdattack.sprites.models.BaseSprite;

public class Bullet extends BaseSprite {

	public double velX;
	public double velY;
	public final double accel = 10;
	public int rotationVel;
	private boolean IS_ATTACHED;

	private static final long FD = 40; // frame duration

	public Bullet() {
		super(ResourcesManager.getSharedInstance().bulletRegion);
		collisionBox.setSize(28, 28);
		collisionBox.setVisible(false);
		IS_ATTACHED = false;
	}

	// --------------------------------------------------------------------------------------------------
	// //
	// -- PUBLIC METHODS -- //
	// --------------------------------------------------------------------------------------------------
	// //

	public void move(float pSecondsElapsed) {
		velY = velY - (((ResourcesManager.GRAVITY / 10))); // calculate gravity
															// decrease
		float newY = getY() + (float) velY;
		float newX = getX() + (float) velX;
		setPosition(newX, newY);
		setRotation(getRotation() + rotationVel + 2);
	}

	public boolean isAttached() {
		return IS_ATTACHED;
	}

	public void setAttached(boolean flag) {
		IS_ATTACHED = flag;
	}
}
