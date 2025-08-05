package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.IOP.ServiceContext;
import org.omg.IOP.TaggedComponent;
import org.omg.IOP.TaggedProfile;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ClientRequestInfoOperations.class */
public interface ClientRequestInfoOperations extends RequestInfoOperations {
    Object target();

    Object effective_target();

    TaggedProfile effective_profile();

    Any received_exception();

    String received_exception_id();

    TaggedComponent get_effective_component(int i2);

    TaggedComponent[] get_effective_components(int i2);

    Policy get_request_policy(int i2);

    void add_request_service_context(ServiceContext serviceContext, boolean z2);
}
