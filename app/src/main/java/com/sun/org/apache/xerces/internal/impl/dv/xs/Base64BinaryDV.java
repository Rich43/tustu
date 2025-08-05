package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.apache.xerces.internal.impl.dv.util.ByteListImpl;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/Base64BinaryDV.class */
public class Base64BinaryDV extends TypeValidator {
    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public short getAllowedFacets() {
        return (short) 2079;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        byte[] decoded = Base64.decode(content);
        if (decoded == null) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_BASE64BINARY});
        }
        return new XBase64(decoded);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int getDataLength(Object value) {
        return ((XBase64) value).getLength();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/Base64BinaryDV$XBase64.class */
    private static final class XBase64 extends ByteListImpl {
        public XBase64(byte[] data) {
            super(data);
        }

        @Override // java.util.AbstractCollection
        public synchronized String toString() {
            if (this.canonical == null) {
                this.canonical = Base64.encode(this.data);
            }
            return this.canonical;
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            if (!(obj instanceof XBase64)) {
                return false;
            }
            byte[] odata = ((XBase64) obj).data;
            int len = this.data.length;
            if (len != odata.length) {
                return false;
            }
            for (int i2 = 0; i2 < len; i2++) {
                if (this.data[i2] != odata[i2]) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            int hash = 0;
            for (int i2 = 0; i2 < this.data.length; i2++) {
                hash = (hash * 37) + (this.data[i2] & 255);
            }
            return hash;
        }
    }
}
