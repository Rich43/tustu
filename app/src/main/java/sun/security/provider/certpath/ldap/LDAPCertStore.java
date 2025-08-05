package sun.security.provider.certpath.ldap;

import com.sun.jndi.ldap.LdapReferralException;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.LDAPCertStoreParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.LdapContext;
import javax.security.auth.x500.X500Principal;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.misc.HexDumpEncoder;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetPropertyAction;
import sun.security.provider.certpath.X509CertificatePair;
import sun.security.util.Cache;
import sun.security.util.Debug;
import sun.security.x509.X500Name;

/* loaded from: rt.jar:sun/security/provider/certpath/ldap/LDAPCertStore.class */
public final class LDAPCertStore extends CertStoreSpi {
    private static final boolean DEBUG = false;
    private static final String USER_CERT = "userCertificate;binary";
    private static final String CA_CERT = "cACertificate;binary";
    private static final String CROSS_CERT = "crossCertificatePair;binary";
    private static final String CRL = "certificateRevocationList;binary";
    private static final String ARL = "authorityRevocationList;binary";
    private static final String DELTA_CRL = "deltaRevocationList;binary";
    private static final int DEFAULT_CACHE_SIZE = 750;
    private static final int DEFAULT_CACHE_LIFETIME = 30;
    private static final int LIFETIME;
    private static final String PROP_LIFETIME = "sun.security.certpath.ldap.cache.lifetime";
    private static final String PROP_DISABLE_APP_RESOURCE_FILES = "sun.security.certpath.ldap.disable.app.resource.files";
    private CertificateFactory cf;
    private DirContext ctx;
    private boolean prefetchCRLs;
    private final Cache<String, byte[][]> valueCache;
    private int cacheHits;
    private int cacheMisses;
    private int requests;
    private static final Cache<LDAPCertStoreParameters, CertStore> certStoreCache;
    private static final Debug debug = Debug.getInstance("certpath");
    private static final String[] STRING0 = new String[0];
    private static final byte[][] BB0 = new byte[0];
    private static final Attributes EMPTY_ATTRIBUTES = new BasicAttributes();

    static /* synthetic */ int access$108(LDAPCertStore lDAPCertStore) {
        int i2 = lDAPCertStore.cacheHits;
        lDAPCertStore.cacheHits = i2 + 1;
        return i2;
    }

    static /* synthetic */ int access$208(LDAPCertStore lDAPCertStore) {
        int i2 = lDAPCertStore.cacheMisses;
        lDAPCertStore.cacheMisses = i2 + 1;
        return i2;
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [byte[], byte[][]] */
    static {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(PROP_LIFETIME));
        if (str != null) {
            LIFETIME = Integer.parseInt(str);
        } else {
            LIFETIME = 30;
        }
        certStoreCache = Cache.newSoftMemoryCache(185);
    }

    public LDAPCertStore(CertStoreParameters certStoreParameters) throws InvalidAlgorithmParameterException {
        super(certStoreParameters);
        this.prefetchCRLs = false;
        this.cacheHits = 0;
        this.cacheMisses = 0;
        this.requests = 0;
        if (!(certStoreParameters instanceof LDAPCertStoreParameters)) {
            throw new InvalidAlgorithmParameterException("parameters must be LDAPCertStoreParameters");
        }
        LDAPCertStoreParameters lDAPCertStoreParameters = (LDAPCertStoreParameters) certStoreParameters;
        createInitialDirContext(lDAPCertStoreParameters.getServerName(), lDAPCertStoreParameters.getPort());
        try {
            this.cf = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
            if (LIFETIME == 0) {
                this.valueCache = Cache.newNullCache();
            } else if (LIFETIME < 0) {
                this.valueCache = Cache.newSoftMemoryCache(DEFAULT_CACHE_SIZE);
            } else {
                this.valueCache = Cache.newSoftMemoryCache(DEFAULT_CACHE_SIZE, LIFETIME);
            }
        } catch (CertificateException e2) {
            throw new InvalidAlgorithmParameterException("unable to create CertificateFactory for X.509");
        }
    }

