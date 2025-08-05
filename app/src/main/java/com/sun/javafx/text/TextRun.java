package com.sun.javafx.text;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextSpan;

/* loaded from: jfxrt.jar:com/sun/javafx/text/TextRun.class */
public class TextRun implements GlyphList {
    int glyphCount;
    int[] gids;
    float[] positions;
    int[] charIndices;
    int start;
    int length;
    byte level;
    int script;
    TextSpan span;
    TextLine line;
    Point2D location;
    private float ascent;
    private float descent;
    private float leading;
    int slot;
    static final int FLAGS_TAB = 1;
    static final int FLAGS_LINEBREAK = 2;
    static final int FLAGS_SOFTBREAK = 4;
    static final int FLAGS_NO_LINK_BEFORE = 8;
    static final int FLAGS_NO_LINK_AFTER = 16;
    static final int FLAGS_COMPLEX = 32;
    static final int FLAGS_EMBEDDED = 64;
    static final int FLAGS_SPLIT = 128;
    static final int FLAGS_SPLIT_LAST = 256;
    static final int FLAGS_LEFT_BEARING = 512;
    static final int FLAGS_RIGHT_BEARING = 1024;
    static final int FLAGS_CANONICAL = 2048;
    static final int FLAGS_COMPACT = 4096;
    float width = -1.0f;
    int flags = 0;
    float cacheWidth = 0.0f;
    int cacheIndex = 0;

    public TextRun(int start, int length, byte level, boolean complex, int script, TextSpan span, int slot, boolean canonical) {
        this.slot = 0;
        this.start = start;
        this.length = length;
        this.level = level;
        this.script = script;
        this.span = span;
        this.slot = slot;
        if (complex) {
            this.flags |= 32;
        }
        if (canonical) {
            this.flags |= 2048;
        }
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.start + this.length;
    }

    public int getLength() {
        return this.length;
    }

