package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/AttributePSVImpl.class */
public class AttributePSVImpl implements AttributePSVI {
    protected XSAttributeDeclaration fDeclaration = null;
    protected XSTypeDefinition fTypeDecl = null;
    protected boolean fSpecified = false;
    protected String fNormalizedValue = null;
    protected Object fActualValue = null;
    protected short fActualValueType = 45;
    protected ShortList fItemValueTypes = null;
    protected XSSimpleTypeDefinition fMemberType = null;
    protected short fValidationAttempted = 0;
    protected short fValidity = 0;
    protected String[] fErrorCodes = null;
    protected String fValidationContext = null;

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
        if (this.fErrorCodes == null) {
            return null;
        }
        return new StringListImpl(this.fErrorCodes, this.fErrorCodes.length);
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

    public void reset() {
        this.fNormalizedValue = null;
        this.fActualValue = null;
        this.fActualValueType = (short) 45;
        this.fItemValueTypes = null;
        this.fDeclaration = null;
        this.fTypeDecl = null;
        this.fSpecified = false;
        this.fMemberType = null;
        this.fValidationAttempted = (short) 0;
        this.fValidity = (short) 0;
        this.fErrorCodes = null;
        this.fValidationContext = null;
    }
}
