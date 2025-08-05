package sun.applet;

import java.lang.Thread;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import sun.awt.AWTSecurityManager;
import sun.awt.AppContext;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/applet/AppletSecurity.class */
public class AppletSecurity extends AWTSecurityManager {
    private static Field facc;
    private static Field fcontext;
    private HashSet restrictedPackages = new HashSet();
    private boolean inThreadGroupCheck = false;

    static {
        facc = null;
        fcontext = null;
        try {
            facc = URLClassLoader.class.getDeclaredField("acc");
            facc.setAccessible(true);
            fcontext = AccessControlContext.class.getDeclaredField("context");
            fcontext.setAccessible(true);
        } catch (NoSuchFieldException e2) {
            throw new UnsupportedOperationException(e2);
        }
    }

    public AppletSecurity() {
        reset();
    }

    public void reset() {
        this.restrictedPackages.clear();
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.applet.AppletSecurity.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                String property;
                Enumeration<?> enumerationPropertyNames = System.getProperties().propertyNames();
                while (enumerationPropertyNames.hasMoreElements()) {
                    String str = (String) enumerationPropertyNames.nextElement2();
                    if (str != null && str.startsWith("package.restrict.access.") && (property = System.getProperty(str)) != null && property.equalsIgnoreCase("true")) {
                        AppletSecurity.this.restrictedPackages.add(str.substring(24));
                    }
                }
                return null;
            }
        });
    }

    private AppletClassLoader currentAppletClassLoader() {
        ClassLoader classLoader;
        ClassLoader classLoaderCurrentClassLoader = currentClassLoader();
        if (classLoaderCurrentClassLoader == null || (classLoaderCurrentClassLoader instanceof AppletClassLoader)) {
            return (AppletClassLoader) classLoaderCurrentClassLoader;
        }
        Class[] classContext = getClassContext();
        for (Class cls : classContext) {
            ClassLoader classLoader2 = cls.getClassLoader();
            if (classLoader2 instanceof AppletClassLoader) {
                return (AppletClassLoader) classLoader2;
            }
        }
        for (Class cls2 : classContext) {
            final ClassLoader classLoader3 = cls2.getClassLoader();
            if ((classLoader3 instanceof URLClassLoader) && (classLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.applet.AppletSecurity.2
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    try {
                        AccessControlContext accessControlContext = (AccessControlContext) AppletSecurity.facc.get(classLoader3);
                        if (accessControlContext != null) {
                            ProtectionDomain[] protectionDomainArr = (ProtectionDomain[]) AppletSecurity.fcontext.get(accessControlContext);
                            if (protectionDomainArr == null) {
                                return null;
                            }
                            for (ProtectionDomain protectionDomain : protectionDomainArr) {
                                ClassLoader classLoader4 = protectionDomain.getClassLoader();
                                if (classLoader4 instanceof AppletClassLoader) {
                                    return classLoader4;
                                }
                            }
                            return null;
                        }
                        return null;
                    } catch (Exception e2) {
                        throw new UnsupportedOperationException(e2);
                    }
                }
            })) != null) {
                return (AppletClassLoader) classLoader;
            }
        }
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader instanceof AppletClassLoader) {
            return (AppletClassLoader) contextClassLoader;
        }
        return (AppletClassLoader) null;
    }

    protected boolean inThreadGroup(ThreadGroup threadGroup) {
        if (currentAppletClassLoader() == null) {
            return false;
        }
        return getThreadGroup().parentOf(threadGroup);
    }

    protected boolean inThreadGroup(Thread thread) {
        return inThreadGroup(thread.getThreadGroup());
    }

    @Override // java.lang.SecurityManager
    public void checkAccess(Thread thread) {
        if (thread.getState() != Thread.State.TERMINATED && !inThreadGroup(thread)) {
            checkPermission(SecurityConstants.MODIFY_THREAD_PERMISSION);
        }
    }

    @Override // java.lang.SecurityManager
    public synchronized void checkAccess(ThreadGroup threadGroup) {
        if (this.inThreadGroupCheck) {
            checkPermission(SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
            return;
        }
        try {
            this.inThreadGroupCheck = true;
            if (!inThreadGroup(threadGroup)) {
                checkPermission(SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
            }
        } finally {
            this.inThreadGroupCheck = false;
        }
    }

    @Override // java.lang.SecurityManager
    public void checkPackageAccess(String str) {
        super.checkPackageAccess(str);
        Iterator it = this.restrictedPackages.iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (str.equals(str2) || str.startsWith(str2 + ".")) {
                checkPermission(new RuntimePermission("accessClassInPackage." + str));
            }
        }
    }

    @Override // java.lang.SecurityManager
    public void checkAwtEventQueueAccess() {
        AppContext appContext = AppContext.getAppContext();
        AppletClassLoader appletClassLoaderCurrentAppletClassLoader = currentAppletClassLoader();
        if (AppContext.isMainContext(appContext) && appletClassLoaderCurrentAppletClassLoader != null) {
            super.checkPermission(SecurityConstants.AWT.CHECK_AWT_EVENTQUEUE_PERMISSION);
        }
    }

    @Override // java.lang.SecurityManager
    public ThreadGroup getThreadGroup() {
        AppletClassLoader appletClassLoaderCurrentAppletClassLoader = currentAppletClassLoader();
        ThreadGroup threadGroup = appletClassLoaderCurrentAppletClassLoader == null ? null : appletClassLoaderCurrentAppletClassLoader.getThreadGroup();
        if (threadGroup != null) {
            return threadGroup;
        }
        return super.getThreadGroup();
    }

    @Override // sun.awt.AWTSecurityManager
    public AppContext getAppContext() {
        AppletClassLoader appletClassLoaderCurrentAppletClassLoader = currentAppletClassLoader();
        if (appletClassLoaderCurrentAppletClassLoader == null) {
            return null;
        }
        AppContext appContext = appletClassLoaderCurrentAppletClassLoader.getAppContext();
        if (appContext == null) {
            throw new SecurityException("Applet classloader has invalid AppContext");
        }
        return appContext;
    }
}
