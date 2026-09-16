package com.app.graphics;

public class Sprite {
    private final String name;
    private final int[] pixels;
    private final int width;
    private final int height;

    public Sprite(String name, int[] pixels, int width, int height) {
        this.name = name;
        this.pixels = pixels.clone();
        this.width = width;
        this.height = height;
    }

    public String getName() { return name; }
    public int[] getPixels() { return pixels; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getPixel(int x, int y) { return pixels[y * width + x]; }

    // Применяет цвет к пикселям (заменяет RGB, сохраняет альфа-канал)
    public int[] tinted(int targetColor) {
        int[] tinted = new int[pixels.length];

        int targetR = (targetColor >> 16) & 0xFF;
        int targetG = (targetColor >> 8) & 0xFF;
        int targetB = targetColor & 0xFF;

        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int alpha = (pixel >> 24) & 0xFF;

            if (alpha > 0 && pixel != 0xFF000000) {  // Не чёрный и не прозрачный
                // Получаем яркость исходного пикселя
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // Усредняем яркость или просто проверяем что пиксель не чёрный
                int brightness = (r + g + b) / 3;

                // Применяем целевой цвет с сохранением яркости
                int newR = targetR * brightness / 255;
                int newG = targetG * brightness / 255;
                int newB = targetB * brightness / 255;

                tinted[i] = (alpha << 24) | (newR << 16) | (newG << 8) | newB;
            } else {
                tinted[i] = pixel;  // Прозрачный или чёрный оставляем как есть
            }
        }
        return tinted;
    }
}