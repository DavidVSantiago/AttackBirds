package com.dsgames.birdattack.advertising;

public interface AdvertisingObserver {
	
	public void advertisingIsLoaded();
	public void advertisingIsFailed();
	public void advertisingIsClosed();
	public void advertisingIsOpened();
	public void advertisingIsLeftApp();
}
