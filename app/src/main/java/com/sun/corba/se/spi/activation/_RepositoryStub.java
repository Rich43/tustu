package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDefHelper;
import com.sun.corba.se.spi.activation.RepositoryPackage.StringSeqHelper;
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

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_RepositoryStub.class */
public class _RepositoryStub extends ObjectImpl implements Repository {
    private static String[] __ids = {"IDL:activation/Repository:1.0"};

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public int registerServer(ServerDef serverDef) throws ServerAlreadyRegistered, BadServerDefinition {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("registerServer", true);
                ServerDefHelper.write(outputStream_request, serverDef);
                inputStream_invoke = _invoke(outputStream_request);
                int i2 = ServerIdHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return i2;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerAlreadyRegistered:1.0")) {
                    throw ServerAlreadyRegisteredHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/BadServerDefinition:1.0")) {
                    throw BadServerDefinitionHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                int iRegisterServer = registerServer(serverDef);
                _releaseReply(inputStream_invoke);
                return iRegisterServer;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public void unregisterServer(int i2) throws ServerNotRegistered {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("unregisterServer", true);
                ServerIdHelper.write(outputStream_request, i2);
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
                unregisterServer(i2);
                _releaseReply(inputStream_invoke);
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public ServerDef getServer(int i2) throws ServerNotRegistered {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("getServer", true);
                ServerIdHelper.write(outputStream_request, i2);
                inputStream_invoke = _invoke(outputStream_request);
                ServerDef serverDef = ServerDefHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return serverDef;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                ServerDef server = getServer(i2);
                _releaseReply(inputStream_invoke);
                return server;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public boolean isInstalled(int i2) throws ServerNotRegistered {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("isInstalled", true);
                ServerIdHelper.write(outputStream_request, i2);
                inputStream_invoke = _invoke(outputStream_request);
                boolean z2 = inputStream_invoke.read_boolean();
                _releaseReply(inputStream_invoke);
                return z2;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                boolean zIsInstalled = isInstalled(i2);
                _releaseReply(inputStream_invoke);
                return zIsInstalled;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public void install(int i2) throws ServerNotRegistered, ServerAlreadyInstalled {
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

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public void uninstall(int i2) throws ServerNotRegistered, ServerAlreadyUninstalled {
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

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public int[] listRegisteredServers() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("listRegisteredServers", true));
                    int[] iArr = ServerIdsHelper.read(inputStream_invoke);
                    _releaseReply(inputStream_invoke);
                    return iArr;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                int[] iArrListRegisteredServers = listRegisteredServers();
                _releaseReply(inputStream_invoke);
                return iArrListRegisteredServers;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public String[] getApplicationNames() {
        InputStream inputStream_invoke = null;
        try {
            try {
                try {
                    inputStream_invoke = _invoke(_request("getApplicationNames", true));
                    String[] strArr = StringSeqHelper.read(inputStream_invoke);
                    _releaseReply(inputStream_invoke);
                    return strArr;
                } catch (ApplicationException e2) {
                    e2.getInputStream();
                    throw new MARSHAL(e2.getId());
                }
            } catch (RemarshalException e3) {
                String[] applicationNames = getApplicationNames();
                _releaseReply(inputStream_invoke);
                return applicationNames;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public int getServerID(String str) throws ServerNotRegistered {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("getServerID", true);
                outputStream_request.write_string(str);
                inputStream_invoke = _invoke(outputStream_request);
                int i2 = ServerIdHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return i2;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                int serverID = getServerID(str);
                _releaseReply(inputStream_invoke);
                return serverID;
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
