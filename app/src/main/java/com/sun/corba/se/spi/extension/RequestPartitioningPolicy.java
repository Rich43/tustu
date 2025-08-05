package com.sun.corba.se.spi.extension;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Policy;

/* loaded from: rt.jar:com/sun/corba/se/spi/extension/RequestPartitioningPolicy.class */
public class RequestPartitioningPolicy extends LocalObject implements Policy {
    private static ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.OA_IOR);
    public static final int DEFAULT_VALUE = 0;
    private final int value;

    public RequestPartitioningPolicy(int i2) {
        if (i2 < 0 || i2 > 63) {
            throw wrapper.invalidRequestPartitioningPolicyValue(new Integer(i2), new Integer(0), new Integer(63));
        }
        this.value = i2;
    }

    public int getValue() {
        return this.value;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        return ORBConstants.REQUEST_PARTITIONING_POLICY;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        return this;
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
    }

    public String toString() {
        return "RequestPartitioningPolicy[" + this.value + "]";
    }
}
