package javax.swing.text;

import java.awt.Color;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.lang.ref.SoftReference;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/WrappedPlainView.class */
public class WrappedPlainView extends BoxView implements TabExpander {
    FontMetrics metrics;
    Segment lineBuffer;
    boolean widthChanging;
    int tabBase;
    int tabSize;
    boolean wordWrap;
    int sel0;
    int sel1;
    Color unselected;
    Color selected;

    public WrappedPlainView(Element element) {
        this(element, false);
    }

    public WrappedPlainView(Element element, boolean z2) {
        super(element, 1);
        this.wordWrap = z2;
    }

    protected int getTabSize() {
        Integer num = (Integer) getDocument().getProperty(PlainDocument.tabSizeAttribute);
        return num != null ? num.intValue() : 8;
    }

    protected void drawLine(int i2, int i3, Graphics graphics, int i4, int i5) {
        Element element = getElement();
        Element element2 = element.getElement(element.getElementIndex(i2));
        try {
            if (element2.isLeaf()) {
                drawText(element2, i2, i3, graphics, i4, i5);
            } else {
                int elementIndex = element2.getElementIndex(i3);
                for (int elementIndex2 = element2.getElementIndex(i2); elementIndex2 <= elementIndex; elementIndex2++) {
                    Element element3 = element2.getElement(elementIndex2);
                    i4 = drawText(element3, Math.max(element3.getStartOffset(), i2), Math.min(element3.getEndOffset(), i3), graphics, i4, i5);
                }
            }
        } catch (BadLocationException e2) {
            throw new StateInvariantError("Can't render: " + i2 + "," + i3);
        }
    }

    private int drawText(Element element, int i2, int i3, Graphics graphics, int i4, int i5) throws BadLocationException {
        int iDrawUnselectedText;
        int iMin = Math.min(getDocument().getLength(), i3);
        AttributeSet attributes = element.getAttributes();
        if (Utilities.isComposedTextAttributeDefined(attributes)) {
            graphics.setColor(this.unselected);
            iDrawUnselectedText = Utilities.drawComposedText(this, attributes, graphics, i4, i5, i2 - element.getStartOffset(), iMin - element.getStartOffset());
        } else if (this.sel0 == this.sel1 || this.selected == this.unselected) {
            iDrawUnselectedText = drawUnselectedText(graphics, i4, i5, i2, iMin);
        } else if (i2 >= this.sel0 && i2 <= this.sel1 && iMin >= this.sel0 && iMin <= this.sel1) {
            iDrawUnselectedText = drawSelectedText(graphics, i4, i5, i2, iMin);
        } else if (this.sel0 >= i2 && this.sel0 <= iMin) {
            if (this.sel1 >= i2 && this.sel1 <= iMin) {
                iDrawUnselectedText = drawUnselectedText(graphics, drawSelectedText(graphics, drawUnselectedText(graphics, i4, i5, i2, this.sel0), i5, this.sel0, this.sel1), i5, this.sel1, iMin);
            } else {
                iDrawUnselectedText = drawSelectedText(graphics, drawUnselectedText(graphics, i4, i5, i2, this.sel0), i5, this.sel0, iMin);
            }
        } else if (this.sel1 >= i2 && this.sel1 <= iMin) {
            iDrawUnselectedText = drawUnselectedText(graphics, drawSelectedText(graphics, i4, i5, i2, this.sel1), i5, this.sel1, iMin);
        } else {
            iDrawUnselectedText = drawUnselectedText(graphics, i4, i5, i2, iMin);
        }
        return iDrawUnselectedText;
    }

    protected int drawUnselectedText(Graphics graphics, int i2, int i3, int i4, int i5) throws BadLocationException {
        graphics.setColor(this.unselected);
        Document document = getDocument();
        Segment sharedSegment = SegmentCache.getSharedSegment();
        document.getText(i4, i5 - i4, sharedSegment);
        int iDrawTabbedText = Utilities.drawTabbedText(this, sharedSegment, i2, i3, graphics, this, i4);
        SegmentCache.releaseSharedSegment(sharedSegment);
        return iDrawTabbedText;
    }

    protected int drawSelectedText(Graphics graphics, int i2, int i3, int i4, int i5) throws BadLocationException {
        graphics.setColor(this.selected);
        Document document = getDocument();
        Segment sharedSegment = SegmentCache.getSharedSegment();
        document.getText(i4, i5 - i4, sharedSegment);
        int iDrawTabbedText = Utilities.drawTabbedText(this, sharedSegment, i2, i3, graphics, this, i4);
        SegmentCache.releaseSharedSegment(sharedSegment);
        return iDrawTabbedText;
    }

