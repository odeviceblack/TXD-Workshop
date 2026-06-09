package com.gtasatutoymas.txdw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class ListAdapter extends ArrayAdapter<String> {
    public ListAdapter(Context context, ArrayList<String> arrayList) {
        super(context, R.layout.list_adapter, arrayList);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.list_adapter, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.adapter_tv)).setText(getItem(i));
        return viewInflate;
    }
}
