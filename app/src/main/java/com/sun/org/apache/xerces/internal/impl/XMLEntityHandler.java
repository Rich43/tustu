package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLEntityHandler.class */
public interface XMLEntityHandler {
    void startEntity(String str, XMLResourceIdentifier xMLResourceIdentifier, String str2, Augmentations augmentations) throws XNIException;

    void endEntity(String str, Augmentations augmentations) throws IOException, XNIException;
}
