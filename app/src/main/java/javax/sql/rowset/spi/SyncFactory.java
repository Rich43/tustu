package javax.sql.rowset.spi;

import com.sun.rowset.providers.RIOptimisticProvider;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.sql.SQLPermission;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.PropertyPermission;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/sql/rowset/spi/SyncFactory.class */
public class SyncFactory {
    public static final String ROWSET_SYNC_PROVIDER = "rowset.provider.classname";
    public static final String ROWSET_SYNC_VENDOR = "rowset.provider.vendor";
    public static final String ROWSET_SYNC_PROVIDER_VERSION = "rowset.provider.version";
    private static Context ic;
    private static volatile Logger rsLogger;
    private static Hashtable<String, SyncProvider> implementations;
    private static String ROWSET_PROPERTIES = "rowset.properties";
    private static final SQLPermission SET_SYNCFACTORY_PERMISSION = new SQLPermission("setSyncFactory");
    private static String colon = CallSiteDescriptor.TOKEN_DELIMITER;
    private static String strFileSep = "/";
    private static boolean debug = false;
    private static int providerImplIndex = 0;
    private static boolean lazyJNDICtxRefresh = false;

    private SyncFactory() {
    }

    public static synchronized void registerProvider(String str) throws SyncFactoryException {
        ProviderImpl providerImpl = new ProviderImpl();
        providerImpl.setClassname(str);
        initMapIfNecessary();
        implementations.put(str, providerImpl);
    }

    public static SyncFactory getSyncFactory() {
        return SyncFactoryHolder.factory;
    }

    public static synchronized void unregisterProvider(String str) throws SyncFactoryException {
        initMapIfNecessary();
        if (implementations.containsKey(str)) {
            implementations.remove(str);
        }
    }

