package com.sun.org.apache.xml.internal.serializer;

import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/XSLOutputAttributes.class */
public interface XSLOutputAttributes {
    String getDoctypePublic();

    String getDoctypeSystem();

    String getEncoding();

    boolean getIndent();

    int getIndentAmount();

    String getMediaType();

    boolean getOmitXMLDeclaration();

    String getStandalone();

    String getVersion();

    void setCdataSectionElements(ArrayList<String> arrayList);

    void setDoctype(String str, String str2);

    void setDoctypePublic(String str);

    void setDoctypeSystem(String str);

    void setEncoding(String str);

    void setIndent(boolean z2);

    void setMediaType(String str);

    void setOmitXMLDeclaration(boolean z2);

    void setStandalone(String str);

    void setVersion(String str);

    String getOutputProperty(String str);

    String getOutputPropertyDefault(String str);

    void setOutputProperty(String str, String str2);

    void setOutputPropertyDefault(String str, String str2);
}
