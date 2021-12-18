package com.dsgames.birdattack.pools;

import org.andengine.util.adt.pool.GenericPool;

import com.dsgames.birdattack.sprites.Explosion;

public class ExplosionPool extends GenericPool<Explosion> {

	private static ExplosionPool instance;

	public static ExplosionPool sharedExplosionPool() {
		if (instance == null) {
			instance = new ExplosionPool();
		}
		return instance;
	}

	private ExplosionPool() {
		super();
	}

	@Override
	protected Explosion onAllocatePoolItem() {
		return new Explosion();
	}

	@Override
	protected void onHandleObtainItem(Explosion pItem) {
		//pItem.reset();
		pItem.setVisible(true);
	}

	@Override
	protected void onHandleRecycleItem(final Explosion pItem) {
		//pItem.setIgnoreUpdate(true);
		pItem.setVisible(false);
		pItem.detachSelf();
	}
	
	public void clean(){
		instance = new ExplosionPool();
	}
}
