package org.omg.CosNaming;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
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
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddressHelper;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.omg.CosNaming.NamingContextExtPackage.URLStringHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.AlreadyBoundHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.CannotProceedHelper;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.InvalidNameHelper;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.omg.CosNaming.NamingContextPackage.NotEmptyHelper;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.CosNaming.NamingContextPackage.NotFoundHelper;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:org/omg/CosNaming/_NamingContextExtStub.class */
public class _NamingContextExtStub extends ObjectImpl implements NamingContextExt {
    private static String[] __ids = {"IDL:omg.org/CosNaming/NamingContextExt:1.0", "IDL:omg.org/CosNaming/NamingContext:1.0"};

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public String to_string(NameComponent[] nameComponentArr) throws InvalidName {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("to_string", true);
                NameHelper.write(outputStream_request, nameComponentArr);
                inputStream_invoke = _invoke(outputStream_request);
                String str = StringNameHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return str;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                String str2 = to_string(nameComponentArr);
                _releaseReply(inputStream_invoke);
                return str2;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public NameComponent[] to_name(String str) throws InvalidName {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("to_name", true);
                StringNameHelper.write(outputStream_request, str);
                inputStream_invoke = _invoke(outputStream_request);
                NameComponent[] nameComponentArr = NameHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return nameComponentArr;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                NameComponent[] nameComponentArr2 = to_name(str);
                _releaseReply(inputStream_invoke);
                return nameComponentArr2;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public String to_url(String str, String str2) throws InvalidAddress, InvalidName {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("to_url", true);
                AddressHelper.write(outputStream_request, str);
                StringNameHelper.write(outputStream_request, str2);
                inputStream_invoke = _invoke(outputStream_request);
                String str3 = URLStringHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return str3;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContextExt/InvalidAddress:1.0")) {
                    throw InvalidAddressHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                String str4 = to_url(str, str2);
                _releaseReply(inputStream_invoke);
                return str4;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public Object resolve_str(String str) throws NotFound, InvalidName, CannotProceed {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("resolve_str", true);
                StringNameHelper.write(outputStream_request, str);
                inputStream_invoke = _invoke(outputStream_request);
                Object object = ObjectHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return object;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
                    throw NotFoundHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
                    throw CannotProceedHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                Object objectResolve_str = resolve_str(str);
                _releaseReply(inputStream_invoke);
                return objectResolve_str;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void bind(NameComponent[] nameComponentArr, Object object) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("bind", true);
                NameHelper.write(outputStream_request, nameComponentArr);
                ObjectHelper.write(outputStream_request, object);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
                    throw NotFoundHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
                    throw CannotProceedHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
                    throw AlreadyBoundHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                bind(nameComponentArr, object);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void bind_context(NameComponent[] nameComponentArr, NamingContext namingContext) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("bind_context", true);
                NameHelper.write(outputStream_request, nameComponentArr);
                NamingContextHelper.write(outputStream_request, namingContext);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
                    throw NotFoundHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
                    throw CannotProceedHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
                    throw AlreadyBoundHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                bind_context(nameComponentArr, namingContext);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void rebind(NameComponent[] nameComponentArr, Object object) throws NotFound, InvalidName, CannotProceed {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("rebind", true);
                NameHelper.write(outputStream_request, nameComponentArr);
                ObjectHelper.write(outputStream_request, object);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
                    throw NotFoundHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
                    throw CannotProceedHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                rebind(nameComponentArr, object);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void rebind_context(NameComponent[] nameComponentArr, NamingContext namingContext) throws NotFound, InvalidName, CannotProceed {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("rebind_context", true);
                NameHelper.write(outputStream_request, nameComponentArr);
                NamingContextHelper.write(outputStream_request, namingContext);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
                    throw NotFoundHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
                    throw CannotProceedHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                rebind_context(nameComponentArr, namingContext);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public Object resolve(NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request(SecurityConstants.SOCKET_RESOLVE_ACTION, true);
                NameHelper.write(outputStream_request, nameComponentArr);
                inputStream_invoke = _invoke(outputStream_request);
                Object object = ObjectHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return object;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
                    throw NotFoundHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
                    throw CannotProceedHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                Object objectResolve = resolve(nameComponentArr);
                _releaseReply(inputStream_invoke);
                return objectResolve;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void unbind(NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("unbind", true);
                NameHelper.write(outputStream_request, nameComponentArr);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
                    throw NotFoundHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
                    throw CannotProceedHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                unbind(nameComponentArr);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void list(int i2, BindingListHolder bindingListHolder, BindingIteratorHolder bindingIteratorHolder) {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request(SchemaSymbols.ATTVAL_LIST, true);
                outputStream_request.write_ulong(i2);
                inputStream_invoke = _invoke(outputStream_request);
                bindingListHolder.value = BindingListHelper.read(inputStream_invoke);
                bindingIteratorHolder.value = BindingIteratorHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                list(i2, bindingListHolder, bindingIteratorHolder);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public NamingContext new_context() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("new_context", true));
                    NamingContext namingContext = NamingContextHelper.read(inputStream_invoke);
                    _releaseReply(inputStream_invoke);
                    return namingContext;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                NamingContext namingContextNew_context = new_context();
                _releaseReply(inputStream_invoke);
                return namingContextNew_context;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public NamingContext bind_new_context(NameComponent[] nameComponentArr) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("bind_new_context", true);
                NameHelper.write(outputStream_request, nameComponentArr);
                inputStream_invoke = _invoke(outputStream_request);
                NamingContext namingContext = NamingContextHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return namingContext;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
                    throw NotFoundHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
                    throw AlreadyBoundHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
                    throw CannotProceedHelper.read(inputStream);
                }
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
                    throw InvalidNameHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                NamingContext namingContextBind_new_context = bind_new_context(nameComponentArr);
                _releaseReply(inputStream_invoke);
                return namingContextBind_new_context;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void destroy() throws NotEmpty {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("destroy", true));
                    _releaseReply(inputStream_invoke);
                } catch (RemarshalException e2) {
                    destroy();
                    _releaseReply(inputStream_invoke);
                }
            } catch (ApplicationException e3) {
                InputStream inputStream = e3.getInputStream();
                String id = e3.getId();
                if (id.equals("IDL:omg.org/CosNaming/NamingContext/NotEmpty:1.0")) {
                    throw NotEmptyHelper.read(inputStream);
                }
                throw new MARSHAL(id);
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
