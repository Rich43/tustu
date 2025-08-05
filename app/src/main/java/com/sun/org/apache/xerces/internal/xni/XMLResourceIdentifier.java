package com.sun.org.apache.xerces.internal.xni;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/XMLResourceIdentifier.class */
public interface XMLResourceIdentifier {
    void setPublicId(String str);

    String getPublicId();

    void setExpandedSystemId(String str);

    String getExpandedSystemId();

    void setLiteralSystemId(String str);

    String getLiteralSystemId();

    void setBaseSystemId(String str);

    String getBaseSystemId();

    void setNamespace(String str);

    String getNamespace();
}
