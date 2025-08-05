package java.text;

/* loaded from: rt.jar:java/text/PatternEntry.class */
class PatternEntry {
    static final int RESET = -2;
    static final int UNSET = -1;
    int strength;
    String chars;
    String extension;

    public void appendQuotedExtension(StringBuffer stringBuffer) {
        appendQuoted(this.extension, stringBuffer);
    }

    public void appendQuotedChars(StringBuffer stringBuffer) {
        appendQuoted(this.chars, stringBuffer);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this.chars.equals(((PatternEntry) obj).chars);
    }

    public int hashCode() {
        return this.chars.hashCode();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        addToBuffer(stringBuffer, true, false, null);
        return stringBuffer.toString();
    }

    final int getStrength() {
        return this.strength;
    }

    final String getExtension() {
        return this.extension;
    }

    final String getChars() {
        return this.chars;
    }

    void addToBuffer(StringBuffer stringBuffer, boolean z2, boolean z3, PatternEntry patternEntry) {
        if (z3 && stringBuffer.length() > 0) {
            if (this.strength == 0 || patternEntry != null) {
                stringBuffer.append('\n');
            } else {
                stringBuffer.append(' ');
            }
        }
        if (patternEntry != null) {
            stringBuffer.append('&');
            if (z3) {
                stringBuffer.append(' ');
            }
            patternEntry.appendQuotedChars(stringBuffer);
            appendQuotedExtension(stringBuffer);
            if (z3) {
                stringBuffer.append(' ');
            }
        }
        switch (this.strength) {
            case -2:
                stringBuffer.append('&');
                break;
            case -1:
                stringBuffer.append('?');
                break;
            case 0:
                stringBuffer.append('<');
                break;
            case 1:
                stringBuffer.append(';');
                break;
            case 2:
                stringBuffer.append(',');
                break;
            case 3:
                stringBuffer.append('=');
                break;
        }
        if (z3) {
            stringBuffer.append(' ');
        }
        appendQuoted(this.chars, stringBuffer);
        if (z2 && this.extension.length() != 0) {
            stringBuffer.append('/');
            appendQuoted(this.extension, stringBuffer);
        }
    }

    static void appendQuoted(String str, StringBuffer stringBuffer) {
        boolean z2 = false;
        char cCharAt = str.charAt(0);
        if (Character.isSpaceChar(cCharAt) || isSpecialChar(cCharAt)) {
            z2 = true;
            stringBuffer.append('\'');
        } else {
            switch (cCharAt) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case 16:
                case '@':
                    z2 = true;
                    stringBuffer.append('\'');
                    break;
                case '\'':
                    z2 = true;
                    stringBuffer.append('\'');
                    break;
                default:
                    if (0 != 0) {
                        z2 = false;
                        stringBuffer.append('\'');
                        break;
                    }
                    break;
            }
        }
        stringBuffer.append(str);
        if (z2) {
            stringBuffer.append('\'');
        }
    }

    PatternEntry(int i2, StringBuffer stringBuffer, StringBuffer stringBuffer2) {
        this.strength = -1;
        this.chars = "";
        this.extension = "";
        this.strength = i2;
        this.chars = stringBuffer.toString();
        this.extension = stringBuffer2.length() > 0 ? stringBuffer2.toString() : "";
    }

    /* loaded from: rt.jar:java/text/PatternEntry$Parser.class */
    static class Parser {
        private String pattern;
        private StringBuffer newChars = new StringBuffer();
        private StringBuffer newExtension = new StringBuffer();

        /* renamed from: i, reason: collision with root package name */
        private int f12492i = 0;

        public Parser(String str) {
            this.pattern = str;
        }

        /* JADX WARN: Removed duplicated region for block: B:69:0x0227 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0229  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.text.PatternEntry next() throws java.text.ParseException {
            /*
                Method dump skipped, instructions count: 655
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.text.PatternEntry.Parser.next():java.text.PatternEntry");
        }
    }

    static boolean isSpecialChar(char c2) {
        return c2 == ' ' || (c2 <= '/' && c2 >= '\"') || ((c2 <= '?' && c2 >= ':') || ((c2 <= '`' && c2 >= '[') || (c2 <= '~' && c2 >= '{')));
    }
}
