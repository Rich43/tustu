package javax.naming.spi;

import com.sun.naming.internal.FactoryEnumeration;
import com.sun.naming.internal.ResourceManager;
import java.util.Hashtable;
import javax.naming.CannotProceedException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.spi.DirStateFactory;

/* loaded from: rt.jar:javax/naming/spi/DirectoryManager.class */
public class DirectoryManager extends NamingManager {
    DirectoryManager() {
    }

    public static DirContext getContinuationDirContext(CannotProceedException cannotProceedException) throws NamingException {
        Hashtable hashtable;
        Hashtable<?, ?> environment = cannotProceedException.getEnvironment();
        if (environment == null) {
            hashtable = new Hashtable(7);
        } else {
            hashtable = (Hashtable) environment.clone();
        }
        hashtable.put(NamingManager.CPE, cannotProceedException);
        return new ContinuationDirContext(cannotProceedException, hashtable);
    }

    public static Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable, Attributes attributes) throws Exception {
        ObjectFactoryBuilder objectFactoryBuilder = getObjectFactoryBuilder();
        if (objectFactoryBuilder != null) {
            ObjectFactory objectFactoryCreateObjectFactory = objectFactoryBuilder.createObjectFactory(obj, hashtable);
            if (objectFactoryCreateObjectFactory instanceof DirObjectFactory) {
                return ((DirObjectFactory) objectFactoryCreateObjectFactory).getObjectInstance(obj, name, context, hashtable, attributes);
            }
            return objectFactoryCreateObjectFactory.getObjectInstance(obj, name, context, hashtable);
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
                if (objectFactoryFromReference instanceof DirObjectFactory) {
                    return ((DirObjectFactory) objectFactoryFromReference).getObjectInstance(reference, name, context, hashtable, attributes);
                }
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
        Object objCreateObjectFromFactories = createObjectFromFactories(obj, name, context, hashtable, attributes);
        return objCreateObjectFromFactories != null ? objCreateObjectFromFactories : obj;
    }

    private static Object createObjectFromFactories(Object obj, Name name, Context context, Hashtable<?, ?> hashtable, Attributes attributes) throws Exception {
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
            ObjectFactory objectFactory = (ObjectFactory) factories.next();
            if (objectFactory instanceof DirObjectFactory) {
                objectInstance = ((DirObjectFactory) objectFactory).getObjectInstance(obj, name, context, hashtable, attributes);
            } else {
                objectInstance = objectFactory.getObjectInstance(obj, name, context, hashtable);
            }
        }
        return obj2;
    }

    public static DirStateFactory.Result getStateToBind(Object obj, Name name, Context context, Hashtable<?, ?> hashtable, Attributes attributes) throws NamingException {
        FactoryEnumeration factories = ResourceManager.getFactories(Context.STATE_FACTORIES, hashtable, context);
        if (factories == null) {
            return new DirStateFactory.Result(obj, attributes);
        }
        DirStateFactory.Result stateToBind = null;
        while (stateToBind == null && factories.hasMore()) {
            StateFactory stateFactory = (StateFactory) factories.next();
            if (stateFactory instanceof DirStateFactory) {
                stateToBind = ((DirStateFactory) stateFactory).getStateToBind(obj, name, context, hashtable, attributes);
            } else {
                Object stateToBind2 = stateFactory.getStateToBind(obj, name, context, hashtable);
                if (stateToBind2 != null) {
                    stateToBind = new DirStateFactory.Result(stateToBind2, attributes);
                }
            }
        }
        return stateToBind != null ? stateToBind : new DirStateFactory.Result(obj, attributes);
    }
}
