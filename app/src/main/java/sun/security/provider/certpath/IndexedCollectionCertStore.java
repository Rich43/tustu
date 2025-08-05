package sun.security.provider.certpath;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;

/* loaded from: rt.jar:sun/security/provider/certpath/IndexedCollectionCertStore.class */
public class IndexedCollectionCertStore extends CertStoreSpi {
    private Map<X500Principal, Object> certSubjects;
    private Map<X500Principal, Object> crlIssuers;
    private Set<Certificate> otherCertificates;
    private Set<CRL> otherCRLs;

    public IndexedCollectionCertStore(CertStoreParameters certStoreParameters) throws InvalidAlgorithmParameterException {
        super(certStoreParameters);
        if (!(certStoreParameters instanceof CollectionCertStoreParameters)) {
            throw new InvalidAlgorithmParameterException("parameters must be CollectionCertStoreParameters");
        }
        Collection<?> collection = ((CollectionCertStoreParameters) certStoreParameters).getCollection();
        if (collection == null) {
            throw new InvalidAlgorithmParameterException("Collection must not be null");
        }
        buildIndex(collection);
    }

    private void buildIndex(Collection<?> collection) {
        this.certSubjects = new HashMap();
        this.crlIssuers = new HashMap();
        this.otherCertificates = null;
        this.otherCRLs = null;
        for (Object obj : collection) {
            if (obj instanceof X509Certificate) {
                indexCertificate((X509Certificate) obj);
            } else if (obj instanceof X509CRL) {
                indexCRL((X509CRL) obj);
            } else if (obj instanceof Certificate) {
                if (this.otherCertificates == null) {
                    this.otherCertificates = new HashSet();
                }
                this.otherCertificates.add((Certificate) obj);
            } else if (obj instanceof CRL) {
                if (this.otherCRLs == null) {
                    this.otherCRLs = new HashSet();
                }
                this.otherCRLs.add((CRL) obj);
            }
        }
        if (this.otherCertificates == null) {
            this.otherCertificates = Collections.emptySet();
        }
        if (this.otherCRLs == null) {
            this.otherCRLs = Collections.emptySet();
        }
    }

    private void indexCertificate(X509Certificate x509Certificate) {
        X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
        Object objPut = this.certSubjects.put(subjectX500Principal, x509Certificate);
        if (objPut != null) {
            if (objPut instanceof X509Certificate) {
                if (x509Certificate.equals(objPut)) {
                    return;
                }
                ArrayList arrayList = new ArrayList(2);
                arrayList.add(x509Certificate);
                arrayList.add((X509Certificate) objPut);
                this.certSubjects.put(subjectX500Principal, arrayList);
                return;
            }
            List list = (List) objPut;
            if (!list.contains(x509Certificate)) {
                list.add(x509Certificate);
            }
            this.certSubjects.put(subjectX500Principal, list);
        }
    }

    private void indexCRL(X509CRL x509crl) {
        X500Principal issuerX500Principal = x509crl.getIssuerX500Principal();
        Object objPut = this.crlIssuers.put(issuerX500Principal, x509crl);
        if (objPut != null) {
            if (objPut instanceof X509CRL) {
                if (x509crl.equals(objPut)) {
                    return;
                }
                ArrayList arrayList = new ArrayList(2);
                arrayList.add(x509crl);
                arrayList.add((X509CRL) objPut);
                this.crlIssuers.put(issuerX500Principal, arrayList);
                return;
            }
            List list = (List) objPut;
            if (!list.contains(x509crl)) {
                list.add(x509crl);
            }
            this.crlIssuers.put(issuerX500Principal, list);
        }
    }

