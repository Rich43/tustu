package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCFont.class */
public abstract class WCFont extends Ref {
    public abstract Object getPlatformFont();

    public abstract WCFont deriveFont(float f2);

    public abstract WCTextRun[] getTextRuns(String str);

    public abstract int[] getGlyphCodes(char[] cArr);

    public abstract float getXHeight();

    public abstract double getGlyphWidth(int i2);

    public abstract float[] getGlyphBoundingBox(int i2);

    public abstract float getAscent();

    public abstract float getDescent();

    public abstract float getLineSpacing();

    public abstract float getLineGap();

    public abstract boolean hasUniformLineMetrics();

    public abstract float getCapHeight();

    public int hashCode() {
        Object font = getPlatformFont();
        if (font != null) {
            return font.hashCode();
        }
        return 0;
    }

    public boolean equals(Object object) {
        if (object instanceof WCFont) {
            Object font1 = getPlatformFont();
            Object font2 = ((WCFont) object).getPlatformFont();
            return font1 == null ? font2 == null : font1.equals(font2);
        }
        return false;
    }
}
