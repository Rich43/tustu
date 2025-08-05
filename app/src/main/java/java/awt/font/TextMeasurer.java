package java.awt.font;

import java.awt.Font;
import java.text.AttributedCharacterIterator;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.Hashtable;
import java.util.Map;
import sun.font.AttributeValues;
import sun.font.BidiUtils;
import sun.font.TextLabelFactory;
import sun.font.TextLineComponent;

/* loaded from: rt.jar:java/awt/font/TextMeasurer.class */
public final class TextMeasurer implements Cloneable {
    private FontRenderContext fFrc;
    private int fStart;
    private char[] fChars;
    private Bidi fBidi;
    private byte[] fLevels;
    private TextLineComponent[] fComponents;
    private int fComponentStart;
    private int fComponentLimit;
    private boolean haveLayoutWindow;
    private StyledParagraph fParagraph;
    private boolean fIsDirectionLTR;
    private byte fBaseline;
    private float[] fBaselineOffsets;
    private static float EST_LINES = 2.1f;
    private static boolean wantStats = false;
    private BreakIterator fLineBreak = null;
    private CharArrayIterator charIter = null;
    int layoutCount = 0;
    int layoutCharCount = 0;
    private float fJustifyRatio = 1.0f;
    private int formattedChars = 0;
    private boolean collectStats = false;

    public TextMeasurer(AttributedCharacterIterator attributedCharacterIterator, FontRenderContext fontRenderContext) {
        this.fFrc = fontRenderContext;
        initAll(attributedCharacterIterator);
    }

    protected Object clone() {
        try {
            TextMeasurer textMeasurer = (TextMeasurer) super.clone();
            if (this.fComponents != null) {
                textMeasurer.fComponents = (TextLineComponent[]) this.fComponents.clone();
            }
            return textMeasurer;
        } catch (CloneNotSupportedException e2) {
            throw new Error();
        }
    }

    private void invalidateComponents() {
        int length = this.fChars.length;
        this.fComponentLimit = length;
        this.fComponentStart = length;
        this.fComponents = null;
        this.haveLayoutWindow = false;
    }

    private void initAll(AttributedCharacterIterator attributedCharacterIterator) {
        this.fStart = attributedCharacterIterator.getBeginIndex();
        this.fChars = new char[attributedCharacterIterator.getEndIndex() - this.fStart];
        int i2 = 0;
        char cFirst = attributedCharacterIterator.first();
        while (true) {
            char c2 = cFirst;
            if (c2 == 65535) {
                break;
            }
            int i3 = i2;
            i2++;
            this.fChars[i3] = c2;
            cFirst = attributedCharacterIterator.next();
        }
        attributedCharacterIterator.first();
        this.fBidi = new Bidi(attributedCharacterIterator);
        if (this.fBidi.isLeftToRight()) {
            this.fBidi = null;
        }
        attributedCharacterIterator.first();
        Map<AttributedCharacterIterator.Attribute, Object> attributes = attributedCharacterIterator.getAttributes();
        NumericShaper numericShaping = AttributeValues.getNumericShaping(attributes);
        if (numericShaping != null) {
            numericShaping.shape(this.fChars, 0, this.fChars.length);
        }
        this.fParagraph = new StyledParagraph(attributedCharacterIterator, this.fChars);
        this.fJustifyRatio = AttributeValues.getJustification(attributes);
        if (TextLine.advanceToFirstFont(attributedCharacterIterator)) {
            Font fontAtCurrentPos = TextLine.getFontAtCurrentPos(attributedCharacterIterator);
            int index = attributedCharacterIterator.getIndex() - attributedCharacterIterator.getBeginIndex();
            LineMetrics lineMetrics = fontAtCurrentPos.getLineMetrics(this.fChars, index, index + 1, this.fFrc);
            this.fBaseline = (byte) lineMetrics.getBaselineIndex();
            this.fBaselineOffsets = lineMetrics.getBaselineOffsets();
        } else {
            this.fBaseline = TextLayout.getBaselineFromGraphic((GraphicAttribute) attributes.get(TextAttribute.CHAR_REPLACEMENT));
            this.fBaselineOffsets = new Font(new Hashtable(5, 0.9f)).getLineMetrics(" ", 0, 1, this.fFrc).getBaselineOffsets();
        }
        this.fBaselineOffsets = TextLine.getNormalizedOffsets(this.fBaselineOffsets, this.fBaseline);
        invalidateComponents();
    }

