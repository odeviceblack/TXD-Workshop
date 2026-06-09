package com.gtasatutoymas.txdw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class File_Browser extends Activity implements AdapterView.OnItemClickListener {
    File current;
    File root = new File(new StringBuffer().append(Environment.getExternalStorageDirectory()).append("/").toString());
    ListView tv;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.file_browser);
        String[] list = this.root.list();
        this.current = this.root;
        ListLines listLines = new ListLines(this, getApplicationContext(), list, this.root);
        updatetitle();
        this.tv = (ListView) findViewById(R.id.file_browserListView);
        this.tv.setAdapter((android.widget.ListAdapter) listLines);
        this.tv.setOnItemClickListener(this);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        browser(new File(new StringBuffer().append(new StringBuffer().append(this.current).append("/").toString()).append((String) adapterView.getItemAtPosition(i)).toString()).getAbsolutePath());
    }

    public void updatetitle() {
        setTitle(new StringBuffer().append(new StringBuffer().append(getString(R.string.lib_name)).append(" : ").toString()).append(this.current.getName().toString()).toString());
    }

    public void browser(String str) {
        File file = new File(str);
        if (!file.isDirectory()) {
            open(file);
            return;
        }
        String[] list = file.list();
        this.current = file;
        updatetitle();
        this.tv.setAdapter((android.widget.ListAdapter) new ListLines(this, getApplicationContext(), list, file));
    }

    public void open(File file) {
        if (!file.getAbsolutePath().endsWith(".txd")) {
            Toast.makeText(getApplicationContext(), new StringBuffer().append(new StringBuffer().append(getResources().getString(R.string.notex)).append(" (txd) : ").toString()).append(file.getAbsolutePath()).toString(), 1).show();
            return;
        }
        Intent intent = new Intent();
        intent.setData(Uri.parse(new StringBuffer().append("file://").append(file.getAbsolutePath()).toString()));
        setResult(-1, intent);
        finish();
    }

    public class ListLines extends ArrayAdapter {
        Context c;
        File dirs;
        private final File_Browser this$0;

        public ListLines(File_Browser file_Browser, Context context, String[] strArr, File file) {
            super(context, R.layout.list_item, strArr);
            this.this$0 = file_Browser;
            this.c = context;
            this.dirs = file;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.list_item, (ViewGroup) null);
            String str = (String) getItem(i);
            TextView textView = (TextView) viewInflate.findViewById(R.id.listitemTextView);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.listitemImageView1);
            File file = new File(new StringBuffer().append(new StringBuffer().append(this.dirs).append("/").toString()).append(str).toString());
            if (file.isDirectory()) {
                imageView.setImageResource(R.drawable.fsm_folder);
            } else if (str.endsWith(".img")) {
                imageView.setImageResource(R.drawable.img_img);
            } else if (str.endsWith(".fxp")) {
                imageView.setImageResource(R.drawable.img_fxp);
            } else if (str.endsWith(".txd")) {
                imageView.setImageResource(R.drawable.img_txd);
            } else if (str.endsWith(".col")) {
                imageView.setImageResource(R.drawable.img_col);
            } else if (str.endsWith(".gxt")) {
                imageView.setImageResource(R.drawable.img_gxt);
            } else if (str.endsWith(".ipl")) {
                imageView.setImageResource(R.drawable.img_ipl);
            } else if (str.endsWith(".ide")) {
                imageView.setImageResource(R.drawable.img_ide);
            } else if (str.endsWith(".dff")) {
                imageView.setImageResource(R.drawable.img_dff);
            } else if (str.endsWith(".ifp")) {
                imageView.setImageResource(R.drawable.img_ifp);
            } else if (str.endsWith("png") || str.endsWith("jpg")) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                imageView.setImageResource(R.drawable.fsm_unknown);
            }
            textView.setText(str);
            return viewInflate;
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        try {
            browser(this.current.getParent());
        } catch (Exception e) {
            finish();
        }
        return true;
    }
}
