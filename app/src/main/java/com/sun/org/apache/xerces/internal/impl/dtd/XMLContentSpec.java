package com.sun.org.apache.xerces.internal.impl.dtd;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/XMLContentSpec.class */
public class XMLContentSpec {
    public static final short CONTENTSPECNODE_LEAF = 0;
    public static final short CONTENTSPECNODE_ZERO_OR_ONE = 1;
    public static final short CONTENTSPECNODE_ZERO_OR_MORE = 2;
    public static final short CONTENTSPECNODE_ONE_OR_MORE = 3;
    public static final short CONTENTSPECNODE_CHOICE = 4;
    public static final short CONTENTSPECNODE_SEQ = 5;
    public static final short CONTENTSPECNODE_ANY = 6;
    public static final short CONTENTSPECNODE_ANY_OTHER = 7;
    public static final short CONTENTSPECNODE_ANY_LOCAL = 8;
    public static final short CONTENTSPECNODE_ANY_LAX = 22;
    public static final short CONTENTSPECNODE_ANY_OTHER_LAX = 23;
    public static final short CONTENTSPECNODE_ANY_LOCAL_LAX = 24;
    public static final short CONTENTSPECNODE_ANY_SKIP = 38;
    public static final short CONTENTSPECNODE_ANY_OTHER_SKIP = 39;
    public static final short CONTENTSPECNODE_ANY_LOCAL_SKIP = 40;
    public short type;
    public Object value;
    public Object otherValue;

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/XMLContentSpec$Provider.class */
    public interface Provider {
        boolean getContentSpec(int i2, XMLContentSpec xMLContentSpec);
    }

    public XMLContentSpec() {
        clear();
    }

    public XMLContentSpec(short type, Object value, Object otherValue) {
        setValues(type, value, otherValue);
    }

    public XMLContentSpec(XMLContentSpec contentSpec) {
        setValues(contentSpec);
    }

    public XMLContentSpec(Provider provider, int contentSpecIndex) {
        setValues(provider, contentSpecIndex);
    }

    public void clear() {
        this.type = (short) -1;
        this.value = null;
        this.otherValue = null;
    }

    public void setValues(short type, Object value, Object otherValue) {
        this.type = type;
        this.value = value;
        this.otherValue = otherValue;
    }

    public void setValues(XMLContentSpec contentSpec) {
        this.type = contentSpec.type;
        this.value = contentSpec.value;
        this.otherValue = contentSpec.otherValue;
    }

    public void setValues(Provider provider, int contentSpecIndex) {
        if (!provider.getContentSpec(contentSpecIndex, this)) {
            clear();
        }
    }

    public int hashCode() {
        return (this.type << 16) | (this.value.hashCode() << 8) | this.otherValue.hashCode();
    }

    public boolean equals(Object object) {
        if (object != null && (object instanceof XMLContentSpec)) {
            XMLContentSpec contentSpec = (XMLContentSpec) object;
            return this.type == contentSpec.type && this.value == contentSpec.value && this.otherValue == contentSpec.otherValue;
        }
        return false;
    }
}
