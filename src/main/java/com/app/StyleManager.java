package com.app;

import com.app.graphics.SpriteManager;
import com.app.graphics.ColorConfig;
import java.io.*;
import java.nio.file.*;

public class StyleManager {
    private static final String CSS_PATH = "data/styles.css";

    private static final String CSS_TEMPLATE =
            ".button {\n" +
                    "    -fx-background-color: %s;\n" +
                    "    -fx-text-fill: %s;\n" +
                    "    -fx-font-size: 24px;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-padding: 3 15;\n" +
                    "    -fx-background-radius: 5;\n" +
                    "    -fx-cursor: hand;\n" +
                    "    -fx-min-width: 250;\n" +
                    "    -fx-min-height: 42;\n" +
                    "    -fx-pref-height: 42;\n" +
                    "}\n" +
                    "\n" +
                    ".button:hover {\n" +
                    "    -fx-background-color: %s;\n" +
                    "    -fx-text-fill: %s;\n" +
                    "}\n" +
                    "\n" +
                    ".button:pressed {\n" +
                    "    -fx-background-color: %s;\n" +
                    "    -fx-text-fill: %s;\n" +
                    "}\n" +
                    "\n" +
                    ".map-row {\n" +
                    "    -fx-background-color: %s;\n" +
                    "    -fx-background-radius: 5;\n" +
                    "    -fx-padding: 8 15;\n" +
                    "    -fx-min-height: 50;\n" +
                    "    -fx-pref-height: 50;\n" +
                    "}\n" +
                    "\n" +
                    ".map-row .map-name {\n" +
                    "    -fx-font-size: 24px;\n" +
                    "    -fx-fill: %s;\n" +
                    "}\n" +
                    "\n" +
                    ".map-row-hover {\n" +
                    "    -fx-background-color: %s;\n" +
                    "}\n" +
                    "\n" +
                    ".empty-text {\n" +
                    "    -fx-fill: %s;\n" +
                    "    -fx-font-size: 24px;\n" +
                    "    -fx-font-style: italic;\n" +
                    "}\n" +
                    "\n" +
                    ".menu-title {\n" +
                    "    -fx-fill: %s;\n" +
                    "    -fx-font-size: 32px;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "}\n" +
                    "\n" +
                    ".icon-button {\n" +
                    "    -fx-text-fill: %s;\n" +
                    "    -fx-font-size: 24px;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-padding: 5 10;\n" +
                    "    -fx-background-radius: 3;\n" +
                    "    -fx-cursor: hand;\n" +
                    "    -fx-min-width: 50;\n" +
                    "    -fx-min-height: 42;\n" +
                    "    -fx-pref-height: 42;\n" +
                    "}\n" +
                    "\n" +
                    ".icon-button:hover {\n" +
                    "    -fx-background-color: %s;\n" +
                    "    -fx-text-fill: %s;\n" +
                    "}\n" +
                    "\n" +
                    ".icon-button:pressed {\n" +
                    "    -fx-background-color: %s;\n" +
                    "    -fx-text-fill: %s;\n" +
                    "}\n" +
                    "\n" +
                    ".map-scroll-pane {\n" +
                    "    -fx-background: transparent;\n" +
                    "    -fx-background-color: transparent;\n" +
                    "    -fx-border-color: transparent;\n" +
                    "    -fx-padding: 0;\n" +
                    "}\n" +
                    "\n" +
                    ".map-scroll-pane .viewport {\n" +
                    "    -fx-background-color: transparent;\n" +
                    "}\n" +
                    "\n" +
                    ".dialog-pane {\n" +
                    "    -fx-background-color: %s;\n" +
                    "}\n" +
                    "\n" +
                    ".dialog-pane .header-panel {\n" +
                    "    -fx-background-color: %s;\n" +
                    "}\n";

    public static void generateCSS() {
        try {
            Files.createDirectories(Paths.get("data"));

            ColorConfig colors = SpriteManager.getInstance().getColors();

            String buttonBgNormal = colors.getBridge();
            String buttonTextNormal = colors.getBackground();
            String buttonBgHover = colors.getSignalOn();
            String buttonTextHover = colors.getBackground();
            String buttonBgPressed = colors.getPower();
            String buttonTextPressed = colors.getBackground();

            String mapRowBgNormal = colors.getNot();
            String mapRowTextNormal = colors.getBackground();
            String mapRowBgHover = colors.getBridge();

            String emptyTextColor = colors.getGrid();
            String titleColor = colors.getGrid();

            String iconTextNormal = colors.getBackground();
            String iconBgHover = colors.getPower();
            String iconTextHover = colors.getBackground();
            String iconBgPressed = colors.getPower();
            String iconTextPressed = colors.getBackground();

            String dialogBg = colors.getBackground();
            String dialogHeader = colors.getSignalOn();

            String css = String.format(CSS_TEMPLATE,
                    buttonBgNormal, buttonTextNormal,
                    buttonBgHover, buttonTextHover,
                    buttonBgPressed, buttonTextPressed,
                    mapRowBgNormal, mapRowTextNormal,
                    mapRowBgHover,
                    emptyTextColor, titleColor,
                    iconTextNormal, iconBgHover, iconTextHover, iconBgPressed, iconTextPressed,
                    dialogBg, dialogHeader
            );

            Files.write(Paths.get(CSS_PATH), css.getBytes());
            System.out.println("CSS generated successfully");

        } catch (IOException e) {
            System.err.println("Failed to generate CSS: " + e.getMessage());
        }
    }
}