    private void generateComponents(int i2, int i3) {
        if (this.collectStats) {
            this.formattedChars += i3 - i2;
        }
        TextLabelFactory textLabelFactory = new TextLabelFactory(this.fFrc, this.fChars, this.fBidi, 0);
        int[] iArrCreateInverseMap = null;
        if (this.fBidi != null) {
            this.fLevels = BidiUtils.getLevels(this.fBidi);
            iArrCreateInverseMap = BidiUtils.createInverseMap(BidiUtils.createVisualToLogicalMap(this.fLevels));
            this.fIsDirectionLTR = this.fBidi.baseIsLeftToRight();
        } else {
            this.fLevels = null;
            this.fIsDirectionLTR = true;
        }
        try {
            this.fComponents = TextLine.getComponents(this.fParagraph, this.fChars, i2, i3, iArrCreateInverseMap, this.fLevels, textLabelFactory);
            this.fComponentStart = i2;
            this.fComponentLimit = i3;
        } catch (IllegalArgumentException e2) {
            System.out.println("startingAt=" + i2 + "; endingAt=" + i3);
            System.out.println("fComponentLimit=" + this.fComponentLimit);
            throw e2;
        }
    }

    private int calcLineBreak(int i2, float f2) {
        int numCharacters;
        int i3 = i2;
        float advanceBetween = f2;
        int i4 = this.fComponentStart;
        int i5 = 0;
        while (i5 < this.fComponents.length && (numCharacters = i4 + this.fComponents[i5].getNumCharacters()) <= i3) {
            i4 = numCharacters;
            i5++;
        }
        while (i5 < this.fComponents.length) {
            TextLineComponent textLineComponent = this.fComponents[i5];
            int numCharacters2 = textLineComponent.getNumCharacters();
            int lineBreakIndex = textLineComponent.getLineBreakIndex(i3 - i4, advanceBetween);
            if (lineBreakIndex == numCharacters2 && i5 < this.fComponents.length) {
                advanceBetween -= textLineComponent.getAdvanceBetween(i3 - i4, lineBreakIndex);
                i4 += numCharacters2;
                i3 = i4;
                i5++;
            } else {
                return i4 + lineBreakIndex;
            }
        }
        if (this.fComponentLimit < this.fChars.length) {
            generateComponents(i2, this.fChars.length);
            return calcLineBreak(i2, f2);
        }
        return this.fChars.length;
    }

    private int trailingCdWhitespaceStart(int i2, int i3) {
        if (this.fLevels != null) {
            byte b2 = (byte) (this.fIsDirectionLTR ? 0 : 1);
            int i4 = i3;
            do {
                i4--;
                if (i4 >= i2) {
                    if (this.fLevels[i4] % 2 == b2) {
                        break;
                    }
                }
            } while (Character.getDirectionality(this.fChars[i4]) == 12);
            return i4 + 1;
        }
        return i2;
    }

    private TextLineComponent[] makeComponentsOnRange(int i2, int i3) {
        int i4;
        int numCharacters;
        int iTrailingCdWhitespaceStart = trailingCdWhitespaceStart(i2, i3);
        int i5 = this.fComponentStart;
        int i6 = 0;
        while (i6 < this.fComponents.length && (numCharacters = i5 + this.fComponents[i6].getNumCharacters()) <= i2) {
            i5 = numCharacters;
            i6++;
        }
        boolean z2 = false;
        int i7 = i5;
        int i8 = i6;
        boolean z3 = true;
        while (z3) {
            int numCharacters2 = i7 + this.fComponents[i8].getNumCharacters();
            if (iTrailingCdWhitespaceStart > Math.max(i7, i2) && iTrailingCdWhitespaceStart < Math.min(numCharacters2, i3)) {
                z2 = true;
            }
            if (numCharacters2 >= i3) {
                z3 = false;
            } else {
                i7 = numCharacters2;
            }
            i8++;
        }
        int i9 = i8 - i6;
        if (z2) {
            i9++;
        }
        TextLineComponent[] textLineComponentArr = new TextLineComponent[i9];
        int i10 = 0;
        int i11 = i2;
        int i12 = iTrailingCdWhitespaceStart;
        if (i12 == i2) {
            i4 = this.fIsDirectionLTR ? 0 : 1;
            i12 = i3;
        } else {
            i4 = 2;
        }
        while (i11 < i3) {
            int numCharacters3 = i5 + this.fComponents[i6].getNumCharacters();
            int iMax = Math.max(i11, i5);
            int iMin = Math.min(i12, numCharacters3);
            int i13 = i10;
            i10++;
            textLineComponentArr[i13] = this.fComponents[i6].getSubset(iMax - i5, iMin - i5, i4);
            i11 += iMin - iMax;
            if (i11 == i12) {
                i12 = i3;
                i4 = this.fIsDirectionLTR ? 0 : 1;
            }
            if (i11 == numCharacters3) {
                i6++;
                i5 = numCharacters3;
            }
        }
        return textLineComponentArr;
    }

    private TextLine makeTextLineOnRange(int i2, int i3) {
        int[] iArrCreateInverseMap = null;
        byte[] levels = null;
        if (this.fBidi != null) {
            levels = BidiUtils.getLevels(this.fBidi.createLineBidi(i2, i3));
            iArrCreateInverseMap = BidiUtils.createInverseMap(BidiUtils.createVisualToLogicalMap(levels));
        }
        return new TextLine(this.fFrc, makeComponentsOnRange(i2, i3), this.fBaselineOffsets, this.fChars, i2, i3, iArrCreateInverseMap, levels, this.fIsDirectionLTR);
    }

