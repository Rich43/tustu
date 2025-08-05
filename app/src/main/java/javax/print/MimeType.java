package javax.print;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

/* loaded from: rt.jar:javax/print/MimeType.class */
class MimeType implements Serializable, Cloneable {
    private static final long serialVersionUID = -2785720609362367683L;
    private String[] myPieces;
    private transient String myStringValue = null;
    private transient ParameterMapEntrySet myEntrySet = null;
    private transient ParameterMap myParameterMap = null;
    private static final int TOKEN_LEXEME = 0;
    private static final int QUOTED_STRING_LEXEME = 1;
    private static final int TSPECIAL_LEXEME = 2;
    private static final int EOF_LEXEME = 3;
    private static final int ILLEGAL_LEXEME = 4;

    /* loaded from: rt.jar:javax/print/MimeType$ParameterMapEntry.class */
    private class ParameterMapEntry implements Map.Entry {
        private int myIndex;

        public ParameterMapEntry(int i2) {
            this.myIndex = i2;
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            return MimeType.this.myPieces[this.myIndex];
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return MimeType.this.myPieces[this.myIndex + 1];
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            return obj != null && (obj instanceof Map.Entry) && getKey().equals(((Map.Entry) obj).getKey()) && getValue().equals(((Map.Entry) obj).getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return getKey().hashCode() ^ getValue().hashCode();
        }
    }

    /* loaded from: rt.jar:javax/print/MimeType$ParameterMapEntrySetIterator.class */
    private class ParameterMapEntrySetIterator implements Iterator {
        private int myIndex;

