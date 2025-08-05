package sun.instrument;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.jar.JarFile;

/* loaded from: rt.jar:sun/instrument/InstrumentationImpl.class */
public class InstrumentationImpl implements Instrumentation {
    private final long mNativeAgent;
    private final boolean mEnvironmentSupportsRedefineClasses;
    private final boolean mEnvironmentSupportsNativeMethodPrefix;
    private final TransformerManager mTransformerManager = new TransformerManager(false);
    private TransformerManager mRetransfomableTransformerManager = null;
    private volatile boolean mEnvironmentSupportsRetransformClassesKnown = false;
    private volatile boolean mEnvironmentSupportsRetransformClasses = false;

    private native boolean isModifiableClass0(long j2, Class<?> cls);

    private native boolean isRetransformClassesSupported0(long j2);

    private native void setHasRetransformableTransformers(long j2, boolean z2);

    private native void retransformClasses0(long j2, Class<?>[] clsArr);

    private native void redefineClasses0(long j2, ClassDefinition[] classDefinitionArr) throws ClassNotFoundException;

    private native Class[] getAllLoadedClasses0(long j2);

    private native Class[] getInitiatedClasses0(long j2, ClassLoader classLoader);

    private native long getObjectSize0(long j2, Object obj);

    private native void appendToClassLoaderSearch0(long j2, String str, boolean z2);

    private native void setNativeMethodPrefixes(long j2, String[] strArr, boolean z2);

    private InstrumentationImpl(long j2, boolean z2, boolean z3) {
        this.mNativeAgent = j2;
        this.mEnvironmentSupportsRedefineClasses = z2;
        this.mEnvironmentSupportsNativeMethodPrefix = z3;
    }

    @Override // java.lang.instrument.Instrumentation
    public void addTransformer(ClassFileTransformer classFileTransformer) {
        addTransformer(classFileTransformer, false);
    }

    @Override // java.lang.instrument.Instrumentation
    public synchronized void addTransformer(ClassFileTransformer classFileTransformer, boolean z2) {
        if (classFileTransformer == null) {
            throw new NullPointerException("null passed as 'transformer' in addTransformer");
        }
        if (z2) {
            if (!isRetransformClassesSupported()) {
                throw new UnsupportedOperationException("adding retransformable transformers is not supported in this environment");
            }
            if (this.mRetransfomableTransformerManager == null) {
                this.mRetransfomableTransformerManager = new TransformerManager(true);
            }
            this.mRetransfomableTransformerManager.addTransformer(classFileTransformer);
            if (this.mRetransfomableTransformerManager.getTransformerCount() == 1) {
                setHasRetransformableTransformers(this.mNativeAgent, true);
                return;
            }
            return;
        }
        this.mTransformerManager.addTransformer(classFileTransformer);
    }

