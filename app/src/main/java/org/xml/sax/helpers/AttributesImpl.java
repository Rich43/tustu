package org.xml.sax.helpers;

import org.xml.sax.Attributes;

/* loaded from: rt.jar:org/xml/sax/helpers/AttributesImpl.class */
public class AttributesImpl implements Attributes {
    int length;
    String[] data;

    public AttributesImpl() {
        this.length = 0;
        this.data = null;
    }

    public AttributesImpl(Attributes atts) {
        setAttributes(atts);
    }

    @Override // org.xml.sax.Attributes
    public int getLength() {
        return this.length;
    }

    @Override // org.xml.sax.Attributes
    public String getURI(int index) {
        if (index >= 0 && index < this.length) {
            return this.data[index * 5];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getLocalName(int index) {
        if (index >= 0 && index < this.length) {
            return this.data[(index * 5) + 1];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getQName(int index) {
        if (index >= 0 && index < this.length) {
            return this.data[(index * 5) + 2];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getType(int index) {
        if (index >= 0 && index < this.length) {
            return this.data[(index * 5) + 3];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getValue(int index) {
        if (index >= 0 && index < this.length) {
            return this.data[(index * 5) + 4];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public int getIndex(String uri, String localName) {
        int max = this.length * 5;
        for (int i2 = 0; i2 < max; i2 += 5) {
            if (this.data[i2].equals(uri) && this.data[i2 + 1].equals(localName)) {
                return i2 / 5;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public int getIndex(String qName) {
        int max = this.length * 5;
        for (int i2 = 0; i2 < max; i2 += 5) {
            if (this.data[i2 + 2].equals(qName)) {
                return i2 / 5;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public String getType(String uri, String localName) {
        int max = this.length * 5;
        for (int i2 = 0; i2 < max; i2 += 5) {
            if (this.data[i2].equals(uri) && this.data[i2 + 1].equals(localName)) {
                return this.data[i2 + 3];
            }
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getType(String qName) {
        int max = this.length * 5;
        for (int i2 = 0; i2 < max; i2 += 5) {
            if (this.data[i2 + 2].equals(qName)) {
                return this.data[i2 + 3];
            }
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getValue(String uri, String localName) {
        int max = this.length * 5;
        for (int i2 = 0; i2 < max; i2 += 5) {
            if (this.data[i2].equals(uri) && this.data[i2 + 1].equals(localName)) {
                return this.data[i2 + 4];
            }
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public String getValue(String qName) {
        int max = this.length * 5;
        for (int i2 = 0; i2 < max; i2 += 5) {
            if (this.data[i2 + 2].equals(qName)) {
                return this.data[i2 + 4];
            }
        }
        return null;
    }

    public void clear() {
        if (this.data != null) {
            for (int i2 = 0; i2 < this.length * 5; i2++) {
                this.data[i2] = null;
            }
        }
        this.length = 0;
    }

    public void setAttributes(Attributes atts) {
        clear();
        this.length = atts.getLength();
        if (this.length > 0) {
            this.data = new String[this.length * 5];
            for (int i2 = 0; i2 < this.length; i2++) {
                this.data[i2 * 5] = atts.getURI(i2);
                this.data[(i2 * 5) + 1] = atts.getLocalName(i2);
                this.data[(i2 * 5) + 2] = atts.getQName(i2);
                this.data[(i2 * 5) + 3] = atts.getType(i2);
                this.data[(i2 * 5) + 4] = atts.getValue(i2);
            }
        }
    }

    public void addAttribute(String uri, String localName, String qName, String type, String value) {
        ensureCapacity(this.length + 1);
        this.data[this.length * 5] = uri;
        this.data[(this.length * 5) + 1] = localName;
        this.data[(this.length * 5) + 2] = qName;
        this.data[(this.length * 5) + 3] = type;
        this.data[(this.length * 5) + 4] = value;
        this.length++;
    }

    public void setAttribute(int index, String uri, String localName, String qName, String type, String value) {
        if (index >= 0 && index < this.length) {
            this.data[index * 5] = uri;
            this.data[(index * 5) + 1] = localName;
            this.data[(index * 5) + 2] = qName;
            this.data[(index * 5) + 3] = type;
            this.data[(index * 5) + 4] = value;
            return;
        }
        badIndex(index);
    }

    public void removeAttribute(int index) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.length) {
            if (index < this.length - 1) {
                System.arraycopy(this.data, (index + 1) * 5, this.data, index * 5, ((this.length - index) - 1) * 5);
            }
            int index2 = (this.length - 1) * 5;
            int index3 = index2 + 1;
            this.data[index2] = null;
            int index4 = index3 + 1;
            this.data[index3] = null;
            int index5 = index4 + 1;
            this.data[index4] = null;
            this.data[index5] = null;
            this.data[index5 + 1] = null;
            this.length--;
            return;
        }
        badIndex(index);
    }

    public void setURI(int index, String uri) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.length) {
            this.data[index * 5] = uri;
        } else {
            badIndex(index);
        }
    }

    public void setLocalName(int index, String localName) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.length) {
            this.data[(index * 5) + 1] = localName;
        } else {
            badIndex(index);
        }
    }

    public void setQName(int index, String qName) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.length) {
            this.data[(index * 5) + 2] = qName;
        } else {
            badIndex(index);
        }
    }

    public void setType(int index, String type) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.length) {
            this.data[(index * 5) + 3] = type;
        } else {
            badIndex(index);
        }
    }

    public void setValue(int index, String value) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.length) {
            this.data[(index * 5) + 4] = value;
        } else {
            badIndex(index);
        }
    }

    private void ensureCapacity(int n2) {
        int length;
        int max;
        if (n2 <= 0) {
            return;
        }
        if (this.data == null || this.data.length == 0) {
            length = 25;
        } else if (this.data.length >= n2 * 5) {
            return;
        } else {
            length = this.data.length;
        }
        while (true) {
            max = length;
            if (max >= n2 * 5) {
                break;
            } else {
                length = max * 2;
            }
        }
        String[] newData = new String[max];
        if (this.length > 0) {
            System.arraycopy(this.data, 0, newData, 0, this.length * 5);
        }
        this.data = newData;
    }

    private void badIndex(int index) throws ArrayIndexOutOfBoundsException {
        String msg = "Attempt to modify attribute at illegal index: " + index;
        throw new ArrayIndexOutOfBoundsException(msg);
    }
}
