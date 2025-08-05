package sun.security.ssl;

import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLHandshakeException;
import sun.security.util.SecurityConstants;

/* loaded from: jsse.jar:sun/security/ssl/KrbKeyExchange.class */
final class KrbKeyExchange {
    static final SSLPossessionGenerator poGenerator = new KrbPossessionGenerator();
    static final SSLKeyAgreementGenerator kaGenerator = new KrbKAGenerator();

    KrbKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/KrbKeyExchange$KrbPossessionGenerator.class */
    static final class KrbPossessionGenerator implements SSLPossessionGenerator {
        KrbPossessionGenerator() {
        }

        @Override // sun.security.ssl.SSLPossessionGenerator
        public SSLPossession createPossession(HandshakeContext handshakeContext) {
            try {
                final AccessControlContext accessControlContext = handshakeContext.conContext.acc;
                Object objDoPrivileged = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: sun.security.ssl.KrbKeyExchange.KrbPossessionGenerator.1
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() throws Exception {
                        return Krb5Helper.getServiceCreds(accessControlContext);
                    }
                });
                if (objDoPrivileged != null) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("Using Kerberos creds", new Object[0]);
                    }
                    String serverPrincipalName = Krb5Helper.getServerPrincipalName(objDoPrivileged);
                    if (serverPrincipalName != null) {
                        SecurityManager securityManager = System.getSecurityManager();
                        if (securityManager != null) {
                            try {
                                securityManager.checkPermission(Krb5Helper.getServicePermission(serverPrincipalName, SecurityConstants.SOCKET_ACCEPT_ACTION), accessControlContext);
                            } catch (SecurityException e2) {
                                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                                    SSLLogger.fine("Permission to access Kerberos secret key denied", new Object[0]);
                                    return null;
                                }
                                return null;
                            }
                        }
                    }
                    return new KrbServiceCreds(objDoPrivileged);
                }
                return null;
            } catch (PrivilegedActionException e3) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Attempt to obtain Kerberos key failed: " + e3.toString(), new Object[0]);
                    return null;
                }
                return null;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KrbKeyExchange$KrbServiceCreds.class */
    static final class KrbServiceCreds implements SSLPossession {
        final Object serviceCreds;

        KrbServiceCreds(Object obj) {
            this.serviceCreds = obj;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KrbKeyExchange$KrbPremasterSecret.class */
    static final class KrbPremasterSecret implements SSLPossession, SSLCredentials {
        final byte[] preMaster;

        KrbPremasterSecret(byte[] bArr) {
            this.preMaster = bArr;
        }

        static KrbPremasterSecret createPremasterSecret(ProtocolVersion protocolVersion, SecureRandom secureRandom) {
            byte[] bArr = new byte[48];
            secureRandom.nextBytes(bArr);
            bArr[0] = protocolVersion.major;
            bArr[1] = protocolVersion.minor;
            return new KrbPremasterSecret(bArr);
        }

        static KrbPremasterSecret decode(ProtocolVersion protocolVersion, ProtocolVersion protocolVersion2, byte[] bArr, SecureRandom secureRandom) {
            KrbPremasterSecret krbPremasterSecret;
            boolean z2 = true;
            if (bArr != null && bArr.length == 48) {
                ProtocolVersion protocolVersionValueOf = ProtocolVersion.valueOf(bArr[0], bArr[1]);
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Kerberos pre-master secret protocol version: " + ((Object) protocolVersionValueOf), new Object[0]);
                }
                z2 = protocolVersionValueOf.compare(protocolVersion2) != 0;
                if (z2 && protocolVersion2.compare(ProtocolVersion.TLS10) <= 0) {
                    z2 = protocolVersionValueOf.compare(protocolVersion) != 0;
                }
            }
            if (z2) {
                krbPremasterSecret = createPremasterSecret(protocolVersion2, secureRandom);
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Kerberos pre-master secret error, generating random secret for safe failure.", new Object[0]);
                }
            } else {
                krbPremasterSecret = new KrbPremasterSecret(bArr);
            }
            return krbPremasterSecret;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KrbKeyExchange$KrbKAGenerator.class */
    private static final class KrbKAGenerator implements SSLKeyAgreementGenerator {
        private KrbKAGenerator() {
        }

        @Override // sun.security.ssl.SSLKeyAgreementGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext) throws IOException {
            KrbPremasterSecret krbPremasterSecret = null;
            if (handshakeContext instanceof ClientHandshakeContext) {
                Iterator<SSLPossession> it = handshakeContext.handshakePossessions.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SSLPossession next = it.next();
                    if (next instanceof KrbPremasterSecret) {
                        krbPremasterSecret = (KrbPremasterSecret) next;
                        break;
                    }
                }
            } else {
                Iterator<SSLCredentials> it2 = handshakeContext.handshakeCredentials.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    SSLCredentials next2 = it2.next();
                    if (next2 instanceof KrbPremasterSecret) {
                        krbPremasterSecret = (KrbPremasterSecret) next2;
                        break;
                    }
                }
            }
            if (krbPremasterSecret == null) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No sufficient KRB key agreement parameters negotiated");
            }
            return new KRBKAKeyDerivation(handshakeContext, krbPremasterSecret.preMaster);
        }

        /* loaded from: jsse.jar:sun/security/ssl/KrbKeyExchange$KrbKAGenerator$KRBKAKeyDerivation.class */
        private static final class KRBKAKeyDerivation implements SSLKeyDerivation {
            private final HandshakeContext context;
            private final byte[] secretBytes;

            KRBKAKeyDerivation(HandshakeContext handshakeContext, byte[] bArr) {
                this.context = handshakeContext;
                this.secretBytes = bArr;
            }

            @Override // sun.security.ssl.SSLKeyDerivation
            public SecretKey deriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IOException {
                try {
                    SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretBytes, "TlsPremasterSecret");
                    SSLMasterKeyDerivation sSLMasterKeyDerivationValueOf = SSLMasterKeyDerivation.valueOf(this.context.negotiatedProtocol);
                    if (sSLMasterKeyDerivationValueOf == null) {
                        throw new SSLHandshakeException("No expected master key derivation for protocol: " + this.context.negotiatedProtocol.name);
                    }
                    return sSLMasterKeyDerivationValueOf.createKeyDerivation(this.context, secretKeySpec).deriveKey("MasterSecret", algorithmParameterSpec);
                } catch (Exception e2) {
                    throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate secret").initCause(e2));
                }
            }
        }
    }
}
