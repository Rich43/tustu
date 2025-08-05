package org.omg.PortableInterceptor;

import org.omg.CORBA.Policy;
import org.omg.IOP.TaggedComponent;

/* loaded from: rt.jar:org/omg/PortableInterceptor/IORInfoOperations.class */
public interface IORInfoOperations {
    Policy get_effective_policy(int i2);

    void add_ior_component(TaggedComponent taggedComponent);

    void add_ior_component_to_profile(TaggedComponent taggedComponent, int i2);

    int manager_id();

    short state();

    ObjectReferenceTemplate adapter_template();

    ObjectReferenceFactory current_factory();

    void current_factory(ObjectReferenceFactory objectReferenceFactory);
}
