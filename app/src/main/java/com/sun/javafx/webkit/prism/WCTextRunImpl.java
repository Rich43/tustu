package com.sun.javafx.webkit.prism;

import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.text.TextRun;
import com.sun.webkit.graphics.WCTextRun;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCTextRunImpl.class */
public final class WCTextRunImpl implements WCTextRun {
    private final TextRun run;
    private static float[] POS_AND_ADVANCE = new float[4];

    public WCTextRunImpl(GlyphList run) {
        this.run = (TextRun) run;
    }

    @Override // com.sun.webkit.graphics.WCTextRun
    public int getGlyphCount() {
        return this.run.getGlyphCount();
    }

    @Override // com.sun.webkit.graphics.WCTextRun
    public boolean isLeftToRight() {
        return this.run.isLeftToRight();
    }

    @Override // com.sun.webkit.graphics.WCTextRun
    public int getGlyph(int index) {
        if (index < this.run.getGlyphCount()) {
            return this.run.getGlyphCode(index);
        }
        return 0;
    }

    @Override // com.sun.webkit.graphics.WCTextRun
    public float[] getGlyphPosAndAdvance(int glyphIndex) {
        POS_AND_ADVANCE[0] = this.run.getPosX(glyphIndex);
        POS_AND_ADVANCE[1] = this.run.getPosY(glyphIndex);
        POS_AND_ADVANCE[2] = this.run.getAdvance(glyphIndex);
        POS_AND_ADVANCE[3] = 0.0f;
        return POS_AND_ADVANCE;
    }

    @Override // com.sun.webkit.graphics.WCTextRun
    public int getStart() {
        return this.run.getStart();
    }

    @Override // com.sun.webkit.graphics.WCTextRun
    public int getEnd() {
        return this.run.getEnd();
    }

    @Override // com.sun.webkit.graphics.WCTextRun
    public int getCharOffset(int index) {
        return this.run.getCharOffset(index);
    }
}
