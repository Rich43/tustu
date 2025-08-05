package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSAttributeDecl.class */
public class XSAttributeDecl implements XSAttributeDeclaration {
    public static final short SCOPE_ABSENT = 0;
    public static final short SCOPE_GLOBAL = 1;
    public static final short SCOPE_LOCAL = 2;
    String fName = null;
    String fTargetNamespace = null;
    XSSimpleType fType = null;
    public QName fUnresolvedTypeName = null;
    short fConstraintType = 0;
    short fScope = 0;
    XSComplexTypeDecl fEnclosingCT = null;
    XSObjectList fAnnotations = null;
    ValidatedInfo fDefault = null;
    private XSNamespaceItem fNamespaceItem = null;

    public void setValues(String name, String targetNamespace, XSSimpleType simpleType, short constraintType, short scope, ValidatedInfo valInfo, XSComplexTypeDecl enclosingCT, XSObjectList annotations) {
        this.fName = name;
        this.fTargetNamespace = targetNamespace;
        this.fType = simpleType;
        this.fConstraintType = constraintType;
        this.fScope = scope;
        this.fDefault = valInfo;
        this.fEnclosingCT = enclosingCT;
        this.fAnnotations = annotations;
    }

    public void reset() {
        this.fName = null;
        this.fTargetNamespace = null;
        this.fType = null;
        this.fUnresolvedTypeName = null;
        this.fConstraintType = (short) 0;
        this.fScope = (short) 0;
        this.fDefault = null;
        this.fAnnotations = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 1;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return this.fName;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return this.fTargetNamespace;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public XSSimpleTypeDefinition getTypeDefinition() {
        return this.fType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public short getScope() {
        return this.fScope;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public XSComplexTypeDefinition getEnclosingCTDefinition() {
        return this.fEnclosingCT;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public short getConstraintType() {
        return this.fConstraintType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public String getConstraintValue() {
        if (getConstraintType() == 0) {
            return null;
        }
        return this.fDefault.stringValue();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public XSAnnotation getAnnotation() {
        if (this.fAnnotations != null) {
            return (XSAnnotation) this.fAnnotations.item(0);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }

    public ValidatedInfo getValInfo() {
        return this.fDefault;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public XSNamespaceItem getNamespaceItem() {
        return this.fNamespaceItem;
    }

    void setNamespaceItem(XSNamespaceItem namespaceItem) {
        this.fNamespaceItem = namespaceItem;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public Object getActualVC() {
        if (getConstraintType() == 0) {
            return null;
        }
        return this.fDefault.actualValue;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public short getActualVCType() {
        if (getConstraintType() == 0) {
            return (short) 45;
        }
        return this.fDefault.actualValueType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
    public ShortList getItemValueTypes() {
        if (getConstraintType() == 0) {
            return null;
        }
        return this.fDefault.itemValueTypes;
    }
}
