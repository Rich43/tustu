package sun.security.ssl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

/* loaded from: jsse.jar:sun/security/ssl/EphemeralKeyManager.class */
final class EphemeralKeyManager {
    private static final int INDEX_RSA512 = 0;
    private static final int INDEX_RSA1024 = 1;
    private final EphemeralKeyPair[] keys = {new EphemeralKeyPair(null), new EphemeralKeyPair(null)};

    EphemeralKeyManager() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    KeyPair getRSAKeyPair(boolean z2, SecureRandom secureRandom) {
        int i2;
        byte b2;
        KeyPair keyPair;
        if (z2) {
            i2 = 512;
            b2 = false;
        } else {
            i2 = 1024;
            b2 = true;
        }
        synchronized (this.keys) {
            KeyPair keyPair2 = this.keys[b2 == true ? 1 : 0].getKeyPair();
            if (keyPair2 == null) {
                try {
                    KeyPairGenerator keyPairGenerator = JsseJce.getKeyPairGenerator("RSA");
                    keyPairGenerator.initialize(i2, secureRandom);
                    this.keys[b2 == true ? 1 : 0] = new EphemeralKeyPair(keyPairGenerator.genKeyPair());
                    keyPair2 = this.keys[b2 == true ? 1 : 0].getKeyPair();
                } catch (Exception e2) {
                }
            }
            keyPair = keyPair2;
        }
        return keyPair;
    }

    /* loaded from: jsse.jar:sun/security/ssl/EphemeralKeyManager$EphemeralKeyPair.class */
    private static class EphemeralKeyPair {
        private static final int MAX_USE = 200;
        private static final long USE_INTERVAL = 3600000;
        private KeyPair keyPair;
        private int uses;
        private long expirationTime;

        private EphemeralKeyPair(KeyPair keyPair) {
            this.keyPair = keyPair;
            this.expirationTime = System.currentTimeMillis() + 3600000;
        }

        private boolean isValid() {
            return this.keyPair != null && this.uses < 200 && System.currentTimeMillis() < this.expirationTime;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public KeyPair getKeyPair() {
            if (!isValid()) {
                this.keyPair = null;
                return null;
            }
            this.uses++;
            return this.keyPair;
        }
    }
}
