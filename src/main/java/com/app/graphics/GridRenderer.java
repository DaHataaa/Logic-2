package com.app.graphics;

import com.app.Config;
import com.app.core.Direction;
import com.app.core.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class GridRenderer {
    private final World world;
    private final Camera camera;
    private final SpriteManager spriteManager;
    private final ColorConfig colors;
    private final int baseCellSize;
    private final int worldSizeCells;

    private Canvas canvas;
    private GraphicsContext gc;

    private final Map<String, Map<Integer, Map<String, WritableImage>>> spriteImageCache = new HashMap<>();

    public GridRenderer(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
        this.spriteManager = SpriteManager.getInstance();
        this.colors = spriteManager.getColors();
        this.baseCellSize = spriteManager.getBaseSize();
        this.worldSizeCells = world.getSize();

        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();

        canvas.setStyle("-fx-scale-shape: false;");

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() > 0) {
                camera.setViewportSize(newVal.intValue(), (int) canvas.getHeight());
                render();
            }
        });
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() > 0) {
                camera.setViewportSize((int) canvas.getWidth(), newVal.intValue());
                render();
            }
        });
    }

    private String getCacheKey(Direction dir, boolean signalOn) {
        return dir.toString() + "_" + signalOn;
    }

    private WritableImage getCachedSprite(String spriteName, int size, Direction dir, boolean signalOn) {
        Map<Integer, Map<String, WritableImage>> sizeMap = spriteImageCache.get(spriteName);
        if (sizeMap == null) {
            sizeMap = new HashMap<>();
            spriteImageCache.put(spriteName, sizeMap);
        }

        Map<String, WritableImage> dirMap = sizeMap.get(size);
        if (dirMap == null) {
            dirMap = new HashMap<>();
            sizeMap.put(size, dirMap);
        }

        String key = getCacheKey(dir, signalOn);
        WritableImage cached = dirMap.get(key);

        if (cached != null) {
            return cached;
        }

        int[] sprite = spriteManager.getSpriteWithDirection(spriteName, size, dir, signalOn);
        if (sprite == null) return null;

        WritableImage image = new WritableImage(size, size);
        PixelWriter pw = image.getPixelWriter();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int color = sprite[y * size + x];
                if ((color & 0xFF000000) != 0) {
                    pw.setArgb(x, y, color);
                }
            }
        }

        dirMap.put(key, image);
        return image;
    }

    public void render() {
        if (canvas.getWidth() <= 0 || canvas.getHeight() <= 0) return;

        int cellScreenSize = camera.getCellScreenSize();
        int currentLayer = world.getCurrentLayer();

        int startX = Math.max(0, camera.getVisibleStartX(worldSizeCells));
        int startY = Math.max(0, camera.getVisibleStartY(worldSizeCells));
        int endX = Math.min(worldSizeCells, camera.getVisibleEndX(worldSizeCells));
        int endY = Math.min(worldSizeCells, camera.getVisibleEndY(worldSizeCells));

        int visibleCells = (endX - startX) * (endY - startY);
        boolean useSimplifiedRendering = cellScreenSize > 64 && visibleCells > 5000;

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                String block = world.getBlock(currentLayer, x, y);
                boolean hasSignal = world.getSignal(currentLayer, x, y);

                double screenX = camera.worldToScreenX(x * baseCellSize);
                double screenY = camera.worldToScreenY(y * baseCellSize);
                int ix = (int) Math.round(screenX);
                int iy = (int) Math.round(screenY);

                // 1. ФОН КЛЕТКИ
                if (hasSignal) {
                    gc.setFill(Color.web(colors.getSignalOn()));
                } else {
                    gc.setFill(Color.web(colors.getBackground()));
                }
                gc.fillRect(ix, iy, cellScreenSize, cellScreenSize);

                // 2. СПРАЙТ ПОВЕРХ
                if (block != null && !block.equals("0")) {
                    String[] parts = block.split("_");
                    String blockName = parts[0];
                    Direction dir = Direction.UP;

                    if (parts.length > 1 && parts[1].length() == 2) {
                        char dirChar = parts[1].charAt(1);
                        dir = Direction.fromChar(dirChar);
                    }

                    if (useSimplifiedRendering && cellScreenSize > 32) {
                        gc.setFill(Color.web(colors.getColorForBlock(blockName)));
                        gc.fillRect(ix, iy, cellScreenSize, cellScreenSize);
                        gc.setFill(Color.WHITE);
                        gc.setFont(javafx.scene.text.Font.font(cellScreenSize / 2));
                        gc.fillText(blockName.substring(0, 1).toUpperCase(),
                                ix + cellScreenSize / 3, iy + cellScreenSize / 1.5);
                    } else {
                        WritableImage spriteImage = getCachedSprite(blockName, cellScreenSize, dir, false);
                        if (spriteImage != null) {
                            gc.drawImage(spriteImage, ix, iy);
                        }
                    }
                }
            }
        }

        // РИСУЕМ СЕТКУ ИЛИ РАМКУ ПОЛЯ
        double firstCellScreenX = camera.worldToScreenX(startX * baseCellSize);
        double lastCellScreenX = camera.worldToScreenX(endX * baseCellSize);
        double firstCellScreenY = camera.worldToScreenY(startY * baseCellSize);
        double lastCellScreenY = camera.worldToScreenY(endY * baseCellSize);

        gc.setStroke(Color.web(colors.getGrid()));

        if (cellScreenSize >= 8) {
            // Рисуем полную сетку
            gc.setLineWidth(1.6);

            for (int x = startX; x <= endX; x++) {
                double screenX = camera.worldToScreenX(x * baseCellSize);
                if (screenX >= firstCellScreenX - 10 && screenX <= lastCellScreenX + 10) {
                    gc.strokeLine(screenX, firstCellScreenY, screenX, lastCellScreenY);
                }
            }

            for (int y = startY; y <= endY; y++) {
                double screenY = camera.worldToScreenY(y * baseCellSize);
                if (screenY >= firstCellScreenY - 10 && screenY <= lastCellScreenY + 10) {
                    gc.strokeLine(firstCellScreenX, screenY, lastCellScreenX, screenY);
                }
            }
        } else {
            // Рисуем только внешнюю рамку всего поля
            gc.setLineWidth(2.0);
            gc.strokeRect(firstCellScreenX, firstCellScreenY,
                    lastCellScreenX - firstCellScreenX,
                    lastCellScreenY - firstCellScreenY);
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void drawSprite(GraphicsContext gc, int[] sprite, int size, int x, int y) {
        WritableImage image = new WritableImage(size, size);
        PixelWriter pw = image.getPixelWriter();
        for (int py = 0; py < size; py++) {
            for (int px = 0; px < size; px++) {
                int color = sprite[py * size + px];
                if ((color & 0xFF000000) != 0) {
                    pw.setArgb(px, py, color);
                }
            }
        }
        gc.drawImage(image, x, y);
    }
}