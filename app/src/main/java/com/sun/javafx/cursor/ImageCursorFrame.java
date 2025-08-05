package com.sun.javafx.cursor;

/* loaded from: jfxrt.jar:com/sun/javafx/cursor/ImageCursorFrame.class */
public final class ImageCursorFrame extends CursorFrame {
    private final Object platformImage;
    private final double width;
    private final double height;
    private final double hotspotX;
    private final double hotspotY;

    public ImageCursorFrame(Object platformImage, double width, double height, double hotspotX, double hotspotY) {
        this.platformImage = platformImage;
        this.width = width;
        this.height = height;
        this.hotspotX = hotspotX;
        this.hotspotY = hotspotY;
    }

    @Override // com.sun.javafx.cursor.CursorFrame
    public CursorType getCursorType() {
        return CursorType.IMAGE;
    }

    public Object getPlatformImage() {
        return this.platformImage;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getHotspotX() {
        return this.hotspotX;
    }

    public double getHotspotY() {
        return this.hotspotY;
    }
}
