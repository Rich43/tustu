package java.awt.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.Bidi;
import java.util.Map;
import sun.font.AttributeValues;
import sun.font.BidiUtils;
import sun.font.CoreMetrics;
import sun.font.Decoration;
import sun.font.ExtendedTextLabel;
import sun.font.FontResolver;
import sun.font.GraphicComponent;
import sun.font.LayoutPathImpl;
import sun.font.TextLabelFactory;
import sun.font.TextLineComponent;
import sun.text.CodePointIterator;

/* loaded from: rt.jar:java/awt/font/TextLine.class */
final class TextLine {
    private TextLineComponent[] fComponents;
    private float[] fBaselineOffsets;
    private int[] fComponentVisualOrder;
    private float[] locs;
    private char[] fChars;
    private int fCharsStart;
    private int fCharsLimit;
    private int[] fCharVisualOrder;
    private int[] fCharLogicalOrder;
    private byte[] fCharLevels;
    private boolean fIsDirectionLTR;
    private LayoutPathImpl lp;
    private boolean isSimple;
    private Rectangle pixelBounds;
    private FontRenderContext frc;
    private TextLineMetrics fMetrics = null;
    private static Function fgPosAdvF = new Function() { // from class: java.awt.font.TextLine.1
        @Override // java.awt.font.TextLine.Function
        float computeFunction(TextLine textLine, int i2, int i3) {
            TextLineComponent textLineComponent = textLine.fComponents[i2];
            return textLine.locs[textLine.getComponentVisualIndex(i2) * 2] + textLineComponent.getCharX(i3) + textLineComponent.getCharAdvance(i3);
        }
    };
    private static Function fgAdvanceF = new Function() { // from class: java.awt.font.TextLine.2
        @Override // java.awt.font.TextLine.Function
        float computeFunction(TextLine textLine, int i2, int i3) {
            return textLine.fComponents[i2].getCharAdvance(i3);
        }
    };
    private static Function fgXPositionF = new Function() { // from class: java.awt.font.TextLine.3
        @Override // java.awt.font.TextLine.Function
        float computeFunction(TextLine textLine, int i2, int i3) {
            return textLine.locs[textLine.getComponentVisualIndex(i2) * 2] + textLine.fComponents[i2].getCharX(i3);
        }
    };
    private static Function fgYPositionF = new Function() { // from class: java.awt.font.TextLine.4
        @Override // java.awt.font.TextLine.Function
        float computeFunction(TextLine textLine, int i2, int i3) {
            return textLine.fComponents[i2].getCharY(i3) + textLine.getComponentShift(i2);
        }
    };

    /* loaded from: rt.jar:java/awt/font/TextLine$TextLineMetrics.class */
    static final class TextLineMetrics {
        public final float ascent;
        public final float descent;
        public final float leading;
        public final float advance;

        public TextLineMetrics(float f2, float f3, float f4, float f5) {
            this.ascent = f2;
            this.descent = f3;
            this.leading = f4;
            this.advance = f5;
        }
    }

    public TextLine(FontRenderContext fontRenderContext, TextLineComponent[] textLineComponentArr, float[] fArr, char[] cArr, int i2, int i3, int[] iArr, byte[] bArr, boolean z2) {
        int[] iArrComputeComponentOrder = computeComponentOrder(textLineComponentArr, iArr);
        this.frc = fontRenderContext;
        this.fComponents = textLineComponentArr;
        this.fBaselineOffsets = fArr;
        this.fComponentVisualOrder = iArrComputeComponentOrder;
        this.fChars = cArr;
        this.fCharsStart = i2;
        this.fCharsLimit = i3;
        this.fCharLogicalOrder = iArr;
        this.fCharLevels = bArr;
        this.fIsDirectionLTR = z2;
        checkCtorArgs();
        init();
    }

    private void checkCtorArgs() {
        int numCharacters = 0;
        for (int i2 = 0; i2 < this.fComponents.length; i2++) {
            numCharacters += this.fComponents[i2].getNumCharacters();
        }
        if (numCharacters != characterCount()) {
            throw new IllegalArgumentException("Invalid TextLine!  char count is different from sum of char counts of components.");
        }
    }

