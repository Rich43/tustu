package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/SSLExtensions.class */
final class SSLExtensions {
    private final SSLHandshake.HandshakeMessage handshakeMessage;
    private Map<SSLExtension, byte[]> extMap = new LinkedHashMap();
    private int encodedLength;
    private final Map<Integer, byte[]> logMap;

    SSLExtensions(SSLHandshake.HandshakeMessage handshakeMessage) {
        this.logMap = SSLLogger.isOn ? new LinkedHashMap() : null;
        this.handshakeMessage = handshakeMessage;
        this.encodedLength = 2;
    }

    SSLExtensions(SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer, SSLExtension[] sSLExtensionArr) throws IOException {
        this.logMap = SSLLogger.isOn ? new LinkedHashMap() : null;
        this.handshakeMessage = handshakeMessage;
        int int16 = Record.getInt16(byteBuffer);
        this.encodedLength = int16 + 2;
        while (int16 > 0) {
            int int162 = Record.getInt16(byteBuffer);
            int int163 = Record.getInt16(byteBuffer);
            if (int163 > byteBuffer.remaining()) {
                throw handshakeMessage.handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Error parsing extension (" + int162 + "): no sufficient data");
            }
            boolean z2 = true;
            SSLHandshake sSLHandshakeHandshakeType = handshakeMessage.handshakeType();
            if (SSLExtension.isConsumable(int162) && SSLExtension.valueOf(sSLHandshakeHandshakeType, int162) == null) {
                if (int162 == SSLExtension.CH_SUPPORTED_GROUPS.id && sSLHandshakeHandshakeType == SSLHandshake.SERVER_HELLO) {
                    z2 = false;
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.warning("Received buggy supported_groups extension in the ServerHello handshake message", new Object[0]);
                    }
                } else {
                    if (sSLHandshakeHandshakeType == SSLHandshake.SERVER_HELLO) {
                        throw handshakeMessage.handshakeContext.conContext.fatal(Alert.UNSUPPORTED_EXTENSION, "extension (" + int162 + ") should not be presented in " + sSLHandshakeHandshakeType.name);
                    }
                    z2 = false;
                }
            }
            if (z2) {
                z2 = false;
                int length = sSLExtensionArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    SSLExtension sSLExtension = sSLExtensionArr[i2];
                    if (sSLExtension.id != int162 || sSLExtension.onLoadConsumer == null) {
                        i2++;
                    } else {
                        if (sSLExtension.handshakeType != sSLHandshakeHandshakeType) {
                            throw handshakeMessage.handshakeContext.conContext.fatal(Alert.UNSUPPORTED_EXTENSION, "extension (" + int162 + ") should not be presented in " + sSLHandshakeHandshakeType.name);
                        }
                        byte[] bArr = new byte[int163];
                        byteBuffer.get(bArr);
                        this.extMap.put(sSLExtension, bArr);
                        if (this.logMap != null) {
                            this.logMap.put(Integer.valueOf(int162), bArr);
                        }
                        z2 = true;
                    }
                }
            }
            if (!z2) {
                if (this.logMap != null) {
                    byte[] bArr2 = new byte[int163];
                    byteBuffer.get(bArr2);
                    this.logMap.put(Integer.valueOf(int162), bArr2);
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("Ignore unknown or unsupported extension", toString(int162, bArr2));
                    }
                } else {
                    byteBuffer.position(byteBuffer.position() + int163);
                }
            }
            int16 -= int163 + 4;
        }
    }

    byte[] get(SSLExtension sSLExtension) {
        return this.extMap.get(sSLExtension);
    }

    void consumeOnLoad(HandshakeContext handshakeContext, SSLExtension[] sSLExtensionArr) throws IOException {
        for (SSLExtension sSLExtension : sSLExtensionArr) {
            if (handshakeContext.negotiatedProtocol != null && !sSLExtension.isAvailable(handshakeContext.negotiatedProtocol)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unsupported extension: " + sSLExtension.name, new Object[0]);
                }
            } else if (!this.extMap.containsKey(sSLExtension)) {
                if (sSLExtension.onLoadAbsence != null) {
                    sSLExtension.absentOnLoad(handshakeContext, this.handshakeMessage);
                } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + sSLExtension.name, new Object[0]);
                }
            } else if (sSLExtension.onLoadConsumer == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore unsupported extension: " + sSLExtension.name, new Object[0]);
                }
            } else {
                sSLExtension.consumeOnLoad(handshakeContext, this.handshakeMessage, ByteBuffer.wrap(this.extMap.get(sSLExtension)));
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Consumed extension: " + sSLExtension.name, new Object[0]);
                }
            }
        }
    }

    void consumeOnTrade(HandshakeContext handshakeContext, SSLExtension[] sSLExtensionArr) throws IOException {
        for (SSLExtension sSLExtension : sSLExtensionArr) {
            if (!this.extMap.containsKey(sSLExtension)) {
                if (sSLExtension.onTradeAbsence != null) {
                    sSLExtension.absentOnTrade(handshakeContext, this.handshakeMessage);
                } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + sSLExtension.name, new Object[0]);
                }
            } else if (sSLExtension.onTradeConsumer == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore impact of unsupported extension: " + sSLExtension.name, new Object[0]);
                }
            } else {
                sSLExtension.consumeOnTrade(handshakeContext, this.handshakeMessage);
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Populated with extension: " + sSLExtension.name, new Object[0]);
                }
            }
        }
    }

    void produce(HandshakeContext handshakeContext, SSLExtension[] sSLExtensionArr) throws IOException {
        for (SSLExtension sSLExtension : sSLExtensionArr) {
            if (this.extMap.containsKey(sSLExtension)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore, duplicated extension: " + sSLExtension.name, new Object[0]);
                }
            } else if (sSLExtension.networkProducer == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore, no extension producer defined: " + sSLExtension.name, new Object[0]);
                }
            } else {
                byte[] bArrProduce = sSLExtension.produce(handshakeContext, this.handshakeMessage);
                if (bArrProduce != null) {
                    this.extMap.put(sSLExtension, bArrProduce);
                    this.encodedLength += bArrProduce.length + 4;
                } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore, context unavailable extension: " + sSLExtension.name, new Object[0]);
                }
            }
        }
    }

    void reproduce(HandshakeContext handshakeContext, SSLExtension[] sSLExtensionArr) throws IOException {
        for (SSLExtension sSLExtension : sSLExtensionArr) {
            if (sSLExtension.networkProducer == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore, no extension producer defined: " + sSLExtension.name, new Object[0]);
                }
            } else {
                byte[] bArrProduce = sSLExtension.produce(handshakeContext, this.handshakeMessage);
                if (bArrProduce != null) {
                    if (this.extMap.containsKey(sSLExtension)) {
                        byte[] bArrReplace = this.extMap.replace(sSLExtension, bArrProduce);
                        if (bArrReplace != null) {
                            this.encodedLength -= bArrReplace.length + 4;
                        }
                        this.encodedLength += bArrProduce.length + 4;
                    } else {
                        this.extMap.put(sSLExtension, bArrProduce);
                        this.encodedLength += bArrProduce.length + 4;
                    }
                } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore, context unavailable extension: " + sSLExtension.name, new Object[0]);
                }
            }
        }
    }

    int length() {
        if (this.extMap.isEmpty()) {
            return 0;
        }
        return this.encodedLength;
    }

    void send(HandshakeOutStream handshakeOutStream) throws IOException {
        int length = length();
        if (length == 0) {
            return;
        }
        handshakeOutStream.putInt16(length - 2);
        for (SSLExtension sSLExtension : SSLExtension.values()) {
            byte[] bArr = this.extMap.get(sSLExtension);
            if (bArr != null) {
                handshakeOutStream.putInt16(sSLExtension.id);
                handshakeOutStream.putBytes16(bArr);
            }
        }
    }

    public String toString() {
        if (this.extMap.isEmpty() && (this.logMap == null || this.logMap.isEmpty())) {
            return "<no extension>";
        }
        StringBuilder sb = new StringBuilder(512);
        if (this.logMap != null && !this.logMap.isEmpty()) {
            for (Map.Entry<Integer, byte[]> entry : this.logMap.entrySet()) {
                SSLExtension sSLExtensionValueOf = SSLExtension.valueOf(this.handshakeMessage.handshakeType(), entry.getKey().intValue());
                if (sb.length() != 0) {
                    sb.append(",\n");
                }
                if (sSLExtensionValueOf != null) {
                    sb.append(sSLExtensionValueOf.toString(ByteBuffer.wrap(entry.getValue())));
                } else {
                    sb.append(toString(entry.getKey().intValue(), entry.getValue()));
                }
            }
            return sb.toString();
        }
        for (Map.Entry<SSLExtension, byte[]> entry2 : this.extMap.entrySet()) {
            if (sb.length() != 0) {
                sb.append(",\n");
            }
            sb.append(entry2.getKey().toString(ByteBuffer.wrap(entry2.getValue())));
        }
        return sb.toString();
    }

    private static String toString(int i2, byte[] bArr) {
        return new MessageFormat("\"{0} ({1})\": '{'\n{2}\n'}'", Locale.ENGLISH).format(new Object[]{SSLExtension.nameOf(i2), Integer.valueOf(i2), Utilities.indent(new HexDumpEncoder().encodeBuffer(bArr))});
    }
}
