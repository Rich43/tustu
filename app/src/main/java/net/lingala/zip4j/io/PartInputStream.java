package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import net.lingala.zip4j.crypto.AESDecrypter;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.unzip.UnzipEngine;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/io/PartInputStream.class */
public class PartInputStream extends BaseInputStream {
    private RandomAccessFile raf;
    private long length;
    private UnzipEngine unzipEngine;
    private IDecrypter decrypter;
    private boolean isAESEncryptedFile;
    private byte[] oneByteBuff = new byte[1];
    private byte[] aesBlockByte = new byte[16];
    private int aesBytesReturned = 0;
    private int count = -1;
    private long bytesRead = 0;

    public PartInputStream(RandomAccessFile raf, long start, long len, UnzipEngine unzipEngine) {
        this.isAESEncryptedFile = false;
        this.raf = raf;
        this.unzipEngine = unzipEngine;
        this.decrypter = unzipEngine.getDecrypter();
        this.length = len;
        this.isAESEncryptedFile = unzipEngine.getFileHeader().isEncrypted() && unzipEngine.getFileHeader().getEncryptionMethod() == 99;
    }

    @Override // net.lingala.zip4j.io.BaseInputStream, java.io.InputStream
    public int available() {
        long amount = this.length - this.bytesRead;
        if (amount > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) amount;
    }

    @Override // net.lingala.zip4j.io.BaseInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.bytesRead >= this.length) {
            return -1;
        }
        if (this.isAESEncryptedFile) {
            if (this.aesBytesReturned == 0 || this.aesBytesReturned == 16) {
                if (read(this.aesBlockByte) == -1) {
                    return -1;
                }
                this.aesBytesReturned = 0;
            }
            byte[] bArr = this.aesBlockByte;
            int i2 = this.aesBytesReturned;
            this.aesBytesReturned = i2 + 1;
            return bArr[i2] & 255;
        }
        if (read(this.oneByteBuff, 0, 1) == -1) {
            return -1;
        }
        return this.oneByteBuff[0] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2) throws IOException {
        return read(b2, 0, b2.length);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.io.RandomAccessFile] */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Throwable] */
    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int len) throws IOException {
        if (len > this.length - this.bytesRead) {
            len = (int) (this.length - this.bytesRead);
            if (len == 0) {
                checkAndReadAESMacBytes();
                return -1;
            }
        }
        if ((this.unzipEngine.getDecrypter() instanceof AESDecrypter) && this.bytesRead + len < this.length && len % 16 != 0) {
            len -= len % 16;
        }
        ?? r0 = this.raf;
        synchronized (r0) {
            this.count = this.raf.read(b2, off, len);
            if (this.count < len && this.unzipEngine.getZipModel().isSplitArchive()) {
                this.raf.close();
                this.raf = this.unzipEngine.startNextSplitFile();
                if (this.count < 0) {
                    this.count = 0;
                }
                int newlyRead = this.raf.read(b2, this.count, len - this.count);
                if (newlyRead > 0) {
                    this.count += newlyRead;
                }
            }
            r0 = r0;
            if (this.count > 0) {
                if (this.decrypter != null) {
                    try {
                        this.decrypter.decryptData(b2, off, this.count);
                    } catch (ZipException e2) {
                        throw new IOException(e2.getMessage());
                    }
                }
                this.bytesRead += this.count;
            }
            if (this.bytesRead >= this.length) {
                checkAndReadAESMacBytes();
            }
            return this.count;
        }
    }

    private void checkAndReadAESMacBytes() throws IOException {
        if (!this.isAESEncryptedFile || this.decrypter == null || !(this.decrypter instanceof AESDecrypter) || ((AESDecrypter) this.decrypter).getStoredMac() != null) {
            return;
        }
        byte[] macBytes = new byte[10];
        int readLen = this.raf.read(macBytes);
        if (readLen != 10) {
            if (this.unzipEngine.getZipModel().isSplitArchive()) {
                this.raf.close();
                this.raf = this.unzipEngine.startNextSplitFile();
                int newlyRead = this.raf.read(macBytes, readLen, 10 - readLen);
                int i2 = readLen + newlyRead;
            } else {
                throw new IOException("Error occured while reading stored AES authentication bytes");
            }
        }
        ((AESDecrypter) this.unzipEngine.getDecrypter()).setStoredMac(macBytes);
    }

    @Override // java.io.InputStream
    public long skip(long amount) throws IOException {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        if (amount > this.length - this.bytesRead) {
            amount = this.length - this.bytesRead;
        }
        this.bytesRead += amount;
        return amount;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.raf.close();
    }

    @Override // net.lingala.zip4j.io.BaseInputStream
    public void seek(long pos) throws IOException {
        this.raf.seek(pos);
    }

    @Override // net.lingala.zip4j.io.BaseInputStream
    public UnzipEngine getUnzipEngine() {
        return this.unzipEngine;
    }
}
