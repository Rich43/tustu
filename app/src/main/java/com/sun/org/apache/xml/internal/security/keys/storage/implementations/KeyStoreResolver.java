package com.sun.org.apache.xml.internal.security.keys.storage.implementations;

import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverException;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/storage/implementations/KeyStoreResolver.class */
public class KeyStoreResolver extends StorageResolverSpi {
    private KeyStore keyStore;

    public KeyStoreResolver(KeyStore keyStore) throws StorageResolverException {
        this.keyStore = keyStore;
        try {
            keyStore.aliases();
        } catch (KeyStoreException e2) {
            throw new StorageResolverException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi
    public Iterator<Certificate> getIterator() {
        return new KeyStoreIterator(this.keyStore);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/storage/implementations/KeyStoreResolver$KeyStoreIterator.class */
    static class KeyStoreIterator implements Iterator<Certificate> {
        KeyStore keyStore;
        Enumeration<String> aliases;
        Certificate nextCert = null;

        public KeyStoreIterator(KeyStore keyStore) {
            this.keyStore = null;
            this.aliases = null;
            try {
                this.keyStore = keyStore;
                this.aliases = this.keyStore.aliases();
            } catch (KeyStoreException e2) {
                this.aliases = new Enumeration<String>() { // from class: com.sun.org.apache.xml.internal.security.keys.storage.implementations.KeyStoreResolver.KeyStoreIterator.1
                    @Override // java.util.Enumeration
                    public boolean hasMoreElements() {
                        return false;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Enumeration
                    /* renamed from: nextElement */
                    public String nextElement2() {
                        return null;
                    }
                };
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextCert == null) {
                this.nextCert = findNextCert();
            }
            return this.nextCert != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Certificate next() {
            if (this.nextCert == null) {
                this.nextCert = findNextCert();
                if (this.nextCert == null) {
                    throw new NoSuchElementException();
                }
            }
            Certificate certificate = this.nextCert;
            this.nextCert = null;
            return certificate;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Can't remove keys from KeyStore");
        }

        private Certificate findNextCert() {
            while (this.aliases.hasMoreElements()) {
                try {
                    Certificate certificate = this.keyStore.getCertificate(this.aliases.nextElement2());
                    if (certificate != null) {
                        return certificate;
                    }
                } catch (KeyStoreException e2) {
                    return null;
                }
            }
            return null;
        }
    }
}
