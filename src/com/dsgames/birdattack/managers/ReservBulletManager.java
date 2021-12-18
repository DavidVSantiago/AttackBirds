package com.dsgames.birdattack.managers;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseExponentialOut;

public class ReservBulletManager {
	
	public int NUMBER_OF_BULLETS = 12;
	public int reservBulletPointer = -1;
	public Sprite[] reserveBulletList;
	
	public ReservBulletManager() {
		reserveBulletList = new Sprite[NUMBER_OF_BULLETS];
	}

	// -------------------------------------------- 

	public void addReserveBullet(){
		if(!checkStockFull()){
			reservBulletPointer++;
			Sprite bullet = reserveBulletList[reservBulletPointer];
			bullet.clearEntityModifiers();
			bullet.registerEntityModifier(new ScaleModifier(0.5f, 0, 1f, EaseExponentialOut.getInstance()));
			bullet.setVisible(true);
		}
	}
	
	public void subtractReserveBullet(){
		final Sprite bullet = reserveBulletList[reservBulletPointer];
		bullet.clearEntityModifiers();
		bullet.registerEntityModifier(new ScaleModifier(0.5f, 1, 0f, new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				bullet.setVisible(false);
			}
		}, EaseExponentialOut.getInstance()));
		reservBulletPointer--;
	}
	
	public int bulletCount(){
		return reservBulletPointer + 1;
	}
	
	private boolean checkStockFull(){
		if(reservBulletPointer>=NUMBER_OF_BULLETS-1){
			return true;
		}
		return false;
	}
}
