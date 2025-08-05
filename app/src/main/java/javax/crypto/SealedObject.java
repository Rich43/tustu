package javax.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import sun.misc.SharedSecrets;

/* loaded from: jce.jar:javax/crypto/SealedObject.class */
public class SealedObject implements Serializable {
    static final long serialVersionUID = 4482838265551344752L;
    private byte[] encryptedContent;
    private String sealAlg;
    private String paramsAlg;
    protected byte[] encodedParams;

    public SealedObject(Serializable serializable, Cipher cipher) throws IllegalBlockSizeException, IOException {
        this.encryptedContent = null;
        this.sealAlg = null;
        this.paramsAlg = null;
        this.encodedParams = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        try {
            objectOutputStream.writeObject(serializable);
            objectOutputStream.flush();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            objectOutputStream.close();
            try {
                this.encryptedContent = cipher.doFinal(byteArray);
            } catch (BadPaddingException e2) {
            }
            if (cipher.getParameters() != null) {
                this.encodedParams = cipher.getParameters().getEncoded();
                this.paramsAlg = cipher.getParameters().getAlgorithm();
            }
            this.sealAlg = cipher.getAlgorithm();
        } catch (Throwable th) {
            objectOutputStream.close();
            throw th;
        }
    }

    protected SealedObject(SealedObject sealedObject) {
        this.encryptedContent = null;
        this.sealAlg = null;
        this.paramsAlg = null;
        this.encodedParams = null;
        this.encryptedContent = (byte[]) sealedObject.encryptedContent.clone();
        this.sealAlg = sealedObject.sealAlg;
        this.paramsAlg = sealedObject.paramsAlg;
        if (sealedObject.encodedParams != null) {
            this.encodedParams = (byte[]) sealedObject.encodedParams.clone();
        } else {
            this.encodedParams = null;
        }
    }

    public final String getAlgorithm() {
        return this.sealAlg;
    }

    public final Object getObject(Key key) throws NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        try {
            return unseal(key, null);
        } catch (NoSuchProviderException e2) {
            throw new NoSuchAlgorithmException("algorithm not found");
        } catch (BadPaddingException e3) {
            throw new InvalidKeyException(e3.getMessage());
        } catch (IllegalBlockSizeException e4) {
            throw new InvalidKeyException(e4.getMessage());
        }
    }

    public final Object getObject(Cipher cipher) throws BadPaddingException, IllegalBlockSizeException, IOException, ClassNotFoundException {
        ObjectInputStream extObjectInputStream = getExtObjectInputStream(cipher);
        try {
            Object object = extObjectInputStream.readObject();
            extObjectInputStream.close();
            return object;
        } catch (Throwable th) {
            extObjectInputStream.close();
            throw th;
        }
    }

    public final Object getObject(Key key, String str) throws NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException, NoSuchProviderException {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        }
        try {
            return unseal(key, str);
        } catch (BadPaddingException | IllegalBlockSizeException e2) {
            throw new InvalidKeyException(e2.getMessage());
        }
    }

    private Object unseal(Key key, String str) throws BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, ClassNotFoundException, NoSuchProviderException {
        Cipher cipher;
        AlgorithmParameters algorithmParameters = null;
        if (this.encodedParams != null) {
            try {
                if (str != null) {
                    algorithmParameters = AlgorithmParameters.getInstance(this.paramsAlg, str);
                } else {
                    algorithmParameters = AlgorithmParameters.getInstance(this.paramsAlg);
                }
                algorithmParameters.init(this.encodedParams);
            } catch (NoSuchProviderException e2) {
                if (str == null) {
                    throw new NoSuchAlgorithmException(this.paramsAlg + " not found");
                }
                throw new NoSuchProviderException(e2.getMessage());
            }
        }
        try {
            if (str != null) {
                cipher = Cipher.getInstance(this.sealAlg, str);
            } else {
                cipher = Cipher.getInstance(this.sealAlg);
            }
            try {
                if (algorithmParameters != null) {
                    cipher.init(2, key, algorithmParameters);
                } else {
                    cipher.init(2, key);
                }
                ObjectInputStream extObjectInputStream = getExtObjectInputStream(cipher);
                try {
                    Object object = extObjectInputStream.readObject();
                    extObjectInputStream.close();
                    return object;
                } catch (Throwable th) {
                    extObjectInputStream.close();
                    throw th;
                }
            } catch (InvalidAlgorithmParameterException e3) {
                throw new RuntimeException(e3.getMessage());
            }
        } catch (NoSuchProviderException e4) {
            if (str == null) {
                throw new NoSuchAlgorithmException(this.sealAlg + " not found");
            }
            throw new NoSuchProviderException(e4.getMessage());
        } catch (NoSuchPaddingException e5) {
            throw new NoSuchAlgorithmException("Padding that was used in sealing operation not available");
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.encryptedContent != null) {
            this.encryptedContent = (byte[]) this.encryptedContent.clone();
        }
        if (this.encodedParams != null) {
            this.encodedParams = (byte[]) this.encodedParams.clone();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ObjectInputStream getExtObjectInputStream(Cipher cipher) throws BadPaddingException, IllegalBlockSizeException, IOException {
        return new extObjectInputStream(new ByteArrayInputStream(cipher.doFinal(this.encryptedContent)));
    }

    static {
        SharedSecrets.setJavaxCryptoSealedObjectAccess((sealedObject, cipher) -> {
            return sealedObject.getExtObjectInputStream(cipher);
        });
    }
}
