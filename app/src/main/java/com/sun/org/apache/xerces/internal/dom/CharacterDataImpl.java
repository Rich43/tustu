package com.sun.org.apache.xerces.internal.dom;

import java.util.MissingResourceException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/CharacterDataImpl.class */
public abstract class CharacterDataImpl extends ChildNode {
    static final long serialVersionUID = 7931170150428474230L;
    protected String data;
    private static transient NodeList singletonNodeList = new NodeList() { // from class: com.sun.org.apache.xerces.internal.dom.CharacterDataImpl.1
        @Override // org.w3c.dom.NodeList
        public Node item(int index) {
            return null;
        }

        @Override // org.w3c.dom.NodeList
        public int getLength() {
            return 0;
        }
    };

    public CharacterDataImpl() {
    }

    protected CharacterDataImpl(CoreDocumentImpl ownerDocument, String data) {
        super(ownerDocument);
        this.data = data;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public NodeList getChildNodes() {
        return singletonNodeList;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeValue() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.data;
    }

    protected void setNodeValueInternal(String value) throws MissingResourceException {
        setNodeValueInternal(value, false);
    }

    protected void setNodeValueInternal(String value, boolean replace) throws MissingResourceException {
        CoreDocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        String oldvalue = this.data;
        ownerDocument.modifyingCharacterData(this, replace);
        this.data = value;
        ownerDocument.modifiedCharacterData(this, oldvalue, value, replace);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void setNodeValue(String value) throws MissingResourceException {
        setNodeValueInternal(value);
        ownerDocument().replacedText(this);
    }

    public String getData() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.data;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.NodeList
    public int getLength() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.data.length();
    }

    public void appendData(String data) throws MissingResourceException {
        if (isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        if (data == null) {
            return;
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        setNodeValue(this.data + data);
    }

    public void deleteData(int offset, int count) throws DOMException, MissingResourceException {
        internalDeleteData(offset, count, false);
    }

    void internalDeleteData(int offset, int count, boolean replace) throws DOMException, MissingResourceException {
        CoreDocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (count < 0) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INDEX_SIZE_ERR", null);
                throw new DOMException((short) 1, msg2);
            }
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        int tailLength = Math.max((this.data.length() - count) - offset, 0);
        try {
            String value = this.data.substring(0, offset) + (tailLength > 0 ? this.data.substring(offset + count, offset + count + tailLength) : "");
            setNodeValueInternal(value, replace);
            ownerDocument.deletedText(this, offset, count);
        } catch (StringIndexOutOfBoundsException e2) {
            String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INDEX_SIZE_ERR", null);
            throw new DOMException((short) 1, msg3);
        }
    }

    public void insertData(int offset, String data) throws DOMException, MissingResourceException {
        internalInsertData(offset, data, false);
    }

    void internalInsertData(int offset, String data, boolean replace) throws DOMException, MissingResourceException {
        CoreDocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        try {
            String value = new StringBuffer(this.data).insert(offset, data).toString();
            setNodeValueInternal(value, replace);
            ownerDocument.insertedText(this, offset, data.length());
        } catch (StringIndexOutOfBoundsException e2) {
            String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INDEX_SIZE_ERR", null);
            throw new DOMException((short) 1, msg2);
        }
    }

    public void replaceData(int offset, int count, String data) throws DOMException, MissingResourceException {
        CoreDocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        ownerDocument.replacingData(this);
        String oldvalue = this.data;
        internalDeleteData(offset, count, true);
        internalInsertData(offset, data, true);
        ownerDocument.replacedCharacterData(this, oldvalue, this.data);
    }

    public void setData(String value) throws DOMException, MissingResourceException {
        setNodeValue(value);
    }

    public String substringData(int offset, int count) throws DOMException, MissingResourceException {
        if (needsSyncData()) {
            synchronizeData();
        }
        int length = this.data.length();
        if (count < 0 || offset < 0 || offset > length - 1) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INDEX_SIZE_ERR", null);
            throw new DOMException((short) 1, msg);
        }
        int tailIndex = Math.min(offset + count, length);
        return this.data.substring(offset, tailIndex);
    }
}
