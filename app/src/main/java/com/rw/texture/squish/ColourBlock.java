package com.rw.texture.squish;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
final class ColourBlock {
    private final int[] remapped = new int[16];
    private final int[] indices = new int[16];
    private final int[] codes = new int[16];

    ColourBlock() {
    }

    static int gammaColour(float f, float f2) {
        return Math.round(f2 * f);
    }

    private static int floatTo565(Vec vec) {
        int iRound = Math.round(31.0f * vec.x());
        int iRound2 = Math.round(63.0f * vec.y());
        return (iRound << 11) | (iRound2 << 5) | Math.round(31.0f * vec.z());
    }

    private static void writeColourBlock(int i, int i2, int[] iArr, byte[] bArr, int i3) {
        bArr[i3 + 0] = (byte) (i & 255);
        bArr[i3 + 1] = (byte) (i >> 8);
        bArr[i3 + 2] = (byte) (i2 & 255);
        bArr[i3 + 3] = (byte) (i2 >> 8);
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= 4) {
                return;
            }
            int i6 = 4 * i5;
            bArr[i3 + 4 + i5] = (byte) (iArr[i6 + 0] | (iArr[i6 + 1] << 2) | (iArr[i6 + 2] << 4) | (iArr[i6 + 3] << 6));
            i4 = i5 + 1;
        }
    }

    void writeColourBlock3(Vec vec, Vec vec2, int[] iArr, byte[] bArr, int i) {
        int iFloatTo565 = floatTo565(vec);
        int iFloatTo5652 = floatTo565(vec2);
        if (iFloatTo565 <= iFloatTo5652) {
            System.arraycopy(iArr, 0, this.remapped, 0, 16);
        } else {
            iFloatTo565 = iFloatTo5652;
            iFloatTo5652 = iFloatTo565;
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 >= 16) {
                    break;
                }
                if (iArr[i3] == 0) {
                    this.remapped[i3] = 1;
                } else if (iArr[i3] == 1) {
                    this.remapped[i3] = 0;
                } else {
                    this.remapped[i3] = iArr[i3];
                }
                i2 = i3 + 1;
            }
        }
        writeColourBlock(iFloatTo565, iFloatTo5652, this.remapped, bArr, i);
    }

    void writeColourBlock4(Vec vec, Vec vec2, int[] iArr, byte[] bArr, int i) {
        int iFloatTo565 = floatTo565(vec);
        int iFloatTo5652 = floatTo565(vec2);
        if (iFloatTo565 < iFloatTo5652) {
            iFloatTo565 = iFloatTo5652;
            iFloatTo5652 = iFloatTo565;
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 >= 16) {
                    break;
                }
                this.remapped[i3] = (iArr[i3] ^ 1) & 3;
                i2 = i3 + 1;
            }
        } else if (iFloatTo565 == iFloatTo5652) {
            Arrays.fill(this.remapped, 0);
        } else {
            System.arraycopy(iArr, 0, this.remapped, 0, 16);
        }
        writeColourBlock(iFloatTo565, iFloatTo5652, this.remapped, bArr, i);
    }

    void decompressColour(byte[] bArr, byte[] bArr2, int i, boolean z) {
        int iUnpack565 = unpack565(bArr2, i, this.codes, 0);
        int iUnpack5652 = unpack565(bArr2, i + 2, this.codes, 4);
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= 3) {
                break;
            }
            int i4 = this.codes[i3];
            int i5 = this.codes[4 + i3];
            if (z && iUnpack565 <= iUnpack5652) {
                this.codes[8 + i3] = (i4 + i5) / 2;
                this.codes[12 + i3] = 0;
            } else {
                this.codes[8 + i3] = ((2 * i4) + i5) / 3;
                this.codes[12 + i3] = (i4 + (2 * i5)) / 3;
            }
            i2 = i3 + 1;
        }
        this.codes[11] = 255;
        this.codes[15] = (!z || iUnpack565 > iUnpack5652) ? 255 : 0;
        int i6 = 0;
        while (true) {
            int i7 = i6;
            if (i7 >= 4) {
                break;
            }
            int i8 = 4 * i7;
            int i9 = bArr2[i + 4 + i7] & 255;
            this.indices[i8 + 0] = i9 & 3;
            this.indices[i8 + 1] = (i9 >> 2) & 3;
            this.indices[i8 + 2] = (i9 >> 4) & 3;
            this.indices[i8 + 3] = (i9 >> 6) & 3;
            i6 = i7 + 1;
        }
        int i10 = 0;
        while (true) {
            int i11 = i10;
            if (i11 >= 16) {
                return;
            }
            int i12 = 4 * this.indices[i11];
            int i13 = 0;
            while (true) {
                int i14 = i13;
                if (i14 >= 4) {
                    break;
                }
                bArr[(4 * i11) + i14] = (byte) this.codes[i12 + i14];
                i13 = i14 + 1;
            }
            i10 = i11 + 1;
        }
    }

    private static int unpack565(byte[] bArr, int i, int[] iArr, int i2) {
        int i3 = (bArr[i + 0] & 255) | ((bArr[i + 1] & 255) << 8);
        int i4 = (i3 >> 11) & 31;
        int i5 = (i3 >> 5) & 63;
        int i6 = i3 & 31;
        iArr[i2 + 0] = (i4 << 3) | (i4 >> 2);
        iArr[i2 + 1] = (i5 << 2) | (i5 >> 4);
        iArr[i2 + 2] = (i6 << 3) | (i6 >> 2);
        iArr[i2 + 3] = 255;
        return i3;
    }
}
