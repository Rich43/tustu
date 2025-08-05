package org.omg.PortableInterceptor;

import org.omg.CORBA.Object;
import org.omg.IOP.CodecFactory;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.omg.PortableInterceptor.ORBInitInfoPackage.InvalidName;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ORBInitInfoOperations.class */
public interface ORBInitInfoOperations {
    String[] arguments();

    String orb_id();

    CodecFactory codec_factory();

    void register_initial_reference(String str, Object object) throws InvalidName;

    Object resolve_initial_references(String str) throws InvalidName;

    void add_client_request_interceptor(ClientRequestInterceptor clientRequestInterceptor) throws DuplicateName;

    void add_server_request_interceptor(ServerRequestInterceptor serverRequestInterceptor) throws DuplicateName;

    void add_ior_interceptor(IORInterceptor iORInterceptor) throws DuplicateName;

    int allocate_slot_id();

    void register_policy_factory(int i2, PolicyFactory policyFactory);
}
