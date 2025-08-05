package javax.xml.bind;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Map;
import org.w3c.dom.Node;

/* loaded from: rt.jar:javax/xml/bind/JAXBContext.class */
public abstract class JAXBContext {
    public static final String JAXB_CONTEXT_FACTORY = "javax.xml.bind.context.factory";

    public abstract Unmarshaller createUnmarshaller() throws JAXBException;

    public abstract Marshaller createMarshaller() throws JAXBException;

    public abstract Validator createValidator() throws JAXBException;

    protected JAXBContext() {
    }

    public static JAXBContext newInstance(String contextPath) throws JAXBException {
        return newInstance(contextPath, getContextClassLoader());
    }

    public static JAXBContext newInstance(String contextPath, ClassLoader classLoader) throws JAXBException {
        return newInstance(contextPath, classLoader, Collections.emptyMap());
    }

    public static JAXBContext newInstance(String contextPath, ClassLoader classLoader, Map<String, ?> properties) throws JAXBException {
        return ContextFinder.find(JAXB_CONTEXT_FACTORY, contextPath, classLoader, properties);
    }

    public static JAXBContext newInstance(Class... classesToBeBound) throws JAXBException {
        return newInstance(classesToBeBound, (Map<String, ?>) Collections.emptyMap());
    }

    public static JAXBContext newInstance(Class[] classesToBeBound, Map<String, ?> properties) throws JAXBException {
        if (classesToBeBound == null) {
            throw new IllegalArgumentException();
        }
        for (int i2 = classesToBeBound.length - 1; i2 >= 0; i2--) {
            if (classesToBeBound[i2] == null) {
                throw new IllegalArgumentException();
            }
        }
        return ContextFinder.find(classesToBeBound, properties);
    }

    public <T> Binder<T> createBinder(Class<T> domType) {
        throw new UnsupportedOperationException();
    }

    public Binder<Node> createBinder() {
        return createBinder(Node.class);
    }

    public JAXBIntrospector createJAXBIntrospector() {
        throw new UnsupportedOperationException();
    }

    public void generateSchema(SchemaOutputResolver outputResolver) throws IOException {
        throw new UnsupportedOperationException();
    }

    private static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.xml.bind.JAXBContext.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }
}
