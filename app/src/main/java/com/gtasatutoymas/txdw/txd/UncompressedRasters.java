package com.gtasatutoymas.txdw.txd;

import android.graphics.Bitmap;
import android.graphics.Color;

/* JADX INFO: loaded from: classes.dex */
public class UncompressedRasters {
    private static int[] rgbmask = {0, 8, 16, 24, 32, 41, 49, 57, 65, 74, 82, 90, 98, 106, 115, 123, 131, 139, 148, 156, 164, 172, 180, 189, 197, 205, 213, 222, 230, 238, 246, 255};
    private static int[] rgbmask2 = {0, 17, 34, 51, 68, 85, 102, 119, 136, 153, 170, 187, 204, 221, 238, 255};
    private static int[] rgbmask3 = {0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52, 56, 60, 64, 68, 72, 76, 80, 85, 89, 93, 97, 101, 105, 109, 113, 117, 121, 125, 129, 133, 137, 141, 145, 149, 153, 157, 161, 165, 170, 174, 178, 182, 186, 190, 194, 198, 202, 206, 210, 214, 218, 222, 226, 230, 234, 238, 242, 246, 250, 255};

    public static Bitmap convertRGBA8888(TXDHeader tXDHeader) {
        int[] iArr = new int[tXDHeader.width * tXDHeader.height];
        byte[] bArr = tXDHeader.data;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < bArr.length) {
                iArr[i] = Color.argb(bArr[i3 + 3] & 255, bArr[i3] & 255, bArr[i3 + 1] & 255, bArr[i3 + 2] & 255);
                i++;
                i2 = i3 + 4;
            } else {
                return Bitmap.createBitmap(iArr, tXDHeader.width, tXDHeader.height, Bitmap.Config.ARGB_8888);
            }
        }
    }

    public static Bitmap convertBGRA8888(TXDHeader tXDHeader) {
        int[] iArr = new int[tXDHeader.width * tXDHeader.height];
        byte[] bArr = tXDHeader.data;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < bArr.length) {
                iArr[i] = Color.argb(bArr[i3 + 3] & 255, bArr[i3 + 2] & 255, bArr[i3 + 1] & 255, bArr[i3] & 255);
                i++;
                i2 = i3 + 4;
            } else {
                return Bitmap.createBitmap(iArr, tXDHeader.width, tXDHeader.height, Bitmap.Config.ARGB_8888);
            }
        }
    }

    public static Bitmap convertBGR888(TXDHeader tXDHeader) {
        int[] iArr = new int[tXDHeader.width * tXDHeader.height];
        byte[] bArr = tXDHeader.data;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < bArr.length) {
                iArr[i] = Color.argb(255, bArr[i3 + 2] & 255, bArr[i3 + 1] & 255, bArr[i3] & 255);
                i++;
                i2 = i3 + 4;
            } else {
                return Bitmap.createBitmap(iArr, tXDHeader.width, tXDHeader.height, Bitmap.Config.ARGB_8888);
            }
        }
    }

    public static Bitmap ConvertRGB565(TXDHeader tXDHeader) {
        byte[] bArr = tXDHeader.data;
        int[] iArr = new int[tXDHeader.width * tXDHeader.height];
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2 += 2) {
            int i3 = ((bArr[i2 + 1] & 255) << 8) | (bArr[i2] & 255);
            iArr[i] = Color.argb(255, rgbmask[(63488 & i3) >> 11], rgbmask3[(i3 & 2016) >> 5], rgbmask[i3 & 31]);
            i++;
        }
        return Bitmap.createBitmap(iArr, tXDHeader.width, tXDHeader.height, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap convertRGBA4444(TXDHeader tXDHeader) {
        return convertRGBA8888(tXDHeader);
    }
}
