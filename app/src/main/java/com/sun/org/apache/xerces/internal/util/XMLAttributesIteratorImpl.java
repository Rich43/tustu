package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLAttributesIteratorImpl.class */
public class XMLAttributesIteratorImpl extends XMLAttributesImpl implements Iterator {
    protected int fCurrent = 0;
    protected XMLAttributesImpl.Attribute fLastReturnedItem;

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.fCurrent < getLength();
    }

    @Override // java.util.Iterator
    public Object next() {
        if (hasNext()) {
            XMLAttributesImpl.Attribute[] attributeArr = this.fAttributes;
            int i2 = this.fCurrent;
            this.fCurrent = i2 + 1;
            XMLAttributesImpl.Attribute attribute = attributeArr[i2];
            this.fLastReturnedItem = attribute;
            return attribute;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public void remove() {
        if (this.fLastReturnedItem == this.fAttributes[this.fCurrent - 1]) {
            int i2 = this.fCurrent;
            this.fCurrent = i2 - 1;
            removeAttributeAt(i2);
            return;
        }
        throw new IllegalStateException();
    }

    @Override // com.sun.org.apache.xerces.internal.util.XMLAttributesImpl, com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void removeAllAttributes() {
        super.removeAllAttributes();
        this.fCurrent = 0;
    }
}