    protected final Segment getLineBuffer() {
        if (this.lineBuffer == null) {
            this.lineBuffer = new Segment();
        }
        return this.lineBuffer;
    }

    protected int calculateBreakPosition(int i2, int i3) {
        int tabbedTextOffset;
        Segment sharedSegment = SegmentCache.getSharedSegment();
        loadText(sharedSegment, i2, i3);
        int width = getWidth();
        if (this.wordWrap) {
            tabbedTextOffset = i2 + Utilities.getBreakLocation(sharedSegment, this.metrics, this.tabBase, this.tabBase + width, this, i2);
        } else {
            tabbedTextOffset = i2 + Utilities.getTabbedTextOffset(sharedSegment, this.metrics, this.tabBase, this.tabBase + width, this, i2, false);
        }
        SegmentCache.releaseSharedSegment(sharedSegment);
        return tabbedTextOffset;
    }

    @Override // javax.swing.text.CompositeView
    protected void loadChildren(ViewFactory viewFactory) {
        Element element = getElement();
        int elementCount = element.getElementCount();
        if (elementCount > 0) {
            View[] viewArr = new View[elementCount];
            for (int i2 = 0; i2 < elementCount; i2++) {
                viewArr[i2] = new WrappedLine(element.getElement(i2));
            }
            replace(0, 0, viewArr);
        }
    }

    void updateChildren(DocumentEvent documentEvent, Shape shape) {
        DocumentEvent.ElementChange change = documentEvent.getChange(getElement());
        if (change != null) {
            Element[] childrenRemoved = change.getChildrenRemoved();
            Element[] childrenAdded = change.getChildrenAdded();
            View[] viewArr = new View[childrenAdded.length];
            for (int i2 = 0; i2 < childrenAdded.length; i2++) {
                viewArr[i2] = new WrappedLine(childrenAdded[i2]);
            }
            replace(change.getIndex(), childrenRemoved.length, viewArr);
            if (shape != null) {
                preferenceChanged(null, true, true);
                getContainer().repaint();
            }
        }
        updateMetrics();
    }

    final void loadText(Segment segment, int i2, int i3) {
        try {
            getDocument().getText(i2, i3 - i2, segment);
        } catch (BadLocationException e2) {
            throw new StateInvariantError("Can't get line text");
        }
    }

    final void updateMetrics() {
        Container container = getContainer();
        this.metrics = container.getFontMetrics(container.getFont());
        this.tabSize = getTabSize() * this.metrics.charWidth('m');
    }

