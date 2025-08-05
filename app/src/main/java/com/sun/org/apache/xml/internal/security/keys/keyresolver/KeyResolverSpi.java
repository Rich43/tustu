package com.sun.org.apache.xml.internal.security.keys.keyresolver;

import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/KeyResolverSpi.class */
public abstract class KeyResolverSpi {
    protected Map<String, String> properties;
    protected boolean globalResolver = false;
    protected boolean secureValidation;

    public void setSecureValidation(boolean z2) {
        this.secureValidation = z2;
    }

    public boolean engineCanResolve(Element element, String str, StorageResolver storageResolver) {
        throw new UnsupportedOperationException();
    }

    public PublicKey engineResolvePublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        throw new UnsupportedOperationException();
    }

    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        KeyResolverSpi keyResolverSpiCloneIfNeeded = cloneIfNeeded();
        if (!keyResolverSpiCloneIfNeeded.engineCanResolve(element, str, storageResolver)) {
            return null;
        }
        return keyResolverSpiCloneIfNeeded.engineResolvePublicKey(element, str, storageResolver);
    }

    private KeyResolverSpi cloneIfNeeded() throws KeyResolverException {
        if (this.globalResolver) {
            try {
                return (KeyResolverSpi) getClass().newInstance();
            } catch (IllegalAccessException e2) {
                throw new KeyResolverException(e2, "");
            } catch (InstantiationException e3) {
                throw new KeyResolverException(e3, "");
            }
        }
        return this;
    }

    public X509Certificate engineResolveX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        throw new UnsupportedOperationException();
    }

    public X509Certificate engineLookupResolveX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        KeyResolverSpi keyResolverSpiCloneIfNeeded = cloneIfNeeded();
        if (!keyResolverSpiCloneIfNeeded.engineCanResolve(element, str, storageResolver)) {
            return null;
        }
        return keyResolverSpiCloneIfNeeded.engineResolveX509Certificate(element, str, storageResolver);
    }

    public SecretKey engineResolveSecretKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        throw new UnsupportedOperationException();
    }

    public SecretKey engineLookupAndResolveSecretKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        KeyResolverSpi keyResolverSpiCloneIfNeeded = cloneIfNeeded();
        if (!keyResolverSpiCloneIfNeeded.engineCanResolve(element, str, storageResolver)) {
            return null;
        }
        return keyResolverSpiCloneIfNeeded.engineResolveSecretKey(element, str, storageResolver);
    }

    public PrivateKey engineLookupAndResolvePrivateKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    public void engineSetProperty(String str, String str2) {
        if (this.properties == null) {
            this.properties = new HashMap();
        }
        this.properties.put(str, str2);
    }

    public String engineGetProperty(String str) {
        if (this.properties == null) {
            return null;
        }
        return this.properties.get(str);
    }

    public boolean understandsProperty(String str) {
        return (this.properties == null || this.properties.get(str) == null) ? false : true;
    }

    public void setGlobalResolver(boolean z2) {
        this.globalResolver = z2;
    }

    protected static Element getDocFromBytes(byte[] bArr, boolean z2) throws KeyResolverException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            Throwable th = null;
            try {
                Element documentElement = XMLUtils.createDocumentBuilder(false, z2).parse(byteArrayInputStream).getDocumentElement();
                if (byteArrayInputStream != null) {
                    if (0 != 0) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        byteArrayInputStream.close();
                    }
                }
                return documentElement;
            } catch (Throwable th3) {
                if (byteArrayInputStream != null) {
                    if (0 != 0) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        byteArrayInputStream.close();
                    }
                }
                throw th3;
            }
        } catch (IOException e2) {
            throw new KeyResolverException(e2);
        } catch (ParserConfigurationException e3) {
            throw new KeyResolverException(e3);
        } catch (SAXException e4) {
            throw new KeyResolverException(e4);
        }
    }
}
