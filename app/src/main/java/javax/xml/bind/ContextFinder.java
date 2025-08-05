package javax.xml.bind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:javax/xml/bind/ContextFinder.class */
class ContextFinder {
    private static final Logger logger = Logger.getLogger("javax.xml.bind");
    private static final String PLATFORM_DEFAULT_FACTORY_CLASS = "com.sun.xml.internal.bind.v2.ContextFactory";

    ContextFinder() {
    }

    static {
        try {
            if (AccessController.doPrivileged(new GetPropertyAction("jaxb.debug")) != null) {
                logger.setUseParentHandlers(false);
                logger.setLevel(Level.ALL);
                ConsoleHandler handler = new ConsoleHandler();
                handler.setLevel(Level.ALL);
                logger.addHandler(handler);
            }
        } catch (Throwable th) {
        }
    }

    private static void handleInvocationTargetException(InvocationTargetException x2) throws JAXBException {
        Throwable t2 = x2.getTargetException();
        if (t2 != null) {
            if (t2 instanceof JAXBException) {
                throw ((JAXBException) t2);
            }
            if (t2 instanceof RuntimeException) {
                throw ((RuntimeException) t2);
            }
            if (t2 instanceof Error) {
                throw ((Error) t2);
            }
        }
    }

    private static JAXBException handleClassCastException(Class originalType, Class targetType) {
        URL targetTypeURL = which(targetType);
        return new JAXBException(Messages.format("JAXBContext.IllegalCast", getClassClassLoader(originalType).getResource("javax/xml/bind/JAXBContext.class"), targetTypeURL));
    }

    static JAXBContext newInstance(String contextPath, String className, ClassLoader classLoader, Map properties) throws JAXBException {
        try {
            Class spFactory = safeLoadClass(className, classLoader);
            return newInstance(contextPath, spFactory, classLoader, properties);
        } catch (ClassNotFoundException x2) {
            throw new JAXBException(Messages.format("ContextFinder.ProviderNotFound", className), x2);
        } catch (RuntimeException x3) {
            throw x3;
        } catch (Exception x4) {
            throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", className, x4), x4);
        }
    }

    static JAXBContext newInstance(String contextPath, Class spFactory, ClassLoader classLoader, Map properties) throws JAXBException {
        Object context = null;
        try {
            try {
                Method m2 = spFactory.getMethod("createContext", String.class, ClassLoader.class, Map.class);
                context = m2.invoke(null, contextPath, classLoader, properties);
            } catch (RuntimeException x2) {
                throw x2;
            } catch (InvocationTargetException x3) {
                handleInvocationTargetException(x3);
                Throwable e2 = x3;
                if (x3.getTargetException() != null) {
                    e2 = x3.getTargetException();
                }
                throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", spFactory, e2), e2);
            } catch (Exception x4) {
                throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", spFactory, x4), x4);
            }
        } catch (NoSuchMethodException e3) {
        }
        if (context == null) {
            Method m3 = spFactory.getMethod("createContext", String.class, ClassLoader.class);
            context = m3.invoke(null, contextPath, classLoader);
        }
        if (!(context instanceof JAXBContext)) {
            throw handleClassCastException(context.getClass(), JAXBContext.class);
        }
        return (JAXBContext) context;
    }

