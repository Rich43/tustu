package java.awt.font;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLine;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;
import sun.font.AttributeValues;
import sun.font.CoreMetrics;
import sun.font.FontResolver;
import sun.font.GraphicComponent;
import sun.font.LayoutPathImpl;
import sun.security.x509.IssuingDistributionPointExtension;
import sun.text.CodePointIterator;

/* loaded from: rt.jar:java/awt/font/TextLayout.class */
public final class TextLayout implements Cloneable {
    private int characterCount;
    private byte baseline;
    private float[] baselineOffsets;
    private TextLine textLine;
    private float visibleAdvance;
    private int hashCodeCache;
    private float justifyRatio;
    private static final float ALREADY_JUSTIFIED = -53.9f;
    private static float dx;
    private static float dy;
    public static final CaretPolicy DEFAULT_CARET_POLICY = new CaretPolicy();
    private boolean isVerticalLine = false;
    private TextLine.TextLineMetrics lineMetrics = null;
    private boolean cacheIsValid = false;
    private Rectangle2D naturalBounds = null;
    private Rectangle2D boundsRect = null;
    private boolean caretsInLigaturesAreAllowed = false;

    /* loaded from: rt.jar:java/awt/font/TextLayout$CaretPolicy.class */
    public static class CaretPolicy {
        public TextHitInfo getStrongCaret(TextHitInfo textHitInfo, TextHitInfo textHitInfo2, TextLayout textLayout) {
            return textLayout.getStrongHit(textHitInfo, textHitInfo2);
        }
    }

    public TextLayout(String str, Font font, FontRenderContext fontRenderContext) {
        if (font == null) {
            throw new IllegalArgumentException("Null font passed to TextLayout constructor.");
        }
        if (str == null) {
            throw new IllegalArgumentException("Null string passed to TextLayout constructor.");
        }
        if (str.length() == 0) {
            throw new IllegalArgumentException("Zero length string passed to TextLayout constructor.");
        }
        Map<TextAttribute, ?> attributes = font.hasLayoutAttributes() ? font.getAttributes() : null;
        char[] charArray = str.toCharArray();
        if (sameBaselineUpTo(font, charArray, 0, charArray.length) == charArray.length) {
            fastInit(charArray, font, attributes, fontRenderContext);
            return;
        }
        AttributedString attributedString = attributes == null ? new AttributedString(str) : new AttributedString(str, attributes);
        attributedString.addAttribute(TextAttribute.FONT, font);
        standardInit(attributedString.getIterator(), charArray, fontRenderContext);
    }

    public TextLayout(String str, Map<? extends AttributedCharacterIterator.Attribute, ?> map, FontRenderContext fontRenderContext) {
        if (str == null) {
            throw new IllegalArgumentException("Null string passed to TextLayout constructor.");
        }
        if (map == null) {
            throw new IllegalArgumentException("Null map passed to TextLayout constructor.");
        }
        if (str.length() == 0) {
            throw new IllegalArgumentException("Zero length string passed to TextLayout constructor.");
        }
        char[] charArray = str.toCharArray();
        Font fontSingleFont = singleFont(charArray, 0, charArray.length, map);
        if (fontSingleFont != null) {
            fastInit(charArray, fontSingleFont, map, fontRenderContext);
        } else {
            standardInit(new AttributedString(str, map).getIterator(), charArray, fontRenderContext);
        }
    }

    private static Font singleFont(char[] cArr, int i2, int i3, Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        if (map.get(TextAttribute.CHAR_REPLACEMENT) != null) {
            return null;
        }
        Font font = null;
        try {
            font = (Font) map.get(TextAttribute.FONT);
        } catch (ClassCastException e2) {
        }
        if (font == null) {
            if (map.get(TextAttribute.FAMILY) != null) {
                font = Font.getFont(map);
                if (font.canDisplayUpTo(cArr, i2, i3) != -1) {
                    return null;
                }
            } else {
                FontResolver fontResolver = FontResolver.getInstance();
                CodePointIterator codePointIteratorCreate = CodePointIterator.create(cArr, i2, i3);
                int iNextFontRunIndex = fontResolver.nextFontRunIndex(codePointIteratorCreate);
                if (codePointIteratorCreate.charIndex() == i3) {
                    font = fontResolver.getFont(iNextFontRunIndex, map);
                }
            }
        }
        if (sameBaselineUpTo(font, cArr, i2, i3) != i3) {
            return null;
        }
        return font;
    }

    public TextLayout(AttributedCharacterIterator attributedCharacterIterator, FontRenderContext fontRenderContext) {
        Map<AttributedCharacterIterator.Attribute, Object> attributes;
        Font fontSingleFont;
        if (attributedCharacterIterator == null) {
            throw new IllegalArgumentException("Null iterator passed to TextLayout constructor.");
        }
        int beginIndex = attributedCharacterIterator.getBeginIndex();
        int endIndex = attributedCharacterIterator.getEndIndex();
        if (beginIndex == endIndex) {
            throw new IllegalArgumentException("Zero length iterator passed to TextLayout constructor.");
        }
        int i2 = endIndex - beginIndex;
        attributedCharacterIterator.first();
        char[] cArr = new char[i2];
        int i3 = 0;
        char cFirst = attributedCharacterIterator.first();
        while (true) {
            char c2 = cFirst;
            if (c2 == 65535) {
                break;
            }
            int i4 = i3;
            i3++;
            cArr[i4] = c2;
            cFirst = attributedCharacterIterator.next();
        }
        attributedCharacterIterator.first();
        if (attributedCharacterIterator.getRunLimit() == endIndex && (fontSingleFont = singleFont(cArr, 0, i2, (attributes = attributedCharacterIterator.getAttributes()))) != null) {
            fastInit(cArr, fontSingleFont, attributes, fontRenderContext);
        } else {
            standardInit(attributedCharacterIterator, cArr, fontRenderContext);
        }
    }

    TextLayout(TextLine textLine, byte b2, float[] fArr, float f2) {
        this.characterCount = textLine.characterCount();
        this.baseline = b2;
        this.baselineOffsets = fArr;
        this.textLine = textLine;
        this.justifyRatio = f2;
    }

    private void paragraphInit(byte b2, CoreMetrics coreMetrics, Map<? extends AttributedCharacterIterator.Attribute, ?> map, char[] cArr) {
        this.baseline = b2;
        this.baselineOffsets = TextLine.getNormalizedOffsets(coreMetrics.baselineOffsets, this.baseline);
        this.justifyRatio = AttributeValues.getJustification(map);
        NumericShaper numericShaping = AttributeValues.getNumericShaping(map);
        if (numericShaping != null) {
            numericShaping.shape(cArr, 0, cArr.length);
        }
    }

