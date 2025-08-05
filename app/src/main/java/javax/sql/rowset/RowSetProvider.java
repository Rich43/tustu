package javax.sql.rowset;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.PropertyPermission;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/sql/rowset/RowSetProvider.class */
public class RowSetProvider {
    private static final String ROWSET_DEBUG_PROPERTY = "javax.sql.rowset.RowSetProvider.debug";
    private static final String ROWSET_FACTORY_IMPL = "com.sun.rowset.RowSetFactoryImpl";
    private static final String ROWSET_FACTORY_NAME = "javax.sql.rowset.RowSetFactory";
    private static boolean debug;

    static {
        debug = true;
        String systemProperty = getSystemProperty(ROWSET_DEBUG_PROPERTY);
        debug = (systemProperty == null || "false".equals(systemProperty)) ? false : true;
    }

    protected RowSetProvider() {
    }

    public static RowSetFactory newFactory() throws SQLException {
        RowSetFactory rowSetFactoryNewFactory = null;
        String systemProperty = null;
        try {
            trace("Checking for Rowset System Property...");
            systemProperty = getSystemProperty(ROWSET_FACTORY_NAME);
            if (systemProperty != null) {
                trace("Found system property, value=" + systemProperty);
                rowSetFactoryNewFactory = (RowSetFactory) ReflectUtil.newInstance(getFactoryClass(systemProperty, null, true));
            }
            if (rowSetFactoryNewFactory == null) {
                RowSetFactory rowSetFactoryLoadViaServiceLoader = loadViaServiceLoader();
                rowSetFactoryNewFactory = rowSetFactoryLoadViaServiceLoader == null ? newFactory(ROWSET_FACTORY_IMPL, null) : rowSetFactoryLoadViaServiceLoader;
            }
            return rowSetFactoryNewFactory;
        } catch (Exception e2) {
            throw new SQLException("RowSetFactory: " + systemProperty + " could not be instantiated: ", e2);
        }
    }

    public static RowSetFactory newFactory(String str, ClassLoader classLoader) throws SQLException {
        trace("***In newInstance()");
        if (str == null) {
            throw new SQLException("Error: factoryClassName cannot be null");
        }
        try {
            ReflectUtil.checkPackageAccess(str);
            try {
                Class<?> factoryClass = getFactoryClass(str, classLoader, false);
                RowSetFactory rowSetFactory = (RowSetFactory) factoryClass.newInstance();
                if (debug) {
                    trace("Created new instance of " + ((Object) factoryClass) + " using ClassLoader: " + ((Object) classLoader));
                }
                return rowSetFactory;
            } catch (ClassNotFoundException e2) {
                throw new SQLException("Provider " + str + " not found", e2);
            } catch (Exception e3) {
                throw new SQLException("Provider " + str + " could not be instantiated: " + ((Object) e3), e3);
            }
        } catch (AccessControlException e4) {
            throw new SQLException("Access Exception", e4);
        }
    }

    private static ClassLoader getContextClassLoader() throws SecurityException {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: javax.sql.rowset.RowSetProvider.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader == null) {
                    contextClassLoader = ClassLoader.getSystemClassLoader();
                }
                return contextClassLoader;
            }
        });
    }

    private static Class<?> getFactoryClass(String str, ClassLoader classLoader, boolean z2) throws SecurityException, ClassNotFoundException {
        try {
            if (classLoader == null) {
                ClassLoader contextClassLoader = getContextClassLoader();
                if (contextClassLoader == null) {
                    throw new ClassNotFoundException();
                }
                return contextClassLoader.loadClass(str);
            }
            return classLoader.loadClass(str);
        } catch (ClassNotFoundException e2) {
            if (z2) {
                return Class.forName(str, true, RowSetFactory.class.getClassLoader());
            }
            throw e2;
        }
    }

    private static RowSetFactory loadViaServiceLoader() throws SQLException {
        RowSetFactory rowSetFactory = null;
        try {
            trace("***in loadViaServiceLoader():");
            Iterator it = ServiceLoader.load(RowSetFactory.class).iterator();
            if (it.hasNext()) {
                RowSetFactory rowSetFactory2 = (RowSetFactory) it.next();
                trace(" Loading done by the java.util.ServiceLoader :" + rowSetFactory2.getClass().getName());
                rowSetFactory = rowSetFactory2;
            }
            return rowSetFactory;
        } catch (ServiceConfigurationError e2) {
            throw new SQLException("RowSetFactory: Error locating RowSetFactory using Service Loader API: " + ((Object) e2), e2);
        }
    }

    private static String getSystemProperty(final String str) {
        String str2 = null;
        try {
            str2 = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.sql.rowset.RowSetProvider.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public String run2() {
                    return System.getProperty(str);
                }
            }, (AccessControlContext) null, new PropertyPermission(str, "read"));
        } catch (SecurityException e2) {
            trace("error getting " + str + ":  " + ((Object) e2));
            if (debug) {
                e2.printStackTrace();
            }
        }
        return str2;
    }

    private static void trace(String str) {
        if (debug) {
            System.err.println("###RowSets: " + str);
        }
    }
}
