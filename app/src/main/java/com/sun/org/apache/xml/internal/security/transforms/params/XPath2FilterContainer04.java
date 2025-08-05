package com.sun.org.apache.xml.internal.security.transforms.params;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/params/XPath2FilterContainer04.class */
public class XPath2FilterContainer04 extends ElementProxy implements TransformParam {
    private static final String _ATT_FILTER = "Filter";
    private static final String _ATT_FILTER_VALUE_INTERSECT = "intersect";
    private static final String _ATT_FILTER_VALUE_SUBTRACT = "subtract";
    private static final String _ATT_FILTER_VALUE_UNION = "union";
    public static final String _TAG_XPATH2 = "XPath";
    public static final String XPathFilter2NS = "http://www.w3.org/2002/04/xmldsig-filter2";

    private XPath2FilterContainer04() {
    }

    private XPath2FilterContainer04(Document document, String str, String str2) {
        super(document);
        setLocalAttribute("Filter", str2);
        if (str.length() > 2 && !Character.isWhitespace(str.charAt(0))) {
            addReturnToSelf();
            appendSelf(createText(str));
            addReturnToSelf();
            return;
        }
        appendSelf(createText(str));
    }

    private XPath2FilterContainer04(Element element, String str) throws XMLSecurityException {
        super(element, str);
        String localAttribute = getLocalAttribute("Filter");
        if (!localAttribute.equals("intersect") && !localAttribute.equals("subtract") && !localAttribute.equals("union")) {
            throw new XMLSecurityException("attributeValueIllegal", new Object[]{"Filter", localAttribute, "intersect, subtract or union"});
        }
    }

    public static XPath2FilterContainer04 newInstanceIntersect(Document document, String str) {
        return new XPath2FilterContainer04(document, str, "intersect");
    }

    public static XPath2FilterContainer04 newInstanceSubtract(Document document, String str) {
        return new XPath2FilterContainer04(document, str, "subtract");
    }

    public static XPath2FilterContainer04 newInstanceUnion(Document document, String str) {
        return new XPath2FilterContainer04(document, str, "union");
    }

    public static XPath2FilterContainer04 newInstance(Element element, String str) throws XMLSecurityException {
        return new XPath2FilterContainer04(element, str);
    }

    public boolean isIntersect() {
        return getLocalAttribute("Filter").equals("intersect");
    }

    public boolean isSubtract() {
        return getLocalAttribute("Filter").equals("subtract");
    }

    public boolean isUnion() {
        return getLocalAttribute("Filter").equals("union");
    }

    public String getXPathFilterStr() {
        return getTextFromTextChild();
    }

    public Node getXPathFilterTextNode() {
        Node firstChild = getElement().getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (node.getNodeType() == 3) {
                    return node;
                }
                firstChild = node.getNextSibling();
            } else {
                return null;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public final String getBaseLocalName() {
        return "XPath";
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public final String getBaseNamespace() {
        return XPathFilter2NS;
    }
}