    private void init() {
        AffineTransform baselineTransform;
        float f2;
        float fEffectiveBaselineOffset;
        float fMax = 0.0f;
        float fMax2 = 0.0f;
        float fMax3 = 0.0f;
        float fMax4 = 0.0f;
        float fMax5 = 0.0f;
        boolean z2 = false;
        this.isSimple = true;
        for (int i2 = 0; i2 < this.fComponents.length; i2++) {
            TextLineComponent textLineComponent = this.fComponents[i2];
            this.isSimple &= textLineComponent.isSimple();
            CoreMetrics coreMetrics = textLineComponent.getCoreMetrics();
            byte b2 = (byte) coreMetrics.baselineIndex;
            if (b2 >= 0) {
                float f3 = this.fBaselineOffsets[b2];
                fMax = Math.max(fMax, (-f3) + coreMetrics.ascent);
                float f4 = f3 + coreMetrics.descent;
                fMax2 = Math.max(fMax2, f4);
                fMax3 = Math.max(fMax3, f4 + coreMetrics.leading);
            } else {
                z2 = true;
                float f5 = coreMetrics.ascent + coreMetrics.descent;
                float f6 = f5 + coreMetrics.leading;
                fMax4 = Math.max(fMax4, f5);
                fMax5 = Math.max(fMax5, f6);
            }
        }
        if (z2) {
            if (fMax4 > fMax + fMax2) {
                fMax2 = fMax4 - fMax;
            }
            if (fMax5 > fMax + fMax3) {
                fMax3 = fMax5 - fMax;
            }
        }
        float f7 = fMax3 - fMax2;
        if (z2) {
            this.fBaselineOffsets = new float[]{this.fBaselineOffsets[0], this.fBaselineOffsets[1], this.fBaselineOffsets[2], fMax2, -fMax};
        }
        float advance = 0.0f;
        CoreMetrics coreMetrics2 = null;
        boolean z3 = false;
        this.locs = new float[(this.fComponents.length * 2) + 2];
        int i3 = 0;
        int i4 = 0;
        while (i3 < this.fComponents.length) {
            TextLineComponent textLineComponent2 = this.fComponents[getComponentLogicalIndex(i3)];
            CoreMetrics coreMetrics3 = textLineComponent2.getCoreMetrics();
            if (coreMetrics2 != null && ((coreMetrics2.italicAngle != 0.0f || coreMetrics3.italicAngle != 0.0f) && (coreMetrics2.italicAngle != coreMetrics3.italicAngle || coreMetrics2.baselineIndex != coreMetrics3.baselineIndex || coreMetrics2.ssOffset != coreMetrics3.ssOffset))) {
                float fEffectiveBaselineOffset2 = coreMetrics2.effectiveBaselineOffset(this.fBaselineOffsets);
                float f8 = fEffectiveBaselineOffset2 - coreMetrics2.ascent;
                float f9 = fEffectiveBaselineOffset2 + coreMetrics2.descent;
                float fEffectiveBaselineOffset3 = coreMetrics3.effectiveBaselineOffset(this.fBaselineOffsets);
                float f10 = fEffectiveBaselineOffset3 - coreMetrics3.ascent;
                float f11 = fEffectiveBaselineOffset3 + coreMetrics3.descent;
                float fMax6 = Math.max(f8, f10);
                float fMin = Math.min(f9, f11);
                advance += Math.max((coreMetrics2.italicAngle * (fEffectiveBaselineOffset2 - fMax6)) - (coreMetrics3.italicAngle * (fEffectiveBaselineOffset3 - fMax6)), (coreMetrics2.italicAngle * (fEffectiveBaselineOffset2 - fMin)) - (coreMetrics3.italicAngle * (fEffectiveBaselineOffset3 - fMin)));
                fEffectiveBaselineOffset = fEffectiveBaselineOffset3;
            } else {
                fEffectiveBaselineOffset = coreMetrics3.effectiveBaselineOffset(this.fBaselineOffsets);
            }
            this.locs[i4] = advance;
            this.locs[i4 + 1] = fEffectiveBaselineOffset;
            advance += textLineComponent2.getAdvance();
            coreMetrics2 = coreMetrics3;
            z3 |= textLineComponent2.getBaselineTransform() != null;
            i3++;
            i4 += 2;
        }
        if (coreMetrics2.italicAngle != 0.0f) {
            float fEffectiveBaselineOffset4 = coreMetrics2.effectiveBaselineOffset(this.fBaselineOffsets);
            float f12 = fEffectiveBaselineOffset4 - coreMetrics2.ascent;
            float f13 = fEffectiveBaselineOffset4 + coreMetrics2.descent;
            float f14 = fEffectiveBaselineOffset4 + coreMetrics2.ssOffset;
            if (coreMetrics2.italicAngle > 0.0f) {
                f2 = f14 + coreMetrics2.ascent;
            } else {
                f2 = f14 - coreMetrics2.descent;
            }
            advance += f2 * coreMetrics2.italicAngle;
        }
        this.locs[this.locs.length - 2] = advance;
        this.fMetrics = new TextLineMetrics(fMax, fMax2, f7, advance);
        if (z3) {
            this.isSimple = false;
            Point2D.Double r0 = new Point2D.Double();
            double d2 = 0.0d;
            double d3 = 0.0d;
            LayoutPathImpl.SegmentPathBuilder segmentPathBuilder = new LayoutPathImpl.SegmentPathBuilder();
            segmentPathBuilder.moveTo(this.locs[0], 0.0d);
            int i5 = 0;
            int i6 = 0;
            while (i5 < this.fComponents.length) {
                AffineTransform baselineTransform2 = this.fComponents[getComponentLogicalIndex(i5)].getBaselineTransform();
                if (baselineTransform2 != null && (baselineTransform2.getType() & 1) != 0) {
                    double translateX = d2 + baselineTransform2.getTranslateX();
                    d2 = translateX;
                    double translateY = d3 + baselineTransform2.getTranslateY();
                    d3 = translateY;
                    segmentPathBuilder.moveTo(translateX, translateY);
                }
                r0.f12394x = this.locs[i6 + 2] - this.locs[i6];
                r0.f12395y = 0.0d;
                if (baselineTransform2 != null) {
                    baselineTransform2.deltaTransform(r0, r0);
                }
                double d4 = d2 + r0.f12394x;
                d2 = d4;
                double d5 = d3 + r0.f12395y;
                d3 = d5;
                segmentPathBuilder.lineTo(d4, d5);
                i5++;
                i6 += 2;
            }
            this.lp = segmentPathBuilder.complete();
            if (this.lp == null && (baselineTransform = this.fComponents[getComponentLogicalIndex(0)].getBaselineTransform()) != null) {
                this.lp = new LayoutPathImpl.EmptyPath(baselineTransform);
            }
        }
    }

