package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/RevalidationHandler.class */
public interface RevalidationHandler extends XMLDocumentFilter {
    boolean characterData(String str, Augmentations augmentations);
}
