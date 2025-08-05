package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/font/FontResource.class */
public interface FontResource {
    public static final int AA_GREYSCALE = 0;
    public static final int AA_LCD = 1;
    public static final int KERN = 1;
    public static final int CLIG = 2;
    public static final int DLIG = 4;
    public static final int HLIG = 8;
    public static final int LIGA = 16;
    public static final int RLIG = 32;
    public static final int LIGATURES = 62;
    public static final int SMCP = 64;
    public static final int FRAC = 128;
    public static final int AFRC = 256;
    public static final int ZERO = 512;
    public static final int SWSH = 1024;
    public static final int CSWH = 2048;
    public static final int SALT = 4096;
    public static final int NALT = 8192;
    public static final int RUBY = 16384;
    public static final int SS01 = 32768;
    public static final int SS02 = 65536;
    public static final int SS03 = 131072;
    public static final int SS04 = 262144;
    public static final int SS05 = 524288;
    public static final int SS06 = 1048576;
    public static final int SS07 = 2097152;

    String getFullName();

    String getPSName();

    String getFamilyName();

    String getFileName();

    String getStyleName();

    String getLocaleFullName();

    String getLocaleFamilyName();

    String getLocaleStyleName();

    int getFeatures();

    boolean isBold();

    boolean isItalic();

    float getAdvance(int i2, float f2);

    float[] getGlyphBoundingBox(int i2, float f2, float[] fArr);

    int getDefaultAAMode();

    CharToGlyphMapper getGlyphMapper();

    Map<FontStrikeDesc, WeakReference<FontStrike>> getStrikeMap();

    FontStrike getStrike(float f2, BaseTransform baseTransform);

    FontStrike getStrike(float f2, BaseTransform baseTransform, int i2);

    Object getPeer();

    void setPeer(Object obj);

    boolean isEmbeddedFont();
}