    public Rectangle getPixelBounds(FontRenderContext fontRenderContext, float f2, float f3) {
        Rectangle rectangleComputePixelBounds = null;
        if (fontRenderContext != null && fontRenderContext.equals(this.frc)) {
            fontRenderContext = null;
        }
        int iFloor = (int) Math.floor(f2);
        int iFloor2 = (int) Math.floor(f3);
        float f4 = f2 - iFloor;
        float f5 = f3 - iFloor2;
        boolean z2 = fontRenderContext == null && f4 == 0.0f && f5 == 0.0f;
        if (z2 && this.pixelBounds != null) {
            Rectangle rectangle = new Rectangle(this.pixelBounds);
            rectangle.f12372x += iFloor;
            rectangle.f12373y += iFloor2;
            return rectangle;
        }
        if (this.isSimple) {
            int i2 = 0;
            int i3 = 0;
            while (i2 < this.fComponents.length) {
                Rectangle pixelBounds = this.fComponents[getComponentLogicalIndex(i2)].getPixelBounds(fontRenderContext, this.locs[i3] + f4, this.locs[i3 + 1] + f5);
                if (!pixelBounds.isEmpty()) {
                    if (rectangleComputePixelBounds == null) {
                        rectangleComputePixelBounds = pixelBounds;
                    } else {
                        rectangleComputePixelBounds.add(pixelBounds);
                    }
                }
                i2++;
                i3 += 2;
            }
            if (rectangleComputePixelBounds == null) {
                rectangleComputePixelBounds = new Rectangle(0, 0, 0, 0);
            }
        } else {
            Rectangle2D visualBounds = getVisualBounds();
            if (this.lp != null) {
                visualBounds = this.lp.mapShape(visualBounds).getBounds();
            }
            Rectangle bounds = visualBounds.getBounds();
            BufferedImage bufferedImage = new BufferedImage(bounds.width + 6, bounds.height + 6, 2);
            Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
            graphics2DCreateGraphics.setColor(Color.WHITE);
            graphics2DCreateGraphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            graphics2DCreateGraphics.setColor(Color.BLACK);
            draw(graphics2DCreateGraphics, (f4 + 3.0f) - bounds.f12372x, (f5 + 3.0f) - bounds.f12373y);
            rectangleComputePixelBounds = computePixelBounds(bufferedImage);
            rectangleComputePixelBounds.f12372x -= 3 - bounds.f12372x;
            rectangleComputePixelBounds.f12373y -= 3 - bounds.f12373y;
        }
        if (z2) {
            this.pixelBounds = new Rectangle(rectangleComputePixelBounds);
        }
        rectangleComputePixelBounds.f12372x += iFloor;
        rectangleComputePixelBounds.f12373y += iFloor2;
        return rectangleComputePixelBounds;
    }

    static Rectangle computePixelBounds(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int i2 = -1;
        int i3 = -1;
        int i4 = width;
        int i5 = height;
        int[] iArr = new int[width];
        loop0: while (true) {
            i3++;
            if (i3 >= height) {
                break;
            }
            bufferedImage.getRGB(0, i3, iArr.length, 1, iArr, 0, width);
            for (int i6 : iArr) {
                if (i6 != -1) {
                    break loop0;
                }
            }
        }
        int[] iArr2 = new int[width];
        loop2: while (true) {
            i5--;
            if (i5 <= i3) {
                break;
            }
            bufferedImage.getRGB(0, i5, iArr2.length, 1, iArr2, 0, width);
            for (int i7 : iArr2) {
                if (i7 != -1) {
                    break loop2;
                }
            }
        }
        int i8 = i5 + 1;
        loop4: while (true) {
            i2++;
            if (i2 >= i4) {
                break;
            }
            for (int i9 = i3; i9 < i8; i9++) {
                if (bufferedImage.getRGB(i2, i9) != -1) {
                    break loop4;
                }
            }
        }
        loop6: while (true) {
            i4--;
            if (i4 <= i2) {
                break;
            }
            for (int i10 = i3; i10 < i8; i10++) {
                if (bufferedImage.getRGB(i4, i10) != -1) {
                    break loop6;
                }
            }
        }
        return new Rectangle(i2, i3, (i4 + 1) - i2, i8 - i3);
    }

    /* loaded from: rt.jar:java/awt/font/TextLine$Function.class */
    private static abstract class Function {
        abstract float computeFunction(TextLine textLine, int i2, int i3);

