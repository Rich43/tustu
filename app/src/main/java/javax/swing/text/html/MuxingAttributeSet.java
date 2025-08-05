package javax.swing.text.html;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;

/* loaded from: rt.jar:javax/swing/text/html/MuxingAttributeSet.class */
class MuxingAttributeSet implements AttributeSet, Serializable {
    private AttributeSet[] attrs;

    public MuxingAttributeSet(AttributeSet[] attributeSetArr) {
        this.attrs = attributeSetArr;
    }

    protected MuxingAttributeSet() {
    }

    protected synchronized void setAttributes(AttributeSet[] attributeSetArr) {
        this.attrs = attributeSetArr;
    }

    protected synchronized AttributeSet[] getAttributes() {
        return this.attrs;
    }

    protected synchronized void insertAttributeSetAt(AttributeSet attributeSet, int i2) {
        int length = this.attrs.length;
        AttributeSet[] attributeSetArr = new AttributeSet[length + 1];
        if (i2 < length) {
            if (i2 > 0) {
                System.arraycopy(this.attrs, 0, attributeSetArr, 0, i2);
                System.arraycopy(this.attrs, i2, attributeSetArr, i2 + 1, length - i2);
            } else {
                System.arraycopy(this.attrs, 0, attributeSetArr, 1, length);
            }
        } else {
            System.arraycopy(this.attrs, 0, attributeSetArr, 0, length);
        }
        attributeSetArr[i2] = attributeSet;
        this.attrs = attributeSetArr;
    }

    protected synchronized void removeAttributeSetAt(int i2) {
        int length = this.attrs.length;
        AttributeSet[] attributeSetArr = new AttributeSet[length - 1];
        if (length > 0) {
            if (i2 == 0) {
                System.arraycopy(this.attrs, 1, attributeSetArr, 0, length - 1);
            } else if (i2 < length - 1) {
                System.arraycopy(this.attrs, 0, attributeSetArr, 0, i2);
                System.arraycopy(this.attrs, i2 + 1, attributeSetArr, i2, (length - i2) - 1);
            } else {
                System.arraycopy(this.attrs, 0, attributeSetArr, 0, length - 1);
            }
        }
        this.attrs = attributeSetArr;
    }

    @Override // javax.swing.text.AttributeSet
    public int getAttributeCount() {
        int attributeCount = 0;
        for (AttributeSet attributeSet : getAttributes()) {
            attributeCount += attributeSet.getAttributeCount();
        }
        return attributeCount;
    }

    @Override // javax.swing.text.AttributeSet
    public boolean isDefined(Object obj) {
        for (AttributeSet attributeSet : getAttributes()) {
            if (attributeSet.isDefined(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.swing.text.AttributeSet
    public boolean isEqual(AttributeSet attributeSet) {
        return getAttributeCount() == attributeSet.getAttributeCount() && containsAttributes(attributeSet);
    }

    @Override // javax.swing.text.AttributeSet
    public AttributeSet copyAttributes() {
        AttributeSet[] attributes = getAttributes();
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        for (int length = attributes.length - 1; length >= 0; length--) {
            simpleAttributeSet.addAttributes(attributes[length]);
        }
        return simpleAttributeSet;
    }

    @Override // javax.swing.text.AttributeSet
    public Object getAttribute(Object obj) {
        for (AttributeSet attributeSet : getAttributes()) {
            Object attribute = attributeSet.getAttribute(obj);
            if (attribute != null) {
                return attribute;
            }
        }
        return null;
    }

    @Override // javax.swing.text.AttributeSet
    public Enumeration getAttributeNames() {
        return new MuxingAttributeNameEnumeration();
    }

    @Override // javax.swing.text.AttributeSet
    public boolean containsAttribute(Object obj, Object obj2) {
        return obj2.equals(getAttribute(obj));
    }

    @Override // javax.swing.text.AttributeSet
    public boolean containsAttributes(AttributeSet attributeSet) {
        boolean zEquals = true;
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (zEquals && attributeNames.hasMoreElements()) {
            Object objNextElement2 = attributeNames.nextElement2();
            zEquals = attributeSet.getAttribute(objNextElement2).equals(getAttribute(objNextElement2));
        }
        return zEquals;
    }

    @Override // javax.swing.text.AttributeSet
    public AttributeSet getResolveParent() {
        return null;
    }

    /* loaded from: rt.jar:javax/swing/text/html/MuxingAttributeSet$MuxingAttributeNameEnumeration.class */
    private class MuxingAttributeNameEnumeration implements Enumeration {
        private int attrIndex;
        private Enumeration currentEnum;

        MuxingAttributeNameEnumeration() {
            updateEnum();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            if (this.currentEnum == null) {
                return false;
            }
            return this.currentEnum.hasMoreElements();
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public Object nextElement2() {
            if (this.currentEnum == null) {
                throw new NoSuchElementException("No more names");
            }
            Object objNextElement2 = this.currentEnum.nextElement2();
            if (!this.currentEnum.hasMoreElements()) {
                updateEnum();
            }
            return objNextElement2;
        }

        void updateEnum() {
            AttributeSet[] attributes = MuxingAttributeSet.this.getAttributes();
            this.currentEnum = null;
            while (this.currentEnum == null && this.attrIndex < attributes.length) {
                int i2 = this.attrIndex;
                this.attrIndex = i2 + 1;
                this.currentEnum = attributes[i2].getAttributeNames();
                if (!this.currentEnum.hasMoreElements()) {
                    this.currentEnum = null;
                }
            }
        }
    }
}
