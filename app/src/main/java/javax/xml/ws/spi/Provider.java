package javax.xml.ws.spi;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.Endpoint;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;

/* loaded from: rt.jar:javax/xml/ws/spi/Provider.class */
public abstract class Provider {
    public static final String JAXWSPROVIDER_PROPERTY = "javax.xml.ws.spi.Provider";
    static final String DEFAULT_JAXWSPROVIDER = "com.sun.xml.internal.ws.spi.ProviderImpl";
    private static final Method loadMethod;
    private static final Method iteratorMethod;

    public abstract ServiceDelegate createServiceDelegate(URL url, QName qName, Class<? extends Service> cls);

    public abstract Endpoint createEndpoint(String str, Object obj);

    public abstract Endpoint createAndPublishEndpoint(String str, Object obj);

    public abstract EndpointReference readEndpointReference(Source source);

    public abstract <T> T getPort(EndpointReference endpointReference, Class<T> cls, WebServiceFeature... webServiceFeatureArr);

    public abstract W3CEndpointReference createW3CEndpointReference(String str, QName qName, QName qName2, List<Element> list, String str2, List<Element> list2);

    static {
        Method tLoadMethod = null;
        Method tIteratorMethod = null;
        try {
            Class<?> clazz = Class.forName("java.util.ServiceLoader");
            tLoadMethod = clazz.getMethod("load", Class.class);
            tIteratorMethod = clazz.getMethod(Constants.ITERATOR_PNAME, new Class[0]);
        } catch (ClassNotFoundException e2) {
        } catch (NoSuchMethodException e3) {
        }
        loadMethod = tLoadMethod;
        iteratorMethod = tIteratorMethod;
    }

    protected Provider() {
    }

    public static Provider provider() {
        try {
            Object provider = getProviderUsingServiceLoader();
            if (provider == null) {
                provider = FactoryFinder.find(JAXWSPROVIDER_PROPERTY, DEFAULT_JAXWSPROVIDER);
            }
            if (!(provider instanceof Provider)) {
                String classnameAsResource = Provider.class.getName().replace('.', '/') + ".class";
                ClassLoader loader = Provider.class.getClassLoader();
                if (loader == null) {
                    loader = ClassLoader.getSystemClassLoader();
                }
                URL targetTypeURL = loader.getResource(classnameAsResource);
                throw new LinkageError("ClassCastException: attempting to cast" + ((Object) provider.getClass().getClassLoader().getResource(classnameAsResource)) + "to" + targetTypeURL.toString());
            }
            return (Provider) provider;
        } catch (WebServiceException ex) {
            throw ex;
        } catch (Exception ex2) {
            throw new WebServiceException("Unable to createEndpointReference Provider", ex2);
        }
    }

    private static Provider getProviderUsingServiceLoader() {
        if (loadMethod != null) {
            try {
                Object loader = loadMethod.invoke(null, Provider.class);
                try {
                    Iterator<Provider> it = (Iterator) iteratorMethod.invoke(loader, new Object[0]);
                    if (it.hasNext()) {
                        return it.next();
                    }
                    return null;
                } catch (Exception e2) {
                    throw new WebServiceException("Cannot invoke java.util.ServiceLoader#iterator()", e2);
                }
            } catch (Exception e3) {
                throw new WebServiceException("Cannot invoke java.util.ServiceLoader#load()", e3);
            }
        }
        return null;
    }

    public ServiceDelegate createServiceDelegate(URL wsdlDocumentLocation, QName serviceName, Class<? extends Service> serviceClass, WebServiceFeature... features) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementation must override this default behaviour.");
    }

    public W3CEndpointReference createW3CEndpointReference(String address, QName interfaceName, QName serviceName, QName portName, List<Element> metadata, String wsdlDocumentLocation, List<Element> referenceParameters, List<Element> elements, Map<QName, String> attributes) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementation must override this default behaviour.");
    }

    public Endpoint createAndPublishEndpoint(String address, Object implementor, WebServiceFeature... features) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementation must override this default behaviour.");
    }

    public Endpoint createEndpoint(String bindingId, Object implementor, WebServiceFeature... features) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementation must override this default behaviour.");
    }

    public Endpoint createEndpoint(String bindingId, Class<?> implementorClass, Invoker invoker, WebServiceFeature... features) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementation must override this default behaviour.");
    }
}
