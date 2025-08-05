package com.sun.imageio.plugins.common;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageInputStreamImpl;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/SubImageInputStream.class */
public final class SubImageInputStream extends ImageInputStreamImpl {
    ImageInputStream stream;
    long startingPos;
    int startingLength;
    int length;

    public SubImageInputStream(ImageInputStream imageInputStream, int i2) throws IOException {
        this.stream = imageInputStream;
        this.startingPos = imageInputStream.getStreamPosition();
        this.length = i2;
        this.startingLength = i2;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read() throws IOException {
        if (this.length == 0) {
            return -1;
        }
        this.length--;
        return this.stream.read();
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.length == 0) {
            return -1;
        }
        int i4 = this.stream.read(bArr, i2, Math.min(i3, this.length));
        this.length -= i4;
        return i4;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public long length() {
        return this.startingLength;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public void seek(long j2) throws IOException {
        this.stream.seek(j2 - this.startingPos);
        this.streamPos = j2;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl
    protected void finalize() throws Throwable {
    }
}
