package com.sun.org.apache.xml.internal.security.keys.content;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/KeyName.class */
public class KeyName extends SignatureElementProxy implements KeyInfoContent {
    public KeyName(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public KeyName(Document document, String str) {
        super(document);
        addText(str);
    }

    public String getKeyName() {
        return getTextFromTextChild();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_KEYNAME;
    }
}
