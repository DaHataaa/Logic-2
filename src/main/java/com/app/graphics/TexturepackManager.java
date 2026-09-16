package com.app.graphics;

import com.app.Config;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TexturepackManager {
    private static final String TEXTUREPACKS_DIR = "data/texturepacks";
    private String currentTexturepack;
    private ColorConfig colors;

    public TexturepackManager() {
        loadTexturepack(Config.getInstance().getCurrentTexturepack());
    }


    public void loadTexturepack(String name) {
        this.currentTexturepack = name;
        this.colors = ColorConfig.load(name);
        Config.getInstance().setCurrentTexturepack(name);
        Config.getInstance().save();
        System.out.println("Loaded texturepack: " + name);
    }

    public List<String> getAvailableTexturepacks() {
        List<String> texpacks = new ArrayList<>();
        try {
            Files.list(Paths.get(TEXTUREPACKS_DIR))
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
        return TEXTUREPACKS_DIR + "/" + currentTexturepack + "/" + spriteName + ".png";
    }
}