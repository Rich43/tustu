package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder;
import com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSParticle;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSWildcard;
import org.w3c.dom.TypeInfo;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSComplexTypeDecl.class */
public class XSComplexTypeDecl implements XSComplexTypeDefinition, TypeInfo {
    String fName = null;
    String fTargetNamespace = null;
    XSTypeDefinition fBaseType = null;
    short fDerivedBy = 2;
    short fFinal = 0;
    short fBlock = 0;
    short fMiscFlags = 0;
    XSAttributeGroupDecl fAttrGrp = null;
    short fContentType = 0;
    XSSimpleType fXSSimpleType = null;
    XSParticleDecl fParticle = null;
    volatile XSCMValidator fCMValidator = null;
    XSCMValidator fUPACMValidator = null;
    XSObjectListImpl fAnnotations = null;
    private XSNamespaceItem fNamespaceItem = null;
    static final int DERIVATION_ANY = 0;
    static final int DERIVATION_RESTRICTION = 1;
    static final int DERIVATION_EXTENSION = 2;
    static final int DERIVATION_UNION = 4;
    static final int DERIVATION_LIST = 8;
    private static final short CT_IS_ABSTRACT = 1;
    private static final short CT_HAS_TYPE_ID = 2;
    private static final short CT_IS_ANONYMOUS = 4;

    public void setValues(String name, String targetNamespace, XSTypeDefinition baseType, short derivedBy, short schemaFinal, short block, short contentType, boolean isAbstract, XSAttributeGroupDecl attrGrp, XSSimpleType simpleType, XSParticleDecl particle, XSObjectListImpl annotations) {
        this.fTargetNamespace = targetNamespace;
        this.fBaseType = baseType;
        this.fDerivedBy = derivedBy;
        this.fFinal = schemaFinal;
        this.fBlock = block;
        this.fContentType = contentType;
        if (isAbstract) {
            this.fMiscFlags = (short) (this.fMiscFlags | 1);
        }
        this.fAttrGrp = attrGrp;
        this.fXSSimpleType = simpleType;
        this.fParticle = particle;
        this.fAnnotations = annotations;
    }

