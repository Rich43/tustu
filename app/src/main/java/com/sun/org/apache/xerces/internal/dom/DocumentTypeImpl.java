package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.dom.ParentNode;
import com.sun.org.apache.xerces.internal.impl.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DocumentTypeImpl.class */
public class DocumentTypeImpl extends ParentNode implements DocumentType {
    static final long serialVersionUID = 7751299192316526485L;
    protected String name;
    protected NamedNodeMapImpl entities;
    protected NamedNodeMapImpl notations;
    protected NamedNodeMapImpl elements;
    protected String publicID;
    protected String systemID;
    protected String internalSubset;
    private int doctypeNumber;
    private Map<String, ParentNode.UserDataRecord> userData;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("name", String.class), new ObjectStreamField(Constants.DOM_ENTITIES, NamedNodeMapImpl.class), new ObjectStreamField("notations", NamedNodeMapImpl.class), new ObjectStreamField(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ELEMENTS, NamedNodeMapImpl.class), new ObjectStreamField("publicID", String.class), new ObjectStreamField("systemID", String.class), new ObjectStreamField("internalSubset", String.class), new ObjectStreamField("doctypeNumber", Integer.TYPE), new ObjectStreamField("userData", Hashtable.class)};

    public DocumentTypeImpl(CoreDocumentImpl ownerDocument, String name) {
        super(ownerDocument);
        this.doctypeNumber = 0;
        this.userData = null;
        this.name = name;
        this.entities = new NamedNodeMapImpl(this);
        this.notations = new NamedNodeMapImpl(this);
        this.elements = new NamedNodeMapImpl(this);
    }

    public DocumentTypeImpl(CoreDocumentImpl ownerDocument, String qualifiedName, String publicID, String systemID) {
        this(ownerDocument, qualifiedName);
        this.publicID = publicID;
        this.systemID = systemID;
    }

    @Override // org.w3c.dom.DocumentType
    public String getPublicId() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.publicID;
    }

    @Override // org.w3c.dom.DocumentType
    public String getSystemId() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.systemID;
    }

    public void setInternalSubset(String internalSubset) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.internalSubset = internalSubset;
    }

    @Override // org.w3c.dom.DocumentType
    public String getInternalSubset() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.internalSubset;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 10;
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
        DocumentTypeImpl newnode = (DocumentTypeImpl) super.cloneNode(deep);
        newnode.entities = this.entities.cloneMap(newnode);
        newnode.notations = this.notations.cloneMap(newnode);
        newnode.elements = this.elements.cloneMap(newnode);
        return newnode;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void setTextContent(String textContent) throws DOMException {
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public boolean isEqualNode(Node arg) {
        if (!super.isEqualNode(arg)) {
            return false;
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        DocumentTypeImpl argDocType = (DocumentTypeImpl) arg;
        if (getPublicId() != null || argDocType.getPublicId() == null) {
            if (getPublicId() == null || argDocType.getPublicId() != null) {
                if (getSystemId() != null || argDocType.getSystemId() == null) {
                    if (getSystemId() == null || argDocType.getSystemId() != null) {
                        if (getInternalSubset() != null || argDocType.getInternalSubset() == null) {
                            if (getInternalSubset() != null && argDocType.getInternalSubset() == null) {
                                return false;
                            }
                            if (getPublicId() != null && !getPublicId().equals(argDocType.getPublicId())) {
                                return false;
                            }
                            if (getSystemId() != null && !getSystemId().equals(argDocType.getSystemId())) {
                                return false;
                            }
                            if (getInternalSubset() != null && !getInternalSubset().equals(argDocType.getInternalSubset())) {
                                return false;
                            }
                            NamedNodeMapImpl argEntities = argDocType.entities;
                            if (this.entities == null && argEntities != null) {
                                return false;
                            }
                            if (this.entities != null && argEntities == null) {
                                return false;
                            }
                            if (this.entities != null && argEntities != null) {
                                if (this.entities.getLength() != argEntities.getLength()) {
                                    return false;
                                }
                                for (int index = 0; this.entities.item(index) != null; index++) {
                                    Node entNode1 = this.entities.item(index);
                                    Node entNode2 = argEntities.getNamedItem(entNode1.getNodeName());
                                    if (!((NodeImpl) entNode1).isEqualNode((NodeImpl) entNode2)) {
                                        return false;
                                    }
                                }
                            }
                            NamedNodeMapImpl argNotations = argDocType.notations;
                            if (this.notations == null && argNotations != null) {
                                return false;
                            }
                            if (this.notations != null && argNotations == null) {
                                return false;
                            }
                            if (this.notations != null && argNotations != null) {
                                if (this.notations.getLength() != argNotations.getLength()) {
                                    return false;
                                }
                                for (int index2 = 0; this.notations.item(index2) != null; index2++) {
                                    Node noteNode1 = this.notations.item(index2);
                                    Node noteNode2 = argNotations.getNamedItem(noteNode1.getNodeName());
                                    if (!((NodeImpl) noteNode1).isEqualNode((NodeImpl) noteNode2)) {
                                        return false;
                                    }
                                }
                                return true;
                            }
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl
    void setOwnerDocument(CoreDocumentImpl doc) {
        super.setOwnerDocument(doc);
        this.entities.setOwnerDocument(doc);
        this.notations.setOwnerDocument(doc);
        this.elements.setOwnerDocument(doc);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    protected int getNodeNumber() {
        if (getOwnerDocument() != null) {
            return super.getNodeNumber();
        }
        if (this.doctypeNumber == 0) {
            CoreDOMImplementationImpl cd = (CoreDOMImplementationImpl) CoreDOMImplementationImpl.getDOMImplementation();
            this.doctypeNumber = cd.assignDocTypeNumber();
        }
        return this.doctypeNumber;
    }

    @Override // org.w3c.dom.DocumentType
    public String getName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // org.w3c.dom.DocumentType
    public NamedNodeMap getEntities() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.entities;
    }

    @Override // org.w3c.dom.DocumentType
    public NamedNodeMap getNotations() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.notations;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl
    public void setReadOnly(boolean readOnly, boolean deep) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        super.setReadOnly(readOnly, deep);
        this.elements.setReadOnly(readOnly, true);
        this.entities.setReadOnly(readOnly, true);
        this.notations.setReadOnly(readOnly, true);
    }

    public NamedNodeMap getElements() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.elements;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        ParentNode.UserDataRecord udr;
        if (this.userData == null) {
            this.userData = new HashMap();
        }
        if (data == null) {
            if (this.userData != null && (udr = this.userData.remove(key)) != null) {
                return udr.fData;
            }
            return null;
        }
        ParentNode.UserDataRecord udr2 = this.userData.put(key, new ParentNode.UserDataRecord(data, handler));
        if (udr2 != null) {
            return udr2.fData;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Object getUserData(String key) {
        ParentNode.UserDataRecord udr;
        if (this.userData != null && (udr = this.userData.get(key)) != null) {
            return udr.fData;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    protected Map<String, ParentNode.UserDataRecord> getUserDataRecord() {
        return this.userData;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Hashtable<String, ParentNode.UserDataRecord> ud = this.userData == null ? null : new Hashtable<>(this.userData);
        ObjectOutputStream.PutField pf = out.putFields();
        pf.put("name", this.name);
        pf.put(Constants.DOM_ENTITIES, this.entities);
        pf.put("notations", this.notations);
        pf.put(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ELEMENTS, this.elements);
        pf.put("publicID", this.publicID);
        pf.put("systemID", this.systemID);
        pf.put("internalSubset", this.internalSubset);
        pf.put("doctypeNumber", this.doctypeNumber);
        pf.put("userData", ud);
        out.writeFields();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField gf = in.readFields();
        this.name = (String) gf.get("name", (Object) null);
        this.entities = (NamedNodeMapImpl) gf.get(Constants.DOM_ENTITIES, (Object) null);
        this.notations = (NamedNodeMapImpl) gf.get("notations", (Object) null);
        this.elements = (NamedNodeMapImpl) gf.get(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ELEMENTS, (Object) null);
        this.publicID = (String) gf.get("publicID", (Object) null);
        this.systemID = (String) gf.get("systemID", (Object) null);
        this.internalSubset = (String) gf.get("internalSubset", (Object) null);
        this.doctypeNumber = gf.get("doctypeNumber", 0);
        Hashtable<String, ParentNode.UserDataRecord> ud = (Hashtable) gf.get("userData", (Object) null);
        if (ud != null) {
            this.userData = new HashMap(ud);
        }
    }
}