        private Function() {
        }
    }

    public int characterCount() {
        return this.fCharsLimit - this.fCharsStart;
    }

    public boolean isDirectionLTR() {
        return this.fIsDirectionLTR;
    }

    public TextLineMetrics getMetrics() {
        return this.fMetrics;
    }

    public int visualToLogical(int i2) {
        if (this.fCharLogicalOrder == null) {
            return i2;
        }
        if (this.fCharVisualOrder == null) {
            this.fCharVisualOrder = BidiUtils.createInverseMap(this.fCharLogicalOrder);
        }
        return this.fCharVisualOrder[i2];
    }

    public int logicalToVisual(int i2) {
        return this.fCharLogicalOrder == null ? i2 : this.fCharLogicalOrder[i2];
    }

    public byte getCharLevel(int i2) {
        if (this.fCharLevels == null) {
            return (byte) 0;
        }
        return this.fCharLevels[i2];
    }

    public boolean isCharLTR(int i2) {
        return (getCharLevel(i2) & 1) == 0;
    }

    public int getCharType(int i2) {
        return Character.getType(this.fChars[i2 + this.fCharsStart]);
    }

    public boolean isCharSpace(int i2) {
        return Character.isSpaceChar(this.fChars[i2 + this.fCharsStart]);
    }

    public boolean isCharWhitespace(int i2) {
        return Character.isWhitespace(this.fChars[i2 + this.fCharsStart]);
    }

    public float getCharAngle(int i2) {
        return getCoreMetricsAt(i2).italicAngle;
    }

