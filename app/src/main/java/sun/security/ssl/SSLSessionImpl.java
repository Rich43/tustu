package sun.security.ssl;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.crypto.SecretKey;
import javax.net.ssl.ExtendedSSLSession;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLPermission;
import javax.net.ssl.SSLSessionBindingEvent;
import javax.net.ssl.SSLSessionBindingListener;
import javax.net.ssl.SSLSessionContext;
import javax.security.cert.CertificateException;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.ssl.CipherSuite;

/* loaded from: jsse.jar:sun/security/ssl/SSLSessionImpl.class */
final class SSLSessionImpl extends ExtendedSSLSession {
    private final ProtocolVersion protocolVersion;
    private final SessionId sessionId;
    private X509Certificate[] peerCerts;
    private Principal peerPrincipal;
    private CipherSuite cipherSuite;
    private SecretKey masterSecret;
    final boolean useExtendedMasterSecret;
    private final long creationTime;
    private long lastUsedTime;
    private final String host;
    private final int port;
    private SSLSessionContextImpl context;
    private boolean invalidated;
    private X509Certificate[] localCerts;
    private Principal localPrincipal;
    private PrivateKey localPrivateKey;
    private final Collection<SignatureScheme> localSupportedSignAlgs;
    private String[] peerSupportedSignAlgs;
    private boolean useDefaultPeerSignAlgs;
    private List<byte[]> statusResponses;
    private SecretKey resumptionMasterSecret;
    private SecretKey preSharedKey;
    private byte[] pskIdentity;
    private final long ticketCreationTime;
    private int ticketAgeAdd;
    private int negotiatedMaxFragLen;
    private int maximumPacketSize;
    private final Queue<SSLSessionImpl> childSessions;
    private boolean isSessionResumption;
    private static boolean defaultRejoinable = true;
    final SNIServerName serverNameIndication;
    private final List<SNIServerName> requestedServerNames;
    private BigInteger ticketNonceCounter;
    private final String identificationProtocol;
    private final ConcurrentHashMap<SecureKey, Object> boundValues;
    private boolean acceptLargeFragments;

    SSLSessionImpl() {
        this.lastUsedTime = 0L;
        this.useDefaultPeerSignAlgs = false;
        this.ticketCreationTime = System.currentTimeMillis();
        this.negotiatedMaxFragLen = -1;
        this.childSessions = new ConcurrentLinkedQueue();
        this.isSessionResumption = false;
        this.ticketNonceCounter = BigInteger.ONE;
        this.acceptLargeFragments = Utilities.getBooleanProperty("jsse.SSLEngine.acceptLargeFragments", false);
        this.protocolVersion = ProtocolVersion.NONE;
        this.cipherSuite = CipherSuite.C_NULL;
        this.sessionId = new SessionId(false, null);
        this.host = null;
        this.port = -1;
        this.localSupportedSignAlgs = Collections.emptySet();
        this.serverNameIndication = null;
        this.requestedServerNames = Collections.emptyList();
        this.useExtendedMasterSecret = false;
        this.creationTime = System.currentTimeMillis();
        this.identificationProtocol = null;
        this.boundValues = new ConcurrentHashMap<>();
    }

    SSLSessionImpl(HandshakeContext handshakeContext, CipherSuite cipherSuite) {
        this(handshakeContext, cipherSuite, new SessionId(defaultRejoinable, handshakeContext.sslContext.getSecureRandom()));
    }

    SSLSessionImpl(HandshakeContext handshakeContext, CipherSuite cipherSuite, SessionId sessionId) {
        this(handshakeContext, cipherSuite, sessionId, System.currentTimeMillis());
    }

