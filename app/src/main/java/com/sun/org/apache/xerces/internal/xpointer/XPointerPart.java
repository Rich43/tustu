package com.sun.org.apache.xerces.internal.xpointer;

import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XNIException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xpointer/XPointerPart.class */
public interface XPointerPart {
    public static final int EVENT_ELEMENT_START = 0;
    public static final int EVENT_ELEMENT_END = 1;
    public static final int EVENT_ELEMENT_EMPTY = 2;

    void parseXPointer(String str) throws XNIException;

    boolean resolveXPointer(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations, int i2) throws XNIException;

    boolean isFragmentResolved() throws XNIException;

    boolean isChildFragmentResolved() throws XNIException;

    String getSchemeName();

    String getSchemeData();

    void setSchemeName(String str);

    void setSchemeData(String str);
}
