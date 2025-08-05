package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDefHelper;
import com.sun.corba.se.spi.activation.RepositoryPackage.StringSeqHelper;
import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_RepositoryImplBase.class */
public abstract class _RepositoryImplBase extends ObjectImpl implements Repository, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("registerServer", new Integer(0));
        _methods.put("unregisterServer", new Integer(1));
        _methods.put("getServer", new Integer(2));
        _methods.put("isInstalled", new Integer(3));
        _methods.put("install", new Integer(4));
        _methods.put("uninstall", new Integer(5));
        _methods.put("listRegisteredServers", new Integer(6));
        _methods.put("getApplicationNames", new Integer(7));
        _methods.put("getServerID", new Integer(8));
        __ids = new String[]{"IDL:activation/Repository:1.0"};
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
                    int iRegisterServer = registerServer(ServerDefHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    outputStreamCreateExceptionReply.write_long(iRegisterServer);
                    break;
                } catch (BadServerDefinition e2) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    BadServerDefinitionHelper.write(outputStreamCreateExceptionReply, e2);
                    break;
                } catch (ServerAlreadyRegistered e3) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerAlreadyRegisteredHelper.write(outputStreamCreateExceptionReply, e3);
                    break;
                }
            case 1:
                try {
                    unregisterServer(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (ServerNotRegistered e4) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e4);
                    break;
                }
            case 2:
                try {
                    ServerDef server = getServer(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    ServerDefHelper.write(outputStreamCreateExceptionReply, server);
                    break;
                } catch (ServerNotRegistered e5) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e5);
                    break;
                }
            case 3:
                try {
                    boolean zIsInstalled = isInstalled(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    outputStreamCreateExceptionReply.write_boolean(zIsInstalled);
                    break;
                } catch (ServerNotRegistered e6) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e6);
                    break;
                }
            case 4:
                try {
                    install(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (ServerAlreadyInstalled e7) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerAlreadyInstalledHelper.write(outputStreamCreateExceptionReply, e7);
                    break;
                } catch (ServerNotRegistered e8) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e8);
                    break;
                }
            case 5:
                try {
                    uninstall(ServerIdHelper.read(inputStream));
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    break;
                } catch (ServerAlreadyUninstalled e9) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerAlreadyUninstalledHelper.write(outputStreamCreateExceptionReply, e9);
                    break;
                } catch (ServerNotRegistered e10) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e10);
                    break;
                }
            case 6:
                int[] iArrListRegisteredServers = listRegisteredServers();
                outputStreamCreateExceptionReply = responseHandler.createReply();
                ServerIdsHelper.write(outputStreamCreateExceptionReply, iArrListRegisteredServers);
                break;
            case 7:
                String[] applicationNames = getApplicationNames();
                outputStreamCreateExceptionReply = responseHandler.createReply();
                StringSeqHelper.write(outputStreamCreateExceptionReply, applicationNames);
                break;
            case 8:
                try {
                    int serverID = getServerID(inputStream.read_string());
                    outputStreamCreateExceptionReply = responseHandler.createReply();
                    outputStreamCreateExceptionReply.write_long(serverID);
                    break;
                } catch (ServerNotRegistered e11) {
                    outputStreamCreateExceptionReply = responseHandler.createExceptionReply();
                    ServerNotRegisteredHelper.write(outputStreamCreateExceptionReply, e11);
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