    @Override // javax.swing.text.TabExpander
    public float nextTabStop(float f2, int i2) {
        if (this.tabSize == 0) {
            return f2;
        }
        return this.tabBase + ((((((int) f2) - this.tabBase) / this.tabSize) + 1) * this.tabSize);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        this.tabBase = ((Rectangle) shape).f12372x;
        JTextComponent jTextComponent = (JTextComponent) getContainer();
        this.sel0 = jTextComponent.getSelectionStart();
        this.sel1 = jTextComponent.getSelectionEnd();
        this.unselected = jTextComponent.isEnabled() ? jTextComponent.getForeground() : jTextComponent.getDisabledTextColor();
        this.selected = (!jTextComponent.getCaret().isSelectionVisible() || jTextComponent.getHighlighter() == null) ? this.unselected : jTextComponent.getSelectedTextColor();
        graphics.setFont(jTextComponent.getFont());
        super.paint(graphics, shape);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public void setSize(float f2, float f3) {
        updateMetrics();
        if (((int) f2) != getWidth()) {
            preferenceChanged(null, true, true);
            this.widthChanging = true;
        }
        super.setSize(f2, f3);
        this.widthChanging = false;
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getPreferredSpan(int i2) {
        updateMetrics();
        return super.getPreferredSpan(i2);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getMinimumSpan(int i2) {
        updateMetrics();
        return super.getMinimumSpan(i2);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getMaximumSpan(int i2) {
        updateMetrics();
        return super.getMaximumSpan(i2);
    }

    @Override // javax.swing.text.View
    public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        updateChildren(documentEvent, shape);
        Rectangle insideAllocation = (shape == null || !isAllocationValid()) ? null : getInsideAllocation(shape);
        View viewAtPosition = getViewAtPosition(documentEvent.getOffset(), insideAllocation);
        if (viewAtPosition != null) {
            viewAtPosition.insertUpdate(documentEvent, insideAllocation, viewFactory);
        }
    }

    @Override // javax.swing.text.View
    public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        updateChildren(documentEvent, shape);
        Rectangle insideAllocation = (shape == null || !isAllocationValid()) ? null : getInsideAllocation(shape);
        View viewAtPosition = getViewAtPosition(documentEvent.getOffset(), insideAllocation);
        if (viewAtPosition != null) {
            viewAtPosition.removeUpdate(documentEvent, insideAllocation, viewFactory);
        }
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        updateChildren(documentEvent, shape);
    }

    /* loaded from: rt.jar:javax/swing/text/WrappedPlainView$WrappedLine.class */
    class WrappedLine extends View {
        int lineCount;
        SoftReference<int[]> lineCache;

        WrappedLine(Element element) {
            super(element);
            this.lineCache = null;
            this.lineCount = -1;
        }

        @Override // javax.swing.text.View
        public float getPreferredSpan(int i2) {
            switch (i2) {
                case 0:
                    float width = WrappedPlainView.this.getWidth();
                    if (width == 2.1474836E9f) {
                        return 100.0f;
                    }
                    return width;
                case 1:
                    if (this.lineCount < 0 || WrappedPlainView.this.widthChanging) {
                        breakLines(getStartOffset());
                    }
                    return this.lineCount * WrappedPlainView.this.metrics.getHeight();
                default:
                    throw new IllegalArgumentException("Invalid axis: " + i2);
            }
        }

        @Override // javax.swing.text.View
        public void paint(Graphics graphics, Shape shape) {
            Rectangle rectangle = (Rectangle) shape;
            int ascent = rectangle.f12373y + WrappedPlainView.this.metrics.getAscent();
            int i2 = rectangle.f12372x;
            JTextComponent jTextComponent = (JTextComponent) getContainer();
            Highlighter highlighter = jTextComponent.getHighlighter();
            LayeredHighlighter layeredHighlighter = highlighter instanceof LayeredHighlighter ? (LayeredHighlighter) highlighter : null;
            int startOffset = getStartOffset();
            int endOffset = getEndOffset();
            int i3 = startOffset;
            int[] lineEnds = getLineEnds();
            for (int i4 = 0; i4 < this.lineCount; i4++) {
                int i5 = lineEnds == null ? endOffset : startOffset + lineEnds[i4];
                if (layeredHighlighter != null) {
                    layeredHighlighter.paintLayeredHighlights(graphics, i3, i5 == endOffset ? i5 - 1 : i5, shape, jTextComponent, this);
                }
                WrappedPlainView.this.drawLine(i3, i5, graphics, i2, ascent);
                i3 = i5;
                ascent += WrappedPlainView.this.metrics.getHeight();
            }
        }

        @Override // javax.swing.text.View
        public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
            Rectangle bounds = shape.getBounds();
            bounds.height = WrappedPlainView.this.metrics.getHeight();
            bounds.width = 1;
            int startOffset = getStartOffset();
            if (i2 < startOffset || i2 > getEndOffset()) {
                throw new BadLocationException("Position out of range", i2);
            }
            int iMax = bias == Position.Bias.Forward ? i2 : Math.max(startOffset, i2 - 1);
            int[] lineEnds = getLineEnds();
            if (lineEnds != null) {
                int iFindLine = findLine(iMax - startOffset);
                if (iFindLine > 0) {
                    startOffset += lineEnds[iFindLine - 1];
                }
                bounds.f12373y += bounds.height * iFindLine;
            }
            if (i2 > startOffset) {
                Segment sharedSegment = SegmentCache.getSharedSegment();
                WrappedPlainView.this.loadText(sharedSegment, startOffset, i2);
                bounds.f12372x += Utilities.getTabbedTextWidth(sharedSegment, WrappedPlainView.this.metrics, bounds.f12372x, WrappedPlainView.this, startOffset);
                SegmentCache.releaseSharedSegment(sharedSegment);
            }
            return bounds;
        }

        @Override // javax.swing.text.View
        public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
            int endOffset;
            biasArr[0] = Position.Bias.Forward;
            Rectangle rectangle = (Rectangle) shape;
            int i2 = (int) f2;
            int i3 = (int) f3;
            if (i3 < rectangle.f12373y) {
                return getStartOffset();
            }
            if (i3 > rectangle.f12373y + rectangle.height) {
                return getEndOffset() - 1;
            }
            rectangle.height = WrappedPlainView.this.metrics.getHeight();
            int i4 = rectangle.height > 0 ? (i3 - rectangle.f12373y) / rectangle.height : this.lineCount - 1;
            if (i4 >= this.lineCount) {
                return getEndOffset() - 1;
            }
            int startOffset = getStartOffset();
            if (this.lineCount == 1) {
                endOffset = getEndOffset();
            } else {
                int[] lineEnds = getLineEnds();
                endOffset = startOffset + lineEnds[i4];
                if (i4 > 0) {
                    startOffset += lineEnds[i4 - 1];
                }
            }
            if (i2 < rectangle.f12372x) {
                return startOffset;
            }
            if (i2 > rectangle.f12372x + rectangle.width) {
                return endOffset - 1;
            }
            Segment sharedSegment = SegmentCache.getSharedSegment();
            WrappedPlainView.this.loadText(sharedSegment, startOffset, endOffset);
            int tabbedTextOffset = Utilities.getTabbedTextOffset(sharedSegment, WrappedPlainView.this.metrics, rectangle.f12372x, i2, WrappedPlainView.this, startOffset);
            SegmentCache.releaseSharedSegment(sharedSegment);
            return Math.min(startOffset + tabbedTextOffset, endOffset - 1);
        }

        @Override // javax.swing.text.View
        public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            update(documentEvent, shape);
        }

