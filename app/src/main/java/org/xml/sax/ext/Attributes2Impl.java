package org.xml.sax.ext;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:org/xml/sax/ext/Attributes2Impl.class */
public class Attributes2Impl extends AttributesImpl implements Attributes2 {
    private boolean[] declared;
    private boolean[] specified;

    public Attributes2Impl() {
        this.specified = null;
        this.declared = null;
    }

    public Attributes2Impl(Attributes atts) {
        super(atts);
    }

    @Override // org.xml.sax.ext.Attributes2
    public boolean isDeclared(int index) {
        if (index < 0 || index >= getLength()) {
            throw new ArrayIndexOutOfBoundsException("No attribute at index: " + index);
        }
        return this.declared[index];
    }

    @Override // org.xml.sax.ext.Attributes2
    public boolean isDeclared(String uri, String localName) {
        int index = getIndex(uri, localName);
        if (index < 0) {
            throw new IllegalArgumentException("No such attribute: local=" + localName + ", namespace=" + uri);
        }
        return this.declared[index];
    }

    @Override // org.xml.sax.ext.Attributes2
    public boolean isDeclared(String qName) {
        int index = getIndex(qName);
        if (index < 0) {
            throw new IllegalArgumentException("No such attribute: " + qName);
        }
        return this.declared[index];
    }

    @Override // org.xml.sax.ext.Attributes2
    public boolean isSpecified(int index) {
        if (index < 0 || index >= getLength()) {
            throw new ArrayIndexOutOfBoundsException("No attribute at index: " + index);
        }
        return this.specified[index];
    }

    @Override // org.xml.sax.ext.Attributes2
    public boolean isSpecified(String uri, String localName) {
        int index = getIndex(uri, localName);
        if (index < 0) {
            throw new IllegalArgumentException("No such attribute: local=" + localName + ", namespace=" + uri);
        }
        return this.specified[index];
    }

    @Override // org.xml.sax.ext.Attributes2
    public boolean isSpecified(String qName) {
        int index = getIndex(qName);
        if (index < 0) {
            throw new IllegalArgumentException("No such attribute: " + qName);
        }
        return this.specified[index];
    }

    @Override // org.xml.sax.helpers.AttributesImpl
    public void setAttributes(Attributes atts) {
        int length = atts.getLength();
        super.setAttributes(atts);
        this.declared = new boolean[length];
        this.specified = new boolean[length];
        if (atts instanceof Attributes2) {
            Attributes2 a2 = (Attributes2) atts;
            for (int i2 = 0; i2 < length; i2++) {
                this.declared[i2] = a2.isDeclared(i2);
                this.specified[i2] = a2.isSpecified(i2);
            }
            return;
        }
        for (int i3 = 0; i3 < length; i3++) {
            this.declared[i3] = !"CDATA".equals(atts.getType(i3));
            this.specified[i3] = true;
        }
    }

    @Override // org.xml.sax.helpers.AttributesImpl
    public void addAttribute(String uri, String localName, String qName, String type, String value) {
        super.addAttribute(uri, localName, qName, type, value);
        int length = getLength();
        if (this.specified == null) {
            this.specified = new boolean[length];
            this.declared = new boolean[length];
        } else if (length > this.specified.length) {
            boolean[] newFlags = new boolean[length];
            System.arraycopy(this.declared, 0, newFlags, 0, this.declared.length);
            this.declared = newFlags;
            boolean[] newFlags2 = new boolean[length];
            System.arraycopy(this.specified, 0, newFlags2, 0, this.specified.length);
            this.specified = newFlags2;
        }
        this.specified[length - 1] = true;
        this.declared[length - 1] = !"CDATA".equals(type);
    }

    @Override // org.xml.sax.helpers.AttributesImpl
    public void removeAttribute(int index) throws ArrayIndexOutOfBoundsException {
        int origMax = getLength() - 1;
        super.removeAttribute(index);
        if (index != origMax) {
            System.arraycopy(this.declared, index + 1, this.declared, index, origMax - index);
            System.arraycopy(this.specified, index + 1, this.specified, index, origMax - index);
        }
    }

    public void setDeclared(int index, boolean value) {
        if (index < 0 || index >= getLength()) {
            throw new ArrayIndexOutOfBoundsException("No attribute at index: " + index);
        }
        this.declared[index] = value;
    }

    public void setSpecified(int index, boolean value) {
        if (index < 0 || index >= getLength()) {
            throw new ArrayIndexOutOfBoundsException("No attribute at index: " + index);
        }
        this.specified[index] = value;
    }
}
