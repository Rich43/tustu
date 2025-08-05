package javax.rmi.ssl;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.util.StringTokenizer;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/* loaded from: rt.jar:javax/rmi/ssl/SslRMIClientSocketFactory.class */
public class SslRMIClientSocketFactory implements RMIClientSocketFactory, Serializable {
    private static SocketFactory defaultSocketFactory = null;
    private static final long serialVersionUID = -8310631444933958385L;

    @Override // java.rmi.server.RMIClientSocketFactory
    public Socket createSocket(String str, int i2) throws IOException {
        SSLSocket sSLSocket = (SSLSocket) getDefaultClientSocketFactory().createSocket(str, i2);
        String property = System.getProperty("javax.rmi.ssl.client.enabledCipherSuites");
        if (property != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(property, ",");
            int iCountTokens = stringTokenizer.countTokens();
            String[] strArr = new String[iCountTokens];
            for (int i3 = 0; i3 < iCountTokens; i3++) {
                strArr[i3] = stringTokenizer.nextToken();
            }
            try {
                sSLSocket.setEnabledCipherSuites(strArr);
            } catch (IllegalArgumentException e2) {
                throw ((IOException) new IOException(e2.getMessage()).initCause(e2));
            }
        }
        String property2 = System.getProperty("javax.rmi.ssl.client.enabledProtocols");
        if (property2 != null) {
            StringTokenizer stringTokenizer2 = new StringTokenizer(property2, ",");
            int iCountTokens2 = stringTokenizer2.countTokens();
            String[] strArr2 = new String[iCountTokens2];
            for (int i4 = 0; i4 < iCountTokens2; i4++) {
                strArr2[i4] = stringTokenizer2.nextToken();
            }
            try {
                sSLSocket.setEnabledProtocols(strArr2);
            } catch (IllegalArgumentException e3) {
                throw ((IOException) new IOException(e3.getMessage()).initCause(e3));
            }
        }
        return sSLSocket;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return getClass().equals(obj.getClass());
    }

    public int hashCode() {
        return getClass().hashCode();
    }

    private static synchronized SocketFactory getDefaultClientSocketFactory() {
        if (defaultSocketFactory == null) {
            defaultSocketFactory = SSLSocketFactory.getDefault();
        }
        return defaultSocketFactory;
    }
}
