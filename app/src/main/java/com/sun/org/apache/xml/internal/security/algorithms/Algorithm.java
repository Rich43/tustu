package com.sun.org.apache.xml.internal.security.algorithms;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/Algorithm.class */
public abstract class Algorithm extends SignatureElementProxy {
    public Algorithm(Document document, String str) {
        super(document);
        setAlgorithmURI(str);
    }

    public Algorithm(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public String getAlgorithmURI() {
        return getLocalAttribute(Constants._ATT_ALGORITHM);
    }

    protected void setAlgorithmURI(String str) {
        if (str != null) {
            setLocalAttribute(Constants._ATT_ALGORITHM, str);
        }
    }
}