        private ParameterMapEntrySetIterator() {
            this.myIndex = 2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.myIndex < MimeType.this.myPieces.length;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (hasNext()) {
                ParameterMapEntry parameterMapEntry = MimeType.this.new ParameterMapEntry(this.myIndex);
                this.myIndex += 2;
                return parameterMapEntry;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:javax/print/MimeType$ParameterMapEntrySet.class */
    private class ParameterMapEntrySet extends AbstractSet {
        private ParameterMapEntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator iterator() {
            return new ParameterMapEntrySetIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return (MimeType.this.myPieces.length - 2) / 2;
        }
    }

    /* loaded from: rt.jar:javax/print/MimeType$ParameterMap.class */
    private class ParameterMap extends AbstractMap {
        private ParameterMap() {
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set entrySet() {
            if (MimeType.this.myEntrySet == null) {
                MimeType.this.myEntrySet = new ParameterMapEntrySet();
            }
            return MimeType.this.myEntrySet;
        }
    }

    public MimeType(String str) {
        parse(str);
    }

    public String getMimeType() {
        return getStringValue();
    }

    public String getMediaType() {
        return this.myPieces[0];
    }

    public String getMediaSubtype() {
        return this.myPieces[1];
    }

    public Map getParameterMap() {
        if (this.myParameterMap == null) {
            this.myParameterMap = new ParameterMap();
        }
        return this.myParameterMap;
    }

    public String toString() {
        return getStringValue();
    }

    public int hashCode() {
        return getStringValue().hashCode();
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof MimeType) && getStringValue().equals(((MimeType) obj).getStringValue());
    }

    private String getStringValue() {
        if (this.myStringValue == null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.myPieces[0]);
            stringBuffer.append('/');
            stringBuffer.append(this.myPieces[1]);
            int length = this.myPieces.length;
            for (int i2 = 2; i2 < length; i2 += 2) {
                stringBuffer.append(';');
                stringBuffer.append(' ');
                stringBuffer.append(this.myPieces[i2]);
                stringBuffer.append('=');
                stringBuffer.append(addQuotes(this.myPieces[i2 + 1]));
            }
            this.myStringValue = stringBuffer.toString();
        }
        return this.myStringValue;
    }

    /* loaded from: rt.jar:javax/print/MimeType$LexicalAnalyzer.class */
    private static class LexicalAnalyzer {
        protected String mySource;
        protected int mySourceLength;
        protected int myCurrentIndex = 0;
        protected int myLexemeType;
        protected int myLexemeBeginIndex;
        protected int myLexemeEndIndex;

        public LexicalAnalyzer(String str) {
            this.mySource = str;
            this.mySourceLength = str.length();
            nextLexeme();
        }

        public int getLexemeType() {
            return this.myLexemeType;
        }

        public String getLexeme() {
            if (this.myLexemeBeginIndex >= this.mySourceLength) {
                return null;
            }
            return this.mySource.substring(this.myLexemeBeginIndex, this.myLexemeEndIndex);
        }

        public char getLexemeFirstCharacter() {
            if (this.myLexemeBeginIndex >= this.mySourceLength) {
                return (char) 0;
            }
            return this.mySource.charAt(this.myLexemeBeginIndex);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v2 */
        /* JADX WARN: Type inference failed for: r0v53 */
        /* JADX WARN: Type inference failed for: r0v54 */
        /* JADX WARN: Type inference failed for: r0v55 */
        public void nextLexeme() {
            boolean z2 = false;
            int i2 = 0;
            while (z2 >= 0) {
                switch (z2) {
                    case false:
                        if (this.myCurrentIndex >= this.mySourceLength) {
                            this.myLexemeType = 3;
                            this.myLexemeBeginIndex = this.mySourceLength;
                            this.myLexemeEndIndex = this.mySourceLength;
                            z2 = -1;
                            break;
                        } else {
                            String str = this.mySource;
                            int i3 = this.myCurrentIndex;
                            this.myCurrentIndex = i3 + 1;
                            char cCharAt = str.charAt(i3);
                            if (Character.isWhitespace(cCharAt)) {
                                z2 = false;
                                break;
                            } else if (cCharAt == '\"') {
                                this.myLexemeType = 1;
                                this.myLexemeBeginIndex = this.myCurrentIndex;
                                z2 = true;
                                break;
                            } else if (cCharAt == '(') {
                                i2++;
                                z2 = 3;
                                break;
                            } else if (cCharAt == '/' || cCharAt == ';' || cCharAt == '=' || cCharAt == ')' || cCharAt == '<' || cCharAt == '>' || cCharAt == '@' || cCharAt == ',' || cCharAt == ':' || cCharAt == '\\' || cCharAt == '[' || cCharAt == ']' || cCharAt == '?') {
                                this.myLexemeType = 2;
                                this.myLexemeBeginIndex = this.myCurrentIndex - 1;
                                this.myLexemeEndIndex = this.myCurrentIndex;
                                z2 = -1;
                                break;
                            } else {
                                this.myLexemeType = 0;
                                this.myLexemeBeginIndex = this.myCurrentIndex - 1;
                                z2 = 5;
                                break;
                            }
                        }
                        break;
                    case true:
                        if (this.myCurrentIndex >= this.mySourceLength) {
                            this.myLexemeType = 4;
                            this.myLexemeBeginIndex = this.mySourceLength;
                            this.myLexemeEndIndex = this.mySourceLength;
                            z2 = -1;
                            break;
                        } else {
                            String str2 = this.mySource;
                            int i4 = this.myCurrentIndex;
                            this.myCurrentIndex = i4 + 1;
                            char cCharAt2 = str2.charAt(i4);
                            if (cCharAt2 == '\"') {
                                this.myLexemeEndIndex = this.myCurrentIndex - 1;
                                z2 = -1;
                                break;
                            } else if (cCharAt2 == '\\') {
                                z2 = 2;
                                break;
                            } else {
                                z2 = true;
                                break;
                            }
                        }
                    case true:
                        if (this.myCurrentIndex >= this.mySourceLength) {
                            this.myLexemeType = 4;
                            this.myLexemeBeginIndex = this.mySourceLength;
                            this.myLexemeEndIndex = this.mySourceLength;
                            z2 = -1;
                            break;
                        } else {
                            this.myCurrentIndex++;
                            z2 = true;
                            break;
                        }
                    case true:
                        if (this.myCurrentIndex >= this.mySourceLength) {
                            this.myLexemeType = 4;
                            this.myLexemeBeginIndex = this.mySourceLength;
                            this.myLexemeEndIndex = this.mySourceLength;
                            z2 = -1;
                            break;
                        } else {
                            String str3 = this.mySource;
                            int i5 = this.myCurrentIndex;
                            this.myCurrentIndex = i5 + 1;
                            char cCharAt3 = str3.charAt(i5);
                            if (cCharAt3 == '(') {
                                i2++;
                                z2 = 3;
                                break;
                            } else if (cCharAt3 == ')') {
                                i2--;
                                z2 = i2 == 0 ? 0 : 3;
                                break;
                            } else if (cCharAt3 == '\\') {
                                z2 = 4;
                                break;
                            } else {
                                z2 = 3;
                                break;
                            }
                        }
                    case true:
                        if (this.myCurrentIndex >= this.mySourceLength) {
                            this.myLexemeType = 4;
                            this.myLexemeBeginIndex = this.mySourceLength;
                            this.myLexemeEndIndex = this.mySourceLength;
                            z2 = -1;
                            break;
                        } else {
                            this.myCurrentIndex++;
                            z2 = 3;
                            break;
                        }
                    case true:
                        if (this.myCurrentIndex >= this.mySourceLength) {
                            this.myLexemeEndIndex = this.myCurrentIndex;
                            z2 = -1;
                            break;
                        } else {
                            String str4 = this.mySource;
                            int i6 = this.myCurrentIndex;
                            this.myCurrentIndex = i6 + 1;
                            char cCharAt4 = str4.charAt(i6);
                            if (Character.isWhitespace(cCharAt4)) {
                                this.myLexemeEndIndex = this.myCurrentIndex - 1;
                                z2 = -1;
                                break;
                            } else if (cCharAt4 == '\"' || cCharAt4 == '(' || cCharAt4 == '/' || cCharAt4 == ';' || cCharAt4 == '=' || cCharAt4 == ')' || cCharAt4 == '<' || cCharAt4 == '>' || cCharAt4 == '@' || cCharAt4 == ',' || cCharAt4 == ':' || cCharAt4 == '\\' || cCharAt4 == '[' || cCharAt4 == ']' || cCharAt4 == '?') {
                                this.myCurrentIndex--;
                                this.myLexemeEndIndex = this.myCurrentIndex;
                                z2 = -1;
                                break;
                            } else {
                                z2 = 5;
                                break;
                            }
                        }
                }
            }
        }
    }

    private static String toUnicodeLowerCase(String str) {
        int length = str.length();
        char[] cArr = new char[length];
        for (int i2 = 0; i2 < length; i2++) {
            cArr[i2] = Character.toLowerCase(str.charAt(i2));
        }
        return new String(cArr);
    }

    private static String removeBackslashes(String str) {
        int length = str.length();
        char[] cArr = new char[length];
        int i2 = 0;
        int i3 = 0;
        while (i3 < length) {
            char cCharAt = str.charAt(i3);
            if (cCharAt == '\\') {
                i3++;
                cCharAt = str.charAt(i3);
            }
            int i4 = i2;
            i2++;
            cArr[i4] = cCharAt;
            i3++;
        }
        return new String(cArr, 0, i2);
    }

    private static String addQuotes(String str) {
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length + 2);
        stringBuffer.append('\"');
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\"') {
                stringBuffer.append('\\');
            }
            stringBuffer.append(cCharAt);
        }
        stringBuffer.append('\"');
        return stringBuffer.toString();
    }

