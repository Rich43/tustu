package javax.swing.text;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import javax.swing.text.GlyphView;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/GlyphPainter2.class */
class GlyphPainter2 extends GlyphView.GlyphPainter {
    TextLayout layout;

    public GlyphPainter2(TextLayout textLayout) {
        this.layout = textLayout;
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public GlyphView.GlyphPainter getPainter(GlyphView glyphView, int i2, int i3) {
        return null;
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public float getSpan(GlyphView glyphView, int i2, int i3, TabExpander tabExpander, float f2) {
        if (i2 == glyphView.getStartOffset() && i3 == glyphView.getEndOffset()) {
            return this.layout.getAdvance();
        }
        int startOffset = glyphView.getStartOffset();
        int i4 = i2 - startOffset;
        int i5 = i3 - startOffset;
        TextHitInfo textHitInfoAfterOffset = TextHitInfo.afterOffset(i4);
        TextHitInfo textHitInfoBeforeOffset = TextHitInfo.beforeOffset(i5);
        float f3 = this.layout.getCaretInfo(textHitInfoAfterOffset)[0];
        float f4 = this.layout.getCaretInfo(textHitInfoBeforeOffset)[0];
        return f4 > f3 ? f4 - f3 : f3 - f4;
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public float getHeight(GlyphView glyphView) {
        return this.layout.getAscent() + this.layout.getDescent() + this.layout.getLeading();
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public float getAscent(GlyphView glyphView) {
        return this.layout.getAscent();
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public float getDescent(GlyphView glyphView) {
        return this.layout.getDescent();
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public void paint(GlyphView glyphView, Graphics graphics, Shape shape, int i2, int i3) {
        if (graphics instanceof Graphics2D) {
            Rectangle2D bounds2D = shape.getBounds2D();
            Graphics2D graphics2D = (Graphics2D) graphics;
            float y2 = ((float) bounds2D.getY()) + this.layout.getAscent() + this.layout.getLeading();
            float x2 = (float) bounds2D.getX();
            if (i2 > glyphView.getStartOffset() || i3 < glyphView.getEndOffset()) {
                try {
                    Shape shapeModelToView = glyphView.modelToView(i2, Position.Bias.Forward, i3, Position.Bias.Backward, shape);
                    Shape clip = graphics.getClip();
                    graphics2D.clip(shapeModelToView);
                    this.layout.draw(graphics2D, x2, y2);
                    graphics.setClip(clip);
                    return;
                } catch (BadLocationException e2) {
                    return;
                }
            }
            this.layout.draw(graphics2D, x2, y2);
        }
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public Shape modelToView(GlyphView glyphView, int i2, Position.Bias bias, Shape shape) throws BadLocationException {
        int startOffset = i2 - glyphView.getStartOffset();
        Rectangle2D bounds2D = shape.getBounds2D();
        bounds2D.setRect(bounds2D.getX() + this.layout.getCaretInfo(bias == Position.Bias.Forward ? TextHitInfo.afterOffset(startOffset) : TextHitInfo.beforeOffset(startOffset))[0], bounds2D.getY(), 1.0d, bounds2D.getHeight());
        return bounds2D;
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public int viewToModel(GlyphView glyphView, float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        TextHitInfo textHitInfoHitTestChar = this.layout.hitTestChar(f2 - ((float) (shape instanceof Rectangle2D ? (Rectangle2D) shape : shape.getBounds2D()).getX()), 0.0f);
        int insertionIndex = textHitInfoHitTestChar.getInsertionIndex();
        if (insertionIndex == glyphView.getEndOffset()) {
            insertionIndex--;
        }
        biasArr[0] = textHitInfoHitTestChar.isLeadingEdge() ? Position.Bias.Forward : Position.Bias.Backward;
        return insertionIndex + glyphView.getStartOffset();
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public int getBoundedPosition(GlyphView glyphView, int i2, float f2, float f3) {
        TextHitInfo textHitInfoHitTestChar;
        if (f3 < 0.0f) {
            throw new IllegalArgumentException("Length must be >= 0.");
        }
        if (this.layout.isLeftToRight()) {
            textHitInfoHitTestChar = this.layout.hitTestChar(f3, 0.0f);
        } else {
            textHitInfoHitTestChar = this.layout.hitTestChar(this.layout.getAdvance() - f3, 0.0f);
        }
        return glyphView.getStartOffset() + textHitInfoHitTestChar.getCharIndex();
    }

    @Override // javax.swing.text.GlyphView.GlyphPainter
    public int getNextVisualPositionFrom(GlyphView glyphView, int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
        TextHitInfo textHitInfoBeforeOffset;
        TextHitInfo textHitInfoBeforeOffset2;
        Document document = glyphView.getDocument();
        int startOffset = glyphView.getStartOffset();
        int endOffset = glyphView.getEndOffset();
        switch (i3) {
            case 1:
            case 5:
                return i2;
            case 2:
            case 4:
            case 6:
            default:
                throw new IllegalArgumentException("Bad direction: " + i3);
            case 3:
                boolean zIsLeftToRight = AbstractDocument.isLeftToRight(document, startOffset, endOffset);
                if (startOffset == document.getLength()) {
                    if (i2 == -1) {
                        biasArr[0] = Position.Bias.Forward;
                        return startOffset;
                    }
                    return -1;
                }
                if (i2 == -1) {
                    if (zIsLeftToRight) {
                        biasArr[0] = Position.Bias.Forward;
                        return startOffset;
                    }
                    Segment text = glyphView.getText(endOffset - 1, endOffset);
                    char c2 = text.array[text.offset];
                    SegmentCache.releaseSharedSegment(text);
                    if (c2 == '\n') {
                        biasArr[0] = Position.Bias.Forward;
                        return endOffset - 1;
                    }
                    biasArr[0] = Position.Bias.Backward;
                    return endOffset;
                }
                if (bias == Position.Bias.Forward) {
                    textHitInfoBeforeOffset2 = TextHitInfo.afterOffset(i2 - startOffset);
                } else {
                    textHitInfoBeforeOffset2 = TextHitInfo.beforeOffset(i2 - startOffset);
                }
                TextHitInfo nextRightHit = this.layout.getNextRightHit(textHitInfoBeforeOffset2);
                if (nextRightHit == null) {
                    return -1;
                }
                if (zIsLeftToRight != this.layout.isLeftToRight()) {
                    nextRightHit = this.layout.getVisualOtherHit(nextRightHit);
                }
                int insertionIndex = nextRightHit.getInsertionIndex() + startOffset;
                if (insertionIndex == endOffset) {
                    Segment text2 = glyphView.getText(endOffset - 1, endOffset);
                    char c3 = text2.array[text2.offset];
                    SegmentCache.releaseSharedSegment(text2);
                    if (c3 == '\n') {
                        return -1;
                    }
                    biasArr[0] = Position.Bias.Backward;
                } else {
                    biasArr[0] = Position.Bias.Forward;
                }
                return insertionIndex;
            case 7:
                boolean zIsLeftToRight2 = AbstractDocument.isLeftToRight(document, startOffset, endOffset);
                if (startOffset == document.getLength()) {
                    if (i2 == -1) {
                        biasArr[0] = Position.Bias.Forward;
                        return startOffset;
                    }
                    return -1;
                }
                if (i2 == -1) {
                    if (zIsLeftToRight2) {
                        Segment text3 = glyphView.getText(endOffset - 1, endOffset);
                        char c4 = text3.array[text3.offset];
                        SegmentCache.releaseSharedSegment(text3);
                        if (c4 == '\n' || Character.isSpaceChar(c4)) {
                            biasArr[0] = Position.Bias.Forward;
                            return endOffset - 1;
                        }
                        biasArr[0] = Position.Bias.Backward;
                        return endOffset;
                    }
                    biasArr[0] = Position.Bias.Forward;
                    return startOffset;
                }
                if (bias == Position.Bias.Forward) {
                    textHitInfoBeforeOffset = TextHitInfo.afterOffset(i2 - startOffset);
                } else {
                    textHitInfoBeforeOffset = TextHitInfo.beforeOffset(i2 - startOffset);
                }
                TextHitInfo nextLeftHit = this.layout.getNextLeftHit(textHitInfoBeforeOffset);
                if (nextLeftHit == null) {
                    return -1;
                }
                if (zIsLeftToRight2 != this.layout.isLeftToRight()) {
                    nextLeftHit = this.layout.getVisualOtherHit(nextLeftHit);
                }
                int insertionIndex2 = nextLeftHit.getInsertionIndex() + startOffset;
                if (insertionIndex2 == endOffset) {
                    Segment text4 = glyphView.getText(endOffset - 1, endOffset);
                    char c5 = text4.array[text4.offset];
                    SegmentCache.releaseSharedSegment(text4);
                    if (c5 == '\n') {
                        return -1;
                    }
                    biasArr[0] = Position.Bias.Backward;
                } else {
                    biasArr[0] = Position.Bias.Forward;
                }
                return insertionIndex2;
        }
    }
}
