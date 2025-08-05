package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import javafx.fxml.FXMLLoader;

/* loaded from: gson-2.9.0.jar:com/google/gson/stream/JsonReader.class */
public class JsonReader implements Closeable {
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_EOF = 17;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private final Reader in;
    static final int BUFFER_SIZE = 1024;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int stackSize;
    private String[] pathNames;
    private int[] pathIndices;
    private boolean lenient = false;
    private final char[] buffer = new char[1024];
    private int pos = 0;
    private int limit = 0;
    private int lineNumber = 0;
    private int lineStart = 0;
    int peeked = 0;
    private int[] stack = new int[32];

    public JsonReader(Reader in) {
        this.stackSize = 0;
        int[] iArr = this.stack;
        int i2 = this.stackSize;
        this.stackSize = i2 + 1;
        iArr[i2] = 6;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }

    public final void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public final boolean isLenient() {
        return this.lenient;
    }

    public void beginArray() throws IOException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 3) {
            push(1);
            this.pathIndices[this.stackSize - 1] = 0;
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_ARRAY but was " + ((Object) peek()) + locationString());
    }

    public void endArray() throws IOException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 4) {
            this.stackSize--;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected END_ARRAY but was " + ((Object) peek()) + locationString());
    }

    public void beginObject() throws IOException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 1) {
            push(3);
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_OBJECT but was " + ((Object) peek()) + locationString());
    }

    public void endObject() throws IOException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 2) {
            this.stackSize--;
            this.pathNames[this.stackSize] = null;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected END_OBJECT but was " + ((Object) peek()) + locationString());
    }

    public boolean hasNext() throws IOException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        return (p2 == 2 || p2 == 4 || p2 == 17) ? false : true;
    }

    public JsonToken peek() throws IOException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        switch (p2) {
            case 1:
                return JsonToken.BEGIN_OBJECT;
            case 2:
                return JsonToken.END_OBJECT;
            case 3:
                return JsonToken.BEGIN_ARRAY;
            case 4:
                return JsonToken.END_ARRAY;
            case 5:
            case 6:
                return JsonToken.BOOLEAN;
            case 7:
                return JsonToken.NULL;
            case 8:
            case 9:
            case 10:
            case 11:
                return JsonToken.STRING;
            case 12:
            case 13:
            case 14:
                return JsonToken.NAME;
            case 15:
            case 16:
                return JsonToken.NUMBER;
            case 17:
                return JsonToken.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    int doPeek() throws IOException {
        int peekStack = this.stack[this.stackSize - 1];
        if (peekStack == 1) {
            this.stack[this.stackSize - 1] = 2;
        } else if (peekStack == 2) {
            switch (nextNonWhitespace(true)) {
                case 44:
                    break;
                case 59:
                    checkLenient();
                    break;
                case 93:
                    this.peeked = 4;
                    return 4;
                default:
                    throw syntaxError("Unterminated array");
            }
        } else {
            if (peekStack == 3 || peekStack == 5) {
                this.stack[this.stackSize - 1] = 4;
                if (peekStack == 5) {
                    switch (nextNonWhitespace(true)) {
                        case 44:
                            break;
                        case 59:
                            checkLenient();
                            break;
                        case 125:
                            this.peeked = 2;
                            return 2;
                        default:
                            throw syntaxError("Unterminated object");
                    }
                }
                int c2 = nextNonWhitespace(true);
                switch (c2) {
                    case 34:
                        this.peeked = 13;
                        return 13;
                    case 39:
                        checkLenient();
                        this.peeked = 12;
                        return 12;
                    case 125:
                        if (peekStack != 5) {
                            this.peeked = 2;
                            return 2;
                        }
                        throw syntaxError("Expected name");
                    default:
                        checkLenient();
                        this.pos--;
                        if (isLiteral((char) c2)) {
                            this.peeked = 14;
                            return 14;
                        }
                        throw syntaxError("Expected name");
                }
            }
            if (peekStack == 4) {
                this.stack[this.stackSize - 1] = 5;
                switch (nextNonWhitespace(true)) {
                    case 58:
                        break;
                    case 61:
                        checkLenient();
                        if ((this.pos < this.limit || fillBuffer(1)) && this.buffer[this.pos] == '>') {
                            this.pos++;
                            break;
                        }
                        break;
                    default:
                        throw syntaxError("Expected ':'");
                }
            } else if (peekStack == 6) {
                if (this.lenient) {
                    consumeNonExecutePrefix();
                }
                this.stack[this.stackSize - 1] = 7;
            } else if (peekStack == 7) {
                if (nextNonWhitespace(false) == -1) {
                    this.peeked = 17;
                    return 17;
                }
                checkLenient();
                this.pos--;
            } else if (peekStack == 8) {
                throw new IllegalStateException("JsonReader is closed");
            }
        }
        switch (nextNonWhitespace(true)) {
            case 34:
                this.peeked = 9;
                return 9;
            case 39:
                checkLenient();
                this.peeked = 8;
                return 8;
            case 44:
            case 59:
                break;
            case 91:
                this.peeked = 3;
                return 3;
            case 93:
                if (peekStack == 1) {
                    this.peeked = 4;
                    return 4;
                }
                break;
            case 123:
                this.peeked = 1;
                return 1;
            default:
                this.pos--;
                int result = peekKeyword();
                if (result != 0) {
                    return result;
                }
                int result2 = peekNumber();
                if (result2 != 0) {
                    return result2;
                }
                if (!isLiteral(this.buffer[this.pos])) {
                    throw syntaxError("Expected value");
                }
                checkLenient();
                this.peeked = 10;
                return 10;
        }
        if (peekStack == 1 || peekStack == 2) {
            checkLenient();
            this.pos--;
            this.peeked = 7;
            return 7;
        }
        throw syntaxError("Unexpected value");
    }

    private int peekKeyword() throws IOException {
        String keyword;
        String keywordUpper;
        int peeking;
        char c2 = this.buffer[this.pos];
        if (c2 == 't' || c2 == 'T') {
            keyword = "true";
            keywordUpper = "TRUE";
            peeking = 5;
        } else if (c2 == 'f' || c2 == 'F') {
            keyword = "false";
            keywordUpper = "FALSE";
            peeking = 6;
        } else if (c2 == 'n' || c2 == 'N') {
            keyword = FXMLLoader.NULL_KEYWORD;
            keywordUpper = "NULL";
            peeking = 7;
        } else {
            return 0;
        }
        int length = keyword.length();
        for (int i2 = 1; i2 < length; i2++) {
            if (this.pos + i2 >= this.limit && !fillBuffer(i2 + 1)) {
                return 0;
            }
            char c3 = this.buffer[this.pos + i2];
            if (c3 != keyword.charAt(i2) && c3 != keywordUpper.charAt(i2)) {
                return 0;
            }
        }
        if ((this.pos + length < this.limit || fillBuffer(length + 1)) && isLiteral(this.buffer[this.pos + length])) {
            return 0;
        }
        this.pos += length;
        int i3 = peeking;
        this.peeked = i3;
        return i3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x01ce, code lost:
    
        if (r14 != 7) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x01d1, code lost:
    
        r6.peekedNumberLength = r15;
        r6.peeked = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x01de, code lost:
    
        return 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x01df, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00e6, code lost:
    
        if (isLiteral(r0) != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00ec, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0177, code lost:
    
        if (r14 != 2) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x017c, code lost:
    
        if (r13 == false) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0185, code lost:
    
        if (r10 != Long.MIN_VALUE) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x018a, code lost:
    
        if (r12 == false) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0191, code lost:
    
        if (r10 != 0) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0197, code lost:
    
        if (false != r12) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x019d, code lost:
    
        if (r12 == false) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01a0, code lost:
    
        r1 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01a5, code lost:
    
        r1 = -r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x01a8, code lost:
    
        r6.peekedLong = r1;
        r6.pos += r15;
        r6.peeked = 15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x01bd, code lost:
    
        return 15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x01c1, code lost:
    
        if (r14 == 2) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x01c7, code lost:
    
        if (r14 == 4) goto L102;
     */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00d2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int peekNumber() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 481
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.stream.JsonReader.peekNumber():int");
    }

    private boolean isLiteral(char c2) throws IOException {
        switch (c2) {
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case '[':
            case ']':
            case '{':
            case '}':
                return false;
            case '#':
            case '/':
            case ';':
            case '=':
            case '\\':
                checkLenient();
                return false;
            default:
                return true;
        }
    }

    public String nextName() throws IOException {
        String result;
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 14) {
            result = nextUnquotedValue();
        } else if (p2 == 12) {
            result = nextQuotedValue('\'');
        } else if (p2 == 13) {
            result = nextQuotedValue('\"');
        } else {
            throw new IllegalStateException("Expected a name but was " + ((Object) peek()) + locationString());
        }
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = result;
        return result;
    }

    public String nextString() throws IOException {
        String result;
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 10) {
            result = nextUnquotedValue();
        } else if (p2 == 8) {
            result = nextQuotedValue('\'');
        } else if (p2 == 9) {
            result = nextQuotedValue('\"');
        } else if (p2 == 11) {
            result = this.peekedString;
            this.peekedString = null;
        } else if (p2 == 15) {
            result = Long.toString(this.peekedLong);
        } else if (p2 == 16) {
            result = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            throw new IllegalStateException("Expected a string but was " + ((Object) peek()) + locationString());
        }
        this.peeked = 0;
        int[] iArr = this.pathIndices;
        int i2 = this.stackSize - 1;
        iArr[i2] = iArr[i2] + 1;
        return result;
    }

    public boolean nextBoolean() throws IOException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 5) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return true;
        }
        if (p2 == 6) {
            this.peeked = 0;
            int[] iArr2 = this.pathIndices;
            int i3 = this.stackSize - 1;
            iArr2[i3] = iArr2[i3] + 1;
            return false;
        }
        throw new IllegalStateException("Expected a boolean but was " + ((Object) peek()) + locationString());
    }

    public void nextNull() throws IOException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 7) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return;
        }
        throw new IllegalStateException("Expected null but was " + ((Object) peek()) + locationString());
    }

    public double nextDouble() throws IOException, NumberFormatException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 15) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return this.peekedLong;
        }
        if (p2 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (p2 == 8 || p2 == 9) {
            this.peekedString = nextQuotedValue(p2 == 8 ? '\'' : '\"');
        } else if (p2 == 10) {
            this.peekedString = nextUnquotedValue();
        } else if (p2 != 11) {
            throw new IllegalStateException("Expected a double but was " + ((Object) peek()) + locationString());
        }
        this.peeked = 11;
        double result = Double.parseDouble(this.peekedString);
        if (!this.lenient && (Double.isNaN(result) || Double.isInfinite(result))) {
            throw new MalformedJsonException("JSON forbids NaN and infinities: " + result + locationString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] iArr2 = this.pathIndices;
        int i3 = this.stackSize - 1;
        iArr2[i3] = iArr2[i3] + 1;
        return result;
    }

    public long nextLong() throws IOException, NumberFormatException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 15) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return this.peekedLong;
        }
        if (p2 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (p2 == 8 || p2 == 9 || p2 == 10) {
            if (p2 == 10) {
                this.peekedString = nextUnquotedValue();
            } else {
                this.peekedString = nextQuotedValue(p2 == 8 ? '\'' : '\"');
            }
            try {
                long result = Long.parseLong(this.peekedString);
                this.peeked = 0;
                int[] iArr2 = this.pathIndices;
                int i3 = this.stackSize - 1;
                iArr2[i3] = iArr2[i3] + 1;
                return result;
            } catch (NumberFormatException e2) {
            }
        } else {
            throw new IllegalStateException("Expected a long but was " + ((Object) peek()) + locationString());
        }
        this.peeked = 11;
        double asDouble = Double.parseDouble(this.peekedString);
        long result2 = (long) asDouble;
        if (result2 != asDouble) {
            throw new NumberFormatException("Expected a long but was " + this.peekedString + locationString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] iArr3 = this.pathIndices;
        int i4 = this.stackSize - 1;
        iArr3[i4] = iArr3[i4] + 1;
        return result2;
    }

    private String nextQuotedValue(char quote) throws IOException {
        char[] buffer = this.buffer;
        StringBuilder builder = null;
        do {
            int p2 = this.pos;
            int l2 = this.limit;
            int start = p2;
            while (p2 < l2) {
                int i2 = p2;
                p2++;
                char c2 = buffer[i2];
                if (c2 == quote) {
                    this.pos = p2;
                    int len = (p2 - start) - 1;
                    if (builder == null) {
                        return new String(buffer, start, len);
                    }
                    builder.append(buffer, start, len);
                    return builder.toString();
                }
                if (c2 == '\\') {
                    this.pos = p2;
                    int len2 = (p2 - start) - 1;
                    if (builder == null) {
                        int estimatedLength = (len2 + 1) * 2;
                        builder = new StringBuilder(Math.max(estimatedLength, 16));
                    }
                    builder.append(buffer, start, len2);
                    builder.append(readEscapeCharacter());
                    p2 = this.pos;
                    l2 = this.limit;
                    start = p2;
                } else if (c2 == '\n') {
                    this.lineNumber++;
                    this.lineStart = p2;
                }
            }
            if (builder == null) {
                int estimatedLength2 = (p2 - start) * 2;
                builder = new StringBuilder(Math.max(estimatedLength2, 16));
            }
            builder.append(buffer, start, p2 - start);
            this.pos = p2;
        } while (fillBuffer(1));
        throw syntaxError("Unterminated string");
    }

    private String nextUnquotedValue() throws IOException {
        StringBuilder builder = null;
        int i2 = 0;
        while (true) {
            if (this.pos + i2 < this.limit) {
                switch (this.buffer[this.pos + i2]) {
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                    case ',':
                    case ':':
                    case '[':
                    case ']':
                    case '{':
                    case '}':
                        break;
                    case '#':
                    case '/':
                    case ';':
                    case '=':
                    case '\\':
                        checkLenient();
                        break;
                    default:
                        i2++;
                        continue;
                }
            } else if (i2 < this.buffer.length) {
                if (fillBuffer(i2 + 1)) {
                }
            } else {
                if (builder == null) {
                    builder = new StringBuilder(Math.max(i2, 16));
                }
                builder.append(this.buffer, this.pos, i2);
                this.pos += i2;
                i2 = 0;
                if (!fillBuffer(1)) {
                }
            }
        }
        String result = null == builder ? new String(this.buffer, this.pos, i2) : builder.append(this.buffer, this.pos, i2).toString();
        this.pos += i2;
        return result;
    }

    private void skipQuotedValue(char quote) throws IOException {
        char[] buffer = this.buffer;
        do {
            int p2 = this.pos;
            int l2 = this.limit;
            while (p2 < l2) {
                int i2 = p2;
                p2++;
                char c2 = buffer[i2];
                if (c2 == quote) {
                    this.pos = p2;
                    return;
                }
                if (c2 == '\\') {
                    this.pos = p2;
                    readEscapeCharacter();
                    p2 = this.pos;
                    l2 = this.limit;
                } else if (c2 == '\n') {
                    this.lineNumber++;
                    this.lineStart = p2;
                }
            }
            this.pos = p2;
        } while (fillBuffer(1));
        throw syntaxError("Unterminated string");
    }

    private void skipUnquotedValue() throws IOException {
        do {
            int i2 = 0;
            while (this.pos + i2 < this.limit) {
                switch (this.buffer[this.pos + i2]) {
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                    case ',':
                    case ':':
                    case '[':
                    case ']':
                    case '{':
                    case '}':
                        break;
                    case '#':
                    case '/':
                    case ';':
                    case '=':
                    case '\\':
                        checkLenient();
                        break;
                    default:
                        i2++;
                }
                this.pos += i2;
                return;
            }
            this.pos += i2;
        } while (fillBuffer(1));
    }

    public int nextInt() throws IOException, NumberFormatException {
        int p2 = this.peeked;
        if (p2 == 0) {
            p2 = doPeek();
        }
        if (p2 == 15) {
            int result = (int) this.peekedLong;
            if (this.peekedLong != result) {
                throw new NumberFormatException("Expected an int but was " + this.peekedLong + locationString());
            }
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return result;
        }
        if (p2 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (p2 == 8 || p2 == 9 || p2 == 10) {
            if (p2 == 10) {
                this.peekedString = nextUnquotedValue();
            } else {
                this.peekedString = nextQuotedValue(p2 == 8 ? '\'' : '\"');
            }
            try {
                int result2 = Integer.parseInt(this.peekedString);
                this.peeked = 0;
                int[] iArr2 = this.pathIndices;
                int i3 = this.stackSize - 1;
                iArr2[i3] = iArr2[i3] + 1;
                return result2;
            } catch (NumberFormatException e2) {
            }
        } else {
            throw new IllegalStateException("Expected an int but was " + ((Object) peek()) + locationString());
        }
        this.peeked = 11;
        double asDouble = Double.parseDouble(this.peekedString);
        int result3 = (int) asDouble;
        if (result3 != asDouble) {
            throw new NumberFormatException("Expected an int but was " + this.peekedString + locationString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] iArr3 = this.pathIndices;
        int i4 = this.stackSize - 1;
        iArr3[i4] = iArr3[i4] + 1;
        return result3;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.in.close();
    }

    public void skipValue() throws IOException {
        int count = 0;
        do {
            int p2 = this.peeked;
            if (p2 == 0) {
                p2 = doPeek();
            }
            if (p2 == 3) {
                push(1);
                count++;
            } else if (p2 == 1) {
                push(3);
                count++;
            } else if (p2 == 4 || p2 == 2) {
                this.stackSize--;
                count--;
            } else if (p2 == 14 || p2 == 10) {
                skipUnquotedValue();
            } else if (p2 == 8 || p2 == 12) {
                skipQuotedValue('\'');
            } else if (p2 == 9 || p2 == 13) {
                skipQuotedValue('\"');
            } else if (p2 == 16) {
                this.pos += this.peekedNumberLength;
            }
            this.peeked = 0;
        } while (count != 0);
        int[] iArr = this.pathIndices;
        int i2 = this.stackSize - 1;
        iArr[i2] = iArr[i2] + 1;
        this.pathNames[this.stackSize - 1] = FXMLLoader.NULL_KEYWORD;
    }

    private void push(int newTop) {
        if (this.stackSize == this.stack.length) {
            int newLength = this.stackSize * 2;
            this.stack = Arrays.copyOf(this.stack, newLength);
            this.pathIndices = Arrays.copyOf(this.pathIndices, newLength);
            this.pathNames = (String[]) Arrays.copyOf(this.pathNames, newLength);
        }
        int[] iArr = this.stack;
        int i2 = this.stackSize;
        this.stackSize = i2 + 1;
        iArr[i2] = newTop;
    }

    private boolean fillBuffer(int minimum) throws IOException {
        char[] buffer = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(buffer, this.pos, buffer, 0, this.limit);
        } else {
            this.limit = 0;
        }
        this.pos = 0;
        do {
            int total = this.in.read(buffer, this.limit, buffer.length - this.limit);
            if (total != -1) {
                this.limit += total;
                if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && buffer[0] == 65279) {
                    this.pos++;
                    this.lineStart++;
                    minimum++;
                }
            } else {
                return false;
            }
        } while (this.limit < minimum);
        return true;
    }

    private int nextNonWhitespace(boolean throwOnEof) throws IOException {
        char[] buffer = this.buffer;
        int p2 = this.pos;
        int l2 = this.limit;
        while (true) {
            if (p2 == l2) {
                this.pos = p2;
                if (fillBuffer(1)) {
                    p2 = this.pos;
                    l2 = this.limit;
                } else {
                    if (throwOnEof) {
                        throw new EOFException("End of input" + locationString());
                    }
                    return -1;
                }
            }
            int i2 = p2;
            p2++;
            char c2 = buffer[i2];
            if (c2 == '\n') {
                this.lineNumber++;
                this.lineStart = p2;
            } else if (c2 != ' ' && c2 != '\r' && c2 != '\t') {
                if (c2 == '/') {
                    this.pos = p2;
                    if (p2 == l2) {
                        this.pos--;
                        boolean charsLoaded = fillBuffer(2);
                        this.pos++;
                        if (!charsLoaded) {
                            return c2;
                        }
                    }
                    checkLenient();
                    char peek = buffer[this.pos];
                    switch (peek) {
                        case '*':
                            this.pos++;
                            if (!skipTo("*/")) {
                                throw syntaxError("Unterminated comment");
                            }
                            p2 = this.pos + 2;
                            l2 = this.limit;
                            break;
                        case '/':
                            this.pos++;
                            skipToEndOfLine();
                            p2 = this.pos;
                            l2 = this.limit;
                            break;
                        default:
                            return c2;
                    }
                } else if (c2 == '#') {
                    this.pos = p2;
                    checkLenient();
                    skipToEndOfLine();
                    p2 = this.pos;
                    l2 = this.limit;
                } else {
                    this.pos = p2;
                    return c2;
                }
            }
        }
    }

    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() throws IOException {
        char c2;
        do {
            if (this.pos < this.limit || fillBuffer(1)) {
                char[] cArr = this.buffer;
                int i2 = this.pos;
                this.pos = i2 + 1;
                c2 = cArr[i2];
                if (c2 == '\n') {
                    this.lineNumber++;
                    this.lineStart = this.pos;
                    return;
                }
            } else {
                return;
            }
        } while (c2 != '\r');
    }

    private boolean skipTo(String toFind) throws IOException {
        int length = toFind.length();
        while (true) {
            if (this.pos + length <= this.limit || fillBuffer(length)) {
                if (this.buffer[this.pos] == '\n') {
                    this.lineNumber++;
                    this.lineStart = this.pos + 1;
                } else {
                    for (int c2 = 0; c2 < length; c2++) {
                        if (this.buffer[this.pos + c2] != toFind.charAt(c2)) {
                            break;
                        }
                    }
                    return true;
                }
                this.pos++;
            } else {
                return false;
            }
        }
    }

    public String toString() {
        return getClass().getSimpleName() + locationString();
    }

    String locationString() {
        int line = this.lineNumber + 1;
        int column = (this.pos - this.lineStart) + 1;
        return " at line " + line + " column " + column + " path " + getPath();
    }

    private String getPath(boolean usePreviousPath) {
        StringBuilder result = new StringBuilder().append('$');
        for (int i2 = 0; i2 < this.stackSize; i2++) {
            switch (this.stack[i2]) {
                case 1:
                case 2:
                    int pathIndex = this.pathIndices[i2];
                    if (usePreviousPath && pathIndex > 0 && i2 == this.stackSize - 1) {
                        pathIndex--;
                    }
                    result.append('[').append(pathIndex).append(']');
                    break;
                case 3:
                case 4:
                case 5:
                    result.append('.');
                    if (this.pathNames[i2] != null) {
                        result.append(this.pathNames[i2]);
                        break;
                    } else {
                        break;
                    }
            }
        }
        return result.toString();
    }

    public String getPreviousPath() {
        return getPath(true);
    }

    public String getPath() {
        return getPath(false);
    }

    private char readEscapeCharacter() throws IOException {
        char c2;
        int i2;
        if (this.pos == this.limit && !fillBuffer(1)) {
            throw syntaxError("Unterminated escape sequence");
        }
        char[] cArr = this.buffer;
        int i3 = this.pos;
        this.pos = i3 + 1;
        char escaped = cArr[i3];
        switch (escaped) {
            case '\n':
                this.lineNumber++;
                this.lineStart = this.pos;
                break;
            case '\"':
            case '\'':
            case '/':
            case '\\':
                break;
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            case 'u':
                if (this.pos + 4 > this.limit && !fillBuffer(4)) {
                    throw syntaxError("Unterminated escape sequence");
                }
                char result = 0;
                int i4 = this.pos;
                int end = i4 + 4;
                while (i4 < end) {
                    char c3 = this.buffer[i4];
                    char result2 = (char) (result << 4);
                    if (c3 >= '0' && c3 <= '9') {
                        c2 = result2;
                        i2 = c3 - '0';
                    } else if (c3 >= 'a' && c3 <= 'f') {
                        c2 = result2;
                        i2 = (c3 - 'a') + 10;
                    } else if (c3 >= 'A' && c3 <= 'F') {
                        c2 = result2;
                        i2 = (c3 - 'A') + 10;
                    } else {
                        throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
                    }
                    result = (char) (c2 + i2);
                    i4++;
                }
                this.pos += 4;
                return result;
            default:
                throw syntaxError("Invalid escape sequence");
        }
        return escaped;
    }

    private IOException syntaxError(String message) throws IOException {
        throw new MalformedJsonException(message + locationString());
    }

    private void consumeNonExecutePrefix() throws IOException {
        nextNonWhitespace(true);
        this.pos--;
        if (this.pos + 5 > this.limit && !fillBuffer(5)) {
            return;
        }
        int p2 = this.pos;
        char[] buf = this.buffer;
        if (buf[p2] != ')' || buf[p2 + 1] != ']' || buf[p2 + 2] != '}' || buf[p2 + 3] != '\'' || buf[p2 + 4] != '\n') {
            return;
        }
        this.pos += 5;
    }

    static {
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() { // from class: com.google.gson.stream.JsonReader.1
            @Override // com.google.gson.internal.JsonReaderInternalAccess
            public void promoteNameToValue(JsonReader reader) throws IOException {
                if (reader instanceof JsonTreeReader) {
                    ((JsonTreeReader) reader).promoteNameToValue();
                    return;
                }
                int p2 = reader.peeked;
                if (p2 == 0) {
                    p2 = reader.doPeek();
                }
                if (p2 == 13) {
                    reader.peeked = 9;
                } else if (p2 == 12) {
                    reader.peeked = 8;
                } else {
                    if (p2 == 14) {
                        reader.peeked = 10;
                        return;
                    }
                    throw new IllegalStateException("Expected a name but was " + ((Object) reader.peek()) + reader.locationString());
                }
            }
        };
    }
}
