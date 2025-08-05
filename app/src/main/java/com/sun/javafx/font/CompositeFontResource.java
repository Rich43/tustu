package com.sun.javafx.font;

/* loaded from: jfxrt.jar:com/sun/javafx/font/CompositeFontResource.class */
public interface CompositeFontResource extends FontResource {
    FontResource getSlotResource(int i2);

    int getNumSlots();

    int getSlotForFont(String str);
}
