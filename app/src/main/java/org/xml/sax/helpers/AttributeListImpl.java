package org.xml.sax.helpers;

import java.util.Vector;
import org.xml.sax.AttributeList;

/* loaded from: rt.jar:org/xml/sax/helpers/AttributeListImpl.class */
public class AttributeListImpl implements AttributeList {
    Vector names = new Vector();
    Vector types = new Vector();
    Vector values = new Vector();

    public AttributeListImpl() {
    }

    public AttributeListImpl(AttributeList atts) {
        setAttributeList(atts);
    }

    public void setAttributeList(AttributeList atts) {
        int count = atts.getLength();
        clear();
        for (int i2 = 0; i2 < count; i2++) {
            addAttribute(atts.getName(i2), atts.getType(i2), atts.getValue(i2));
        }
    }

    public void addAttribute(String name, String type, String value) {
        this.names.addElement(name);
        this.types.addElement(type);
        this.values.addElement(value);
    }

    public void removeAttribute(String name) {
        int i2 = this.names.indexOf(name);
        if (i2 >= 0) {
            this.names.removeElementAt(i2);
            this.types.removeElementAt(i2);
            this.values.removeElementAt(i2);
        }
    }

    public void clear() {
        this.names.removeAllElements();
        this.types.removeAllElements();
        this.values.removeAllElements();
    }

    @Override // org.xml.sax.AttributeList
    public int getLength() {
        return this.names.size();
    }

    @Override // org.xml.sax.AttributeList
    public String getName(int i2) {
        if (i2 < 0) {
            return null;
        }
        try {
            return (String) this.names.elementAt(i2);
        } catch (ArrayIndexOutOfBoundsException e2) {
            return null;
        }
    }

    @Override // org.xml.sax.AttributeList
    public String getType(int i2) {
        if (i2 < 0) {
            return null;
        }
        try {
            return (String) this.types.elementAt(i2);
        } catch (ArrayIndexOutOfBoundsException e2) {
            return null;
        }
    }

    @Override // org.xml.sax.AttributeList
    public String getValue(int i2) {
        if (i2 < 0) {
            return null;
        }
        try {
            return (String) this.values.elementAt(i2);
        } catch (ArrayIndexOutOfBoundsException e2) {
            return null;
        }
    }

    @Override // org.xml.sax.AttributeList
    public String getType(String name) {
        return getType(this.names.indexOf(name));
    }

    @Override // org.xml.sax.AttributeList
    public String getValue(String name) {
        return getValue(this.names.indexOf(name));
    }
}
