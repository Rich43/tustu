package org.omg.CosNaming;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ObjectHelper;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
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
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPOA.class */
public abstract class NamingContextPOA extends Servant implements NamingContextOperations, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("bind", new Integer(0));
        _methods.put("bind_context", new Integer(1));
        _methods.put("rebind", new Integer(2));
        _methods.put("rebind_context", new Integer(3));
        _methods.put(SecurityConstants.SOCKET_RESOLVE_ACTION, new Integer(4));
        _methods.put("unbind", new Integer(5));
        _methods.put(SchemaSymbols.ATTVAL_LIST, new Integer(6));
        _methods.put("new_context", new Integer(7));
        _methods.put("bind_new_context", new Integer(8));
        _methods.put("destroy", new Integer(9));
        __ids = new String[]{"IDL:omg.org/CosNaming/NamingContext:1.0"};
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
                    bind(NameHelper.read(inputStream), ObjectHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (AlreadyBound e2) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    AlreadyBoundHelper.write(outputStreamCreateExceptionReply, e2);
                    break;
                } catch (CannotProceed e3) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e3);
                    break;
                } catch (InvalidName e4) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e4);
                    break;
                } catch (NotFound e5) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e5);
                    break;
                }
            case 1:
                try {
                    bind_context(NameHelper.read(inputStream), NamingContextHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (AlreadyBound e6) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    AlreadyBoundHelper.write(outputStreamCreateExceptionReply, e6);
                    break;
                } catch (CannotProceed e7) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e7);
                    break;
                } catch (InvalidName e8) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e8);
                    break;
                } catch (NotFound e9) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e9);
                    break;
                }
            case 2:
                try {
                    rebind(NameHelper.read(inputStream), ObjectHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (CannotProceed e10) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e10);
                    break;
                } catch (InvalidName e11) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e11);
                    break;
                } catch (NotFound e12) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e12);
                    break;
                }
            case 3:
                try {
                    rebind_context(NameHelper.read(inputStream), NamingContextHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (CannotProceed e13) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e13);
                    break;
                } catch (InvalidName e14) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e14);
                    break;
                } catch (NotFound e15) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e15);
                    break;
                }
            case 4:
                try {
                    Object objectResolve = resolve(NameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    ObjectHelper.write(outputStreamCreateExceptionReply, objectResolve);
                    break;
                } catch (CannotProceed e16) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e16);
                    break;
                } catch (InvalidName e17) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e17);
                    break;
                } catch (NotFound e18) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e18);
                    break;
                }
            case 5:
                try {
                    unbind(NameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (CannotProceed e19) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e19);
                    break;
                } catch (InvalidName e20) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e20);
                    break;
                } catch (NotFound e21) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e21);
                    break;
                }
            case 6:
                int i2 = inputStream.read_ulong();
                BindingListHolder bindingListHolder = new BindingListHolder();
                BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
                list(i2, bindingListHolder, bindingIteratorHolder);
                outputStreamCreateExceptionReply = responseHandler.createReply();
                BindingListHelper.write(outputStreamCreateExceptionReply, bindingListHolder.value);
                BindingIteratorHelper.write(outputStreamCreateExceptionReply, bindingIteratorHolder.value);
                break;
            case 7:
                NamingContext namingContextNew_context = new_context();
                outputStreamCreateExceptionReply = responseHandler.createReply();
                NamingContextHelper.write(outputStreamCreateExceptionReply, namingContextNew_context);
                break;
            case 8:
                try {
                    NamingContext namingContextBind_new_context = bind_new_context(NameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    NamingContextHelper.write(outputStreamCreateExceptionReply, namingContextBind_new_context);
                    break;
                } catch (AlreadyBound e22) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    AlreadyBoundHelper.write(outputStreamCreateExceptionReply, e22);
                    break;
                } catch (CannotProceed e23) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e23);
                    break;
                } catch (InvalidName e24) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e24);
                    break;
                } catch (NotFound e25) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e25);
                    break;
                }
            case 9:
                try {
                    destroy();
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (NotEmpty e26) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotEmptyHelper.write(outputStreamCreateExceptionReply, e26);
                    break;
                }
            default:
                throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
        return outputStreamCreateExceptionReply;
    }

    @Override // org.omg.PortableServer.Servant
    public String[] _all_interfaces(POA poa, byte[] bArr) {
        return (String[]) __ids.clone();
    }

    public NamingContext _this() {
        return NamingContextHelper.narrow(super._this_object());
    }

    public NamingContext _this(ORB orb) {
        return NamingContextHelper.narrow(super._this_object(orb));
    }
}
