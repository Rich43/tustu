package com.sun.org.apache.xalan.internal.xsltc;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/DOMEnhancedForDTM.class */
public interface DOMEnhancedForDTM extends DOM {
    short[] getMapping(String[] strArr, String[] strArr2, int[] iArr);

    int[] getReverseMapping(String[] strArr, String[] strArr2, int[] iArr);

    short[] getNamespaceMapping(String[] strArr);

    short[] getReverseNamespaceMapping(String[] strArr);

    String getDocumentURI();

    void setDocumentURI(String str);

    int getExpandedTypeID2(int i2);

    boolean hasDOMSource();

    int getElementById(String str);
}