    private void parse(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(str);
        Vector vector = new Vector();
        if (lexicalAnalyzer.getLexemeType() == 0) {
            String unicodeLowerCase = toUnicodeLowerCase(lexicalAnalyzer.getLexeme());
            vector.add(unicodeLowerCase);
            lexicalAnalyzer.nextLexeme();
            boolean zEquals = unicodeLowerCase.equals("text");
            if (lexicalAnalyzer.getLexemeType() == 2 && lexicalAnalyzer.getLexemeFirstCharacter() == '/') {
                lexicalAnalyzer.nextLexeme();
                if (lexicalAnalyzer.getLexemeType() == 0) {
                    vector.add(toUnicodeLowerCase(lexicalAnalyzer.getLexeme()));
                    lexicalAnalyzer.nextLexeme();
                    while (lexicalAnalyzer.getLexemeType() == 2 && lexicalAnalyzer.getLexemeFirstCharacter() == ';') {
                        lexicalAnalyzer.nextLexeme();
                        if (lexicalAnalyzer.getLexemeType() == 0) {
                            String unicodeLowerCase2 = toUnicodeLowerCase(lexicalAnalyzer.getLexeme());
                            vector.add(unicodeLowerCase2);
                            lexicalAnalyzer.nextLexeme();
                            boolean zEquals2 = unicodeLowerCase2.equals("charset");
                            if (lexicalAnalyzer.getLexemeType() == 2 && lexicalAnalyzer.getLexemeFirstCharacter() == '=') {
                                lexicalAnalyzer.nextLexeme();
                                if (lexicalAnalyzer.getLexemeType() == 0) {
                                    String lexeme = lexicalAnalyzer.getLexeme();
                                    vector.add((zEquals && zEquals2) ? toUnicodeLowerCase(lexeme) : lexeme);
                                    lexicalAnalyzer.nextLexeme();
                                } else if (lexicalAnalyzer.getLexemeType() == 1) {
                                    String strRemoveBackslashes = removeBackslashes(lexicalAnalyzer.getLexeme());
                                    vector.add((zEquals && zEquals2) ? toUnicodeLowerCase(strRemoveBackslashes) : strRemoveBackslashes);
                                    lexicalAnalyzer.nextLexeme();
                                } else {
                                    throw new IllegalArgumentException();
                                }
                            } else {
                                throw new IllegalArgumentException();
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                    if (lexicalAnalyzer.getLexemeType() != 3) {
                        throw new IllegalArgumentException();
                    }
                    int size = vector.size();
                    this.myPieces = (String[]) vector.toArray(new String[size]);
                    for (int i2 = 4; i2 < size; i2 += 2) {
                        int i3 = 2;
                        while (i3 < i2 && this.myPieces[i3].compareTo(this.myPieces[i2]) <= 0) {
                            i3 += 2;
                        }
                        while (i3 < i2) {
                            String str2 = this.myPieces[i3];
                            this.myPieces[i3] = this.myPieces[i2];
                            this.myPieces[i2] = str2;
                            String str3 = this.myPieces[i3 + 1];
                            this.myPieces[i3 + 1] = this.myPieces[i2 + 1];
                            this.myPieces[i2 + 1] = str3;
                            i3 += 2;
                        }
                    }
                    return;
                }
                throw new IllegalArgumentException();
            }
            throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException();
    }
}
