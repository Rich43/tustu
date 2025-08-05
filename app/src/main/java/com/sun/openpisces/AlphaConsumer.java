package com.sun.openpisces;

/* loaded from: jfxrt.jar:com/sun/openpisces/AlphaConsumer.class */
public interface AlphaConsumer {
    int getOriginX();

    int getOriginY();

    int getWidth();

    int getHeight();

    void setMaxAlpha(int i2);

    void setAndClearRelativeAlphas(int[] iArr, int i2, int i3, int i4);
}
