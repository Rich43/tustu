package javax.swing.text;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.text.BreakIterator;
import java.util.BitSet;
import java.util.Locale;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Highlighter;
import javax.swing.text.Position;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/GlyphView.class */
public class GlyphView extends View implements TabableView, Cloneable {
    private byte[] selections;
    int offset;
    int length;
    boolean impliedCR;
    boolean skipWidth;
    TabExpander expander;
    private float minimumSpan;
    private int[] breakSpots;

    /* renamed from: x, reason: collision with root package name */
    int f12836x;
    GlyphPainter painter;
    static GlyphPainter defaultPainter;
    private JustificationInfo justificationInfo;

    public GlyphView(Element element) {
        super(element);
        this.selections = null;
        this.minimumSpan = -1.0f;
        this.breakSpots = null;
        this.justificationInfo = null;
        this.offset = 0;
        this.length = 0;
        Element parentElement = element.getParentElement();
        AttributeSet attributes = element.getAttributes();
        this.impliedCR = (attributes == null || attributes.getAttribute(SwingUtilities2.IMPLIED_CR) == null || parentElement == null || parentElement.getElementCount() <= 1) ? false : true;
        this.skipWidth = element.getName().equals("br");
    }

    protected final Object clone() {
        Object objClone;
        try {
            objClone = super.clone();
        } catch (CloneNotSupportedException e2) {
            objClone = null;
        }
        return objClone;
    }

    public GlyphPainter getGlyphPainter() {
        return this.painter;
    }

    public void setGlyphPainter(GlyphPainter glyphPainter) {
        this.painter = glyphPainter;
    }

    public Segment getText(int i2, int i3) {
        Segment sharedSegment = SegmentCache.getSharedSegment();
        try {
            getDocument().getText(i2, i3 - i2, sharedSegment);
            return sharedSegment;
        } catch (BadLocationException e2) {
            throw new StateInvariantError("GlyphView: Stale view: " + ((Object) e2));
        }
    }

    public Color getBackground() {
        Document document = getDocument();
        if (document instanceof StyledDocument) {
            AttributeSet attributes = getAttributes();
            if (attributes.isDefined(StyleConstants.Background)) {
                return ((StyledDocument) document).getBackground(attributes);
            }
            return null;
        }
        return null;
    }

    public Color getForeground() {
        Document document = getDocument();
        if (document instanceof StyledDocument) {
            return ((StyledDocument) document).getForeground(getAttributes());
        }
        Container container = getContainer();
        if (container != null) {
            return container.getForeground();
        }
        return null;
    }

    public Font getFont() {
        Document document = getDocument();
        if (document instanceof StyledDocument) {
            return ((StyledDocument) document).getFont(getAttributes());
        }
        Container container = getContainer();
        if (container != null) {
            return container.getFont();
        }
        return null;
    }

    public boolean isUnderline() {
        return StyleConstants.isUnderline(getAttributes());
    }

    public boolean isStrikeThrough() {
        return StyleConstants.isStrikeThrough(getAttributes());
    }

    public boolean isSubscript() {
        return StyleConstants.isSubscript(getAttributes());
    }

    public boolean isSuperscript() {
        return StyleConstants.isSuperscript(getAttributes());
    }

    public TabExpander getTabExpander() {
        return this.expander;
    }

    protected void checkPainter() {
        Class<?> cls;
        if (this.painter == null) {
            if (defaultPainter == null) {
                try {
                    ClassLoader classLoader = getClass().getClassLoader();
                    if (classLoader != null) {
                        cls = classLoader.loadClass("javax.swing.text.GlyphPainter1");
                    } else {
                        cls = Class.forName("javax.swing.text.GlyphPainter1");
                    }
                    Object objNewInstance = cls.newInstance();
                    if (objNewInstance instanceof GlyphPainter) {
                        defaultPainter = (GlyphPainter) objNewInstance;
                    }
                } catch (Throwable th) {
                    throw new StateInvariantError("GlyphView: Can't load glyph painter: javax.swing.text.GlyphPainter1");
                }
            }
            setGlyphPainter(defaultPainter.getPainter(this, getStartOffset(), getEndOffset()));
        }
    }

    @Override // javax.swing.text.TabableView
    public float getTabbedSpan(float f2, TabExpander tabExpander) {
        checkPainter();
        TabExpander tabExpander2 = this.expander;
        this.expander = tabExpander;
        if (this.expander != tabExpander2) {
            preferenceChanged(null, true, false);
        }
        this.f12836x = (int) f2;
        return this.painter.getSpan(this, getStartOffset(), getEndOffset(), this.expander, f2);
    }

