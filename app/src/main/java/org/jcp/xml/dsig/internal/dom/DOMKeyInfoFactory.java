package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.math.BigInteger;
import java.security.KeyException;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyName;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.PGPData;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.jcp.xml.dsig.internal.dom.DOMKeyValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyInfoFactory.class */
public final class DOMKeyInfoFactory extends KeyInfoFactory {
    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public KeyInfo newKeyInfo(List list) {
        return newKeyInfo(list, null);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public KeyInfo newKeyInfo(List list, String str) {
        return new DOMKeyInfo(list, str);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public KeyName newKeyName(String str) {
        return new DOMKeyName(str);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public KeyValue newKeyValue(PublicKey publicKey) throws KeyException {
        String algorithm = publicKey.getAlgorithm();
        if ("DSA".equals(algorithm)) {
            return new DOMKeyValue.DSA((DSAPublicKey) publicKey);
        }
        if ("RSA".equals(algorithm)) {
            return new DOMKeyValue.RSA((RSAPublicKey) publicKey);
        }
        if ("EC".equals(algorithm)) {
            return new DOMKeyValue.EC((ECPublicKey) publicKey);
        }
        throw new KeyException("unsupported key algorithm: " + algorithm);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public PGPData newPGPData(byte[] bArr) {
        return newPGPData(bArr, null, null);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public PGPData newPGPData(byte[] bArr, byte[] bArr2, List list) {
        return new DOMPGPData(bArr, bArr2, list);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public PGPData newPGPData(byte[] bArr, List list) {
        return new DOMPGPData(bArr, list);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public RetrievalMethod newRetrievalMethod(String str) {
        return newRetrievalMethod(str, null, null);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public RetrievalMethod newRetrievalMethod(String str, String str2, List list) {
        if (str == null) {
            throw new NullPointerException("uri must not be null");
        }
        return new DOMRetrievalMethod(str, str2, (List<? extends Transform>) list);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public X509Data newX509Data(List list) {
        return new DOMX509Data((List<?>) list);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public X509IssuerSerial newX509IssuerSerial(String str, BigInteger bigInteger) {
        return new DOMX509IssuerSerial(str, bigInteger);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public boolean isFeatureSupported(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        return false;
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public URIDereferencer getURIDereferencer() {
        return DOMURIDereferencer.INSTANCE;
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
    public KeyInfo unmarshalKeyInfo(XMLStructure xMLStructure) throws MarshalException {
        Element documentElement;
        if (xMLStructure == null) {
            throw new NullPointerException("xmlStructure cannot be null");
        }
        if (!(xMLStructure instanceof javax.xml.crypto.dom.DOMStructure)) {
            throw new ClassCastException("xmlStructure must be of type DOMStructure");
        }
        Node node = ((javax.xml.crypto.dom.DOMStructure) xMLStructure).getNode();
        node.normalize();
        if (node.getNodeType() == 9) {
            documentElement = ((Document) node).getDocumentElement();
        } else if (node.getNodeType() == 1) {
            documentElement = (Element) node;
        } else {
            throw new MarshalException("xmlStructure does not contain a proper Node");
        }
        String localName = documentElement.getLocalName();
        String namespaceURI = documentElement.getNamespaceURI();
        if (localName == null || namespaceURI == null) {
            throw new MarshalException("Document implementation must support DOM Level 2 and be namespace aware");
        }
        if (Constants._TAG_KEYINFO.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
            try {
                return new DOMKeyInfo(documentElement, new UnmarshalContext(), getProvider());
            } catch (MarshalException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new MarshalException(e3);
            }
        }
        throw new MarshalException("Invalid KeyInfo tag: " + namespaceURI + CallSiteDescriptor.TOKEN_DELIMITER + localName);
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyInfoFactory$UnmarshalContext.class */
    private static class UnmarshalContext extends DOMCryptoContext {
        UnmarshalContext() {
        }
    }
}
