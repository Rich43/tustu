package java.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarEntry;
import java.util.spi.ResourceBundleControlProvider;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.util.locale.BaseLocale;
import sun.util.locale.LocaleObjectCache;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:58)
    */
/* loaded from: rt.jar:java/util/ResourceBundle.class */
public abstract class ResourceBundle {
    private static final int INITIAL_CACHE_SIZE = 32;
    private static final ResourceBundle NONEXISTENT_BUNDLE;
    private static final ConcurrentMap<CacheKey, BundleReference> cacheList;
    private static final ReferenceQueue<Object> referenceQueue;
    protected ResourceBundle parent = null;
    private Locale locale = null;
    private String name;
    private volatile boolean expired;
    private volatile CacheKey cacheKey;
    private volatile Set<String> keySet;
    private static final List<ResourceBundleControlProvider> providers;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:java/util/ResourceBundle$CacheKeyReference.class */
    private interface CacheKeyReference {
        CacheKey getCacheKey();
    }

    protected abstract Object handleGetObject(String str);

    public abstract Enumeration<String> getKeys();

    static {
        $assertionsDisabled = !ResourceBundle.class.desiredAssertionStatus();
        NONEXISTENT_BUNDLE = new ResourceBundle() { // from class: java.util.ResourceBundle.1
            @Override // java.util.ResourceBundle
            public Enumeration<String> getKeys() {
                return null;
            }

            @Override // java.util.ResourceBundle
            protected Object handleGetObject(String str) {
                return null;
            }

            public String toString() {
                return "NONEXISTENT_BUNDLE";
            }
        };
        cacheList = new ConcurrentHashMap(32);
        referenceQueue = new ReferenceQueue<>();
        ArrayList arrayList = null;
        Iterator it = ServiceLoader.loadInstalled(ResourceBundleControlProvider.class).iterator();
        while (it.hasNext()) {
            ResourceBundleControlProvider resourceBundleControlProvider = (ResourceBundleControlProvider) it.next();
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            arrayList.add(resourceBundleControlProvider);
        }
        providers = arrayList;
    }

    public String getBaseBundleName() {
        return this.name;
    }

    public ResourceBundle() {
    }

    public final String getString(String str) {
        return (String) getObject(str);
    }

    public final String[] getStringArray(String str) {
        return (String[]) getObject(str);
    }

    public final Object getObject(String str) {
        Object objHandleGetObject = handleGetObject(str);
        if (objHandleGetObject == null) {
            if (this.parent != null) {
                objHandleGetObject = this.parent.getObject(str);
            }
            if (objHandleGetObject == null) {
                throw new MissingResourceException("Can't find resource for bundle " + getClass().getName() + ", key " + str, getClass().getName(), str);
            }
        }
        return objHandleGetObject;
    }

    public Locale getLocale() {
        return this.locale;
    }

    private static ClassLoader getLoader(Class<?> cls) {
        ClassLoader classLoader = cls == null ? null : cls.getClassLoader();
        if (classLoader == null) {
            classLoader = RBClassLoader.INSTANCE;
        }
        return classLoader;
    }

    /* loaded from: rt.jar:java/util/ResourceBundle$RBClassLoader.class */
    private static class RBClassLoader extends ClassLoader {
        private static final RBClassLoader INSTANCE = (RBClassLoader) AccessController.doPrivileged(new PrivilegedAction<RBClassLoader>() { // from class: java.util.ResourceBundle.RBClassLoader.1
            @Override // java.security.PrivilegedAction
            public /* bridge */ /* synthetic */ RBClassLoader run() {
                return run();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public RBClassLoader run() {
                return new RBClassLoader(null);
            }
        });
        private static final ClassLoader loader;

        /* synthetic */ RBClassLoader(AnonymousClass1 anonymousClass1) {
            this();
        }

        static {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            while (true) {
                ClassLoader classLoader = systemClassLoader;
                ClassLoader parent = classLoader.getParent();
                if (parent != null) {
                    systemClassLoader = parent;
                } else {
                    loader = classLoader;
                    return;
                }
            }
        }

        private RBClassLoader() {
        }

        @Override // java.lang.ClassLoader
        public Class<?> loadClass(String str) throws ClassNotFoundException {
            if (loader != null) {
                return loader.loadClass(str);
            }
            return Class.forName(str);
        }

        @Override // java.lang.ClassLoader
        public URL getResource(String str) {
            if (loader != null) {
                return loader.getResource(str);
            }
            return ClassLoader.getSystemResource(str);
        }

        @Override // java.lang.ClassLoader
        public InputStream getResourceAsStream(String str) {
            if (loader != null) {
                return loader.getResourceAsStream(str);
            }
            return ClassLoader.getSystemResourceAsStream(str);
        }
    }

