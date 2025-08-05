package com.sun.org.apache.xerces.internal.impl.xs.identity;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.sun.org.apache.xerces.internal.impl.xpath.XPathException;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/Selector.class */
public class Selector {
    protected final XPath fXPath;
    protected final IdentityConstraint fIdentityConstraint;
    protected IdentityConstraint fIDConstraint;

    public Selector(XPath xpath, IdentityConstraint identityConstraint) {
        this.fXPath = xpath;
        this.fIdentityConstraint = identityConstraint;
    }

    public com.sun.org.apache.xerces.internal.impl.xpath.XPath getXPath() {
        return this.fXPath;
    }

    public IdentityConstraint getIDConstraint() {
        return this.fIdentityConstraint;
    }

    public XPathMatcher createMatcher(FieldActivator activator, int initialDepth) {
        return new Matcher(this.fXPath, activator, initialDepth);
    }

    public String toString() {
        return this.fXPath.toString();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/Selector$XPath.class */
    public static class XPath extends com.sun.org.apache.xerces.internal.impl.xpath.XPath {
        public XPath(String xpath, SymbolTable symbolTable, NamespaceContext context) throws XPathException {
            super(normalize(xpath), symbolTable, context);
            for (int i2 = 0; i2 < this.fLocationPaths.length; i2++) {
                XPath.Axis axis = this.fLocationPaths[i2].steps[this.fLocationPaths[i2].steps.length - 1].axis;
                if (axis.type == 2) {
                    throw new XPathException("c-selector-xpath");
                }
            }
        }

        private static String normalize(String xpath) {
            StringBuffer modifiedXPath = new StringBuffer(xpath.length() + 5);
            while (true) {
                if (!XMLChar.trim(xpath).startsWith("/") && !XMLChar.trim(xpath).startsWith(".")) {
                    modifiedXPath.append("./");
                }
                int unionIndex = xpath.indexOf(124);
                if (unionIndex == -1) {
                    modifiedXPath.append(xpath);
                    return modifiedXPath.toString();
                }
                modifiedXPath.append(xpath.substring(0, unionIndex + 1));
                xpath = xpath.substring(unionIndex + 1, xpath.length());
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/Selector$Matcher.class */
    public class Matcher extends XPathMatcher {
        protected final FieldActivator fFieldActivator;
        protected final int fInitialDepth;
        protected int fElementDepth;
        protected int fMatchedDepth;

        public Matcher(XPath xpath, FieldActivator activator, int initialDepth) {
            super(xpath);
            this.fFieldActivator = activator;
            this.fInitialDepth = initialDepth;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher
        public void startDocumentFragment() {
            super.startDocumentFragment();
            this.fElementDepth = 0;
            this.fMatchedDepth = -1;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher
        public void startElement(QName element, XMLAttributes attributes) {
            super.startElement(element, attributes);
            this.fElementDepth++;
            if (isMatched()) {
                this.fMatchedDepth = this.fElementDepth;
                this.fFieldActivator.startValueScopeFor(Selector.this.fIdentityConstraint, this.fInitialDepth);
                int count = Selector.this.fIdentityConstraint.getFieldCount();
                for (int i2 = 0; i2 < count; i2++) {
                    Field field = Selector.this.fIdentityConstraint.getFieldAt(i2);
                    XPathMatcher matcher = this.fFieldActivator.activateField(field, this.fInitialDepth);
                    matcher.startElement(element, attributes);
                }
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher
        public void endElement(QName element, XSTypeDefinition type, boolean nillable, Object actualValue, short valueType, ShortList itemValueType) {
            super.endElement(element, type, nillable, actualValue, valueType, itemValueType);
            int i2 = this.fElementDepth;
            this.fElementDepth = i2 - 1;
            if (i2 == this.fMatchedDepth) {
                this.fMatchedDepth = -1;
                this.fFieldActivator.endValueScopeFor(Selector.this.fIdentityConstraint, this.fInitialDepth);
            }
        }

        public IdentityConstraint getIdentityConstraint() {
            return Selector.this.fIdentityConstraint;
        }

        public int getInitialDepth() {
            return this.fInitialDepth;
        }
    }
}
