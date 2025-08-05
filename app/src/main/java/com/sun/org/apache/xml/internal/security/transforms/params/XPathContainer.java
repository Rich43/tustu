package com.sun.org.apache.xml.internal.security.transforms.params;

import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/params/XPathContainer.class */
public class XPathContainer extends SignatureElementProxy implements TransformParam {
    public XPathContainer(Document document) {
        super(document);
    }

    public void setXPath(String str) {
        Node firstChild = getElement().getFirstChild();
        while (firstChild != null) {
            Node node = firstChild;
            firstChild = firstChild.getNextSibling();
            getElement().removeChild(node);
        }
        appendSelf(createText(str));
    }

    public String getXPath() {
        return getTextFromTextChild();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return "XPath";
    }
}
