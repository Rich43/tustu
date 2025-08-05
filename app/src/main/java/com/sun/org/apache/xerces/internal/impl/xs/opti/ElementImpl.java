package com.sun.org.apache.xerces.internal.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/ElementImpl.class */
public class ElementImpl extends DefaultElement {
    SchemaDOM schemaDOM;
    Attr[] attrs;
    int row;
    int col;
    int parentRow;
    int line;
    int column;
    int charOffset;
    String fAnnotation;
    String fSyntheticAnnotation;

    public ElementImpl(int line, int column, int offset) {
        this.row = -1;
        this.col = -1;
        this.parentRow = -1;
        this.nodeType = (short) 1;
        this.line = line;
        this.column = column;
        this.charOffset = offset;
    }

    public ElementImpl(int line, int column) {
        this(line, column, -1);
    }

    public ElementImpl(String prefix, String localpart, String rawname, String uri, int line, int column, int offset) {
        super(prefix, localpart, rawname, uri, (short) 1);
        this.row = -1;
        this.col = -1;
        this.parentRow = -1;
        this.line = line;
        this.column = column;
        this.charOffset = offset;
    }

    public ElementImpl(String prefix, String localpart, String rawname, String uri, int line, int column) {
        this(prefix, localpart, rawname, uri, line, column, -1);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Document getOwnerDocument() {
        return this.schemaDOM;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Node getParentNode() {
        return this.schemaDOM.relations[this.row][0];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public boolean hasChildNodes() {
        if (this.parentRow == -1) {
            return false;
        }
        return true;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Node getFirstChild() {
        if (this.parentRow == -1) {
            return null;
        }
        return this.schemaDOM.relations[this.parentRow][1];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Node getLastChild() {
        if (this.parentRow == -1) {
            return null;
        }
        int i2 = 1;
        while (i2 < this.schemaDOM.relations[this.parentRow].length) {
            if (this.schemaDOM.relations[this.parentRow][i2] != null) {
                i2++;
            } else {
                return this.schemaDOM.relations[this.parentRow][i2 - 1];
            }
        }
        if (i2 == 1) {
            i2++;
        }
        return this.schemaDOM.relations[this.parentRow][i2 - 1];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Node getPreviousSibling() {
        if (this.col == 1) {
            return null;
        }
        return this.schemaDOM.relations[this.row][this.col - 1];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Node getNextSibling() {
        if (this.col == this.schemaDOM.relations[this.row].length - 1) {
            return null;
        }
        return this.schemaDOM.relations[this.row][this.col + 1];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        return new NamedNodeMapImpl(this.attrs);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public boolean hasAttributes() {
        return this.attrs.length != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement, org.w3c.dom.Element
    public String getTagName() {
        return this.rawname;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement, org.w3c.dom.Element
    public String getAttribute(String name) {
        for (int i2 = 0; i2 < this.attrs.length; i2++) {
            if (this.attrs[i2].getName().equals(name)) {
                return this.attrs[i2].getValue();
            }
        }
        return "";
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement, org.w3c.dom.Element
    public Attr getAttributeNode(String name) {
        for (int i2 = 0; i2 < this.attrs.length; i2++) {
            if (this.attrs[i2].getName().equals(name)) {
                return this.attrs[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement, org.w3c.dom.Element
    public String getAttributeNS(String namespaceURI, String localName) {
        for (int i2 = 0; i2 < this.attrs.length; i2++) {
            if (this.attrs[i2].getLocalName().equals(localName) && nsEquals(this.attrs[i2].getNamespaceURI(), namespaceURI)) {
                return this.attrs[i2].getValue();
            }
        }
        return "";
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement, org.w3c.dom.Element
    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        for (int i2 = 0; i2 < this.attrs.length; i2++) {
            if (this.attrs[i2].getName().equals(localName) && nsEquals(this.attrs[i2].getNamespaceURI(), namespaceURI)) {
                return this.attrs[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement, org.w3c.dom.Element
    public boolean hasAttribute(String name) {
        for (int i2 = 0; i2 < this.attrs.length; i2++) {
            if (this.attrs[i2].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement, org.w3c.dom.Element
    public boolean hasAttributeNS(String namespaceURI, String localName) {
        for (int i2 = 0; i2 < this.attrs.length; i2++) {
            if (this.attrs[i2].getName().equals(localName) && nsEquals(this.attrs[i2].getNamespaceURI(), namespaceURI)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement, org.w3c.dom.Element
    public void setAttribute(String name, String value) throws DOMException {
        for (int i2 = 0; i2 < this.attrs.length; i2++) {
            if (this.attrs[i2].getName().equals(name)) {
                this.attrs[i2].setValue(value);
                return;
            }
        }
    }

    public int getLineNumber() {
        return this.line;
    }

    public int getColumnNumber() {
        return this.column;
    }

    public int getCharacterOffset() {
        return this.charOffset;
    }

    public String getAnnotation() {
        return this.fAnnotation;
    }

    public String getSyntheticAnnotation() {
        return this.fSyntheticAnnotation;
    }

    private static boolean nsEquals(String nsURI_1, String nsURI_2) {
        if (nsURI_1 == null) {
            return nsURI_2 == null;
        }
        return nsURI_1.equals(nsURI_2);
    }
}