    private void fastInit(char[] cArr, Font font, Map<? extends AttributedCharacterIterator.Attribute, ?> map, FontRenderContext fontRenderContext) {
        this.isVerticalLine = false;
        CoreMetrics coreMetrics = CoreMetrics.get(font.getLineMetrics(cArr, 0, cArr.length, fontRenderContext));
        byte b2 = (byte) coreMetrics.baselineIndex;
        if (map == null) {
            this.baseline = b2;
            this.baselineOffsets = coreMetrics.baselineOffsets;
            this.justifyRatio = 1.0f;
        } else {
            paragraphInit(b2, coreMetrics, map, cArr);
        }
        this.characterCount = cArr.length;
        this.textLine = TextLine.fastCreateTextLine(fontRenderContext, cArr, font, coreMetrics, map);
    }

    private void standardInit(AttributedCharacterIterator attributedCharacterIterator, char[] cArr, FontRenderContext fontRenderContext) {
        this.characterCount = cArr.length;
        Map<AttributedCharacterIterator.Attribute, Object> attributes = attributedCharacterIterator.getAttributes();
        if (TextLine.advanceToFirstFont(attributedCharacterIterator)) {
            Font fontAtCurrentPos = TextLine.getFontAtCurrentPos(attributedCharacterIterator);
            int index = attributedCharacterIterator.getIndex() - attributedCharacterIterator.getBeginIndex();
            CoreMetrics coreMetrics = CoreMetrics.get(fontAtCurrentPos.getLineMetrics(cArr, index, index + 1, fontRenderContext));
            paragraphInit((byte) coreMetrics.baselineIndex, coreMetrics, attributes, cArr);
        } else {
            GraphicAttribute graphicAttribute = (GraphicAttribute) attributes.get(TextAttribute.CHAR_REPLACEMENT);
            paragraphInit(getBaselineFromGraphic(graphicAttribute), GraphicComponent.createCoreMetrics(graphicAttribute), attributes, cArr);
        }
        this.textLine = TextLine.standardCreateTextLine(fontRenderContext, attributedCharacterIterator, cArr, this.baselineOffsets);
    }

    private void ensureCache() {
        if (!this.cacheIsValid) {
            buildCache();
        }
    }

    private void buildCache() {
        this.lineMetrics = this.textLine.getMetrics();
        if (this.textLine.isDirectionLTR()) {
            int i2 = this.characterCount - 1;
            while (i2 != -1) {
                if (!this.textLine.isCharSpace(this.textLine.visualToLogical(i2))) {
                    break;
                } else {
                    i2--;
                }
            }
            if (i2 == this.characterCount - 1) {
                this.visibleAdvance = this.lineMetrics.advance;
            } else if (i2 == -1) {
                this.visibleAdvance = 0.0f;
            } else {
                int iVisualToLogical = this.textLine.visualToLogical(i2);
                this.visibleAdvance = this.textLine.getCharLinePosition(iVisualToLogical) + this.textLine.getCharAdvance(iVisualToLogical);
            }
        } else {
            int i3 = 0;
            while (i3 != this.characterCount) {
                if (!this.textLine.isCharSpace(this.textLine.visualToLogical(i3))) {
                    break;
                } else {
                    i3++;
                }
            }
            if (i3 == this.characterCount) {
                this.visibleAdvance = 0.0f;
            } else if (i3 == 0) {
                this.visibleAdvance = this.lineMetrics.advance;
            } else {
                this.visibleAdvance = this.lineMetrics.advance - this.textLine.getCharLinePosition(this.textLine.visualToLogical(i3));
            }
        }
        this.naturalBounds = null;
        this.boundsRect = null;
        this.hashCodeCache = 0;
        this.cacheIsValid = true;
    }

    private Rectangle2D getNaturalBounds() {
        ensureCache();
        if (this.naturalBounds == null) {
            this.naturalBounds = this.textLine.getItalicBounds();
        }
        return this.naturalBounds;
    }

    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    private void checkTextHit(TextHitInfo textHitInfo) {
        if (textHitInfo == null) {
            throw new IllegalArgumentException("TextHitInfo is null.");
        }
        if (textHitInfo.getInsertionIndex() < 0 || textHitInfo.getInsertionIndex() > this.characterCount) {
            throw new IllegalArgumentException("TextHitInfo is out of range");
        }
    }

    public TextLayout getJustifiedLayout(float f2) {
        if (f2 <= 0.0f) {
            throw new IllegalArgumentException("justificationWidth <= 0 passed to TextLayout.getJustifiedLayout()");
        }
        if (this.justifyRatio == ALREADY_JUSTIFIED) {
            throw new Error("Can't justify again.");
        }
        ensureCache();
        int i2 = this.characterCount;
        while (i2 > 0 && this.textLine.isCharWhitespace(i2 - 1)) {
            i2--;
        }
        TextLine justifiedLine = this.textLine.getJustifiedLine(f2, this.justifyRatio, 0, i2);
        if (justifiedLine != null) {
            return new TextLayout(justifiedLine, this.baseline, this.baselineOffsets, ALREADY_JUSTIFIED);
        }
        return this;
    }

    protected void handleJustify(float f2) {
    }

    public byte getBaseline() {
        return this.baseline;
    }

    public float[] getBaselineOffsets() {
        float[] fArr = new float[this.baselineOffsets.length];
        System.arraycopy(this.baselineOffsets, 0, fArr, 0, fArr.length);
        return fArr;
    }

    public float getAdvance() {
        ensureCache();
        return this.lineMetrics.advance;
    }

    public float getVisibleAdvance() {
        ensureCache();
        return this.visibleAdvance;
    }

    public float getAscent() {
        ensureCache();
        return this.lineMetrics.ascent;
    }

    public float getDescent() {
        ensureCache();
        return this.lineMetrics.descent;
    }

    public float getLeading() {
        ensureCache();
        return this.lineMetrics.leading;
    }

    public Rectangle2D getBounds() {
        ensureCache();
        if (this.boundsRect == null) {
            Rectangle2D visualBounds = this.textLine.getVisualBounds();
            if (dx != 0.0f || dy != 0.0f) {
                visualBounds.setRect(visualBounds.getX() - dx, visualBounds.getY() - dy, visualBounds.getWidth(), visualBounds.getHeight());
            }
            this.boundsRect = visualBounds;
        }
        Rectangle2D.Float r0 = new Rectangle2D.Float();
        r0.setRect(this.boundsRect);
        return r0;
    }

