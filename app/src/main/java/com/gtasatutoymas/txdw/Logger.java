package com.gtasatutoymas.txdw;

import android.os.Environment;
import java.io.BufferedWriter;
import java.io.FileWriter;

/* JADX INFO: loaded from: classes.dex */
public class Logger {
    public static void logger(int i, String str) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new StringBuffer().append(Environment.getExternalStorageDirectory()).append("/logs.txt").toString(), true));
            bufferedWriter.append((CharSequence) new StringBuffer().append(new StringBuffer().append("RW log: ").append(i == 1 ? "Error " : "Normal ").toString()).append(str).toString());
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
        }
    }
}
