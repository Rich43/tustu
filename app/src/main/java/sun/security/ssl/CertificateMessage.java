package sun.security.ssl;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.ClientHello;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.X509Authentication;

/* loaded from: jsse.jar:sun/security/ssl/CertificateMessage.class */
final class CertificateMessage {
    static final SSLConsumer t12HandshakeConsumer = new T12CertificateConsumer();
    static final HandshakeProducer t12HandshakeProducer = new T12CertificateProducer();
    static final SSLConsumer t13HandshakeConsumer = new T13CertificateConsumer();
    static final HandshakeProducer t13HandshakeProducer = new T13CertificateProducer();

    CertificateMessage() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateMessage$T12CertificateMessage.class */
    static final class T12CertificateMessage extends SSLHandshake.HandshakeMessage {
        final List<byte[]> encodedCertChain;

        T12CertificateMessage(HandshakeContext handshakeContext, X509Certificate[] x509CertificateArr) throws SSLException {
            super(handshakeContext);
            ArrayList arrayList = new ArrayList(x509CertificateArr.length);
            for (X509Certificate x509Certificate : x509CertificateArr) {
                try {
                    arrayList.add(x509Certificate.getEncoded());
                } catch (CertificateEncodingException e2) {
                    throw handshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Could not encode certificate (" + ((Object) x509Certificate.getSubjectX500Principal()) + ")", e2);
                }
            }
            this.encodedCertChain = arrayList;
        }

