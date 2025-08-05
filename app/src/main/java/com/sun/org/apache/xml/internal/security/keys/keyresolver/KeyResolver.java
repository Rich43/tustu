package com.sun.org.apache.xml.internal.security.keys.keyresolver;

import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.DEREncodedKeyValueResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.DSAKeyValueResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.ECKeyValueResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.KeyInfoReferenceResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.RSAKeyValueResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.RetrievalMethodResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509CertificateResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509DigestResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509IssuerSerialResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509SKIResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509SubjectNameResolver;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.fxml.FXMLLoader;
import javax.crypto.SecretKey;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/KeyResolver.class */
public class KeyResolver {
    private static final Logger LOG = LoggerFactory.getLogger(KeyResolver.class);
    private static List<KeyResolver> resolverVector = new CopyOnWriteArrayList();
    private final KeyResolverSpi resolverSpi;

    private KeyResolver(KeyResolverSpi keyResolverSpi) {
        this.resolverSpi = keyResolverSpi;
    }

    public static int length() {
        return resolverVector.size();
    }

    public static final X509Certificate getX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        for (KeyResolver keyResolver : resolverVector) {
            if (keyResolver == null) {
                Object[] objArr = new Object[1];
                objArr[0] = (element == null || element.getNodeType() != 1) ? FXMLLoader.NULL_KEYWORD : element.getTagName();
                throw new KeyResolverException("utils.resolver.noClass", objArr);
            }
            LOG.debug("check resolvability by class {}", keyResolver.getClass());
            X509Certificate x509CertificateResolveX509Certificate = keyResolver.resolveX509Certificate(element, str, storageResolver);
            if (x509CertificateResolveX509Certificate != null) {
                return x509CertificateResolveX509Certificate;
            }
        }
        Object[] objArr2 = new Object[1];
        objArr2[0] = (element == null || element.getNodeType() != 1) ? FXMLLoader.NULL_KEYWORD : element.getTagName();
        throw new KeyResolverException("utils.resolver.noClass", objArr2);
    }

    public static final PublicKey getPublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        for (KeyResolver keyResolver : resolverVector) {
            if (keyResolver == null) {
                Object[] objArr = new Object[1];
                objArr[0] = (element == null || element.getNodeType() != 1) ? FXMLLoader.NULL_KEYWORD : element.getTagName();
                throw new KeyResolverException("utils.resolver.noClass", objArr);
            }
            LOG.debug("check resolvability by class {}", keyResolver.getClass());
            PublicKey publicKeyResolvePublicKey = keyResolver.resolvePublicKey(element, str, storageResolver);
            if (publicKeyResolvePublicKey != null) {
                return publicKeyResolvePublicKey;
            }
        }
        Object[] objArr2 = new Object[1];
        objArr2[0] = (element == null || element.getNodeType() != 1) ? FXMLLoader.NULL_KEYWORD : element.getTagName();
        throw new KeyResolverException("utils.resolver.noClass", objArr2);
    }

    public static void register(String str, boolean z2) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        JavaUtils.checkRegisterPermission();
        KeyResolverSpi keyResolverSpi = (KeyResolverSpi) ClassLoaderUtils.loadClass(str, KeyResolver.class).newInstance();
        keyResolverSpi.setGlobalResolver(z2);
        register(keyResolverSpi, false);
    }

    public static void registerAtStart(String str, boolean z2) {
        JavaUtils.checkRegisterPermission();
        Throwable th = null;
        try {
            KeyResolverSpi keyResolverSpi = (KeyResolverSpi) ClassLoaderUtils.loadClass(str, KeyResolver.class).newInstance();
            keyResolverSpi.setGlobalResolver(z2);
            register(keyResolverSpi, true);
        } catch (ClassNotFoundException e2) {
            th = e2;
        } catch (IllegalAccessException e3) {
            th = e3;
        } catch (InstantiationException e4) {
            th = e4;
        }
        if (th != null) {
            throw ((IllegalArgumentException) new IllegalArgumentException("Invalid KeyResolver class name").initCause(th));
        }
    }

    public static void register(KeyResolverSpi keyResolverSpi, boolean z2) {
        JavaUtils.checkRegisterPermission();
        KeyResolver keyResolver = new KeyResolver(keyResolverSpi);
        if (z2) {
            resolverVector.add(0, keyResolver);
        } else {
            resolverVector.add(keyResolver);
        }
    }

    public static void registerClassNames(List<String> list) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        JavaUtils.checkRegisterPermission();
        ArrayList arrayList = new ArrayList(list.size());
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            KeyResolverSpi keyResolverSpi = (KeyResolverSpi) ClassLoaderUtils.loadClass(it.next(), KeyResolver.class).newInstance();
            keyResolverSpi.setGlobalResolver(false);
            arrayList.add(new KeyResolver(keyResolverSpi));
        }
        resolverVector.addAll(arrayList);
    }

    public static void registerDefaultResolvers() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KeyResolver(new RSAKeyValueResolver()));
        arrayList.add(new KeyResolver(new DSAKeyValueResolver()));
        arrayList.add(new KeyResolver(new X509CertificateResolver()));
        arrayList.add(new KeyResolver(new X509SKIResolver()));
        arrayList.add(new KeyResolver(new RetrievalMethodResolver()));
        arrayList.add(new KeyResolver(new X509SubjectNameResolver()));
        arrayList.add(new KeyResolver(new X509IssuerSerialResolver()));
        arrayList.add(new KeyResolver(new DEREncodedKeyValueResolver()));
        arrayList.add(new KeyResolver(new KeyInfoReferenceResolver()));
        arrayList.add(new KeyResolver(new X509DigestResolver()));
        arrayList.add(new KeyResolver(new ECKeyValueResolver()));
        resolverVector.addAll(arrayList);
    }

    public PublicKey resolvePublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return this.resolverSpi.engineLookupAndResolvePublicKey(element, str, storageResolver);
    }

    public X509Certificate resolveX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return this.resolverSpi.engineLookupResolveX509Certificate(element, str, storageResolver);
    }

    public SecretKey resolveSecretKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return this.resolverSpi.engineLookupAndResolveSecretKey(element, str, storageResolver);
    }

    public void setProperty(String str, String str2) {
        this.resolverSpi.engineSetProperty(str, str2);
    }

    public String getProperty(String str) {
        return this.resolverSpi.engineGetProperty(str);
    }

    public boolean understandsProperty(String str) {
        return this.resolverSpi.understandsProperty(str);
    }

    public String resolverClassName() {
        return this.resolverSpi.getClass().getName();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/KeyResolver$ResolverIterator.class */
    static class ResolverIterator implements Iterator<KeyResolverSpi> {
        List<KeyResolver> res;
        Iterator<KeyResolver> it;

        public ResolverIterator(List<KeyResolver> list) {
            this.res = list;
            this.it = this.res.iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.it.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public KeyResolverSpi next() {
            KeyResolver next = this.it.next();
            if (next != null) {
                return next.resolverSpi;
            }
            throw new RuntimeException("utils.resolver.noClass");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Can't remove resolvers using the iterator");
        }
    }

    public static Iterator<KeyResolverSpi> iterator() {
        return new ResolverIterator(resolverVector);
    }
}
