package com.sun.org.apache.xml.internal.security.c14n.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/CanonicalizerPhysical.class */
public class CanonicalizerPhysical extends CanonicalizerBase {
    public CanonicalizerPhysical() {
        super(true);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeXPathNodeSet(Set<Node> set, String str) throws CanonicalizationException {
        throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeSubTree(Node node, String str) throws CanonicalizationException {
        throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeSubTree(Node node, String str, boolean z2) throws CanonicalizationException {
        throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void outputAttributesSubtree(Element element, NameSpaceSymbTable nameSpaceSymbTable, Map<String, byte[]> map) throws CanonicalizationException, DOMException, IOException {
        if (element.hasAttributes()) {
            TreeSet<Attr> treeSet = new TreeSet(COMPARE);
            NamedNodeMap attributes = element.getAttributes();
            int length = attributes.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                treeSet.add((Attr) attributes.item(i2));
            }
            OutputStream writer = getWriter();
            for (Attr attr : treeSet) {
                outputAttrToWriter(attr.getNodeName(), attr.getNodeValue(), writer, map);
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void outputAttributes(Element element, NameSpaceSymbTable nameSpaceSymbTable, Map<String, byte[]> map) throws CanonicalizationException, DOMException, IOException {
        throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void circumventBugIfNeeded(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException, ParserConfigurationException, SAXException, IOException {
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void handleParent(Element element, NameSpaceSymbTable nameSpaceSymbTable) {
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public final String engineGetURI() {
        return Canonicalizer.ALGO_ID_C14N_PHYSICAL;
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public final boolean engineGetIncludeComments() {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void outputPItoWriter(ProcessingInstruction processingInstruction, OutputStream outputStream, int i2) throws IOException {
        super.outputPItoWriter(processingInstruction, outputStream, 0);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void outputCommentToWriter(Comment comment, OutputStream outputStream, int i2) throws IOException {
        super.outputCommentToWriter(comment, outputStream, 0);
    }
}
