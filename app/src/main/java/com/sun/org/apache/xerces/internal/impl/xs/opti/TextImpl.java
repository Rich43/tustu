package com.sun.org.apache.xerces.internal.impl.xs.opti;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/TextImpl.class */
public class TextImpl extends DefaultText {
    String fData;
    SchemaDOM fSchemaDOM;
    int fRow;
    int fCol;

    public TextImpl(StringBuffer str, SchemaDOM sDOM, int row, int col) {
        this.fData = null;
        this.fSchemaDOM = null;
        this.fData = str.toString();
        this.fSchemaDOM = sDOM;
        this.fRow = row;
        this.fCol = col;
        this.uri = null;
        this.localpart = null;
        this.prefix = null;
        this.rawname = null;
        this.nodeType = (short) 3;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Node getParentNode() {
        return this.fSchemaDOM.relations[this.fRow][0];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Node getPreviousSibling() {
        if (this.fCol == 1) {
            return null;
        }
        return this.fSchemaDOM.relations[this.fRow][this.fCol - 1];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public Node getNextSibling() {
        if (this.fCol == this.fSchemaDOM.relations[this.fRow].length - 1) {
            return null;
        }
        return this.fSchemaDOM.relations[this.fRow][this.fCol + 1];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultText, org.w3c.dom.CharacterData
    public String getData() throws DOMException {
        return this.fData;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultText, org.w3c.dom.CharacterData
    public int getLength() {
        if (this.fData == null) {
            return 0;
        }
        return this.fData.length();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultText, org.w3c.dom.CharacterData
    public String substringData(int offset, int count) throws DOMException {
        if (this.fData == null) {
            return null;
        }
        if (count < 0 || offset < 0 || offset > this.fData.length()) {
            throw new DOMException((short) 1, "parameter error");
        }
        if (offset + count >= this.fData.length()) {
            return this.fData.substring(offset);
        }
        return this.fData.substring(offset, offset + count);
    }
}
