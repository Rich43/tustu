package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/font/PGFont.class */
public interface PGFont {
    String getFullName();

    String getFamilyName();

    String getStyleName();

    String getName();

    float getSize();

    FontResource getFontResource();

    FontStrike getStrike(BaseTransform baseTransform);

    FontStrike getStrike(BaseTransform baseTransform, int i2);

    int getFeatures();
}
