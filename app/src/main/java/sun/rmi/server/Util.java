package sun.rmi.server;

import com.sun.corba.se.impl.util.Utility;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.StubNotFoundException;
import java.rmi.server.LogStream;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.rmi.server.Skeleton;
import java.rmi.server.SkeletonNotFoundException;
import java.security.AccessController;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import org.icepdf.core.util.PdfOps;
import sun.rmi.runtime.Log;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/server/Util.class */
public final class Util {
    static final int logLevel = LogStream.parseLevel((String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.server.logLevel")));
    public static final Log serverRefLog = Log.getLog("sun.rmi.server.ref", WSDLConstants.ATTR_TRANSPORT, logLevel);
    private static final boolean ignoreStubClasses = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("java.rmi.server.ignoreStubClasses"))).booleanValue();
    private static final Map<Class<?>, Void> withoutStubs = Collections.synchronizedMap(new WeakHashMap(11));
    private static final Class<?>[] stubConsParamTypes = {RemoteRef.class};

    private Util() {
    }

    public static Remote createProxy(Class<?> cls, RemoteRef remoteRef, boolean z2) throws StubNotFoundException {
        try {
            Class<?> remoteClass = getRemoteClass(cls);
            if (z2 || (!ignoreStubClasses && stubClassExists(remoteClass))) {
                return createStub(remoteClass, remoteRef);
            }
            final ClassLoader classLoader = cls.getClassLoader();
            final Class<?>[] remoteInterfaces = getRemoteInterfaces(cls);
            final RemoteObjectInvocationHandler remoteObjectInvocationHandler = new RemoteObjectInvocationHandler(remoteRef);
            try {
                return (Remote) AccessController.doPrivileged(new PrivilegedAction<Remote>() { // from class: sun.rmi.server.Util.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Remote run2() {
                        return (Remote) Proxy.newProxyInstance(classLoader, remoteInterfaces, remoteObjectInvocationHandler);
                    }
                });
            } catch (IllegalArgumentException e2) {
                throw new StubNotFoundException("unable to create proxy", e2);
            }
        } catch (ClassNotFoundException e3) {
            throw new StubNotFoundException("object does not implement a remote interface: " + cls.getName());
        }
    }

    private static boolean stubClassExists(Class<?> cls) {
        if (!withoutStubs.containsKey(cls)) {
            try {
                Class.forName(cls.getName() + Utility.RMI_STUB_SUFFIX, false, cls.getClassLoader());
                return true;
            } catch (ClassNotFoundException e2) {
                withoutStubs.put(cls, null);
                return false;
            }
        }
        return false;
    }

    private static Class<?> getRemoteClass(Class<?> cls) throws ClassNotFoundException {
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            for (int length = interfaces.length - 1; length >= 0; length--) {
                if (Remote.class.isAssignableFrom(interfaces[length])) {
                    return cls;
                }
            }
            cls = cls.getSuperclass();
        }
        throw new ClassNotFoundException("class does not implement java.rmi.Remote");
    }

    private static Class<?>[] getRemoteInterfaces(Class<?> cls) throws SecurityException {
        ArrayList arrayList = new ArrayList();
        getRemoteInterfaces(arrayList, cls);
        return (Class[]) arrayList.toArray(new Class[arrayList.size()]);
    }

    private static void getRemoteInterfaces(ArrayList<Class<?>> arrayList, Class<?> cls) throws SecurityException {
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass != null) {
            getRemoteInterfaces(arrayList, superclass);
        }
        for (Class<?> cls2 : cls.getInterfaces()) {
            if (Remote.class.isAssignableFrom(cls2) && !arrayList.contains(cls2)) {
                for (Method method : cls2.getMethods()) {
                    checkMethod(method);
                }
                arrayList.add(cls2);
            }
        }
    }

    private static void checkMethod(Method method) {
        for (Class<?> cls : method.getExceptionTypes()) {
            if (cls.isAssignableFrom(RemoteException.class)) {
                return;
            }
        }
        throw new IllegalArgumentException("illegal remote method encountered: " + ((Object) method));
    }

    private static RemoteStub createStub(Class<?> cls, RemoteRef remoteRef) throws StubNotFoundException {
        String str = cls.getName() + Utility.RMI_STUB_SUFFIX;
        try {
            return (RemoteStub) Class.forName(str, false, cls.getClassLoader()).getConstructor(stubConsParamTypes).newInstance(remoteRef);
        } catch (ClassCastException e2) {
            throw new StubNotFoundException("Stub class not instance of RemoteStub: " + str, e2);
        } catch (ClassNotFoundException e3) {
            throw new StubNotFoundException("Stub class not found: " + str, e3);
        } catch (IllegalAccessException e4) {
            throw new StubNotFoundException("Stub class constructor not public: " + str, e4);
        } catch (InstantiationException e5) {
            throw new StubNotFoundException("Can't create instance of stub class: " + str, e5);
        } catch (NoSuchMethodException e6) {
            throw new StubNotFoundException("Stub class missing constructor: " + str, e6);
        } catch (InvocationTargetException e7) {
            throw new StubNotFoundException("Exception creating instance of stub class: " + str, e7);
        }
    }

    static Skeleton createSkeleton(Remote remote) throws SkeletonNotFoundException {
        try {
            Class<?> remoteClass = getRemoteClass(remote.getClass());
            String str = remoteClass.getName() + "_Skel";
            try {
                return (Skeleton) Class.forName(str, false, remoteClass.getClassLoader()).newInstance();
            } catch (ClassCastException e2) {
                throw new SkeletonNotFoundException("Skeleton not of correct class: " + str, e2);
            } catch (ClassNotFoundException e3) {
                throw new SkeletonNotFoundException("Skeleton class not found: " + str, e3);
            } catch (IllegalAccessException e4) {
                throw new SkeletonNotFoundException("No public constructor: " + str, e4);
            } catch (InstantiationException e5) {
                throw new SkeletonNotFoundException("Can't create skeleton: " + str, e5);
            }
        } catch (ClassNotFoundException e6) {
            throw new SkeletonNotFoundException("object does not implement a remote interface: " + remote.getClass().getName());
        }
    }

    public static long computeMethodHash(Method method) {
        long j2 = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(127);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(byteArrayOutputStream, messageDigest));
            String methodNameAndDescriptor = getMethodNameAndDescriptor(method);
            if (serverRefLog.isLoggable(Log.VERBOSE)) {
                serverRefLog.log(Log.VERBOSE, "string used for method hash: \"" + methodNameAndDescriptor + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            dataOutputStream.writeUTF(methodNameAndDescriptor);
            dataOutputStream.flush();
            for (int i2 = 0; i2 < Math.min(8, messageDigest.digest().length); i2++) {
                j2 += (r0[i2] & 255) << (i2 * 8);
            }
        } catch (IOException e2) {
            j2 = -1;
        } catch (NoSuchAlgorithmException e3) {
            throw new SecurityException(e3.getMessage());
        }
        return j2;
    }

    private static String getMethodNameAndDescriptor(Method method) {
        StringBuffer stringBuffer = new StringBuffer(method.getName());
        stringBuffer.append('(');
        for (Class<?> cls : method.getParameterTypes()) {
            stringBuffer.append(getTypeDescriptor(cls));
        }
        stringBuffer.append(')');
        Class<?> returnType = method.getReturnType();
        if (returnType == Void.TYPE) {
            stringBuffer.append('V');
        } else {
            stringBuffer.append(getTypeDescriptor(returnType));
        }
        return stringBuffer.toString();
    }

    private static String getTypeDescriptor(Class<?> cls) {
        if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                return "I";
            }
            if (cls == Boolean.TYPE) {
                return Constants.HASIDCALL_INDEX_SIG;
            }
            if (cls == Byte.TYPE) {
                return PdfOps.B_TOKEN;
            }
            if (cls == Character.TYPE) {
                return "C";
            }
            if (cls == Short.TYPE) {
                return PdfOps.S_TOKEN;
            }
            if (cls == Long.TYPE) {
                return "J";
            }
            if (cls == Float.TYPE) {
                return PdfOps.F_TOKEN;
            }
            if (cls == Double.TYPE) {
                return PdfOps.D_TOKEN;
            }
            if (cls == Void.TYPE) {
                return "V";
            }
            throw new Error("unrecognized primitive type: " + ((Object) cls));
        }
        if (cls.isArray()) {
            return cls.getName().replace('.', '/');
        }
        return "L" + cls.getName().replace('.', '/') + ";";
    }

    public static String getUnqualifiedName(Class<?> cls) {
        String name = cls.getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }
}
