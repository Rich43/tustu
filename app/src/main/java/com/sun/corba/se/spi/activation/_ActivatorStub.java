package com.sun.corba.se.spi.activation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_ActivatorStub.class */
public class _ActivatorStub extends ObjectImpl implements Activator {
    private static String[] __ids = {"IDL:activation/Activator:1.0"};

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void active(int i2, Server server) throws ServerNotRegistered {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("active", true);
                ServerIdHelper.write(outputStream_request, i2);
                ServerHelper.write(outputStream_request, server);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                active(i2, server);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void registerEndpoints(int i2, String str, EndPointInfo[] endPointInfoArr) throws NoSuchEndPoint, ORBAlreadyRegistered, ServerNotRegistered {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    OutputStream outputStream_request = _request("registerEndpoints", true);
                    ServerIdHelper.write(outputStream_request, i2);
                    ORBidHelper.write(outputStream_request, str);
                    EndpointInfoListHelper.write(outputStream_request, endPointInfoArr);
                    inputStream_invoke = _invoke(outputStream_request);
                    _releaseReply(inputStream_invoke);
                } catch (ApplicationException e2) {
                    InputStream inputStream = e2.getInputStream();
                    String id = e2.getId();
                    if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                        throw ServerNotRegisteredHelper.read(inputStream);
                    }
                    if (id.equals("IDL:activation/NoSuchEndPoint:1.0")) {
                        throw NoSuchEndPointHelper.read(inputStream);
                    }
                    if (id.equals("IDL:activation/ORBAlreadyRegistered:1.0")) {
                        throw ORBAlreadyRegisteredHelper.read(inputStream);
                    }
                    throw new MARSHAL(id);
                }
            } catch (RemarshalException e3) {
                registerEndpoints(i2, str, endPointInfoArr);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public int[] getActiveServers() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("getActiveServers", true));
                    int[] iArr = ServerIdsHelper.read(inputStream_invoke);
                    _releaseReply(inputStream_invoke);
                    return iArr;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                int[] activeServers = getActiveServers();
                _releaseReply(inputStream_invoke);
                return activeServers;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void activate(int i2) throws ServerNotRegistered, ServerHeldDown, ServerAlreadyActive {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("activate", true);
                ServerIdHelper.write(outputStream_request, i2);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerAlreadyActive:1.0")) {
                    throw ServerAlreadyActiveHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerHeldDown:1.0")) {
                    throw ServerHeldDownHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                activate(i2);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void shutdown(int i2) throws ServerNotRegistered, ServerNotActive {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("shutdown", true);
                ServerIdHelper.write(outputStream_request, i2);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerNotActive:1.0")) {
                    throw ServerNotActiveHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                shutdown(i2);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void install(int i2) throws ServerNotRegistered, ServerAlreadyInstalled, ServerHeldDown {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("install", true);
                ServerIdHelper.write(outputStream_request, i2);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerHeldDown:1.0")) {
                    throw ServerHeldDownHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerAlreadyInstalled:1.0")) {
                    throw ServerAlreadyInstalledHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                install(i2);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public String[] getORBNames(int i2) throws ServerNotRegistered {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("getORBNames", true);
                ServerIdHelper.write(outputStream_request, i2);
                inputStream_invoke = _invoke(outputStream_request);
                String[] strArr = ORBidListHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return strArr;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                String[] oRBNames = getORBNames(i2);
                _releaseReply(inputStream_invoke);
                return oRBNames;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void uninstall(int i2) throws ServerNotRegistered, ServerHeldDown, ServerAlreadyUninstalled {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("uninstall", true);
                ServerIdHelper.write(outputStream_request, i2);
                inputStream_invoke = _invoke(outputStream_request);
                _releaseReply(inputStream_invoke);
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerHeldDown:1.0")) {
                    throw ServerHeldDownHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerAlreadyUninstalled:1.0")) {
                    throw ServerAlreadyUninstalledHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                uninstall(i2);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        String utf = objectInputStream.readUTF();
        ORB orbInit = ORB.init((String[]) null, (Properties) null);
        try {
            _set_delegate(((ObjectImpl) orbInit.string_to_object(utf))._get_delegate());
            orbInit.destroy();
        } catch (Throwable th) {
            orbInit.destroy();
            throw th;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ORB orbInit = ORB.init((String[]) null, (Properties) null);
        try {
            objectOutputStream.writeUTF(orbInit.object_to_string(this));
            orbInit.destroy();
        } catch (Throwable th) {
            orbInit.destroy();
            throw th;
        }
    }
}
