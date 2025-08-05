package com.sun.org.apache.xerces.internal.impl.xs.identity;

import com.sun.org.apache.xerces.internal.xs.XSIDCDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/KeyRef.class */
public class KeyRef extends IdentityConstraint {
    protected UniqueOrKey fKey;

    public KeyRef(String namespace, String identityConstraintName, String elemName, UniqueOrKey key) {
        super(namespace, identityConstraintName, elemName);
        this.fKey = key;
        this.type = (short) 2;
    }

    public UniqueOrKey getKey() {
        return this.fKey;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint, com.sun.org.apache.xerces.internal.xs.XSIDCDefinition
    public XSIDCDefinition getRefKey() {
        return this.fKey;
    }
}
