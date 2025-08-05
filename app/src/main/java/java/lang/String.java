package java.lang;

import java.io.ObjectStreamField;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/lang/String.class */
public final class String implements Serializable, Comparable<String>, CharSequence {
    private final char[] value;
    private int hash;
    private static final long serialVersionUID = -6849794470754667710L;
    private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];
    public static final Comparator<String> CASE_INSENSITIVE_ORDER = new CaseInsensitiveComparator();

    public native String intern();

    public String() {
        this.value = "".value;
    }

    public String(String str) {
        this.value = str.value;
        this.hash = str.hash;
    }

    public String(char[] cArr) {
        this.value = Arrays.copyOf(cArr, cArr.length);
    }

    public String(char[] cArr, int i2, int i3) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 <= 0) {
            if (i3 < 0) {
                throw new StringIndexOutOfBoundsException(i3);
            }
            if (i2 <= cArr.length) {
                this.value = "".value;
                return;
            }
        }
        if (i2 > cArr.length - i3) {
            throw new StringIndexOutOfBoundsException(i2 + i3);
        }
        this.value = Arrays.copyOfRange(cArr, i2, i2 + i3);
    }

    public String(int[] iArr, int i2, int i3) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 <= 0) {
            if (i3 < 0) {
                throw new StringIndexOutOfBoundsException(i3);
            }
            if (i2 <= iArr.length) {
                this.value = "".value;
                return;
            }
        }
        if (i2 > iArr.length - i3) {
            throw new StringIndexOutOfBoundsException(i2 + i3);
        }
        int i4 = i2 + i3;
        int i5 = i3;
        for (int i6 = i2; i6 < i4; i6++) {
            int i7 = iArr[i6];
            if (!Character.isBmpCodePoint(i7)) {
                if (Character.isValidCodePoint(i7)) {
                    i5++;
                } else {
                    throw new IllegalArgumentException(Integer.toString(i7));
                }
            }
        }
        char[] cArr = new char[i5];
        int i8 = i2;
        int i9 = 0;
        while (i8 < i4) {
            int i10 = iArr[i8];
            if (Character.isBmpCodePoint(i10)) {
                cArr[i9] = (char) i10;
            } else {
                int i11 = i9;
                i9++;
                Character.toSurrogates(i10, cArr, i11);
            }
            i8++;
            i9++;
        }
        this.value = cArr;
    }

    @Deprecated
    public String(byte[] bArr, int i2, int i3, int i4) {
        checkBounds(bArr, i3, i4);
        char[] cArr = new char[i4];
        if (i2 == 0) {
            int i5 = i4;
            while (true) {
                int i6 = i5;
                i5--;
                if (i6 <= 0) {
                    break;
                } else {
                    cArr[i5] = (char) (bArr[i5 + i3] & 255);
                }
            }
        } else {
            int i7 = i2 << 8;
            int i8 = i4;
            while (true) {
                int i9 = i8;
                i8--;
                if (i9 <= 0) {
                    break;
                } else {
                    cArr[i8] = (char) (i7 | (bArr[i8 + i3] & 255));
                }
            }
        }
        this.value = cArr;
    }

    @Deprecated
    public String(byte[] bArr, int i2) {
        this(bArr, i2, 0, bArr.length);
    }

    private static void checkBounds(byte[] bArr, int i2, int i3) {
        if (i3 < 0) {
            throw new StringIndexOutOfBoundsException(i3);
        }
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i2 > bArr.length - i3) {
            throw new StringIndexOutOfBoundsException(i2 + i3);
        }
    }

    public String(byte[] bArr, int i2, int i3, String str) throws UnsupportedEncodingException {
        if (str == null) {
            throw new NullPointerException("charsetName");
        }
        checkBounds(bArr, i2, i3);
        this.value = StringCoding.decode(str, bArr, i2, i3);
    }

    public String(byte[] bArr, int i2, int i3, Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        checkBounds(bArr, i2, i3);
        this.value = StringCoding.decode(charset, bArr, i2, i3);
    }

    public String(byte[] bArr, String str) throws UnsupportedEncodingException {
        this(bArr, 0, bArr.length, str);
    }

    public String(byte[] bArr, Charset charset) {
        this(bArr, 0, bArr.length, charset);
    }

    public String(byte[] bArr, int i2, int i3) {
        checkBounds(bArr, i2, i3);
        this.value = StringCoding.decode(bArr, i2, i3);
    }

    public String(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public String(StringBuffer stringBuffer) {
        synchronized (stringBuffer) {
            this.value = Arrays.copyOf(stringBuffer.getValue(), stringBuffer.length());
        }
    }

    public String(StringBuilder sb) {
        this.value = Arrays.copyOf(sb.getValue(), sb.length());
    }

    String(char[] cArr, boolean z2) {
        this.value = cArr;
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.value.length;
    }

    public boolean isEmpty() {
        return this.value.length == 0;
    }

    @Override // java.lang.CharSequence
    public char charAt(int i2) {
        if (i2 < 0 || i2 >= this.value.length) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        return this.value[i2];
    }

    public int codePointAt(int i2) {
        if (i2 < 0 || i2 >= this.value.length) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        return Character.codePointAtImpl(this.value, i2, this.value.length);
    }

    public int codePointBefore(int i2) {
        int i3 = i2 - 1;
        if (i3 < 0 || i3 >= this.value.length) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        return Character.codePointBeforeImpl(this.value, i2, 0);
    }

    public int codePointCount(int i2, int i3) {
        if (i2 < 0 || i3 > this.value.length || i2 > i3) {
            throw new IndexOutOfBoundsException();
        }
        return Character.codePointCountImpl(this.value, i2, i3 - i2);
    }

    public int offsetByCodePoints(int i2, int i3) {
        if (i2 < 0 || i2 > this.value.length) {
            throw new IndexOutOfBoundsException();
        }
        return Character.offsetByCodePointsImpl(this.value, 0, this.value.length, i2, i3);
    }

    void getChars(char[] cArr, int i2) {
        System.arraycopy(this.value, 0, cArr, i2, this.value.length);
    }

    public void getChars(int i2, int i3, char[] cArr, int i4) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 > this.value.length) {
            throw new StringIndexOutOfBoundsException(i3);
        }
        if (i2 > i3) {
            throw new StringIndexOutOfBoundsException(i3 - i2);
        }
        System.arraycopy(this.value, i2, cArr, i4, i3 - i2);
    }

    @Deprecated
    public void getBytes(int i2, int i3, byte[] bArr, int i4) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 > this.value.length) {
            throw new StringIndexOutOfBoundsException(i3);
        }
        if (i2 > i3) {
            throw new StringIndexOutOfBoundsException(i3 - i2);
        }
        Objects.requireNonNull(bArr);
        int i5 = i4;
        int i6 = i2;
        char[] cArr = this.value;
        while (i6 < i3) {
            int i7 = i5;
            i5++;
            int i8 = i6;
            i6++;
            bArr[i7] = (byte) cArr[i8];
        }
    }

    public byte[] getBytes(String str) throws UnsupportedEncodingException {
        if (str == null) {
            throw new NullPointerException();
        }
        return StringCoding.encode(str, this.value, 0, this.value.length);
    }

    public byte[] getBytes(Charset charset) {
        if (charset == null) {
            throw new NullPointerException();
        }
        return StringCoding.encode(charset, this.value, 0, this.value.length);
    }

    public byte[] getBytes() {
        return StringCoding.encode(this.value, 0, this.value.length);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            int length = this.value.length;
            if (length == str.value.length) {
                char[] cArr = this.value;
                char[] cArr2 = str.value;
                int i2 = 0;
                while (true) {
                    int i3 = length;
                    length--;
                    if (i3 != 0) {
                        if (cArr[i2] != cArr2[i2]) {
                            return false;
                        }
                        i2++;
                    } else {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean contentEquals(StringBuffer stringBuffer) {
        return contentEquals((CharSequence) stringBuffer);
    }

    private boolean nonSyncContentEquals(AbstractStringBuilder abstractStringBuilder) {
        char[] cArr = this.value;
        char[] value = abstractStringBuilder.getValue();
        int length = cArr.length;
        if (length != abstractStringBuilder.length()) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (cArr[i2] != value[i2]) {
                return false;
            }
        }
        return true;
    }

    public boolean contentEquals(CharSequence charSequence) {
        boolean zNonSyncContentEquals;
        if (charSequence instanceof AbstractStringBuilder) {
            if (charSequence instanceof StringBuffer) {
                synchronized (charSequence) {
                    zNonSyncContentEquals = nonSyncContentEquals((AbstractStringBuilder) charSequence);
                }
                return zNonSyncContentEquals;
            }
            return nonSyncContentEquals((AbstractStringBuilder) charSequence);
        }
        if (charSequence instanceof String) {
            return equals(charSequence);
        }
        char[] cArr = this.value;
        int length = cArr.length;
        if (length != charSequence.length()) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (cArr[i2] != charSequence.charAt(i2)) {
                return false;
            }
        }
        return true;
    }

    public boolean equalsIgnoreCase(String str) {
        if (this == str) {
            return true;
        }
        return str != null && str.value.length == this.value.length && regionMatches(true, 0, str, 0, this.value.length);
    }

    @Override // java.lang.Comparable
    public int compareTo(String str) {
        int length = this.value.length;
        int length2 = str.value.length;
        int iMin = Math.min(length, length2);
        char[] cArr = this.value;
        char[] cArr2 = str.value;
        for (int i2 = 0; i2 < iMin; i2++) {
            char c2 = cArr[i2];
            char c3 = cArr2[i2];
            if (c2 != c3) {
                return c2 - c3;
            }
        }
        return length - length2;
    }

    /* loaded from: rt.jar:java/lang/String$CaseInsensitiveComparator.class */
    private static class CaseInsensitiveComparator implements Comparator<String>, Serializable {
        private static final long serialVersionUID = 8575799808933029326L;

        private CaseInsensitiveComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String str, String str2) {
            char upperCase;
            char upperCase2;
            char lowerCase;
            char lowerCase2;
            int length = str.length();
            int length2 = str2.length();
            int iMin = Math.min(length, length2);
            for (int i2 = 0; i2 < iMin; i2++) {
                char cCharAt = str.charAt(i2);
                char cCharAt2 = str2.charAt(i2);
                if (cCharAt != cCharAt2 && (upperCase = Character.toUpperCase(cCharAt)) != (upperCase2 = Character.toUpperCase(cCharAt2)) && (lowerCase = Character.toLowerCase(upperCase)) != (lowerCase2 = Character.toLowerCase(upperCase2))) {
                    return lowerCase - lowerCase2;
                }
            }
            return length - length2;
        }

        private Object readResolve() {
            return String.CASE_INSENSITIVE_ORDER;
        }
    }

    public int compareToIgnoreCase(String str) {
        return CASE_INSENSITIVE_ORDER.compare(this, str);
    }

    public boolean regionMatches(int i2, String str, int i3, int i4) {
        int i5;
        int i6;
        char[] cArr = this.value;
        int i7 = i2;
        char[] cArr2 = str.value;
        int i8 = i3;
        if (i3 < 0 || i2 < 0 || i2 > this.value.length - i4 || i3 > str.value.length - i4) {
            return false;
        }
        do {
            int i9 = i4;
            i4--;
            if (i9 <= 0) {
                return true;
            }
            i5 = i7;
            i7++;
            i6 = i8;
            i8++;
        } while (cArr[i5] == cArr2[i6]);
        return false;
    }

    public boolean regionMatches(boolean z2, int i2, String str, int i3, int i4) {
        char[] cArr = this.value;
        int i5 = i2;
        char[] cArr2 = str.value;
        int i6 = i3;
        if (i3 < 0 || i2 < 0 || i2 > this.value.length - i4 || i3 > str.value.length - i4) {
            return false;
        }
        while (true) {
            int i7 = i4;
            i4--;
            if (i7 > 0) {
                int i8 = i5;
                i5++;
                char c2 = cArr[i8];
                int i9 = i6;
                i6++;
                char c3 = cArr2[i9];
                if (c2 != c3) {
                    if (z2) {
                        char upperCase = Character.toUpperCase(c2);
                        char upperCase2 = Character.toUpperCase(c3);
                        if (upperCase != upperCase2 && Character.toLowerCase(upperCase) != Character.toLowerCase(upperCase2)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                return true;
            }
        }
    }

    public boolean startsWith(String str, int i2) {
        int i3;
        int i4;
        char[] cArr = this.value;
        int i5 = i2;
        char[] cArr2 = str.value;
        int i6 = 0;
        int length = str.value.length;
        if (i2 < 0 || i2 > this.value.length - length) {
            return false;
        }
        do {
            length--;
            if (length < 0) {
                return true;
            }
            i3 = i5;
            i5++;
            i4 = i6;
            i6++;
        } while (cArr[i3] == cArr2[i4]);
        return false;
    }

    public boolean startsWith(String str) {
        return startsWith(str, 0);
    }

    public boolean endsWith(String str) {
        return startsWith(str, this.value.length - str.value.length);
    }

    public int hashCode() {
        int i2 = this.hash;
        if (i2 == 0 && this.value.length > 0) {
            char[] cArr = this.value;
            for (int i3 = 0; i3 < this.value.length; i3++) {
                i2 = (31 * i2) + cArr[i3];
            }
            this.hash = i2;
        }
        return i2;
    }

    public int indexOf(int i2) {
        return indexOf(i2, 0);
    }

    public int indexOf(int i2, int i3) {
        int length = this.value.length;
        if (i3 < 0) {
            i3 = 0;
        } else if (i3 >= length) {
            return -1;
        }
        if (i2 < 65536) {
            char[] cArr = this.value;
            for (int i4 = i3; i4 < length; i4++) {
                if (cArr[i4] == i2) {
                    return i4;
                }
            }
            return -1;
        }
        return indexOfSupplementary(i2, i3);
    }

    private int indexOfSupplementary(int i2, int i3) {
        if (Character.isValidCodePoint(i2)) {
            char[] cArr = this.value;
            char cHighSurrogate = Character.highSurrogate(i2);
            char cLowSurrogate = Character.lowSurrogate(i2);
            int length = cArr.length - 1;
            for (int i4 = i3; i4 < length; i4++) {
                if (cArr[i4] == cHighSurrogate && cArr[i4 + 1] == cLowSurrogate) {
                    return i4;
                }
            }
            return -1;
        }
        return -1;
    }

    public int lastIndexOf(int i2) {
        return lastIndexOf(i2, this.value.length - 1);
    }

    public int lastIndexOf(int i2, int i3) {
        if (i2 < 65536) {
            char[] cArr = this.value;
            for (int iMin = Math.min(i3, cArr.length - 1); iMin >= 0; iMin--) {
                if (cArr[iMin] == i2) {
                    return iMin;
                }
            }
            return -1;
        }
        return lastIndexOfSupplementary(i2, i3);
    }

    private int lastIndexOfSupplementary(int i2, int i3) {
        if (Character.isValidCodePoint(i2)) {
            char[] cArr = this.value;
            char cHighSurrogate = Character.highSurrogate(i2);
            char cLowSurrogate = Character.lowSurrogate(i2);
            for (int iMin = Math.min(i3, cArr.length - 2); iMin >= 0; iMin--) {
                if (cArr[iMin] == cHighSurrogate && cArr[iMin + 1] == cLowSurrogate) {
                    return iMin;
                }
            }
            return -1;
        }
        return -1;
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    public int indexOf(String str, int i2) {
        return indexOf(this.value, 0, this.value.length, str.value, 0, str.value.length, i2);
    }

    static int indexOf(char[] cArr, int i2, int i3, String str, int i4) {
        return indexOf(cArr, i2, i3, str.value, 0, str.value.length, i4);
    }

    static int indexOf(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, int i6) {
        if (i6 >= i3) {
            if (i5 == 0) {
                return i3;
            }
            return -1;
        }
        if (i6 < 0) {
            i6 = 0;
        }
        if (i5 == 0) {
            return i6;
        }
        char c2 = cArr2[i4];
        int i7 = i2 + (i3 - i5);
        int i8 = i2 + i6;
        while (i8 <= i7) {
            if (cArr[i8] != c2) {
                do {
                    i8++;
                    if (i8 > i7) {
                        break;
                    }
                } while (cArr[i8] != c2);
            }
            if (i8 <= i7) {
                int i9 = i8 + 1;
                int i10 = (i9 + i5) - 1;
                for (int i11 = i4 + 1; i9 < i10 && cArr[i9] == cArr2[i11]; i11++) {
                    i9++;
                }
                if (i9 == i10) {
                    return i8 - i2;
                }
            }
            i8++;
        }
        return -1;
    }

    public int lastIndexOf(String str) {
        return lastIndexOf(str, this.value.length);
    }

    public int lastIndexOf(String str, int i2) {
        return lastIndexOf(this.value, 0, this.value.length, str.value, 0, str.value.length, i2);
    }

    static int lastIndexOf(char[] cArr, int i2, int i3, String str, int i4) {
        return lastIndexOf(cArr, i2, i3, str.value, 0, str.value.length, i4);
    }

    static int lastIndexOf(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5, int i6) {
        int i7 = i3 - i5;
        if (i6 < 0) {
            return -1;
        }
        if (i6 > i7) {
            i6 = i7;
        }
        if (i5 == 0) {
            return i6;
        }
        int i8 = (i4 + i5) - 1;
        char c2 = cArr2[i8];
        int i9 = (i2 + i5) - 1;
        int i10 = i9 + i6;
        while (true) {
            if (i10 >= i9 && cArr[i10] != c2) {
                i10--;
            } else {
                if (i10 < i9) {
                    return -1;
                }
                int i11 = i10 - 1;
                int i12 = i11 - (i5 - 1);
                int i13 = i8 - 1;
                while (i11 > i12) {
                    int i14 = i11;
                    i11--;
                    int i15 = i13;
                    i13--;
                    if (cArr[i14] != cArr2[i15]) {
                        i10--;
                    }
                }
                return (i12 - i2) + 1;
            }
        }
    }

    public String substring(int i2) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        int length = this.value.length - i2;
        if (length < 0) {
            throw new StringIndexOutOfBoundsException(length);
        }
        return i2 == 0 ? this : new String(this.value, i2, length);
    }

    public String substring(int i2, int i3) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 > this.value.length) {
            throw new StringIndexOutOfBoundsException(i3);
        }
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new StringIndexOutOfBoundsException(i4);
        }
        return (i2 == 0 && i3 == this.value.length) ? this : new String(this.value, i2, i4);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int i2, int i3) {
        return substring(i2, i3);
    }

    public String concat(String str) {
        int length = str.length();
        if (length == 0) {
            return this;
        }
        int length2 = this.value.length;
        char[] cArrCopyOf = Arrays.copyOf(this.value, length2 + length);
        str.getChars(cArrCopyOf, length2);
        return new String(cArrCopyOf, true);
    }

    public String replace(char c2, char c3) {
        if (c2 != c3) {
            int length = this.value.length;
            int i2 = -1;
            char[] cArr = this.value;
            do {
                i2++;
                if (i2 >= length) {
                    break;
                }
            } while (cArr[i2] != c2);
            if (i2 < length) {
                char[] cArr2 = new char[length];
                for (int i3 = 0; i3 < i2; i3++) {
                    cArr2[i3] = cArr[i3];
                }
                while (i2 < length) {
                    char c4 = cArr[i2];
                    cArr2[i2] = c4 == c2 ? c3 : c4;
                    i2++;
                }
                return new String(cArr2, true);
            }
        }
        return this;
    }

    public boolean matches(String str) {
        return Pattern.matches(str, this);
    }

    public boolean contains(CharSequence charSequence) {
        return indexOf(charSequence.toString()) > -1;
    }

    public String replaceFirst(String str, String str2) {
        return Pattern.compile(str).matcher(this).replaceFirst(str2);
    }

    public String replaceAll(String str, String str2) {
        return Pattern.compile(str).matcher(this).replaceAll(str2);
    }

    public String replace(CharSequence charSequence, CharSequence charSequence2) {
        return Pattern.compile(charSequence.toString(), 16).matcher(this).replaceAll(Matcher.quoteReplacement(charSequence2.toString()));
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0057 A[PHI: r8
  0x0057: PHI (r8v2 char) = (r8v1 char), (r8v3 char) binds: [B:15:0x0054, B:5:0x0018] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:6:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String[] split(java.lang.String r6, int r7) {
        /*
            Method dump skipped, instructions count: 326
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.String.split(java.lang.String, int):java.lang.String[]");
    }

    public String[] split(String str) {
        return split(str, 0);
    }

    public static String join(CharSequence charSequence, CharSequence... charSequenceArr) {
        Objects.requireNonNull(charSequence);
        Objects.requireNonNull(charSequenceArr);
        StringJoiner stringJoiner = new StringJoiner(charSequence);
        for (CharSequence charSequence2 : charSequenceArr) {
            stringJoiner.add(charSequence2);
        }
        return stringJoiner.toString();
    }

    public static String join(CharSequence charSequence, Iterable<? extends CharSequence> iterable) {
        Objects.requireNonNull(charSequence);
        Objects.requireNonNull(iterable);
        StringJoiner stringJoiner = new StringJoiner(charSequence);
        Iterator<? extends CharSequence> it = iterable.iterator();
        while (it.hasNext()) {
            stringJoiner.add(it.next());
        }
        return stringJoiner.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01a8 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String toLowerCase(java.util.Locale r8) {
        /*
            Method dump skipped, instructions count: 439
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.String.toLowerCase(java.util.Locale):java.lang.String");
    }

    public String toLowerCase() {
        return toLowerCase(Locale.getDefault());
    }

    public String toUpperCase(Locale locale) {
        int i2;
        int iCharCount;
        int iCharCount2;
        int upperCaseEx;
        char[] chars;
        if (locale == null) {
            throw new NullPointerException();
        }
        int length = this.value.length;
        int i3 = 0;
        while (true) {
            i2 = i3;
            if (i2 < length) {
                int iCodePointAt = this.value[i2];
                if (iCodePointAt >= 55296 && iCodePointAt <= 56319) {
                    iCodePointAt = codePointAt(i2);
                    iCharCount = Character.charCount(iCodePointAt);
                } else {
                    iCharCount = 1;
                }
                int upperCaseEx2 = Character.toUpperCaseEx(iCodePointAt);
                if (upperCaseEx2 == -1 || iCodePointAt != upperCaseEx2) {
                    break;
                }
                i3 = i2 + iCharCount;
            } else {
                return this;
            }
        }
        int chars2 = 0;
        char[] cArr = new char[length];
        System.arraycopy(this.value, 0, cArr, 0, i2);
        String language = locale.getLanguage();
        boolean z2 = language == "tr" || language == "az" || language == "lt";
        int i4 = i2;
        while (true) {
            int i5 = i4;
            if (i5 < length) {
                int iCodePointAt2 = this.value[i5];
                if (((char) iCodePointAt2) >= 55296 && ((char) iCodePointAt2) <= 56319) {
                    iCodePointAt2 = codePointAt(i5);
                    iCharCount2 = Character.charCount(iCodePointAt2);
                } else {
                    iCharCount2 = 1;
                }
                if (z2) {
                    upperCaseEx = ConditionalSpecialCasing.toUpperCaseEx(this, i5, locale);
                } else {
                    upperCaseEx = Character.toUpperCaseEx(iCodePointAt2);
                }
                if (upperCaseEx == -1 || upperCaseEx >= 65536) {
                    if (upperCaseEx == -1) {
                        if (z2) {
                            chars = ConditionalSpecialCasing.toUpperCaseCharArray(this, i5, locale);
                        } else {
                            chars = Character.toUpperCaseCharArray(iCodePointAt2);
                        }
                    } else if (iCharCount2 == 2) {
                        chars2 += Character.toChars(upperCaseEx, cArr, i5 + chars2) - iCharCount2;
                    } else {
                        chars = Character.toChars(upperCaseEx);
                    }
                    int length2 = chars.length;
                    if (length2 > iCharCount2) {
                        char[] cArr2 = new char[(cArr.length + length2) - iCharCount2];
                        System.arraycopy(cArr, 0, cArr2, 0, i5 + chars2);
                        cArr = cArr2;
                    }
                    for (int i6 = 0; i6 < length2; i6++) {
                        cArr[i5 + chars2 + i6] = chars[i6];
                    }
                    chars2 += length2 - iCharCount2;
                } else {
                    cArr[i5 + chars2] = (char) upperCaseEx;
                }
                i4 = i5 + iCharCount2;
            } else {
                return new String(cArr, 0, length + chars2);
            }
        }
    }

    public String toUpperCase() {
        return toUpperCase(Locale.getDefault());
    }

    public String trim() {
        int length = this.value.length;
        int i2 = 0;
        char[] cArr = this.value;
        while (i2 < length && cArr[i2] <= ' ') {
            i2++;
        }
        while (i2 < length && cArr[length - 1] <= ' ') {
            length--;
        }
        return (i2 > 0 || length < this.value.length) ? substring(i2, length) : this;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this;
    }

    public char[] toCharArray() {
        char[] cArr = new char[this.value.length];
        System.arraycopy(this.value, 0, cArr, 0, this.value.length);
        return cArr;
    }

    public static String format(String str, Object... objArr) {
        return new Formatter().format(str, objArr).toString();
    }

    public static String format(Locale locale, String str, Object... objArr) {
        return new Formatter(locale).format(str, objArr).toString();
    }

    public static String valueOf(Object obj) {
        return obj == null ? FXMLLoader.NULL_KEYWORD : obj.toString();
    }

    public static String valueOf(char[] cArr) {
        return new String(cArr);
    }

    public static String valueOf(char[] cArr, int i2, int i3) {
        return new String(cArr, i2, i3);
    }

    public static String copyValueOf(char[] cArr, int i2, int i3) {
        return new String(cArr, i2, i3);
    }

    public static String copyValueOf(char[] cArr) {
        return new String(cArr);
    }

    public static String valueOf(boolean z2) {
        return z2 ? "true" : "false";
    }

    public static String valueOf(char c2) {
        return new String(new char[]{c2}, true);
    }

    public static String valueOf(int i2) {
        return Integer.toString(i2);
    }

    public static String valueOf(long j2) {
        return Long.toString(j2);
    }

    public static String valueOf(float f2) {
        return Float.toString(f2);
    }

    public static String valueOf(double d2) {
        return Double.toString(d2);
    }
}
