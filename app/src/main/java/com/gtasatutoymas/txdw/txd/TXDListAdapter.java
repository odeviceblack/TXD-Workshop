package com.gtasatutoymas.txdw.txd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gtasatutoymas.txdw.R;

/* JADX INFO: loaded from: classes.dex */
public class TXDListAdapter extends ArrayAdapter {
    Context c;
    TXDWorkshop txd;

    public TXDListAdapter(Context context, String[] strArr, String str) {
        super(context, R.layout.txd_adapter, strArr);
        this.txd = new TXDWorkshop(str, context);
        this.c = context;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.txd_adapter, (ViewGroup) null);
        String str = (String) getItem(i);
        ((ImageView) viewInflate.findViewById(R.id.ivTexture)).setImageBitmap(this.txd.getImageDecompress(i));
        ((TextView) viewInflate.findViewById(R.id.nameTexture)).setText(str);
        ((TextView) viewInflate.findViewById(R.id.pixels)).setText(this.txd.getDimens(i));
        ((TextView) viewInflate.findViewById(R.id.compTex)).setText(this.txd.getCompression(i));
        TextView textView = (TextView) viewInflate.findViewById(R.id.stateAlpha);
        if (this.txd.getAlphaIV(i) != 0) {
            textView.setTextColor(-65536);
            textView.setText(this.c.getResources().getString(R.string.a));
        } else {
            textView.setText(this.c.getResources().getString(R.string.wa));
        }
        return viewInflate;
    }
}
