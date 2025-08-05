package com.sun.org.apache.xerces.internal.xni;

import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/XMLDTDContentModelHandler.class */
public interface XMLDTDContentModelHandler {
    public static final short SEPARATOR_CHOICE = 0;
    public static final short SEPARATOR_SEQUENCE = 1;
    public static final short OCCURS_ZERO_OR_ONE = 2;
    public static final short OCCURS_ZERO_OR_MORE = 3;
    public static final short OCCURS_ONE_OR_MORE = 4;

    void startContentModel(String str, Augmentations augmentations) throws XNIException;

    void any(Augmentations augmentations) throws XNIException;

    void empty(Augmentations augmentations) throws XNIException;

    void startGroup(Augmentations augmentations) throws XNIException;

    void pcdata(Augmentations augmentations) throws XNIException;

    void element(String str, Augmentations augmentations) throws XNIException;

    void separator(short s2, Augmentations augmentations) throws XNIException;

    void occurrence(short s2, Augmentations augmentations) throws XNIException;

    void endGroup(Augmentations augmentations) throws XNIException;

    void endContentModel(Augmentations augmentations) throws XNIException;

    void setDTDContentModelSource(XMLDTDContentModelSource xMLDTDContentModelSource);

    XMLDTDContentModelSource getDTDContentModelSource();
}
