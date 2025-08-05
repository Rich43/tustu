package org.omg.CosNaming;

import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

/* loaded from: rt.jar:org/omg/CosNaming/BindingIteratorPOA.class */
public abstract class BindingIteratorPOA extends Servant implements BindingIteratorOperations, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("next_one", new Integer(0));
        _methods.put("next_n", new Integer(1));
        _methods.put("destroy", new Integer(2));
        __ids = new String[]{"IDL:omg.org/CosNaming/BindingIterator:1.0"};
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
                BindingHolder bindingHolder = new BindingHolder();
                boolean zNext_one = next_one(bindingHolder);
                outputStreamCreateReply = responseHandler.createReply();
                outputStreamCreateReply.write_boolean(zNext_one);
                BindingHelper.write(outputStreamCreateReply, bindingHolder.value);
                break;
            case 1:
                int i2 = inputStream.read_ulong();
                BindingListHolder bindingListHolder = new BindingListHolder();
                boolean zNext_n = next_n(i2, bindingListHolder);
                outputStreamCreateReply = responseHandler.createReply();
                outputStreamCreateReply.write_boolean(zNext_n);
                BindingListHelper.write(outputStreamCreateReply, bindingListHolder.value);
                break;
            case 2:
                destroy();
                outputStreamCreateReply = responseHandler.createReply();
                break;
            default:
                throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
        return outputStreamCreateReply;
    }

    @Override // org.omg.PortableServer.Servant
    public String[] _all_interfaces(POA poa, byte[] bArr) {
        return (String[]) __ids.clone();
    }

    public BindingIterator _this() {
        return BindingIteratorHelper.narrow(super._this_object());
    }

    public BindingIterator _this(ORB orb) {
        return BindingIteratorHelper.narrow(super._this_object(orb));
    }
}
