package sun.security.provider.certpath;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.cert.CRLException;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import javax.security.auth.x500.X500Principal;
import sun.security.action.GetIntegerAction;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Cache;
import sun.security.util.Debug;
import sun.security.x509.AccessDescription;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.URIName;

/* loaded from: rt.jar:sun/security/provider/certpath/URICertStore.class */
class URICertStore extends CertStoreSpi {
    private static final int CHECK_INTERVAL = 30000;
    private static final int CACHE_SIZE = 185;
    private final CertificateFactory factory;
    private Collection<X509Certificate> certs;
    private X509CRL crl;
    private long lastChecked;
    private long lastModified;
    private URI uri;
    private boolean ldap;
    private CertStoreHelper ldapHelper;
    private CertStore ldapCertStore;
    private String ldapPath;
    private static final int DEFAULT_CRL_CONNECT_TIMEOUT = 15000;
    private static final Debug debug = Debug.getInstance("certpath");
    private static final int CRL_CONNECT_TIMEOUT = initializeTimeout();
    private static final Cache<URICertStoreParameters, CertStore> certStoreCache = Cache.newSoftMemoryCache(185);

    private static int initializeTimeout() {
        Integer num = (Integer) AccessController.doPrivileged(new GetIntegerAction("com.sun.security.crl.timeout"));
        if (num == null || num.intValue() < 0) {
            return DEFAULT_CRL_CONNECT_TIMEOUT;
        }
        return num.intValue() * 1000;
    }

