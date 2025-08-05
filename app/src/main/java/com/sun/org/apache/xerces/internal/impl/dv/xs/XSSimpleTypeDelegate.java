package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.DatatypeException;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDelegate.class */
public class XSSimpleTypeDelegate implements XSSimpleType {
    protected final XSSimpleType type;

    public XSSimpleTypeDelegate(XSSimpleType type) {
        if (type == null) {
            throw new NullPointerException();
        }
        this.type = type;
    }

    public XSSimpleType getWrappedXSSimpleType() {
        return this.type;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSObjectList getAnnotations() {
        return this.type.getAnnotations();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean getBounded() {
        return this.type.getBounded();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getBuiltInKind() {
        return this.type.getBuiltInKind();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getDefinedFacets() {
        return this.type.getDefinedFacets();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSObjectList getFacets() {
        return this.type.getFacets();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean getFinite() {
        return this.type.getFinite();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getFixedFacets() {
        return this.type.getFixedFacets();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSSimpleTypeDefinition getItemType() {
        return this.type.getItemType();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public StringList getLexicalEnumeration() {
        return this.type.getLexicalEnumeration();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public String getLexicalFacetValue(short facetName) {
        return this.type.getLexicalFacetValue(facetName);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public StringList getLexicalPattern() {
        return this.type.getLexicalPattern();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSObjectList getMemberTypes() {
        return this.type.getMemberTypes();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSObjectList getMultiValueFacets() {
        return this.type.getMultiValueFacets();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean getNumeric() {
        return this.type.getNumeric();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getOrdered() {
        return this.type.getOrdered();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSSimpleTypeDefinition getPrimitiveType() {
        return this.type.getPrimitiveType();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getVariety() {
        return this.type.getVariety();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean isDefinedFacet(short facetName) {
        return this.type.isDefinedFacet(facetName);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean isFixedFacet(short facetName) {
        return this.type.isFixedFacet(facetName);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean derivedFrom(String namespace, String name, short derivationMethod) {
        return this.type.derivedFrom(namespace, name, derivationMethod);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean derivedFromType(XSTypeDefinition ancestorType, short derivationMethod) {
        return this.type.derivedFromType(ancestorType, derivationMethod);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean getAnonymous() {
        return this.type.getAnonymous();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public XSTypeDefinition getBaseType() {
        return this.type.getBaseType();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public short getFinal() {
        return this.type.getFinal();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public short getTypeCategory() {
        return this.type.getTypeCategory();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean isFinal(short restriction) {
        return this.type.isFinal(restriction);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return this.type.getName();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return this.type.getNamespace();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public XSNamespaceItem getNamespaceItem() {
        return this.type.getNamespaceItem();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return this.type.getType();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public void applyFacets(XSFacets facets, short presentFacet, short fixedFacet, ValidationContext context) throws InvalidDatatypeFacetException {
        this.type.applyFacets(facets, presentFacet, fixedFacet, context);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public short getPrimitiveKind() {
        return this.type.getPrimitiveKind();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public short getWhitespace() throws DatatypeException {
        return this.type.getWhitespace();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public boolean isEqual(Object value1, Object value2) {
        return this.type.isEqual(value1, value2);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public boolean isIDType() {
        return this.type.isIDType();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public void validate(ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        this.type.validate(context, validatedInfo);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public Object validate(String content, ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        return this.type.validate(content, context, validatedInfo);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public Object validate(Object content, ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        return this.type.validate(content, context, validatedInfo);
    }

    public String toString() {
        return this.type.toString();
    }
}
