package javax.xml.ws.spi;

import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.Executor;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.ws.Dispatch;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.HandlerResolver;

/* loaded from: rt.jar:javax/xml/ws/spi/ServiceDelegate.class */
public abstract class ServiceDelegate {
    public abstract <T> T getPort(QName qName, Class<T> cls);

    public abstract <T> T getPort(QName qName, Class<T> cls, WebServiceFeature... webServiceFeatureArr);

    public abstract <T> T getPort(EndpointReference endpointReference, Class<T> cls, WebServiceFeature... webServiceFeatureArr);

    public abstract <T> T getPort(Class<T> cls);

    public abstract <T> T getPort(Class<T> cls, WebServiceFeature... webServiceFeatureArr);

    public abstract void addPort(QName qName, String str, String str2);

    public abstract <T> Dispatch<T> createDispatch(QName qName, Class<T> cls, Service.Mode mode);

    public abstract <T> Dispatch<T> createDispatch(QName qName, Class<T> cls, Service.Mode mode, WebServiceFeature... webServiceFeatureArr);

    public abstract <T> Dispatch<T> createDispatch(EndpointReference endpointReference, Class<T> cls, Service.Mode mode, WebServiceFeature... webServiceFeatureArr);

    public abstract Dispatch<Object> createDispatch(QName qName, JAXBContext jAXBContext, Service.Mode mode);

    public abstract Dispatch<Object> createDispatch(QName qName, JAXBContext jAXBContext, Service.Mode mode, WebServiceFeature... webServiceFeatureArr);

    public abstract Dispatch<Object> createDispatch(EndpointReference endpointReference, JAXBContext jAXBContext, Service.Mode mode, WebServiceFeature... webServiceFeatureArr);

    public abstract QName getServiceName();

    public abstract Iterator<QName> getPorts();

    public abstract URL getWSDLDocumentLocation();

    public abstract HandlerResolver getHandlerResolver();

    public abstract void setHandlerResolver(HandlerResolver handlerResolver);

    public abstract Executor getExecutor();

    public abstract void setExecutor(Executor executor);

    protected ServiceDelegate() {
    }
}