    public void setName(String name) {
        this.fName = name;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public short getTypeCategory() {
        return (short) 15;
    }

    @Override // org.w3c.dom.TypeInfo
    public String getTypeName() {
        return this.fName;
    }

    public short getFinalSet() {
        return this.fFinal;
    }

    public String getTargetNamespace() {
        return this.fTargetNamespace;
    }

    public boolean containsTypeID() {
        return (this.fMiscFlags & 2) != 0;
    }

    public void setIsAbstractType() {
        this.fMiscFlags = (short) (this.fMiscFlags | 1);
    }

    public void setContainsTypeID() {
        this.fMiscFlags = (short) (this.fMiscFlags | 2);
    }

    public void setIsAnonymous() {
        this.fMiscFlags = (short) (this.fMiscFlags | 4);
    }

    public XSCMValidator getContentModel(CMBuilder cmBuilder) {
        if (this.fContentType == 1 || this.fContentType == 0) {
            return null;
        }
        if (this.fCMValidator == null) {
            synchronized (this) {
                if (this.fCMValidator == null) {
                    this.fCMValidator = cmBuilder.getContentModel(this);
                }
            }
        }
        return this.fCMValidator;
    }

    public XSAttributeGroupDecl getAttrGrp() {
        return this.fAttrGrp;
    }

    public String toString() {
        StringBuilder str = new StringBuilder(192);
        appendTypeInfo(str);
        return str.toString();
    }

    void appendTypeInfo(StringBuilder str) {
        String[] contentType = {"EMPTY", "SIMPLE", "ELEMENT", "MIXED"};
        String[] derivedBy = {"EMPTY", "EXTENSION", "RESTRICTION"};
        str.append("Complex type name='").append(this.fTargetNamespace).append(',').append(getTypeName()).append("', ");
        if (this.fBaseType != null) {
            str.append(" base type name='").append(this.fBaseType.getName()).append("', ");
        }
        str.append(" content type='").append(contentType[this.fContentType]).append("', ");
        str.append(" isAbstract='").append(getAbstract()).append("', ");
        str.append(" hasTypeId='").append(containsTypeID()).append("', ");
        str.append(" final='").append((int) this.fFinal).append("', ");
        str.append(" block='").append((int) this.fBlock).append("', ");
        if (this.fParticle != null) {
            str.append(" particle='").append(this.fParticle.toString()).append("', ");
        }
        str.append(" derivedBy='").append(derivedBy[this.fDerivedBy]).append("'. ");
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean derivedFromType(XSTypeDefinition ancestor, short derivationMethod) {
        XSTypeDefinition type;
        if (ancestor == null) {
            return false;
        }
        if (ancestor == SchemaGrammar.fAnyType) {
            return true;
        }
        XSTypeDefinition baseType = this;
        while (true) {
            type = baseType;
            if (type == ancestor || type == SchemaGrammar.fAnySimpleType || type == SchemaGrammar.fAnyType) {
                break;
            }
            baseType = type.getBaseType();
        }
        return type == ancestor;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean derivedFrom(String ancestorNS, String ancestorName, short derivationMethod) {
        XSTypeDefinition type;
        if (ancestorName == null) {
            return false;
        }
        if (ancestorNS != null && ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && ancestorName.equals(SchemaSymbols.ATTVAL_ANYTYPE)) {
            return true;
        }
        XSTypeDefinition baseType = this;
        while (true) {
            type = baseType;
            if ((ancestorName.equals(type.getName()) && ((ancestorNS == null && type.getNamespace() == null) || (ancestorNS != null && ancestorNS.equals(type.getNamespace())))) || type == SchemaGrammar.fAnySimpleType || type == SchemaGrammar.fAnyType) {
                break;
            }
            baseType = type.getBaseType();
        }
        return (type == SchemaGrammar.fAnySimpleType || type == SchemaGrammar.fAnyType) ? false : true;
    }

    public boolean isDOMDerivedFrom(String ancestorNS, String ancestorName, int derivationMethod) {
        if (ancestorName == null) {
            return false;
        }
        if (ancestorNS != null && ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && ancestorName.equals(SchemaSymbols.ATTVAL_ANYTYPE) && derivationMethod == 1 && derivationMethod == 2) {
            return true;
        }
        if ((derivationMethod & 1) != 0 && isDerivedByRestriction(ancestorNS, ancestorName, derivationMethod, this)) {
            return true;
        }
        if ((derivationMethod & 2) != 0 && isDerivedByExtension(ancestorNS, ancestorName, derivationMethod, this)) {
            return true;
        }
        if (((derivationMethod & 8) != 0 || (derivationMethod & 4) != 0) && (derivationMethod & 1) == 0 && (derivationMethod & 2) == 0) {
            if (ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && ancestorName.equals(SchemaSymbols.ATTVAL_ANYTYPE)) {
                ancestorName = SchemaSymbols.ATTVAL_ANYSIMPLETYPE;
            }
            if (!this.fName.equals(SchemaSymbols.ATTVAL_ANYTYPE) || !this.fTargetNamespace.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
                if (this.fBaseType != null && (this.fBaseType instanceof XSSimpleTypeDecl)) {
                    return ((XSSimpleTypeDecl) this.fBaseType).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod);
                }
                if (this.fBaseType != null && (this.fBaseType instanceof XSComplexTypeDecl)) {
                    return ((XSComplexTypeDecl) this.fBaseType).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod);
                }
            }
        }
        if ((derivationMethod & 2) == 0 && (derivationMethod & 1) == 0 && (derivationMethod & 8) == 0 && (derivationMethod & 4) == 0) {
            return isDerivedByAny(ancestorNS, ancestorName, derivationMethod, this);
        }
        return false;
    }

    private boolean isDerivedByAny(String ancestorNS, String ancestorName, int derivationMethod, XSTypeDefinition type) {
        XSTypeDefinition oldType = null;
        boolean derivedFrom = false;
        while (type != null && type != oldType) {
            if (ancestorName.equals(type.getName()) && ((ancestorNS == null && type.getNamespace() == null) || (ancestorNS != null && ancestorNS.equals(type.getNamespace())))) {
                derivedFrom = true;
                break;
            }
            if (isDerivedByRestriction(ancestorNS, ancestorName, derivationMethod, type) || !isDerivedByExtension(ancestorNS, ancestorName, derivationMethod, type)) {
                return true;
            }
            oldType = type;
            type = type.getBaseType();
        }
        return derivedFrom;
    }

    private boolean isDerivedByRestriction(String ancestorNS, String ancestorName, int derivationMethod, XSTypeDefinition type) {
        XSTypeDefinition oldType = null;
        while (type != null && type != oldType) {
            if (ancestorNS != null && ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && ancestorName.equals(SchemaSymbols.ATTVAL_ANYSIMPLETYPE)) {
                return false;
            }
            if (!ancestorName.equals(type.getName()) || ancestorNS == null || !ancestorNS.equals(type.getNamespace())) {
                if (type.getNamespace() == null && ancestorNS == null) {
                    return true;
                }
                if (type instanceof XSSimpleTypeDecl) {
                    if (ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && ancestorName.equals(SchemaSymbols.ATTVAL_ANYTYPE)) {
                        ancestorName = SchemaSymbols.ATTVAL_ANYSIMPLETYPE;
                    }
                    return ((XSSimpleTypeDecl) type).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod);
                }
                if (((XSComplexTypeDecl) type).getDerivationMethod() != 2) {
                    return false;
                }
                oldType = type;
                type = type.getBaseType();
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isDerivedByExtension(String ancestorNS, String ancestorName, int derivationMethod, XSTypeDefinition type) {
        boolean extension = false;
        XSTypeDefinition oldType = null;
        while (type != null && type != oldType) {
            if (ancestorNS == null || !ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) || !ancestorName.equals(SchemaSymbols.ATTVAL_ANYSIMPLETYPE) || !SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(type.getNamespace()) || !SchemaSymbols.ATTVAL_ANYTYPE.equals(type.getName())) {
                if (ancestorName.equals(type.getName()) && ((ancestorNS == null && type.getNamespace() == null) || (ancestorNS != null && ancestorNS.equals(type.getNamespace())))) {
                    return extension;
                }
                if (type instanceof XSSimpleTypeDecl) {
                    if (ancestorNS.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && ancestorName.equals(SchemaSymbols.ATTVAL_ANYTYPE)) {
                        ancestorName = SchemaSymbols.ATTVAL_ANYSIMPLETYPE;
                    }
                    if ((derivationMethod & 2) != 0) {
                        return extension & ((XSSimpleTypeDecl) type).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod & 1);
                    }
                    return extension & ((XSSimpleTypeDecl) type).isDOMDerivedFrom(ancestorNS, ancestorName, derivationMethod);
                }
                if (((XSComplexTypeDecl) type).getDerivationMethod() == 1) {
                    extension |= true;
                }
                oldType = type;
                type = type.getBaseType();
            } else {
                return false;
            }
        }
        return false;
    }

    public void reset() {
        this.fName = null;
        this.fTargetNamespace = null;
        this.fBaseType = null;
        this.fDerivedBy = (short) 2;
        this.fFinal = (short) 0;
        this.fBlock = (short) 0;
        this.fMiscFlags = (short) 0;
        this.fAttrGrp.reset();
        this.fContentType = (short) 0;
        this.fXSSimpleType = null;
        this.fParticle = null;
        this.fCMValidator = null;
        this.fUPACMValidator = null;
        if (this.fAnnotations != null) {
            this.fAnnotations.clearXSObjectList();
        }
        this.fAnnotations = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 3;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        if (getAnonymous()) {
            return null;
        }
        return this.fName;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean getAnonymous() {
        return (this.fMiscFlags & 4) != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return this.fTargetNamespace;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public XSTypeDefinition getBaseType() {
        return this.fBaseType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
    public short getDerivationMethod() {
        return this.fDerivedBy;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean isFinal(short derivation) {
        return (this.fFinal & derivation) != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public short getFinal() {
        return this.fFinal;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
    public boolean getAbstract() {
        return (this.fMiscFlags & 1) != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
    public XSObjectList getAttributeUses() {
        return this.fAttrGrp.getAttributeUses();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
    public XSWildcard getAttributeWildcard() {
        return this.fAttrGrp.getAttributeWildcard();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
    public short getContentType() {
        return this.fContentType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
    public XSSimpleTypeDefinition getSimpleType() {
        return this.fXSSimpleType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
    public XSParticle getParticle() {
        return this.fParticle;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
    public boolean isProhibitedSubstitution(short prohibited) {
        return (this.fBlock & prohibited) != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
    public short getProhibitedSubstitutions() {
        return this.fBlock;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
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

    public XSAttributeUse getAttributeUse(String namespace, String name) {
        return this.fAttrGrp.getAttributeUse(namespace, name);
    }

    @Override // org.w3c.dom.TypeInfo
    public String getTypeNamespace() {
        return getNamespace();
    }

    @Override // org.w3c.dom.TypeInfo
    public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod) {
        return isDOMDerivedFrom(typeNamespaceArg, typeNameArg, derivationMethod);
    }
}
