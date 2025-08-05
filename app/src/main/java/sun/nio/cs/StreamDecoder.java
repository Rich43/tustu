package sun.nio.cs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;

/* loaded from: rt.jar:sun/nio/cs/StreamDecoder.class */
public class StreamDecoder extends Reader {
    private static final int MIN_BYTE_BUFFER_SIZE = 32;
    private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
    private volatile boolean isOpen;
    private boolean haveLeftoverChar;
    private char leftoverChar;
    private static volatile boolean channelsAvailable;
    private Charset cs;
    private CharsetDecoder decoder;

    /* renamed from: bb, reason: collision with root package name */
    private ByteBuffer f13591bb;
    private InputStream in;
    private ReadableByteChannel ch;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StreamDecoder.class.desiredAssertionStatus();
        channelsAvailable = true;
    }

    private void ensureOpen() throws IOException {
        if (!this.isOpen) {
            throw new IOException("Stream closed");
        }
    }

    public static StreamDecoder forInputStreamReader(InputStream inputStream, Object obj, String str) throws UnsupportedEncodingException {
        String strName = str;
        if (strName == null) {
            strName = Charset.defaultCharset().name();
        }
        try {
            if (Charset.isSupported(strName)) {
                return new StreamDecoder(inputStream, obj, Charset.forName(strName));
            }
        } catch (IllegalCharsetNameException e2) {
        }
        throw new UnsupportedEncodingException(strName);
    }

    public static StreamDecoder forInputStreamReader(InputStream inputStream, Object obj, Charset charset) {
        return new StreamDecoder(inputStream, obj, charset);
    }

    public static StreamDecoder forInputStreamReader(InputStream inputStream, Object obj, CharsetDecoder charsetDecoder) {
        return new StreamDecoder(inputStream, obj, charsetDecoder);
    }

    public static StreamDecoder forDecoder(ReadableByteChannel readableByteChannel, CharsetDecoder charsetDecoder, int i2) {
        return new StreamDecoder(readableByteChannel, charsetDecoder, i2);
    }

    public String getEncoding() {
        if (isOpen()) {
            return encodingName();
        }
        return null;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        return read0();
    }

    private int read0() throws IOException {
        synchronized (this.lock) {
            if (this.haveLeftoverChar) {
                this.haveLeftoverChar = false;
                return this.leftoverChar;
            }
            char[] cArr = new char[2];
            int i2 = read(cArr, 0, 2);
            switch (i2) {
                case -1:
                    return -1;
                case 0:
                default:
                    if ($assertionsDisabled) {
                        return -1;
                    }
                    throw new AssertionError(i2);
                case 1:
                    break;
                case 2:
                    this.leftoverChar = cArr[1];
                    this.haveLeftoverChar = true;
                    break;
            }
            return cArr[0];
        }
    }

    @Override // java.io.Reader
    public int read(char[] cArr, int i2, int i3) throws IOException {
        int i4 = i2;
        int i5 = i3;
        synchronized (this.lock) {
            ensureOpen();
            if (i4 < 0 || i4 > cArr.length || i5 < 0 || i4 + i5 > cArr.length || i4 + i5 < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (i5 == 0) {
                return 0;
            }
            int i6 = 0;
            if (this.haveLeftoverChar) {
                cArr[i4] = this.leftoverChar;
                i4++;
                i5--;
                this.haveLeftoverChar = false;
                i6 = 1;
                if (i5 == 0 || !implReady()) {
                    return 1;
                }
            }
            if (i5 == 1) {
                int i7 = read0();
                if (i7 == -1) {
                    return i6 == 0 ? -1 : i6;
                }
                cArr[i4] = (char) i7;
                return i6 + 1;
            }
            return i6 + implRead(cArr, i4, i4 + i5);
        }
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        boolean z2;
        synchronized (this.lock) {
            ensureOpen();
            z2 = this.haveLeftoverChar || implReady();
        }
        return z2;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
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

    private static FileChannel getChannel(FileInputStream fileInputStream) {
        if (!channelsAvailable) {
            return null;
        }
        try {
            return fileInputStream.getChannel();
        } catch (UnsatisfiedLinkError e2) {
            channelsAvailable = false;
            return null;
        }
    }

    StreamDecoder(InputStream inputStream, Object obj, Charset charset) {
        this(inputStream, obj, charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
    }

    StreamDecoder(InputStream inputStream, Object obj, CharsetDecoder charsetDecoder) {
        super(obj);
        this.isOpen = true;
        this.haveLeftoverChar = false;
        this.cs = charsetDecoder.charset();
        this.decoder = charsetDecoder;
        if (this.ch == null) {
            this.in = inputStream;
            this.ch = null;
            this.f13591bb = ByteBuffer.allocate(8192);
        }
        this.f13591bb.flip();
    }

    StreamDecoder(ReadableByteChannel readableByteChannel, CharsetDecoder charsetDecoder, int i2) {
        this.isOpen = true;
        this.haveLeftoverChar = false;
        this.in = null;
        this.ch = readableByteChannel;
        this.decoder = charsetDecoder;
        this.cs = charsetDecoder.charset();
        this.f13591bb = ByteBuffer.allocate(i2 < 0 ? 8192 : i2 < 32 ? 32 : i2);
        this.f13591bb.flip();
    }

    private int readBytes() throws IOException {
        this.f13591bb.compact();
        try {
            if (this.ch != null) {
                int i2 = this.ch.read(this.f13591bb);
                if (i2 < 0) {
                    return i2;
                }
            } else {
                int iLimit = this.f13591bb.limit();
                int iPosition = this.f13591bb.position();
                if (!$assertionsDisabled && iPosition > iLimit) {
                    throw new AssertionError();
                }
                int i3 = iPosition <= iLimit ? iLimit - iPosition : 0;
                if (!$assertionsDisabled && i3 <= 0) {
                    throw new AssertionError();
                }
                int i4 = this.in.read(this.f13591bb.array(), this.f13591bb.arrayOffset() + iPosition, i3);
                if (i4 < 0) {
                    this.f13591bb.flip();
                    return i4;
                }
                if (i4 == 0) {
                    throw new IOException("Underlying input stream returned zero bytes");
                }
                if (!$assertionsDisabled && i4 > i3) {
                    throw new AssertionError((Object) ("n = " + i4 + ", rem = " + i3));
                }
                this.f13591bb.position(iPosition + i4);
            }
            this.f13591bb.flip();
            int iRemaining = this.f13591bb.remaining();
            if ($assertionsDisabled || iRemaining != 0) {
                return iRemaining;
            }
            throw new AssertionError(iRemaining);
        } finally {
            this.f13591bb.flip();
        }
    }

    int implRead(char[] cArr, int i2, int i3) throws IOException {
        if (!$assertionsDisabled && i3 - i2 <= 1) {
            throw new AssertionError();
        }
        CharBuffer charBufferWrap = CharBuffer.wrap(cArr, i2, i3 - i2);
        if (charBufferWrap.position() != 0) {
            charBufferWrap = charBufferWrap.slice();
        }
        boolean z2 = false;
        while (true) {
            CoderResult coderResultDecode = this.decoder.decode(this.f13591bb, charBufferWrap, z2);
            if (coderResultDecode.isUnderflow()) {
                if (z2 || !charBufferWrap.hasRemaining() || (charBufferWrap.position() > 0 && !inReady())) {
                    break;
                }
                if (readBytes() < 0) {
                    z2 = true;
                    if (charBufferWrap.position() == 0 && !this.f13591bb.hasRemaining()) {
                        break;
                    }
                    this.decoder.reset();
                } else {
                    continue;
                }
            } else if (coderResultDecode.isOverflow()) {
                if (!$assertionsDisabled && charBufferWrap.position() <= 0) {
                    throw new AssertionError();
                }
            } else {
                coderResultDecode.throwException();
            }
        }
        if (z2) {
            this.decoder.reset();
        }
        if (charBufferWrap.position() == 0) {
            if (z2) {
                return -1;
            }
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        return charBufferWrap.position();
    }

    String encodingName() {
        if (this.cs instanceof HistoricallyNamedCharset) {
            return ((HistoricallyNamedCharset) this.cs).historicalName();
        }
        return this.cs.name();
    }

    private boolean inReady() {
        try {
            if (this.in == null || this.in.available() <= 0) {
                if (!(this.ch instanceof FileChannel)) {
                    return false;
                }
            }
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    boolean implReady() {
        return this.f13591bb.hasRemaining() || inReady();
    }

    void implClose() throws IOException {
        if (this.ch != null) {
            this.ch.close();
        } else {
            this.in.close();
        }
    }
}
