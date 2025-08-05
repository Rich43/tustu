package sun.font;

import java.awt.Font;
import java.awt.font.FontRenderContext;

/* loaded from: rt.jar:sun/font/TextSource.class */
public abstract class TextSource {
    public static final boolean WITHOUT_CONTEXT = false;
    public static final boolean WITH_CONTEXT = true;

    public abstract char[] getChars();

    public abstract int getStart();

    public abstract int getLength();

    public abstract int getContextStart();

    public abstract int getContextLength();

    public abstract int getLayoutFlags();

    public abstract int getBidiLevel();

    public abstract Font getFont();

    public abstract FontRenderContext getFRC();

    public abstract CoreMetrics getCoreMetrics();

    public abstract TextSource getSubSource(int i2, int i3, int i4);

    public abstract String toString(boolean z2);
}
