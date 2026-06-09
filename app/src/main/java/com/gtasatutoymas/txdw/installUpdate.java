package com.gtasatutoymas.txdw;

import android.app.Activity;
import android.os.Environment;

public class installUpdate {
	public static String pathDownload = Environment.getExternalStorageDirectory() + "/Android/data/com.gtasatutoymas.txdw/";

	Activity c;
	int v = 1;

	public installUpdate(Activity activity) {
		c = activity;
	}

	public void start() {
		// Sistema de atualização removido.
	}

	private void checkDataAndDownload(Activity activity) {
		// Sistema de atualização removido.
	}
}