    @Override // javax.swing.text.TabableView
    public float getPartialSpan(int i2, int i3) {
        checkPainter();
        return this.painter.getSpan(this, i2, i3, this.expander, this.f12836x);
    }

    @Override // javax.swing.text.View
    public int getStartOffset() {
        Element element = getElement();
        return this.length > 0 ? element.getStartOffset() + this.offset : element.getStartOffset();
    }

    @Override // javax.swing.text.View
    public int getEndOffset() {
        Element element = getElement();
        return this.length > 0 ? element.getStartOffset() + this.offset + this.length : element.getEndOffset();
    }

    private void initSelections(int i2, int i3) {
        int i4 = (i3 - i2) + 1;
        if (this.selections == null || i4 > this.selections.length) {
            this.selections = new byte[i4];
            return;
        }
        int i5 = 0;
        while (i5 < i4) {
            int i6 = i5;
            i5++;
            this.selections[i6] = 0;
        }
    }

    @Override // javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Color color;
        checkPainter();
        boolean z2 = false;
        Container container = getContainer();
        int startOffset = getStartOffset();
        int endOffset = getEndOffset();
        Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
        Color background = getBackground();
        Color foreground = getForeground();
        if (container != null && !container.isEnabled()) {
            if (container instanceof JTextComponent) {
                color = ((JTextComponent) container).getDisabledTextColor();
            } else {
                color = UIManager.getColor("textInactiveText");
            }
            foreground = color;
        }
        if (background != null) {
            graphics.setColor(background);
            graphics.fillRect(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
        }
        if (container instanceof JTextComponent) {
            JTextComponent jTextComponent = (JTextComponent) container;
            Highlighter highlighter = jTextComponent.getHighlighter();
            if (highlighter instanceof LayeredHighlighter) {
                ((LayeredHighlighter) highlighter).paintLayeredHighlights(graphics, startOffset, endOffset, shape, jTextComponent, this);
            }
        }
        if (Utilities.isComposedTextElement(getElement())) {
            Utilities.paintComposedText(graphics, shape.getBounds(), this);
            z2 = true;
        } else if (container instanceof JTextComponent) {
            JTextComponent jTextComponent2 = (JTextComponent) container;
            Color selectedTextColor = jTextComponent2.getSelectedTextColor();
            if (jTextComponent2.getHighlighter() != null && selectedTextColor != null && !selectedTextColor.equals(foreground)) {
                Highlighter.Highlight[] highlights = jTextComponent2.getHighlighter().getHighlights();
                if (highlights.length != 0) {
                    boolean z3 = false;
                    int i2 = 0;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= highlights.length) {
                            break;
                        }
                        Highlighter.Highlight highlight = highlights[i3];
                        int startOffset2 = highlight.getStartOffset();
                        int endOffset2 = highlight.getEndOffset();
                        if (startOffset2 <= endOffset && endOffset2 >= startOffset && SwingUtilities2.useSelectedTextColor(highlight, jTextComponent2)) {
                            if (startOffset2 <= startOffset && endOffset2 >= endOffset) {
                                paintTextUsingColor(graphics, shape, selectedTextColor, startOffset, endOffset);
                                z2 = true;
                                break;
                            }
                            if (!z3) {
                                initSelections(startOffset, endOffset);
                                z3 = true;
                            }
                            int iMax = Math.max(startOffset, startOffset2);
                            int iMin = Math.min(endOffset, endOffset2);
                            paintTextUsingColor(graphics, shape, selectedTextColor, iMax, iMin);
                            byte[] bArr = this.selections;
                            int i4 = iMax - startOffset;
                            bArr[i4] = (byte) (bArr[i4] + 1);
                            byte[] bArr2 = this.selections;
                            int i5 = iMin - startOffset;
                            bArr2[i5] = (byte) (bArr2[i5] - 1);
                            i2++;
                        }
                        i3++;
                    }
                    if (!z2 && i2 > 0) {
                        int i6 = -1;
                        int i7 = 0;
                        int i8 = endOffset - startOffset;
                        while (true) {
                            int i9 = i6;
                            i6++;
                            if (i9 >= i8) {
                                break;
                            }
                            while (i6 < i8 && this.selections[i6] == 0) {
                                i6++;
                            }
                            if (i7 != i6) {
                                paintTextUsingColor(graphics, shape, foreground, startOffset + i7, startOffset + i6);
                            }
                            int i10 = 0;
                            while (i6 < i8) {
                                int i11 = i10 + this.selections[i6];
                                i10 = i11;
                                if (i11 != 0) {
                                    i6++;
                                }
                            }
                            i7 = i6;
                        }
                        z2 = true;
                    }
                }
            }
        }
        if (!z2) {
            paintTextUsingColor(graphics, shape, foreground, startOffset, endOffset);
        }
    }

    final void paintTextUsingColor(Graphics graphics, Shape shape, Color color, int i2, int i3) {
        graphics.setColor(color);
        this.painter.paint(this, graphics, shape, i2, i3);
        boolean zIsUnderline = isUnderline();
        boolean zIsStrikeThrough = isStrikeThrough();
        if (zIsUnderline || zIsStrikeThrough) {
            Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
            View parent = getParent();
            if (parent != null && parent.getEndOffset() == i3) {
                Segment text = getText(i2, i3);
                while (Character.isWhitespace(text.last())) {
                    i3--;
                    text.count--;
                }
                SegmentCache.releaseSharedSegment(text);
            }
            int span = bounds.f12372x;
            int startOffset = getStartOffset();
            if (startOffset != i2) {
                span += (int) this.painter.getSpan(this, startOffset, i2, getTabExpander(), span);
            }
            int span2 = span + ((int) this.painter.getSpan(this, i2, i3, getTabExpander(), span));
            int height = bounds.f12373y + ((int) (this.painter.getHeight(this) - this.painter.getDescent(this)));
            if (zIsUnderline) {
                int i4 = height + 1;
                graphics.drawLine(span, i4, span2, i4);
            }
            if (zIsStrikeThrough) {
                int ascent = height - ((int) (this.painter.getAscent(this) * 0.3f));
                graphics.drawLine(span, ascent, span2, ascent);
            }
        }
    }

    @Override // javax.swing.text.View
    public float getMinimumSpan(int i2) {
        switch (i2) {
            case 0:
                if (this.minimumSpan < 0.0f) {
                    this.minimumSpan = 0.0f;
                    int startOffset = getStartOffset();
                    int endOffset = getEndOffset();
                    while (true) {
                        int i3 = endOffset;
                        if (i3 > startOffset) {
                            int breakSpot = getBreakSpot(startOffset, i3);
                            if (breakSpot == -1) {
                                breakSpot = startOffset;
                            }
                            this.minimumSpan = Math.max(this.minimumSpan, getPartialSpan(breakSpot, i3));
                            endOffset = breakSpot - 1;
                        }
                    }
                }
                return this.minimumSpan;
            case 1:
                return super.getMinimumSpan(i2);
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.View
    public float getPreferredSpan(int i2) {
        if (this.impliedCR) {
            return 0.0f;
        }
        checkPainter();
        int startOffset = getStartOffset();
        int endOffset = getEndOffset();
        switch (i2) {
            case 0:
                if (this.skipWidth) {
                    return 0.0f;
                }
                return this.painter.getSpan(this, startOffset, endOffset, this.expander, this.f12836x);
            case 1:
                float height = this.painter.getHeight(this);
                if (isSuperscript()) {
                    height += height / 3.0f;
                }
                return height;
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.View
    public float getAlignment(int i2) {
        float f2;
        checkPainter();
        if (i2 == 1) {
            boolean zIsSuperscript = isSuperscript();
            boolean zIsSubscript = isSubscript();
            float height = this.painter.getHeight(this);
            float descent = this.painter.getDescent(this);
            float ascent = this.painter.getAscent(this);
            if (zIsSuperscript) {
                f2 = 1.0f;
            } else if (zIsSubscript) {
                f2 = height > 0.0f ? (height - (descent + (ascent / 2.0f))) / height : 0.0f;
            } else {
                f2 = height > 0.0f ? (height - descent) / height : 0.0f;
            }
            return f2;
        }
        return super.getAlignment(i2);
    }

    @Override // javax.swing.text.View
    public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
        checkPainter();
        return this.painter.modelToView(this, i2, bias, shape);
    }

    @Override // javax.swing.text.View
    public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        checkPainter();
        return this.painter.viewToModel(this, f2, f3, shape, biasArr);
    }

    @Override // javax.swing.text.View
    public int getBreakWeight(int i2, float f2, float f3) {
        if (i2 == 0) {
            checkPainter();
            int startOffset = getStartOffset();
            int boundedPosition = this.painter.getBoundedPosition(this, startOffset, f2, f3);
            if (boundedPosition == startOffset) {
                return 0;
            }
            return getBreakSpot(startOffset, boundedPosition) != -1 ? 2000 : 1000;
        }
        return super.getBreakWeight(i2, f2, f3);
    }

    @Override // javax.swing.text.View
    public View breakView(int i2, int i3, float f2, float f3) {
        if (i2 == 0) {
            checkPainter();
            int boundedPosition = this.painter.getBoundedPosition(this, i3, f2, f3);
            int breakSpot = getBreakSpot(i3, boundedPosition);
            if (breakSpot != -1) {
                boundedPosition = breakSpot;
            }
            if (i3 == getStartOffset() && boundedPosition == getEndOffset()) {
                return this;
            }
            GlyphView glyphView = (GlyphView) createFragment(i3, boundedPosition);
            glyphView.f12836x = (int) f2;
            return glyphView;
        }
        return this;
    }

    private int getBreakSpot(int i2, int i3) {
        if (this.breakSpots == null) {
            int startOffset = getStartOffset();
            int endOffset = getEndOffset();
            int[] iArr = new int[(endOffset + 1) - startOffset];
            int i4 = 0;
            Element parentElement = getElement().getParentElement();
            int startOffset2 = parentElement == null ? startOffset : parentElement.getStartOffset();
            int endOffset2 = parentElement == null ? endOffset : parentElement.getEndOffset();
            Segment text = getText(startOffset2, endOffset2);
            text.first();
            BreakIterator breaker = getBreaker();
            breaker.setText(text);
            int iPreceding = endOffset + (endOffset2 > endOffset ? 1 : 0);
            while (true) {
                iPreceding = breaker.preceding(text.offset + (iPreceding - startOffset2)) + (startOffset2 - text.offset);
                if (iPreceding <= startOffset) {
                    break;
                }
                int i5 = i4;
                i4++;
                iArr[i5] = iPreceding;
            }
            SegmentCache.releaseSharedSegment(text);
            this.breakSpots = new int[i4];
            System.arraycopy(iArr, 0, this.breakSpots, 0, i4);
        }
        int i6 = -1;
        int i7 = 0;
        while (true) {
            if (i7 >= this.breakSpots.length) {
                break;
            }
            int i8 = this.breakSpots[i7];
            if (i8 > i3) {
                i7++;
            } else if (i8 > i2) {
                i6 = i8;
            }
        }
        return i6;
    }

    private BreakIterator getBreaker() {
        Document document = getDocument();
        if (document != null && Boolean.TRUE.equals(document.getProperty(AbstractDocument.MultiByteProperty))) {
            Container container = getContainer();
            return BreakIterator.getLineInstance(container == null ? Locale.getDefault() : container.getLocale());
        }
        return new WhitespaceBasedBreakIterator();
    }

    @Override // javax.swing.text.View
    public View createFragment(int i2, int i3) {
        checkPainter();
        Element element = getElement();
        GlyphView glyphView = (GlyphView) clone();
        glyphView.offset = i2 - element.getStartOffset();
        glyphView.length = i3 - i2;
        glyphView.painter = this.painter.getPainter(glyphView, i2, i3);
        glyphView.justificationInfo = null;
        return glyphView;
    }

    @Override // javax.swing.text.View
    public int getNextVisualPositionFrom(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
        if (i2 < -1) {
            throw new BadLocationException("invalid position", i2);
        }
        return this.painter.getNextVisualPositionFrom(this, i2, bias, shape, i3, biasArr);
    }

    @Override // javax.swing.text.View
    public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        this.justificationInfo = null;
        this.breakSpots = null;
        this.minimumSpan = -1.0f;
        syncCR();
        preferenceChanged(null, true, false);
    }

    @Override // javax.swing.text.View
    public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        this.justificationInfo = null;
        this.breakSpots = null;
        this.minimumSpan = -1.0f;
        syncCR();
        preferenceChanged(null, true, false);
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        this.minimumSpan = -1.0f;
        syncCR();
        preferenceChanged(null, true, true);
    }

    private void syncCR() {
        if (this.impliedCR) {
            Element parentElement = getElement().getParentElement();
            this.impliedCR = parentElement != null && parentElement.getElementCount() > 1;
        }
    }

    @Override // javax.swing.text.View
    void updateAfterChange() {
        this.breakSpots = null;
    }

    /* loaded from: rt.jar:javax/swing/text/GlyphView$JustificationInfo.class */
    static class JustificationInfo {
        final int start;
        final int end;
        final int leadingSpaces;
        final int contentSpaces;
        final int trailingSpaces;
        final boolean hasTab;
        final BitSet spaceMap;

        JustificationInfo(int i2, int i3, int i4, int i5, int i6, boolean z2, BitSet bitSet) {
            this.start = i2;
            this.end = i3;
            this.leadingSpaces = i4;
            this.contentSpaces = i5;
            this.trailingSpaces = i6;
            this.hasTab = z2;
            this.spaceMap = bitSet;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    JustificationInfo getJustificationInfo(int i2) {
        if (this.justificationInfo != null) {
            return this.justificationInfo;
        }
        int startOffset = getStartOffset();
        int endOffset = getEndOffset();
        Segment text = getText(startOffset, endOffset);
        int i3 = text.offset;
        int i4 = (text.offset + text.count) - 1;
        int i5 = i4 + 1;
        int i6 = i3 - 1;
        int i7 = i3 - 1;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        boolean z2 = false;
        BitSet bitSet = new BitSet((endOffset - startOffset) + 1);
        int i11 = i4;
        boolean z3 = false;
        while (true) {
            if (i11 < i3) {
                break;
            }
            if (' ' == text.array[i11]) {
                bitSet.set(i11 - i3);
                if (!z3) {
                    i8++;
                } else if (z3) {
                    z3 = 2;
                    i10 = 1;
                } else if (z3 == 2) {
                    i10++;
                }
            } else {
                if ('\t' == text.array[i11]) {
                    z2 = true;
                    break;
                }
                if (!z3) {
                    if ('\n' != text.array[i11] && '\r' != text.array[i11]) {
                        z3 = true;
                        i6 = i11;
                    }
                } else if (!z3 && z3 == 2) {
                    i9 += i10;
                    i10 = 0;
                }
                i5 = i11;
            }
            i11--;
        }
        SegmentCache.releaseSharedSegment(text);
        int i12 = -1;
        if (i5 < i4) {
            i12 = i5 - i3;
        }
        int i13 = -1;
        if (i6 > i3) {
            i13 = i6 - i3;
        }
        this.justificationInfo = new JustificationInfo(i12, i13, i10, i9, i8, z2, bitSet);
        return this.justificationInfo;
    }

    /* loaded from: rt.jar:javax/swing/text/GlyphView$GlyphPainter.class */
    public static abstract class GlyphPainter {
        public abstract float getSpan(GlyphView glyphView, int i2, int i3, TabExpander tabExpander, float f2);

        public abstract float getHeight(GlyphView glyphView);

        public abstract float getAscent(GlyphView glyphView);

        public abstract float getDescent(GlyphView glyphView);

        public abstract void paint(GlyphView glyphView, Graphics graphics, Shape shape, int i2, int i3);

        public abstract Shape modelToView(GlyphView glyphView, int i2, Position.Bias bias, Shape shape) throws BadLocationException;

        public abstract int viewToModel(GlyphView glyphView, float f2, float f3, Shape shape, Position.Bias[] biasArr);

        public abstract int getBoundedPosition(GlyphView glyphView, int i2, float f2, float f3);

        public GlyphPainter getPainter(GlyphView glyphView, int i2, int i3) {
            return this;
        }

        public int getNextVisualPositionFrom(GlyphView glyphView, int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
            int i4;
            int startOffset = glyphView.getStartOffset();
            int endOffset = glyphView.getEndOffset();
            switch (i3) {
                case 1:
                case 5:
                    if (i2 != -1) {
                        return -1;
                    }
                    Container container = glyphView.getContainer();
                    if (container instanceof JTextComponent) {
                        Caret caret = ((JTextComponent) container).getCaret();
                        if ((caret != null ? caret.getMagicCaretPosition() : null) == null) {
                            biasArr[0] = Position.Bias.Forward;
                            return startOffset;
                        }
                        return glyphView.viewToModel(r18.f12370x, 0.0f, shape, biasArr);
                    }
                    return i2;
                case 2:
                case 4:
                case 6:
                default:
                    throw new IllegalArgumentException("Bad direction: " + i3);
                case 3:
                    if (startOffset == glyphView.getDocument().getLength()) {
                        if (i2 == -1) {
                            biasArr[0] = Position.Bias.Forward;
                            return startOffset;
                        }
                        return -1;
                    }
                    if (i2 == -1) {
                        biasArr[0] = Position.Bias.Forward;
                        return startOffset;
                    }
                    if (i2 == endOffset || (i4 = i2 + 1) == endOffset) {
                        return -1;
                    }
                    biasArr[0] = Position.Bias.Forward;
                    return i4;
                case 7:
                    if (startOffset == glyphView.getDocument().getLength()) {
                        if (i2 == -1) {
                            biasArr[0] = Position.Bias.Forward;
                            return startOffset;
                        }
                        return -1;
                    }
                    if (i2 == -1) {
                        biasArr[0] = Position.Bias.Forward;
                        return endOffset - 1;
                    }
                    if (i2 == startOffset) {
                        return -1;
                    }
                    biasArr[0] = Position.Bias.Forward;
                    return i2 - 1;
            }
        }
    }
}
