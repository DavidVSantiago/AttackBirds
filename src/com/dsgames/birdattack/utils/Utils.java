package com.dsgames.birdattack.utils;

import java.util.Random;

public class Utils {
	
	private static Utils instance;

	private Utils() {}

	private static Utils getInstance() {
		if (instance == null) {
			instance = new Utils();
		}
		return instance;
	}

	// --------------------------------------------------------------------------------------------------
	// -- STATIC UTIL METHODS -- //
	// --------------------------------------------------------------------------------------------------

	public static int getInt() {
		Random random = new Random();
		return random.nextInt();
	}

	public static int getInt(int limit){
		Random random = new Random();
		return random.nextInt(limit);
	}

	public static float getFloat() {
		Random random = new Random();
		return random.nextFloat();
	}

}