    URICertStore(CertStoreParameters certStoreParameters) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        super(certStoreParameters);
        this.certs = Collections.emptySet();
        this.ldap = false;
        if (!(certStoreParameters instanceof URICertStoreParameters)) {
            throw new InvalidAlgorithmParameterException("params must be instanceof URICertStoreParameters");
        }
        this.uri = ((URICertStoreParameters) certStoreParameters).uri;
        if (this.uri.getScheme().toLowerCase(Locale.ENGLISH).equals("ldap")) {
            this.ldap = true;
            this.ldapHelper = CertStoreHelper.getInstance("LDAP");
            this.ldapCertStore = this.ldapHelper.getCertStore(this.uri);
            this.ldapPath = this.uri.getPath();
            if (this.ldapPath.charAt(0) == '/') {
                this.ldapPath = this.ldapPath.substring(1);
            }
        }
        try {
            this.factory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
        } catch (CertificateException e2) {
            throw new RuntimeException();
        }
    }

    static synchronized CertStore getInstance(URICertStoreParameters uRICertStoreParameters) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (debug != null) {
            debug.println("CertStore URI:" + ((Object) uRICertStoreParameters.uri));
        }
        CertStore ucs = certStoreCache.get(uRICertStoreParameters);
        if (ucs == null) {
            ucs = new UCS(new URICertStore(uRICertStoreParameters), null, Constants._ATT_URI, uRICertStoreParameters);
            certStoreCache.put(uRICertStoreParameters, ucs);
        } else if (debug != null) {
            debug.println("URICertStore.getInstance: cache hit");
        }
        return ucs;
    }

    static CertStore getInstance(AccessDescription accessDescription) {
        if (!accessDescription.getAccessMethod().equals((Object) AccessDescription.Ad_CAISSUERS_Id)) {
            return null;
        }
        GeneralNameInterface name = accessDescription.getAccessLocation().getName();
        if (!(name instanceof URIName)) {
            return null;
        }
        try {
            return getInstance(new URICertStoreParameters(((URIName) name).getURI()));
        } catch (Exception e2) {
            if (debug != null) {
                debug.println("exception creating CertStore: " + ((Object) e2));
                e2.printStackTrace();
                return null;
            }
            return null;
        }
    }

    /* JADX WARN: Failed to calculate best type for var: r12v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x018a: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:72:0x018a */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x018f: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:74:0x018f */
    /* JADX WARN: Type inference failed for: r12v0, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.Throwable] */
    @Override // java.security.cert.CertStoreSpi
    public synchronized Collection<X509Certificate> engineGetCertificates(CertSelector certSelector) throws CertStoreException {
        if (this.ldap) {
            X509CertSelector x509CertSelector = (X509CertSelector) certSelector;
            try {
                return this.ldapCertStore.getCertificates(this.ldapHelper.wrap(x509CertSelector, x509CertSelector.getSubject(), this.ldapPath));
            } catch (IOException e2) {
                throw new CertStoreException(e2);
            }
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.lastChecked < 30000) {
            if (debug != null) {
                debug.println("Returning certificates from cache");
            }
            return getMatchingCerts(this.certs, certSelector);
        }
        this.lastChecked = jCurrentTimeMillis;
        try {
            URLConnection uRLConnectionOpenConnection = this.uri.toURL().openConnection();
            if (this.lastModified != 0) {
                uRLConnectionOpenConnection.setIfModifiedSince(this.lastModified);
            }
            try {
                long j2 = this.lastModified;
                InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
                Throwable th = null;
                this.lastModified = uRLConnectionOpenConnection.getLastModified();
                if (j2 != 0) {
                    if (j2 == this.lastModified) {
                        if (debug != null) {
                            debug.println("Not modified, using cached copy");
                        }
                        Collection<X509Certificate> matchingCerts = getMatchingCerts(this.certs, certSelector);
                        if (inputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        return matchingCerts;
                    }
                    if ((uRLConnectionOpenConnection instanceof HttpURLConnection) && ((HttpURLConnection) uRLConnectionOpenConnection).getResponseCode() == 304) {
                        if (debug != null) {
                            debug.println("Not modified, using cached copy");
                        }
                        Collection<X509Certificate> matchingCerts2 = getMatchingCerts(this.certs, certSelector);
                        if (inputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th3) {
                                    th.addSuppressed(th3);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        return matchingCerts2;
                    }
                }
                if (debug != null) {
                    debug.println("Downloading new certificates...");
                }
                this.certs = this.factory.generateCertificates(inputStream);
                if (inputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        inputStream.close();
                    }
                }
                return getMatchingCerts(this.certs, certSelector);
            } finally {
            }
        } catch (IOException | CertificateException e3) {
            if (debug != null) {
                debug.println("Exception fetching certificates:");
                e3.printStackTrace();
            }
            this.lastModified = 0L;
            this.certs = Collections.emptySet();
            return this.certs;
        }
    }

    private static Collection<X509Certificate> getMatchingCerts(Collection<X509Certificate> collection, CertSelector certSelector) {
        if (certSelector == null) {
            return collection;
        }
        ArrayList arrayList = new ArrayList(collection.size());
        for (X509Certificate x509Certificate : collection) {
            if (certSelector.match(x509Certificate)) {
                arrayList.add(x509Certificate);
            }
        }
        return arrayList;
    }

    /* JADX WARN: Failed to calculate best type for var: r14v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r15v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 14, insn: 0x019e: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:75:0x019e */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x01a3: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:77:0x01a3 */
    /* JADX WARN: Type inference failed for: r14v0, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r15v0, types: [java.lang.Throwable] */
    @Override // java.security.cert.CertStoreSpi
    public synchronized Collection<X509CRL> engineGetCRLs(CRLSelector cRLSelector) throws CertStoreException {
        if (this.ldap) {
            try {
                try {
                    return this.ldapCertStore.getCRLs(this.ldapHelper.wrap((X509CRLSelector) cRLSelector, (Collection<X500Principal>) null, this.ldapPath));
                } catch (CertStoreException e2) {
                    throw new PKIX.CertStoreTypeException("LDAP", e2);
                }
            } catch (IOException e3) {
                throw new CertStoreException(e3);
            }
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.lastChecked < 30000) {
            if (debug != null) {
                debug.println("Returning CRL from cache");
            }
            return getMatchingCRLs(this.crl, cRLSelector);
        }
        this.lastChecked = jCurrentTimeMillis;
        try {
            URLConnection uRLConnectionOpenConnection = this.uri.toURL().openConnection();
            if (this.lastModified != 0) {
                uRLConnectionOpenConnection.setIfModifiedSince(this.lastModified);
            }
            try {
                long j2 = this.lastModified;
                uRLConnectionOpenConnection.setConnectTimeout(CRL_CONNECT_TIMEOUT);
                InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
                Throwable th = null;
                this.lastModified = uRLConnectionOpenConnection.getLastModified();
                if (j2 != 0) {
                    if (j2 == this.lastModified) {
                        if (debug != null) {
                            debug.println("Not modified, using cached copy");
                        }
                        Collection<X509CRL> matchingCRLs = getMatchingCRLs(this.crl, cRLSelector);
                        if (inputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        return matchingCRLs;
                    }
                    if ((uRLConnectionOpenConnection instanceof HttpURLConnection) && ((HttpURLConnection) uRLConnectionOpenConnection).getResponseCode() == 304) {
                        if (debug != null) {
                            debug.println("Not modified, using cached copy");
                        }
                        Collection<X509CRL> matchingCRLs2 = getMatchingCRLs(this.crl, cRLSelector);
                        if (inputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th3) {
                                    th.addSuppressed(th3);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        return matchingCRLs2;
                    }
                }
                if (debug != null) {
                    debug.println("Downloading new CRL...");
                }
                this.crl = (X509CRL) this.factory.generateCRL(inputStream);
                if (inputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        inputStream.close();
                    }
                }
                return getMatchingCRLs(this.crl, cRLSelector);
            } finally {
            }
        } catch (IOException | CRLException e4) {
            if (debug != null) {
                debug.println("Exception fetching CRL:");
                e4.printStackTrace();
            }
            this.lastModified = 0L;
            this.crl = null;
            throw new PKIX.CertStoreTypeException(Constants._ATT_URI, new CertStoreException(e4));
        }
    }

    private static Collection<X509CRL> getMatchingCRLs(X509CRL x509crl, CRLSelector cRLSelector) {
        if (cRLSelector == null || (x509crl != null && cRLSelector.match(x509crl))) {
            return Collections.singletonList(x509crl);
        }
        return Collections.emptyList();
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/URICertStore$URICertStoreParameters.class */
    static class URICertStoreParameters implements CertStoreParameters {
        private final URI uri;
        private volatile int hashCode = 0;

        URICertStoreParameters(URI uri) {
            this.uri = uri;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof URICertStoreParameters)) {
                return false;
            }
            return this.uri.equals(((URICertStoreParameters) obj).uri);
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                this.hashCode = (37 * 17) + this.uri.hashCode();
            }
            return this.hashCode;
        }

        @Override // java.security.cert.CertStoreParameters
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e2) {
                throw new InternalError(e2.toString(), e2);
            }
        }
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/URICertStore$UCS.class */
    private static class UCS extends CertStore {
        protected UCS(CertStoreSpi certStoreSpi, Provider provider, String str, CertStoreParameters certStoreParameters) {
            super(certStoreSpi, provider, str, certStoreParameters);
        }
    }
}
