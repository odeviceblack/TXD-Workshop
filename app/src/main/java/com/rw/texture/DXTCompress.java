package com.rw.texture;

import android.graphics.Color;
import com.rw.texture.squish.Squish;
import min3d.core.Color4BufferList;

/* JADX INFO: loaded from: classes.dex */
public class DXTCompress {
    byte[] RGBA;
    byte[] dxtData = (byte[]) null;
    int height;
    int type;
    int width;

    public DXTCompress(int[] iArr, int i, int i2, int i3) {
        this.RGBA = (byte[]) null;
        this.width = 0;
        this.height = 0;
        this.type = 0;
        this.RGBA = new byte[i * i2 * 4];
        if (i3 == 1) {
            int i4 = 0;
            for (int i5 = 0; i5 < iArr.length; i5++) {
                int iRed = Color.red(iArr[i5]);
                int iGreen = Color.green(iArr[i5]);
                int iBlue = Color.blue(iArr[i5]);
                this.RGBA[i4] = (byte) iRed;
                int i6 = i4 + 1;
                this.RGBA[i6] = (byte) iGreen;
                int i7 = i6 + 1;
                this.RGBA[i7] = (byte) iBlue;
                int i8 = i7 + 1;
                this.RGBA[i8] = -1;
                i4 = i8 + 1;
            }
        } else if (i3 == 3 || i3 == 5) {
            int i9 = 0;
            for (int i10 = 0; i10 < iArr.length; i10++) {
                int iRed2 = Color.red(iArr[i10]);
                int iGreen2 = Color.green(iArr[i10]);
                int iBlue2 = Color.blue(iArr[i10]);
                int iAlpha = Color.alpha(iArr[i10]);
                this.RGBA[i9] = (byte) iRed2;
                int i11 = i9 + 1;
                this.RGBA[i11] = (byte) iGreen2;
                int i12 = i11 + 1;
                this.RGBA[i12] = (byte) iBlue2;
                int i13 = i12 + 1;
                this.RGBA[i13] = (byte) iAlpha;
                i9 = i13 + 1;
            }
        }
        this.width = i;
        this.height = i2;
        this.type = i3;
    }

    public DXTCompress(byte[] bArr, int i, int i2, int i3) {
        this.RGBA = (byte[]) null;
        this.width = 0;
        this.height = 0;
        this.type = 0;
        this.RGBA = bArr;
        this.width = i;
        this.height = i2;
        this.type = i3;
    }

    public void Compress() {
        switch (this.type) {
            case Color4BufferList.BYTES_PER_PROPERTY /* 1 */:
                this.dxtData = Squish.compressImage(this.RGBA, this.width, this.height, new byte[Squish.getStorageRequirements(this.width, this.height, Squish.CompressionType.DXT1)], Squish.CompressionType.DXT1, Squish.CompressionMethod.CLUSTER_FIT);
                break;
            case 3:
                this.dxtData = Squish.compressImage(this.RGBA, this.width, this.height, new byte[Squish.getStorageRequirements(this.width, this.height, Squish.CompressionType.DXT3)], Squish.CompressionType.DXT3, Squish.CompressionMethod.CLUSTER_FIT);
                break;
            case 5:
                this.dxtData = Squish.compressImage(this.RGBA, this.width, this.height, new byte[Squish.getStorageRequirements(this.width, this.height, Squish.CompressionType.DXT5)], Squish.CompressionType.DXT5, Squish.CompressionMethod.CLUSTER_FIT);
                break;
        }
    }

    public byte[] getCompressedData() {
        return this.dxtData;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
