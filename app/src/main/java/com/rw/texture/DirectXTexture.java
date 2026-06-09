package com.rw.texture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import com.rw.texture.squish.Squish;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class DirectXTexture {

    public static class DXTCData {
        public byte[] data;
        public int heigth;
        public boolean isAlpha;
        public int width;

        public DXTCData(byte[] bArr, int i, int i2, boolean z) {
            this.data = bArr;
            this.width = i;
            this.heigth = i2;
            this.isAlpha = z;
        }
    }

    public static void SaveDataFromFile(File file, DXTCData dXTCData) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[12];
            writeInt(bArr, 0, dXTCData.width);
            writeInt(bArr, 4, dXTCData.heigth);
            if (dXTCData.isAlpha) {
                writeInt(bArr, 8, 1);
            } else {
                writeInt(bArr, 8, 0);
            }
            fileOutputStream.write(bArr);
            fileOutputStream.write(dXTCData.data);
            fileOutputStream.close();
        } catch (IOException e) {
        }
    }

    private static void writeInt(byte[] bArr, int i, int i2) {
        bArr[i + 0] = (byte) (i2 & 255);
        bArr[i + 1] = (byte) ((i2 >> 8) & 255);
        bArr[i + 2] = (byte) ((i2 >> 16) & 255);
        bArr[i + 3] = (byte) ((i2 >> 24) & 255);
    }

    public static DXTCData getDataFromFile(File file) {
        boolean z;
        int storageRequirements;
        DXTCData dXTCData = (DXTCData) null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[12];
            fileInputStream.read(bArr, 0, 12);
            int i = readInt(bArr, 0);
            int i2 = readInt(bArr, 4);
            if (readInt(bArr, 8) == 1) {
                z = true;
                storageRequirements = Squish.getStorageRequirements(i, i2, Squish.CompressionType.DXT5);
            } else {
                z = false;
                storageRequirements = Squish.getStorageRequirements(i, i2, Squish.CompressionType.DXT1);
            }
            byte[] bArr2 = new byte[storageRequirements];
            fileInputStream.read(bArr2, 0, storageRequirements);
            fileInputStream.close();
            dXTCData = new DXTCData(bArr2, i, i2, z);
        } catch (IOException e) {
        }
        return dXTCData;
    }

    private static int readInt(byte[] bArr, int i) {
        return (bArr[i + 0] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 3] & 255) << 24);
    }

    public static int[] ByteColortoIntColor(byte[] bArr, boolean z) {
        int[] iArr = new int[bArr.length / 4];
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < bArr.length) {
                iArr[i] = Color.argb(bArr[i3 + 3] & 255, bArr[i3] & 255, bArr[i3 + 1] & 255, bArr[i3 + 2] & 255);
                i++;
                i2 = i3 + 4;
            } else {
                return iArr;
            }
        }
    }

    public static DXTCData compressDXT1(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[width * height];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        byte[] bArr = new byte[width * height * 4];
        int i = 0;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int iRed = Color.red(iArr[i2]);
            int iGreen = Color.green(iArr[i2]);
            int iBlue = Color.blue(iArr[i2]);
            bArr[i] = (byte) iRed;
            int i3 = i + 1;
            bArr[i3] = (byte) iGreen;
            int i4 = i3 + 1;
            bArr[i4] = (byte) iBlue;
            int i5 = i4 + 1;
            bArr[i5] = -2;
            i = i5 + 1;
        }
        return new DXTCData(Squish.compressImage(bArr, width, height, new byte[Squish.getStorageRequirements(width, height, Squish.CompressionType.DXT1)], Squish.CompressionType.DXT1, Squish.CompressionMethod.CLUSTER_FIT), width, height, false);
    }

    public static DXTCData CompressDXT5(String str) {
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(str);
        int width = bitmapDecodeFile.getWidth();
        int height = bitmapDecodeFile.getHeight();
        int[] iArr = new int[width * height];
        bitmapDecodeFile.getPixels(iArr, 0, width, 0, 0, width, height);
        byte[] bArr = new byte[width * height * 4];
        int i = 0;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int iRed = Color.red(iArr[i2]);
            int iGreen = Color.green(iArr[i2]);
            int iBlue = Color.blue(iArr[i2]);
            int iAlpha = Color.alpha(iArr[i2]);
            bArr[i] = (byte) iRed;
            int i3 = i + 1;
            bArr[i3] = (byte) iGreen;
            int i4 = i3 + 1;
            bArr[i4] = (byte) iBlue;
            int i5 = i4 + 1;
            bArr[i5] = (byte) iAlpha;
            i = i5 + 1;
        }
        return new DXTCData(Squish.compressImage(bArr, width, height, new byte[Squish.getStorageRequirements(width, height, Squish.CompressionType.DXT5)], Squish.CompressionType.DXT5, Squish.CompressionMethod.CLUSTER_FIT), width, height, true);
    }

    public static Bitmap Decompress(DXTCData dXTCData) {
        int[] iArr = new int[dXTCData.width * dXTCData.heigth];
        if (dXTCData.isAlpha) {
            byte[] bArrDecompressImage = Squish.decompressImage(new byte[dXTCData.width * dXTCData.heigth * 4], dXTCData.width, dXTCData.heigth, dXTCData.data, Squish.CompressionType.DXT5);
            int i = 0;
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 >= bArrDecompressImage.length) {
                    break;
                }
                iArr[i] = Color.argb(bArrDecompressImage[i3 + 3] & 255, bArrDecompressImage[i3] & 255, bArrDecompressImage[i3 + 1] & 255, bArrDecompressImage[i3 + 2] & 255);
                i++;
                i2 = i3 + 4;
            }
        } else {
            byte[] bArrDecompressImage2 = Squish.decompressImage(new byte[dXTCData.width * dXTCData.heigth * 4], dXTCData.width, dXTCData.heigth, dXTCData.data, Squish.CompressionType.DXT1);
            int i4 = 0;
            int i5 = 0;
            while (true) {
                int i6 = i5;
                if (i6 >= bArrDecompressImage2.length) {
                    break;
                }
                iArr[i4] = Color.argb(bArrDecompressImage2[i6 + 3] & 255, bArrDecompressImage2[i6] & 255, bArrDecompressImage2[i6 + 1] & 255, bArrDecompressImage2[i6 + 2] & 255);
                i4++;
                i5 = i6 + 4;
            }
        }
        return Bitmap.createBitmap(iArr, dXTCData.width, dXTCData.heigth, Bitmap.Config.ARGB_8888);
    }

    public static void writeData(String str, byte[] bArr) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            fileOutputStream.write(bArr);
            fileOutputStream.close();
        } catch (IOException e) {
        }
    }

    public static void readData(String str, byte[] bArr) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            fileInputStream.read(bArr);
            fileInputStream.close();
        } catch (IOException e) {
        }
    }
}
