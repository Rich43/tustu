package org.apache.commons.net.ftp;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.commons.net.util.Base64;
import org.apache.commons.net.util.SSLContextUtils;
import org.apache.commons.net.util.SSLSocketUtils;
import org.apache.commons.net.util.TrustManagerUtils;
import org.icepdf.core.util.PdfOps;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/FTPSClient.class */
public class FTPSClient extends FTPClient {
    public static final int DEFAULT_FTPS_DATA_PORT = 989;
    public static final int DEFAULT_FTPS_PORT = 990;
    private static final String DEFAULT_PROTOCOL = "TLS";
    private static final String CMD_AUTH = "AUTH";
    private static final String CMD_ADAT = "ADAT";
    private static final String CMD_PROT = "PROT";
    private static final String CMD_PBSZ = "PBSZ";
    private static final String CMD_MIC = "MIC";
    private static final String CMD_CONF = "CONF";
    private static final String CMD_ENC = "ENC";
    private static final String CMD_CCC = "CCC";
    private final boolean isImplicit;
    private final String protocol;
    private String auth;
    private SSLContext context;
    private Socket plainSocket;
    private boolean isCreation;
    private boolean isClientMode;
    private boolean isNeedClientAuth;
    private boolean isWantClientAuth;
    private String[] suites;
    private String[] protocols;
    private TrustManager trustManager;
    private KeyManager keyManager;
    private HostnameVerifier hostnameVerifier;
    private boolean tlsEndpointChecking;

    @Deprecated
    public static String KEYSTORE_ALGORITHM;

    @Deprecated
    public static String TRUSTSTORE_ALGORITHM;

    @Deprecated
    public static String PROVIDER;

    @Deprecated
    public static String STORE_TYPE;
    private static final String DEFAULT_PROT = "C";
    private static final String[] PROT_COMMAND_VALUE = {DEFAULT_PROT, "E", PdfOps.S_TOKEN, Constants._TAG_P};

    public FTPSClient() {
        this("TLS", false);
    }

    public FTPSClient(boolean isImplicit) {
        this("TLS", isImplicit);
    }

    public FTPSClient(String protocol) {
        this(protocol, false);
    }

    public FTPSClient(String protocol, boolean isImplicit) {
        this.auth = "TLS";
        this.isCreation = true;
        this.isClientMode = true;
        this.isNeedClientAuth = false;
        this.isWantClientAuth = false;
        this.suites = null;
        this.protocols = null;
        this.trustManager = TrustManagerUtils.getValidateServerCertificateTrustManager();
        this.keyManager = null;
        this.hostnameVerifier = null;
        this.protocol = protocol;
        this.isImplicit = isImplicit;
        if (isImplicit) {
            setDefaultPort(DEFAULT_FTPS_PORT);
        }
    }

    public FTPSClient(boolean isImplicit, SSLContext context) {
        this("TLS", isImplicit);
        this.context = context;
    }

    public FTPSClient(SSLContext context) {
        this(false, context);
    }

    public void setAuthValue(String auth) {
        this.auth = auth;
    }

    public String getAuthValue() {
        return this.auth;
    }

    @Override // org.apache.commons.net.ftp.FTPClient, org.apache.commons.net.ftp.FTP, org.apache.commons.net.SocketClient
    protected void _connectAction_() throws IOException {
        if (this.isImplicit) {
            sslNegotiation();
        }
        super._connectAction_();
        if (!this.isImplicit) {
            execAUTH();
            sslNegotiation();
        }
    }

    protected void execAUTH() throws IOException {
        int replyCode = sendCommand(CMD_AUTH, this.auth);
        if (334 != replyCode && 234 != replyCode) {
            throw new SSLException(getReplyString());
        }
    }

    private void initSslContext() throws IOException {
        if (this.context == null) {
            this.context = SSLContextUtils.createSSLContext(this.protocol, getKeyManager(), getTrustManager());
        }
    }

