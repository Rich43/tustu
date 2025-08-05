package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationHelper;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORBHelper;
import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_LocatorImplBase.class */
public abstract class _LocatorImplBase extends ObjectImpl implements Locator, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("locateServer", new Integer(0));
        _methods.put("locateServerForORB", new Integer(1));
        _methods.put("getEndpoint", new Integer(2));
        _methods.put("getServerPortForType", new Integer(3));
        __ids = new String[]{"IDL:activation/Locator:1.0"};
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
                    ServerLocation serverLocationLocateServer = locateServer(ServerIdHelper.read(inputStream), inputStream.read_string());
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    ServerLocationHelper.write(outputStreamCreateExceptionReply, serverLocationLocateServer);
                    break;
                } catch (NoSuchEndPoint e2) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NoSuchEndPointHelper.write(outputStreamCreateExceptionReply, e2);
                    break;
                } catch (ServerHeldDown e3) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerHeldDownHelper.write(outputStreamCreateExceptionReply, e3);
                    break;
                } catch (ServerNotRegistered e4) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e4);
                    break;
                }
            case 1:
                try {
                    ServerLocationPerORB serverLocationPerORBLocateServerForORB = locateServerForORB(ServerIdHelper.read(inputStream), ORBidHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    ServerLocationPerORBHelper.write(outputStreamCreateExceptionReply, serverLocationPerORBLocateServerForORB);
                    break;
                } catch (InvalidORBid e5) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidORBidHelper.write(outputStreamCreateExceptionReply, e5);
                    break;
                } catch (ServerHeldDown e6) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerHeldDownHelper.write(outputStreamCreateExceptionReply, e6);
                    break;
                } catch (ServerNotRegistered e7) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e7);
                    break;
                }
            case 2:
                try {
                    int endpoint = getEndpoint(inputStream.read_string());
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    outputStreamCreateExceptionReply.write_long(endpoint);
                    break;
                } catch (NoSuchEndPoint e8) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NoSuchEndPointHelper.write(outputStreamCreateExceptionReply, e8);
                    break;
                }
            case 3:
                try {
                    int serverPortForType = getServerPortForType(ServerLocationPerORBHelper.read(inputStream), inputStream.read_string());
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    outputStreamCreateExceptionReply.write_long(serverPortForType);
                    break;
                } catch (NoSuchEndPoint e9) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NoSuchEndPointHelper.write(outputStreamCreateExceptionReply, e9);
                    break;
                }
            default:
                throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
        return outputStreamCreateExceptionReply;
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) __ids.clone();
    }
}
