package com.sun.corba.se.spi.activation;

import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_ServerImplBase.class */
public abstract class _ServerImplBase extends ObjectImpl implements Server, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("shutdown", new Integer(0));
        _methods.put("install", new Integer(1));
        _methods.put("uninstall", new Integer(2));
        __ids = new String[]{"IDL:activation/Server:1.0"};
    }

    @Override // org.omg.CORBA.portable.InvokeHandler
    public OutputStream _invoke(String str, InputStream inputStream, ResponseHandler responseHandler) {
        OutputStream outputStreamCreateReply;
        Integer num = (Integer) _methods.get(str);
        if (num == null) {
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
        switch (num.intValue()) {
            case 0:
                shutdown();
                outputStreamCreateReply = responseHandler.createReply();
                break;
            case 1:
                install();
                outputStreamCreateReply = responseHandler.createReply();
                break;
            case 2:
                uninstall();
                outputStreamCreateReply = responseHandler.createReply();
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
