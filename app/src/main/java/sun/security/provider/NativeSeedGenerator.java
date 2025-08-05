package sun.security.provider;

import java.io.IOException;

/* loaded from: rt.jar:sun/security/provider/NativeSeedGenerator.class */
class NativeSeedGenerator extends SeedGenerator {
    private static native boolean nativeGenerateSeed(byte[] bArr);

    NativeSeedGenerator(String str) throws IOException {
        if (!nativeGenerateSeed(new byte[2])) {
            throw new IOException("Required native CryptoAPI features not  available on this machine");
        }
    }

    @Override // sun.security.provider.SeedGenerator
    void getSeedBytes(byte[] bArr) {
        if (!nativeGenerateSeed(bArr)) {
            throw new InternalError("Unexpected CryptoAPI failure generating seed");
        }
    }
}
