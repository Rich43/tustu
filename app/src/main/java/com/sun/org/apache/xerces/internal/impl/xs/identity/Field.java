package com.sun.org.apache.xerces.internal.impl.xs.identity;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.sun.org.apache.xerces.internal.impl.xpath.XPathException;
import com.sun.org.apache.xerces.internal.impl.xs.util.ShortListImpl;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/Field.class */
public class Field {
    protected XPath fXPath;
    protected IdentityConstraint fIdentityConstraint;

    public Field(XPath xpath, IdentityConstraint identityConstraint) {
        this.fXPath = xpath;
        this.fIdentityConstraint = identityConstraint;
    }

    public com.sun.org.apache.xerces.internal.impl.xpath.XPath getXPath() {
        return this.fXPath;
    }

    public IdentityConstraint getIdentityConstraint() {
        return this.fIdentityConstraint;
    }

    public XPathMatcher createMatcher(FieldActivator activator, ValueStore store) {
        return new Matcher(this.fXPath, activator, store);
    }

    public String toString() {
        return this.fXPath.toString();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/Field$XPath.class */
    public static class XPath extends com.sun.org.apache.xerces.internal.impl.xpath.XPath {
        public XPath(String xpath, SymbolTable symbolTable, NamespaceContext context) throws XPathException {
            super((xpath.trim().startsWith("/") || xpath.trim().startsWith(".")) ? xpath : "./" + xpath, symbolTable, context);
            for (int i2 = 0; i2 < this.fLocationPaths.length; i2++) {
                for (int j2 = 0; j2 < this.fLocationPaths[i2].steps.length; j2++) {
                    XPath.Axis axis = this.fLocationPaths[i2].steps[j2].axis;
                    if (axis.type == 2 && j2 < this.fLocationPaths[i2].steps.length - 1) {
                        throw new XPathException("c-fields-xpaths");
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/Field$Matcher.class */
    protected class Matcher extends XPathMatcher {
        protected FieldActivator fFieldActivator;
        protected ValueStore fStore;

        public Matcher(XPath xpath, FieldActivator activator, ValueStore store) {
            super(xpath);
            this.fFieldActivator = activator;
            this.fStore = store;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher
        protected void matched(Object actualValue, short valueType, ShortList itemValueType, boolean isNil) {
            super.matched(actualValue, valueType, itemValueType, isNil);
            if (isNil && Field.this.fIdentityConstraint.getCategory() == 1) {
                this.fStore.reportError("KeyMatchesNillable", new Object[]{Field.this.fIdentityConstraint.getElementName(), Field.this.fIdentityConstraint.getIdentityConstraintName()});
            }
            this.fStore.addValue(Field.this, actualValue, convertToPrimitiveKind(valueType), convertToPrimitiveKind(itemValueType));
            this.fFieldActivator.setMayMatch(Field.this, Boolean.FALSE);
        }

        private short convertToPrimitiveKind(short valueType) {
            if (valueType <= 20) {
                return valueType;
            }
            if (valueType <= 29) {
                return (short) 2;
            }
            if (valueType <= 42) {
                return (short) 4;
            }
            return valueType;
        }

        private ShortList convertToPrimitiveKind(ShortList itemValueType) {
            short type;
            if (itemValueType != null) {
                int length = itemValueType.getLength();
                int i2 = 0;
                while (i2 < length && (type = itemValueType.item(i2)) == convertToPrimitiveKind(type)) {
                    i2++;
                }
                if (i2 != length) {
                    short[] arr = new short[length];
                    for (int j2 = 0; j2 < i2; j2++) {
                        arr[j2] = itemValueType.item(j2);
                    }
                    while (i2 < length) {
                        arr[i2] = convertToPrimitiveKind(itemValueType.item(i2));
                        i2++;
                    }
                    return new ShortListImpl(arr, arr.length);
                }
            }
            return itemValueType;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher
        protected void handleContent(XSTypeDefinition type, boolean nillable, Object actualValue, short valueType, ShortList itemValueType) {
            if (type == null || (type.getTypeCategory() == 15 && ((XSComplexTypeDefinition) type).getContentType() != 1)) {
                this.fStore.reportError("cvc-id.3", new Object[]{Field.this.fIdentityConstraint.getName(), Field.this.fIdentityConstraint.getElementName()});
            }
            this.fMatchedString = actualValue;
            matched(this.fMatchedString, valueType, itemValueType, nillable);
        }
    }
}
