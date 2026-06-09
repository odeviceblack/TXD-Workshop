package com.rw.texture.squish;

import com.rw.texture.squish.Squish;

/* JADX INFO: loaded from: classes.dex */
abstract class CompressorColourFit {
    protected static final float GRID_X = 31.0f;
    protected static final float GRID_X_RCP = 0.032258064f;
    protected static final float GRID_Y = 63.0f;
    protected static final float GRID_Y_RCP = 0.015873017f;
    protected static final float GRID_Z = 31.0f;
    protected static final float GRID_Z_RCP = 0.032258064f;
    protected final ColourSet colours;
    protected final Squish.CompressionType type;

    abstract void compress3(byte[] bArr, int i);

    abstract void compress4(byte[] bArr, int i);

    abstract void init();

    protected CompressorColourFit(ColourSet colourSet, Squish.CompressionType compressionType) {
        this.colours = colourSet;
        this.type = compressionType;
    }

    final void compress(byte[] bArr, int i) {
        if (this.type != Squish.CompressionType.DXT1) {
            compress4(bArr, i);
            return;
        }
        compress3(bArr, i);
        if (!this.colours.isTransparent()) {
            compress4(bArr, i);
        }
    }

    protected static float clamp(float f, float f2, float f3) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        return ((int) ((f2 * f) + 0.5f)) * f3;
    }
}
