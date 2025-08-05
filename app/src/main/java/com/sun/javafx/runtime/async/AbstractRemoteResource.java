package com.sun.javafx.runtime.async;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/runtime/async/AbstractRemoteResource.class */
public abstract class AbstractRemoteResource<T> extends AbstractAsyncOperation<T> {
    protected final String url;
    protected final String method;
    protected final String outboundContent;
    protected int fileSize;
    private Map<String, String> headers;
    private Map<String, List<String>> responseHeaders;

    protected abstract T processStream(InputStream inputStream) throws IOException;

    protected AbstractRemoteResource(String url, AsyncOperationListener<T> listener) {
        this(url, "GET", listener);
    }

    protected AbstractRemoteResource(String url, String method, AsyncOperationListener<T> listener) {
        this(url, method, null, listener);
    }

    protected AbstractRemoteResource(String url, String method, String outboundContent, AsyncOperationListener<T> listener) {
        super(listener);
        this.headers = new HashMap();
        this.responseHeaders = new HashMap();
        this.url = url;
        this.method = method;
        this.outboundContent = outboundContent;
    }

    @Override // java.util.concurrent.Callable
    public T call() throws IOException {
        InputStream stream;
        URL u2 = new URL(this.url);
        String protocol = u2.getProtocol();
        if (protocol.equals("http") || protocol.equals("https")) {
            HttpURLConnection conn = (HttpURLConnection) u2.openConnection();
            conn.setRequestMethod(this.method);
            conn.setDoInput(true);
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value != null && !value.equals("")) {
                    conn.setRequestProperty(key, value);
                }
            }
            if (this.outboundContent != null && this.method.equals("POST")) {
                conn.setDoOutput(true);
                byte[] outBytes = this.outboundContent.getBytes("utf-8");
                conn.setRequestProperty("Content-Length", String.valueOf(outBytes.length));
                OutputStream out = conn.getOutputStream();
                out.write(outBytes);
                out.close();
            }
            conn.connect();
            this.fileSize = conn.getContentLength();
            setProgressMax(this.fileSize);
            this.responseHeaders = conn.getHeaderFields();
            stream = new ProgressInputStream(conn.getInputStream());
        } else {
            URLConnection con = u2.openConnection();
            setProgressMax(con.getContentLength());
            stream = new ProgressInputStream(con.getInputStream());
        }
        try {
            T tProcessStream = processStream(stream);
            stream.close();
            return tProcessStream;
        } catch (Throwable th) {
            stream.close();
            throw th;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/runtime/async/AbstractRemoteResource$ProgressInputStream.class */
    protected class ProgressInputStream extends BufferedInputStream {
        public ProgressInputStream(InputStream in) {
            super(in);
        }

        @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
        public synchronized int read() throws IOException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException();
            }
            int ch = super.read();
            AbstractRemoteResource.this.addProgress(1);
            return ch;
        }

        @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
        public synchronized int read(byte[] b2, int off, int len) throws IOException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException();
            }
            int bytes = super.read(b2, off, len);
            AbstractRemoteResource.this.addProgress(bytes);
            return bytes;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] b2) throws IOException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException();
            }
            int bytes = super.read(b2);
            AbstractRemoteResource.this.addProgress(bytes);
            return bytes;
        }
    }

    public void setHeader(String header, String value) {
        this.headers.put(header, value);
    }

    public String getResponseHeader(String header) {
        String value = null;
        List<String> list = this.responseHeaders.get(header);
        if (list != null) {
            StringBuilder sb = new StringBuilder();
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                sb.append((Object) iter.next());
                if (iter.hasNext()) {
                    sb.append(',');
                }
            }
            value = sb.toString();
        }
        return value;
    }
}
