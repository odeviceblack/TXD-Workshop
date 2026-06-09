package com.rw.texture.squish;

import com.rw.texture.squish.Squish;

/* JADX INFO: loaded from: classes.dex */
final class CompressorCluster extends CompressorColourFit {
    private static final int MAX_ITERATIONS = 10;
    private final float[] alpha;
    private final int[] bestIndices;
    private final float[] beta;
    private final ColourBlock colourBlockWriter;
    private final int[] indices;
    private final Squish.CompressionMetric metric;
    private final int[] orders;
    private Vec principle;
    private float totalBestError;
    private final int[] unordered;
    private final float[] weighted;
    private final float[] weights;
    private final Vec xxSum;

    CompressorCluster(ColourSet colourSet, Squish.CompressionType compressionType, Squish.CompressionMetric compressionMetric, ColourBlock colourBlock) {
        super(colourSet, compressionType);
        this.indices = new int[16];
        this.bestIndices = new int[16];
        this.unordered = new int[16];
        this.orders = new int[160];
        this.alpha = new float[16];
        this.beta = new float[16];
        this.weights = new float[16];
        this.weighted = new float[48];
        this.xxSum = new Vec();
        this.metric = compressionMetric;
        this.colourBlockWriter = colourBlock;
    }

    @Override // com.rw.texture.squish.CompressorColourFit
    void init() {
        this.totalBestError = Float.MAX_VALUE;
        this.principle = Matrix.computePrincipleComponent(Matrix.computeWeightedCovariance(this.colours, (Matrix) null));
    }