    @Override // java.security.cert.CertStoreSpi
    public Collection<? extends Certificate> engineGetCertificates(CertSelector certSelector) throws CertStoreException {
        X500Principal subject;
        if (certSelector == null) {
            Collection<Certificate> hashSet = new HashSet<>();
            matchX509Certs(new X509CertSelector(), hashSet);
            hashSet.addAll(this.otherCertificates);
            return hashSet;
        }
        if (!(certSelector instanceof X509CertSelector)) {
            HashSet hashSet2 = new HashSet();
            matchX509Certs(certSelector, hashSet2);
            for (Certificate certificate : this.otherCertificates) {
                if (certSelector.match(certificate)) {
                    hashSet2.add(certificate);
                }
            }
            return hashSet2;
        }
        if (this.certSubjects.isEmpty()) {
            return Collections.emptySet();
        }
        X509CertSelector x509CertSelector = (X509CertSelector) certSelector;
        X509Certificate certificate2 = x509CertSelector.getCertificate();
        if (certificate2 != null) {
            subject = certificate2.getSubjectX500Principal();
        } else {
            subject = x509CertSelector.getSubject();
        }
        if (subject != null) {
            Object obj = this.certSubjects.get(subject);
            if (obj == null) {
                return Collections.emptySet();
            }
            if (obj instanceof X509Certificate) {
                X509Certificate x509Certificate = (X509Certificate) obj;
                if (x509CertSelector.match(x509Certificate)) {
                    return Collections.singleton(x509Certificate);
                }
                return Collections.emptySet();
            }
            List<X509Certificate> list = (List) obj;
            HashSet hashSet3 = new HashSet(16);
            for (X509Certificate x509Certificate2 : list) {
                if (x509CertSelector.match(x509Certificate2)) {
                    hashSet3.add(x509Certificate2);
                }
            }
            return hashSet3;
        }
        Collection<Certificate> hashSet4 = new HashSet<>(16);
        matchX509Certs(x509CertSelector, hashSet4);
        return hashSet4;
    }

    private void matchX509Certs(CertSelector certSelector, Collection<Certificate> collection) {
        for (Object obj : this.certSubjects.values()) {
            if (obj instanceof X509Certificate) {
                X509Certificate x509Certificate = (X509Certificate) obj;
                if (certSelector.match(x509Certificate)) {
                    collection.add(x509Certificate);
                }
            } else {
                for (X509Certificate x509Certificate2 : (List) obj) {
                    if (certSelector.match(x509Certificate2)) {
                        collection.add(x509Certificate2);
                    }
                }
            }
        }
    }

    @Override // java.security.cert.CertStoreSpi
    public Collection<CRL> engineGetCRLs(CRLSelector cRLSelector) throws CertStoreException {
        if (cRLSelector == null) {
            Collection<CRL> hashSet = new HashSet<>();
            matchX509CRLs(new X509CRLSelector(), hashSet);
            hashSet.addAll(this.otherCRLs);
            return hashSet;
        }
        if (!(cRLSelector instanceof X509CRLSelector)) {
            HashSet hashSet2 = new HashSet();
            matchX509CRLs(cRLSelector, hashSet2);
            for (CRL crl : this.otherCRLs) {
                if (cRLSelector.match(crl)) {
                    hashSet2.add(crl);
                }
            }
            return hashSet2;
        }
        if (this.crlIssuers.isEmpty()) {
            return Collections.emptySet();
        }
        X509CRLSelector x509CRLSelector = (X509CRLSelector) cRLSelector;
        Collection<X500Principal> issuers = x509CRLSelector.getIssuers();
        if (issuers != null) {
            HashSet hashSet3 = new HashSet(16);
            Iterator<X500Principal> it = issuers.iterator();
            while (it.hasNext()) {
                Object obj = this.crlIssuers.get(it.next());
                if (obj != null) {
                    if (obj instanceof X509CRL) {
                        X509CRL x509crl = (X509CRL) obj;
                        if (x509CRLSelector.match(x509crl)) {
                            hashSet3.add(x509crl);
                        }
                    } else {
                        for (X509CRL x509crl2 : (List) obj) {
                            if (x509CRLSelector.match(x509crl2)) {
                                hashSet3.add(x509crl2);
                            }
                        }
                    }
                }
            }
            return hashSet3;
        }
        Collection<CRL> hashSet4 = new HashSet<>(16);
        matchX509CRLs(x509CRLSelector, hashSet4);
        return hashSet4;
    }

    private void matchX509CRLs(CRLSelector cRLSelector, Collection<CRL> collection) {
        for (Object obj : this.crlIssuers.values()) {
            if (obj instanceof X509CRL) {
                X509CRL x509crl = (X509CRL) obj;
                if (cRLSelector.match(x509crl)) {
                    collection.add(x509crl);
                }
            } else {
                for (X509CRL x509crl2 : (List) obj) {
                    if (cRLSelector.match(x509crl2)) {
                        collection.add(x509crl2);
                    }
                }
            }
        }
    }
}
