package java.awt.font;

import java.text.AttributedCharacterIterator;
import java.text.BreakIterator;

/* loaded from: rt.jar:java/awt/font/LineBreakMeasurer.class */
public final class LineBreakMeasurer {
    private BreakIterator breakIter;
    private int start;
    private int pos;
    private int limit;
    private TextMeasurer measurer;
    private CharArrayIterator charIter;

    public LineBreakMeasurer(AttributedCharacterIterator attributedCharacterIterator, FontRenderContext fontRenderContext) {
        this(attributedCharacterIterator, BreakIterator.getLineInstance(), fontRenderContext);
    }

    public LineBreakMeasurer(AttributedCharacterIterator attributedCharacterIterator, BreakIterator breakIterator, FontRenderContext fontRenderContext) {
        if (attributedCharacterIterator.getEndIndex() - attributedCharacterIterator.getBeginIndex() < 1) {
            throw new IllegalArgumentException("Text must contain at least one character.");
        }
        this.breakIter = breakIterator;
        this.measurer = new TextMeasurer(attributedCharacterIterator, fontRenderContext);
        this.limit = attributedCharacterIterator.getEndIndex();
        int beginIndex = attributedCharacterIterator.getBeginIndex();
        this.start = beginIndex;
        this.pos = beginIndex;
        this.charIter = new CharArrayIterator(this.measurer.getChars(), this.start);
        this.breakIter.setText(this.charIter);
    }

    public int nextOffset(float f2) {
        return nextOffset(f2, this.limit, false);
    }

    public int nextOffset(float f2, int i2, boolean z2) {
        int iPreceding = this.pos;
        if (this.pos < this.limit) {
            if (i2 <= this.pos) {
                throw new IllegalArgumentException("offsetLimit must be after current position");
            }
            int lineBreakIndex = this.measurer.getLineBreakIndex(this.pos, f2);
            if (lineBreakIndex == this.limit) {
                iPreceding = this.limit;
            } else if (Character.isWhitespace(this.measurer.getChars()[lineBreakIndex - this.start])) {
                iPreceding = this.breakIter.following(lineBreakIndex);
            } else {
                int i3 = lineBreakIndex + 1;
                if (i3 == this.limit) {
                    this.breakIter.last();
                    iPreceding = this.breakIter.previous();
                } else {
                    iPreceding = this.breakIter.preceding(i3);
                }
                if (iPreceding <= this.pos) {
                    if (z2) {
                        iPreceding = this.pos;
                    } else {
                        iPreceding = Math.max(this.pos + 1, lineBreakIndex);
                    }
                }
            }
        }
        if (iPreceding > i2) {
            iPreceding = i2;
        }
        return iPreceding;
    }

    public TextLayout nextLayout(float f2) {
        return nextLayout(f2, this.limit, false);
    }

    public TextLayout nextLayout(float f2, int i2, boolean z2) {
        int iNextOffset;
        if (this.pos >= this.limit || (iNextOffset = nextOffset(f2, i2, z2)) == this.pos) {
            return null;
        }
        TextLayout layout = this.measurer.getLayout(this.pos, iNextOffset);
        this.pos = iNextOffset;
        return layout;
    }

    public int getPosition() {
        return this.pos;
    }

    public void setPosition(int i2) {
        if (i2 < this.start || i2 > this.limit) {
            throw new IllegalArgumentException("position is out of range");
        }
        this.pos = i2;
    }

    public void insertChar(AttributedCharacterIterator attributedCharacterIterator, int i2) {
        this.measurer.insertChar(attributedCharacterIterator, i2);
        this.limit = attributedCharacterIterator.getEndIndex();
        int beginIndex = attributedCharacterIterator.getBeginIndex();
        this.start = beginIndex;
        this.pos = beginIndex;
        this.charIter.reset(this.measurer.getChars(), attributedCharacterIterator.getBeginIndex());
        this.breakIter.setText(this.charIter);
    }

    public void deleteChar(AttributedCharacterIterator attributedCharacterIterator, int i2) {
        this.measurer.deleteChar(attributedCharacterIterator, i2);
        this.limit = attributedCharacterIterator.getEndIndex();
        int beginIndex = attributedCharacterIterator.getBeginIndex();
        this.start = beginIndex;
        this.pos = beginIndex;
        this.charIter.reset(this.measurer.getChars(), this.start);
        this.breakIter.setText(this.charIter);
    }
}
