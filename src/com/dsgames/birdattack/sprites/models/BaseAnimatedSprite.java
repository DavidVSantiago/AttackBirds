package com.dsgames.birdattack.sprites.models;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.adt.color.Color;

import com.dsgames.birdattack.managers.ResourcesManager;

public abstract class BaseAnimatedSprite extends AnimatedSprite {

	public Rectangle collisionBox;

	public BaseAnimatedSprite(ITiledTextureRegion textureRegion) {
		super(0, 0, textureRegion,
				ResourcesManager.getSharedInstance().vertexBufferObjectManager);
		collisionBox = new Rectangle(0, 0, getWidth(), getHeight(),
				ResourcesManager.getSharedInstance().vertexBufferObjectManager);
		collisionBox.setPosition(getWidth() / 2, getHeight() / 2);
		collisionBox.setColor(Color.RED);
		collisionBox.setAlpha(0.5f);
		attachChild(collisionBox);
		setCullingEnabled(true);
	}

	// --------------------------------------------------------------------------------------------------
	// -- PUBLIC METHODS -- //
	// --------------------------------------------------------------------------------------------------

	public void clear() {
		clearEntityModifiers();
		clearUpdateHandlers();
	}
	
	// --------------------------------------------------------------------------------------------------
	// -- SUPER METHODS -- //
	// --------------------------------------------------------------------------------------------------
	
	@Override
	public boolean collidesWith(IEntity pOtherEntity) {
		return collisionBox
				.collidesWith(((BaseSprite) pOtherEntity).collisionBox);
	}
}
