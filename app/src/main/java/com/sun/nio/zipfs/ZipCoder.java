package com.sun.nio.zipfs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipCoder.class */
final class ZipCoder {
    private Charset cs;
    private boolean isutf8;
    private ZipCoder utf8;
    private final ThreadLocal<CharsetDecoder> decTL = new ThreadLocal<>();
    private final ThreadLocal<CharsetEncoder> encTL = new ThreadLocal<>();

    String toString(byte[] bArr, int i2) {
        CharsetDecoder charsetDecoderReset = decoder().reset();
        int iMaxCharsPerByte = (int) (i2 * charsetDecoderReset.maxCharsPerByte());
        char[] cArr = new char[iMaxCharsPerByte];
        if (iMaxCharsPerByte == 0) {
            return new String(cArr);
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, 0, i2);
        CharBuffer charBufferWrap = CharBuffer.wrap(cArr);
        CoderResult coderResultDecode = charsetDecoderReset.decode(byteBufferWrap, charBufferWrap, true);
        if (!coderResultDecode.isUnderflow()) {
            throw new IllegalArgumentException(coderResultDecode.toString());
        }
        CoderResult coderResultFlush = charsetDecoderReset.flush(charBufferWrap);
        if (!coderResultFlush.isUnderflow()) {
            throw new IllegalArgumentException(coderResultFlush.toString());
        }
        return new String(cArr, 0, charBufferWrap.position());
    }

    String toString(byte[] bArr) {
        return toString(bArr, bArr.length);
    }

    byte[] getBytes(String str) {
        CharsetEncoder charsetEncoderReset = encoder().reset();
        char[] charArray = str.toCharArray();
        int length = (int) (charArray.length * charsetEncoderReset.maxBytesPerChar());
        byte[] bArr = new byte[length];
        if (length == 0) {
            return bArr;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        CoderResult coderResultEncode = charsetEncoderReset.encode(CharBuffer.wrap(charArray), byteBufferWrap, true);
        if (!coderResultEncode.isUnderflow()) {
            throw new IllegalArgumentException(coderResultEncode.toString());
        }
        CoderResult coderResultFlush = charsetEncoderReset.flush(byteBufferWrap);
        if (!coderResultFlush.isUnderflow()) {
            throw new IllegalArgumentException(coderResultFlush.toString());
        }
        if (byteBufferWrap.position() == bArr.length) {
            return bArr;
        }
        return Arrays.copyOf(bArr, byteBufferWrap.position());
    }

    byte[] getBytesUTF8(String str) {
        if (this.isutf8) {
            return getBytes(str);
        }
        if (this.utf8 == null) {
            this.utf8 = new ZipCoder(Charset.forName("UTF-8"));
        }
        return this.utf8.getBytes(str);
    }

    String toStringUTF8(byte[] bArr, int i2) {
        if (this.isutf8) {
            return toString(bArr, i2);
        }
        if (this.utf8 == null) {
            this.utf8 = new ZipCoder(Charset.forName("UTF-8"));
        }
        return this.utf8.toString(bArr, i2);
    }

    boolean isUTF8() {
        return this.isutf8;
    }

    private ZipCoder(Charset charset) {
        this.cs = charset;
        this.isutf8 = charset.name().equals("UTF-8");
    }

    static ZipCoder get(Charset charset) {
        return new ZipCoder(charset);
    }

    static ZipCoder get(String str) {
        try {
            return new ZipCoder(Charset.forName(str));
        } catch (Throwable th) {
            th.printStackTrace();
            return new ZipCoder(Charset.defaultCharset());
        }
    }

    private CharsetDecoder decoder() {
        CharsetDecoder charsetDecoderOnUnmappableCharacter = this.decTL.get();
        if (charsetDecoderOnUnmappableCharacter == null) {
            charsetDecoderOnUnmappableCharacter = this.cs.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
            this.decTL.set(charsetDecoderOnUnmappableCharacter);
        }
        return charsetDecoderOnUnmappableCharacter;
    }

    private CharsetEncoder encoder() {
        CharsetEncoder charsetEncoderOnUnmappableCharacter = this.encTL.get();
        if (charsetEncoderOnUnmappableCharacter == null) {
            charsetEncoderOnUnmappableCharacter = this.cs.newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
            this.encTL.set(charsetEncoderOnUnmappableCharacter);
        }
        return charsetEncoderOnUnmappableCharacter;
    }
}
