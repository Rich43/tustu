package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/ElementPSVI.class */
public interface ElementPSVI extends ItemPSVI {
    XSElementDeclaration getElementDeclaration();

    XSNotationDeclaration getNotation();

    boolean getNil();

    XSModel getSchemaInformation();
}
