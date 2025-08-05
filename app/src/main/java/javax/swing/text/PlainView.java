package javax.swing.text;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/PlainView.class */
public class PlainView extends View implements TabExpander {
    protected FontMetrics metrics;
    Element longLine;
    Font font;
    Segment lineBuffer;
    int tabSize;
    int tabBase;
    int sel0;
    int sel1;
    Color unselected;
    Color selected;
    int firstLineOffset;

    public PlainView(Element element) {
        super(element);
    }

    protected int getTabSize() {
        Integer num = (Integer) getDocument().getProperty(PlainDocument.tabSizeAttribute);
        return num != null ? num.intValue() : 8;
    }

    protected void drawLine(int i2, Graphics graphics, int i3, int i4) {
        Element element = getElement().getElement(i2);
        try {
            if (element.isLeaf()) {
                drawElement(i2, element, graphics, i3, i4);
            } else {
                int elementCount = element.getElementCount();
                for (int i5 = 0; i5 < elementCount; i5++) {
                    i3 = drawElement(i2, element.getElement(i5), graphics, i3, i4);
                }
            }
        } catch (BadLocationException e2) {
            throw new StateInvariantError("Can't render line: " + i2);
        }
    }

    private int drawElement(int i2, Element element, Graphics graphics, int i3, int i4) throws BadLocationException {
        int iDrawUnselectedText;
        int startOffset = element.getStartOffset();
        int iMin = Math.min(getDocument().getLength(), element.getEndOffset());
        if (i2 == 0) {
            i3 += this.firstLineOffset;
        }
        AttributeSet attributes = element.getAttributes();
        if (Utilities.isComposedTextAttributeDefined(attributes)) {
            graphics.setColor(this.unselected);
            iDrawUnselectedText = Utilities.drawComposedText(this, attributes, graphics, i3, i4, startOffset - element.getStartOffset(), iMin - element.getStartOffset());
        } else if (this.sel0 == this.sel1 || this.selected == this.unselected) {
            iDrawUnselectedText = drawUnselectedText(graphics, i3, i4, startOffset, iMin);
        } else if (startOffset >= this.sel0 && startOffset <= this.sel1 && iMin >= this.sel0 && iMin <= this.sel1) {
            iDrawUnselectedText = drawSelectedText(graphics, i3, i4, startOffset, iMin);
        } else if (this.sel0 >= startOffset && this.sel0 <= iMin) {
            if (this.sel1 >= startOffset && this.sel1 <= iMin) {
                iDrawUnselectedText = drawUnselectedText(graphics, drawSelectedText(graphics, drawUnselectedText(graphics, i3, i4, startOffset, this.sel0), i4, this.sel0, this.sel1), i4, this.sel1, iMin);
            } else {
                iDrawUnselectedText = drawSelectedText(graphics, drawUnselectedText(graphics, i3, i4, startOffset, this.sel0), i4, this.sel0, iMin);
            }
        } else if (this.sel1 >= startOffset && this.sel1 <= iMin) {
            iDrawUnselectedText = drawUnselectedText(graphics, drawSelectedText(graphics, i3, i4, startOffset, this.sel1), i4, this.sel1, iMin);
        } else {
            iDrawUnselectedText = drawUnselectedText(graphics, i3, i4, startOffset, iMin);
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

    protected void updateMetrics() {
        if (this.font != getContainer().getFont()) {
            calculateLongestLine();
            this.tabSize = getTabSize() * this.metrics.charWidth('m');
        }
    }

    @Override // javax.swing.text.View
    public float getPreferredSpan(int i2) {
        updateMetrics();
        switch (i2) {
            case 0:
                return getLineWidth(this.longLine);
            case 1:
                return getElement().getElementCount() * this.metrics.getHeight();
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        int i2;
        int iMax;
        int iMax2;
        Shape shapeAdjustPaintRegion = adjustPaintRegion(shape);
        Rectangle rectangle = (Rectangle) shapeAdjustPaintRegion;
        this.tabBase = rectangle.f12372x;
        JTextComponent jTextComponent = (JTextComponent) getContainer();
        Highlighter highlighter = jTextComponent.getHighlighter();
        graphics.setFont(jTextComponent.getFont());
        this.sel0 = jTextComponent.getSelectionStart();
        this.sel1 = jTextComponent.getSelectionEnd();
        this.unselected = jTextComponent.isEnabled() ? jTextComponent.getForeground() : jTextComponent.getDisabledTextColor();
        this.selected = (!jTextComponent.getCaret().isSelectionVisible() || highlighter == null) ? this.unselected : jTextComponent.getSelectedTextColor();
        updateMetrics();
        Rectangle clipBounds = graphics.getClipBounds();
        int height = this.metrics.getHeight();
        int i3 = (rectangle.f12373y + rectangle.height) - (clipBounds.f12373y + clipBounds.height);
        int i4 = clipBounds.f12373y - rectangle.f12373y;
        if (height > 0) {
            iMax2 = Math.max(0, i3 / height);
            iMax = Math.max(0, i4 / height);
            i2 = rectangle.height / height;
            if (rectangle.height % height != 0) {
                i2++;
            }
        } else {
            i2 = 0;
            iMax = 0;
            iMax2 = 0;
        }
        Rectangle rectangleLineToRect = lineToRect(shapeAdjustPaintRegion, iMax);
        int ascent = rectangleLineToRect.f12373y + this.metrics.getAscent();
        int i5 = rectangleLineToRect.f12372x;
        Element element = getElement();
        int elementCount = element.getElementCount();
        int iMin = Math.min(elementCount, i2 - iMax2);
        int i6 = elementCount - 1;
        LayeredHighlighter layeredHighlighter = highlighter instanceof LayeredHighlighter ? (LayeredHighlighter) highlighter : null;
        for (int i7 = iMax; i7 < iMin; i7++) {
            if (layeredHighlighter != null) {
                Element element2 = element.getElement(i7);
                if (i7 == i6) {
                    layeredHighlighter.paintLayeredHighlights(graphics, element2.getStartOffset(), element2.getEndOffset(), shape, jTextComponent, this);
                } else {
                    layeredHighlighter.paintLayeredHighlights(graphics, element2.getStartOffset(), element2.getEndOffset() - 1, shape, jTextComponent, this);
                }
            }
            drawLine(i7, graphics, i5, ascent);
            ascent += height;
            if (i7 == 0) {
                i5 -= this.firstLineOffset;
            }
        }
    }

    Shape adjustPaintRegion(Shape shape) {
        return shape;
    }

    @Override // javax.swing.text.View
    public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
        Document document = getDocument();
        Element element = getElement();
        int elementIndex = element.getElementIndex(i2);
        if (elementIndex < 0) {
            return lineToRect(shape, 0);
        }
        Rectangle rectangleLineToRect = lineToRect(shape, elementIndex);
        this.tabBase = rectangleLineToRect.f12372x;
        int startOffset = element.getElement(elementIndex).getStartOffset();
        Segment sharedSegment = SegmentCache.getSharedSegment();
        document.getText(startOffset, i2 - startOffset, sharedSegment);
        int tabbedTextWidth = Utilities.getTabbedTextWidth(sharedSegment, this.metrics, this.tabBase, this, startOffset);
        SegmentCache.releaseSharedSegment(sharedSegment);
        rectangleLineToRect.f12372x += tabbedTextWidth;
        rectangleLineToRect.width = 1;
        rectangleLineToRect.height = this.metrics.getHeight();
        return rectangleLineToRect;
    }

    @Override // javax.swing.text.View
    public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        int elementCount;
        biasArr[0] = Position.Bias.Forward;
        Rectangle bounds = shape.getBounds();
        Document document = getDocument();
        int i2 = (int) f2;
        int i3 = (int) f3;
        if (i3 < bounds.f12373y) {
            return getStartOffset();
        }
        if (i3 > bounds.f12373y + bounds.height) {
            return getEndOffset() - 1;
        }
        Element defaultRootElement = document.getDefaultRootElement();
        int height = this.metrics.getHeight();
        if (height > 0) {
            elementCount = Math.abs((i3 - bounds.f12373y) / height);
        } else {
            elementCount = defaultRootElement.getElementCount() - 1;
        }
        int i4 = elementCount;
        if (i4 >= defaultRootElement.getElementCount()) {
            return getEndOffset() - 1;
        }
        Element element = defaultRootElement.getElement(i4);
        if (i4 == 0) {
            bounds.f12372x += this.firstLineOffset;
            bounds.width -= this.firstLineOffset;
        }
        if (i2 < bounds.f12372x) {
            return element.getStartOffset();
        }
        if (i2 > bounds.f12372x + bounds.width) {
            return element.getEndOffset() - 1;
        }
        try {
            int startOffset = element.getStartOffset();
            int endOffset = element.getEndOffset() - 1;
            Segment sharedSegment = SegmentCache.getSharedSegment();
            document.getText(startOffset, endOffset - startOffset, sharedSegment);
            this.tabBase = bounds.f12372x;
            int tabbedTextOffset = startOffset + Utilities.getTabbedTextOffset(sharedSegment, this.metrics, this.tabBase, i2, this, startOffset);
            SegmentCache.releaseSharedSegment(sharedSegment);
            return tabbedTextOffset;
        } catch (BadLocationException e2) {
            return -1;
        }
    }

    @Override // javax.swing.text.View
    public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        updateDamage(documentEvent, shape, viewFactory);
    }

