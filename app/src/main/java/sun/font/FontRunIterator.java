package sun.font;

/* loaded from: rt.jar:sun/font/FontRunIterator.class */
public final class FontRunIterator {
    CompositeFont font;
    char[] text;
    int start;
    int limit;
    CompositeGlyphMapper mapper;
    int slot = -1;
    int pos;
    static final int SURROGATE_START = 65536;
    static final int LEAD_START = 55296;
    static final int LEAD_LIMIT = 56320;
    static final int TAIL_START = 56320;
    static final int TAIL_LIMIT = 57344;
    static final int LEAD_SURROGATE_SHIFT = 10;
    static final int SURROGATE_OFFSET = -56613888;
    static final int DONE = -1;

    public void init(CompositeFont compositeFont, char[] cArr, int i2, int i3) {
        if (compositeFont == null || cArr == null || i2 < 0 || i3 < i2 || i3 > cArr.length) {
            throw new IllegalArgumentException();
        }
        this.font = compositeFont;
        this.text = cArr;
        this.start = i2;
        this.limit = i3;
        this.mapper = (CompositeGlyphMapper) compositeFont.getMapper();
        this.slot = -1;
        this.pos = i2;
    }

    public PhysicalFont getFont() {
        if (this.slot == -1) {
            return null;
        }
        return this.font.getSlotFont(this.slot);
    }

    public int getGlyphMask() {
        return this.slot << 24;
    }

    public int getPos() {
        return this.pos;
    }

    public boolean next(int i2, int i3) {
        int iNextCodePoint;
        if (this.pos == i3) {
            return false;
        }
        int iCharToGlyph = this.mapper.charToGlyph(nextCodePoint(i3)) & (-16777216);
        this.slot = iCharToGlyph >>> 24;
        do {
            iNextCodePoint = nextCodePoint(i3);
            if (iNextCodePoint == -1) {
                break;
            }
        } while ((this.mapper.charToGlyph(iNextCodePoint) & (-16777216)) == iCharToGlyph);
        pushback(iNextCodePoint);
        return true;
    }

    public boolean next() {
        return next(0, this.limit);
    }

    final int nextCodePoint() {
        return nextCodePoint(this.limit);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19, types: [int] */
    final int nextCodePoint(int i2) {
        char c2;
        if (this.pos >= i2) {
            return -1;
        }
        char[] cArr = this.text;
        int i3 = this.pos;
        this.pos = i3 + 1;
        char c3 = cArr[i3];
        if (c3 >= 55296 && c3 < 56320 && this.pos < i2 && (c2 = this.text[this.pos]) >= 56320 && c2 < TAIL_LIMIT) {
            this.pos++;
            c3 = (c3 << '\n') + c2 + SURROGATE_OFFSET;
        }
        return c3;
    }

    final void pushback(int i2) {
        if (i2 >= 0) {
            if (i2 >= 65536) {
                this.pos -= 2;
            } else {
                this.pos--;
            }
        }
    }
}