    SSLSessionImpl(HandshakeContext handshakeContext, CipherSuite cipherSuite, SessionId sessionId, long j2) {
        Collection<SignatureScheme> collectionUnmodifiableCollection;
        this.lastUsedTime = 0L;
        this.useDefaultPeerSignAlgs = false;
        this.ticketCreationTime = System.currentTimeMillis();
        this.negotiatedMaxFragLen = -1;
        this.childSessions = new ConcurrentLinkedQueue();
        this.isSessionResumption = false;
        this.ticketNonceCounter = BigInteger.ONE;
        this.acceptLargeFragments = Utilities.getBooleanProperty("jsse.SSLEngine.acceptLargeFragments", false);
        this.protocolVersion = handshakeContext.negotiatedProtocol;
        this.cipherSuite = cipherSuite;
        this.sessionId = sessionId;
        this.host = handshakeContext.conContext.transport.getPeerHost();
        this.port = handshakeContext.conContext.transport.getPeerPort();
        if (handshakeContext.localSupportedSignAlgs == null) {
            collectionUnmodifiableCollection = Collections.emptySet();
        } else {
            collectionUnmodifiableCollection = Collections.unmodifiableCollection(new ArrayList(handshakeContext.localSupportedSignAlgs));
        }
        this.localSupportedSignAlgs = collectionUnmodifiableCollection;
        this.serverNameIndication = handshakeContext.negotiatedServerName;
        this.requestedServerNames = Collections.unmodifiableList(new ArrayList(handshakeContext.getRequestedServerNames()));
        if (handshakeContext.sslConfig.isClientMode) {
            this.useExtendedMasterSecret = (handshakeContext.handshakeExtensions.get(SSLExtension.CH_EXTENDED_MASTER_SECRET) == null || handshakeContext.handshakeExtensions.get(SSLExtension.SH_EXTENDED_MASTER_SECRET) == null) ? false : true;
        } else {
            this.useExtendedMasterSecret = (handshakeContext.handshakeExtensions.get(SSLExtension.CH_EXTENDED_MASTER_SECRET) == null || handshakeContext.negotiatedProtocol.useTLS13PlusSpec()) ? false : true;
        }
        this.creationTime = j2;
        this.identificationProtocol = handshakeContext.sslConfig.identificationProtocol;
        this.boundValues = new ConcurrentHashMap<>();
        if (SSLLogger.isOn && SSLLogger.isOn("session")) {
            SSLLogger.finest("Session initialized:  " + ((Object) this), new Object[0]);
        }
    }

    SSLSessionImpl(SSLSessionImpl sSLSessionImpl, SessionId sessionId) {
        this.lastUsedTime = 0L;
        this.useDefaultPeerSignAlgs = false;
        this.ticketCreationTime = System.currentTimeMillis();
        this.negotiatedMaxFragLen = -1;
        this.childSessions = new ConcurrentLinkedQueue();
        this.isSessionResumption = false;
        this.ticketNonceCounter = BigInteger.ONE;
        this.acceptLargeFragments = Utilities.getBooleanProperty("jsse.SSLEngine.acceptLargeFragments", false);
        this.protocolVersion = sSLSessionImpl.getProtocolVersion();
        this.cipherSuite = sSLSessionImpl.cipherSuite;
        this.sessionId = sessionId;
        this.host = sSLSessionImpl.getPeerHost();
        this.port = sSLSessionImpl.getPeerPort();
        this.localSupportedSignAlgs = sSLSessionImpl.localSupportedSignAlgs == null ? Collections.emptySet() : sSLSessionImpl.localSupportedSignAlgs;
        this.peerSupportedSignAlgs = sSLSessionImpl.getPeerSupportedSignatureAlgorithms();
        this.serverNameIndication = sSLSessionImpl.serverNameIndication;
        this.requestedServerNames = sSLSessionImpl.getRequestedServerNames();
        this.masterSecret = sSLSessionImpl.getMasterSecret();
        this.useExtendedMasterSecret = sSLSessionImpl.useExtendedMasterSecret;
        this.creationTime = sSLSessionImpl.getCreationTime();
        this.lastUsedTime = System.currentTimeMillis();
        this.identificationProtocol = sSLSessionImpl.getIdentificationProtocol();
        this.localCerts = sSLSessionImpl.localCerts;
        this.peerCerts = sSLSessionImpl.peerCerts;
        this.localPrincipal = sSLSessionImpl.localPrincipal;
        this.peerPrincipal = sSLSessionImpl.peerPrincipal;
        this.statusResponses = sSLSessionImpl.statusResponses;
        this.resumptionMasterSecret = sSLSessionImpl.resumptionMasterSecret;
        this.context = sSLSessionImpl.context;
        this.negotiatedMaxFragLen = sSLSessionImpl.negotiatedMaxFragLen;
        this.maximumPacketSize = sSLSessionImpl.maximumPacketSize;
        this.boundValues = sSLSessionImpl.boundValues;
        if (SSLLogger.isOn && SSLLogger.isOn("session")) {
            SSLLogger.finest("Session initialized:  " + ((Object) this), new Object[0]);
        }
    }

