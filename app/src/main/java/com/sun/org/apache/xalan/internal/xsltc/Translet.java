package com.sun.org.apache.xalan.internal.xsltc;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/Translet.class */
public interface Translet {
    void transform(DOM dom, SerializationHandler serializationHandler) throws TransletException;

    void transform(DOM dom, SerializationHandler[] serializationHandlerArr) throws TransletException;

    void transform(DOM dom, DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler) throws TransletException;

    Object addParameter(String str, Object obj);

    void buildKeys(DOM dom, DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler, int i2) throws TransletException;

    void addAuxiliaryClass(Class cls);

    Class getAuxiliaryClass(String str);

    String[] getNamesArray();

    String[] getUrisArray();

    int[] getTypesArray();

    String[] getNamespaceArray();

    boolean overrideDefaultParser();

    void setOverrideDefaultParser(boolean z2);
}
