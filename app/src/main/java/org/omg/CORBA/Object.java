package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/Object.class */
public interface Object {
    boolean _is_a(String str);

    boolean _is_equivalent(Object object);

    boolean _non_existent();

    int _hash(int i2);

    Object _duplicate();

    void _release();

    Object _get_interface_def();

    Request _request(String str);

    Request _create_request(Context context, String str, NVList nVList, NamedValue namedValue);

    Request _create_request(Context context, String str, NVList nVList, NamedValue namedValue, ExceptionList exceptionList, ContextList contextList);

    Policy _get_policy(int i2);

    DomainManager[] _get_domain_managers();

    Object _set_policy_override(Policy[] policyArr, SetOverrideType setOverrideType);
}
