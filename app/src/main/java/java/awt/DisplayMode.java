package java.awt;

/* loaded from: rt.jar:java/awt/DisplayMode.class */
public final class DisplayMode {
    private Dimension size;
    private int bitDepth;
    private int refreshRate;
    public static final int BIT_DEPTH_MULTI = -1;
    public static final int REFRESH_RATE_UNKNOWN = 0;

    public DisplayMode(int i2, int i3, int i4, int i5) {
        this.size = new Dimension(i2, i3);
        this.bitDepth = i4;
        this.refreshRate = i5;
    }

    public int getHeight() {
        return this.size.height;
    }

    public int getWidth() {
        return this.size.width;
    }

    public int getBitDepth() {
        return this.bitDepth;
    }

    public int getRefreshRate() {
        return this.refreshRate;
    }

    public boolean equals(DisplayMode displayMode) {
        return displayMode != null && getHeight() == displayMode.getHeight() && getWidth() == displayMode.getWidth() && getBitDepth() == displayMode.getBitDepth() && getRefreshRate() == displayMode.getRefreshRate();
    }

    public boolean equals(Object obj) {
        if (obj instanceof DisplayMode) {
            return equals((DisplayMode) obj);
        }
        return false;
    }

    public int hashCode() {
        return getWidth() + getHeight() + (getBitDepth() * 7) + (getRefreshRate() * 13);
    }
}