        @Override // javax.swing.text.View
        public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            update(documentEvent, shape);
        }

        private void update(DocumentEvent documentEvent, Shape shape) {
            int i2 = this.lineCount;
            breakLines(documentEvent.getOffset());
            if (i2 != this.lineCount) {
                WrappedPlainView.this.preferenceChanged(this, false, true);
                getContainer().repaint();
            } else if (shape != null) {
                Rectangle rectangle = (Rectangle) shape;
                getContainer().repaint(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
            }
        }

        final int[] getLineEnds() {
            if (this.lineCache == null) {
                return null;
            }
            int[] iArr = this.lineCache.get();
            if (iArr == null) {
                return breakLines(getStartOffset());
            }
            return iArr;
        }

        final int[] breakLines(int i2) {
            int i3;
            int[] iArr = this.lineCache == null ? null : this.lineCache.get();
            int startOffset = getStartOffset();
            int iFindLine = 0;
            if (iArr != null) {
                iFindLine = findLine(i2 - startOffset);
                if (iFindLine > 0) {
                    iFindLine--;
                }
            }
            int i4 = iFindLine == 0 ? startOffset : startOffset + iArr[iFindLine - 1];
            int endOffset = getEndOffset();
            while (true) {
                if (i4 >= endOffset) {
                    break;
                }
                int iCalculateBreakPosition = WrappedPlainView.this.calculateBreakPosition(i4, endOffset);
                i4 = iCalculateBreakPosition == i4 ? iCalculateBreakPosition + 1 : iCalculateBreakPosition;
                if (iFindLine == 0 && i4 >= endOffset) {
                    this.lineCache = null;
                    iArr = null;
                    iFindLine = 1;
                    break;
                }
                if (iArr == null || iFindLine >= iArr.length) {
                    int[] iArr2 = new int[Math.max((int) Math.ceil((iFindLine + 1) * ((endOffset - startOffset) / (i4 - startOffset))), iFindLine + 2)];
                    if (iArr != null) {
                        System.arraycopy(iArr, 0, iArr2, 0, iFindLine);
                    }
                    iArr = iArr2;
                }
                int i5 = iFindLine;
                iFindLine++;
                iArr[i5] = i4 - startOffset;
            }
            this.lineCount = iFindLine;
            if (this.lineCount > 1 && iArr.length > (i3 = this.lineCount + (this.lineCount / 3))) {
                int[] iArr3 = new int[i3];
                System.arraycopy(iArr, 0, iArr3, 0, this.lineCount);
                iArr = iArr3;
            }
            if (iArr != null && iArr != iArr) {
                this.lineCache = new SoftReference<>(iArr);
            }
            return iArr;
        }

        private int findLine(int i2) {
            int[] iArr = this.lineCache.get();
            if (i2 < iArr[0]) {
                return 0;
            }
            if (i2 > iArr[this.lineCount - 1]) {
                return this.lineCount;
            }
            return findLine(iArr, i2, 0, this.lineCount - 1);
        }

        private int findLine(int[] iArr, int i2, int i3, int i4) {
            if (i4 - i3 <= 1) {
                return i4;
            }
            int i5 = (i4 + i3) / 2;
            if (i2 < iArr[i5]) {
                return findLine(iArr, i2, i3, i5);
            }
            return findLine(iArr, i2, i5, i4);
        }
    }
}
