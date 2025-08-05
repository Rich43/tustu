package com.sun.org.omg.SendingContext;

import com.sun.org.omg.CORBA.Repository;
import com.sun.org.omg.CORBA.RepositoryHelper;
import com.sun.org.omg.CORBA.RepositoryIdHelper;
import com.sun.org.omg.CORBA.RepositoryIdSeqHelper;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescriptionHelper;
import com.sun.org.omg.SendingContext.CodeBasePackage.URLHelper;
import com.sun.org.omg.SendingContext.CodeBasePackage.URLSeqHelper;
import com.sun.org.omg.SendingContext.CodeBasePackage.ValueDescSeqHelper;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:com/sun/org/omg/SendingContext/_CodeBaseStub.class */
public class _CodeBaseStub extends ObjectImpl implements CodeBase {
    private static String[] __ids = {"IDL:omg.org/SendingContext/CodeBase:1.0", "IDL:omg.org/SendingContext/RunTime:1.0"};

    public _CodeBaseStub() {
    }

    public _CodeBaseStub(Delegate delegate) {
        _set_delegate(delegate);
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public Repository get_ir() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("get_ir", true));
                    Repository repository = RepositoryHelper.read(inputStream_invoke);
                    _releaseReply(inputStream_invoke);
                    return repository;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                Repository repository2 = get_ir();
                _releaseReply(inputStream_invoke);
                return repository2;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public String implementation(String str) {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request(DeploymentDescriptorParser.ATTR_IMPLEMENTATION, true);
                RepositoryIdHelper.write(outputStream_request, str);
                inputStream_invoke = _invoke(outputStream_request);
                String str2 = URLHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return str2;
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                String strImplementation = implementation(str);
                _releaseReply(inputStream_invoke);
                return strImplementation;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public String[] implementations(String[] strArr) {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("implementations", true);
                RepositoryIdSeqHelper.write(outputStream_request, strArr);
                inputStream_invoke = _invoke(outputStream_request);
                String[] strArr2 = URLSeqHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return strArr2;
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                String[] strArrImplementations = implementations(strArr);
                _releaseReply(inputStream_invoke);
                return strArrImplementations;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public FullValueDescription meta(String str) {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("meta", true);
                RepositoryIdHelper.write(outputStream_request, str);
                inputStream_invoke = _invoke(outputStream_request);
                FullValueDescription fullValueDescription = FullValueDescriptionHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return fullValueDescription;
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                FullValueDescription fullValueDescriptionMeta = meta(str);
                _releaseReply(inputStream_invoke);
                return fullValueDescriptionMeta;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public FullValueDescription[] metas(String[] strArr) {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("metas", true);
                RepositoryIdSeqHelper.write(outputStream_request, strArr);
                inputStream_invoke = _invoke(outputStream_request);
                FullValueDescription[] fullValueDescriptionArr = ValueDescSeqHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return fullValueDescriptionArr;
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                FullValueDescription[] fullValueDescriptionArrMetas = metas(strArr);
                _releaseReply(inputStream_invoke);
                return fullValueDescriptionArrMetas;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public String[] bases(String str) {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("bases", true);
                RepositoryIdHelper.write(outputStream_request, str);
                inputStream_invoke = _invoke(outputStream_request);
                String[] strArr = RepositoryIdSeqHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return strArr;
            } catch (ApplicationException e2) {
                e2.getInputStream();
                throw new MARSHAL(e2.getId());
            } catch (RemarshalException e3) {
                String[] strArrBases = bases(str);
                _releaseReply(inputStream_invoke);
                return strArrBases;
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
