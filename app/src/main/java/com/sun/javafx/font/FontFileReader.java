package com.sun.javafx.font;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: jfxrt.jar:com/sun/javafx/font/FontFileReader.class */
class FontFileReader implements FontConstants {
    String filename;
    long filesize;
    RandomAccessFile raFile;
    private static final int READBUFFERSIZE = 1024;
    private byte[] readBuffer;
    private int readBufferLen;
    private int readBufferStart;

    public FontFileReader(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    public synchronized boolean openFile() throws PrivilegedActionException {
        if (this.raFile != null) {
            return false;
        }
        this.raFile = (RandomAccessFile) AccessController.doPrivileged(() -> {
            try {
                return new RandomAccessFile(this.filename, InternalZipConstants.READ_MODE);
            } catch (FileNotFoundException e2) {
                return null;
            }
        });
        if (this.raFile != null) {
            try {
                this.filesize = this.raFile.length();
                return true;
            } catch (IOException e2) {
                return false;
            }
        }
        return false;
    }

    public synchronized void closeFile() throws IOException {
        if (this.raFile != null) {
            this.raFile.close();
            this.raFile = null;
            this.readBuffer = null;
        }
    }

    public synchronized long getLength() {
        return this.filesize;
    }

    public synchronized void reset() throws IOException {
        if (this.raFile != null) {
            this.raFile.seek(0L);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/FontFileReader$Buffer.class */
    static class Buffer {
        byte[] data;
        int pos;
        int orig;

        Buffer(byte[] data, int bufStart) {
            this.pos = bufStart;
            this.orig = bufStart;
            this.data = data;
        }

        int getInt(int tpos) {
            int tpos2 = tpos + this.orig;
            int tpos3 = tpos2 + 1;
            int val = this.data[tpos2] & 255;
            int tpos4 = tpos3 + 1;
            int val2 = ((val << 8) | (this.data[tpos3] & 255)) << 8;
            int tpos5 = tpos4 + 1;
            int val3 = (val2 | (this.data[tpos4] & 255)) << 8;
            int i2 = tpos5 + 1;
            return val3 | (this.data[tpos5] & 255);
        }

        int getInt() {
            byte[] bArr = this.data;
            int i2 = this.pos;
            this.pos = i2 + 1;
            int val = bArr[i2] & 255;
            byte[] bArr2 = this.data;
            int i3 = this.pos;
            this.pos = i3 + 1;
            int val2 = ((val << 8) | (bArr2[i3] & 255)) << 8;
            byte[] bArr3 = this.data;
            int i4 = this.pos;
            this.pos = i4 + 1;
            int val3 = (val2 | (bArr3[i4] & 255)) << 8;
            byte[] bArr4 = this.data;
            int i5 = this.pos;
            this.pos = i5 + 1;
            return val3 | (bArr4[i5] & 255);
        }

        short getShort(int tpos) {
            int tpos2 = tpos + this.orig;
            int tpos3 = tpos2 + 1;
            int val = this.data[tpos2] & 255;
            int i2 = tpos3 + 1;
            return (short) ((val << 8) | (this.data[tpos3] & 255));
        }

        short getShort() {
            byte[] bArr = this.data;
            int i2 = this.pos;
            this.pos = i2 + 1;
            int val = bArr[i2] & 255;
            byte[] bArr2 = this.data;
            int i3 = this.pos;
            this.pos = i3 + 1;
            return (short) ((val << 8) | (bArr2[i3] & 255));
        }

        char getChar(int tpos) {
            int tpos2 = tpos + this.orig;
            int tpos3 = tpos2 + 1;
            int val = this.data[tpos2] & 255;
            int i2 = tpos3 + 1;
            return (char) ((val << 8) | (this.data[tpos3] & 255));
        }

        char getChar() {
            byte[] bArr = this.data;
            int i2 = this.pos;
            this.pos = i2 + 1;
            int val = bArr[i2] & 255;
            byte[] bArr2 = this.data;
            int i3 = this.pos;
            this.pos = i3 + 1;
            return (char) ((val << 8) | (bArr2[i3] & 255));
        }

        void position(int newPos) {
            this.pos = this.orig + newPos;
        }

        int capacity() {
            return this.data.length - this.orig;
        }

        byte get() {
            byte[] bArr = this.data;
            int i2 = this.pos;
            this.pos = i2 + 1;
            return bArr[i2];
        }

        byte get(int tpos) {
            return this.data[tpos + this.orig];
        }

        void skip(int nbytes) {
            this.pos += nbytes;
        }

        void get(int startPos, byte[] dest, int destPos, int destLen) {
            System.arraycopy(this.data, this.orig + startPos, dest, destPos, destLen);
        }
    }

    private synchronized int readFromFile(byte[] buffer, long seekPos, int requestedLen) {
        try {
            this.raFile.seek(seekPos);
            int bytesRead = this.raFile.read(buffer, 0, requestedLen);
            return bytesRead;
        } catch (IOException e2) {
            if (PrismFontFactory.debugFonts) {
                e2.printStackTrace();
                return 0;
            }
            return 0;
        }
    }

    public synchronized Buffer readBlock(int offset, int len) {
        if (this.readBuffer == null) {
            this.readBuffer = new byte[1024];
            this.readBufferLen = 0;
        }
        if (len <= 1024) {
            if (this.readBufferStart <= offset && this.readBufferStart + this.readBufferLen >= offset + len) {
                return new Buffer(this.readBuffer, offset - this.readBufferStart);
            }
            this.readBufferStart = offset;
            this.readBufferLen = ((long) (offset + 1024)) > this.filesize ? ((int) this.filesize) - offset : 1024;
            readFromFile(this.readBuffer, this.readBufferStart, this.readBufferLen);
            return new Buffer(this.readBuffer, 0);
        }
        byte[] data = new byte[len];
        readFromFile(data, offset, len);
        return new Buffer(data, 0);
    }
}
