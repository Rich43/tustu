package net.lingala.zip4j.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/io/DeflaterOutputStream.class */
public class DeflaterOutputStream extends CipherOutputStream {
    private byte[] buff;
    protected Deflater deflater;
    private boolean firstBytesRead;

    public DeflaterOutputStream(OutputStream outputStream, ZipModel zipModel) {
        super(outputStream, zipModel);
        this.deflater = new Deflater();
        this.buff = new byte[4096];
        this.firstBytesRead = false;
    }

    @Override // net.lingala.zip4j.io.CipherOutputStream
    public void putNextEntry(File file, ZipParameters zipParameters) throws ZipException {
        super.putNextEntry(file, zipParameters);
        if (zipParameters.getCompressionMethod() == 8) {
            this.deflater.reset();
            if ((zipParameters.getCompressionLevel() < 0 || zipParameters.getCompressionLevel() > 9) && zipParameters.getCompressionLevel() != -1) {
                throw new ZipException("invalid compression level for deflater. compression level should be in the range of 0-9");
            }
            this.deflater.setLevel(zipParameters.getCompressionLevel());
        }
    }

    @Override // net.lingala.zip4j.io.CipherOutputStream, java.io.OutputStream
    public void write(byte[] b2) throws IOException {
        write(b2, 0, b2.length);
    }

    private void deflate() throws IOException {
        int len = this.deflater.deflate(this.buff, 0, this.buff.length);
        if (len > 0) {
            if (this.deflater.finished()) {
                if (len == 4) {
                    return;
                }
                if (len < 4) {
                    decrementCompressedFileSize(4 - len);
                    return;
                }
                len -= 4;
            }
            if (!this.firstBytesRead) {
                super.write(this.buff, 2, len - 2);
                this.firstBytesRead = true;
            } else {
                super.write(this.buff, 0, len);
            }
        }
    }

    @Override // net.lingala.zip4j.io.CipherOutputStream, net.lingala.zip4j.io.BaseOutputStream, java.io.OutputStream
    public void write(int bval) throws IOException {
        byte[] b2 = {(byte) bval};
        write(b2, 0, 1);
    }

    @Override // net.lingala.zip4j.io.CipherOutputStream, java.io.OutputStream
    public void write(byte[] buf, int off, int len) throws IOException {
        if (this.zipParameters.getCompressionMethod() != 8) {
            super.write(buf, off, len);
            return;
        }
        this.deflater.setInput(buf, off, len);
        while (!this.deflater.needsInput()) {
            deflate();
        }
    }

    @Override // net.lingala.zip4j.io.CipherOutputStream
    public void closeEntry() throws ZipException, IOException {
        if (this.zipParameters.getCompressionMethod() == 8) {
            if (!this.deflater.finished()) {
                this.deflater.finish();
                while (!this.deflater.finished()) {
                    deflate();
                }
            }
            this.firstBytesRead = false;
        }
        super.closeEntry();
    }

    @Override // net.lingala.zip4j.io.CipherOutputStream
    public void finish() throws ZipException, IOException {
        super.finish();
    }
}
