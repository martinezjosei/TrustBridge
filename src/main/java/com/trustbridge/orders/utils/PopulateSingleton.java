package com.trustbridge.orders.utils;

public class PopulateSingleton {

	private static PopulateSingleton instance;
	boolean isDuplicateFlag = false;

	private PopulateSingleton() {

	}

	public static PopulateSingleton getInstance() {
		if (instance == null) {
			synchronized (PopulateSingleton.class) {
				if (instance == null) {
					instance = new PopulateSingleton();
				}
			}
		}

		return instance;
	}
	
	
	public void setDuplicateFlag(boolean isDuplicateFlag) {
		this.isDuplicateFlag = isDuplicateFlag;
	}
	
	
	public boolean isDuplicateFlag() {
		return isDuplicateFlag;
	}
}