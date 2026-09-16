package com.app.graphics;

import com.app.Config;
import java.util.HashMap;
import java.util.Map;

public class SpriteCache {
    private final Map<String, Map<Integer, Map<Boolean, int[]>>> cache = new HashMap<>();
    private final int baseSize = 16;
    private final int minSize;
    private final int maxSize;
    private final ColorConfig colors;

    public SpriteCache(Map<String, Sprite> originalSprites, ColorConfig colors) {
        this.colors = colors;
        Config config = Config.getInstance();
        this.minSize = config.getCellSizeMin();
        this.maxSize = config.getCellSizeMax();

        for (Map.Entry<String, Sprite> entry : originalSprites.entrySet()) {
            String name = entry.getKey();
            Sprite original = entry.getValue();
            Map<Integer, Map<Boolean, int[]>> sizeMap = new HashMap<>();

            int blockColor = parseColor(colors.getColorForBlock(name));
            int signalColor = parseColor(colors.getSignalOn());

            for (int size = minSize; size <= maxSize; size++) {
                Map<Boolean, int[]> signalMap = new HashMap<>();

                int[] scaled;
                if (size == baseSize) {
                    scaled = original.getPixels();
                } else {
                    scaled = SpriteScaler.scaleNearest(original.getPixels(), baseSize, size);
                }

                // БЕЗ СИГНАЛА: чёрный → цвет блока, белый → прозрачный
                signalMap.put(false, applyTint(scaled, blockColor));
                // С СИГНАЛОМ: чёрный → цвет сигнала, белый → прозрачный
                signalMap.put(true, applyTint(scaled, signalColor));

                sizeMap.put(size, signalMap);
            }
            cache.put(name, sizeMap);
        }
    }

    private int[] applyTint(int[] pixels, int targetColor) {
        int[] result = new int[pixels.length];
        int targetR = (targetColor >> 16) & 0xFF;
        int targetG = (targetColor >> 8) & 0xFF;
        int targetB = targetColor & 0xFF;

        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int alpha = (pixel >> 24) & 0xFF;

            if (alpha == 0) {
                result[i] = 0x00000000;
                continue;
            }

            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = pixel & 0xFF;

            // ЧЁРНЫЙ (0,0,0) → заменяем на целевой цвет
            if (r == 0 && g == 0 && b == 0) {
                result[i] = (alpha << 24) | (targetR << 16) | (targetG << 8) | targetB;
            }
            // ВСЕ ОСТАЛЬНЫЕ ЦВЕТА (включая белый) → прозрачные
            else {
                result[i] = 0x00000000;
            }
        }
        return result;
    }

    private int parseColor(String hex) {
        if (hex == null) return 0xFFFFFFFF;
        if (hex.startsWith("#")) hex = hex.substring(1);
        if (hex.length() == 6) {
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return (255 << 24) | (r << 16) | (g << 8) | b;
        }
        return 0xFFFFFFFF;
    }

    public int[] getSprite(String name, int targetSize, boolean signalOn) {
        Map<Integer, Map<Boolean, int[]>> sizeMap = cache.get(name);
        if (sizeMap == null) return null;

        Map<Boolean, int[]> signalMap = sizeMap.get(targetSize);
        if (signalMap == null) {
            int closest = -1;
            int minDiff = Integer.MAX_VALUE;
            for (int size : sizeMap.keySet()) {
                int diff = Math.abs(size - targetSize);
                if (diff < minDiff) {
                    minDiff = diff;
                    closest = size;
                }
            }
            signalMap = sizeMap.get(closest);
        }

        if (signalMap == null) return null;
        return signalMap.get(signalOn);
    }

    public int getBaseSize() {
        return baseSize;
    }
}