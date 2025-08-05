package java.nio.charset;

import com.sun.jmx.defaults.ServiceName;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/* loaded from: rt.jar:java/nio/charset/CharsetDecoder.class */
public abstract class CharsetDecoder {
    private final Charset charset;
    private final float averageCharsPerByte;
    private final float maxCharsPerByte;
    private String replacement;
    private CodingErrorAction malformedInputAction;
    private CodingErrorAction unmappableCharacterAction;
    private static final int ST_RESET = 0;
    private static final int ST_CODING = 1;
    private static final int ST_END = 2;
    private static final int ST_FLUSHED = 3;
    private int state;
    private static String[] stateNames;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer);

    static {
        $assertionsDisabled = !CharsetDecoder.class.desiredAssertionStatus();
        stateNames = new String[]{"RESET", "CODING", "CODING_END", "FLUSHED"};
    }

    private CharsetDecoder(Charset charset, float f2, float f3, String str) {
        this.malformedInputAction = CodingErrorAction.REPORT;
        this.unmappableCharacterAction = CodingErrorAction.REPORT;
        this.state = 0;
        this.charset = charset;
        if (f2 <= 0.0f) {
            throw new IllegalArgumentException("Non-positive averageCharsPerByte");
        }
        if (f3 <= 0.0f) {
            throw new IllegalArgumentException("Non-positive maxCharsPerByte");
        }
        if (!Charset.atBugLevel(ServiceName.JMX_SPEC_VERSION) && f2 > f3) {
            throw new IllegalArgumentException("averageCharsPerByte exceeds maxCharsPerByte");
        }
        this.replacement = str;
        this.averageCharsPerByte = f2;
        this.maxCharsPerByte = f3;
        replaceWith(str);
    }

    protected CharsetDecoder(Charset charset, float f2, float f3) {
        this(charset, f2, f3, "�");
    }

    public final Charset charset() {
        return this.charset;
    }

    public final String replacement() {
        return this.replacement;
    }

    public final CharsetDecoder replaceWith(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Null replacement");
        }
        int length = str.length();
        if (length == 0) {
            throw new IllegalArgumentException("Empty replacement");
        }
        if (length > this.maxCharsPerByte) {
            throw new IllegalArgumentException("Replacement too long");
        }
        this.replacement = str;
        implReplaceWith(this.replacement);
        return this;
    }

    protected void implReplaceWith(String str) {
    }

    public CodingErrorAction malformedInputAction() {
        return this.malformedInputAction;
    }

    public final CharsetDecoder onMalformedInput(CodingErrorAction codingErrorAction) {
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

    public final CharsetDecoder onUnmappableCharacter(CodingErrorAction codingErrorAction) {
        if (codingErrorAction == null) {
            throw new IllegalArgumentException("Null action");
        }
        this.unmappableCharacterAction = codingErrorAction;
        implOnUnmappableCharacter(codingErrorAction);
        return this;
    }

    protected void implOnUnmappableCharacter(CodingErrorAction codingErrorAction) {
    }

    public final float averageCharsPerByte() {
        return this.averageCharsPerByte;
    }

    public final float maxCharsPerByte() {
        return this.maxCharsPerByte;
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0085, code lost:
    
        return r9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.nio.charset.CoderResult decode(java.nio.ByteBuffer r5, java.nio.CharBuffer r6, boolean r7) {
        /*
            Method dump skipped, instructions count: 287
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.charset.CharsetDecoder.decode(java.nio.ByteBuffer, java.nio.CharBuffer, boolean):java.nio.charset.CoderResult");
    }

    public final CoderResult flush(CharBuffer charBuffer) {
        if (this.state == 2) {
            CoderResult coderResultImplFlush = implFlush(charBuffer);
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

    protected CoderResult implFlush(CharBuffer charBuffer) {
        return CoderResult.UNDERFLOW;
    }

    public final CharsetDecoder reset() {
        implReset();
        this.state = 0;
        return this;
    }

    protected void implReset() {
    }

    public final CharBuffer decode(ByteBuffer byteBuffer) throws CharacterCodingException {
        int iRemaining = (int) (byteBuffer.remaining() * averageCharsPerByte());
        CharBuffer charBufferAllocate = CharBuffer.allocate(iRemaining);
        if (iRemaining == 0 && byteBuffer.remaining() == 0) {
            return charBufferAllocate;
        }
        reset();
        while (true) {
            CoderResult coderResultDecode = byteBuffer.hasRemaining() ? decode(byteBuffer, charBufferAllocate, true) : CoderResult.UNDERFLOW;
            if (coderResultDecode.isUnderflow()) {
                coderResultDecode = flush(charBufferAllocate);
            }
            if (!coderResultDecode.isUnderflow()) {
                if (coderResultDecode.isOverflow()) {
                    iRemaining = (2 * iRemaining) + 1;
                    CharBuffer charBufferAllocate2 = CharBuffer.allocate(iRemaining);
                    charBufferAllocate.flip();
                    charBufferAllocate2.put(charBufferAllocate);
                    charBufferAllocate = charBufferAllocate2;
                } else {
                    coderResultDecode.throwException();
                }
            } else {
                charBufferAllocate.flip();
                return charBufferAllocate;
            }
        }
    }

    public boolean isAutoDetecting() {
        return false;
    }

    public boolean isCharsetDetected() {
        throw new UnsupportedOperationException();
    }

    public Charset detectedCharset() {
        throw new UnsupportedOperationException();
    }

    private void throwIllegalStateException(int i2, int i3) {
        throw new IllegalStateException("Current state = " + stateNames[i2] + ", new state = " + stateNames[i3]);
    }
}
