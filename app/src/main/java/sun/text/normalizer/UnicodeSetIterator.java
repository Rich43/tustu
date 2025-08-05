package sun.text.normalizer;

import java.util.Iterator;

/* loaded from: rt.jar:sun/text/normalizer/UnicodeSetIterator.class */
public class UnicodeSetIterator {
    public static int IS_STRING = -1;
    public int codepoint;
    public int codepointEnd;
    public String string;
    private UnicodeSet set;
    protected int endElement;
    protected int nextElement;
    private int endRange = 0;
    private int range = 0;
    private Iterator<String> stringIterator = null;

    public UnicodeSetIterator(UnicodeSet unicodeSet) {
        reset(unicodeSet);
    }

    public boolean nextRange() {
        if (this.nextElement <= this.endElement) {
            this.codepointEnd = this.endElement;
            this.codepoint = this.nextElement;
            this.nextElement = this.endElement + 1;
            return true;
        }
        if (this.range < this.endRange) {
            int i2 = this.range + 1;
            this.range = i2;
            loadRange(i2);
            this.codepointEnd = this.endElement;
            this.codepoint = this.nextElement;
            this.nextElement = this.endElement + 1;
            return true;
        }
        if (this.stringIterator == null) {
            return false;
        }
        this.codepoint = IS_STRING;
        this.string = this.stringIterator.next();
        if (!this.stringIterator.hasNext()) {
            this.stringIterator = null;
            return true;
        }
        return true;
    }

    public void reset(UnicodeSet unicodeSet) {
        this.set = unicodeSet;
        reset();
    }

    public void reset() {
        this.endRange = this.set.getRangeCount() - 1;
        this.range = 0;
        this.endElement = -1;
        this.nextElement = 0;
        if (this.endRange >= 0) {
            loadRange(this.range);
        }
        this.stringIterator = null;
        if (this.set.strings != null) {
            this.stringIterator = this.set.strings.iterator();
            if (!this.stringIterator.hasNext()) {
                this.stringIterator = null;
            }
        }
    }

    protected void loadRange(int i2) {
        this.nextElement = this.set.getRangeStart(i2);
        this.endElement = this.set.getRangeEnd(i2);
    }
}
