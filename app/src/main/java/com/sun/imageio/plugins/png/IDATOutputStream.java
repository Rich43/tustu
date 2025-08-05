package com.sun.imageio.plugins.png;

import java.io.IOException;
import java.util.zip.Deflater;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.ImageOutputStreamImpl;

/* compiled from: PNGImageWriter.java */
/* loaded from: rt.jar:com/sun/imageio/plugins/png/IDATOutputStream.class */
final class IDATOutputStream extends ImageOutputStreamImpl {
    private static byte[] chunkType = {73, 68, 65, 84};
    private ImageOutputStream stream;
    private int chunkLength;
    private long startPos;
    private CRC crc = new CRC();
    Deflater def = new Deflater(9);
    byte[] buf = new byte[512];
    private int bytesRemaining;

    public IDATOutputStream(ImageOutputStream imageOutputStream, int i2) throws IOException {
        this.stream = imageOutputStream;
        this.chunkLength = i2;
        startChunk();
    }

    private void startChunk() throws IOException {
        this.crc.reset();
        this.startPos = this.stream.getStreamPosition();
        this.stream.writeInt(-1);
        this.crc.update(chunkType, 0, 4);
        this.stream.write(chunkType, 0, 4);
        this.bytesRemaining = this.chunkLength;
    }

    private void finishChunk() throws IOException {
        this.stream.writeInt(this.crc.getValue());
        long streamPosition = this.stream.getStreamPosition();
        this.stream.seek(this.startPos);
        this.stream.writeInt(((int) (streamPosition - this.startPos)) - 12);
        this.stream.seek(streamPosition);
        this.stream.flushBefore(streamPosition);
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
        if (i3 != 0 && !this.def.finished()) {
            this.def.setInput(bArr, i2, i3);
            while (!this.def.needsInput()) {
                deflate();
            }
        }
    }

    public void deflate() throws IOException {
        int iDeflate = this.def.deflate(this.buf, 0, this.buf.length);
        int i2 = 0;
        while (iDeflate > 0) {
            if (this.bytesRemaining == 0) {
                finishChunk();
                startChunk();
            }
            int iMin = Math.min(iDeflate, this.bytesRemaining);
            this.crc.update(this.buf, i2, iMin);
            this.stream.write(this.buf, i2, iMin);
            i2 += iMin;
            iDeflate -= iMin;
            this.bytesRemaining -= iMin;
        }
    }

    @Override // javax.imageio.stream.ImageOutputStreamImpl, javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void write(int i2) throws IOException {
        write(new byte[]{(byte) i2}, 0, 1);
    }

    public void finish() throws IOException {
        try {
            if (!this.def.finished()) {
                this.def.finish();
                while (!this.def.finished()) {
                    deflate();
                }
            }
            finishChunk();
        } finally {
            this.def.end();
        }
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl
    protected void finalize() throws Throwable {
    }
}
