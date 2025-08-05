package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/PSVIAttrNSImpl.class */
public class PSVIAttrNSImpl extends AttrNSImpl implements AttributePSVI {
    static final long serialVersionUID = -3241738699421018889L;
    protected XSAttributeDeclaration fDeclaration;
    protected XSTypeDefinition fTypeDecl;
    protected boolean fSpecified;
    protected String fNormalizedValue;
    protected Object fActualValue;
    protected short fActualValueType;
    protected ShortList fItemValueTypes;
    protected XSSimpleTypeDefinition fMemberType;
    protected short fValidationAttempted;
    protected short fValidity;
    protected StringList fErrorCodes;
    protected String fValidationContext;

    public PSVIAttrNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
        this.fDeclaration = null;
        this.fTypeDecl = null;
        this.fSpecified = true;
        this.fNormalizedValue = null;
        this.fActualValue = null;
        this.fActualValueType = (short) 45;
        this.fItemValueTypes = null;
        this.fMemberType = null;
        this.fValidationAttempted = (short) 0;
        this.fValidity = (short) 0;
        this.fErrorCodes = null;
        this.fValidationContext = null;
    }

    public PSVIAttrNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName) {
        super(ownerDocument, namespaceURI, qualifiedName);
        this.fDeclaration = null;
        this.fTypeDecl = null;
        this.fSpecified = true;
        this.fNormalizedValue = null;
        this.fActualValue = null;
        this.fActualValueType = (short) 45;
        this.fItemValueTypes = null;
        this.fMemberType = null;
        this.fValidationAttempted = (short) 0;
        this.fValidity = (short) 0;
        this.fErrorCodes = null;
        this.fValidationContext = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public String getSchemaDefault() {
        if (this.fDeclaration == null) {
            return null;
        }
        return this.fDeclaration.getConstraintValue();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public String getSchemaNormalizedValue() {
        return this.fNormalizedValue;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public boolean getIsSchemaSpecified() {
        return this.fSpecified;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public short getValidationAttempted() {
        return this.fValidationAttempted;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public short getValidity() {
        return this.fValidity;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public StringList getErrorCodes() {
        return this.fErrorCodes;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public String getValidationContext() {
        return this.fValidationContext;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public XSTypeDefinition getTypeDefinition() {
        return this.fTypeDecl;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public XSSimpleTypeDefinition getMemberTypeDefinition() {
        return this.fMemberType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.AttributePSVI
    public XSAttributeDeclaration getAttributeDeclaration() {
        return this.fDeclaration;
    }

    public void setPSVI(AttributePSVI attr) {
        this.fDeclaration = attr.getAttributeDeclaration();
        this.fValidationContext = attr.getValidationContext();
        this.fValidity = attr.getValidity();
        this.fValidationAttempted = attr.getValidationAttempted();
        this.fErrorCodes = attr.getErrorCodes();
        this.fNormalizedValue = attr.getSchemaNormalizedValue();
        this.fActualValue = attr.getActualNormalizedValue();
        this.fActualValueType = attr.getActualNormalizedValueType();
        this.fItemValueTypes = attr.getItemValueTypes();
        this.fTypeDecl = attr.getTypeDefinition();
        this.fMemberType = attr.getMemberTypeDefinition();
        this.fSpecified = attr.getIsSchemaSpecified();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public Object getActualNormalizedValue() {
        return this.fActualValue;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public short getActualNormalizedValueType() {
        return this.fActualValueType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public ShortList getItemValueTypes() {
        return this.fItemValueTypes;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new NotSerializableException(getClass().getName());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new NotSerializableException(getClass().getName());
    }
}
