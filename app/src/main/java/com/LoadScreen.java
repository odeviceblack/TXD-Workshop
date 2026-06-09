package com;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.gtasatutoymas.txdw.MainActivity;
import com.gtasatutoymas.txdw.R;

public class LoadScreen extends Activity {

	private static final int REQUEST_STORAGE_PERMISSIONS = 300;
	private static final int REQUEST_MANAGE_EXTERNAL_STORAGE = 400;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadingscreen);

		checkPermissionsAndContinue();
	}

	// ============================================================
	// Permission Flow
	// ============================================================

	private void checkPermissionsAndContinue() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			// Android 11+ → precisa do MANAGE_EXTERNAL_STORAGE
			if (!android.os.Environment.isExternalStorageManager()) {
				requestManageAllFilesAccess();
				return;
			}
		} else {
			// Android 8–10 → WRITE/READ
			if (!hasLegacyPermissions()) {
				requestLegacyPermissions();
				return;
			}
		}

		launchGame();
	}

	private boolean hasLegacyPermissions() {
		return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
				android.content.pm.PackageManager.PERMISSION_GRANTED &&
			   checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
				android.content.pm.PackageManager.PERMISSION_GRANTED;
	}

	private void requestLegacyPermissions() {
		requestPermissions(
				new String[]{
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.READ_EXTERNAL_STORAGE
				},
				REQUEST_STORAGE_PERMISSIONS
		);
	}

	private void requestManageAllFilesAccess() {
		Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivityForResult(intent, REQUEST_MANAGE_EXTERNAL_STORAGE);
	}

	// ============================================================
	// Callbacks
	// ============================================================

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		checkPermissionsAndContinue();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_MANAGE_EXTERNAL_STORAGE) {
			checkPermissionsAndContinue();
		}
	}

	// ============================================================
	// Launch
	// ============================================================

	private void launchGame() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}
}