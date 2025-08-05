package com.sun.org.apache.xerces.internal.impl.xs.opti;

import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.util.ArrayList;
import java.util.Enumeration;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/SchemaDOM.class */
public class SchemaDOM extends DefaultDocument {
    static final int relationsRowResizeFactor = 15;
    static final int relationsColResizeFactor = 10;
    NodeImpl[][] relations;
    ElementImpl parent;
    int currLoc;
    int nextFreeLoc;
    boolean hidden;
    boolean inCDATA;
    private StringBuffer fAnnotationBuffer = null;

    public SchemaDOM() {
        reset();
    }

    public ElementImpl startElement(QName element, XMLAttributes attributes, int line, int column, int offset) {
        ElementImpl node = new ElementImpl(line, column, offset);
        processElement(element, attributes, node);
        this.parent = node;
        return node;
    }

    public ElementImpl emptyElement(QName element, XMLAttributes attributes, int line, int column, int offset) {
        ElementImpl node = new ElementImpl(line, column, offset);
        processElement(element, attributes, node);
        return node;
    }

    public ElementImpl startElement(QName element, XMLAttributes attributes, int line, int column) {
        return startElement(element, attributes, line, column, -1);
    }

    public ElementImpl emptyElement(QName element, XMLAttributes attributes, int line, int column) {
        return emptyElement(element, attributes, line, column, -1);
    }

