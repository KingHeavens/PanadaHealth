package com.jws.pandahealth.component.askdoctor.view.photo.util;


import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class Bimp {

	private static Bimp instance = null;
	public List<Integer> max = null;
	public List<ArrayList<ImageItem>> selectBitmap = null;
	public List<ArrayList<ImageItem>> tempSelectBitmap = null;

	public static synchronized Bimp getInstance() {
		if (instance == null)
			instance = new Bimp();
		return instance;
	}

	private Bimp() {
		max = new ArrayList<Integer>();
		selectBitmap = new ArrayList<ArrayList<ImageItem>>(); //
		tempSelectBitmap = new ArrayList<ArrayList<ImageItem>>(); //
	}

	public void close() {
		max.clear();
		selectBitmap.clear();
		tempSelectBitmap.clear();
		instance = null;
	}
	
}