    private static synchronized void initMapIfNecessary() throws SyncFactoryException {
        String str;
        String str2;
        Properties properties = new Properties();
        if (implementations == null) {
            implementations = new Hashtable<>();
            try {
                try {
                    str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.sql.rowset.spi.SyncFactory.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public String run() {
                            return System.getProperty("rowset.properties");
                        }
                    }, (AccessControlContext) null, new PropertyPermission("rowset.properties", "read"));
                } catch (FileNotFoundException e2) {
                    throw new SyncFactoryException("Cannot locate properties file: " + ((Object) e2));
                } catch (IOException e3) {
                    throw new SyncFactoryException("IOException: " + ((Object) e3));
                }
            } catch (Exception e4) {
                System.out.println("errorget rowset.properties: " + ((Object) e4));
                str = null;
            }
            if (str != null) {
                ROWSET_PROPERTIES = str;
                FileInputStream fileInputStream = new FileInputStream(ROWSET_PROPERTIES);
                Throwable th = null;
                try {
                    try {
                        properties.load(fileInputStream);
                        if (fileInputStream != null) {
                            if (0 != 0) {
                                try {
                                    fileInputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                fileInputStream.close();
                            }
                        }
                        parseProperties(properties);
                    } catch (Throwable th3) {
                        if (fileInputStream != null) {
                            if (th != null) {
                                try {
                                    fileInputStream.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                fileInputStream.close();
                            }
                        }
                        throw th3;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    throw th5;
                }
            }
            ROWSET_PROPERTIES = "javax" + strFileSep + "sql" + strFileSep + "rowset" + strFileSep + "rowset.properties";
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                AccessController.doPrivileged(() -> {
                    InputStream resourceAsStream;
                    if (contextClassLoader == null) {
                        resourceAsStream = ClassLoader.getSystemResourceAsStream(ROWSET_PROPERTIES);
                    } else {
                        resourceAsStream = contextClassLoader.getResourceAsStream(ROWSET_PROPERTIES);
                    }
                    InputStream inputStream = resourceAsStream;
                    Throwable th6 = null;
                    try {
                        if (inputStream == null) {
                            throw new SyncFactoryException("Resource " + ROWSET_PROPERTIES + " not found");
                        }
                        properties.load(inputStream);
                        if (inputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                    return null;
                                } catch (Throwable th7) {
                                    th6.addSuppressed(th7);
                                    return null;
                                }
                            }
                            inputStream.close();
                            return null;
                        }
                        return null;
                    } catch (Throwable th8) {
                        if (inputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th9) {
                                    th6.addSuppressed(th9);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        throw th8;
                    }
                });
                parseProperties(properties);
                properties.clear();
                try {
                    str2 = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.sql.rowset.spi.SyncFactory.2
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public String run() {
                            return System.getProperty(SyncFactory.ROWSET_SYNC_PROVIDER);
                        }
                    }, (AccessControlContext) null, new PropertyPermission(ROWSET_SYNC_PROVIDER, "read"));
                } catch (Exception e5) {
                    str2 = null;
                }
                if (str2 != null) {
                    int i2 = 0;
                    if (str2.indexOf(colon) > 0) {
                        StringTokenizer stringTokenizer = new StringTokenizer(str2, colon);
                        while (stringTokenizer.hasMoreElements()) {
                            properties.put("rowset.provider.classname." + i2, stringTokenizer.nextToken());
                            i2++;
                        }
                    } else {
                        properties.put(ROWSET_SYNC_PROVIDER, str2);
                    }
                    parseProperties(properties);
                }
            } catch (PrivilegedActionException e6) {
                Exception exception = e6.getException();
                if (exception instanceof SyncFactoryException) {
                    throw ((SyncFactoryException) exception);
                }
                SyncFactoryException syncFactoryException = new SyncFactoryException();
                syncFactoryException.initCause(e6.getException());
                throw syncFactoryException;
            }
        }
    }

    private static void parseProperties(Properties properties) {
        String[] propertyNames;
        Enumeration<?> enumerationPropertyNames = properties.propertyNames();
        while (enumerationPropertyNames.hasMoreElements()) {
            String str = (String) enumerationPropertyNames.nextElement2();
            int length = str.length();
            if (str.startsWith(ROWSET_SYNC_PROVIDER)) {
                ProviderImpl providerImpl = new ProviderImpl();
                int i2 = providerImplIndex;
                providerImplIndex = i2 + 1;
                providerImpl.setIndex(i2);
                if (length == ROWSET_SYNC_PROVIDER.length()) {
                    propertyNames = getPropertyNames(false);
                } else {
                    propertyNames = getPropertyNames(true, str.substring(length - 1));
                }
                String[] strArr = propertyNames;
                String property = properties.getProperty(strArr[0]);
                providerImpl.setClassname(property);
                providerImpl.setVendor(properties.getProperty(strArr[1]));
                providerImpl.setVersion(properties.getProperty(strArr[2]));
                implementations.put(property, providerImpl);
            }
        }
    }

    private static String[] getPropertyNames(boolean z2) {
        return getPropertyNames(z2, null);
    }

    private static String[] getPropertyNames(boolean z2, String str) {
        String[] strArr = new String[3];
        strArr[0] = ROWSET_SYNC_PROVIDER;
        strArr[1] = ROWSET_SYNC_VENDOR;
        strArr[2] = ROWSET_SYNC_PROVIDER_VERSION;
        if (z2) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                strArr[i2] = strArr[i2] + "." + str;
            }
            return strArr;
        }
        return strArr;
    }

    private static void showImpl(ProviderImpl providerImpl) {
        System.out.println("Provider implementation:");
        System.out.println("Classname: " + providerImpl.getClassname());
        System.out.println("Vendor: " + providerImpl.getVendor());
        System.out.println("Version: " + providerImpl.getVersion());
        System.out.println("Impl index: " + providerImpl.getIndex());
    }

    public static SyncProvider getInstance(String str) throws SyncFactoryException {
        if (str == null) {
            throw new SyncFactoryException("The providerID cannot be null");
        }
        initMapIfNecessary();
        initJNDIContext();
        if (((ProviderImpl) implementations.get(str)) == null) {
            return new RIOptimisticProvider();
        }
        try {
            ReflectUtil.checkPackageAccess(str);
            try {
                Class<?> cls = Class.forName(str, true, Thread.currentThread().getContextClassLoader());
                if (cls != null) {
                    return (SyncProvider) cls.newInstance();
                }
                return new RIOptimisticProvider();
            } catch (ClassNotFoundException e2) {
                throw new SyncFactoryException("ClassNotFoundException: " + e2.getMessage());
            } catch (IllegalAccessException e3) {
                throw new SyncFactoryException("IllegalAccessException: " + e3.getMessage());
            } catch (InstantiationException e4) {
                throw new SyncFactoryException("InstantiationException: " + e4.getMessage());
            }
        } catch (AccessControlException e5) {
            SyncFactoryException syncFactoryException = new SyncFactoryException();
            syncFactoryException.initCause(e5);
            throw syncFactoryException;
        }
    }

    public static Enumeration<SyncProvider> getRegisteredProviders() throws SyncFactoryException {
        initMapIfNecessary();
        return implementations.elements();
    }

    public static void setLogger(Logger logger) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SET_SYNCFACTORY_PERMISSION);
        }
        if (logger == null) {
            throw new NullPointerException("You must provide a Logger");
        }
        rsLogger = logger;
    }

    public static void setLogger(Logger logger, Level level) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SET_SYNCFACTORY_PERMISSION);
        }
        if (logger == null) {
            throw new NullPointerException("You must provide a Logger");
        }
        logger.setLevel(level);
        rsLogger = logger;
    }

    public static Logger getLogger() throws SyncFactoryException {
        Logger logger = rsLogger;
        if (logger == null) {
            throw new SyncFactoryException("(SyncFactory) : No logger has been set");
        }
        return logger;
    }

    public static synchronized void setJNDIContext(Context context) throws SyncFactoryException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SET_SYNCFACTORY_PERMISSION);
        }
        if (context == null) {
            throw new SyncFactoryException("Invalid JNDI context supplied");
        }
        ic = context;
    }

    private static synchronized void initJNDIContext() throws SyncFactoryException {
        if (ic != null && !lazyJNDICtxRefresh) {
            try {
                parseProperties(parseJNDIContext());
                lazyJNDICtxRefresh = true;
            } catch (NamingException e2) {
                e2.printStackTrace();
                throw new SyncFactoryException("SPI: NamingException: " + e2.getExplanation());
            } catch (Exception e3) {
                e3.printStackTrace();
                throw new SyncFactoryException("SPI: Exception: " + e3.getMessage());
            }
        }
    }

    private static Properties parseJNDIContext() throws NamingException {
        NamingEnumeration<Binding> namingEnumerationListBindings = ic.listBindings("");
        Properties properties = new Properties();
        enumerateBindings(namingEnumerationListBindings, properties);
        return properties;
    }

    private static void enumerateBindings(NamingEnumeration<?> namingEnumeration, Properties properties) throws NamingException {
        boolean z2 = false;
        while (namingEnumeration.hasMore()) {
            try {
                Binding binding = (Binding) namingEnumeration.next();
                String name = binding.getName();
                Object object = binding.getObject();
                if (!(ic.lookup(name) instanceof Context) && (ic.lookup(name) instanceof SyncProvider)) {
                    z2 = true;
                }
                if (z2) {
                    properties.put(ROWSET_SYNC_PROVIDER, ((SyncProvider) object).getProviderID());
                    z2 = false;
                }
            } catch (NotContextException e2) {
                namingEnumeration.next();
                enumerateBindings(namingEnumeration, properties);
                return;
            }
        }
    }

    /* loaded from: rt.jar:javax/sql/rowset/spi/SyncFactory$SyncFactoryHolder.class */
    private static class SyncFactoryHolder {
        static final SyncFactory factory = new SyncFactory();

        private SyncFactoryHolder() {
        }
    }
}
