package com.rw.texture.squish;

/* JADX INFO: loaded from: classes.dex */
public final class Squish {

    public enum CompressionType {
        DXT1(8),
        DXT3(16),
        DXT5(16);

        public final int blockOffset;
        public final int blockSize;

        CompressionType(int i) {
            this.blockSize = i;
            this.blockOffset = i - 8;
        }
    }

    public enum CompressionMethod {
        CLUSTER_FIT { // from class: com.rw.texture.squish.Squish.CompressionMethod.100000000
            @Override // com.rw.texture.squish.Squish.CompressionMethod
            CompressorColourFit getCompressor(ColourSet colourSet, CompressionType compressionType, CompressionMetric compressionMetric, ColourBlock colourBlock) {
                return new CompressorCluster(colourSet, compressionType, compressionMetric, colourBlock);
            }
        },
        RANGE_FIT { // from class: com.rw.texture.squish.Squish.CompressionMethod.100000001
            @Override // com.rw.texture.squish.Squish.CompressionMethod
            CompressorColourFit getCompressor(ColourSet colourSet, CompressionType compressionType, CompressionMetric compressionMetric, ColourBlock colourBlock) {
                return new CompressorRange(colourSet, compressionType, compressionMetric, colourBlock);
            }
        };

        abstract CompressorColourFit getCompressor(ColourSet colourSet, CompressionType compressionType, CompressionMetric compressionMetric, ColourBlock colourBlock);
    }

    public enum CompressionMetric {
        PERCEPTUAL(0.2126f, 0.7152f, 0.0722f),
        UNIFORM(1.0f, 1.0f, 1.0f);

        public final float b;
        public final float g;
        public final float r;

        CompressionMetric(float f, float f2, float f3) {
            this.r = f;
            this.g = f2;
            this.b = f3;
        }

        public float dot(float f, float f2, float f3) {
            return (this.r * f) + (this.g * f2) + (this.b * f3);
        }
    }

    private static class CompressionTask {
        private final CompressionMethod method;
        private final CompressionMetric metric;
        private final CompressorColourFit multiColour;
        private final CompressionType type;
        private final boolean weightAlpha;
        private final ColourSet colours = new ColourSet();
        private final ColourBlock writer = new ColourBlock();
        private CompressorSingleColour singleColour = (CompressorSingleColour) null;
        private CompressorAlpha alphaCompressor = (CompressorAlpha) null;

        CompressionTask(CompressionType compressionType, CompressionMethod compressionMethod, CompressionMetric compressionMetric, boolean z) {
            this.type = compressionType;
            this.method = compressionMethod;
            this.metric = compressionMetric;
            this.weightAlpha = z;
            this.multiColour = compressionMethod.getCompressor(this.colours, compressionType, compressionMetric, this.writer);
        }

        CompressorSingleColour getSingleColourCompressor() {
            if (this.singleColour == null) {
                this.singleColour = new CompressorSingleColour(this.colours, this.type, this.writer);
            }
            return this.singleColour;
        }

        CompressorAlpha getAlphaCompressor() {
            if (this.alphaCompressor == null) {
                this.alphaCompressor = new CompressorAlpha();
            }
            return this.alphaCompressor;
        }
    }

    Squish() {
    }