    void setMasterSecret(SecretKey secretKey) {
        this.masterSecret = secretKey;
    }

    void setResumptionMasterSecret(SecretKey secretKey) {
        this.resumptionMasterSecret = secretKey;
    }

    void setPreSharedKey(SecretKey secretKey) {
        this.preSharedKey = secretKey;
    }

    void addChild(SSLSessionImpl sSLSessionImpl) {
        this.childSessions.add(sSLSessionImpl);
    }

    void setTicketAgeAdd(int i2) {
        this.ticketAgeAdd = i2;
    }

    void setPskIdentity(byte[] bArr) {
        this.pskIdentity = bArr;
    }

    BigInteger incrTicketNonceCounter() {
        BigInteger bigInteger = this.ticketNonceCounter;
        this.ticketNonceCounter = this.ticketNonceCounter.add(BigInteger.valueOf(1L));
        return bigInteger;
    }

    SecretKey getMasterSecret() {
        return this.masterSecret;
    }

    SecretKey getResumptionMasterSecret() {
        return this.resumptionMasterSecret;
    }

    synchronized SecretKey getPreSharedKey() {
        return this.preSharedKey;
    }

    synchronized SecretKey consumePreSharedKey() {
        try {
            return this.preSharedKey;
        } finally {
            this.preSharedKey = null;
        }
    }

    int getTicketAgeAdd() {
        return this.ticketAgeAdd;
    }

    String getIdentificationProtocol() {
        return this.identificationProtocol;
    }

    synchronized byte[] consumePskIdentity() {
        try {
            return this.pskIdentity;
        } finally {
            this.pskIdentity = null;
        }
    }

    void setPeerCertificates(X509Certificate[] x509CertificateArr) {
        if (this.peerCerts == null) {
            this.peerCerts = x509CertificateArr;
        }
    }

    void setPeerPrincipal(Principal principal) {
        if (this.peerPrincipal == null) {
            this.peerPrincipal = principal;
        }
    }

    void setLocalCertificates(X509Certificate[] x509CertificateArr) {
        this.localCerts = x509CertificateArr;
    }

    void setLocalPrincipal(Principal principal) {
        this.localPrincipal = principal;
    }

    void setLocalPrivateKey(PrivateKey privateKey) {
        this.localPrivateKey = privateKey;
    }

    void setPeerSupportedSignatureAlgorithms(Collection<SignatureScheme> collection) {
        this.peerSupportedSignAlgs = SignatureScheme.getAlgorithmNames(collection);
    }

    void setUseDefaultPeerSignAlgs() {
        this.useDefaultPeerSignAlgs = true;
        this.peerSupportedSignAlgs = new String[]{"SHA1withRSA", "SHA1withDSA", "SHA1withECDSA"};
    }

    SSLSessionImpl finish() {
        if (this.useDefaultPeerSignAlgs) {
            this.peerSupportedSignAlgs = new String[0];
        }
        return this;
    }

    void setStatusResponses(List<byte[]> list) {
        if (list != null && !list.isEmpty()) {
            this.statusResponses = list;
        } else {
            this.statusResponses = Collections.emptyList();
        }
    }

