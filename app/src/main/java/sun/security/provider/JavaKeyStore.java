package sun.security.provider;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.misc.IOUtils;
import sun.security.pkcs.EncryptedPrivateKeyInfo;
import sun.security.pkcs12.PKCS12KeyStore;
import sun.security.util.Debug;

/* loaded from: rt.jar:sun/security/provider/JavaKeyStore.class */
abstract class JavaKeyStore extends KeyStoreSpi {
    private static final Debug debug = Debug.getInstance("keystore");
    private static final int MAGIC = -17957139;
    private static final int VERSION_1 = 1;
    private static final int VERSION_2 = 2;
    private final Hashtable<String, Object> entries = new Hashtable<>();

    abstract String convertAlias(String str);

    /* loaded from: rt.jar:sun/security/provider/JavaKeyStore$JKS.class */
    public static final class JKS extends JavaKeyStore {
        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineLoad(inputStream, cArr);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineStore(outputStream, cArr);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ String engineGetCertificateAlias(Certificate certificate) {
            return super.engineGetCertificateAlias(certificate);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsCertificateEntry(String str) {
            return super.engineIsCertificateEntry(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsKeyEntry(String str) {
            return super.engineIsKeyEntry(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ int engineSize() {
            return super.engineSize();
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineContainsAlias(String str) {
            return super.engineContainsAlias(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Enumeration engineAliases() {
            return super.engineAliases();
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineDeleteEntry(String str) throws KeyStoreException {
            super.engineDeleteEntry(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
            super.engineSetCertificateEntry(str, certificate);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, bArr, certificateArr);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, key, cArr, certificateArr);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Date engineGetCreationDate(String str) {
            return super.engineGetCreationDate(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate engineGetCertificate(String str) {
            return super.engineGetCertificate(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate[] engineGetCertificateChain(String str) {
            return super.engineGetCertificateChain(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            return super.engineGetKey(str, cArr);
        }

        @Override // sun.security.provider.JavaKeyStore
        String convertAlias(String str) {
            return str.toLowerCase(Locale.ENGLISH);
        }
    }

    /* loaded from: rt.jar:sun/security/provider/JavaKeyStore$CaseExactJKS.class */
    public static final class CaseExactJKS extends JavaKeyStore {
        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineLoad(inputStream, cArr);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineStore(outputStream, cArr);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ String engineGetCertificateAlias(Certificate certificate) {
            return super.engineGetCertificateAlias(certificate);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsCertificateEntry(String str) {
            return super.engineIsCertificateEntry(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsKeyEntry(String str) {
            return super.engineIsKeyEntry(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ int engineSize() {
            return super.engineSize();
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineContainsAlias(String str) {
            return super.engineContainsAlias(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Enumeration engineAliases() {
            return super.engineAliases();
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineDeleteEntry(String str) throws KeyStoreException {
            super.engineDeleteEntry(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
            super.engineSetCertificateEntry(str, certificate);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, bArr, certificateArr);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, key, cArr, certificateArr);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Date engineGetCreationDate(String str) {
            return super.engineGetCreationDate(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate engineGetCertificate(String str) {
            return super.engineGetCertificate(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate[] engineGetCertificateChain(String str) {
            return super.engineGetCertificateChain(str);
        }

        @Override // sun.security.provider.JavaKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            return super.engineGetKey(str, cArr);
        }

        @Override // sun.security.provider.JavaKeyStore
        String convertAlias(String str) {
            return str;
        }
    }

    /* loaded from: rt.jar:sun/security/provider/JavaKeyStore$DualFormatJKS.class */
    public static final class DualFormatJKS extends KeyStoreDelegator {
        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineLoad(inputStream, cArr);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineStore(outputStream, cArr);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineEntryInstanceOf(String str, Class cls) {
            return super.engineEntryInstanceOf(str, cls);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetEntry(String str, KeyStore.Entry entry, KeyStore.ProtectionParameter protectionParameter) throws KeyStoreException {
            super.engineSetEntry(str, entry, protectionParameter);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ KeyStore.Entry engineGetEntry(String str, KeyStore.ProtectionParameter protectionParameter) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableEntryException {
            return super.engineGetEntry(str, protectionParameter);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ String engineGetCertificateAlias(Certificate certificate) {
            return super.engineGetCertificateAlias(certificate);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsCertificateEntry(String str) {
            return super.engineIsCertificateEntry(str);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsKeyEntry(String str) {
            return super.engineIsKeyEntry(str);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ int engineSize() {
            return super.engineSize();
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineContainsAlias(String str) {
            return super.engineContainsAlias(str);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Enumeration engineAliases() {
            return super.engineAliases();
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineDeleteEntry(String str) throws KeyStoreException {
            super.engineDeleteEntry(str);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
            super.engineSetCertificateEntry(str, certificate);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, bArr, certificateArr);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, key, cArr, certificateArr);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Date engineGetCreationDate(String str) {
            return super.engineGetCreationDate(str);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate engineGetCertificate(String str) {
            return super.engineGetCertificate(str);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate[] engineGetCertificateChain(String str) {
            return super.engineGetCertificateChain(str);
        }

        @Override // sun.security.provider.KeyStoreDelegator, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            return super.engineGetKey(str, cArr);
        }

        public DualFormatJKS() {
            super("JKS", JKS.class, "PKCS12", PKCS12KeyStore.class);
        }
    }

    /* loaded from: rt.jar:sun/security/provider/JavaKeyStore$KeyEntry.class */
    private static class KeyEntry {
        Date date;
        byte[] protectedPrivKey;
        Certificate[] chain;

        private KeyEntry() {
        }
    }

    /* loaded from: rt.jar:sun/security/provider/JavaKeyStore$TrustedCertEntry.class */
    private static class TrustedCertEntry {
        Date date;
        Certificate cert;

        private TrustedCertEntry() {
        }
    }

    JavaKeyStore() {
    }

    @Override // java.security.KeyStoreSpi
    public Key engineGetKey(String str, char[] cArr) throws UnrecoverableKeyException, NoSuchAlgorithmException {
        Object obj = this.entries.get(convertAlias(str));
        if (obj == null || !(obj instanceof KeyEntry)) {
            return null;
        }
        if (cArr == null) {
            throw new UnrecoverableKeyException("Password must not be null");
        }
        byte[] bArrConvertToBytes = convertToBytes(cArr);
        try {
            try {
                Key keyRecover = new KeyProtector(bArrConvertToBytes).recover(new EncryptedPrivateKeyInfo(((KeyEntry) obj).protectedPrivKey));
                Arrays.fill(bArrConvertToBytes, (byte) 0);
                return keyRecover;
            } catch (IOException e2) {
                throw new UnrecoverableKeyException("Private key not stored as PKCS #8 EncryptedPrivateKeyInfo");
            }
        } catch (Throwable th) {
            Arrays.fill(bArrConvertToBytes, (byte) 0);
            throw th;
        }
    }

    @Override // java.security.KeyStoreSpi
    public Certificate[] engineGetCertificateChain(String str) {
        Object obj = this.entries.get(convertAlias(str));
        if (obj == null || !(obj instanceof KeyEntry) || ((KeyEntry) obj).chain == null) {
            return null;
        }
        return (Certificate[]) ((KeyEntry) obj).chain.clone();
    }

    @Override // java.security.KeyStoreSpi
    public Certificate engineGetCertificate(String str) {
        Object obj = this.entries.get(convertAlias(str));
        if (obj != null) {
            if (obj instanceof TrustedCertEntry) {
                return ((TrustedCertEntry) obj).cert;
            }
            if (((KeyEntry) obj).chain == null) {
                return null;
            }
            return ((KeyEntry) obj).chain[0];
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public Date engineGetCreationDate(String str) {
        Object obj = this.entries.get(convertAlias(str));
        if (obj != null) {
            if (obj instanceof TrustedCertEntry) {
                return new Date(((TrustedCertEntry) obj).date.getTime());
            }
            return new Date(((KeyEntry) obj).date.getTime());
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
        byte[] bArrConvertToBytes = null;
        try {
            if (!(key instanceof PrivateKey)) {
                throw new KeyStoreException("Cannot store non-PrivateKeys");
            }
            try {
                synchronized (this.entries) {
                    KeyEntry keyEntry = new KeyEntry();
                    keyEntry.date = new Date();
                    bArrConvertToBytes = convertToBytes(cArr);
                    keyEntry.protectedPrivKey = new KeyProtector(bArrConvertToBytes).protect(key);
                    if (certificateArr != null && certificateArr.length != 0) {
                        keyEntry.chain = (Certificate[]) certificateArr.clone();
                    } else {
                        keyEntry.chain = null;
                    }
                    this.entries.put(convertAlias(str), keyEntry);
                }
                if (bArrConvertToBytes != null) {
                    Arrays.fill(bArrConvertToBytes, (byte) 0);
                }
            } catch (NoSuchAlgorithmException e2) {
                throw new KeyStoreException("Key protection algorithm not found");
            }
        } catch (Throwable th) {
            if (bArrConvertToBytes != null) {
                Arrays.fill(bArrConvertToBytes, (byte) 0);
            }
            throw th;
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
        synchronized (this.entries) {
            try {
                new EncryptedPrivateKeyInfo(bArr);
                KeyEntry keyEntry = new KeyEntry();
                keyEntry.date = new Date();
                keyEntry.protectedPrivKey = (byte[]) bArr.clone();
                if (certificateArr != null && certificateArr.length != 0) {
                    keyEntry.chain = (Certificate[]) certificateArr.clone();
                } else {
                    keyEntry.chain = null;
                }
                this.entries.put(convertAlias(str), keyEntry);
            } catch (IOException e2) {
                throw new KeyStoreException("key is not encoded as EncryptedPrivateKeyInfo");
            }
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
        synchronized (this.entries) {
            Object obj = this.entries.get(convertAlias(str));
            if (obj != null && (obj instanceof KeyEntry)) {
                throw new KeyStoreException("Cannot overwrite own certificate");
            }
            TrustedCertEntry trustedCertEntry = new TrustedCertEntry();
            trustedCertEntry.cert = certificate;
            trustedCertEntry.date = new Date();
            this.entries.put(convertAlias(str), trustedCertEntry);
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineDeleteEntry(String str) throws KeyStoreException {
        synchronized (this.entries) {
            this.entries.remove(convertAlias(str));
        }
    }

    @Override // java.security.KeyStoreSpi
    public Enumeration<String> engineAliases() {
        return this.entries.keys();
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineContainsAlias(String str) {
        return this.entries.containsKey(convertAlias(str));
    }

    @Override // java.security.KeyStoreSpi
    public int engineSize() {
        return this.entries.size();
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsKeyEntry(String str) {
        Object obj = this.entries.get(convertAlias(str));
        if (obj != null && (obj instanceof KeyEntry)) {
            return true;
        }
        return false;
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsCertificateEntry(String str) {
        Object obj = this.entries.get(convertAlias(str));
        if (obj != null && (obj instanceof TrustedCertEntry)) {
            return true;
        }
        return false;
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
            } else if (((KeyEntry) obj).chain != null) {
                certificate2 = ((KeyEntry) obj).chain[0];
            } else {
                continue;
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
            dataOutputStream.writeInt(MAGIC);
            dataOutputStream.writeInt(2);
            dataOutputStream.writeInt(this.entries.size());
            Enumeration<String> enumerationKeys = this.entries.keys();
            while (enumerationKeys.hasMoreElements()) {
                String strNextElement2 = enumerationKeys.nextElement2();
                Object obj = this.entries.get(strNextElement2);
                if (obj instanceof KeyEntry) {
                    dataOutputStream.writeInt(1);
                    dataOutputStream.writeUTF(strNextElement2);
                    dataOutputStream.writeLong(((KeyEntry) obj).date.getTime());
                    dataOutputStream.writeInt(((KeyEntry) obj).protectedPrivKey.length);
                    dataOutputStream.write(((KeyEntry) obj).protectedPrivKey);
                    if (((KeyEntry) obj).chain == null) {
                        length = 0;
                    } else {
                        length = ((KeyEntry) obj).chain.length;
                    }
                    dataOutputStream.writeInt(length);
                    for (int i2 = 0; i2 < length; i2++) {
                        byte[] encoded = ((KeyEntry) obj).chain[i2].getEncoded();
                        dataOutputStream.writeUTF(((KeyEntry) obj).chain[i2].getType());
                        dataOutputStream.writeInt(encoded.length);
                        dataOutputStream.write(encoded);
                    }
                } else {
                    dataOutputStream.writeInt(2);
                    dataOutputStream.writeUTF(strNextElement2);
                    dataOutputStream.writeLong(((TrustedCertEntry) obj).date.getTime());
                    byte[] encoded2 = ((TrustedCertEntry) obj).cert.getEncoded();
                    dataOutputStream.writeUTF(((TrustedCertEntry) obj).cert.getType());
                    dataOutputStream.writeInt(encoded2.length);
                    dataOutputStream.write(encoded2);
                }
            }
            dataOutputStream.write(preKeyedHash.digest());
            dataOutputStream.flush();
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
            if (inputStream == null) {
                return;
            }
            if (cArr != null) {
                preKeyedHash = getPreKeyedHash(cArr);
                dataInputStream = new DataInputStream(new DigestInputStream(inputStream, preKeyedHash));
            } else {
                dataInputStream = new DataInputStream(inputStream);
            }
            int i4 = dataInputStream.readInt();
            int i5 = dataInputStream.readInt();
            if (i4 != MAGIC || (i5 != 1 && i5 != 2)) {
                throw new IOException("Invalid keystore format");
            }
            if (i5 == 1) {
                certificateFactory = CertificateFactory.getInstance("X509");
            } else {
                hashtable = new Hashtable(3);
            }
            this.entries.clear();
            int i6 = dataInputStream.readInt();
            for (int i7 = 0; i7 < i6; i7++) {
                int i8 = dataInputStream.readInt();
                if (i8 == 1) {
                    i3++;
                    KeyEntry keyEntry = new KeyEntry();
                    String utf = dataInputStream.readUTF();
                    keyEntry.date = new Date(dataInputStream.readLong());
                    keyEntry.protectedPrivKey = IOUtils.readExactlyNBytes(dataInputStream, dataInputStream.readInt());
                    int i9 = dataInputStream.readInt();
                    if (i9 > 0) {
                        ArrayList arrayList = new ArrayList(i9 > 10 ? 10 : i9);
                        for (int i10 = 0; i10 < i9; i10++) {
                            if (i5 == 2) {
                                String utf2 = dataInputStream.readUTF();
                                if (hashtable.containsKey(utf2)) {
                                    certificateFactory = (CertificateFactory) hashtable.get(utf2);
                                } else {
                                    certificateFactory = CertificateFactory.getInstance(utf2);
                                    hashtable.put(utf2, certificateFactory);
                                }
                            }
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.readExactlyNBytes(dataInputStream, dataInputStream.readInt()));
                            arrayList.add(certificateFactory.generateCertificate(byteArrayInputStream));
                            byteArrayInputStream.close();
                        }
                        keyEntry.chain = (Certificate[]) arrayList.toArray(new Certificate[i9]);
                    }
                    this.entries.put(utf, keyEntry);
                } else if (i8 == 2) {
                    i2++;
                    TrustedCertEntry trustedCertEntry = new TrustedCertEntry();
                    String utf3 = dataInputStream.readUTF();
                    trustedCertEntry.date = new Date(dataInputStream.readLong());
                    if (i5 == 2) {
                        String utf4 = dataInputStream.readUTF();
                        if (hashtable.containsKey(utf4)) {
                            certificateFactory = (CertificateFactory) hashtable.get(utf4);
                        } else {
                            certificateFactory = CertificateFactory.getInstance(utf4);
                            hashtable.put(utf4, certificateFactory);
                        }
                    }
                    ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(IOUtils.readExactlyNBytes(dataInputStream, dataInputStream.readInt()));
                    trustedCertEntry.cert = certificateFactory.generateCertificate(byteArrayInputStream2);
                    byteArrayInputStream2.close();
                    this.entries.put(utf3, trustedCertEntry);
                } else {
                    throw new IOException("Unrecognized keystore entry: " + i8);
                }
            }
            if (debug != null) {
                debug.println("JavaKeyStore load: private key count: " + i3 + ". trusted key count: " + i2);
            }
            if (cArr != null) {
                byte[] bArrDigest = preKeyedHash.digest();
                if (!MessageDigest.isEqual(bArrDigest, IOUtils.readExactlyNBytes(dataInputStream, bArrDigest.length))) {
                    throw ((IOException) new IOException("Keystore was tampered with, or password was incorrect").initCause(new UnrecoverableKeyException("Password verification failed")));
                }
            }
        }
    }

    private MessageDigest getPreKeyedHash(char[] cArr) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        byte[] bArrConvertToBytes = convertToBytes(cArr);
        messageDigest.update(bArrConvertToBytes);
        Arrays.fill(bArrConvertToBytes, (byte) 0);
        messageDigest.update("Mighty Aphrodite".getBytes(InternalZipConstants.CHARSET_UTF8));
        return messageDigest;
    }

    private byte[] convertToBytes(char[] cArr) {
        byte[] bArr = new byte[cArr.length * 2];
        int i2 = 0;
        for (int i3 = 0; i3 < cArr.length; i3++) {
            int i4 = i2;
            int i5 = i2 + 1;
            bArr[i4] = (byte) (cArr[i3] >> '\b');
            i2 = i5 + 1;
            bArr[i5] = (byte) cArr[i3];
        }
        return bArr;
    }
}
