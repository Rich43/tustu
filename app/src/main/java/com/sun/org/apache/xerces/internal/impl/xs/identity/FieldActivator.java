package com.sun.org.apache.xerces.internal.impl.xs.identity;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/FieldActivator.class */
public interface FieldActivator {
    void startValueScopeFor(IdentityConstraint identityConstraint, int i2);

    XPathMatcher activateField(Field field, int i2);

    void setMayMatch(Field field, Boolean bool);

    Boolean mayMatch(Field field);

    void endValueScopeFor(IdentityConstraint identityConstraint, int i2);
}
