package com.rw.texture;

import android.graphics.Color;
import com.rw.texture.squish.Squish;
import min3d.core.Color4BufferList;

/* JADX INFO: loaded from: classes.dex */
public class DXTDecompress {
    byte[] RGBA = (byte[]) null;
    byte[] dxtData;
    int height;
    int[] pixels;
    int type;
    int width;

    public DXTDecompress(byte[] bArr, int i, int i2, int i3) {
        this.dxtData = (byte[]) null;
        this.pixels = (int[]) null;
        this.width = 0;
        this.height = 0;
        this.type = 0;
        this.dxtData = bArr;
        this.width = i;
        this.height = i2;
        this.type = i3;
        this.pixels = new int[i * i2];
        Decompress();
    }

    public void Decompress() {
        switch (this.type) {
            case Color4BufferList.BYTES_PER_PROPERTY /* 1 */:
                this.RGBA = Squish.decompressImage(new byte[this.width * this.height * 4], this.width, this.height, this.dxtData, Squish.CompressionType.DXT1);
                break;
            case 3:
                this.RGBA = Squish.decompressImage(new byte[this.width * this.height * 4], this.width, this.height, this.dxtData, Squish.CompressionType.DXT3);
                break;
            case 5:
                this.RGBA = Squish.decompressImage(new byte[this.width * this.height * 4], this.width, this.height, this.dxtData, Squish.CompressionType.DXT5);
                break;
        }
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= this.RGBA.length) {
                return;
            }
            this.pixels[i] = Color.argb(this.RGBA[i3 + 3] & 255, this.RGBA[i3] & 255, this.RGBA[i3 + 1] & 255, this.RGBA[i3 + 2] & 255);
            i++;
            i2 = i3 + 4;
        }
    }

    public byte[] getImageData() {
        return this.RGBA;
    }

    public int[] getImagePixels() {
        return this.pixels;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
