package org.omg.CORBA;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:org/omg/CORBA/_PolicyStub.class */
public class _PolicyStub extends ObjectImpl implements Policy {
    private static String[] __ids = {"IDL:omg.org/CORBA/Policy:1.0"};

    public _PolicyStub() {
    }

    public _PolicyStub(Delegate delegate) {
        _set_delegate(delegate);
    }

    @Override // org.omg.CORBA.PolicyOperations
    public int policy_type() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("_get_policy_type", true));
                    int i2 = PolicyTypeHelper.read(inputStream_invoke);
                    _releaseReply(inputStream_invoke);
                    return i2;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                int iPolicy_type = policy_type();
                _releaseReply(inputStream_invoke);
                return iPolicy_type;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.PolicyOperations
    public Policy copy() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("copy", true));
                    Policy policy = PolicyHelper.read(inputStream_invoke);
                    _releaseReply(inputStream_invoke);
                    return policy;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                Policy policyCopy = copy();
                _releaseReply(inputStream_invoke);
                return policyCopy;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.PolicyOperations
    public void destroy() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("destroy", true));
                    _releaseReply(inputStream_invoke);
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                destroy();
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(ObjectInputStream objectInputStream) {
        try {
            _set_delegate(((ObjectImpl) ORB.init().string_to_object(objectInputStream.readUTF()))._get_delegate());
        } catch (IOException e2) {
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        try {
            objectOutputStream.writeUTF(ORB.init().object_to_string(this));
        } catch (IOException e2) {
        }
    }
}
