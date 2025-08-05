package sun.font;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.PrintStream;
import java.util.Map;
import sun.font.Decoration;

/* loaded from: rt.jar:sun/font/ExtendedTextSourceLabel.class */
class ExtendedTextSourceLabel extends ExtendedTextLabel implements Decoration.Label {
    TextSource source;
    private Decoration decorator;
    private Font font;
    private AffineTransform baseTX;
    private CoreMetrics cm;
    Rectangle2D lb;

    /* renamed from: ab, reason: collision with root package name */
    Rectangle2D f13551ab;
    Rectangle2D vb;
    Rectangle2D ib;
    StandardGlyphVector gv;
    float[] charinfo;
    private static final int posx = 0;
    private static final int posy = 1;
    private static final int advx = 2;
    private static final int advy = 3;
    private static final int visx = 4;
    private static final int visy = 5;
    private static final int visw = 6;
    private static final int vish = 7;
    private static final int numvals = 8;

    public ExtendedTextSourceLabel(TextSource textSource, Decoration decoration) {
        this.source = textSource;
        this.decorator = decoration;
        finishInit();
    }

    public ExtendedTextSourceLabel(TextSource textSource, ExtendedTextSourceLabel extendedTextSourceLabel, int i2) {
        this.source = textSource;
        this.decorator = extendedTextSourceLabel.decorator;
        finishInit();
    }

    private void finishInit() {
        this.font = this.source.getFont();
        Map<TextAttribute, ?> attributes = this.font.getAttributes();
        this.baseTX = AttributeValues.getBaselineTransform(attributes);
        if (this.baseTX == null) {
            this.cm = this.source.getCoreMetrics();
            return;
        }
        AffineTransform charTransform = AttributeValues.getCharTransform(attributes);
        if (charTransform == null) {
            charTransform = new AffineTransform();
        }
        this.font = this.font.deriveFont(charTransform);
        this.cm = CoreMetrics.get(this.font.getLineMetrics(this.source.getChars(), this.source.getStart(), this.source.getStart() + this.source.getLength(), this.source.getFRC()));
    }

    @Override // sun.font.TextLabel, sun.font.TextLineComponent, sun.font.Decoration.Label
    public Rectangle2D getLogicalBounds() {
        return getLogicalBounds(0.0f, 0.0f);
    }

    @Override // sun.font.TextLabel
    public Rectangle2D getLogicalBounds(float f2, float f3) {
        if (this.lb == null) {
            this.lb = createLogicalBounds();
        }
        return new Rectangle2D.Float((float) (this.lb.getX() + f2), (float) (this.lb.getY() + f3), (float) this.lb.getWidth(), (float) this.lb.getHeight());
    }

    @Override // sun.font.TextLineComponent
    public float getAdvance() {
        if (this.lb == null) {
            this.lb = createLogicalBounds();
        }
        return (float) this.lb.getWidth();
    }

    @Override // sun.font.TextLabel
    public Rectangle2D getVisualBounds(float f2, float f3) {
        if (this.vb == null) {
            this.vb = this.decorator.getVisualBounds(this);
        }
        return new Rectangle2D.Float((float) (this.vb.getX() + f2), (float) (this.vb.getY() + f3), (float) this.vb.getWidth(), (float) this.vb.getHeight());
    }

    @Override // sun.font.TextLabel
    public Rectangle2D getAlignBounds(float f2, float f3) {
        if (this.f13551ab == null) {
            this.f13551ab = createAlignBounds();
        }
        return new Rectangle2D.Float((float) (this.f13551ab.getX() + f2), (float) (this.f13551ab.getY() + f3), (float) this.f13551ab.getWidth(), (float) this.f13551ab.getHeight());
    }

    @Override // sun.font.TextLabel
    public Rectangle2D getItalicBounds(float f2, float f3) {
        if (this.ib == null) {
            this.ib = createItalicBounds();
        }
        return new Rectangle2D.Float((float) (this.ib.getX() + f2), (float) (this.ib.getY() + f3), (float) this.ib.getWidth(), (float) this.ib.getHeight());
    }

    @Override // sun.font.TextLineComponent
    public Rectangle getPixelBounds(FontRenderContext fontRenderContext, float f2, float f3) {
        return getGV().getPixelBounds(fontRenderContext, f2, f3);
    }

