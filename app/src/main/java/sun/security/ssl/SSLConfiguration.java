package sun.security.ssl;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AlgorithmConstraints;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import org.icepdf.core.util.PdfOps;
import sun.security.action.GetIntegerAction;
import sun.security.action.GetPropertyAction;
import sun.security.ssl.SSLExtension;

/* loaded from: jsse.jar:sun/security/ssl/SSLConfiguration.class */
final class SSLConfiguration implements Cloneable {
    List<ProtocolVersion> enabledProtocols;
    List<CipherSuite> enabledCipherSuites;
    ClientAuthType clientAuthType;
    String identificationProtocol;
    List<SNIServerName> serverNames;
    Collection<SNIMatcher> sniMatchers;
    String[] applicationProtocols;
    boolean preferLocalCipherSuites;
    List<SignatureScheme> signatureSchemes;
    ProtocolVersion maximumProtocolVersion;
    boolean isClientMode;
    boolean enableSessionCreation;
    BiFunction<SSLSocket, List<String>, String> socketAPSelector;
    BiFunction<SSLEngine, List<String>, String> engineAPSelector;
    HashMap<HandshakeCompletedListener, AccessControlContext> handshakeListeners;
    boolean noSniExtension;
    boolean noSniMatcher;
    static final boolean useExtendedMasterSecret;
    static final boolean allowLegacyResumption = Utilities.getBooleanProperty("jdk.tls.allowLegacyResumption", true);
    static final boolean allowLegacyMasterSecret = Utilities.getBooleanProperty("jdk.tls.allowLegacyMasterSecret", true);
    static final boolean useCompatibilityMode = Utilities.getBooleanProperty("jdk.tls.client.useCompatibilityMode", true);
    static final boolean acknowledgeCloseNotify = Utilities.getBooleanProperty("jdk.tls.acknowledgeCloseNotify", false);
    static final int maxHandshakeMessageSize = ((Integer) AccessController.doPrivileged(new GetIntegerAction("jdk.tls.maxHandshakeMessageSize", 32768))).intValue();
    static final int maxCertificateChainLength = ((Integer) AccessController.doPrivileged(new GetIntegerAction("jdk.tls.maxCertificateChainLength", 10))).intValue();
    int maximumPacketSize = 0;
    AlgorithmConstraints userSpecifiedAlgorithmConstraints = SSLAlgorithmConstraints.DEFAULT;

    static {
        boolean booleanProperty = Utilities.getBooleanProperty("jdk.tls.useExtendedMasterSecret", true);
        if (booleanProperty) {
            try {
                JsseJce.getKeyGenerator("SunTlsExtendedMasterSecret");
            } catch (NoSuchAlgorithmException e2) {
                booleanProperty = false;
            }
        }
        useExtendedMasterSecret = booleanProperty;
    }

    SSLConfiguration(SSLContextImpl sSLContextImpl, boolean z2) {
        List<SignatureScheme> list;
        this.enabledProtocols = sSLContextImpl.getDefaultProtocolVersions(!z2);
        this.enabledCipherSuites = sSLContextImpl.getDefaultCipherSuites(!z2);
        this.clientAuthType = ClientAuthType.CLIENT_AUTH_NONE;
        this.identificationProtocol = null;
        this.serverNames = Collections.emptyList();
        this.sniMatchers = Collections.emptyList();
        this.preferLocalCipherSuites = false;
        this.applicationProtocols = new String[0];
        if (z2) {
            list = CustomizedClientSignatureSchemes.signatureSchemes;
        } else {
            list = CustomizedServerSignatureSchemes.signatureSchemes;
        }
        this.signatureSchemes = list;
        this.maximumProtocolVersion = ProtocolVersion.NONE;
        for (ProtocolVersion protocolVersion : this.enabledProtocols) {
            if (protocolVersion.compareTo(this.maximumProtocolVersion) > 0) {
                this.maximumProtocolVersion = protocolVersion;
            }
        }
        this.isClientMode = z2;
        this.enableSessionCreation = true;
        this.socketAPSelector = null;
        this.engineAPSelector = null;
        this.handshakeListeners = null;
        this.noSniExtension = false;
        this.noSniMatcher = false;
    }

