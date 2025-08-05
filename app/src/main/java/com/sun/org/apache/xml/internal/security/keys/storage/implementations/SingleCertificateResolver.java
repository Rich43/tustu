package com.sun.org.apache.xml.internal.security.keys.storage.implementations;

import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/storage/implementations/SingleCertificateResolver.class */
public class SingleCertificateResolver extends StorageResolverSpi {
    private X509Certificate certificate;

    public SingleCertificateResolver(X509Certificate x509Certificate) {
        this.certificate = x509Certificate;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi
    public Iterator<Certificate> getIterator() {
        return new InternalIterator(this.certificate);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/storage/implementations/SingleCertificateResolver$InternalIterator.class */
    static class InternalIterator implements Iterator<Certificate> {
        boolean alreadyReturned = false;
        X509Certificate certificate;

        public InternalIterator(X509Certificate x509Certificate) {
            this.certificate = null;
            this.certificate = x509Certificate;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.alreadyReturned;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Certificate next() {
            if (this.alreadyReturned) {
                throw new NoSuchElementException();
            }
            this.alreadyReturned = true;
            return this.certificate;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Can't remove keys from KeyStore");
        }
    }
}
