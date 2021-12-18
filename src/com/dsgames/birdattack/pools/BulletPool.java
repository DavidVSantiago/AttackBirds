package com.dsgames.birdattack.pools;

import org.andengine.util.adt.pool.GenericPool;

import com.dsgames.birdattack.sprites.Bullet;

public class BulletPool extends GenericPool<Bullet> {

	private static BulletPool instance;

	public static BulletPool sharedBulletPool() {
		if (instance == null) {
			instance = new BulletPool();
		}
		return instance;
	}

	private BulletPool() {
		super();
	}

	@Override
	protected Bullet onAllocatePoolItem() {
		return new Bullet();
	}

	@Override
	protected void onHandleObtainItem(Bullet pItem) {
		//pItem.reset();
		pItem.setIgnoreUpdate(false);
		pItem.setVisible(true);
	}

	@Override
	protected void onHandleRecycleItem(final Bullet pItem) {
		pItem.setIgnoreUpdate(true);
		pItem.setVisible(false);
		//pItem.detachSelf();
	}
	
	public void clean(){
		instance = new BulletPool();
	}

}
