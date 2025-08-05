package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/WriterToUTF8Buffered.class */
final class WriterToUTF8Buffered extends Writer implements WriterChain {
    private static final int BYTES_MAX = 16384;
    private static final int CHARS_MAX = 5461;
    private final OutputStream m_os;
    private final byte[] m_outputBytes = new byte[16387];
    private final char[] m_inputChars = new char[5463];
    private int count = 0;

    public WriterToUTF8Buffered(OutputStream out) throws UnsupportedEncodingException {
        this.m_os = out;
    }

    @Override // java.io.Writer
    public void write(int c2) throws IOException {
        if (this.count >= 16384) {
            flushBuffer();
        }
        if (c2 < 128) {
            byte[] bArr = this.m_outputBytes;
            int i2 = this.count;
            this.count = i2 + 1;
            bArr[i2] = (byte) c2;
            return;
        }
        if (c2 < 2048) {
            byte[] bArr2 = this.m_outputBytes;
            int i3 = this.count;
            this.count = i3 + 1;
            bArr2[i3] = (byte) (192 + (c2 >> 6));
            byte[] bArr3 = this.m_outputBytes;
            int i4 = this.count;
            this.count = i4 + 1;
            bArr3[i4] = (byte) (128 + (c2 & 63));
            return;
        }
        if (c2 < 65536) {
            byte[] bArr4 = this.m_outputBytes;
            int i5 = this.count;
            this.count = i5 + 1;
            bArr4[i5] = (byte) (224 + (c2 >> 12));
            byte[] bArr5 = this.m_outputBytes;
            int i6 = this.count;
            this.count = i6 + 1;
            bArr5[i6] = (byte) (128 + ((c2 >> 6) & 63));
            byte[] bArr6 = this.m_outputBytes;
            int i7 = this.count;
            this.count = i7 + 1;
            bArr6[i7] = (byte) (128 + (c2 & 63));
            return;
        }
        byte[] bArr7 = this.m_outputBytes;
        int i8 = this.count;
        this.count = i8 + 1;
        bArr7[i8] = (byte) (240 + (c2 >> 18));
        byte[] bArr8 = this.m_outputBytes;
        int i9 = this.count;
        this.count = i9 + 1;
        bArr8[i9] = (byte) (128 + ((c2 >> 12) & 63));
        byte[] bArr9 = this.m_outputBytes;
        int i10 = this.count;
        this.count = i10 + 1;
        bArr9[i10] = (byte) (128 + ((c2 >> 6) & 63));
        byte[] bArr10 = this.m_outputBytes;
        int i11 = this.count;
        this.count = i11 + 1;
        bArr10[i11] = (byte) (128 + (c2 & 63));
    }

    @Override // java.io.Writer
    public void write(char[] chars, int start, int length) throws IOException {
        char c2;
        int chunks;
        int lengthx3 = 3 * length;
        if (lengthx3 >= 16384 - this.count) {
            flushBuffer();
            if (lengthx3 > 16384) {
                int split = length / CHARS_MAX;
                if (length % CHARS_MAX > 0) {
                    chunks = split + 1;
                } else {
                    chunks = split;
                }
                int end_chunk = start;
                for (int chunk = 1; chunk <= chunks; chunk++) {
                    int start_chunk = end_chunk;
                    end_chunk = start + ((int) ((length * chunk) / chunks));
                    char c3 = chars[end_chunk - 1];
                    char c4 = chars[end_chunk - 1];
                    if (c3 >= 55296 && c3 <= 56319) {
                        end_chunk = end_chunk < start + length ? end_chunk + 1 : end_chunk - 1;
                    }
                    int len_chunk = end_chunk - start_chunk;
                    write(chars, start_chunk, len_chunk);
                }
                return;
            }
        }
        int n2 = length + start;
        byte[] buf_loc = this.m_outputBytes;
        int count_loc = this.count;
        int i2 = start;
        while (i2 < n2 && (c2 = chars[i2]) < 128) {
            int i3 = count_loc;
            count_loc++;
            buf_loc[i3] = (byte) c2;
            i2++;
        }
        while (i2 < n2) {
            char c5 = chars[i2];
            if (c5 < 128) {
                int i4 = count_loc;
                count_loc++;
                buf_loc[i4] = (byte) c5;
            } else if (c5 < 2048) {
                int i5 = count_loc;
                int count_loc2 = count_loc + 1;
                buf_loc[i5] = (byte) (192 + (c5 >> 6));
                count_loc = count_loc2 + 1;
                buf_loc[count_loc2] = (byte) (128 + (c5 & '?'));
            } else if (c5 >= 55296 && c5 <= 56319) {
                i2++;
                char low = chars[i2];
                int i6 = count_loc;
                int count_loc3 = count_loc + 1;
                buf_loc[i6] = (byte) (240 | (((c5 + '@') >> 8) & 240));
                int count_loc4 = count_loc3 + 1;
                buf_loc[count_loc3] = (byte) (128 | (((c5 + '@') >> 2) & 63));
                int count_loc5 = count_loc4 + 1;
                buf_loc[count_loc4] = (byte) (128 | (((low >> 6) & 15) + ((c5 << 4) & 48)));
                count_loc = count_loc5 + 1;
                buf_loc[count_loc5] = (byte) (128 | (low & '?'));
            } else {
                int i7 = count_loc;
                int count_loc6 = count_loc + 1;
                buf_loc[i7] = (byte) (224 + (c5 >> '\f'));
                int count_loc7 = count_loc6 + 1;
                buf_loc[count_loc6] = (byte) (128 + ((c5 >> 6) & 63));
                count_loc = count_loc7 + 1;
                buf_loc[count_loc7] = (byte) (128 + (c5 & '?'));
            }
            i2++;
        }
        this.count = count_loc;
    }

