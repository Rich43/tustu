package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSModel;
import com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/PSVIElementNSImpl.class */
public class PSVIElementNSImpl extends ElementNSImpl implements ElementPSVI {
    static final long serialVersionUID = 6815489624636016068L;
    protected XSElementDeclaration fDeclaration;
    protected XSTypeDefinition fTypeDecl;
    protected boolean fNil;
    protected boolean fSpecified;
    protected String fNormalizedValue;
    protected Object fActualValue;
    protected short fActualValueType;
    protected ShortList fItemValueTypes;
    protected XSNotationDeclaration fNotation;
    protected XSSimpleTypeDefinition fMemberType;
    protected short fValidationAttempted;
    protected short fValidity;
    protected StringList fErrorCodes;
    protected String fValidationContext;
    protected XSModel fSchemaInformation;

    public PSVIElementNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
        this.fDeclaration = null;
        this.fTypeDecl = null;
        this.fNil = false;
        this.fSpecified = true;
        this.fNormalizedValue = null;
        this.fActualValue = null;
        this.fActualValueType = (short) 45;
        this.fItemValueTypes = null;
        this.fNotation = null;
        this.fMemberType = null;
        this.fValidationAttempted = (short) 0;
        this.fValidity = (short) 0;
        this.fErrorCodes = null;
        this.fValidationContext = null;
        this.fSchemaInformation = null;
    }

    public PSVIElementNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName) {
        super(ownerDocument, namespaceURI, qualifiedName);
        this.fDeclaration = null;
        this.fTypeDecl = null;
        this.fNil = false;
        this.fSpecified = true;
        this.fNormalizedValue = null;
        this.fActualValue = null;
        this.fActualValueType = (short) 45;
        this.fItemValueTypes = null;
        this.fNotation = null;
        this.fMemberType = null;
        this.fValidationAttempted = (short) 0;
        this.fValidity = (short) 0;
        this.fErrorCodes = null;
        this.fValidationContext = null;
        this.fSchemaInformation = null;
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

    @Override // com.sun.org.apache.xerces.internal.xs.ElementPSVI
    public boolean getNil() {
        return this.fNil;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ElementPSVI
    public XSNotationDeclaration getNotation() {
        return this.fNotation;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public XSTypeDefinition getTypeDefinition() {
        return this.fTypeDecl;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ItemPSVI
    public XSSimpleTypeDefinition getMemberTypeDefinition() {
        return this.fMemberType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ElementPSVI
    public XSElementDeclaration getElementDeclaration() {
        return this.fDeclaration;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ElementPSVI
    public XSModel getSchemaInformation() {
        return this.fSchemaInformation;
    }

    public void setPSVI(ElementPSVI elem) {
        this.fDeclaration = elem.getElementDeclaration();
        this.fNotation = elem.getNotation();
        this.fValidationContext = elem.getValidationContext();
        this.fTypeDecl = elem.getTypeDefinition();
        this.fSchemaInformation = elem.getSchemaInformation();
        this.fValidity = elem.getValidity();
        this.fValidationAttempted = elem.getValidationAttempted();
        this.fErrorCodes = elem.getErrorCodes();
        this.fNormalizedValue = elem.getSchemaNormalizedValue();
        this.fActualValue = elem.getActualNormalizedValue();
        this.fActualValueType = elem.getActualNormalizedValueType();
        this.fItemValueTypes = elem.getItemValueTypes();
        this.fMemberType = elem.getMemberTypeDefinition();
        this.fSpecified = elem.getIsSchemaSpecified();
        this.fNil = elem.getNil();
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
