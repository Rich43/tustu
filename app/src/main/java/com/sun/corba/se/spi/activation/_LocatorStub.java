package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationHelper;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORBHelper;
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

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/_LocatorStub.class */
public class _LocatorStub extends ObjectImpl implements Locator {
    private static String[] __ids = {"IDL:activation/Locator:1.0"};

    @Override // com.sun.corba.se.spi.activation.LocatorOperations
    public ServerLocation locateServer(int i2, String str) throws NoSuchEndPoint, ServerNotRegistered, ServerHeldDown {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("locateServer", true);
                ServerIdHelper.write(outputStream_request, i2);
                outputStream_request.write_string(str);
                inputStream_invoke = _invoke(outputStream_request);
                ServerLocation serverLocation = ServerLocationHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return serverLocation;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/NoSuchEndPoint:1.0")) {
                    throw NoSuchEndPointHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerHeldDown:1.0")) {
                    throw ServerHeldDownHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                ServerLocation serverLocationLocateServer = locateServer(i2, str);
                _releaseReply(inputStream_invoke);
                return serverLocationLocateServer;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.LocatorOperations
    public ServerLocationPerORB locateServerForORB(int i2, String str) throws InvalidORBid, ServerNotRegistered, ServerHeldDown {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("locateServerForORB", true);
                ServerIdHelper.write(outputStream_request, i2);
                ORBidHelper.write(outputStream_request, str);
                inputStream_invoke = _invoke(outputStream_request);
                ServerLocationPerORB serverLocationPerORB = ServerLocationPerORBHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return serverLocationPerORB;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/InvalidORBid:1.0")) {
                    throw InvalidORBidHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerNotRegistered:1.0")) {
                    throw ServerNotRegisteredHelper.read(inputStream);
                }
                if (id.equals("IDL:activation/ServerHeldDown:1.0")) {
                    throw ServerHeldDownHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                ServerLocationPerORB serverLocationPerORBLocateServerForORB = locateServerForORB(i2, str);
                _releaseReply(inputStream_invoke);
                return serverLocationPerORBLocateServerForORB;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.LocatorOperations
    public int getEndpoint(String str) throws NoSuchEndPoint {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("getEndpoint", true);
                outputStream_request.write_string(str);
                inputStream_invoke = _invoke(outputStream_request);
                int i2 = TCPPortHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return i2;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/NoSuchEndPoint:1.0")) {
                    throw NoSuchEndPointHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                int endpoint = getEndpoint(str);
                _releaseReply(inputStream_invoke);
                return endpoint;
            }
        } catch (Throwable th) {
            _releaseReply(inputStream_invoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.activation.LocatorOperations
    public int getServerPortForType(ServerLocationPerORB serverLocationPerORB, String str) throws NoSuchEndPoint {
        InputStream inputStream_invoke = null;
        try {
            try {
                OutputStream outputStream_request = _request("getServerPortForType", true);
                ServerLocationPerORBHelper.write(outputStream_request, serverLocationPerORB);
                outputStream_request.write_string(str);
                inputStream_invoke = _invoke(outputStream_request);
                int i2 = TCPPortHelper.read(inputStream_invoke);
                _releaseReply(inputStream_invoke);
                return i2;
            } catch (ApplicationException e2) {
                InputStream inputStream = e2.getInputStream();
                String id = e2.getId();
                if (id.equals("IDL:activation/NoSuchEndPoint:1.0")) {
                    throw NoSuchEndPointHelper.read(inputStream);
                }
                throw new MARSHAL(id);
            } catch (RemarshalException e3) {
                int serverPortForType = getServerPortForType(serverLocationPerORB, str);
                _releaseReply(inputStream_invoke);
                return serverPortForType;
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
