package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.ClientHello;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;
import sun.util.locale.LanguageTag;

/* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension.class */
final class PreSharedKeyExtension {
    static final HandshakeProducer chNetworkProducer = new CHPreSharedKeyProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHPreSharedKeyConsumer();
    static final HandshakeAbsence chOnLoadAbsence = new CHPreSharedKeyAbsence();
    static final HandshakeConsumer chOnTradeConsumer = new CHPreSharedKeyUpdate();
    static final SSLStringizer chStringizer = new CHPreSharedKeyStringizer();
    static final HandshakeProducer shNetworkProducer = new SHPreSharedKeyProducer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHPreSharedKeyConsumer();
    static final HandshakeAbsence shOnLoadAbsence = new SHPreSharedKeyAbsence();
    static final SSLStringizer shStringizer = new SHPreSharedKeyStringizer();

    PreSharedKeyExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$PskIdentity.class */
    private static final class PskIdentity {
        final byte[] identity;
        final int obfuscatedAge;

        PskIdentity(byte[] bArr, int i2) {
            this.identity = bArr;
            this.obfuscatedAge = i2;
        }

        int getEncodedLength() {
            return 2 + this.identity.length + 4;
        }

        void writeEncoded(ByteBuffer byteBuffer) throws IOException {
            Record.putBytes16(byteBuffer, this.identity);
            Record.putInt32(byteBuffer, this.obfuscatedAge);
        }

        public String toString() {
            return VectorFormat.DEFAULT_PREFIX + Utilities.toHexString(this.identity) + "," + this.obfuscatedAge + "}";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$CHPreSharedKeySpec.class */
    private static final class CHPreSharedKeySpec implements SSLExtension.SSLExtensionSpec {
        final List<PskIdentity> identities;
        final List<byte[]> binders;

        CHPreSharedKeySpec(List<PskIdentity> list, List<byte[]> list2) {
            this.identities = list;
            this.binders = list2;
        }

        CHPreSharedKeySpec(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 44) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid pre_shared_key extension: insufficient data (length=" + byteBuffer.remaining() + ")");
            }
            int int16 = Record.getInt16(byteBuffer);
            if (int16 < 7) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid pre_shared_key extension: insufficient identities (length=" + int16 + ")");
            }
            this.identities = new ArrayList();
            int encodedLength = 0;
            while (true) {
                int i2 = encodedLength;
                if (i2 < int16) {
                    byte[] bytes16 = Record.getBytes16(byteBuffer);
                    if (bytes16.length < 1) {
                        throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid pre_shared_key extension: insufficient identity (length=" + bytes16.length + ")");
                    }
                    PskIdentity pskIdentity = new PskIdentity(bytes16, Record.getInt32(byteBuffer));
                    this.identities.add(pskIdentity);
                    encodedLength = i2 + pskIdentity.getEncodedLength();
                } else {
                    if (byteBuffer.remaining() < 35) {
                        throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid pre_shared_key extension: insufficient binders data (length=" + byteBuffer.remaining() + ")");
                    }
                    int int162 = Record.getInt16(byteBuffer);
                    if (int162 < 33) {
                        throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid pre_shared_key extension: insufficient binders (length=" + int162 + ")");
                    }
                    this.binders = new ArrayList();
                    int length = 0;
                    while (true) {
                        int i3 = length;
                        if (i3 < int162) {
                            byte[] bytes8 = Record.getBytes8(byteBuffer);
                            if (bytes8.length < 32) {
                                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid pre_shared_key extension: insufficient binder entry (length=" + bytes8.length + ")");
                            }
                            this.binders.add(bytes8);
                            length = i3 + 1 + bytes8.length;
                        } else {
                            return;
                        }
                    }
                }
            }
        }

        int getIdsEncodedLength() {
            int encodedLength = 0;
            Iterator<PskIdentity> it = this.identities.iterator();
            while (it.hasNext()) {
                encodedLength += it.next().getEncodedLength();
            }
            return encodedLength;
        }

