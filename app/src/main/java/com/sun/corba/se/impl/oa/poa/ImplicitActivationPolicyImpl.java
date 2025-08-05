package com.sun.corba.se.impl.oa.poa;

import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.ImplicitActivationPolicy;
import org.omg.PortableServer.ImplicitActivationPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/ImplicitActivationPolicyImpl.class */
final class ImplicitActivationPolicyImpl extends LocalObject implements ImplicitActivationPolicy {
    private ImplicitActivationPolicyValue value;

    public ImplicitActivationPolicyImpl(ImplicitActivationPolicyValue implicitActivationPolicyValue) {
        this.value = implicitActivationPolicyValue;
    }

    @Override // org.omg.PortableServer.ImplicitActivationPolicyOperations
    public ImplicitActivationPolicyValue value() {
        return this.value;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return 20;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return new ImplicitActivationPolicyImpl(this.value);
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
        this.value = null;
    }

    public String toString() {
        return "ImplicitActivationPolicy[" + (this.value.value() == 0 ? "IMPLICIT_ACTIVATION" : "NO_IMPLICIT_ACTIVATION]");
    }
}
