package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSFacet.class */
public interface XSFacet extends XSObject {
    short getFacetKind();

    String getLexicalFacetValue();

    boolean getFixed();

    XSAnnotation getAnnotation();

    XSObjectList getAnnotations();
}
