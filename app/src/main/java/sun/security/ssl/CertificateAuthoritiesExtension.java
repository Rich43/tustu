package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLProtocolException;
import javax.security.auth.x500.X500Principal;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/CertificateAuthoritiesExtension.class */
final class CertificateAuthoritiesExtension {
    static final HandshakeProducer chNetworkProducer = new CHCertificateAuthoritiesProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHCertificateAuthoritiesConsumer();
    static final HandshakeProducer crNetworkProducer = new CRCertificateAuthoritiesProducer();
    static final SSLExtension.ExtensionConsumer crOnLoadConsumer = new CRCertificateAuthoritiesConsumer();
    static final SSLStringizer ssStringizer = new CertificateAuthoritiesStringizer();

    CertificateAuthoritiesExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateAuthoritiesExtension$CertificateAuthoritiesSpec.class */
    static final class CertificateAuthoritiesSpec implements SSLExtension.SSLExtensionSpec {
        final List<byte[]> authorities;

        private CertificateAuthoritiesSpec(List<byte[]> list) {
            this.authorities = list;
        }

        private CertificateAuthoritiesSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 3) {
                throw new SSLProtocolException("Invalid certificate_authorities extension: insufficient data");
            }
            int int16 = Record.getInt16(byteBuffer);
            if (int16 == 0) {
                throw new SSLProtocolException("Invalid certificate_authorities extension: no certificate authorities");
            }
            if (int16 > byteBuffer.remaining()) {
                throw new SSLProtocolException("Invalid certificate_authorities extension: insufficient data");
            }
            this.authorities = new LinkedList();
            while (int16 > 0) {
                byte[] bytes16 = Record.getBytes16(byteBuffer);
                int16 -= 2 + bytes16.length;
                this.authorities.add(bytes16);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static List<byte[]> getEncodedAuthorities(X509Certificate[] x509CertificateArr) {
            ArrayList arrayList = new ArrayList(x509CertificateArr.length);
            int length = 0;
            for (X509Certificate x509Certificate : x509CertificateArr) {
                byte[] encoded = x509Certificate.getSubjectX500Principal().getEncoded();
                length += encoded.length;
                if (length > 65535) {
                    return Collections.emptyList();
                }
                if (encoded.length != 0) {
                    arrayList.add(encoded);
                }
            }
            return arrayList;
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

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"certificate authorities\": '['\n{0}']'", Locale.ENGLISH);
            StringBuilder sb = new StringBuilder(512);
            Iterator<byte[]> it = this.authorities.iterator();
            while (it.hasNext()) {
                sb.append(new X500Principal(it.next()).toString());
                sb.append("\n");
            }
            return messageFormat.format(new Object[]{Utilities.indent(sb.toString())});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateAuthoritiesExtension$CertificateAuthoritiesStringizer.class */
    private static final class CertificateAuthoritiesStringizer implements SSLStringizer {
        private CertificateAuthoritiesStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new CertificateAuthoritiesSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateAuthoritiesExtension$CHCertificateAuthoritiesProducer.class */
    private static final class CHCertificateAuthoritiesProducer implements HandshakeProducer {
        private CHCertificateAuthoritiesProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_CERTIFICATE_AUTHORITIES)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable certificate_authorities extension", new Object[0]);
                    return null;
                }
                return null;
            }
            X509Certificate[] acceptedIssuers = clientHandshakeContext.sslContext.getX509TrustManager().getAcceptedIssuers();
            if (acceptedIssuers.length != 0) {
                List encodedAuthorities = CertificateAuthoritiesSpec.getEncodedAuthorities(acceptedIssuers);
                if (encodedAuthorities.isEmpty()) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.warning("The number of CAs exceeds the maximum sizeof the certificate_authorities extension", new Object[0]);
                        return null;
                    }
                    return null;
                }
                CertificateAuthoritiesSpec certificateAuthoritiesSpec = new CertificateAuthoritiesSpec(encodedAuthorities);
                int length = 0;
                Iterator<byte[]> it = certificateAuthoritiesSpec.authorities.iterator();
                while (it.hasNext()) {
                    length += it.next().length + 2;
                }
                byte[] bArr = new byte[length + 2];
                ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
                Record.putInt16(byteBufferWrap, length);
                Iterator<byte[]> it2 = certificateAuthoritiesSpec.authorities.iterator();
                while (it2.hasNext()) {
                    Record.putBytes16(byteBufferWrap, it2.next());
                }
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_CERTIFICATE_AUTHORITIES, certificateAuthoritiesSpec);
                return bArr;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("No available certificate authorities", new Object[0]);
                return null;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateAuthoritiesExtension$CHCertificateAuthoritiesConsumer.class */
    private static final class CHCertificateAuthoritiesConsumer implements SSLExtension.ExtensionConsumer {
        private CHCertificateAuthoritiesConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_CERTIFICATE_AUTHORITIES)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable certificate_authorities extension", new Object[0]);
                    return;
                }
                return;
            }
            CertificateAuthoritiesSpec certificateAuthoritiesSpec = new CertificateAuthoritiesSpec(byteBuffer);
            serverHandshakeContext.peerSupportedAuthorities = certificateAuthoritiesSpec.getAuthorities();
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_CERTIFICATE_AUTHORITIES, certificateAuthoritiesSpec);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateAuthoritiesExtension$CRCertificateAuthoritiesProducer.class */
    private static final class CRCertificateAuthoritiesProducer implements HandshakeProducer {
        private CRCertificateAuthoritiesProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CR_CERTIFICATE_AUTHORITIES)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable certificate_authorities extension", new Object[0]);
                    return null;
                }
                return null;
            }
            X509Certificate[] acceptedIssuers = serverHandshakeContext.sslContext.getX509TrustManager().getAcceptedIssuers();
            if (acceptedIssuers.length != 0) {
                List encodedAuthorities = CertificateAuthoritiesSpec.getEncodedAuthorities(acceptedIssuers);
                if (encodedAuthorities.isEmpty()) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.warning("Too many certificate authorities to use the certificate_authorities extension", new Object[0]);
                        return null;
                    }
                    return null;
                }
                CertificateAuthoritiesSpec certificateAuthoritiesSpec = new CertificateAuthoritiesSpec(encodedAuthorities);
                int length = 0;
                Iterator<byte[]> it = certificateAuthoritiesSpec.authorities.iterator();
                while (it.hasNext()) {
                    length += it.next().length + 2;
                }
                byte[] bArr = new byte[length + 2];
                ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
                Record.putInt16(byteBufferWrap, length);
                Iterator<byte[]> it2 = certificateAuthoritiesSpec.authorities.iterator();
                while (it2.hasNext()) {
                    Record.putBytes16(byteBufferWrap, it2.next());
                }
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CR_CERTIFICATE_AUTHORITIES, certificateAuthoritiesSpec);
                return bArr;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("No available certificate authorities", new Object[0]);
                return null;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateAuthoritiesExtension$CRCertificateAuthoritiesConsumer.class */
    private static final class CRCertificateAuthoritiesConsumer implements SSLExtension.ExtensionConsumer {
        private CRCertificateAuthoritiesConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CR_CERTIFICATE_AUTHORITIES)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable certificate_authorities extension", new Object[0]);
                    return;
                }
                return;
            }
            CertificateAuthoritiesSpec certificateAuthoritiesSpec = new CertificateAuthoritiesSpec(byteBuffer);
            clientHandshakeContext.peerSupportedAuthorities = certificateAuthoritiesSpec.getAuthorities();
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CR_CERTIFICATE_AUTHORITIES, certificateAuthoritiesSpec);
        }
    }
}
