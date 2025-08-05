package sun.font;

import java.awt.font.GlyphJustificationInfo;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/ExtendedTextLabel.class */
public abstract class ExtendedTextLabel extends TextLabel implements TextLineComponent {
    @Override // sun.font.TextLineComponent
    public abstract int getNumCharacters();

    @Override // sun.font.TextLineComponent
    public abstract CoreMetrics getCoreMetrics();

    @Override // sun.font.TextLineComponent
    public abstract float getCharX(int i2);

    @Override // sun.font.TextLineComponent
    public abstract float getCharY(int i2);

    @Override // sun.font.TextLineComponent
    public abstract float getCharAdvance(int i2);

    public abstract Rectangle2D getCharVisualBounds(int i2, float f2, float f3);

    public abstract int logicalToVisual(int i2);

    public abstract int visualToLogical(int i2);

    @Override // sun.font.TextLineComponent
    public abstract int getLineBreakIndex(int i2, float f2);

    @Override // sun.font.TextLineComponent
    public abstract float getAdvanceBetween(int i2, int i3);

    @Override // sun.font.TextLineComponent
    public abstract boolean caretAtOffsetIsValid(int i2);

    @Override // sun.font.TextLineComponent
    public abstract TextLineComponent getSubset(int i2, int i3, int i4);

    @Override // sun.font.TextLineComponent
    public abstract int getNumJustificationInfos();

    @Override // sun.font.TextLineComponent
    public abstract void getJustificationInfos(GlyphJustificationInfo[] glyphJustificationInfoArr, int i2, int i3, int i4);

    @Override // sun.font.TextLineComponent
    public abstract TextLineComponent applyJustificationDeltas(float[] fArr, int i2, boolean[] zArr);

    @Override // sun.font.TextLineComponent
    public Rectangle2D getCharVisualBounds(int i2) {
        return getCharVisualBounds(i2, 0.0f, 0.0f);
    }
}