    @Override // sun.font.TextLineComponent
    public boolean isSimple() {
        return this.decorator == Decoration.getPlainDecoration() && this.baseTX == null;
    }

    @Override // sun.font.TextLineComponent
    public AffineTransform getBaselineTransform() {
        return this.baseTX;
    }

    @Override // sun.font.Decoration.Label
    public Shape handleGetOutline(float f2, float f3) {
        return getGV().getOutline(f2, f3);
    }

    @Override // sun.font.TextLabel, sun.font.TextLineComponent
    public Shape getOutline(float f2, float f3) {
        return this.decorator.getOutline(this, f2, f3);
    }

    @Override // sun.font.Decoration.Label
    public void handleDraw(Graphics2D graphics2D, float f2, float f3) {
        graphics2D.drawGlyphVector(getGV(), f2, f3);
    }

    @Override // sun.font.TextLabel, sun.font.TextLineComponent
    public void draw(Graphics2D graphics2D, float f2, float f3) {
        this.decorator.drawTextAndDecorations(this, graphics2D, f2, f3);
    }

    protected Rectangle2D createLogicalBounds() {
        return getGV().getLogicalBounds();
    }

    @Override // sun.font.Decoration.Label
    public Rectangle2D handleGetVisualBounds() {
        return getGV().getVisualBounds();
    }

    protected Rectangle2D createAlignBounds() {
        float[] charinfo = getCharinfo();
        float fMax = 0.0f;
        float f2 = -this.cm.ascent;
        float f3 = 0.0f;
        float f4 = this.cm.ascent + this.cm.descent;
        if (this.charinfo == null || this.charinfo.length == 0) {
            return new Rectangle2D.Float(0.0f, f2, 0.0f, f4);
        }
        boolean z2 = (this.source.getLayoutFlags() & 8) == 0;
        int length = charinfo.length - 8;
        if (z2) {
            while (length > 0 && charinfo[length + 6] == 0.0f) {
                length -= 8;
            }
        }
        if (length >= 0) {
            int i2 = 0;
            while (i2 < length && (charinfo[i2 + 2] == 0.0f || (!z2 && charinfo[i2 + 6] == 0.0f))) {
                i2 += 8;
            }
            fMax = Math.max(0.0f, charinfo[i2 + 0]);
            f3 = (charinfo[length + 0] + charinfo[length + 2]) - fMax;
        }
        return new Rectangle2D.Float(fMax, f2, f3, f4);
    }

    public Rectangle2D createItalicBounds() {
        float f2 = this.cm.italicAngle;
        Rectangle2D logicalBounds = getLogicalBounds();
        float minX = (float) logicalBounds.getMinX();
        float f3 = -this.cm.ascent;
        float maxX = (float) logicalBounds.getMaxX();
        float f4 = this.cm.descent;
        if (f2 != 0.0f) {
            if (f2 > 0.0f) {
                minX -= f2 * (f4 - this.cm.ssOffset);
                maxX -= f2 * (f3 - this.cm.ssOffset);
            } else {
                minX -= f2 * (f3 - this.cm.ssOffset);
                maxX -= f2 * (f4 - this.cm.ssOffset);
            }
        }
        return new Rectangle2D.Float(minX, f3, maxX - minX, f4 - f3);
    }

    private final StandardGlyphVector getGV() {
        if (this.gv == null) {
            this.gv = createGV();
        }
        return this.gv;
    }

