package sun.rmi.server;

import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.rmi.MarshalException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.security.AccessController;
import sun.misc.SharedSecrets;
import sun.rmi.runtime.Log;
import sun.rmi.transport.Connection;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.StreamRemoteCall;
import sun.security.action.GetBooleanAction;

/* loaded from: rt.jar:sun/rmi/server/UnicastRef.class */
public class UnicastRef implements RemoteRef {
    public static final Log clientRefLog = Log.getLog("sun.rmi.client.ref", WSDLConstants.ATTR_TRANSPORT, Util.logLevel);
    public static final Log clientCallLog = Log.getLog("sun.rmi.client.call", "RMI", ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.rmi.client.logCalls"))).booleanValue());
    private static final long serialVersionUID = 8258372400816541186L;
    protected LiveRef ref;

    public UnicastRef() {
    }

    public UnicastRef(LiveRef liveRef) {
        this.ref = liveRef;
    }

    public LiveRef getLiveRef() {
        return this.ref;
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.rmi.server.RemoteRef
    public Object invoke(Remote remote, Method method, Object[] objArr, long j2) throws Exception {
        if (clientRefLog.isLoggable(Log.VERBOSE)) {
            clientRefLog.log(Log.VERBOSE, "method: " + ((Object) method));
        }
        if (clientCallLog.isLoggable(Log.VERBOSE)) {
            logClientCall(remote, method);
        }
        Connection connectionNewConnection = this.ref.getChannel().newConnection();
        boolean z2 = true;
        try {
            try {
                if (clientRefLog.isLoggable(Log.VERBOSE)) {
                    clientRefLog.log(Log.VERBOSE, "opnum = " + j2);
                }
                StreamRemoteCall streamRemoteCall = new StreamRemoteCall(connectionNewConnection, this.ref.getObjID(), -1, j2);
                try {
                    ObjectOutput outputStream = streamRemoteCall.getOutputStream();
                    marshalCustomCallData(outputStream);
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (int i2 = 0; i2 < parameterTypes.length; i2++) {
                        marshalValue(parameterTypes[i2], objArr[i2], outputStream);
                    }
                    streamRemoteCall.executeCall();
                    try {
                        try {
                            Class<?> returnType = method.getReturnType();
                            if (returnType != Void.TYPE) {
                                Object objUnmarshalValue = unmarshalValue(returnType, streamRemoteCall.getInputStream());
                                clientRefLog.log(Log.BRIEF, "free connection (reuse = true)");
                                this.ref.getChannel().free(connectionNewConnection, true);
                                try {
                                    streamRemoteCall.done();
                                } catch (IOException e2) {
                                    z2 = false;
                                }
                                if (1 == 0) {
                                    if (clientRefLog.isLoggable(Log.BRIEF)) {
                                        clientRefLog.log(Log.BRIEF, "free connection (reuse = " + z2 + ")");
                                    }
                                    this.ref.getChannel().free(connectionNewConnection, z2);
                                }
                                return objUnmarshalValue;
                            }
                            try {
                                streamRemoteCall.done();
                            } catch (IOException e3) {
                                z2 = false;
                            }
                            if (0 == 0) {
                                if (clientRefLog.isLoggable(Log.BRIEF)) {
                                    clientRefLog.log(Log.BRIEF, "free connection (reuse = " + z2 + ")");
                                }
                                this.ref.getChannel().free(connectionNewConnection, z2);
                            }
                            return null;
                        } catch (IOException | ClassNotFoundException e4) {
                            streamRemoteCall.discardPendingRefs();
                            clientRefLog.log(Log.BRIEF, e4.getClass().getName() + " unmarshalling return: ", e4);
                            throw new UnmarshalException("error unmarshalling return", e4);
                        }
                    } catch (Throwable th) {
                        try {
                            streamRemoteCall.done();
                        } catch (IOException e5) {
                        }
                        throw th;
                    }
                } catch (IOException e6) {
                    clientRefLog.log(Log.BRIEF, "IOException marshalling arguments: ", e6);
                    throw new MarshalException("error marshalling arguments", e6);
                }
            } catch (Error e7) {
                throw e7;
            } catch (RuntimeException e8) {
                if (0 == 0 || ((StreamRemoteCall) null).getServerException() != e8) {
                }
                throw e8;
            } catch (RemoteException e9) {
                throw e9;
            }
        } catch (Throwable th2) {
            if (0 == 0) {
                if (clientRefLog.isLoggable(Log.BRIEF)) {
                    clientRefLog.log(Log.BRIEF, "free connection (reuse = true)");
                }
                this.ref.getChannel().free(connectionNewConnection, true);
            }
            throw th2;
        }
    }

    protected void marshalCustomCallData(ObjectOutput objectOutput) throws IOException {
    }

    protected static void marshalValue(Class<?> cls, Object obj, ObjectOutput objectOutput) throws IOException {
        if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                objectOutput.writeInt(((Integer) obj).intValue());
                return;
            }
            if (cls == Boolean.TYPE) {
                objectOutput.writeBoolean(((Boolean) obj).booleanValue());
                return;
            }
            if (cls == Byte.TYPE) {
                objectOutput.writeByte(((Byte) obj).byteValue());
                return;
            }
            if (cls == Character.TYPE) {
                objectOutput.writeChar(((Character) obj).charValue());
                return;
            }
            if (cls == Short.TYPE) {
                objectOutput.writeShort(((Short) obj).shortValue());
                return;
            }
            if (cls == Long.TYPE) {
                objectOutput.writeLong(((Long) obj).longValue());
                return;
            } else if (cls == Float.TYPE) {
                objectOutput.writeFloat(((Float) obj).floatValue());
                return;
            } else {
                if (cls == Double.TYPE) {
                    objectOutput.writeDouble(((Double) obj).doubleValue());
                    return;
                }
                throw new Error("Unrecognized primitive type: " + ((Object) cls));
            }
        }
        objectOutput.writeObject(obj);
    }

