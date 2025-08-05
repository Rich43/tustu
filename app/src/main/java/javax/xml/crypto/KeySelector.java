package javax.xml.crypto;

import java.security.Key;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;

/* loaded from: rt.jar:javax/xml/crypto/KeySelector.class */
public abstract class KeySelector {
    public abstract KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod algorithmMethod, XMLCryptoContext xMLCryptoContext) throws KeySelectorException;

    /* loaded from: rt.jar:javax/xml/crypto/KeySelector$Purpose.class */
    public static class Purpose {
        private final String name;
        public static final Purpose SIGN = new Purpose("sign");
        public static final Purpose VERIFY = new Purpose("verify");
        public static final Purpose ENCRYPT = new Purpose("encrypt");
        public static final Purpose DECRYPT = new Purpose("decrypt");

        private Purpose(String str) {
            this.name = str;
        }

        public String toString() {
            return this.name;
        }
    }

    protected KeySelector() {
    }

    public static KeySelector singletonKeySelector(Key key) {
        return new SingletonKeySelector(key);
    }

    /* loaded from: rt.jar:javax/xml/crypto/KeySelector$SingletonKeySelector.class */
    private static class SingletonKeySelector extends KeySelector {
        private final Key key;

        SingletonKeySelector(Key key) {
            if (key == null) {
                throw new NullPointerException();
            }
            this.key = key;
        }

        @Override // javax.xml.crypto.KeySelector
        public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod algorithmMethod, XMLCryptoContext xMLCryptoContext) throws KeySelectorException {
            return new KeySelectorResult() { // from class: javax.xml.crypto.KeySelector.SingletonKeySelector.1
                @Override // javax.xml.crypto.KeySelectorResult
                public Key getKey() {
                    return SingletonKeySelector.this.key;
                }
            };
        }
    }
}
