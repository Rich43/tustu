package sun.security.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactorySpi;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.ParsingException;
import sun.security.provider.certpath.X509CertPath;
import sun.security.provider.certpath.X509CertificatePair;
import sun.security.util.Cache;
import sun.security.util.Pem;
import sun.security.x509.X509CRLImpl;
import sun.security.x509.X509CertImpl;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/provider/X509Factory.class */
public class X509Factory extends CertificateFactorySpi {
    public static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERT = "-----END CERTIFICATE-----";
    private static final int ENC_MAX_LENGTH = 4194304;
    private static final Cache<Object, X509CertImpl> certCache = Cache.newSoftMemoryCache(750);
    private static final Cache<Object, X509CRLImpl> crlCache = Cache.newSoftMemoryCache(750);

    @Override // java.security.cert.CertificateFactorySpi
    public Certificate engineGenerateCertificate(InputStream inputStream) throws IOException, CertificateException {
        if (inputStream == null) {
            certCache.clear();
            X509CertificatePair.clearCache();
            throw new CertificateException("Missing input stream");
        }
        try {
            byte[] oneBlock = readOneBlock(inputStream);
            if (oneBlock != null) {
                X509CertImpl x509CertImpl = (X509CertImpl) getFromCache(certCache, oneBlock);
                if (x509CertImpl != null) {
                    return x509CertImpl;
                }
                X509CertImpl x509CertImpl2 = new X509CertImpl(oneBlock);
                addToCache(certCache, x509CertImpl2.getEncodedInternal(), x509CertImpl2);
                return x509CertImpl2;
            }
            throw new IOException("Empty input");
        } catch (IOException e2) {
            throw new CertificateException("Could not parse certificate: " + e2.toString(), e2);
        }
    }

    private static int readFully(InputStream inputStream, ByteArrayOutputStream byteArrayOutputStream, int i2) throws IOException {
        int i3 = 0;
        byte[] bArr = new byte[2048];
        while (i2 > 0) {
            int i4 = inputStream.read(bArr, 0, i2 < 2048 ? i2 : 2048);
            if (i4 <= 0) {
                break;
            }
            byteArrayOutputStream.write(bArr, 0, i4);
            i3 += i4;
            i2 -= i4;
        }
        return i3;
    }

    public static synchronized X509CertImpl intern(X509Certificate x509Certificate) throws CertificateException {
        byte[] encoded;
        X509CertImpl x509CertImpl;
        if (x509Certificate == null) {
            return null;
        }
        boolean z2 = x509Certificate instanceof X509CertImpl;
        if (z2) {
            encoded = ((X509CertImpl) x509Certificate).getEncodedInternal();
        } else {
            encoded = x509Certificate.getEncoded();
        }
        X509CertImpl x509CertImpl2 = (X509CertImpl) getFromCache(certCache, encoded);
        if (x509CertImpl2 != null) {
            return x509CertImpl2;
        }
        if (z2) {
            x509CertImpl = (X509CertImpl) x509Certificate;
        } else {
            x509CertImpl = new X509CertImpl(encoded);
            encoded = x509CertImpl.getEncodedInternal();
        }
        addToCache(certCache, encoded, x509CertImpl);
        return x509CertImpl;
    }

    public static synchronized X509CRLImpl intern(X509CRL x509crl) throws CRLException {
        byte[] encoded;
        X509CRLImpl x509CRLImpl;
        if (x509crl == null) {
            return null;
        }
        boolean z2 = x509crl instanceof X509CRLImpl;
        if (z2) {
            encoded = ((X509CRLImpl) x509crl).getEncodedInternal();
        } else {
            encoded = x509crl.getEncoded();
        }
        X509CRLImpl x509CRLImpl2 = (X509CRLImpl) getFromCache(crlCache, encoded);
        if (x509CRLImpl2 != null) {
            return x509CRLImpl2;
        }
        if (z2) {
            x509CRLImpl = (X509CRLImpl) x509crl;
        } else {
            x509CRLImpl = new X509CRLImpl(encoded);
            encoded = x509CRLImpl.getEncodedInternal();
        }
        addToCache(crlCache, encoded, x509CRLImpl);
        return x509CRLImpl;
    }