    public static int getStorageRequirements(int i, int i2, CompressionType compressionType) {
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("Invalid image dimensions specified: ").append(i).toString()).append(" x ").toString()).append(i2).toString());
        }
        return ((i + 3) / 4) * ((i2 + 3) / 4) * compressionType.blockSize;
    }

    public static byte[] compressImage(byte[] bArr, int i, int i2, byte[] bArr2, CompressionType compressionType) {
        return compressImage(bArr, i, i2, bArr2, compressionType, CompressionMethod.CLUSTER_FIT, CompressionMetric.PERCEPTUAL, false);
    }

    public static byte[] compressImage(byte[] bArr, int i, int i2, byte[] bArr2, CompressionType compressionType, CompressionMethod compressionMethod) {
        return compressImage(bArr, i, i2, bArr2, compressionType, compressionMethod, CompressionMetric.PERCEPTUAL, false);
    }

    public static byte[] compressImage(byte[] bArr, int i, int i2, byte[] bArr2, CompressionType compressionType, CompressionMethod compressionMethod, CompressionMetric compressionMetric, boolean z) {
        byte[] bArrCheckCompressInput = checkCompressInput(bArr, i, i2, bArr2, compressionType);
        byte[] bArr3 = new byte[64];
        CompressionTask compressionTask = new CompressionTask(compressionType, compressionMethod, compressionMetric, z);
        int i3 = 0;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < i2) {
                int i6 = 0;
                while (true) {
                    int i7 = i6;
                    if (i7 >= i) {
                        break;
                    }
                    int i8 = 0;
                    int i9 = 0;
                    int i10 = 0;
                    while (true) {
                        int i11 = i10;
                        if (i11 >= 4) {
                            break;
                        }
                        int i12 = i5 + i11;
                        int i13 = 0;
                        while (true) {
                            int i14 = i13;
                            if (i14 >= 4) {
                                break;
                            }
                            int i15 = i7 + i14;
                            if (i15 < i && i12 < i2) {
                                int i16 = 4 * ((i * i12) + i15);
                                int i17 = 0;
                                while (true) {
                                    int i18 = i17;
                                    if (i18 >= 4) {
                                        break;
                                    }
                                    int i19 = i8;
                                    i8++;
                                    int i20 = i16;
                                    i16++;
                                    bArr3[i19] = bArr[i20];
                                    i17 = i18 + 1;
                                }
                                i9 |= 1 << ((4 * i11) + i14);
                            } else {
                                i8 += 4;
                            }
                            i13 = i14 + 1;
                        }
                        i10 = i11 + 1;
                    }
                    compress(bArr3, i9, bArrCheckCompressInput, i3, compressionTask);
                    i3 += compressionType.blockSize;
                    i6 = i7 + 4;
                }
                i4 = i5 + 4;
            } else {
                return bArrCheckCompressInput;
            }
        }
    }

    private static byte[] checkCompressInput(byte[] bArr, int i, int i2, byte[] bArr2, CompressionType compressionType) {
        byte[] bArr3 = bArr2;
        int storageRequirements = getStorageRequirements(i, i2, compressionType);
        if (bArr == null || bArr.length < i * i2 * 4) {
            throw new IllegalArgumentException("Invalid source image data specified.");
        }
        if (bArr3 == null || bArr3.length < storageRequirements) {
            bArr3 = new byte[storageRequirements];
        }
        return bArr3;
    }

    private static void compress(byte[] bArr, int i, byte[] bArr2, int i2, CompressionTask compressionTask) {
        CompressorColourFit singleColourCompressor;
        CompressionType compressionType = compressionTask.type;
        int i3 = i2 + compressionType.blockOffset;
        compressionTask.colours.init(bArr, i, compressionType, compressionTask.weightAlpha);
        if (compressionTask.colours.getCount() == 1) {
            singleColourCompressor = compressionTask.getSingleColourCompressor();
        } else {
            singleColourCompressor = compressionTask.multiColour;
        }
        singleColourCompressor.init();
        singleColourCompressor.compress(bArr2, i3);
        if (compressionType == CompressionType.DXT3) {
            compressionTask.getAlphaCompressor().compressAlphaDxt3(bArr, i, bArr2, i2);
        } else if (compressionType == CompressionType.DXT5) {
            compressionTask.getAlphaCompressor().compressAlphaDxt5(bArr, i, bArr2, i2);
        }
    }

    public static byte[] decompressImage(byte[] bArr, int i, int i2, byte[] bArr2, CompressionType compressionType) {
        byte[] bArrCheckDecompressInput = checkDecompressInput(bArr, i, i2, bArr2, compressionType);
        byte[] bArr3 = new byte[64];
        ColourBlock colourBlock = new ColourBlock();
        CompressorAlpha compressorAlpha = new CompressorAlpha();
        int i3 = 0;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < i2) {
                int i6 = 0;
                while (true) {
                    int i7 = i6;
                    if (i7 >= i) {
                        break;
                    }
                    decompress(bArr3, bArr2, i3, compressionType, colourBlock, compressorAlpha);
                    int i8 = 0;
                    int i9 = 0;
                    while (true) {
                        int i10 = i9;
                        if (i10 >= 4) {
                            break;
                        }
                        int i11 = 0;
                        while (true) {
                            int i12 = i11;
                            if (i12 >= 4) {
                                break;
                            }
                            int i13 = i7 + i12;
                            int i14 = i5 + i10;
                            if (i13 < i && i14 < i2) {
                                int i15 = 4 * ((i * i14) + i13);
                                int i16 = 0;
                                while (true) {
                                    int i17 = i16;
                                    if (i17 >= 4) {
                                        break;
                                    }
                                    int i18 = i15;
                                    i15++;
                                    int i19 = i8;
                                    i8++;
                                    bArrCheckDecompressInput[i18] = bArr3[i19];
                                    i16 = i17 + 1;
                                }
                            } else {
                                i8 += 4;
                            }
                            i11 = i12 + 1;
                        }
                        i9 = i10 + 1;
                    }
                    i3 += compressionType.blockSize;
                    i6 = i7 + 4;
                }
                i4 = i5 + 4;
            } else {
                return bArrCheckDecompressInput;
            }
        }
    }

    private static byte[] checkDecompressInput(byte[] bArr, int i, int i2, byte[] bArr2, CompressionType compressionType) {
        byte[] bArr3 = bArr;
        int storageRequirements = getStorageRequirements(i, i2, compressionType);
        if (bArr2 == null || bArr2.length < storageRequirements) {
            throw new IllegalArgumentException("Invalid source image data specified.");
        }
        if (bArr3 == null || bArr3.length < i * i2 * 4) {
            bArr3 = new byte[i * i2 * 4];
        }
        return bArr3;
    }

    private static void decompress(byte[] bArr, byte[] bArr2, int i, CompressionType compressionType, ColourBlock colourBlock, CompressorAlpha compressorAlpha) {
        colourBlock.decompressColour(bArr, bArr2, i + compressionType.blockOffset, compressionType == CompressionType.DXT1);
        if (compressionType == CompressionType.DXT3) {
            compressorAlpha.decompressAlphaDxt3(bArr, bArr2, i);
        } else if (compressionType == CompressionType.DXT5) {
            compressorAlpha.decompressAlphaDxt5(bArr, bArr2, i);
        }
    }
}
