package com.sun.org.apache.xml.internal.security.transforms.params;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/params/XPathFilterCHGPContainer.class */
public class XPathFilterCHGPContainer extends ElementProxy implements TransformParam {
    public static final String TRANSFORM_XPATHFILTERCHGP = "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter";
    private static final String _TAG_INCLUDE_BUT_SEARCH = "IncludeButSearch";
    private static final String _TAG_EXCLUDE_BUT_SEARCH = "ExcludeButSearch";
    private static final String _TAG_EXCLUDE = "Exclude";
    public static final String _TAG_XPATHCHGP = "XPathAlternative";
    public static final String _ATT_INCLUDESLASH = "IncludeSlashPolicy";
    public static final boolean IncludeSlash = true;
    public static final boolean ExcludeSlash = false;

    private XPathFilterCHGPContainer() {
    }

    private XPathFilterCHGPContainer(Document document, boolean z2, String str, String str2, String str3) throws DOMException {
        super(document);
        if (z2) {
            setLocalAttribute(_ATT_INCLUDESLASH, "true");
        } else {
            setLocalAttribute(_ATT_INCLUDESLASH, "false");
        }
        if (str != null && str.trim().length() > 0) {
            Element elementCreateElementForFamily = ElementProxy.createElementForFamily(document, getBaseNamespace(), _TAG_INCLUDE_BUT_SEARCH);
            elementCreateElementForFamily.appendChild(createText(indentXPathText(str)));
            addReturnToSelf();
            appendSelf(elementCreateElementForFamily);
        }
        if (str2 != null && str2.trim().length() > 0) {
            Element elementCreateElementForFamily2 = ElementProxy.createElementForFamily(document, getBaseNamespace(), _TAG_EXCLUDE_BUT_SEARCH);
            elementCreateElementForFamily2.appendChild(createText(indentXPathText(str2)));
            addReturnToSelf();
            appendSelf(elementCreateElementForFamily2);
        }
        if (str3 != null && str3.trim().length() > 0) {
            Element elementCreateElementForFamily3 = ElementProxy.createElementForFamily(document, getBaseNamespace(), _TAG_EXCLUDE);
            elementCreateElementForFamily3.appendChild(createText(indentXPathText(str3)));
            addReturnToSelf();
            appendSelf(elementCreateElementForFamily3);
        }
        addReturnToSelf();
    }

    static String indentXPathText(String str) {
        if (str.length() > 2 && !Character.isWhitespace(str.charAt(0))) {
            return "\n" + str + "\n";
        }
        return str;
    }

    private XPathFilterCHGPContainer(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public static XPathFilterCHGPContainer getInstance(Document document, boolean z2, String str, String str2, String str3) {
        return new XPathFilterCHGPContainer(document, z2, str, str2, str3);
    }

    public static XPathFilterCHGPContainer getInstance(Element element, String str) throws XMLSecurityException {
        return new XPathFilterCHGPContainer(element, str);
    }

    private String getXStr(String str) {
        if (length(getBaseNamespace(), str) != 1) {
            return "";
        }
        return XMLUtils.getFullTextChildrenFromNode(XMLUtils.selectNode(getElement().getFirstChild(), getBaseNamespace(), str, 0));
    }

    public String getIncludeButSearch() {
        return getXStr(_TAG_INCLUDE_BUT_SEARCH);
    }

    public String getExcludeButSearch() {
        return getXStr(_TAG_EXCLUDE_BUT_SEARCH);
    }

    public String getExclude() {
        return getXStr(_TAG_EXCLUDE);
    }

    public boolean getIncludeSlashPolicy() {
        return getLocalAttribute(_ATT_INCLUDESLASH).equals("true");
    }

    private Node getHereContextNode(String str) {
        if (length(getBaseNamespace(), str) != 1) {
            return null;
        }
        return selectNodeText(getFirstChild(), getBaseNamespace(), str, 0);
    }

    private static Text selectNodeText(Node node, String str, String str2, int i2) {
        Node node2;
        Element elementSelectNode = XMLUtils.selectNode(node, str, str2, i2);
        if (elementSelectNode == null) {
            return null;
        }
        Node firstChild = elementSelectNode.getFirstChild();
        while (true) {
            node2 = firstChild;
            if (node2 == null || node2.getNodeType() == 3) {
                break;
            }
            firstChild = node2.getNextSibling();
        }
        return (Text) node2;
    }

    public Node getHereContextNodeIncludeButSearch() {
        return getHereContextNode(_TAG_INCLUDE_BUT_SEARCH);
    }

    public Node getHereContextNodeExcludeButSearch() {
        return getHereContextNode(_TAG_EXCLUDE_BUT_SEARCH);
    }

    public Node getHereContextNodeExclude() {
        return getHereContextNode(_TAG_EXCLUDE);
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public final String getBaseLocalName() {
        return _TAG_XPATHCHGP;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public final String getBaseNamespace() {
        return TRANSFORM_XPATHFILTERCHGP;
    }
}
