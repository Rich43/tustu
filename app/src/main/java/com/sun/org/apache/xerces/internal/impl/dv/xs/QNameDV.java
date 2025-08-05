package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xs.datatypes.XSQName;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/QNameDV.class */
public class QNameDV extends TypeValidator {
    private static final String EMPTY_STRING = "".intern();

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public short getAllowedFacets() {
        return (short) 2079;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        String prefix;
        String localpart;
        int colonptr = content.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        if (colonptr > 0) {
            prefix = context.getSymbol(content.substring(0, colonptr));
            localpart = content.substring(colonptr + 1);
        } else {
            prefix = EMPTY_STRING;
            localpart = content;
        }
        if (prefix.length() > 0 && !XMLChar.isValidNCName(prefix)) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_QNAME});
        }
        if (!XMLChar.isValidNCName(localpart)) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_QNAME});
        }
        String uri = context.getURI(prefix);
        if (prefix.length() > 0 && uri == null) {
            throw new InvalidDatatypeValueException("UndeclaredPrefix", new Object[]{content, prefix});
        }
        return new XQName(prefix, context.getSymbol(localpart), context.getSymbol(content), uri);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int getDataLength(Object value) {
        return ((XQName) value).rawname.length();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/QNameDV$XQName.class */
    private static final class XQName extends QName implements XSQName {
        public XQName(String prefix, String localpart, String rawname, String uri) {
            setValues(prefix, localpart, rawname, uri);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.QName
        public boolean equals(Object object) {
            if (object instanceof QName) {
                QName qname = (QName) object;
                return this.uri == qname.uri && this.localpart == qname.localpart;
            }
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.QName
        public synchronized String toString() {
            return this.rawname;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSQName
        public javax.xml.namespace.QName getJAXPQName() {
            return new javax.xml.namespace.QName(this.uri, this.localpart, this.prefix);
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSQName
        public QName getXNIQName() {
            return this;
        }
    }
}
