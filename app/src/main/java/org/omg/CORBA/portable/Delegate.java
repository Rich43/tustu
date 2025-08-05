package org.omg.CORBA.portable;

import jdk.internal.dynalink.CallSiteDescriptor;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;

/* loaded from: rt.jar:org/omg/CORBA/portable/Delegate.class */
public abstract class Delegate {
    public abstract Object get_interface_def(Object object);

    public abstract Object duplicate(Object object);

    public abstract void release(Object object);

    public abstract boolean is_a(Object object, String str);

    public abstract boolean non_existent(Object object);

    public abstract boolean is_equivalent(Object object, Object object2);

    public abstract int hash(Object object, int i2);

    public abstract Request request(Object object, String str);

    public abstract Request create_request(Object object, Context context, String str, NVList nVList, NamedValue namedValue);

    public abstract Request create_request(Object object, Context context, String str, NVList nVList, NamedValue namedValue, ExceptionList exceptionList, ContextList contextList);

    public ORB orb(Object object) {
        throw new NO_IMPLEMENT();
    }

    public Policy get_policy(Object object, int i2) {
        throw new NO_IMPLEMENT();
    }

    public DomainManager[] get_domain_managers(Object object) {
        throw new NO_IMPLEMENT();
    }

    public Object set_policy_override(Object object, Policy[] policyArr, SetOverrideType setOverrideType) {
        throw new NO_IMPLEMENT();
    }

    public boolean is_local(Object object) {
        return false;
    }

    public ServantObject servant_preinvoke(Object object, String str, Class cls) {
        return null;
    }

    public void servant_postinvoke(Object object, ServantObject servantObject) {
    }

    public OutputStream request(Object object, String str, boolean z2) {
        throw new NO_IMPLEMENT();
    }

    public InputStream invoke(Object object, OutputStream outputStream) throws ApplicationException, RemarshalException {
        throw new NO_IMPLEMENT();
    }

    public void releaseReply(Object object, InputStream inputStream) {
        throw new NO_IMPLEMENT();
    }

    public String toString(Object object) {
        return object.getClass().getName() + CallSiteDescriptor.TOKEN_DELIMITER + toString();
    }

    public int hashCode(Object object) {
        return System.identityHashCode(object);
    }

    public boolean equals(Object object, Object obj) {
        return object == obj;
    }
}
