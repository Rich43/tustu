package sun.font;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.text.Bidi;

/* loaded from: rt.jar:sun/font/TextLabelFactory.class */
public final class TextLabelFactory {
    private final FontRenderContext frc;
    private final char[] text;
    private final Bidi bidi;
    private Bidi lineBidi;
    private final int flags;
    private int lineStart = 0;
    private int lineLimit;

    public TextLabelFactory(FontRenderContext fontRenderContext, char[] cArr, Bidi bidi, int i2) {
        this.frc = fontRenderContext;
        this.text = (char[]) cArr.clone();
        this.bidi = bidi;
        this.flags = i2;
        this.lineBidi = bidi;
        this.lineLimit = cArr.length;
    }

    public FontRenderContext getFontRenderContext() {
        return this.frc;
    }

    public Bidi getLineBidi() {
        return this.lineBidi;
    }

    public void setLineContext(int i2, int i3) {
        this.lineStart = i2;
        this.lineLimit = i3;
        if (this.bidi != null) {
            this.lineBidi = this.bidi.createLineBidi(i2, i3);
        }
    }

    public ExtendedTextLabel createExtended(Font font, CoreMetrics coreMetrics, Decoration decoration, int i2, int i3) {
        if (i2 >= i3 || i2 < this.lineStart || i3 > this.lineLimit) {
            throw new IllegalArgumentException("bad start: " + i2 + " or limit: " + i3);
        }
        int levelAt = this.lineBidi == null ? 0 : this.lineBidi.getLevelAt(i2 - this.lineStart);
        boolean z2 = (this.lineBidi == null || this.lineBidi.baseIsLeftToRight()) ? false : true;
        int i4 = this.flags & (-10);
        if ((levelAt & 1) != 0) {
            i4 |= 1;
        }
        if (z2 & true) {
            i4 |= 8;
        }
        return new ExtendedTextSourceLabel(new StandardTextSource(this.text, i2, i3 - i2, this.lineStart, this.lineLimit - this.lineStart, levelAt, i4, font, this.frc, coreMetrics), decoration);
    }

    public TextLabel createSimple(Font font, CoreMetrics coreMetrics, int i2, int i3) {
        if (i2 >= i3 || i2 < this.lineStart || i3 > this.lineLimit) {
            throw new IllegalArgumentException("bad start: " + i2 + " or limit: " + i3);
        }
        int levelAt = this.lineBidi == null ? 0 : this.lineBidi.getLevelAt(i2 - this.lineStart);
        boolean z2 = (this.lineBidi == null || this.lineBidi.baseIsLeftToRight()) ? false : true;
        int i4 = this.flags & (-10);
        if ((levelAt & 1) != 0) {
            i4 |= 1;
        }
        if (z2 & true) {
            i4 |= 8;
        }
        return new TextSourceLabel(new StandardTextSource(this.text, i2, i3 - i2, this.lineStart, this.lineLimit - this.lineStart, levelAt, i4, font, this.frc, coreMetrics));
    }
}
