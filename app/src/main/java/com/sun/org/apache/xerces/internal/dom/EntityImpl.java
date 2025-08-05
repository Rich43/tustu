package com.sun.org.apache.xerces.internal.dom;

import java.util.MissingResourceException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Entity;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/EntityImpl.class */
public class EntityImpl extends ParentNode implements Entity {
    static final long serialVersionUID = -3575760943444303423L;
    protected String name;
    protected String publicId;
    protected String systemId;
    protected String encoding;
    protected String inputEncoding;
    protected String version;
    protected String notationName;
    protected String baseURI;

    public EntityImpl(CoreDocumentImpl ownerDoc, String name) {
        super(ownerDoc);
        this.name = name;
        isReadOnly(true);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 6;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void setNodeValue(String x2) throws DOMException, MissingResourceException {
        if (this.ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void setPrefix(String prefix) throws DOMException {
        if (this.ownerDocument.errorChecking && isReadOnly()) {
            throw new DOMException((short) 7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.ChildNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        EntityImpl newentity = (EntityImpl) super.cloneNode(deep);
        newentity.setReadOnly(true, deep);
        return newentity;
    }

    @Override // org.w3c.dom.Entity
    public String getPublicId() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.publicId;
    }

    @Override // org.w3c.dom.Entity
    public String getSystemId() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.systemId;
    }

    @Override // org.w3c.dom.Entity
    public String getXmlVersion() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.version;
    }

    @Override // org.w3c.dom.Entity
    public String getXmlEncoding() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.encoding;
    }

    @Override // org.w3c.dom.Entity
    public String getNotationName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.notationName;
    }

    public void setPublicId(String id) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.publicId = id;
    }

    public void setXmlEncoding(String value) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.encoding = value;
    }

    @Override // org.w3c.dom.Entity
    public String getInputEncoding() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.inputEncoding;
    }

    public void setInputEncoding(String inputEncoding) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.inputEncoding = inputEncoding;
    }

    public void setXmlVersion(String value) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.version = value;
    }

    public void setSystemId(String id) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.systemId = id;
    }

    public void setNotationName(String name) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.notationName = name;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getBaseURI() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.baseURI != null ? this.baseURI : ((CoreDocumentImpl) getOwnerDocument()).getBaseURI();
    }

    public void setBaseURI(String uri) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.baseURI = uri;
    }
}
