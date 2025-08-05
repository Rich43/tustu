package com.sun.org.apache.xerces.internal.xni;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/XMLAttributes.class */
public interface XMLAttributes {
    int addAttribute(QName qName, String str, String str2);

    void removeAllAttributes();

    void removeAttributeAt(int i2);

    int getLength();

    int getIndex(String str);

    int getIndex(String str, String str2);

    void setName(int i2, QName qName);

    void getName(int i2, QName qName);

    String getPrefix(int i2);

    String getURI(int i2);

    String getLocalName(int i2);

    String getQName(int i2);

    QName getQualifiedName(int i2);

    void setType(int i2, String str);

    String getType(int i2);

    String getType(String str);

    String getType(String str, String str2);

    void setValue(int i2, String str);

    void setValue(int i2, String str, XMLString xMLString);

    String getValue(int i2);

    String getValue(String str);

    String getValue(String str, String str2);

    void setNonNormalizedValue(int i2, String str);

    String getNonNormalizedValue(int i2);

    void setSpecified(int i2, boolean z2);

    boolean isSpecified(int i2);

    Augmentations getAugmentations(int i2);

    Augmentations getAugmentations(String str, String str2);

    Augmentations getAugmentations(String str);

    void setAugmentations(int i2, Augmentations augmentations);
}
