package net.lingala.zip4j.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/io/SplitOutputStream.class */
public class SplitOutputStream extends OutputStream {
    private RandomAccessFile raf;
    private long splitLength;
    private File zipFile;
    private File outFile;
    private int currSplitFileCounter;
    private long bytesWrittenForThisPart;

    public SplitOutputStream(String name) throws ZipException, FileNotFoundException {
        this(Zip4jUtil.isStringNotNullAndNotEmpty(name) ? new File(name) : null);
    }

    public SplitOutputStream(File file) throws ZipException, FileNotFoundException {
        this(file, -1L);
    }

    public SplitOutputStream(String name, long splitLength) throws ZipException, FileNotFoundException {
        this(!Zip4jUtil.isStringNotNullAndNotEmpty(name) ? new File(name) : null, splitLength);
    }

    public SplitOutputStream(File file, long splitLength) throws ZipException, FileNotFoundException {
        if (splitLength >= 0 && splitLength < 65536) {
            throw new ZipException("split length less than minimum allowed split length of 65536 Bytes");
        }
        this.raf = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
        this.splitLength = splitLength;
        this.outFile = file;
        this.zipFile = file;
        this.currSplitFileCounter = 0;
        this.bytesWrittenForThisPart = 0L;
    }

    @Override // java.io.OutputStream
    public void write(int b2) throws IOException {
        byte[] buff = {(byte) b2};
        write(buff, 0, 1);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2) throws IOException {
        write(b2, 0, b2.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2, int off, int len) throws IOException {
        if (len <= 0) {
            return;
        }
        if (this.splitLength == -1) {
            this.raf.write(b2, off, len);
            this.bytesWrittenForThisPart += len;
            return;
        }
        if (this.splitLength < 65536) {
            throw new IOException("split length less than minimum allowed split length of 65536 Bytes");
        }
        if (this.bytesWrittenForThisPart >= this.splitLength) {
            startNextSplitFile();
            this.raf.write(b2, off, len);
            this.bytesWrittenForThisPart = len;
        } else {
            if (this.bytesWrittenForThisPart + len > this.splitLength) {
                if (isHeaderData(b2)) {
                    startNextSplitFile();
                    this.raf.write(b2, off, len);
                    this.bytesWrittenForThisPart = len;
                    return;
                } else {
                    this.raf.write(b2, off, (int) (this.splitLength - this.bytesWrittenForThisPart));
                    startNextSplitFile();
                    this.raf.write(b2, off + ((int) (this.splitLength - this.bytesWrittenForThisPart)), (int) (len - (this.splitLength - this.bytesWrittenForThisPart)));
                    this.bytesWrittenForThisPart = len - (this.splitLength - this.bytesWrittenForThisPart);
                    return;
                }
            }
            this.raf.write(b2, off, len);
            this.bytesWrittenForThisPart += len;
        }
    }

    private void startNextSplitFile() throws IOException {
        File currSplitFile;
        try {
            String zipFileWithoutExt = Zip4jUtil.getZipFileNameWithoutExt(this.outFile.getName());
            String zipFileName = this.zipFile.getAbsolutePath();
            if (this.currSplitFileCounter < 9) {
                currSplitFile = new File(new StringBuffer(String.valueOf(this.outFile.getParent())).append(System.getProperty("file.separator")).append(zipFileWithoutExt).append(".z0").append(this.currSplitFileCounter + 1).toString());
            } else {
                currSplitFile = new File(new StringBuffer(String.valueOf(this.outFile.getParent())).append(System.getProperty("file.separator")).append(zipFileWithoutExt).append(".z").append(this.currSplitFileCounter + 1).toString());
            }
            this.raf.close();
            if (currSplitFile.exists()) {
                throw new IOException(new StringBuffer("split file: ").append(currSplitFile.getName()).append(" already exists in the current directory, cannot rename this file").toString());
            }
            if (!this.zipFile.renameTo(currSplitFile)) {
                throw new IOException("cannot rename newly created split file");
            }
            this.zipFile = new File(zipFileName);
            this.raf = new RandomAccessFile(this.zipFile, InternalZipConstants.WRITE_MODE);
            this.currSplitFileCounter++;
        } catch (ZipException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    private boolean isHeaderData(byte[] buff) {
        if (buff == null || buff.length < 4) {
            return false;
        }
        int signature = Raw.readIntLittleEndian(buff, 0);
        long[] allHeaderSignatures = Zip4jUtil.getAllHeaderSignatures();
        if (allHeaderSignatures != null && allHeaderSignatures.length > 0) {
            for (int i2 = 0; i2 < allHeaderSignatures.length; i2++) {
                if (allHeaderSignatures[i2] != 134695760 && allHeaderSignatures[i2] == signature) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean checkBuffSizeAndStartNextSplitFile(int bufferSize) throws ZipException {
        if (bufferSize < 0) {
            throw new ZipException("negative buffersize for checkBuffSizeAndStartNextSplitFile");
        }
        if (!isBuffSizeFitForCurrSplitFile(bufferSize)) {
            try {
                startNextSplitFile();
                this.bytesWrittenForThisPart = 0L;
                return true;
            } catch (IOException e2) {
                throw new ZipException(e2);
            }
        }
        return false;
    }

    public boolean isBuffSizeFitForCurrSplitFile(int bufferSize) throws ZipException {
        if (bufferSize < 0) {
            throw new ZipException("negative buffersize for isBuffSizeFitForCurrSplitFile");
        }
        return this.splitLength < 65536 || this.bytesWrittenForThisPart + ((long) bufferSize) <= this.splitLength;
    }

    public void seek(long pos) throws IOException {
        this.raf.seek(pos);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.raf != null) {
            this.raf.close();
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
    }

    public long getFilePointer() throws IOException {
        return this.raf.getFilePointer();
    }

    public boolean isSplitZipFile() {
        return this.splitLength != -1;
    }

    public long getSplitLength() {
        return this.splitLength;
    }

    public int getCurrSplitFileCounter() {
        return this.currSplitFileCounter;
    }
}
