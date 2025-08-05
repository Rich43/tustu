package sun.security.ssl;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.Extension;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLProtocolException;
import sun.misc.HexDumpEncoder;
import sun.security.provider.certpath.OCSPResponse;
import sun.security.provider.certpath.ResponderId;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

/* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension.class */
final class CertStatusExtension {
    static final HandshakeProducer chNetworkProducer = new CHCertStatusReqProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHCertStatusReqConsumer();
    static final HandshakeProducer shNetworkProducer = new SHCertStatusReqProducer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHCertStatusReqConsumer();
    static final HandshakeProducer ctNetworkProducer = new CTCertStatusResponseProducer();
    static final SSLExtension.ExtensionConsumer ctOnLoadConsumer = new CTCertStatusResponseConsumer();
    static final SSLStringizer certStatusReqStringizer = new CertStatusRequestStringizer();
    static final HandshakeProducer chV2NetworkProducer = new CHCertStatusReqV2Producer();
    static final SSLExtension.ExtensionConsumer chV2OnLoadConsumer = new CHCertStatusReqV2Consumer();
    static final HandshakeProducer shV2NetworkProducer = new SHCertStatusReqV2Producer();
    static final SSLExtension.ExtensionConsumer shV2OnLoadConsumer = new SHCertStatusReqV2Consumer();
    static final SSLStringizer certStatusReqV2Stringizer = new CertStatusRequestsStringizer();
    static final SSLStringizer certStatusRespStringizer = new CertStatusRespStringizer();

    CertStatusExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CertStatusRequestSpec.class */
    static final class CertStatusRequestSpec implements SSLExtension.SSLExtensionSpec {
        static final CertStatusRequestSpec DEFAULT = new CertStatusRequestSpec(OCSPStatusRequest.EMPTY_OCSP);
        final CertStatusRequest statusRequest;

        private CertStatusRequestSpec(CertStatusRequest certStatusRequest) {
            this.statusRequest = certStatusRequest;
        }

