package com.app.graphics;

public class SpriteScaler {

    public static int[] scaleNearest(int[] src, int srcSize, int dstSize) {
        if (src == null) {
            System.err.println("SpriteScaler: src is null");
            return createPlaceholder(dstSize);
        }

        if (srcSize == dstSize) {
            return src.clone();
        }

        int[] dst = new int[dstSize * dstSize];
        float ratio = (float) srcSize / dstSize;

        for (int y = 0; y < dstSize; y++) {
            int srcY = (int) (y * ratio);
            int srcRowStart = srcY * srcSize;
            int dstRowStart = y * dstSize;

            for (int x = 0; x < dstSize; x++) {
                int srcX = (int) (x * ratio);
                dst[dstRowStart + x] = src[srcRowStart + srcX];
            }
        }

        return dst;
    }

    private static int[] createPlaceholder(int size) {
        int[] pixels = new int[size * size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                boolean isEven = (x / 4 + y / 4) % 2 == 0;
                pixels[y * size + x] = isEven ? 0xFFFF00FF : 0xFF000000;
            }
        }
        return pixels;
    }
}