package com.app.tools;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

public class PNGSpriteConverter {

    public static void convertToJavaClass(String pngPath, String className, String outputDir) throws IOException {
        Image image = new Image("file:" + pngPath);
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();

        StringBuilder sb = new StringBuilder();
        sb.append("package com.app.generated;\n\n");
        sb.append("public class ").append(className).append(" {\n");
        sb.append("    public static final int WIDTH = ").append(width).append(";\n");
        sb.append("    public static final int HEIGHT = ").append(height).append(";\n");
        sb.append("    public static final int[] PIXELS = {\n");

        for (int y = 0; y < height; y++) {
            sb.append("        ");
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);
                sb.append(String.format("0x%08X", argb));
                if (x < width - 1) sb.append(", ");
            }
            if (y < height - 1) sb.append(",\n");
            else sb.append("\n");
        }

        sb.append("    };\n");
        sb.append("}\n");

        Path outputPath = Paths.get(outputDir, className + ".java");
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, sb.toString());

        System.out.println("Generated: " + outputPath);
    }

    public static void main(String[] args) throws IOException {
        // Пример: конвертация одного спрайта
        convertToJavaClass("data/gfx/arrow.png", "ArrowSprite", "src/main/java/com/app/generated");
    }
}