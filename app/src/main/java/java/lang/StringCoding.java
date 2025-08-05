package java.lang;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import org.apache.commons.net.ftp.FTP;
import sun.misc.MessageUtils;
import sun.nio.cs.ArrayDecoder;
import sun.nio.cs.ArrayEncoder;
import sun.nio.cs.HistoricallyNamedCharset;

/* loaded from: rt.jar:java/lang/StringCoding.class */
class StringCoding {
    private static final ThreadLocal<SoftReference<StringDecoder>> decoder = new ThreadLocal<>();
    private static final ThreadLocal<SoftReference<StringEncoder>> encoder = new ThreadLocal<>();
    private static boolean warnUnsupportedCharset = true;

    private StringCoding() {
    }

    private static <T> T deref(ThreadLocal<SoftReference<T>> threadLocal) {
        SoftReference<T> softReference = threadLocal.get();
        if (softReference == null) {
            return null;
        }
        return softReference.get();
    }

    private static <T> void set(ThreadLocal<SoftReference<T>> threadLocal, T t2) {
        threadLocal.set(new SoftReference<>(t2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] safeTrim(byte[] bArr, int i2, Charset charset, boolean z2) {
        if (i2 == bArr.length && (z2 || System.getSecurityManager() == null)) {
            return bArr;
        }
        return Arrays.copyOf(bArr, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static char[] safeTrim(char[] cArr, int i2, Charset charset, boolean z2) {
        if (i2 == cArr.length && (z2 || System.getSecurityManager() == null)) {
            return cArr;
        }
        return Arrays.copyOf(cArr, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int scale(int i2, float f2) {
        return (int) (i2 * f2);
    }

    private static Charset lookupCharset(String str) {
        if (Charset.isSupported(str)) {
            try {
                return Charset.forName(str);
            } catch (UnsupportedCharsetException e2) {
                throw new Error(e2);
            }
        }
        return null;
    }

    private static void warnUnsupportedCharset(String str) {
        if (warnUnsupportedCharset) {
            MessageUtils.err("WARNING: Default charset " + str + " not supported, using ISO-8859-1 instead");
            warnUnsupportedCharset = false;
        }
    }

    /* loaded from: rt.jar:java/lang/StringCoding$StringDecoder.class */
    private static class StringDecoder {
        private final String requestedCharsetName;
        private final Charset cs;
        private final CharsetDecoder cd;
        private final boolean isTrusted;

        private StringDecoder(Charset charset, String str) {
            this.requestedCharsetName = str;
            this.cs = charset;
            this.cd = charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            this.isTrusted = charset.getClass().getClassLoader0() == null;
        }

        String charsetName() {
            if (this.cs instanceof HistoricallyNamedCharset) {
                return ((HistoricallyNamedCharset) this.cs).historicalName();
            }
            return this.cs.name();
        }

        final String requestedCharsetName() {
            return this.requestedCharsetName;
        }

        char[] decode(byte[] bArr, int i2, int i3) {
            char[] cArr = new char[StringCoding.scale(i3, this.cd.maxCharsPerByte())];
            if (i3 == 0) {
                return cArr;
            }
            if (this.cd instanceof ArrayDecoder) {
                return StringCoding.safeTrim(cArr, ((ArrayDecoder) this.cd).decode(bArr, i2, i3, cArr), this.cs, this.isTrusted);
            }
            this.cd.reset();
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, i2, i3);
            CharBuffer charBufferWrap = CharBuffer.wrap(cArr);
            try {
                CoderResult coderResultDecode = this.cd.decode(byteBufferWrap, charBufferWrap, true);
                if (!coderResultDecode.isUnderflow()) {
                    coderResultDecode.throwException();
                }
                CoderResult coderResultFlush = this.cd.flush(charBufferWrap);
                if (!coderResultFlush.isUnderflow()) {
                    coderResultFlush.throwException();
                }
                return StringCoding.safeTrim(cArr, charBufferWrap.position(), this.cs, this.isTrusted);
            } catch (CharacterCodingException e2) {
                throw new Error(e2);
            }
        }
    }

    static char[] decode(String str, byte[] bArr, int i2, int i3) throws UnsupportedEncodingException {
        StringDecoder stringDecoder = (StringDecoder) deref(decoder);
        String str2 = str == null ? FTP.DEFAULT_CONTROL_ENCODING : str;
        if (stringDecoder == null || (!str2.equals(stringDecoder.requestedCharsetName()) && !str2.equals(stringDecoder.charsetName()))) {
            stringDecoder = null;
            try {
                Charset charsetLookupCharset = lookupCharset(str2);
                if (charsetLookupCharset != null) {
                    stringDecoder = new StringDecoder(charsetLookupCharset, str2);
                }
            } catch (IllegalCharsetNameException e2) {
            }
            if (stringDecoder == null) {
                throw new UnsupportedEncodingException(str2);
            }
            set(decoder, stringDecoder);
        }
        return stringDecoder.decode(bArr, i2, i3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static char[] decode(Charset charset, byte[] bArr, int i2, int i3) {
        CharsetDecoder charsetDecoderNewDecoder = charset.newDecoder();
        char[] cArr = new char[scale(i3, charsetDecoderNewDecoder.maxCharsPerByte())];
        if (i3 == 0) {
            return cArr;
        }
        boolean z2 = false;
        if (System.getSecurityManager() != null) {
            boolean z3 = charset.getClass().getClassLoader0() == null;
            z2 = z3;
            if (!z3) {
                bArr = Arrays.copyOfRange(bArr, i2, i2 + i3);
                i2 = 0;
            }
        }
        charsetDecoderNewDecoder.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).reset();
        if (charsetDecoderNewDecoder instanceof ArrayDecoder) {
            return safeTrim(cArr, ((ArrayDecoder) charsetDecoderNewDecoder).decode(bArr, i2, i3, cArr), charset, z2);
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, i2, i3);
        CharBuffer charBufferWrap = CharBuffer.wrap(cArr);
        try {
            CoderResult coderResultDecode = charsetDecoderNewDecoder.decode(byteBufferWrap, charBufferWrap, true);
            if (!coderResultDecode.isUnderflow()) {
                coderResultDecode.throwException();
            }
            CoderResult coderResultFlush = charsetDecoderNewDecoder.flush(charBufferWrap);
            if (!coderResultFlush.isUnderflow()) {
                coderResultFlush.throwException();
            }
            return safeTrim(cArr, charBufferWrap.position(), charset, z2);
        } catch (CharacterCodingException e2) {
            throw new Error(e2);
        }
    }

    static char[] decode(byte[] bArr, int i2, int i3) {
        String strName = Charset.defaultCharset().name();
        try {
            return decode(strName, bArr, i2, i3);
        } catch (UnsupportedEncodingException e2) {
            warnUnsupportedCharset(strName);
            try {
                return decode(FTP.DEFAULT_CONTROL_ENCODING, bArr, i2, i3);
            } catch (UnsupportedEncodingException e3) {
                MessageUtils.err("ISO-8859-1 charset not available: " + e3.toString());
                System.exit(1);
                return null;
            }
        }
    }

    /* loaded from: rt.jar:java/lang/StringCoding$StringEncoder.class */
    private static class StringEncoder {
        private Charset cs;
        private CharsetEncoder ce;
        private final String requestedCharsetName;
        private final boolean isTrusted;

        private StringEncoder(Charset charset, String str) {
            this.requestedCharsetName = str;
            this.cs = charset;
            this.ce = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            this.isTrusted = charset.getClass().getClassLoader0() == null;
        }

        String charsetName() {
            if (this.cs instanceof HistoricallyNamedCharset) {
                return ((HistoricallyNamedCharset) this.cs).historicalName();
            }
            return this.cs.name();
        }

        final String requestedCharsetName() {
            return this.requestedCharsetName;
        }

        byte[] encode(char[] cArr, int i2, int i3) {
            byte[] bArr = new byte[StringCoding.scale(i3, this.ce.maxBytesPerChar())];
            if (i3 == 0) {
                return bArr;
            }
            if (this.ce instanceof ArrayEncoder) {
                return StringCoding.safeTrim(bArr, ((ArrayEncoder) this.ce).encode(cArr, i2, i3, bArr), this.cs, this.isTrusted);
            }
            this.ce.reset();
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            try {
                CoderResult coderResultEncode = this.ce.encode(CharBuffer.wrap(cArr, i2, i3), byteBufferWrap, true);
                if (!coderResultEncode.isUnderflow()) {
                    coderResultEncode.throwException();
                }
                CoderResult coderResultFlush = this.ce.flush(byteBufferWrap);
                if (!coderResultFlush.isUnderflow()) {
                    coderResultFlush.throwException();
                }
                return StringCoding.safeTrim(bArr, byteBufferWrap.position(), this.cs, this.isTrusted);
            } catch (CharacterCodingException e2) {
                throw new Error(e2);
            }
        }
    }

    static byte[] encode(String str, char[] cArr, int i2, int i3) throws UnsupportedEncodingException {
        StringEncoder stringEncoder = (StringEncoder) deref(encoder);
        String str2 = str == null ? FTP.DEFAULT_CONTROL_ENCODING : str;
        if (stringEncoder == null || (!str2.equals(stringEncoder.requestedCharsetName()) && !str2.equals(stringEncoder.charsetName()))) {
            stringEncoder = null;
            try {
                Charset charsetLookupCharset = lookupCharset(str2);
                if (charsetLookupCharset != null) {
                    stringEncoder = new StringEncoder(charsetLookupCharset, str2);
                }
            } catch (IllegalCharsetNameException e2) {
            }
            if (stringEncoder == null) {
                throw new UnsupportedEncodingException(str2);
            }
            set(encoder, stringEncoder);
        }
        return stringEncoder.encode(cArr, i2, i3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static byte[] encode(Charset charset, char[] cArr, int i2, int i3) {
        CharsetEncoder charsetEncoderNewEncoder = charset.newEncoder();
        byte[] bArr = new byte[scale(i3, charsetEncoderNewEncoder.maxBytesPerChar())];
        if (i3 == 0) {
            return bArr;
        }
        boolean z2 = false;
        if (System.getSecurityManager() != null) {
            boolean z3 = charset.getClass().getClassLoader0() == null;
            z2 = z3;
            if (!z3) {
                cArr = Arrays.copyOfRange(cArr, i2, i2 + i3);
                i2 = 0;
            }
        }
        charsetEncoderNewEncoder.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).reset();
        if (charsetEncoderNewEncoder instanceof ArrayEncoder) {
            return safeTrim(bArr, ((ArrayEncoder) charsetEncoderNewEncoder).encode(cArr, i2, i3, bArr), charset, z2);
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        try {
            CoderResult coderResultEncode = charsetEncoderNewEncoder.encode(CharBuffer.wrap(cArr, i2, i3), byteBufferWrap, true);
            if (!coderResultEncode.isUnderflow()) {
                coderResultEncode.throwException();
            }
            CoderResult coderResultFlush = charsetEncoderNewEncoder.flush(byteBufferWrap);
            if (!coderResultFlush.isUnderflow()) {
                coderResultFlush.throwException();
            }
            return safeTrim(bArr, byteBufferWrap.position(), charset, z2);
        } catch (CharacterCodingException e2) {
            throw new Error(e2);
        }
    }

    static byte[] encode(char[] cArr, int i2, int i3) {
        String strName = Charset.defaultCharset().name();
        try {
            return encode(strName, cArr, i2, i3);
        } catch (UnsupportedEncodingException e2) {
            warnUnsupportedCharset(strName);
            try {
                return encode(FTP.DEFAULT_CONTROL_ENCODING, cArr, i2, i3);
            } catch (UnsupportedEncodingException e3) {
                MessageUtils.err("ISO-8859-1 charset not available: " + e3.toString());
                System.exit(1);
                return null;
            }
        }
    }
}