    static synchronized CertStore getInstance(LDAPCertStoreParameters lDAPCertStoreParameters) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkConnect(lDAPCertStoreParameters.getServerName(), lDAPCertStoreParameters.getPort());
        }
        CertStore certStore = certStoreCache.get(lDAPCertStoreParameters);
        if (certStore == null) {
            certStore = CertStore.getInstance("LDAP", lDAPCertStoreParameters);
            certStoreCache.put(lDAPCertStoreParameters, certStore);
        } else if (debug != null) {
            debug.println("LDAPCertStore.getInstance: cache hit");
        }
        return certStore;
    }

    private void createInitialDirContext(String str, int i2) throws InvalidAlgorithmParameterException {
        String str2 = "ldap://" + str + CallSiteDescriptor.TOKEN_DELIMITER + i2;
        Hashtable hashtable = new Hashtable();
        hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        hashtable.put(Context.PROVIDER_URL, str2);
        if (((Boolean) AccessController.doPrivileged(new GetBooleanAction(PROP_DISABLE_APP_RESOURCE_FILES))).booleanValue()) {
            if (debug != null) {
                debug.println("LDAPCertStore disabling app resource files");
            }
            hashtable.put("com.sun.naming.disable.app.resource.files", "true");
        }
        try {
            this.ctx = new InitialDirContext((Hashtable<?, ?>) hashtable);
            if (this.ctx.getEnvironment().get(Context.REFERRAL) == null) {
                this.ctx.addToEnvironment(Context.REFERRAL, "throw");
            }
        } catch (NamingException e2) {
            if (debug != null) {
                debug.println("LDAPCertStore.engineInit about to throw InvalidAlgorithmParameterException");
                e2.printStackTrace();
            }
            InvalidAlgorithmParameterException invalidAlgorithmParameterException = new InvalidAlgorithmParameterException("unable to create InitialDirContext using supplied parameters");
            invalidAlgorithmParameterException.initCause(e2);
            throw invalidAlgorithmParameterException;
        }
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/ldap/LDAPCertStore$LDAPRequest.class */
    private class LDAPRequest {
        private final String name;
        private Map<String, byte[][]> valueMap;
        private final List<String> requestedAttributes = new ArrayList(5);

        LDAPRequest(String str) throws CertStoreException {
            this.name = checkName(str);
        }

        private String checkName(String str) throws CertStoreException {
            if (str == null) {
                throw new CertStoreException("Name absent");
            }
            try {
                if (new CompositeName(str).size() > 1) {
                    throw new CertStoreException("Invalid name: " + str);
                }
                return str;
            } catch (InvalidNameException e2) {
                throw new CertStoreException("Invalid name: " + str, e2);
            }
        }

        String getName() {
            return this.name;
        }

        void addRequestedAttribute(String str) {
            if (this.valueMap != null) {
                throw new IllegalStateException("Request already sent");
            }
            this.requestedAttributes.add(str);
        }

        byte[][] getValues(String str) throws NamingException {
            byte[][] bArr = (byte[][]) LDAPCertStore.this.valueCache.get(this.name + CallSiteDescriptor.OPERATOR_DELIMITER + str);
            if (bArr != null) {
                LDAPCertStore.access$108(LDAPCertStore.this);
                return bArr;
            }
            LDAPCertStore.access$208(LDAPCertStore.this);
            return getValueMap().get(str);
        }

        private Map<String, byte[][]> getValueMap() throws NamingException {
            Attributes attributes;
            if (this.valueMap != null) {
                return this.valueMap;
            }
            this.valueMap = new HashMap(8);
            String[] strArr = (String[]) this.requestedAttributes.toArray(LDAPCertStore.STRING0);
            try {
                attributes = LDAPCertStore.this.ctx.getAttributes(this.name, strArr);
            } catch (LdapReferralException e2) {
                e = e2;
                while (true) {
                    try {
                        URI uri = new URI((String) e.getReferralInfo());
                        if (!uri.getScheme().equalsIgnoreCase("ldap")) {
                            throw new IllegalArgumentException("Not LDAP");
                        }
                        String path = uri.getPath();
                        if (path != null && path.charAt(0) == '/') {
                            path = path.substring(1);
                        }
                        checkName(path);
                        LdapContext ldapContext = (LdapContext) e.getReferralContext();
                        try {
                            attributes = ldapContext.getAttributes(this.name, strArr);
                            ldapContext.close();
                            break;
                        } catch (LdapReferralException e3) {
                            e = e3;
                            ldapContext.close();
                        } catch (Throwable th) {
                            ldapContext.close();
                            throw th;
                        }
                    } catch (Exception e4) {
                        throw new NamingException("Cannot follow referral to " + e.getReferralInfo());
                    }
                }
            } catch (NameNotFoundException e5) {
                attributes = LDAPCertStore.EMPTY_ATTRIBUTES;
            }
            for (String str : this.requestedAttributes) {
                byte[][] attributeValues = getAttributeValues(attributes.get(str));
                cacheAttribute(str, attributeValues);
                this.valueMap.put(str, attributeValues);
            }
            return this.valueMap;
        }

        private void cacheAttribute(String str, byte[][] bArr) {
            LDAPCertStore.this.valueCache.put(this.name + CallSiteDescriptor.OPERATOR_DELIMITER + str, bArr);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r6v0 */
        /* JADX WARN: Type inference failed for: r6v1 */
        /* JADX WARN: Type inference failed for: r6v2 */
        private byte[][] getAttributeValues(Attribute attribute) throws NamingException {
            byte[][] bArr;
            if (attribute == null) {
                bArr = LDAPCertStore.BB0;
            } else {
                bArr = new byte[attribute.size()];
                int i2 = 0;
                NamingEnumeration<?> all = attribute.getAll();
                while (all.hasMore()) {
                    Object next = all.next();
                    if (LDAPCertStore.debug != null && (next instanceof String)) {
                        LDAPCertStore.debug.println("LDAPCertStore.getAttrValues() enum.next is a string!: " + next);
                    }
                    int i3 = i2;
                    i2++;
                    bArr[i3] = (byte[]) next;
                }
            }
            return bArr;
        }
    }

    private Collection<X509Certificate> getCertificates(LDAPRequest lDAPRequest, String str, X509CertSelector x509CertSelector) throws CertStoreException {
        try {
            byte[][] values = lDAPRequest.getValues(str);
            int length = values.length;
            if (length == 0) {
                return Collections.emptySet();
            }
            ArrayList arrayList = new ArrayList(length);
            for (int i2 = 0; i2 < length; i2++) {
                try {
                    Certificate certificateGenerateCertificate = this.cf.generateCertificate(new ByteArrayInputStream(values[i2]));
                    if (x509CertSelector.match(certificateGenerateCertificate)) {
                        arrayList.add((X509Certificate) certificateGenerateCertificate);
                    }
                } catch (CertificateException e2) {
                    if (debug != null) {
                        debug.println("LDAPCertStore.getCertificates() encountered exception while parsing cert, skipping the bad data: ");
                        debug.println("[ " + new HexDumpEncoder().encodeBuffer(values[i2]) + " ]");
                    }
                }
            }
            return arrayList;
        } catch (NamingException e3) {
            throw new CertStoreException(e3);
        }
    }

    private Collection<X509CertificatePair> getCertPairs(LDAPRequest lDAPRequest, String str) throws CertStoreException {
        try {
            byte[][] values = lDAPRequest.getValues(str);
            int length = values.length;
            if (length == 0) {
                return Collections.emptySet();
            }
            ArrayList arrayList = new ArrayList(length);
            for (int i2 = 0; i2 < length; i2++) {
                try {
                    arrayList.add(X509CertificatePair.generateCertificatePair(values[i2]));
                } catch (CertificateException e2) {
                    if (debug != null) {
                        debug.println("LDAPCertStore.getCertPairs() encountered exception while parsing cert, skipping the bad data: ");
                        debug.println("[ " + new HexDumpEncoder().encodeBuffer(values[i2]) + " ]");
                    }
                }
            }
            return arrayList;
        } catch (NamingException e3) {
            throw new CertStoreException(e3);
        }
    }

    private Collection<X509Certificate> getMatchingCrossCerts(LDAPRequest lDAPRequest, X509CertSelector x509CertSelector, X509CertSelector x509CertSelector2) throws CertStoreException {
        X509Certificate reverse;
        X509Certificate forward;
        Collection<X509CertificatePair> certPairs = getCertPairs(lDAPRequest, CROSS_CERT);
        ArrayList arrayList = new ArrayList();
        for (X509CertificatePair x509CertificatePair : certPairs) {
            if (x509CertSelector != null && (forward = x509CertificatePair.getForward()) != null && x509CertSelector.match(forward)) {
                arrayList.add(forward);
            }
            if (x509CertSelector2 != null && (reverse = x509CertificatePair.getReverse()) != null && x509CertSelector2.match(reverse)) {
                arrayList.add(reverse);
            }
        }
        return arrayList;
    }

    @Override // java.security.cert.CertStoreSpi
    public synchronized Collection<X509Certificate> engineGetCertificates(CertSelector certSelector) throws CertStoreException {
        if (debug != null) {
            debug.println("LDAPCertStore.engineGetCertificates() selector: " + String.valueOf(certSelector));
        }
        if (certSelector == null) {
            certSelector = new X509CertSelector();
        }
        if (!(certSelector instanceof X509CertSelector)) {
            throw new CertStoreException("LDAPCertStore needs an X509CertSelector to find certs");
        }
        X509CertSelector x509CertSelector = (X509CertSelector) certSelector;
        int basicConstraints = x509CertSelector.getBasicConstraints();
        String subjectAsString = x509CertSelector.getSubjectAsString();
        String issuerAsString = x509CertSelector.getIssuerAsString();
        HashSet hashSet = new HashSet();
        if (debug != null) {
            debug.println("LDAPCertStore.engineGetCertificates() basicConstraints: " + basicConstraints);
        }
        if (subjectAsString != null) {
            if (debug != null) {
                debug.println("LDAPCertStore.engineGetCertificates() subject is not null");
            }
            LDAPRequest lDAPRequest = new LDAPRequest(subjectAsString);
            if (basicConstraints > -2) {
                lDAPRequest.addRequestedAttribute(CROSS_CERT);
                lDAPRequest.addRequestedAttribute(CA_CERT);
                lDAPRequest.addRequestedAttribute(ARL);
                if (this.prefetchCRLs) {
                    lDAPRequest.addRequestedAttribute(CRL);
                }
            }
            if (basicConstraints < 0) {
                lDAPRequest.addRequestedAttribute(USER_CERT);
            }
            if (basicConstraints > -2) {
                hashSet.addAll(getMatchingCrossCerts(lDAPRequest, x509CertSelector, null));
                if (debug != null) {
                    debug.println("LDAPCertStore.engineGetCertificates() after getMatchingCrossCerts(subject,xsel,null),certs.size(): " + hashSet.size());
                }
                hashSet.addAll(getCertificates(lDAPRequest, CA_CERT, x509CertSelector));
                if (debug != null) {
                    debug.println("LDAPCertStore.engineGetCertificates() after getCertificates(subject,CA_CERT,xsel),certs.size(): " + hashSet.size());
                }
            }
            if (basicConstraints < 0) {
                hashSet.addAll(getCertificates(lDAPRequest, USER_CERT, x509CertSelector));
                if (debug != null) {
                    debug.println("LDAPCertStore.engineGetCertificates() after getCertificates(subject,USER_CERT, xsel),certs.size(): " + hashSet.size());
                }
            }
        } else {
            if (debug != null) {
                debug.println("LDAPCertStore.engineGetCertificates() subject is null");
            }
            if (basicConstraints == -2) {
                throw new CertStoreException("need subject to find EE certs");
            }
            if (issuerAsString == null) {
                throw new CertStoreException("need subject or issuer to find certs");
            }
        }
        if (debug != null) {
            debug.println("LDAPCertStore.engineGetCertificates() about to getMatchingCrossCerts...");
        }
        if (issuerAsString != null && basicConstraints > -2) {
            LDAPRequest lDAPRequest2 = new LDAPRequest(issuerAsString);
            lDAPRequest2.addRequestedAttribute(CROSS_CERT);
            lDAPRequest2.addRequestedAttribute(CA_CERT);
            lDAPRequest2.addRequestedAttribute(ARL);
            if (this.prefetchCRLs) {
                lDAPRequest2.addRequestedAttribute(CRL);
            }
            hashSet.addAll(getMatchingCrossCerts(lDAPRequest2, null, x509CertSelector));
            if (debug != null) {
                debug.println("LDAPCertStore.engineGetCertificates() after getMatchingCrossCerts(issuer,null,xsel),certs.size(): " + hashSet.size());
            }
            hashSet.addAll(getCertificates(lDAPRequest2, CA_CERT, x509CertSelector));
            if (debug != null) {
                debug.println("LDAPCertStore.engineGetCertificates() after getCertificates(issuer,CA_CERT,xsel),certs.size(): " + hashSet.size());
            }
        }
        if (debug != null) {
            debug.println("LDAPCertStore.engineGetCertificates() returning certs");
        }
        return hashSet;
    }

    private Collection<X509CRL> getCRLs(LDAPRequest lDAPRequest, String str, X509CRLSelector x509CRLSelector) throws CertStoreException {
        try {
            byte[][] values = lDAPRequest.getValues(str);
            int length = values.length;
            if (length == 0) {
                return Collections.emptySet();
            }
            ArrayList arrayList = new ArrayList(length);
            for (int i2 = 0; i2 < length; i2++) {
                try {
                    CRL crlGenerateCRL = this.cf.generateCRL(new ByteArrayInputStream(values[i2]));
                    if (x509CRLSelector.match(crlGenerateCRL)) {
                        arrayList.add((X509CRL) crlGenerateCRL);
                    }
                } catch (CRLException e2) {
                    if (debug != null) {
                        debug.println("LDAPCertStore.getCRLs() encountered exception while parsing CRL, skipping the bad data: ");
                        debug.println("[ " + new HexDumpEncoder().encodeBuffer(values[i2]) + " ]");
                    }
                }
            }
            return arrayList;
        } catch (NamingException e3) {
            throw new CertStoreException(e3);
        }
    }

    @Override // java.security.cert.CertStoreSpi
    public synchronized Collection<X509CRL> engineGetCRLs(CRLSelector cRLSelector) throws CertStoreException {
        Collection<Object> issuerNames;
        String name;
        if (debug != null) {
            debug.println("LDAPCertStore.engineGetCRLs() selector: " + ((Object) cRLSelector));
        }
        if (cRLSelector == null) {
            cRLSelector = new X509CRLSelector();
        }
        if (!(cRLSelector instanceof X509CRLSelector)) {
            throw new CertStoreException("need X509CRLSelector to find CRLs");
        }
        X509CRLSelector x509CRLSelector = (X509CRLSelector) cRLSelector;
        HashSet hashSet = new HashSet();
        X509Certificate certificateChecking = x509CRLSelector.getCertificateChecking();
        if (certificateChecking != null) {
            issuerNames = new HashSet();
            issuerNames.add(certificateChecking.getIssuerX500Principal().getName(X500Principal.RFC2253));
        } else {
            issuerNames = x509CRLSelector.getIssuerNames();
            if (issuerNames == null) {
                throw new CertStoreException("need issuerNames or certChecking to find CRLs");
            }
        }
        for (Object obj : issuerNames) {
            if (obj instanceof byte[]) {
                try {
                    name = new X500Principal((byte[]) obj).getName(X500Principal.RFC2253);
                } catch (IllegalArgumentException e2) {
                }
            } else {
                name = (String) obj;
            }
            Collection<X509CRL> collectionEmptySet = Collections.emptySet();
            if (certificateChecking == null || certificateChecking.getBasicConstraints() != -1) {
                LDAPRequest lDAPRequest = new LDAPRequest(name);
                lDAPRequest.addRequestedAttribute(CROSS_CERT);
                lDAPRequest.addRequestedAttribute(CA_CERT);
                lDAPRequest.addRequestedAttribute(ARL);
                if (this.prefetchCRLs) {
                    lDAPRequest.addRequestedAttribute(CRL);
                }
                try {
                    collectionEmptySet = getCRLs(lDAPRequest, ARL, x509CRLSelector);
                    if (collectionEmptySet.isEmpty()) {
                        this.prefetchCRLs = true;
                    } else {
                        hashSet.addAll(collectionEmptySet);
                    }
                } catch (CertStoreException e3) {
                    if (debug != null) {
                        debug.println("LDAPCertStore.engineGetCRLs non-fatal error retrieving ARLs:" + ((Object) e3));
                        e3.printStackTrace();
                    }
                }
            }
            if (collectionEmptySet.isEmpty() || certificateChecking == null) {
                LDAPRequest lDAPRequest2 = new LDAPRequest(name);
                lDAPRequest2.addRequestedAttribute(CRL);
                hashSet.addAll(getCRLs(lDAPRequest2, CRL, x509CRLSelector));
            }
        }
        return hashSet;
    }

    static LDAPCertStoreParameters getParameters(URI uri) {
        String host = uri.getHost();
        if (host == null) {
            return new SunLDAPCertStoreParameters();
        }
        int port = uri.getPort();
        return port == -1 ? new SunLDAPCertStoreParameters(host) : new SunLDAPCertStoreParameters(host, port);
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/ldap/LDAPCertStore$SunLDAPCertStoreParameters.class */
    private static class SunLDAPCertStoreParameters extends LDAPCertStoreParameters {
        private volatile int hashCode;

        SunLDAPCertStoreParameters(String str, int i2) {
            super(str, i2);
            this.hashCode = 0;
        }

        SunLDAPCertStoreParameters(String str) {
            super(str);
            this.hashCode = 0;
        }

        SunLDAPCertStoreParameters() {
            this.hashCode = 0;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof LDAPCertStoreParameters)) {
                return false;
            }
            LDAPCertStoreParameters lDAPCertStoreParameters = (LDAPCertStoreParameters) obj;
            return getPort() == lDAPCertStoreParameters.getPort() && getServerName().equalsIgnoreCase(lDAPCertStoreParameters.getServerName());
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                this.hashCode = (37 * ((37 * 17) + getPort())) + getServerName().toLowerCase(Locale.ENGLISH).hashCode();
            }
            return this.hashCode;
        }
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/ldap/LDAPCertStore$LDAPCertSelector.class */
    static class LDAPCertSelector extends X509CertSelector {
        private X500Principal certSubject;
        private X509CertSelector selector;
        private X500Principal subject;

        LDAPCertSelector(X509CertSelector x509CertSelector, X500Principal x500Principal, String str) throws IOException {
            this.selector = x509CertSelector == null ? new X509CertSelector() : x509CertSelector;
            this.certSubject = x500Principal;
            this.subject = new X500Name(str).asX500Principal();
        }

        @Override // java.security.cert.X509CertSelector
        public X509Certificate getCertificate() {
            return this.selector.getCertificate();
        }

        @Override // java.security.cert.X509CertSelector
        public BigInteger getSerialNumber() {
            return this.selector.getSerialNumber();
        }

        @Override // java.security.cert.X509CertSelector
        public X500Principal getIssuer() {
            return this.selector.getIssuer();
        }

        @Override // java.security.cert.X509CertSelector
        public String getIssuerAsString() {
            return this.selector.getIssuerAsString();
        }

        @Override // java.security.cert.X509CertSelector
        public byte[] getIssuerAsBytes() throws IOException {
            return this.selector.getIssuerAsBytes();
        }

        @Override // java.security.cert.X509CertSelector
        public X500Principal getSubject() {
            return this.subject;
        }

        @Override // java.security.cert.X509CertSelector
        public String getSubjectAsString() {
            return this.subject.getName();
        }

        @Override // java.security.cert.X509CertSelector
        public byte[] getSubjectAsBytes() throws IOException {
            return this.subject.getEncoded();
        }

        @Override // java.security.cert.X509CertSelector
        public byte[] getSubjectKeyIdentifier() {
            return this.selector.getSubjectKeyIdentifier();
        }

        @Override // java.security.cert.X509CertSelector
        public byte[] getAuthorityKeyIdentifier() {
            return this.selector.getAuthorityKeyIdentifier();
        }

        @Override // java.security.cert.X509CertSelector
        public Date getCertificateValid() {
            return this.selector.getCertificateValid();
        }

        @Override // java.security.cert.X509CertSelector
        public Date getPrivateKeyValid() {
            return this.selector.getPrivateKeyValid();
        }

        @Override // java.security.cert.X509CertSelector
        public String getSubjectPublicKeyAlgID() {
            return this.selector.getSubjectPublicKeyAlgID();
        }

        @Override // java.security.cert.X509CertSelector
        public PublicKey getSubjectPublicKey() {
            return this.selector.getSubjectPublicKey();
        }

        @Override // java.security.cert.X509CertSelector
        public boolean[] getKeyUsage() {
            return this.selector.getKeyUsage();
        }

        @Override // java.security.cert.X509CertSelector
        public Set<String> getExtendedKeyUsage() {
            return this.selector.getExtendedKeyUsage();
        }

        @Override // java.security.cert.X509CertSelector
        public boolean getMatchAllSubjectAltNames() {
            return this.selector.getMatchAllSubjectAltNames();
        }

        @Override // java.security.cert.X509CertSelector
        public Collection<List<?>> getSubjectAlternativeNames() {
            return this.selector.getSubjectAlternativeNames();
        }

        @Override // java.security.cert.X509CertSelector
        public byte[] getNameConstraints() {
            return this.selector.getNameConstraints();
        }

        @Override // java.security.cert.X509CertSelector
        public int getBasicConstraints() {
            return this.selector.getBasicConstraints();
        }

        @Override // java.security.cert.X509CertSelector
        public Set<String> getPolicy() {
            return this.selector.getPolicy();
        }

        @Override // java.security.cert.X509CertSelector
        public Collection<List<?>> getPathToNames() {
            return this.selector.getPathToNames();
        }

        @Override // java.security.cert.X509CertSelector, java.security.cert.CertSelector
        public boolean match(Certificate certificate) {
            this.selector.setSubject(this.certSubject);
            boolean zMatch = this.selector.match(certificate);
            this.selector.setSubject(this.subject);
            return zMatch;
        }
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/ldap/LDAPCertStore$LDAPCRLSelector.class */
    static class LDAPCRLSelector extends X509CRLSelector {
        private X509CRLSelector selector;
        private Collection<X500Principal> certIssuers;
        private Collection<X500Principal> issuers;
        private HashSet<Object> issuerNames;

        LDAPCRLSelector(X509CRLSelector x509CRLSelector, Collection<X500Principal> collection, String str) throws IOException {
            this.selector = x509CRLSelector == null ? new X509CRLSelector() : x509CRLSelector;
            this.certIssuers = collection;
            this.issuerNames = new HashSet<>();
            this.issuerNames.add(str);
            this.issuers = new HashSet();
            this.issuers.add(new X500Name(str).asX500Principal());
        }

        @Override // java.security.cert.X509CRLSelector
        public Collection<X500Principal> getIssuers() {
            return Collections.unmodifiableCollection(this.issuers);
        }

        @Override // java.security.cert.X509CRLSelector
        public Collection<Object> getIssuerNames() {
            return Collections.unmodifiableCollection(this.issuerNames);
        }

        @Override // java.security.cert.X509CRLSelector
        public BigInteger getMinCRL() {
            return this.selector.getMinCRL();
        }

        @Override // java.security.cert.X509CRLSelector
        public BigInteger getMaxCRL() {
            return this.selector.getMaxCRL();
        }

        @Override // java.security.cert.X509CRLSelector
        public Date getDateAndTime() {
            return this.selector.getDateAndTime();
        }

        @Override // java.security.cert.X509CRLSelector
        public X509Certificate getCertificateChecking() {
            return this.selector.getCertificateChecking();
        }

        @Override // java.security.cert.X509CRLSelector, java.security.cert.CRLSelector
        public boolean match(CRL crl) {
            this.selector.setIssuers(this.certIssuers);
            boolean zMatch = this.selector.match(crl);
            this.selector.setIssuers(this.issuers);
            return zMatch;
        }
    }
}
