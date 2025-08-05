package java.io;

import java.util.Arrays;

/* loaded from: rt.jar:java/io/StreamTokenizer.class */
public class StreamTokenizer {
    private Reader reader;
    private InputStream input;
    private char[] buf;
    private int peekc;
    private static final int NEED_CHAR = Integer.MAX_VALUE;
    private static final int SKIP_LF = 2147483646;
    private boolean pushedBack;
    private boolean forceLower;
    private int LINENO;
    private boolean eolIsSignificantP;
    private boolean slashSlashCommentsP;
    private boolean slashStarCommentsP;
    private byte[] ctype;
    private static final byte CT_WHITESPACE = 1;
    private static final byte CT_DIGIT = 2;
    private static final byte CT_ALPHA = 4;
    private static final byte CT_QUOTE = 8;
    private static final byte CT_COMMENT = 16;
    public int ttype;
    public static final int TT_EOF = -1;
    public static final int TT_EOL = 10;
    public static final int TT_NUMBER = -2;
    public static final int TT_WORD = -3;
    private static final int TT_NOTHING = -4;
    public String sval;
    public double nval;

    private StreamTokenizer() {
        this.reader = null;
        this.input = null;
        this.buf = new char[20];
        this.peekc = Integer.MAX_VALUE;
        this.LINENO = 1;
        this.eolIsSignificantP = false;
        this.slashSlashCommentsP = false;
        this.slashStarCommentsP = false;
        this.ctype = new byte[256];
        this.ttype = -4;
        wordChars(97, 122);
        wordChars(65, 90);
        wordChars(160, 255);
        whitespaceChars(0, 32);
        commentChar(47);
        quoteChar(34);
        quoteChar(39);
        parseNumbers();
    }

    @Deprecated
    public StreamTokenizer(InputStream inputStream) {
        this();
        if (inputStream == null) {
            throw new NullPointerException();
        }
        this.input = inputStream;
    }

    public StreamTokenizer(Reader reader) {
        this();
        if (reader == null) {
            throw new NullPointerException();
        }
        this.reader = reader;
    }

    public void resetSyntax() {
        int length = this.ctype.length;
        while (true) {
            length--;
            if (length >= 0) {
                this.ctype[length] = 0;
            } else {
                return;
            }
        }
    }

    public void wordChars(int i2, int i3) {
        if (i2 < 0) {
            i2 = 0;
        }
        if (i3 >= this.ctype.length) {
            i3 = this.ctype.length - 1;
        }
        while (i2 <= i3) {
            byte[] bArr = this.ctype;
            int i4 = i2;
            i2++;
            bArr[i4] = (byte) (bArr[i4] | 4);
        }
    }

    public void whitespaceChars(int i2, int i3) {
        if (i2 < 0) {
            i2 = 0;
        }
        if (i3 >= this.ctype.length) {
            i3 = this.ctype.length - 1;
        }
        while (i2 <= i3) {
            int i4 = i2;
            i2++;
            this.ctype[i4] = 1;
        }
    }

    public void ordinaryChars(int i2, int i3) {
        if (i2 < 0) {
            i2 = 0;
        }
        if (i3 >= this.ctype.length) {
            i3 = this.ctype.length - 1;
        }
        while (i2 <= i3) {
            int i4 = i2;
            i2++;
            this.ctype[i4] = 0;
        }
    }

    public void ordinaryChar(int i2) {
        if (i2 >= 0 && i2 < this.ctype.length) {
            this.ctype[i2] = 0;
        }
    }

    public void commentChar(int i2) {
        if (i2 >= 0 && i2 < this.ctype.length) {
            this.ctype[i2] = 16;
        }
    }

    public void quoteChar(int i2) {
        if (i2 >= 0 && i2 < this.ctype.length) {
            this.ctype[i2] = 8;
        }
    }

    public void parseNumbers() {
        for (int i2 = 48; i2 <= 57; i2++) {
            byte[] bArr = this.ctype;
            int i3 = i2;
            bArr[i3] = (byte) (bArr[i3] | 2);
        }
        byte[] bArr2 = this.ctype;
        bArr2[46] = (byte) (bArr2[46] | 2);
        byte[] bArr3 = this.ctype;
        bArr3[45] = (byte) (bArr3[45] | 2);
    }

