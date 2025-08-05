package com.sun.org.apache.xerces.internal.xpointer;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xpointer/ShortHandPointer.class */
final class ShortHandPointer implements XPointerPart {
    private String fShortHandPointer;
    private SymbolTable fSymbolTable;
    private boolean fIsFragmentResolved = false;
    int fMatchingChildCount = 0;

    public ShortHandPointer() {
    }

    public ShortHandPointer(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public void parseXPointer(String part) throws XNIException {
        this.fShortHandPointer = part;
        this.fIsFragmentResolved = false;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public boolean resolveXPointer(QName element, XMLAttributes attributes, Augmentations augs, int event) throws XNIException {
        if (this.fMatchingChildCount == 0) {
            this.fIsFragmentResolved = false;
        }
        if (event == 0) {
            if (this.fMatchingChildCount == 0) {
                this.fIsFragmentResolved = hasMatchingIdentifier(element, attributes, augs, event);
            }
            if (this.fIsFragmentResolved) {
                this.fMatchingChildCount++;
            }
        } else if (event == 2) {
            if (this.fMatchingChildCount == 0) {
                this.fIsFragmentResolved = hasMatchingIdentifier(element, attributes, augs, event);
            }
        } else if (this.fIsFragmentResolved) {
            this.fMatchingChildCount--;
        }
        return this.fIsFragmentResolved;
    }

    private boolean hasMatchingIdentifier(QName element, XMLAttributes attributes, Augmentations augs, int event) throws XNIException {
        String normalizedValue = null;
        if (attributes != null) {
            for (int i2 = 0; i2 < attributes.getLength(); i2++) {
                normalizedValue = getSchemaDeterminedID(attributes, i2);
                if (normalizedValue != null) {
                    break;
                }
                normalizedValue = getChildrenSchemaDeterminedID(attributes, i2);
                if (normalizedValue != null) {
                    break;
                }
                normalizedValue = getDTDDeterminedID(attributes, i2);
                if (normalizedValue != null) {
                    break;
                }
            }
        }
        if (normalizedValue != null && normalizedValue.equals(this.fShortHandPointer)) {
            return true;
        }
        return false;
    }

    public String getDTDDeterminedID(XMLAttributes attributes, int index) throws XNIException {
        if (attributes.getType(index).equals("ID")) {
            return attributes.getValue(index);
        }
        return null;
    }

    public String getSchemaDeterminedID(XMLAttributes attributes, int index) throws XNIException {
        Augmentations augs = attributes.getAugmentations(index);
        AttributePSVI attrPSVI = (AttributePSVI) augs.getItem(Constants.ATTRIBUTE_PSVI);
        if (attrPSVI != null) {
            XSTypeDefinition typeDef = attrPSVI.getMemberTypeDefinition();
            if (typeDef != null) {
                typeDef = attrPSVI.getTypeDefinition();
            }
            if (typeDef != null && ((XSSimpleType) typeDef).isIDType()) {
                return attrPSVI.getSchemaNormalizedValue();
            }
            return null;
        }
        return null;
    }

    public String getChildrenSchemaDeterminedID(XMLAttributes attributes, int index) throws XNIException {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public boolean isFragmentResolved() {
        return this.fIsFragmentResolved;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public boolean isChildFragmentResolved() {
        return this.fIsFragmentResolved && this.fMatchingChildCount > 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public String getSchemeName() {
        return this.fShortHandPointer;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public String getSchemeData() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public void setSchemeName(String schemeName) {
        this.fShortHandPointer = schemeName;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public void setSchemeData(String schemeData) {
    }
}
