package com.sun.jndi.ldap.ext;

import com.sun.jndi.ldap.Connection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.naming.ldap.StartTlsResponse;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import sun.security.util.HostnameChecker;

/* loaded from: rt.jar:com/sun/jndi/ldap/ext/StartTlsResponseImpl.class */
public final class StartTlsResponseImpl extends StartTlsResponse {
    private static final boolean debug = false;
    private static final int DNSNAME_TYPE = 2;
    private transient String hostname = null;
    private transient Connection ldapConnection = null;
    private transient InputStream originalInputStream = null;
    private transient OutputStream originalOutputStream = null;
    private transient SSLSocket sslSocket = null;
    private transient SSLSocketFactory defaultFactory = null;
    private transient SSLSocketFactory currentFactory = null;
    private transient String[] suites = null;
    private transient HostnameVerifier verifier = null;
    private transient boolean isClosed = true;
    private static final long serialVersionUID = -1126624615143411328L;

    @Override // javax.naming.ldap.StartTlsResponse
    public void setEnabledCipherSuites(String[] strArr) {
        this.suites = strArr == null ? null : (String[]) strArr.clone();
    }

    @Override // javax.naming.ldap.StartTlsResponse
    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.verifier = hostnameVerifier;
    }

    @Override // javax.naming.ldap.StartTlsResponse
    public SSLSession negotiate() throws IOException {
        return negotiate(null);
    }

    @Override // javax.naming.ldap.StartTlsResponse
    public SSLSession negotiate(SSLSocketFactory sSLSocketFactory) throws IOException {
        if (this.isClosed && this.sslSocket != null) {
            throw new IOException("TLS connection is closed.");
        }
        if (sSLSocketFactory == null) {
            sSLSocketFactory = getDefaultFactory();
        }
        SSLSession session = startHandshake(sSLSocketFactory).getSession();
        SSLPeerUnverifiedException sSLPeerUnverifiedException = null;
        try {
            if (verify(this.hostname, session)) {
                this.isClosed = false;
                return session;
            }
        } catch (SSLPeerUnverifiedException e2) {
            sSLPeerUnverifiedException = e2;
        }
        if (this.verifier != null && this.verifier.verify(this.hostname, session)) {
            this.isClosed = false;
            return session;
        }
        close();
        session.invalidate();
        if (sSLPeerUnverifiedException == null) {
            sSLPeerUnverifiedException = new SSLPeerUnverifiedException("hostname of the server '" + this.hostname + "' does not match the hostname in the server's certificate.");
        }
        throw sSLPeerUnverifiedException;
    }

    @Override // javax.naming.ldap.StartTlsResponse
    public void close() throws IOException {
        if (this.isClosed) {
            return;
        }
        this.ldapConnection.replaceStreams(this.originalInputStream, this.originalOutputStream, false);
        this.sslSocket.close();
        this.isClosed = true;
    }

    public void setConnection(Connection connection, String str) {
        this.ldapConnection = connection;
        this.hostname = (str == null || str.isEmpty()) ? connection.host : str;
        this.originalInputStream = connection.inStream;
        this.originalOutputStream = connection.outStream;
    }

    private SSLSocketFactory getDefaultFactory() throws IOException {
        if (this.defaultFactory != null) {
            return this.defaultFactory;
        }
        SSLSocketFactory sSLSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.defaultFactory = sSLSocketFactory;
        return sSLSocketFactory;
    }

    private SSLSocket startHandshake(SSLSocketFactory sSLSocketFactory) throws IOException {
        if (this.ldapConnection == null) {
            throw new IllegalStateException("LDAP connection has not been set. TLS requires an existing LDAP connection.");
        }
        if (sSLSocketFactory != this.currentFactory) {
            this.sslSocket = (SSLSocket) sSLSocketFactory.createSocket(this.ldapConnection.sock, this.ldapConnection.host, this.ldapConnection.port, false);
            this.currentFactory = sSLSocketFactory;
        }
        if (this.suites != null) {
            this.sslSocket.setEnabledCipherSuites(this.suites);
        }
        try {
            this.sslSocket.startHandshake();
            this.ldapConnection.replaceStreams(this.sslSocket.getInputStream(), this.sslSocket.getOutputStream(), true);
            return this.sslSocket;
        } catch (IOException e2) {
            this.sslSocket.close();
            this.isClosed = true;
            throw e2;
        }
    }

    private boolean verify(String str, SSLSession sSLSession) throws SSLPeerUnverifiedException {
        if (str != null && str.startsWith("[") && str.endsWith("]")) {
            str = str.substring(1, str.length() - 1);
        }
        try {
            HostnameChecker hostnameChecker = HostnameChecker.getInstance((byte) 2);
            if (sSLSession.getCipherSuite().startsWith("TLS_KRB5")) {
                Principal peerPrincipal = getPeerPrincipal(sSLSession);
                if (!HostnameChecker.match(str, peerPrincipal)) {
                    throw new SSLPeerUnverifiedException("hostname of the kerberos principal:" + ((Object) peerPrincipal) + " does not match the hostname:" + str);
                }
                return true;
            }
            Certificate[] peerCertificates = sSLSession.getPeerCertificates();
            if (!(peerCertificates[0] instanceof X509Certificate)) {
                throw new SSLPeerUnverifiedException("Received a non X509Certificate from the server");
            }
            hostnameChecker.match(str, (X509Certificate) peerCertificates[0]);
            return true;
        } catch (CertificateException e2) {
            throw ((SSLPeerUnverifiedException) new SSLPeerUnverifiedException("hostname of the server '" + str + "' does not match the hostname in the server's certificate.").initCause(e2));
        } catch (SSLPeerUnverifiedException e3) {
            String cipherSuite = sSLSession.getCipherSuite();
            if (cipherSuite != null && cipherSuite.indexOf("_anon_") != -1) {
                return true;
            }
            throw e3;
        }
    }

    private static Principal getPeerPrincipal(SSLSession sSLSession) throws SSLPeerUnverifiedException {
        Principal peerPrincipal;
        try {
            peerPrincipal = sSLSession.getPeerPrincipal();
        } catch (AbstractMethodError e2) {
            peerPrincipal = null;
        }
        return peerPrincipal;
    }
}
