package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import java.util.AbstractList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/ListDV.class */
public class ListDV extends TypeValidator {
    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public short getAllowedFacets() {
        return (short) 2079;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        return content;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int getDataLength(Object value) {
        return ((ListData) value).getLength();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/ListDV$ListData.class */
    static final class ListData extends AbstractList implements ObjectList {
        final Object[] data;
        private String canonical;

        public ListData(Object[] data) {
            this.data = data;
        }

        @Override // java.util.AbstractCollection
        public synchronized String toString() {
            if (this.canonical == null) {
                int len = this.data.length;
                StringBuffer buf = new StringBuffer();
                if (len > 0) {
                    buf.append(this.data[0].toString());
                }
                for (int i2 = 1; i2 < len; i2++) {
                    buf.append(' ');
                    buf.append(this.data[i2].toString());
                }
                this.canonical = buf.toString();
            }
            return this.canonical;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList
        public int getLength() {
            return this.data.length;
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            if (!(obj instanceof ListData)) {
                return false;
            }
            Object[] odata = ((ListData) obj).data;
            int count = this.data.length;
            if (count != odata.length) {
                return false;
            }
            for (int i2 = 0; i2 < count; i2++) {
                if (!this.data[i2].equals(odata[i2])) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            int hash = 0;
            for (int i2 = 0; i2 < this.data.length; i2++) {
                hash ^= this.data[i2].hashCode();
            }
            return hash;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object item) {
            for (int i2 = 0; i2 < this.data.length; i2++) {
                if (item == this.data[i2]) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList
        public Object item(int index) {
            if (index < 0 || index >= this.data.length) {
                return null;
            }
            return this.data[index];
        }

        @Override // java.util.AbstractList, java.util.List
        public Object get(int index) {
            if (index >= 0 && index < this.data.length) {
                return this.data[index];
            }
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return getLength();
        }
    }
}
