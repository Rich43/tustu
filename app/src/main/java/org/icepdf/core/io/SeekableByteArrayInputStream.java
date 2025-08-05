package org.icepdf.core.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/SeekableByteArrayInputStream.class */
public class SeekableByteArrayInputStream extends ByteArrayInputStream implements SeekableInput {
    private static final Logger log = Logger.getLogger(SeekableByteArrayInputStream.class.toString());
    private int m_iBeginningOffset;
    private final ReentrantLock lock;

    public SeekableByteArrayInputStream(byte[] buf) {
        super(buf);
        this.lock = new ReentrantLock();
        this.m_iBeginningOffset = 0;
    }

    public SeekableByteArrayInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
        this.lock = new ReentrantLock();
        this.m_iBeginningOffset = offset;
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void seekAbsolute(long absolutePosition) {
        int absPos = (int) (absolutePosition & (-1));
        this.pos = this.m_iBeginningOffset + absPos;
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void seekRelative(long relativeOffset) {
        int relOff = (int) (relativeOffset & (-1));
        int currPos = this.pos + relOff;
        if (currPos < this.m_iBeginningOffset) {
            currPos = this.m_iBeginningOffset;
        }
        this.pos = currPos;
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void seekEnd() {
        seekAbsolute(getLength());
    }

    @Override // org.icepdf.core.io.SeekableInput
    public long getAbsolutePosition() {
        int absPos = this.pos - this.m_iBeginningOffset;
        return absPos & (-1);
    }

    @Override // org.icepdf.core.io.SeekableInput
    public long getLength() {
        int len = this.count - this.m_iBeginningOffset;
        return len & (-1);
    }

    @Override // org.icepdf.core.io.SeekableInput
    public InputStream getInputStream() {
        return this;
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void beginThreadAccess() {
        this.lock.lock();
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void endThreadAccess() {
        this.lock.unlock();
    }
}
