package com.app.graphics;

import com.app.Config;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TexturepackManager {
    private static final Path TEXTUREPACKS_DIR = Config.getDataDir().resolve("texturepacks");

    private String currentTexturepack;
    private ColorConfig colors;

    public TexturepackManager() {
        loadTexturepack(Config.getInstance().getCurrentTexturepack());
    }

    public void loadTexturepack(String name) {
        // Если папки нет — создаём, чтобы не падало при старте
        Path packDir = TEXTUREPACKS_DIR.resolve(name);
        if (!Files.exists(packDir)) {
            try {
                Files.createDirectories(packDir);
            } catch (IOException e) {
                System.err.println("Failed to create texpack dir: " + e.getMessage());
            }
        }

        this.currentTexturepack = name;
        this.colors = ColorConfig.load(name);
        Config.getInstance().setCurrentTexturepack(name);
        Config.getInstance().save();
        System.out.println("Loaded texturepack: " + name);
    }

    public List<String> getAvailableTexturepacks() {
        List<String> texpacks = new ArrayList<>();

        try {
            if (!Files.exists(TEXTUREPACKS_DIR)) {
                Files.createDirectories(TEXTUREPACKS_DIR);
            }
            Files.list(TEXTUREPACKS_DIR)
                    .filter(Files::isDirectory)
                    .forEach(p -> texpacks.add(p.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Failed to list texturepacks: " + e.getMessage());
        }

        return texpacks;
    }

    public String getCurrentTexturepack() {
        return currentTexturepack;
    }

    public ColorConfig getColors() {
        return colors;
    }

    public String getSpritePath(String spriteName) {
        return TEXTUREPACKS_DIR
                .resolve(currentTexturepack)
                .resolve(spriteName + ".png")
                .toString();
    }
}