package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/font/PrismFontUtils.class */
public class PrismFontUtils {
    private PrismFontUtils() {
    }

    static Metrics getFontMetrics(PGFont font) {
        FontStrike strike = font.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
        return strike.getMetrics();
    }

    static double computeStringWidth(PGFont font, String string) {
        if (string == null || string.equals("")) {
            return 0.0d;
        }
        FontStrike strike = font.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
        double width = 0.0d;
        for (int i2 = 0; i2 < string.length(); i2++) {
            width += strike.getCharAdvance(string.charAt(i2));
        }
        return width;
    }
}
