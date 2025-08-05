package sun.nio.cs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;

/* loaded from: rt.jar:sun/nio/cs/StreamEncoder.class */
public class StreamEncoder extends Writer {
    private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
    private volatile boolean isOpen;
    private Charset cs;
    private CharsetEncoder encoder;

    /* renamed from: bb, reason: collision with root package name */
    private ByteBuffer f13592bb;
    private final OutputStream out;
    private WritableByteChannel ch;
    private boolean haveLeftoverChar;
    private char leftoverChar;
    private CharBuffer lcb;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StreamEncoder.class.desiredAssertionStatus();
    }

    private void ensureOpen() throws IOException {
        if (!this.isOpen) {
            throw new IOException("Stream closed");
        }
    }

    public static StreamEncoder forOutputStreamWriter(OutputStream outputStream, Object obj, String str) throws UnsupportedEncodingException {
        String strName = str;
        if (strName == null) {
            strName = Charset.defaultCharset().name();
        }
        try {
            if (Charset.isSupported(strName)) {
                return new StreamEncoder(outputStream, obj, Charset.forName(strName));
            }
        } catch (IllegalCharsetNameException e2) {
        }
        throw new UnsupportedEncodingException(strName);
    }

    public static StreamEncoder forOutputStreamWriter(OutputStream outputStream, Object obj, Charset charset) {
        return new StreamEncoder(outputStream, obj, charset);
    }

    public static StreamEncoder forOutputStreamWriter(OutputStream outputStream, Object obj, CharsetEncoder charsetEncoder) {
        return new StreamEncoder(outputStream, obj, charsetEncoder);
    }

    public static StreamEncoder forEncoder(WritableByteChannel writableByteChannel, CharsetEncoder charsetEncoder, int i2) {
        return new StreamEncoder(writableByteChannel, charsetEncoder, i2);
    }

    public String getEncoding() {
        if (isOpen()) {
            return encodingName();
        }
        return null;
    }

    public void flushBuffer() throws IOException {
        synchronized (this.lock) {
            if (isOpen()) {
                implFlushBuffer();
            } else {
                throw new IOException("Stream closed");
            }
        }
    }

    @Override // java.io.Writer
    public void write(int i2) throws IOException {
        write(new char[]{(char) i2}, 0, 1);
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i2, int i3) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (i2 < 0 || i2 > cArr.length || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (i3 == 0) {
                return;
            }
            implWrite(cArr, i2, i3);
        }
    }

    @Override // java.io.Writer
    public void write(String str, int i2, int i3) throws IOException {
        if (i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        char[] cArr = new char[i3];
        str.getChars(i2, i2 + i3, cArr, 0);
        write(cArr, 0, i3);
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            implFlush();
        }
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.lock) {
            if (this.isOpen) {
                implClose();
                this.isOpen = false;
            }
        }
    }

    private boolean isOpen() {
        return this.isOpen;
    }

    private StreamEncoder(OutputStream outputStream, Object obj, Charset charset) {
        this(outputStream, obj, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
    }

    private StreamEncoder(OutputStream outputStream, Object obj, CharsetEncoder charsetEncoder) {
        super(obj);
        this.isOpen = true;
        this.haveLeftoverChar = false;
        this.lcb = null;
        this.out = outputStream;
        this.ch = null;
        this.cs = charsetEncoder.charset();
        this.encoder = charsetEncoder;
        if (this.ch == null) {
            this.f13592bb = ByteBuffer.allocate(8192);
        }
    }

    private StreamEncoder(WritableByteChannel writableByteChannel, CharsetEncoder charsetEncoder, int i2) {
        this.isOpen = true;
        this.haveLeftoverChar = false;
        this.lcb = null;
        this.out = null;
        this.ch = writableByteChannel;
        this.cs = charsetEncoder.charset();
        this.encoder = charsetEncoder;
        this.f13592bb = ByteBuffer.allocate(i2 < 0 ? 8192 : i2);
    }

    private void writeBytes() throws IOException {
        this.f13592bb.flip();
        int iLimit = this.f13592bb.limit();
        int iPosition = this.f13592bb.position();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        if (i2 > 0) {
            if (this.ch != null) {
                if (this.ch.write(this.f13592bb) != i2 && !$assertionsDisabled) {
                    throw new AssertionError(i2);
                }
            } else {
                this.out.write(this.f13592bb.array(), this.f13592bb.arrayOffset() + iPosition, i2);
            }
        }
        this.f13592bb.clear();
    }

    private void flushLeftoverChar(CharBuffer charBuffer, boolean z2) throws IOException {
        if (!this.haveLeftoverChar && !z2) {
            return;
        }
        if (this.lcb == null) {
            this.lcb = CharBuffer.allocate(2);
        } else {
            this.lcb.clear();
        }
        if (this.haveLeftoverChar) {
            this.lcb.put(this.leftoverChar);
        }
        if (charBuffer != null && charBuffer.hasRemaining()) {
            this.lcb.put(charBuffer.get());
        }
        this.lcb.flip();
        while (true) {
            if (!this.lcb.hasRemaining() && !z2) {
                break;
            }
            CoderResult coderResultEncode = this.encoder.encode(this.lcb, this.f13592bb, z2);
            if (coderResultEncode.isUnderflow()) {
                if (this.lcb.hasRemaining()) {
                    this.leftoverChar = this.lcb.get();
                    if (charBuffer != null && charBuffer.hasRemaining()) {
                        flushLeftoverChar(charBuffer, z2);
                        return;
                    }
                    return;
                }
            } else if (coderResultEncode.isOverflow()) {
                if (!$assertionsDisabled && this.f13592bb.position() <= 0) {
                    throw new AssertionError();
                }
                writeBytes();
            } else {
                coderResultEncode.throwException();
            }
        }
        this.haveLeftoverChar = false;
    }

    void implWrite(char[] cArr, int i2, int i3) throws IOException {
        CharBuffer charBufferWrap = CharBuffer.wrap(cArr, i2, i3);
        if (this.haveLeftoverChar) {
            flushLeftoverChar(charBufferWrap, false);
        }
        while (charBufferWrap.hasRemaining()) {
            CoderResult coderResultEncode = this.encoder.encode(charBufferWrap, this.f13592bb, false);
            if (coderResultEncode.isUnderflow()) {
                if (!$assertionsDisabled && charBufferWrap.remaining() > 1) {
                    throw new AssertionError(charBufferWrap.remaining());
                }
                if (charBufferWrap.remaining() == 1) {
                    this.haveLeftoverChar = true;
                    this.leftoverChar = charBufferWrap.get();
                    return;
                }
                return;
            }
            if (coderResultEncode.isOverflow()) {
                if (!$assertionsDisabled && this.f13592bb.position() <= 0) {
                    throw new AssertionError();
                }
                writeBytes();
            } else {
                coderResultEncode.throwException();
            }
        }
    }

    void implFlushBuffer() throws IOException {
        if (this.f13592bb.position() > 0) {
            writeBytes();
        }
    }

    void implFlush() throws IOException {
        implFlushBuffer();
        if (this.out != null) {
            this.out.flush();
        }
    }

    void implClose() throws IOException {
        flushLeftoverChar(null, true);
        while (true) {
            try {
                CoderResult coderResultFlush = this.encoder.flush(this.f13592bb);
                if (!coderResultFlush.isUnderflow()) {
                    if (coderResultFlush.isOverflow()) {
                        if (!$assertionsDisabled && this.f13592bb.position() <= 0) {
                            throw new AssertionError();
                        }
                        writeBytes();
                    } else {
                        coderResultFlush.throwException();
                    }
                } else {
                    if (this.f13592bb.position() > 0) {
                        writeBytes();
                    }
                    if (this.ch != null) {
                        this.ch.close();
                    } else {
                        this.out.close();
                    }
                    return;
                }
            } catch (IOException e2) {
                this.encoder.reset();
                throw e2;
            }
        }
    }

    String encodingName() {
        if (this.cs instanceof HistoricallyNamedCharset) {
            return ((HistoricallyNamedCharset) this.cs).historicalName();
        }
        return this.cs.name();
    }
}
