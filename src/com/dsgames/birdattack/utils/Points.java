package com.dsgames.birdattack.utils;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.util.modifier.IModifier;

import com.dsgames.birdattack.managers.ResourcesManager;

public class Points extends Text{
	
	public Points(float posX, float posY, Font font, String points) {
		super(posX, posY, font, points, ResourcesManager.getSharedInstance().vertexBufferObjectManager);
		registerEntityModifier(new MoveYModifier(1, getY(), getY()+10, new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				setVisible(false);
				//clearEntityModifiers();
				//detachSelf();
			}
		}));
	}
}