    protected StandardGlyphVector createGV() {
        FontRenderContext frc = this.source.getFRC();
        int layoutFlags = this.source.getLayoutFlags();
        char[] chars = this.source.getChars();
        int start = this.source.getStart();
        int length = this.source.getLength();
        GlyphLayout glyphLayout = GlyphLayout.get(null);
        this.gv = glyphLayout.layout(this.font, frc, chars, start, length, layoutFlags, null);
        GlyphLayout.done(glyphLayout);
        return this.gv;
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public int getNumCharacters() {
        return this.source.getLength();
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public CoreMetrics getCoreMetrics() {
        return this.cm;
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public float getCharX(int i2) {
        validate(i2);
        float[] charinfo = getCharinfo();
        int iL2v = (l2v(i2) * 8) + 0;
        if (charinfo == null || iL2v >= charinfo.length) {
            return 0.0f;
        }
        return charinfo[iL2v];
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public float getCharY(int i2) {
        validate(i2);
        float[] charinfo = getCharinfo();
        int iL2v = (l2v(i2) * 8) + 1;
        if (charinfo == null || iL2v >= charinfo.length) {
            return 0.0f;
        }
        return charinfo[iL2v];
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public float getCharAdvance(int i2) {
        validate(i2);
        float[] charinfo = getCharinfo();
        int iL2v = (l2v(i2) * 8) + 2;
        if (charinfo == null || iL2v >= charinfo.length) {
            return 0.0f;
        }
        return charinfo[iL2v];
    }

    @Override // sun.font.Decoration.Label
    public Rectangle2D handleGetCharVisualBounds(int i2) {
        validate(i2);
        float[] charinfo = getCharinfo();
        int iL2v = l2v(i2) * 8;
        if (charinfo == null || iL2v + 7 >= charinfo.length) {
            return new Rectangle2D.Float();
        }
        return new Rectangle2D.Float(charinfo[iL2v + 4], charinfo[iL2v + 5], charinfo[iL2v + 6], charinfo[iL2v + 7]);
    }

    @Override // sun.font.ExtendedTextLabel
    public Rectangle2D getCharVisualBounds(int i2, float f2, float f3) {
        Rectangle2D charVisualBounds = this.decorator.getCharVisualBounds(this, i2);
        if (f2 != 0.0f || f3 != 0.0f) {
            charVisualBounds.setRect(charVisualBounds.getX() + f2, charVisualBounds.getY() + f3, charVisualBounds.getWidth(), charVisualBounds.getHeight());
        }
        return charVisualBounds;
    }

    private void validate(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("index " + i2 + " < 0");
        }
        if (i2 >= this.source.getLength()) {
            throw new IllegalArgumentException("index " + i2 + " < " + this.source.getLength());
        }
    }

    @Override // sun.font.ExtendedTextLabel
    public int logicalToVisual(int i2) {
        validate(i2);
        return l2v(i2);
    }

    @Override // sun.font.ExtendedTextLabel
    public int visualToLogical(int i2) {
        validate(i2);
        return v2l(i2);
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public int getLineBreakIndex(int i2, float f2) {
        int iL2v;
        float[] charinfo = getCharinfo();
        int length = this.source.getLength();
        int i3 = i2 - 1;
        while (f2 >= 0.0f) {
            i3++;
            if (i3 >= length || (iL2v = (l2v(i3) * 8) + 2) >= charinfo.length) {
                break;
            }
            f2 -= charinfo[iL2v];
        }
        return i3;
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public float getAdvanceBetween(int i2, int i3) {
        int iL2v;
        float f2 = 0.0f;
        float[] charinfo = getCharinfo();
        int i4 = i2 - 1;
        while (true) {
            i4++;
            if (i4 >= i3 || (iL2v = (l2v(i4) * 8) + 2) >= charinfo.length) {
                break;
            }
            f2 += charinfo[iL2v];
        }
        return f2;
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public boolean caretAtOffsetIsValid(int i2) {
        char c2;
        if (i2 == 0 || i2 == this.source.getLength() || (c2 = this.source.getChars()[this.source.getStart() + i2]) == '\t' || c2 == '\n' || c2 == '\r') {
            return true;
        }
        int iL2v = (l2v(i2) * 8) + 2;
        float[] charinfo = getCharinfo();
        return (charinfo == null || iL2v >= charinfo.length || charinfo[iL2v] == 0.0f) ? false : true;
    }

    private final float[] getCharinfo() {
        if (this.charinfo == null) {
            this.charinfo = createCharinfo();
        }
        return this.charinfo;
    }

    protected float[] createCharinfo() {
        int i2;
        StandardGlyphVector gv = getGV();
        float[] glyphInfo = null;
        try {
            glyphInfo = gv.getGlyphInfo();
        } catch (Exception e2) {
            System.out.println(this.source);
        }
        int numGlyphs = gv.getNumGlyphs();
        if (numGlyphs == 0) {
            return glyphInfo;
        }
        int[] glyphCharIndices = gv.getGlyphCharIndices(0, numGlyphs, null);
        if (0 != 0) {
            System.err.println("number of glyphs: " + numGlyphs);
            for (int i3 = 0; i3 < numGlyphs; i3++) {
                System.err.println("g: " + i3 + ", x: " + glyphInfo[(i3 * 8) + 0] + ", a: " + glyphInfo[(i3 * 8) + 2] + ", n: " + glyphCharIndices[i3]);
            }
        }
        int i4 = glyphCharIndices[0];
        int i5 = 0;
        int length = 0;
        int i6 = 0;
        int length2 = 0;
        int i7 = 0;
        int i8 = numGlyphs;
        int i9 = 8;
        int i10 = 1;
        boolean z2 = (this.source.getLayoutFlags() & 1) == 0;
        if (!z2) {
            int i11 = glyphCharIndices[numGlyphs - 1];
            i5 = 0;
            length = glyphInfo.length - 8;
            i6 = 0;
            length2 = glyphInfo.length - 8;
            i7 = numGlyphs - 1;
            i8 = -1;
            i9 = -8;
            i10 = -1;
        }
        float fMin = 0.0f;
        float fMax = 0.0f;
        float fMin2 = 0.0f;
        float fMin3 = 0.0f;
        float fMax2 = 0.0f;
        float fMax3 = 0.0f;
        boolean z3 = false;
        while (i7 != i8) {
            boolean z4 = false;
            int i12 = 0;
            int iMin = glyphCharIndices[i7];
            int iMax = iMin;
            while (true) {
                i2 = iMax;
                i7 += i10;
                length2 += i9;
                if (i7 == i8 || (glyphInfo[length2 + 2] != 0.0f && iMin == i5 && glyphCharIndices[i7] > i2 && i2 - iMin <= i12)) {
                    break;
                }
                if (!z4) {
                    int i13 = length2 - i9;
                    fMin = glyphInfo[i13 + 0];
                    fMax = fMin + glyphInfo[i13 + 2];
                    fMin2 = glyphInfo[i13 + 4];
                    fMin3 = glyphInfo[i13 + 5];
                    fMax2 = fMin2 + glyphInfo[i13 + 6];
                    fMax3 = fMin3 + glyphInfo[i13 + 7];
                    z4 = true;
                }
                i12++;
                float f2 = glyphInfo[length2 + 2];
                if (f2 != 0.0f) {
                    float f3 = glyphInfo[length2 + 0];
                    fMin = Math.min(fMin, f3);
                    fMax = Math.max(fMax, f3 + f2);
                }
                float f4 = glyphInfo[length2 + 6];
                if (f4 != 0.0f) {
                    float f5 = glyphInfo[length2 + 4];
                    float f6 = glyphInfo[length2 + 5];
                    fMin2 = Math.min(fMin2, f5);
                    fMin3 = Math.min(fMin3, f6);
                    fMax2 = Math.max(fMax2, f5 + f4);
                    fMax3 = Math.max(fMax3, f6 + glyphInfo[length2 + 7]);
                }
                iMin = Math.min(iMin, glyphCharIndices[i7]);
                iMax = Math.max(i2, glyphCharIndices[i7]);
            }
            if (0 != 0) {
                System.out.println("minIndex = " + iMin + ", maxIndex = " + i2);
            }
            i5 = i2 + 1;
            glyphInfo[length + 1] = 0.0f;
            glyphInfo[length + 3] = 0.0f;
            if (z4) {
                glyphInfo[length + 0] = fMin;
                glyphInfo[length + 2] = fMax - fMin;
                glyphInfo[length + 4] = fMin2;
                glyphInfo[length + 5] = fMin3;
                glyphInfo[length + 6] = fMax2 - fMin2;
                glyphInfo[length + 7] = fMax3 - fMin3;
                if (i2 - iMin < i12) {
                    z3 = true;
                }
                if (iMin < i2) {
                    if (!z2) {
                        fMax = fMin;
                    }
                    fMax2 -= fMin2;
                    fMax3 -= fMin3;
                    int i14 = iMin;
                    int i15 = length / 8;
                    while (iMin < i2) {
                        iMin++;
                        i6 += i10;
                        length += i9;
                        if ((length < 0 || length >= glyphInfo.length) && 0 != 0) {
                            System.out.println("minIndex = " + i14 + ", maxIndex = " + i2 + ", cp = " + i15);
                        }
                        glyphInfo[length + 0] = fMax;
                        glyphInfo[length + 1] = 0.0f;
                        glyphInfo[length + 2] = 0.0f;
                        glyphInfo[length + 3] = 0.0f;
                        glyphInfo[length + 4] = fMin2;
                        glyphInfo[length + 5] = fMin3;
                        glyphInfo[length + 6] = fMax2;
                        glyphInfo[length + 7] = fMax3;
                    }
                }
            } else if (z3) {
                int i16 = length2 - i9;
                glyphInfo[length + 0] = glyphInfo[i16 + 0];
                glyphInfo[length + 2] = glyphInfo[i16 + 2];
                glyphInfo[length + 4] = glyphInfo[i16 + 4];
                glyphInfo[length + 5] = glyphInfo[i16 + 5];
                glyphInfo[length + 6] = glyphInfo[i16 + 6];
                glyphInfo[length + 7] = glyphInfo[i16 + 7];
            }
            length += i9;
            i6 += i10;
        }
        if (z3 && !z2) {
            int i17 = length - i9;
            System.arraycopy(glyphInfo, i17, glyphInfo, 0, glyphInfo.length - i17);
        }
        if (0 != 0) {
            char[] chars = this.source.getChars();
            int start = this.source.getStart();
            int length3 = this.source.getLength();
            System.out.println("char info for " + length3 + " characters");
            int i18 = 0;
            while (i18 < length3 * 8) {
                PrintStream printStream = System.out;
                StringBuilder sbAppend = new StringBuilder().append(" ch: ").append(Integer.toHexString(chars[start + v2l(i18 / 8)])).append(" x: ");
                int i19 = i18;
                int i20 = i18 + 1;
                int i21 = i20 + 1;
                StringBuilder sbAppend2 = sbAppend.append(glyphInfo[i19]).append(" y: ").append(glyphInfo[i20]).append(" xa: ");
                int i22 = i21 + 1;
                StringBuilder sbAppend3 = sbAppend2.append(glyphInfo[i21]).append(" ya: ");
                int i23 = i22 + 1;
                StringBuilder sbAppend4 = sbAppend3.append(glyphInfo[i22]).append(" l: ");
                int i24 = i23 + 1;
                StringBuilder sbAppend5 = sbAppend4.append(glyphInfo[i23]).append(" t: ");
                int i25 = i24 + 1;
                StringBuilder sbAppend6 = sbAppend5.append(glyphInfo[i24]).append(" w: ");
                int i26 = i25 + 1;
                StringBuilder sbAppend7 = sbAppend6.append(glyphInfo[i25]).append(" h: ");
                i18 = i26 + 1;
                printStream.println(sbAppend7.append(glyphInfo[i26]).toString());
            }
        }
        return glyphInfo;
    }

    protected int l2v(int i2) {
        return (this.source.getLayoutFlags() & 1) == 0 ? i2 : (this.source.getLength() - 1) - i2;
    }

    protected int v2l(int i2) {
        return (this.source.getLayoutFlags() & 1) == 0 ? i2 : (this.source.getLength() - 1) - i2;
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public TextLineComponent getSubset(int i2, int i3, int i4) {
        return new ExtendedTextSourceLabel(this.source.getSubSource(i2, i3 - i2, i4), this.decorator);
    }

    public String toString() {
        TextSource textSource = this.source;
        TextSource textSource2 = this.source;
        return textSource.toString(false);
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public int getNumJustificationInfos() {
        return getGV().getNumGlyphs();
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public void getJustificationInfos(GlyphJustificationInfo[] glyphJustificationInfoArr, int i2, int i3, int i4) {
        StandardGlyphVector gv = getGV();
        float[] charinfo = getCharinfo();
        float size2D = gv.getFont().getSize2D();
        GlyphJustificationInfo glyphJustificationInfo = new GlyphJustificationInfo(0.0f, false, 3, 0.0f, 0.0f, false, 3, 0.0f, 0.0f);
        GlyphJustificationInfo glyphJustificationInfo2 = new GlyphJustificationInfo(size2D, true, 1, 0.0f, size2D, true, 1, 0.0f, size2D / 4.0f);
        GlyphJustificationInfo glyphJustificationInfo3 = new GlyphJustificationInfo(size2D, true, 2, size2D, size2D, false, 3, 0.0f, 0.0f);
        char[] chars = this.source.getChars();
        int start = this.source.getStart();
        int numGlyphs = gv.getNumGlyphs();
        int i5 = 0;
        int i6 = numGlyphs;
        boolean z2 = (this.source.getLayoutFlags() & 1) == 0;
        if (i3 != 0 || i4 != this.source.getLength()) {
            if (z2) {
                i5 = i3;
                i6 = i4;
            } else {
                i5 = numGlyphs - i4;
                i6 = numGlyphs - i3;
            }
        }
        for (int i7 = 0; i7 < numGlyphs; i7++) {
            GlyphJustificationInfo glyphJustificationInfo4 = null;
            if (i7 >= i5 && i7 < i6) {
                if (charinfo[(i7 * 8) + 2] == 0.0f) {
                    glyphJustificationInfo4 = glyphJustificationInfo;
                } else {
                    char c2 = chars[start + v2l(i7)];
                    glyphJustificationInfo4 = Character.isWhitespace(c2) ? glyphJustificationInfo2 : ((c2 < 19968 || c2 >= 40960) && (c2 < 44032 || c2 >= 55216) && (c2 < 63744 || c2 >= 64256)) ? glyphJustificationInfo : glyphJustificationInfo3;
                }
            }
            glyphJustificationInfoArr[i2 + i7] = glyphJustificationInfo4;
        }
    }

    @Override // sun.font.ExtendedTextLabel, sun.font.TextLineComponent
    public TextLineComponent applyJustificationDeltas(float[] fArr, int i2, boolean[] zArr) {
        float f2;
        float f3;
        float[] fArr2 = (float[]) getCharinfo().clone();
        zArr[0] = false;
        StandardGlyphVector standardGlyphVector = (StandardGlyphVector) getGV().clone();
        float[] glyphPositions = standardGlyphVector.getGlyphPositions(null);
        int numGlyphs = standardGlyphVector.getNumGlyphs();
        char[] chars = this.source.getChars();
        int start = this.source.getStart();
        float f4 = 0.0f;
        for (int i3 = 0; i3 < numGlyphs; i3++) {
            if (Character.isWhitespace(chars[start + v2l(i3)])) {
                int i4 = i3 * 2;
                glyphPositions[i4] = glyphPositions[i4] + f4;
                float f5 = fArr[i2 + (i3 * 2)] + fArr[i2 + (i3 * 2) + 1];
                int i5 = (i3 * 8) + 0;
                fArr2[i5] = fArr2[i5] + f4;
                int i6 = (i3 * 8) + 4;
                fArr2[i6] = fArr2[i6] + f4;
                int i7 = (i3 * 8) + 2;
                fArr2[i7] = fArr2[i7] + f5;
                f2 = f4;
                f3 = f5;
            } else {
                float f6 = f4 + fArr[i2 + (i3 * 2)];
                int i8 = i3 * 2;
                glyphPositions[i8] = glyphPositions[i8] + f6;
                int i9 = (i3 * 8) + 0;
                fArr2[i9] = fArr2[i9] + f6;
                int i10 = (i3 * 8) + 4;
                fArr2[i10] = fArr2[i10] + f6;
                f2 = f6;
                f3 = fArr[i2 + (i3 * 2) + 1];
            }
            f4 = f2 + f3;
        }
        int i11 = numGlyphs * 2;
        glyphPositions[i11] = glyphPositions[i11] + f4;
        standardGlyphVector.setGlyphPositions(glyphPositions);
        ExtendedTextSourceLabel extendedTextSourceLabel = new ExtendedTextSourceLabel(this.source, this.decorator);
        extendedTextSourceLabel.gv = standardGlyphVector;
        extendedTextSourceLabel.charinfo = fArr2;
        return extendedTextSourceLabel;
    }
}
