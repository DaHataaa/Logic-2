package com.app.graphics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.nio.file.*;

public class ColorConfig {
    @SerializedName("author")
    private String author = "Unknown";

    @SerializedName("background")
    private String background = "#FFFFFF";

    @SerializedName("grid")
    private String grid = "#000000";

    @SerializedName("signal_on")
    private String signalOn = "#00FF00";

    @SerializedName("arrow")
    private String arrow = "#FFAA00";

    @SerializedName("getter")
    private String getter = "#FFAA00";

    @SerializedName("bridge")
    private String bridge = "#44AAFF";

    @SerializedName("peg")
    private String peg = "#44AAFF";

    @SerializedName("not")
    private String not = "#FF4444";

    @SerializedName("or")
    private String or = "#FF44FF";

    @SerializedName("and")
    private String and = "#44FF44";

    @SerializedName("xor")
    private String xor = "#FFFF44";

    @SerializedName("power")
    private String power = "#FF6600";

    public static ColorConfig load(String texpackName) {
        String path = "data/texturepacks/" + texpackName + "/colors.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Path filePath = Paths.get(path);
            if (!Files.exists(filePath)) {
                return new ColorConfig();
            }
            try (FileReader reader = new FileReader(path)) {
                ColorConfig config = gson.fromJson(reader, ColorConfig.class);
                if (config.getAuthor() == null || config.getAuthor().isEmpty()) {
                    config.author = "Unknown";
                }
                return config;
            }
        } catch (IOException e) {
            return new ColorConfig();
        }
    }

    public void save(String texpackName) {
        String path = "data/texturepacks/" + texpackName + "/colors.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Path parent = Paths.get(path).getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (FileWriter writer = new FileWriter(path)) {
                gson.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save color config: " + e.getMessage());
        }
    }

    // Getters
    public String getAuthor() { return author; }
    public String getBackground() { return background; }
    public String getGrid() { return grid; }
    public String getSignalOn() { return signalOn; }
    public String getArrow() { return arrow; }
    public String getGetter() { return getter; }
    public String getBridge() { return bridge; }
    public String getPeg() { return peg; }
    public String getNot() { return not; }
    public String getOr() { return or; }
    public String getAnd() { return and; }
    public String getXor() { return xor; }
    public String getPower() { return power; }

    public String getColorForBlock(String blockName) {
        switch (blockName) {
            case "arrow": return arrow;
            case "getter": return getter;
            case "bridge": return bridge;
            case "peg": return peg;
            case "not": return not;
            case "or": return or;
            case "and": return and;
            case "xor": return xor;
            case "power": return power;
            default: return not;
        }
    }
}