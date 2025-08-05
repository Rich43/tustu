package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.util.URI;
import org.w3c.dom.DocumentType;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/EntityReferenceImpl.class */
public class EntityReferenceImpl extends ParentNode implements EntityReference {
    static final long serialVersionUID = -7381452955687102062L;
    protected String name;
    protected String baseURI;

    public EntityReferenceImpl(CoreDocumentImpl ownerDoc, String name) {
        super(ownerDoc);
        this.name = name;
        isReadOnly(true);
        needsSyncChildren(true);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 5;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.ChildNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        EntityReferenceImpl er = (EntityReferenceImpl) super.cloneNode(deep);
        er.setReadOnly(true, deep);
        return er;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getBaseURI() {
        NamedNodeMap entities;
        EntityImpl entDef;
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.baseURI == null) {
            DocumentType doctype = getOwnerDocument().getDoctype();
            if (null != doctype && null != (entities = doctype.getEntities()) && (entDef = (EntityImpl) entities.getNamedItem(getNodeName())) != null) {
                return entDef.getBaseURI();
            }
        } else if (this.baseURI != null && this.baseURI.length() != 0) {
            try {
                return new URI(this.baseURI).toString();
            } catch (URI.MalformedURIException e2) {
                return null;
            }
        }
        return this.baseURI;
    }

    public void setBaseURI(String uri) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.baseURI = uri;
    }

    protected String getEntityRefValue() {
        String value;
        String nodeValue;
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (this.firstChild != null) {
            if (this.firstChild.getNodeType() == 5) {
                value = ((EntityReferenceImpl) this.firstChild).getEntityRefValue();
            } else if (this.firstChild.getNodeType() == 3) {
                value = this.firstChild.getNodeValue();
            } else {
                return null;
            }
            if (this.firstChild.nextSibling == null) {
                return value;
            }
            StringBuffer buff = new StringBuffer(value);
            ChildNode childNode = this.firstChild.nextSibling;
            while (true) {
                ChildNode next = childNode;
                if (next != null) {
                    if (next.getNodeType() == 5) {
                        nodeValue = ((EntityReferenceImpl) next).getEntityRefValue();
                    } else if (next.getNodeType() == 3) {
                        nodeValue = next.getNodeValue();
                    } else {
                        return null;
                    }
                    String value2 = nodeValue;
                    buff.append(value2);
                    childNode = next.nextSibling;
                } else {
                    return buff.toString();
                }
            }
        } else {
            return "";
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode
    protected void synchronizeChildren() {
        NamedNodeMap entities;
        EntityImpl entDef;
        needsSyncChildren(false);
        DocumentType doctype = getOwnerDocument().getDoctype();
        if (null == doctype || null == (entities = doctype.getEntities()) || (entDef = (EntityImpl) entities.getNamedItem(getNodeName())) == null) {
            return;
        }
        isReadOnly(false);
        Node firstChild = entDef.getFirstChild();
        while (true) {
            Node defkid = firstChild;
            if (defkid != null) {
                Node newkid = defkid.cloneNode(true);
                insertBefore(newkid, null);
                firstChild = defkid.getNextSibling();
            } else {
                setReadOnly(true, true);
                return;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl
    public void setReadOnly(boolean readOnly, boolean deep) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (deep) {
            if (needsSyncChildren()) {
                synchronizeChildren();
            }
            ChildNode childNode = this.firstChild;
            while (true) {
                ChildNode mykid = childNode;
                if (mykid == null) {
                    break;
                }
                mykid.setReadOnly(readOnly, true);
                childNode = mykid.nextSibling;
            }
        }
        isReadOnly(readOnly);
    }
}
