package org.xml.sax;

/* loaded from: rt.jar:org/xml/sax/Attributes.class */
public interface Attributes {
    int getLength();

    String getURI(int i2);

    String getLocalName(int i2);

    String getQName(int i2);

    String getType(int i2);

    String getValue(int i2);

    int getIndex(String str, String str2);

    int getIndex(String str);

    String getType(String str, String str2);

    String getType(String str);

    String getValue(String str, String str2);

    String getValue(String str);
}
