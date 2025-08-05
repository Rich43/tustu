package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSMultiValueFacet.class */
public interface XSMultiValueFacet extends XSObject {
    short getFacetKind();

    StringList getLexicalFacetValues();

    XSObjectList getAnnotations();
}
