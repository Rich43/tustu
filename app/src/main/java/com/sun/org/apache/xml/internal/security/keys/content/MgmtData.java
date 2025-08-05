package com.sun.org.apache.xml.internal.security.keys.content;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/MgmtData.class */
public class MgmtData extends SignatureElementProxy implements KeyInfoContent {
    public MgmtData(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public MgmtData(Document document, String str) {
        super(document);
        addText(str);
    }

    public String getMgmtData() {
        return getTextFromTextChild();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_MGMTDATA;
    }
}
