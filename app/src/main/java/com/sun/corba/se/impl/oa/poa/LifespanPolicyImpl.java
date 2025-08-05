package com.sun.corba.se.impl.oa.poa;

import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.LifespanPolicy;
import org.omg.PortableServer.LifespanPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/LifespanPolicyImpl.class */
final class LifespanPolicyImpl extends LocalObject implements LifespanPolicy {
    private LifespanPolicyValue value;

    public LifespanPolicyImpl(LifespanPolicyValue lifespanPolicyValue) {
        this.value = lifespanPolicyValue;
    }

    @Override // org.omg.PortableServer.LifespanPolicyOperations
    public LifespanPolicyValue value() {
        return this.value;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return 17;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return new LifespanPolicyImpl(this.value);
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
        this.value = null;
    }

    public String toString() {
        return "LifespanPolicy[" + (this.value.value() == 0 ? "TRANSIENT" : "PERSISTENT]");
    }
}
