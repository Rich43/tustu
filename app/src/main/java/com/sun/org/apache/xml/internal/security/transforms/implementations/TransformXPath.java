package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityRuntimeException;
import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.XPathAPI;
import com.sun.org.apache.xml.internal.security.utils.XPathFactory;
import java.io.OutputStream;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformXPath.class */
public class TransformXPath extends TransformSpi {
    public static final String implementedTransformURI = "http://www.w3.org/TR/1999/REC-xpath-19991116";

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected String engineGetURI() {
        return "http://www.w3.org/TR/1999/REC-xpath-19991116";
    }

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws TransformationException {
        try {
            Element elementSelectDsNode = XMLUtils.selectDsNode(transform.getElement().getFirstChild(), "XPath", 0);
            if (elementSelectDsNode == null) {
                throw new TransformationException("xml.WrongContent", new Object[]{"ds:XPath", Constants._TAG_TRANSFORM});
            }
            Node firstChild = elementSelectDsNode.getFirstChild();
            if (firstChild == null) {
                throw new DOMException((short) 3, "Text must be in ds:Xpath");
            }
            String strFromNode = XMLUtils.getStrFromNode(firstChild);
            xMLSignatureInput.setNeedsToBeExpanded(needsCircumvent(strFromNode));
            xMLSignatureInput.addNodeFilter(new XPathNodeFilter(elementSelectDsNode, firstChild, strFromNode, XPathFactory.newInstance().newXPathAPI()));
            xMLSignatureInput.setNodeSet(true);
            return xMLSignatureInput;
        } catch (DOMException e2) {
            throw new TransformationException(e2);
        }
    }

    private boolean needsCircumvent(String str) {
        return (str.indexOf(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NAMESPACE) == -1 && str.indexOf("name()") == -1) ? false : true;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformXPath$XPathNodeFilter.class */
    static class XPathNodeFilter implements NodeFilter {
        XPathAPI xPathAPI;
        Node xpathnode;
        Element xpathElement;
        String str;

        XPathNodeFilter(Element element, Node node, String str, XPathAPI xPathAPI) {
            this.xpathnode = node;
            this.str = str;
            this.xpathElement = element;
            this.xPathAPI = xPathAPI;
        }

        @Override // com.sun.org.apache.xml.internal.security.signature.NodeFilter
        public int isNodeInclude(Node node) {
            try {
                if (this.xPathAPI.evaluate(node, this.xpathnode, this.str, this.xpathElement)) {
                    return 1;
                }
                return 0;
            } catch (TransformerException e2) {
                throw new XMLSecurityRuntimeException("signature.Transform.XPathError");
            }
        }

        @Override // com.sun.org.apache.xml.internal.security.signature.NodeFilter
        public int isNodeIncludeDO(Node node, int i2) {
            return isNodeInclude(node);
        }
    }
}
