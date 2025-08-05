package java.util;

/* loaded from: rt.jar:java/util/StringTokenizer.class */
public class StringTokenizer implements Enumeration<Object> {
    private int currentPosition;
    private int newPosition;
    private int maxPosition;
    private String str;
    private String delimiters;
    private boolean retDelims;
    private boolean delimsChanged;
    private int maxDelimCodePoint;
    private boolean hasSurrogates;
    private int[] delimiterCodePoints;

    private void setMaxDelimCodePoint() {
        if (this.delimiters == null) {
            this.maxDelimCodePoint = 0;
            return;
        }
        int i2 = 0;
        int i3 = 0;
        int iCharCount = 0;
        while (true) {
            int i4 = iCharCount;
            if (i4 >= this.delimiters.length()) {
                break;
            }
            int iCharAt = this.delimiters.charAt(i4);
            if (iCharAt >= 55296 && iCharAt <= 57343) {
                iCharAt = this.delimiters.codePointAt(i4);
                this.hasSurrogates = true;
            }
            if (i2 < iCharAt) {
                i2 = iCharAt;
            }
            i3++;
            iCharCount = i4 + Character.charCount(iCharAt);
        }
        this.maxDelimCodePoint = i2;
        if (this.hasSurrogates) {
            this.delimiterCodePoints = new int[i3];
            int i5 = 0;
            int iCharCount2 = 0;
            while (true) {
                int i6 = iCharCount2;
                if (i5 < i3) {
                    int iCodePointAt = this.delimiters.codePointAt(i6);
                    this.delimiterCodePoints[i5] = iCodePointAt;
                    i5++;
                    iCharCount2 = i6 + Character.charCount(iCodePointAt);
                } else {
                    return;
                }
            }
        }
    }

    public StringTokenizer(String str, String str2, boolean z2) {
        this.hasSurrogates = false;
        this.currentPosition = 0;
        this.newPosition = -1;
        this.delimsChanged = false;
        this.str = str;
        this.maxPosition = str.length();
        this.delimiters = str2;
        this.retDelims = z2;
        setMaxDelimCodePoint();
    }

    public StringTokenizer(String str, String str2) {
        this(str, str2, false);
    }

    public StringTokenizer(String str) {
        this(str, " \t\n\r\f", false);
    }

    private int skipDelimiters(int i2) {
        if (this.delimiters == null) {
            throw new NullPointerException();
        }
        int iCharCount = i2;
        while (!this.retDelims && iCharCount < this.maxPosition) {
            if (!this.hasSurrogates) {
                char cCharAt = this.str.charAt(iCharCount);
                if (cCharAt > this.maxDelimCodePoint || this.delimiters.indexOf(cCharAt) < 0) {
                    break;
                }
                iCharCount++;
            } else {
                int iCodePointAt = this.str.codePointAt(iCharCount);
                if (iCodePointAt > this.maxDelimCodePoint || !isDelimiter(iCodePointAt)) {
                    break;
                }
                iCharCount += Character.charCount(iCodePointAt);
            }
        }
        return iCharCount;
    }

    private int scanToken(int i2) {
        int iCharCount = i2;
        while (iCharCount < this.maxPosition) {
            if (!this.hasSurrogates) {
                char cCharAt = this.str.charAt(iCharCount);
                if (cCharAt <= this.maxDelimCodePoint && this.delimiters.indexOf(cCharAt) >= 0) {
                    break;
                }
                iCharCount++;
            } else {
                int iCodePointAt = this.str.codePointAt(iCharCount);
                if (iCodePointAt <= this.maxDelimCodePoint && isDelimiter(iCodePointAt)) {
                    break;
                }
                iCharCount += Character.charCount(iCodePointAt);
            }
        }
        if (this.retDelims && i2 == iCharCount) {
            if (!this.hasSurrogates) {
                char cCharAt2 = this.str.charAt(iCharCount);
                if (cCharAt2 <= this.maxDelimCodePoint && this.delimiters.indexOf(cCharAt2) >= 0) {
                    iCharCount++;
                }
            } else {
                int iCodePointAt2 = this.str.codePointAt(iCharCount);
                if (iCodePointAt2 <= this.maxDelimCodePoint && isDelimiter(iCodePointAt2)) {
                    iCharCount += Character.charCount(iCodePointAt2);
                }
            }
        }
        return iCharCount;
    }

    private boolean isDelimiter(int i2) {
        for (int i3 = 0; i3 < this.delimiterCodePoints.length; i3++) {
            if (this.delimiterCodePoints[i3] == i2) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMoreTokens() {
        this.newPosition = skipDelimiters(this.currentPosition);
        return this.newPosition < this.maxPosition;
    }

    public String nextToken() {
        this.currentPosition = (this.newPosition < 0 || this.delimsChanged) ? skipDelimiters(this.currentPosition) : this.newPosition;
        this.delimsChanged = false;
        this.newPosition = -1;
        if (this.currentPosition >= this.maxPosition) {
            throw new NoSuchElementException();
        }
        int i2 = this.currentPosition;
        this.currentPosition = scanToken(this.currentPosition);
        return this.str.substring(i2, this.currentPosition);
    }

    public String nextToken(String str) {
        this.delimiters = str;
        this.delimsChanged = true;
        setMaxDelimCodePoint();
        return nextToken();
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        return nextToken();
    }

    public int countTokens() {
        int iSkipDelimiters;
        int i2 = 0;
        int iScanToken = this.currentPosition;
        while (iScanToken < this.maxPosition && (iSkipDelimiters = skipDelimiters(iScanToken)) < this.maxPosition) {
            iScanToken = scanToken(iSkipDelimiters);
            i2++;
        }
        return i2;
    }
}