    static JAXBContext newInstance(Class[] classes, Map properties, String className) throws JAXBException {
        ClassLoader cl = getContextClassLoader();
        try {
            Class spi = safeLoadClass(className, cl);
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "loaded {0} from {1}", new Object[]{className, which(spi)});
            }
            return newInstance(classes, properties, spi);
        } catch (ClassNotFoundException e2) {
            throw new JAXBException(e2);
        }
    }

    static JAXBContext newInstance(Class[] classes, Map properties, Class spFactory) throws SecurityException, IllegalArgumentException, JAXBException {
        try {
            Method m2 = spFactory.getMethod("createContext", Class[].class, Map.class);
            try {
                Object context = m2.invoke(null, classes, properties);
                if (!(context instanceof JAXBContext)) {
                    throw handleClassCastException(context.getClass(), JAXBContext.class);
                }
                return (JAXBContext) context;
            } catch (IllegalAccessException e2) {
                throw new JAXBException(e2);
            } catch (InvocationTargetException e3) {
                handleInvocationTargetException(e3);
                Throwable x2 = e3;
                if (e3.getTargetException() != null) {
                    x2 = e3.getTargetException();
                }
                throw new JAXBException(x2);
            }
        } catch (NoSuchMethodException e4) {
            throw new JAXBException(e4);
        }
    }

    static JAXBContext find(String factoryId, String contextPath, ClassLoader classLoader, Map properties) throws JAXBException {
        String jaxbContextFQCN = JAXBContext.class.getName();
        StringTokenizer packages = new StringTokenizer(contextPath, CallSiteDescriptor.TOKEN_DELIMITER);
        if (!packages.hasMoreTokens()) {
            throw new JAXBException(Messages.format("ContextFinder.NoPackageInContextPath"));
        }
        logger.fine("Searching jaxb.properties");
        while (packages.hasMoreTokens()) {
            String packageName = packages.nextToken(CallSiteDescriptor.TOKEN_DELIMITER).replace('.', '/');
            StringBuilder propFileName = new StringBuilder().append(packageName).append("/jaxb.properties");
            Properties props = loadJAXBProperties(classLoader, propFileName.toString());
            if (props != null) {
                if (props.containsKey(factoryId)) {
                    return newInstance(contextPath, props.getProperty(factoryId), classLoader, properties);
                }
                throw new JAXBException(Messages.format("ContextFinder.MissingProperty", packageName, factoryId));
            }
        }
        logger.fine("Searching the system property");
        String factoryClassName = (String) AccessController.doPrivileged(new GetPropertyAction(JAXBContext.JAXB_CONTEXT_FACTORY));
        if (factoryClassName != null) {
            return newInstance(contextPath, factoryClassName, classLoader, properties);
        }
        String factoryClassName2 = (String) AccessController.doPrivileged(new GetPropertyAction(jaxbContextFQCN));
        if (factoryClassName2 != null) {
            return newInstance(contextPath, factoryClassName2, classLoader, properties);
        }
        Class jaxbContext = lookupJaxbContextUsingOsgiServiceLoader();
        if (jaxbContext != null) {
            logger.fine("OSGi environment detected");
            return newInstance(contextPath, jaxbContext, classLoader, properties);
        }
        logger.fine("Searching META-INF/services");
        BufferedReader r2 = null;
        try {
            try {
                StringBuilder resource = new StringBuilder().append("META-INF/services/").append(jaxbContextFQCN);
                InputStream resourceStream = classLoader.getResourceAsStream(resource.toString());
                if (resourceStream != null) {
                    r2 = new BufferedReader(new InputStreamReader(resourceStream, "UTF-8"));
                    String factoryClassName3 = r2.readLine();
                    if (factoryClassName3 != null) {
                        factoryClassName3 = factoryClassName3.trim();
                    }
                    r2.close();
                    return newInstance(contextPath, factoryClassName3, classLoader, properties);
                }
                logger.log(Level.FINE, "Unable to load:{0}", resource.toString());
                if (r2 != null) {
                    try {
                        r2.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ContextFinder.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex);
                    }
                }
                logger.fine("Trying to create the platform default provider");
                return newInstance(contextPath, PLATFORM_DEFAULT_FACTORY_CLASS, classLoader, properties);
            } catch (UnsupportedEncodingException e2) {
                throw new JAXBException(e2);
            } catch (IOException e3) {
                throw new JAXBException(e3);
            }
        } finally {
            if (r2 != null) {
                try {
                    r2.close();
                } catch (IOException ex2) {
                    Logger.getLogger(ContextFinder.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex2);
                }
            }
        }
    }

    static JAXBContext find(Class[] classes, Map properties) throws JAXBException {
        String jaxbContextFQCN = JAXBContext.class.getName();
        for (Class c2 : classes) {
            ClassLoader classLoader = getClassClassLoader(c2);
            Package pkg = c2.getPackage();
            if (pkg != null) {
                String packageName = pkg.getName().replace('.', '/');
                String resourceName = packageName + "/jaxb.properties";
                logger.log(Level.FINE, "Trying to locate {0}", resourceName);
                Properties props = loadJAXBProperties(classLoader, resourceName);
                if (props != null) {
                    logger.fine("  found");
                    if (props.containsKey(JAXBContext.JAXB_CONTEXT_FACTORY)) {
                        return newInstance(classes, properties, props.getProperty(JAXBContext.JAXB_CONTEXT_FACTORY).trim());
                    }
                    throw new JAXBException(Messages.format("ContextFinder.MissingProperty", packageName, JAXBContext.JAXB_CONTEXT_FACTORY));
                }
                logger.fine("  not found");
            }
        }
        logger.log(Level.FINE, "Checking system property {0}", JAXBContext.JAXB_CONTEXT_FACTORY);
        String factoryClassName = (String) AccessController.doPrivileged(new GetPropertyAction(JAXBContext.JAXB_CONTEXT_FACTORY));
        if (factoryClassName != null) {
            logger.log(Level.FINE, "  found {0}", factoryClassName);
            return newInstance(classes, properties, factoryClassName);
        }
        logger.fine("  not found");
        logger.log(Level.FINE, "Checking system property {0}", jaxbContextFQCN);
        String factoryClassName2 = (String) AccessController.doPrivileged(new GetPropertyAction(jaxbContextFQCN));
        if (factoryClassName2 != null) {
            logger.log(Level.FINE, "  found {0}", factoryClassName2);
            return newInstance(classes, properties, factoryClassName2);
        }
        logger.fine("  not found");
        Class jaxbContext = lookupJaxbContextUsingOsgiServiceLoader();
        if (jaxbContext != null) {
            logger.fine("OSGi environment detected");
            return newInstance(classes, properties, jaxbContext);
        }
        logger.fine("Checking META-INF/services");
        BufferedReader r2 = null;
        try {
            try {
                try {
                    String resource = "META-INF/services/" + jaxbContextFQCN;
                    ClassLoader classLoader2 = getContextClassLoader();
                    URL resourceURL = classLoader2 == null ? ClassLoader.getSystemResource(resource) : classLoader2.getResource(resource);
                    if (resourceURL == null) {
                        logger.log(Level.FINE, "Unable to find: {0}", resource);
                        if (0 != 0) {
                            try {
                                r2.close();
                            } catch (IOException ex) {
                                logger.log(Level.FINE, "Unable to close stream", (Throwable) ex);
                            }
                        }
                        logger.fine("Trying to create the platform default provider");
                        return newInstance(classes, properties, PLATFORM_DEFAULT_FACTORY_CLASS);
                    }
                    logger.log(Level.FINE, "Reading {0}", resourceURL);
                    BufferedReader r3 = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "UTF-8"));
                    String factoryClassName3 = r3.readLine();
                    if (factoryClassName3 != null) {
                        factoryClassName3 = factoryClassName3.trim();
                    }
                    JAXBContext jAXBContextNewInstance = newInstance(classes, properties, factoryClassName3);
                    if (r3 != null) {
                        try {
                            r3.close();
                        } catch (IOException ex2) {
                            logger.log(Level.FINE, "Unable to close stream", (Throwable) ex2);
                        }
                    }
                    return jAXBContextNewInstance;
                } catch (IOException e2) {
                    throw new JAXBException(e2);
                }
            } catch (UnsupportedEncodingException e3) {
                throw new JAXBException(e3);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    r2.close();
                } catch (IOException ex3) {
                    logger.log(Level.FINE, "Unable to close stream", (Throwable) ex3);
                }
            }
            throw th;
        }
    }

    private static Class lookupJaxbContextUsingOsgiServiceLoader() {
        try {
            Class target = Class.forName("com.sun.org.glassfish.hk2.osgiresourcelocator.ServiceLoader");
            Method m2 = target.getMethod("lookupProviderClasses", Class.class);
            Iterator iter = ((Iterable) m2.invoke(null, JAXBContext.class)).iterator();
            if (iter.hasNext()) {
                return (Class) iter.next();
            }
            return null;
        } catch (Exception e2) {
            logger.log(Level.FINE, "Unable to find from OSGi: javax.xml.bind.JAXBContext");
            return null;
        }
    }

    private static Properties loadJAXBProperties(ClassLoader classLoader, String propFileName) throws JAXBException {
        URL url;
        Properties props = null;
        try {
            if (classLoader == null) {
                url = ClassLoader.getSystemResource(propFileName);
            } else {
                url = classLoader.getResource(propFileName);
            }
            if (url != null) {
                logger.log(Level.FINE, "loading props from {0}", url);
                props = new Properties();
                InputStream is = url.openStream();
                props.load(is);
                is.close();
            }
            return props;
        } catch (IOException ioe) {
            logger.log(Level.FINE, "Unable to load " + propFileName, (Throwable) ioe);
            throw new JAXBException(ioe.toString(), ioe);
        }
    }

    static URL which(Class clazz, ClassLoader loader) {
        String classnameAsResource = clazz.getName().replace('.', '/') + ".class";
        if (loader == null) {
            loader = getSystemClassLoader();
        }
        return loader.getResource(classnameAsResource);
    }

    static URL which(Class clazz) {
        return which(clazz, getClassClassLoader(clazz));
    }

    private static Class safeLoadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        int i2;
        logger.log(Level.FINE, "Trying to load {0}", className);
        try {
            SecurityManager s2 = System.getSecurityManager();
            if (s2 != null && (i2 = className.lastIndexOf(46)) != -1) {
                s2.checkPackageAccess(className.substring(0, i2));
            }
            if (classLoader == null) {
                return Class.forName(className);
            }
            return classLoader.loadClass(className);
        } catch (SecurityException se) {
            if (PLATFORM_DEFAULT_FACTORY_CLASS.equals(className)) {
                return Class.forName(className);
            }
            throw se;
        }
    }

    private static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.xml.bind.ContextFinder.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    private static ClassLoader getClassClassLoader(final Class c2) {
        if (System.getSecurityManager() == null) {
            return c2.getClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.xml.bind.ContextFinder.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return c2.getClassLoader();
            }
        });
    }

    private static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.xml.bind.ContextFinder.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }
}