        int getBindersEncodedLength() {
            int length = 0;
            Iterator<byte[]> it = this.binders.iterator();
            while (it.hasNext()) {
                length += 1 + it.next().length;
            }
            return length;
        }

        byte[] getEncoded() throws IOException {
            int idsEncodedLength = getIdsEncodedLength();
            int bindersEncodedLength = getBindersEncodedLength();
            byte[] bArr = new byte[4 + idsEncodedLength + bindersEncodedLength];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            Record.putInt16(byteBufferWrap, idsEncodedLength);
            Iterator<PskIdentity> it = this.identities.iterator();
            while (it.hasNext()) {
                it.next().writeEncoded(byteBufferWrap);
            }
            Record.putInt16(byteBufferWrap, bindersEncodedLength);
            Iterator<byte[]> it2 = this.binders.iterator();
            while (it2.hasNext()) {
                Record.putBytes8(byteBufferWrap, it2.next());
            }
            return bArr;
        }

        public String toString() {
            return new MessageFormat("\"PreSharedKey\": '{'\n  \"identities\"    : \"{0}\",\n  \"binders\"       : \"{1}\",\n'}'", Locale.ENGLISH).format(new Object[]{Utilities.indent(identitiesString()), Utilities.indent(bindersString())});
        }

        String identitiesString() {
            StringBuilder sb = new StringBuilder();
            Iterator<PskIdentity> it = this.identities.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString() + "\n");
            }
            return sb.toString();
        }

        String bindersString() {
            StringBuilder sb = new StringBuilder();
            Iterator<byte[]> it = this.binders.iterator();
            while (it.hasNext()) {
                sb.append(VectorFormat.DEFAULT_PREFIX + Utilities.toHexString(it.next()) + "}\n");
            }
            return sb.toString();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$CHPreSharedKeyStringizer.class */
    private static final class CHPreSharedKeyStringizer implements SSLStringizer {
        private CHPreSharedKeyStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new CHPreSharedKeySpec((HandshakeContext) null, byteBuffer).toString();
            } catch (Exception e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$SHPreSharedKeySpec.class */
    private static final class SHPreSharedKeySpec implements SSLExtension.SSLExtensionSpec {
        final int selectedIdentity;

        SHPreSharedKeySpec(int i2) {
            this.selectedIdentity = i2;
        }

        SHPreSharedKeySpec(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 2) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid pre_shared_key extension: insufficient selected_identity (length=" + byteBuffer.remaining() + ")");
            }
            this.selectedIdentity = Record.getInt16(byteBuffer);
        }

        byte[] getEncoded() throws IOException {
            return new byte[]{(byte) ((this.selectedIdentity >> 8) & 255), (byte) (this.selectedIdentity & 255)};
        }

        public String toString() {
            return new MessageFormat("\"PreSharedKey\": '{'\n  \"selected_identity\"      : \"{0}\",\n'}'", Locale.ENGLISH).format(new Object[]{Utilities.byte16HexString(this.selectedIdentity)});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$SHPreSharedKeyStringizer.class */
    private static final class SHPreSharedKeyStringizer implements SSLStringizer {
        private SHPreSharedKeyStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new SHPreSharedKeySpec(null, byteBuffer).toString();
            } catch (Exception e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$CHPreSharedKeyConsumer.class */
    private static final class CHPreSharedKeyConsumer implements SSLExtension.ExtensionConsumer {
        private CHPreSharedKeyConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHello.ClientHelloMessage clientHelloMessage = (ClientHello.ClientHelloMessage) handshakeMessage;
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_PRE_SHARED_KEY)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable pre_shared_key extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                CHPreSharedKeySpec cHPreSharedKeySpec = new CHPreSharedKeySpec(serverHandshakeContext, byteBuffer);
                if (!serverHandshakeContext.handshakeExtensions.containsKey(SSLExtension.PSK_KEY_EXCHANGE_MODES)) {
                    throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Client sent PSK but not PSK modes, or the PSK extension is not the last extension");
                }
                if (cHPreSharedKeySpec.identities.size() != cHPreSharedKeySpec.binders.size()) {
                    throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "PSK extension has incorrect number of binders");
                }
                if (serverHandshakeContext.isResumption) {
                    SSLSessionContextImpl sSLSessionContextImpl = (SSLSessionContextImpl) serverHandshakeContext.sslContext.engineGetServerSessionContext();
                    int i2 = 0;
                    Iterator<PskIdentity> it = cHPreSharedKeySpec.identities.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        SSLSessionImpl sSLSessionImplPull = sSLSessionContextImpl.pull(it.next().identity);
                        if (sSLSessionImplPull != null && PreSharedKeyExtension.canRejoin(clientHelloMessage, serverHandshakeContext, sSLSessionImplPull)) {
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                                SSLLogger.fine("Resuming session: ", sSLSessionImplPull);
                            }
                            serverHandshakeContext.resumingSession = sSLSessionImplPull;
                            serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_PRE_SHARED_KEY, new SHPreSharedKeySpec(i2));
                        } else {
                            i2++;
                        }
                    }
                    if (i2 == cHPreSharedKeySpec.identities.size()) {
                        serverHandshakeContext.isResumption = false;
                        serverHandshakeContext.resumingSession = null;
                    }
                }
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_PRE_SHARED_KEY, cHPreSharedKeySpec);
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean canRejoin(ClientHello.ClientHelloMessage clientHelloMessage, ServerHandshakeContext serverHandshakeContext, SSLSessionImpl sSLSessionImpl) {
        boolean z2 = sSLSessionImpl.isRejoinable() && sSLSessionImpl.getPreSharedKey() != null;
        if (z2 && sSLSessionImpl.getProtocolVersion() != serverHandshakeContext.negotiatedProtocol) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                SSLLogger.finest("Can't resume, incorrect protocol version", new Object[0]);
            }
            z2 = false;
        }
        if (serverHandshakeContext.localSupportedSignAlgs == null) {
            serverHandshakeContext.localSupportedSignAlgs = SignatureScheme.getSupportedAlgorithms(serverHandshakeContext.sslConfig, serverHandshakeContext.algorithmConstraints, serverHandshakeContext.activeProtocols);
        }
        if (z2 && serverHandshakeContext.sslConfig.clientAuthType == ClientAuthType.CLIENT_AUTH_REQUIRED) {
            try {
                sSLSessionImpl.getPeerPrincipal();
            } catch (SSLPeerUnverifiedException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                    SSLLogger.finest("Can't resume, client authentication is required", new Object[0]);
                }
                z2 = false;
            }
            Collection<SignatureScheme> localSupportedSignatureSchemes = sSLSessionImpl.getLocalSupportedSignatureSchemes();
            if (z2 && !serverHandshakeContext.localSupportedSignAlgs.containsAll(localSupportedSignatureSchemes)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Can't resume. Session uses different signature algorithms", new Object[0]);
                }
                z2 = false;
            }
        }
        String str = serverHandshakeContext.sslConfig.identificationProtocol;
        if (z2 && str != null) {
            String identificationProtocol = sSLSessionImpl.getIdentificationProtocol();
            if (!str.equalsIgnoreCase(identificationProtocol)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                    SSLLogger.finest("Can't resume, endpoint id algorithm does not match, requested: " + str + ", cached: " + identificationProtocol, new Object[0]);
                }
                z2 = false;
            }
        }
        if (z2 && (!serverHandshakeContext.isNegotiable(sSLSessionImpl.getSuite()) || !clientHelloMessage.cipherSuites.contains(sSLSessionImpl.getSuite()))) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                SSLLogger.finest("Can't resume, unavailable session cipher suite", new Object[0]);
            }
            z2 = false;
        }
        return z2;
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$CHPreSharedKeyUpdate.class */
    private static final class CHPreSharedKeyUpdate implements HandshakeConsumer {
        private CHPreSharedKeyUpdate() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.isResumption || serverHandshakeContext.resumingSession == null) {
                return;
            }
            CHPreSharedKeySpec cHPreSharedKeySpec = (CHPreSharedKeySpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_PRE_SHARED_KEY);
            SHPreSharedKeySpec sHPreSharedKeySpec = (SHPreSharedKeySpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.SH_PRE_SHARED_KEY);
            if (cHPreSharedKeySpec == null || sHPreSharedKeySpec == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Required extensions are unavailable");
            }
            byte[] bArr = cHPreSharedKeySpec.binders.get(sHPreSharedKeySpec.selectedIdentity);
            HandshakeHash handshakeHashCopy = serverHandshakeContext.handshakeHash.copy();
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(handshakeHashCopy.removeLastReceived());
            byteBufferWrap.position(4);
            ClientHello.ClientHelloMessage.readPartial(serverHandshakeContext.conContext, byteBufferWrap);
            int iPosition = byteBufferWrap.position();
            byteBufferWrap.position(0);
            handshakeHashCopy.receive(byteBufferWrap, iPosition);
            PreSharedKeyExtension.checkBinder(serverHandshakeContext, serverHandshakeContext.resumingSession, handshakeHashCopy, bArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkBinder(ServerHandshakeContext serverHandshakeContext, SSLSessionImpl sSLSessionImpl, HandshakeHash handshakeHash, byte[] bArr) throws IOException {
        SecretKey preSharedKey = sSLSessionImpl.getPreSharedKey();
        if (preSharedKey == null) {
            throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Session has no PSK");
        }
        if (!MessageDigest.isEqual(bArr, computeBinder(serverHandshakeContext, deriveBinderKey(serverHandshakeContext, preSharedKey, sSLSessionImpl), sSLSessionImpl, handshakeHash))) {
            throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Incorect PSK binder value");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$PartialClientHelloMessage.class */
    static final class PartialClientHelloMessage extends SSLHandshake.HandshakeMessage {
        private final ClientHello.ClientHelloMessage msg;
        private final CHPreSharedKeySpec psk;

        PartialClientHelloMessage(HandshakeContext handshakeContext, ClientHello.ClientHelloMessage clientHelloMessage, CHPreSharedKeySpec cHPreSharedKeySpec) {
            super(handshakeContext);
            this.msg = clientHelloMessage;
            this.psk = cHPreSharedKeySpec;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        SSLHandshake handshakeType() {
            return this.msg.handshakeType();
        }

        private int pskTotalLength() {
            return this.psk.getIdsEncodedLength() + this.psk.getBindersEncodedLength() + 8;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        int messageLength() {
            if (this.msg.extensions.get(SSLExtension.CH_PRE_SHARED_KEY) != null) {
                return this.msg.messageLength();
            }
            return this.msg.messageLength() + pskTotalLength();
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        void send(HandshakeOutStream handshakeOutStream) throws IOException {
            this.msg.sendCore(handshakeOutStream);
            int length = this.msg.extensions.length();
            if (this.msg.extensions.get(SSLExtension.CH_PRE_SHARED_KEY) == null) {
                length += pskTotalLength();
            }
            handshakeOutStream.putInt16(length - 2);
            for (SSLExtension sSLExtension : SSLExtension.values()) {
                byte[] bArr = this.msg.extensions.get(sSLExtension);
                if (bArr != null && sSLExtension != SSLExtension.CH_PRE_SHARED_KEY) {
                    handshakeOutStream.putInt16(sSLExtension.id);
                    handshakeOutStream.putBytes16(bArr);
                }
            }
            handshakeOutStream.putInt16(SSLExtension.CH_PRE_SHARED_KEY.id);
            byte[] encoded = this.psk.getEncoded();
            handshakeOutStream.putInt16(encoded.length);
            handshakeOutStream.write(encoded, 0, this.psk.getIdsEncodedLength() + 2);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$CHPreSharedKeyProducer.class */
    private static final class CHPreSharedKeyProducer implements HandshakeProducer {
        private CHPreSharedKeyProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.isResumption || clientHandshakeContext.resumingSession == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("No session to resume.", new Object[0]);
                    return null;
                }
                return null;
            }
            if (!clientHandshakeContext.localSupportedSignAlgs.containsAll(clientHandshakeContext.resumingSession.getLocalSupportedSignatureSchemes())) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Existing session uses different signature algorithms", new Object[0]);
                    return null;
                }
                return null;
            }
            SecretKey preSharedKey = clientHandshakeContext.resumingSession.getPreSharedKey();
            if (preSharedKey == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Existing session has no PSK.", new Object[0]);
                    return null;
                }
                return null;
            }
            if (clientHandshakeContext.pskIdentity == null) {
                clientHandshakeContext.pskIdentity = clientHandshakeContext.resumingSession.consumePskIdentity();
            }
            if (clientHandshakeContext.pskIdentity == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("PSK has no identity, or identity was already used", new Object[0]);
                    return null;
                }
                return null;
            }
            ((SSLSessionContextImpl) clientHandshakeContext.sslContext.engineGetClientSessionContext()).remove(clientHandshakeContext.resumingSession.getSessionId());
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Found resumable session. Preparing PSK message.", new Object[0]);
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(new PskIdentity(clientHandshakeContext.pskIdentity, ((int) (System.currentTimeMillis() - clientHandshakeContext.resumingSession.getTicketCreationTime())) + clientHandshakeContext.resumingSession.getTicketAgeAdd()));
            CHPreSharedKeySpec cHPreSharedKeySpecCreatePskPrototype = createPskPrototype(clientHandshakeContext.resumingSession.getSuite().hashAlg.hashLength, arrayList);
            byte[] bArrComputeBinder = PreSharedKeyExtension.computeBinder(clientHandshakeContext, PreSharedKeyExtension.deriveBinderKey(clientHandshakeContext, preSharedKey, clientHandshakeContext.resumingSession), clientHandshakeContext.handshakeHash.copy(), clientHandshakeContext.resumingSession, clientHandshakeContext, (ClientHello.ClientHelloMessage) handshakeMessage, cHPreSharedKeySpecCreatePskPrototype);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(bArrComputeBinder);
            CHPreSharedKeySpec cHPreSharedKeySpec = new CHPreSharedKeySpec(arrayList, arrayList2);
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_PRE_SHARED_KEY, cHPreSharedKeySpec);
            return cHPreSharedKeySpec.getEncoded();
        }

        private CHPreSharedKeySpec createPskPrototype(int i2, List<PskIdentity> list) {
            ArrayList arrayList = new ArrayList();
            byte[] bArr = new byte[i2];
            for (PskIdentity pskIdentity : list) {
                arrayList.add(bArr);
            }
            return new CHPreSharedKeySpec(list, arrayList);
        }
    }

    private static byte[] computeBinder(HandshakeContext handshakeContext, SecretKey secretKey, SSLSessionImpl sSLSessionImpl, HandshakeHash handshakeHash) throws IOException {
        handshakeHash.determine(sSLSessionImpl.getProtocolVersion(), sSLSessionImpl.getSuite());
        handshakeHash.update();
        return computeBinder(handshakeContext, secretKey, sSLSessionImpl, handshakeHash.digest());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] computeBinder(HandshakeContext handshakeContext, SecretKey secretKey, HandshakeHash handshakeHash, SSLSessionImpl sSLSessionImpl, HandshakeContext handshakeContext2, ClientHello.ClientHelloMessage clientHelloMessage, CHPreSharedKeySpec cHPreSharedKeySpec) throws IOException {
        new PartialClientHelloMessage(handshakeContext2, clientHelloMessage, cHPreSharedKeySpec).write(new HandshakeOutStream(new SSLEngineOutputRecord(handshakeHash)));
        handshakeHash.determine(sSLSessionImpl.getProtocolVersion(), sSLSessionImpl.getSuite());
        handshakeHash.update();
        return computeBinder(handshakeContext, secretKey, sSLSessionImpl, handshakeHash.digest());
    }

    private static byte[] computeBinder(HandshakeContext handshakeContext, SecretKey secretKey, SSLSessionImpl sSLSessionImpl, byte[] bArr) throws IllegalStateException, IOException {
        try {
            CipherSuite.HashAlg hashAlg = sSLSessionImpl.getSuite().hashAlg;
            SecretKey secretKeyExpand = new HKDF(hashAlg.name).expand(secretKey, SSLSecretDerivation.createHkdfInfo("tls13 finished".getBytes(), new byte[0], hashAlg.hashLength), hashAlg.hashLength, "TlsBinderKey");
            try {
                Mac mac = JsseJce.getMac("Hmac" + hashAlg.name.replace(LanguageTag.SEP, ""));
                mac.init(secretKeyExpand);
                return mac.doFinal(bArr);
            } catch (InvalidKeyException | NoSuchAlgorithmException e2) {
                throw handshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, e2);
            }
        } catch (GeneralSecurityException e3) {
            throw handshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, e3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SecretKey deriveBinderKey(HandshakeContext handshakeContext, SecretKey secretKey, SSLSessionImpl sSLSessionImpl) throws IOException {
        try {
            CipherSuite.HashAlg hashAlg = sSLSessionImpl.getSuite().hashAlg;
            HKDF hkdf = new HKDF(hashAlg.name);
            return hkdf.expand(hkdf.extract(new byte[hashAlg.hashLength], secretKey, "TlsEarlySecret"), SSLSecretDerivation.createHkdfInfo("tls13 res binder".getBytes(), MessageDigest.getInstance(hashAlg.name).digest(new byte[0]), hashAlg.hashLength), hashAlg.hashLength, "TlsBinderKey");
        } catch (GeneralSecurityException e2) {
            throw handshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, e2);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$CHPreSharedKeyAbsence.class */
    private static final class CHPreSharedKeyAbsence implements HandshakeAbsence {
        private CHPreSharedKeyAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Handling pre_shared_key absence.", new Object[0]);
            }
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            serverHandshakeContext.resumingSession = null;
            serverHandshakeContext.isResumption = false;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$SHPreSharedKeyConsumer.class */
    private static final class SHPreSharedKeyConsumer implements SSLExtension.ExtensionConsumer {
        private SHPreSharedKeyConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.handshakeExtensions.containsKey(SSLExtension.CH_PRE_SHARED_KEY)) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Server sent unexpected pre_shared_key extension");
            }
            SHPreSharedKeySpec sHPreSharedKeySpec = new SHPreSharedKeySpec(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Received pre_shared_key extension: ", sHPreSharedKeySpec);
            }
            if (sHPreSharedKeySpec.selectedIdentity != 0) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Selected identity index is not in correct range.");
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Resuming session: ", clientHandshakeContext.resumingSession);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$SHPreSharedKeyAbsence.class */
    private static final class SHPreSharedKeyAbsence implements HandshakeAbsence {
        private SHPreSharedKeyAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Handling pre_shared_key absence.", new Object[0]);
            }
            clientHandshakeContext.resumingSession = null;
            clientHandshakeContext.isResumption = false;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PreSharedKeyExtension$SHPreSharedKeyProducer.class */
    private static final class SHPreSharedKeyProducer implements HandshakeProducer {
        private SHPreSharedKeyProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            SHPreSharedKeySpec sHPreSharedKeySpec = (SHPreSharedKeySpec) ((ServerHandshakeContext) connectionContext).handshakeExtensions.get(SSLExtension.SH_PRE_SHARED_KEY);
            if (sHPreSharedKeySpec == null) {
                return null;
            }
            return sHPreSharedKeySpec.getEncoded();
        }
    }
}