    @Override // javax.swing.text.View
    public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        updateDamage(documentEvent, shape, viewFactory);
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        updateDamage(documentEvent, shape, viewFactory);
    }

    @Override // javax.swing.text.View
    public void setSize(float f2, float f3) {
        super.setSize(f2, f3);
        updateMetrics();
    }

    @Override // javax.swing.text.TabExpander
    public float nextTabStop(float f2, int i2) {
        if (this.tabSize == 0) {
            return f2;
        }
        return this.tabBase + ((((((int) f2) - this.tabBase) / this.tabSize) + 1) * this.tabSize);
    }

    protected void updateDamage(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        Container container = getContainer();
        updateMetrics();
        DocumentEvent.ElementChange change = documentEvent.getChange(getElement());
        Element[] childrenAdded = change != null ? change.getChildrenAdded() : null;
        Element[] childrenRemoved = change != null ? change.getChildrenRemoved() : null;
        if ((childrenAdded != null && childrenAdded.length > 0) || (childrenRemoved != null && childrenRemoved.length > 0)) {
            if (childrenAdded != null) {
                int lineWidth = getLineWidth(this.longLine);
                for (int i2 = 0; i2 < childrenAdded.length; i2++) {
                    int lineWidth2 = getLineWidth(childrenAdded[i2]);
                    if (lineWidth2 > lineWidth) {
                        lineWidth = lineWidth2;
                        this.longLine = childrenAdded[i2];
                    }
                }
            }
            if (childrenRemoved != null) {
                int i3 = 0;
                while (true) {
                    if (i3 >= childrenRemoved.length) {
                        break;
                    }
                    if (childrenRemoved[i3] != this.longLine) {
                        i3++;
                    } else {
                        calculateLongestLine();
                        break;
                    }
                }
            }
            preferenceChanged(null, true, true);
            container.repaint();
            return;
        }
        Element element = getElement();
        int elementIndex = element.getElementIndex(documentEvent.getOffset());
        damageLineRange(elementIndex, elementIndex, shape, container);
        if (documentEvent.getType() == DocumentEvent.EventType.INSERT) {
            int lineWidth3 = getLineWidth(this.longLine);
            Element element2 = element.getElement(elementIndex);
            if (element2 == this.longLine) {
                preferenceChanged(null, true, false);
                return;
            } else {
                if (getLineWidth(element2) > lineWidth3) {
                    this.longLine = element2;
                    preferenceChanged(null, true, false);
                    return;
                }
                return;
            }
        }
        if (documentEvent.getType() == DocumentEvent.EventType.REMOVE && element.getElement(elementIndex) == this.longLine) {
            calculateLongestLine();
            preferenceChanged(null, true, false);
        }
    }

    protected void damageLineRange(int i2, int i3, Shape shape, Component component) {
        if (shape != null) {
            Rectangle rectangleLineToRect = lineToRect(shape, i2);
            Rectangle rectangleLineToRect2 = lineToRect(shape, i3);
            if (rectangleLineToRect != null && rectangleLineToRect2 != null) {
                Rectangle rectangleUnion = rectangleLineToRect.union(rectangleLineToRect2);
                component.repaint(rectangleUnion.f12372x, rectangleUnion.f12373y, rectangleUnion.width, rectangleUnion.height);
            } else {
                component.repaint();
            }
        }
    }

    protected Rectangle lineToRect(Shape shape, int i2) {
        Rectangle rectangle = null;
        updateMetrics();
        if (this.metrics != null) {
            Rectangle bounds = shape.getBounds();
            if (i2 == 0) {
                bounds.f12372x += this.firstLineOffset;
                bounds.width -= this.firstLineOffset;
            }
            rectangle = new Rectangle(bounds.f12372x, bounds.f12373y + (i2 * this.metrics.getHeight()), bounds.width, this.metrics.getHeight());
        }
        return rectangle;
    }

    private void calculateLongestLine() {
        Container container = getContainer();
        this.font = container.getFont();
        this.metrics = container.getFontMetrics(this.font);
        getDocument();
        Element element = getElement();
        int elementCount = element.getElementCount();
        int i2 = -1;
        for (int i3 = 0; i3 < elementCount; i3++) {
            Element element2 = element.getElement(i3);
            int lineWidth = getLineWidth(element2);
            if (lineWidth > i2) {
                i2 = lineWidth;
                this.longLine = element2;
            }
        }
    }

    private int getLineWidth(Element element) {
        int tabbedTextWidth;
        if (element == null) {
            return 0;
        }
        int startOffset = element.getStartOffset();
        int endOffset = element.getEndOffset();
        Segment sharedSegment = SegmentCache.getSharedSegment();
        try {
            element.getDocument().getText(startOffset, endOffset - startOffset, sharedSegment);
            tabbedTextWidth = Utilities.getTabbedTextWidth(sharedSegment, this.metrics, this.tabBase, this, startOffset);
        } catch (BadLocationException e2) {
            tabbedTextWidth = 0;
        }
        SegmentCache.releaseSharedSegment(sharedSegment);
        return tabbedTextWidth;
    }
}
