package sun.security.ssl;

import java.io.FileInputStream;
import java.security.AccessController;
import java.security.CryptoPrimitive;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.PrivilegedExceptionAction;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import org.icepdf.core.util.PdfOps;
import sun.security.action.GetPropertyAction;
import sun.security.ssl.HelloCookieManager;

/* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl.class */
public abstract class SSLContextImpl extends SSLContextSpi {
    private boolean isInitialized;
    private X509ExtendedKeyManager keyManager;
    private X509TrustManager trustManager;
    private SecureRandom secureRandom;
    private volatile HelloCookieManager.Builder helloCookieManagerBuilder;
    private static final Collection<CipherSuite> clientCustomizedCipherSuites = getCustomizedCipherSuites("jdk.tls.client.cipherSuites");
    private static final Collection<CipherSuite> serverCustomizedCipherSuites = getCustomizedCipherSuites("jdk.tls.server.cipherSuites");
    private volatile StatusResponseManager statusResponseManager;
    private final boolean clientEnableStapling = Utilities.getBooleanProperty("jdk.tls.client.enableStatusRequestExtension", false);
    private final boolean serverEnableStapling = Utilities.getBooleanProperty("jdk.tls.server.enableStatusRequestExtension", false);
    private final EphemeralKeyManager ephemeralKeyManager = new EphemeralKeyManager();
    private final SSLSessionContextImpl clientCache = new SSLSessionContextImpl();
    private final SSLSessionContextImpl serverCache = new SSLSessionContextImpl();

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$TLSContext.class */
    public static final class TLSContext extends CustomizedTLSContext {
    }

    abstract SSLEngine createSSLEngineImpl();

    abstract SSLEngine createSSLEngineImpl(String str, int i2);

    abstract List<ProtocolVersion> getSupportedProtocolVersions();

    abstract List<ProtocolVersion> getServerDefaultProtocolVersions();

    abstract List<ProtocolVersion> getClientDefaultProtocolVersions();

    abstract List<CipherSuite> getSupportedCipherSuites();

    abstract List<CipherSuite> getServerDefaultCipherSuites();

    abstract List<CipherSuite> getClientDefaultCipherSuites();

    SSLContextImpl() {
    }

    @Override // javax.net.ssl.SSLContextSpi
    protected void engineInit(KeyManager[] keyManagerArr, TrustManager[] trustManagerArr, SecureRandom secureRandom) throws KeyManagementException {
        this.isInitialized = false;
        this.keyManager = chooseKeyManager(keyManagerArr);
        if (trustManagerArr == null) {
            try {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((KeyStore) null);
                trustManagerArr = trustManagerFactory.getTrustManagers();
            } catch (Exception e2) {
            }
        }
        this.trustManager = chooseTrustManager(trustManagerArr);
        if (secureRandom == null) {
            this.secureRandom = JsseJce.getSecureRandom();
        } else {
            if (SunJSSE.isFIPS() && secureRandom.getProvider() != SunJSSE.cryptoProvider) {
                throw new KeyManagementException("FIPS mode: SecureRandom must be from provider " + SunJSSE.cryptoProvider.getName());
            }
            this.secureRandom = secureRandom;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx")) {
            SSLLogger.finest("trigger seeding of SecureRandom", new Object[0]);
        }
        this.secureRandom.nextInt();
        if (SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx")) {
            SSLLogger.finest("done seeding of SecureRandom", new Object[0]);
        }
        this.isInitialized = true;
    }

    private X509TrustManager chooseTrustManager(TrustManager[] trustManagerArr) throws KeyManagementException {
        for (int i2 = 0; trustManagerArr != null && i2 < trustManagerArr.length; i2++) {
            if (trustManagerArr[i2] instanceof X509TrustManager) {
                if (SunJSSE.isFIPS() && !(trustManagerArr[i2] instanceof X509TrustManagerImpl)) {
                    throw new KeyManagementException("FIPS mode: only SunJSSE TrustManagers may be used");
                }
                if (trustManagerArr[i2] instanceof X509ExtendedTrustManager) {
                    return (X509TrustManager) trustManagerArr[i2];
                }
                return new AbstractTrustManagerWrapper((X509TrustManager) trustManagerArr[i2]);
            }
        }
        return DummyX509TrustManager.INSTANCE;
    }

