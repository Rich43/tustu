package javax.management.remote.rmi;

import java.io.IOException;
import java.rmi.Remote;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CORBA.portable.UnknownException;
import org.omg.CORBA_2_3.portable.ObjectImpl;

/* loaded from: rt.jar:javax/management/remote/rmi/_RMIServerImpl_Tie.class */
public class _RMIServerImpl_Tie extends ObjectImpl implements Tie {
    private volatile RMIServerImpl target = null;
    private static final String[] _type_ids = {"RMI:javax.management.remote.rmi.RMIServer:0000000000000000"};
    static Class class$java$io$IOException;
    static Class class$java$lang$String;

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) _type_ids.clone();
    }

    @Override // org.omg.CORBA.portable.InvokeHandler
    public OutputStream _invoke(String str, InputStream inputStream, ResponseHandler responseHandler) throws SystemException {
        Class clsClass$;
        Class clsClass$2;
        try {
            RMIServerImpl rMIServerImpl = this.target;
            if (rMIServerImpl == null) {
                throw new IOException();
            }
            org.omg.CORBA_2_3.portable.InputStream inputStream2 = (org.omg.CORBA_2_3.portable.InputStream) inputStream;
            switch (str.length()) {
                case 9:
                    if (str.equals("newClient")) {
                        try {
                            RMIConnection rMIConnectionNewClient = rMIServerImpl.newClient(Util.readAny(inputStream2));
                            OutputStream outputStreamCreateReply = responseHandler.createReply();
                            Util.writeRemoteObject(outputStreamCreateReply, rMIConnectionNewClient);
                            return outputStreamCreateReply;
                        } catch (IOException e2) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$2 = class$java$io$IOException;
                            } else {
                                clsClass$2 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$2;
                            }
                            outputStream.write_value(e2, clsClass$2);
                            return outputStream;
                        }
                    }
                case 12:
                    if (str.equals("_get_version")) {
                        String version = rMIServerImpl.getVersion();
                        org.omg.CORBA_2_3.portable.OutputStream outputStream2 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                        if (class$java$lang$String != null) {
                            clsClass$ = class$java$lang$String;
                        } else {
                            clsClass$ = class$("java.lang.String");
                            class$java$lang$String = clsClass$;
                        }
                        outputStream2.write_value(version, clsClass$);
                        return outputStream2;
                    }
                case 10:
                case 11:
                default:
                    throw new BAD_OPERATION();
            }
        } catch (SystemException e3) {
            throw e3;
        } catch (Throwable th) {
            throw new UnknownException(th);
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError(e2.getMessage());
        }
    }

    @Override // javax.rmi.CORBA.Tie
    public void deactivate() {
        _orb().disconnect(this);
        _set_delegate(null);
        this.target = null;
    }

    @Override // javax.rmi.CORBA.Tie
    public Remote getTarget() {
        return this.target;
    }

    @Override // javax.rmi.CORBA.Tie
    public ORB orb() {
        return _orb();
    }

    @Override // javax.rmi.CORBA.Tie
    public void orb(ORB orb) {
        orb.connect(this);
    }

    @Override // javax.rmi.CORBA.Tie
    public void setTarget(Remote remote) {
        this.target = (RMIServerImpl) remote;
    }

    @Override // javax.rmi.CORBA.Tie
    public Object thisObject() {
        return this;
    }
}
