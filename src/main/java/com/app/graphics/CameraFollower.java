package com.app.graphics;

public class CameraFollower {
    // Мёртвые зоны (доля от размера viewport)
    public static final double DEAD_ZONE_KEYBOARD = 0.25;    // 1/4 — для клавиатурного курсора
    public static final double DEAD_ZONE_MOUSE    = 0.0625;  // 1/16 — для мыши

    // Сглаживание (0..1): меньше — плавнее, больше — резче
    public static final double SMOOTHING_KEYBOARD = 0.08;
    public static final double SMOOTHING_MOUSE    = 0.16;   // в 2 раза резче

    // Максимальная скорость камеры (пикселей мира в секунду)
    private static final double MAX_SPEED = 900.0;

    private final Camera camera;

    private double vx = 0;
    private double vy = 0;

    public CameraFollower(Camera camera) {
        this.camera = camera;
    }

    /**
     * @param deltaSeconds  время с прошлого кадра (сек)
     * @param cursorScreenX canvas-X цели
     * @param cursorScreenY canvas-Y цели
     * @param active        активна ли цель; если нет — плавно тормозим
     * @param deadZone      доля мёртвой зоны (0..0.5)
     * @param smoothing     сглаживание (0..1)
     */
    public void update(double deltaSeconds,
                       double cursorScreenX, double cursorScreenY,
                       boolean active,
                       double deadZone,
                       double smoothing) {
        double targetVx = 0;
        double targetVy = 0;

        if (active) {
            int vw = camera.getViewportWidth();
            int vh = camera.getViewportHeight();

            if (deadZone < 0.0) deadZone = 0.0;
            if (deadZone > 0.5) deadZone = 0.5;

            double leftEdge = vw * deadZone;
            double rightEdge = vw * (1.0 - deadZone);
            double topEdge = vh * deadZone;
            double bottomEdge = vh * (1.0 - deadZone);

            if (cursorScreenX < leftEdge) {
                double t = (leftEdge - cursorScreenX) / leftEdge;
                t = Math.min(1.0, t);
                targetVx = -MAX_SPEED * t;
            } else if (cursorScreenX > rightEdge) {
                double edgeWidth = vw - rightEdge;
                double t = (cursorScreenX - rightEdge) / edgeWidth;
                t = Math.min(1.0, t);
                targetVx = MAX_SPEED * t;
            }

            if (cursorScreenY < topEdge) {
                double t = (topEdge - cursorScreenY) / topEdge;
                t = Math.min(1.0, t);
                targetVy = -MAX_SPEED * t;
            } else if (cursorScreenY > bottomEdge) {
                double edgeWidth = vh - bottomEdge;
                double t = (cursorScreenY - bottomEdge) / edgeWidth;
                t = Math.min(1.0, t);
                targetVy = MAX_SPEED * t;
            }
        }

        if (smoothing < 0.001) smoothing = 0.001;
        if (smoothing > 1.0) smoothing = 1.0;

        vx += (targetVx - vx) * smoothing;
        vy += (targetVy - vy) * smoothing;

        if (Math.abs(vx) < 1.0) vx = 0;
        if (Math.abs(vy) < 1.0) vy = 0;

        if (vx != 0 || vy != 0) {
            camera.moveWorld(vx * deltaSeconds, vy * deltaSeconds);
        }
    }

    public void reset() {
        vx = 0;
        vy = 0;
    }
}