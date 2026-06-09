package com.rw.texture.squish;

/* JADX INFO: loaded from: classes.dex */
final class CompressorAlpha {
    private final int[] swapped = new int[16];
    private final int[] codes5 = new int[8];
    private final int[] codes7 = new int[8];
    private final int[] indices5 = new int[16];
    private final int[] indices7 = new int[16];
    private final int[] codes = new int[8];
    private final int[] indices = new int[16];

    CompressorAlpha() {
    }

    void compressAlphaDxt3(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= 8) {
                return;
            }
            int iRound = Math.round((bArr[(8 * i4) + 3] & 255) * 0.05882353f);
            int iRound2 = Math.round((bArr[(8 * i4) + 7] & 255) * 0.05882353f);
            int i5 = 1 << ((2 * i4) + 1);
            if ((i & (1 << (2 * i4))) == 0) {
                iRound = 0;
            }
            if ((i & i5) == 0) {
                iRound2 = 0;
            }
            bArr2[i2 + i4] = (byte) (iRound | (iRound2 << 4));
            i3 = i4 + 1;
        }
    }

    void decompressAlphaDxt3(byte[] bArr, byte[] bArr2, int i) {
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= 8) {
                return;
            }
            int i4 = bArr2[i + i3] & 255;
            int i5 = i4 & 15;
            int i6 = i4 & 240;
            bArr[(8 * i3) + 3] = (byte) (i5 | (i5 << 4));
            bArr[(8 * i3) + 7] = (byte) (i6 | (i6 >> 4));
            i2 = i3 + 1;
        }
    }

    private int fitCodes(byte[] bArr, int i, int[] iArr, int[] iArr2) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < 16) {
                if ((i & (1 << i4)) == 0) {
                    iArr2[i4] = 0;
                } else {
                    int i5 = bArr[(4 * i4) + 3] & 255;
                    int i6 = Integer.MAX_VALUE;
                    int i7 = 0;
                    int i8 = 0;
                    while (true) {
                        int i9 = i8;
                        if (i9 >= 8) {
                            break;
                        }
                        int i10 = i5 - iArr[i9];
                        int i11 = i10 * i10;
                        if (i11 < i6) {
                            i6 = i11;
                            i7 = i9;
                        }
                        i8 = i9 + 1;
                    }
                    iArr2[i4] = i7;
                    i2 += i6;
                }
                i3 = i4 + 1;
            } else {
                return i2;
            }
        }
    }

    private void writeAlphaBlock(int i, int i2, int[] iArr, byte[] bArr, int i3) {
        bArr[i3 + 0] = (byte) i;
        bArr[i3 + 1] = (byte) i2;
        int i4 = 0;
        int i5 = 2;
        int i6 = 0;
        while (true) {
            int i7 = i6;
            if (i7 >= 2) {
                return;
            }
            int i8 = 0;
            int i9 = 0;
            while (true) {
                int i10 = i9;
                if (i10 >= 8) {
                    break;
                }
                int i11 = i4;
                i4++;
                i8 |= iArr[i11] << (3 * i10);
                i9 = i10 + 1;
            }
            int i12 = 0;
            while (true) {
                int i13 = i12;
                if (i13 >= 3) {
                    break;
                }
                int i14 = i5;
                i5++;
                bArr[i3 + i14] = (byte) ((i8 >> (8 * i13)) & 255);
                i12 = i13 + 1;
            }
            i6 = i7 + 1;
        }
    }

    private void writeAlphaBlock5(int i, int i2, int[] iArr, byte[] bArr, int i3) {
        if (i > i2) {
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 < 16) {
                    int i6 = iArr[i5];
                    if (i6 == 0) {
                        this.swapped[i5] = 1;
                    } else if (i6 == 1) {
                        this.swapped[i5] = 0;
                    } else if (i6 <= 5) {
                        this.swapped[i5] = 7 - i6;
                    } else {
                        this.swapped[i5] = i6;
                    }
                    i4 = i5 + 1;
                } else {
                    writeAlphaBlock(i2, i, this.swapped, bArr, i3);
                    return;
                }
            }
        } else {
            writeAlphaBlock(i, i2, iArr, bArr, i3);
        }
    }

    private void writeAlphaBlock7(int i, int i2, int[] iArr, byte[] bArr, int i3) {
        if (i < i2) {
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 < 16) {
                    int i6 = iArr[i5];
                    if (i6 == 0) {
                        this.swapped[i5] = 1;
                    } else if (i6 == 1) {
                        this.swapped[i5] = 0;
                    } else {
                        this.swapped[i5] = 9 - i6;
                    }
                    i4 = i5 + 1;
                } else {
                    writeAlphaBlock(i2, i, this.swapped, bArr, i3);
                    return;
                }
            }
        } else {
            writeAlphaBlock(i, i2, iArr, bArr, i3);
        }
    }

    void compressAlphaDxt5(byte[] bArr, int i, byte[] bArr2, int i2) {
        int iMax = 255;
        int iMin = 0;
        int iMax2 = 255;
        int iMin2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= 16) {
                break;
            }
            if ((i & (1 << i4)) != 0) {
                int i5 = bArr[(4 * i4) + 3] & 255;
                if (i5 < iMax2) {
                    iMax2 = i5;
                }
                if (i5 > iMin2) {
                    iMin2 = i5;
                }
                if (i5 != 0 && i5 < iMax) {
                    iMax = i5;
                }
                if (i5 != 255 && i5 > iMin) {
                    iMin = i5;
                }
            }
            i3 = i4 + 1;
        }
        if (iMax > iMin) {
            iMax = iMin;
        }
        if (iMax2 > iMin2) {
            iMax2 = iMin2;
        }
        if (iMin - iMax < 5) {
            iMin = Math.min(iMax + 5, 255);
        }
        if (iMin - iMax < 5) {
            iMax = Math.max(0, iMin - 5);
        }
        if (iMin2 - iMax2 < 7) {
            iMin2 = Math.min(iMax2 + 7, 255);
        }
        if (iMin2 - iMax2 < 7) {
            iMax2 = Math.max(0, iMin2 - 7);
        }
        this.codes5[0] = iMax;
        this.codes5[1] = iMin;
        int i6 = 1;
        while (true) {
            int i7 = i6;
            if (i7 >= 5) {
                break;
            }
            this.codes5[1 + i7] = (((5 - i7) * iMax) + (i7 * iMin)) / 5;
            i6 = i7 + 1;
        }
        this.codes5[6] = 0;
        this.codes5[7] = 255;
        this.codes7[0] = iMax2;
        this.codes7[1] = iMin2;
        int i8 = 1;
        while (true) {
            int i9 = i8;
            if (i9 >= 7) {
                break;
            }
            this.codes7[1 + i9] = (((7 - i9) * iMax2) + (i9 * iMin2)) / 7;
            i8 = i9 + 1;
        }
        if (fitCodes(bArr, i, this.codes5, this.indices5) <= fitCodes(bArr, i, this.codes7, this.indices7)) {
            writeAlphaBlock5(iMax, iMin, this.indices5, bArr2, i2);
        } else {
            writeAlphaBlock7(iMax2, iMin2, this.indices7, bArr2, i2);
        }
    }

    void decompressAlphaDxt5(byte[] bArr, byte[] bArr2, int i) {
        int i2 = bArr2[i + 0] & 255;
        int i3 = bArr2[i + 1] & 255;
        this.codes[0] = i2;
        this.codes[1] = i3;
        if (i2 <= i3) {
            int i4 = 1;
            while (true) {
                int i5 = i4;
                if (i5 >= 5) {
                    break;
                }
                this.codes[1 + i5] = (((5 - i5) * i2) + (i5 * i3)) / 5;
                i4 = i5 + 1;
            }
            this.codes[6] = 0;
            this.codes[7] = 255;
        } else {
            int i6 = 1;
            while (true) {
                int i7 = i6;
                if (i7 >= 7) {
                    break;
                }
                this.codes[1 + i7] = (((7 - i7) * i2) + (i7 * i3)) / 7;
                i6 = i7 + 1;
            }
        }
        int i8 = 2;
        int i9 = 0;
        int i10 = 0;
        while (true) {
            int i11 = i10;
            if (i11 >= 2) {
                break;
            }
            int i12 = 0;
            int i13 = 0;
            while (true) {
                int i14 = i13;
                if (i14 >= 3) {
                    break;
                }
                int i15 = i8;
                i8++;
                i12 |= (bArr2[i + i15] & 255) << (8 * i14);
                i13 = i14 + 1;
            }
            int i16 = 0;
            while (true) {
                int i17 = i16;
                if (i17 >= 8) {
                    break;
                }
                int i18 = i9;
                i9++;
                this.indices[i18] = (i12 >> (3 * i17)) & 7;
                i16 = i17 + 1;
            }
            i10 = i11 + 1;
        }
        int i19 = 0;
        while (true) {
            int i20 = i19;
            if (i20 >= 16) {
                return;
            }
            bArr[(4 * i20) + 3] = (byte) this.codes[this.indices[i20]];
            i19 = i20 + 1;
        }
    }
}
