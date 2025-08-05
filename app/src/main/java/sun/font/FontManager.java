package sun.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;

/* loaded from: rt.jar:sun/font/FontManager.class */
public interface FontManager {
    public static final int NO_FALLBACK = 0;
    public static final int PHYSICAL_FALLBACK = 1;
    public static final int LOGICAL_FALLBACK = 2;

    boolean registerFont(Font font);

    void deRegisterBadFont(Font2D font2D);

    Font2D findFont2D(String str, int i2, int i3);

    Font2D createFont2D(File file, int i2, boolean z2, CreatedFontTracker createdFontTracker) throws FontFormatException;

    boolean usingPerAppContextComposites();

    Font2DHandle getNewComposite(String str, int i2, Font2DHandle font2DHandle);

    void preferLocaleFonts();

    void preferProportionalFonts();
}