    @Override // java.lang.instrument.Instrumentation
    public synchronized boolean removeTransformer(ClassFileTransformer classFileTransformer) {
        if (classFileTransformer == null) {
            throw new NullPointerException("null passed as 'transformer' in removeTransformer");
        }
        TransformerManager transformerManagerFindTransformerManager = findTransformerManager(classFileTransformer);
        if (transformerManagerFindTransformerManager != null) {
            transformerManagerFindTransformerManager.removeTransformer(classFileTransformer);
            if (transformerManagerFindTransformerManager.isRetransformable() && transformerManagerFindTransformerManager.getTransformerCount() == 0) {
                setHasRetransformableTransformers(this.mNativeAgent, false);
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // java.lang.instrument.Instrumentation
    public boolean isModifiableClass(Class<?> cls) {
        if (cls == null) {
            throw new NullPointerException("null passed as 'theClass' in isModifiableClass");
        }
        return isModifiableClass0(this.mNativeAgent, cls);
    }

    @Override // java.lang.instrument.Instrumentation
    public boolean isRetransformClassesSupported() {
        if (!this.mEnvironmentSupportsRetransformClassesKnown) {
            this.mEnvironmentSupportsRetransformClasses = isRetransformClassesSupported0(this.mNativeAgent);
            this.mEnvironmentSupportsRetransformClassesKnown = true;
        }
        return this.mEnvironmentSupportsRetransformClasses;
    }

    @Override // java.lang.instrument.Instrumentation
    public void retransformClasses(Class<?>... clsArr) {
        if (!isRetransformClassesSupported()) {
            throw new UnsupportedOperationException("retransformClasses is not supported in this environment");
        }
        retransformClasses0(this.mNativeAgent, clsArr);
    }

    @Override // java.lang.instrument.Instrumentation
    public boolean isRedefineClassesSupported() {
        return this.mEnvironmentSupportsRedefineClasses;
    }

    @Override // java.lang.instrument.Instrumentation
    public void redefineClasses(ClassDefinition... classDefinitionArr) throws ClassNotFoundException {
        if (!isRedefineClassesSupported()) {
            throw new UnsupportedOperationException("redefineClasses is not supported in this environment");
        }
        if (classDefinitionArr == null) {
            throw new NullPointerException("null passed as 'definitions' in redefineClasses");
        }
        for (ClassDefinition classDefinition : classDefinitionArr) {
            if (classDefinition == null) {
                throw new NullPointerException("element of 'definitions' is null in redefineClasses");
            }
        }
        if (classDefinitionArr.length == 0) {
            return;
        }
        redefineClasses0(this.mNativeAgent, classDefinitionArr);
    }

    @Override // java.lang.instrument.Instrumentation
    public Class[] getAllLoadedClasses() {
        return getAllLoadedClasses0(this.mNativeAgent);
    }

    @Override // java.lang.instrument.Instrumentation
    public Class[] getInitiatedClasses(ClassLoader classLoader) {
        return getInitiatedClasses0(this.mNativeAgent, classLoader);
    }

    @Override // java.lang.instrument.Instrumentation
    public long getObjectSize(Object obj) {
        if (obj == null) {
            throw new NullPointerException("null passed as 'objectToSize' in getObjectSize");
        }
        return getObjectSize0(this.mNativeAgent, obj);
    }

    @Override // java.lang.instrument.Instrumentation
    public void appendToBootstrapClassLoaderSearch(JarFile jarFile) {
        appendToClassLoaderSearch0(this.mNativeAgent, jarFile.getName(), true);
    }

    @Override // java.lang.instrument.Instrumentation
    public void appendToSystemClassLoaderSearch(JarFile jarFile) {
        appendToClassLoaderSearch0(this.mNativeAgent, jarFile.getName(), false);
    }

    @Override // java.lang.instrument.Instrumentation
    public boolean isNativeMethodPrefixSupported() {
        return this.mEnvironmentSupportsNativeMethodPrefix;
    }

    @Override // java.lang.instrument.Instrumentation
    public synchronized void setNativeMethodPrefix(ClassFileTransformer classFileTransformer, String str) {
        if (!isNativeMethodPrefixSupported()) {
            throw new UnsupportedOperationException("setNativeMethodPrefix is not supported in this environment");
        }
        if (classFileTransformer == null) {
            throw new NullPointerException("null passed as 'transformer' in setNativeMethodPrefix");
        }
        TransformerManager transformerManagerFindTransformerManager = findTransformerManager(classFileTransformer);
        if (transformerManagerFindTransformerManager == null) {
            throw new IllegalArgumentException("transformer not registered in setNativeMethodPrefix");
        }
        transformerManagerFindTransformerManager.setNativeMethodPrefix(classFileTransformer, str);
        setNativeMethodPrefixes(this.mNativeAgent, transformerManagerFindTransformerManager.getNativeMethodPrefixes(), transformerManagerFindTransformerManager.isRetransformable());
    }

    private TransformerManager findTransformerManager(ClassFileTransformer classFileTransformer) {
        if (this.mTransformerManager.includesTransformer(classFileTransformer)) {
            return this.mTransformerManager;
        }
        if (this.mRetransfomableTransformerManager != null && this.mRetransfomableTransformerManager.includesTransformer(classFileTransformer)) {
            return this.mRetransfomableTransformerManager;
        }
        return null;
    }

    static {
        System.loadLibrary("instrument");
    }

    private static void setAccessible(final AccessibleObject accessibleObject, final boolean z2) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.instrument.InstrumentationImpl.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() throws SecurityException {
                accessibleObject.setAccessible(z2);
                return null;
            }
        });
    }

    private void loadClassAndStartAgent(String str, String str2, String str3) throws Throwable {
        Class<?> clsLoadClass = ClassLoader.getSystemClassLoader().loadClass(str);
        Method declaredMethod = null;
        NoSuchMethodException noSuchMethodException = null;
        boolean z2 = false;
        try {
            declaredMethod = clsLoadClass.getDeclaredMethod(str2, String.class, Instrumentation.class);
            z2 = true;
        } catch (NoSuchMethodException e2) {
            noSuchMethodException = e2;
        }
        if (declaredMethod == null) {
            try {
                declaredMethod = clsLoadClass.getDeclaredMethod(str2, String.class);
            } catch (NoSuchMethodException e3) {
            }
        }
        if (declaredMethod == null) {
            try {
                declaredMethod = clsLoadClass.getMethod(str2, String.class, Instrumentation.class);
                z2 = true;
            } catch (NoSuchMethodException e4) {
            }
        }
        if (declaredMethod == null) {
            try {
                declaredMethod = clsLoadClass.getMethod(str2, String.class);
            } catch (NoSuchMethodException e5) {
                throw noSuchMethodException;
            }
        }
        setAccessible(declaredMethod, true);
        if (z2) {
            declaredMethod.invoke(null, str3, this);
        } else {
            declaredMethod.invoke(null, str3);
        }
        setAccessible(declaredMethod, false);
    }

    private void loadClassAndCallPremain(String str, String str2) throws Throwable {
        loadClassAndStartAgent(str, "premain", str2);
    }

    private void loadClassAndCallAgentmain(String str, String str2) throws Throwable {
        loadClassAndStartAgent(str, "agentmain", str2);
    }

    private byte[] transform(ClassLoader classLoader, String str, Class<?> cls, ProtectionDomain protectionDomain, byte[] bArr, boolean z2) {
        TransformerManager transformerManager = z2 ? this.mRetransfomableTransformerManager : this.mTransformerManager;
        if (transformerManager == null) {
            return null;
        }
        return transformerManager.transform(classLoader, str, cls, protectionDomain, bArr);
    }
}
