package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformBase64Decode.class */
public class TransformBase64Decode extends TransformSpi {
    public static final String implementedTransformURI = "http://www.w3.org/2000/09/xmldsig#base64";

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected String engineGetURI() {
        return "http://www.w3.org/2000/09/xmldsig#base64";
    }

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, Transform transform) throws CanonicalizationException, IOException, TransformationException {
        return enginePerformTransform(xMLSignatureInput, null, transform);
    }

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws CanonicalizationException, IOException, TransformationException {
        if (xMLSignatureInput.isElement()) {
            Node subNode = xMLSignatureInput.getSubNode();
            if (xMLSignatureInput.getSubNode().getNodeType() == 3) {
                subNode = subNode.getParentNode();
            }
            StringBuilder sb = new StringBuilder();
            traverseElement((Element) subNode, sb);
            if (outputStream == null) {
                XMLSignatureInput xMLSignatureInput2 = new XMLSignatureInput(XMLUtils.decode(sb.toString()));
                xMLSignatureInput2.setSecureValidation(this.secureValidation);
                return xMLSignatureInput2;
            }
            outputStream.write(XMLUtils.decode(sb.toString()));
            XMLSignatureInput xMLSignatureInput3 = new XMLSignatureInput((byte[]) null);
            xMLSignatureInput3.setSecureValidation(this.secureValidation);
            xMLSignatureInput3.setOutputStream(outputStream);
            return xMLSignatureInput3;
        }
        if (xMLSignatureInput.isOctetStream() || xMLSignatureInput.isNodeSet()) {
            if (outputStream == null) {
                XMLSignatureInput xMLSignatureInput4 = new XMLSignatureInput(XMLUtils.decode(xMLSignatureInput.getBytes()));
                xMLSignatureInput4.setSecureValidation(this.secureValidation);
                return xMLSignatureInput4;
            }
            if (xMLSignatureInput.isByteArray() || xMLSignatureInput.isNodeSet()) {
                outputStream.write(XMLUtils.decode(xMLSignatureInput.getBytes()));
            } else {
                outputStream.write(XMLUtils.decode(JavaUtils.getBytesFromStream(xMLSignatureInput.getOctetStreamReal())));
            }
            XMLSignatureInput xMLSignatureInput5 = new XMLSignatureInput((byte[]) null);
            xMLSignatureInput5.setSecureValidation(this.secureValidation);
            xMLSignatureInput5.setOutputStream(outputStream);
            return xMLSignatureInput5;
        }
        try {
            Element documentElement = XMLUtils.createDocumentBuilder(false, this.secureValidation).parse(xMLSignatureInput.getOctetStream()).getDocumentElement();
            StringBuilder sb2 = new StringBuilder();
            traverseElement(documentElement, sb2);
            XMLSignatureInput xMLSignatureInput6 = new XMLSignatureInput(XMLUtils.decode(sb2.toString()));
            xMLSignatureInput6.setSecureValidation(this.secureValidation);
            return xMLSignatureInput6;
        } catch (ParserConfigurationException e2) {
            throw new TransformationException(e2, "c14n.Canonicalizer.Exception");
        } catch (SAXException e3) {
            throw new TransformationException(e3, "SAX exception");
        }
    }

    void traverseElement(Element element, StringBuilder sb) {
        Node firstChild = element.getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                switch (node.getNodeType()) {
                    case 1:
                        traverseElement((Element) node, sb);
                        break;
                    case 3:
                        sb.append(((Text) node).getData());
                        break;
                }
                firstChild = node.getNextSibling();
            } else {
                return;
            }
        }
    }
}
