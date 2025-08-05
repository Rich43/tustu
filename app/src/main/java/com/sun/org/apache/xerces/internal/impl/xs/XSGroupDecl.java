package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSModelGroup;
import com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSGroupDecl.class */
public class XSGroupDecl implements XSModelGroupDefinition {
    public String fName = null;
    public String fTargetNamespace = null;
    public XSModelGroupImpl fModelGroup = null;
    public XSObjectList fAnnotations = null;
    private XSNamespaceItem fNamespaceItem = null;

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 6;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return this.fName;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return this.fTargetNamespace;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition
    public XSModelGroup getModelGroup() {
        return this.fModelGroup;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition
    public XSAnnotation getAnnotation() {
        if (this.fAnnotations != null) {
            return (XSAnnotation) this.fAnnotations.item(0);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition
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
