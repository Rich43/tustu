package java.lang;

import java.util.Arrays;
import javafx.fxml.FXMLLoader;
import sun.misc.FloatingDecimal;

/* loaded from: rt.jar:java/lang/AbstractStringBuilder.class */
abstract class AbstractStringBuilder implements Appendable, CharSequence {
    char[] value;
    int count;
    private static final int MAX_ARRAY_SIZE = 2147483639;

    @Override // java.lang.CharSequence
    public abstract String toString();

    AbstractStringBuilder() {
    }

    AbstractStringBuilder(int i2) {
        this.value = new char[i2];
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.count;
    }

    public int capacity() {
        return this.value.length;
    }

    public void ensureCapacity(int i2) {
        if (i2 > 0) {
            ensureCapacityInternal(i2);
        }
    }

    private void ensureCapacityInternal(int i2) {
        if (i2 - this.value.length > 0) {
            this.value = Arrays.copyOf(this.value, newCapacity(i2));
        }
    }

    private int newCapacity(int i2) {
        int length = (this.value.length << 1) + 2;
        if (length - i2 < 0) {
            length = i2;
        }
        return (length <= 0 || MAX_ARRAY_SIZE - length < 0) ? hugeCapacity(i2) : length;
    }

    private int hugeCapacity(int i2) {
        if (Integer.MAX_VALUE - i2 < 0) {
            throw new OutOfMemoryError();
        }
        return i2 > MAX_ARRAY_SIZE ? i2 : MAX_ARRAY_SIZE;
    }

    public void trimToSize() {
        if (this.count < this.value.length) {
            this.value = Arrays.copyOf(this.value, this.count);
        }
    }

