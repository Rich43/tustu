package com.sun.org.apache.xml.internal.security.keys.storage;

import com.sun.org.apache.xml.internal.security.keys.storage.implementations.KeyStoreResolver;
import com.sun.org.apache.xml.internal.security.keys.storage.implementations.SingleCertificateResolver;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/storage/StorageResolver.class */
public class StorageResolver {
    private static final Logger LOG = LoggerFactory.getLogger(StorageResolver.class);
    private List<StorageResolverSpi> storageResolvers;

    public StorageResolver() {
    }

    public StorageResolver(StorageResolverSpi storageResolverSpi) {
        add(storageResolverSpi);
    }

    public void add(StorageResolverSpi storageResolverSpi) {
        if (this.storageResolvers == null) {
            this.storageResolvers = new ArrayList();
        }
        this.storageResolvers.add(storageResolverSpi);
    }

    public StorageResolver(KeyStore keyStore) {
        add(keyStore);
    }

    public void add(KeyStore keyStore) {
        try {
            add(new KeyStoreResolver(keyStore));
        } catch (StorageResolverException e2) {
            LOG.error("Could not add KeyStore because of: ", e2);
        }
    }

    public StorageResolver(X509Certificate x509Certificate) {
        add(x509Certificate);
    }

    public void add(X509Certificate x509Certificate) {
        add(new SingleCertificateResolver(x509Certificate));
    }

    public Iterator<Certificate> getIterator() {
        return new StorageResolverIterator(this.storageResolvers.iterator());
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/storage/StorageResolver$StorageResolverIterator.class */
    static class StorageResolverIterator implements Iterator<Certificate> {
        Iterator<StorageResolverSpi> resolvers;
        Iterator<Certificate> currentResolver;

        public StorageResolverIterator(Iterator<StorageResolverSpi> it) {
            this.resolvers = null;
            this.currentResolver = null;
            this.resolvers = it;
            this.currentResolver = findNextResolver();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.currentResolver == null) {
                return false;
            }
            if (this.currentResolver.hasNext()) {
                return true;
            }
            this.currentResolver = findNextResolver();
            return this.currentResolver != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Certificate next() {
            if (hasNext()) {
                return this.currentResolver.next();
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Can't remove keys from KeyStore");
        }

        private Iterator<Certificate> findNextResolver() {
            while (this.resolvers.hasNext()) {
                Iterator<Certificate> iterator = this.resolvers.next().getIterator();
                if (iterator.hasNext()) {
                    return iterator;
                }
            }
            return null;
        }
    }
}