    public CoreMetrics getCoreMetricsAt(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative logicalIndex.");
        }
        if (i2 > this.fCharsLimit - this.fCharsStart) {
            throw new IllegalArgumentException("logicalIndex too large.");
        }
        int i3 = 0;
        int numCharacters = 0;
        do {
            numCharacters += this.fComponents[i3].getNumCharacters();
            if (numCharacters > i2) {
                break;
            }
            i3++;
        } while (i3 < this.fComponents.length);
        return this.fComponents[i3].getCoreMetrics();
    }

    public float getCharAscent(int i2) {
        return getCoreMetricsAt(i2).ascent;
    }

    public float getCharDescent(int i2) {
        return getCoreMetricsAt(i2).descent;
    }

    public float getCharShift(int i2) {
        return getCoreMetricsAt(i2).ssOffset;
    }

    private float applyFunctionAtIndex(int i2, Function function) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative logicalIndex.");
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.fComponents.length; i4++) {
            int numCharacters = i3 + this.fComponents[i4].getNumCharacters();
            if (numCharacters > i2) {
                return function.computeFunction(this, i4, i2 - i3);
            }
            i3 = numCharacters;
        }
        throw new IllegalArgumentException("logicalIndex too large.");
    }

    public float getCharAdvance(int i2) {
        return applyFunctionAtIndex(i2, fgAdvanceF);
    }

    public float getCharXPosition(int i2) {
        return applyFunctionAtIndex(i2, fgXPositionF);
    }

    public float getCharYPosition(int i2) {
        return applyFunctionAtIndex(i2, fgYPositionF);
    }

    public float getCharLinePosition(int i2) {
        return getCharXPosition(i2);
    }

    public float getCharLinePosition(int i2, boolean z2) {
        return applyFunctionAtIndex(i2, isCharLTR(i2) == z2 ? fgXPositionF : fgPosAdvF);
    }

    public boolean caretAtOffsetIsValid(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative offset.");
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.fComponents.length; i4++) {
            int numCharacters = i3 + this.fComponents[i4].getNumCharacters();
            if (numCharacters > i2) {
                return this.fComponents[i4].caretAtOffsetIsValid(i2 - i3);
            }
            i3 = numCharacters;
        }
        throw new IllegalArgumentException("logicalIndex too large.");
    }

    private int getComponentLogicalIndex(int i2) {
        if (this.fComponentVisualOrder == null) {
            return i2;
        }
        return this.fComponentVisualOrder[i2];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getComponentVisualIndex(int i2) {
        if (this.fComponentVisualOrder == null) {
            return i2;
        }
        for (int i3 = 0; i3 < this.fComponentVisualOrder.length; i3++) {
            if (this.fComponentVisualOrder[i3] == i2) {
                return i3;
            }
        }
        throw new IndexOutOfBoundsException("bad component index: " + i2);
    }

    public Rectangle2D getCharBounds(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative logicalIndex.");
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.fComponents.length; i4++) {
            int numCharacters = i3 + this.fComponents[i4].getNumCharacters();
            if (numCharacters > i2) {
                Rectangle2D charVisualBounds = this.fComponents[i4].getCharVisualBounds(i2 - i3);
                int componentVisualIndex = getComponentVisualIndex(i4);
                charVisualBounds.setRect(charVisualBounds.getX() + this.locs[componentVisualIndex * 2], charVisualBounds.getY() + this.locs[(componentVisualIndex * 2) + 1], charVisualBounds.getWidth(), charVisualBounds.getHeight());
                return charVisualBounds;
            }
            i3 = numCharacters;
        }
        throw new IllegalArgumentException("logicalIndex too large.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getComponentShift(int i2) {
        return this.fComponents[i2].getCoreMetrics().effectiveBaselineOffset(this.fBaselineOffsets);
    }

    public void draw(Graphics2D graphics2D, float f2, float f3) {
        if (this.lp == null) {
            int i2 = 0;
            int i3 = 0;
            while (i2 < this.fComponents.length) {
                this.fComponents[getComponentLogicalIndex(i2)].draw(graphics2D, this.locs[i3] + f2, this.locs[i3 + 1] + f3);
                i2++;
                i3 += 2;
            }
            return;
        }
        AffineTransform transform = graphics2D.getTransform();
        Point2D.Float r0 = new Point2D.Float();
        int i4 = 0;
        int i5 = 0;
        while (i4 < this.fComponents.length) {
            TextLineComponent textLineComponent = this.fComponents[getComponentLogicalIndex(i4)];
            this.lp.pathToPoint(this.locs[i5], this.locs[i5 + 1], false, r0);
            r0.f12396x += f2;
            r0.f12397y += f3;
            AffineTransform baselineTransform = textLineComponent.getBaselineTransform();
            if (baselineTransform != null) {
                graphics2D.translate(r0.f12396x - baselineTransform.getTranslateX(), r0.f12397y - baselineTransform.getTranslateY());
                graphics2D.transform(baselineTransform);
                textLineComponent.draw(graphics2D, 0.0f, 0.0f);
                graphics2D.setTransform(transform);
            } else {
                textLineComponent.draw(graphics2D, r0.f12396x, r0.f12397y);
            }
            i4++;
            i5 += 2;
        }
    }

    public Rectangle2D getVisualBounds() {
        Rectangle2D rectangle2D = null;
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.fComponents.length) {
            TextLineComponent textLineComponent = this.fComponents[getComponentLogicalIndex(i2)];
            Rectangle2D visualBounds = textLineComponent.getVisualBounds();
            Point2D.Float r0 = new Point2D.Float(this.locs[i3], this.locs[i3 + 1]);
            if (this.lp == null) {
                visualBounds.setRect(visualBounds.getMinX() + r0.f12396x, visualBounds.getMinY() + r0.f12397y, visualBounds.getWidth(), visualBounds.getHeight());
            } else {
                this.lp.pathToPoint((Point2D) r0, false, (Point2D) r0);
                AffineTransform baselineTransform = textLineComponent.getBaselineTransform();
                if (baselineTransform != null) {
                    AffineTransform translateInstance = AffineTransform.getTranslateInstance(r0.f12396x - baselineTransform.getTranslateX(), r0.f12397y - baselineTransform.getTranslateY());
                    translateInstance.concatenate(baselineTransform);
                    visualBounds = translateInstance.createTransformedShape(visualBounds).getBounds2D();
                } else {
                    visualBounds.setRect(visualBounds.getMinX() + r0.f12396x, visualBounds.getMinY() + r0.f12397y, visualBounds.getWidth(), visualBounds.getHeight());
                }
            }
            if (rectangle2D == null) {
                rectangle2D = visualBounds;
            } else {
                rectangle2D.add(visualBounds);
            }
            i2++;
            i3 += 2;
        }
        if (rectangle2D == null) {
            rectangle2D = new Rectangle2D.Float(Float.MAX_VALUE, Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        }
        return rectangle2D;
    }

    public Rectangle2D getItalicBounds() {
        float fMin = Float.MAX_VALUE;
        float fMax = -3.4028235E38f;
        float fMin2 = Float.MAX_VALUE;
        float fMax2 = -3.4028235E38f;
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.fComponents.length) {
            Rectangle2D italicBounds = this.fComponents[getComponentLogicalIndex(i2)].getItalicBounds();
            float f2 = this.locs[i3];
            float f3 = this.locs[i3 + 1];
            fMin = Math.min(fMin, f2 + ((float) italicBounds.getX()));
            fMax = Math.max(fMax, f2 + ((float) italicBounds.getMaxX()));
            fMin2 = Math.min(fMin2, f3 + ((float) italicBounds.getY()));
            fMax2 = Math.max(fMax2, f3 + ((float) italicBounds.getMaxY()));
            i2++;
            i3 += 2;
        }
        return new Rectangle2D.Float(fMin, fMin2, fMax - fMin, fMax2 - fMin2);
    }

    public Shape getOutline(AffineTransform affineTransform) {
        GeneralPath generalPath = new GeneralPath(1);
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.fComponents.length) {
            generalPath.append(this.fComponents[getComponentLogicalIndex(i2)].getOutline(this.locs[i3], this.locs[i3 + 1]), false);
            i2++;
            i3 += 2;
        }
        if (affineTransform != null) {
            generalPath.transform(affineTransform);
        }
        return generalPath;
    }

    public int hashCode() {
        return ((this.fComponents.length << 16) ^ (this.fComponents[0].hashCode() << 3)) ^ (this.fCharsLimit - this.fCharsStart);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < this.fComponents.length; i2++) {
            sb.append((Object) this.fComponents[i2]);
        }
        return sb.toString();
    }

    public static TextLine fastCreateTextLine(FontRenderContext fontRenderContext, char[] cArr, Font font, CoreMetrics coreMetrics, Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        boolean zBaseIsLeftToRight = true;
        byte[] levels = null;
        int[] iArrCreateInverseMap = null;
        Bidi bidi = null;
        int length = cArr.length;
        boolean zRequiresBidi = false;
        byte[] bArr = null;
        AttributeValues attributeValuesFromMap = null;
        if (map != null) {
            attributeValuesFromMap = AttributeValues.fromMap(map);
            if (attributeValuesFromMap.getRunDirection() >= 0) {
                zBaseIsLeftToRight = attributeValuesFromMap.getRunDirection() == 0;
                zRequiresBidi = !zBaseIsLeftToRight;
            }
            if (attributeValuesFromMap.getBidiEmbedding() != 0) {
                zRequiresBidi = true;
                byte bidiEmbedding = (byte) attributeValuesFromMap.getBidiEmbedding();
                bArr = new byte[length];
                for (int i2 = 0; i2 < bArr.length; i2++) {
                    bArr[i2] = bidiEmbedding;
                }
            }
        }
        if (!zRequiresBidi) {
            zRequiresBidi = Bidi.requiresBidi(cArr, 0, cArr.length);
        }
        if (zRequiresBidi) {
            bidi = new Bidi(cArr, 0, bArr, 0, cArr.length, attributeValuesFromMap == null ? -2 : attributeValuesFromMap.getRunDirection());
            if (!bidi.isLeftToRight()) {
                levels = BidiUtils.getLevels(bidi);
                iArrCreateInverseMap = BidiUtils.createInverseMap(BidiUtils.createVisualToLogicalMap(levels));
                zBaseIsLeftToRight = bidi.baseIsLeftToRight();
            }
        }
        TextLineComponent[] textLineComponentArrCreateComponentsOnRun = createComponentsOnRun(0, cArr.length, cArr, iArrCreateInverseMap, levels, new TextLabelFactory(fontRenderContext, cArr, bidi, 0), font, coreMetrics, fontRenderContext, Decoration.getDecoration(attributeValuesFromMap), new TextLineComponent[1], 0);
        int length2 = textLineComponentArrCreateComponentsOnRun.length;
        while (textLineComponentArrCreateComponentsOnRun[length2 - 1] == null) {
            length2--;
        }
        if (length2 != textLineComponentArrCreateComponentsOnRun.length) {
            TextLineComponent[] textLineComponentArr = new TextLineComponent[length2];
            System.arraycopy(textLineComponentArrCreateComponentsOnRun, 0, textLineComponentArr, 0, length2);
            textLineComponentArrCreateComponentsOnRun = textLineComponentArr;
        }
        return new TextLine(fontRenderContext, textLineComponentArrCreateComponentsOnRun, coreMetrics.baselineOffsets, cArr, 0, cArr.length, iArrCreateInverseMap, levels, zBaseIsLeftToRight);
    }

    private static TextLineComponent[] expandArray(TextLineComponent[] textLineComponentArr) {
        TextLineComponent[] textLineComponentArr2 = new TextLineComponent[textLineComponentArr.length + 8];
        System.arraycopy(textLineComponentArr, 0, textLineComponentArr2, 0, textLineComponentArr.length);
        return textLineComponentArr2;
    }

    public static TextLineComponent[] createComponentsOnRun(int i2, int i3, char[] cArr, int[] iArr, byte[] bArr, TextLabelFactory textLabelFactory, Font font, CoreMetrics coreMetrics, FontRenderContext fontRenderContext, Decoration decoration, TextLineComponent[] textLineComponentArr, int i4) {
        int numChars;
        int i5 = i2;
        do {
            int iFirstVisualChunk = firstVisualChunk(iArr, bArr, i5, i3);
            do {
                int i6 = i5;
                if (coreMetrics == null) {
                    LineMetrics lineMetrics = font.getLineMetrics(cArr, i6, iFirstVisualChunk, fontRenderContext);
                    coreMetrics = CoreMetrics.get(lineMetrics);
                    numChars = lineMetrics.getNumChars();
                } else {
                    numChars = iFirstVisualChunk - i6;
                }
                ExtendedTextLabel extendedTextLabelCreateExtended = textLabelFactory.createExtended(font, coreMetrics, decoration, i6, i6 + numChars);
                i4++;
                if (i4 >= textLineComponentArr.length) {
                    textLineComponentArr = expandArray(textLineComponentArr);
                }
                textLineComponentArr[i4 - 1] = extendedTextLabelCreateExtended;
                i5 += numChars;
            } while (i5 < iFirstVisualChunk);
        } while (i5 < i3);
        return textLineComponentArr;
    }

    public static TextLineComponent[] getComponents(StyledParagraph styledParagraph, char[] cArr, int i2, int i3, int[] iArr, byte[] bArr, TextLabelFactory textLabelFactory) {
        TextLineComponent[] textLineComponentArr;
        FontRenderContext fontRenderContext = textLabelFactory.getFontRenderContext();
        int length = 0;
        TextLineComponent[] textLineComponentArrCreateComponentsOnRun = new TextLineComponent[1];
        int i4 = i2;
        do {
            int iMin = Math.min(styledParagraph.getRunLimit(i4), i3);
            Decoration decorationAt = styledParagraph.getDecorationAt(i4);
            Object fontOrGraphicAt = styledParagraph.getFontOrGraphicAt(i4);
            if (fontOrGraphicAt instanceof GraphicAttribute) {
                GraphicAttribute graphicAttribute = (GraphicAttribute) fontOrGraphicAt;
                do {
                    int iFirstVisualChunk = firstVisualChunk(iArr, bArr, i4, iMin);
                    GraphicComponent graphicComponent = new GraphicComponent(graphicAttribute, decorationAt, iArr, bArr, i4, iFirstVisualChunk, null);
                    i4 = iFirstVisualChunk;
                    length++;
                    if (length >= textLineComponentArrCreateComponentsOnRun.length) {
                        textLineComponentArrCreateComponentsOnRun = expandArray(textLineComponentArrCreateComponentsOnRun);
                    }
                    textLineComponentArrCreateComponentsOnRun[length - 1] = graphicComponent;
                } while (i4 < iMin);
            } else {
                textLineComponentArrCreateComponentsOnRun = createComponentsOnRun(i4, iMin, cArr, iArr, bArr, textLabelFactory, (Font) fontOrGraphicAt, null, fontRenderContext, decorationAt, textLineComponentArrCreateComponentsOnRun, length);
                i4 = iMin;
                length = textLineComponentArrCreateComponentsOnRun.length;
                while (textLineComponentArrCreateComponentsOnRun[length - 1] == null) {
                    length--;
                }
            }
        } while (i4 < i3);
        if (textLineComponentArrCreateComponentsOnRun.length == length) {
            textLineComponentArr = textLineComponentArrCreateComponentsOnRun;
        } else {
            textLineComponentArr = new TextLineComponent[length];
            System.arraycopy(textLineComponentArrCreateComponentsOnRun, 0, textLineComponentArr, 0, length);
        }
        return textLineComponentArr;
    }

    public static TextLine createLineFromText(char[] cArr, StyledParagraph styledParagraph, TextLabelFactory textLabelFactory, boolean z2, float[] fArr) {
        textLabelFactory.setLineContext(0, cArr.length);
        Bidi lineBidi = textLabelFactory.getLineBidi();
        int[] iArrCreateInverseMap = null;
        byte[] levels = null;
        if (lineBidi != null) {
            levels = BidiUtils.getLevels(lineBidi);
            iArrCreateInverseMap = BidiUtils.createInverseMap(BidiUtils.createVisualToLogicalMap(levels));
        }
        return new TextLine(textLabelFactory.getFontRenderContext(), getComponents(styledParagraph, cArr, 0, cArr.length, iArrCreateInverseMap, levels, textLabelFactory), fArr, cArr, 0, cArr.length, iArrCreateInverseMap, levels, z2);
    }

    private static int[] computeComponentOrder(TextLineComponent[] textLineComponentArr, int[] iArr) {
        int[] iArrCreateInverseMap = null;
        if (iArr != null && textLineComponentArr.length > 1) {
            int[] iArr2 = new int[textLineComponentArr.length];
            int numCharacters = 0;
            for (int i2 = 0; i2 < textLineComponentArr.length; i2++) {
                iArr2[i2] = iArr[numCharacters];
                numCharacters += textLineComponentArr[i2].getNumCharacters();
            }
            iArrCreateInverseMap = BidiUtils.createInverseMap(BidiUtils.createContiguousOrder(iArr2));
        }
        return iArrCreateInverseMap;
    }

    public static TextLine standardCreateTextLine(FontRenderContext fontRenderContext, AttributedCharacterIterator attributedCharacterIterator, char[] cArr, float[] fArr) {
        StyledParagraph styledParagraph = new StyledParagraph(attributedCharacterIterator, cArr);
        Bidi bidi = new Bidi(attributedCharacterIterator);
        if (bidi.isLeftToRight()) {
            bidi = null;
        }
        TextLabelFactory textLabelFactory = new TextLabelFactory(fontRenderContext, cArr, bidi, 0);
        boolean zBaseIsLeftToRight = true;
        if (bidi != null) {
            zBaseIsLeftToRight = bidi.baseIsLeftToRight();
        }
        return createLineFromText(cArr, styledParagraph, textLabelFactory, zBaseIsLeftToRight, fArr);
    }

    static boolean advanceToFirstFont(AttributedCharacterIterator attributedCharacterIterator) {
        char cFirst = attributedCharacterIterator.first();
        while (cFirst != 65535) {
            if (attributedCharacterIterator.getAttribute(TextAttribute.CHAR_REPLACEMENT) != null) {
                cFirst = attributedCharacterIterator.setIndex(attributedCharacterIterator.getRunLimit());
            } else {
                return true;
            }
        }
        return false;
    }

    static float[] getNormalizedOffsets(float[] fArr, byte b2) {
        if (fArr[b2] != 0.0f) {
            float f2 = fArr[b2];
            float[] fArr2 = new float[fArr.length];
            for (int i2 = 0; i2 < fArr2.length; i2++) {
                fArr2[i2] = fArr[i2] - f2;
            }
            fArr = fArr2;
        }
        return fArr;
    }

    static Font getFontAtCurrentPos(AttributedCharacterIterator attributedCharacterIterator) {
        Object attribute = attributedCharacterIterator.getAttribute(TextAttribute.FONT);
        if (attribute != null) {
            return (Font) attribute;
        }
        if (attributedCharacterIterator.getAttribute(TextAttribute.FAMILY) != null) {
            return Font.getFont(attributedCharacterIterator.getAttributes());
        }
        int next = CodePointIterator.create(attributedCharacterIterator).next();
        if (next != -1) {
            FontResolver fontResolver = FontResolver.getInstance();
            return fontResolver.getFont(fontResolver.getFontIndex(next), attributedCharacterIterator.getAttributes());
        }
        return null;
    }

    private static int firstVisualChunk(int[] iArr, byte[] bArr, int i2, int i3) {
        if (iArr != null && bArr != null) {
            byte b2 = bArr[i2];
            do {
                i2++;
                if (i2 >= i3) {
                    break;
                }
            } while (bArr[i2] == b2);
            return i2;
        }
        return i3;
    }

    public TextLine getJustifiedLine(float f2, float f3, int i2, int i3) {
        TextLineComponent[] textLineComponentArr = new TextLineComponent[this.fComponents.length];
        System.arraycopy(this.fComponents, 0, textLineComponentArr, 0, this.fComponents.length);
        boolean z2 = false;
        do {
            getAdvanceBetween(textLineComponentArr, 0, characterCount());
            float advanceBetween = (f2 - getAdvanceBetween(textLineComponentArr, i2, i3)) * f3;
            int[] iArr = new int[textLineComponentArr.length];
            int numJustificationInfos = 0;
            for (int i4 = 0; i4 < textLineComponentArr.length; i4++) {
                int componentLogicalIndex = getComponentLogicalIndex(i4);
                iArr[componentLogicalIndex] = numJustificationInfos;
                numJustificationInfos += textLineComponentArr[componentLogicalIndex].getNumJustificationInfos();
            }
            GlyphJustificationInfo[] glyphJustificationInfoArr = new GlyphJustificationInfo[numJustificationInfos];
            for (int i5 = 0; i5 < textLineComponentArr.length; i5++) {
                TextLineComponent textLineComponent = textLineComponentArr[i5];
                int numCharacters = textLineComponent.getNumCharacters();
                int i6 = 0 + numCharacters;
                if (i6 > i2) {
                    textLineComponent.getJustificationInfos(glyphJustificationInfoArr, iArr[i5], Math.max(0, i2 - 0), Math.min(numCharacters, i3 - 0));
                    if (i6 >= i3) {
                        break;
                    }
                }
            }
            int i7 = 0;
            int i8 = numJustificationInfos;
            while (i7 < i8 && glyphJustificationInfoArr[i7] == null) {
                i7++;
            }
            while (i8 > i7 && glyphJustificationInfoArr[i8 - 1] == null) {
                i8--;
            }
            float[] fArrJustify = new TextJustifier(glyphJustificationInfoArr, i7, i8).justify(advanceBetween);
            boolean z3 = !z2;
            boolean z4 = false;
            boolean[] zArr = new boolean[1];
            for (int i9 = 0; i9 < textLineComponentArr.length; i9++) {
                TextLineComponent textLineComponent2 = textLineComponentArr[i9];
                int numCharacters2 = textLineComponent2.getNumCharacters();
                int i10 = 0 + numCharacters2;
                if (i10 > i2) {
                    Math.max(0, i2 - 0);
                    Math.min(numCharacters2, i3 - 0);
                    textLineComponentArr[i9] = textLineComponent2.applyJustificationDeltas(fArrJustify, iArr[i9] * 2, zArr);
                    z4 |= zArr[0];
                    if (i10 >= i3) {
                        break;
                    }
                }
            }
            z2 = z4 && !z2;
        } while (z2);
        return new TextLine(this.frc, textLineComponentArr, this.fBaselineOffsets, this.fChars, this.fCharsStart, this.fCharsLimit, this.fCharLogicalOrder, this.fCharLevels, this.fIsDirectionLTR);
    }

    public static float getAdvanceBetween(TextLineComponent[] textLineComponentArr, int i2, int i3) {
        float advanceBetween = 0.0f;
        int i4 = 0;
        for (TextLineComponent textLineComponent : textLineComponentArr) {
            int numCharacters = textLineComponent.getNumCharacters();
            int i5 = i4 + numCharacters;
            if (i5 > i2) {
                advanceBetween += textLineComponent.getAdvanceBetween(Math.max(0, i2 - i4), Math.min(numCharacters, i3 - i4));
                if (i5 >= i3) {
                    break;
                }
            }
            i4 = i5;
        }
        return advanceBetween;
    }

    LayoutPathImpl getLayoutPath() {
        return this.lp;
    }
}
