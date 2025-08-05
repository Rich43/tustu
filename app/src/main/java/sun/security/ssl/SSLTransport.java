package sun.security.ssl;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.net.ssl.SSLHandshakeException;

/* loaded from: jsse.jar:sun/security/ssl/SSLTransport.class */
interface SSLTransport {
    String getPeerHost();

    int getPeerPort();

    boolean useDelegatedTask();

    default void shutdown() throws IOException {
    }

    static Plaintext decode(TransportContext transportContext, ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) throws IOException {
        try {
            Plaintext[] plaintextArrDecode = transportContext.inputRecord.decode(byteBufferArr, i2, i3);
            if (plaintextArrDecode == null || plaintextArrDecode.length == 0) {
                return Plaintext.PLAINTEXT_NULL;
            }
            Plaintext plaintext = Plaintext.PLAINTEXT_NULL;
            for (Plaintext plaintext2 : plaintextArrDecode) {
                if (plaintext2 != null && plaintext2 != Plaintext.PLAINTEXT_NULL && plaintext2.contentType != ContentType.APPLICATION_DATA.id) {
                    transportContext.dispatch(plaintext2);
                }
                if (plaintext2 == null) {
                    plaintext2 = Plaintext.PLAINTEXT_NULL;
                } else if (plaintext2.contentType != ContentType.APPLICATION_DATA.id) {
                    continue;
                } else {
                    if (!transportContext.isNegotiated) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,verbose")) {
                            SSLLogger.warning("unexpected application data before handshake completion", new Object[0]);
                        }
                        throw transportContext.fatal(Alert.UNEXPECTED_MESSAGE, "Receiving application data before handshake complete");
                    }
                    if (byteBufferArr2 != null && i5 > 0) {
                        ByteBuffer byteBuffer = plaintext2.fragment;
                        int iRemaining = byteBuffer.remaining();
                        int i6 = i4 + i5;
                        for (int i7 = i4; i7 < i6 && iRemaining > 0; i7++) {
                            int iMin = Math.min(byteBufferArr2[i7].remaining(), iRemaining);
                            byteBuffer.limit(byteBuffer.position() + iMin);
                            byteBufferArr2[i7].put(byteBuffer);
                            iRemaining -= iMin;
                            if (!byteBufferArr2[i7].hasRemaining()) {
                                i4++;
                            }
                        }
                        if (iRemaining > 0) {
                            throw transportContext.fatal(Alert.INTERNAL_ERROR, "no sufficient room in the destination buffers");
                        }
                    }
                }
                plaintext = plaintext2;
            }
            return plaintext;
        } catch (EOFException e2) {
            throw e2;
        } catch (IOException e3) {
            throw transportContext.fatal(Alert.UNEXPECTED_MESSAGE, e3);
        } catch (UnsupportedOperationException e4) {
            transportContext.outputRecord.encodeV2NoCipher();
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.finest("may be talking to SSLv2", new Object[0]);
            }
            throw transportContext.fatal(Alert.UNEXPECTED_MESSAGE, e4);
        } catch (AEADBadTagException e5) {
            throw transportContext.fatal(Alert.BAD_RECORD_MAC, e5);
        } catch (BadPaddingException e6) {
            throw transportContext.fatal(transportContext.handshakeContext != null ? Alert.HANDSHAKE_FAILURE : Alert.BAD_RECORD_MAC, e6);
        } catch (SSLHandshakeException e7) {
            throw transportContext.fatal(Alert.HANDSHAKE_FAILURE, e7);
        }
    }
}
