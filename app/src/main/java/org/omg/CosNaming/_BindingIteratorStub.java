package org.omg.CosNaming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:org/omg/CosNaming/_BindingIteratorStub.class */
public class _BindingIteratorStub extends ObjectImpl implements BindingIterator {
    private static String[] __ids = {"IDL:omg.org/CosNaming/BindingIterator:1.0"};

    @Override // org.omg.CosNaming.BindingIteratorOperations
    public boolean next_one(BindingHolder bindingHolder) {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("next_one", true));
                    boolean z2 = inputStream_invoke.read_boolean();
                    bindingHolder.value = BindingHelper.read(inputStream_invoke);
                    _releaseReply(inputStream_invoke);
                    return z2;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                boolean zNext_one = next_one(bindingHolder);
                _releaseReply(inputStream_invoke);
                return zNext_one;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.BindingIteratorOperations
    public boolean next_n(int i2, BindingListHolder bindingListHolder) {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("next_n", true);
                outputStream_request.write_ulong(i2);
                inputStream_invoke = _invoke(outputStream_request);
                boolean z2 = inputStream_invoke.read_boolean();
                bindingListHolder.value = BindingListHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return z2;
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                boolean zNext_n = next_n(i2, bindingListHolder);
                _releaseReply(inputStream_invoke);
                return zNext_n;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.BindingIteratorOperations
    public void destroy() {
        InputStream inputStream_invoke = null;
        try {
            try {
                inputStream_invoke = _invoke(_request("destroy", true));
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
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

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        String utf = objectInputStream.readUTF();
        ORB orbInit = ORB.init((String[]) null, (Properties) null);
        try {
            _set_delegate(((ObjectImpl) orbInit.string_to_object(utf))._get_delegate());
            orbInit.destroy();
        } catch (Throwable th) {
            orbInit.destroy();
            throw th;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ORB orbInit = ORB.init((String[]) null, (Properties) null);
        try {
            objectOutputStream.writeUTF(orbInit.object_to_string(this));
            orbInit.destroy();
        } catch (Throwable th) {
            orbInit.destroy();
            throw th;
        }
    }
}
