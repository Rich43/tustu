package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.OutputStream;
import net.lingala.zip4j.model.ZipModel;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/io/ZipOutputStream.class */
public class ZipOutputStream extends DeflaterOutputStream {
    public ZipOutputStream(OutputStream outputStream) {
        this(outputStream, null);
    }

    public ZipOutputStream(OutputStream outputStream, ZipModel zipModel) {
        super(outputStream, zipModel);
    }

    @Override // net.lingala.zip4j.io.DeflaterOutputStream, net.lingala.zip4j.io.CipherOutputStream, net.lingala.zip4j.io.BaseOutputStream, java.io.OutputStream
    public void write(int bval) throws IOException {
        byte[] b2 = {(byte) bval};
        write(b2, 0, 1);
    }

    @Override // net.lingala.zip4j.io.DeflaterOutputStream, net.lingala.zip4j.io.CipherOutputStream, java.io.OutputStream
    public void write(byte[] b2) throws IOException {
        write(b2, 0, b2.length);
    }

    @Override // net.lingala.zip4j.io.DeflaterOutputStream, net.lingala.zip4j.io.CipherOutputStream, java.io.OutputStream
    public void write(byte[] b2, int off, int len) throws IOException {
        this.crc.update(b2, off, len);
        updateTotalBytesRead(len);
        super.write(b2, off, len);
    }
}