    private void ensureComponents(int i2, int i3) {
        if (i2 < this.fComponentStart || i3 > this.fComponentLimit) {
            generateComponents(i2, i3);
        }
    }

    private void makeLayoutWindow(int i2) {
        int iPreceding = i2;
        int length = this.fChars.length;
        if (this.layoutCount > 0 && !this.haveLayoutWindow) {
            length = Math.min(i2 + ((int) (Math.max(this.layoutCharCount / this.layoutCount, 1) * EST_LINES)), this.fChars.length);
        }
        if (i2 > 0 || length < this.fChars.length) {
            if (this.charIter == null) {
                this.charIter = new CharArrayIterator(this.fChars);
            } else {
                this.charIter.reset(this.fChars);
            }
            if (this.fLineBreak == null) {
                this.fLineBreak = BreakIterator.getLineInstance();
            }
            this.fLineBreak.setText(this.charIter);
            if (i2 > 0 && !this.fLineBreak.isBoundary(i2)) {
                iPreceding = this.fLineBreak.preceding(i2);
            }
            if (length < this.fChars.length && !this.fLineBreak.isBoundary(length)) {
                length = this.fLineBreak.following(length);
            }
        }
        ensureComponents(iPreceding, length);
        this.haveLayoutWindow = true;
    }

    public int getLineBreakIndex(int i2, float f2) {
        int i3 = i2 - this.fStart;
        if (!this.haveLayoutWindow || i3 < this.fComponentStart || i3 >= this.fComponentLimit) {
            makeLayoutWindow(i3);
        }
        return calcLineBreak(i3, f2) + this.fStart;
    }

    public float getAdvanceBetween(int i2, int i3) {
        int i4 = i2 - this.fStart;
        int i5 = i3 - this.fStart;
        ensureComponents(i4, i5);
        return makeTextLineOnRange(i4, i5).getMetrics().advance;
    }

    public TextLayout getLayout(int i2, int i3) {
        int i4 = i2 - this.fStart;
        int i5 = i3 - this.fStart;
        ensureComponents(i4, i5);
        TextLine textLineMakeTextLineOnRange = makeTextLineOnRange(i4, i5);
        if (i5 < this.fChars.length) {
            this.layoutCharCount += i3 - i2;
            this.layoutCount++;
        }
        return new TextLayout(textLineMakeTextLineOnRange, this.fBaseline, this.fBaselineOffsets, this.fJustifyRatio);
    }

    private void printStats() {
        System.out.println("formattedChars: " + this.formattedChars);
        this.collectStats = false;
    }

    public void insertChar(AttributedCharacterIterator attributedCharacterIterator, int i2) {
        if (this.collectStats) {
            printStats();
        }
        if (wantStats) {
            this.collectStats = true;
        }
        this.fStart = attributedCharacterIterator.getBeginIndex();
        int endIndex = attributedCharacterIterator.getEndIndex();
        if (endIndex - this.fStart != this.fChars.length + 1) {
            initAll(attributedCharacterIterator);
        }
        char[] cArr = new char[endIndex - this.fStart];
        int i3 = i2 - this.fStart;
        System.arraycopy(this.fChars, 0, cArr, 0, i3);
        cArr[i3] = attributedCharacterIterator.setIndex(i2);
        System.arraycopy(this.fChars, i3, cArr, i3 + 1, (endIndex - i2) - 1);
        this.fChars = cArr;
        if (this.fBidi != null || Bidi.requiresBidi(cArr, i3, i3 + 1) || attributedCharacterIterator.getAttribute(TextAttribute.BIDI_EMBEDDING) != null) {
            this.fBidi = new Bidi(attributedCharacterIterator);
            if (this.fBidi.isLeftToRight()) {
                this.fBidi = null;
            }
        }
        this.fParagraph = StyledParagraph.insertChar(attributedCharacterIterator, this.fChars, i2, this.fParagraph);
        invalidateComponents();
    }

    public void deleteChar(AttributedCharacterIterator attributedCharacterIterator, int i2) {
        this.fStart = attributedCharacterIterator.getBeginIndex();
        int endIndex = attributedCharacterIterator.getEndIndex();
        if (endIndex - this.fStart != this.fChars.length - 1) {
            initAll(attributedCharacterIterator);
        }
        char[] cArr = new char[endIndex - this.fStart];
        int i3 = i2 - this.fStart;
        System.arraycopy(this.fChars, 0, cArr, 0, i2 - this.fStart);
        System.arraycopy(this.fChars, i3 + 1, cArr, i3, endIndex - i2);
        this.fChars = cArr;
        if (this.fBidi != null) {
            this.fBidi = new Bidi(attributedCharacterIterator);
            if (this.fBidi.isLeftToRight()) {
                this.fBidi = null;
            }
        }
        this.fParagraph = StyledParagraph.deleteChar(attributedCharacterIterator, this.fChars, i2, this.fParagraph);
        invalidateComponents();
    }

    char[] getChars() {
        return this.fChars;
    }
}
