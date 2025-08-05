package com.sun.corba.se.impl.oa.poa;

import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.IdAssignmentPolicy;
import org.omg.PortableServer.IdAssignmentPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/IdAssignmentPolicyImpl.class */
final class IdAssignmentPolicyImpl extends LocalObject implements IdAssignmentPolicy {
    private IdAssignmentPolicyValue value;

    public IdAssignmentPolicyImpl(IdAssignmentPolicyValue idAssignmentPolicyValue) {
        this.value = idAssignmentPolicyValue;
    }

    @Override // org.omg.PortableServer.IdAssignmentPolicyOperations
    public IdAssignmentPolicyValue value() {
        return this.value;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return 19;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return new IdAssignmentPolicyImpl(this.value);
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
        this.value = null;
    }

    public String toString() {
        return "IdAssignmentPolicy[" + (this.value.value() == 0 ? "USER_ID" : "SYSTEM_ID]");
    }
}
