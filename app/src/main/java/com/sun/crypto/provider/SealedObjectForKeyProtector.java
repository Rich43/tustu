package com.sun.crypto.provider;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import sun.misc.ObjectInputFilter;
import sun.misc.SharedSecrets;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/SealedObjectForKeyProtector.class */
final class SealedObjectForKeyProtector extends SealedObject {
    static final long serialVersionUID = -3650226485480866989L;
    private static final String KEY_SERIAL_FILTER = "jceks.key.serialFilter";

    SealedObjectForKeyProtector(Serializable serializable, Cipher cipher) throws IllegalBlockSizeException, IOException {
        super(serializable, cipher);
    }

    SealedObjectForKeyProtector(SealedObject sealedObject) {
        super(sealedObject);
    }

    AlgorithmParameters getParameters() {
        AlgorithmParameters algorithmParameters = null;
        if (this.encodedParams != null) {
            try {
                algorithmParameters = AlgorithmParameters.getInstance("PBE", SunJCE.getInstance());
                algorithmParameters.init(this.encodedParams);
            } catch (IOException e2) {
                throw new RuntimeException("Parameter failure: " + e2.getMessage());
            } catch (NoSuchAlgorithmException e3) {
                throw new RuntimeException("SunJCE provider is not configured properly");
            }
        }
        return algorithmParameters;
    }

    final Key getKey(Cipher cipher, int i2) throws BadPaddingException, IllegalBlockSizeException, IOException, ClassNotFoundException {
        ObjectInputStream extObjectInputStream = SharedSecrets.getJavaxCryptoSealedObjectAccess().getExtObjectInputStream(this, cipher);
        Throwable th = null;
        try {
            AccessController.doPrivileged(() -> {
                ObjectInputFilter.Config.setObjectInputFilter(extObjectInputStream, new DeserializationChecker(i2));
                return null;
            });
            try {
                Key key = (Key) extObjectInputStream.readObject();
                if (extObjectInputStream != null) {
                    if (0 != 0) {
                        try {
                            extObjectInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        extObjectInputStream.close();
                    }
                }
                return key;
            } catch (InvalidClassException e2) {
                if (e2.getMessage().contains("REJECTED")) {
                    throw new IOException("Rejected by the jceks.key.serialFilter or jdk.serialFilter property", e2);
                }
                throw e2;
            }
        } catch (Throwable th3) {
            if (extObjectInputStream != null) {
                if (0 != 0) {
                    try {
                        extObjectInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    extObjectInputStream.close();
                }
            }
            throw th3;
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/SealedObjectForKeyProtector$DeserializationChecker.class */
    private static class DeserializationChecker implements ObjectInputFilter {
        private static final ObjectInputFilter OWN_FILTER;
        private final int maxLength;

        static {
            String str = (String) AccessController.doPrivileged(() -> {
                String property = System.getProperty(SealedObjectForKeyProtector.KEY_SERIAL_FILTER);
                if (property != null) {
                    return property;
                }
                return Security.getProperty(SealedObjectForKeyProtector.KEY_SERIAL_FILTER);
            });
            OWN_FILTER = str == null ? null : ObjectInputFilter.Config.createFilter(str);
        }

        private DeserializationChecker(int i2) {
            this.maxLength = i2;
        }

        @Override // sun.misc.ObjectInputFilter
        public ObjectInputFilter.Status checkInput(ObjectInputFilter.FilterInfo filterInfo) {
            ObjectInputFilter.Status statusCheckInput;
            if (filterInfo.arrayLength() > this.maxLength) {
                return ObjectInputFilter.Status.REJECTED;
            }
            if (filterInfo.serialClass() == Object.class) {
                return ObjectInputFilter.Status.UNDECIDED;
            }
            if (OWN_FILTER != null && (statusCheckInput = OWN_FILTER.checkInput(filterInfo)) != ObjectInputFilter.Status.UNDECIDED) {
                return statusCheckInput;
            }
            ObjectInputFilter serialFilter = ObjectInputFilter.Config.getSerialFilter();
            if (serialFilter != null) {
                return serialFilter.checkInput(filterInfo);
            }
            return ObjectInputFilter.Status.UNDECIDED;
        }
    }
}
