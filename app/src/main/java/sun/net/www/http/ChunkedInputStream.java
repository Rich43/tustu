package sun.net.www.http;

import java.io.IOException;
import java.io.InputStream;
import sun.net.www.MessageHeader;

/* loaded from: rt.jar:sun/net/www/http/ChunkedInputStream.class */
public class ChunkedInputStream extends InputStream implements Hurryable {
    private InputStream in;
    private HttpClient hc;
    private MessageHeader responses;
    private int chunkSize;
    private int chunkRead;
    private int chunkPos;
    private int chunkCount;
    private int rawPos;
    private int rawCount;
    private boolean error;
    private boolean closed;
    private static final int MAX_CHUNK_HEADER_SIZE = 2050;
    static final int STATE_AWAITING_CHUNK_HEADER = 1;
    static final int STATE_READING_CHUNK = 2;
    static final int STATE_AWAITING_CHUNK_EOL = 3;
    static final int STATE_AWAITING_TRAILERS = 4;
    static final int STATE_DONE = 5;
    private byte[] chunkData = new byte[4096];
    private byte[] rawData = new byte[32];
    private int state = 1;

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("stream is closed");
        }
    }

    private void ensureRawAvailable(int i2) {
        if (this.rawCount + i2 > this.rawData.length) {
            int i3 = this.rawCount - this.rawPos;
            if (i3 + i2 > this.rawData.length) {
                byte[] bArr = new byte[i3 + i2];
                if (i3 > 0) {
                    System.arraycopy(this.rawData, this.rawPos, bArr, 0, i3);
                }
                this.rawData = bArr;
            } else if (i3 > 0) {
                System.arraycopy(this.rawData, this.rawPos, this.rawData, 0, i3);
            }
            this.rawCount = i3;
            this.rawPos = 0;
        }
    }

    private void closeUnderlying() throws IOException {
        if (this.in == null) {
            return;
        }
        if (!this.error && this.state == 5) {
            this.hc.finished();
        } else if (!hurry()) {
            this.hc.closeServer();
        }
        this.in = null;
    }

    private int fastRead(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = this.chunkSize - this.chunkRead;
        int i5 = i4 < i3 ? i4 : i3;
        if (i5 > 0) {
            try {
                int i6 = this.in.read(bArr, i2, i5);
                if (i6 > 0) {
                    this.chunkRead += i6;
                    if (this.chunkRead >= this.chunkSize) {
                        this.state = 3;
                    }
                    return i6;
                }
                this.error = true;
                throw new IOException("Premature EOF");
            } catch (IOException e2) {
                this.error = true;
                throw e2;
            }
        }
        return 0;
    }

    private void processRaw() throws IOException {
        while (this.state != 5) {
            switch (this.state) {
                case 1:
                    int i2 = this.rawPos;
                    while (i2 < this.rawCount && this.rawData[i2] != 10) {
                        i2++;
                        if (i2 - this.rawPos >= MAX_CHUNK_HEADER_SIZE) {
                            this.error = true;
                            throw new IOException("Chunk header too long");
                        }
                    }
                    if (i2 >= this.rawCount) {
                        return;
                    }
                    String str = new String(this.rawData, this.rawPos, (i2 - this.rawPos) + 1, "US-ASCII");
                    int i3 = 0;
                    while (i3 < str.length() && Character.digit(str.charAt(i3), 16) != -1) {
                        i3++;
                    }
                    try {
                        this.chunkSize = Integer.parseInt(str.substring(0, i3), 16);
                        this.rawPos = i2 + 1;
                        this.chunkRead = 0;
                        if (this.chunkSize > 0) {
                            this.state = 2;
                            break;
                        } else {
                            this.state = 4;
                            break;
                        }
                    } catch (NumberFormatException e2) {
                        this.error = true;
                        throw new IOException("Bogus chunk size");
                    }
                case 2:
                    if (this.rawPos >= this.rawCount) {
                        return;
                    }
                    int iMin = Math.min(this.chunkSize - this.chunkRead, this.rawCount - this.rawPos);
                    if (this.chunkData.length < this.chunkCount + iMin) {
                        int i4 = this.chunkCount - this.chunkPos;
                        if (this.chunkData.length < i4 + iMin) {
                            byte[] bArr = new byte[i4 + iMin];
                            System.arraycopy(this.chunkData, this.chunkPos, bArr, 0, i4);
                            this.chunkData = bArr;
                        } else {
                            System.arraycopy(this.chunkData, this.chunkPos, this.chunkData, 0, i4);
                        }
                        this.chunkPos = 0;
                        this.chunkCount = i4;
                    }
                    System.arraycopy(this.rawData, this.rawPos, this.chunkData, this.chunkCount, iMin);
                    this.rawPos += iMin;
                    this.chunkCount += iMin;
                    this.chunkRead += iMin;
                    if (this.chunkSize - this.chunkRead <= 0) {
                        this.state = 3;
                        break;
                    } else {
                        return;
                    }
                case 3:
                    if (this.rawPos + 1 >= this.rawCount) {
                        return;
                    }
                    if (this.rawData[this.rawPos] != 13) {
                        this.error = true;
                        throw new IOException("missing CR");
                    }
                    if (this.rawData[this.rawPos + 1] != 10) {
                        this.error = true;
                        throw new IOException("missing LF");
                    }
                    this.rawPos += 2;
                    this.state = 1;
                    break;
                case 4:
                    int i5 = this.rawPos;
                    while (i5 < this.rawCount && this.rawData[i5] != 10) {
                        i5++;
                    }
                    if (i5 >= this.rawCount) {
                        return;
                    }
                    if (i5 == this.rawPos) {
                        this.error = true;
                        throw new IOException("LF should be proceeded by CR");
                    }
                    if (this.rawData[i5 - 1] != 13) {
                        this.error = true;
                        throw new IOException("LF should be proceeded by CR");
                    }
                    if (i5 == this.rawPos + 1) {
                        this.state = 5;
                        closeUnderlying();
                        return;
                    }
                    String str2 = new String(this.rawData, this.rawPos, i5 - this.rawPos, "US-ASCII");
                    int iIndexOf = str2.indexOf(58);
                    if (iIndexOf == -1) {
                        throw new IOException("Malformed tailer - format should be key:value");
                    }
                    this.responses.add(str2.substring(0, iIndexOf).trim(), str2.substring(iIndexOf + 1, str2.length()).trim());
                    this.rawPos = i5 + 1;
                    break;
            }
        }
    }

    private int readAheadNonBlocking() throws IOException {
        int iAvailable = this.in.available();
        if (iAvailable > 0) {
            ensureRawAvailable(iAvailable);
            try {
                int i2 = this.in.read(this.rawData, this.rawCount, iAvailable);
                if (i2 < 0) {
                    this.error = true;
                    return -1;
                }
                this.rawCount += i2;
                processRaw();
            } catch (IOException e2) {
                this.error = true;
                throw e2;
            }
        }
        return this.chunkCount - this.chunkPos;
    }

    private int readAheadBlocking() throws IOException {
        while (this.state != 5) {
            ensureRawAvailable(32);
            try {
                int i2 = this.in.read(this.rawData, this.rawCount, this.rawData.length - this.rawCount);
                if (i2 < 0) {
                    this.error = true;
                    throw new IOException("Premature EOF");
                }
                this.rawCount += i2;
                processRaw();
                if (this.chunkCount > 0) {
                    return this.chunkCount - this.chunkPos;
                }
            } catch (IOException e2) {
                this.error = true;
                throw e2;
            }
        }
        return -1;
    }

    private int readAhead(boolean z2) throws IOException {
        if (this.state == 5) {
            return -1;
        }
        if (this.chunkPos >= this.chunkCount) {
            this.chunkCount = 0;
            this.chunkPos = 0;
        }
        if (z2) {
            return readAheadBlocking();
        }
        return readAheadNonBlocking();
    }

    public ChunkedInputStream(InputStream inputStream, HttpClient httpClient, MessageHeader messageHeader) throws IOException {
        this.in = inputStream;
        this.responses = messageHeader;
        this.hc = httpClient;
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        ensureOpen();
        if (this.chunkPos >= this.chunkCount && readAhead(true) <= 0) {
            return -1;
        }
        byte[] bArr = this.chunkData;
        int i2 = this.chunkPos;
        this.chunkPos = i2 + 1;
        return bArr[i2] & 255;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
        ensureOpen();
        if (i2 < 0 || i2 > bArr.length || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        int ahead = this.chunkCount - this.chunkPos;
        if (ahead <= 0) {
            if (this.state == 2) {
                return fastRead(bArr, i2, i3);
            }
            ahead = readAhead(true);
            if (ahead < 0) {
                return -1;
            }
        }
        int i4 = ahead < i3 ? ahead : i3;
        System.arraycopy(this.chunkData, this.chunkPos, bArr, i2, i4);
        this.chunkPos += i4;
        return i4;
    }

    @Override // java.io.InputStream
    public synchronized int available() throws IOException {
        ensureOpen();
        int i2 = this.chunkCount - this.chunkPos;
        if (i2 > 0) {
            return i2;
        }
        int ahead = readAhead(false);
        if (ahead < 0) {
            return 0;
        }
        return ahead;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.closed) {
            return;
        }
        closeUnderlying();
        this.closed = true;
    }

    @Override // sun.net.www.http.Hurryable
    public synchronized boolean hurry() {
        if (this.in == null || this.error) {
            return false;
        }
        try {
            readAhead(false);
            return !this.error && this.state == 5;
        } catch (Exception e2) {
            return false;
        }
    }
}
