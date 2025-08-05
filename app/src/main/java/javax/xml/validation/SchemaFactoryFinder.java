package javax.xml.validation;

import com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory;
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

/* loaded from: rt.jar:javax/xml/validation/SchemaFactoryFinder.class */
class SchemaFactoryFinder {
    private static boolean debug;
    private static final SecuritySupport ss;
    private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xerces.internal";
    private static final Properties cacheProps;
    private static volatile boolean firstTime;
    private final ClassLoader classLoader;
    private static final Class<SchemaFactory> SERVICE_CLASS;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SchemaFactoryFinder.class.desiredAssertionStatus();
        debug = false;
        ss = new SecuritySupport();
        cacheProps = new Properties();
        firstTime = true;
        try {
            debug = ss.getSystemProperty("jaxp.debug") != null;
        } catch (Exception e2) {
            debug = false;
        }
        SERVICE_CLASS = SchemaFactory.class;
    }

    private static void debugPrintln(String msg) {
        if (debug) {
            System.err.println("JAXP: " + msg);
        }
    }

    public SchemaFactoryFinder(ClassLoader loader) {
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

    public SchemaFactory newFactory(String schemaLanguage) {
        if (schemaLanguage == null) {
            throw new NullPointerException();
        }
        SchemaFactory f2 = _newFactory(schemaLanguage);
        if (f2 != null) {
            debugPrintln("factory '" + f2.getClass().getName() + "' was found for " + schemaLanguage);
        } else {
            debugPrintln("unable to find a factory for " + schemaLanguage);
        }
        return f2;
    }

    private SchemaFactory _newFactory(String schemaLanguage) {
        SchemaFactory sf;
        String propertyName = SERVICE_CLASS.getName() + CallSiteDescriptor.TOKEN_DELIMITER + schemaLanguage;
        try {
            debugPrintln("Looking up system property '" + propertyName + PdfOps.SINGLE_QUOTE_TOKEN);
            String r2 = ss.getSystemProperty(propertyName);
            if (r2 != null) {
                debugPrintln("The value is '" + r2 + PdfOps.SINGLE_QUOTE_TOKEN);
                SchemaFactory sf2 = createInstance(r2);
                if (sf2 != null) {
                    return sf2;
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
            if (factoryClassName != null && (sf = createInstance(factoryClassName)) != null) {
                return sf;
            }
        } catch (Exception ex) {
            if (debug) {
                ex.printStackTrace();
            }
        }
        SchemaFactory factoryImpl = findServiceProvider(schemaLanguage);
        if (factoryImpl != null) {
            return factoryImpl;
        }
        if (schemaLanguage.equals("http://www.w3.org/2001/XMLSchema")) {
            debugPrintln("attempting to use the platform default XML Schema validator");
            return new XMLSchemaFactory();
        }
        debugPrintln("all things were tried, but none was found. bailing out.");
        return null;
    }

    private Class<?> createClass(String className) {
        Class<?> clazz;
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

    SchemaFactory createInstance(String className) {
        debugPrintln("createInstance(" + className + ")");
        Class<?> clazz = createClass(className);
        if (clazz == null) {
            debugPrintln("failed to getClass(" + className + ")");
            return null;
        }
        debugPrintln("loaded " + className + " from " + which(clazz));
        try {
            if (!SchemaFactory.class.isAssignableFrom(clazz)) {
                throw new ClassCastException(clazz.getName() + " cannot be cast to " + ((Object) SchemaFactory.class));
            }
            SchemaFactory schemaFactory = (SchemaFactory) clazz.newInstance();
            return schemaFactory;
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
    public boolean isSchemaLanguageSupportedBy(final SchemaFactory factory, final String schemaLanguage, AccessControlContext acc) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: javax.xml.validation.SchemaFactoryFinder.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return Boolean.valueOf(factory.isSchemaLanguageSupported(schemaLanguage));
            }
        }, acc)).booleanValue();
    }

    private SchemaFactory findServiceProvider(final String schemaLanguage) {
        if (!$assertionsDisabled && schemaLanguage == null) {
            throw new AssertionError();
        }
        final AccessControlContext acc = AccessController.getContext();
        try {
            return (SchemaFactory) AccessController.doPrivileged(new PrivilegedAction<SchemaFactory>() { // from class: javax.xml.validation.SchemaFactoryFinder.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public SchemaFactory run2() {
                    ServiceLoader<SchemaFactory> loader = ServiceLoader.load(SchemaFactoryFinder.SERVICE_CLASS);
                    Iterator<SchemaFactory> it = loader.iterator();
                    while (it.hasNext()) {
                        SchemaFactory factory = it.next();
                        if (SchemaFactoryFinder.this.isSchemaLanguageSupportedBy(factory, schemaLanguage, acc)) {
                            return factory;
                        }
                    }
                    return null;
                }
            });
        } catch (ServiceConfigurationError error) {
            throw new SchemaFactoryConfigurationError("Provider for " + ((Object) SERVICE_CLASS) + " cannot be created", error);
        }
    }

    private static String which(Class<?> clazz) {
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
