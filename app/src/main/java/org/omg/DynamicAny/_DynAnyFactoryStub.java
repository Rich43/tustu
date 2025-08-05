package org.omg.DynamicAny;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.ServantObject;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;

/* loaded from: rt.jar:org/omg/DynamicAny/_DynAnyFactoryStub.class */
public class _DynAnyFactoryStub extends ObjectImpl implements DynAnyFactory {
    public static final Class _opsClass = DynAnyFactoryOperations.class;
    private static String[] __ids = {"IDL:omg.org/DynamicAny/DynAnyFactory:1.0"};

    @Override // org.omg.DynamicAny.DynAnyFactoryOperations
    public DynAny create_dyn_any(Any any) throws InconsistentTypeCode {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("create_dyn_any", _opsClass);
        try {
            DynAny dynAnyCreate_dyn_any = ((DynAnyFactoryOperations) servantObject_servant_preinvoke.servant).create_dyn_any(any);
            _servant_postinvoke(servantObject_servant_preinvoke);
            return dynAnyCreate_dyn_any;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyFactoryOperations
    public DynAny create_dyn_any_from_type_code(TypeCode typeCode) throws InconsistentTypeCode {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("create_dyn_any_from_type_code", _opsClass);
        try {
            DynAny dynAnyCreate_dyn_any_from_type_code = ((DynAnyFactoryOperations) servantObject_servant_preinvoke.servant).create_dyn_any_from_type_code(typeCode);
            _servant_postinvoke(servantObject_servant_preinvoke);
            return dynAnyCreate_dyn_any_from_type_code;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        String utf = objectInputStream.readUTF();
        if (!utf.startsWith(ORBConstants.STRINGIFY_PREFIX) && !Boolean.getBoolean(ORBConstants.ALLOW_DESERIALIZE_OBJECT)) {
            throw new InvalidObjectException("IOR: expected");
        }
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