    private void processElement(QName element, XMLAttributes attributes, ElementImpl node) {
        node.prefix = element.prefix;
        node.localpart = element.localpart;
        node.rawname = element.rawname;
        node.uri = element.uri;
        node.schemaDOM = this;
        Attr[] attrs = new Attr[attributes.getLength()];
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            attrs[i2] = new AttrImpl(node, attributes.getPrefix(i2), attributes.getLocalName(i2), attributes.getQName(i2), attributes.getURI(i2), attributes.getValue(i2));
        }
        node.attrs = attrs;
        if (this.nextFreeLoc == this.relations.length) {
            resizeRelations();
        }
        if (this.relations[this.currLoc][0] != this.parent) {
            this.relations[this.nextFreeLoc][0] = this.parent;
            int i3 = this.nextFreeLoc;
            this.nextFreeLoc = i3 + 1;
            this.currLoc = i3;
        }
        boolean foundPlace = false;
        int i4 = 1;
        while (true) {
            if (i4 >= this.relations[this.currLoc].length) {
                break;
            }
            if (this.relations[this.currLoc][i4] != null) {
                i4++;
            } else {
                foundPlace = true;
                break;
            }
        }
        if (!foundPlace) {
            resizeRelations(this.currLoc);
        }
        this.relations[this.currLoc][i4] = node;
        this.parent.parentRow = this.currLoc;
        node.row = this.currLoc;
        node.col = i4;
    }

    public void endElement() {
        this.currLoc = this.parent.row;
        this.parent = (ElementImpl) this.relations[this.currLoc][0];
    }

    void comment(XMLString text) {
        this.fAnnotationBuffer.append("<!--");
        if (text.length > 0) {
            this.fAnnotationBuffer.append(text.ch, text.offset, text.length);
        }
        this.fAnnotationBuffer.append("-->");
    }

    void processingInstruction(String target, XMLString data) {
        this.fAnnotationBuffer.append("<?").append(target);
        if (data.length > 0) {
            this.fAnnotationBuffer.append(' ').append(data.ch, data.offset, data.length);
        }
        this.fAnnotationBuffer.append("?>");
    }

    void characters(XMLString text) {
        if (!this.inCDATA) {
            StringBuffer annotationBuffer = this.fAnnotationBuffer;
            for (int i2 = text.offset; i2 < text.offset + text.length; i2++) {
                char ch = text.ch[i2];
                if (ch == '&') {
                    annotationBuffer.append(SerializerConstants.ENTITY_AMP);
                } else if (ch == '<') {
                    annotationBuffer.append(SerializerConstants.ENTITY_LT);
                } else if (ch == '>') {
                    annotationBuffer.append(SerializerConstants.ENTITY_GT);
                } else if (ch == '\r') {
                    annotationBuffer.append("&#xD;");
                } else {
                    annotationBuffer.append(ch);
                }
            }
            return;
        }
        this.fAnnotationBuffer.append(text.ch, text.offset, text.length);
    }

    void charactersRaw(String text) {
        this.fAnnotationBuffer.append(text);
    }

    void endAnnotation(QName elemName, ElementImpl annotation) {
        this.fAnnotationBuffer.append("\n</").append(elemName.rawname).append(">");
        annotation.fAnnotation = this.fAnnotationBuffer.toString();
        this.fAnnotationBuffer = null;
    }

    void endAnnotationElement(QName elemName) {
        endAnnotationElement(elemName.rawname);
    }

    void endAnnotationElement(String elemRawName) {
        this.fAnnotationBuffer.append("</").append(elemRawName).append(">");
    }

    void endSyntheticAnnotationElement(QName elemName, boolean complete) {
        endSyntheticAnnotationElement(elemName.rawname, complete);
    }

    void endSyntheticAnnotationElement(String elemRawName, boolean complete) {
        if (complete) {
            this.fAnnotationBuffer.append("\n</").append(elemRawName).append(">");
            this.parent.fSyntheticAnnotation = this.fAnnotationBuffer.toString();
            this.fAnnotationBuffer = null;
            return;
        }
        this.fAnnotationBuffer.append("</").append(elemRawName).append(">");
    }

    void startAnnotationCDATA() {
        this.inCDATA = true;
        this.fAnnotationBuffer.append("<![CDATA[");
    }

    void endAnnotationCDATA() {
        this.fAnnotationBuffer.append("]]>");
        this.inCDATA = false;
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl[], com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl[][], java.lang.Object] */
    private void resizeRelations() {
        ?? r0 = new NodeImpl[this.relations.length + 15];
        System.arraycopy(this.relations, 0, r0, 0, this.relations.length);
        for (int i2 = this.relations.length; i2 < r0.length; i2++) {
            r0[i2] = new NodeImpl[10];
        }
        this.relations = r0;
    }

    private void resizeRelations(int i2) {
        NodeImpl[] temp = new NodeImpl[this.relations[i2].length + 10];
        System.arraycopy(this.relations[i2], 0, temp, 0, this.relations[i2].length);
        this.relations[i2] = temp;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl[], com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl[][]] */
    public void reset() {
        if (this.relations != null) {
            for (int i2 = 0; i2 < this.relations.length; i2++) {
                for (int j2 = 0; j2 < this.relations[i2].length; j2++) {
                    this.relations[i2][j2] = null;
                }
            }
        }
        this.relations = new NodeImpl[15];
        this.parent = new ElementImpl(0, 0, 0);
        this.parent.rawname = "DOCUMENT_NODE";
        this.currLoc = 0;
        this.nextFreeLoc = 1;
        this.inCDATA = false;
        for (int i3 = 0; i3 < 15; i3++) {
            this.relations[i3] = new NodeImpl[10];
        }
        this.relations[this.currLoc][0] = this.parent;
    }

    public void printDOM() {
    }

    public static void traverse(Node node, int depth) {
        indent(depth);
        System.out.print("<" + node.getNodeName());
        if (node.hasAttributes()) {
            NamedNodeMap attrs = node.getAttributes();
            for (int i2 = 0; i2 < attrs.getLength(); i2++) {
                System.out.print(Constants.INDENT + ((Attr) attrs.item(i2)).getName() + "=\"" + ((Attr) attrs.item(i2)).getValue() + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        }
        if (node.hasChildNodes()) {
            System.out.println(">");
            int depth2 = depth + 4;
            Node firstChild = node.getFirstChild();
            while (true) {
                Node child = firstChild;
                if (child != null) {
                    traverse(child, depth2);
                    firstChild = child.getNextSibling();
                } else {
                    indent(depth2 - 4);
                    System.out.println("</" + node.getNodeName() + ">");
                    return;
                }
            }
        } else {
            System.out.println("/>");
        }
    }

    public static void indent(int amount) {
        for (int i2 = 0; i2 < amount; i2++) {
            System.out.print(' ');
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultDocument, org.w3c.dom.Document
    public Element getDocumentElement() {
        return (ElementImpl) this.relations[0][1];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultDocument, org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return SchemaDOMImplementation.getDOMImplementation();
    }

    void startAnnotation(QName elemName, XMLAttributes attributes, NamespaceContext namespaceContext) {
        startAnnotation(elemName.rawname, attributes, namespaceContext);
    }

    void startAnnotation(String elemRawName, XMLAttributes attributes, NamespaceContext namespaceContext) {
        if (this.fAnnotationBuffer == null) {
            this.fAnnotationBuffer = new StringBuffer(256);
        }
        this.fAnnotationBuffer.append("<").append(elemRawName).append(" ");
        ArrayList namespaces = new ArrayList();
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            String aValue = attributes.getValue(i2);
            String aPrefix = attributes.getPrefix(i2);
            String aQName = attributes.getQName(i2);
            if (aPrefix == XMLSymbols.PREFIX_XMLNS || aQName == XMLSymbols.PREFIX_XMLNS) {
                namespaces.add(aPrefix == XMLSymbols.PREFIX_XMLNS ? attributes.getLocalName(i2) : XMLSymbols.EMPTY_STRING);
            }
            this.fAnnotationBuffer.append(aQName).append("=\"").append(processAttValue(aValue)).append("\" ");
        }
        Enumeration currPrefixes = namespaceContext.getAllPrefixes();
        while (currPrefixes.hasMoreElements()) {
            String prefix = (String) currPrefixes.nextElement2();
            String uri = namespaceContext.getURI(prefix);
            if (uri == null) {
                uri = XMLSymbols.EMPTY_STRING;
            }
            if (!namespaces.contains(prefix)) {
                if (prefix == XMLSymbols.EMPTY_STRING) {
                    this.fAnnotationBuffer.append("xmlns").append("=\"").append(processAttValue(uri)).append("\" ");
                } else {
                    this.fAnnotationBuffer.append("xmlns:").append(prefix).append("=\"").append(processAttValue(uri)).append("\" ");
                }
            }
        }
        this.fAnnotationBuffer.append(">\n");
    }

    void startAnnotationElement(QName elemName, XMLAttributes attributes) {
        startAnnotationElement(elemName.rawname, attributes);
    }

    void startAnnotationElement(String elemRawName, XMLAttributes attributes) {
        this.fAnnotationBuffer.append("<").append(elemRawName);
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            String aValue = attributes.getValue(i2);
            this.fAnnotationBuffer.append(" ").append(attributes.getQName(i2)).append("=\"").append(processAttValue(aValue)).append(PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        this.fAnnotationBuffer.append(">");
    }

    private static String processAttValue(String original) {
        int length = original.length();
        for (int i2 = 0; i2 < length; i2++) {
            char currChar = original.charAt(i2);
            if (currChar == '\"' || currChar == '<' || currChar == '&' || currChar == '\t' || currChar == '\n' || currChar == '\r') {
                return escapeAttValue(original, i2);
            }
        }
        return original;
    }

    private static String escapeAttValue(String original, int from) {
        int length = original.length();
        StringBuffer newVal = new StringBuffer(length);
        newVal.append(original.substring(0, from));
        for (int i2 = from; i2 < length; i2++) {
            char currChar = original.charAt(i2);
            if (currChar == '\"') {
                newVal.append(SerializerConstants.ENTITY_QUOT);
            } else if (currChar == '<') {
                newVal.append(SerializerConstants.ENTITY_LT);
            } else if (currChar == '&') {
                newVal.append(SerializerConstants.ENTITY_AMP);
            } else if (currChar == '\t') {
                newVal.append("&#x9;");
            } else if (currChar == '\n') {
                newVal.append(SerializerConstants.ENTITY_CRLF);
            } else if (currChar == '\r') {
                newVal.append("&#xD;");
            } else {
                newVal.append(currChar);
            }
        }
        return newVal.toString();
    }
}
