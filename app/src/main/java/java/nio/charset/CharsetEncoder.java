package java.nio.charset;

import com.sun.jmx.defaults.ServiceName;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

/* loaded from: rt.jar:java/nio/charset/CharsetEncoder.class */
public abstract class CharsetEncoder {
    private final Charset charset;
    private final float averageBytesPerChar;
    private final float maxBytesPerChar;
    private byte[] replacement;
    private CodingErrorAction malformedInputAction;
    private CodingErrorAction unmappableCharacterAction;
    private static final int ST_RESET = 0;
    private static final int ST_CODING = 1;
    private static final int ST_END = 2;
    private static final int ST_FLUSHED = 3;
    private int state;
    private static String[] stateNames;
    private WeakReference<CharsetDecoder> cachedDecoder;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer);

    static {
        $assertionsDisabled = !CharsetEncoder.class.desiredAssertionStatus();
        stateNames = new String[]{"RESET", "CODING", "CODING_END", "FLUSHED"};
    }

    protected CharsetEncoder(Charset charset, float f2, float f3, byte[] bArr) {
        this.malformedInputAction = CodingErrorAction.REPORT;
        this.unmappableCharacterAction = CodingErrorAction.REPORT;
        this.state = 0;
        this.cachedDecoder = null;
        this.charset = charset;
        if (f2 <= 0.0f) {
            throw new IllegalArgumentException("Non-positive averageBytesPerChar");
        }
        if (f3 <= 0.0f) {
            throw new IllegalArgumentException("Non-positive maxBytesPerChar");
        }
        if (!Charset.atBugLevel(ServiceName.JMX_SPEC_VERSION) && f2 > f3) {
            throw new IllegalArgumentException("averageBytesPerChar exceeds maxBytesPerChar");
        }
        this.replacement = bArr;
        this.averageBytesPerChar = f2;
        this.maxBytesPerChar = f3;
        replaceWith(bArr);
    }

    protected CharsetEncoder(Charset charset, float f2, float f3) {
        this(charset, f2, f3, new byte[]{63});
    }

    public final Charset charset() {
        return this.charset;
    }

    public final byte[] replacement() {
        return Arrays.copyOf(this.replacement, this.replacement.length);
    }

    public final CharsetEncoder replaceWith(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("Null replacement");
        }
        int length = bArr.length;
        if (length == 0) {
            throw new IllegalArgumentException("Empty replacement");
        }
        if (length > this.maxBytesPerChar) {
            throw new IllegalArgumentException("Replacement too long");
        }
        if (!isLegalReplacement(bArr)) {
            throw new IllegalArgumentException("Illegal replacement");
        }
        this.replacement = Arrays.copyOf(bArr, bArr.length);
        implReplaceWith(this.replacement);
        return this;
    }

    protected void implReplaceWith(byte[] bArr) {
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0017  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isLegalReplacement(byte[] r6) {
        /*
            r5 = this;
            r0 = r5
            java.lang.ref.WeakReference<java.nio.charset.CharsetDecoder> r0 = r0.cachedDecoder
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = r7
            if (r0 == 0) goto L17
            r0 = r7
            java.lang.Object r0 = r0.get()
            java.nio.charset.CharsetDecoder r0 = (java.nio.charset.CharsetDecoder) r0
            r1 = r0
            r8 = r1
            if (r0 != 0) goto L3e
        L17:
            r0 = r5
            java.nio.charset.Charset r0 = r0.charset()
            java.nio.charset.CharsetDecoder r0 = r0.newDecoder()
            r8 = r0
            r0 = r8
            java.nio.charset.CodingErrorAction r1 = java.nio.charset.CodingErrorAction.REPORT
            java.nio.charset.CharsetDecoder r0 = r0.onMalformedInput(r1)
            r0 = r8
            java.nio.charset.CodingErrorAction r1 = java.nio.charset.CodingErrorAction.REPORT
            java.nio.charset.CharsetDecoder r0 = r0.onUnmappableCharacter(r1)
            r0 = r5
            java.lang.ref.WeakReference r1 = new java.lang.ref.WeakReference
            r2 = r1
            r3 = r8
            r2.<init>(r3)
            r0.cachedDecoder = r1
            goto L43
        L3e:
            r0 = r8
            java.nio.charset.CharsetDecoder r0 = r0.reset()
        L43:
            r0 = r6
            java.nio.ByteBuffer r0 = java.nio.ByteBuffer.wrap(r0)
            r9 = r0
            r0 = r9
            int r0 = r0.remaining()
            float r0 = (float) r0
            r1 = r8
            float r1 = r1.maxCharsPerByte()
            float r0 = r0 * r1
            int r0 = (int) r0
            java.nio.CharBuffer r0 = java.nio.CharBuffer.allocate(r0)
            r10 = r0
            r0 = r8
            r1 = r9
            r2 = r10
            r3 = 1
            java.nio.charset.CoderResult r0 = r0.decode(r1, r2, r3)
            r11 = r0
            r0 = r11
            boolean r0 = r0.isError()
            if (r0 != 0) goto L71
            r0 = 1
            goto L72
        L71:
            r0 = 0
        L72:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.charset.CharsetEncoder.isLegalReplacement(byte[]):boolean");
    }

    public CodingErrorAction malformedInputAction() {
        return this.malformedInputAction;
    }

    public final CharsetEncoder onMalformedInput(CodingErrorAction codingErrorAction) {
        if (codingErrorAction == null) {
            throw new IllegalArgumentException("Null action");
        }
        this.malformedInputAction = codingErrorAction;
        implOnMalformedInput(codingErrorAction);
        return this;
    }

    protected void implOnMalformedInput(CodingErrorAction codingErrorAction) {
    }

    public CodingErrorAction unmappableCharacterAction() {
        return this.unmappableCharacterAction;
    }

    public final CharsetEncoder onUnmappableCharacter(CodingErrorAction codingErrorAction) {
        if (codingErrorAction == null) {
            throw new IllegalArgumentException("Null action");
        }
        this.unmappableCharacterAction = codingErrorAction;
        implOnUnmappableCharacter(codingErrorAction);
        return this;
    }

    protected void implOnUnmappableCharacter(CodingErrorAction codingErrorAction) {
    }

    public final float averageBytesPerChar() {
        return this.averageBytesPerChar;
    }

    public final float maxBytesPerChar() {
        return this.maxBytesPerChar;
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0085, code lost:
    
        return r9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.nio.charset.CoderResult encode(java.nio.CharBuffer r5, java.nio.ByteBuffer r6, boolean r7) {
        /*
            Method dump skipped, instructions count: 285
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.charset.CharsetEncoder.encode(java.nio.CharBuffer, java.nio.ByteBuffer, boolean):java.nio.charset.CoderResult");
    }

    public final CoderResult flush(ByteBuffer byteBuffer) {
        if (this.state == 2) {
            CoderResult coderResultImplFlush = implFlush(byteBuffer);
            if (coderResultImplFlush.isUnderflow()) {
                this.state = 3;
            }
            return coderResultImplFlush;
        }
        if (this.state != 3) {
            throwIllegalStateException(this.state, 3);
        }
        return CoderResult.UNDERFLOW;
    }

    protected CoderResult implFlush(ByteBuffer byteBuffer) {
        return CoderResult.UNDERFLOW;
    }

    public final CharsetEncoder reset() {
        implReset();
        this.state = 0;
        return this;
    }

    protected void implReset() {
    }

    public final ByteBuffer encode(CharBuffer charBuffer) throws CharacterCodingException {
        int iRemaining = (int) (charBuffer.remaining() * averageBytesPerChar());
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(iRemaining);
        if (iRemaining == 0 && charBuffer.remaining() == 0) {
            return byteBufferAllocate;
        }
        reset();
        while (true) {
            CoderResult coderResultEncode = charBuffer.hasRemaining() ? encode(charBuffer, byteBufferAllocate, true) : CoderResult.UNDERFLOW;
            if (coderResultEncode.isUnderflow()) {
                coderResultEncode = flush(byteBufferAllocate);
            }
            if (!coderResultEncode.isUnderflow()) {
                if (coderResultEncode.isOverflow()) {
                    iRemaining = (2 * iRemaining) + 1;
                    ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(iRemaining);
                    byteBufferAllocate.flip();
                    byteBufferAllocate2.put(byteBufferAllocate);
                    byteBufferAllocate = byteBufferAllocate2;
                } else {
                    coderResultEncode.throwException();
                }
            } else {
                byteBufferAllocate.flip();
                return byteBufferAllocate;
            }
        }
    }

    private boolean canEncode(CharBuffer charBuffer) {
        if (this.state == 3) {
            reset();
        } else if (this.state != 0) {
            throwIllegalStateException(this.state, 1);
        }
        CodingErrorAction codingErrorActionMalformedInputAction = malformedInputAction();
        CodingErrorAction codingErrorActionUnmappableCharacterAction = unmappableCharacterAction();
        try {
            onMalformedInput(CodingErrorAction.REPORT);
            onUnmappableCharacter(CodingErrorAction.REPORT);
            encode(charBuffer);
            onMalformedInput(codingErrorActionMalformedInputAction);
            onUnmappableCharacter(codingErrorActionUnmappableCharacterAction);
            reset();
            return true;
        } catch (CharacterCodingException e2) {
            onMalformedInput(codingErrorActionMalformedInputAction);
            onUnmappableCharacter(codingErrorActionUnmappableCharacterAction);
            reset();
            return false;
        } catch (Throwable th) {
            onMalformedInput(codingErrorActionMalformedInputAction);
            onUnmappableCharacter(codingErrorActionUnmappableCharacterAction);
            reset();
            throw th;
        }
    }

    public boolean canEncode(char c2) {
        CharBuffer charBufferAllocate = CharBuffer.allocate(1);
        charBufferAllocate.put(c2);
        charBufferAllocate.flip();
        return canEncode(charBufferAllocate);
    }

    public boolean canEncode(CharSequence charSequence) {
        CharBuffer charBufferWrap;
        if (charSequence instanceof CharBuffer) {
            charBufferWrap = ((CharBuffer) charSequence).duplicate();
        } else {
            charBufferWrap = CharBuffer.wrap(charSequence.toString());
        }
        return canEncode(charBufferWrap);
    }

    private void throwIllegalStateException(int i2, int i3) {
        throw new IllegalStateException("Current state = " + stateNames[i2] + ", new state = " + stateNames[i3]);
    }
}