    protected static Object unmarshalValue(Class<?> cls, ObjectInput objectInput) throws IOException, ClassNotFoundException {
        if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                return Integer.valueOf(objectInput.readInt());
            }
            if (cls == Boolean.TYPE) {
                return Boolean.valueOf(objectInput.readBoolean());
            }
            if (cls == Byte.TYPE) {
                return Byte.valueOf(objectInput.readByte());
            }
            if (cls == Character.TYPE) {
                return Character.valueOf(objectInput.readChar());
            }
            if (cls == Short.TYPE) {
                return Short.valueOf(objectInput.readShort());
            }
            if (cls == Long.TYPE) {
                return Long.valueOf(objectInput.readLong());
            }
            if (cls == Float.TYPE) {
                return Float.valueOf(objectInput.readFloat());
            }
            if (cls == Double.TYPE) {
                return Double.valueOf(objectInput.readDouble());
            }
            throw new Error("Unrecognized primitive type: " + ((Object) cls));
        }
        if (cls == String.class && (objectInput instanceof ObjectInputStream)) {
            return SharedSecrets.getJavaObjectInputStreamReadString().readString((ObjectInputStream) objectInput);
        }
        return objectInput.readObject();
    }

    @Override // java.rmi.server.RemoteRef
    public RemoteCall newCall(RemoteObject remoteObject, Operation[] operationArr, int i2, long j2) throws RemoteException {
        clientRefLog.log(Log.BRIEF, "get connection");
        Connection connectionNewConnection = this.ref.getChannel().newConnection();
        try {
            clientRefLog.log(Log.VERBOSE, "create call context");
            if (clientCallLog.isLoggable(Log.VERBOSE)) {
                logClientCall(remoteObject, operationArr[i2]);
            }
            StreamRemoteCall streamRemoteCall = new StreamRemoteCall(connectionNewConnection, this.ref.getObjID(), i2, j2);
            try {
                marshalCustomCallData(streamRemoteCall.getOutputStream());
                return streamRemoteCall;
            } catch (IOException e2) {
                throw new MarshalException("error marshaling custom call data");
            }
        } catch (RemoteException e3) {
            this.ref.getChannel().free(connectionNewConnection, false);
            throw e3;
        }
    }

    @Override // java.rmi.server.RemoteRef
    public void invoke(RemoteCall remoteCall) throws Exception {
        try {
            clientRefLog.log(Log.VERBOSE, "execute call");
            remoteCall.executeCall();
        } catch (Error e2) {
            clientRefLog.log(Log.BRIEF, "error: ", e2);
            free(remoteCall, false);
            throw e2;
        } catch (RuntimeException e3) {
            clientRefLog.log(Log.BRIEF, "exception: ", e3);
            free(remoteCall, false);
            throw e3;
        } catch (RemoteException e4) {
            clientRefLog.log(Log.BRIEF, "exception: ", e4);
            free(remoteCall, false);
            throw e4;
        } catch (Exception e5) {
            clientRefLog.log(Log.BRIEF, "exception: ", e5);
            free(remoteCall, true);
            throw e5;
        }
    }

    private void free(RemoteCall remoteCall, boolean z2) throws RemoteException {
        this.ref.getChannel().free(((StreamRemoteCall) remoteCall).getConnection(), z2);
    }

    @Override // java.rmi.server.RemoteRef
    public void done(RemoteCall remoteCall) throws RemoteException {
        clientRefLog.log(Log.BRIEF, "free connection (reuse = true)");
        free(remoteCall, true);
        try {
            remoteCall.done();
        } catch (IOException e2) {
        }
    }

    void logClientCall(Object obj, Object obj2) {
        clientCallLog.log(Log.VERBOSE, "outbound call: " + ((Object) this.ref) + " : " + obj.getClass().getName() + this.ref.getObjID().toString() + ": " + obj2);
    }

    @Override // java.rmi.server.RemoteRef
    public String getRefClass(ObjectOutput objectOutput) {
        return "UnicastRef";
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        this.ref.write(objectOutput, false);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.ref = LiveRef.read(objectInput, false);
    }

    @Override // java.rmi.server.RemoteRef
    public String remoteToString() {
        return Util.getUnqualifiedName(getClass()) + " [liveRef: " + ((Object) this.ref) + "]";
    }

    @Override // java.rmi.server.RemoteRef
    public int remoteHashCode() {
        return this.ref.hashCode();
    }

    @Override // java.rmi.server.RemoteRef
    public boolean remoteEquals(RemoteRef remoteRef) {
        if (remoteRef instanceof UnicastRef) {
            return this.ref.remoteEquals(((UnicastRef) remoteRef).ref);
        }
        return false;
    }
}
