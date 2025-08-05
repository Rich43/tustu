package com.sun.org.apache.xerces.internal.impl.xs.identity;

import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSIDCDefinition;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/IdentityConstraint.class */
public abstract class IdentityConstraint implements XSIDCDefinition {
    protected short type;
    protected String fNamespace;
    protected String fIdentityConstraintName;
    protected String fElementName;
    protected Selector fSelector;
    protected int fFieldCount;
    protected Field[] fFields;
    protected XSAnnotationImpl[] fAnnotations = null;
    protected int fNumAnnotations;

    protected IdentityConstraint(String namespace, String identityConstraintName, String elemName) {
        this.fNamespace = namespace;
        this.fIdentityConstraintName = identityConstraintName;
        this.fElementName = elemName;
    }

    public String getIdentityConstraintName() {
        return this.fIdentityConstraintName;
    }

    public void setSelector(Selector selector) {
        this.fSelector = selector;
    }

    public Selector getSelector() {
        return this.fSelector;
    }

    public void addField(Field field) {
        if (this.fFields == null) {
            this.fFields = new Field[4];
        } else if (this.fFieldCount == this.fFields.length) {
            this.fFields = resize(this.fFields, this.fFieldCount * 2);
        }
        Field[] fieldArr = this.fFields;
        int i2 = this.fFieldCount;
        this.fFieldCount = i2 + 1;
        fieldArr[i2] = field;
    }

    public int getFieldCount() {
        return this.fFieldCount;
    }

    public Field getFieldAt(int index) {
        return this.fFields[index];
    }

    public String getElementName() {
        return this.fElementName;
    }

    public String toString() {
        String s2 = super.toString();
        int index1 = s2.lastIndexOf(36);
        if (index1 != -1) {
            return s2.substring(index1 + 1);
        }
        int index2 = s2.lastIndexOf(46);
        if (index2 != -1) {
            return s2.substring(index2 + 1);
        }
        return s2;
    }

    public boolean equals(IdentityConstraint id) {
        boolean areEqual = this.fIdentityConstraintName.equals(id.fIdentityConstraintName);
        if (!areEqual) {
            return false;
        }
        boolean areEqual2 = this.fSelector.toString().equals(id.fSelector.toString());
        if (!areEqual2) {
            return false;
        }
        boolean areEqual3 = this.fFieldCount == id.fFieldCount;
        if (!areEqual3) {
            return false;
        }
        for (int i2 = 0; i2 < this.fFieldCount; i2++) {
            if (!this.fFields[i2].toString().equals(id.fFields[i2].toString())) {
                return false;
            }
        }
        return true;
    }

    static final Field[] resize(Field[] oldArray, int newSize) {
        Field[] newArray = new Field[newSize];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        return newArray;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 10;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return this.fIdentityConstraintName;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return this.fNamespace;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSIDCDefinition
    public short getCategory() {
        return this.type;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSIDCDefinition
    public String getSelectorStr() {
        if (this.fSelector != null) {
            return this.fSelector.toString();
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSIDCDefinition
    public StringList getFieldStrs() {
        String[] strs = new String[this.fFieldCount];
        for (int i2 = 0; i2 < this.fFieldCount; i2++) {
            strs[i2] = this.fFields[i2].toString();
        }
        return new StringListImpl(strs, this.fFieldCount);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSIDCDefinition
    public XSIDCDefinition getRefKey() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSIDCDefinition
    public XSObjectList getAnnotations() {
        return new XSObjectListImpl(this.fAnnotations, this.fNumAnnotations);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public XSNamespaceItem getNamespaceItem() {
        return null;
    }

    public void addAnnotation(XSAnnotationImpl annotation) {
        if (annotation == null) {
            return;
        }
        if (this.fAnnotations == null) {
            this.fAnnotations = new XSAnnotationImpl[2];
        } else if (this.fNumAnnotations == this.fAnnotations.length) {
            XSAnnotationImpl[] newArray = new XSAnnotationImpl[this.fNumAnnotations << 1];
            System.arraycopy(this.fAnnotations, 0, newArray, 0, this.fNumAnnotations);
            this.fAnnotations = newArray;
        }
        XSAnnotationImpl[] xSAnnotationImplArr = this.fAnnotations;
        int i2 = this.fNumAnnotations;
        this.fNumAnnotations = i2 + 1;
        xSAnnotationImplArr[i2] = annotation;
    }
}
