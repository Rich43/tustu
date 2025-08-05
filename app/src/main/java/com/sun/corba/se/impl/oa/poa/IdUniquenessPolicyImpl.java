package com.sun.corba.se.impl.oa.poa;

import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.IdUniquenessPolicy;
import org.omg.PortableServer.IdUniquenessPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/IdUniquenessPolicyImpl.class */
final class IdUniquenessPolicyImpl extends LocalObject implements IdUniquenessPolicy {
    private IdUniquenessPolicyValue value;

    public IdUniquenessPolicyImpl(IdUniquenessPolicyValue idUniquenessPolicyValue) {
        this.value = idUniquenessPolicyValue;
    }

    @Override // org.omg.PortableServer.IdUniquenessPolicyOperations
    public IdUniquenessPolicyValue value() {
        return this.value;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return 18;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return new IdUniquenessPolicyImpl(this.value);
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
        this.value = null;
    }

    public String toString() {
        return "IdUniquenessPolicy[" + (this.value.value() == 0 ? "UNIQUE_ID" : "MULTIPLE_ID]");
    }
}