    protected void setParent(ResourceBundle resourceBundle) {
        if (!$assertionsDisabled && resourceBundle == NONEXISTENT_BUNDLE) {
            throw new AssertionError();
        }
        this.parent = resourceBundle;
    }

    /* loaded from: rt.jar:java/util/ResourceBundle$CacheKey.class */
    private static class CacheKey implements Cloneable {
        private String name;
        private Locale locale;
        private LoaderReference loaderRef;
        private String format;
        private volatile long loadTime;
        private volatile long expirationTime;
        private Throwable cause;
        private int hashCodeCache;

        /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$702(java.util.ResourceBundle.CacheKey r6, long r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.loadTime = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.ResourceBundle.CacheKey.access$702(java.util.ResourceBundle$CacheKey, long):long");
        }

        /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$602(java.util.ResourceBundle.CacheKey r6, long r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.expirationTime = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.ResourceBundle.CacheKey.access$602(java.util.ResourceBundle$CacheKey, long):long");
        }

        CacheKey(String str, Locale locale, ClassLoader classLoader) {
            this.name = str;
            this.locale = locale;
            if (classLoader == null) {
                this.loaderRef = null;
            } else {
                this.loaderRef = new LoaderReference(classLoader, ResourceBundle.referenceQueue, this);
            }
            calculateHashCode();
        }

        String getName() {
            return this.name;
        }

        CacheKey setName(String str) {
            if (!this.name.equals(str)) {
                this.name = str;
                calculateHashCode();
            }
            return this;
        }

        Locale getLocale() {
            return this.locale;
        }

        CacheKey setLocale(Locale locale) {
            if (!this.locale.equals(locale)) {
                this.locale = locale;
                calculateHashCode();
            }
            return this;
        }

        ClassLoader getLoader() {
            if (this.loaderRef != null) {
                return this.loaderRef.get();
            }
            return null;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            try {
                CacheKey cacheKey = (CacheKey) obj;
                if (this.hashCodeCache != cacheKey.hashCodeCache || !this.name.equals(cacheKey.name) || !this.locale.equals(cacheKey.locale)) {
                    return false;
                }
                if (this.loaderRef == null) {
                    return cacheKey.loaderRef == null;
                }
                ClassLoader classLoader = this.loaderRef.get();
                return (cacheKey.loaderRef == null || classLoader == null || classLoader != cacheKey.loaderRef.get()) ? false : true;
            } catch (ClassCastException | NullPointerException e2) {
                return false;
            }
        }

        public int hashCode() {
            return this.hashCodeCache;
        }

        private void calculateHashCode() {
            this.hashCodeCache = this.name.hashCode() << 3;
            this.hashCodeCache ^= this.locale.hashCode();
            ClassLoader loader = getLoader();
            if (loader != null) {
                this.hashCodeCache ^= loader.hashCode();
            }
        }

        public Object clone() {
            try {
                CacheKey cacheKey = (CacheKey) super.clone();
                if (this.loaderRef != null) {
                    cacheKey.loaderRef = new LoaderReference(this.loaderRef.get(), ResourceBundle.referenceQueue, cacheKey);
                }
                cacheKey.cause = null;
                return cacheKey;
            } catch (CloneNotSupportedException e2) {
                throw new InternalError(e2);
            }
        }

        String getFormat() {
            return this.format;
        }

