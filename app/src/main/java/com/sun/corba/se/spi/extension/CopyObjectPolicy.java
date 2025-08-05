package com.sun.corba.se.spi.extension;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;

/* loaded from: rt.jar:com/sun/corba/se/spi/extension/CopyObjectPolicy.class */
public class CopyObjectPolicy extends LocalObject implements Policy {
    private final int value;

    public CopyObjectPolicy(int i2) {
        this.value = i2;
    }

    public int getValue() {
        return this.value;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return ORBConstants.COPY_OBJECT_POLICY;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return this;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
    }

    public String toString() {
        return "CopyObjectPolicy[" + this.value + "]";
    }
}
