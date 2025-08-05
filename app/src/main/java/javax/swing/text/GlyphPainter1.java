package javax.swing.text;

import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import javax.swing.text.GlyphView;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/GlyphPainter1.class */
class GlyphPainter1 extends GlyphView.GlyphPainter {
    FontMetrics metrics;

    GlyphPainter1() {
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public float getSpan(GlyphView glyphView, int i2, int i3, TabExpander tabExpander, float f2) {
        sync(glyphView);
        Segment text = glyphView.getText(i2, i3);
        int tabbedTextWidth = Utilities.getTabbedTextWidth(glyphView, text, this.metrics, (int) f2, tabExpander, i2, getJustificationData(glyphView));
        SegmentCache.releaseSharedSegment(text);
        return tabbedTextWidth;
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public float getHeight(GlyphView glyphView) {
        sync(glyphView);
        return this.metrics.getHeight();
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public float getAscent(GlyphView glyphView) {
        sync(glyphView);
        return this.metrics.getAscent();
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public float getDescent(GlyphView glyphView) {
        sync(glyphView);
        return this.metrics.getDescent();
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public void paint(GlyphView glyphView, Graphics graphics, Shape shape, int i2, int i3) {
        sync(glyphView);
        TabExpander tabExpander = glyphView.getTabExpander();
        Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
        int tabbedTextWidth = bounds.f12372x;
        int startOffset = glyphView.getStartOffset();
        int[] justificationData = getJustificationData(glyphView);
        if (startOffset != i2) {
            Segment text = glyphView.getText(startOffset, i2);
            tabbedTextWidth += Utilities.getTabbedTextWidth(glyphView, text, this.metrics, tabbedTextWidth, tabExpander, startOffset, justificationData);
            SegmentCache.releaseSharedSegment(text);
        }
        int height = (bounds.f12373y + this.metrics.getHeight()) - this.metrics.getDescent();
        Segment text2 = glyphView.getText(i2, i3);
        graphics.setFont(this.metrics.getFont());
        Utilities.drawTabbedText(glyphView, text2, tabbedTextWidth, height, graphics, tabExpander, i2, justificationData);
        SegmentCache.releaseSharedSegment(text2);
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public Shape modelToView(GlyphView glyphView, int i2, Position.Bias bias, Shape shape) throws BadLocationException {
        sync(glyphView);
        Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
        int startOffset = glyphView.getStartOffset();
        int endOffset = glyphView.getEndOffset();
        TabExpander tabExpander = glyphView.getTabExpander();
        if (i2 == endOffset) {
            return new Rectangle(bounds.f12372x + bounds.width, bounds.f12373y, 0, this.metrics.getHeight());
        }
        if (i2 >= startOffset && i2 <= endOffset) {
            Segment text = glyphView.getText(startOffset, i2);
            int tabbedTextWidth = Utilities.getTabbedTextWidth(glyphView, text, this.metrics, bounds.f12372x, tabExpander, startOffset, getJustificationData(glyphView));
            SegmentCache.releaseSharedSegment(text);
            return new Rectangle(bounds.f12372x + tabbedTextWidth, bounds.f12373y, 0, this.metrics.getHeight());
        }
        throw new BadLocationException("modelToView - can't convert", endOffset);
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public int viewToModel(GlyphView glyphView, float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        sync(glyphView);
        Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
        int startOffset = glyphView.getStartOffset();
        int endOffset = glyphView.getEndOffset();
        TabExpander tabExpander = glyphView.getTabExpander();
        Segment text = glyphView.getText(startOffset, endOffset);
        int tabbedTextOffset = Utilities.getTabbedTextOffset(glyphView, text, this.metrics, bounds.f12372x, (int) f2, tabExpander, startOffset, getJustificationData(glyphView));
        SegmentCache.releaseSharedSegment(text);
        int i2 = startOffset + tabbedTextOffset;
        if (i2 == endOffset) {
            i2--;
        }
        biasArr[0] = Position.Bias.Forward;
        return i2;
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public int getBoundedPosition(GlyphView glyphView, int i2, float f2, float f3) {
        sync(glyphView);
        TabExpander tabExpander = glyphView.getTabExpander();
        Segment text = glyphView.getText(i2, glyphView.getEndOffset());
        int tabbedTextOffset = Utilities.getTabbedTextOffset(glyphView, text, this.metrics, (int) f2, (int) (f2 + f3), tabExpander, i2, false, getJustificationData(glyphView));
        SegmentCache.releaseSharedSegment(text);
        return i2 + tabbedTextOffset;
    }

    void sync(GlyphView glyphView) {
        Font font = glyphView.getFont();
        if (this.metrics == null || !font.equals(this.metrics.getFont())) {
            Container container = glyphView.getContainer();
            this.metrics = container != null ? container.getFontMetrics(font) : Toolkit.getDefaultToolkit().getFontMetrics(font);
        }
    }

    private int[] getJustificationData(GlyphView glyphView) {
        View parent = glyphView.getParent();
        int[] iArr = null;
        if (parent instanceof ParagraphView.Row) {
            iArr = ((ParagraphView.Row) parent).justificationData;
        }
        return iArr;
    }
}
