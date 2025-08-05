package javax.naming.spi;

import com.sun.naming.internal.FactoryEnumeration;
import com.sun.naming.internal.ObjectFactoriesFilter;
import com.sun.naming.internal.ResourceManager;
import com.sun.naming.internal.VersionHelper;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.naming.CannotProceedException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;

/* loaded from: rt.jar:javax/naming/spi/NamingManager.class */
public class NamingManager {
    private static final String defaultPkgPrefix = "com.sun.jndi.url";
    public static final String CPE = "java.naming.spi.CannotProceedException";
    static final VersionHelper helper = VersionHelper.getVersionHelper();
    private static ObjectFactoryBuilder object_factory_builder = null;
    private static InitialContextFactoryBuilder initctx_factory_builder = null;

    NamingManager() {
    }

    public static synchronized void setObjectFactoryBuilder(ObjectFactoryBuilder objectFactoryBuilder) throws NamingException {
        if (object_factory_builder != null) {
            throw new IllegalStateException("ObjectFactoryBuilder already set");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        object_factory_builder = objectFactoryBuilder;
    }

    static synchronized ObjectFactoryBuilder getObjectFactoryBuilder() {
        return object_factory_builder;
    }

    static ObjectFactory getObjectFactoryFromReference(Reference reference, String str) throws IllegalAccessException, MalformedURLException, InstantiationException {
        String factoryClassLocation;
        Class<?> clsLoadClass = null;
        try {
            clsLoadClass = helper.loadClassWithoutInit(str);
            if (!ObjectFactoriesFilter.canInstantiateObjectsFactory(clsLoadClass)) {
                return null;
            }
        } catch (ClassNotFoundException e2) {
        }
        if (clsLoadClass == null && (factoryClassLocation = reference.getFactoryClassLocation()) != null) {
            try {
                clsLoadClass = helper.loadClass(str, factoryClassLocation);
                if (clsLoadClass != null) {
                    if (!ObjectFactoriesFilter.canInstantiateObjectsFactory(clsLoadClass)) {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (ClassNotFoundException e3) {
            }
        }
        if (clsLoadClass != null) {
            return (ObjectFactory) clsLoadClass.newInstance();
        }
        return null;
    }

    private static Object createObjectFromFactories(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws Exception {
        Object obj2;
        FactoryEnumeration factories = ResourceManager.getFactories(Context.OBJECT_FACTORIES, hashtable, context);
        if (factories == null) {
            return null;
        }
        Object objectInstance = null;
        while (true) {
            obj2 = objectInstance;
            if (obj2 != null || !factories.hasMore()) {
                break;
            }
            objectInstance = ((ObjectFactory) factories.next()).getObjectInstance(obj, name, context, hashtable);
        }
        return obj2;
    }

    private static String getURLScheme(String str) {
        int iIndexOf = str.indexOf(58);
        int iIndexOf2 = str.indexOf(47);
        if (iIndexOf <= 0) {
            return null;
        }
        if (iIndexOf2 == -1 || iIndexOf < iIndexOf2) {
            return str.substring(0, iIndexOf);
        }
        return null;
    }

    public static Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws Exception {
        ObjectFactoryBuilder objectFactoryBuilder = getObjectFactoryBuilder();
        if (objectFactoryBuilder != null) {
            return objectFactoryBuilder.createObjectFactory(obj, hashtable).getObjectInstance(obj, name, context, hashtable);
        }
        Reference reference = null;
        if (obj instanceof Reference) {
            reference = (Reference) obj;
        } else if (obj instanceof Referenceable) {
            reference = ((Referenceable) obj).getReference();
        }
        if (reference != null) {
            String factoryClassName = reference.getFactoryClassName();
            if (factoryClassName != null) {
                ObjectFactory objectFactoryFromReference = getObjectFactoryFromReference(reference, factoryClassName);
                if (objectFactoryFromReference != null) {
                    return objectFactoryFromReference.getObjectInstance(reference, name, context, hashtable);
                }
                return obj;
            }
            Object objProcessURLAddrs = processURLAddrs(reference, name, context, hashtable);
            if (objProcessURLAddrs != null) {
                return objProcessURLAddrs;
            }
        }
        Object objCreateObjectFromFactories = createObjectFromFactories(obj, name, context, hashtable);
        return objCreateObjectFromFactories != null ? objCreateObjectFromFactories : obj;
    }

    static Object processURLAddrs(Reference reference, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        Object objProcessURL;
        for (int i2 = 0; i2 < reference.size(); i2++) {
            RefAddr refAddr = reference.get(i2);
            if ((refAddr instanceof StringRefAddr) && refAddr.getType().equalsIgnoreCase("URL") && (objProcessURL = processURL((String) refAddr.getContent(), name, context, hashtable)) != null) {
                return objProcessURL;
            }
        }
        return null;
    }

    private static Object processURL(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        Object uRLObject;
        String uRLScheme;
        Object uRLObject2;
        if ((obj instanceof String) && (uRLScheme = getURLScheme((String) obj)) != null && (uRLObject2 = getURLObject(uRLScheme, obj, name, context, hashtable)) != null) {
            return uRLObject2;
        }
        if (obj instanceof String[]) {
            for (String str : (String[]) obj) {
                String uRLScheme2 = getURLScheme(str);
                if (uRLScheme2 != null && (uRLObject = getURLObject(uRLScheme2, obj, name, context, hashtable)) != null) {
                    return uRLObject;
                }
            }
            return null;
        }
        return null;
    }

    static Context getContext(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        if (obj instanceof Context) {
            return (Context) obj;
        }
        try {
            Object objectInstance = getObjectInstance(obj, name, context, hashtable);
            if (objectInstance instanceof Context) {
                return (Context) objectInstance;
            }
            return null;
        } catch (NamingException e2) {
            throw e2;
        } catch (Exception e3) {
            NamingException namingException = new NamingException();
            namingException.setRootCause(e3);
            throw namingException;
        }
    }

    static Resolver getResolver(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        if (obj instanceof Resolver) {
            return (Resolver) obj;
        }
        try {
            Object objectInstance = getObjectInstance(obj, name, context, hashtable);
            if (objectInstance instanceof Resolver) {
                return (Resolver) objectInstance;
            }
            return null;
        } catch (NamingException e2) {
            throw e2;
        } catch (Exception e3) {
            NamingException namingException = new NamingException();
            namingException.setRootCause(e3);
            throw namingException;
        }
    }

    public static Context getURLContext(String str, Hashtable<?, ?> hashtable) throws NamingException {
        Object uRLObject = getURLObject(str, null, null, null, hashtable);
        if (uRLObject instanceof Context) {
            return (Context) uRLObject;
        }
        return null;
    }

    private static Object getURLObject(String str, Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        ObjectFactory objectFactory = (ObjectFactory) ResourceManager.getFactory(Context.URL_PKG_PREFIXES, hashtable, context, "." + str + "." + str + "URLContextFactory", defaultPkgPrefix);
        if (objectFactory == null) {
            return null;
        }
        try {
            return objectFactory.getObjectInstance(obj, name, context, hashtable);
        } catch (NamingException e2) {
            throw e2;
        } catch (Exception e3) {
            NamingException namingException = new NamingException();
            namingException.setRootCause(e3);
            throw namingException;
        }
    }

    private static synchronized InitialContextFactoryBuilder getInitialContextFactoryBuilder() {
        return initctx_factory_builder;
    }

    public static Context getInitialContext(Hashtable<?, ?> hashtable) throws NamingException {
        InitialContextFactory initialContextFactoryCreateInitialContextFactory;
        InitialContextFactoryBuilder initialContextFactoryBuilder = getInitialContextFactoryBuilder();
        if (initialContextFactoryBuilder == null) {
            String str = hashtable != null ? (String) hashtable.get(Context.INITIAL_CONTEXT_FACTORY) : null;
            if (str == null) {
                throw new NoInitialContextException("Need to specify class name in environment or system property, or as an applet parameter, or in an application resource file:  java.naming.factory.initial");
            }
            try {
                initialContextFactoryCreateInitialContextFactory = (InitialContextFactory) helper.loadClass(str).newInstance();
            } catch (Exception e2) {
                NoInitialContextException noInitialContextException = new NoInitialContextException("Cannot instantiate class: " + str);
                noInitialContextException.setRootCause(e2);
                throw noInitialContextException;
            }
        } else {
            initialContextFactoryCreateInitialContextFactory = initialContextFactoryBuilder.createInitialContextFactory(hashtable);
        }
        return initialContextFactoryCreateInitialContextFactory.getInitialContext(hashtable);
    }

    public static synchronized void setInitialContextFactoryBuilder(InitialContextFactoryBuilder initialContextFactoryBuilder) throws NamingException {
        if (initctx_factory_builder != null) {
            throw new IllegalStateException("InitialContextFactoryBuilder already set");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        initctx_factory_builder = initialContextFactoryBuilder;
    }

    public static boolean hasInitialContextFactoryBuilder() {
        return getInitialContextFactoryBuilder() != null;
    }

    public static Context getContinuationContext(CannotProceedException cannotProceedException) throws NamingException {
        Hashtable hashtable;
        Hashtable<?, ?> environment = cannotProceedException.getEnvironment();
        if (environment == null) {
            hashtable = new Hashtable(7);
        } else {
            hashtable = (Hashtable) environment.clone();
        }
        hashtable.put(CPE, cannotProceedException);
        return new ContinuationContext(cannotProceedException, hashtable).getTargetContext();
    }

    public static Object getStateToBind(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        Object obj2;
        FactoryEnumeration factories = ResourceManager.getFactories(Context.STATE_FACTORIES, hashtable, context);
        if (factories == null) {
            return obj;
        }
        Object stateToBind = null;
        while (true) {
            obj2 = stateToBind;
            if (obj2 != null || !factories.hasMore()) {
                break;
            }
            stateToBind = ((StateFactory) factories.next()).getStateToBind(obj, name, context, hashtable);
        }
        return obj2 != null ? obj2 : obj;
    }
}
