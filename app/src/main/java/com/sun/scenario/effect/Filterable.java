package com.sun.scenario.effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Filterable.class */
public interface Filterable extends LockableResource {
    Object getData();

    int getContentWidth();

    int getContentHeight();

    void setContentWidth(int i2);

    void setContentHeight(int i2);

    int getMaxContentWidth();

    int getMaxContentHeight();

    int getPhysicalWidth();

    int getPhysicalHeight();

    float getPixelScale();

    void flush();
}
