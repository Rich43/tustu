package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.security.auth.x500.X500Principal;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.X509Authentication;

/* loaded from: jsse.jar:sun/security/ssl/CertificateRequest.class */
final class CertificateRequest {
    static final SSLConsumer t10HandshakeConsumer = new T10CertificateRequestConsumer();
    static final HandshakeProducer t10HandshakeProducer = new T10CertificateRequestProducer();
    static final SSLConsumer t12HandshakeConsumer = new T12CertificateRequestConsumer();
    static final HandshakeProducer t12HandshakeProducer = new T12CertificateRequestProducer();
    static final SSLConsumer t13HandshakeConsumer = new T13CertificateRequestConsumer();
    static final HandshakeProducer t13HandshakeProducer = new T13CertificateRequestProducer();

    CertificateRequest() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$ClientCertificateType.class */
    private enum ClientCertificateType {
        RSA_SIGN((byte) 1, "rsa_sign", "RSA", true),
        DSS_SIGN((byte) 2, "dss_sign", "DSA", true),
        RSA_FIXED_DH((byte) 3, "rsa_fixed_dh"),
        DSS_FIXED_DH((byte) 4, "dss_fixed_dh"),
        RSA_EPHEMERAL_DH((byte) 5, "rsa_ephemeral_dh"),
        DSS_EPHEMERAL_DH((byte) 6, "dss_ephemeral_dh"),
        FORTEZZA_DMS((byte) 20, "fortezza_dms"),
        ECDSA_SIGN((byte) 64, "ecdsa_sign", "EC", JsseJce.isEcAvailable()),
        RSA_FIXED_ECDH((byte) 65, "rsa_fixed_ecdh"),
        ECDSA_FIXED_ECDH((byte) 66, "ecdsa_fixed_ecdh");

        private static final byte[] CERT_TYPES;
        final byte id;
        final String name;
        final String keyAlgorithm;
        final boolean isAvailable;

        static {
            CERT_TYPES = JsseJce.isEcAvailable() ? new byte[]{ECDSA_SIGN.id, RSA_SIGN.id, DSS_SIGN.id} : new byte[]{RSA_SIGN.id, DSS_SIGN.id};
        }

        ClientCertificateType(byte b2, String str) {
            this(b2, str, null, false);
        }