    boolean isRejoinable() {
        return (this.sessionId == null || this.sessionId.length() == 0 || this.invalidated || !isLocalAuthenticationValid()) ? false : true;
    }

    @Override // javax.net.ssl.SSLSession
    public synchronized boolean isValid() {
        return isRejoinable();
    }

    private boolean isLocalAuthenticationValid() {
        if (this.localPrivateKey != null) {
            try {
                this.localPrivateKey.getAlgorithm();
                return true;
            } catch (Exception e2) {
                invalidate();
                return false;
            }
        }
        return true;
    }

    @Override // javax.net.ssl.SSLSession
    public byte[] getId() {
        return this.sessionId.getId();
    }

    @Override // javax.net.ssl.SSLSession
    public SSLSessionContext getSessionContext() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SSLPermission("getSSLSessionContext"));
        }
        return this.context;
    }

    SessionId getSessionId() {
        return this.sessionId;
    }

    CipherSuite getSuite() {
        return this.cipherSuite;
    }

    void setSuite(CipherSuite cipherSuite) {
        this.cipherSuite = cipherSuite;
        if (SSLLogger.isOn && SSLLogger.isOn("session")) {
            SSLLogger.finest("Negotiating session:  " + ((Object) this), new Object[0]);
        }
    }

    boolean isSessionResumption() {
        return this.isSessionResumption;
    }

    void setAsSessionResumption(boolean z2) {
        this.isSessionResumption = z2;
    }

    @Override // javax.net.ssl.SSLSession
    public String getCipherSuite() {
        return getSuite().name;
    }

    ProtocolVersion getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override // javax.net.ssl.SSLSession
    public String getProtocol() {
        return getProtocolVersion().name;
    }

    public int hashCode() {
        return this.sessionId.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SSLSessionImpl) {
            return this.sessionId != null && this.sessionId.equals(((SSLSessionImpl) obj).getSessionId());
        }
        return false;
    }

    @Override // javax.net.ssl.SSLSession
    public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
        if (this.cipherSuite.keyExchange == CipherSuite.KeyExchange.K_KRB5 || this.cipherSuite.keyExchange == CipherSuite.KeyExchange.K_KRB5_EXPORT) {
            throw new SSLPeerUnverifiedException("no certificates expected for Kerberos cipher suites");
        }
        if (this.peerCerts == null) {
            throw new SSLPeerUnverifiedException("peer not authenticated");
        }
        return (Certificate[]) this.peerCerts.clone();
    }

    @Override // javax.net.ssl.SSLSession
    public Certificate[] getLocalCertificates() {
        if (this.localCerts == null) {
            return null;
        }
        return (Certificate[]) this.localCerts.clone();
    }

    @Override // javax.net.ssl.SSLSession
    @Deprecated
    public javax.security.cert.X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
        if (this.cipherSuite.keyExchange == CipherSuite.KeyExchange.K_KRB5 || this.cipherSuite.keyExchange == CipherSuite.KeyExchange.K_KRB5_EXPORT) {
            throw new SSLPeerUnverifiedException("no certificates expected for Kerberos cipher suites");
        }
        if (this.peerCerts == null) {
            throw new SSLPeerUnverifiedException("peer not authenticated");
        }
        javax.security.cert.X509Certificate[] x509CertificateArr = new javax.security.cert.X509Certificate[this.peerCerts.length];
        for (int i2 = 0; i2 < this.peerCerts.length; i2++) {
            try {
                x509CertificateArr[i2] = javax.security.cert.X509Certificate.getInstance(this.peerCerts[i2].getEncoded());
            } catch (CertificateEncodingException e2) {
                throw new SSLPeerUnverifiedException(e2.getMessage());
            } catch (CertificateException e3) {
                throw new SSLPeerUnverifiedException(e3.getMessage());
            }
        }
        return x509CertificateArr;
    }

    public X509Certificate[] getCertificateChain() throws SSLPeerUnverifiedException {
        if (this.cipherSuite.keyExchange == CipherSuite.KeyExchange.K_KRB5 || this.cipherSuite.keyExchange == CipherSuite.KeyExchange.K_KRB5_EXPORT) {
            throw new SSLPeerUnverifiedException("no certificates expected for Kerberos cipher suites");
        }
        if (this.peerCerts != null) {
            return (X509Certificate[]) this.peerCerts.clone();
        }
        throw new SSLPeerUnverifiedException("peer not authenticated");
    }

    public List<byte[]> getStatusResponses() {
        if (this.statusResponses == null || this.statusResponses.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(this.statusResponses.size());
        Iterator<byte[]> it = this.statusResponses.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().clone());
        }
        return Collections.unmodifiableList(arrayList);
    }

    @Override // javax.net.ssl.SSLSession
    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        if (this.peerCerts == null) {
            if (this.peerPrincipal != null) {
                return this.peerPrincipal;
            }
            throw new SSLPeerUnverifiedException("peer not authenticated");
        }
        return this.peerCerts[0].getSubjectX500Principal();
    }

    @Override // javax.net.ssl.SSLSession
    public Principal getLocalPrincipal() {
        if (this.localCerts != null && this.localCerts.length != 0) {
            return this.localCerts[0].getSubjectX500Principal();
        }
        if (this.localPrincipal != null) {
            return this.localPrincipal;
        }
        return null;
    }

    public long getTicketCreationTime() {
        return this.ticketCreationTime;
    }

    @Override // javax.net.ssl.SSLSession
    public long getCreationTime() {
        return this.creationTime;
    }

    @Override // javax.net.ssl.SSLSession
    public long getLastAccessedTime() {
        return this.lastUsedTime != 0 ? this.lastUsedTime : this.creationTime;
    }

    void setLastAccessedTime(long j2) {
        this.lastUsedTime = j2;
    }

    public InetAddress getPeerAddress() {
        try {
            return InetAddress.getByName(this.host);
        } catch (UnknownHostException e2) {
            return null;
        }
    }

    @Override // javax.net.ssl.SSLSession
    public String getPeerHost() {
        return this.host;
    }

    @Override // javax.net.ssl.SSLSession
    public int getPeerPort() {
        return this.port;
    }

    void setContext(SSLSessionContextImpl sSLSessionContextImpl) {
        if (this.context == null) {
            this.context = sSLSessionContextImpl;
        }
    }

    @Override // javax.net.ssl.SSLSession
    public synchronized void invalidate() {
        if (this.context != null) {
            this.context.remove(this.sessionId);
            this.context = null;
        }
        if (this.invalidated) {
            return;
        }
        this.invalidated = true;
        if (SSLLogger.isOn && SSLLogger.isOn("session")) {
            SSLLogger.finest("Invalidated session:  " + ((Object) this), new Object[0]);
        }
        Iterator<SSLSessionImpl> it = this.childSessions.iterator();
        while (it.hasNext()) {
            it.next().invalidate();
        }
    }

    @Override // javax.net.ssl.SSLSession
    public void putValue(String str, Object obj) {
        if (str == null || obj == null) {
            throw new IllegalArgumentException("arguments can not be null");
        }
        Object objPut = this.boundValues.put(new SecureKey(str), obj);
        if (objPut instanceof SSLSessionBindingListener) {
            ((SSLSessionBindingListener) objPut).valueUnbound(new SSLSessionBindingEvent(this, str));
        }
        if (obj instanceof SSLSessionBindingListener) {
            ((SSLSessionBindingListener) obj).valueBound(new SSLSessionBindingEvent(this, str));
        }
    }

    @Override // javax.net.ssl.SSLSession
    public Object getValue(String str) {
        if (str == null) {
            throw new IllegalArgumentException("argument can not be null");
        }
        return this.boundValues.get(new SecureKey(str));
    }

    @Override // javax.net.ssl.SSLSession
    public void removeValue(String str) {
        if (str == null) {
            throw new IllegalArgumentException("argument can not be null");
        }
        Object objRemove = this.boundValues.remove(new SecureKey(str));
        if (objRemove instanceof SSLSessionBindingListener) {
            ((SSLSessionBindingListener) objRemove).valueUnbound(new SSLSessionBindingEvent(this, str));
        }
    }

    @Override // javax.net.ssl.SSLSession
    public String[] getValueNames() {
        ArrayList arrayList = new ArrayList();
        Object currentSecurityContext = SecureKey.getCurrentSecurityContext();
        Enumeration<SecureKey> enumerationKeys = this.boundValues.keys();
        while (enumerationKeys.hasMoreElements()) {
            SecureKey secureKeyNextElement = enumerationKeys.nextElement2();
            if (currentSecurityContext.equals(secureKeyNextElement.getSecurityContext())) {
                arrayList.add(secureKeyNextElement.getAppKey());
            }
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    protected synchronized void expandBufferSizes() {
        this.acceptLargeFragments = true;
    }

    @Override // javax.net.ssl.SSLSession
    public synchronized int getPacketBufferSize() {
        int iCalculatePacketSize = 0;
        if (this.negotiatedMaxFragLen > 0) {
            iCalculatePacketSize = this.cipherSuite.calculatePacketSize(this.negotiatedMaxFragLen, this.protocolVersion);
        }
        if (this.maximumPacketSize > 0) {
            return this.maximumPacketSize > iCalculatePacketSize ? this.maximumPacketSize : iCalculatePacketSize;
        }
        if (iCalculatePacketSize != 0) {
            return iCalculatePacketSize;
        }
        return this.acceptLargeFragments ? SSLRecord.maxLargeRecordSize : SSLRecord.maxRecordSize;
    }

    @Override // javax.net.ssl.SSLSession
    public synchronized int getApplicationBufferSize() {
        int iCalculateFragSize = 0;
        if (this.maximumPacketSize > 0) {
            iCalculateFragSize = this.cipherSuite.calculateFragSize(this.maximumPacketSize, this.protocolVersion);
        }
        if (this.negotiatedMaxFragLen > 0) {
            return this.negotiatedMaxFragLen > iCalculateFragSize ? this.negotiatedMaxFragLen : iCalculateFragSize;
        }
        if (iCalculateFragSize != 0) {
            return iCalculateFragSize;
        }
        return (this.acceptLargeFragments ? SSLRecord.maxLargeRecordSize : SSLRecord.maxRecordSize) - 5;
    }

    synchronized void setNegotiatedMaxFragSize(int i2) {
        this.negotiatedMaxFragLen = i2;
    }

    synchronized int getNegotiatedMaxFragSize() {
        return this.negotiatedMaxFragLen;
    }

    synchronized void setMaximumPacketSize(int i2) {
        this.maximumPacketSize = i2;
    }

    synchronized int getMaximumPacketSize() {
        return this.maximumPacketSize;
    }

    @Override // javax.net.ssl.ExtendedSSLSession
    public String[] getLocalSupportedSignatureAlgorithms() {
        return SignatureScheme.getAlgorithmNames(this.localSupportedSignAlgs);
    }

    public Collection<SignatureScheme> getLocalSupportedSignatureSchemes() {
        return this.localSupportedSignAlgs;
    }

    @Override // javax.net.ssl.ExtendedSSLSession
    public String[] getPeerSupportedSignatureAlgorithms() {
        if (this.peerSupportedSignAlgs != null) {
            return (String[]) this.peerSupportedSignAlgs.clone();
        }
        return new String[0];
    }

    @Override // javax.net.ssl.ExtendedSSLSession
    public List<SNIServerName> getRequestedServerNames() {
        return this.requestedServerNames;
    }

    public String toString() {
        return "Session(" + this.creationTime + CallSiteDescriptor.OPERATOR_DELIMITER + getCipherSuite() + ")";
    }
}
