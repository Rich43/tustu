package org.omg.CORBA_2_3.portable;

/* loaded from: rt.jar:org/omg/CORBA_2_3/portable/ObjectImpl.class */
public abstract class ObjectImpl extends org.omg.CORBA.portable.ObjectImpl {
    public String _get_codebase() {
        org.omg.CORBA.portable.Delegate delegate_get_delegate = _get_delegate();
        if (delegate_get_delegate instanceof Delegate) {
            return ((Delegate) delegate_get_delegate).get_codebase(this);
        }
        return null;
    }
}
