package java.nio;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/nio/CharBuffer.class */
public abstract class CharBuffer extends Buffer implements Comparable<CharBuffer>, Appendable, CharSequence, Readable {
    final char[] hb;
    final int offset;
    boolean isReadOnly;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract CharBuffer slice();

    public abstract CharBuffer duplicate();

    public abstract CharBuffer asReadOnlyBuffer();

    public abstract char get();

    public abstract CharBuffer put(char c2);

    public abstract char get(int i2);

    abstract char getUnchecked(int i2);

    public abstract CharBuffer put(int i2, char c2);

    public abstract CharBuffer compact();

    @Override // java.nio.Buffer
    public abstract boolean isDirect();

    abstract String toString(int i2, int i3);

    @Override // java.lang.CharSequence
    public abstract CharBuffer subSequence(int i2, int i3);

    public abstract ByteOrder order();

    static {
        $assertionsDisabled = !CharBuffer.class.desiredAssertionStatus();
    }

    CharBuffer(int i2, int i3, int i4, int i5, char[] cArr, int i6) {
        super(i2, i3, i4, i5);
        this.hb = cArr;
        this.offset = i6;
    }

    CharBuffer(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, null, 0);
    }

    public static CharBuffer allocate(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        return new HeapCharBuffer(i2, i2);
    }

    public static CharBuffer wrap(char[] cArr, int i2, int i3) {
        try {
            return new HeapCharBuffer(cArr, i2, i3);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static CharBuffer wrap(char[] cArr) {
        return wrap(cArr, 0, cArr.length);
    }

    @Override // java.lang.Readable
    public int read(CharBuffer charBuffer) throws IOException {
        int iLimit = limit();
        int iPosition = position();
        int i2 = iLimit - iPosition;
        if (!$assertionsDisabled && i2 < 0) {
            throw new AssertionError();
        }
        if (i2 <= 0) {
            return -1;
        }
        int iRemaining = charBuffer.remaining();
        if (!$assertionsDisabled && iRemaining < 0) {
            throw new AssertionError();
        }
        if (iRemaining <= 0) {
            return 0;
        }
        int iMin = Math.min(i2, iRemaining);
        if (iRemaining < i2) {
            limit(iPosition + iMin);
        }
        if (iMin > 0) {
            try {
                charBuffer.put(this);
            } finally {
                limit(iLimit);
            }
        }
        return iMin;
    }

    public static CharBuffer wrap(CharSequence charSequence, int i2, int i3) {
        try {
            return new StringCharBuffer(charSequence, i2, i3);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static CharBuffer wrap(CharSequence charSequence) {
        return wrap(charSequence, 0, charSequence.length());
    }

    public CharBuffer get(char[] cArr, int i2, int i3) {
        checkBounds(i2, i3, cArr.length);
        if (i3 > remaining()) {
            throw new BufferUnderflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            cArr[i5] = get();
        }
        return this;
    }

    public CharBuffer get(char[] cArr) {
        return get(cArr, 0, cArr.length);
    }

    public CharBuffer put(CharBuffer charBuffer) {
        if (charBuffer == this) {
            throw new IllegalArgumentException();
        }
        if (isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        int iRemaining = charBuffer.remaining();
        if (iRemaining > remaining()) {
            throw new BufferOverflowException();
        }
        for (int i2 = 0; i2 < iRemaining; i2++) {
            put(charBuffer.get());
        }
        return this;
    }

    public CharBuffer put(char[] cArr, int i2, int i3) {
        checkBounds(i2, i3, cArr.length);
        if (i3 > remaining()) {
            throw new BufferOverflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            put(cArr[i5]);
        }
        return this;
    }

    public final CharBuffer put(char[] cArr) {
        return put(cArr, 0, cArr.length);
    }

    public CharBuffer put(String str, int i2, int i3) {
        checkBounds(i2, i3 - i2, str.length());
        if (isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (i3 - i2 > remaining()) {
            throw new BufferOverflowException();
        }
        for (int i4 = i2; i4 < i3; i4++) {
            put(str.charAt(i4));
        }
        return this;
    }

    public final CharBuffer put(String str) {
        return put(str, 0, str.length());
    }

    @Override // java.nio.Buffer
    public final boolean hasArray() {
        return (this.hb == null || this.isReadOnly) ? false : true;
    }

    @Override // java.nio.Buffer
    public final char[] array() {
        if (this.hb == null) {
            throw new UnsupportedOperationException();
        }
        if (this.isReadOnly) {
            throw new ReadOnlyBufferException();
        }
        return this.hb;
    }

    @Override // java.nio.Buffer
    public final int arrayOffset() {
        if (this.hb == null) {
            throw new UnsupportedOperationException();
        }
        if (this.isReadOnly) {
            throw new ReadOnlyBufferException();
        }
        return this.offset;
    }

    public int hashCode() {
        int i2 = 1;
        int iPosition = position();
        for (int iLimit = limit() - 1; iLimit >= iPosition; iLimit--) {
            i2 = (31 * i2) + get(iLimit);
        }
        return i2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CharBuffer)) {
            return false;
        }
        CharBuffer charBuffer = (CharBuffer) obj;
        int iPosition = position();
        int iLimit = limit();
        int iPosition2 = charBuffer.position();
        int iLimit2 = charBuffer.limit();
        int i2 = iLimit - iPosition;
        int i3 = iLimit2 - iPosition2;
        if (i2 < 0 || i2 != i3) {
            return false;
        }
        int i4 = iLimit - 1;
        int i5 = iLimit2 - 1;
        while (i4 >= iPosition) {
            if (equals(get(i4), charBuffer.get(i5))) {
                i4--;
                i5--;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(char c2, char c3) {
        return c2 == c3;
    }

    @Override // java.lang.Comparable
    public int compareTo(CharBuffer charBuffer) {
        int iPosition = position();
        int iLimit = limit() - iPosition;
        int iPosition2 = charBuffer.position();
        int iLimit2 = charBuffer.limit() - iPosition2;
        if (Math.min(iLimit, iLimit2) < 0) {
            return -1;
        }
        int iMin = iPosition + Math.min(iLimit, iLimit2);
        int i2 = iPosition;
        int i3 = iPosition2;
        while (i2 < iMin) {
            int iCompare = compare(get(i2), charBuffer.get(i3));
            if (iCompare == 0) {
                i2++;
                i3++;
            } else {
                return iCompare;
            }
        }
        return iLimit - iLimit2;
    }

    private static int compare(char c2, char c3) {
        return Character.compare(c2, c3);
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return toString(position(), limit());
    }

    @Override // java.lang.CharSequence
    public final int length() {
        return remaining();
    }

    @Override // java.lang.CharSequence
    public final char charAt(int i2) {
        return get(position() + checkIndex(i2, 1));
    }

    @Override // java.lang.Appendable
    public CharBuffer append(CharSequence charSequence) {
        if (charSequence == null) {
            return put(FXMLLoader.NULL_KEYWORD);
        }
        return put(charSequence.toString());
    }

    @Override // java.lang.Appendable
    public CharBuffer append(CharSequence charSequence, int i2, int i3) {
        return put((charSequence == null ? FXMLLoader.NULL_KEYWORD : charSequence).subSequence(i2, i3).toString());
    }

    @Override // java.lang.Appendable
    public CharBuffer append(char c2) {
        return put(c2);
    }

    @Override // java.lang.CharSequence
    public IntStream chars() {
        return StreamSupport.intStream(() -> {
            return new CharBufferSpliterator(this);
        }, 16464, false);
    }
}
