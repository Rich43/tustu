package javax.swing.text;

import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.text.BreakIterator;
import javax.swing.JComponent;
import javax.swing.text.Position;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/Utilities.class */
public class Utilities {
    static JComponent getJComponent(View view) {
        if (view != null) {
            Container container = view.getContainer();
            if (container instanceof JComponent) {
                return (JComponent) container;
            }
            return null;
        }
        return null;
    }

    public static final int drawTabbedText(Segment segment, int i2, int i3, Graphics graphics, TabExpander tabExpander, int i4) {
        return drawTabbedText(null, segment, i2, i3, graphics, tabExpander, i4);
    }

    static final int drawTabbedText(View view, Segment segment, int i2, int i3, Graphics graphics, TabExpander tabExpander, int i4) {
        return drawTabbedText(view, segment, i2, i3, graphics, tabExpander, i4, null);
    }

    static final int drawTabbedText(View view, Segment segment, int i2, int i3, Graphics graphics, TabExpander tabExpander, int i4, int[] iArr) {
        View parent;
        JComponent jComponent = getJComponent(view);
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jComponent, graphics);
        int iDrawChars = i2;
        char[] cArr = segment.array;
        int i5 = segment.offset;
        int i6 = 0;
        int i7 = segment.offset;
        int i8 = 0;
        int i9 = -1;
        int i10 = 0;
        int i11 = 0;
        if (iArr != null) {
            int startOffset = (-i4) + i5;
            if (view != null && (parent = view.getParent()) != null) {
                startOffset += parent.getStartOffset();
            }
            i8 = iArr[0];
            i9 = iArr[1] + startOffset;
            i10 = iArr[2] + startOffset;
            i11 = iArr[3] + startOffset;
        }
        int i12 = segment.offset + segment.count;
        for (int i13 = i5; i13 < i12; i13++) {
            if (cArr[i13] == '\t' || ((i8 != 0 || i13 <= i9) && cArr[i13] == ' ' && i10 <= i13 && i13 <= i11)) {
                if (i6 > 0) {
                    iDrawChars = SwingUtilities2.drawChars(jComponent, graphics, cArr, i7, i6, i2, i3);
                    i6 = 0;
                }
                i7 = i13 + 1;
                if (cArr[i13] == '\t') {
                    if (tabExpander != null) {
                        iDrawChars = (int) tabExpander.nextTabStop(iDrawChars, (i4 + i13) - i5);
                    } else {
                        iDrawChars += fontMetrics.charWidth(' ');
                    }
                } else if (cArr[i13] == ' ') {
                    iDrawChars += fontMetrics.charWidth(' ') + i8;
                    if (i13 <= i9) {
                        iDrawChars++;
                    }
                }
                i2 = iDrawChars;
            } else if (cArr[i13] == '\n' || cArr[i13] == '\r') {
                if (i6 > 0) {
                    iDrawChars = SwingUtilities2.drawChars(jComponent, graphics, cArr, i7, i6, i2, i3);
                    i6 = 0;
                }
                i7 = i13 + 1;
                i2 = iDrawChars;
            } else {
                i6++;
            }
        }
        if (i6 > 0) {
            iDrawChars = SwingUtilities2.drawChars(jComponent, graphics, cArr, i7, i6, i2, i3);
        }
        return iDrawChars;
    }

    public static final int getTabbedTextWidth(Segment segment, FontMetrics fontMetrics, int i2, TabExpander tabExpander, int i3) {
        return getTabbedTextWidth(null, segment, fontMetrics, i2, tabExpander, i3, null);
    }

    static final int getTabbedTextWidth(View view, Segment segment, FontMetrics fontMetrics, int i2, TabExpander tabExpander, int i3, int[] iArr) {
        View parent;
        int iCharsWidth = i2;
        char[] cArr = segment.array;
        int i4 = segment.offset;
        int i5 = segment.offset + segment.count;
        int i6 = 0;
        int i7 = 0;
        int i8 = -1;
        int i9 = 0;
        int i10 = 0;
        if (iArr != null) {
            int startOffset = (-i3) + i4;
            if (view != null && (parent = view.getParent()) != null) {
                startOffset += parent.getStartOffset();
            }
            i7 = iArr[0];
            i8 = iArr[1] + startOffset;
            i9 = iArr[2] + startOffset;
            i10 = iArr[3] + startOffset;
        }
        for (int i11 = i4; i11 < i5; i11++) {
            if (cArr[i11] == '\t' || ((i7 != 0 || i11 <= i8) && cArr[i11] == ' ' && i9 <= i11 && i11 <= i10)) {
                iCharsWidth += fontMetrics.charsWidth(cArr, i11 - i6, i6);
                i6 = 0;
                if (cArr[i11] == '\t') {
                    if (tabExpander != null) {
                        iCharsWidth = (int) tabExpander.nextTabStop(iCharsWidth, (i3 + i11) - i4);
                    } else {
                        iCharsWidth += fontMetrics.charWidth(' ');
                    }
                } else if (cArr[i11] == ' ') {
                    iCharsWidth += fontMetrics.charWidth(' ') + i7;
                    if (i11 <= i8) {
                        iCharsWidth++;
                    }
                }
            } else if (cArr[i11] == '\n') {
                iCharsWidth += fontMetrics.charsWidth(cArr, i11 - i6, i6);
                i6 = 0;
            } else {
                i6++;
            }
        }
        return (iCharsWidth + fontMetrics.charsWidth(cArr, i5 - i6, i6)) - i2;
    }

    public static final int getTabbedTextOffset(Segment segment, FontMetrics fontMetrics, int i2, int i3, TabExpander tabExpander, int i4) {
        return getTabbedTextOffset(segment, fontMetrics, i2, i3, tabExpander, i4, true);
    }

    static final int getTabbedTextOffset(View view, Segment segment, FontMetrics fontMetrics, int i2, int i3, TabExpander tabExpander, int i4, int[] iArr) {
        return getTabbedTextOffset(view, segment, fontMetrics, i2, i3, tabExpander, i4, true, iArr);
    }

    public static final int getTabbedTextOffset(Segment segment, FontMetrics fontMetrics, int i2, int i3, TabExpander tabExpander, int i4, boolean z2) {
        return getTabbedTextOffset(null, segment, fontMetrics, i2, i3, tabExpander, i4, z2, null);
    }

    static final int getTabbedTextOffset(View view, Segment segment, FontMetrics fontMetrics, int i2, int i3, TabExpander tabExpander, int i4, boolean z2, int[] iArr) {
        int i5;
        View parent;
        if (i2 >= i3) {
            return 0;
        }
        int iCharWidth = i2;
        char[] cArr = segment.array;
        int i6 = segment.offset;
        int i7 = segment.count;
        int i8 = 0;
        int i9 = -1;
        int i10 = 0;
        int i11 = 0;
        if (iArr != null) {
            int startOffset = (-i4) + i6;
            if (view != null && (parent = view.getParent()) != null) {
                startOffset += parent.getStartOffset();
            }
            i8 = iArr[0];
            i9 = iArr[1] + startOffset;
            i10 = iArr[2] + startOffset;
            i11 = iArr[3] + startOffset;
        }
        int i12 = segment.offset + segment.count;
        for (int i13 = segment.offset; i13 < i12; i13++) {
            if (cArr[i13] == '\t' || ((i8 != 0 || i13 <= i9) && cArr[i13] == ' ' && i10 <= i13 && i13 <= i11)) {
                if (cArr[i13] == '\t') {
                    if (tabExpander != null) {
                        iCharWidth = (int) tabExpander.nextTabStop(iCharWidth, (i4 + i13) - i6);
                    } else {
                        iCharWidth += fontMetrics.charWidth(' ');
                    }
                } else if (cArr[i13] == ' ') {
                    iCharWidth += fontMetrics.charWidth(' ') + i8;
                    if (i13 <= i9) {
                        iCharWidth++;
                    }
                }
            } else {
                iCharWidth += fontMetrics.charWidth(cArr[i13]);
            }
            if (i3 < iCharWidth) {
                if (z2) {
                    i5 = (i13 + 1) - i6;
                    int iCharsWidth = fontMetrics.charsWidth(cArr, i6, i5);
                    int i14 = i3 - i2;
                    if (i14 < iCharsWidth) {
                        while (true) {
                            if (i5 <= 0) {
                                break;
                            }
                            int iCharsWidth2 = i5 > 1 ? fontMetrics.charsWidth(cArr, i6, i5 - 1) : 0;
                            if (i14 >= iCharsWidth2) {
                                if (i14 - iCharsWidth2 < iCharsWidth - i14) {
                                    i5--;
                                }
                            } else {
                                iCharsWidth = iCharsWidth2;
                                i5--;
                            }
                        }
                    }
                } else {
                    i5 = i13 - i6;
                    while (i5 > 0 && fontMetrics.charsWidth(cArr, i6, i5) > i3 - i2) {
                        i5--;
                    }
                }
                return i5;
            }
        }
        return i7;
    }

    public static final int getBreakLocation(Segment segment, FontMetrics fontMetrics, int i2, int i3, TabExpander tabExpander, int i4) {
        char[] cArr = segment.array;
        int i5 = segment.offset;
        int i6 = segment.count;
        int tabbedTextOffset = getTabbedTextOffset(segment, fontMetrics, i2, i3, tabExpander, i4, false);
        if (tabbedTextOffset >= i6 - 1) {
            return i6;
        }
        int i7 = i5 + tabbedTextOffset;
        while (true) {
            if (i7 < i5) {
                break;
            }
            char c2 = cArr[i7];
            if (c2 < 256) {
                if (!Character.isWhitespace(c2)) {
                    i7--;
                } else {
                    tabbedTextOffset = (i7 - i5) + 1;
                    break;
                }
            } else {
                BreakIterator lineInstance = BreakIterator.getLineInstance();
                lineInstance.setText(segment);
                int iPreceding = lineInstance.preceding(i7 + 1);
                if (iPreceding > i5) {
                    tabbedTextOffset = iPreceding - i5;
                }
            }
        }
        return tabbedTextOffset;
    }

    public static final int getRowStart(JTextComponent jTextComponent, int i2) throws BadLocationException {
        Rectangle rectangleModelToView = jTextComponent.modelToView(i2);
        if (rectangleModelToView == null) {
            return -1;
        }
        int i3 = i2;
        int i4 = rectangleModelToView.f12373y;
        while (rectangleModelToView != null && i4 == rectangleModelToView.f12373y) {
            if (rectangleModelToView.height != 0) {
                i2 = i3;
            }
            i3--;
            rectangleModelToView = i3 >= 0 ? jTextComponent.modelToView(i3) : null;
        }
        return i2;
    }

    public static final int getRowEnd(JTextComponent jTextComponent, int i2) throws BadLocationException {
        Rectangle rectangleModelToView = jTextComponent.modelToView(i2);
        if (rectangleModelToView == null) {
            return -1;
        }
        int length = jTextComponent.getDocument().getLength();
        int i3 = i2;
        int i4 = rectangleModelToView.f12373y;
        while (rectangleModelToView != null && i4 == rectangleModelToView.f12373y) {
            if (rectangleModelToView.height != 0) {
                i2 = i3;
            }
            i3++;
            rectangleModelToView = i3 <= length ? jTextComponent.modelToView(i3) : null;
        }
        return i2;
    }

    public static final int getPositionAbove(JTextComponent jTextComponent, int i2, int i3) throws BadLocationException {
        int rowStart = getRowStart(jTextComponent, i2) - 1;
        if (rowStart < 0) {
            return -1;
        }
        int i4 = Integer.MAX_VALUE;
        int i5 = 0;
        Rectangle rectangleModelToView = null;
        if (rowStart >= 0) {
            rectangleModelToView = jTextComponent.modelToView(rowStart);
            i5 = rectangleModelToView.f12373y;
        }
        while (rectangleModelToView != null && i5 == rectangleModelToView.f12373y) {
            int iAbs = Math.abs(rectangleModelToView.f12372x - i3);
            if (iAbs < i4) {
                i2 = rowStart;
                i4 = iAbs;
            }
            rowStart--;
            rectangleModelToView = rowStart >= 0 ? jTextComponent.modelToView(rowStart) : null;
        }
        return i2;
    }

    public static final int getPositionBelow(JTextComponent jTextComponent, int i2, int i3) throws BadLocationException {
        int rowEnd = getRowEnd(jTextComponent, i2) + 1;
        if (rowEnd <= 0) {
            return -1;
        }
        int i4 = Integer.MAX_VALUE;
        int length = jTextComponent.getDocument().getLength();
        int i5 = 0;
        Rectangle rectangleModelToView = null;
        if (rowEnd <= length) {
            rectangleModelToView = jTextComponent.modelToView(rowEnd);
            i5 = rectangleModelToView.f12373y;
        }
        while (rectangleModelToView != null && i5 == rectangleModelToView.f12373y) {
            int iAbs = Math.abs(i3 - rectangleModelToView.f12372x);
            if (iAbs < i4) {
                i2 = rowEnd;
                i4 = iAbs;
            }
            rowEnd++;
            rectangleModelToView = rowEnd <= length ? jTextComponent.modelToView(rowEnd) : null;
        }
        return i2;
    }

    public static final int getWordStart(JTextComponent jTextComponent, int i2) throws BadLocationException {
        Document document = jTextComponent.getDocument();
        Element paragraphElement = getParagraphElement(jTextComponent, i2);
        if (paragraphElement == null) {
            throw new BadLocationException("No word at " + i2, i2);
        }
        int startOffset = paragraphElement.getStartOffset();
        int iMin = Math.min(paragraphElement.getEndOffset(), document.getLength());
        Segment sharedSegment = SegmentCache.getSharedSegment();
        document.getText(startOffset, iMin - startOffset, sharedSegment);
        if (sharedSegment.count > 0) {
            BreakIterator wordInstance = BreakIterator.getWordInstance(jTextComponent.getLocale());
            wordInstance.setText(sharedSegment);
            int iLast = (sharedSegment.offset + i2) - startOffset;
            if (iLast >= wordInstance.last()) {
                iLast = wordInstance.last() - 1;
            }
            wordInstance.following(iLast);
            i2 = (startOffset + wordInstance.previous()) - sharedSegment.offset;
        }
        SegmentCache.releaseSharedSegment(sharedSegment);
        return i2;
    }

    public static final int getWordEnd(JTextComponent jTextComponent, int i2) throws BadLocationException {
        Document document = jTextComponent.getDocument();
        Element paragraphElement = getParagraphElement(jTextComponent, i2);
        if (paragraphElement == null) {
            throw new BadLocationException("No word at " + i2, i2);
        }
        int startOffset = paragraphElement.getStartOffset();
        int iMin = Math.min(paragraphElement.getEndOffset(), document.getLength());
        Segment sharedSegment = SegmentCache.getSharedSegment();
        document.getText(startOffset, iMin - startOffset, sharedSegment);
        if (sharedSegment.count > 0) {
            BreakIterator wordInstance = BreakIterator.getWordInstance(jTextComponent.getLocale());
            wordInstance.setText(sharedSegment);
            int iLast = (i2 - startOffset) + sharedSegment.offset;
            if (iLast >= wordInstance.last()) {
                iLast = wordInstance.last() - 1;
            }
            i2 = (startOffset + wordInstance.following(iLast)) - sharedSegment.offset;
        }
        SegmentCache.releaseSharedSegment(sharedSegment);
        return i2;
    }

    public static final int getNextWord(JTextComponent jTextComponent, int i2) throws BadLocationException {
        Element paragraphElement = getParagraphElement(jTextComponent, i2);
        int nextWordInParagraph = getNextWordInParagraph(jTextComponent, paragraphElement, i2, false);
        while (true) {
            int i3 = nextWordInParagraph;
            if (i3 == -1) {
                int endOffset = paragraphElement.getEndOffset();
                paragraphElement = getParagraphElement(jTextComponent, endOffset);
                nextWordInParagraph = getNextWordInParagraph(jTextComponent, paragraphElement, endOffset, true);
            } else {
                return i3;
            }
        }
    }

    static int getNextWordInParagraph(JTextComponent jTextComponent, Element element, int i2, boolean z2) throws BadLocationException {
        int i3;
        if (element == null) {
            throw new BadLocationException("No more words", i2);
        }
        Document document = element.getDocument();
        int startOffset = element.getStartOffset();
        int iMin = Math.min(element.getEndOffset(), document.getLength());
        if (i2 >= iMin || i2 < startOffset) {
            throw new BadLocationException("No more words", i2);
        }
        Segment sharedSegment = SegmentCache.getSharedSegment();
        document.getText(startOffset, iMin - startOffset, sharedSegment);
        BreakIterator wordInstance = BreakIterator.getWordInstance(jTextComponent.getLocale());
        wordInstance.setText(sharedSegment);
        if (z2 && wordInstance.first() == (sharedSegment.offset + i2) - startOffset && !Character.isWhitespace(sharedSegment.array[wordInstance.first()])) {
            return i2;
        }
        int iFollowing = wordInstance.following((sharedSegment.offset + i2) - startOffset);
        if (iFollowing == -1 || iFollowing >= sharedSegment.offset + sharedSegment.count) {
            return -1;
        }
        if (!Character.isWhitespace(sharedSegment.array[iFollowing])) {
            return (startOffset + iFollowing) - sharedSegment.offset;
        }
        int next = wordInstance.next();
        if (next != -1 && (i3 = (startOffset + next) - sharedSegment.offset) != iMin) {
            return i3;
        }
        SegmentCache.releaseSharedSegment(sharedSegment);
        return -1;
    }

    public static final int getPreviousWord(JTextComponent jTextComponent, int i2) throws BadLocationException {
        Element paragraphElement = getParagraphElement(jTextComponent, i2);
        int prevWordInParagraph = getPrevWordInParagraph(jTextComponent, paragraphElement, i2);
        while (true) {
            int i3 = prevWordInParagraph;
            if (i3 == -1) {
                int startOffset = paragraphElement.getStartOffset() - 1;
                paragraphElement = getParagraphElement(jTextComponent, startOffset);
                prevWordInParagraph = getPrevWordInParagraph(jTextComponent, paragraphElement, startOffset);
            } else {
                return i3;
            }
        }
    }

    static int getPrevWordInParagraph(JTextComponent jTextComponent, Element element, int i2) throws BadLocationException {
        if (element == null) {
            throw new BadLocationException("No more words", i2);
        }
        Document document = element.getDocument();
        int startOffset = element.getStartOffset();
        int endOffset = element.getEndOffset();
        if (i2 > endOffset || i2 < startOffset) {
            throw new BadLocationException("No more words", i2);
        }
        Segment sharedSegment = SegmentCache.getSharedSegment();
        document.getText(startOffset, endOffset - startOffset, sharedSegment);
        BreakIterator wordInstance = BreakIterator.getWordInstance(jTextComponent.getLocale());
        wordInstance.setText(sharedSegment);
        if (wordInstance.following((sharedSegment.offset + i2) - startOffset) == -1) {
            wordInstance.last();
        }
        int iPrevious = wordInstance.previous();
        if (iPrevious == (sharedSegment.offset + i2) - startOffset) {
            iPrevious = wordInstance.previous();
        }
        if (iPrevious == -1) {
            return -1;
        }
        if (!Character.isWhitespace(sharedSegment.array[iPrevious])) {
            return (startOffset + iPrevious) - sharedSegment.offset;
        }
        int iPrevious2 = wordInstance.previous();
        if (iPrevious2 != -1) {
            return (startOffset + iPrevious2) - sharedSegment.offset;
        }
        SegmentCache.releaseSharedSegment(sharedSegment);
        return -1;
    }

    public static final Element getParagraphElement(JTextComponent jTextComponent, int i2) {
        Document document = jTextComponent.getDocument();
        if (document instanceof StyledDocument) {
            return ((StyledDocument) document).getParagraphElement(i2);
        }
        Element defaultRootElement = document.getDefaultRootElement();
        Element element = defaultRootElement.getElement(defaultRootElement.getElementIndex(i2));
        if (i2 >= element.getStartOffset() && i2 < element.getEndOffset()) {
            return element;
        }
        return null;
    }

    static boolean isComposedTextElement(Document document, int i2) {
        Element defaultRootElement = document.getDefaultRootElement();
        while (true) {
            Element element = defaultRootElement;
            if (!element.isLeaf()) {
                defaultRootElement = element.getElement(element.getElementIndex(i2));
            } else {
                return isComposedTextElement(element);
            }
        }
    }

    static boolean isComposedTextElement(Element element) {
        return isComposedTextAttributeDefined(element.getAttributes());
    }

    static boolean isComposedTextAttributeDefined(AttributeSet attributeSet) {
        return attributeSet != null && attributeSet.isDefined(StyleConstants.ComposedTextAttribute);
    }

    static int drawComposedText(View view, AttributeSet attributeSet, Graphics graphics, int i2, int i3, int i4, int i5) throws BadLocationException {
        Graphics2D graphics2D = (Graphics2D) graphics;
        AttributedString attributedString = (AttributedString) attributeSet.getAttribute(StyleConstants.ComposedTextAttribute);
        attributedString.addAttribute(TextAttribute.FONT, graphics.getFont());
        if (i4 >= i5) {
            return i2;
        }
        return i2 + ((int) SwingUtilities2.drawString(getJComponent(view), graphics2D, attributedString.getIterator(null, i4, i5), i2, i3));
    }

    static void paintComposedText(Graphics graphics, Rectangle rectangle, GlyphView glyphView) {
        if (graphics instanceof Graphics2D) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            int startOffset = glyphView.getStartOffset();
            int endOffset = glyphView.getEndOffset();
            AttributedString attributedString = (AttributedString) glyphView.getElement().getAttributes().getAttribute(StyleConstants.ComposedTextAttribute);
            int startOffset2 = glyphView.getElement().getStartOffset();
            int descent = (rectangle.f12373y + rectangle.height) - ((int) glyphView.getGlyphPainter().getDescent(glyphView));
            int i2 = rectangle.f12372x;
            attributedString.addAttribute(TextAttribute.FONT, glyphView.getFont());
            attributedString.addAttribute(TextAttribute.FOREGROUND, glyphView.getForeground());
            if (StyleConstants.isBold(glyphView.getAttributes())) {
                attributedString.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
            }
            if (StyleConstants.isItalic(glyphView.getAttributes())) {
                attributedString.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
            }
            if (glyphView.isUnderline()) {
                attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            }
            if (glyphView.isStrikeThrough()) {
                attributedString.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            }
            if (glyphView.isSuperscript()) {
                attributedString.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
            }
            if (glyphView.isSubscript()) {
                attributedString.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
            }
            SwingUtilities2.drawString(getJComponent(glyphView), graphics2D, attributedString.getIterator(null, startOffset - startOffset2, endOffset - startOffset2), i2, descent);
        }
    }

    static boolean isLeftToRight(Component component) {
        return component.getComponentOrientation().isLeftToRight();
    }

    static int getNextVisualPositionFrom(View view, int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
        int viewIndex;
        int nextVisualPositionFrom;
        if (view.getViewCount() == 0) {
            return i2;
        }
        boolean z2 = i3 == 1 || i3 == 7;
        if (i2 == -1) {
            int viewCount = z2 ? view.getViewCount() - 1 : 0;
            nextVisualPositionFrom = view.getView(viewCount).getNextVisualPositionFrom(i2, bias, view.getChildAllocation(viewCount, shape), i3, biasArr);
            if (nextVisualPositionFrom == -1 && !z2 && view.getViewCount() > 1) {
                nextVisualPositionFrom = view.getView(1).getNextVisualPositionFrom(-1, biasArr[0], view.getChildAllocation(1, shape), i3, biasArr);
            }
        } else {
            int i4 = z2 ? -1 : 1;
            if (bias == Position.Bias.Backward && i2 > 0) {
                viewIndex = view.getViewIndex(i2 - 1, Position.Bias.Forward);
            } else {
                viewIndex = view.getViewIndex(i2, Position.Bias.Forward);
            }
            View view2 = view.getView(viewIndex);
            nextVisualPositionFrom = view2.getNextVisualPositionFrom(i2, bias, view.getChildAllocation(viewIndex, shape), i3, biasArr);
            if ((i3 == 3 || i3 == 7) && (view instanceof CompositeView) && ((CompositeView) view).flipEastAndWestAtEnds(i2, bias)) {
                i4 *= -1;
            }
            int i5 = viewIndex + i4;
            if (nextVisualPositionFrom == -1 && i5 >= 0 && i5 < view.getViewCount()) {
                nextVisualPositionFrom = view.getView(i5).getNextVisualPositionFrom(-1, bias, view.getChildAllocation(i5, shape), i3, biasArr);
                if (nextVisualPositionFrom == i2 && biasArr[0] != bias) {
                    return getNextVisualPositionFrom(view, i2, biasArr[0], shape, i3, biasArr);
                }
            } else if (nextVisualPositionFrom != -1 && biasArr[0] != bias && (((i4 == 1 && view2.getEndOffset() == nextVisualPositionFrom) || (i4 == -1 && view2.getStartOffset() == nextVisualPositionFrom)) && i5 >= 0 && i5 < view.getViewCount())) {
                View view3 = view.getView(i5);
                Shape childAllocation = view.getChildAllocation(i5, shape);
                Position.Bias bias2 = biasArr[0];
                int nextVisualPositionFrom2 = view3.getNextVisualPositionFrom(-1, bias, childAllocation, i3, biasArr);
                if (biasArr[0] == bias) {
                    nextVisualPositionFrom = nextVisualPositionFrom2;
                } else {
                    biasArr[0] = bias2;
                }
            }
        }
        return nextVisualPositionFrom;
    }
}