    @Override // java.io.Writer, com.sun.org.apache.xml.internal.serializer.WriterChain
    public void write(String s2) throws IOException {
        char c2;
        int chunks;
        int length = s2.length();
        int lengthx3 = 3 * length;
        if (lengthx3 >= 16384 - this.count) {
            flushBuffer();
            if (lengthx3 > 16384) {
                int split = length / CHARS_MAX;
                if (length % CHARS_MAX > 0) {
                    chunks = split + 1;
                } else {
                    chunks = split;
                }
                int end_chunk = 0;
                for (int chunk = 1; chunk <= chunks; chunk++) {
                    int start_chunk = end_chunk;
                    end_chunk = 0 + ((int) ((length * chunk) / chunks));
                    s2.getChars(start_chunk, end_chunk, this.m_inputChars, 0);
                    int len_chunk = end_chunk - start_chunk;
                    char c3 = this.m_inputChars[len_chunk - 1];
                    if (c3 >= 55296 && c3 <= 56319) {
                        end_chunk--;
                        len_chunk--;
                        if (chunk == chunks) {
                        }
                    }
                    write(this.m_inputChars, 0, len_chunk);
                }
                return;
            }
        }
        s2.getChars(0, length, this.m_inputChars, 0);
        char[] chars = this.m_inputChars;
        byte[] buf_loc = this.m_outputBytes;
        int count_loc = this.count;
        int i2 = 0;
        while (i2 < length && (c2 = chars[i2]) < 128) {
            int i3 = count_loc;
            count_loc++;
            buf_loc[i3] = (byte) c2;
            i2++;
        }
        while (i2 < length) {
            char c4 = chars[i2];
            if (c4 < 128) {
                int i4 = count_loc;
                count_loc++;
                buf_loc[i4] = (byte) c4;
            } else if (c4 < 2048) {
                int i5 = count_loc;
                int count_loc2 = count_loc + 1;
                buf_loc[i5] = (byte) (192 + (c4 >> 6));
                count_loc = count_loc2 + 1;
                buf_loc[count_loc2] = (byte) (128 + (c4 & '?'));
            } else if (c4 >= 55296 && c4 <= 56319) {
                i2++;
                char low = chars[i2];
                int i6 = count_loc;
                int count_loc3 = count_loc + 1;
                buf_loc[i6] = (byte) (240 | (((c4 + '@') >> 8) & 240));
                int count_loc4 = count_loc3 + 1;
                buf_loc[count_loc3] = (byte) (128 | (((c4 + '@') >> 2) & 63));
                int count_loc5 = count_loc4 + 1;
                buf_loc[count_loc4] = (byte) (128 | (((low >> 6) & 15) + ((c4 << 4) & 48)));
                count_loc = count_loc5 + 1;
                buf_loc[count_loc5] = (byte) (128 | (low & '?'));
            } else {
                int i7 = count_loc;
                int count_loc6 = count_loc + 1;
                buf_loc[i7] = (byte) (224 + (c4 >> '\f'));
                int count_loc7 = count_loc6 + 1;
                buf_loc[count_loc6] = (byte) (128 + ((c4 >> 6) & 63));
                count_loc = count_loc7 + 1;
                buf_loc[count_loc7] = (byte) (128 + (c4 & '?'));
            }
            i2++;
        }
        this.count = count_loc;
    }

    public void flushBuffer() throws IOException {
        if (this.count > 0) {
            this.m_os.write(this.m_outputBytes, 0, this.count);
            this.count = 0;
        }
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        flushBuffer();
        this.m_os.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flushBuffer();
        this.m_os.close();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.WriterChain
    public OutputStream getOutputStream() {
        return this.m_os;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.WriterChain
    public Writer getWriter() {
        return null;
    }
}