    private static synchronized <K, V> V getFromCache(Cache<K, V> cache, byte[] bArr) {
        return cache.get(new Cache.EqualByteArray(bArr));
    }

    private static synchronized <V> void addToCache(Cache<Object, V> cache, byte[] bArr, V v2) {
        if (bArr.length > 4194304) {
            return;
        }
        cache.put(new Cache.EqualByteArray(bArr), v2);
    }

    @Override // java.security.cert.CertificateFactorySpi
    public CertPath engineGenerateCertPath(InputStream inputStream) throws IOException, CertificateException {
        if (inputStream == null) {
            throw new CertificateException("Missing input stream");
        }
        try {
            byte[] oneBlock = readOneBlock(inputStream);
            if (oneBlock != null) {
                return new X509CertPath(new ByteArrayInputStream(oneBlock));
            }
            throw new IOException("Empty input");
        } catch (IOException e2) {
            throw new CertificateException(e2.getMessage());
        }
    }

    @Override // java.security.cert.CertificateFactorySpi
    public CertPath engineGenerateCertPath(InputStream inputStream, String str) throws IOException, CertificateException {
        if (inputStream == null) {
            throw new CertificateException("Missing input stream");
        }
        try {
            byte[] oneBlock = readOneBlock(inputStream);
            if (oneBlock != null) {
                return new X509CertPath(new ByteArrayInputStream(oneBlock), str);
            }
            throw new IOException("Empty input");
        } catch (IOException e2) {
            throw new CertificateException(e2.getMessage());
        }
    }

    @Override // java.security.cert.CertificateFactorySpi
    public CertPath engineGenerateCertPath(List<? extends Certificate> list) throws CertificateException {
        return new X509CertPath(list);
    }

    @Override // java.security.cert.CertificateFactorySpi
    public Iterator<String> engineGetCertPathEncodings() {
        return X509CertPath.getEncodingsStatic();
    }

    @Override // java.security.cert.CertificateFactorySpi
    public Collection<? extends Certificate> engineGenerateCertificates(InputStream inputStream) throws CertificateException {
        if (inputStream == null) {
            throw new CertificateException("Missing input stream");
        }
        try {
            return parseX509orPKCS7Cert(inputStream);
        } catch (IOException e2) {
            throw new CertificateException(e2);
        }
    }

    @Override // java.security.cert.CertificateFactorySpi
    public CRL engineGenerateCRL(InputStream inputStream) throws IOException, CRLException {
        if (inputStream == null) {
            crlCache.clear();
            throw new CRLException("Missing input stream");
        }
        try {
            byte[] oneBlock = readOneBlock(inputStream);
            if (oneBlock != null) {
                X509CRLImpl x509CRLImpl = (X509CRLImpl) getFromCache(crlCache, oneBlock);
                if (x509CRLImpl != null) {
                    return x509CRLImpl;
                }
                X509CRLImpl x509CRLImpl2 = new X509CRLImpl(oneBlock);
                addToCache(crlCache, x509CRLImpl2.getEncodedInternal(), x509CRLImpl2);
                return x509CRLImpl2;
            }
            throw new IOException("Empty input");
        } catch (IOException e2) {
            throw new CRLException(e2.getMessage());
        }
    }

    @Override // java.security.cert.CertificateFactorySpi
    public Collection<? extends CRL> engineGenerateCRLs(InputStream inputStream) throws CRLException {
        if (inputStream == null) {
            throw new CRLException("Missing input stream");
        }
        try {
            return parseX509orPKCS7CRL(inputStream);
        } catch (IOException e2) {
            throw new CRLException(e2.getMessage());
        }
    }

