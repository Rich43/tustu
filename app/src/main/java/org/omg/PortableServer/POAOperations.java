package org.omg.PortableServer;

import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.POAPackage.AdapterAlreadyExists;
import org.omg.PortableServer.POAPackage.AdapterNonExistent;
import org.omg.PortableServer.POAPackage.InvalidPolicy;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongAdapter;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/* loaded from: rt.jar:org/omg/PortableServer/POAOperations.class */
public interface POAOperations {
    POA create_POA(String str, POAManager pOAManager, Policy[] policyArr) throws InvalidPolicy, AdapterAlreadyExists;

    POA find_POA(String str, boolean z2) throws AdapterNonExistent;

    void destroy(boolean z2, boolean z3);

    ThreadPolicy create_thread_policy(ThreadPolicyValue threadPolicyValue);

    LifespanPolicy create_lifespan_policy(LifespanPolicyValue lifespanPolicyValue);

    IdUniquenessPolicy create_id_uniqueness_policy(IdUniquenessPolicyValue idUniquenessPolicyValue);

    IdAssignmentPolicy create_id_assignment_policy(IdAssignmentPolicyValue idAssignmentPolicyValue);

    ImplicitActivationPolicy create_implicit_activation_policy(ImplicitActivationPolicyValue implicitActivationPolicyValue);

    ServantRetentionPolicy create_servant_retention_policy(ServantRetentionPolicyValue servantRetentionPolicyValue);

    RequestProcessingPolicy create_request_processing_policy(RequestProcessingPolicyValue requestProcessingPolicyValue);

    String the_name();

    POA the_parent();

    POA[] the_children();

    POAManager the_POAManager();

    AdapterActivator the_activator();

    void the_activator(AdapterActivator adapterActivator);

    ServantManager get_servant_manager() throws WrongPolicy;

    void set_servant_manager(ServantManager servantManager) throws WrongPolicy;

    Servant get_servant() throws NoServant, WrongPolicy;

    void set_servant(Servant servant) throws WrongPolicy;

    byte[] activate_object(Servant servant) throws ServantAlreadyActive, WrongPolicy;

    void activate_object_with_id(byte[] bArr, Servant servant) throws ObjectAlreadyActive, ServantAlreadyActive, WrongPolicy;

    void deactivate_object(byte[] bArr) throws ObjectNotActive, WrongPolicy;

    Object create_reference(String str) throws WrongPolicy;

    Object create_reference_with_id(byte[] bArr, String str);

    byte[] servant_to_id(Servant servant) throws ServantNotActive, WrongPolicy;

    Object servant_to_reference(Servant servant) throws ServantNotActive, WrongPolicy;

    Servant reference_to_servant(Object object) throws ObjectNotActive, WrongAdapter, WrongPolicy;

    byte[] reference_to_id(Object object) throws WrongAdapter, WrongPolicy;

    Servant id_to_servant(byte[] bArr) throws ObjectNotActive, WrongPolicy;

    Object id_to_reference(byte[] bArr) throws ObjectNotActive, WrongPolicy;

    byte[] id();
}
