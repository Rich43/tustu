package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBound;
import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBoundHelper;
import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ObjectHelper;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_InitialNameServiceImplBase.class */
public abstract class _InitialNameServiceImplBase extends ObjectImpl implements InitialNameService, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("bind", new Integer(0));
        __ids = new String[]{"IDL:activation/InitialNameService:1.0"};
    }

    @Override // org.omg.CORBA.portable.InvokeHandler
    public OutputStream _invoke(String str, InputStream inputStream, ResponseHandler responseHandler) {
        OutputStream outputStreamCreateExceptionReply;
        Integer num = (Integer) _methods.get(str);
        if (num == null) {
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
        switch (num.intValue()) {
            case 0:
                try {
                    bind(inputStream.read_string(), ObjectHelper.read(inputStream), inputStream.read_boolean());
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                } catch (NameAlreadyBound e2) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NameAlreadyBoundHelper.write(outputStreamCreateExceptionReply, e2);
                }
                return outputStreamCreateExceptionReply;
            default:
                throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) __ids.clone();
    }
}
