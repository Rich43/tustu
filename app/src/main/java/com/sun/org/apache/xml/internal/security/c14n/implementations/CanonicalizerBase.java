package com.sun.org.apache.xml.internal.security.c14n.implementations;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi;
import com.sun.org.apache.xml.internal.security.c14n.helper.AttrCompare;
import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.UnsyncByteArrayOutputStream;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/CanonicalizerBase.class */
public abstract class CanonicalizerBase extends CanonicalizerSpi {
    public static final String XML = "xml";
    public static final String XMLNS = "xmlns";
    public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    public static final String XML_LANG_URI = "http://www.w3.org/XML/1998/namespace";
    protected static final AttrCompare COMPARE = new AttrCompare();
    private static final byte[] END_PI = {63, 62};
    private static final byte[] BEGIN_PI = {60, 63};
    private static final byte[] END_COMM = {45, 45, 62};
    private static final byte[] BEGIN_COMM = {60, 33, 45, 45};
    private static final byte[] XA = {38, 35, 120, 65, 59};
    private static final byte[] X9 = {38, 35, 120, 57, 59};
    private static final byte[] QUOT = {38, 113, 117, 111, 116, 59};
    private static final byte[] XD = {38, 35, 120, 68, 59};
    private static final byte[] GT = {38, 103, 116, 59};
    private static final byte[] LT = {38, 108, 116, 59};
    private static final byte[] END_TAG = {60, 47};
    private static final byte[] AMP = {38, 97, 109, 112, 59};
    private static final byte[] EQUALS_STR = {61, 34};
    protected static final int NODE_BEFORE_DOCUMENT_ELEMENT = -1;
    protected static final int NODE_NOT_BEFORE_OR_AFTER_DOCUMENT_ELEMENT = 0;
    protected static final int NODE_AFTER_DOCUMENT_ELEMENT = 1;
    private List<NodeFilter> nodeFilter;
    private boolean includeComments;
    private Set<Node> xpathNodeSet;
    private Node excludeNode;
    private OutputStream writer = new ByteArrayOutputStream();
    private Attr nullNode;

    abstract void outputAttributes(Element element, NameSpaceSymbTable nameSpaceSymbTable, Map<String, byte[]> map) throws CanonicalizationException, DOMException, IOException;

    abstract void outputAttributesSubtree(Element element, NameSpaceSymbTable nameSpaceSymbTable, Map<String, byte[]> map) throws CanonicalizationException, DOMException, IOException;

