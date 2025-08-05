package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/PSVIProvider.class */
public interface PSVIProvider {
    ElementPSVI getElementPSVI();

    AttributePSVI getAttributePSVI(int i2);

    AttributePSVI getAttributePSVIByName(String str, String str2);
}
