package javax.xml.xpath;

import com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl;
import java.io.File;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/xml/xpath/XPathFactoryFinder.class */
class XPathFactoryFinder {
    private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xpath.internal";
    private static final SecuritySupport ss;
    private static boolean debug;
    private static final Properties cacheProps;
    private static volatile boolean firstTime;
    private final ClassLoader classLoader;
    private static final Class<XPathFactory> SERVICE_CLASS;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XPathFactoryFinder.class.desiredAssertionStatus();
        ss = new SecuritySupport();
        debug = false;
        try {
            debug = ss.getSystemProperty("jaxp.debug") != null;
        } catch (Exception e2) {
            debug = false;
        }
        cacheProps = new Properties();
        firstTime = true;
        SERVICE_CLASS = XPathFactory.class;
    }

    private static void debugPrintln(String msg) {
        if (debug) {
            System.err.println("JAXP: " + msg);
        }
    }

    public XPathFactoryFinder(ClassLoader loader) {
        this.classLoader = loader;
        if (debug) {
            debugDisplayClassLoader();
        }
    }

    private void debugDisplayClassLoader() {
        try {
            if (this.classLoader == ss.getContextClassLoader()) {
                debugPrintln("using thread context class loader (" + ((Object) this.classLoader) + ") for search");
                return;
            }
        } catch (Throwable th) {
        }
        if (this.classLoader == ClassLoader.getSystemClassLoader()) {
            debugPrintln("using system class loader (" + ((Object) this.classLoader) + ") for search");
        } else {
            debugPrintln("using class loader (" + ((Object) this.classLoader) + ") for search");
        }
    }

    public XPathFactory newFactory(String uri) throws XPathFactoryConfigurationException {
        if (uri == null) {
            throw new NullPointerException();
        }
        XPathFactory f2 = _newFactory(uri);
        if (f2 != null) {
            debugPrintln("factory '" + f2.getClass().getName() + "' was found for " + uri);
        } else {
            debugPrintln("unable to find a factory for " + uri);
        }
        return f2;
    }

    private XPathFactory _newFactory(String uri) throws XPathFactoryConfigurationException {
        XPathFactory xpathFactory = null;
        String propertyName = SERVICE_CLASS.getName() + CallSiteDescriptor.TOKEN_DELIMITER + uri;
        try {
            debugPrintln("Looking up system property '" + propertyName + PdfOps.SINGLE_QUOTE_TOKEN);
            String r2 = ss.getSystemProperty(propertyName);
            if (r2 != null) {
                debugPrintln("The value is '" + r2 + PdfOps.SINGLE_QUOTE_TOKEN);
                xpathFactory = createInstance(r2);
                if (xpathFactory != null) {
                    return xpathFactory;
                }
            } else {
                debugPrintln("The property is undefined.");
            }
        } catch (Throwable t2) {
            if (debug) {
                debugPrintln("failed to look up system property '" + propertyName + PdfOps.SINGLE_QUOTE_TOKEN);
                t2.printStackTrace();
            }
        }
        String javah = ss.getSystemProperty("java.home");
        String configFile = javah + File.separator + "lib" + File.separator + "jaxp.properties";
        try {
            if (firstTime) {
                synchronized (cacheProps) {
                    if (firstTime) {
                        File f2 = new File(configFile);
                        firstTime = false;
                        if (ss.doesFileExist(f2)) {
                            debugPrintln("Read properties file " + ((Object) f2));
                            cacheProps.load(ss.getFileInputStream(f2));
                        }
                    }
                }
            }
            String factoryClassName = cacheProps.getProperty(propertyName);
            debugPrintln("found " + factoryClassName + " in $java.home/jaxp.properties");
            if (factoryClassName != null) {
                xpathFactory = createInstance(factoryClassName);
                if (xpathFactory != null) {
                    return xpathFactory;
                }
            }
        } catch (Exception ex) {
            if (debug) {
                ex.printStackTrace();
            }
        }
        if (!$assertionsDisabled && xpathFactory != null) {
            throw new AssertionError();
        }
        XPathFactory xpathFactory2 = findServiceProvider(uri);
        if (xpathFactory2 != null) {
            return xpathFactory2;
        }
        if (uri.equals("http://java.sun.com/jaxp/xpath/dom")) {
            debugPrintln("attempting to use the platform default W3C DOM XPath lib");
            return new XPathFactoryImpl();
        }
        debugPrintln("all things were tried, but none was found. bailing out.");
        return null;
    }

    private Class<?> createClass(String className) {
        Class clazz;
        boolean internal = false;
        if (System.getSecurityManager() != null && className != null && className.startsWith(DEFAULT_PACKAGE)) {
            internal = true;
        }
        try {
            if (this.classLoader != null && !internal) {
                clazz = Class.forName(className, false, this.classLoader);
            } else {
                clazz = Class.forName(className);
            }
            return clazz;
        } catch (Throwable t2) {
            if (debug) {
                t2.printStackTrace();
                return null;
            }
            return null;
        }
    }

    XPathFactory createInstance(String className) throws XPathFactoryConfigurationException {
        debugPrintln("createInstance(" + className + ")");
        Class<?> clazz = createClass(className);
        if (clazz == null) {
            debugPrintln("failed to getClass(" + className + ")");
            return null;
        }
        debugPrintln("loaded " + className + " from " + which(clazz));
        try {
            XPathFactory xPathFactory = (XPathFactory) clazz.newInstance();
            return xPathFactory;
        } catch (ClassCastException classCastException) {
            debugPrintln("could not instantiate " + clazz.getName());
            if (debug) {
                classCastException.printStackTrace();
                return null;
            }
            return null;
        } catch (IllegalAccessException illegalAccessException) {
            debugPrintln("could not instantiate " + clazz.getName());
            if (debug) {
                illegalAccessException.printStackTrace();
                return null;
            }
            return null;
        } catch (InstantiationException instantiationException) {
            debugPrintln("could not instantiate " + clazz.getName());
            if (debug) {
                instantiationException.printStackTrace();
                return null;
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isObjectModelSupportedBy(final XPathFactory factory, final String objectModel, AccessControlContext acc) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: javax.xml.xpath.XPathFactoryFinder.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return Boolean.valueOf(factory.isObjectModelSupported(objectModel));
            }
        }, acc)).booleanValue();
    }

    private XPathFactory findServiceProvider(final String objectModel) throws XPathFactoryConfigurationException {
        if (!$assertionsDisabled && objectModel == null) {
            throw new AssertionError();
        }
        final AccessControlContext acc = AccessController.getContext();
        try {
            return (XPathFactory) AccessController.doPrivileged(new PrivilegedAction<XPathFactory>() { // from class: javax.xml.xpath.XPathFactoryFinder.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public XPathFactory run2() {
                    ServiceLoader<XPathFactory> loader = ServiceLoader.load(XPathFactoryFinder.SERVICE_CLASS);
                    Iterator<XPathFactory> it = loader.iterator();
                    while (it.hasNext()) {
                        XPathFactory factory = it.next();
                        if (XPathFactoryFinder.this.isObjectModelSupportedBy(factory, objectModel, acc)) {
                            return factory;
                        }
                    }
                    return null;
                }
            });
        } catch (ServiceConfigurationError error) {
            throw new XPathFactoryConfigurationException(error);
        }
    }

    private static String which(Class clazz) {
        return which(clazz.getName(), clazz.getClassLoader());
    }

    private static String which(String classname, ClassLoader loader) {
        String classnameAsResource = classname.replace('.', '/') + ".class";
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        URL it = ss.getResourceAsURL(loader, classnameAsResource);
        if (it != null) {
            return it.toString();
        }
        return null;
    }
}
