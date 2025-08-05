package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.Policy;
import org.omg.IOP.ServiceContext;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ServerRequestInfoOperations.class */
public interface ServerRequestInfoOperations extends RequestInfoOperations {
    Any sending_exception();

    byte[] object_id();

    byte[] adapter_id();

    String server_id();

    String orb_id();

    String[] adapter_name();

    String target_most_derived_interface();

    Policy get_server_policy(int i2);

    void set_slot(int i2, Any any) throws InvalidSlot;

    boolean target_is_a(String str);

    void add_reply_service_context(ServiceContext serviceContext, boolean z2);
}
