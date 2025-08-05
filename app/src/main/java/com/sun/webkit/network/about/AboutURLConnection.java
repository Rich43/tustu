package com.sun.webkit.network.about;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

/* loaded from: jfxrt.jar:com/sun/webkit/network/about/AboutURLConnection.class */
final class AboutURLConnection extends URLConnection {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_MIMETYPE = "text/html";
    private final AboutRecord record;

    AboutURLConnection(URL url) {
        super(url);
        this.record = new AboutRecord("");
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        if (this.connected) {
            return;
        }
        this.connected = this.record != null;
        if (!this.connected) {
            throw new ProtocolException("The URL is not valid and cannot be loaded.");
        }
        this.record.content.reset();
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        connect();
        return this.record.content;
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        try {
            connect();
            if (this.record.contentType == null) {
                return "text/html";
            }
            return this.record.contentType;
        } catch (IOException e2) {
            return "text/html";
        }
    }

    @Override // java.net.URLConnection
    public String getContentEncoding() {
        try {
            connect();
            if (this.record.contentEncoding == null) {
                return "UTF-8";
            }
            return this.record.contentEncoding;
        } catch (IOException e2) {
            return "UTF-8";
        }
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        try {
            connect();
            return this.record.contentLength;
        } catch (IOException e2) {
            return -1;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/about/AboutURLConnection$AboutRecord.class */
    private static final class AboutRecord {
        private final InputStream content;
        private final int contentLength;
        private final String contentEncoding;
        private final String contentType;

        private AboutRecord(String info) {
            byte[] bytes = new byte[0];
            try {
                bytes = info.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e2) {
            }
            this.content = new ByteArrayInputStream(bytes);
            this.contentLength = bytes.length;
            this.contentEncoding = "UTF-8";
            this.contentType = "text/html";
        }
    }
}
