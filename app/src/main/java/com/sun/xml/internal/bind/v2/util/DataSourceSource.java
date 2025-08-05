package com.sun.xml.internal.bind.v2.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.xml.transform.stream.StreamSource;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/util/DataSourceSource.class */
public final class DataSourceSource extends StreamSource {
    private final DataSource source;
    private final String charset;

    /* renamed from: r, reason: collision with root package name */
    private Reader f12074r;
    private InputStream is;

    public DataSourceSource(DataHandler dh) throws MimeTypeParseException {
        this(dh.getDataSource());
    }

    public DataSourceSource(DataSource source) throws MimeTypeParseException {
        this.source = source;
        String ct = source.getContentType();
        if (ct == null) {
            this.charset = null;
        } else {
            MimeType mimeType = new MimeType(ct);
            this.charset = mimeType.getParameter("charset");
        }
    }

    @Override // javax.xml.transform.stream.StreamSource
    public void setReader(Reader reader) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.transform.stream.StreamSource
    public void setInputStream(InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.transform.stream.StreamSource
    public Reader getReader() {
        try {
            if (this.charset == null) {
                return null;
            }
            if (this.f12074r == null) {
                this.f12074r = new InputStreamReader(this.source.getInputStream(), this.charset);
            }
            return this.f12074r;
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.xml.transform.stream.StreamSource
    public InputStream getInputStream() {
        try {
            if (this.charset != null) {
                return null;
            }
            if (this.is == null) {
                this.is = this.source.getInputStream();
            }
            return this.is;
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public DataSource getDataSource() {
        return this.source;
    }
}
