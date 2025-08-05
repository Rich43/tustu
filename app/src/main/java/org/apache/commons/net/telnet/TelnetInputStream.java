package org.apache.commons.net.telnet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/telnet/TelnetInputStream.class */
final class TelnetInputStream extends BufferedInputStream implements Runnable {
    private static final int EOF = -1;
    private static final int WOULD_BLOCK = -2;
    static final int _STATE_DATA = 0;
    static final int _STATE_IAC = 1;
    static final int _STATE_WILL = 2;
    static final int _STATE_WONT = 3;
    static final int _STATE_DO = 4;
    static final int _STATE_DONT = 5;
    static final int _STATE_SB = 6;
    static final int _STATE_SE = 7;
    static final int _STATE_CR = 8;
    static final int _STATE_IAC_SB = 9;
    private boolean __hasReachedEOF;
    private volatile boolean __isClosed;
    private boolean __readIsWaiting;
    private int __receiveState;
    private int __queueHead;
    private int __queueTail;
    private int __bytesAvailable;
    private final int[] __queue;
    private final TelnetClient __client;
    private final Thread __thread;
    private IOException __ioException;
    private final int[] __suboption;
    private int __suboption_count;
    private volatile boolean __threaded;

    TelnetInputStream(InputStream input, TelnetClient client, boolean readerThread) {
        super(input);
        this.__suboption = new int[512];
        this.__suboption_count = 0;
        this.__client = client;
        this.__receiveState = 0;
        this.__isClosed = true;
        this.__hasReachedEOF = false;
        this.__queue = new int[2049];
        this.__queueHead = 0;
        this.__queueTail = 0;
        this.__bytesAvailable = 0;
        this.__ioException = null;
        this.__readIsWaiting = false;
        this.__threaded = false;
        if (readerThread) {
            this.__thread = new Thread(this);
        } else {
            this.__thread = null;
        }
    }

    TelnetInputStream(InputStream input, TelnetClient client) {
        this(input, client, true);
    }

    void _start() {
        if (this.__thread == null) {
            return;
        }
        this.__isClosed = false;
        int priority = Thread.currentThread().getPriority() + 1;
        if (priority > 10) {
            priority = 10;
        }
        this.__thread.setPriority(priority);
        this.__thread.setDaemon(true);
        this.__thread.start();
        this.__threaded = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:155:0x0092 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x008a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int __read(boolean r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 723
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.net.telnet.TelnetInputStream.__read(boolean):int");
    }

    private boolean __processChar(int ch) throws InterruptedException {
        boolean bufferWasEmpty;
        synchronized (this.__queue) {
            bufferWasEmpty = this.__bytesAvailable == 0;
            while (this.__bytesAvailable >= this.__queue.length - 1) {
                if (this.__threaded) {
                    this.__queue.notify();
                    try {
                        this.__queue.wait();
                    } catch (InterruptedException e2) {
                        throw e2;
                    }
                } else {
                    throw new IllegalStateException("Queue is full! Cannot process another character.");
                }
            }
            if (this.__readIsWaiting && this.__threaded) {
                this.__queue.notify();
            }
            this.__queue[this.__queueTail] = ch;
            this.__bytesAvailable++;
            int i2 = this.__queueTail + 1;
            this.__queueTail = i2;
            if (i2 >= this.__queue.length) {
                this.__queueTail = 0;
            }
        }
        return bufferWasEmpty;
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x00e2 A[EDGE_INSN: B:100:0x00e2->B:62:0x00e2 BREAK  A[LOOP:1: B:79:0x0060->B:102:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00d4 A[Catch: all -> 0x012f, TryCatch #5 {, blocks: (B:4:0x0007, B:6:0x000e, B:7:0x0019, B:8:0x001a, B:10:0x0021, B:13:0x002a, B:15:0x002c, B:17:0x0033, B:18:0x003a, B:20:0x004f, B:21:0x0058, B:22:0x0059, B:23:0x0060, B:28:0x0072, B:50:0x00b5, B:58:0x00cb, B:60:0x00d4, B:62:0x00e2, B:53:0x00c0, B:56:0x00c9, B:32:0x0079, B:33:0x0080, B:34:0x0081, B:35:0x008e, B:38:0x009f, B:46:0x00ad, B:42:0x00a7, B:44:0x00aa, B:63:0x00ea, B:65:0x0107, B:66:0x010c, B:68:0x011d, B:70:0x0124, B:72:0x012d), top: B:89:0x0007, inners: #0, #1, #2, #3 }] */
    @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int read() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 310
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.net.telnet.TelnetInputStream.read():int");
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int i2;
        if (length < 1) {
            return 0;
        }
        synchronized (this.__queue) {
            if (length > this.__bytesAvailable) {
                length = this.__bytesAvailable;
            }
        }
        int i3 = read();
        int ch = i3;
        if (i3 == -1) {
            return -1;
        }
        do {
            int i4 = offset;
            offset++;
            buffer[i4] = (byte) ch;
            length--;
            if (length <= 0) {
                break;
            }
            i2 = read();
            ch = i2;
        } while (i2 != -1);
        return offset - offset;
    }

    @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        synchronized (this.__queue) {
            if (this.__threaded) {
                return this.__bytesAvailable;
            }
            return this.__bytesAvailable + super.available();
        }
    }

    @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        synchronized (this.__queue) {
            this.__hasReachedEOF = true;
            this.__isClosed = true;
            if (this.__thread != null && this.__thread.isAlive()) {
                this.__thread.interrupt();
            }
            this.__queue.notifyAll();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        int ch;
        while (!this.__isClosed) {
            try {
                try {
                    ch = __read(true);
                } catch (InterruptedIOException e2) {
                    synchronized (this.__queue) {
                        this.__ioException = e2;
                        this.__queue.notifyAll();
                        try {
                            this.__queue.wait(100L);
                        } catch (InterruptedException e3) {
                            if (this.__isClosed) {
                                break;
                            }
                        }
                    }
                } catch (RuntimeException e4) {
                    super.close();
                }
                if (ch < 0) {
                    break;
                }
                boolean notify = false;
                try {
                    notify = __processChar(ch);
                } catch (InterruptedException e5) {
                    if (this.__isClosed) {
                        break;
                    }
                }
                if (notify) {
                    this.__client.notifyInputListener();
                }
            } catch (IOException ioe) {
                synchronized (this.__queue) {
                    this.__ioException = ioe;
                    this.__client.notifyInputListener();
                }
            }
        }
        synchronized (this.__queue) {
            this.__isClosed = true;
            this.__hasReachedEOF = true;
            this.__queue.notify();
        }
        this.__threaded = false;
    }
}
