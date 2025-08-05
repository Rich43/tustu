package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.CryptoPrimitive;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.SSLProtocolException;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.DHKeyExchange;
import sun.security.ssl.ECDHKeyExchange;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SupportedGroupsExtension;

/* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension.class */
final class KeyShareExtension {
    static final HandshakeProducer chNetworkProducer = new CHKeyShareProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHKeyShareConsumer();
    static final SSLStringizer chStringizer = new CHKeyShareStringizer();
    static final HandshakeProducer shNetworkProducer = new SHKeyShareProducer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHKeyShareConsumer();
    static final HandshakeAbsence shOnLoadAbsence = new SHKeyShareAbsence();
    static final SSLStringizer shStringizer = new SHKeyShareStringizer();
    static final HandshakeProducer hrrNetworkProducer = new HRRKeyShareProducer();
    static final SSLExtension.ExtensionConsumer hrrOnLoadConsumer = new HRRKeyShareConsumer();
    static final HandshakeProducer hrrNetworkReproducer = new HRRKeyShareReproducer();
    static final SSLStringizer hrrStringizer = new HRRKeyShareStringizer();

    KeyShareExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$KeyShareEntry.class */
    private static final class KeyShareEntry {
        final int namedGroupId;
        final byte[] keyExchange;

        private KeyShareEntry(int i2, byte[] bArr) {
            this.namedGroupId = i2;
            this.keyExchange = bArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public byte[] getEncoded() {
            byte[] bArr = new byte[this.keyExchange.length + 4];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            try {
                Record.putInt16(byteBufferWrap, this.namedGroupId);
                Record.putBytes16(byteBufferWrap, this.keyExchange);
            } catch (IOException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Unlikely IOException", e2);
                }
            }
            return bArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getEncodedSize() {
            return this.keyExchange.length + 4;
        }

        public String toString() {
            return new MessageFormat("\n'{'\n  \"named group\": {0}\n  \"key_exchange\": '{'\n{1}\n  '}'\n'}',", Locale.ENGLISH).format(new Object[]{SupportedGroupsExtension.NamedGroup.nameOf(this.namedGroupId), Utilities.indent(new HexDumpEncoder().encode(this.keyExchange), "    ")});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$CHKeyShareSpec.class */
    static final class CHKeyShareSpec implements SSLExtension.SSLExtensionSpec {
        final List<KeyShareEntry> clientShares;

        private CHKeyShareSpec(List<KeyShareEntry> list) {
            this.clientShares = list;
        }

        private CHKeyShareSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 2) {
                throw new SSLProtocolException("Invalid key_share extension: insufficient data (length=" + byteBuffer.remaining() + ")");
            }
            int int16 = Record.getInt16(byteBuffer);
            if (int16 != byteBuffer.remaining()) {
                throw new SSLProtocolException("Invalid key_share extension: incorrect list length (length=" + int16 + ")");
            }
            LinkedList linkedList = new LinkedList();
            while (byteBuffer.hasRemaining()) {
                int int162 = Record.getInt16(byteBuffer);
                byte[] bytes16 = Record.getBytes16(byteBuffer);
                if (bytes16.length == 0) {
                    throw new SSLProtocolException("Invalid key_share extension: empty key_exchange");
                }
                linkedList.add(new KeyShareEntry(int162, bytes16));
            }
            this.clientShares = Collections.unmodifiableList(linkedList);
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"client_shares\": '['{0}\n']'", Locale.ENGLISH);
            StringBuilder sb = new StringBuilder(512);
            Iterator<KeyShareEntry> it = this.clientShares.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString());
            }
            return messageFormat.format(new Object[]{Utilities.indent(sb.toString())});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$CHKeyShareStringizer.class */
    private static final class CHKeyShareStringizer implements SSLStringizer {
        private CHKeyShareStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new CHKeyShareSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$CHKeyShareProducer.class */
    private static final class CHKeyShareProducer implements HandshakeProducer {
        private CHKeyShareProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            List<SupportedGroupsExtension.NamedGroup> listAsList;
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_KEY_SHARE)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable key_share extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (clientHandshakeContext.serverSelectedNamedGroup != null) {
                listAsList = Arrays.asList(clientHandshakeContext.serverSelectedNamedGroup);
            } else {
                listAsList = clientHandshakeContext.clientRequestedNamedGroups;
                if (listAsList == null || listAsList.isEmpty()) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.warning("Ignore key_share extension, no supported groups", new Object[0]);
                        return null;
                    }
                    return null;
                }
            }
            LinkedList linkedList = new LinkedList();
            for (SupportedGroupsExtension.NamedGroup namedGroup : listAsList) {
                SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(namedGroup);
                if (sSLKeyExchangeValueOf == null) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.warning("No key exchange for named group " + namedGroup.name, new Object[0]);
                    }
                } else {
                    for (SSLPossession sSLPossession : sSLKeyExchangeValueOf.createPossessions(clientHandshakeContext)) {
                        clientHandshakeContext.handshakePossessions.add(sSLPossession);
                        if ((sSLPossession instanceof ECDHKeyExchange.ECDHEPossession) || (sSLPossession instanceof DHKeyExchange.DHEPossession)) {
                            linkedList.add(new KeyShareEntry(namedGroup.id, sSLPossession.encode()));
                        }
                    }
                    if (!linkedList.isEmpty()) {
                        break;
                    }
                }
            }
            int encodedSize = 0;
            Iterator<E> it = linkedList.iterator();
            while (it.hasNext()) {
                encodedSize += ((KeyShareEntry) it.next()).getEncodedSize();
            }
            byte[] bArr = new byte[encodedSize + 2];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            Record.putInt16(byteBufferWrap, encodedSize);
            Iterator<E> it2 = linkedList.iterator();
            while (it2.hasNext()) {
                byteBufferWrap.put(((KeyShareEntry) it2.next()).getEncoded());
            }
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_KEY_SHARE, new CHKeyShareSpec(linkedList));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$CHKeyShareConsumer.class */
    private static final class CHKeyShareConsumer implements SSLExtension.ExtensionConsumer {
        private CHKeyShareConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.handshakeExtensions.containsKey(SSLExtension.CH_KEY_SHARE)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("The key_share extension has been loaded", new Object[0]);
                    return;
                }
                return;
            }
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_KEY_SHARE)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable key_share extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                CHKeyShareSpec cHKeyShareSpec = new CHKeyShareSpec(byteBuffer);
                LinkedList linkedList = new LinkedList();
                for (KeyShareEntry keyShareEntry : cHKeyShareSpec.clientShares) {
                    SupportedGroupsExtension.NamedGroup namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(keyShareEntry.namedGroupId);
                    if (namedGroupValueOf == null || !SupportedGroupsExtension.SupportedGroups.isActivatable(serverHandshakeContext.algorithmConstraints, namedGroupValueOf)) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                            SSLLogger.fine("Ignore unsupported named group: " + SupportedGroupsExtension.NamedGroup.nameOf(keyShareEntry.namedGroupId), new Object[0]);
                        }
                    } else if (namedGroupValueOf.type == SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_ECDHE) {
                        try {
                            ECDHKeyExchange.ECDHECredentials eCDHECredentialsValueOf = ECDHKeyExchange.ECDHECredentials.valueOf(namedGroupValueOf, keyShareEntry.keyExchange);
                            if (eCDHECredentialsValueOf != null) {
                                if (serverHandshakeContext.algorithmConstraints != null && !serverHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), eCDHECredentialsValueOf.popPublicKey)) {
                                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                                        SSLLogger.warning("ECDHE key share entry does not comply to algorithm constraints", new Object[0]);
                                    }
                                } else {
                                    linkedList.add(eCDHECredentialsValueOf);
                                }
                            }
                        } catch (IOException | GeneralSecurityException e2) {
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                                SSLLogger.warning("Cannot decode named group: " + SupportedGroupsExtension.NamedGroup.nameOf(keyShareEntry.namedGroupId), new Object[0]);
                            }
                        }
                    } else if (namedGroupValueOf.type == SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_FFDHE) {
                        try {
                            DHKeyExchange.DHECredentials dHECredentialsValueOf = DHKeyExchange.DHECredentials.valueOf(namedGroupValueOf, keyShareEntry.keyExchange);
                            if (dHECredentialsValueOf != null) {
                                if (serverHandshakeContext.algorithmConstraints != null && !serverHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), dHECredentialsValueOf.popPublicKey)) {
                                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                                        SSLLogger.warning("DHE key share entry does not comply to algorithm constraints", new Object[0]);
                                    }
                                } else {
                                    linkedList.add(dHECredentialsValueOf);
                                }
                            }
                        } catch (IOException | GeneralSecurityException e3) {
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                                SSLLogger.warning("Cannot decode named group: " + SupportedGroupsExtension.NamedGroup.nameOf(keyShareEntry.namedGroupId), new Object[0]);
                            }
                        }
                    }
                }
                if (!linkedList.isEmpty()) {
                    serverHandshakeContext.handshakeCredentials.addAll(linkedList);
                } else {
                    serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.HELLO_RETRY_REQUEST.id), SSLHandshake.HELLO_RETRY_REQUEST);
                }
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_KEY_SHARE, cHKeyShareSpec);
            } catch (IOException e4) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e4);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$SHKeyShareSpec.class */
    static final class SHKeyShareSpec implements SSLExtension.SSLExtensionSpec {
        final KeyShareEntry serverShare;

        SHKeyShareSpec(KeyShareEntry keyShareEntry) {
            this.serverShare = keyShareEntry;
        }

        private SHKeyShareSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 5) {
                throw new SSLProtocolException("Invalid key_share extension: insufficient data (length=" + byteBuffer.remaining() + ")");
            }
            int int16 = Record.getInt16(byteBuffer);
            byte[] bytes16 = Record.getBytes16(byteBuffer);
            if (byteBuffer.hasRemaining()) {
                throw new SSLProtocolException("Invalid key_share extension: unknown extra data");
            }
            this.serverShare = new KeyShareEntry(int16, bytes16);
        }

        public String toString() {
            return new MessageFormat("\"server_share\": '{'\n  \"named group\": {0}\n  \"key_exchange\": '{'\n{1}\n  '}'\n'}',", Locale.ENGLISH).format(new Object[]{SupportedGroupsExtension.NamedGroup.nameOf(this.serverShare.namedGroupId), Utilities.indent(new HexDumpEncoder().encode(this.serverShare.keyExchange), "    ")});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$SHKeyShareStringizer.class */
    private static final class SHKeyShareStringizer implements SSLStringizer {
        private SHKeyShareStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new SHKeyShareSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$SHKeyShareProducer.class */
    private static final class SHKeyShareProducer implements HandshakeProducer {
        private SHKeyShareProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (((CHKeyShareSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_KEY_SHARE)) == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore, no client key_share extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.SH_KEY_SHARE)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore, no available server key_share extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (serverHandshakeContext.handshakeCredentials == null || serverHandshakeContext.handshakeCredentials.isEmpty()) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("No available client key share entries", new Object[0]);
                    return null;
                }
                return null;
            }
            KeyShareEntry keyShareEntry = null;
            Iterator<SSLCredentials> it = serverHandshakeContext.handshakeCredentials.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLCredentials next = it.next();
                SupportedGroupsExtension.NamedGroup namedGroup = null;
                if (next instanceof ECDHKeyExchange.ECDHECredentials) {
                    namedGroup = ((ECDHKeyExchange.ECDHECredentials) next).namedGroup;
                } else if (next instanceof DHKeyExchange.DHECredentials) {
                    namedGroup = ((DHKeyExchange.DHECredentials) next).namedGroup;
                }
                if (namedGroup != null) {
                    SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(namedGroup);
                    if (sSLKeyExchangeValueOf == null) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                            SSLLogger.warning("No key exchange for named group " + namedGroup.name, new Object[0]);
                        }
                    } else {
                        for (SSLPossession sSLPossession : sSLKeyExchangeValueOf.createPossessions(serverHandshakeContext)) {
                            if ((sSLPossession instanceof ECDHKeyExchange.ECDHEPossession) || (sSLPossession instanceof DHKeyExchange.DHEPossession)) {
                                serverHandshakeContext.handshakeKeyExchange = sSLKeyExchangeValueOf;
                                serverHandshakeContext.handshakePossessions.add(sSLPossession);
                                keyShareEntry = new KeyShareEntry(namedGroup.id, sSLPossession.encode());
                                break;
                            }
                        }
                        if (keyShareEntry != null) {
                            for (Map.Entry<Byte, HandshakeProducer> entry : sSLKeyExchangeValueOf.getHandshakeProducers(serverHandshakeContext)) {
                                serverHandshakeContext.handshakeProducers.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                }
            }
            if (keyShareEntry != null) {
                byte[] encoded = keyShareEntry.getEncoded();
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_KEY_SHARE, new SHKeyShareSpec(keyShareEntry));
                return encoded;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.warning("No available server key_share extension", new Object[0]);
                return null;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$SHKeyShareConsumer.class */
    private static final class SHKeyShareConsumer implements SSLExtension.ExtensionConsumer {
        private SHKeyShareConsumer() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (clientHandshakeContext.clientRequestedNamedGroups == null || clientHandshakeContext.clientRequestedNamedGroups.isEmpty()) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected key_share extension in ServerHello");
            }
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.SH_KEY_SHARE)) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported key_share extension in ServerHello");
            }
            try {
                SHKeyShareSpec sHKeyShareSpec = new SHKeyShareSpec(byteBuffer);
                KeyShareEntry keyShareEntry = sHKeyShareSpec.serverShare;
                SupportedGroupsExtension.NamedGroup namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(keyShareEntry.namedGroupId);
                if (namedGroupValueOf == null || !SupportedGroupsExtension.SupportedGroups.isActivatable(clientHandshakeContext.algorithmConstraints, namedGroupValueOf)) {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported named group: " + SupportedGroupsExtension.NamedGroup.nameOf(keyShareEntry.namedGroupId));
                }
                SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(namedGroupValueOf);
                if (sSLKeyExchangeValueOf == null) {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "No key exchange for named group " + namedGroupValueOf.name);
                }
                ECDHKeyExchange.ECDHECredentials eCDHECredentials = null;
                if (namedGroupValueOf.type == SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_ECDHE) {
                    try {
                        ECDHKeyExchange.ECDHECredentials eCDHECredentialsValueOf = ECDHKeyExchange.ECDHECredentials.valueOf(namedGroupValueOf, keyShareEntry.keyExchange);
                        if (eCDHECredentialsValueOf != null) {
                            if (clientHandshakeContext.algorithmConstraints != null && !clientHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), eCDHECredentialsValueOf.popPublicKey)) {
                                throw clientHandshakeContext.conContext.fatal(Alert.INSUFFICIENT_SECURITY, "ECDHE key share entry does not comply to algorithm constraints");
                            }
                            eCDHECredentials = eCDHECredentialsValueOf;
                        }
                    } catch (IOException | GeneralSecurityException e2) {
                        throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Cannot decode named group: " + SupportedGroupsExtension.NamedGroup.nameOf(keyShareEntry.namedGroupId));
                    }
                } else if (namedGroupValueOf.type == SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_FFDHE) {
                    try {
                        DHKeyExchange.DHECredentials dHECredentialsValueOf = DHKeyExchange.DHECredentials.valueOf(namedGroupValueOf, keyShareEntry.keyExchange);
                        if (dHECredentialsValueOf != 0) {
                            if (clientHandshakeContext.algorithmConstraints != null && !clientHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), dHECredentialsValueOf.popPublicKey)) {
                                throw clientHandshakeContext.conContext.fatal(Alert.INSUFFICIENT_SECURITY, "DHE key share entry does not comply to algorithm constraints");
                            }
                            eCDHECredentials = dHECredentialsValueOf;
                        }
                    } catch (IOException | GeneralSecurityException e3) {
                        throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Cannot decode named group: " + SupportedGroupsExtension.NamedGroup.nameOf(keyShareEntry.namedGroupId));
                    }
                } else {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported named group: " + SupportedGroupsExtension.NamedGroup.nameOf(keyShareEntry.namedGroupId));
                }
                if (eCDHECredentials == null) {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported named group: " + namedGroupValueOf.name);
                }
                clientHandshakeContext.handshakeKeyExchange = sSLKeyExchangeValueOf;
                clientHandshakeContext.handshakeCredentials.add(eCDHECredentials);
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.SH_KEY_SHARE, sHKeyShareSpec);
            } catch (IOException e4) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e4);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$SHKeyShareAbsence.class */
    private static final class SHKeyShareAbsence implements HandshakeAbsence {
        private SHKeyShareAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (SSLLogger.isOn && SSLLogger.isOn("handshake")) {
                SSLLogger.fine("No key_share extension in ServerHello, cleanup the key shares if necessary", new Object[0]);
            }
            clientHandshakeContext.handshakePossessions.clear();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$HRRKeyShareSpec.class */
    static final class HRRKeyShareSpec implements SSLExtension.SSLExtensionSpec {
        final int selectedGroup;

        HRRKeyShareSpec(SupportedGroupsExtension.NamedGroup namedGroup) {
            this.selectedGroup = namedGroup.id;
        }

        private HRRKeyShareSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() != 2) {
                throw new SSLProtocolException("Invalid key_share extension: improper data (length=" + byteBuffer.remaining() + ")");
            }
            this.selectedGroup = Record.getInt16(byteBuffer);
        }

        public String toString() {
            return new MessageFormat("\"selected group\": '['{0}']'", Locale.ENGLISH).format(new Object[]{SupportedGroupsExtension.NamedGroup.nameOf(this.selectedGroup)});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$HRRKeyShareStringizer.class */
    private static final class HRRKeyShareStringizer implements SSLStringizer {
        private HRRKeyShareStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new HRRKeyShareSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$HRRKeyShareProducer.class */
    private static final class HRRKeyShareProducer implements HandshakeProducer {
        private HRRKeyShareProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.HRR_KEY_SHARE)) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported key_share extension in HelloRetryRequest");
            }
            if (serverHandshakeContext.clientRequestedNamedGroups == null || serverHandshakeContext.clientRequestedNamedGroups.isEmpty()) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected key_share extension in HelloRetryRequest");
            }
            SupportedGroupsExtension.NamedGroup namedGroup = null;
            Iterator<SupportedGroupsExtension.NamedGroup> it = serverHandshakeContext.clientRequestedNamedGroups.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SupportedGroupsExtension.NamedGroup next = it.next();
                if (SupportedGroupsExtension.SupportedGroups.isActivatable(serverHandshakeContext.algorithmConstraints, next)) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("HelloRetryRequest selected named group: " + next.name, new Object[0]);
                    }
                    namedGroup = next;
                }
            }
            if (namedGroup == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "No common named group");
            }
            byte[] bArr = {(byte) ((namedGroup.id >> 8) & 255), (byte) (namedGroup.id & 255)};
            serverHandshakeContext.serverSelectedNamedGroup = namedGroup;
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.HRR_KEY_SHARE, new HRRKeyShareSpec(namedGroup));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$HRRKeyShareReproducer.class */
    private static final class HRRKeyShareReproducer implements HandshakeProducer {
        private HRRKeyShareReproducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.HRR_KEY_SHARE)) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported key_share extension in HelloRetryRequest");
            }
            CHKeyShareSpec cHKeyShareSpec = (CHKeyShareSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_KEY_SHARE);
            if (cHKeyShareSpec != null && cHKeyShareSpec.clientShares != null && cHKeyShareSpec.clientShares.size() == 1) {
                int i2 = cHKeyShareSpec.clientShares.get(0).namedGroupId;
                return new byte[]{(byte) ((i2 >> 8) & 255), (byte) (i2 & 255)};
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyShareExtension$HRRKeyShareConsumer.class */
    private static final class HRRKeyShareConsumer implements SSLExtension.ExtensionConsumer {
        private HRRKeyShareConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.HRR_KEY_SHARE)) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported key_share extension in HelloRetryRequest");
            }
            if (clientHandshakeContext.clientRequestedNamedGroups == null || clientHandshakeContext.clientRequestedNamedGroups.isEmpty()) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected key_share extension in HelloRetryRequest");
            }
            try {
                HRRKeyShareSpec hRRKeyShareSpec = new HRRKeyShareSpec(byteBuffer);
                SupportedGroupsExtension.NamedGroup namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(hRRKeyShareSpec.selectedGroup);
                if (namedGroupValueOf == null) {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported HelloRetryRequest selected group: " + SupportedGroupsExtension.NamedGroup.nameOf(hRRKeyShareSpec.selectedGroup));
                }
                if (!clientHandshakeContext.clientRequestedNamedGroups.contains(namedGroupValueOf)) {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected HelloRetryRequest selected group: " + namedGroupValueOf.name);
                }
                clientHandshakeContext.serverSelectedNamedGroup = namedGroupValueOf;
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.HRR_KEY_SHARE, hRRKeyShareSpec);
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }
}
