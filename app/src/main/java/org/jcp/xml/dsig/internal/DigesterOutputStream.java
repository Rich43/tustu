package org.jcp.xml.dsig.internal;

import com.sun.org.apache.xml.internal.security.utils.UnsyncByteArrayOutputStream;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/DigesterOutputStream.class */
public class DigesterOutputStream extends OutputStream {
    private static final Logger LOG = LoggerFactory.getLogger(DigesterOutputStream.class);
    private final boolean buffer;
    private UnsyncByteArrayOutputStream bos;
    private final MessageDigest md;

    public DigesterOutputStream(MessageDigest messageDigest) {
        this(messageDigest, false);
    }

    public DigesterOutputStream(MessageDigest messageDigest, boolean z2) {
        this.md = messageDigest;
        this.buffer = z2;
        if (z2) {
            this.bos = new UnsyncByteArrayOutputStream();
        }
    }

    @Override // java.io.OutputStream
    public void write(int i2) {
        if (this.buffer) {
            this.bos.write(i2);
        }
        this.md.update((byte) i2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) {
        if (this.buffer) {
            this.bos.write(bArr, i2, i3);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Pre-digested input:");
            StringBuilder sb = new StringBuilder(i3);
            for (int i4 = i2; i4 < i2 + i3; i4++) {
                sb.append((char) bArr[i4]);
            }
            LOG.debug(sb.toString());
        }
        this.md.update(bArr, i2, i3);
    }

    public byte[] getDigestValue() {
        return this.md.digest();
    }

    public InputStream getInputStream() {
        if (this.buffer) {
            return new ByteArrayInputStream(this.bos.toByteArray());
        }
        return null;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.buffer) {
            this.bos.close();
        }
    }
}
