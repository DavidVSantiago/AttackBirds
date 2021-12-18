package com.dsgames.birdattack.managers;

import org.andengine.util.debug.Debug;

import com.dsgames.birdattack.scenes.LoadScene;
import com.dsgames.birdattack.scenes.models.BaseScene;


public class SceneManager {

	private static SceneManager instance;
	private BaseScene currentScene;
	
	private SceneManager(){}
	
	// ------------------------------------------------------------------- //
	// - PUBLIC METHODS - //
	// ------------------------------------------------------------------- //
	
	public static SceneManager getInstance(){
		if(instance == null){
			instance = new SceneManager();
		}
		return instance;
	}
	
	public BaseScene getCurrentScene(){
		return currentScene;
	}
	
	public void setCurrentScene(BaseScene currentScene){
		this.currentScene = currentScene;
	}
	
	public void showScene(final Class<? extends BaseScene> sceneClazz) {

		if(sceneClazz == LoadScene.class){
			throw new IllegalArgumentException("Não pode passar LoadingScreen");
		}
		final BaseScene oldScene = getCurrentScene();
			
		final LoadScene loadScene = new LoadScene();
		setCurrentScene(loadScene);
		ResourcesManager.getSharedInstance().engine.setScene(loadScene);
		new Thread(new Runnable() {
			@Override
			public void run() {
				long timeStamp = System.currentTimeMillis();
				
				// libera recursos da antiga (cena B) 
				if(oldScene != null){
					oldScene.unmountScene();
				}
				// carrega os recursos da nova cena
				BaseScene scene;
				try {
					scene = sceneClazz.newInstance();
				} catch (InstantiationException e) {
					String error = "Erro durante a mudança de cena";
					Debug.e(error);
					throw new RuntimeException(error,e);
				} catch (IllegalAccessException e) {
					String error = "Erro durante a mudança de cena";
					Debug.e(error);
					throw new RuntimeException(error,e);
				}
				
				long elapsed = System.currentTimeMillis() - timeStamp;
				if(elapsed<ResourcesManager.LOADING_DURATION){
					try{
		        		Thread.sleep(ResourcesManager.LOADING_DURATION - elapsed); // espera o tempo da splash 
		        	}catch(InterruptedException exception){
		        		Debug.w("Erro on Loading Duration thrad");
		        	}
				}
				loadScene.unmountScene();
				
				setCurrentScene(scene);
				ResourcesManager.getSharedInstance().engine.setScene(scene);
			}
		}).start();
	}
	
	
}
