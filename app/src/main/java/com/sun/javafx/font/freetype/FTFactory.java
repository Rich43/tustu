package com.sun.javafx.font.freetype;

import com.sun.istack.internal.localization.Localizable;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.TextRun;

/* loaded from: jfxrt.jar:com/sun/javafx/font/freetype/FTFactory.class */
public class FTFactory extends PrismFontFactory {
    static boolean LCD_SUPPORT;

    public static PrismFontFactory getFactory() {
        PrismFontFactory factory = null;
        long[] ptr = new long[1];
        int error = OSFreetype.FT_Init_FreeType(ptr);
        long library = ptr[0];
        int[] major = new int[1];
        int[] minor = new int[1];
        int[] patch = new int[1];
        if (error == 0) {
            factory = new FTFactory();
            OSFreetype.FT_Library_Version(library, major, minor, patch);
            error = OSFreetype.FT_Library_SetLcdFilter(library, 1);
            LCD_SUPPORT = error == 0;
            OSFreetype.FT_Done_FreeType(library);
        }
        if (PrismFontFactory.debugFonts) {
            if (factory != null) {
                String version = major[0] + "." + minor[0] + "." + patch[0];
                System.err.println("Freetype2 Loaded (version " + version + ")");
                String lcdSupport = LCD_SUPPORT ? "Enabled" : "Disabled";
                System.err.println("LCD support " + lcdSupport);
            } else {
                System.err.println("Freetype2 Failed (error " + error + ")");
            }
        }
        return factory;
    }

    private FTFactory() {
    }

    @Override // com.sun.javafx.font.PrismFontFactory
    protected PrismFontFile createFontFile(String name, String filename, int fIndex, boolean register, boolean embedded, boolean copy, boolean tracked) throws Exception {
        return new FTFontFile(name, filename, fIndex, register, embedded, copy, tracked);
    }

    @Override // com.sun.javafx.font.PrismFontFactory
    public GlyphLayout createGlyphLayout() {
        if (OSFreetype.isPangoEnabled()) {
            return new PangoGlyphLayout();
        }
        if (OSFreetype.isHarfbuzzEnabled()) {
            return new HBGlyphLayout();
        }
        return new StubGlyphLayout();
    }

    @Override // com.sun.javafx.font.PrismFontFactory
    public boolean isLCDTextSupported() {
        return LCD_SUPPORT && super.isLCDTextSupported();
    }

    @Override // com.sun.javafx.font.PrismFontFactory
    protected boolean registerEmbeddedFont(String path) {
        long[] ptr = new long[1];
        if (OSFreetype.FT_Init_FreeType(ptr) != 0) {
            return false;
        }
        long library = ptr[0];
        byte[] buffer = (path + Localizable.NOT_LOCALIZABLE).getBytes();
        int error = OSFreetype.FT_New_Face(library, buffer, 0L, ptr);
        if (error != 0) {
            long face = ptr[0];
            OSFreetype.FT_Done_Face(face);
        }
        OSFreetype.FT_Done_FreeType(library);
        if (error != 0) {
            return false;
        }
        if (OSFreetype.isPangoEnabled()) {
            return OSPango.FcConfigAppFontAddFile(0L, path);
        }
        return true;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/freetype/FTFactory$StubGlyphLayout.class */
    private static class StubGlyphLayout extends GlyphLayout {
        @Override // com.sun.javafx.text.GlyphLayout
        public void layout(TextRun run, PGFont font, FontStrike strike, char[] text) {
        }
    }
}
