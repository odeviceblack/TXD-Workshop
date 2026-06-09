package com.gtasatutoymas.txdw;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

/* JADX INFO: loaded from: classes.dex */
public class HelpActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.about);
        WebView webView = (WebView) findViewById(R.id.aboutWebView);
        String string = getIntent().getExtras().getString("result");
        if (string.equals("h")) {
            webView.loadUrl(new StringBuffer().append("file:///android_asset/").append(getResources().getString(R.string.fileh)).toString());
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.traslate), 1).show();
        } else if (string.equals("a")) {
            webView.loadUrl(new StringBuffer().append("file:///android_asset/").append(getResources().getString(R.string.filea)).toString());
            setTitle(getResources().getString(R.string.about));
        }
    }
}
