package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;

/* loaded from: rt.jar:org/omg/PortableInterceptor/PolicyFactoryOperations.class */
public interface PolicyFactoryOperations {
    Policy create_policy(int i2, Any any) throws PolicyError;
}