    public void setLength(int i2) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        ensureCapacityInternal(i2);
        if (this.count < i2) {
            Arrays.fill(this.value, this.count, i2, (char) 0);
        }
        this.count = i2;
    }

    @Override // java.lang.CharSequence
    public char charAt(int i2) {
        if (i2 < 0 || i2 >= this.count) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        return this.value[i2];
    }

    public int codePointAt(int i2) {
        if (i2 < 0 || i2 >= this.count) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        return Character.codePointAtImpl(this.value, i2, this.count);
    }

    public int codePointBefore(int i2) {
        int i3 = i2 - 1;
        if (i3 < 0 || i3 >= this.count) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        return Character.codePointBeforeImpl(this.value, i2, 0);
    }

    public int codePointCount(int i2, int i3) {
        if (i2 < 0 || i3 > this.count || i2 > i3) {
            throw new IndexOutOfBoundsException();
        }
        return Character.codePointCountImpl(this.value, i2, i3 - i2);
    }

    public int offsetByCodePoints(int i2, int i3) {
        if (i2 < 0 || i2 > this.count) {
            throw new IndexOutOfBoundsException();
        }
        return Character.offsetByCodePointsImpl(this.value, 0, this.count, i2, i3);
    }

    public void getChars(int i2, int i3, char[] cArr, int i4) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 < 0 || i3 > this.count) {
            throw new StringIndexOutOfBoundsException(i3);
        }
        if (i2 > i3) {
            throw new StringIndexOutOfBoundsException("srcBegin > srcEnd");
        }
        System.arraycopy(this.value, i2, cArr, i4, i3 - i2);
    }

    public void setCharAt(int i2, char c2) {
        if (i2 < 0 || i2 >= this.count) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        this.value[i2] = c2;
    }

    public AbstractStringBuilder append(Object obj) {
        return append(String.valueOf(obj));
    }

    public AbstractStringBuilder append(String str) {
        if (str == null) {
            return appendNull();
        }
        int length = str.length();
        ensureCapacityInternal(this.count + length);
        str.getChars(0, length, this.value, this.count);
        this.count += length;
        return this;
    }

    public AbstractStringBuilder append(StringBuffer stringBuffer) {
        if (stringBuffer == null) {
            return appendNull();
        }
        int length = stringBuffer.length();
        ensureCapacityInternal(this.count + length);
        stringBuffer.getChars(0, length, this.value, this.count);
        this.count += length;
        return this;
    }

    AbstractStringBuilder append(AbstractStringBuilder abstractStringBuilder) {
        if (abstractStringBuilder == null) {
            return appendNull();
        }
        int length = abstractStringBuilder.length();
        ensureCapacityInternal(this.count + length);
        abstractStringBuilder.getChars(0, length, this.value, this.count);
        this.count += length;
        return this;
    }

    @Override // java.lang.Appendable
    public AbstractStringBuilder append(CharSequence charSequence) {
        if (charSequence == null) {
            return appendNull();
        }
        if (charSequence instanceof String) {
            return append((String) charSequence);
        }
        if (charSequence instanceof AbstractStringBuilder) {
            return append((AbstractStringBuilder) charSequence);
        }
        return append(charSequence, 0, charSequence.length());
    }

    private AbstractStringBuilder appendNull() {
        int i2 = this.count;
        ensureCapacityInternal(i2 + 4);
        char[] cArr = this.value;
        int i3 = i2 + 1;
        cArr[i2] = 'n';
        int i4 = i3 + 1;
        cArr[i3] = 'u';
        int i5 = i4 + 1;
        cArr[i4] = 'l';
        cArr[i5] = 'l';
        this.count = i5 + 1;
        return this;
    }

    @Override // java.lang.Appendable
    public AbstractStringBuilder append(CharSequence charSequence, int i2, int i3) {
        if (charSequence == null) {
            charSequence = FXMLLoader.NULL_KEYWORD;
        }
        if (i2 < 0 || i2 > i3 || i3 > charSequence.length()) {
            throw new IndexOutOfBoundsException("start " + i2 + ", end " + i3 + ", s.length() " + charSequence.length());
        }
        int i4 = i3 - i2;
        ensureCapacityInternal(this.count + i4);
        int i5 = i2;
        int i6 = this.count;
        while (i5 < i3) {
            this.value[i6] = charSequence.charAt(i5);
            i5++;
            i6++;
        }
        this.count += i4;
        return this;
    }

    public AbstractStringBuilder append(char[] cArr) {
        int length = cArr.length;
        ensureCapacityInternal(this.count + length);
        System.arraycopy(cArr, 0, this.value, this.count, length);
        this.count += length;
        return this;
    }

    public AbstractStringBuilder append(char[] cArr, int i2, int i3) {
        if (i3 > 0) {
            ensureCapacityInternal(this.count + i3);
        }
        System.arraycopy(cArr, i2, this.value, this.count, i3);
        this.count += i3;
        return this;
    }

    public AbstractStringBuilder append(boolean z2) {
        if (z2) {
            ensureCapacityInternal(this.count + 4);
            char[] cArr = this.value;
            int i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = 't';
            char[] cArr2 = this.value;
            int i3 = this.count;
            this.count = i3 + 1;
            cArr2[i3] = 'r';
            char[] cArr3 = this.value;
            int i4 = this.count;
            this.count = i4 + 1;
            cArr3[i4] = 'u';
            char[] cArr4 = this.value;
            int i5 = this.count;
            this.count = i5 + 1;
            cArr4[i5] = 'e';
        } else {
            ensureCapacityInternal(this.count + 5);
            char[] cArr5 = this.value;
            int i6 = this.count;
            this.count = i6 + 1;
            cArr5[i6] = 'f';
            char[] cArr6 = this.value;
            int i7 = this.count;
            this.count = i7 + 1;
            cArr6[i7] = 'a';
            char[] cArr7 = this.value;
            int i8 = this.count;
            this.count = i8 + 1;
            cArr7[i8] = 'l';
            char[] cArr8 = this.value;
            int i9 = this.count;
            this.count = i9 + 1;
            cArr8[i9] = 's';
            char[] cArr9 = this.value;
            int i10 = this.count;
            this.count = i10 + 1;
            cArr9[i10] = 'e';
        }
        return this;
    }

    @Override // java.lang.Appendable
    public AbstractStringBuilder append(char c2) {
        ensureCapacityInternal(this.count + 1);
        char[] cArr = this.value;
        int i2 = this.count;
        this.count = i2 + 1;
        cArr[i2] = c2;
        return this;
    }

    public AbstractStringBuilder append(int i2) {
        if (i2 == Integer.MIN_VALUE) {
            append("-2147483648");
            return this;
        }
        int iStringSize = this.count + (i2 < 0 ? Integer.stringSize(-i2) + 1 : Integer.stringSize(i2));
        ensureCapacityInternal(iStringSize);
        Integer.getChars(i2, iStringSize, this.value);
        this.count = iStringSize;
        return this;
    }

    public AbstractStringBuilder append(long j2) {
        if (j2 == Long.MIN_VALUE) {
            append("-9223372036854775808");
            return this;
        }
        int iStringSize = this.count + (j2 < 0 ? Long.stringSize(-j2) + 1 : Long.stringSize(j2));
        ensureCapacityInternal(iStringSize);
        Long.getChars(j2, iStringSize, this.value);
        this.count = iStringSize;
        return this;
    }

    public AbstractStringBuilder append(float f2) {
        FloatingDecimal.appendTo(f2, (Appendable) this);
        return this;
    }

    public AbstractStringBuilder append(double d2) {
        FloatingDecimal.appendTo(d2, this);
        return this;
    }

    public AbstractStringBuilder delete(int i2, int i3) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 > this.count) {
            i3 = this.count;
        }
        if (i2 > i3) {
            throw new StringIndexOutOfBoundsException();
        }
        int i4 = i3 - i2;
        if (i4 > 0) {
            System.arraycopy(this.value, i2 + i4, this.value, i2, this.count - i3);
            this.count -= i4;
        }
        return this;
    }

    public AbstractStringBuilder appendCodePoint(int i2) {
        int i3 = this.count;
        if (Character.isBmpCodePoint(i2)) {
            ensureCapacityInternal(i3 + 1);
            this.value[i3] = (char) i2;
            this.count = i3 + 1;
        } else if (Character.isValidCodePoint(i2)) {
            ensureCapacityInternal(i3 + 2);
            Character.toSurrogates(i2, this.value, i3);
            this.count = i3 + 2;
        } else {
            throw new IllegalArgumentException();
        }
        return this;
    }

    public AbstractStringBuilder deleteCharAt(int i2) {
        if (i2 < 0 || i2 >= this.count) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        System.arraycopy(this.value, i2 + 1, this.value, i2, (this.count - i2) - 1);
        this.count--;
        return this;
    }

    public AbstractStringBuilder replace(int i2, int i3, String str) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i2 > this.count) {
            throw new StringIndexOutOfBoundsException("start > length()");
        }
        if (i2 > i3) {
            throw new StringIndexOutOfBoundsException("start > end");
        }
        if (i3 > this.count) {
            i3 = this.count;
        }
        int length = str.length();
        int i4 = (this.count + length) - (i3 - i2);
        ensureCapacityInternal(i4);
        System.arraycopy(this.value, i3, this.value, i2 + length, this.count - i3);
        str.getChars(this.value, i2);
        this.count = i4;
        return this;
    }

    public String substring(int i2) {
        return substring(i2, this.count);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int i2, int i3) {
        return substring(i2, i3);
    }

    public String substring(int i2, int i3) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 > this.count) {
            throw new StringIndexOutOfBoundsException(i3);
        }
        if (i2 > i3) {
            throw new StringIndexOutOfBoundsException(i3 - i2);
        }
        return new String(this.value, i2, i3 - i2);
    }

    public AbstractStringBuilder insert(int i2, char[] cArr, int i3, int i4) {
        if (i2 < 0 || i2 > length()) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 < 0 || i4 < 0 || i3 > cArr.length - i4) {
            throw new StringIndexOutOfBoundsException("offset " + i3 + ", len " + i4 + ", str.length " + cArr.length);
        }
        ensureCapacityInternal(this.count + i4);
        System.arraycopy(this.value, i2, this.value, i2 + i4, this.count - i2);
        System.arraycopy(cArr, i3, this.value, i2, i4);
        this.count += i4;
        return this;
    }

    public AbstractStringBuilder insert(int i2, Object obj) {
        return insert(i2, String.valueOf(obj));
    }

    public AbstractStringBuilder insert(int i2, String str) {
        if (i2 < 0 || i2 > length()) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (str == null) {
            str = FXMLLoader.NULL_KEYWORD;
        }
        int length = str.length();
        ensureCapacityInternal(this.count + length);
        System.arraycopy(this.value, i2, this.value, i2 + length, this.count - i2);
        str.getChars(this.value, i2);
        this.count += length;
        return this;
    }

    public AbstractStringBuilder insert(int i2, char[] cArr) {
        if (i2 < 0 || i2 > length()) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        int length = cArr.length;
        ensureCapacityInternal(this.count + length);
        System.arraycopy(this.value, i2, this.value, i2 + length, this.count - i2);
        System.arraycopy(cArr, 0, this.value, i2, length);
        this.count += length;
        return this;
    }

    public AbstractStringBuilder insert(int i2, CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = FXMLLoader.NULL_KEYWORD;
        }
        if (charSequence instanceof String) {
            return insert(i2, (String) charSequence);
        }
        return insert(i2, charSequence, 0, charSequence.length());
    }

    public AbstractStringBuilder insert(int i2, CharSequence charSequence, int i3, int i4) {
        if (charSequence == null) {
            charSequence = FXMLLoader.NULL_KEYWORD;
        }
        if (i2 < 0 || i2 > length()) {
            throw new IndexOutOfBoundsException("dstOffset " + i2);
        }
        if (i3 < 0 || i4 < 0 || i3 > i4 || i4 > charSequence.length()) {
            throw new IndexOutOfBoundsException("start " + i3 + ", end " + i4 + ", s.length() " + charSequence.length());
        }
        int i5 = i4 - i3;
        ensureCapacityInternal(this.count + i5);
        System.arraycopy(this.value, i2, this.value, i2 + i5, this.count - i2);
        for (int i6 = i3; i6 < i4; i6++) {
            int i7 = i2;
            i2++;
            this.value[i7] = charSequence.charAt(i6);
        }
        this.count += i5;
        return this;
    }

    public AbstractStringBuilder insert(int i2, boolean z2) {
        return insert(i2, String.valueOf(z2));
    }

    public AbstractStringBuilder insert(int i2, char c2) {
        ensureCapacityInternal(this.count + 1);
        System.arraycopy(this.value, i2, this.value, i2 + 1, this.count - i2);
        this.value[i2] = c2;
        this.count++;
        return this;
    }

    public AbstractStringBuilder insert(int i2, int i3) {
        return insert(i2, String.valueOf(i3));
    }

    public AbstractStringBuilder insert(int i2, long j2) {
        return insert(i2, String.valueOf(j2));
    }

    public AbstractStringBuilder insert(int i2, float f2) {
        return insert(i2, String.valueOf(f2));
    }

    public AbstractStringBuilder insert(int i2, double d2) {
        return insert(i2, String.valueOf(d2));
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    public int indexOf(String str, int i2) {
        return String.indexOf(this.value, 0, this.count, str, i2);
    }

    public int lastIndexOf(String str) {
        return lastIndexOf(str, this.count);
    }

    public int lastIndexOf(String str, int i2) {
        return String.lastIndexOf(this.value, 0, this.count, str, i2);
    }

    public AbstractStringBuilder reverse() {
        boolean z2 = false;
        int i2 = this.count - 1;
        for (int i3 = (i2 - 1) >> 1; i3 >= 0; i3--) {
            int i4 = i2 - i3;
            char c2 = this.value[i3];
            char c3 = this.value[i4];
            this.value[i3] = c3;
            this.value[i4] = c2;
            if (Character.isSurrogate(c2) || Character.isSurrogate(c3)) {
                z2 = true;
            }
        }
        if (z2) {
            reverseAllValidSurrogatePairs();
        }
        return this;
    }

    private void reverseAllValidSurrogatePairs() {
        int i2 = 0;
        while (i2 < this.count - 1) {
            char c2 = this.value[i2];
            if (Character.isLowSurrogate(c2)) {
                char c3 = this.value[i2 + 1];
                if (Character.isHighSurrogate(c3)) {
                    int i3 = i2;
                    i2++;
                    this.value[i3] = c3;
                    this.value[i2] = c2;
                }
            }
            i2++;
        }
    }

    final char[] getValue() {
        return this.value;
    }
}
