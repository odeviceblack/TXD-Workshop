package com.rw.texture.squish;

import com.rw.texture.squish.Squish;

/* JADX INFO: loaded from: classes.dex */
final class CompressorSingleColour extends CompressorColourFit {
    private int[] colour;
    private final ColourBlock colourBlockWriter;
    private final Vec end;
    private final int[] index;
    private final int[] indices;
    private final int[][][][] lookups;
    private final int[][] sources;
    private final Vec start;
    private int totalBestError;

    CompressorSingleColour(ColourSet colourSet, Squish.CompressionType compressionType, ColourBlock colourBlock) {
        super(colourSet, compressionType);
        this.indices = new int[16];
        this.lookups = new int[3][][][];
        this.sources = new int[3][];
        this.start = new Vec();
        this.end = new Vec();
        this.index = new int[1];
        this.colour = new int[3];
        this.colourBlockWriter = colourBlock;
    }

    @Override // com.rw.texture.squish.CompressorColourFit
    void init() {
        Vec vec = this.colours.getPoints()[0];
        this.colour[0] = Math.round(255.0f * vec.x());
        this.colour[1] = Math.round(255.0f * vec.y());
        this.colour[2] = Math.round(255.0f * vec.z());
        this.totalBestError = Integer.MAX_VALUE;
    }

    @Override // com.rw.texture.squish.CompressorColourFit
    void compress3(byte[] bArr, int i) {
        this.lookups[0] = SingleColourLookup3.LOOKUP_5_3;
        this.lookups[1] = SingleColourLookup3.LOOKUP_6_3;
        this.lookups[2] = SingleColourLookup3.LOOKUP_5_3;
        int iComputeEndPoints = computeEndPoints(3, this.lookups);
        if (iComputeEndPoints < this.totalBestError) {
            this.colours.remapIndices(this.index, this.indices);
            this.colourBlockWriter.writeColourBlock3(this.start, this.end, this.indices, bArr, i);
            this.totalBestError = iComputeEndPoints;
        }
    }

    @Override // com.rw.texture.squish.CompressorColourFit
    void compress4(byte[] bArr, int i) {
        this.lookups[0] = SingleColourLookup4.LOOKUP_5_4;
        this.lookups[1] = SingleColourLookup4.LOOKUP_6_4;
        this.lookups[2] = SingleColourLookup4.LOOKUP_5_4;
        int iComputeEndPoints = computeEndPoints(4, this.lookups);
        if (iComputeEndPoints < this.totalBestError) {
            this.colours.remapIndices(this.index, this.indices);
            this.colourBlockWriter.writeColourBlock4(this.start, this.end, this.indices, bArr, i);
            this.totalBestError = iComputeEndPoints;
        }
    }

    private int computeEndPoints(int i, int[][][][] iArr) {
        int i2 = this.totalBestError;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < i) {
                int i5 = 0;
                int i6 = 0;
                while (true) {
                    int i7 = i6;
                    if (i7 >= 3) {
                        break;
                    }
                    this.sources[i7] = iArr[i7][this.colour[i7]][i4];
                    int i8 = this.sources[i7][2];
                    i5 += i8 * i8;
                    i6 = i7 + 1;
                }
                if (i5 < i2) {
                    this.start.set(this.sources[0][0] * 0.032258064f, this.sources[1][0] * 0.015873017f, this.sources[2][0] * 0.032258064f);
                    this.end.set(this.sources[0][1] * 0.032258064f, this.sources[1][1] * 0.015873017f, this.sources[2][1] * 0.032258064f);
                    this.index[0] = i4;
                    i2 = i5;
                }
                i3 = i4 + 1;
            } else {
                return i2;
            }
        }
    }
}
