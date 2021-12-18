package com.dsgames.birdattack.sprites.models;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import com.dsgames.birdattack.managers.ResourcesManager;

public abstract class BaseSprite extends Sprite {

	public Rectangle collisionBox;

	public BaseSprite(ITextureRegion textureRegion) {
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
