package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import com.sun.org.apache.xml.internal.security.transforms.params.XPath2FilterContainer;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.XPathFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformXPath2Filter.class */
public class TransformXPath2Filter extends TransformSpi {
    public static final String implementedTransformURI = "http://www.w3.org/2002/06/xmldsig-filter2";

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected String engineGetURI() {
        return "http://www.w3.org/2002/06/xmldsig-filter2";
    }

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws TransformationException {
        Document ownerDocument;
        try {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            Element[] elementArrSelectNodes = XMLUtils.selectNodes(transform.getElement().getFirstChild(), "http://www.w3.org/2002/06/xmldsig-filter2", "XPath");
            if (elementArrSelectNodes.length == 0) {
                throw new TransformationException("xml.WrongContent", new Object[]{"http://www.w3.org/2002/06/xmldsig-filter2", "XPath"});
            }
            if (xMLSignatureInput.getSubNode() != null) {
                ownerDocument = XMLUtils.getOwnerDocument(xMLSignatureInput.getSubNode());
            } else {
                ownerDocument = XMLUtils.getOwnerDocument(xMLSignatureInput.getNodeSet());
            }
            for (Element element : elementArrSelectNodes) {
                XPath2FilterContainer xPath2FilterContainerNewInstance = XPath2FilterContainer.newInstance(element, xMLSignatureInput.getSourceURI());
                NodeList nodeListSelectNodeList = XPathFactory.newInstance().newXPathAPI().selectNodeList(ownerDocument, xPath2FilterContainerNewInstance.getXPathFilterTextNode(), XMLUtils.getStrFromNode(xPath2FilterContainerNewInstance.getXPathFilterTextNode()), xPath2FilterContainerNewInstance.getElement());
                if (xPath2FilterContainerNewInstance.isIntersect()) {
                    arrayList3.add(nodeListSelectNodeList);
                } else if (xPath2FilterContainerNewInstance.isSubtract()) {
                    arrayList2.add(nodeListSelectNodeList);
                } else if (xPath2FilterContainerNewInstance.isUnion()) {
                    arrayList.add(nodeListSelectNodeList);
                }
            }
            xMLSignatureInput.addNodeFilter(new XPath2NodeFilter(arrayList, arrayList2, arrayList3));
            xMLSignatureInput.setNodeSet(true);
            return xMLSignatureInput;
        } catch (CanonicalizationException e2) {
            throw new TransformationException(e2);
        } catch (InvalidCanonicalizerException e3) {
            throw new TransformationException(e3);
        } catch (XMLSecurityException e4) {
            throw new TransformationException(e4);
        } catch (IOException e5) {
            throw new TransformationException(e5);
        } catch (ParserConfigurationException e6) {
            throw new TransformationException(e6);
        } catch (TransformerException e7) {
            throw new TransformationException(e7);
        } catch (DOMException e8) {
            throw new TransformationException(e8);
        } catch (SAXException e9) {
            throw new TransformationException(e9);
        }
    }
}