        T12CertificateMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            int int24 = Record.getInt24(byteBuffer);
            if (int24 > byteBuffer.remaining()) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Error parsing certificate message:no sufficient data");
            }
            if (int24 > 0) {
                LinkedList linkedList = new LinkedList();
                while (int24 > 0) {
                    byte[] bytes24 = Record.getBytes24(byteBuffer);
                    int24 -= 3 + bytes24.length;
                    linkedList.add(bytes24);
                    if (linkedList.size() > SSLConfiguration.maxCertificateChainLength) {
                        throw new SSLProtocolException("The certificate chain length (" + linkedList.size() + ") exceeds the maximum allowed length (" + SSLConfiguration.maxCertificateChainLength + ")");
                    }
                }
                this.encodedCertChain = linkedList;
                return;
            }
            this.encodedCertChain = Collections.emptyList();
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            int length = 3;
            Iterator<byte[]> it = this.encodedCertChain.iterator();
            while (it.hasNext()) {
                length += it.next().length + 3;
            }
            return length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            int length = 0;
            Iterator<byte[]> it = this.encodedCertChain.iterator();
            while (it.hasNext()) {
                length += it.next().length + 3;
            }
            handshakeOutStream.putInt24(length);
            Iterator<byte[]> it2 = this.encodedCertChain.iterator();
            while (it2.hasNext()) {
                handshakeOutStream.putBytes24(it2.next());
            }
        }

        public String toString() {
            Object obj;
            if (this.encodedCertChain.isEmpty()) {
                return "\"Certificates\": <empty list>";
            }
            Object[] objArr = new Object[this.encodedCertChain.size()];
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
                int i2 = 0;
                for (byte[] bArr : this.encodedCertChain) {
                    try {
                        obj = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(bArr));
                    } catch (CertificateException e2) {
                        obj = bArr;
                    }
                    int i3 = i2;
                    i2++;
                    objArr[i3] = obj;
                }
            } catch (CertificateException e3) {
                int i4 = 0;
                Iterator<byte[]> it = this.encodedCertChain.iterator();
                while (it.hasNext()) {
                    int i5 = i4;
                    i4++;
                    objArr[i5] = it.next();
                }
            }
            return new MessageFormat("\"Certificates\": [\n{0}\n]", Locale.ENGLISH).format(new Object[]{SSLLogger.toString(objArr)});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateMessage$T12CertificateProducer.class */
    private static final class T12CertificateProducer implements HandshakeProducer {
        private T12CertificateProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            if (((HandshakeContext) connectionContext).sslConfig.isClientMode) {
                return onProduceCertificate((ClientHandshakeContext) connectionContext, handshakeMessage);
            }
            return onProduceCertificate((ServerHandshakeContext) connectionContext, handshakeMessage);
        }

        private byte[] onProduceCertificate(ServerHandshakeContext serverHandshakeContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            X509Authentication.X509Possession x509Possession = null;
            Iterator<SSLPossession> it = serverHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) next;
                    break;
                }
            }
            if (x509Possession == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "No expected X.509 certificate for server authentication");
            }
            serverHandshakeContext.handshakeSession.setLocalPrivateKey(x509Possession.popPrivateKey);
            serverHandshakeContext.handshakeSession.setLocalCertificates(x509Possession.popCerts);
            T12CertificateMessage t12CertificateMessage = new T12CertificateMessage(serverHandshakeContext, x509Possession.popCerts);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced server Certificate handshake message", t12CertificateMessage);
            }
            t12CertificateMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            return null;
        }

        private byte[] onProduceCertificate(ClientHandshakeContext clientHandshakeContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            X509Authentication.X509Possession x509Possession = null;
            Iterator<SSLPossession> it = clientHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) next;
                    break;
                }
            }
            if (x509Possession == null) {
                if (clientHandshakeContext.negotiatedProtocol.useTLS10PlusSpec()) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("No X.509 certificate for client authentication, use empty Certificate message instead", new Object[0]);
                    }
                    x509Possession = new X509Authentication.X509Possession(null, new X509Certificate[0]);
                } else {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("No X.509 certificate for client authentication, send a no_certificate alert", new Object[0]);
                    }
                    clientHandshakeContext.conContext.warning(Alert.NO_CERTIFICATE);
                    return null;
                }
            }
            clientHandshakeContext.handshakeSession.setLocalPrivateKey(x509Possession.popPrivateKey);
            if (x509Possession.popCerts != null && x509Possession.popCerts.length != 0) {
                clientHandshakeContext.handshakeSession.setLocalCertificates(x509Possession.popCerts);
            } else {
                clientHandshakeContext.handshakeSession.setLocalCertificates(null);
            }
            T12CertificateMessage t12CertificateMessage = new T12CertificateMessage(clientHandshakeContext, x509Possession.popCerts);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced client Certificate handshake message", t12CertificateMessage);
            }
            t12CertificateMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateMessage$T12CertificateConsumer.class */
    static final class T12CertificateConsumer implements SSLConsumer {
        private T12CertificateConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException, CertificateException {
            HandshakeContext handshakeContext = (HandshakeContext) connectionContext;
            handshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE.id));
            T12CertificateMessage t12CertificateMessage = new T12CertificateMessage(handshakeContext, byteBuffer);
            if (handshakeContext.sslConfig.isClientMode) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Consuming server Certificate handshake message", t12CertificateMessage);
                }
                onCertificate((ClientHandshakeContext) connectionContext, t12CertificateMessage);
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming client Certificate handshake message", t12CertificateMessage);
            }
            onCertificate((ServerHandshakeContext) connectionContext, t12CertificateMessage);
        }

        private void onCertificate(ServerHandshakeContext serverHandshakeContext, T12CertificateMessage t12CertificateMessage) throws IOException, CertificateException {
            List<byte[]> list = t12CertificateMessage.encodedCertChain;
            if (list == null || list.isEmpty()) {
                serverHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id));
                if (serverHandshakeContext.sslConfig.clientAuthType != ClientAuthType.CLIENT_AUTH_REQUESTED) {
                    throw serverHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "Empty server certificate chain");
                }
                return;
            }
            X509Certificate[] x509CertificateArr = new X509Certificate[list.size()];
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
                int i2 = 0;
                Iterator<byte[]> it = list.iterator();
                while (it.hasNext()) {
                    int i3 = i2;
                    i2++;
                    x509CertificateArr[i3] = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(it.next()));
                }
                checkClientCerts(serverHandshakeContext, x509CertificateArr);
                serverHandshakeContext.handshakeCredentials.add(new X509Authentication.X509Credentials(x509CertificateArr[0].getPublicKey(), x509CertificateArr));
                serverHandshakeContext.handshakeSession.setPeerCertificates(x509CertificateArr);
            } catch (CertificateException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "Failed to parse server certificates", e2);
            }
        }

        private void onCertificate(ClientHandshakeContext clientHandshakeContext, T12CertificateMessage t12CertificateMessage) throws IOException, CertificateException {
            String str;
            List<byte[]> list = t12CertificateMessage.encodedCertChain;
            if (list == null || list.isEmpty()) {
                throw clientHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "Empty server certificate chain");
            }
            X509Certificate[] x509CertificateArr = new X509Certificate[list.size()];
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
                int i2 = 0;
                Iterator<byte[]> it = list.iterator();
                while (it.hasNext()) {
                    int i3 = i2;
                    i2++;
                    x509CertificateArr[i3] = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(it.next()));
                }
                if (clientHandshakeContext.reservedServerCerts != null && !clientHandshakeContext.handshakeSession.useExtendedMasterSecret && (((str = clientHandshakeContext.sslConfig.identificationProtocol) == null || str.isEmpty()) && !isIdentityEquivalent(x509CertificateArr[0], clientHandshakeContext.reservedServerCerts[0]))) {
                    throw clientHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "server certificate change is restricted during renegotiation");
                }
                if (clientHandshakeContext.staplingActive) {
                    clientHandshakeContext.deferredCerts = x509CertificateArr;
                } else {
                    checkServerCerts(clientHandshakeContext, x509CertificateArr);
                }
                clientHandshakeContext.handshakeCredentials.add(new X509Authentication.X509Credentials(x509CertificateArr[0].getPublicKey(), x509CertificateArr));
                clientHandshakeContext.handshakeSession.setPeerCertificates(x509CertificateArr);
            } catch (CertificateException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "Failed to parse server certificates", e2);
            }
        }

        private static boolean isIdentityEquivalent(X509Certificate x509Certificate, X509Certificate x509Certificate2) {
            if (x509Certificate.equals(x509Certificate2)) {
                return true;
            }
            Collection<List<?>> subjectAlternativeNames = null;
            try {
                subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            } catch (CertificateParsingException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("handshake")) {
                    SSLLogger.fine("Attempt to obtain subjectAltNames extension failed!", new Object[0]);
                }
            }
            Collection<List<?>> subjectAlternativeNames2 = null;
            try {
                subjectAlternativeNames2 = x509Certificate2.getSubjectAlternativeNames();
            } catch (CertificateParsingException e3) {
                if (SSLLogger.isOn && SSLLogger.isOn("handshake")) {
                    SSLLogger.fine("Attempt to obtain subjectAltNames extension failed!", new Object[0]);
                }
            }
            if (subjectAlternativeNames != null && subjectAlternativeNames2 != null) {
                Collection<String> subjectAltNames = getSubjectAltNames(subjectAlternativeNames, 7);
                Collection<String> subjectAltNames2 = getSubjectAltNames(subjectAlternativeNames2, 7);
                if (subjectAltNames != null && subjectAltNames2 != null && isEquivalent(subjectAltNames, subjectAltNames2)) {
                    return true;
                }
                Collection<String> subjectAltNames3 = getSubjectAltNames(subjectAlternativeNames, 2);
                Collection<String> subjectAltNames4 = getSubjectAltNames(subjectAlternativeNames2, 2);
                if (subjectAltNames3 != null && subjectAltNames4 != null && isEquivalent(subjectAltNames3, subjectAltNames4)) {
                    return true;
                }
            }
            X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
            X500Principal subjectX500Principal2 = x509Certificate2.getSubjectX500Principal();
            return !subjectX500Principal.getName().isEmpty() && !subjectX500Principal2.getName().isEmpty() && subjectX500Principal.equals(subjectX500Principal2) && x509Certificate.getIssuerX500Principal().equals(x509Certificate2.getIssuerX500Principal());
        }

        private static Collection<String> getSubjectAltNames(Collection<List<?>> collection, int i2) {
            String str;
            HashSet hashSet = null;
            for (List<?> list : collection) {
                if (((Integer) list.get(0)).intValue() == i2 && (str = (String) list.get(1)) != null && !str.isEmpty()) {
                    if (hashSet == null) {
                        hashSet = new HashSet(collection.size());
                    }
                    hashSet.add(str);
                }
            }
            return hashSet;
        }

        private static boolean isEquivalent(Collection<String> collection, Collection<String> collection2) {
            for (String str : collection) {
                Iterator<String> it = collection2.iterator();
                while (it.hasNext()) {
                    if (str.equalsIgnoreCase(it.next())) {
                        return true;
                    }
                }
            }
            return false;
        }

        static void checkServerCerts(ClientHandshakeContext clientHandshakeContext, X509Certificate[] x509CertificateArr) throws IOException, CertificateException {
            String str;
            X509TrustManager x509TrustManager = clientHandshakeContext.sslContext.getX509TrustManager();
            if (clientHandshakeContext.negotiatedCipherSuite.keyExchange == CipherSuite.KeyExchange.K_RSA_EXPORT || clientHandshakeContext.negotiatedCipherSuite.keyExchange == CipherSuite.KeyExchange.K_DHE_RSA_EXPORT) {
                str = CipherSuite.KeyExchange.K_RSA.name;
            } else {
                str = clientHandshakeContext.negotiatedCipherSuite.keyExchange.name;
            }
            try {
                if (x509TrustManager instanceof X509ExtendedTrustManager) {
                    if (clientHandshakeContext.conContext.transport instanceof SSLEngine) {
                        ((X509ExtendedTrustManager) x509TrustManager).checkServerTrusted((X509Certificate[]) x509CertificateArr.clone(), str, (SSLEngine) clientHandshakeContext.conContext.transport);
                    } else {
                        ((X509ExtendedTrustManager) x509TrustManager).checkServerTrusted((X509Certificate[]) x509CertificateArr.clone(), str, (SSLSocket) clientHandshakeContext.conContext.transport);
                    }
                    clientHandshakeContext.handshakeSession.setPeerCertificates(x509CertificateArr);
                    return;
                }
                throw new CertificateException("Improper X509TrustManager implementation");
            } catch (CertificateException e2) {
                throw clientHandshakeContext.conContext.fatal(getCertificateAlert(clientHandshakeContext, e2), e2);
            }
        }

        private static void checkClientCerts(ServerHandshakeContext serverHandshakeContext, X509Certificate[] x509CertificateArr) throws IOException, CertificateException {
            String algorithm;
            String str;
            X509TrustManager x509TrustManager = serverHandshakeContext.sslContext.getX509TrustManager();
            algorithm = x509CertificateArr[0].getPublicKey().getAlgorithm();
            switch (algorithm) {
                case "RSA":
                case "DSA":
                case "EC":
                case "RSASSA-PSS":
                    str = algorithm;
                    break;
                default:
                    str = "UNKNOWN";
                    break;
            }
            try {
                if (x509TrustManager instanceof X509ExtendedTrustManager) {
                    if (serverHandshakeContext.conContext.transport instanceof SSLEngine) {
                        ((X509ExtendedTrustManager) x509TrustManager).checkClientTrusted((X509Certificate[]) x509CertificateArr.clone(), str, (SSLEngine) serverHandshakeContext.conContext.transport);
                    } else {
                        ((X509ExtendedTrustManager) x509TrustManager).checkClientTrusted((X509Certificate[]) x509CertificateArr.clone(), str, (SSLSocket) serverHandshakeContext.conContext.transport);
                    }
                    return;
                }
                throw new CertificateException("Improper X509TrustManager implementation");
            } catch (CertificateException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.CERTIFICATE_UNKNOWN, e2);
            }
        }

        private static Alert getCertificateAlert(ClientHandshakeContext clientHandshakeContext, CertificateException certificateException) {
            Alert alert = Alert.CERTIFICATE_UNKNOWN;
            Throwable cause = certificateException.getCause();
            if (cause instanceof CertPathValidatorException) {
                CertPathValidatorException.Reason reason = ((CertPathValidatorException) cause).getReason();
                if (reason == CertPathValidatorException.BasicReason.REVOKED) {
                    alert = clientHandshakeContext.staplingActive ? Alert.BAD_CERT_STATUS_RESPONSE : Alert.CERTIFICATE_REVOKED;
                } else if (reason == CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS) {
                    alert = clientHandshakeContext.staplingActive ? Alert.BAD_CERT_STATUS_RESPONSE : Alert.CERTIFICATE_UNKNOWN;
                } else if (reason == CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED) {
                    alert = Alert.UNSUPPORTED_CERTIFICATE;
                } else if (reason == CertPathValidatorException.BasicReason.EXPIRED) {
                    alert = Alert.CERTIFICATE_EXPIRED;
                } else if (reason == CertPathValidatorException.BasicReason.INVALID_SIGNATURE || reason == CertPathValidatorException.BasicReason.NOT_YET_VALID) {
                    alert = Alert.BAD_CERTIFICATE;
                }
            }
            return alert;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateMessage$CertificateEntry.class */
    static final class CertificateEntry {
        final byte[] encoded;
        private final SSLExtensions extensions;

        CertificateEntry(byte[] bArr, SSLExtensions sSLExtensions) {
            this.encoded = bArr;
            this.extensions = sSLExtensions;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getEncodedSize() {
            int length = this.extensions.length();
            if (length == 0) {
                length = 2;
            }
            return 3 + this.encoded.length + length;
        }

        public String toString() {
            Object objGenerateCertificate;
            MessageFormat messageFormat = new MessageFormat("\n'{'\n{0}\n  \"extensions\": '{'\n{1}\n  '}'\n'}',", Locale.ENGLISH);
            try {
                objGenerateCertificate = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID).generateCertificate(new ByteArrayInputStream(this.encoded));
            } catch (CertificateException e2) {
                objGenerateCertificate = this.encoded;
            }
            return messageFormat.format(new Object[]{SSLLogger.toString(objGenerateCertificate), Utilities.indent(this.extensions.toString(), "    ")});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateMessage$T13CertificateMessage.class */
    static final class T13CertificateMessage extends SSLHandshake.HandshakeMessage {
        private final byte[] requestContext;
        private final List<CertificateEntry> certEntries;

        T13CertificateMessage(HandshakeContext handshakeContext, byte[] bArr, X509Certificate[] x509CertificateArr) throws SSLException, CertificateException {
            super(handshakeContext);
            this.requestContext = (byte[]) bArr.clone();
            this.certEntries = new LinkedList();
            for (X509Certificate x509Certificate : x509CertificateArr) {
                this.certEntries.add(new CertificateEntry(x509Certificate.getEncoded(), new SSLExtensions(this)));
            }
        }

        T13CertificateMessage(HandshakeContext handshakeContext, byte[] bArr, List<CertificateEntry> list) {
            super(handshakeContext);
            this.requestContext = (byte[]) bArr.clone();
            this.certEntries = list;
        }

        T13CertificateMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.remaining() < 4) {
                throw new SSLProtocolException("Invalid Certificate message: insufficient data (length=" + byteBuffer.remaining() + ")");
            }
            this.requestContext = Record.getBytes8(byteBuffer);
            if (byteBuffer.remaining() < 3) {
                throw new SSLProtocolException("Invalid Certificate message: insufficient certificate entries data (length=" + byteBuffer.remaining() + ")");
            }
            int int24 = Record.getInt24(byteBuffer);
            if (int24 != byteBuffer.remaining()) {
                throw new SSLProtocolException("Invalid Certificate message: incorrect list length (length=" + int24 + ")");
            }
            SSLExtension[] enabledExtensions = handshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CERTIFICATE);
            LinkedList linkedList = new LinkedList();
            while (byteBuffer.hasRemaining()) {
                byte[] bytes24 = Record.getBytes24(byteBuffer);
                if (bytes24.length == 0) {
                    throw new SSLProtocolException("Invalid Certificate message: empty cert_data");
                }
                linkedList.add(new CertificateEntry(bytes24, new SSLExtensions(this, byteBuffer, enabledExtensions)));
                if (linkedList.size() > SSLConfiguration.maxCertificateChainLength) {
                    throw new SSLProtocolException("The certificate chain length (" + linkedList.size() + ") exceeds the maximum allowed length (" + SSLConfiguration.maxCertificateChainLength + ")");
                }
            }
            this.certEntries = Collections.unmodifiableList(linkedList);
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            int length = 4 + this.requestContext.length;
            Iterator<CertificateEntry> it = this.certEntries.iterator();
            while (it.hasNext()) {
                length += it.next().getEncodedSize();
            }
            return length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            int encodedSize = 0;
            Iterator<CertificateEntry> it = this.certEntries.iterator();
            while (it.hasNext()) {
                encodedSize += it.next().getEncodedSize();
            }
            handshakeOutStream.putBytes8(this.requestContext);
            handshakeOutStream.putInt24(encodedSize);
            for (CertificateEntry certificateEntry : this.certEntries) {
                handshakeOutStream.putBytes24(certificateEntry.encoded);
                if (certificateEntry.extensions.length() != 0) {
                    certificateEntry.extensions.send(handshakeOutStream);
                } else {
                    handshakeOutStream.putInt16(0);
                }
            }
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"Certificate\": '{'\n  \"certificate_request_context\": \"{0}\",\n  \"certificate_list\": [{1}\n]\n'}'", Locale.ENGLISH);
            StringBuilder sb = new StringBuilder(512);
            Iterator<CertificateEntry> it = this.certEntries.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString());
            }
            return messageFormat.format(new Object[]{Utilities.toHexString(this.requestContext), Utilities.indent(sb.toString())});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateMessage$T13CertificateProducer.class */
    private static final class T13CertificateProducer implements HandshakeProducer {
        private T13CertificateProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            if (((HandshakeContext) connectionContext).sslConfig.isClientMode) {
                return onProduceCertificate((ClientHandshakeContext) connectionContext, handshakeMessage);
            }
            return onProduceCertificate((ServerHandshakeContext) connectionContext, handshakeMessage);
        }

        private byte[] onProduceCertificate(ServerHandshakeContext serverHandshakeContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            SSLPossession sSLPossessionChoosePossession = choosePossession(serverHandshakeContext, (ClientHello.ClientHelloMessage) handshakeMessage);
            if (sSLPossessionChoosePossession == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No available authentication scheme");
            }
            if (!(sSLPossessionChoosePossession instanceof X509Authentication.X509Possession)) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No X.509 certificate for server authentication");
            }
            X509Authentication.X509Possession x509Possession = (X509Authentication.X509Possession) sSLPossessionChoosePossession;
            X509Certificate[] x509CertificateArr = x509Possession.popCerts;
            if (x509CertificateArr == null || x509CertificateArr.length == 0) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No X.509 certificate for server authentication");
            }
            serverHandshakeContext.handshakePossessions.add(x509Possession);
            serverHandshakeContext.handshakeSession.setLocalPrivateKey(x509Possession.popPrivateKey);
            serverHandshakeContext.handshakeSession.setLocalCertificates(x509CertificateArr);
            try {
                T13CertificateMessage t13CertificateMessage = new T13CertificateMessage(serverHandshakeContext, new byte[0], x509CertificateArr);
                serverHandshakeContext.stapleParams = StatusResponseManager.processStapling(serverHandshakeContext);
                serverHandshakeContext.staplingActive = serverHandshakeContext.stapleParams != null;
                SSLExtension[] enabledExtensions = serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CERTIFICATE, Arrays.asList(ProtocolVersion.PROTOCOLS_OF_13));
                for (CertificateEntry certificateEntry : t13CertificateMessage.certEntries) {
                    serverHandshakeContext.currentCertEntry = certificateEntry;
                    certificateEntry.extensions.produce(serverHandshakeContext, enabledExtensions);
                }
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Produced server Certificate message", t13CertificateMessage);
                }
                t13CertificateMessage.write(serverHandshakeContext.handshakeOutput);
                serverHandshakeContext.handshakeOutput.flush();
                return null;
            } catch (CertificateException | SSLException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Failed to produce server Certificate message", e2);
            }
        }

        private static SSLPossession choosePossession(HandshakeContext handshakeContext, ClientHello.ClientHelloMessage clientHelloMessage) throws IOException {
            if (handshakeContext.peerRequestedCertSignSchemes == null || handshakeContext.peerRequestedCertSignSchemes.isEmpty()) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("No signature_algorithms(_cert) in ClientHello", new Object[0]);
                    return null;
                }
                return null;
            }
            HashSet hashSet = new HashSet();
            for (SignatureScheme signatureScheme : handshakeContext.peerRequestedCertSignSchemes) {
                if (hashSet.contains(signatureScheme.keyAlgorithm)) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.warning("Unsupported authentication scheme: " + signatureScheme.name, new Object[0]);
                    }
                } else if (SignatureScheme.getPreferableAlgorithm(handshakeContext.peerRequestedSignatureSchemes, signatureScheme, handshakeContext.negotiatedProtocol) == null) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.warning("Unable to produce CertificateVerify for signature scheme: " + signatureScheme.name, new Object[0]);
                    }
                    hashSet.add(signatureScheme.keyAlgorithm);
                } else {
                    X509Authentication x509AuthenticationValueOf = X509Authentication.valueOf(signatureScheme);
                    if (x509AuthenticationValueOf == null) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                            SSLLogger.warning("Unsupported authentication scheme: " + signatureScheme.name, new Object[0]);
                        }
                        hashSet.add(signatureScheme.keyAlgorithm);
                    } else {
                        SSLPossession sSLPossessionCreatePossession = x509AuthenticationValueOf.createPossession(handshakeContext);
                        if (sSLPossessionCreatePossession == null) {
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                                SSLLogger.warning("Unavailable authentication scheme: " + signatureScheme.name, new Object[0]);
                            }
                        } else {
                            return sSLPossessionCreatePossession;
                        }
                    }
                }
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.warning("No available authentication scheme", new Object[0]);
                return null;
            }
            return null;
        }

        private byte[] onProduceCertificate(ClientHandshakeContext clientHandshakeContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            X509Certificate[] x509CertificateArr;
            SSLPossession sSLPossessionChoosePossession = choosePossession(clientHandshakeContext, (ClientHello.ClientHelloMessage) handshakeMessage);
            if (sSLPossessionChoosePossession == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("No available client authentication scheme", new Object[0]);
                }
                x509CertificateArr = new X509Certificate[0];
            } else {
                clientHandshakeContext.handshakePossessions.add(sSLPossessionChoosePossession);
                if (!(sSLPossessionChoosePossession instanceof X509Authentication.X509Possession)) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("No X.509 certificate for client authentication", new Object[0]);
                    }
                    x509CertificateArr = new X509Certificate[0];
                } else {
                    X509Authentication.X509Possession x509Possession = (X509Authentication.X509Possession) sSLPossessionChoosePossession;
                    x509CertificateArr = x509Possession.popCerts;
                    clientHandshakeContext.handshakeSession.setLocalPrivateKey(x509Possession.popPrivateKey);
                }
            }
            if (x509CertificateArr != null && x509CertificateArr.length != 0) {
                clientHandshakeContext.handshakeSession.setLocalCertificates(x509CertificateArr);
            } else {
                clientHandshakeContext.handshakeSession.setLocalCertificates(null);
            }
            try {
                T13CertificateMessage t13CertificateMessage = new T13CertificateMessage(clientHandshakeContext, clientHandshakeContext.certRequestContext, x509CertificateArr);
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Produced client Certificate message", t13CertificateMessage);
                }
                t13CertificateMessage.write(clientHandshakeContext.handshakeOutput);
                clientHandshakeContext.handshakeOutput.flush();
                return null;
            } catch (CertificateException | SSLException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Failed to produce client Certificate message", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateMessage$T13CertificateConsumer.class */
    private static final class T13CertificateConsumer implements SSLConsumer {
        private T13CertificateConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException, CertificateException {
            HandshakeContext handshakeContext = (HandshakeContext) connectionContext;
            handshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE.id));
            T13CertificateMessage t13CertificateMessage = new T13CertificateMessage(handshakeContext, byteBuffer);
            if (handshakeContext.sslConfig.isClientMode) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Consuming server Certificate handshake message", t13CertificateMessage);
                }
                onConsumeCertificate((ClientHandshakeContext) connectionContext, t13CertificateMessage);
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming client Certificate handshake message", t13CertificateMessage);
            }
            onConsumeCertificate((ServerHandshakeContext) connectionContext, t13CertificateMessage);
        }

        private void onConsumeCertificate(ServerHandshakeContext serverHandshakeContext, T13CertificateMessage t13CertificateMessage) throws IOException, CertificateException {
            if (t13CertificateMessage.certEntries == null || t13CertificateMessage.certEntries.isEmpty()) {
                serverHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id));
                if (serverHandshakeContext.sslConfig.clientAuthType == ClientAuthType.CLIENT_AUTH_REQUIRED) {
                    throw serverHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "Empty client certificate chain");
                }
            } else {
                X509Certificate[] x509CertificateArrCheckClientCerts = checkClientCerts(serverHandshakeContext, t13CertificateMessage.certEntries);
                serverHandshakeContext.handshakeCredentials.add(new X509Authentication.X509Credentials(x509CertificateArrCheckClientCerts[0].getPublicKey(), x509CertificateArrCheckClientCerts));
                serverHandshakeContext.handshakeSession.setPeerCertificates(x509CertificateArrCheckClientCerts);
            }
        }

        private void onConsumeCertificate(ClientHandshakeContext clientHandshakeContext, T13CertificateMessage t13CertificateMessage) throws IOException, CertificateException {
            if (t13CertificateMessage.certEntries == null || t13CertificateMessage.certEntries.isEmpty()) {
                throw clientHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "Empty server certificate chain");
            }
            SSLExtension[] enabledExtensions = clientHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CERTIFICATE);
            Iterator it = t13CertificateMessage.certEntries.iterator();
            while (it.hasNext()) {
                ((CertificateEntry) it.next()).extensions.consumeOnLoad(clientHandshakeContext, enabledExtensions);
            }
            X509Certificate[] x509CertificateArrCheckServerCerts = checkServerCerts(clientHandshakeContext, t13CertificateMessage.certEntries);
            clientHandshakeContext.handshakeCredentials.add(new X509Authentication.X509Credentials(x509CertificateArrCheckServerCerts[0].getPublicKey(), x509CertificateArrCheckServerCerts));
            clientHandshakeContext.handshakeSession.setPeerCertificates(x509CertificateArrCheckServerCerts);
        }

        private static X509Certificate[] checkClientCerts(ServerHandshakeContext serverHandshakeContext, List<CertificateEntry> list) throws IOException, CertificateException {
            String algorithm;
            String str;
            X509Certificate[] x509CertificateArr = new X509Certificate[list.size()];
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
                int i2 = 0;
                Iterator<CertificateEntry> it = list.iterator();
                while (it.hasNext()) {
                    int i3 = i2;
                    i2++;
                    x509CertificateArr[i3] = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(it.next().encoded));
                }
                algorithm = x509CertificateArr[0].getPublicKey().getAlgorithm();
                switch (algorithm) {
                    case "RSA":
                    case "DSA":
                    case "EC":
                    case "RSASSA-PSS":
                        str = algorithm;
                        break;
                    default:
                        str = "UNKNOWN";
                        break;
                }
                try {
                    X509TrustManager x509TrustManager = serverHandshakeContext.sslContext.getX509TrustManager();
                    if (x509TrustManager instanceof X509ExtendedTrustManager) {
                        if (serverHandshakeContext.conContext.transport instanceof SSLEngine) {
                            ((X509ExtendedTrustManager) x509TrustManager).checkClientTrusted((X509Certificate[]) x509CertificateArr.clone(), str, (SSLEngine) serverHandshakeContext.conContext.transport);
                        } else {
                            ((X509ExtendedTrustManager) x509TrustManager).checkClientTrusted((X509Certificate[]) x509CertificateArr.clone(), str, (SSLSocket) serverHandshakeContext.conContext.transport);
                        }
                        serverHandshakeContext.handshakeSession.setPeerCertificates(x509CertificateArr);
                        return x509CertificateArr;
                    }
                    throw new CertificateException("Improper X509TrustManager implementation");
                } catch (CertificateException e2) {
                    throw serverHandshakeContext.conContext.fatal(Alert.CERTIFICATE_UNKNOWN, e2);
                }
            } catch (CertificateException e3) {
                throw serverHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "Failed to parse server certificates", e3);
            }
        }

        private static X509Certificate[] checkServerCerts(ClientHandshakeContext clientHandshakeContext, List<CertificateEntry> list) throws IOException, CertificateException {
            X509Certificate[] x509CertificateArr = new X509Certificate[list.size()];
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
                int i2 = 0;
                Iterator<CertificateEntry> it = list.iterator();
                while (it.hasNext()) {
                    int i3 = i2;
                    i2++;
                    x509CertificateArr[i3] = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(it.next().encoded));
                }
                try {
                    X509TrustManager x509TrustManager = clientHandshakeContext.sslContext.getX509TrustManager();
                    if (x509TrustManager instanceof X509ExtendedTrustManager) {
                        if (clientHandshakeContext.conContext.transport instanceof SSLEngine) {
                            ((X509ExtendedTrustManager) x509TrustManager).checkServerTrusted((X509Certificate[]) x509CertificateArr.clone(), "UNKNOWN", (SSLEngine) clientHandshakeContext.conContext.transport);
                        } else {
                            ((X509ExtendedTrustManager) x509TrustManager).checkServerTrusted((X509Certificate[]) x509CertificateArr.clone(), "UNKNOWN", (SSLSocket) clientHandshakeContext.conContext.transport);
                        }
                        clientHandshakeContext.handshakeSession.setPeerCertificates(x509CertificateArr);
                        return x509CertificateArr;
                    }
                    throw new CertificateException("Improper X509TrustManager implementation");
                } catch (CertificateException e2) {
                    throw clientHandshakeContext.conContext.fatal(getCertificateAlert(clientHandshakeContext, e2), e2);
                }
            } catch (CertificateException e3) {
                throw clientHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "Failed to parse server certificates", e3);
            }
        }

        private static Alert getCertificateAlert(ClientHandshakeContext clientHandshakeContext, CertificateException certificateException) {
            Alert alert = Alert.CERTIFICATE_UNKNOWN;
            Throwable cause = certificateException.getCause();
            if (cause instanceof CertPathValidatorException) {
                CertPathValidatorException.Reason reason = ((CertPathValidatorException) cause).getReason();
                if (reason == CertPathValidatorException.BasicReason.REVOKED) {
                    alert = clientHandshakeContext.staplingActive ? Alert.BAD_CERT_STATUS_RESPONSE : Alert.CERTIFICATE_REVOKED;
                } else if (reason == CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS) {
                    alert = clientHandshakeContext.staplingActive ? Alert.BAD_CERT_STATUS_RESPONSE : Alert.CERTIFICATE_UNKNOWN;
                }
            }
            return alert;
        }
    }
}
