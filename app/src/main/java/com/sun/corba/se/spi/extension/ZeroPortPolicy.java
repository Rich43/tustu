package com.sun.corba.se.spi.extension;

import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;

/* loaded from: rt.jar:com/sun/corba/se/spi/extension/ZeroPortPolicy.class */
public class ZeroPortPolicy extends LocalObject implements Policy {
    private static ZeroPortPolicy policy = new ZeroPortPolicy(true);
    private boolean flag;

    private ZeroPortPolicy(boolean z2) {
        this.flag = true;
        this.flag = z2;
    }

    public String toString() {
        return "ZeroPortPolicy[" + this.flag + "]";
    }

    public boolean forceZeroPort() {
        return this.flag;
    }

    public static synchronized ZeroPortPolicy getPolicy() {
        return policy;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return 1398079489;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return this;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
    }
}
