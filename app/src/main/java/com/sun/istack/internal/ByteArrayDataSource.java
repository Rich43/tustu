package com.sun.istack.internal;

import com.sun.media.jfxmedia.locator.Locator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/istack/internal/ByteArrayDataSource.class */
public final class ByteArrayDataSource implements DataSource {
    private final String contentType;
    private final byte[] buf;
    private final int len;

    public ByteArrayDataSource(byte[] buf, String contentType) {
        this(buf, buf.length, contentType);
    }

    public ByteArrayDataSource(byte[] buf, int length, String contentType) {
        this.buf = buf;
        this.len = length;
        this.contentType = contentType;
    }

    @Override // javax.activation.DataSource
    public String getContentType() {
        if (this.contentType == null) {
            return Locator.DEFAULT_CONTENT_TYPE;
        }
        return this.contentType;
    }

    @Override // javax.activation.DataSource
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.buf, 0, this.len);
    }

    @Override // javax.activation.DataSource
    public String getName() {
        return null;
    }

    @Override // javax.activation.DataSource
    public OutputStream getOutputStream() {
        throw new UnsupportedOperationException();
    }
}