    @Override // com.rw.texture.squish.CompressorColourFit
    void compress3(byte[] bArr, int i) {
        int count = this.colours.getCount();
        Vec vec = new Vec(0.0f);
        Vec vec2 = new Vec(0.0f);
        float f = this.totalBestError;
        Vec vec3 = new Vec();
        Vec vec4 = new Vec();
        int[] iArrConstructOrderingAndCanonicalCluster = constructOrderingAndCanonicalCluster(this.principle, 0, false);
        int i2 = 0;
        int i3 = 0;
        do {
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 >= count) {
                    break;
                }
                this.indices[i5] = 0;
                this.alpha[i5] = this.weights[i5];
                this.beta[i5] = 0.0f;
                i4 = i5 + 1;
            }
            for (int i6 = -iArrConstructOrderingAndCanonicalCluster[1]; i6 <= iArrConstructOrderingAndCanonicalCluster[0]; i6++) {
                int i7 = iArrConstructOrderingAndCanonicalCluster[0] - i6;
                int i8 = i7;
                while (true) {
                    int i9 = i8;
                    if (i9 >= count) {
                        break;
                    }
                    this.indices[i9] = 2;
                    float[] fArr = this.alpha;
                    float[] fArr2 = this.beta;
                    float f2 = 0.5f * this.weights[i9];
                    fArr2[i9] = f2;
                    fArr[i9] = f2;
                    i8 = i9 + 1;
                }
                for (int i10 = -iArrConstructOrderingAndCanonicalCluster[2]; i10 <= iArrConstructOrderingAndCanonicalCluster[1] && i10 <= iArrConstructOrderingAndCanonicalCluster[1] + i6; i10++) {
                    int i11 = ((i7 + iArrConstructOrderingAndCanonicalCluster[1]) + i6) - i10;
                    if (i11 < count) {
                        this.indices[i11] = 1;
                        this.alpha[i11] = 0.0f;
                        this.beta[i11] = this.weights[i11];
                    }
                    float fSolveLeastSquares = solveLeastSquares(vec3, vec4);
                    if (fSolveLeastSquares < f) {
                        vec.set(vec3);
                        vec2.set(vec4);
                        System.arraycopy(this.indices, 0, this.bestIndices, 0, 16);
                        f = fSolveLeastSquares;
                        i2 = i3;
                    }
                }
            }
            if (i2 != i3) {
                break;
            }
            int i12 = i3 + 1;
            i3 = i12;
            if (i12 == MAX_ITERATIONS) {
                break;
            } else {
                iArrConstructOrderingAndCanonicalCluster = constructOrderingAndCanonicalCluster(vec3.set(vec2).sub(vec), i3, false);
            }
        } while (iArrConstructOrderingAndCanonicalCluster != null);
        if (f >= this.totalBestError) {
            return;
        }
        int i13 = 16 * i2;
        int i14 = 0;
        while (true) {
            int i15 = i14;
            if (i15 >= count) {
                this.colours.remapIndices(this.unordered, this.bestIndices);
                this.colourBlockWriter.writeColourBlock3(vec, vec2, this.bestIndices, bArr, i);
                this.totalBestError = f;
                return;
            }
            this.unordered[this.orders[i13 + i15]] = this.bestIndices[i15];
            i14 = i15 + 1;
        }
    }

    @Override // com.rw.texture.squish.CompressorColourFit
    void compress4(byte[] bArr, int i) {
        int count = this.colours.getCount();
        Vec vec = new Vec(0.0f);
        Vec vec2 = new Vec(0.0f);
        float f = this.totalBestError;
        Vec vec3 = new Vec();
        Vec vec4 = new Vec();
        int[] iArrConstructOrderingAndCanonicalCluster = constructOrderingAndCanonicalCluster(this.principle, 0, true);
        int i2 = 0;
        int i3 = 0;
        do {
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 >= count) {
                    break;
                }
                this.indices[i5] = 0;
                this.alpha[i5] = this.weights[i5];
                this.beta[i5] = 0.0f;
                i4 = i5 + 1;
            }
            for (int i6 = -iArrConstructOrderingAndCanonicalCluster[1]; i6 <= iArrConstructOrderingAndCanonicalCluster[0]; i6++) {
                int i7 = iArrConstructOrderingAndCanonicalCluster[0] - i6;
                int i8 = i7;
                while (true) {
                    int i9 = i8;
                    if (i9 >= count) {
                        break;
                    }
                    this.indices[i9] = 2;
                    this.alpha[i9] = 0.6666667f * this.weights[i9];
                    this.beta[i9] = 0.33333334f * this.weights[i9];
                    i8 = i9 + 1;
                }
                for (int i10 = -iArrConstructOrderingAndCanonicalCluster[2]; i10 <= iArrConstructOrderingAndCanonicalCluster[1] && i10 <= iArrConstructOrderingAndCanonicalCluster[1] + i6; i10++) {
                    int i11 = ((i7 + iArrConstructOrderingAndCanonicalCluster[1]) + i6) - i10;
                    int i12 = i11;
                    while (true) {
                        int i13 = i12;
                        if (i13 >= count) {
                            break;
                        }
                        this.indices[i13] = 3;
                        this.alpha[i13] = 0.33333334f * this.weights[i13];
                        this.beta[i13] = 0.6666667f * this.weights[i13];
                        i12 = i13 + 1;
                    }
                    for (int i14 = -iArrConstructOrderingAndCanonicalCluster[3]; i14 <= iArrConstructOrderingAndCanonicalCluster[2] && i14 <= iArrConstructOrderingAndCanonicalCluster[2] + i10; i14++) {
                        int i15 = ((i11 + iArrConstructOrderingAndCanonicalCluster[2]) + i10) - i14;
                        if (i15 < count) {
                            this.indices[i15] = 1;
                            this.alpha[i15] = 0.0f;
                            this.beta[i15] = this.weights[i15];
                        }
                        float fSolveLeastSquares = solveLeastSquares(vec3, vec4);
                        if (fSolveLeastSquares < f) {
                            vec.set(vec3);
                            vec2.set(vec4);
                            System.arraycopy(this.indices, 0, this.bestIndices, 0, 16);
                            f = fSolveLeastSquares;
                            i2 = i3;
                        }
                    }
                }
            }
            if (i2 != i3) {
                break;
            }
            i3++;
            if (i3 == MAX_ITERATIONS) {
                break;
            } else {
                iArrConstructOrderingAndCanonicalCluster = constructOrderingAndCanonicalCluster(vec3.set(vec2).sub(vec), i3, true);
            }
        } while (iArrConstructOrderingAndCanonicalCluster != null);
        if (f >= this.totalBestError) {
            return;
        }
        int i16 = 16 * i2;
        int i17 = 0;
        while (true) {
            int i18 = i17;
            if (i18 >= count) {
                this.colours.remapIndices(this.unordered, this.bestIndices);
                this.colourBlockWriter.writeColourBlock4(vec, vec2, this.bestIndices, bArr, i);
                this.totalBestError = f;
                return;
            }
            this.unordered[this.orders[i16 + i18]] = this.bestIndices[i18];
            i17 = i18 + 1;
        }
    }

    private int[] constructOrderingAndCanonicalCluster(Vec vec, int i, boolean z) {
        int count = this.colours.getCount();
        Vec[] points = this.colours.getPoints();
        float[] fArr = new float[16];
        int i2 = 16 * i;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= count) {
                break;
            }
            fArr[i4] = points[i4].dot(vec);
            this.orders[i2 + i4] = i4;
            i3 = i4 + 1;
        }
        int i5 = 0;
        while (true) {
            int i6 = i5;
            if (i6 >= count) {
                break;
            }
            int i7 = i6;
            while (true) {
                int i8 = i7;
                if (i8 <= 0 || fArr[i8] >= fArr[i8 - 1]) {
                    break;
                }
                float f = fArr[i8];
                fArr[i8] = fArr[i8 - 1];
                fArr[i8 - 1] = f;
                int i9 = this.orders[i2 + i8];
                this.orders[i2 + i8] = this.orders[(i2 + i8) - 1];
                this.orders[(i2 + i8) - 1] = i9;
                i7 = i8 - 1;
            }
            i5 = i6 + 1;
        }
        int i10 = 0;
        while (true) {
            int i11 = i10;
            if (i11 < i) {
                int i12 = 16 * i11;
                boolean z2 = true;
                int i13 = 0;
                while (true) {
                    int i14 = i13;
                    if (i14 >= count) {
                        break;
                    }
                    if (this.orders[i2 + i14] == this.orders[i12 + i14]) {
                        i13 = i14 + 1;
                    } else {
                        z2 = false;
                        break;
                    }
                }
                if (!z2) {
                    i10 = i11 + 1;
                } else {
                    return (int[]) null;
                }
            } else {
                Vec[] points2 = this.colours.getPoints();
                float[] weights = this.colours.getWeights();
                this.xxSum.set(0.0f);
                int i15 = 0;
                int i16 = 0;
                while (true) {
                    int i17 = i16;
                    if (i15 < count) {
                        int i18 = this.orders[i2 + i15];
                        float f2 = weights[i18];
                        Vec vec2 = points2[i18];
                        this.weights[i15] = f2;
                        float fX = f2 * vec2.x();
                        float fY = f2 * vec2.y();
                        float fZ = f2 * vec2.z();
                        this.xxSum.add(fX * fX, fY * fY, fZ * fZ);
                        this.weighted[i17 + 0] = fX;
                        this.weighted[i17 + 1] = fY;
                        this.weighted[i17 + 2] = fZ;
                        i15++;
                        i16 = i17 + 3;
                    } else {
                        return canonicalCluster(fArr, count, z);
                    }
                }
            }
        }
    }

    private static int[] canonicalCluster(float[] fArr, int i, boolean z) {
        int[] iArr = new int[z ? 4 : 3];
        if (i == 0) {
            return iArr;
        }
        float f = fArr[0];
        float f2 = fArr[i - 1];
        float[] fArr2 = z ? new float[]{((3 * f) + f2) / 4, (f + f2) / 2, (f + (3 * f2)) / 4, f2} : new float[]{((2 * f) + f2) / 3, (f + (2 * f2)) / 3, f2};
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            while (fArr[i3] > fArr2[i2]) {
                i2++;
            }
            iArr[i2] = iArr[i2] + 1;
        }
        return iArr;
    }

    private float solveLeastSquares(Vec vec, Vec vec2) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        int count = this.colours.getCount();
        float f7 = 0.0f;
        float f8 = 0.0f;
        float f9 = 0.0f;
        float f10 = 0.0f;
        float f11 = 0.0f;
        float f12 = 0.0f;
        float f13 = 0.0f;
        float f14 = 0.0f;
        float f15 = 0.0f;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i >= count) {
                break;
            }
            float f16 = this.alpha[i];
            float f17 = this.beta[i];
            f7 += f16 * f16;
            f8 += f17 * f17;
            f9 += f16 * f17;
            f10 += this.weighted[i3 + 0] * f16;
            f11 += this.weighted[i3 + 1] * f16;
            f12 += this.weighted[i3 + 2] * f16;
            f13 += this.weighted[i3 + 0] * f17;
            f14 += this.weighted[i3 + 1] * f17;
            f15 += this.weighted[i3 + 2] * f17;
            i++;
            i2 = i3 + 3;
        }
        if (f8 == 0.0f) {
            float f18 = 1.0f / f7;
            f = f10 * f18;
            f2 = f11 * f18;
            f3 = f12 * f18;
            f6 = 0.0f;
            f5 = 0.0f;
            f4 = 0.0f;
        } else if (f7 == 0.0f) {
            float f19 = 1.0f / f8;
            f3 = 0.0f;
            f2 = 0.0f;
            f = 0.0f;
            f4 = f13 * f19;
            f5 = f14 * f19;
            f6 = f15 * f19;
        } else {
            float f20 = 1.0f / ((f7 * f8) - (f9 * f9));
            if (f20 == Float.POSITIVE_INFINITY) {
                return Float.MAX_VALUE;
            }
            f = ((f10 * f8) - (f13 * f9)) * f20;
            f2 = ((f11 * f8) - (f14 * f9)) * f20;
            f3 = ((f12 * f8) - (f15 * f9)) * f20;
            f4 = ((f13 * f7) - (f10 * f9)) * f20;
            f5 = ((f14 * f7) - (f11 * f9)) * f20;
            f6 = ((f15 * f7) - (f12 * f9)) * f20;
        }
        float fClamp = CompressorColourFit.clamp(f, 31.0f, 0.032258064f);
        float fClamp2 = CompressorColourFit.clamp(f2, 63.0f, 0.015873017f);
        float fClamp3 = CompressorColourFit.clamp(f3, 31.0f, 0.032258064f);
        vec.set(fClamp, fClamp2, fClamp3);
        float fClamp4 = CompressorColourFit.clamp(f4, 31.0f, 0.032258064f);
        float fClamp5 = CompressorColourFit.clamp(f5, 63.0f, 0.015873017f);
        float fClamp6 = CompressorColourFit.clamp(f6, 31.0f, 0.032258064f);
        vec2.set(fClamp4, fClamp5, fClamp6);
        return this.metric.dot((fClamp * fClamp * f7) + (fClamp4 * fClamp4 * f8) + this.xxSum.x() + (2.0f * ((((fClamp * fClamp4) * f9) - (fClamp * f10)) - (fClamp4 * f13))), (fClamp2 * fClamp2 * f7) + (fClamp5 * fClamp5 * f8) + this.xxSum.y() + (2.0f * ((((fClamp2 * fClamp5) * f9) - (fClamp2 * f11)) - (fClamp5 * f14))), (fClamp3 * fClamp3 * f7) + (fClamp6 * fClamp6 * f8) + this.xxSum.z() + (2.0f * ((((fClamp3 * fClamp6) * f9) - (fClamp3 * f12)) - (fClamp6 * f15))));
    }
}