    public Rectangle getPixelBounds(FontRenderContext fontRenderContext, float f2, float f3) {
        return this.textLine.getPixelBounds(fontRenderContext, f2, f3);
    }

    public boolean isLeftToRight() {
        return this.textLine.isDirectionLTR();
    }

    public boolean isVertical() {
        return this.isVerticalLine;
    }

    public int getCharacterCount() {
        return this.characterCount;
    }

    private float[] getCaretInfo(int i2, Rectangle2D rectangle2D, float[] fArr) {
        int iVisualToLogical;
        float charLinePosition;
        float charAscent;
        float charAscent2;
        float charDescent;
        float charDescent2;
        if (i2 == 0 || i2 == this.characterCount) {
            if (i2 == this.characterCount) {
                iVisualToLogical = this.textLine.visualToLogical(this.characterCount - 1);
                charLinePosition = this.textLine.getCharLinePosition(iVisualToLogical) + this.textLine.getCharAdvance(iVisualToLogical);
            } else {
                iVisualToLogical = this.textLine.visualToLogical(i2);
                charLinePosition = this.textLine.getCharLinePosition(iVisualToLogical);
            }
            float charAngle = this.textLine.getCharAngle(iVisualToLogical);
            float charShift = charLinePosition + (charAngle * this.textLine.getCharShift(iVisualToLogical));
            float charAscent3 = charShift + (charAngle * this.textLine.getCharAscent(iVisualToLogical));
            charAscent = charAscent3;
            charAscent2 = charAscent3;
            float charDescent3 = charShift - (charAngle * this.textLine.getCharDescent(iVisualToLogical));
            charDescent = charDescent3;
            charDescent2 = charDescent3;
        } else {
            int iVisualToLogical2 = this.textLine.visualToLogical(i2 - 1);
            float charAngle2 = this.textLine.getCharAngle(iVisualToLogical2);
            float charLinePosition2 = this.textLine.getCharLinePosition(iVisualToLogical2) + this.textLine.getCharAdvance(iVisualToLogical2);
            if (charAngle2 != 0.0f) {
                float charShift2 = charLinePosition2 + (charAngle2 * this.textLine.getCharShift(iVisualToLogical2));
                charAscent2 = charShift2 + (charAngle2 * this.textLine.getCharAscent(iVisualToLogical2));
                charDescent2 = charShift2 - (charAngle2 * this.textLine.getCharDescent(iVisualToLogical2));
            } else {
                charDescent2 = charLinePosition2;
                charAscent2 = charLinePosition2;
            }
            int iVisualToLogical3 = this.textLine.visualToLogical(i2);
            float charAngle3 = this.textLine.getCharAngle(iVisualToLogical3);
            float charLinePosition3 = this.textLine.getCharLinePosition(iVisualToLogical3);
            if (charAngle3 != 0.0f) {
                float charShift3 = charLinePosition3 + (charAngle3 * this.textLine.getCharShift(iVisualToLogical3));
                charAscent = charShift3 + (charAngle3 * this.textLine.getCharAscent(iVisualToLogical3));
                charDescent = charShift3 - (charAngle3 * this.textLine.getCharDescent(iVisualToLogical3));
            } else {
                charDescent = charLinePosition3;
                charAscent = charLinePosition3;
            }
        }
        float f2 = (charAscent2 + charAscent) / 2.0f;
        float f3 = (charDescent2 + charDescent) / 2.0f;
        if (fArr == null) {
            fArr = new float[2];
        }
        if (this.isVerticalLine) {
            fArr[1] = (float) ((f2 - f3) / rectangle2D.getWidth());
            fArr[0] = (float) (f2 + (fArr[1] * rectangle2D.getX()));
        } else {
            fArr[1] = (float) ((f2 - f3) / rectangle2D.getHeight());
            fArr[0] = (float) (f3 + (fArr[1] * rectangle2D.getMaxY()));
        }
        return fArr;
    }