    SSLParameters getSSLParameters() {
        SSLParameters sSLParameters = new SSLParameters();
        sSLParameters.setAlgorithmConstraints(this.userSpecifiedAlgorithmConstraints);
        sSLParameters.setProtocols(ProtocolVersion.toStringArray(this.enabledProtocols));
        sSLParameters.setCipherSuites(CipherSuite.namesOf(this.enabledCipherSuites));
        switch (this.clientAuthType) {
            case CLIENT_AUTH_REQUIRED:
                sSLParameters.setNeedClientAuth(true);
                break;
            case CLIENT_AUTH_REQUESTED:
                sSLParameters.setWantClientAuth(true);
                break;
            default:
                sSLParameters.setWantClientAuth(false);
                break;
        }
        sSLParameters.setEndpointIdentificationAlgorithm(this.identificationProtocol);
        if (this.serverNames.isEmpty() && !this.noSniExtension) {
            sSLParameters.setServerNames(null);
        } else {
            sSLParameters.setServerNames(this.serverNames);
        }
        if (this.sniMatchers.isEmpty() && !this.noSniMatcher) {
            sSLParameters.setSNIMatchers(null);
        } else {
            sSLParameters.setSNIMatchers(this.sniMatchers);
        }
        sSLParameters.setApplicationProtocols(this.applicationProtocols);
        sSLParameters.setUseCipherSuitesOrder(this.preferLocalCipherSuites);
        return sSLParameters;
    }

    void setSSLParameters(SSLParameters sSLParameters) {
        AlgorithmConstraints algorithmConstraints = sSLParameters.getAlgorithmConstraints();
        if (algorithmConstraints != null) {
            this.userSpecifiedAlgorithmConstraints = algorithmConstraints;
        }
        String[] cipherSuites = sSLParameters.getCipherSuites();
        if (cipherSuites != null) {
            this.enabledCipherSuites = CipherSuite.validValuesOf(cipherSuites);
        }
        String[] protocols = sSLParameters.getProtocols();
        if (protocols != null) {
            this.enabledProtocols = ProtocolVersion.namesOf(protocols);
            this.maximumProtocolVersion = ProtocolVersion.NONE;
            for (ProtocolVersion protocolVersion : this.enabledProtocols) {
                if (protocolVersion.compareTo(this.maximumProtocolVersion) > 0) {
                    this.maximumProtocolVersion = protocolVersion;
                }
            }
        }
        if (sSLParameters.getNeedClientAuth()) {
            this.clientAuthType = ClientAuthType.CLIENT_AUTH_REQUIRED;
        } else if (sSLParameters.getWantClientAuth()) {
            this.clientAuthType = ClientAuthType.CLIENT_AUTH_REQUESTED;
        } else {
            this.clientAuthType = ClientAuthType.CLIENT_AUTH_NONE;
        }
        String endpointIdentificationAlgorithm = sSLParameters.getEndpointIdentificationAlgorithm();
        if (endpointIdentificationAlgorithm != null) {
            this.identificationProtocol = endpointIdentificationAlgorithm;
        }
        List<SNIServerName> serverNames = sSLParameters.getServerNames();
        if (serverNames != null) {
            this.noSniExtension = serverNames.isEmpty();
            this.serverNames = serverNames;
        }
        Collection<SNIMatcher> sNIMatchers = sSLParameters.getSNIMatchers();
        if (sNIMatchers != null) {
            this.noSniMatcher = sNIMatchers.isEmpty();
            this.sniMatchers = sNIMatchers;
        }
        String[] applicationProtocols = sSLParameters.getApplicationProtocols();
        if (applicationProtocols != null) {
            this.applicationProtocols = applicationProtocols;
        }
        this.preferLocalCipherSuites = sSLParameters.getUseCipherSuitesOrder();
    }

