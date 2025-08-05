package com.sun.org.apache.xerces.internal.impl.validation;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/validation/EntityState.class */
public interface EntityState {
    boolean isEntityDeclared(String str);

    boolean isEntityUnparsed(String str);
}
