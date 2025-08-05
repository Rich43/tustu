package com.sun.net.ssl.internal.www.protocol.https;

import com.sun.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.security.Permission;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.security.cert.X509Certificate;

/* loaded from: rt.jar:com/sun/net/ssl/internal/www/protocol/https/HttpsURLConnectionOldImpl.class */
public class HttpsURLConnectionOldImpl extends HttpsURLConnection {
    private DelegateHttpsURLConnection delegate;

    HttpsURLConnectionOldImpl(URL url, Handler handler) throws IOException {
        this(url, null, handler);
    }

    static URL checkURL(URL url) throws IOException {
        if (url != null && url.toExternalForm().indexOf(10) > -1) {
            throw new MalformedURLException("Illegal character in URL");
        }
        return url;
    }

    HttpsURLConnectionOldImpl(URL url, Proxy proxy, Handler handler) throws IOException {
        super(checkURL(url));
        this.delegate = new DelegateHttpsURLConnection(this.url, proxy, handler, this);
    }

    protected void setNewClient(URL url) throws IOException {
        this.delegate.setNewClient(url, false);
    }

    protected void setNewClient(URL url, boolean z2) throws IOException {
        this.delegate.setNewClient(url, z2);
    }

    protected void setProxiedClient(URL url, String str, int i2) throws IOException {
        this.delegate.setProxiedClient(url, str, i2);
    }

    protected void setProxiedClient(URL url, String str, int i2, boolean z2) throws IOException {
        this.delegate.setProxiedClient(url, str, i2, z2);
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        this.delegate.connect();
    }

    protected boolean isConnected() {
        return this.delegate.isConnected();
    }

    protected void setConnected(boolean z2) {
        this.delegate.setConnected(z2);
    }

    @Override // com.sun.net.ssl.HttpsURLConnection
    public String getCipherSuite() {
        return this.delegate.getCipherSuite();
    }

    public Certificate[] getLocalCertificates() {
        return this.delegate.getLocalCertificates();
    }

