package com.sun.org.omg.CORBA;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.omg.CORBA.DefinitionKind;
import org.omg.CORBA.IDLType;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/_IDLTypeStub.class */
public class _IDLTypeStub extends ObjectImpl implements IDLType {
    private static String[] __ids = {"IDL:omg.org/CORBA/IDLType:1.0", "IDL:omg.org/CORBA/IRObject:1.0"};

    public _IDLTypeStub() {
    }

    public _IDLTypeStub(Delegate delegate) {
        _set_delegate(delegate);
    }

    @Override // org.omg.CORBA.IDLTypeOperations
    public TypeCode type() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("_get_type", true));
                    TypeCode typeCode = inputStream_invoke.read_TypeCode();
                    _releaseReply(inputStream_invoke);
                    return typeCode;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                TypeCode typeCodeType = type();
                _releaseReply(inputStream_invoke);
                return typeCodeType;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.IRObjectOperations
    public DefinitionKind def_kind() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("_get_def_kind", true));
                    DefinitionKind definitionKind = DefinitionKindHelper.read(inputStream_invoke);
                    _releaseReply(inputStream_invoke);
                    return definitionKind;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                DefinitionKind definitionKindDef_kind = def_kind();
                _releaseReply(inputStream_invoke);
                return definitionKindDef_kind;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.IRObjectOperations
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
