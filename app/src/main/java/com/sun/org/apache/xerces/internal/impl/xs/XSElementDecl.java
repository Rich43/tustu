package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSElementDecl.class */
public class XSElementDecl implements XSElementDeclaration {
    public static final short SCOPE_ABSENT = 0;
    public static final short SCOPE_GLOBAL = 1;
    public static final short SCOPE_LOCAL = 2;
    static final int INITIAL_SIZE = 2;
    private static final short CONSTRAINT_MASK = 3;
    private static final short NILLABLE = 4;
    private static final short ABSTRACT = 8;
    public String fName = null;
    public String fTargetNamespace = null;
    public XSTypeDefinition fType = null;
    public QName fUnresolvedTypeName = null;
    short fMiscFlags = 0;
    public short fScope = 0;
    XSComplexTypeDecl fEnclosingCT = null;
    public short fBlock = 0;
    public short fFinal = 0;
    public XSObjectList fAnnotations = null;
    public ValidatedInfo fDefault = null;
    public XSElementDecl fSubGroup = null;
    int fIDCPos = 0;
    IdentityConstraint[] fIDConstraints = new IdentityConstraint[2];
    private XSNamespaceItem fNamespaceItem = null;
    private String fDescription = null;

    public void setConstraintType(short constraintType) {
        this.fMiscFlags = (short) (this.fMiscFlags ^ (this.fMiscFlags & 3));
        this.fMiscFlags = (short) (this.fMiscFlags | (constraintType & 3));
    }

    public void setIsNillable() {
        this.fMiscFlags = (short) (this.fMiscFlags | 4);
    }

    public void setIsAbstract() {
        this.fMiscFlags = (short) (this.fMiscFlags | 8);
    }

    public void setIsGlobal() {
        this.fScope = (short) 1;
    }

    public void setIsLocal(XSComplexTypeDecl enclosingCT) {
        this.fScope = (short) 2;
        this.fEnclosingCT = enclosingCT;
    }

    public void addIDConstraint(IdentityConstraint idc) {
        if (this.fIDCPos == this.fIDConstraints.length) {
            this.fIDConstraints = resize(this.fIDConstraints, this.fIDCPos * 2);
        }
        IdentityConstraint[] identityConstraintArr = this.fIDConstraints;
        int i2 = this.fIDCPos;
        this.fIDCPos = i2 + 1;
        identityConstraintArr[i2] = idc;
    }

    public IdentityConstraint[] getIDConstraints() {
        if (this.fIDCPos == 0) {
            return null;
        }
        if (this.fIDCPos < this.fIDConstraints.length) {
            this.fIDConstraints = resize(this.fIDConstraints, this.fIDCPos);
        }
        return this.fIDConstraints;
    }

    static final IdentityConstraint[] resize(IdentityConstraint[] oldArray, int newSize) {
        IdentityConstraint[] newArray = new IdentityConstraint[newSize];
        System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
        return newArray;
    }

    public String toString() {
        if (this.fDescription == null) {
            if (this.fTargetNamespace != null) {
                StringBuffer buffer = new StringBuffer(this.fTargetNamespace.length() + (this.fName != null ? this.fName.length() : 4) + 3);
                buffer.append('\"');
                buffer.append(this.fTargetNamespace);
                buffer.append('\"');
                buffer.append(':');
                buffer.append(this.fName);
                this.fDescription = buffer.toString();
            } else {
                this.fDescription = this.fName;
            }
        }
        return this.fDescription;
    }

    public int hashCode() {
        int code = this.fName.hashCode();
        if (this.fTargetNamespace != null) {
            code = (code << 16) + this.fTargetNamespace.hashCode();
        }
        return code;
    }

    public boolean equals(Object o2) {
        return o2 == this;
    }

    public void reset() {
        this.fScope = (short) 0;
        this.fName = null;
        this.fTargetNamespace = null;
        this.fType = null;
        this.fUnresolvedTypeName = null;
        this.fMiscFlags = (short) 0;
        this.fBlock = (short) 0;
        this.fFinal = (short) 0;
        this.fDefault = null;
        this.fAnnotations = null;
        this.fSubGroup = null;
        for (int i2 = 0; i2 < this.fIDCPos; i2++) {
            this.fIDConstraints[i2] = null;
        }
        this.fIDCPos = 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 2;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return this.fName;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return this.fTargetNamespace;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public XSTypeDefinition getTypeDefinition() {
        return this.fType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public short getScope() {
        return this.fScope;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public XSComplexTypeDefinition getEnclosingCTDefinition() {
        return this.fEnclosingCT;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public short getConstraintType() {
        return (short) (this.fMiscFlags & 3);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public String getConstraintValue() {
        if (getConstraintType() == 0) {
            return null;
        }
        return this.fDefault.stringValue();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public boolean getNillable() {
        return (this.fMiscFlags & 4) != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public XSNamedMap getIdentityConstraints() {
        return new XSNamedMapImpl(this.fIDConstraints, this.fIDCPos);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public XSElementDeclaration getSubstitutionGroupAffiliation() {
        return this.fSubGroup;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public boolean isSubstitutionGroupExclusion(short exclusion) {
        return (this.fFinal & exclusion) != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public short getSubstitutionGroupExclusions() {
        return this.fFinal;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public boolean isDisallowedSubstitution(short disallowed) {
        return (this.fBlock & disallowed) != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public short getDisallowedSubstitutions() {
        return this.fBlock;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public boolean getAbstract() {
        return (this.fMiscFlags & 8) != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public XSAnnotation getAnnotation() {
        if (this.fAnnotations != null) {
            return (XSAnnotation) this.fAnnotations.item(0);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
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

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public Object getActualVC() {
        if (getConstraintType() == 0) {
            return null;
        }
        return this.fDefault.actualValue;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public short getActualVCType() {
        if (getConstraintType() == 0) {
            return (short) 45;
        }
        return this.fDefault.actualValueType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSElementDeclaration
    public ShortList getItemValueTypes() {
        if (getConstraintType() == 0) {
            return null;
        }
        return this.fDefault.itemValueTypes;
    }
}
