package sun.awt;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/* loaded from: rt.jar:sun/awt/AWTCharset.class */
public class AWTCharset extends Charset {
    protected Charset awtCs;
    protected Charset javaCs;

    public AWTCharset(String str, Charset charset) {
        super(str, null);
        this.javaCs = charset;
        this.awtCs = this;
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        if (this.javaCs == null) {
            return false;
        }
        return this.javaCs.contains(charset);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        if (this.javaCs == null) {
            throw new Error("Encoder is not supported by this Charset");
        }
        return new Encoder(this.javaCs.newEncoder());
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        if (this.javaCs == null) {
            throw new Error("Decoder is not supported by this Charset");
        }
        return new Decoder(this.javaCs.newDecoder());
    }

    /* loaded from: rt.jar:sun/awt/AWTCharset$Encoder.class */
    public class Encoder extends CharsetEncoder {
        protected CharsetEncoder enc;

        protected Encoder(AWTCharset aWTCharset) {
            this(aWTCharset.javaCs.newEncoder());
        }

        protected Encoder(CharsetEncoder charsetEncoder) {
            super(AWTCharset.this.awtCs, charsetEncoder.averageBytesPerChar(), charsetEncoder.maxBytesPerChar());
            this.enc = charsetEncoder;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return this.enc.canEncode(c2);
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(CharSequence charSequence) {
            return this.enc.canEncode(charSequence);
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            return this.enc.encode(charBuffer, byteBuffer, true);
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult implFlush(ByteBuffer byteBuffer) {
            return this.enc.flush(byteBuffer);
        }

        @Override // java.nio.charset.CharsetEncoder
        protected void implReset() {
            this.enc.reset();
        }

        @Override // java.nio.charset.CharsetEncoder
        protected void implReplaceWith(byte[] bArr) {
            if (this.enc != null) {
                this.enc.replaceWith(bArr);
            }
        }

        @Override // java.nio.charset.CharsetEncoder
        protected void implOnMalformedInput(CodingErrorAction codingErrorAction) {
            this.enc.onMalformedInput(codingErrorAction);
        }

        @Override // java.nio.charset.CharsetEncoder
        protected void implOnUnmappableCharacter(CodingErrorAction codingErrorAction) {
            this.enc.onUnmappableCharacter(codingErrorAction);
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean isLegalReplacement(byte[] bArr) {
            return true;
        }
    }

    /* loaded from: rt.jar:sun/awt/AWTCharset$Decoder.class */
    public class Decoder extends CharsetDecoder {
        protected CharsetDecoder dec;
        private String nr;
        ByteBuffer fbb;

        protected Decoder(AWTCharset aWTCharset) {
            this(aWTCharset.javaCs.newDecoder());
        }

        protected Decoder(CharsetDecoder charsetDecoder) {
            super(AWTCharset.this.awtCs, charsetDecoder.averageCharsPerByte(), charsetDecoder.maxCharsPerByte());
            this.fbb = ByteBuffer.allocate(0);
            this.dec = charsetDecoder;
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            return this.dec.decode(byteBuffer, charBuffer, true);
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult implFlush(CharBuffer charBuffer) {
            this.dec.decode(this.fbb, charBuffer, true);
            return this.dec.flush(charBuffer);
        }

        @Override // java.nio.charset.CharsetDecoder
        protected void implReset() {
            this.dec.reset();
        }

        @Override // java.nio.charset.CharsetDecoder
        protected void implReplaceWith(String str) {
            if (this.dec != null) {
                this.dec.replaceWith(str);
            }
        }

        @Override // java.nio.charset.CharsetDecoder
        protected void implOnMalformedInput(CodingErrorAction codingErrorAction) {
            this.dec.onMalformedInput(codingErrorAction);
        }

        @Override // java.nio.charset.CharsetDecoder
        protected void implOnUnmappableCharacter(CodingErrorAction codingErrorAction) {
            this.dec.onUnmappableCharacter(codingErrorAction);
        }
    }
}