    abstract void circumventBugIfNeeded(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException, ParserConfigurationException, SAXException, IOException;

    public CanonicalizerBase(boolean z2) {
        this.includeComments = z2;
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeSubTree(Node node) throws CanonicalizationException {
        return engineCanonicalizeSubTree(node, (Node) null);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeXPathNodeSet(Set<Node> set) throws CanonicalizationException {
        this.xpathNodeSet = set;
        return engineCanonicalizeXPathNodeSetInternal(XMLUtils.getOwnerDocument(this.xpathNodeSet));
    }

    public byte[] engineCanonicalize(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException {
        try {
            if (xMLSignatureInput.isExcludeComments()) {
                this.includeComments = false;
            }
            if (xMLSignatureInput.isOctetStream()) {
                return engineCanonicalize(xMLSignatureInput.getBytes());
            }
            if (xMLSignatureInput.isElement()) {
                return engineCanonicalizeSubTree(xMLSignatureInput.getSubNode(), xMLSignatureInput.getExcludeNode());
            }
            if (xMLSignatureInput.isNodeSet()) {
                this.nodeFilter = xMLSignatureInput.getNodeFilters();
                circumventBugIfNeeded(xMLSignatureInput);
                if (xMLSignatureInput.getSubNode() != null) {
                    return engineCanonicalizeXPathNodeSetInternal(xMLSignatureInput.getSubNode());
                }
                return engineCanonicalizeXPathNodeSet(xMLSignatureInput.getNodeSet());
            }
            return null;
        } catch (IOException e2) {
            throw new CanonicalizationException(e2);
        } catch (ParserConfigurationException e3) {
            throw new CanonicalizationException(e3);
        } catch (SAXException e4) {
            throw new CanonicalizationException(e4);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public void setWriter(OutputStream outputStream) {
        this.writer = outputStream;
    }

    protected OutputStream getWriter() {
        return this.writer;
    }

    protected byte[] engineCanonicalizeSubTree(Node node, Node node2) throws CanonicalizationException, DOMException, CloneNotSupportedException {
        this.excludeNode = node2;
        try {
            NameSpaceSymbTable nameSpaceSymbTable = new NameSpaceSymbTable();
            int i2 = -1;
            if (node != null && 1 == node.getNodeType()) {
                getParentNameSpaces((Element) node, nameSpaceSymbTable);
                i2 = 0;
            }
            canonicalizeSubTree(node, nameSpaceSymbTable, node, i2);
            this.writer.flush();
            if (this.writer instanceof ByteArrayOutputStream) {
                byte[] byteArray = ((ByteArrayOutputStream) this.writer).toByteArray();
                if (this.reset) {
                    ((ByteArrayOutputStream) this.writer).reset();
                } else {
                    this.writer.close();
                }
                return byteArray;
            }
            if (this.writer instanceof UnsyncByteArrayOutputStream) {
                byte[] byteArray2 = ((UnsyncByteArrayOutputStream) this.writer).toByteArray();
                if (this.reset) {
                    ((UnsyncByteArrayOutputStream) this.writer).reset();
                } else {
                    this.writer.close();
                }
                return byteArray2;
            }
            this.writer.close();
            return null;
        } catch (UnsupportedEncodingException e2) {
            throw new CanonicalizationException(e2);
        } catch (IOException e3) {
            throw new CanonicalizationException(e3);
        }
    }

    protected final void canonicalizeSubTree(Node node, NameSpaceSymbTable nameSpaceSymbTable, Node node2, int i2) throws CanonicalizationException, DOMException, IOException, CloneNotSupportedException {
        if (node == null || isVisibleInt(node) == -1) {
            return;
        }
        Node nextSibling = null;
        Node parentNode = null;
        OutputStream outputStream = this.writer;
        Node node3 = this.excludeNode;
        boolean z2 = this.includeComments;
        HashMap map = new HashMap();
        while (true) {
            switch (node.getNodeType()) {
                case 1:
                    i2 = 0;
                    if (node != node3) {
                        Element element = (Element) node;
                        nameSpaceSymbTable.outputNodePush();
                        outputStream.write(60);
                        String tagName = element.getTagName();
                        UtfHelpper.writeByte(tagName, outputStream, map);
                        outputAttributesSubtree(element, nameSpaceSymbTable, map);
                        outputStream.write(62);
                        nextSibling = node.getFirstChild();
                        if (nextSibling == null) {
                            outputStream.write((byte[]) END_TAG.clone());
                            UtfHelpper.writeStringToUtf8(tagName, outputStream);
                            outputStream.write(62);
                            nameSpaceSymbTable.outputNodePop();
                            if (parentNode != null) {
                                nextSibling = node.getNextSibling();
                                break;
                            }
                        } else {
                            parentNode = element;
                            break;
                        }
                    }
                    break;
                case 2:
                case 6:
                case 12:
                    throw new CanonicalizationException(Constants.ELEMNAME_EMPTY_STRING, new Object[]{"illegal node type during traversal"});
                case 3:
                case 4:
                    outputTextToWriter(node.getNodeValue(), outputStream);
                    break;
                case 7:
                    outputPItoWriter((ProcessingInstruction) node, outputStream, i2);
                    break;
                case 8:
                    if (z2) {
                        outputCommentToWriter((Comment) node, outputStream, i2);
                        break;
                    }
                    break;
                case 9:
                case 11:
                    nameSpaceSymbTable.outputNodePush();
                    nextSibling = node.getFirstChild();
                    break;
            }
            while (nextSibling == null && parentNode != null) {
                outputStream.write((byte[]) END_TAG.clone());
                UtfHelpper.writeByte(((Element) parentNode).getTagName(), outputStream, map);
                outputStream.write(62);
                nameSpaceSymbTable.outputNodePop();
                if (parentNode == node2) {
                    return;
                }
                nextSibling = parentNode.getNextSibling();
                parentNode = parentNode.getParentNode();
                if (parentNode == null || 1 != parentNode.getNodeType()) {
                    i2 = 1;
                    parentNode = null;
                }
            }
            if (nextSibling == null) {
                return;
            }
            node = nextSibling;
            nextSibling = node.getNextSibling();
        }
    }

    private byte[] engineCanonicalizeXPathNodeSetInternal(Node node) throws CanonicalizationException, DOMException, CloneNotSupportedException {
        try {
            canonicalizeXPathNodeSet(node, node);
            this.writer.flush();
            if (this.writer instanceof ByteArrayOutputStream) {
                byte[] byteArray = ((ByteArrayOutputStream) this.writer).toByteArray();
                if (this.reset) {
                    ((ByteArrayOutputStream) this.writer).reset();
                } else {
                    this.writer.close();
                }
                return byteArray;
            }
            if (this.writer instanceof UnsyncByteArrayOutputStream) {
                byte[] byteArray2 = ((UnsyncByteArrayOutputStream) this.writer).toByteArray();
                if (this.reset) {
                    ((UnsyncByteArrayOutputStream) this.writer).reset();
                } else {
                    this.writer.close();
                }
                return byteArray2;
            }
            this.writer.close();
            return null;
        } catch (UnsupportedEncodingException e2) {
            throw new CanonicalizationException(e2);
        } catch (IOException e3) {
            throw new CanonicalizationException(e3);
        }
    }

    protected final void canonicalizeXPathNodeSet(Node node, Node node2) throws CanonicalizationException, DOMException, IOException, CloneNotSupportedException {
        if (isVisibleInt(node) == -1) {
            return;
        }
        NameSpaceSymbTable nameSpaceSymbTable = new NameSpaceSymbTable();
        if (node != null && 1 == node.getNodeType()) {
            getParentNameSpaces((Element) node, nameSpaceSymbTable);
        }
        if (node == null) {
            return;
        }
        Node nextSibling = null;
        Node parentNode = null;
        int i2 = -1;
        HashMap map = new HashMap();
        while (true) {
            switch (node.getNodeType()) {
                case 1:
                    i2 = 0;
                    Element element = (Element) node;
                    String tagName = null;
                    int iIsVisibleDO = isVisibleDO(node, nameSpaceSymbTable.getLevel());
                    if (iIsVisibleDO == -1) {
                        nextSibling = node.getNextSibling();
                        break;
                    } else {
                        boolean z2 = iIsVisibleDO == 1;
                        if (z2) {
                            nameSpaceSymbTable.outputNodePush();
                            this.writer.write(60);
                            tagName = element.getTagName();
                            UtfHelpper.writeByte(tagName, this.writer, map);
                        } else {
                            nameSpaceSymbTable.push();
                        }
                        outputAttributes(element, nameSpaceSymbTable, map);
                        if (z2) {
                            this.writer.write(62);
                        }
                        nextSibling = node.getFirstChild();
                        if (nextSibling == null) {
                            if (z2) {
                                this.writer.write((byte[]) END_TAG.clone());
                                UtfHelpper.writeByte(tagName, this.writer, map);
                                this.writer.write(62);
                                nameSpaceSymbTable.outputNodePop();
                            } else {
                                nameSpaceSymbTable.pop();
                            }
                            if (parentNode != null) {
                                nextSibling = node.getNextSibling();
                                break;
                            }
                        } else {
                            parentNode = element;
                            break;
                        }
                    }
                    break;
                case 2:
                case 6:
                case 12:
                    throw new CanonicalizationException(Constants.ELEMNAME_EMPTY_STRING, new Object[]{"illegal node type during traversal"});
                case 3:
                case 4:
                    if (isVisible(node)) {
                        outputTextToWriter(node.getNodeValue(), this.writer);
                        Node nextSibling2 = node.getNextSibling();
                        while (true) {
                            Node node3 = nextSibling2;
                            if (node3 == null || !(node3.getNodeType() == 3 || node3.getNodeType() == 4)) {
                                break;
                            } else {
                                outputTextToWriter(node3.getNodeValue(), this.writer);
                                nextSibling = node3.getNextSibling();
                                nextSibling2 = node3.getNextSibling();
                            }
                        }
                    }
                    break;
                case 7:
                    if (isVisible(node)) {
                        outputPItoWriter((ProcessingInstruction) node, this.writer, i2);
                        break;
                    }
                    break;
                case 8:
                    if (this.includeComments && isVisibleDO(node, nameSpaceSymbTable.getLevel()) == 1) {
                        outputCommentToWriter((Comment) node, this.writer, i2);
                        break;
                    }
                    break;
                case 9:
                case 11:
                    nameSpaceSymbTable.outputNodePush();
                    nextSibling = node.getFirstChild();
                    break;
            }
            while (nextSibling == null && parentNode != null) {
                if (isVisible(parentNode)) {
                    this.writer.write((byte[]) END_TAG.clone());
                    UtfHelpper.writeByte(((Element) parentNode).getTagName(), this.writer, map);
                    this.writer.write(62);
                    nameSpaceSymbTable.outputNodePop();
                } else {
                    nameSpaceSymbTable.pop();
                }
                if (parentNode == node2) {
                    return;
                }
                nextSibling = parentNode.getNextSibling();
                parentNode = parentNode.getParentNode();
                if (parentNode == null || 1 != parentNode.getNodeType()) {
                    parentNode = null;
                    i2 = 1;
                }
            }
            if (nextSibling == null) {
                return;
            }
            node = nextSibling;
            nextSibling = node.getNextSibling();
        }
    }

    protected int isVisibleDO(Node node, int i2) {
        if (this.nodeFilter != null) {
            Iterator<NodeFilter> it = this.nodeFilter.iterator();
            while (it.hasNext()) {
                int iIsNodeIncludeDO = it.next().isNodeIncludeDO(node, i2);
                if (iIsNodeIncludeDO != 1) {
                    return iIsNodeIncludeDO;
                }
            }
        }
        if (this.xpathNodeSet != null && !this.xpathNodeSet.contains(node)) {
            return 0;
        }
        return 1;
    }

    protected int isVisibleInt(Node node) {
        if (this.nodeFilter != null) {
            Iterator<NodeFilter> it = this.nodeFilter.iterator();
            while (it.hasNext()) {
                int iIsNodeInclude = it.next().isNodeInclude(node);
                if (iIsNodeInclude != 1) {
                    return iIsNodeInclude;
                }
            }
        }
        if (this.xpathNodeSet != null && !this.xpathNodeSet.contains(node)) {
            return 0;
        }
        return 1;
    }

    protected boolean isVisible(Node node) {
        if (this.nodeFilter != null) {
            Iterator<NodeFilter> it = this.nodeFilter.iterator();
            while (it.hasNext()) {
                if (it.next().isNodeInclude(node) != 1) {
                    return false;
                }
            }
        }
        if (this.xpathNodeSet != null && !this.xpathNodeSet.contains(node)) {
            return false;
        }
        return true;
    }

    protected void handleParent(Element element, NameSpaceSymbTable nameSpaceSymbTable) throws DOMException {
        String str;
        if (!element.hasAttributes() && element.getNamespaceURI() == null) {
            return;
        }
        NamedNodeMap attributes = element.getAttributes();
        int length = attributes.getLength();
        for (int i2 = 0; i2 < length; i2++) {
            Attr attr = (Attr) attributes.item(i2);
            String localName = attr.getLocalName();
            String nodeValue = attr.getNodeValue();
            if ("http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI()) && (!"xml".equals(localName) || !"http://www.w3.org/XML/1998/namespace".equals(nodeValue))) {
                nameSpaceSymbTable.addMapping(localName, nodeValue, attr);
            }
        }
        if (element.getNamespaceURI() != null) {
            String prefix = element.getPrefix();
            String namespaceURI = element.getNamespaceURI();
            if (prefix == null || prefix.equals("")) {
                prefix = "xmlns";
                str = "xmlns";
            } else {
                str = "xmlns:" + prefix;
            }
            Attr attrCreateAttributeNS = element.getOwnerDocument().createAttributeNS("http://www.w3.org/2000/xmlns/", str);
            attrCreateAttributeNS.setValue(namespaceURI);
            nameSpaceSymbTable.addMapping(prefix, namespaceURI, attrCreateAttributeNS);
        }
    }

    protected final void getParentNameSpaces(Element element, NameSpaceSymbTable nameSpaceSymbTable) throws DOMException {
        Node parentNode = element.getParentNode();
        if (parentNode == null || 1 != parentNode.getNodeType()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        Node parentNode2 = parentNode;
        while (true) {
            Node node = parentNode2;
            if (node == null || 1 != node.getNodeType()) {
                break;
            }
            arrayList.add((Element) node);
            parentNode2 = node.getParentNode();
        }
        ListIterator<E> listIterator = arrayList.listIterator(arrayList.size());
        while (listIterator.hasPrevious()) {
            handleParent((Element) listIterator.previous(), nameSpaceSymbTable);
        }
        arrayList.clear();
        Attr mappingWithoutRendered = nameSpaceSymbTable.getMappingWithoutRendered("xmlns");
        if (mappingWithoutRendered != null && "".equals(mappingWithoutRendered.getValue())) {
            nameSpaceSymbTable.addMappingAndRender("xmlns", "", getNullNode(mappingWithoutRendered.getOwnerDocument()));
        }
    }

    protected static final void outputAttrToWriter(String str, String str2, OutputStream outputStream, Map<String, byte[]> map) throws IOException, CloneNotSupportedException {
        Object objClone;
        outputStream.write(32);
        UtfHelpper.writeByte(str, outputStream, map);
        outputStream.write((byte[]) EQUALS_STR.clone());
        int length = str2.length();
        int iCharCount = 0;
        while (iCharCount < length) {
            int iCodePointAt = str2.codePointAt(iCharCount);
            iCharCount += Character.charCount(iCodePointAt);
            switch (iCodePointAt) {
                case 9:
                    objClone = X9.clone();
                    break;
                case 10:
                    objClone = XA.clone();
                    break;
                case 13:
                    objClone = XD.clone();
                    break;
                case 34:
                    objClone = QUOT.clone();
                    break;
                case 38:
                    objClone = AMP.clone();
                    break;
                case 60:
                    objClone = LT.clone();
                    break;
                default:
                    if (iCodePointAt < 128) {
                        outputStream.write(iCodePointAt);
                    } else {
                        UtfHelpper.writeCodePointToUtf8(iCodePointAt, outputStream);
                        continue;
                    }
            }
            outputStream.write((byte[]) objClone);
        }
        outputStream.write(34);
    }

    protected void outputPItoWriter(ProcessingInstruction processingInstruction, OutputStream outputStream, int i2) throws IOException {
        if (i2 == 1) {
            outputStream.write(10);
        }
        outputStream.write((byte[]) BEGIN_PI.clone());
        String target = processingInstruction.getTarget();
        int length = target.length();
        int iCharCount = 0;
        while (iCharCount < length) {
            int iCodePointAt = target.codePointAt(iCharCount);
            iCharCount += Character.charCount(iCodePointAt);
            if (iCodePointAt == 13) {
                outputStream.write((byte[]) XD.clone());
            } else if (iCodePointAt < 128) {
                outputStream.write(iCodePointAt);
            } else {
                UtfHelpper.writeCodePointToUtf8(iCodePointAt, outputStream);
            }
        }
        String data = processingInstruction.getData();
        int length2 = data.length();
        if (length2 > 0) {
            outputStream.write(32);
            int iCharCount2 = 0;
            while (iCharCount2 < length2) {
                int iCodePointAt2 = data.codePointAt(iCharCount2);
                iCharCount2 += Character.charCount(iCodePointAt2);
                if (iCodePointAt2 == 13) {
                    outputStream.write((byte[]) XD.clone());
                } else {
                    UtfHelpper.writeCodePointToUtf8(iCodePointAt2, outputStream);
                }
            }
        }
        outputStream.write((byte[]) END_PI.clone());
        if (i2 == -1) {
            outputStream.write(10);
        }
    }

    protected void outputCommentToWriter(Comment comment, OutputStream outputStream, int i2) throws IOException {
        if (i2 == 1) {
            outputStream.write(10);
        }
        outputStream.write((byte[]) BEGIN_COMM.clone());
        String data = comment.getData();
        int length = data.length();
        int iCharCount = 0;
        while (iCharCount < length) {
            int iCodePointAt = data.codePointAt(iCharCount);
            iCharCount += Character.charCount(iCodePointAt);
            if (iCodePointAt == 13) {
                outputStream.write((byte[]) XD.clone());
            } else if (iCodePointAt < 128) {
                outputStream.write(iCodePointAt);
            } else {
                UtfHelpper.writeCodePointToUtf8(iCodePointAt, outputStream);
            }
        }
        outputStream.write((byte[]) END_COMM.clone());
        if (i2 == -1) {
            outputStream.write(10);
        }
    }

    protected static final void outputTextToWriter(String str, OutputStream outputStream) throws IOException, CloneNotSupportedException {
        Object objClone;
        int length = str.length();
        int iCharCount = 0;
        while (iCharCount < length) {
            int iCodePointAt = str.codePointAt(iCharCount);
            iCharCount += Character.charCount(iCodePointAt);
            switch (iCodePointAt) {
                case 13:
                    objClone = XD.clone();
                    break;
                case 38:
                    objClone = AMP.clone();
                    break;
                case 60:
                    objClone = LT.clone();
                    break;
                case 62:
                    objClone = GT.clone();
                    break;
                default:
                    if (iCodePointAt < 128) {
                        outputStream.write(iCodePointAt);
                    } else {
                        UtfHelpper.writeCodePointToUtf8(iCodePointAt, outputStream);
                        continue;
                    }
            }
            outputStream.write((byte[]) objClone);
        }
    }

    protected Attr getNullNode(Document document) {
        if (this.nullNode == null) {
            try {
                this.nullNode = document.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns");
                this.nullNode.setValue("");
            } catch (Exception e2) {
                throw new RuntimeException("Unable to create nullNode: " + ((Object) e2));
            }
        }
        return this.nullNode;
    }
}
