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

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_ServerManagerImplBase.class */
public abstract class _ServerManagerImplBase extends ObjectImpl implements ServerManager, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("active", new Integer(0));
        _methods.put("registerEndpoints", new Integer(1));
        _methods.put("getActiveServers", new Integer(2));
        _methods.put("activate", new Integer(3));
        _methods.put("shutdown", new Integer(4));
        _methods.put("install", new Integer(5));
        _methods.put("getORBNames", new Integer(6));
        _methods.put("uninstall", new Integer(7));
        _methods.put("locateServer", new Integer(8));
        _methods.put("locateServerForORB", new Integer(9));
        _methods.put("getEndpoint", new Integer(10));
        _methods.put("getServerPortForType", new Integer(11));
        __ids = new String[]{"IDL:activation/ServerManager:1.0", "IDL:activation/Activator:1.0", "IDL:activation/Locator:1.0"};
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
                    active(ServerIdHelper.read(inputStream), ServerHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (ServerNotRegistered e2) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e2);
                    break;
                }
            case 1:
                try {
                    registerEndpoints(ServerIdHelper.read(inputStream), ORBidHelper.read(inputStream), EndpointInfoListHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (NoSuchEndPoint e3) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NoSuchEndPointHelper.write(outputStreamCreateExceptionReply, e3);
                    break;
                } catch (ORBAlreadyRegistered e4) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ORBAlreadyRegisteredHelper.write(outputStreamCreateExceptionReply, e4);
                    break;
                } catch (ServerNotRegistered e5) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e5);
                    break;
                }
            case 2:
                int[] activeServers = getActiveServers();
                outputStreamCreateExceptionReply = responseHandler.createReply();
                ServerIdsHelper.write(outputStreamCreateExceptionReply, activeServers);
                break;
            case 3:
                try {
                    activate(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (ServerAlreadyActive e6) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerAlreadyActiveHelper.write(outputStreamCreateExceptionReply, e6);
                    break;
                } catch (ServerHeldDown e7) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerHeldDownHelper.write(outputStreamCreateExceptionReply, e7);
                    break;
                } catch (ServerNotRegistered e8) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e8);
                    break;
                }
            case 4:
                try {
                    shutdown(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (ServerNotActive e9) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotActiveHelper.write(outputStreamCreateExceptionReply, e9);
                    break;
                } catch (ServerNotRegistered e10) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e10);
                    break;
                }
            case 5:
                try {
                    install(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (ServerAlreadyInstalled e11) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerAlreadyInstalledHelper.write(outputStreamCreateExceptionReply, e11);
                    break;
                } catch (ServerHeldDown e12) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerHeldDownHelper.write(outputStreamCreateExceptionReply, e12);
                    break;
                } catch (ServerNotRegistered e13) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e13);
                    break;
                }
            case 6:
                try {
                    String[] oRBNames = getORBNames(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    ORBidListHelper.write(outputStreamCreateExceptionReply, oRBNames);
                    break;
                } catch (ServerNotRegistered e14) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e14);
                    break;
                }
            case 7:
                try {
                    uninstall(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (ServerAlreadyUninstalled e15) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerAlreadyUninstalledHelper.write(outputStreamCreateExceptionReply, e15);
                    break;
                } catch (ServerHeldDown e16) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerHeldDownHelper.write(outputStreamCreateExceptionReply, e16);
                    break;
                } catch (ServerNotRegistered e17) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e17);
                    break;
                }
            case 8:
                try {
                    ServerLocation serverLocationLocateServer = locateServer(ServerIdHelper.read(inputStream), inputStream.read_string());
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    ServerLocationHelper.write(outputStreamCreateExceptionReply, serverLocationLocateServer);
                    break;
                } catch (NoSuchEndPoint e18) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NoSuchEndPointHelper.write(outputStreamCreateExceptionReply, e18);
                    break;
                } catch (ServerHeldDown e19) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerHeldDownHelper.write(outputStreamCreateExceptionReply, e19);
                    break;
                } catch (ServerNotRegistered e20) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e20);
                    break;
                }
            case 9:
                try {
                    ServerLocationPerORB serverLocationPerORBLocateServerForORB = locateServerForORB(ServerIdHelper.read(inputStream), ORBidHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    ServerLocationPerORBHelper.write(outputStreamCreateExceptionReply, serverLocationPerORBLocateServerForORB);
                    break;
                } catch (InvalidORBid e21) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    InvalidORBidHelper.write(outputStreamCreateExceptionReply, e21);
                    break;
                } catch (ServerHeldDown e22) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerHeldDownHelper.write(outputStreamCreateExceptionReply, e22);
                    break;
                } catch (ServerNotRegistered e23) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e23);
                    break;
                }
            case 10:
                try {
                    int endpoint = getEndpoint(inputStream.read_string());
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    outputStreamCreateExceptionReply.write_long(endpoint);
                    break;
                } catch (NoSuchEndPoint e24) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NoSuchEndPointHelper.write(outputStreamCreateExceptionReply, e24);
                    break;
                }
            case 11:
                try {
                    int serverPortForType = getServerPortForType(ServerLocationPerORBHelper.read(inputStream), inputStream.read_string());
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    outputStreamCreateExceptionReply.write_long(serverPortForType);
                    break;
                } catch (NoSuchEndPoint e25) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    NoSuchEndPointHelper.write(outputStreamCreateExceptionReply, e25);
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
