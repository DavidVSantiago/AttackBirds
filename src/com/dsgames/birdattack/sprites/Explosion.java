package com.dsgames.birdattack.sprites;

import com.dsgames.birdattack.managers.ResourcesManager;
import com.dsgames.birdattack.sprites.models.BaseAnimatedSprite;

public class Explosion extends BaseAnimatedSprite {

	private static final long FD = 40; // frame duration

	public Explosion() {
		super(ResourcesManager.getSharedInstance().explosionRegion);
		collisionBox.setVisible(false);
	}

	// --------------------------------------------------------------------------------------------------
	// //
	// -- PUBLIC METHODS -- //
	// --------------------------------------------------------------------------------------------------
	// //
}