        private CertStatusRequestSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() == 0) {
                this.statusRequest = null;
                return;
            }
            if (byteBuffer.remaining() < 1) {
                throw new SSLProtocolException("Invalid status_request extension: insufficient data");
            }
            byte int8 = (byte) Record.getInt8(byteBuffer);
            byte[] bArr = new byte[byteBuffer.remaining()];
            if (bArr.length != 0) {
                byteBuffer.get(bArr);
            }
            if (int8 == CertStatusRequestType.OCSP.id) {
                this.statusRequest = new OCSPStatusRequest(int8, bArr);
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.info("Unknown certificate status request (status type: " + ((int) int8) + ")", new Object[0]);
            }
            this.statusRequest = new CertStatusRequest(int8, bArr);
        }

        public String toString() {
            return this.statusRequest == null ? "<empty>" : this.statusRequest.toString();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CertStatusResponseSpec.class */
    static final class CertStatusResponseSpec implements SSLExtension.SSLExtensionSpec {
        final CertStatusResponse statusResponse;

        private CertStatusResponseSpec(CertStatusResponse certStatusResponse) {
            this.statusResponse = certStatusResponse;
        }

        private CertStatusResponseSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 2) {
                throw new SSLProtocolException("Invalid status_request extension: insufficient data");
            }
            byte int8 = (byte) Record.getInt8(byteBuffer);
            byte[] bytes24 = Record.getBytes24(byteBuffer);
            if (int8 == CertStatusRequestType.OCSP.id) {
                this.statusResponse = new OCSPStatusResponse(int8, bytes24);
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.info("Unknown certificate status response (status type: " + ((int) int8) + ")", new Object[0]);
            }
            this.statusResponse = new CertStatusResponse(int8, bytes24);
        }

        public String toString() {
            return this.statusResponse == null ? "<empty>" : this.statusResponse.toString();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CertStatusRequestStringizer.class */
    private static final class CertStatusRequestStringizer implements SSLStringizer {
        private CertStatusRequestStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new CertStatusRequestSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CertStatusRespStringizer.class */
    private static final class CertStatusRespStringizer implements SSLStringizer {
        private CertStatusRespStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new CertStatusResponseSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CertStatusRequestType.class */
    enum CertStatusRequestType {
        OCSP((byte) 1, "ocsp"),
        OCSP_MULTI((byte) 2, "ocsp_multi");

        final byte id;
        final String name;

        CertStatusRequestType(byte b2, String str) {
            this.id = b2;
            this.name = str;
        }

        static CertStatusRequestType valueOf(byte b2) {
            for (CertStatusRequestType certStatusRequestType : values()) {
                if (certStatusRequestType.id == b2) {
                    return certStatusRequestType;
                }
            }
            return null;
        }

        static String nameOf(byte b2) {
            for (CertStatusRequestType certStatusRequestType : values()) {
                if (certStatusRequestType.id == b2) {
                    return certStatusRequestType.name;
                }
            }
            return "UNDEFINED-CERT-STATUS-TYPE(" + ((int) b2) + ")";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CertStatusRequest.class */
    static class CertStatusRequest {
        final byte statusType;
        final byte[] encodedRequest;

        protected CertStatusRequest(byte b2, byte[] bArr) {
            this.statusType = b2;
            this.encodedRequest = bArr;
        }

        public String toString() {
            return new MessageFormat("\"certificate status type\": {0}\n\"encoded certificate status\": '{'\n{1}\n'}'", Locale.ENGLISH).format(new Object[]{CertStatusRequestType.nameOf(this.statusType), Utilities.indent(new HexDumpEncoder().encodeBuffer(this.encodedRequest))});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$OCSPStatusRequest.class */
    static final class OCSPStatusRequest extends CertStatusRequest {
        static final OCSPStatusRequest EMPTY_OCSP;
        static final OCSPStatusRequest EMPTY_OCSP_MULTI;
        final List<ResponderId> responderIds;
        final List<Extension> extensions;
        private final int ridListLen;
        private final int extListLen;

        static {
            OCSPStatusRequest oCSPStatusRequest = null;
            OCSPStatusRequest oCSPStatusRequest2 = null;
            try {
                oCSPStatusRequest = new OCSPStatusRequest(CertStatusRequestType.OCSP.id, new byte[]{0, 0, 0, 0});
                oCSPStatusRequest2 = new OCSPStatusRequest(CertStatusRequestType.OCSP_MULTI.id, new byte[]{0, 0, 0, 0});
            } catch (IOException e2) {
            }
            EMPTY_OCSP = oCSPStatusRequest;
            EMPTY_OCSP_MULTI = oCSPStatusRequest2;
        }

        private OCSPStatusRequest(byte b2, byte[] bArr) throws IOException {
            int i2;
            super(b2, bArr);
            if (bArr == null || bArr.length < 4) {
                throw new SSLProtocolException("Invalid OCSP status request: insufficient data");
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            this.ridListLen = Record.getInt16(byteBufferWrap);
            if (byteBufferWrap.remaining() < this.ridListLen + 2) {
                throw new SSLProtocolException("Invalid OCSP status request: insufficient data");
            }
            int length = this.ridListLen;
            while (true) {
                i2 = length;
                if (i2 < 2) {
                    break;
                }
                byte[] bytes16 = Record.getBytes16(byteBufferWrap);
                try {
                    arrayList.add(new ResponderId(bytes16));
                    length = i2 - (bytes16.length + 2);
                } catch (IOException e2) {
                    throw new SSLProtocolException("Invalid OCSP status request: invalid responder ID");
                }
            }
            if (i2 != 0) {
                throw new SSLProtocolException("Invalid OCSP status request: incomplete data");
            }
            byte[] bytes162 = Record.getBytes16(byteBufferWrap);
            this.extListLen = bytes162.length;
            if (this.extListLen > 0) {
                try {
                    for (DerValue derValue : new DerInputStream(bytes162).getSequence(bytes162.length)) {
                        arrayList2.add(new sun.security.x509.Extension(derValue));
                    }
                } catch (IOException e3) {
                    throw new SSLProtocolException("Invalid OCSP status request: invalid extension");
                }
            }
            this.responderIds = arrayList;
            this.extensions = arrayList2;
        }

        @Override // sun.security.ssl.CertStatusExtension.CertStatusRequest
        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"certificate status type\": {0}\n\"OCSP status request\": '{'\n{1}\n'}'", Locale.ENGLISH);
            MessageFormat messageFormat2 = new MessageFormat("\"responder_id\": {0}\n\"request extensions\": '{'\n{1}\n'}'", Locale.ENGLISH);
            String string = "<empty>";
            if (!this.responderIds.isEmpty()) {
                string = this.responderIds.toString();
            }
            String string2 = "<empty>";
            if (!this.extensions.isEmpty()) {
                StringBuilder sb = new StringBuilder(512);
                boolean z2 = true;
                for (Extension extension : this.extensions) {
                    if (z2) {
                        z2 = false;
                    } else {
                        sb.append(",\n");
                    }
                    sb.append("{\n").append(Utilities.indent(extension.toString())).append("}");
                }
                string2 = sb.toString();
            }
            return messageFormat.format(new Object[]{CertStatusRequestType.nameOf(this.statusType), Utilities.indent(messageFormat2.format(new Object[]{string, Utilities.indent(string2)}))});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CertStatusResponse.class */
    static class CertStatusResponse {
        final byte statusType;
        final byte[] encodedResponse;

        protected CertStatusResponse(byte b2, byte[] bArr) {
            this.statusType = b2;
            this.encodedResponse = bArr;
        }

        byte[] toByteArray() throws IOException {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[this.encodedResponse.length + 4]);
            Record.putInt8(byteBufferWrap, this.statusType);
            Record.putBytes24(byteBufferWrap, this.encodedResponse);
            return byteBufferWrap.array();
        }

        public String toString() {
            return new MessageFormat("\"certificate status response type\": {0}\n\"encoded certificate status\": '{'\n{1}\n'}'", Locale.ENGLISH).format(new Object[]{CertStatusRequestType.nameOf(this.statusType), Utilities.indent(new HexDumpEncoder().encodeBuffer(this.encodedResponse))});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$OCSPStatusResponse.class */
    static final class OCSPStatusResponse extends CertStatusResponse {
        final OCSPResponse ocspResponse;

        private OCSPStatusResponse(byte b2, byte[] bArr) throws IOException {
            super(b2, bArr);
            if (bArr == null || bArr.length < 1) {
                throw new SSLProtocolException("Invalid OCSP status response: insufficient data");
            }
            this.ocspResponse = new OCSPResponse(bArr);
        }

        @Override // sun.security.ssl.CertStatusExtension.CertStatusResponse
        public String toString() {
            return new MessageFormat("\"certificate status response type\": {0}\n\"OCSP status response\": '{'\n{1}\n'}'", Locale.ENGLISH).format(new Object[]{CertStatusRequestType.nameOf(this.statusType), Utilities.indent(this.ocspResponse.toString())});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CHCertStatusReqProducer.class */
    private static final class CHCertStatusReqProducer implements HandshakeProducer {
        private CHCertStatusReqProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslContext.isStaplingEnabled(true)) {
                return null;
            }
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_STATUS_REQUEST)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.CH_STATUS_REQUEST.name, new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] bArr = {1, 0, 0, 0, 0};
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_STATUS_REQUEST, CertStatusRequestSpec.DEFAULT);
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CHCertStatusReqConsumer.class */
    private static final class CHCertStatusReqConsumer implements SSLExtension.ExtensionConsumer {
        private CHCertStatusReqConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_STATUS_REQUEST)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.CH_STATUS_REQUEST.name, new Object[0]);
                    return;
                }
                return;
            }
            try {
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_STATUS_REQUEST, new CertStatusRequestSpec(byteBuffer));
                if (!serverHandshakeContext.isResumption && !serverHandshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
                    serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_STATUS.id), SSLHandshake.CERTIFICATE_STATUS);
                }
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$SHCertStatusReqProducer.class */
    private static final class SHCertStatusReqProducer implements HandshakeProducer {
        private SHCertStatusReqProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.stapleParams == null || serverHandshakeContext.stapleParams.statusRespExt != SSLExtension.CH_STATUS_REQUEST) {
                return null;
            }
            if (((CertStatusRequestSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_STATUS_REQUEST)) == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Ignore unavailable extension: " + SSLExtension.CH_STATUS_REQUEST.name, new Object[0]);
                    return null;
                }
                return null;
            }
            if (serverHandshakeContext.isResumption) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("No status_request response for session resuming", new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] bArr = new byte[0];
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_STATUS_REQUEST, CertStatusRequestSpec.DEFAULT);
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$SHCertStatusReqConsumer.class */
    private static final class SHCertStatusReqConsumer implements SSLExtension.ExtensionConsumer {
        private SHCertStatusReqConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (((CertStatusRequestSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_STATUS_REQUEST)) == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected status_request extension in ServerHello");
            }
            if (byteBuffer.hasRemaining()) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid status_request extension in ServerHello message: the extension data must be empty");
            }
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.SH_STATUS_REQUEST, CertStatusRequestSpec.DEFAULT);
            clientHandshakeContext.staplingActive = clientHandshakeContext.sslContext.isStaplingEnabled(true);
            if (clientHandshakeContext.staplingActive) {
                clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_STATUS.id), SSLHandshake.CERTIFICATE_STATUS);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CertStatusRequestV2Spec.class */
    static final class CertStatusRequestV2Spec implements SSLExtension.SSLExtensionSpec {
        static final CertStatusRequestV2Spec DEFAULT = new CertStatusRequestV2Spec(new CertStatusRequest[]{OCSPStatusRequest.EMPTY_OCSP_MULTI});
        final CertStatusRequest[] certStatusRequests;

        private CertStatusRequestV2Spec(CertStatusRequest[] certStatusRequestArr) {
            this.certStatusRequests = certStatusRequestArr;
        }

        private CertStatusRequestV2Spec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() == 0) {
                this.certStatusRequests = new CertStatusRequest[0];
                return;
            }
            if (byteBuffer.remaining() < 5) {
                throw new SSLProtocolException("Invalid status_request_v2 extension: insufficient data");
            }
            int int16 = Record.getInt16(byteBuffer);
            if (int16 <= 0) {
                throw new SSLProtocolException("certificate_status_req_list length must be positive (received length: " + int16 + ")");
            }
            int i2 = int16;
            ArrayList arrayList = new ArrayList();
            while (i2 > 0) {
                byte int8 = (byte) Record.getInt8(byteBuffer);
                int int162 = Record.getInt16(byteBuffer);
                if (byteBuffer.remaining() < int162) {
                    throw new SSLProtocolException("Invalid status_request_v2 extension: insufficient data (request_length=" + int162 + ", remining=" + byteBuffer.remaining() + ")");
                }
                byte[] bArr = new byte[int162];
                if (bArr.length != 0) {
                    byteBuffer.get(bArr);
                }
                i2 = (i2 - 3) - int162;
                if (int8 == CertStatusRequestType.OCSP.id || int8 == CertStatusRequestType.OCSP_MULTI.id) {
                    if (bArr.length < 4) {
                        throw new SSLProtocolException("Invalid status_request_v2 extension: insufficient data");
                    }
                    arrayList.add(new OCSPStatusRequest(int8, bArr));
                } else {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.info("Unknown certificate status request (status type: " + ((int) int8) + ")", new Object[0]);
                    }
                    arrayList.add(new CertStatusRequest(int8, bArr));
                }
            }
            this.certStatusRequests = (CertStatusRequest[]) arrayList.toArray(new CertStatusRequest[0]);
        }

        public String toString() {
            if (this.certStatusRequests == null || this.certStatusRequests.length == 0) {
                return "<empty>";
            }
            MessageFormat messageFormat = new MessageFormat("\"cert status request\": '{'\n{0}\n'}'", Locale.ENGLISH);
            StringBuilder sb = new StringBuilder(512);
            boolean z2 = true;
            for (CertStatusRequest certStatusRequest : this.certStatusRequests) {
                if (z2) {
                    z2 = false;
                } else {
                    sb.append(", ");
                }
                sb.append(messageFormat.format(new Object[]{Utilities.indent(certStatusRequest.toString())}));
            }
            return sb.toString();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CertStatusRequestsStringizer.class */
    private static final class CertStatusRequestsStringizer implements SSLStringizer {
        private CertStatusRequestsStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new CertStatusRequestV2Spec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CHCertStatusReqV2Producer.class */
    private static final class CHCertStatusReqV2Producer implements HandshakeProducer {
        private CHCertStatusReqV2Producer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslContext.isStaplingEnabled(true)) {
                return null;
            }
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_STATUS_REQUEST_V2)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Ignore unavailable status_request_v2 extension", new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] bArr = {0, 7, 2, 0, 4, 0, 0, 0, 0};
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_STATUS_REQUEST_V2, CertStatusRequestV2Spec.DEFAULT);
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CHCertStatusReqV2Consumer.class */
    private static final class CHCertStatusReqV2Consumer implements SSLExtension.ExtensionConsumer {
        private CHCertStatusReqV2Consumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_STATUS_REQUEST_V2)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Ignore unavailable status_request_v2 extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_STATUS_REQUEST_V2, new CertStatusRequestV2Spec(byteBuffer));
                if (!serverHandshakeContext.isResumption) {
                    serverHandshakeContext.handshakeProducers.putIfAbsent(Byte.valueOf(SSLHandshake.CERTIFICATE_STATUS.id), SSLHandshake.CERTIFICATE_STATUS);
                }
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$SHCertStatusReqV2Producer.class */
    private static final class SHCertStatusReqV2Producer implements HandshakeProducer {
        private SHCertStatusReqV2Producer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.stapleParams == null || serverHandshakeContext.stapleParams.statusRespExt != SSLExtension.CH_STATUS_REQUEST_V2) {
                return null;
            }
            if (((CertStatusRequestV2Spec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_STATUS_REQUEST_V2)) == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Ignore unavailable status_request_v2 extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (serverHandshakeContext.isResumption) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("No status_request_v2 response for session resumption", new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] bArr = new byte[0];
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_STATUS_REQUEST_V2, CertStatusRequestV2Spec.DEFAULT);
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$SHCertStatusReqV2Consumer.class */
    private static final class SHCertStatusReqV2Consumer implements SSLExtension.ExtensionConsumer {
        private SHCertStatusReqV2Consumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (((CertStatusRequestV2Spec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_STATUS_REQUEST_V2)) == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected status_request_v2 extension in ServerHello");
            }
            if (byteBuffer.hasRemaining()) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid status_request_v2 extension in ServerHello: the extension data must be empty");
            }
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.SH_STATUS_REQUEST_V2, CertStatusRequestV2Spec.DEFAULT);
            clientHandshakeContext.staplingActive = clientHandshakeContext.sslContext.isStaplingEnabled(true);
            if (clientHandshakeContext.staplingActive) {
                clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_STATUS.id), SSLHandshake.CERTIFICATE_STATUS);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CTCertStatusResponseProducer.class */
    private static final class CTCertStatusResponseProducer implements HandshakeProducer {
        private CTCertStatusResponseProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.stapleParams == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Stapling is disabled for this connection", new Object[0]);
                    return null;
                }
                return null;
            }
            if (serverHandshakeContext.currentCertEntry == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Found null CertificateEntry in context", new Object[0]);
                    return null;
                }
                return null;
            }
            try {
                X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID).generateCertificate(new ByteArrayInputStream(serverHandshakeContext.currentCertEntry.encoded));
                byte[] bArr = serverHandshakeContext.stapleParams.responseMap.get(x509Certificate);
                if (bArr == null) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("No status response found for " + ((Object) x509Certificate.getSubjectX500Principal()), new Object[0]);
                    }
                    serverHandshakeContext.currentCertEntry = null;
                    return null;
                }
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                    SSLLogger.finest("Found status response for " + ((Object) x509Certificate.getSubjectX500Principal()) + ", response length: " + bArr.length, new Object[0]);
                }
                byte[] byteArray = (serverHandshakeContext.stapleParams.statReqType == CertStatusRequestType.OCSP ? new OCSPStatusResponse(serverHandshakeContext.stapleParams.statReqType.id, bArr) : new CertStatusResponse(serverHandshakeContext.stapleParams.statReqType.id, bArr)).toByteArray();
                serverHandshakeContext.currentCertEntry = null;
                return byteArray;
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.BAD_CERT_STATUS_RESPONSE, "Failed to parse certificate status response", e2);
            } catch (CertificateException e3) {
                throw serverHandshakeContext.conContext.fatal(Alert.BAD_CERTIFICATE, "Failed to parse server certificates", e3);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertStatusExtension$CTCertStatusResponseConsumer.class */
    private static final class CTCertStatusResponseConsumer implements SSLExtension.ExtensionConsumer {
        private CTCertStatusResponseConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            try {
                CertStatusResponseSpec certStatusResponseSpec = new CertStatusResponseSpec(byteBuffer);
                if (clientHandshakeContext.sslContext.isStaplingEnabled(true)) {
                    clientHandshakeContext.staplingActive = true;
                    if (clientHandshakeContext.handshakeSession != null && !clientHandshakeContext.isResumption) {
                        ArrayList arrayList = new ArrayList(clientHandshakeContext.handshakeSession.getStatusResponses());
                        arrayList.add(certStatusResponseSpec.statusResponse.encodedResponse);
                        clientHandshakeContext.handshakeSession.setStatusResponses(arrayList);
                    } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("Ignoring stapled data on resumed session", new Object[0]);
                    }
                }
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.DECODE_ERROR, e2);
            }
        }
    }
}
