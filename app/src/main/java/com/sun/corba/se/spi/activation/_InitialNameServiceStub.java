package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBound;
import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBoundHelper;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ObjectHelper;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_InitialNameServiceStub.class */
public class _InitialNameServiceStub extends ObjectImpl implements InitialNameService {
    private static String[] __ids = {"IDL:activation/InitialNameService:1.0"};

    @Override // com.sun.corba.se.spi.activation.InitialNameServiceOperations
    public void bind(String str, Object object, boolean z2) throws NameAlreadyBound {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    OutputStream outputStream_request = _request("bind", true);
                    outputStream_request.write_string(str);
                    ObjectHelper.write(outputStream_request, object);
                    outputStream_request.write_boolean(z2);
                    inputStream_invoke = _invoke(outputStream_request);
                    _releaseReply(inputStream_invoke);
                } catch (ApplicationException e2) {
                    InputStream inputStream = e2.getInputStream();
                    String id = e2.getId();
                    if (id.equals("IDL:activation/InitialNameService/NameAlreadyBound:1.0")) {
                        throw NameAlreadyBoundHelper.read(inputStream);
                    }
                    throw new MARSHAL(id);
                }
            } catch (RemarshalException e3) {
                bind(str, object, z2);
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
