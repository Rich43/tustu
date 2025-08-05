package com.sun.corba.se.impl.oa.poa;

import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.RequestProcessingPolicy;
import org.omg.PortableServer.RequestProcessingPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/RequestProcessingPolicyImpl.class */
public class RequestProcessingPolicyImpl extends LocalObject implements RequestProcessingPolicy {
    private RequestProcessingPolicyValue value;

    public RequestProcessingPolicyImpl(RequestProcessingPolicyValue requestProcessingPolicyValue) {
        this.value = requestProcessingPolicyValue;
    }

    @Override // org.omg.PortableServer.RequestProcessingPolicyOperations
    public RequestProcessingPolicyValue value() {
        return this.value;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return 22;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return new RequestProcessingPolicyImpl(this.value);
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
        this.value = null;
    }

    public String toString() {
        String str = null;
        switch (this.value.value()) {
            case 0:
                str = "USE_ACTIVE_OBJECT_MAP_ONLY";
                break;
            case 1:
                str = "USE_DEFAULT_SERVANT";
                break;
            case 2:
                str = "USE_SERVANT_MANAGER";
                break;
        }
        return "RequestProcessingPolicy[" + str + "]";
    }
}
