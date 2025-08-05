package com.sun.jmx.mbeanserver;

import com.sun.jmx.defaults.JmxProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.management.MBeanPermission;
import javax.management.ObjectName;
import javax.management.loading.PrivateClassLoader;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/ClassLoaderRepositorySupport.class */
final class ClassLoaderRepositorySupport implements ModifiableClassLoaderRepository {
    private static final LoaderEntry[] EMPTY_LOADER_ARRAY = new LoaderEntry[0];
    private LoaderEntry[] loaders = EMPTY_LOADER_ARRAY;
    private final Map<String, List<ClassLoader>> search = new Hashtable(10);
    private final Map<ObjectName, ClassLoader> loadersWithNames = new Hashtable(10);

    ClassLoaderRepositorySupport() {
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/ClassLoaderRepositorySupport$LoaderEntry.class */
    private static class LoaderEntry {
        ObjectName name;
        ClassLoader loader;

        LoaderEntry(ObjectName objectName, ClassLoader classLoader) {
            this.name = objectName;
            this.loader = classLoader;
        }
    }

    private synchronized boolean add(ObjectName objectName, ClassLoader classLoader) {
        ArrayList arrayList = new ArrayList(Arrays.asList(this.loaders));
        arrayList.add(new LoaderEntry(objectName, classLoader));
        this.loaders = (LoaderEntry[]) arrayList.toArray(EMPTY_LOADER_ARRAY);
        return true;
    }

    private synchronized boolean remove(ObjectName objectName, ClassLoader classLoader) {
        boolean zEquals;
        int length = this.loaders.length;
        for (int i2 = 0; i2 < length; i2++) {
            LoaderEntry loaderEntry = this.loaders[i2];
            if (objectName == null) {
                zEquals = classLoader == loaderEntry.loader;
            } else {
                zEquals = objectName.equals(loaderEntry.name);
            }
            if (zEquals) {
                LoaderEntry[] loaderEntryArr = new LoaderEntry[length - 1];
                System.arraycopy(this.loaders, 0, loaderEntryArr, 0, i2);
                System.arraycopy(this.loaders, i2 + 1, loaderEntryArr, i2, (length - 1) - i2);
                this.loaders = loaderEntryArr;
                return true;
            }
        }
        return false;
    }

    @Override // javax.management.loading.ClassLoaderRepository
    public final Class<?> loadClass(String str) throws ClassNotFoundException {
        return loadClass(this.loaders, str, null, null);
    }

    @Override // javax.management.loading.ClassLoaderRepository
    public final Class<?> loadClassWithout(ClassLoader classLoader, String str) throws ClassNotFoundException {
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "loadClassWithout", str + " without " + ((Object) classLoader));
        }
        if (classLoader == null) {
            return loadClass(this.loaders, str, null, null);
        }
        startValidSearch(classLoader, str);
        try {
            Class<?> clsLoadClass = loadClass(this.loaders, str, classLoader, null);
            stopValidSearch(classLoader, str);
            return clsLoadClass;
        } catch (Throwable th) {
            stopValidSearch(classLoader, str);
            throw th;
        }
    }

    @Override // javax.management.loading.ClassLoaderRepository
    public final Class<?> loadClassBefore(ClassLoader classLoader, String str) throws ClassNotFoundException {
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "loadClassBefore", str + " before " + ((Object) classLoader));
        }
        if (classLoader == null) {
            return loadClass(this.loaders, str, null, null);
        }
        startValidSearch(classLoader, str);
        try {
            Class<?> clsLoadClass = loadClass(this.loaders, str, null, classLoader);
            stopValidSearch(classLoader, str);
            return clsLoadClass;
        } catch (Throwable th) {
            stopValidSearch(classLoader, str);
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0082, code lost:
    
        throw new java.lang.ClassNotFoundException(r9);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Class<?> loadClass(com.sun.jmx.mbeanserver.ClassLoaderRepositorySupport.LoaderEntry[] r8, java.lang.String r9, java.lang.ClassLoader r10, java.lang.ClassLoader r11) throws java.lang.ClassNotFoundException {
        /*
            r7 = this;
            r0 = r9
            sun.reflect.misc.ReflectUtil.checkPackageAccess(r0)
            r0 = r8
            int r0 = r0.length
            r12 = r0
            r0 = 0
            r13 = r0
        Lb:
            r0 = r13
            r1 = r12
            if (r0 >= r1) goto L7a
            r0 = r8
            r1 = r13
            r0 = r0[r1]     // Catch: java.lang.ClassNotFoundException -> L72
            java.lang.ClassLoader r0 = r0.loader     // Catch: java.lang.ClassNotFoundException -> L72
            r14 = r0
            r0 = r14
            if (r0 != 0) goto L27
            r0 = r9
            r1 = 0
            r2 = 0
            java.lang.Class r0 = java.lang.Class.forName(r0, r1, r2)     // Catch: java.lang.ClassNotFoundException -> L72
            return r0
        L27:
            r0 = r14
            r1 = r10
            if (r0 != r1) goto L30
            goto L74
        L30:
            r0 = r14
            r1 = r11
            if (r0 != r1) goto L3a
            goto L7a
        L3a:
            java.util.logging.Logger r0 = com.sun.jmx.defaults.JmxProperties.MBEANSERVER_LOGGER     // Catch: java.lang.ClassNotFoundException -> L72
            java.util.logging.Level r1 = java.util.logging.Level.FINER     // Catch: java.lang.ClassNotFoundException -> L72
            boolean r0 = r0.isLoggable(r1)     // Catch: java.lang.ClassNotFoundException -> L72
            if (r0 == 0) goto L6a
            java.util.logging.Logger r0 = com.sun.jmx.defaults.JmxProperties.MBEANSERVER_LOGGER     // Catch: java.lang.ClassNotFoundException -> L72
            java.util.logging.Level r1 = java.util.logging.Level.FINER     // Catch: java.lang.ClassNotFoundException -> L72
            java.lang.Class<com.sun.jmx.mbeanserver.ClassLoaderRepositorySupport> r2 = com.sun.jmx.mbeanserver.ClassLoaderRepositorySupport.class
            java.lang.String r2 = r2.getName()     // Catch: java.lang.ClassNotFoundException -> L72
            java.lang.String r3 = "loadClass"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.ClassNotFoundException -> L72
            r5 = r4
            r5.<init>()     // Catch: java.lang.ClassNotFoundException -> L72
            java.lang.String r5 = "Trying loader = "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.ClassNotFoundException -> L72
            r5 = r14
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.ClassNotFoundException -> L72
            java.lang.String r4 = r4.toString()     // Catch: java.lang.ClassNotFoundException -> L72
            r0.logp(r1, r2, r3, r4)     // Catch: java.lang.ClassNotFoundException -> L72
        L6a:
            r0 = r9
            r1 = 0
            r2 = r14
            java.lang.Class r0 = java.lang.Class.forName(r0, r1, r2)     // Catch: java.lang.ClassNotFoundException -> L72
            return r0
        L72:
            r14 = move-exception
        L74:
            int r13 = r13 + 1
            goto Lb
        L7a:
            java.lang.ClassNotFoundException r0 = new java.lang.ClassNotFoundException
            r1 = r0
            r2 = r9
            r1.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jmx.mbeanserver.ClassLoaderRepositorySupport.loadClass(com.sun.jmx.mbeanserver.ClassLoaderRepositorySupport$LoaderEntry[], java.lang.String, java.lang.ClassLoader, java.lang.ClassLoader):java.lang.Class");
    }

    private synchronized void startValidSearch(ClassLoader classLoader, String str) throws ClassNotFoundException {
        List<ClassLoader> arrayList = this.search.get(str);
        if (arrayList != null && arrayList.contains(classLoader)) {
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "startValidSearch", "Already requested loader = " + ((Object) classLoader) + " class = " + str);
            }
            throw new ClassNotFoundException(str);
        }
        if (arrayList == null) {
            arrayList = new ArrayList(1);
            this.search.put(str, arrayList);
        }
        arrayList.add(classLoader);
        if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "startValidSearch", "loader = " + ((Object) classLoader) + " class = " + str);
        }
    }

    private synchronized void stopValidSearch(ClassLoader classLoader, String str) {
        List<ClassLoader> list = this.search.get(str);
        if (list != null) {
            list.remove(classLoader);
            if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "stopValidSearch", "loader = " + ((Object) classLoader) + " class = " + str);
            }
        }
    }

    @Override // com.sun.jmx.mbeanserver.ModifiableClassLoaderRepository
    public final void addClassLoader(ClassLoader classLoader) {
        add(null, classLoader);
    }

    @Override // com.sun.jmx.mbeanserver.ModifiableClassLoaderRepository
    public final void removeClassLoader(ClassLoader classLoader) {
        remove(null, classLoader);
    }

    @Override // com.sun.jmx.mbeanserver.ModifiableClassLoaderRepository
    public final synchronized void addClassLoader(ObjectName objectName, ClassLoader classLoader) {
        this.loadersWithNames.put(objectName, classLoader);
        if (!(classLoader instanceof PrivateClassLoader)) {
            add(objectName, classLoader);
        }
    }

    @Override // com.sun.jmx.mbeanserver.ModifiableClassLoaderRepository
    public final synchronized void removeClassLoader(ObjectName objectName) {
        ClassLoader classLoaderRemove = this.loadersWithNames.remove(objectName);
        if (!(classLoaderRemove instanceof PrivateClassLoader)) {
            remove(objectName, classLoaderRemove);
        }
    }

    @Override // com.sun.jmx.mbeanserver.ModifiableClassLoaderRepository
    public final ClassLoader getClassLoader(ObjectName objectName) {
        SecurityManager securityManager;
        ClassLoader classLoader = this.loadersWithNames.get(objectName);
        if (classLoader != null && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPermission(new MBeanPermission(classLoader.getClass().getName(), null, objectName, "getClassLoader"));
        }
        return classLoader;
    }
}
