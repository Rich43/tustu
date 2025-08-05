package com.sun.corba.se.impl.oa.poa;

import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.ServantRetentionPolicy;
import org.omg.PortableServer.ServantRetentionPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/ServantRetentionPolicyImpl.class */
final class ServantRetentionPolicyImpl extends LocalObject implements ServantRetentionPolicy {
    private ServantRetentionPolicyValue value;

    public ServantRetentionPolicyImpl(ServantRetentionPolicyValue servantRetentionPolicyValue) {
        this.value = servantRetentionPolicyValue;
    }

    @Override // org.omg.PortableServer.ServantRetentionPolicyOperations
    public ServantRetentionPolicyValue value() {
        return this.value;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return 21;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return new ServantRetentionPolicyImpl(this.value);
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
        this.value = null;
    }

    public String toString() {
        return "ServantRetentionPolicy[" + (this.value.value() == 0 ? "RETAIN" : "NON_RETAIN]");
    }
}
