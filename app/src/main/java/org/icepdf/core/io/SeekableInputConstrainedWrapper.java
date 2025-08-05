package org.icepdf.core.io;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/SeekableInputConstrainedWrapper.class */
public class SeekableInputConstrainedWrapper extends InputStream {
    private SeekableInput streamDataInput;
    private long filePositionOfStreamData;
    private long lengthOfStreamData;
    private long filePositionBeforeUse = 0;
    private boolean usedYet = false;

    public SeekableInputConstrainedWrapper(SeekableInput in, long offset, long length) {
        this.streamDataInput = in;
        this.filePositionOfStreamData = offset;
        this.lengthOfStreamData = length;
    }

    private void ensureReadyOnFirstUse() throws IOException {
        if (this.usedYet) {
            return;
        }
        this.usedYet = true;
        this.filePositionBeforeUse = this.streamDataInput.getAbsolutePosition();
        this.streamDataInput.seekAbsolute(this.filePositionOfStreamData);
    }

    private long getBytesRemaining() throws IOException {
        long absPos = this.streamDataInput.getAbsolutePosition();
        if (absPos < this.filePositionOfStreamData) {
            return -1L;
        }
        long end = this.filePositionOfStreamData + this.lengthOfStreamData;
        return end - absPos;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        ensureReadyOnFirstUse();
        long remain = getBytesRemaining();
        if (remain <= 0) {
            return -1;
        }
        return this.streamDataInput.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        ensureReadyOnFirstUse();
        long remain = getBytesRemaining();
        if (remain <= 0) {
            return -1;
        }
        return this.streamDataInput.read(buffer, offset, (int) Math.min(Math.min(remain, length), 2147483647L));
    }

    @Override // java.io.InputStream
    public int available() {
        return 0;
    }

    @Override // java.io.InputStream
    public void mark(int readLimit) {
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
    }

    @Override // java.io.InputStream
    public long skip(long n2) throws IOException {
        ensureReadyOnFirstUse();
        long remain = getBytesRemaining();
        if (remain <= 0) {
            return -1L;
        }
        return this.streamDataInput.skip((int) Math.min(Math.min(remain, n2), 2147483647L));
    }

    public void seekAbsolute(long absolutePosition) throws IOException {
        ensureReadyOnFirstUse();
        if (absolutePosition < 0) {
            throw new IOException("Attempt to absolutely seek to negative location: " + absolutePosition);
        }
        this.streamDataInput.seekAbsolute(absolutePosition + this.filePositionOfStreamData);
    }

    public void seekRelative(long relativeOffset) throws IOException {
        ensureReadyOnFirstUse();
        long pos = this.streamDataInput.getAbsolutePosition() + relativeOffset;
        if (pos < this.filePositionOfStreamData) {
            pos = this.filePositionOfStreamData;
        }
        this.streamDataInput.seekAbsolute(pos);
    }

    public void seekEnd() throws IOException {
        ensureReadyOnFirstUse();
        this.streamDataInput.seekAbsolute(this.filePositionOfStreamData + this.lengthOfStreamData);
    }

    public long getAbsolutePosition() throws IOException {
        ensureReadyOnFirstUse();
        long absolutePosition = getAbsolutePosition();
        return absolutePosition - this.filePositionOfStreamData;
    }

    public long getLength() {
        return this.lengthOfStreamData;
    }

    public InputStream getInputStream() {
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" ( ");
        sb.append("pos=").append(this.filePositionOfStreamData).append(", ");
        sb.append("len=").append(this.lengthOfStreamData).append(", ");
        sb.append("posToRestore=").append(this.filePositionBeforeUse).append(", ");
        sb.append(" ) ");
        sb.append(": ");
        if (this.streamDataInput == null) {
            sb.append("null ");
        } else {
            sb.append(this.streamDataInput.toString());
        }
        return sb.toString();
    }
}
