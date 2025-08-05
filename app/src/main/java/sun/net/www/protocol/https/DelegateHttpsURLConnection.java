package sun.net.www.protocol.https;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/* loaded from: rt.jar:sun/net/www/protocol/https/DelegateHttpsURLConnection.class */
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
        return this.httpsURLConnection.getHostnameVerifier();
    }

    protected void dispose() throws Throwable {
        super.finalize();
    }
}
