package javax.naming.ldap;

import java.io.IOException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

/* loaded from: rt.jar:javax/naming/ldap/StartTlsResponse.class */
public abstract class StartTlsResponse implements ExtendedResponse {
    public static final String OID = "1.3.6.1.4.1.1466.20037";
    private static final long serialVersionUID = 8372842182579276418L;

    public abstract void setEnabledCipherSuites(String[] strArr);

    public abstract void setHostnameVerifier(HostnameVerifier hostnameVerifier);

    public abstract SSLSession negotiate() throws IOException;

    public abstract SSLSession negotiate(SSLSocketFactory sSLSocketFactory) throws IOException;

    public abstract void close() throws IOException;

    protected StartTlsResponse() {
    }

    @Override // javax.naming.ldap.ExtendedResponse
    public String getID() {
        return "1.3.6.1.4.1.1466.20037";
    }

    @Override // javax.naming.ldap.ExtendedResponse
    public byte[] getEncodedValue() {
        return null;
    }
}
