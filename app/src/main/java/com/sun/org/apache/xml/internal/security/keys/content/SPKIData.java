package com.sun.org.apache.xml.internal.security.keys.content;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/SPKIData.class */
public class SPKIData extends SignatureElementProxy implements KeyInfoContent {
    public SPKIData(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_SPKIDATA;
    }
}
