package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.OutputStream;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformEnvelopedSignature.class */
public class TransformEnvelopedSignature extends TransformSpi {
    public static final String implementedTransformURI = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected String engineGetURI() {
        return "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
    }

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws TransformationException {
        Node nodeSearchSignatureElement = searchSignatureElement(transform.getElement());
        xMLSignatureInput.setExcludeNode(nodeSearchSignatureElement);
        xMLSignatureInput.addNodeFilter(new EnvelopedNodeFilter(nodeSearchSignatureElement));
        return xMLSignatureInput;
    }

    private static Node searchSignatureElement(Node node) throws TransformationException {
        boolean z2 = false;
        while (true) {
            if (node != null && node.getNodeType() != 9) {
                Element element = (Element) node;
                if (element.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#") && element.getLocalName().equals(Constants._TAG_SIGNATURE)) {
                    z2 = true;
                    break;
                }
                node = node.getParentNode();
            } else {
                break;
            }
        }
        if (!z2) {
            throw new TransformationException("transform.envelopedSignatureTransformNotInSignatureElement");
        }
        return node;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformEnvelopedSignature$EnvelopedNodeFilter.class */
    static class EnvelopedNodeFilter implements NodeFilter {
        Node exclude;

        EnvelopedNodeFilter(Node node) {
            this.exclude = node;
        }

        @Override // com.sun.org.apache.xml.internal.security.signature.NodeFilter
        public int isNodeIncludeDO(Node node, int i2) {
            if (node == this.exclude) {
                return -1;
            }
            return 1;
        }

        @Override // com.sun.org.apache.xml.internal.security.signature.NodeFilter
        public int isNodeInclude(Node node) {
            if (node == this.exclude || XMLUtils.isDescendantOrSelf(this.exclude, node)) {
                return -1;
            }
            return 1;
        }
    }
}
