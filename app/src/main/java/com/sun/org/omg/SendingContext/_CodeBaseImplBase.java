package com.sun.org.omg.SendingContext;

import com.sun.org.omg.CORBA.RepositoryHelper;
import com.sun.org.omg.CORBA.RepositoryIdHelper;
import com.sun.org.omg.CORBA.RepositoryIdSeqHelper;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescriptionHelper;
import com.sun.org.omg.SendingContext.CodeBasePackage.URLSeqHelper;
import com.sun.org.omg.SendingContext.CodeBasePackage.ValueDescSeqHelper;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

/* loaded from: rt.jar:com/sun/org/omg/SendingContext/_CodeBaseImplBase.class */
public abstract class _CodeBaseImplBase extends ObjectImpl implements CodeBase, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("get_ir", new Integer(0));
        _methods.put(DeploymentDescriptorParser.ATTR_IMPLEMENTATION, new Integer(1));
        _methods.put("implementations", new Integer(2));
        _methods.put("meta", new Integer(3));
        _methods.put("metas", new Integer(4));
        _methods.put("bases", new Integer(5));
        __ids = new String[]{"IDL:omg.org/SendingContext/CodeBase:1.0", "IDL:omg.org/SendingContext/RunTime:1.0"};
    }

    @Override // org.omg.CORBA.portable.InvokeHandler
    public OutputStream _invoke(String str, InputStream inputStream, ResponseHandler responseHandler) {
        OutputStream outputStreamCreateReply = responseHandler.createReply();
        Integer num = (Integer) _methods.get(str);
        if (num == null) {
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
        switch (num.intValue()) {
            case 0:
                RepositoryHelper.write(outputStreamCreateReply, get_ir());
                break;
            case 1:
                outputStreamCreateReply.write_string(implementation(RepositoryIdHelper.read(inputStream)));
                break;
            case 2:
                URLSeqHelper.write(outputStreamCreateReply, implementations(RepositoryIdSeqHelper.read(inputStream)));
                break;
            case 3:
                FullValueDescriptionHelper.write(outputStreamCreateReply, meta(RepositoryIdHelper.read(inputStream)));
                break;
            case 4:
                ValueDescSeqHelper.write(outputStreamCreateReply, metas(RepositoryIdSeqHelper.read(inputStream)));
                break;
            case 5:
                RepositoryIdSeqHelper.write(outputStreamCreateReply, bases(RepositoryIdHelper.read(inputStream)));
                break;
            default:
                throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
        return outputStreamCreateReply;
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) __ids.clone();
    }
}
