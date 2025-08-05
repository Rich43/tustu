package com.sun.org.apache.xerces.internal.impl.io;

import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import com.sun.xml.internal.stream.util.BufferAllocator;
import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/io/UTF8Reader.class */
public class UTF8Reader extends Reader {
    public static final int DEFAULT_BUFFER_SIZE = 2048;
    private static final boolean DEBUG_READ = false;
    protected InputStream fInputStream;
    protected byte[] fBuffer;
    protected int fOffset;
    private int fSurrogate;
    private MessageFormatter fFormatter;
    private Locale fLocale;

    public UTF8Reader(InputStream inputStream) {
        this(inputStream, 2048, new XMLMessageFormatter(), Locale.getDefault());
    }

    public UTF8Reader(InputStream inputStream, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, 2048, messageFormatter, locale);
    }

    public UTF8Reader(InputStream inputStream, int size, MessageFormatter messageFormatter, Locale locale) {
        this.fSurrogate = -1;
        this.fFormatter = null;
        this.fLocale = null;
        this.fInputStream = inputStream;
        BufferAllocator ba2 = ThreadLocalBufferAllocator.getBufferAllocator();
        this.fBuffer = ba2.getByteBuffer(size);
        if (this.fBuffer == null) {
            this.fBuffer = new byte[size];
        }
        this.fFormatter = messageFormatter;
        this.fLocale = locale;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int c2 = this.fSurrogate;
        if (this.fSurrogate == -1) {
            int index = 0;
            if (0 != this.fOffset) {
                index = 0 + 1;
                i2 = this.fBuffer[0] & 255;
            } else {
                i2 = this.fInputStream.read();
            }
            int b0 = i2;
            if (b0 == -1) {
                return -1;
            }
            if (b0 < 128) {
                c2 = (char) b0;
            } else if ((b0 & 224) == 192 && (b0 & 30) != 0) {
                if (index == this.fOffset) {
                    i8 = this.fInputStream.read();
                } else {
                    int i9 = index;
                    int i10 = index + 1;
                    i8 = this.fBuffer[i9] & 255;
                }
                int b1 = i8;
                if (b1 == -1) {
                    expectedByte(2, 2);
                }
                if ((b1 & 192) != 128) {
                    invalidByte(2, 2, b1);
                }
                c2 = ((b0 << 6) & 1984) | (b1 & 63);
            } else if ((b0 & 240) == 224) {
                if (index == this.fOffset) {
                    i6 = this.fInputStream.read();
                } else {
                    int i11 = index;
                    index++;
                    i6 = this.fBuffer[i11] & 255;
                }
                int b12 = i6;
                if (b12 == -1) {
                    expectedByte(2, 3);
                }
                if ((b12 & 192) != 128 || ((b0 == 237 && b12 >= 160) || ((b0 & 15) == 0 && (b12 & 32) == 0))) {
                    invalidByte(2, 3, b12);
                }
                if (index == this.fOffset) {
                    i7 = this.fInputStream.read();
                } else {
                    int i12 = index;
                    int i13 = index + 1;
                    i7 = this.fBuffer[i12] & 255;
                }
                int b2 = i7;
                if (b2 == -1) {
                    expectedByte(3, 3);
                }
                if ((b2 & 192) != 128) {
                    invalidByte(3, 3, b2);
                }
                c2 = ((b0 << 12) & 61440) | ((b12 << 6) & 4032) | (b2 & 63);
            } else if ((b0 & 248) == 240) {
                if (index == this.fOffset) {
                    i3 = this.fInputStream.read();
                } else {
                    int i14 = index;
                    index++;
                    i3 = this.fBuffer[i14] & 255;
                }
                int b13 = i3;
                if (b13 == -1) {
                    expectedByte(2, 4);
                }
                if ((b13 & 192) != 128 || ((b13 & 48) == 0 && (b0 & 7) == 0)) {
                    invalidByte(2, 3, b13);
                }
                if (index == this.fOffset) {
                    i4 = this.fInputStream.read();
                } else {
                    int i15 = index;
                    index++;
                    i4 = this.fBuffer[i15] & 255;
                }
                int b22 = i4;
                if (b22 == -1) {
                    expectedByte(3, 4);
                }
                if ((b22 & 192) != 128) {
                    invalidByte(3, 3, b22);
                }
                if (index == this.fOffset) {
                    i5 = this.fInputStream.read();
                } else {
                    int i16 = index;
                    int i17 = index + 1;
                    i5 = this.fBuffer[i16] & 255;
                }
                int b3 = i5;
                if (b3 == -1) {
                    expectedByte(4, 4);
                }
                if ((b3 & 192) != 128) {
                    invalidByte(4, 4, b3);
                }
                int uuuuu = ((b0 << 2) & 28) | ((b13 >> 4) & 3);
                if (uuuuu > 16) {
                    invalidSurrogate(uuuuu);
                }
                int wwww = uuuuu - 1;
                int hs = 55296 | ((wwww << 6) & 960) | ((b13 << 2) & 60) | ((b22 >> 4) & 3);
                int ls = 56320 | ((b22 << 6) & 960) | (b3 & 63);
                c2 = hs;
                this.fSurrogate = ls;
            } else {
                invalidByte(1, 1, b0);
            }
        } else {
            this.fSurrogate = -1;
        }
        return c2;
    }

    @Override // java.io.Reader
    public int read(char[] ch, int offset, int length) throws IOException {
        int count;
        int b1;
        int b2;
        int b3;
        int b12;
        int b22;
        int b13;
        byte byte1;
        int out = offset;
        if (this.fSurrogate != -1) {
            ch[offset + 1] = (char) this.fSurrogate;
            this.fSurrogate = -1;
            length--;
            out++;
        }
        if (this.fOffset == 0) {
            if (length > this.fBuffer.length) {
                length = this.fBuffer.length;
            }
            int count2 = this.fInputStream.read(this.fBuffer, 0, length);
            if (count2 == -1) {
                return -1;
            }
            count = count2 + (out - offset);
        } else {
            count = this.fOffset;
            this.fOffset = 0;
        }
        int total = count;
        int in = 0;
        while (in < total && (byte1 = this.fBuffer[in]) >= 0) {
            int i2 = out;
            out++;
            ch[i2] = (char) byte1;
            in++;
        }
        while (in < total) {
            byte byte12 = this.fBuffer[in];
            if (byte12 >= 0) {
                int i3 = out;
                out++;
                ch[i3] = (char) byte12;
            } else {
                int b0 = byte12 & 255;
                if ((b0 & 224) == 192 && (b0 & 30) != 0) {
                    in++;
                    if (in < total) {
                        b13 = this.fBuffer[in] & 255;
                    } else {
                        b13 = this.fInputStream.read();
                        if (b13 == -1) {
                            if (out > offset) {
                                this.fBuffer[0] = (byte) b0;
                                this.fOffset = 1;
                                return out - offset;
                            }
                            expectedByte(2, 2);
                        }
                        count++;
                    }
                    if ((b13 & 192) != 128) {
                        if (out > offset) {
                            this.fBuffer[0] = (byte) b0;
                            this.fBuffer[1] = (byte) b13;
                            this.fOffset = 2;
                            return out - offset;
                        }
                        invalidByte(2, 2, b13);
                    }
                    int c2 = ((b0 << 6) & 1984) | (b13 & 63);
                    int i4 = out;
                    out++;
                    ch[i4] = (char) c2;
                    count--;
                } else if ((b0 & 240) == 224) {
                    int in2 = in + 1;
                    if (in2 < total) {
                        b12 = this.fBuffer[in2] & 255;
                    } else {
                        b12 = this.fInputStream.read();
                        if (b12 == -1) {
                            if (out > offset) {
                                this.fBuffer[0] = (byte) b0;
                                this.fOffset = 1;
                                return out - offset;
                            }
                            expectedByte(2, 3);
                        }
                        count++;
                    }
                    if ((b12 & 192) != 128 || ((b0 == 237 && b12 >= 160) || ((b0 & 15) == 0 && (b12 & 32) == 0))) {
                        if (out > offset) {
                            this.fBuffer[0] = (byte) b0;
                            this.fBuffer[1] = (byte) b12;
                            this.fOffset = 2;
                            return out - offset;
                        }
                        invalidByte(2, 3, b12);
                    }
                    in = in2 + 1;
                    if (in < total) {
                        b22 = this.fBuffer[in] & 255;
                    } else {
                        b22 = this.fInputStream.read();
                        if (b22 == -1) {
                            if (out > offset) {
                                this.fBuffer[0] = (byte) b0;
                                this.fBuffer[1] = (byte) b12;
                                this.fOffset = 2;
                                return out - offset;
                            }
                            expectedByte(3, 3);
                        }
                        count++;
                    }
                    if ((b22 & 192) != 128) {
                        if (out > offset) {
                            this.fBuffer[0] = (byte) b0;
                            this.fBuffer[1] = (byte) b12;
                            this.fBuffer[2] = (byte) b22;
                            this.fOffset = 3;
                            return out - offset;
                        }
                        invalidByte(3, 3, b22);
                    }
                    int c3 = ((b0 << 12) & 61440) | ((b12 << 6) & 4032) | (b22 & 63);
                    int i5 = out;
                    out++;
                    ch[i5] = (char) c3;
                    count -= 2;
                } else if ((b0 & 248) == 240) {
                    int in3 = in + 1;
                    if (in3 < total) {
                        b1 = this.fBuffer[in3] & 255;
                    } else {
                        b1 = this.fInputStream.read();
                        if (b1 == -1) {
                            if (out > offset) {
                                this.fBuffer[0] = (byte) b0;
                                this.fOffset = 1;
                                return out - offset;
                            }
                            expectedByte(2, 4);
                        }
                        count++;
                    }
                    if ((b1 & 192) != 128 || ((b1 & 48) == 0 && (b0 & 7) == 0)) {
                        if (out > offset) {
                            this.fBuffer[0] = (byte) b0;
                            this.fBuffer[1] = (byte) b1;
                            this.fOffset = 2;
                            return out - offset;
                        }
                        invalidByte(2, 4, b1);
                    }
                    int in4 = in3 + 1;
                    if (in4 < total) {
                        b2 = this.fBuffer[in4] & 255;
                    } else {
                        b2 = this.fInputStream.read();
                        if (b2 == -1) {
                            if (out > offset) {
                                this.fBuffer[0] = (byte) b0;
                                this.fBuffer[1] = (byte) b1;
                                this.fOffset = 2;
                                return out - offset;
                            }
                            expectedByte(3, 4);
                        }
                        count++;
                    }
                    if ((b2 & 192) != 128) {
                        if (out > offset) {
                            this.fBuffer[0] = (byte) b0;
                            this.fBuffer[1] = (byte) b1;
                            this.fBuffer[2] = (byte) b2;
                            this.fOffset = 3;
                            return out - offset;
                        }
                        invalidByte(3, 4, b2);
                    }
                    in = in4 + 1;
                    if (in < total) {
                        b3 = this.fBuffer[in] & 255;
                    } else {
                        b3 = this.fInputStream.read();
                        if (b3 == -1) {
                            if (out > offset) {
                                this.fBuffer[0] = (byte) b0;
                                this.fBuffer[1] = (byte) b1;
                                this.fBuffer[2] = (byte) b2;
                                this.fOffset = 3;
                                return out - offset;
                            }
                            expectedByte(4, 4);
                        }
                        count++;
                    }
                    if ((b3 & 192) != 128) {
                        if (out > offset) {
                            this.fBuffer[0] = (byte) b0;
                            this.fBuffer[1] = (byte) b1;
                            this.fBuffer[2] = (byte) b2;
                            this.fBuffer[3] = (byte) b3;
                            this.fOffset = 4;
                            return out - offset;
                        }
                        invalidByte(4, 4, b2);
                    }
                    if (out + 1 >= ch.length) {
                        this.fBuffer[0] = (byte) b0;
                        this.fBuffer[1] = (byte) b1;
                        this.fBuffer[2] = (byte) b2;
                        this.fBuffer[3] = (byte) b3;
                        this.fOffset = 4;
                        return out - offset;
                    }
                    int uuuuu = ((b0 << 2) & 28) | ((b1 >> 4) & 3);
                    if (uuuuu > 16) {
                        invalidSurrogate(uuuuu);
                    }
                    int wwww = uuuuu - 1;
                    int zzzz = b1 & 15;
                    int yyyyyy = b2 & 63;
                    int xxxxxx = b3 & 63;
                    int hs = 55296 | ((wwww << 6) & 960) | (zzzz << 2) | (yyyyyy >> 4);
                    int ls = 56320 | ((yyyyyy << 6) & 960) | xxxxxx;
                    int i6 = out;
                    int out2 = out + 1;
                    ch[i6] = (char) hs;
                    out = out2 + 1;
                    ch[out2] = (char) ls;
                    count -= 2;
                } else {
                    if (out > offset) {
                        this.fBuffer[0] = (byte) b0;
                        this.fOffset = 1;
                        return out - offset;
                    }
                    invalidByte(1, 1, b0);
                }
            }
            in++;
        }
        return count;
    }

    @Override // java.io.Reader
    public long skip(long n2) throws IOException {
        long remaining = n2;
        char[] ch = new char[this.fBuffer.length];
        do {
            int length = ((long) ch.length) < remaining ? ch.length : (int) remaining;
            int count = read(ch, 0, length);
            if (count <= 0) {
                break;
            }
            remaining -= count;
        } while (remaining > 0);
        long skipped = n2 - remaining;
        return skipped;
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        return false;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.Reader
    public void mark(int readAheadLimit) throws IOException {
        throw new IOException(this.fFormatter.formatMessage(this.fLocale, "OperationNotSupported", new Object[]{"mark()", "UTF-8"}));
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        this.fOffset = 0;
        this.fSurrogate = -1;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        BufferAllocator ba2 = ThreadLocalBufferAllocator.getBufferAllocator();
        ba2.returnByteBuffer(this.fBuffer);
        this.fBuffer = null;
        this.fInputStream.close();
    }

    private void expectedByte(int position, int count) throws MalformedByteSequenceException {
        throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "ExpectedByte", new Object[]{Integer.toString(position), Integer.toString(count)});
    }

    private void invalidByte(int position, int count, int c2) throws MalformedByteSequenceException {
        throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidByte", new Object[]{Integer.toString(position), Integer.toString(count)});
    }

    private void invalidSurrogate(int uuuuu) throws MalformedByteSequenceException {
        throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidHighSurrogate", new Object[]{Integer.toHexString(uuuuu)});
    }
}
