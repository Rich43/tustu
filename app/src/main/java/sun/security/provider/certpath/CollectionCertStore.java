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
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;

/* loaded from: rt.jar:sun/security/provider/certpath/CollectionCertStore.class */
public class CollectionCertStore extends CertStoreSpi {
    private Collection<?> coll;

    public CollectionCertStore(CertStoreParameters certStoreParameters) throws InvalidAlgorithmParameterException {
        super(certStoreParameters);
        if (!(certStoreParameters instanceof CollectionCertStoreParameters)) {
            throw new InvalidAlgorithmParameterException("parameters must be CollectionCertStoreParameters");
        }
        this.coll = ((CollectionCertStoreParameters) certStoreParameters).getCollection();
    }

    @Override // java.security.cert.CertStoreSpi
    public Collection<Certificate> engineGetCertificates(CertSelector certSelector) throws CertStoreException {
        if (this.coll == null) {
            throw new CertStoreException("Collection is null");
        }
        for (int i2 = 0; i2 < 10; i2++) {
            try {
                HashSet hashSet = new HashSet();
                if (certSelector != null) {
                    for (Object obj : this.coll) {
                        if ((obj instanceof Certificate) && certSelector.match((Certificate) obj)) {
                            hashSet.add((Certificate) obj);
                        }
                    }
                } else {
                    for (Object obj2 : this.coll) {
                        if (obj2 instanceof Certificate) {
                            hashSet.add((Certificate) obj2);
                        }
                    }
                }
                return hashSet;
            } catch (ConcurrentModificationException e2) {
            }
        }
        throw new ConcurrentModificationException("Too many ConcurrentModificationExceptions");
    }

    @Override // java.security.cert.CertStoreSpi
    public Collection<CRL> engineGetCRLs(CRLSelector cRLSelector) throws CertStoreException {
        if (this.coll == null) {
            throw new CertStoreException("Collection is null");
        }
        for (int i2 = 0; i2 < 10; i2++) {
            try {
                HashSet hashSet = new HashSet();
                if (cRLSelector != null) {
                    for (Object obj : this.coll) {
                        if ((obj instanceof CRL) && cRLSelector.match((CRL) obj)) {
                            hashSet.add((CRL) obj);
                        }
                    }
                } else {
                    for (Object obj2 : this.coll) {
                        if (obj2 instanceof CRL) {
                            hashSet.add((CRL) obj2);
                        }
                    }
                }
                return hashSet;
            } catch (ConcurrentModificationException e2) {
            }
        }
        throw new ConcurrentModificationException("Too many ConcurrentModificationExceptions");
    }
}
