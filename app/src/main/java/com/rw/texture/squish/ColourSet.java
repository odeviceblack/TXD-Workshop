package com.rw.texture.squish;

import com.rw.texture.squish.Squish.CompressionType;

final class ColourSet {

    private int count;
    private boolean transparent;

    private final Vec[] points = new Vec[16];
    private final float[] weights = new float[16];
    private final int[] remap = new int[16];

    ColourSet() {
        for (int i = 0; i < 16; i++) {
            points[i] = new Vec();
        }
    }

    void init(byte[] rgba, int mask,
              CompressionType compressionType,
              boolean weightAlpha) {

        boolean dxt1 = compressionType == CompressionType.DXT1;

        count = 0;
        transparent = false;

        for (int pixel = 0; pixel < 16; pixel++) {

            if ((mask & (1 << pixel)) == 0) {
                remap[pixel] = -1;
                continue;
            }

            int alpha = rgba[(pixel * 4) + 3] & 0xFF;

            if (dxt1 && alpha < 128) {
                remap[pixel] = -1;
                transparent = true;
                continue;
            }

            boolean found = false;

            for (int previous = 0; previous < pixel; previous++) {

                if ((mask & (1 << previous)) == 0) {
                    continue;
                }

                if (rgba[pixel * 4] != rgba[previous * 4]) {
                    continue;
                }

                if (rgba[(pixel * 4) + 1] != rgba[(previous * 4) + 1]) {
                    continue;
                }

                if (rgba[(pixel * 4) + 2] != rgba[(previous * 4) + 2]) {
                    continue;
                }

                if (dxt1 &&
                        ((rgba[(previous * 4) + 3] & 0xFF) < 128)) {
                    continue;
                }

                int index = remap[previous];

                weights[index] += weightAlpha
                        ? ((alpha + 1) / 256.0f)
                        : 1.0f;

                remap[pixel] = index;
                found = true;
                break;
            }

            if (found) {
                continue;
            }

            if (count >= 16) {
                throw new IllegalStateException(
                        "ColourSet overflow: " + count);
            }

            points[count].set(
                    (rgba[pixel * 4] & 0xFF) / 255.0f,
                    (rgba[(pixel * 4) + 1] & 0xFF) / 255.0f,
                    (rgba[(pixel * 4) + 2] & 0xFF) / 255.0f
            );

            weights[count] = weightAlpha
                    ? ((alpha + 1) / 256.0f)
                    : 1.0f;

            remap[pixel] = count;
            count++;
        }
    }

    int getCount() {
        return count;
    }

    Vec[] getPoints() {
        return points;
    }

    float[] getWeights() {
        return weights;
    }

    boolean isTransparent() {
        return transparent;
    }

    void remapIndices(int[] source, int[] destination) {

        for (int i = 0; i < 16; i++) {

            int index = remap[i];

            if (index == -1) {
                destination[i] = 3;
            } else {
                destination[i] = source[index];
            }
        }
    }
}