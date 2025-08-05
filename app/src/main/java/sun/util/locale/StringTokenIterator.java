package sun.util.locale;

/* loaded from: rt.jar:sun/util/locale/StringTokenIterator.class */
public class StringTokenIterator {
    private String text;
    private String dlms;
    private char delimiterChar;
    private String token;
    private int start;
    private int end;
    private boolean done;

    public StringTokenIterator(String str, String str2) {
        this.text = str;
        if (str2.length() == 1) {
            this.delimiterChar = str2.charAt(0);
        } else {
            this.dlms = str2;
        }
        setStart(0);
    }

    public String first() {
        setStart(0);
        return this.token;
    }

    public String current() {
        return this.token;
    }

    public int currentStart() {
        return this.start;
    }

    public int currentEnd() {
        return this.end;
    }

    public boolean isDone() {
        return this.done;
    }

    public String next() {
        if (hasNext()) {
            this.start = this.end + 1;
            this.end = nextDelimiter(this.start);
            this.token = this.text.substring(this.start, this.end);
        } else {
            this.start = this.end;
            this.token = null;
            this.done = true;
        }
        return this.token;
    }

    public boolean hasNext() {
        return this.end < this.text.length();
    }

    public StringTokenIterator setStart(int i2) {
        if (i2 > this.text.length()) {
            throw new IndexOutOfBoundsException();
        }
        this.start = i2;
        this.end = nextDelimiter(this.start);
        this.token = this.text.substring(this.start, this.end);
        this.done = false;
        return this;
    }

    public StringTokenIterator setText(String str) {
        this.text = str;
        setStart(0);
        return this;
    }

    private int nextDelimiter(int i2) {
        int length = this.text.length();
        if (this.dlms == null) {
            for (int i3 = i2; i3 < length; i3++) {
                if (this.text.charAt(i3) == this.delimiterChar) {
                    return i3;
                }
            }
        } else {
            int length2 = this.dlms.length();
            for (int i4 = i2; i4 < length; i4++) {
                char cCharAt = this.text.charAt(i4);
                for (int i5 = 0; i5 < length2; i5++) {
                    if (cCharAt == this.dlms.charAt(i5)) {
                        return i4;
                    }
                }
            }
        }
        return length;
    }
}
