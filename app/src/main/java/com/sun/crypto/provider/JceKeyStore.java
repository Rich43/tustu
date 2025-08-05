package com.sun.crypto.provider;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import javax.crypto.SealedObject;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.misc.IOUtils;
import sun.misc.ObjectInputFilter;
import sun.security.util.Debug;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/JceKeyStore.class */
public final class JceKeyStore extends KeyStoreSpi {
    private static final Debug debug = Debug.getInstance("keystore");
    private static final int JCEKS_MAGIC = -825307442;
    private static final int JKS_MAGIC = -17957139;
    private static final int VERSION_1 = 1;
    private static final int VERSION_2 = 2;
    private Hashtable<String, Object> entries = new Hashtable<>();

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/JceKeyStore$PrivateKeyEntry.class */
    private static final class PrivateKeyEntry {
        Date date;
        byte[] protectedKey;
        Certificate[] chain;

        private PrivateKeyEntry() {
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/JceKeyStore$SecretKeyEntry.class */
    private static final class SecretKeyEntry {
        Date date;
        SealedObject sealedKey;
        int maxLength;

        private SecretKeyEntry() {
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/JceKeyStore$TrustedCertEntry.class */
    private static final class TrustedCertEntry {
        Date date;
        Certificate cert;

        private TrustedCertEntry() {
        }
    }

    @Override // java.security.KeyStoreSpi
    public Key engineGetKey(String str, char[] cArr) throws UnrecoverableKeyException, NoSuchAlgorithmException {
        Key keyUnseal;
        Object obj = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (!(obj instanceof PrivateKeyEntry) && !(obj instanceof SecretKeyEntry)) {
            return null;
        }
        KeyProtector keyProtector = new KeyProtector(cArr);
        if (obj instanceof PrivateKeyEntry) {
            try {
                keyUnseal = keyProtector.recover(new EncryptedPrivateKeyInfo(((PrivateKeyEntry) obj).protectedKey));
            } catch (IOException e2) {
                throw new UnrecoverableKeyException("Private key not stored as PKCS #8 EncryptedPrivateKeyInfo");
            }
        } else {
            SecretKeyEntry secretKeyEntry = (SecretKeyEntry) obj;
            keyUnseal = keyProtector.unseal(secretKeyEntry.sealedKey, secretKeyEntry.maxLength);
        }
        return keyUnseal;
    }

    @Override // java.security.KeyStoreSpi
    public Certificate[] engineGetCertificateChain(String str) {
        Certificate[] certificateArr = null;
        Object obj = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if ((obj instanceof PrivateKeyEntry) && ((PrivateKeyEntry) obj).chain != null) {
            certificateArr = (Certificate[]) ((PrivateKeyEntry) obj).chain.clone();
        }
        return certificateArr;
    }

    @Override // java.security.KeyStoreSpi
    public Certificate engineGetCertificate(String str) {
        Certificate certificate = null;
        Object obj = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (obj != null) {
            if (obj instanceof TrustedCertEntry) {
                certificate = ((TrustedCertEntry) obj).cert;
            } else if ((obj instanceof PrivateKeyEntry) && ((PrivateKeyEntry) obj).chain != null) {
                certificate = ((PrivateKeyEntry) obj).chain[0];
            }
        }
        return certificate;
    }

    @Override // java.security.KeyStoreSpi
    public Date engineGetCreationDate(String str) {
        Date date = null;
        Object obj = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (obj != null) {
            if (obj instanceof TrustedCertEntry) {
                date = new Date(((TrustedCertEntry) obj).date.getTime());
            } else if (obj instanceof PrivateKeyEntry) {
                date = new Date(((PrivateKeyEntry) obj).date.getTime());
            } else {
                date = new Date(((SecretKeyEntry) obj).date.getTime());
            }
        }
        return date;
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
        synchronized (this.entries) {
            try {
                KeyProtector keyProtector = new KeyProtector(cArr);
                if (key instanceof PrivateKey) {
                    PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry();
                    privateKeyEntry.date = new Date();
                    privateKeyEntry.protectedKey = keyProtector.protect((PrivateKey) key);
                    if (certificateArr != null && certificateArr.length != 0) {
                        privateKeyEntry.chain = (Certificate[]) certificateArr.clone();
                    } else {
                        privateKeyEntry.chain = null;
                    }
                    this.entries.put(str.toLowerCase(Locale.ENGLISH), privateKeyEntry);
                } else {
                    SecretKeyEntry secretKeyEntry = new SecretKeyEntry();
                    secretKeyEntry.date = new Date();
                    secretKeyEntry.sealedKey = keyProtector.seal(key);
                    secretKeyEntry.maxLength = Integer.MAX_VALUE;
                    this.entries.put(str.toLowerCase(Locale.ENGLISH), secretKeyEntry);
                }
            } catch (Exception e2) {
                throw new KeyStoreException(e2.getMessage());
            }
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
        synchronized (this.entries) {
            PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry();
            privateKeyEntry.date = new Date();
            privateKeyEntry.protectedKey = (byte[]) bArr.clone();
            if (certificateArr != null && certificateArr.length != 0) {
                privateKeyEntry.chain = (Certificate[]) certificateArr.clone();
            } else {
                privateKeyEntry.chain = null;
            }
            this.entries.put(str.toLowerCase(Locale.ENGLISH), privateKeyEntry);
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
        synchronized (this.entries) {
            Object obj = this.entries.get(str.toLowerCase(Locale.ENGLISH));
            if (obj != null) {
                if (obj instanceof PrivateKeyEntry) {
                    throw new KeyStoreException("Cannot overwrite own certificate");
                }
                if (obj instanceof SecretKeyEntry) {
                    throw new KeyStoreException("Cannot overwrite secret key");
                }
            }
            TrustedCertEntry trustedCertEntry = new TrustedCertEntry();
            trustedCertEntry.cert = certificate;
            trustedCertEntry.date = new Date();
            this.entries.put(str.toLowerCase(Locale.ENGLISH), trustedCertEntry);
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineDeleteEntry(String str) throws KeyStoreException {
        synchronized (this.entries) {
            this.entries.remove(str.toLowerCase(Locale.ENGLISH));
        }
    }

    @Override // java.security.KeyStoreSpi
    public Enumeration<String> engineAliases() {
        return this.entries.keys();
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineContainsAlias(String str) {
        return this.entries.containsKey(str.toLowerCase(Locale.ENGLISH));
    }

    @Override // java.security.KeyStoreSpi
    public int engineSize() {
        return this.entries.size();
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsKeyEntry(String str) {
        boolean z2 = false;
        Object obj = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if ((obj instanceof PrivateKeyEntry) || (obj instanceof SecretKeyEntry)) {
            z2 = true;
        }
        return z2;
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsCertificateEntry(String str) {
        boolean z2 = false;
        if (this.entries.get(str.toLowerCase(Locale.ENGLISH)) instanceof TrustedCertEntry) {
            z2 = true;
        }
        return z2;
    }

    @Override // java.security.KeyStoreSpi
    public String engineGetCertificateAlias(Certificate certificate) {
        Certificate certificate2;
        Enumeration<String> enumerationKeys = this.entries.keys();
        while (enumerationKeys.hasMoreElements()) {
            String strNextElement2 = enumerationKeys.nextElement2();
            Object obj = this.entries.get(strNextElement2);
            if (obj instanceof TrustedCertEntry) {
                certificate2 = ((TrustedCertEntry) obj).cert;
            } else if ((obj instanceof PrivateKeyEntry) && ((PrivateKeyEntry) obj).chain != null) {
                certificate2 = ((PrivateKeyEntry) obj).chain[0];
            }
            if (certificate2.equals(certificate)) {
                return strNextElement2;
            }
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        int length;
        synchronized (this.entries) {
            if (cArr == null) {
                throw new IllegalArgumentException("password can't be null");
            }
            MessageDigest preKeyedHash = getPreKeyedHash(cArr);
            DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(outputStream, preKeyedHash));
            ObjectOutputStream objectOutputStream = null;
            try {
                dataOutputStream.writeInt(JCEKS_MAGIC);
                dataOutputStream.writeInt(2);
                dataOutputStream.writeInt(this.entries.size());
                Enumeration<String> enumerationKeys = this.entries.keys();
                while (enumerationKeys.hasMoreElements()) {
                    String strNextElement2 = enumerationKeys.nextElement2();
                    Object obj = this.entries.get(strNextElement2);
                    if (obj instanceof PrivateKeyEntry) {
                        PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) obj;
                        dataOutputStream.writeInt(1);
                        dataOutputStream.writeUTF(strNextElement2);
                        dataOutputStream.writeLong(privateKeyEntry.date.getTime());
                        dataOutputStream.writeInt(privateKeyEntry.protectedKey.length);
                        dataOutputStream.write(privateKeyEntry.protectedKey);
                        if (privateKeyEntry.chain == null) {
                            length = 0;
                        } else {
                            length = privateKeyEntry.chain.length;
                        }
                        dataOutputStream.writeInt(length);
                        for (int i2 = 0; i2 < length; i2++) {
                            byte[] encoded = privateKeyEntry.chain[i2].getEncoded();
                            dataOutputStream.writeUTF(privateKeyEntry.chain[i2].getType());
                            dataOutputStream.writeInt(encoded.length);
                            dataOutputStream.write(encoded);
                        }
                    } else if (obj instanceof TrustedCertEntry) {
                        dataOutputStream.writeInt(2);
                        dataOutputStream.writeUTF(strNextElement2);
                        dataOutputStream.writeLong(((TrustedCertEntry) obj).date.getTime());
                        byte[] encoded2 = ((TrustedCertEntry) obj).cert.getEncoded();
                        dataOutputStream.writeUTF(((TrustedCertEntry) obj).cert.getType());
                        dataOutputStream.writeInt(encoded2.length);
                        dataOutputStream.write(encoded2);
                    } else {
                        dataOutputStream.writeInt(3);
                        dataOutputStream.writeUTF(strNextElement2);
                        dataOutputStream.writeLong(((SecretKeyEntry) obj).date.getTime());
                        objectOutputStream = new ObjectOutputStream(dataOutputStream);
                        objectOutputStream.writeObject(((SecretKeyEntry) obj).sealedKey);
                    }
                }
                dataOutputStream.write(preKeyedHash.digest());
                dataOutputStream.flush();
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                } else {
                    dataOutputStream.close();
                }
            } catch (Throwable th) {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                } else {
                    dataOutputStream.close();
                }
                throw th;
            }
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        DataInputStream dataInputStream;
        synchronized (this.entries) {
            MessageDigest preKeyedHash = null;
            CertificateFactory certificateFactory = null;
            Hashtable hashtable = null;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            if (inputStream == null) {
                return;
            }
            byte[] allBytes = IOUtils.readAllBytes(inputStream);
            int length = allBytes.length;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(allBytes);
            if (cArr != null) {
                preKeyedHash = getPreKeyedHash(cArr);
                dataInputStream = new DataInputStream(new DigestInputStream(byteArrayInputStream, preKeyedHash));
            } else {
                dataInputStream = new DataInputStream(byteArrayInputStream);
            }
            ObjectInputStream objectInputStream = null;
            try {
                int i5 = dataInputStream.readInt();
                int i6 = dataInputStream.readInt();
                if ((i5 != JCEKS_MAGIC && i5 != JKS_MAGIC) || (i6 != 1 && i6 != 2)) {
                    throw new IOException("Invalid keystore format");
                }
                if (i6 == 1) {
                    certificateFactory = CertificateFactory.getInstance("X509");
                } else {
                    hashtable = new Hashtable(3);
                }
                this.entries.clear();
                int i7 = dataInputStream.readInt();
                for (int i8 = 0; i8 < i7; i8++) {
                    int i9 = dataInputStream.readInt();
                    if (i9 == 1) {
                        i3++;
                        PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry();
                        String utf = dataInputStream.readUTF();
                        privateKeyEntry.date = new Date(dataInputStream.readLong());
                        privateKeyEntry.protectedKey = IOUtils.readExactlyNBytes(dataInputStream, dataInputStream.readInt());
                        int i10 = dataInputStream.readInt();
                        ArrayList arrayList = new ArrayList();
                        for (int i11 = 0; i11 < i10; i11++) {
                            if (i6 == 2) {
                                String utf2 = dataInputStream.readUTF();
                                if (hashtable.containsKey(utf2)) {
                                    certificateFactory = (CertificateFactory) hashtable.get(utf2);
                                } else {
                                    certificateFactory = CertificateFactory.getInstance(utf2);
                                    hashtable.put(utf2, certificateFactory);
                                }
                            }
                            arrayList.add(certificateFactory.generateCertificate(new ByteArrayInputStream(IOUtils.readExactlyNBytes(dataInputStream, dataInputStream.readInt()))));
                        }
                        privateKeyEntry.chain = (Certificate[]) arrayList.toArray(new Certificate[i10]);
                        this.entries.put(utf, privateKeyEntry);
                    } else if (i9 == 2) {
                        i2++;
                        TrustedCertEntry trustedCertEntry = new TrustedCertEntry();
                        String utf3 = dataInputStream.readUTF();
                        trustedCertEntry.date = new Date(dataInputStream.readLong());
                        if (i6 == 2) {
                            String utf4 = dataInputStream.readUTF();
                            if (hashtable.containsKey(utf4)) {
                                certificateFactory = (CertificateFactory) hashtable.get(utf4);
                            } else {
                                certificateFactory = CertificateFactory.getInstance(utf4);
                                hashtable.put(utf4, certificateFactory);
                            }
                        }
                        trustedCertEntry.cert = certificateFactory.generateCertificate(new ByteArrayInputStream(IOUtils.readExactlyNBytes(dataInputStream, dataInputStream.readInt())));
                        this.entries.put(utf3, trustedCertEntry);
                    } else if (i9 == 3) {
                        i4++;
                        SecretKeyEntry secretKeyEntry = new SecretKeyEntry();
                        String utf5 = dataInputStream.readUTF();
                        secretKeyEntry.date = new Date(dataInputStream.readLong());
                        try {
                            objectInputStream = new ObjectInputStream(dataInputStream);
                            AccessController.doPrivileged(() -> {
                                ObjectInputFilter.Config.setObjectInputFilter(objectInputStream, new DeserializationChecker(length));
                                return null;
                            });
                            secretKeyEntry.sealedKey = (SealedObject) objectInputStream.readObject();
                            secretKeyEntry.maxLength = length;
                            this.entries.put(utf5, secretKeyEntry);
                        } catch (InvalidClassException e2) {
                            throw new IOException("Invalid secret key format");
                        } catch (ClassNotFoundException e3) {
                            throw new IOException(e3.getMessage());
                        }
                    } else {
                        throw new IOException("Unrecognized keystore entry: " + i9);
                    }
                }
                if (debug != null) {
                    debug.println("JceKeyStore load: private key count: " + i3 + ". trusted key count: " + i2 + ". secret key count: " + i4);
                }
                if (cArr != null) {
                    byte[] bArrDigest = preKeyedHash.digest();
                    if (!MessageDigest.isEqual(bArrDigest, IOUtils.readExactlyNBytes(dataInputStream, bArrDigest.length))) {
                        throw new IOException("Keystore was tampered with, or password was incorrect", new UnrecoverableKeyException("Password verification failed"));
                    }
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                } else {
                    dataInputStream.close();
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    objectInputStream.close();
                } else {
                    dataInputStream.close();
                }
                throw th;
            }
        }
    }

    private MessageDigest getPreKeyedHash(char[] cArr) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        byte[] bArr = new byte[cArr.length * 2];
        int i2 = 0;
        for (int i3 = 0; i3 < cArr.length; i3++) {
            int i4 = i2;
            int i5 = i2 + 1;
            bArr[i4] = (byte) (cArr[i3] >> '\b');
            i2 = i5 + 1;
            bArr[i5] = (byte) cArr[i3];
        }
        messageDigest.update(bArr);
        for (int i6 = 0; i6 < bArr.length; i6++) {
            bArr[i6] = 0;
        }
        messageDigest.update("Mighty Aphrodite".getBytes(InternalZipConstants.CHARSET_UTF8));
        return messageDigest;
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/JceKeyStore$DeserializationChecker.class */
    private static class DeserializationChecker implements ObjectInputFilter {
        private final int fullLength;

        public DeserializationChecker(int i2) {
            this.fullLength = i2;
        }

        @Override // sun.misc.ObjectInputFilter
        public ObjectInputFilter.Status checkInput(ObjectInputFilter.FilterInfo filterInfo) {
            if (filterInfo.arrayLength() > this.fullLength) {
                return ObjectInputFilter.Status.REJECTED;
            }
            Class<?> clsSerialClass = filterInfo.serialClass();
            switch ((int) filterInfo.depth()) {
                case 1:
                    if (clsSerialClass != SealedObjectForKeyProtector.class) {
                        return ObjectInputFilter.Status.REJECTED;
                    }
                    break;
                case 2:
                    if (clsSerialClass != null && clsSerialClass != SealedObject.class && clsSerialClass != byte[].class) {
                        return ObjectInputFilter.Status.REJECTED;
                    }
                    break;
                default:
                    if (clsSerialClass != null && clsSerialClass != Object.class) {
                        return ObjectInputFilter.Status.REJECTED;
                    }
                    break;
            }
            ObjectInputFilter serialFilter = ObjectInputFilter.Config.getSerialFilter();
            if (serialFilter != null) {
                return serialFilter.checkInput(filterInfo);
            }
            return ObjectInputFilter.Status.UNDECIDED;
        }
    }
}
