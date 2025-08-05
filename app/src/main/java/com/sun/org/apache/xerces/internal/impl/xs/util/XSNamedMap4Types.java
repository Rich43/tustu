package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.xs.XSObject;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/XSNamedMap4Types.class */
public final class XSNamedMap4Types extends XSNamedMapImpl {
    private final short fType;

    public XSNamedMap4Types(String namespace, SymbolHash map, short type) {
        super(namespace, map);
        this.fType = type;
    }

    public XSNamedMap4Types(String[] namespaces, SymbolHash[] maps, int num, short type) {
        super(namespaces, maps, num);
        this.fType = type;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl, com.sun.org.apache.xerces.internal.xs.XSNamedMap
    public synchronized int getLength() {
        if (this.fLength == -1) {
            int length = 0;
            for (int i2 = 0; i2 < this.fNSNum; i2++) {
                length += this.fMaps[i2].getLength();
            }
            int pos = 0;
            XSObject[] array = new XSObject[length];
            for (int i3 = 0; i3 < this.fNSNum; i3++) {
                pos += this.fMaps[i3].getValues(array, pos);
            }
            this.fLength = 0;
            this.fArray = new XSObject[length];
            for (int i4 = 0; i4 < length; i4++) {
                XSTypeDefinition type = (XSTypeDefinition) array[i4];
                if (type.getTypeCategory() == this.fType) {
                    XSObject[] xSObjectArr = this.fArray;
                    int i5 = this.fLength;
                    this.fLength = i5 + 1;
                    xSObjectArr[i5] = type;
                }
            }
        }
        return this.fLength;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl, com.sun.org.apache.xerces.internal.xs.XSNamedMap
    public XSObject itemByName(String namespace, String localName) {
        for (int i2 = 0; i2 < this.fNSNum; i2++) {
            if (isEqual(namespace, this.fNamespaces[i2])) {
                XSTypeDefinition type = (XSTypeDefinition) this.fMaps[i2].get(localName);
                if (type != null && type.getTypeCategory() == this.fType) {
                    return type;
                }
                return null;
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl, com.sun.org.apache.xerces.internal.xs.XSNamedMap
    public synchronized XSObject item(int index) {
        if (this.fArray == null) {
            getLength();
        }
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        return this.fArray[index];
    }
}
