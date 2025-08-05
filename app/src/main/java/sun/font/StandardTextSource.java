package sun.font;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:sun/font/StandardTextSource.class */
final class StandardTextSource extends TextSource {
    private final char[] chars;
    private final int start;
    private final int len;
    private final int cstart;
    private final int clen;
    private final int level;
    private final int flags;
    private final Font font;
    private final FontRenderContext frc;
    private final CoreMetrics cm;

    StandardTextSource(char[] cArr, int i2, int i3, int i4, int i5, int i6, int i7, Font font, FontRenderContext fontRenderContext, CoreMetrics coreMetrics) {
        if (cArr == null) {
            throw new IllegalArgumentException("bad chars: null");
        }
        if (i4 < 0) {
            throw new IllegalArgumentException("bad cstart: " + i4);
        }
        if (i2 < i4) {
            throw new IllegalArgumentException("bad start: " + i2 + " for cstart: " + i4);
        }
        if (i5 < 0) {
            throw new IllegalArgumentException("bad clen: " + i5);
        }
        if (i4 + i5 > cArr.length) {
            throw new IllegalArgumentException("bad clen: " + i5 + " cstart: " + i4 + " for array len: " + cArr.length);
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("bad len: " + i3);
        }
        if (i2 + i3 > i4 + i5) {
            throw new IllegalArgumentException("bad len: " + i3 + " start: " + i2 + " for cstart: " + i4 + " clen: " + i5);
        }
        if (font == null) {
            throw new IllegalArgumentException("bad font: null");
        }
        if (fontRenderContext == null) {
            throw new IllegalArgumentException("bad frc: null");
        }
        this.chars = cArr;
        this.start = i2;
        this.len = i3;
        this.cstart = i4;
        this.clen = i5;
        this.level = i6;
        this.flags = i7;
        this.font = font;
        this.frc = fontRenderContext;
        if (coreMetrics != null) {
            this.cm = coreMetrics;
        } else {
            this.cm = ((FontLineMetrics) font.getLineMetrics(cArr, i4, i5, fontRenderContext)).cm;
        }
    }

    @Override // sun.font.TextSource
    public char[] getChars() {
        return this.chars;
    }

    @Override // sun.font.TextSource
    public int getStart() {
        return this.start;
    }

    @Override // sun.font.TextSource
    public int getLength() {
        return this.len;
    }

    @Override // sun.font.TextSource
    public int getContextStart() {
        return this.cstart;
    }

    @Override // sun.font.TextSource
    public int getContextLength() {
        return this.clen;
    }

    @Override // sun.font.TextSource
    public int getLayoutFlags() {
        return this.flags;
    }

    @Override // sun.font.TextSource
    public int getBidiLevel() {
        return this.level;
    }

    @Override // sun.font.TextSource
    public Font getFont() {
        return this.font;
    }

    @Override // sun.font.TextSource
    public FontRenderContext getFRC() {
        return this.frc;
    }

    @Override // sun.font.TextSource
    public CoreMetrics getCoreMetrics() {
        return this.cm;
    }

    @Override // sun.font.TextSource
    public TextSource getSubSource(int i2, int i3, int i4) {
        if (i2 < 0 || i3 < 0 || i2 + i3 > this.len) {
            throw new IllegalArgumentException("bad start (" + i2 + ") or length (" + i3 + ")");
        }
        int i5 = this.level;
        if (i4 != 2) {
            boolean z2 = (this.flags & 8) == 0;
            if ((i4 != 0 || !z2) && (i4 != 1 || z2)) {
                throw new IllegalArgumentException("direction flag is invalid");
            }
            i5 = z2 ? 0 : 1;
        }
        return new StandardTextSource(this.chars, this.start + i2, i3, this.cstart, this.clen, i5, this.flags, this.font, this.frc, this.cm);
    }

    public String toString() {
        return toString(true);
    }

    @Override // sun.font.TextSource
    public String toString(boolean z2) {
        int i2;
        int i3;
        StringBuffer stringBuffer = new StringBuffer(super.toString());
        stringBuffer.append("[start:");
        stringBuffer.append(this.start);
        stringBuffer.append(", len:");
        stringBuffer.append(this.len);
        stringBuffer.append(", cstart:");
        stringBuffer.append(this.cstart);
        stringBuffer.append(", clen:");
        stringBuffer.append(this.clen);
        stringBuffer.append(", chars:\"");
        if (z2) {
            i2 = this.cstart;
            i3 = this.cstart + this.clen;
        } else {
            i2 = this.start;
            i3 = this.start + this.len;
        }
        for (int i4 = i2; i4 < i3; i4++) {
            if (i4 > i2) {
                stringBuffer.append(" ");
            }
            stringBuffer.append(Integer.toHexString(this.chars[i4]));
        }
        stringBuffer.append(PdfOps.DOUBLE_QUOTE__TOKEN);
        stringBuffer.append(", level:");
        stringBuffer.append(this.level);
        stringBuffer.append(", flags:");
        stringBuffer.append(this.flags);
        stringBuffer.append(", font:");
        stringBuffer.append((Object) this.font);
        stringBuffer.append(", frc:");
        stringBuffer.append((Object) this.frc);
        stringBuffer.append(", cm:");
        stringBuffer.append((Object) this.cm);
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
