package com.sun.org.apache.xml.internal.security.transforms.params;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/params/XPath2FilterContainer.class */
public class XPath2FilterContainer extends ElementProxy implements TransformParam {
    private static final String _ATT_FILTER = "Filter";
    private static final String _ATT_FILTER_VALUE_INTERSECT = "intersect";
    private static final String _ATT_FILTER_VALUE_SUBTRACT = "subtract";
    private static final String _ATT_FILTER_VALUE_UNION = "union";
    public static final String INTERSECT = "intersect";
    public static final String SUBTRACT = "subtract";
    public static final String UNION = "union";
    public static final String _TAG_XPATH2 = "XPath";
    public static final String XPathFilter2NS = "http://www.w3.org/2002/06/xmldsig-filter2";

    private XPath2FilterContainer() {
    }

    private XPath2FilterContainer(Document document, String str, String str2) {
        super(document);
        setLocalAttribute("Filter", str2);
        appendSelf(createText(str));
    }

    private XPath2FilterContainer(Element element, String str) throws XMLSecurityException {
        super(element, str);
        String localAttribute = getLocalAttribute("Filter");
        if (!localAttribute.equals("intersect") && !localAttribute.equals("subtract") && !localAttribute.equals("union")) {
            throw new XMLSecurityException("attributeValueIllegal", new Object[]{"Filter", localAttribute, "intersect, subtract or union"});
        }
    }

    public static XPath2FilterContainer newInstanceIntersect(Document document, String str) {
        return new XPath2FilterContainer(document, str, "intersect");
    }

    public static XPath2FilterContainer newInstanceSubtract(Document document, String str) {
        return new XPath2FilterContainer(document, str, "subtract");
    }

    public static XPath2FilterContainer newInstanceUnion(Document document, String str) {
        return new XPath2FilterContainer(document, str, "union");
    }

    public static NodeList newInstances(Document document, String[][] strArr) throws IllegalArgumentException {
        HelperNodeList helperNodeList = new HelperNodeList();
        XMLUtils.addReturnToElement(document, helperNodeList);
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2][0];
            String str2 = strArr[i2][1];
            if (!str.equals("intersect") && !str.equals("subtract") && !str.equals("union")) {
                throw new IllegalArgumentException("The type(" + i2 + ")=\"" + str + "\" is illegal");
            }
            helperNodeList.appendChild(new XPath2FilterContainer(document, str2, str).getElement());
            XMLUtils.addReturnToElement(document, helperNodeList);
        }
        return helperNodeList;
    }

    public static XPath2FilterContainer newInstance(Element element, String str) throws XMLSecurityException {
        return new XPath2FilterContainer(element, str);
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
        return "http://www.w3.org/2002/06/xmldsig-filter2";
    }
}
