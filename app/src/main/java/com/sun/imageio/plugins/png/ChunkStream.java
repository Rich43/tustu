package com.sun.imageio.plugins.png;

import java.io.IOException;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.ImageOutputStreamImpl;

/* compiled from: PNGImageWriter.java */
/* loaded from: rt.jar:com/sun/imageio/plugins/png/ChunkStream.class */
final class ChunkStream extends ImageOutputStreamImpl {
    private ImageOutputStream stream;
    private long startPos;
    private CRC crc = new CRC();

    public ChunkStream(int i2, ImageOutputStream imageOutputStream) throws IOException {
        this.stream = imageOutputStream;
        this.startPos = imageOutputStream.getStreamPosition();
        imageOutputStream.writeInt(-1);
        writeInt(i2);
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read() throws IOException {
        throw new RuntimeException("Method not available");
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        throw new RuntimeException("Method not available");
    }

    @Override // javax.imageio.stream.ImageOutputStreamImpl, javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        this.crc.update(bArr, i2, i3);
        this.stream.write(bArr, i2, i3);
    }

    @Override // javax.imageio.stream.ImageOutputStreamImpl, javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void write(int i2) throws IOException {
        this.crc.update(i2);
        this.stream.write(i2);
    }

    public void finish() throws IOException {
        this.stream.writeInt(this.crc.getValue());
        long streamPosition = this.stream.getStreamPosition();
        this.stream.seek(this.startPos);
        this.stream.writeInt(((int) (streamPosition - this.startPos)) - 12);
        this.stream.seek(streamPosition);
        this.stream.flushBefore(streamPosition);
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl
    protected void finalize() throws Throwable {
    }
}
