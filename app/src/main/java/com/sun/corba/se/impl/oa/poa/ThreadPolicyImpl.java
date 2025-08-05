package com.sun.corba.se.impl.oa.poa;

import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.ThreadPolicy;
import org.omg.PortableServer.ThreadPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/ThreadPolicyImpl.class */
final class ThreadPolicyImpl extends LocalObject implements ThreadPolicy {
    private ThreadPolicyValue value;

    public ThreadPolicyImpl(ThreadPolicyValue threadPolicyValue) {
        this.value = threadPolicyValue;
    }

    @Override // org.omg.PortableServer.ThreadPolicyOperations
    public ThreadPolicyValue value() {
        return this.value;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return 16;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return new ThreadPolicyImpl(this.value);
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
        this.value = null;
    }

    public String toString() {
        return "ThreadPolicy[" + (this.value.value() == 1 ? "SINGLE_THREAD_MODEL" : "ORB_CTRL_MODEL]");
    }
}