        ClientCertificateType(byte b2, String str, String str2, boolean z2) {
            this.id = b2;
            this.name = str;
            this.keyAlgorithm = str2;
            this.isAvailable = z2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String nameOf(byte b2) {
            for (ClientCertificateType clientCertificateType : values()) {
                if (clientCertificateType.id == b2) {
                    return clientCertificateType.name;
                }
            }
            return "UNDEFINED-CLIENT-CERTIFICATE-TYPE(" + ((int) b2) + ")";
        }

        private static ClientCertificateType valueOf(byte b2) {
            for (ClientCertificateType clientCertificateType : values()) {
                if (clientCertificateType.id == b2) {
                    return clientCertificateType;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String[] getKeyTypes(byte[] bArr) {
            ArrayList arrayList = new ArrayList(3);
            for (byte b2 : bArr) {
                ClientCertificateType clientCertificateTypeValueOf = valueOf(b2);
                if (clientCertificateTypeValueOf.isAvailable) {
                    arrayList.add(clientCertificateTypeValueOf.keyAlgorithm);
                }
            }
            return (String[]) arrayList.toArray(new String[0]);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$T10CertificateRequestMessage.class */
    static final class T10CertificateRequestMessage extends SSLHandshake.HandshakeMessage {
        final byte[] types;
        final List<byte[]> authorities;

        T10CertificateRequestMessage(HandshakeContext handshakeContext, X509Certificate[] x509CertificateArr, CipherSuite.KeyExchange keyExchange) {
            super(handshakeContext);
            this.authorities = new ArrayList(x509CertificateArr.length);
            for (X509Certificate x509Certificate : x509CertificateArr) {
                this.authorities.add(x509Certificate.getSubjectX500Principal().getEncoded());
            }
            this.types = ClientCertificateType.CERT_TYPES;
        }

        T10CertificateRequestMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.remaining() < 4) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Incorrect CertificateRequest message: no sufficient data");
            }
            this.types = Record.getBytes8(byteBuffer);
            int int16 = Record.getInt16(byteBuffer);
            if (int16 > byteBuffer.remaining()) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Incorrect CertificateRequest message:no sufficient data");
            }
            if (int16 > 0) {
                this.authorities = new LinkedList();
                while (int16 > 0) {
                    byte[] bytes16 = Record.getBytes16(byteBuffer);
                    int16 -= 2 + bytes16.length;
                    this.authorities.add(bytes16);
                }
                return;
            }
            this.authorities = Collections.emptyList();
        }

        String[] getKeyTypes() {
            return ClientCertificateType.getKeyTypes(this.types);
        }

        X500Principal[] getAuthorities() {
            X500Principal[] x500PrincipalArr = new X500Principal[this.authorities.size()];
            int i2 = 0;
            Iterator<byte[]> it = this.authorities.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                x500PrincipalArr[i3] = new X500Principal(it.next());
            }
            return x500PrincipalArr;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE_REQUEST;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            int length = 1 + this.types.length + 2;
            Iterator<byte[]> it = this.authorities.iterator();
            while (it.hasNext()) {
                length += it.next().length + 2;
            }
            return length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putBytes8(this.types);
            int length = 0;
            Iterator<byte[]> it = this.authorities.iterator();
            while (it.hasNext()) {
                length += it.next().length + 2;
            }
            handshakeOutStream.putInt16(length);
            Iterator<byte[]> it2 = this.authorities.iterator();
            while (it2.hasNext()) {
                handshakeOutStream.putBytes16(it2.next());
            }
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"CertificateRequest\": '{'\n  \"certificate types\": {0}\n  \"certificate authorities\": {1}\n'}'", Locale.ENGLISH);
            ArrayList arrayList = new ArrayList(this.types.length);
            for (byte b2 : this.types) {
                arrayList.add(ClientCertificateType.nameOf(b2));
            }
            ArrayList arrayList2 = new ArrayList(this.authorities.size());
            Iterator<byte[]> it = this.authorities.iterator();
            while (it.hasNext()) {
                arrayList2.add(new X500Principal(it.next()).toString());
            }
            return messageFormat.format(new Object[]{arrayList, arrayList2});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$T10CertificateRequestProducer.class */
    private static final class T10CertificateRequestProducer implements HandshakeProducer {
        private T10CertificateRequestProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            T10CertificateRequestMessage t10CertificateRequestMessage = new T10CertificateRequestMessage(serverHandshakeContext, serverHandshakeContext.sslContext.getX509TrustManager().getAcceptedIssuers(), serverHandshakeContext.negotiatedCipherSuite.keyExchange);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced CertificateRequest handshake message", t10CertificateRequestMessage);
            }
            t10CertificateRequestMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE);
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id), SSLHandshake.CERTIFICATE_VERIFY);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$T10CertificateRequestConsumer.class */
    private static final class T10CertificateRequestConsumer implements SSLConsumer {
        private T10CertificateRequestConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_REQUEST.id));
            clientHandshakeContext.receivedCertReq = true;
            if (clientHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CERTIFICATE.id))) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected CertificateRequest handshake message");
            }
            if (clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_STATUS.id)) != null) {
                CertificateStatus.handshakeAbsence.absent(connectionContext, null);
            }
            T10CertificateRequestMessage t10CertificateRequestMessage = new T10CertificateRequestMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming CertificateRequest handshake message", t10CertificateRequestMessage);
            }
            clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE);
            X509ExtendedKeyManager x509KeyManager = clientHandshakeContext.sslContext.getX509KeyManager();
            String strChooseEngineClientAlias = null;
            if (clientHandshakeContext.conContext.transport instanceof SSLSocketImpl) {
                strChooseEngineClientAlias = x509KeyManager.chooseClientAlias(t10CertificateRequestMessage.getKeyTypes(), t10CertificateRequestMessage.getAuthorities(), (SSLSocket) clientHandshakeContext.conContext.transport);
            } else if (clientHandshakeContext.conContext.transport instanceof SSLEngineImpl) {
                strChooseEngineClientAlias = x509KeyManager.chooseEngineClientAlias(t10CertificateRequestMessage.getKeyTypes(), t10CertificateRequestMessage.getAuthorities(), (SSLEngine) clientHandshakeContext.conContext.transport);
            }
            if (strChooseEngineClientAlias == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("No available client authentication", new Object[0]);
                    return;
                }
                return;
            }
            PrivateKey privateKey = x509KeyManager.getPrivateKey(strChooseEngineClientAlias);
            if (privateKey == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("No available client private key", new Object[0]);
                    return;
                }
                return;
            }
            X509Certificate[] certificateChain = x509KeyManager.getCertificateChain(strChooseEngineClientAlias);
            if (certificateChain == null || certificateChain.length == 0) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("No available client certificate", new Object[0]);
                    return;
                }
                return;
            }
            clientHandshakeContext.handshakePossessions.add(new X509Authentication.X509Possession(privateKey, certificateChain));
            clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id), SSLHandshake.CERTIFICATE_VERIFY);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$T12CertificateRequestMessage.class */
    static final class T12CertificateRequestMessage extends SSLHandshake.HandshakeMessage {
        final byte[] types;
        final int[] algorithmIds;
        final List<byte[]> authorities;

        T12CertificateRequestMessage(HandshakeContext handshakeContext, X509Certificate[] x509CertificateArr, CipherSuite.KeyExchange keyExchange, List<SignatureScheme> list) throws IOException {
            super(handshakeContext);
            this.types = ClientCertificateType.CERT_TYPES;
            if (list == null || list.isEmpty()) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No signature algorithms specified for CertificateRequest hanshake message");
            }
            this.algorithmIds = new int[list.size()];
            int i2 = 0;
            Iterator<SignatureScheme> it = list.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                this.algorithmIds[i3] = it.next().id;
            }
            this.authorities = new ArrayList(x509CertificateArr.length);
            for (X509Certificate x509Certificate : x509CertificateArr) {
                this.authorities.add(x509Certificate.getSubjectX500Principal().getEncoded());
            }
        }

        T12CertificateRequestMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.remaining() < 8) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateRequest handshake message: no sufficient data");
            }
            this.types = Record.getBytes8(byteBuffer);
            if (byteBuffer.remaining() < 6) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateRequest handshake message: no sufficient data");
            }
            byte[] bytes16 = Record.getBytes16(byteBuffer);
            if (bytes16 == null || bytes16.length == 0 || (bytes16.length & 1) != 0) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateRequest handshake message: incomplete signature algorithms");
            }
            this.algorithmIds = new int[bytes16.length >> 1];
            int i2 = 0;
            int i3 = 0;
            while (i2 < bytes16.length) {
                int i4 = i2;
                int i5 = i2 + 1;
                i2 = i5 + 1;
                int i6 = i3;
                i3++;
                this.algorithmIds[i6] = ((bytes16[i4] & 255) << 8) | (bytes16[i5] & 255);
            }
            if (byteBuffer.remaining() < 2) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateRequest handshake message: no sufficient data");
            }
            int int16 = Record.getInt16(byteBuffer);
            if (int16 > byteBuffer.remaining()) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateRequest message: no sufficient data");
            }
            if (int16 > 0) {
                this.authorities = new LinkedList();
                while (int16 > 0) {
                    byte[] bytes162 = Record.getBytes16(byteBuffer);
                    int16 -= 2 + bytes162.length;
                    this.authorities.add(bytes162);
                }
                return;
            }
            this.authorities = Collections.emptyList();
        }

        String[] getKeyTypes() {
            return ClientCertificateType.getKeyTypes(this.types);
        }

        X500Principal[] getAuthorities() {
            X500Principal[] x500PrincipalArr = new X500Principal[this.authorities.size()];
            int i2 = 0;
            Iterator<byte[]> it = this.authorities.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                x500PrincipalArr[i3] = new X500Principal(it.next());
            }
            return x500PrincipalArr;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE_REQUEST;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            int length = 1 + this.types.length + 2 + (this.algorithmIds.length << 1) + 2;
            Iterator<byte[]> it = this.authorities.iterator();
            while (it.hasNext()) {
                length += it.next().length + 2;
            }
            return length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putBytes8(this.types);
            int length = 0;
            Iterator<byte[]> it = this.authorities.iterator();
            while (it.hasNext()) {
                length += it.next().length + 2;
            }
            handshakeOutStream.putInt16(this.algorithmIds.length << 1);
            for (int i2 : this.algorithmIds) {
                handshakeOutStream.putInt16(i2);
            }
            handshakeOutStream.putInt16(length);
            Iterator<byte[]> it2 = this.authorities.iterator();
            while (it2.hasNext()) {
                handshakeOutStream.putBytes16(it2.next());
            }
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"CertificateRequest\": '{'\n  \"certificate types\": {0}\n  \"supported signature algorithms\": {1}\n  \"certificate authorities\": {2}\n'}'", Locale.ENGLISH);
            ArrayList arrayList = new ArrayList(this.types.length);
            for (byte b2 : this.types) {
                arrayList.add(ClientCertificateType.nameOf(b2));
            }
            ArrayList arrayList2 = new ArrayList(this.algorithmIds.length);
            for (int i2 : this.algorithmIds) {
                arrayList2.add(SignatureScheme.nameOf(i2));
            }
            ArrayList arrayList3 = new ArrayList(this.authorities.size());
            Iterator<byte[]> it = this.authorities.iterator();
            while (it.hasNext()) {
                arrayList3.add(new X500Principal(it.next()).toString());
            }
            return messageFormat.format(new Object[]{arrayList, arrayList2, arrayList3});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$T12CertificateRequestProducer.class */
    private static final class T12CertificateRequestProducer implements HandshakeProducer {
        private T12CertificateRequestProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.localSupportedSignAlgs == null) {
                serverHandshakeContext.localSupportedSignAlgs = SignatureScheme.getSupportedAlgorithms(serverHandshakeContext.sslConfig, serverHandshakeContext.algorithmConstraints, serverHandshakeContext.activeProtocols);
            }
            if (serverHandshakeContext.localSupportedSignAlgs == null || serverHandshakeContext.localSupportedSignAlgs.isEmpty()) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No supported signature algorithm");
            }
            T12CertificateRequestMessage t12CertificateRequestMessage = new T12CertificateRequestMessage(serverHandshakeContext, serverHandshakeContext.sslContext.getX509TrustManager().getAcceptedIssuers(), serverHandshakeContext.negotiatedCipherSuite.keyExchange, serverHandshakeContext.localSupportedSignAlgs);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced CertificateRequest handshake message", t12CertificateRequestMessage);
            }
            t12CertificateRequestMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE);
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id), SSLHandshake.CERTIFICATE_VERIFY);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$T12CertificateRequestConsumer.class */
    private static final class T12CertificateRequestConsumer implements SSLConsumer {
        private T12CertificateRequestConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_REQUEST.id));
            clientHandshakeContext.receivedCertReq = true;
            if (clientHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CERTIFICATE.id))) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected CertificateRequest handshake message");
            }
            if (clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_STATUS.id)) != null) {
                CertificateStatus.handshakeAbsence.absent(connectionContext, null);
            }
            T12CertificateRequestMessage t12CertificateRequestMessage = new T12CertificateRequestMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming CertificateRequest handshake message", t12CertificateRequestMessage);
            }
            clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE);
            LinkedList linkedList = new LinkedList();
            for (int i2 : t12CertificateRequestMessage.algorithmIds) {
                SignatureScheme signatureSchemeValueOf = SignatureScheme.valueOf(i2);
                if (signatureSchemeValueOf != null) {
                    linkedList.add(signatureSchemeValueOf);
                }
            }
            clientHandshakeContext.peerRequestedSignatureSchemes = linkedList;
            clientHandshakeContext.peerRequestedCertSignSchemes = linkedList;
            clientHandshakeContext.handshakeSession.setPeerSupportedSignatureAlgorithms(linkedList);
            clientHandshakeContext.peerSupportedAuthorities = t12CertificateRequestMessage.getAuthorities();
            SSLPossession sSLPossessionChoosePossession = choosePossession(clientHandshakeContext, t12CertificateRequestMessage);
            if (sSLPossessionChoosePossession == null) {
                return;
            }
            clientHandshakeContext.handshakePossessions.add(sSLPossessionChoosePossession);
            clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id), SSLHandshake.CERTIFICATE_VERIFY);
        }

        private static SSLPossession choosePossession(HandshakeContext handshakeContext, T12CertificateRequestMessage t12CertificateRequestMessage) throws IOException {
            if (handshakeContext.peerRequestedCertSignSchemes == null || handshakeContext.peerRequestedCertSignSchemes.isEmpty()) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("No signature and hash algorithms in CertificateRequest", new Object[0]);
                    return null;
                }
                return null;
            }
            List listAsList = Arrays.asList(t12CertificateRequestMessage.getKeyTypes());
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
                    } else if ((x509AuthenticationValueOf.possessionGenerator instanceof X509Authentication.X509PossessionGenerator) && Collections.disjoint(listAsList, Arrays.asList(((X509Authentication.X509PossessionGenerator) x509AuthenticationValueOf.possessionGenerator).keyTypes))) {
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
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$T13CertificateRequestMessage.class */
    static final class T13CertificateRequestMessage extends SSLHandshake.HandshakeMessage {
        private final byte[] requestContext;
        private final SSLExtensions extensions;

        T13CertificateRequestMessage(HandshakeContext handshakeContext) throws IOException {
            super(handshakeContext);
            this.requestContext = new byte[0];
            this.extensions = new SSLExtensions(this);
        }

        T13CertificateRequestMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.remaining() < 5) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateRequest handshake message: no sufficient data");
            }
            this.requestContext = Record.getBytes8(byteBuffer);
            if (byteBuffer.remaining() < 4) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateRequest handshake message: no sufficient extensions data");
            }
            this.extensions = new SSLExtensions(this, byteBuffer, handshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CERTIFICATE_REQUEST));
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE_REQUEST;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        int messageLength() {
            return 1 + this.requestContext.length + this.extensions.length();
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putBytes8(this.requestContext);
            this.extensions.send(handshakeOutStream);
        }

        public String toString() {
            return new MessageFormat("\"CertificateRequest\": '{'\n  \"certificate_request_context\": \"{0}\",\n  \"extensions\": [\n{1}\n  ]\n'}'", Locale.ENGLISH).format(new Object[]{Utilities.toHexString(this.requestContext), Utilities.indent(Utilities.indent(this.extensions.toString()))});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$T13CertificateRequestProducer.class */
    private static final class T13CertificateRequestProducer implements HandshakeProducer {
        private T13CertificateRequestProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            T13CertificateRequestMessage t13CertificateRequestMessage = new T13CertificateRequestMessage(serverHandshakeContext);
            t13CertificateRequestMessage.extensions.produce(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CERTIFICATE_REQUEST, serverHandshakeContext.negotiatedProtocol));
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced CertificateRequest message", t13CertificateRequestMessage);
            }
            t13CertificateRequestMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            serverHandshakeContext.certRequestContext = (byte[]) t13CertificateRequestMessage.requestContext.clone();
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE);
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id), SSLHandshake.CERTIFICATE_VERIFY);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateRequest$T13CertificateRequestConsumer.class */
    private static final class T13CertificateRequestConsumer implements SSLConsumer {
        private T13CertificateRequestConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_REQUEST.id));
            clientHandshakeContext.receivedCertReq = true;
            if (clientHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.ENCRYPTED_EXTENSIONS.id))) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected CertificateRequest handshake message");
            }
            T13CertificateRequestMessage t13CertificateRequestMessage = new T13CertificateRequestMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming CertificateRequest handshake message", t13CertificateRequestMessage);
            }
            SSLExtension[] enabledExtensions = clientHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CERTIFICATE_REQUEST);
            t13CertificateRequestMessage.extensions.consumeOnLoad(clientHandshakeContext, enabledExtensions);
            t13CertificateRequestMessage.extensions.consumeOnTrade(clientHandshakeContext, enabledExtensions);
            clientHandshakeContext.certRequestContext = (byte[]) t13CertificateRequestMessage.requestContext.clone();
            clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE);
            clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id), SSLHandshake.CERTIFICATE_VERIFY);
        }
    }
}
