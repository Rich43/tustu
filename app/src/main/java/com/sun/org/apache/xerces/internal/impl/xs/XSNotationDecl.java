package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSNotationDecl.class */
public class XSNotationDecl implements XSNotationDeclaration {
    public String fName = null;
    public String fTargetNamespace = null;
    public String fPublicId = null;
    public String fSystemId = null;
    public XSObjectList fAnnotations = null;
    private XSNamespaceItem fNamespaceItem = null;

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 11;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return this.fName;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return this.fTargetNamespace;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration
    public String getSystemId() {
        return this.fSystemId;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration
    public String getPublicId() {
        return this.fPublicId;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration
    public XSAnnotation getAnnotation() {
        if (this.fAnnotations != null) {
            return (XSAnnotation) this.fAnnotations.item(0);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration
    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public XSNamespaceItem getNamespaceItem() {
        return this.fNamespaceItem;
    }

    void setNamespaceItem(XSNamespaceItem namespaceItem) {
        this.fNamespaceItem = namespaceItem;
    }
}
