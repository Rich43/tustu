package com.sun.prism;

import java.nio.Buffer;

/* loaded from: jfxrt.jar:com/sun/prism/Texture.class */
public interface Texture extends GraphicsResource {

    /* loaded from: jfxrt.jar:com/sun/prism/Texture$Usage.class */
    public enum Usage {
        DEFAULT,
        DYNAMIC,
        STATIC
    }

    PixelFormat getPixelFormat();

    int getPhysicalWidth();

    int getPhysicalHeight();

    int getContentX();

    int getContentY();

    int getContentWidth();

    int getContentHeight();

    int getMaxContentWidth();

    int getMaxContentHeight();

    void setContentWidth(int i2);

    void setContentHeight(int i2);

    int getLastImageSerial();

    void setLastImageSerial(int i2);

    void update(Image image);

    void update(Image image, int i2, int i3);

    void update(Image image, int i2, int i3, int i4, int i5);

    void update(Image image, int i2, int i3, int i4, int i5, boolean z2);

    void update(Buffer buffer, PixelFormat pixelFormat, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z2);

    void update(MediaFrame mediaFrame, boolean z2);

    WrapMode getWrapMode();

    boolean getUseMipmap();

    Texture getSharedTexture(WrapMode wrapMode);

    boolean getLinearFiltering();

    void setLinearFiltering(boolean z2);

    void lock();

    void unlock();

    boolean isLocked();

    int getLockCount();

    void assertLocked();

    void makePermanent();

    void contentsUseful();

    void contentsNotUseful();

    boolean isSurfaceLost();

    /* loaded from: jfxrt.jar:com/sun/prism/Texture$WrapMode.class */
    public enum WrapMode {
        CLAMP_NOT_NEEDED,
        CLAMP_TO_ZERO,
        CLAMP_TO_EDGE,
        REPEAT,
        CLAMP_TO_ZERO_SIMULATED(CLAMP_TO_ZERO),
        CLAMP_TO_EDGE_SIMULATED(CLAMP_TO_EDGE),
        REPEAT_SIMULATED(REPEAT);

        private WrapMode simulates;
        private WrapMode simulatedBy;

        WrapMode(WrapMode simulates) {
            this.simulates = simulates;
            simulates.simulatedBy = this;
        }

        public WrapMode simulatedVersion() {
            return this.simulatedBy;
        }

        public boolean isCompatibleWith(WrapMode requestedMode) {
            return requestedMode == this || requestedMode == this.simulates || requestedMode == CLAMP_NOT_NEEDED;
        }
    }
}
