package com.gtasatutoymas.txdw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
public class ListOptions extends ArrayAdapter {
    Context c;

    public ListOptions(Context context, String[] strArr) {
        super(context, R.layout.list_item, strArr);
        this.c = context;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.list_item, (ViewGroup) null);
        String str = (String) getItem(i);
        TextView textView = (TextView) viewInflate.findViewById(R.id.listitemTextView);
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.listitemImageView1);
        textView.setText(str);
        if (!str.contains(this.c.getResources().getString(R.string.rename)) && str.contains(this.c.getResources().getString(R.string.delete))) {
            imageView.setImageResource(R.drawable.img_delete);
        } else {
            imageView.setImageResource(R.drawable.img_edit);
        }
        return viewInflate;
    }
}
