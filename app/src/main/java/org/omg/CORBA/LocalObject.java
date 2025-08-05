package org.omg.CORBA;

import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:org/omg/CORBA/LocalObject.class */
public class LocalObject implements Object {
    private static String reason = "This is a locally constrained object.";

    @Override // org.omg.CORBA.Object
    public boolean _is_equivalent(Object object) {
        return equals(object);
    }

    @Override // org.omg.CORBA.Object
    public boolean _non_existent() {
        return false;
    }

    @Override // org.omg.CORBA.Object
    public int _hash(int i2) {
        return hashCode();
    }

    @Override // org.omg.CORBA.Object
    public boolean _is_a(String str) {
        throw new NO_IMPLEMENT(reason);
    }

    @Override // org.omg.CORBA.Object
    public Object _duplicate() {
        throw new NO_IMPLEMENT(reason);
    }

    @Override // org.omg.CORBA.Object
    public void _release() {
        throw new NO_IMPLEMENT(reason);
    }

    @Override // org.omg.CORBA.Object
    public Request _request(String str) {
        throw new NO_IMPLEMENT(reason);
    }

    @Override // org.omg.CORBA.Object
    public Request _create_request(Context context, String str, NVList nVList, NamedValue namedValue) {
        throw new NO_IMPLEMENT(reason);
    }

    @Override // org.omg.CORBA.Object
    public Request _create_request(Context context, String str, NVList nVList, NamedValue namedValue, ExceptionList exceptionList, ContextList contextList) {
        throw new NO_IMPLEMENT(reason);
    }

    public Object _get_interface() {
        throw new NO_IMPLEMENT(reason);
    }

    @Override // org.omg.CORBA.Object
    public Object _get_interface_def() {
        throw new NO_IMPLEMENT(reason);
    }

    public ORB _orb() {
        throw new NO_IMPLEMENT(reason);
    }

    @Override // org.omg.CORBA.Object
    public Policy _get_policy(int i2) {
        throw new NO_IMPLEMENT(reason);
    }

    @Override // org.omg.CORBA.Object
    public DomainManager[] _get_domain_managers() {
        throw new NO_IMPLEMENT(reason);
    }

    @Override // org.omg.CORBA.Object
    public Object _set_policy_override(Policy[] policyArr, SetOverrideType setOverrideType) {
        throw new NO_IMPLEMENT(reason);
    }

    public boolean _is_local() {
        throw new NO_IMPLEMENT(reason);
    }

    public ServantObject _servant_preinvoke(String str, Class cls) {
        throw new NO_IMPLEMENT(reason);
    }

    public void _servant_postinvoke(ServantObject servantObject) {
        throw new NO_IMPLEMENT(reason);
    }

    public OutputStream _request(String str, boolean z2) {
        throw new NO_IMPLEMENT(reason);
    }

    public InputStream _invoke(OutputStream outputStream) throws ApplicationException, RemarshalException {
        throw new NO_IMPLEMENT(reason);
    }

    public void _releaseReply(InputStream inputStream) {
        throw new NO_IMPLEMENT(reason);
    }

    public boolean validate_connection() {
        throw new NO_IMPLEMENT(reason);
    }
}
