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
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddressHelper;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
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

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextExtPOA.class */
public abstract class NamingContextExtPOA extends Servant implements NamingContextExtOperations, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("to_string", new Integer(0));
        _methods.put("to_name", new Integer(1));
        _methods.put("to_url", new Integer(2));
        _methods.put("resolve_str", new Integer(3));
        _methods.put("bind", new Integer(4));
        _methods.put("bind_context", new Integer(5));
        _methods.put("rebind", new Integer(6));
        _methods.put("rebind_context", new Integer(7));
        _methods.put(SecurityConstants.SOCKET_RESOLVE_ACTION, new Integer(8));
        _methods.put("unbind", new Integer(9));
        _methods.put(SchemaSymbols.ATTVAL_LIST, new Integer(10));
        _methods.put("new_context", new Integer(11));
        _methods.put("bind_new_context", new Integer(12));
        _methods.put("destroy", new Integer(13));
        __ids = new String[]{"IDL:omg.org/CosNaming/NamingContextExt:1.0", "IDL:omg.org/CosNaming/NamingContext:1.0"};
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
                    String str2 = to_string(NameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    outputStreamCreateExceptionReply.write_string(str2);
                    break;
                } catch (InvalidName e2) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e2);
                    break;
                }
            case 1:
                try {
                    NameComponent[] nameComponentArr = to_name(StringNameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    NameHelper.write(outputStreamCreateExceptionReply, nameComponentArr);
                    break;
                } catch (InvalidName e3) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e3);
                    break;
                }
            case 2:
                try {
                    String str3 = to_url(AddressHelper.read(inputStream), StringNameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    outputStreamCreateExceptionReply.write_string(str3);
                    break;
                } catch (InvalidAddress e4) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidAddressHelper.write(outputStreamCreateExceptionReply, e4);
                    break;
                } catch (InvalidName e5) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e5);
                    break;
                }
            case 3:
                try {
                    Object objectResolve_str = resolve_str(StringNameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    ObjectHelper.write(outputStreamCreateExceptionReply, objectResolve_str);
                    break;
                } catch (CannotProceed e6) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e6);
                    break;
                } catch (InvalidName e7) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e7);
                    break;
                } catch (NotFound e8) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e8);
                    break;
                }
            case 4:
                try {
                    bind(NameHelper.read(inputStream), ObjectHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (AlreadyBound e9) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    AlreadyBoundHelper.write(outputStreamCreateExceptionReply, e9);
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
            case 5:
                try {
                    bind_context(NameHelper.read(inputStream), NamingContextHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (AlreadyBound e13) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    AlreadyBoundHelper.write(outputStreamCreateExceptionReply, e13);
                    break;
                } catch (CannotProceed e14) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e14);
                    break;
                } catch (InvalidName e15) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e15);
                    break;
                } catch (NotFound e16) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e16);
                    break;
                }
            case 6:
                try {
                    rebind(NameHelper.read(inputStream), ObjectHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (CannotProceed e17) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e17);
                    break;
                } catch (InvalidName e18) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e18);
                    break;
                } catch (NotFound e19) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e19);
                    break;
                }
            case 7:
                try {
                    rebind_context(NameHelper.read(inputStream), NamingContextHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (CannotProceed e20) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e20);
                    break;
                } catch (InvalidName e21) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e21);
                    break;
                } catch (NotFound e22) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e22);
                    break;
                }
            case 8:
                try {
                    Object objectResolve = resolve(NameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    ObjectHelper.write(outputStreamCreateExceptionReply, objectResolve);
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
                    unbind(NameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (CannotProceed e26) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e26);
                    break;
                } catch (InvalidName e27) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e27);
                    break;
                } catch (NotFound e28) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e28);
                    break;
                }
            case 10:
                int i2 = inputStream.read_ulong();
                BindingListHolder bindingListHolder = new BindingListHolder();
                BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
                list(i2, bindingListHolder, bindingIteratorHolder);
                outputStreamCreateExceptionReply = responseHandler.createReply();
                BindingListHelper.write(outputStreamCreateExceptionReply, bindingListHolder.value);
                BindingIteratorHelper.write(outputStreamCreateExceptionReply, bindingIteratorHolder.value);
                break;
            case 11:
                NamingContext namingContextNew_context = new_context();
                outputStreamCreateExceptionReply = responseHandler.createReply();
                NamingContextHelper.write(outputStreamCreateExceptionReply, namingContextNew_context);
                break;
            case 12:
                try {
                    NamingContext namingContextBind_new_context = bind_new_context(NameHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    NamingContextHelper.write(outputStreamCreateExceptionReply, namingContextBind_new_context);
                    break;
                } catch (AlreadyBound e29) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    AlreadyBoundHelper.write(outputStreamCreateExceptionReply, e29);
                    break;
                } catch (CannotProceed e30) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    CannotProceedHelper.write(outputStreamCreateExceptionReply, e30);
                    break;
                } catch (InvalidName e31) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidNameHelper.write(outputStreamCreateExceptionReply, e31);
                    break;
                } catch (NotFound e32) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotFoundHelper.write(outputStreamCreateExceptionReply, e32);
                    break;
                }
            case 13:
                try {
                    destroy();
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (NotEmpty e33) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NotEmptyHelper.write(outputStreamCreateExceptionReply, e33);
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

    public NamingContextExt _this() {
        return NamingContextExtHelper.narrow(super._this_object());
    }

    public NamingContextExt _this(ORB orb) {
        return NamingContextExtHelper.narrow(super._this_object(orb));
    }
}
