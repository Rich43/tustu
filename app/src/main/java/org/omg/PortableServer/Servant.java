package org.omg.PortableServer;

import java.lang.reflect.InvocationTargetException;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.PortableServer.portable.Delegate;

/* loaded from: rt.jar:org/omg/PortableServer/Servant.class */
public abstract class Servant {
    private transient Delegate _delegate = null;

    public abstract String[] _all_interfaces(POA poa, byte[] bArr);

    public final Delegate _get_delegate() {
        if (this._delegate == null) {
            throw new BAD_INV_ORDER("The Servant has not been associated with an ORB instance");
        }
        return this._delegate;
    }

    public final void _set_delegate(Delegate delegate) {
        this._delegate = delegate;
    }

    public final Object _this_object() {
        return _get_delegate().this_object(this);
    }

    public final Object _this_object(ORB orb) {
        try {
            ((org.omg.CORBA_2_3.ORB) orb).set_delegate(this);
            return _this_object();
        } catch (ClassCastException e2) {
            throw new BAD_PARAM("POA Servant requires an instance of org.omg.CORBA_2_3.ORB");
        }
    }

    public final ORB _orb() {
        return _get_delegate().orb(this);
    }

    public final POA _poa() {
        return _get_delegate().poa(this);
    }

    public final byte[] _object_id() {
        return _get_delegate().object_id(this);
    }

    public POA _default_POA() {
        return _get_delegate().default_POA(this);
    }

    public boolean _is_a(String str) {
        return _get_delegate().is_a(this, str);
    }

    public boolean _non_existent() {
        return _get_delegate().non_existent(this);
    }

    public Object _get_interface_def() {
        Delegate delegate_get_delegate = _get_delegate();
        try {
            return delegate_get_delegate.get_interface_def(this);
        } catch (AbstractMethodError e2) {
            try {
                return (Object) delegate_get_delegate.getClass().getMethod("get_interface", Servant.class).invoke(delegate_get_delegate, this);
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
}