        void setFormat(String str) {
            this.format = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCause(Throwable th) {
            if (this.cause == null) {
                this.cause = th;
            } else if (this.cause instanceof ClassNotFoundException) {
                this.cause = th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Throwable getCause() {
            return this.cause;
        }

        public String toString() {
            String string = this.locale.toString();
            if (string.length() == 0) {
                if (this.locale.getVariant().length() != 0) {
                    string = "__" + this.locale.getVariant();
                } else {
                    string = "\"\"";
                }
            }
            return "CacheKey[" + this.name + ", lc=" + string + ", ldr=" + ((Object) getLoader()) + "(format=" + this.format + ")]";
        }
    }

    /* loaded from: rt.jar:java/util/ResourceBundle$LoaderReference.class */
    private static class LoaderReference extends WeakReference<ClassLoader> implements CacheKeyReference {
        private CacheKey cacheKey;

        LoaderReference(ClassLoader classLoader, ReferenceQueue<Object> referenceQueue, CacheKey cacheKey) {
            super(classLoader, referenceQueue);
            this.cacheKey = cacheKey;
        }

        @Override // java.util.ResourceBundle.CacheKeyReference
        public CacheKey getCacheKey() {
            return this.cacheKey;
        }
    }

    /* loaded from: rt.jar:java/util/ResourceBundle$BundleReference.class */
    private static class BundleReference extends SoftReference<ResourceBundle> implements CacheKeyReference {
        private CacheKey cacheKey;

        BundleReference(ResourceBundle resourceBundle, ReferenceQueue<Object> referenceQueue, CacheKey cacheKey) {
            super(resourceBundle, referenceQueue);
            this.cacheKey = cacheKey;
        }

        @Override // java.util.ResourceBundle.CacheKeyReference
        public CacheKey getCacheKey() {
            return this.cacheKey;
        }
    }

    @CallerSensitive
    public static final ResourceBundle getBundle(String str) {
        return getBundleImpl(str, Locale.getDefault(), getLoader(Reflection.getCallerClass()), getDefaultControl(str));
    }

    @CallerSensitive
    public static final ResourceBundle getBundle(String str, Control control) {
        return getBundleImpl(str, Locale.getDefault(), getLoader(Reflection.getCallerClass()), control);
    }

    @CallerSensitive
    public static final ResourceBundle getBundle(String str, Locale locale) {
        return getBundleImpl(str, locale, getLoader(Reflection.getCallerClass()), getDefaultControl(str));
    }

    @CallerSensitive
    public static final ResourceBundle getBundle(String str, Locale locale, Control control) {
        return getBundleImpl(str, locale, getLoader(Reflection.getCallerClass()), control);
    }

    public static ResourceBundle getBundle(String str, Locale locale, ClassLoader classLoader) {
        if (classLoader == null) {
            throw new NullPointerException();
        }
        return getBundleImpl(str, locale, classLoader, getDefaultControl(str));
    }

    public static ResourceBundle getBundle(String str, Locale locale, ClassLoader classLoader, Control control) {
        if (classLoader == null || control == null) {
            throw new NullPointerException();
        }
        return getBundleImpl(str, locale, classLoader, control);
    }

    private static Control getDefaultControl(String str) {
        if (providers != null) {
            Iterator<ResourceBundleControlProvider> it = providers.iterator();
            while (it.hasNext()) {
                Control control = it.next().getControl(str);
                if (control != null) {
                    return control;
                }
            }
        }
        return Control.INSTANCE;
    }

    private static ResourceBundle getBundleImpl(String str, Locale locale, ClassLoader classLoader, Control control) {
        if (locale == null || control == null) {
            throw new NullPointerException();
        }
        CacheKey cacheKey = new CacheKey(str, locale, classLoader);
        ResourceBundle resourceBundleFindBundle = null;
        BundleReference bundleReference = cacheList.get(cacheKey);
        if (bundleReference != null) {
            resourceBundleFindBundle = bundleReference.get();
        }
        if (isValidBundle(resourceBundleFindBundle) && hasValidParentChain(resourceBundleFindBundle)) {
            return resourceBundleFindBundle;
        }
        boolean z2 = control == Control.INSTANCE || (control instanceof SingleFormatControl);
        List<String> formats = control.getFormats(str);
        if (!z2 && !checkList(formats)) {
            throw new IllegalArgumentException("Invalid Control: getFormats");
        }
        ResourceBundle resourceBundle = null;
        Locale fallbackLocale = locale;
        while (true) {
            Locale locale2 = fallbackLocale;
            if (locale2 == null) {
                break;
            }
            List<Locale> candidateLocales = control.getCandidateLocales(str, locale2);
            if (!z2 && !checkList(candidateLocales)) {
                throw new IllegalArgumentException("Invalid Control: getCandidateLocales");
            }
            resourceBundleFindBundle = findBundle(cacheKey, candidateLocales, formats, 0, control, resourceBundle);
            if (isValidBundle(resourceBundleFindBundle)) {
                boolean zEquals = Locale.ROOT.equals(resourceBundleFindBundle.locale);
                if (!zEquals || resourceBundleFindBundle.locale.equals(locale) || (candidateLocales.size() == 1 && resourceBundleFindBundle.locale.equals(candidateLocales.get(0)))) {
                    break;
                }
                if (zEquals && resourceBundle == null) {
                    resourceBundle = resourceBundleFindBundle;
                }
            }
            fallbackLocale = control.getFallbackLocale(str, locale2);
        }
        if (resourceBundleFindBundle == null) {
            if (resourceBundle == null) {
                throwMissingResourceException(str, locale, cacheKey.getCause());
            }
            resourceBundleFindBundle = resourceBundle;
        }
        keepAlive(classLoader);
        return resourceBundleFindBundle;
    }

    private static void keepAlive(ClassLoader classLoader) {
    }

    private static boolean checkList(List<?> list) {
        boolean z2 = (list == null || list.isEmpty()) ? false : true;
        if (z2) {
            int size = list.size();
            for (int i2 = 0; z2 && i2 < size; i2++) {
                z2 = list.get(i2) != null;
            }
        }
        return z2;
    }

    private static ResourceBundle findBundle(CacheKey cacheKey, List<Locale> list, List<String> list2, int i2, Control control, ResourceBundle resourceBundle) {
        Locale locale = list.get(i2);
        ResourceBundle resourceBundleFindBundle = null;
        if (i2 != list.size() - 1) {
            resourceBundleFindBundle = findBundle(cacheKey, list, list2, i2 + 1, control, resourceBundle);
        } else if (resourceBundle != null && Locale.ROOT.equals(locale)) {
            return resourceBundle;
        }
        while (true) {
            Object objPoll = referenceQueue.poll();
            if (objPoll == null) {
                break;
            }
            cacheList.remove(((CacheKeyReference) objPoll).getCacheKey());
        }
        boolean z2 = false;
        cacheKey.setLocale(locale);
        ResourceBundle resourceBundleFindBundleInCache = findBundleInCache(cacheKey, control);
        if (isValidBundle(resourceBundleFindBundleInCache)) {
            z2 = resourceBundleFindBundleInCache.expired;
            if (!z2) {
                if (resourceBundleFindBundleInCache.parent == resourceBundleFindBundle) {
                    return resourceBundleFindBundleInCache;
                }
                BundleReference bundleReference = cacheList.get(cacheKey);
                if (bundleReference != null && bundleReference.get() == resourceBundleFindBundleInCache) {
                    cacheList.remove(cacheKey, bundleReference);
                }
            }
        }
        if (resourceBundleFindBundleInCache != NONEXISTENT_BUNDLE) {
            CacheKey cacheKey2 = (CacheKey) cacheKey.clone();
            try {
                ResourceBundle resourceBundleLoadBundle = loadBundle(cacheKey, list2, control, z2);
                if (resourceBundleLoadBundle != null) {
                    if (resourceBundleLoadBundle.parent == null) {
                        resourceBundleLoadBundle.setParent(resourceBundleFindBundle);
                    }
                    resourceBundleLoadBundle.locale = locale;
                    ResourceBundle resourceBundlePutBundleInCache = putBundleInCache(cacheKey, resourceBundleLoadBundle, control);
                    if (cacheKey2.getCause() instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                    return resourceBundlePutBundleInCache;
                }
                putBundleInCache(cacheKey, NONEXISTENT_BUNDLE, control);
                if (cacheKey2.getCause() instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            } catch (Throwable th) {
                if (cacheKey2.getCause() instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        return resourceBundleFindBundle;
    }

    private static ResourceBundle loadBundle(CacheKey cacheKey, List<String> list, Control control, boolean z2) {
        Locale locale = cacheKey.getLocale();
        ResourceBundle resourceBundleNewBundle = null;
        int size = list.size();
        int i2 = 0;
        while (true) {
            if (i2 >= size) {
                break;
            }
            String str = list.get(i2);
            try {
                resourceBundleNewBundle = control.newBundle(cacheKey.getName(), locale, str, cacheKey.getLoader(), z2);
            } catch (Exception e2) {
                cacheKey.setCause(e2);
            } catch (LinkageError e3) {
                cacheKey.setCause(e3);
            }
            if (resourceBundleNewBundle == null) {
                i2++;
            } else {
                cacheKey.setFormat(str);
                resourceBundleNewBundle.name = cacheKey.getName();
                resourceBundleNewBundle.locale = locale;
                resourceBundleNewBundle.expired = false;
                break;
            }
        }
        return resourceBundleNewBundle;
    }

    private static boolean isValidBundle(ResourceBundle resourceBundle) {
        return (resourceBundle == null || resourceBundle == NONEXISTENT_BUNDLE) ? false : true;
    }

    private static boolean hasValidParentChain(ResourceBundle resourceBundle) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (resourceBundle != null) {
            if (resourceBundle.expired) {
                return false;
            }
            CacheKey cacheKey = resourceBundle.cacheKey;
            if (cacheKey != null) {
                long j2 = cacheKey.expirationTime;
                if (j2 >= 0 && j2 <= jCurrentTimeMillis) {
                    return false;
                }
            }
            resourceBundle = resourceBundle.parent;
        }
        return true;
    }

    private static void throwMissingResourceException(String str, Locale locale, Throwable th) {
        if (th instanceof MissingResourceException) {
            th = null;
        }
        throw new MissingResourceException("Can't find bundle for base name " + str + ", locale " + ((Object) locale), str + "_" + ((Object) locale), "", th);
    }

    private static ResourceBundle findBundleInCache(CacheKey cacheKey, Control control) {
        BundleReference bundleReference = cacheList.get(cacheKey);
        if (bundleReference == null) {
            return null;
        }
        ResourceBundle resourceBundle = bundleReference.get();
        if (resourceBundle == null) {
            return null;
        }
        ResourceBundle resourceBundle2 = resourceBundle.parent;
        if (!$assertionsDisabled && resourceBundle2 == NONEXISTENT_BUNDLE) {
            throw new AssertionError();
        }
        if (resourceBundle2 != null && resourceBundle2.expired) {
            if (!$assertionsDisabled && resourceBundle == NONEXISTENT_BUNDLE) {
                throw new AssertionError();
            }
            resourceBundle.expired = true;
            resourceBundle.cacheKey = null;
            cacheList.remove(cacheKey, bundleReference);
            resourceBundle = null;
        } else {
            CacheKey cacheKey2 = bundleReference.getCacheKey();
            long j2 = cacheKey2.expirationTime;
            if (!resourceBundle.expired && j2 >= 0 && j2 <= System.currentTimeMillis()) {
                if (resourceBundle != NONEXISTENT_BUNDLE) {
                    synchronized (resourceBundle) {
                        long j3 = cacheKey2.expirationTime;
                        if (!resourceBundle.expired && j3 >= 0 && j3 <= System.currentTimeMillis()) {
                            try {
                                resourceBundle.expired = control.needsReload(cacheKey2.getName(), cacheKey2.getLocale(), cacheKey2.getFormat(), cacheKey2.getLoader(), resourceBundle, cacheKey2.loadTime);
                            } catch (Exception e2) {
                                cacheKey.setCause(e2);
                            }
                            if (resourceBundle.expired) {
                                resourceBundle.cacheKey = null;
                                cacheList.remove(cacheKey, bundleReference);
                            } else {
                                setExpirationTime(cacheKey2, control);
                            }
                        }
                    }
                } else {
                    cacheList.remove(cacheKey, bundleReference);
                    resourceBundle = null;
                }
            }
        }
        return resourceBundle;
    }

    private static ResourceBundle putBundleInCache(CacheKey cacheKey, ResourceBundle resourceBundle, Control control) {
        setExpirationTime(cacheKey, control);
        if (cacheKey.expirationTime != -1) {
            CacheKey cacheKey2 = (CacheKey) cacheKey.clone();
            BundleReference bundleReference = new BundleReference(resourceBundle, referenceQueue, cacheKey2);
            resourceBundle.cacheKey = cacheKey2;
            BundleReference bundleReferencePutIfAbsent = cacheList.putIfAbsent(cacheKey2, bundleReference);
            if (bundleReferencePutIfAbsent != null) {
                ResourceBundle resourceBundle2 = bundleReferencePutIfAbsent.get();
                if (resourceBundle2 != null && !resourceBundle2.expired) {
                    resourceBundle.cacheKey = null;
                    resourceBundle = resourceBundle2;
                    bundleReference.clear();
                } else {
                    cacheList.put(cacheKey2, bundleReference);
                }
            }
        }
        return resourceBundle;
    }

    /* JADX WARN: Failed to check method for inline after forced processjava.util.ResourceBundle.CacheKey.access$602(java.util.ResourceBundle$CacheKey, long):long */
    /* JADX WARN: Failed to check method for inline after forced processjava.util.ResourceBundle.CacheKey.access$702(java.util.ResourceBundle$CacheKey, long):long */
    private static void setExpirationTime(CacheKey cacheKey, Control control) {
        long timeToLive = control.getTimeToLive(cacheKey.getName(), cacheKey.getLocale());
        if (timeToLive >= 0) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            CacheKey.access$702(cacheKey, jCurrentTimeMillis);
            CacheKey.access$602(cacheKey, jCurrentTimeMillis + timeToLive);
        } else {
            if (timeToLive >= -2) {
                CacheKey.access$602(cacheKey, timeToLive);
                return;
            }
            throw new IllegalArgumentException("Invalid Control: TTL=" + timeToLive);
        }
    }

    @CallerSensitive
    public static final void clearCache() {
        clearCache(getLoader(Reflection.getCallerClass()));
    }

    public static final void clearCache(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new NullPointerException();
        }
        Set<CacheKey> setKeySet = cacheList.keySet();
        for (CacheKey cacheKey : setKeySet) {
            if (cacheKey.getLoader() == classLoader) {
                setKeySet.remove(cacheKey);
            }
        }
    }

    public boolean containsKey(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        ResourceBundle resourceBundle = this;
        while (true) {
            ResourceBundle resourceBundle2 = resourceBundle;
            if (resourceBundle2 != null) {
                if (!resourceBundle2.handleKeySet().contains(str)) {
                    resourceBundle = resourceBundle2.parent;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public Set<String> keySet() {
        HashSet hashSet = new HashSet();
        ResourceBundle resourceBundle = this;
        while (true) {
            ResourceBundle resourceBundle2 = resourceBundle;
            if (resourceBundle2 != null) {
                hashSet.addAll(resourceBundle2.handleKeySet());
                resourceBundle = resourceBundle2.parent;
            } else {
                return hashSet;
            }
        }
    }

    protected Set<String> handleKeySet() {
        if (this.keySet == null) {
            synchronized (this) {
                if (this.keySet == null) {
                    HashSet hashSet = new HashSet();
                    Enumeration<String> keys = getKeys();
                    while (keys.hasMoreElements()) {
                        String strNextElement = keys.nextElement2();
                        if (handleGetObject(strNextElement) != null) {
                            hashSet.add(strNextElement);
                        }
                    }
                    this.keySet = hashSet;
                }
            }
        }
        return this.keySet;
    }

    /* loaded from: rt.jar:java/util/ResourceBundle$Control.class */
    public static class Control {
        public static final long TTL_DONT_CACHE = -1;
        public static final long TTL_NO_EXPIRATION_CONTROL = -2;
        public static final List<String> FORMAT_DEFAULT = Collections.unmodifiableList(Arrays.asList("java.class", "java.properties"));
        public static final List<String> FORMAT_CLASS = Collections.unmodifiableList(Arrays.asList("java.class"));
        public static final List<String> FORMAT_PROPERTIES = Collections.unmodifiableList(Arrays.asList("java.properties"));
        private static final Control INSTANCE = new Control();
        private static final CandidateListCache CANDIDATES_CACHE = new CandidateListCache(null);

        static {
        }

        protected Control() {
        }

        public static final Control getControl(List<String> list) {
            if (!list.equals(FORMAT_PROPERTIES)) {
                if (!list.equals(FORMAT_CLASS)) {
                    if (list.equals(FORMAT_DEFAULT)) {
                        return INSTANCE;
                    }
                    throw new IllegalArgumentException();
                }
                return SingleFormatControl.CLASS_ONLY;
            }
            return SingleFormatControl.PROPERTIES_ONLY;
        }

        public static final Control getNoFallbackControl(List<String> list) {
            if (!list.equals(FORMAT_DEFAULT)) {
                if (!list.equals(FORMAT_PROPERTIES)) {
                    if (!list.equals(FORMAT_CLASS)) {
                        throw new IllegalArgumentException();
                    }
                    return NoFallbackControl.CLASS_ONLY_NO_FALLBACK;
                }
                return NoFallbackControl.PROPERTIES_ONLY_NO_FALLBACK;
            }
            return NoFallbackControl.NO_FALLBACK;
        }

        public List<String> getFormats(String str) {
            if (str == null) {
                throw new NullPointerException();
            }
            return FORMAT_DEFAULT;
        }

        public List<Locale> getCandidateLocales(String str, Locale locale) {
            if (str == null) {
                throw new NullPointerException();
            }
            return new ArrayList(CANDIDATES_CACHE.get(locale.getBaseLocale()));
        }

        /* loaded from: rt.jar:java/util/ResourceBundle$Control$CandidateListCache.class */
        private static class CandidateListCache extends LocaleObjectCache<BaseLocale, List<Locale>> {
            private CandidateListCache() {
            }

            @Override // sun.util.locale.LocaleObjectCache
            protected /* bridge */ /* synthetic */ List<Locale> createObject(BaseLocale baseLocale) {
                return createObject2(baseLocale);
            }

            /* synthetic */ CandidateListCache(AnonymousClass1 anonymousClass1) {
                this();
            }

            /* renamed from: createObject, reason: avoid collision after fix types in other method */
            protected List<Locale> createObject2(BaseLocale baseLocale) {
                String language = baseLocale.getLanguage();
                String script = baseLocale.getScript();
                String region = baseLocale.getRegion();
                String variant = baseLocale.getVariant();
                boolean z2 = false;
                boolean z3 = false;
                if (language.equals("no")) {
                    if (region.equals("NO") && variant.equals("NY")) {
                        variant = "";
                        z3 = true;
                    } else {
                        z2 = true;
                    }
                }
                if (language.equals("nb") || z2) {
                    List<Locale> defaultList = getDefaultList("nb", script, region, variant);
                    LinkedList linkedList = new LinkedList();
                    for (Locale locale : defaultList) {
                        linkedList.add(locale);
                        if (locale.getLanguage().length() == 0) {
                            break;
                        }
                        linkedList.add(Locale.getInstance("no", locale.getScript(), locale.getCountry(), locale.getVariant(), null));
                    }
                    return linkedList;
                }
                if (language.equals("nn") || z3) {
                    List<Locale> defaultList2 = getDefaultList("nn", script, region, variant);
                    int size = defaultList2.size() - 1;
                    int i2 = size + 1;
                    defaultList2.add(size, Locale.getInstance("no", "NO", "NY"));
                    int i3 = i2 + 1;
                    defaultList2.add(i2, Locale.getInstance("no", "NO", ""));
                    int i4 = i3 + 1;
                    defaultList2.add(i3, Locale.getInstance("no", "", ""));
                    return defaultList2;
                }
                if (language.equals("zh")) {
                    if (script.length() == 0 && region.length() > 0) {
                        switch (region) {
                            case "TW":
                            case "HK":
                            case "MO":
                                script = "Hant";
                                break;
                            case "CN":
                            case "SG":
                                script = "Hans";
                                break;
                        }
                    } else if (script.length() > 0 && region.length() == 0) {
                        switch (script) {
                            case "Hans":
                                region = "CN";
                                break;
                            case "Hant":
                                region = "TW";
                                break;
                        }
                    }
                }
                return getDefaultList(language, script, region, variant);
            }

            private static List<Locale> getDefaultList(String str, String str2, String str3, String str4) {
                LinkedList linkedList = null;
                if (str4.length() > 0) {
                    linkedList = new LinkedList();
                    int length = str4.length();
                    while (true) {
                        int i2 = length;
                        if (i2 == -1) {
                            break;
                        }
                        linkedList.add(str4.substring(0, i2));
                        length = str4.lastIndexOf(95, i2 - 1);
                    }
                }
                LinkedList linkedList2 = new LinkedList();
                if (linkedList != null) {
                    Iterator it = linkedList.iterator();
                    while (it.hasNext()) {
                        linkedList2.add(Locale.getInstance(str, str2, str3, (String) it.next(), null));
                    }
                }
                if (str3.length() > 0) {
                    linkedList2.add(Locale.getInstance(str, str2, str3, "", null));
                }
                if (str2.length() > 0) {
                    linkedList2.add(Locale.getInstance(str, str2, "", "", null));
                    if (linkedList != null) {
                        Iterator it2 = linkedList.iterator();
                        while (it2.hasNext()) {
                            linkedList2.add(Locale.getInstance(str, "", str3, (String) it2.next(), null));
                        }
                    }
                    if (str3.length() > 0) {
                        linkedList2.add(Locale.getInstance(str, "", str3, "", null));
                    }
                }
                if (str.length() > 0) {
                    linkedList2.add(Locale.getInstance(str, "", "", "", null));
                }
                linkedList2.add(Locale.ROOT);
                return linkedList2;
            }
        }

        public Locale getFallbackLocale(String str, Locale locale) {
            if (str == null) {
                throw new NullPointerException();
            }
            Locale locale2 = Locale.getDefault();
            if (locale.equals(locale2)) {
                return null;
            }
            return locale2;
        }

        public ResourceBundle newBundle(String str, Locale locale, String str2, final ClassLoader classLoader, final boolean z2) throws IllegalAccessException, InstantiationException, IOException {
            String bundleName = toBundleName(str, locale);
            ResourceBundle propertyResourceBundle = null;
            if (str2.equals("java.class")) {
                try {
                    Class<?> clsLoadClass = classLoader.loadClass(bundleName);
                    if (ResourceBundle.class.isAssignableFrom(clsLoadClass)) {
                        propertyResourceBundle = (ResourceBundle) clsLoadClass.newInstance();
                    } else {
                        throw new ClassCastException(clsLoadClass.getName() + " cannot be cast to ResourceBundle");
                    }
                } catch (ClassNotFoundException e2) {
                }
            } else if (str2.equals("java.properties")) {
                final String resourceName0 = toResourceName0(bundleName, "properties");
                if (resourceName0 == null) {
                    return null;
                }
                try {
                    InputStream inputStream = (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>(this) { // from class: java.util.ResourceBundle.Control.1
                        final /* synthetic */ Control this$0;

                        {
                            this.this$0 = this;
                        }

                        @Override // java.security.PrivilegedExceptionAction
                        public /* bridge */ /* synthetic */ InputStream run() throws Exception {
                            return run();
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public InputStream run() throws IOException {
                            URLConnection uRLConnectionOpenConnection;
                            InputStream resourceAsStream = null;
                            if (z2) {
                                URL resource = classLoader.getResource(resourceName0);
                                if (resource != null && (uRLConnectionOpenConnection = resource.openConnection()) != null) {
                                    uRLConnectionOpenConnection.setUseCaches(false);
                                    resourceAsStream = uRLConnectionOpenConnection.getInputStream();
                                }
                            } else {
                                resourceAsStream = classLoader.getResourceAsStream(resourceName0);
                            }
                            return resourceAsStream;
                        }
                    });
                    if (inputStream != null) {
                        try {
                            propertyResourceBundle = new PropertyResourceBundle(inputStream);
                            inputStream.close();
                        } catch (Throwable th) {
                            inputStream.close();
                            throw th;
                        }
                    }
                } catch (PrivilegedActionException e3) {
                    throw ((IOException) e3.getException());
                }
            } else {
                throw new IllegalArgumentException("unknown format: " + str2);
            }
            return propertyResourceBundle;
        }

        public long getTimeToLive(String str, Locale locale) {
            if (str == null || locale == null) {
                throw new NullPointerException();
            }
            return -2L;
        }

        public boolean needsReload(String str, Locale locale, String str2, ClassLoader classLoader, ResourceBundle resourceBundle, long j2) {
            String resourceName0;
            if (resourceBundle == null) {
                throw new NullPointerException();
            }
            if (str2.equals("java.class") || str2.equals("java.properties")) {
                str2 = str2.substring(5);
            }
            boolean z2 = false;
            try {
                resourceName0 = toResourceName0(toBundleName(str, locale), str2);
            } catch (NullPointerException e2) {
                throw e2;
            } catch (Exception e3) {
            }
            if (resourceName0 == null) {
                return false;
            }
            URL resource = classLoader.getResource(resourceName0);
            if (resource != null) {
                long lastModified = 0;
                URLConnection uRLConnectionOpenConnection = resource.openConnection();
                if (uRLConnectionOpenConnection != null) {
                    uRLConnectionOpenConnection.setUseCaches(false);
                    if (uRLConnectionOpenConnection instanceof JarURLConnection) {
                        JarEntry jarEntry = ((JarURLConnection) uRLConnectionOpenConnection).getJarEntry();
                        if (jarEntry != null) {
                            lastModified = jarEntry.getTime();
                            if (lastModified == -1) {
                                lastModified = 0;
                            }
                        }
                    } else {
                        lastModified = uRLConnectionOpenConnection.getLastModified();
                    }
                }
                z2 = lastModified >= j2;
            }
            return z2;
        }

        public String toBundleName(String str, Locale locale) {
            if (locale == Locale.ROOT) {
                return str;
            }
            String language = locale.getLanguage();
            String script = locale.getScript();
            String country = locale.getCountry();
            String variant = locale.getVariant();
            if (language == "" && country == "" && variant == "") {
                return str;
            }
            StringBuilder sb = new StringBuilder(str);
            sb.append('_');
            if (script != "") {
                if (variant != "") {
                    sb.append(language).append('_').append(script).append('_').append(country).append('_').append(variant);
                } else if (country != "") {
                    sb.append(language).append('_').append(script).append('_').append(country);
                } else {
                    sb.append(language).append('_').append(script);
                }
            } else if (variant != "") {
                sb.append(language).append('_').append(country).append('_').append(variant);
            } else if (country != "") {
                sb.append(language).append('_').append(country);
            } else {
                sb.append(language);
            }
            return sb.toString();
        }

        public final String toResourceName(String str, String str2) {
            StringBuilder sb = new StringBuilder(str.length() + 1 + str2.length());
            sb.append(str.replace('.', '/')).append('.').append(str2);
            return sb.toString();
        }

        private String toResourceName0(String str, String str2) {
            if (str.contains("://")) {
                return null;
            }
            return toResourceName(str, str2);
        }
    }

    /* loaded from: rt.jar:java/util/ResourceBundle$SingleFormatControl.class */
    private static class SingleFormatControl extends Control {
        private static final Control PROPERTIES_ONLY = new SingleFormatControl(FORMAT_PROPERTIES);
        private static final Control CLASS_ONLY = new SingleFormatControl(FORMAT_CLASS);
        private final List<String> formats;

        static {
        }

        protected SingleFormatControl(List<String> list) {
            this.formats = list;
        }

        @Override // java.util.ResourceBundle.Control
        public List<String> getFormats(String str) {
            if (str == null) {
                throw new NullPointerException();
            }
            return this.formats;
        }
    }

    /* loaded from: rt.jar:java/util/ResourceBundle$NoFallbackControl.class */
    private static final class NoFallbackControl extends SingleFormatControl {
        private static final Control NO_FALLBACK = new NoFallbackControl(FORMAT_DEFAULT);
        private static final Control PROPERTIES_ONLY_NO_FALLBACK = new NoFallbackControl(FORMAT_PROPERTIES);
        private static final Control CLASS_ONLY_NO_FALLBACK = new NoFallbackControl(FORMAT_CLASS);

        static {
        }

        protected NoFallbackControl(List<String> list) {
            super(list);
        }

        @Override // java.util.ResourceBundle.Control
        public Locale getFallbackLocale(String str, Locale locale) {
            if (str == null || locale == null) {
                throw new NullPointerException();
            }
            return null;
        }
    }
}
