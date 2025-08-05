package com.sun.corba.se.spi.activation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_ServerStub.class */
public class _ServerStub extends ObjectImpl implements Server {
    private static String[] __ids = {"IDL:activation/Server:1.0"};

    @Override // com.sun.corba.se.spi.activation.ServerOperations
    public void shutdown() {
        InputStream inputStream_invoke = null;
        try {
            try {
                inputStream_invoke = _invoke(_request("shutdown", true));
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                shutdown();
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ServerOperations
    public void install() {
        InputStream inputStream_invoke = null;
        try {
            try {
                inputStream_invoke = _invoke(_request("install", true));
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                install();
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ServerOperations
    public void uninstall() {
        InputStream inputStream_invoke = null;
        try {
            try {
                inputStream_invoke = _invoke(_request("uninstall", true));
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                uninstall();
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