    public byte getLevel() {
        return this.level;
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public RectBounds getLineBounds() {
        return this.line.getBounds();
    }

    public void setLine(TextLine line) {
        this.line = line;
    }

    public int getScript() {
        return this.script;
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public TextSpan getTextSpan() {
        return this.span;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isLinebreak() {
        return (this.flags & 2) != 0;
    }

    public boolean isCanonical() {
        return (this.flags & 2048) != 0;
    }

    public boolean isSoftbreak() {
        return (this.flags & 4) != 0;
    }

    public boolean isBreak() {
        return (this.flags & 6) != 0;
    }

    public boolean isTab() {
        return (this.flags & 1) != 0;
    }

    public boolean isEmbedded() {
        return (this.flags & 64) != 0;
    }

    public boolean isNoLinkBefore() {
        return (this.flags & 8) != 0;
    }

    public boolean isNoLinkAfter() {
        return (this.flags & 16) != 0;
    }

    public boolean isSplit() {
        return (this.flags & 128) != 0;
    }

    public boolean isSplitLast() {
        return (this.flags & 256) != 0;
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public boolean isComplex() {
        return (this.flags & 32) != 0;
    }

    public boolean isLeftBearing() {
        return (this.flags & 512) != 0;
    }

    public boolean isRightBearing() {
        return (this.flags & 1024) != 0;
    }

    public boolean isLeftToRight() {
        return (this.level & 1) == 0;
    }

    public void setComplex(boolean complex) {
        if (complex) {
            this.flags |= 32;
        } else {
            this.flags &= -33;
        }
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public float getWidth() {
        if (this.width != -1.0f) {
            return this.width;
        }
        if (this.positions != null) {
            if ((this.flags & 4096) != 0) {
                this.width = 0.0f;
                for (int i2 = 0; i2 < this.glyphCount; i2++) {
                    this.width += this.positions[this.start + i2];
                }
                return this.width;
            }
            return this.positions[this.glyphCount << 1];
        }
        return 0.0f;
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public float getHeight() {
        return (-this.ascent) + this.descent + this.leading;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setMetrics(float ascent, float descent, float leading) {
        this.ascent = ascent;
        this.descent = descent;
        this.leading = leading;
    }

    public float getAscent() {
        return this.ascent;
    }

    public float getDescent() {
        return this.descent;
    }

    public float getLeading() {
        return this.leading;
    }

    public void setLocation(float x2, float y2) {
        this.location = new Point2D(x2, y2);
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public Point2D getLocation() {
        return this.location;
    }

    public void setTab() {
        this.flags |= 1;
    }

    public void setEmbedded(RectBounds bounds, int length) {
        this.width = bounds.getWidth() * length;
        this.ascent = bounds.getMinY();
        this.descent = bounds.getHeight() + this.ascent;
        this.length = length;
        this.flags |= 64;
    }

    public void setLinebreak() {
        this.flags |= 2;
    }

    public void setSoftbreak() {
        this.flags |= 4;
    }

    public void setLeftBearing() {
        this.flags |= 512;
    }

    public void setRightBearing() {
        this.flags |= 1024;
    }

    public int getWrapIndex(float width) {
        if (this.glyphCount == 0) {
            return 0;
        }
        if (isLeftToRight()) {
            int gi = 0;
            if ((this.flags & 4096) != 0) {
                float right = 0.0f;
                while (gi < this.glyphCount) {
                    right += this.positions[this.start + gi];
                    if (right > width) {
                        return getCharOffset(gi);
                    }
                    gi++;
                }
                return 0;
            }
            while (gi < this.glyphCount) {
                if (this.positions[(gi + 1) << 1] > width) {
                    return getCharOffset(gi);
                }
                gi++;
            }
            return 0;
        }
        int gi2 = 0;
        float runWidth = this.positions[this.glyphCount << 1];
        while (runWidth > width) {
            float glyphWidth = this.positions[(gi2 + 1) << 1] - this.positions[gi2 << 1];
            if (runWidth - glyphWidth <= width) {
                return getCharOffset(gi2);
            }
            runWidth -= glyphWidth;
            gi2++;
        }
        return 0;
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public int getGlyphCount() {
        return this.glyphCount;
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public int getGlyphCode(int glyphIndex) {
        if (0 <= glyphIndex && glyphIndex < this.glyphCount) {
            if ((this.flags & 4096) != 0) {
                return this.gids[this.start + glyphIndex];
            }
            return this.gids[glyphIndex];
        }
        return 65535;
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public float getPosX(int glyphIndex) {
        if (0 <= glyphIndex && glyphIndex <= this.glyphCount) {
            if ((this.flags & 4096) != 0) {
                if (this.cacheIndex == glyphIndex) {
                    return this.cacheWidth;
                }
                float x2 = 0.0f;
                if (this.cacheIndex + 1 == glyphIndex) {
                    x2 = this.cacheWidth + this.positions[(this.start + glyphIndex) - 1];
                } else {
                    for (int i2 = 0; i2 < glyphIndex; i2++) {
                        x2 += this.positions[this.start + i2];
                    }
                }
                this.cacheIndex = glyphIndex;
                this.cacheWidth = x2;
                return x2;
            }
            return this.positions[glyphIndex << 1];
        }
        if (glyphIndex == 0) {
            return 0.0f;
        }
        return getWidth();
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public float getPosY(int glyphIndex) {
        if ((this.flags & 4096) == 0 && 0 <= glyphIndex && glyphIndex <= this.glyphCount) {
            return this.positions[(glyphIndex << 1) + 1];
        }
        return 0.0f;
    }

    public float getAdvance(int glyphIndex) {
        if ((this.flags & 4096) != 0) {
            return this.positions[this.start + glyphIndex];
        }
        return this.positions[(glyphIndex + 1) << 1] - this.positions[glyphIndex << 1];
    }

    public void shape(int count, int[] glyphs, float[] pos, int[] indices) {
        this.glyphCount = count;
        this.gids = glyphs;
        this.positions = pos;
        this.charIndices = indices;
    }

    public void shape(int count, int[] glyphs, float[] pos) {
        this.glyphCount = count;
        this.gids = glyphs;
        this.positions = pos;
        this.charIndices = null;
        this.flags |= 4096;
    }

    public float getXAtOffset(int offset, boolean leading) {
        boolean ltr = isLeftToRight();
        if (offset == this.length) {
            if (ltr) {
                return getWidth();
            }
            return 0.0f;
        }
        if (this.glyphCount > 0) {
            int glyphIndex = getGlyphIndex(offset);
            if (ltr) {
                return getPosX(glyphIndex + (leading ? 0 : 1));
            }
            return getPosX(glyphIndex + (leading ? 1 : 0));
        }
        if (isTab()) {
            if (ltr) {
                if (leading) {
                    return 0.0f;
                }
                return getWidth();
            }
            if (leading) {
                return getWidth();
            }
            return 0.0f;
        }
        return 0.0f;
    }

    public int getGlyphAtX(float x2, int[] trailing) {
        boolean ltr = isLeftToRight();
        float runX = 0.0f;
        for (int i2 = 0; i2 < this.glyphCount; i2++) {
            float advance = getAdvance(i2);
            if (runX + advance > x2) {
                if (trailing != null) {
                    if (x2 - runX > advance / 2.0f) {
                        trailing[0] = ltr ? 1 : 0;
                    } else {
                        trailing[0] = ltr ? 0 : 1;
                    }
                }
                return i2;
            }
            runX += advance;
        }
        if (trailing != null) {
            trailing[0] = ltr ? 1 : 0;
        }
        return Math.max(0, this.glyphCount - 1);
    }

    public int getOffsetAtX(float x2, int[] trailing) {
        if (this.glyphCount > 0) {
            int glyphIndex = getGlyphAtX(x2, trailing);
            return getCharOffset(glyphIndex);
        }
        if (this.width != -1.0f && this.length > 0 && trailing != null && x2 > this.width / 2.0f) {
            trailing[0] = 1;
            return 0;
        }
        return 0;
    }

    private void reset() {
        this.positions = null;
        this.charIndices = null;
        this.gids = null;
        this.width = -1.0f;
        this.leading = 0.0f;
        this.descent = 0.0f;
        this.ascent = 0.0f;
        this.glyphCount = 0;
    }

    public TextRun split(int offset) {
        int newLength = this.length - offset;
        this.length = offset;
        boolean complex = isComplex();
        TextRun newRun = new TextRun(this.start + this.length, newLength, this.level, complex, this.script, this.span, this.slot, isCanonical());
        this.flags |= 16;
        newRun.flags |= 8;
        this.flags |= 128;
        this.flags &= -257;
        newRun.flags |= 256;
        newRun.setMetrics(this.ascent, this.descent, this.leading);
        if (!complex) {
            this.glyphCount = this.length;
            if ((this.flags & 4096) != 0) {
                newRun.shape(newLength, this.gids, this.positions);
                if (this.width != -1.0f) {
                    if (newLength > this.length) {
                        float oldWidth = this.width;
                        this.width = -1.0f;
                        newRun.setWidth(oldWidth - getWidth());
                    } else {
                        this.width -= newRun.getWidth();
                    }
                }
            } else {
                int[] newGlyphs = new int[newLength];
                float[] newPos = new float[(newLength + 1) << 1];
                System.arraycopy(this.gids, offset, newGlyphs, 0, newLength);
                float width = getWidth();
                int delta = offset << 1;
                for (int i2 = 2; i2 < newPos.length; i2 += 2) {
                    newPos[i2] = this.positions[i2 + delta] - width;
                }
                newRun.shape(newLength, newGlyphs, newPos, null);
            }
        } else {
            reset();
        }
        return newRun;
    }

    public void merge(TextRun run) {
        if (run != null) {
            this.length += run.length;
            this.glyphCount += run.glyphCount;
            if (this.width != -1.0f && run.width != -1.0f) {
                this.width += run.width;
            } else {
                this.width = -1.0f;
            }
        }
        this.flags &= -129;
        this.flags &= -257;
    }

    public TextRun unwrap() {
        TextRun newRun = new TextRun(this.start, this.length, this.level, isComplex(), this.script, this.span, this.slot, isCanonical());
        newRun.shape(this.glyphCount, this.gids, this.positions);
        newRun.setWidth(this.width);
        newRun.setMetrics(this.ascent, this.descent, this.leading);
        newRun.flags = this.flags & (28 ^ (-1));
        return newRun;
    }

    public void justify(int offset, float width) {
        if (this.positions != null) {
            int glyphIndex = getGlyphIndex(offset);
            if (glyphIndex != -1) {
                for (int i2 = glyphIndex + 1; i2 <= this.glyphCount; i2++) {
                    float[] fArr = this.positions;
                    int i3 = i2 << 1;
                    fArr[i3] = fArr[i3] + width;
                }
                this.width = -1.0f;
            }
            setComplex(true);
        }
    }

    public int getGlyphIndex(int charOffset) {
        if (this.charIndices == null) {
            return charOffset;
        }
        for (int i2 = 0; i2 < this.charIndices.length && i2 < this.glyphCount; i2++) {
            if (this.charIndices[i2] == charOffset) {
                return i2;
            }
        }
        if (isLeftToRight()) {
            if (charOffset > 0) {
                return getGlyphIndex(charOffset - 1);
            }
            return 0;
        }
        if (charOffset + 1 < this.length) {
            return getGlyphIndex(charOffset + 1);
        }
        return 0;
    }

    @Override // com.sun.javafx.scene.text.GlyphList
    public int getCharOffset(int glyphIndex) {
        return this.charIndices == null ? glyphIndex : this.charIndices[glyphIndex];
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TextRun start=");
        buffer.append(this.start);
        buffer.append(", length=");
        buffer.append(this.length);
        buffer.append(", script=");
        buffer.append(this.script);
        buffer.append(", linebreak=");
        buffer.append(isLinebreak());
        buffer.append(", softbreak=");
        buffer.append(isSoftbreak());
        buffer.append(", complex=");
        buffer.append(isComplex());
        buffer.append(", tab=");
        buffer.append(isTab());
        buffer.append(", compact=");
        buffer.append((this.flags & 4096) != 0);
        buffer.append(", ltr=");
        buffer.append(isLeftToRight());
        buffer.append(", split=");
        buffer.append(isSplit());
        return buffer.toString();
    }
}