    private Collection<? extends Certificate> parseX509orPKCS7Cert(InputStream inputStream) throws IOException, CertificateException {
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
        ArrayList arrayList = new ArrayList();
        int i2 = pushbackInputStream.read();
        if (i2 == -1) {
            return new ArrayList(0);
        }
        pushbackInputStream.unread(i2);
        byte[] oneBlock = readOneBlock(pushbackInputStream);
        if (oneBlock == null) {
            throw new CertificateException("No certificate data found");
        }
        try {
            X509Certificate[] certificates = new PKCS7(oneBlock).getCertificates();
            if (certificates != null) {
                return Arrays.asList(certificates);
            }
            return new ArrayList(0);
        } catch (ParsingException e2) {
            while (oneBlock != null) {
                arrayList.add(new X509CertImpl(oneBlock));
                oneBlock = readOneBlock(pushbackInputStream);
            }
            return arrayList;
        }
    }

    private Collection<? extends CRL> parseX509orPKCS7CRL(InputStream inputStream) throws IOException, CRLException {
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
        ArrayList arrayList = new ArrayList();
        int i2 = pushbackInputStream.read();
        if (i2 == -1) {
            return new ArrayList(0);
        }
        pushbackInputStream.unread(i2);
        byte[] oneBlock = readOneBlock(pushbackInputStream);
        if (oneBlock == null) {
            throw new CRLException("No CRL data found");
        }
        try {
            X509CRL[] cRLs = new PKCS7(oneBlock).getCRLs();
            if (cRLs != null) {
                return Arrays.asList(cRLs);
            }
            return new ArrayList(0);
        } catch (ParsingException e2) {
            while (oneBlock != null) {
                arrayList.add(new X509CRLImpl(oneBlock));
                oneBlock = readOneBlock(pushbackInputStream);
            }
            return arrayList;
        }
    }

