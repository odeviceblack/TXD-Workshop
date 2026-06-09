package com;

import android.app.Application;
import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrashApplication extends Application {

	private static boolean handled = false;
	
	@Override
	public void onCreate() {
		super.onCreate();

		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			if (!handled) {
				handled = true;
				writeCrash(throwable);
			}
			killNow();
		});
	}

	private void writeCrash(Throwable t) {
		try {
			File dir = getExternalFilesDir("crash");
			if (dir != null && !dir.exists()) dir.mkdirs();

			String timestamp = new SimpleDateFormat(
					"yyyyMMdd_HHmmss", Locale.US
			).format(new Date());

			File out = new File(dir, "crash_" + timestamp + ".txt");

			try (FileOutputStream fos = new FileOutputStream(out);
				 PrintWriter pw = new PrintWriter(fos)) {
				pw.println("Thread: " + Thread.currentThread().getName());
				t.printStackTrace(pw);
			}

		} catch (Exception ignored) {}
	}

	private void killNow() {
		// fecha imediatamente sem recriar activity
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}