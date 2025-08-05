package com.sun.org.apache.xml.internal.security.keys.content;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.DSAKeyValue;
import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.ECKeyValue;
import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.RSAKeyValue;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/KeyValue.class */
public class KeyValue extends SignatureElementProxy implements KeyInfoContent {
    public KeyValue(Document document, DSAKeyValue dSAKeyValue) {
        super(document);
        addReturnToSelf();
        appendSelf(dSAKeyValue);
        addReturnToSelf();
    }

    public KeyValue(Document document, RSAKeyValue rSAKeyValue) {
        super(document);
        addReturnToSelf();
        appendSelf(rSAKeyValue);
        addReturnToSelf();
    }

    public KeyValue(Document document, Element element) {
        super(document);
        addReturnToSelf();
        appendSelf(element);
        addReturnToSelf();
    }

    public KeyValue(Document document, PublicKey publicKey) {
        super(document);
        addReturnToSelf();
        if (publicKey instanceof DSAPublicKey) {
            appendSelf(new DSAKeyValue(getDocument(), publicKey));
            addReturnToSelf();
        } else if (publicKey instanceof RSAPublicKey) {
            appendSelf(new RSAKeyValue(getDocument(), publicKey));
            addReturnToSelf();
        } else {
            if (publicKey instanceof ECPublicKey) {
                appendSelf(new ECKeyValue(getDocument(), publicKey));
                addReturnToSelf();
                return;
            }
            throw new IllegalArgumentException("The given PublicKey type " + ((Object) publicKey) + " is not supported. Only DSAPublicKey and RSAPublicKey and ECPublicKey types are currently supported");
        }
    }

    public KeyValue(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public PublicKey getPublicKey() throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_RSAKEYVALUE, 0);
        if (elementSelectDsNode != null) {
            return new RSAKeyValue(elementSelectDsNode, this.baseURI).getPublicKey();
        }
        Element elementSelectDsNode2 = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_DSAKEYVALUE, 0);
        if (elementSelectDsNode2 != null) {
            return new DSAKeyValue(elementSelectDsNode2, this.baseURI).getPublicKey();
        }
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_KEYVALUE;
    }
}
