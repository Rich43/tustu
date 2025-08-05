package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509IssuerSerial;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SubjectName;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;
import javax.crypto.SecretKey;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/PrivateKeyResolver.class */
public class PrivateKeyResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(PrivateKeyResolver.class);
    private KeyStore keyStore;
    private char[] password;

    public PrivateKeyResolver(KeyStore keyStore, char[] cArr) {
        this.keyStore = keyStore;
        this.password = cArr;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public boolean engineCanResolve(Element element, String str, StorageResolver storageResolver) {
        if (XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_X509DATA) || XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME)) {
            return true;
        }
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public X509Certificate engineLookupResolveX509Certificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineResolveSecretKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PrivateKey engineLookupAndResolvePrivateKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        LOG.debug("Can I resolve {}?", element.getTagName());
        if (XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_X509DATA)) {
            PrivateKey privateKeyResolveX509Data = resolveX509Data(element, str);
            if (privateKeyResolveX509Data != null) {
                return privateKeyResolveX509Data;
            }
        } else if (XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME)) {
            LOG.debug("Can I resolve KeyName?");
            try {
                Key key = this.keyStore.getKey(element.getFirstChild().getNodeValue(), this.password);
                if (key instanceof PrivateKey) {
                    return (PrivateKey) key;
                }
            } catch (Exception e2) {
                LOG.debug("Cannot recover the key", e2);
            }
        }
        LOG.debug("I can't");
        return null;
    }

    private PrivateKey resolveX509Data(Element element, String str) {
        LOG.debug("Can I resolve X509Data?");
        try {
            X509Data x509Data = new X509Data(element, str);
            int iLengthSKI = x509Data.lengthSKI();
            for (int i2 = 0; i2 < iLengthSKI; i2++) {
                PrivateKey privateKeyResolveX509SKI = resolveX509SKI(x509Data.itemSKI(i2));
                if (privateKeyResolveX509SKI != null) {
                    return privateKeyResolveX509SKI;
                }
            }
            int iLengthIssuerSerial = x509Data.lengthIssuerSerial();
            for (int i3 = 0; i3 < iLengthIssuerSerial; i3++) {
                PrivateKey privateKeyResolveX509IssuerSerial = resolveX509IssuerSerial(x509Data.itemIssuerSerial(i3));
                if (privateKeyResolveX509IssuerSerial != null) {
                    return privateKeyResolveX509IssuerSerial;
                }
            }
            int iLengthSubjectName = x509Data.lengthSubjectName();
            for (int i4 = 0; i4 < iLengthSubjectName; i4++) {
                PrivateKey privateKeyResolveX509SubjectName = resolveX509SubjectName(x509Data.itemSubjectName(i4));
                if (privateKeyResolveX509SubjectName != null) {
                    return privateKeyResolveX509SubjectName;
                }
            }
            int iLengthCertificate = x509Data.lengthCertificate();
            for (int i5 = 0; i5 < iLengthCertificate; i5++) {
                PrivateKey privateKeyResolveX509Certificate = resolveX509Certificate(x509Data.itemCertificate(i5));
                if (privateKeyResolveX509Certificate != null) {
                    return privateKeyResolveX509Certificate;
                }
            }
            return null;
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        } catch (KeyStoreException e3) {
            LOG.debug("KeyStoreException", e3);
            return null;
        }
    }

    private PrivateKey resolveX509SKI(XMLX509SKI xmlx509ski) throws KeyStoreException, XMLSecurityException {
        LOG.debug("Can I resolve X509SKI?");
        Enumeration<String> enumerationAliases = this.keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            if (this.keyStore.isKeyEntry(strNextElement2)) {
                Certificate certificate = this.keyStore.getCertificate(strNextElement2);
                if ((certificate instanceof X509Certificate) && new XMLX509SKI(xmlx509ski.getDocument(), (X509Certificate) certificate).equals(xmlx509ski)) {
                    LOG.debug("match !!! ");
                    try {
                        Key key = this.keyStore.getKey(strNextElement2, this.password);
                        if (key instanceof PrivateKey) {
                            return (PrivateKey) key;
                        }
                        continue;
                    } catch (Exception e2) {
                        LOG.debug("Cannot recover the key", e2);
                    }
                }
            }
        }
        return null;
    }

    private PrivateKey resolveX509IssuerSerial(XMLX509IssuerSerial xMLX509IssuerSerial) throws KeyStoreException {
        LOG.debug("Can I resolve X509IssuerSerial?");
        Enumeration<String> enumerationAliases = this.keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            if (this.keyStore.isKeyEntry(strNextElement2)) {
                Certificate certificate = this.keyStore.getCertificate(strNextElement2);
                if ((certificate instanceof X509Certificate) && new XMLX509IssuerSerial(xMLX509IssuerSerial.getDocument(), (X509Certificate) certificate).equals(xMLX509IssuerSerial)) {
                    LOG.debug("match !!! ");
                    try {
                        Key key = this.keyStore.getKey(strNextElement2, this.password);
                        if (key instanceof PrivateKey) {
                            return (PrivateKey) key;
                        }
                        continue;
                    } catch (Exception e2) {
                        LOG.debug("Cannot recover the key", e2);
                    }
                }
            }
        }
        return null;
    }

    private PrivateKey resolveX509SubjectName(XMLX509SubjectName xMLX509SubjectName) throws KeyStoreException {
        LOG.debug("Can I resolve X509SubjectName?");
        Enumeration<String> enumerationAliases = this.keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            if (this.keyStore.isKeyEntry(strNextElement2)) {
                Certificate certificate = this.keyStore.getCertificate(strNextElement2);
                if ((certificate instanceof X509Certificate) && new XMLX509SubjectName(xMLX509SubjectName.getDocument(), (X509Certificate) certificate).equals(xMLX509SubjectName)) {
                    LOG.debug("match !!! ");
                    try {
                        Key key = this.keyStore.getKey(strNextElement2, this.password);
                        if (key instanceof PrivateKey) {
                            return (PrivateKey) key;
                        }
                        continue;
                    } catch (Exception e2) {
                        LOG.debug("Cannot recover the key", e2);
                    }
                }
            }
        }
        return null;
    }

    private PrivateKey resolveX509Certificate(XMLX509Certificate xMLX509Certificate) throws XMLSecurityException, KeyStoreException {
        LOG.debug("Can I resolve X509Certificate?");
        byte[] certificateBytes = xMLX509Certificate.getCertificateBytes();
        Enumeration<String> enumerationAliases = this.keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            if (this.keyStore.isKeyEntry(strNextElement2)) {
                Certificate certificate = this.keyStore.getCertificate(strNextElement2);
                if (certificate instanceof X509Certificate) {
                    byte[] encoded = null;
                    try {
                        encoded = certificate.getEncoded();
                    } catch (CertificateEncodingException e2) {
                        LOG.debug("Cannot recover the key", e2);
                    }
                    if (encoded != null && Arrays.equals(encoded, certificateBytes)) {
                        LOG.debug("match !!! ");
                        try {
                            Key key = this.keyStore.getKey(strNextElement2, this.password);
                            if (key instanceof PrivateKey) {
                                return (PrivateKey) key;
                            }
                            continue;
                        } catch (Exception e3) {
                            LOG.debug("Cannot recover the key", e3);
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }
}