    protected void sslNegotiation() throws IOException {
        this.plainSocket = this._socket_;
        initSslContext();
        SSLSocketFactory ssf = this.context.getSocketFactory();
        String host = this._hostname_ != null ? this._hostname_ : getRemoteAddress().getHostAddress();
        int port = this._socket_.getPort();
        SSLSocket socket = (SSLSocket) ssf.createSocket(this._socket_, host, port, false);
        socket.setEnableSessionCreation(this.isCreation);
        socket.setUseClientMode(this.isClientMode);
        if (this.isClientMode) {
            if (this.tlsEndpointChecking) {
                SSLSocketUtils.enableEndpointNameVerification(socket);
            }
        } else {
            socket.setNeedClientAuth(this.isNeedClientAuth);
            socket.setWantClientAuth(this.isWantClientAuth);
        }
        if (this.protocols != null) {
            socket.setEnabledProtocols(this.protocols);
        }
        if (this.suites != null) {
            socket.setEnabledCipherSuites(this.suites);
        }
        socket.startHandshake();
        this._socket_ = socket;
        this._controlInput_ = new BufferedReader(new InputStreamReader(socket.getInputStream(), getControlEncoding()));
        this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), getControlEncoding()));
        if (this.isClientMode && this.hostnameVerifier != null && !this.hostnameVerifier.verify(host, socket.getSession())) {
            throw new SSLHandshakeException("Hostname doesn't match certificate");
        }
    }

    private KeyManager getKeyManager() {
        return this.keyManager;
    }

    public void setKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public void setEnabledSessionCreation(boolean isCreation) {
        this.isCreation = isCreation;
    }

    public boolean getEnableSessionCreation() {
        if (this._socket_ instanceof SSLSocket) {
            return ((SSLSocket) this._socket_).getEnableSessionCreation();
        }
        return false;
    }

    public void setNeedClientAuth(boolean isNeedClientAuth) {
        this.isNeedClientAuth = isNeedClientAuth;
    }

    public boolean getNeedClientAuth() {
        if (this._socket_ instanceof SSLSocket) {
            return ((SSLSocket) this._socket_).getNeedClientAuth();
        }
        return false;
    }

    public void setWantClientAuth(boolean isWantClientAuth) {
        this.isWantClientAuth = isWantClientAuth;
    }

    public boolean getWantClientAuth() {
        if (this._socket_ instanceof SSLSocket) {
            return ((SSLSocket) this._socket_).getWantClientAuth();
        }
        return false;
    }

    public void setUseClientMode(boolean isClientMode) {
        this.isClientMode = isClientMode;
    }

    public boolean getUseClientMode() {
        if (this._socket_ instanceof SSLSocket) {
            return ((SSLSocket) this._socket_).getUseClientMode();
        }
        return false;
    }

    public void setEnabledCipherSuites(String[] cipherSuites) {
        this.suites = new String[cipherSuites.length];
        System.arraycopy(cipherSuites, 0, this.suites, 0, cipherSuites.length);
    }

    public String[] getEnabledCipherSuites() {
        if (this._socket_ instanceof SSLSocket) {
            return ((SSLSocket) this._socket_).getEnabledCipherSuites();
        }
        return null;
    }

    public void setEnabledProtocols(String[] protocolVersions) {
        this.protocols = new String[protocolVersions.length];
        System.arraycopy(protocolVersions, 0, this.protocols, 0, protocolVersions.length);
    }

    public String[] getEnabledProtocols() {
        if (this._socket_ instanceof SSLSocket) {
            return ((SSLSocket) this._socket_).getEnabledProtocols();
        }
        return null;
    }

    public void execPBSZ(long pbsz) throws IOException {
        if (pbsz < 0 || 4294967295L < pbsz) {
            throw new IllegalArgumentException();
        }
        int status = sendCommand(CMD_PBSZ, String.valueOf(pbsz));
        if (200 != status) {
            throw new SSLException(getReplyString());
        }
    }

    public long parsePBSZ(long pbsz) throws IOException, NumberFormatException {
        execPBSZ(pbsz);
        long minvalue = pbsz;
        String remainder = extractPrefixedData("PBSZ=", getReplyString());
        if (remainder != null) {
            long replysz = Long.parseLong(remainder);
            if (replysz < minvalue) {
                minvalue = replysz;
            }
        }
        return minvalue;
    }

    public void execPROT(String prot) throws IOException {
        if (prot == null) {
            prot = DEFAULT_PROT;
        }
        if (!checkPROTValue(prot)) {
            throw new IllegalArgumentException();
        }
        if (200 != sendCommand(CMD_PROT, prot)) {
            throw new SSLException(getReplyString());
        }
        if (DEFAULT_PROT.equals(prot)) {
            setSocketFactory(null);
            setServerSocketFactory(null);
        } else {
            setSocketFactory(new FTPSSocketFactory(this.context));
            setServerSocketFactory(new FTPSServerSocketFactory(this.context));
            initSslContext();
        }
    }

    private boolean checkPROTValue(String prot) {
        String[] arr$ = PROT_COMMAND_VALUE;
        for (String element : arr$) {
            if (element.equals(prot)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.net.ftp.FTP
    public int sendCommand(String command, String args) throws IOException {
        int repCode = super.sendCommand(command, args);
        if (CMD_CCC.equals(command)) {
            if (200 == repCode) {
                this._socket_.close();
                this._socket_ = this.plainSocket;
                this._controlInput_ = new BufferedReader(new InputStreamReader(this._socket_.getInputStream(), getControlEncoding()));
                this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(this._socket_.getOutputStream(), getControlEncoding()));
            } else {
                throw new SSLException(getReplyString());
            }
        }
        return repCode;
    }

    @Override // org.apache.commons.net.ftp.FTPClient
    @Deprecated
    protected Socket _openDataConnection_(int command, String arg) throws IOException {
        return _openDataConnection_(FTPCommand.getCommand(command), arg);
    }

    @Override // org.apache.commons.net.ftp.FTPClient
    protected Socket _openDataConnection_(String command, String arg) throws IOException {
        Socket socket = super._openDataConnection_(command, arg);
        _prepareDataSocket_(socket);
        if (socket instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket) socket;
            sslSocket.setUseClientMode(this.isClientMode);
            sslSocket.setEnableSessionCreation(this.isCreation);
            if (!this.isClientMode) {
                sslSocket.setNeedClientAuth(this.isNeedClientAuth);
                sslSocket.setWantClientAuth(this.isWantClientAuth);
            }
            if (this.suites != null) {
                sslSocket.setEnabledCipherSuites(this.suites);
            }
            if (this.protocols != null) {
                sslSocket.setEnabledProtocols(this.protocols);
            }
            sslSocket.startHandshake();
        }
        return socket;
    }

    protected void _prepareDataSocket_(Socket socket) throws IOException {
    }

    public TrustManager getTrustManager() {
        return this.trustManager;
    }

    public void setTrustManager(TrustManager trustManager) {
        this.trustManager = trustManager;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier newHostnameVerifier) {
        this.hostnameVerifier = newHostnameVerifier;
    }

    public boolean isEndpointCheckingEnabled() {
        return this.tlsEndpointChecking;
    }

    public void setEndpointCheckingEnabled(boolean enable) {
        this.tlsEndpointChecking = enable;
    }

    @Override // org.apache.commons.net.ftp.FTPClient, org.apache.commons.net.ftp.FTP, org.apache.commons.net.SocketClient
    public void disconnect() throws IOException {
        super.disconnect();
        if (this.plainSocket != null) {
            this.plainSocket.close();
        }
        setSocketFactory(null);
        setServerSocketFactory(null);
    }

    public int execAUTH(String mechanism) throws IOException {
        return sendCommand(CMD_AUTH, mechanism);
    }

    public int execADAT(byte[] data) throws IOException {
        if (data != null) {
            return sendCommand(CMD_ADAT, Base64.encodeBase64StringUnChunked(data));
        }
        return sendCommand(CMD_ADAT);
    }

    public int execCCC() throws IOException {
        int repCode = sendCommand(CMD_CCC);
        return repCode;
    }

    public int execMIC(byte[] data) throws IOException {
        if (data != null) {
            return sendCommand(CMD_MIC, Base64.encodeBase64StringUnChunked(data));
        }
        return sendCommand(CMD_MIC, "");
    }

    public int execCONF(byte[] data) throws IOException {
        if (data != null) {
            return sendCommand(CMD_CONF, Base64.encodeBase64StringUnChunked(data));
        }
        return sendCommand(CMD_CONF, "");
    }

    public int execENC(byte[] data) throws IOException {
        if (data != null) {
            return sendCommand(CMD_ENC, Base64.encodeBase64StringUnChunked(data));
        }
        return sendCommand(CMD_ENC, "");
    }

    public byte[] parseADATReply(String reply) {
        if (reply == null) {
            return null;
        }
        return Base64.decodeBase64(extractPrefixedData("ADAT=", reply));
    }

    private String extractPrefixedData(String prefix, String reply) {
        int idx = reply.indexOf(prefix);
        if (idx == -1) {
            return null;
        }
        return reply.substring(idx + prefix.length()).trim();
    }
}
