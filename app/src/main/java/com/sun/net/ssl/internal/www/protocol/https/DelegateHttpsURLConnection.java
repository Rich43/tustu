package com.sun.net.ssl.internal.www.protocol.https;

import com.sun.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection;

/* loaded from: rt.jar:com/sun/net/ssl/internal/www/protocol/https/DelegateHttpsURLConnection.class */
public class DelegateHttpsURLConnection extends AbstractDelegateHttpsURLConnection {
    public HttpsURLConnection httpsURLConnection;

    DelegateHttpsURLConnection(URL url, sun.net.www.protocol.http.Handler handler, HttpsURLConnection httpsURLConnection) throws IOException {
        this(url, null, handler, httpsURLConnection);
    }

    DelegateHttpsURLConnection(URL url, Proxy proxy, sun.net.www.protocol.http.Handler handler, HttpsURLConnection httpsURLConnection) throws IOException {
        super(url, proxy, handler);
        this.httpsURLConnection = httpsURLConnection;
    }

    @Override // sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection
    protected SSLSocketFactory getSSLSocketFactory() {
        return this.httpsURLConnection.getSSLSocketFactory();
    }

    @Override // sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection
    protected HostnameVerifier getHostnameVerifier() {
        return new VerifierWrapper(this.httpsURLConnection.getHostnameVerifier());
    }

    protected void dispose() throws Throwable {
        super.finalize();
    }
}
