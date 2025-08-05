package org.omg.CORBA.portable;

import java.lang.reflect.InvocationTargetException;
import org.omg.CORBA.BAD_OPERATION;
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

/* loaded from: rt.jar:org/omg/CORBA/portable/ObjectImpl.class */
public abstract class ObjectImpl implements Object {
    private transient Delegate __delegate;

    public abstract String[] _ids();

    public Delegate _get_delegate() {
        if (this.__delegate == null) {
            throw new BAD_OPERATION("The delegate has not been set!");
        }
        return this.__delegate;
    }

    public void _set_delegate(Delegate delegate) {
        this.__delegate = delegate;
    }

    @Override // org.omg.CORBA.Object
    public Object _duplicate() {
        return _get_delegate().duplicate(this);
    }

    @Override // org.omg.CORBA.Object
    public void _release() {
        _get_delegate().release(this);
    }

    @Override // org.omg.CORBA.Object
    public boolean _is_a(String str) {
        return _get_delegate().is_a(this, str);
    }

    @Override // org.omg.CORBA.Object
    public boolean _is_equivalent(Object object) {
        return _get_delegate().is_equivalent(this, object);
    }

    @Override // org.omg.CORBA.Object
    public boolean _non_existent() {
        return _get_delegate().non_existent(this);
    }

    @Override // org.omg.CORBA.Object
    public int _hash(int i2) {
        return _get_delegate().hash(this, i2);
    }

    @Override // org.omg.CORBA.Object
    public Request _request(String str) {
        return _get_delegate().request(this, str);
    }

    @Override // org.omg.CORBA.Object
    public Request _create_request(Context context, String str, NVList nVList, NamedValue namedValue) {
        return _get_delegate().create_request(this, context, str, nVList, namedValue);
    }

    @Override // org.omg.CORBA.Object
    public Request _create_request(Context context, String str, NVList nVList, NamedValue namedValue, ExceptionList exceptionList, ContextList contextList) {
        return _get_delegate().create_request(this, context, str, nVList, namedValue, exceptionList, contextList);
    }

    @Override // org.omg.CORBA.Object
    public Object _get_interface_def() {
        Delegate delegate_get_delegate = _get_delegate();
        try {
            return delegate_get_delegate.get_interface_def(this);
        } catch (NO_IMPLEMENT e2) {
            try {
                return (Object) delegate_get_delegate.getClass().getMethod("get_interface", Object.class).invoke(delegate_get_delegate, this);
            } catch (RuntimeException e3) {
                throw e3;
            } catch (InvocationTargetException e4) {
                Throwable targetException = e4.getTargetException();
                if (targetException instanceof Error) {
                    throw ((Error) targetException);
                }
                if (targetException instanceof RuntimeException) {
                    throw ((RuntimeException) targetException);
                }
                throw new NO_IMPLEMENT();
            } catch (Exception e5) {
                throw new NO_IMPLEMENT();
            }
        }
    }

    public ORB _orb() {
        return _get_delegate().orb(this);
    }

    @Override // org.omg.CORBA.Object
    public Policy _get_policy(int i2) {
        return _get_delegate().get_policy(this, i2);
    }

    @Override // org.omg.CORBA.Object
    public DomainManager[] _get_domain_managers() {
        return _get_delegate().get_domain_managers(this);
    }

    @Override // org.omg.CORBA.Object
    public Object _set_policy_override(Policy[] policyArr, SetOverrideType setOverrideType) {
        return _get_delegate().set_policy_override(this, policyArr, setOverrideType);
    }

    public boolean _is_local() {
        return _get_delegate().is_local(this);
    }

    public ServantObject _servant_preinvoke(String str, Class cls) {
        return _get_delegate().servant_preinvoke(this, str, cls);
    }

    public void _servant_postinvoke(ServantObject servantObject) {
        _get_delegate().servant_postinvoke(this, servantObject);
    }

    public OutputStream _request(String str, boolean z2) {
        return _get_delegate().request(this, str, z2);
    }

    public InputStream _invoke(OutputStream outputStream) throws ApplicationException, RemarshalException {
        return _get_delegate().invoke(this, outputStream);
    }

    public void _releaseReply(InputStream inputStream) {
        _get_delegate().releaseReply(this, inputStream);
    }

    public String toString() {
        if (this.__delegate != null) {
            return this.__delegate.toString(this);
        }
        return getClass().getName() + ": no delegate set";
    }

    public int hashCode() {
        if (this.__delegate != null) {
            return this.__delegate.hashCode(this);
        }
        return super.hashCode();
    }

    public boolean equals(Object obj) {
        if (this.__delegate != null) {
            return this.__delegate.equals(this, obj);
        }
        return this == obj;
    }
}