    void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        if (this.handshakeListeners == null) {
            this.handshakeListeners = new HashMap<>(4);
        }
        this.handshakeListeners.put(handshakeCompletedListener, AccessController.getContext());
    }

    void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        if (this.handshakeListeners == null) {
            throw new IllegalArgumentException("no listeners");
        }
        if (this.handshakeListeners.remove(handshakeCompletedListener) == null) {
            throw new IllegalArgumentException("listener not registered");
        }
        if (this.handshakeListeners.isEmpty()) {
            this.handshakeListeners = null;
        }
    }

    boolean isAvailable(SSLExtension sSLExtension) {
        Iterator<ProtocolVersion> it = this.enabledProtocols.iterator();
        while (it.hasNext()) {
            if (sSLExtension.isAvailable(it.next())) {
                if (this.isClientMode) {
                    if (SSLExtension.ClientExtensions.defaults.contains(sSLExtension)) {
                        return true;
                    }
                } else if (SSLExtension.ServerExtensions.defaults.contains(sSLExtension)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isAvailable(SSLExtension sSLExtension, ProtocolVersion protocolVersion) {
        return sSLExtension.isAvailable(protocolVersion) && (!this.isClientMode ? !SSLExtension.ServerExtensions.defaults.contains(sSLExtension) : !SSLExtension.ClientExtensions.defaults.contains(sSLExtension));
    }

    SSLExtension[] getEnabledExtensions(SSLHandshake sSLHandshake) {
        ArrayList arrayList = new ArrayList();
        for (SSLExtension sSLExtension : SSLExtension.values()) {
            if (sSLExtension.handshakeType == sSLHandshake && isAvailable(sSLExtension)) {
                arrayList.add(sSLExtension);
            }
        }
        return (SSLExtension[]) arrayList.toArray(new SSLExtension[0]);
    }

    SSLExtension[] getExclusiveExtensions(SSLHandshake sSLHandshake, List<SSLExtension> list) {
        ArrayList arrayList = new ArrayList();
        for (SSLExtension sSLExtension : SSLExtension.values()) {
            if (sSLExtension.handshakeType == sSLHandshake && isAvailable(sSLExtension) && !list.contains(sSLExtension)) {
                arrayList.add(sSLExtension);
            }
        }
        return (SSLExtension[]) arrayList.toArray(new SSLExtension[0]);
    }

    SSLExtension[] getEnabledExtensions(SSLHandshake sSLHandshake, ProtocolVersion protocolVersion) {
        return getEnabledExtensions(sSLHandshake, Arrays.asList(protocolVersion));
    }

    SSLExtension[] getEnabledExtensions(SSLHandshake sSLHandshake, List<ProtocolVersion> list) {
        ArrayList arrayList = new ArrayList();
        for (SSLExtension sSLExtension : SSLExtension.values()) {
            if (sSLExtension.handshakeType == sSLHandshake && isAvailable(sSLExtension)) {
                Iterator<ProtocolVersion> it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    if (sSLExtension.isAvailable(it.next())) {
                        arrayList.add(sSLExtension);
                        break;
                    }
                }
            }
        }
        return (SSLExtension[]) arrayList.toArray(new SSLExtension[0]);
    }

    void toggleClientMode() {
        List<SignatureScheme> list;
        this.isClientMode = !this.isClientMode;
        if (this.isClientMode) {
            list = CustomizedClientSignatureSchemes.signatureSchemes;
        } else {
            list = CustomizedServerSignatureSchemes.signatureSchemes;
        }
        this.signatureSchemes = list;
    }

    public Object clone() {
        try {
            SSLConfiguration sSLConfiguration = (SSLConfiguration) super.clone();
            if (this.handshakeListeners != null) {
                sSLConfiguration.handshakeListeners = (HashMap) this.handshakeListeners.clone();
            }
            return sSLConfiguration;
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLConfiguration$CustomizedClientSignatureSchemes.class */
    private static final class CustomizedClientSignatureSchemes {
        private static List<SignatureScheme> signatureSchemes = SSLConfiguration.getCustomizedSignatureScheme("jdk.tls.client.SignatureSchemes");

        private CustomizedClientSignatureSchemes() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLConfiguration$CustomizedServerSignatureSchemes.class */
    private static final class CustomizedServerSignatureSchemes {
        private static List<SignatureScheme> signatureSchemes = SSLConfiguration.getCustomizedSignatureScheme("jdk.tls.server.SignatureSchemes");

        private CustomizedServerSignatureSchemes() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<SignatureScheme> getCustomizedSignatureScheme(String str) {
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
                    SignatureScheme signatureSchemeNameOf = SignatureScheme.nameOf(strArrSplit[i2]);
                    if (signatureSchemeNameOf != null && signatureSchemeNameOf.isAvailable) {
                        arrayList.add(signatureSchemeNameOf);
                    } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,sslctx")) {
                        SSLLogger.fine("The current installed providers do not support signature scheme: " + strArrSplit[i2], new Object[0]);
                    }
                }
            }
            return arrayList;
        }
        return Collections.emptyList();
    }
}
