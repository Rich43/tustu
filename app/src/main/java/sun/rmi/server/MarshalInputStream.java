package sun.rmi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.rmi.server.RMIClassLoader;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permission;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sun.misc.ObjectStreamClassValidator;
import sun.misc.SharedSecrets;
import sun.misc.VM;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/server/MarshalInputStream.class */
public class MarshalInputStream extends ObjectInputStream {
    private volatile StreamChecker streamChecker;
    private static final boolean useCodebaseOnlyProperty;
    protected static Map<String, Class<?>> permittedSunClasses;
    private boolean skipDefaultResolveClass;
    private final Map<Object, Runnable> doneCallbacks;
    private boolean useCodebaseOnly;

    /* loaded from: rt.jar:sun/rmi/server/MarshalInputStream$StreamChecker.class */
    interface StreamChecker extends ObjectStreamClassValidator {
        void checkProxyInterfaceNames(String[] strArr);
    }

    static {
        useCodebaseOnlyProperty = !((String) AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.useCodebaseOnly", "true"))).equalsIgnoreCase("false");
        permittedSunClasses = new HashMap(3);
        try {
            permittedSunClasses.put("sun.rmi.server.Activation$ActivationSystemImpl_Stub", Class.forName("sun.rmi.server.Activation$ActivationSystemImpl_Stub"));
            permittedSunClasses.put("sun.rmi.registry.RegistryImpl_Stub", Class.forName("sun.rmi.registry.RegistryImpl_Stub"));
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError("Missing system class: " + e2.getMessage());
        }
    }

    public MarshalInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
        this.streamChecker = null;
        this.skipDefaultResolveClass = false;
        this.doneCallbacks = new HashMap(3);
        this.useCodebaseOnly = useCodebaseOnlyProperty;
    }

    public Runnable getDoneCallback(Object obj) {
        return this.doneCallbacks.get(obj);
    }

    public void setDoneCallback(Object obj, Runnable runnable) {
        this.doneCallbacks.put(obj, runnable);
    }

    public void done() {
        Iterator<Runnable> it = this.doneCallbacks.values().iterator();
        while (it.hasNext()) {
            it.next().run();
        }
        this.doneCallbacks.clear();
    }

    @Override // java.io.ObjectInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        done();
        super.close();
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        Object location = readLocation();
        String name = objectStreamClass.getName();
        ClassLoader classLoaderLatestUserDefinedLoader = this.skipDefaultResolveClass ? null : latestUserDefinedLoader();
        String str = null;
        if (!this.useCodebaseOnly && (location instanceof String)) {
            str = (String) location;
        }
        try {
            return RMIClassLoader.loadClass(str, name, classLoaderLatestUserDefinedLoader);
        } catch (ClassNotFoundException e2) {
            try {
                if (Character.isLowerCase(name.charAt(0)) && name.indexOf(46) == -1) {
                    return super.resolveClass(objectStreamClass);
                }
            } catch (ClassNotFoundException e3) {
            }
            throw e2;
        } catch (AccessControlException e4) {
            return checkSunClass(name, e4);
        }
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveProxyClass(String[] strArr) throws IOException, ClassNotFoundException {
        StreamChecker streamChecker = this.streamChecker;
        if (streamChecker != null) {
            streamChecker.checkProxyInterfaceNames(strArr);
        }
        Object location = readLocation();
        ClassLoader classLoaderLatestUserDefinedLoader = this.skipDefaultResolveClass ? null : latestUserDefinedLoader();
        String str = null;
        if (!this.useCodebaseOnly && (location instanceof String)) {
            str = (String) location;
        }
        return RMIClassLoader.loadProxyClass(str, strArr, classLoaderLatestUserDefinedLoader);
    }

    private static ClassLoader latestUserDefinedLoader() {
        return VM.latestUserDefinedLoader();
    }

    private Class<?> checkSunClass(String str, AccessControlException accessControlException) throws AccessControlException {
        Permission permission = accessControlException.getPermission();
        String name = null;
        if (permission != null) {
            name = permission.getName();
        }
        Class<?> cls = permittedSunClasses.get(str);
        if (name == null || cls == null || (!name.equals("accessClassInPackage.sun.rmi.server") && !name.equals("accessClassInPackage.sun.rmi.registry"))) {
            throw accessControlException;
        }
        return cls;
    }

    protected Object readLocation() throws IOException, ClassNotFoundException {
        return readObject();
    }

    void skipDefaultResolveClass() {
        this.skipDefaultResolveClass = true;
    }

    void useCodebaseOnly() {
        this.useCodebaseOnly = true;
    }

    synchronized void setStreamChecker(StreamChecker streamChecker) {
        this.streamChecker = streamChecker;
        SharedSecrets.getJavaObjectInputStreamAccess().setValidator(this, streamChecker);
    }

    @Override // java.io.ObjectInputStream
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass classDescriptor = super.readClassDescriptor();
        validateDesc(classDescriptor);
        return classDescriptor;
    }

    private void validateDesc(ObjectStreamClass objectStreamClass) {
        StreamChecker streamChecker;
        synchronized (this) {
            streamChecker = this.streamChecker;
        }
        if (streamChecker != null) {
            streamChecker.validateDescriptor(objectStreamClass);
        }
    }
}
