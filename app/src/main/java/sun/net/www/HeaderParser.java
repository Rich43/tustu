package sun.net.www;

import java.util.Iterator;

/* loaded from: rt.jar:sun/net/www/HeaderParser.class */
public class HeaderParser {
    String raw;
    String[][] tab;
    int nkeys;
    int asize;

    public HeaderParser(String str) {
        this.asize = 10;
        this.raw = str;
        this.tab = new String[this.asize][2];
        parse();
    }

    private HeaderParser() {
        this.asize = 10;
    }

    public HeaderParser subsequence(int i2, int i3) {
        if (i2 == 0 && i3 == this.nkeys) {
            return this;
        }
        if (i2 < 0 || i2 >= i3 || i3 > this.nkeys) {
            throw new IllegalArgumentException("invalid start or end");
        }
        HeaderParser headerParser = new HeaderParser();
        headerParser.tab = new String[this.asize][2];
        headerParser.asize = this.asize;
        System.arraycopy(this.tab, i2, headerParser.tab, 0, i3 - i2);
        headerParser.nkeys = i3 - i2;
        return headerParser;
    }

    private void parse() {
        if (this.raw != null) {
            this.raw = this.raw.trim();
            char[] charArray = this.raw.toCharArray();
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            boolean z2 = true;
            boolean z3 = false;
            int length = charArray.length;
            while (i3 < length) {
                char c2 = charArray[i3];
                if (c2 == '=' && !z3) {
                    this.tab[i4][0] = new String(charArray, i2, i3 - i2).toLowerCase();
                    z2 = false;
                    i3++;
                    i2 = i3;
                } else if (c2 == '\"') {
                    if (z3) {
                        int i5 = i4;
                        i4++;
                        this.tab[i5][1] = new String(charArray, i2, i3 - i2);
                        z3 = false;
                        while (true) {
                            i3++;
                            if (i3 >= length || (charArray[i3] != ' ' && charArray[i3] != ',')) {
                                break;
                            }
                        }
                        z2 = true;
                        i2 = i3;
                    } else {
                        z3 = true;
                        i3++;
                        i2 = i3;
                    }
                } else if (c2 == ' ' || c2 == ',') {
                    if (z3) {
                        i3++;
                    } else {
                        if (z2) {
                            int i6 = i4;
                            i4++;
                            this.tab[i6][0] = new String(charArray, i2, i3 - i2).toLowerCase();
                        } else {
                            int i7 = i4;
                            i4++;
                            this.tab[i7][1] = new String(charArray, i2, i3 - i2);
                        }
                        while (i3 < length && (charArray[i3] == ' ' || charArray[i3] == ',')) {
                            i3++;
                        }
                        z2 = true;
                        i2 = i3;
                    }
                } else {
                    i3++;
                }
                if (i4 == this.asize) {
                    this.asize *= 2;
                    String[][] strArr = new String[this.asize][2];
                    System.arraycopy(this.tab, 0, strArr, 0, this.tab.length);
                    this.tab = strArr;
                }
            }
            int i8 = i3 - 1;
            if (i8 > i2) {
                if (!z2) {
                    if (charArray[i8] == '\"') {
                        int i9 = i4;
                        i4++;
                        this.tab[i9][1] = new String(charArray, i2, i8 - i2);
                    } else {
                        int i10 = i4;
                        i4++;
                        this.tab[i10][1] = new String(charArray, i2, (i8 - i2) + 1);
                    }
                } else {
                    int i11 = i4;
                    i4++;
                    this.tab[i11][0] = new String(charArray, i2, (i8 - i2) + 1).toLowerCase();
                }
            } else if (i8 == i2) {
                if (!z2) {
                    if (charArray[i8] == '\"') {
                        int i12 = i4;
                        i4++;
                        this.tab[i12][1] = String.valueOf(charArray[i8 - 1]);
                    } else {
                        int i13 = i4;
                        i4++;
                        this.tab[i13][1] = String.valueOf(charArray[i8]);
                    }
                } else {
                    int i14 = i4;
                    i4++;
                    this.tab[i14][0] = String.valueOf(charArray[i8]).toLowerCase();
                }
            }
            this.nkeys = i4;
        }
    }

    public String findKey(int i2) {
        if (i2 < 0 || i2 > this.asize) {
            return null;
        }
        return this.tab[i2][0];
    }

    public String findValue(int i2) {
        if (i2 < 0 || i2 > this.asize) {
            return null;
        }
        return this.tab[i2][1];
    }

    public String findValue(String str) {
        return findValue(str, null);
    }

    public String findValue(String str, String str2) {
        if (str == null) {
            return str2;
        }
        String lowerCase = str.toLowerCase();
        for (int i2 = 0; i2 < this.asize; i2++) {
            if (this.tab[i2][0] == null) {
                return str2;
            }
            if (lowerCase.equals(this.tab[i2][0])) {
                return this.tab[i2][1];
            }
        }
        return str2;
    }

    /* loaded from: rt.jar:sun/net/www/HeaderParser$ParserIterator.class */
    class ParserIterator implements Iterator<String> {
        int index;
        boolean returnsValue;

        ParserIterator(boolean z2) {
            this.returnsValue = z2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < HeaderParser.this.nkeys;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public String next() {
            String[][] strArr = HeaderParser.this.tab;
            int i2 = this.index;
            this.index = i2 + 1;
            return strArr[i2][this.returnsValue ? (char) 1 : (char) 0];
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

    public Iterator<String> keys() {
        return new ParserIterator(false);
    }

    public Iterator<String> values() {
        return new ParserIterator(true);
    }

    public String toString() {
        Iterator<String> itKeys = keys();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{size=" + this.asize + " nkeys=" + this.nkeys + " ");
        int i2 = 0;
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            String strFindValue = findValue(i2);
            if (strFindValue != null && "".equals(strFindValue)) {
                strFindValue = null;
            }
            stringBuffer.append(" {" + next + (strFindValue == null ? "" : "," + strFindValue) + "}");
            if (itKeys.hasNext()) {
                stringBuffer.append(",");
            }
            i2++;
        }
        stringBuffer.append(" }");
        return new String(stringBuffer);
    }

    public int findInt(String str, int i2) {
        try {
            return Integer.parseInt(findValue(str, String.valueOf(i2)));
        } catch (Throwable th) {
            return i2;
        }
    }
}
