package com.rw.texture.squish;

import com.rw.texture.squish.Squish;

/* JADX INFO: loaded from: classes.dex */
final class CompressorRange extends CompressorColourFit {
    private float bestError;
    private final int[] closest;
    private final Vec[] codes;
    private final ColourBlock colourBlockWriter;
    private final Vec end;
    private final int[] indices;
    private final Squish.CompressionMetric metric;
    private final Vec start;

    CompressorRange(ColourSet colourSet, Squish.CompressionType compressionType, Squish.CompressionMetric compressionMetric, ColourBlock colourBlock) {
        super(colourSet, compressionType);
        this.closest = new int[16];
        this.indices = new int[16];
        this.codes = new Vec[4];
        this.start = new Vec();
        this.end = new Vec();
        for (int i = 0; i < this.codes.length; i++) {
            this.codes[i] = new Vec();
        }
        this.metric = compressionMetric;
        this.colourBlockWriter = colourBlock;
    }

    @Override // com.rw.texture.squish.CompressorColourFit
    void init() {
        this.bestError = Float.MAX_VALUE;
        int count = this.colours.getCount();
        Vec[] points = this.colours.getPoints();
        Vec vecComputePrincipleComponent = Matrix.computePrincipleComponent(Matrix.computeWeightedCovariance(this.colours, (Matrix) null));
        if (count <= 0) {
            return;
        }
        float fX = points[0].x();
        float fX2 = fX;
        float fX3 = fX;
        float fY = points[0].y();
        float fY2 = fY;
        float fY3 = fY;
        float fZ = points[0].z();
        float fZ2 = fZ;
        float fZ3 = fZ;
        float fDot = points[0].dot(vecComputePrincipleComponent);
        float f = fDot;
        float f2 = fDot;
        int i = 1;
        while (true) {
            int i2 = i;
            if (i2 < count) {
                Vec vec = points[i2];
                float fDot2 = vec.dot(vecComputePrincipleComponent);
                if (fDot2 < f2) {
                    fX3 = vec.x();
                    fY3 = vec.y();
                    fZ3 = vec.z();
                    f2 = fDot2;
                } else if (fDot2 > f) {
                    fX2 = vec.x();
                    fY2 = vec.y();
                    fZ2 = vec.z();
                    f = fDot2;
                }
                i = i2 + 1;
            } else {
                this.start.set(CompressorColourFit.clamp(fX3, 31.0f, 0.032258064f), CompressorColourFit.clamp(fY3, 63.0f, 0.015873017f), CompressorColourFit.clamp(fZ3, 31.0f, 0.032258064f));
                this.end.set(CompressorColourFit.clamp(fX2, 31.0f, 0.032258064f), CompressorColourFit.clamp(fY2, 63.0f, 0.015873017f), CompressorColourFit.clamp(fZ2, 31.0f, 0.032258064f));
                return;
            }
        }
    }

    @Override // com.rw.texture.squish.CompressorColourFit
    void compress3(byte[] bArr, int i) {
        int count = this.colours.getCount();
        Vec[] points = this.colours.getPoints();
        Vec vec = new Vec();
        this.codes[0].set(this.start);
        this.codes[1].set(this.end);
        this.codes[2].set(this.start).add(this.end).mul(0.5f);
        float f = 0.0f;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= count) {
                break;
            }
            Vec vec2 = points[i3];
            float f2 = Float.MAX_VALUE;
            int i4 = 0;
            int i5 = 0;
            while (true) {
                int i6 = i5;
                if (i6 >= 3) {
                    break;
                }
                Vec vec3 = this.codes[i6];
                vec.set((vec2.x() - vec3.x()) * this.metric.r, (vec2.y() - vec3.y()) * this.metric.g, (vec2.z() - vec3.z()) * this.metric.b);
                float fLengthSQ = vec.lengthSQ();
                if (fLengthSQ < f2) {
                    f2 = fLengthSQ;
                    i4 = i6;
                }
                i5 = i6 + 1;
            }
            this.closest[i3] = i4;
            f += f2;
            i2 = i3 + 1;
        }
        if (f < this.bestError) {
            this.colours.remapIndices(this.closest, this.indices);
            this.colourBlockWriter.writeColourBlock3(this.start, this.end, this.indices, bArr, i);
            this.bestError = f;
        }
    }

    @Override // com.rw.texture.squish.CompressorColourFit
    void compress4(byte[] bArr, int i) {
        int count = this.colours.getCount();
        Vec[] points = this.colours.getPoints();
        Vec vec = new Vec();
        this.codes[0].set(this.start);
        this.codes[1].set(this.end);
        this.codes[2].set(0.6666667f).mul(this.start).add(vec.set(0.33333334f).mul(this.end));
        this.codes[3].set(0.33333334f).mul(this.start).add(vec.set(0.6666667f).mul(this.end));
        float f = 0.0f;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= count) {
                break;
            }
            Vec vec2 = points[i3];
            float f2 = Float.MAX_VALUE;
            int i4 = 0;
            int i5 = 0;
            while (true) {
                int i6 = i5;
                if (i6 >= 4) {
                    break;
                }
                Vec vec3 = this.codes[i6];
                vec.set((vec2.x() - vec3.x()) * this.metric.r, (vec2.y() - vec3.y()) * this.metric.g, (vec2.z() - vec3.z()) * this.metric.b);
                float fLengthSQ = vec.lengthSQ();
                if (fLengthSQ < f2) {
                    f2 = fLengthSQ;
                    i4 = i6;
                }
                i5 = i6 + 1;
            }
            this.closest[i3] = i4;
            f += f2;
            i2 = i3 + 1;
        }
        if (f < this.bestError) {
            this.colours.remapIndices(this.closest, this.indices);
            this.colourBlockWriter.writeColourBlock4(this.start, this.end, this.indices, bArr, i);
            this.bestError = f;
        }
    }
}