    public float[] getCaretInfo(TextHitInfo textHitInfo, Rectangle2D rectangle2D) {
        ensureCache();
        checkTextHit(textHitInfo);
        return getCaretInfoTestInternal(textHitInfo, rectangle2D);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [float[]] */
    private float[] getCaretInfoTestInternal(TextHitInfo textHitInfo, Rectangle2D rectangle2D) {
        double d2;
        double d3;
        double d4;
        double d5;
        ensureCache();
        checkTextHit(textHitInfo);
        ?? r0 = new float[6];
        getCaretInfo(hitToCaret(textHitInfo), rectangle2D, r0);
        int charIndex = textHitInfo.getCharIndex();
        boolean zIsLeadingEdge = textHitInfo.isLeadingEdge();
        boolean zIsDirectionLTR = this.textLine.isDirectionLTR();
        boolean z2 = !isVertical();
        if (charIndex == -1 || charIndex == this.characterCount) {
            TextLine.TextLineMetrics metrics = this.textLine.getMetrics();
            boolean z3 = zIsDirectionLTR == (charIndex == -1);
            if (z2) {
                double d6 = z3 ? 0.0d : metrics.advance;
                d5 = d6;
                d4 = d6;
                d3 = -metrics.ascent;
                d2 = metrics.descent;
            } else {
                double d7 = z3 ? 0.0d : metrics.advance;
                d2 = d7;
                d3 = d7;
                d4 = metrics.descent;
                d5 = metrics.ascent;
            }
        } else {
            CoreMetrics coreMetricsAt = this.textLine.getCoreMetricsAt(charIndex);
            double d8 = coreMetricsAt.italicAngle;
            double charLinePosition = this.textLine.getCharLinePosition(charIndex, zIsLeadingEdge);
            if (coreMetricsAt.baselineIndex < 0) {
                TextLine.TextLineMetrics metrics2 = this.textLine.getMetrics();
                if (z2) {
                    d5 = charLinePosition;
                    d4 = charLinePosition;
                    if (coreMetricsAt.baselineIndex == -1) {
                        d3 = -metrics2.ascent;
                        d2 = d3 + coreMetricsAt.height;
                    } else {
                        d2 = metrics2.descent;
                        d3 = d2 - coreMetricsAt.height;
                    }
                } else {
                    d2 = r0;
                    d3 = charLinePosition;
                    d4 = metrics2.descent;
                    d5 = metrics2.ascent;
                }
            } else {
                float f2 = this.baselineOffsets[coreMetricsAt.baselineIndex];
                if (z2) {
                    double d9 = charLinePosition + (d8 * coreMetricsAt.ssOffset);
                    d4 = d9 + (d8 * coreMetricsAt.ascent);
                    d5 = d9 - (d8 * coreMetricsAt.descent);
                    d3 = f2 - coreMetricsAt.ascent;
                    d2 = f2 + coreMetricsAt.descent;
                } else {
                    double d10 = charLinePosition - (d8 * coreMetricsAt.ssOffset);
                    d3 = d10 + (d8 * coreMetricsAt.ascent);
                    d2 = d10 - (d8 * coreMetricsAt.descent);
                    d4 = f2 + coreMetricsAt.ascent;
                    d5 = f2 + coreMetricsAt.descent;
                }
            }
        }
        r0[2] = (float) d4;
        r0[3] = (float) d3;
        r0[4] = (float) d5;
        r0[5] = (float) d2;
        return r0;
    }

    public float[] getCaretInfo(TextHitInfo textHitInfo) {
        return getCaretInfo(textHitInfo, getNaturalBounds());
    }

    private int hitToCaret(TextHitInfo textHitInfo) {
        int charIndex = textHitInfo.getCharIndex();
        if (charIndex < 0) {
            if (this.textLine.isDirectionLTR()) {
                return 0;
            }
            return this.characterCount;
        }
        if (charIndex >= this.characterCount) {
            if (this.textLine.isDirectionLTR()) {
                return this.characterCount;
            }
            return 0;
        }
        int iLogicalToVisual = this.textLine.logicalToVisual(charIndex);
        if (textHitInfo.isLeadingEdge() != this.textLine.isCharLTR(charIndex)) {
            iLogicalToVisual++;
        }
        return iLogicalToVisual;
    }

    private TextHitInfo caretToHit(int i2) {
        if (i2 == 0 || i2 == this.characterCount) {
            if ((i2 == this.characterCount) == this.textLine.isDirectionLTR()) {
                return TextHitInfo.leading(this.characterCount);
            }
            return TextHitInfo.trailing(-1);
        }
        int iVisualToLogical = this.textLine.visualToLogical(i2);
        return this.textLine.isCharLTR(iVisualToLogical) ? TextHitInfo.leading(iVisualToLogical) : TextHitInfo.trailing(iVisualToLogical);
    }

    private boolean caretIsValid(int i2) {
        if (i2 == this.characterCount || i2 == 0) {
            return true;
        }
        int iVisualToLogical = this.textLine.visualToLogical(i2);
        if (!this.textLine.isCharLTR(iVisualToLogical)) {
            iVisualToLogical = this.textLine.visualToLogical(i2 - 1);
            if (this.textLine.isCharLTR(iVisualToLogical)) {
                return true;
            }
        }
        return this.textLine.caretAtOffsetIsValid(iVisualToLogical);
    }

    public TextHitInfo getNextRightHit(TextHitInfo textHitInfo) {
        ensureCache();
        checkTextHit(textHitInfo);
        int iHitToCaret = hitToCaret(textHitInfo);
        if (iHitToCaret == this.characterCount) {
            return null;
        }
        do {
            iHitToCaret++;
        } while (!caretIsValid(iHitToCaret));
        return caretToHit(iHitToCaret);
    }

    public TextHitInfo getNextRightHit(int i2, CaretPolicy caretPolicy) {
        if (i2 < 0 || i2 > this.characterCount) {
            throw new IllegalArgumentException("Offset out of bounds in TextLayout.getNextRightHit()");
        }
        if (caretPolicy == null) {
            throw new IllegalArgumentException("Null CaretPolicy passed to TextLayout.getNextRightHit()");
        }
        TextHitInfo textHitInfoAfterOffset = TextHitInfo.afterOffset(i2);
        TextHitInfo nextRightHit = getNextRightHit(caretPolicy.getStrongCaret(textHitInfoAfterOffset, textHitInfoAfterOffset.getOtherHit(), this));
        if (nextRightHit != null) {
            return caretPolicy.getStrongCaret(getVisualOtherHit(nextRightHit), nextRightHit, this);
        }
        return null;
    }

    public TextHitInfo getNextRightHit(int i2) {
        return getNextRightHit(i2, DEFAULT_CARET_POLICY);
    }

    public TextHitInfo getNextLeftHit(TextHitInfo textHitInfo) {
        ensureCache();
        checkTextHit(textHitInfo);
        int iHitToCaret = hitToCaret(textHitInfo);
        if (iHitToCaret == 0) {
            return null;
        }
        do {
            iHitToCaret--;
        } while (!caretIsValid(iHitToCaret));
        return caretToHit(iHitToCaret);
    }

    public TextHitInfo getNextLeftHit(int i2, CaretPolicy caretPolicy) {
        if (caretPolicy == null) {
            throw new IllegalArgumentException("Null CaretPolicy passed to TextLayout.getNextLeftHit()");
        }
        if (i2 < 0 || i2 > this.characterCount) {
            throw new IllegalArgumentException("Offset out of bounds in TextLayout.getNextLeftHit()");
        }
        TextHitInfo textHitInfoAfterOffset = TextHitInfo.afterOffset(i2);
        TextHitInfo nextLeftHit = getNextLeftHit(caretPolicy.getStrongCaret(textHitInfoAfterOffset, textHitInfoAfterOffset.getOtherHit(), this));
        if (nextLeftHit != null) {
            return caretPolicy.getStrongCaret(getVisualOtherHit(nextLeftHit), nextLeftHit, this);
        }
        return null;
    }

    public TextHitInfo getNextLeftHit(int i2) {
        return getNextLeftHit(i2, DEFAULT_CARET_POLICY);
    }

    public TextHitInfo getVisualOtherHit(TextHitInfo textHitInfo) {
        int i2;
        int iVisualToLogical;
        boolean zIsCharLTR;
        int i3;
        boolean z2;
        ensureCache();
        checkTextHit(textHitInfo);
        int charIndex = textHitInfo.getCharIndex();
        if (charIndex == -1 || charIndex == this.characterCount) {
            if (this.textLine.isDirectionLTR() == (charIndex == -1)) {
                i2 = 0;
            } else {
                i2 = this.characterCount - 1;
            }
            iVisualToLogical = this.textLine.visualToLogical(i2);
            if (this.textLine.isDirectionLTR() == (charIndex == -1)) {
                zIsCharLTR = this.textLine.isCharLTR(iVisualToLogical);
            } else {
                zIsCharLTR = !this.textLine.isCharLTR(iVisualToLogical);
            }
        } else {
            int iLogicalToVisual = this.textLine.logicalToVisual(charIndex);
            if (this.textLine.isCharLTR(charIndex) == textHitInfo.isLeadingEdge()) {
                i3 = iLogicalToVisual - 1;
                z2 = false;
            } else {
                i3 = iLogicalToVisual + 1;
                z2 = true;
            }
            if (i3 > -1 && i3 < this.characterCount) {
                iVisualToLogical = this.textLine.visualToLogical(i3);
                zIsCharLTR = z2 == this.textLine.isCharLTR(iVisualToLogical);
            } else {
                iVisualToLogical = z2 == this.textLine.isDirectionLTR() ? this.characterCount : -1;
                zIsCharLTR = iVisualToLogical == this.characterCount;
            }
        }
        return zIsCharLTR ? TextHitInfo.leading(iVisualToLogical) : TextHitInfo.trailing(iVisualToLogical);
    }

    private double[] getCaretPath(TextHitInfo textHitInfo, Rectangle2D rectangle2D) {
        float[] caretInfo = getCaretInfo(textHitInfo, rectangle2D);
        return new double[]{caretInfo[2], caretInfo[3], caretInfo[4], caretInfo[5]};
    }

    private double[] getCaretPath(int i2, Rectangle2D rectangle2D, boolean z2) {
        double d2;
        double d3;
        double d4;
        double d5;
        float[] caretInfo = getCaretInfo(i2, rectangle2D, null);
        double d6 = caretInfo[0];
        double d7 = caretInfo[1];
        double d8 = -3141.59d;
        double d9 = -2.7d;
        double x2 = rectangle2D.getX();
        double width = x2 + rectangle2D.getWidth();
        double y2 = rectangle2D.getY();
        double height = y2 + rectangle2D.getHeight();
        boolean z3 = false;
        if (this.isVerticalLine) {
            if (d7 >= 0.0d) {
                d4 = x2;
                d5 = width;
            } else {
                d5 = x2;
                d4 = width;
            }
            d3 = d6 + (d4 * d7);
            d2 = d6 + (d5 * d7);
            if (z2) {
                if (d3 < y2) {
                    if (d7 <= 0.0d || d2 <= y2) {
                        d2 = 0.0d;
                        d3 = y2;
                    } else {
                        z3 = true;
                        d3 = y2;
                        d9 = y2;
                        d8 = d5 + ((y2 - d2) / d7);
                        if (d2 > height) {
                            d2 = height;
                        }
                    }
                } else if (d2 > height) {
                    if (d7 >= 0.0d || d3 >= height) {
                        d2 = 0.0d;
                        d3 = height;
                    } else {
                        z3 = true;
                        d2 = height;
                        d9 = height;
                        d8 = d4 + ((height - d5) / d7);
                    }
                }
            }
        } else {
            if (d7 >= 0.0d) {
                d3 = height;
                d2 = y2;
            } else {
                d2 = height;
                d3 = y2;
            }
            d4 = d6 - (d3 * d7);
            d5 = d6 - (d2 * d7);
            if (z2) {
                if (d4 < x2) {
                    if (d7 <= 0.0d || d5 <= x2) {
                        d5 = 0.0d;
                        d4 = x2;
                    } else {
                        z3 = true;
                        d4 = x2;
                        d8 = x2;
                        d9 = d2 - ((x2 - d5) / d7);
                        if (d5 > width) {
                            d5 = width;
                        }
                    }
                } else if (d5 > width) {
                    if (d7 >= 0.0d || d4 >= width) {
                        d5 = 0.0d;
                        d4 = width;
                    } else {
                        z3 = true;
                        d5 = width;
                        d8 = width;
                        d9 = d3 - ((width - d4) / d7);
                    }
                }
            }
        }
        return z3 ? new double[]{d4, d3, d8, d9, d5, d2} : new double[]{d4, d3, d5, d2};
    }

    private static GeneralPath pathToShape(double[] dArr, boolean z2, LayoutPathImpl layoutPathImpl) {
        GeneralPath generalPath = new GeneralPath(0, dArr.length);
        generalPath.moveTo((float) dArr[0], (float) dArr[1]);
        for (int i2 = 2; i2 < dArr.length; i2 += 2) {
            generalPath.lineTo((float) dArr[i2], (float) dArr[i2 + 1]);
        }
        if (z2) {
            generalPath.closePath();
        }
        if (layoutPathImpl != null) {
            generalPath = (GeneralPath) layoutPathImpl.mapShape(generalPath);
        }
        return generalPath;
    }

    public Shape getCaretShape(TextHitInfo textHitInfo, Rectangle2D rectangle2D) {
        ensureCache();
        checkTextHit(textHitInfo);
        if (rectangle2D == null) {
            throw new IllegalArgumentException("Null Rectangle2D passed to TextLayout.getCaret()");
        }
        return pathToShape(getCaretPath(textHitInfo, rectangle2D), false, this.textLine.getLayoutPath());
    }

    public Shape getCaretShape(TextHitInfo textHitInfo) {
        return getCaretShape(textHitInfo, getNaturalBounds());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final TextHitInfo getStrongHit(TextHitInfo textHitInfo, TextHitInfo textHitInfo2) {
        byte characterLevel = getCharacterLevel(textHitInfo.getCharIndex());
        byte characterLevel2 = getCharacterLevel(textHitInfo2.getCharIndex());
        if (characterLevel != characterLevel2) {
            return characterLevel < characterLevel2 ? textHitInfo : textHitInfo2;
        }
        if (textHitInfo2.isLeadingEdge() && !textHitInfo.isLeadingEdge()) {
            return textHitInfo2;
        }
        return textHitInfo;
    }

    public byte getCharacterLevel(int i2) {
        if (i2 < -1 || i2 > this.characterCount) {
            throw new IllegalArgumentException("Index is out of range in getCharacterLevel.");
        }
        ensureCache();
        if (i2 == -1 || i2 == this.characterCount) {
            return (byte) (this.textLine.isDirectionLTR() ? 0 : 1);
        }
        return this.textLine.getCharLevel(i2);
    }

    public Shape[] getCaretShapes(int i2, Rectangle2D rectangle2D, CaretPolicy caretPolicy) {
        ensureCache();
        if (i2 < 0 || i2 > this.characterCount) {
            throw new IllegalArgumentException("Offset out of bounds in TextLayout.getCaretShapes()");
        }
        if (rectangle2D == null) {
            throw new IllegalArgumentException("Null Rectangle2D passed to TextLayout.getCaretShapes()");
        }
        if (caretPolicy == null) {
            throw new IllegalArgumentException("Null CaretPolicy passed to TextLayout.getCaretShapes()");
        }
        Shape[] shapeArr = new Shape[2];
        TextHitInfo textHitInfoAfterOffset = TextHitInfo.afterOffset(i2);
        int iHitToCaret = hitToCaret(textHitInfoAfterOffset);
        LayoutPathImpl layoutPath = this.textLine.getLayoutPath();
        GeneralPath generalPathPathToShape = pathToShape(getCaretPath(textHitInfoAfterOffset, rectangle2D), false, layoutPath);
        TextHitInfo otherHit = textHitInfoAfterOffset.getOtherHit();
        if (iHitToCaret == hitToCaret(otherHit)) {
            shapeArr[0] = generalPathPathToShape;
        } else {
            GeneralPath generalPathPathToShape2 = pathToShape(getCaretPath(otherHit, rectangle2D), false, layoutPath);
            if (caretPolicy.getStrongCaret(textHitInfoAfterOffset, otherHit, this).equals(textHitInfoAfterOffset)) {
                shapeArr[0] = generalPathPathToShape;
                shapeArr[1] = generalPathPathToShape2;
            } else {
                shapeArr[0] = generalPathPathToShape2;
                shapeArr[1] = generalPathPathToShape;
            }
        }
        return shapeArr;
    }

    public Shape[] getCaretShapes(int i2, Rectangle2D rectangle2D) {
        return getCaretShapes(i2, rectangle2D, DEFAULT_CARET_POLICY);
    }

    public Shape[] getCaretShapes(int i2) {
        return getCaretShapes(i2, getNaturalBounds(), DEFAULT_CARET_POLICY);
    }

    private GeneralPath boundingShape(double[] dArr, double[] dArr2) {
        boolean z2;
        int length;
        int length2;
        int i2;
        GeneralPath generalPathPathToShape = pathToShape(dArr, false, null);
        if (this.isVerticalLine) {
            z2 = ((dArr[1] > dArr[dArr.length - 1] ? 1 : (dArr[1] == dArr[dArr.length - 1] ? 0 : -1)) > 0) == ((dArr2[1] > dArr2[dArr2.length - 1] ? 1 : (dArr2[1] == dArr2[dArr2.length - 1] ? 0 : -1)) > 0);
        } else {
            z2 = ((dArr[0] > dArr[dArr.length - 2] ? 1 : (dArr[0] == dArr[dArr.length - 2] ? 0 : -1)) > 0) == ((dArr2[0] > dArr2[dArr2.length - 2] ? 1 : (dArr2[0] == dArr2[dArr2.length - 2] ? 0 : -1)) > 0);
        }
        if (z2) {
            length = dArr2.length - 2;
            length2 = -2;
            i2 = -2;
        } else {
            length = 0;
            length2 = dArr2.length;
            i2 = 2;
        }
        int i3 = length;
        while (true) {
            int i4 = i3;
            if (i4 != length2) {
                generalPathPathToShape.lineTo((float) dArr2[i4], (float) dArr2[i4 + 1]);
                i3 = i4 + i2;
            } else {
                generalPathPathToShape.closePath();
                return generalPathPathToShape;
            }
        }
    }

    private GeneralPath caretBoundingShape(int i2, int i3, Rectangle2D rectangle2D) {
        if (i2 > i3) {
            i2 = i3;
            i3 = i2;
        }
        return boundingShape(getCaretPath(i2, rectangle2D, true), getCaretPath(i3, rectangle2D, true));
    }

    private GeneralPath leftShape(Rectangle2D rectangle2D) {
        double[] dArr;
        if (this.isVerticalLine) {
            dArr = new double[]{rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getX() + rectangle2D.getWidth(), rectangle2D.getY()};
        } else {
            dArr = new double[]{rectangle2D.getX(), rectangle2D.getY() + rectangle2D.getHeight(), rectangle2D.getX(), rectangle2D.getY()};
        }
        return boundingShape(dArr, getCaretPath(0, rectangle2D, true));
    }

    private GeneralPath rightShape(Rectangle2D rectangle2D) {
        double[] dArr;
        if (this.isVerticalLine) {
            dArr = new double[]{rectangle2D.getX(), rectangle2D.getY() + rectangle2D.getHeight(), rectangle2D.getX() + rectangle2D.getWidth(), rectangle2D.getY() + rectangle2D.getHeight()};
        } else {
            dArr = new double[]{rectangle2D.getX() + rectangle2D.getWidth(), rectangle2D.getY() + rectangle2D.getHeight(), rectangle2D.getX() + rectangle2D.getWidth(), rectangle2D.getY()};
        }
        return boundingShape(getCaretPath(this.characterCount, rectangle2D, true), dArr);
    }

    public int[] getLogicalRangesForVisualSelection(TextHitInfo textHitInfo, TextHitInfo textHitInfo2) {
        ensureCache();
        checkTextHit(textHitInfo);
        checkTextHit(textHitInfo2);
        boolean[] zArr = new boolean[this.characterCount];
        int iHitToCaret = hitToCaret(textHitInfo);
        int iHitToCaret2 = hitToCaret(textHitInfo2);
        if (iHitToCaret > iHitToCaret2) {
            iHitToCaret = iHitToCaret2;
            iHitToCaret2 = iHitToCaret;
        }
        if (iHitToCaret < iHitToCaret2) {
            for (int i2 = iHitToCaret; i2 < iHitToCaret2; i2++) {
                zArr[this.textLine.visualToLogical(i2)] = true;
            }
        }
        int i3 = 0;
        boolean z2 = false;
        for (int i4 = 0; i4 < this.characterCount; i4++) {
            if (zArr[i4] != z2) {
                z2 = !z2;
                if (z2) {
                    i3++;
                }
            }
        }
        int[] iArr = new int[i3 * 2];
        int i5 = 0;
        boolean z3 = false;
        for (int i6 = 0; i6 < this.characterCount; i6++) {
            if (zArr[i6] != z3) {
                int i7 = i5;
                i5++;
                iArr[i7] = i6;
                z3 = !z3;
            }
        }
        if (z3) {
            int i8 = i5;
            int i9 = i5 + 1;
            iArr[i8] = this.characterCount;
        }
        return iArr;
    }

    public Shape getVisualHighlightShape(TextHitInfo textHitInfo, TextHitInfo textHitInfo2, Rectangle2D rectangle2D) {
        ensureCache();
        checkTextHit(textHitInfo);
        checkTextHit(textHitInfo2);
        if (rectangle2D == null) {
            throw new IllegalArgumentException("Null Rectangle2D passed to TextLayout.getVisualHighlightShape()");
        }
        GeneralPath generalPath = new GeneralPath(0);
        int iHitToCaret = hitToCaret(textHitInfo);
        int iHitToCaret2 = hitToCaret(textHitInfo2);
        generalPath.append((Shape) caretBoundingShape(iHitToCaret, iHitToCaret2, rectangle2D), false);
        if (iHitToCaret == 0 || iHitToCaret2 == 0) {
            GeneralPath generalPathLeftShape = leftShape(rectangle2D);
            if (!generalPathLeftShape.getBounds().isEmpty()) {
                generalPath.append((Shape) generalPathLeftShape, false);
            }
        }
        if (iHitToCaret == this.characterCount || iHitToCaret2 == this.characterCount) {
            GeneralPath generalPathRightShape = rightShape(rectangle2D);
            if (!generalPathRightShape.getBounds().isEmpty()) {
                generalPath.append((Shape) generalPathRightShape, false);
            }
        }
        LayoutPathImpl layoutPath = this.textLine.getLayoutPath();
        if (layoutPath != null) {
            generalPath = (GeneralPath) layoutPath.mapShape(generalPath);
        }
        return generalPath;
    }

    public Shape getVisualHighlightShape(TextHitInfo textHitInfo, TextHitInfo textHitInfo2) {
        return getVisualHighlightShape(textHitInfo, textHitInfo2, getNaturalBounds());
    }

    public Shape getLogicalHighlightShape(int i2, int i3, Rectangle2D rectangle2D) {
        if (rectangle2D == null) {
            throw new IllegalArgumentException("Null Rectangle2D passed to TextLayout.getLogicalHighlightShape()");
        }
        ensureCache();
        if (i2 > i3) {
            i2 = i3;
            i3 = i2;
        }
        if (i2 < 0 || i3 > this.characterCount) {
            throw new IllegalArgumentException("Range is invalid in TextLayout.getLogicalHighlightShape()");
        }
        GeneralPath generalPath = new GeneralPath(0);
        int[] iArr = new int[10];
        int i4 = 0;
        if (i2 < i3) {
            int i5 = i2;
            do {
                int i6 = i4;
                int i7 = i4 + 1;
                iArr[i6] = hitToCaret(TextHitInfo.leading(i5));
                boolean zIsCharLTR = this.textLine.isCharLTR(i5);
                do {
                    i5++;
                    if (i5 >= i3) {
                        break;
                    }
                } while (this.textLine.isCharLTR(i5) == zIsCharLTR);
                i4 = i7 + 1;
                iArr[i7] = hitToCaret(TextHitInfo.trailing(i5 - 1));
                if (i4 == iArr.length) {
                    int[] iArr2 = new int[iArr.length + 10];
                    System.arraycopy(iArr, 0, iArr2, 0, i4);
                    iArr = iArr2;
                }
            } while (i5 < i3);
        } else {
            i4 = 2;
            int iHitToCaret = hitToCaret(TextHitInfo.leading(i2));
            iArr[1] = iHitToCaret;
            iArr[0] = iHitToCaret;
        }
        for (int i8 = 0; i8 < i4; i8 += 2) {
            generalPath.append((Shape) caretBoundingShape(iArr[i8], iArr[i8 + 1], rectangle2D), false);
        }
        if (i2 != i3) {
            if ((this.textLine.isDirectionLTR() && i2 == 0) || (!this.textLine.isDirectionLTR() && i3 == this.characterCount)) {
                GeneralPath generalPathLeftShape = leftShape(rectangle2D);
                if (!generalPathLeftShape.getBounds().isEmpty()) {
                    generalPath.append((Shape) generalPathLeftShape, false);
                }
            }
            if ((this.textLine.isDirectionLTR() && i3 == this.characterCount) || (!this.textLine.isDirectionLTR() && i2 == 0)) {
                GeneralPath generalPathRightShape = rightShape(rectangle2D);
                if (!generalPathRightShape.getBounds().isEmpty()) {
                    generalPath.append((Shape) generalPathRightShape, false);
                }
            }
        }
        LayoutPathImpl layoutPath = this.textLine.getLayoutPath();
        if (layoutPath != null) {
            generalPath = (GeneralPath) layoutPath.mapShape(generalPath);
        }
        return generalPath;
    }

    public Shape getLogicalHighlightShape(int i2, int i3) {
        return getLogicalHighlightShape(i2, i3, getNaturalBounds());
    }

    public Shape getBlackBoxBounds(int i2, int i3) {
        ensureCache();
        if (i2 > i3) {
            i2 = i3;
            i3 = i2;
        }
        if (i2 < 0 || i3 > this.characterCount) {
            throw new IllegalArgumentException("Invalid range passed to TextLayout.getBlackBoxBounds()");
        }
        Path2D generalPath = new GeneralPath(1);
        if (i2 < this.characterCount) {
            for (int i4 = i2; i4 < i3; i4++) {
                Rectangle2D charBounds = this.textLine.getCharBounds(i4);
                if (!charBounds.isEmpty()) {
                    generalPath.append((Shape) charBounds, false);
                }
            }
        }
        if (dx != 0.0f || dy != 0.0f) {
            generalPath = (GeneralPath) AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(generalPath);
        }
        LayoutPathImpl layoutPath = this.textLine.getLayoutPath();
        if (layoutPath != null) {
            generalPath = (GeneralPath) layoutPath.mapShape(generalPath);
        }
        return generalPath;
    }

    private float caretToPointDistance(float[] fArr, float f2, float f3) {
        return ((this.isVerticalLine ? f3 : f2) - fArr[0]) + ((this.isVerticalLine ? -f2 : f3) * fArr[1]);
    }

    public TextHitInfo hitTestChar(float f2, float f3, Rectangle2D rectangle2D) {
        float fEffectiveBaselineOffset;
        LayoutPathImpl layoutPath = this.textLine.getLayoutPath();
        if (layoutPath != null) {
            Point2D.Float r0 = new Point2D.Float(f2, f3);
            layoutPath.pointToPath(r0, r0);
            f2 = r0.f12396x;
            f3 = r0.f12397y;
        }
        if (isVertical()) {
            if (f3 < rectangle2D.getMinY()) {
                return TextHitInfo.leading(0);
            }
            if (f3 >= rectangle2D.getMaxY()) {
                return TextHitInfo.trailing(this.characterCount - 1);
            }
        } else {
            if (f2 < rectangle2D.getMinX()) {
                return isLeftToRight() ? TextHitInfo.leading(0) : TextHitInfo.trailing(this.characterCount - 1);
            }
            if (f2 >= rectangle2D.getMaxX()) {
                return isLeftToRight() ? TextHitInfo.trailing(this.characterCount - 1) : TextHitInfo.leading(0);
            }
        }
        double d2 = Double.MAX_VALUE;
        int i2 = 0;
        int i3 = -1;
        CoreMetrics coreMetrics = null;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        float f9 = 0.0f;
        for (int i4 = 0; i4 < this.characterCount; i4++) {
            if (this.textLine.caretAtOffsetIsValid(i4)) {
                if (i3 == -1) {
                    i3 = i4;
                }
                CoreMetrics coreMetricsAt = this.textLine.getCoreMetricsAt(i4);
                if (coreMetricsAt != coreMetrics) {
                    coreMetrics = coreMetricsAt;
                    if (coreMetricsAt.baselineIndex == -1) {
                        fEffectiveBaselineOffset = (-(this.textLine.getMetrics().ascent - coreMetricsAt.ascent)) + coreMetricsAt.ssOffset;
                    } else if (coreMetricsAt.baselineIndex == -2) {
                        fEffectiveBaselineOffset = (this.textLine.getMetrics().descent - coreMetricsAt.descent) + coreMetricsAt.ssOffset;
                    } else {
                        fEffectiveBaselineOffset = coreMetricsAt.effectiveBaselineOffset(this.baselineOffsets) + coreMetricsAt.ssOffset;
                    }
                    float f10 = ((coreMetricsAt.descent - coreMetricsAt.ascent) / 2.0f) - fEffectiveBaselineOffset;
                    f8 = f10 * coreMetricsAt.italicAngle;
                    f7 = fEffectiveBaselineOffset + f10;
                    f9 = (f7 - f3) * (f7 - f3);
                }
                float charXPosition = this.textLine.getCharXPosition(i4) + ((this.textLine.getCharAdvance(i4) / 2.0f) - f8);
                double dSqrt = Math.sqrt((4.0f * (charXPosition - f2) * (charXPosition - f2)) + f9);
                if (dSqrt < d2) {
                    d2 = dSqrt;
                    i2 = i4;
                    i3 = -1;
                    f4 = charXPosition;
                    f5 = f7;
                    f6 = coreMetricsAt.italicAngle;
                }
            }
        }
        boolean z2 = this.textLine.isCharLTR(i2) == ((f2 > (f4 - ((f3 - f5) * f6)) ? 1 : (f2 == (f4 - ((f3 - f5) * f6)) ? 0 : -1)) < 0);
        if (i3 == -1) {
            i3 = this.characterCount;
        }
        return z2 ? TextHitInfo.leading(i2) : TextHitInfo.trailing(i3 - 1);
    }

    public TextHitInfo hitTestChar(float f2, float f3) {
        return hitTestChar(f2, f3, getNaturalBounds());
    }

    public int hashCode() {
        if (this.hashCodeCache == 0) {
            ensureCache();
            this.hashCodeCache = this.textLine.hashCode();
        }
        return this.hashCodeCache;
    }

    public boolean equals(Object obj) {
        return (obj instanceof TextLayout) && equals((TextLayout) obj);
    }

    public boolean equals(TextLayout textLayout) {
        if (textLayout == null) {
            return false;
        }
        if (textLayout == this) {
            return true;
        }
        ensureCache();
        return this.textLine.equals(textLayout.textLine);
    }

    public String toString() {
        ensureCache();
        return this.textLine.toString();
    }

    public void draw(Graphics2D graphics2D, float f2, float f3) {
        if (graphics2D == null) {
            throw new IllegalArgumentException("Null Graphics2D passed to TextLayout.draw()");
        }
        this.textLine.draw(graphics2D, f2 - dx, f3 - dy);
    }

    TextLine getTextLineForTesting() {
        return this.textLine;
    }

    private static int sameBaselineUpTo(Font font, char[] cArr, int i2, int i3) {
        return i3;
    }

    static byte getBaselineFromGraphic(GraphicAttribute graphicAttribute) {
        byte alignment = (byte) graphicAttribute.getAlignment();
        if (alignment == -2 || alignment == -1) {
            return (byte) 0;
        }
        return alignment;
    }

    public Shape getOutline(AffineTransform affineTransform) {
        ensureCache();
        Shape outline = this.textLine.getOutline(affineTransform);
        LayoutPathImpl layoutPath = this.textLine.getLayoutPath();
        if (layoutPath != null) {
            outline = layoutPath.mapShape(outline);
        }
        return outline;
    }

    public LayoutPath getLayoutPath() {
        return this.textLine.getLayoutPath();
    }

    public void hitToPoint(TextHitInfo textHitInfo, Point2D point2D) {
        boolean zIsDirectionLTR;
        float charLinePosition;
        if (textHitInfo == null || point2D == null) {
            throw new NullPointerException((textHitInfo == null ? "hit" : IssuingDistributionPointExtension.POINT) + " can't be null");
        }
        ensureCache();
        checkTextHit(textHitInfo);
        float charYPosition = 0.0f;
        int charIndex = textHitInfo.getCharIndex();
        boolean zIsLeadingEdge = textHitInfo.isLeadingEdge();
        if (charIndex == -1 || charIndex == this.textLine.characterCount()) {
            zIsDirectionLTR = this.textLine.isDirectionLTR();
            charLinePosition = zIsDirectionLTR == (charIndex == -1) ? 0.0f : this.lineMetrics.advance;
        } else {
            zIsDirectionLTR = this.textLine.isCharLTR(charIndex);
            charLinePosition = this.textLine.getCharLinePosition(charIndex, zIsLeadingEdge);
            charYPosition = this.textLine.getCharYPosition(charIndex);
        }
        point2D.setLocation(charLinePosition, charYPosition);
        LayoutPathImpl layoutPath = this.textLine.getLayoutPath();
        if (layoutPath != null) {
            layoutPath.pathToPoint(point2D, zIsDirectionLTR != zIsLeadingEdge, point2D);
        }
    }
}