    public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
        return this.delegate.getServerCertificates();
    }

    @Override // com.sun.net.ssl.HttpsURLConnection
    public X509Certificate[] getServerCertificateChain() {
        try {
            return this.delegate.getServerCertificateChain();
        } catch (SSLPeerUnverifiedException e2) {
            return null;
        }
    }

    @Override // java.net.URLConnection
    public synchronized OutputStream getOutputStream() throws IOException {
        return this.delegate.getOutputStream();
    }

    @Override // java.net.URLConnection
    public synchronized InputStream getInputStream() throws IOException {
        return this.delegate.getInputStream();
    }

    @Override // java.net.HttpURLConnection
    public InputStream getErrorStream() {
        return this.delegate.getErrorStream();
    }

    @Override // java.net.HttpURLConnection
    public void disconnect() {
        this.delegate.disconnect();
    }

    @Override // java.net.HttpURLConnection
    public boolean usingProxy() {
        return this.delegate.usingProxy();
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getHeaderFields() {
        return this.delegate.getHeaderFields();
    }

    @Override // java.net.URLConnection
    public String getHeaderField(String str) {
        return this.delegate.getHeaderField(str);
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderField(int i2) {
        return this.delegate.getHeaderField(i2);
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderFieldKey(int i2) {
        return this.delegate.getHeaderFieldKey(i2);
    }

    @Override // java.net.URLConnection
    public void setRequestProperty(String str, String str2) {
        this.delegate.setRequestProperty(str, str2);
    }

    @Override // java.net.URLConnection
    public void addRequestProperty(String str, String str2) {
        this.delegate.addRequestProperty(str, str2);
    }

    @Override // java.net.HttpURLConnection
    public int getResponseCode() throws IOException {
        return this.delegate.getResponseCode();
    }

    @Override // java.net.URLConnection
    public String getRequestProperty(String str) {
        return this.delegate.getRequestProperty(str);
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getRequestProperties() {
        return this.delegate.getRequestProperties();
    }

    @Override // java.net.HttpURLConnection
    public void setInstanceFollowRedirects(boolean z2) {
        this.delegate.setInstanceFollowRedirects(z2);
    }

    @Override // java.net.HttpURLConnection
    public boolean getInstanceFollowRedirects() {
        return this.delegate.getInstanceFollowRedirects();
    }

    @Override // java.net.HttpURLConnection
    public void setRequestMethod(String str) throws ProtocolException {
        this.delegate.setRequestMethod(str);
    }

    @Override // java.net.HttpURLConnection
    public String getRequestMethod() {
        return this.delegate.getRequestMethod();
    }

    @Override // java.net.HttpURLConnection
    public String getResponseMessage() throws IOException {
        return this.delegate.getResponseMessage();
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public long getHeaderFieldDate(String str, long j2) {
        return this.delegate.getHeaderFieldDate(str, j2);
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public Permission getPermission() throws IOException {
        return this.delegate.getPermission();
    }

    @Override // java.net.URLConnection
    public URL getURL() {
        return this.delegate.getURL();
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        return this.delegate.getContentLength();
    }

    @Override // java.net.URLConnection
    public long getContentLengthLong() {
        return this.delegate.getContentLengthLong();
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        return this.delegate.getContentType();
    }

    @Override // java.net.URLConnection
    public String getContentEncoding() {
        return this.delegate.getContentEncoding();
    }

    @Override // java.net.URLConnection
    public long getExpiration() {
        return this.delegate.getExpiration();
    }

    @Override // java.net.URLConnection
    public long getDate() {
        return this.delegate.getDate();
    }

    @Override // java.net.URLConnection
    public long getLastModified() {
        return this.delegate.getLastModified();
    }

    @Override // java.net.URLConnection
    public int getHeaderFieldInt(String str, int i2) {
        return this.delegate.getHeaderFieldInt(str, i2);
    }

    @Override // java.net.URLConnection
    public long getHeaderFieldLong(String str, long j2) {
        return this.delegate.getHeaderFieldLong(str, j2);
    }

    @Override // java.net.URLConnection
    public Object getContent() throws IOException {
        return this.delegate.getContent();
    }

    @Override // java.net.URLConnection
    public Object getContent(Class[] clsArr) throws IOException {
        return this.delegate.getContent(clsArr);
    }

    @Override // java.net.URLConnection
    public String toString() {
        return this.delegate.toString();
    }

    @Override // java.net.URLConnection
    public void setDoInput(boolean z2) {
        this.delegate.setDoInput(z2);
    }

    @Override // java.net.URLConnection
    public boolean getDoInput() {
        return this.delegate.getDoInput();
    }

    @Override // java.net.URLConnection
    public void setDoOutput(boolean z2) {
        this.delegate.setDoOutput(z2);
    }

    @Override // java.net.URLConnection
    public boolean getDoOutput() {
        return this.delegate.getDoOutput();
    }

    @Override // java.net.URLConnection
    public void setAllowUserInteraction(boolean z2) {
        this.delegate.setAllowUserInteraction(z2);
    }

    @Override // java.net.URLConnection
    public boolean getAllowUserInteraction() {
        return this.delegate.getAllowUserInteraction();
    }

    @Override // java.net.URLConnection
    public void setUseCaches(boolean z2) {
        this.delegate.setUseCaches(z2);
    }

    @Override // java.net.URLConnection
    public boolean getUseCaches() {
        return this.delegate.getUseCaches();
    }

    @Override // java.net.URLConnection
    public void setIfModifiedSince(long j2) {
        this.delegate.setIfModifiedSince(j2);
    }

    @Override // java.net.URLConnection
    public long getIfModifiedSince() {
        return this.delegate.getIfModifiedSince();
    }

    @Override // java.net.URLConnection
    public boolean getDefaultUseCaches() {
        return this.delegate.getDefaultUseCaches();
    }

    @Override // java.net.URLConnection
    public void setDefaultUseCaches(boolean z2) {
        this.delegate.setDefaultUseCaches(z2);
    }

    protected void finalize() throws Throwable {
        this.delegate.dispose();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    @Override // java.net.URLConnection
    public void setConnectTimeout(int i2) {
        this.delegate.setConnectTimeout(i2);
    }

    @Override // java.net.URLConnection
    public int getConnectTimeout() {
        return this.delegate.getConnectTimeout();
    }

    @Override // java.net.URLConnection
    public void setReadTimeout(int i2) {
        this.delegate.setReadTimeout(i2);
    }

    @Override // java.net.URLConnection
    public int getReadTimeout() {
        return this.delegate.getReadTimeout();
    }

    @Override // java.net.HttpURLConnection
    public void setFixedLengthStreamingMode(int i2) {
        this.delegate.setFixedLengthStreamingMode(i2);
    }

    @Override // java.net.HttpURLConnection
    public void setFixedLengthStreamingMode(long j2) {
        this.delegate.setFixedLengthStreamingMode(j2);
    }

    @Override // java.net.HttpURLConnection
    public void setChunkedStreamingMode(int i2) {
        this.delegate.setChunkedStreamingMode(i2);
    }
}