    private static byte[] readOneBlock(InputStream inputStream) throws IOException {
        int i2;
        int i3 = inputStream.read();
        if (i3 == -1) {
            return null;
        }
        if (i3 == 48) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
            byteArrayOutputStream.write(i3);
            readBERInternal(inputStream, byteArrayOutputStream, i3);
            return byteArrayOutputStream.toByteArray();
        }
        char[] cArrCopyOf = new char[2048];
        int i4 = 0;
        int i5 = i3 == 45 ? 1 : 0;
        int i6 = i3 == 45 ? -1 : i3;
        while (true) {
            int i7 = inputStream.read();
            if (i7 == -1) {
                return null;
            }
            if (i7 == 45) {
                i5++;
            } else {
                i5 = 0;
                i6 = i7;
            }
            if (i5 == 5 && (i6 == -1 || i6 == 13 || i6 == 10)) {
                break;
            }
        }
        StringBuilder sb = new StringBuilder("-----");
        while (true) {
            int i8 = inputStream.read();
            if (i8 == -1) {
                throw new IOException("Incomplete data");
            }
            if (i8 == 10) {
                i2 = 10;
                break;
            }
            if (i8 == 13) {
                int i9 = inputStream.read();
                if (i9 == -1) {
                    throw new IOException("Incomplete data");
                }
                if (i9 == 10) {
                    i2 = 10;
                } else {
                    i2 = 13;
                    i4 = 0 + 1;
                    cArrCopyOf[0] = (char) i9;
                }
            } else {
                sb.append((char) i8);
            }
        }
        while (true) {
            int i10 = inputStream.read();
            if (i10 == -1) {
                throw new IOException("Incomplete data");
            }
            if (i10 != 45) {
                int i11 = i4;
                i4++;
                cArrCopyOf[i11] = (char) i10;
                if (i4 >= cArrCopyOf.length) {
                    cArrCopyOf = Arrays.copyOf(cArrCopyOf, cArrCopyOf.length + 1024);
                }
            } else {
                StringBuilder sb2 = new StringBuilder(LanguageTag.SEP);
                while (true) {
                    int i12 = inputStream.read();
                    if (i12 == -1 || i12 == i2 || i12 == 10) {
                        break;
                    }
                    if (i12 != 13) {
                        sb2.append((char) i12);
                    }
                }
                checkHeaderFooter(sb.toString(), sb2.toString());
                return Pem.decode(new String(cArrCopyOf, 0, i4));
            }
        }
    }

    private static void checkHeaderFooter(String str, String str2) throws IOException {
        if (str.length() < 16 || !str.startsWith("-----BEGIN ") || !str.endsWith("-----")) {
            throw new IOException("Illegal header: " + str);
        }
        if (str2.length() < 14 || !str2.startsWith("-----END ") || !str2.endsWith("-----")) {
            throw new IOException("Illegal footer: " + str2);
        }
        if (!str.substring(11, str.length() - 5).equals(str2.substring(9, str2.length() - 5))) {
            throw new IOException("Header and footer do not match: " + str + " " + str2);
        }
    }

    private static int readBERInternal(InputStream inputStream, ByteArrayOutputStream byteArrayOutputStream, int i2) throws IOException {
        int i3;
        if (i2 == -1) {
            i2 = inputStream.read();
            if (i2 == -1) {
                throw new IOException("BER/DER tag info absent");
            }
            if ((i2 & 31) == 31) {
                throw new IOException("Multi octets tag not supported");
            }
            byteArrayOutputStream.write(i2);
        }
        int i4 = inputStream.read();
        if (i4 == -1) {
            throw new IOException("BER/DER length info absent");
        }
        byteArrayOutputStream.write(i4);
        if (i4 == 128) {
            if ((i2 & 32) != 32) {
                throw new IOException("Non constructed encoding must have definite length");
            }
            while (readBERInternal(inputStream, byteArrayOutputStream, -1) != 0) {
            }
        } else {
            if (i4 < 128) {
                i3 = i4;
            } else if (i4 == 129) {
                i3 = inputStream.read();
                if (i3 == -1) {
                    throw new IOException("Incomplete BER/DER length info");
                }
                byteArrayOutputStream.write(i3);
            } else if (i4 == 130) {
                int i5 = inputStream.read();
                int i6 = inputStream.read();
                if (i6 == -1) {
                    throw new IOException("Incomplete BER/DER length info");
                }
                byteArrayOutputStream.write(i5);
                byteArrayOutputStream.write(i6);
                i3 = (i5 << 8) | i6;
            } else if (i4 == 131) {
                int i7 = inputStream.read();
                int i8 = inputStream.read();
                int i9 = inputStream.read();
                if (i9 == -1) {
                    throw new IOException("Incomplete BER/DER length info");
                }
                byteArrayOutputStream.write(i7);
                byteArrayOutputStream.write(i8);
                byteArrayOutputStream.write(i9);
                i3 = (i7 << 16) | (i8 << 8) | i9;
            } else if (i4 == 132) {
                int i10 = inputStream.read();
                int i11 = inputStream.read();
                int i12 = inputStream.read();
                int i13 = inputStream.read();
                if (i13 == -1) {
                    throw new IOException("Incomplete BER/DER length info");
                }
                if (i10 > 127) {
                    throw new IOException("Invalid BER/DER data (a little huge?)");
                }
                byteArrayOutputStream.write(i10);
                byteArrayOutputStream.write(i11);
                byteArrayOutputStream.write(i12);
                byteArrayOutputStream.write(i13);
                i3 = (i10 << 24) | (i11 << 16) | (i12 << 8) | i13;
            } else {
                throw new IOException("Invalid BER/DER data (too huge?)");
            }
            if (readFully(inputStream, byteArrayOutputStream, i3) != i3) {
                throw new IOException("Incomplete BER/DER data");
            }
        }
        return i2;
    }
}
