package com.gtasatutoymas.txdw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import com.gtasatutoymas.txdw.engine.Objs;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: classes.dex */
public class TextureGL {
    public static int getID(GL10 gl10, String str) {
        int[] iArr = new int[1];
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(str);
        gl10.glGenTextures(1, iArr, 0);
        gl10.glEnable(3553);
        gl10.glBindTexture(3553, iArr[0]);
        GLUtils.texImage2D(3553, 0, bitmapDecodeFile, 0);
        gl10.glTexParameterx(3553, 10242, 33071);
        gl10.glTexParameterx(3553, 10243, 33071);
        gl10.glTexParameterx(3553, 10240, 9729);
        gl10.glTexParameterx(3553, 10241, 9729);
        return iArr[0];
    }

    public static void draw(GL10 gl10, int i, Objs objs) {
        gl10.glEnable(3553);
        gl10.glActiveTexture(33984);
        gl10.glBindTexture(3553, i);
        gl10.glDrawArrays(4, 0, objs.toVertexBuffer().capacity());
        gl10.glDisable(3553);
    }
}