    private X509ExtendedKeyManager chooseKeyManager(KeyManager[] keyManagerArr) throws KeyManagementException {
        for (int i2 = 0; keyManagerArr != null && i2 < keyManagerArr.length; i2++) {
            KeyManager keyManager = keyManagerArr[i2];
            if (keyManager instanceof X509KeyManager) {
                if (SunJSSE.isFIPS()) {
                    if ((keyManager instanceof X509KeyManagerImpl) || (keyManager instanceof SunX509KeyManagerImpl)) {
                        return (X509ExtendedKeyManager) keyManager;
                    }
                    throw new KeyManagementException("FIPS mode: only SunJSSE KeyManagers may be used");
                }
                if (keyManager instanceof X509ExtendedKeyManager) {
                    return (X509ExtendedKeyManager) keyManager;
                }
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx")) {
                    SSLLogger.warning("X509KeyManager passed to SSLContext.init():  need an X509ExtendedKeyManager for SSLEngine use", new Object[0]);
                }
                return new AbstractKeyManagerWrapper((X509KeyManager) keyManager);
            }
        }
        return DummyX509KeyManager.INSTANCE;
    }

    @Override // javax.net.ssl.SSLContextSpi
    protected SSLEngine engineCreateSSLEngine() {
        if (!this.isInitialized) {
            throw new IllegalStateException("SSLContext is not initialized");
        }
        return createSSLEngineImpl();
    }

    @Override // javax.net.ssl.SSLContextSpi
    protected SSLEngine engineCreateSSLEngine(String str, int i2) {
        if (!this.isInitialized) {
            throw new IllegalStateException("SSLContext is not initialized");
        }
        return createSSLEngineImpl(str, i2);
    }

    @Override // javax.net.ssl.SSLContextSpi
    protected SSLSocketFactory engineGetSocketFactory() {
        if (!this.isInitialized) {
            throw new IllegalStateException("SSLContext is not initialized");
        }
        return new SSLSocketFactoryImpl(this);
    }

    @Override // javax.net.ssl.SSLContextSpi
    protected SSLServerSocketFactory engineGetServerSocketFactory() {
        if (!this.isInitialized) {
            throw new IllegalStateException("SSLContext is not initialized");
        }
        return new SSLServerSocketFactoryImpl(this);
    }

    @Override // javax.net.ssl.SSLContextSpi
    protected SSLSessionContext engineGetClientSessionContext() {
        return this.clientCache;
    }

    @Override // javax.net.ssl.SSLContextSpi
    protected SSLSessionContext engineGetServerSessionContext() {
        return this.serverCache;
    }

    SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    X509ExtendedKeyManager getX509KeyManager() {
        return this.keyManager;
    }

    X509TrustManager getX509TrustManager() {
        return this.trustManager;
    }

    EphemeralKeyManager getEphemeralKeyManager() {
        return this.ephemeralKeyManager;
    }

    HelloCookieManager getHelloCookieManager(ProtocolVersion protocolVersion) {
        if (this.helloCookieManagerBuilder == null) {
            synchronized (this) {
                if (this.helloCookieManagerBuilder == null) {
                    this.helloCookieManagerBuilder = new HelloCookieManager.Builder(this.secureRandom);
                }
            }
        }
        return this.helloCookieManagerBuilder.valueOf(protocolVersion);
    }

    StatusResponseManager getStatusResponseManager() {
        if (this.serverEnableStapling && this.statusResponseManager == null) {
            synchronized (this) {
                if (this.statusResponseManager == null) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx")) {
                        SSLLogger.finest("Initializing StatusResponseManager", new Object[0]);
                    }
                    this.statusResponseManager = new StatusResponseManager();
                }
            }
        }
        return this.statusResponseManager;
    }

    List<ProtocolVersion> getDefaultProtocolVersions(boolean z2) {
        return z2 ? getServerDefaultProtocolVersions() : getClientDefaultProtocolVersions();
    }

    List<CipherSuite> getDefaultCipherSuites(boolean z2) {
        return z2 ? getServerDefaultCipherSuites() : getClientDefaultCipherSuites();
    }

    boolean isDefaultProtocolVesions(List<ProtocolVersion> list) {
        return list == getServerDefaultProtocolVersions() || list == getClientDefaultProtocolVersions();
    }

    boolean isDefaultCipherSuiteList(List<CipherSuite> list) {
        return list == getServerDefaultCipherSuites() || list == getClientDefaultCipherSuites();
    }

    boolean isStaplingEnabled(boolean z2) {
        return z2 ? this.clientEnableStapling : this.serverEnableStapling;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<CipherSuite> getApplicableSupportedCipherSuites(List<ProtocolVersion> list) {
        return getApplicableCipherSuites(CipherSuite.allowedCipherSuites(), list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<CipherSuite> getApplicableEnabledCipherSuites(List<ProtocolVersion> list, boolean z2) {
        if (z2) {
            if (!clientCustomizedCipherSuites.isEmpty()) {
                return getApplicableCipherSuites(clientCustomizedCipherSuites, list);
            }
        } else if (!serverCustomizedCipherSuites.isEmpty()) {
            return getApplicableCipherSuites(serverCustomizedCipherSuites, list);
        }
        return getApplicableCipherSuites(CipherSuite.defaultCipherSuites(), list);
    }

    private static List<CipherSuite> getApplicableCipherSuites(Collection<CipherSuite> collection, List<ProtocolVersion> list) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        if (list != null && !list.isEmpty()) {
            for (CipherSuite cipherSuite : collection) {
                if (cipherSuite.isAvailable()) {
                    boolean z2 = false;
                    Iterator<ProtocolVersion> it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        if (cipherSuite.supports(it.next()) && cipherSuite.bulkCipher.isAvailable()) {
                            if (SSLAlgorithmConstraints.DEFAULT.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), cipherSuite.name, null)) {
                                linkedHashSet.add(cipherSuite);
                                z2 = true;
                            } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx,verbose")) {
                                SSLLogger.fine("Ignore disabled cipher suite: " + cipherSuite.name, new Object[0]);
                            }
                        }
                    }
                    if (!z2 && SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx,verbose")) {
                        SSLLogger.finest("Ignore unsupported cipher suite: " + ((Object) cipherSuite), new Object[0]);
                    }
                }
            }
        }
        return new ArrayList(linkedHashSet);
    }

    private static Collection<CipherSuite> getCustomizedCipherSuites(String str) {
        String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty(str);
        if (SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx")) {
            SSLLogger.fine("System property " + str + " is set to '" + strPrivilegedGetProperty + PdfOps.SINGLE_QUOTE_TOKEN, new Object[0]);
        }
        if (strPrivilegedGetProperty != null && !strPrivilegedGetProperty.isEmpty() && strPrivilegedGetProperty.length() > 1 && strPrivilegedGetProperty.charAt(0) == '\"' && strPrivilegedGetProperty.charAt(strPrivilegedGetProperty.length() - 1) == '\"') {
            strPrivilegedGetProperty = strPrivilegedGetProperty.substring(1, strPrivilegedGetProperty.length() - 1);
        }
        if (strPrivilegedGetProperty != null && !strPrivilegedGetProperty.isEmpty()) {
            String[] strArrSplit = strPrivilegedGetProperty.split(",");
            ArrayList arrayList = new ArrayList(strArrSplit.length);
            for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                strArrSplit[i2] = strArrSplit[i2].trim();
                if (!strArrSplit[i2].isEmpty()) {
                    try {
                        CipherSuite cipherSuiteNameOf = CipherSuite.nameOf(strArrSplit[i2]);
                        if (cipherSuiteNameOf != null && cipherSuiteNameOf.isAvailable()) {
                            arrayList.add(cipherSuiteNameOf);
                        } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx")) {
                            SSLLogger.fine("The current installed providers do not support cipher suite: " + strArrSplit[i2], new Object[0]);
                        }
                    } catch (IllegalArgumentException e2) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx")) {
                            SSLLogger.fine("Unknown or unsupported cipher suite name: " + strArrSplit[i2], new Object[0]);
                        }
                    }
                }
            }
            return arrayList;
        }
        return Collections.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<ProtocolVersion> getAvailableProtocols(ProtocolVersion[] protocolVersionArr) {
        List<ProtocolVersion> listEmptyList = Collections.emptyList();
        if (protocolVersionArr != null && protocolVersionArr.length != 0) {
            listEmptyList = new ArrayList(protocolVersionArr.length);
            for (ProtocolVersion protocolVersion : protocolVersionArr) {
                if (protocolVersion.isAvailable) {
                    listEmptyList.add(protocolVersion);
                }
            }
        }
        return listEmptyList;
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$AbstractTLSContext.class */
    private static abstract class AbstractTLSContext extends SSLContextImpl {
        private static final List<ProtocolVersion> supportedProtocols;
        private static final List<ProtocolVersion> serverDefaultProtocols;
        private static final List<CipherSuite> supportedCipherSuites;
        private static final List<CipherSuite> serverDefaultCipherSuites;

        private AbstractTLSContext() {
        }

        static {
            if (SunJSSE.isFIPS()) {
                supportedProtocols = Arrays.asList(ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10);
                serverDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10});
            } else {
                supportedProtocols = Arrays.asList(ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10, ProtocolVersion.SSL30, ProtocolVersion.SSL20Hello);
                serverDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10, ProtocolVersion.SSL30, ProtocolVersion.SSL20Hello});
            }
            supportedCipherSuites = SSLContextImpl.getApplicableSupportedCipherSuites(supportedProtocols);
            serverDefaultCipherSuites = SSLContextImpl.getApplicableEnabledCipherSuites(serverDefaultProtocols, false);
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<ProtocolVersion> getSupportedProtocolVersions() {
            return supportedProtocols;
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<CipherSuite> getSupportedCipherSuites() {
            return supportedCipherSuites;
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<ProtocolVersion> getServerDefaultProtocolVersions() {
            return serverDefaultProtocols;
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<CipherSuite> getServerDefaultCipherSuites() {
            return serverDefaultCipherSuites;
        }

        @Override // sun.security.ssl.SSLContextImpl
        SSLEngine createSSLEngineImpl() {
            return new SSLEngineImpl(this);
        }

        @Override // sun.security.ssl.SSLContextImpl
        SSLEngine createSSLEngineImpl(String str, int i2) {
            return new SSLEngineImpl(this, str, i2);
        }

        static ProtocolVersion[] getSupportedProtocols() {
            if (SunJSSE.isFIPS()) {
                return new ProtocolVersion[]{ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10};
            }
            return new ProtocolVersion[]{ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10, ProtocolVersion.SSL30, ProtocolVersion.SSL20Hello};
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$TLS10Context.class */
    public static final class TLS10Context extends AbstractTLSContext {
        private static final List<ProtocolVersion> clientDefaultProtocols;
        private static final List<CipherSuite> clientDefaultCipherSuites;

        public TLS10Context() {
            super();
        }

        static {
            if (SunJSSE.isFIPS()) {
                clientDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS10});
            } else {
                clientDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS10, ProtocolVersion.SSL30});
            }
            clientDefaultCipherSuites = SSLContextImpl.getApplicableEnabledCipherSuites(clientDefaultProtocols, true);
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<ProtocolVersion> getClientDefaultProtocolVersions() {
            return clientDefaultProtocols;
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<CipherSuite> getClientDefaultCipherSuites() {
            return clientDefaultCipherSuites;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$TLS11Context.class */
    public static final class TLS11Context extends AbstractTLSContext {
        private static final List<ProtocolVersion> clientDefaultProtocols;
        private static final List<CipherSuite> clientDefaultCipherSuites;

        public TLS11Context() {
            super();
        }

        static {
            if (SunJSSE.isFIPS()) {
                clientDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS11, ProtocolVersion.TLS10});
            } else {
                clientDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS11, ProtocolVersion.TLS10, ProtocolVersion.SSL30});
            }
            clientDefaultCipherSuites = SSLContextImpl.getApplicableEnabledCipherSuites(clientDefaultProtocols, true);
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<ProtocolVersion> getClientDefaultProtocolVersions() {
            return clientDefaultProtocols;
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<CipherSuite> getClientDefaultCipherSuites() {
            return clientDefaultCipherSuites;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$TLS12Context.class */
    public static final class TLS12Context extends AbstractTLSContext {
        private static final List<ProtocolVersion> clientDefaultProtocols;
        private static final List<CipherSuite> clientDefaultCipherSuites;

        public TLS12Context() {
            super();
        }

        static {
            if (SunJSSE.isFIPS()) {
                clientDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10});
            } else {
                clientDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10, ProtocolVersion.SSL30});
            }
            clientDefaultCipherSuites = SSLContextImpl.getApplicableEnabledCipherSuites(clientDefaultProtocols, true);
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<ProtocolVersion> getClientDefaultProtocolVersions() {
            return clientDefaultProtocols;
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<CipherSuite> getClientDefaultCipherSuites() {
            return clientDefaultCipherSuites;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$TLS13Context.class */
    public static final class TLS13Context extends AbstractTLSContext {
        private static final List<ProtocolVersion> clientDefaultProtocols;
        private static final List<CipherSuite> clientDefaultCipherSuites;

        public TLS13Context() {
            super();
        }

        static {
            if (SunJSSE.isFIPS()) {
                clientDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10});
            } else {
                clientDefaultProtocols = SSLContextImpl.getAvailableProtocols(new ProtocolVersion[]{ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10, ProtocolVersion.SSL30});
            }
            clientDefaultCipherSuites = SSLContextImpl.getApplicableEnabledCipherSuites(clientDefaultProtocols, true);
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<ProtocolVersion> getClientDefaultProtocolVersions() {
            return clientDefaultProtocols;
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<CipherSuite> getClientDefaultCipherSuites() {
            return clientDefaultCipherSuites;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$CustomizedSSLProtocols.class */
    private static class CustomizedSSLProtocols {
        private static final String JDK_TLS_CLIENT_PROTOCOLS = "jdk.tls.client.protocols";
        private static final String JDK_TLS_SERVER_PROTOCOLS = "jdk.tls.server.protocols";
        static IllegalArgumentException reservedException = null;
        static final ArrayList<ProtocolVersion> customizedClientProtocols = new ArrayList<>();
        static final ArrayList<ProtocolVersion> customizedServerProtocols = new ArrayList<>();

        private CustomizedSSLProtocols() {
        }

        static {
            populate(JDK_TLS_CLIENT_PROTOCOLS, customizedClientProtocols);
            populate(JDK_TLS_SERVER_PROTOCOLS, customizedServerProtocols);
        }

        private static void populate(String str, ArrayList<ProtocolVersion> arrayList) {
            String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty(str);
            if (strPrivilegedGetProperty == null) {
                return;
            }
            if (!strPrivilegedGetProperty.isEmpty() && strPrivilegedGetProperty.length() > 1 && strPrivilegedGetProperty.charAt(0) == '\"' && strPrivilegedGetProperty.charAt(strPrivilegedGetProperty.length() - 1) == '\"') {
                strPrivilegedGetProperty = strPrivilegedGetProperty.substring(1, strPrivilegedGetProperty.length() - 1);
            }
            if (!strPrivilegedGetProperty.isEmpty()) {
                String[] strArrSplit = strPrivilegedGetProperty.split(",");
                for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                    strArrSplit[i2] = strArrSplit[i2].trim();
                    ProtocolVersion protocolVersionNameOf = ProtocolVersion.nameOf(strArrSplit[i2]);
                    if (protocolVersionNameOf == null) {
                        reservedException = new IllegalArgumentException(str + ": " + strArrSplit[i2] + " is not a supported SSL protocol name");
                    }
                    if (SunJSSE.isFIPS() && (protocolVersionNameOf == ProtocolVersion.SSL30 || protocolVersionNameOf == ProtocolVersion.SSL20Hello)) {
                        reservedException = new IllegalArgumentException(str + ": " + ((Object) protocolVersionNameOf) + " is not FIPS compliant");
                        return;
                    } else {
                        if (!arrayList.contains(protocolVersionNameOf)) {
                            arrayList.add(protocolVersionNameOf);
                        }
                    }
                }
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$CustomizedTLSContext.class */
    private static class CustomizedTLSContext extends AbstractTLSContext {
        private static final List<ProtocolVersion> clientDefaultProtocols;
        private static final List<ProtocolVersion> serverDefaultProtocols;
        private static final List<CipherSuite> clientDefaultCipherSuites;
        private static final List<CipherSuite> serverDefaultCipherSuites;
        private static final IllegalArgumentException reservedException = CustomizedSSLProtocols.reservedException;

        static {
            if (reservedException == null) {
                clientDefaultProtocols = customizedProtocols(true, CustomizedSSLProtocols.customizedClientProtocols);
                serverDefaultProtocols = customizedProtocols(false, CustomizedSSLProtocols.customizedServerProtocols);
                clientDefaultCipherSuites = SSLContextImpl.getApplicableEnabledCipherSuites(clientDefaultProtocols, true);
                serverDefaultCipherSuites = SSLContextImpl.getApplicableEnabledCipherSuites(serverDefaultProtocols, false);
                return;
            }
            clientDefaultProtocols = null;
            serverDefaultProtocols = null;
            clientDefaultCipherSuites = null;
            serverDefaultCipherSuites = null;
        }

        private static List<ProtocolVersion> customizedProtocols(boolean z2, List<ProtocolVersion> list) {
            ProtocolVersion[] supportedProtocols;
            ArrayList arrayList = new ArrayList();
            Iterator<ProtocolVersion> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
            if (arrayList.isEmpty()) {
                if (z2) {
                    supportedProtocols = getProtocols();
                } else {
                    supportedProtocols = getSupportedProtocols();
                }
            } else {
                supportedProtocols = (ProtocolVersion[]) arrayList.toArray(new ProtocolVersion[arrayList.size()]);
            }
            return SSLContextImpl.getAvailableProtocols(supportedProtocols);
        }

        static ProtocolVersion[] getProtocols() {
            if (SunJSSE.isFIPS()) {
                return new ProtocolVersion[]{ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10};
            }
            return new ProtocolVersion[]{ProtocolVersion.TLS13, ProtocolVersion.TLS12, ProtocolVersion.TLS11, ProtocolVersion.TLS10, ProtocolVersion.SSL30};
        }

        protected CustomizedTLSContext() {
            super();
            if (reservedException != null) {
                throw reservedException;
            }
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<ProtocolVersion> getClientDefaultProtocolVersions() {
            return clientDefaultProtocols;
        }

        @Override // sun.security.ssl.SSLContextImpl.AbstractTLSContext, sun.security.ssl.SSLContextImpl
        List<ProtocolVersion> getServerDefaultProtocolVersions() {
            return serverDefaultProtocols;
        }

        @Override // sun.security.ssl.SSLContextImpl
        List<CipherSuite> getClientDefaultCipherSuites() {
            return clientDefaultCipherSuites;
        }

        @Override // sun.security.ssl.SSLContextImpl.AbstractTLSContext, sun.security.ssl.SSLContextImpl
        List<CipherSuite> getServerDefaultCipherSuites() {
            return serverDefaultCipherSuites;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$DefaultManagersHolder.class */
    private static final class DefaultManagersHolder {
        private static final String NONE = "NONE";
        private static final String P11KEYSTORE = "PKCS11";
        private static final TrustManager[] trustManagers;
        private static final KeyManager[] keyManagers;
        private static final Exception reservedException;

        private DefaultManagersHolder() {
        }

        static {
            TrustManager[] trustManagers2;
            KeyManager[] keyManagers2;
            Exception exc = null;
            try {
                trustManagers2 = getTrustManagers();
            } catch (Exception e2) {
                exc = e2;
                trustManagers2 = new TrustManager[0];
            }
            trustManagers = trustManagers2;
            if (exc == null) {
                try {
                    keyManagers2 = getKeyManagers();
                } catch (Exception e3) {
                    exc = e3;
                    keyManagers2 = new KeyManager[0];
                }
                keyManagers = keyManagers2;
            } else {
                keyManagers = new KeyManager[0];
            }
            reservedException = exc;
        }

        private static TrustManager[] getTrustManagers() throws Exception {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            if ("SunJSSE".equals(trustManagerFactory.getProvider().getName())) {
                trustManagerFactory.init((KeyStore) null);
            } else {
                trustManagerFactory.init(TrustStoreManager.getTrustedKeyStore());
            }
            return trustManagerFactory.getTrustManagers();
        }

        /* JADX WARN: Finally extract failed */
        /* JADX WARN: Multi-variable type inference failed */
        private static KeyManager[] getKeyManagers() throws Exception {
            final HashMap map = new HashMap();
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: sun.security.ssl.SSLContextImpl.DefaultManagersHolder.1
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    map.put("keyStore", System.getProperty("javax.net.ssl.keyStore", ""));
                    map.put("keyStoreType", System.getProperty("javax.net.ssl.keyStoreType", KeyStore.getDefaultType()));
                    map.put("keyStoreProvider", System.getProperty("javax.net.ssl.keyStoreProvider", ""));
                    map.put("keyStorePasswd", System.getProperty("javax.net.ssl.keyStorePassword", ""));
                    return null;
                }
            });
            final String str = (String) map.get("keyStore");
            String str2 = (String) map.get("keyStoreType");
            String str3 = (String) map.get("keyStoreProvider");
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,defaultctx")) {
                SSLLogger.fine("keyStore is : " + str, new Object[0]);
                SSLLogger.fine("keyStore type is : " + str2, new Object[0]);
                SSLLogger.fine("keyStore provider is : " + str3, new Object[0]);
            }
            if (P11KEYSTORE.equals(str2) && !NONE.equals(str)) {
                throw new IllegalArgumentException("if keyStoreType is PKCS11, then keyStore must be NONE");
            }
            FileInputStream fileInputStream = null;
            KeyStore keyStore = null;
            char[] charArray = null;
            try {
                if (!str.isEmpty() && !NONE.equals(str)) {
                    fileInputStream = (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<FileInputStream>() { // from class: sun.security.ssl.SSLContextImpl.DefaultManagersHolder.2
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public FileInputStream run() throws Exception {
                            return new FileInputStream(str);
                        }
                    });
                }
                String str4 = (String) map.get("keyStorePasswd");
                if (!str4.isEmpty()) {
                    charArray = str4.toCharArray();
                }
                if (str2.length() != 0) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,defaultctx")) {
                        SSLLogger.finest("init keystore", new Object[0]);
                    }
                    if (str3.isEmpty()) {
                        keyStore = KeyStore.getInstance(str2);
                    } else {
                        keyStore = KeyStore.getInstance(str2, str3);
                    }
                    keyStore.load(fileInputStream, charArray);
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,defaultctx")) {
                    SSLLogger.fine("init keymanager of type " + KeyManagerFactory.getDefaultAlgorithm(), new Object[0]);
                }
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                if (P11KEYSTORE.equals(str2)) {
                    keyManagerFactory.init(keyStore, null);
                } else {
                    keyManagerFactory.init(keyStore, charArray);
                }
                return keyManagerFactory.getKeyManagers();
            } catch (Throwable th) {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$DefaultSSLContextHolder.class */
    private static final class DefaultSSLContextHolder {
        private static final SSLContextImpl sslContext;
        static Exception reservedException;

        private DefaultSSLContextHolder() {
        }

        static {
            reservedException = null;
            DefaultSSLContext defaultSSLContext = null;
            if (DefaultManagersHolder.reservedException != null) {
                reservedException = DefaultManagersHolder.reservedException;
            } else {
                try {
                    defaultSSLContext = new DefaultSSLContext();
                } catch (Exception e2) {
                    reservedException = e2;
                }
            }
            sslContext = defaultSSLContext;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLContextImpl$DefaultSSLContext.class */
    public static final class DefaultSSLContext extends CustomizedTLSContext {
        public DefaultSSLContext() throws Exception {
            if (DefaultManagersHolder.reservedException != null) {
                throw DefaultManagersHolder.reservedException;
            }
            try {
                super.engineInit(DefaultManagersHolder.keyManagers, DefaultManagersHolder.trustManagers, null);
            } catch (Exception e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,defaultctx")) {
                    SSLLogger.fine("default context init failed: ", e2);
                }
                throw e2;
            }
        }

        @Override // sun.security.ssl.SSLContextImpl, javax.net.ssl.SSLContextSpi
        protected void engineInit(KeyManager[] keyManagerArr, TrustManager[] trustManagerArr, SecureRandom secureRandom) throws KeyManagementException {
            throw new KeyManagementException("Default SSLContext is initialized automatically");
        }

        static SSLContextImpl getDefaultImpl() throws Exception {
            if (DefaultSSLContextHolder.reservedException == null) {
                return DefaultSSLContextHolder.sslContext;
            }
            throw DefaultSSLContextHolder.reservedException;
        }
    }
}