    public void eolIsSignificant(boolean z2) {
        this.eolIsSignificantP = z2;
    }

    public void slashStarComments(boolean z2) {
        this.slashStarCommentsP = z2;
    }

    public void slashSlashComments(boolean z2) {
        this.slashSlashCommentsP = z2;
    }

    public void lowerCaseMode(boolean z2) {
        this.forceLower = z2;
    }

    private int read() throws IOException {
        if (this.reader != null) {
            return this.reader.read();
        }
        if (this.input != null) {
            return this.input.read();
        }
        throw new IllegalStateException();
    }

    public int nextToken() throws IOException {
        int i2;
        int i3;
        int i4;
        int i5;
        double d2;
        if (this.pushedBack) {
            this.pushedBack = false;
            return this.ttype;
        }
        byte[] bArr = this.ctype;
        this.sval = null;
        int i6 = this.peekc;
        if (i6 < 0) {
            i6 = Integer.MAX_VALUE;
        }
        if (i6 == SKIP_LF) {
            i6 = read();
            if (i6 < 0) {
                this.ttype = -1;
                return -1;
            }
            if (i6 == 10) {
                i6 = Integer.MAX_VALUE;
            }
        }
        if (i6 == Integer.MAX_VALUE) {
            i6 = read();
            if (i6 < 0) {
                this.ttype = -1;
                return -1;
            }
        }
        this.ttype = i6;
        this.peekc = Integer.MAX_VALUE;
        byte b2 = i6 < 256 ? bArr[i6] : (byte) 4;
        while (true) {
            byte b3 = b2;
            if ((b3 & 1) != 0) {
                if (i6 == 13) {
                    this.LINENO++;
                    if (this.eolIsSignificantP) {
                        this.peekc = SKIP_LF;
                        this.ttype = 10;
                        return 10;
                    }
                    i6 = read();
                    if (i6 == 10) {
                        i6 = read();
                    }
                } else {
                    if (i6 == 10) {
                        this.LINENO++;
                        if (this.eolIsSignificantP) {
                            this.ttype = 10;
                            return 10;
                        }
                    }
                    i6 = read();
                }
                if (i6 < 0) {
                    this.ttype = -1;
                    return -1;
                }
                b2 = i6 < 256 ? bArr[i6] : (byte) 4;
            } else {
                if ((b3 & 2) != 0) {
                    boolean z2 = false;
                    if (i6 == 45) {
                        i6 = read();
                        if (i6 != 46 && (i6 < 48 || i6 > 57)) {
                            this.peekc = i6;
                            this.ttype = 45;
                            return 45;
                        }
                        z2 = true;
                    }
                    double d3 = 0.0d;
                    int i7 = 0;
                    int i8 = 0;
                    while (true) {
                        if (i6 == 46 && i8 == 0) {
                            i8 = 1;
                        } else {
                            if (48 > i6 || i6 > 57) {
                                break;
                            }
                            d3 = (d3 * 10.0d) + (i6 - 48);
                            i7 += i8;
                        }
                        i6 = read();
                    }
                    this.peekc = i6;
                    if (i7 != 0) {
                        double d4 = 10.0d;
                        while (true) {
                            d2 = d4;
                            i7--;
                            if (i7 <= 0) {
                                break;
                            }
                            d4 = d2 * 10.0d;
                        }
                        d3 /= d2;
                    }
                    this.nval = z2 ? -d3 : d3;
                    this.ttype = -2;
                    return -2;
                }
                if ((b3 & 4) != 0) {
                    int i9 = 0;
                    do {
                        if (i9 >= this.buf.length) {
                            this.buf = Arrays.copyOf(this.buf, this.buf.length * 2);
                        }
                        int i10 = i9;
                        i9++;
                        this.buf[i10] = (char) i6;
                        i6 = read();
                    } while (((i6 < 0 ? (byte) 1 : i6 < 256 ? bArr[i6] : (byte) 4) & 6) != 0);
                    this.peekc = i6;
                    this.sval = String.copyValueOf(this.buf, 0, i9);
                    if (this.forceLower) {
                        this.sval = this.sval.toLowerCase();
                    }
                    this.ttype = -3;
                    return -3;
                }
                if ((b3 & 8) != 0) {
                    this.ttype = i6;
                    int i11 = 0;
                    int i12 = read();
                    while (i12 >= 0 && i12 != this.ttype && i12 != 10 && i12 != 13) {
                        if (i12 == 92) {
                            i5 = read();
                            if (i5 >= 48 && i5 <= 55) {
                                i5 -= 48;
                                int i13 = read();
                                if (48 <= i13 && i13 <= 55) {
                                    i5 = (i5 << 3) + (i13 - 48);
                                    int i14 = read();
                                    if (48 <= i14 && i14 <= 55 && i5 <= 51) {
                                        i5 = (i5 << 3) + (i14 - 48);
                                        i12 = read();
                                    } else {
                                        i12 = i14;
                                    }
                                } else {
                                    i12 = i13;
                                }
                            } else {
                                switch (i5) {
                                    case 97:
                                        i5 = 7;
                                        break;
                                    case 98:
                                        i5 = 8;
                                        break;
                                    case 102:
                                        i5 = 12;
                                        break;
                                    case 110:
                                        i5 = 10;
                                        break;
                                    case 114:
                                        i5 = 13;
                                        break;
                                    case 116:
                                        i5 = 9;
                                        break;
                                    case 118:
                                        i5 = 11;
                                        break;
                                }
                                i12 = read();
                            }
                        } else {
                            i5 = i12;
                            i12 = read();
                        }
                        if (i11 >= this.buf.length) {
                            this.buf = Arrays.copyOf(this.buf, this.buf.length * 2);
                        }
                        int i15 = i11;
                        i11++;
                        this.buf[i15] = (char) i5;
                    }
                    this.peekc = i12 == this.ttype ? Integer.MAX_VALUE : i12;
                    this.sval = String.copyValueOf(this.buf, 0, i11);
                    return this.ttype;
                }
                if (i6 == 47 && (this.slashSlashCommentsP || this.slashStarCommentsP)) {
                    int i16 = read();
                    if (i16 == 42 && this.slashStarCommentsP) {
                        int i17 = 0;
                        while (true) {
                            int i18 = i17;
                            int i19 = read();
                            int i20 = i19;
                            if (i19 != 47 || i18 != 42) {
                                if (i20 == 13) {
                                    this.LINENO++;
                                    i20 = read();
                                    if (i20 == 10) {
                                        i20 = read();
                                    }
                                } else if (i20 == 10) {
                                    this.LINENO++;
                                    i20 = read();
                                }
                                if (i20 < 0) {
                                    this.ttype = -1;
                                    return -1;
                                }
                                i17 = i20;
                            } else {
                                return nextToken();
                            }
                        }
                    } else {
                        if (i16 == 47 && this.slashSlashCommentsP) {
                            do {
                                i4 = read();
                                if (i4 == 10 || i4 == 13) {
                                    break;
                                }
                            } while (i4 >= 0);
                            this.peekc = i4;
                            return nextToken();
                        }
                        if ((bArr[47] & 16) != 0) {
                            do {
                                i3 = read();
                                if (i3 == 10 || i3 == 13) {
                                    break;
                                }
                            } while (i3 >= 0);
                            this.peekc = i3;
                            return nextToken();
                        }
                        this.peekc = i16;
                        this.ttype = 47;
                        return 47;
                    }
                } else {
                    if ((b3 & 16) != 0) {
                        do {
                            i2 = read();
                            if (i2 == 10 || i2 == 13) {
                                break;
                            }
                        } while (i2 >= 0);
                        this.peekc = i2;
                        return nextToken();
                    }
                    int i21 = i6;
                    this.ttype = i21;
                    return i21;
                }
            }
        }
    }

    public void pushBack() {
        if (this.ttype != -4) {
            this.pushedBack = true;
        }
    }

    public int lineno() {
        return this.LINENO;
    }

    public String toString() {
        String str;
        switch (this.ttype) {
            case -4:
                str = "NOTHING";
                break;
            case -3:
                str = this.sval;
                break;
            case -2:
                str = "n=" + this.nval;
                break;
            case -1:
                str = "EOF";
                break;
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
                if (this.ttype < 256 && (this.ctype[this.ttype] & 8) != 0) {
                    str = this.sval;
                    break;
                } else {
                    str = new String(new char[]{'\'', (char) this.ttype, '\''});
                    break;
                }
                break;
            case 10:
                str = "EOL";
                break;
        }
        return "Token[" + str + "], line " + this.LINENO;
    }
}
