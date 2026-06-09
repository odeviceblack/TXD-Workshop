package com.rw.texture.squish;

import java.util.Arrays;
import min3d.core.Color4BufferList;

/* JADX INFO: loaded from: classes.dex */
final class Matrix {
    private static final float FLT_EPSILON = 1.0E-5f;
    private float[] values;

    Matrix() {
        this.values = new float[6];
    }

    Matrix(float f) {
        this.values = new float[6];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 6) {
                return;
            }
            this.values[i2] = f;
            i = i2 + 1;
        }
    }

    float get(int i) {
        return this.values[i];
    }

    static Matrix computeWeightedCovariance(ColourSet colourSet, Matrix matrix) {
        Matrix matrix2 = matrix;
        int count = colourSet.getCount();
        Vec[] points = colourSet.getPoints();
        float[] weights = colourSet.getWeights();
        Vec vec = new Vec();
        Vec vec2 = new Vec();
        Vec vec3 = new Vec();
        float f = 0.0f;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= count) {
                break;
            }
            f += weights[i2];
            vec.add(vec2.set(points[i2]).mul(weights[i2]));
            i = i2 + 1;
        }
        vec.div(f);
        if (matrix2 == null) {
            matrix2 = new Matrix();
        } else {
            Arrays.fill(matrix2.values, 0.0f);
        }
        float[] fArr = matrix2.values;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < count) {
                vec2.set(points[i4]).sub(vec);
                vec3.set(vec2).mul(weights[i4]);
                fArr[0] = fArr[0] + (vec2.x() * vec3.x());
                fArr[1] = fArr[1] + (vec2.x() * vec3.y());
                fArr[2] = fArr[2] + (vec2.x() * vec3.z());
                fArr[3] = fArr[3] + (vec2.y() * vec3.y());
                fArr[4] = fArr[4] + (vec2.y() * vec3.z());
                fArr[5] = fArr[5] + (vec2.z() * vec3.z());
                i3 = i4 + 1;
            } else {
                return matrix2;
            }
        }
    }

    private static Vec getMultiplicity1Evector(Matrix matrix, float f) {
        float[] fArr = matrix.values;
        float[] fArr2 = {fArr[0] - f, fArr[1], fArr[2], fArr[3] - f, fArr[4], fArr[5] - f};
        float[] fArr3 = {(fArr2[3] * fArr2[5]) - (fArr2[4] * fArr2[4]), (fArr2[2] * fArr2[4]) - (fArr2[1] * fArr2[5]), (fArr2[1] * fArr2[4]) - (fArr2[2] * fArr2[3]), (fArr2[0] * fArr2[5]) - (fArr2[2] * fArr2[2]), (fArr2[1] * fArr2[2]) - (fArr2[4] * fArr2[0]), (fArr2[0] * fArr2[3]) - (fArr2[1] * fArr2[1])};
        float fAbs = Math.abs(fArr3[0]);
        int i = 0;
        int i2 = 1;
        while (true) {
            int i3 = i2;
            if (i3 >= 6) {
                break;
            }
            float fAbs2 = Math.abs(fArr3[i3]);
            if (fAbs2 > fAbs) {
                fAbs = fAbs2;
                i = i3;
            }
            i2 = i3 + 1;
        }
        switch (i) {
            case 0:
                return new Vec(fArr3[0], fArr3[1], fArr3[2]);
            case Color4BufferList.BYTES_PER_PROPERTY /* 1 */:
            case 3:
                return new Vec(fArr3[1], fArr3[3], fArr3[4]);
            case 2:
            default:
                return new Vec(fArr3[2], fArr3[4], fArr3[5]);
        }
    }

    private static Vec getMultiplicity2Evector(Matrix matrix, float f) {
        float[] fArr = matrix.values;
        float[] fArr2 = {fArr[0] - f, fArr[1], fArr[2], fArr[3] - f, fArr[4], fArr[5] - f};
        float fAbs = Math.abs(fArr2[0]);
        int i = 0;
        int i2 = 1;
        while (true) {
            int i3 = i2;
            if (i3 >= 6) {
                break;
            }
            float fAbs2 = Math.abs(fArr2[i3]);
            if (fAbs2 > fAbs) {
                fAbs = fAbs2;
                i = i3;
            }
            i2 = i3 + 1;
        }
        switch (i) {
            case 0:
            case Color4BufferList.BYTES_PER_PROPERTY /* 1 */:
                return new Vec(-fArr2[1], fArr2[0], 0.0f);
            case 2:
                return new Vec(fArr2[2], 0.0f, -fArr2[0]);
            case 3:
            case 4:
                return new Vec(0.0f, -fArr2[4], fArr2[3]);
            default:
                return new Vec(0.0f, -fArr2[5], fArr2[4]);
        }
    }

    static Vec computePrincipleComponent(Matrix matrix) {
        float fPow;
        float[] fArr = matrix.values;
        float f = (((((fArr[0] * fArr[3]) * fArr[5]) + (((2.0f * fArr[1]) * fArr[2]) * fArr[4])) - ((fArr[0] * fArr[4]) * fArr[4])) - ((fArr[3] * fArr[2]) * fArr[2])) - ((fArr[5] * fArr[1]) * fArr[1]);
        float f2 = (((((fArr[0] * fArr[3]) + (fArr[0] * fArr[5])) + (fArr[3] * fArr[5])) - (fArr[1] * fArr[1])) - (fArr[2] * fArr[2])) - (fArr[4] * fArr[4]);
        float f3 = fArr[0] + fArr[3] + fArr[5];
        float f4 = f2 - ((0.33333334f * f3) * f3);
        float f5 = (((((-0.074074075f) * f3) * f3) * f3) + ((0.33333334f * f2) * f3)) - f;
        float f6 = (0.25f * f5 * f5) + (0.037037037f * f4 * f4 * f4);
        if (FLT_EPSILON < f6) {
            return new Vec(1.0f);
        }
        if (f6 < -1.0E-5f) {
            float fAtan2 = (float) Math.atan2(Math.sqrt(-f6), (-0.5f) * f5);
            float fPow2 = (float) Math.pow((float) Math.sqrt(((0.25f * f5) * f5) - f6), 0.33333334f);
            float fCos = (float) Math.cos(fAtan2 / 3.0f);
            float fSin = (float) Math.sin(fAtan2 / 3.0f);
            float f7 = (0.33333334f * f3) + (2.0f * fPow2 * fCos);
            float fSqrt = (0.33333334f * f3) - (fPow2 * (fCos + (((float) Math.sqrt(3.0f)) * fSin)));
            float fSqrt2 = (0.33333334f * f3) - (fPow2 * (fCos - (((float) Math.sqrt(3.0f)) * fSin)));
            if (Math.abs(fSqrt) > Math.abs(f7)) {
                f7 = fSqrt;
            }
            if (Math.abs(fSqrt2) > Math.abs(f7)) {
                f7 = fSqrt2;
            }
            return getMultiplicity1Evector(matrix, f7);
        }
        if (f5 < 0.0f) {
            fPow = (float) (-Math.pow((-0.5f) * f5, 0.33333334f));
        } else {
            fPow = (float) Math.pow(0.5f * f5, 0.33333334f);
        }
        float f8 = (0.33333334f * f3) + fPow;
        float f9 = (0.33333334f * f3) - (2.0f * fPow);
        if (Math.abs(f8) > Math.abs(f9)) {
            return getMultiplicity2Evector(matrix, f8);
        }
        return getMultiplicity1Evector(matrix, f9);
    }
}
