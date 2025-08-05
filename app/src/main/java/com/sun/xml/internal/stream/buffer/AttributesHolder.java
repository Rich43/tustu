package com.sun.xml.internal.stream.buffer;

import org.xml.sax.Attributes;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/AttributesHolder.class */
public final class AttributesHolder implements Attributes {
    private static final int DEFAULT_CAPACITY = 8;
    private static final int ITEM_SIZE = 8;
    private static final int PREFIX = 0;
    private static final int URI = 1;
    private static final int LOCAL_NAME = 2;
    private static final int QNAME = 3;
    private static final int TYPE = 4;
    private static final int VALUE = 5;
    private int _attributeCount;
    private String[] _strings = new String[64];

    @Override // org.xml.sax.Attributes
    public final int getLength() {
        return this._attributeCount;
    }

    public final String getPrefix(int index) {
        if (index < 0 || index >= this._attributeCount) {
            return null;
        }
        return this._strings[(index << 3) + 0];
    }

    @Override // org.xml.sax.Attributes
    public final String getLocalName(int index) {
        if (index < 0 || index >= this._attributeCount) {
            return null;
        }
        return this._strings[(index << 3) + 2];
    }

    @Override // org.xml.sax.Attributes
    public final String getQName(int index) {
        if (index < 0 || index >= this._attributeCount) {
            return null;
        }
        return this._strings[(index << 3) + 3];
    }

    @Override // org.xml.sax.Attributes
    public final String getType(int index) {
        if (index < 0 || index >= this._attributeCount) {
            return null;
        }
        return this._strings[(index << 3) + 4];
    }

    @Override // org.xml.sax.Attributes
    public final String getURI(int index) {
        if (index < 0 || index >= this._attributeCount) {
            return null;
        }
        return this._strings[(index << 3) + 1];
    }

    @Override // org.xml.sax.Attributes
    public final String getValue(int index) {
        if (index < 0 || index >= this._attributeCount) {
            return null;
        }
        return this._strings[(index << 3) + 5];
    }

    @Override // org.xml.sax.Attributes
    public final int getIndex(String qName) {
        for (int i2 = 0; i2 < this._attributeCount; i2++) {
            if (qName.equals(this._strings[(i2 << 3) + 3])) {
                return i2;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public final String getType(String qName) {
        int i2 = (getIndex(qName) << 3) + 4;
        if (i2 >= 0) {
            return this._strings[i2];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getValue(String qName) {
        int i2 = (getIndex(qName) << 3) + 5;
        if (i2 >= 0) {
            return this._strings[i2];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final int getIndex(String uri, String localName) {
        for (int i2 = 0; i2 < this._attributeCount; i2++) {
            if (localName.equals(this._strings[(i2 << 3) + 2]) && uri.equals(this._strings[(i2 << 3) + 1])) {
                return i2;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public final String getType(String uri, String localName) {
        int i2 = (getIndex(uri, localName) << 3) + 4;
        if (i2 >= 0) {
            return this._strings[i2];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getValue(String uri, String localName) {
        int i2 = (getIndex(uri, localName) << 3) + 5;
        if (i2 >= 0) {
            return this._strings[i2];
        }
        return null;
    }

    public final void clear() {
        if (this._attributeCount > 0) {
            for (int i2 = 0; i2 < this._attributeCount; i2++) {
                this._strings[(i2 << 3) + 5] = null;
            }
            this._attributeCount = 0;
        }
    }

    public final void addAttributeWithQName(String uri, String localName, String qName, String type, String value) {
        int i2 = this._attributeCount << 3;
        if (i2 == this._strings.length) {
            resize(i2);
        }
        this._strings[i2 + 0] = null;
        this._strings[i2 + 1] = uri;
        this._strings[i2 + 2] = localName;
        this._strings[i2 + 3] = qName;
        this._strings[i2 + 4] = type;
        this._strings[i2 + 5] = value;
        this._attributeCount++;
    }

    public final void addAttributeWithPrefix(String prefix, String uri, String localName, String type, String value) {
        int i2 = this._attributeCount << 3;
        if (i2 == this._strings.length) {
            resize(i2);
        }
        this._strings[i2 + 0] = prefix;
        this._strings[i2 + 1] = uri;
        this._strings[i2 + 2] = localName;
        this._strings[i2 + 3] = null;
        this._strings[i2 + 4] = type;
        this._strings[i2 + 5] = value;
        this._attributeCount++;
    }

    private void resize(int length) {
        int newLength = length * 2;
        String[] strings = new String[newLength];
        System.arraycopy(this._strings, 0, strings, 0, length);
        this._strings = strings;
    }
}
