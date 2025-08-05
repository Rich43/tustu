package com.sun.javafx.font.freetype;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.PrismFontFactory;

/* loaded from: jfxrt.jar:com/sun/javafx/font/freetype/FTDisposer.class */
class FTDisposer implements DisposerRecord {
    long library;
    long face;

    FTDisposer(long library, long face) {
        this.library = library;
        this.face = face;
    }

    @Override // com.sun.javafx.font.DisposerRecord
    public synchronized void dispose() {
        if (this.face != 0) {
            OSFreetype.FT_Done_Face(this.face);
            if (PrismFontFactory.debugFonts) {
                System.err.println("Done Face=" + this.face);
            }
            this.face = 0L;
        }
        if (this.library != 0) {
            OSFreetype.FT_Done_FreeType(this.library);
            if (PrismFontFactory.debugFonts) {
                System.err.println("Done Library=" + this.library);
            }
            this.library = 0L;
        }
    }
}
