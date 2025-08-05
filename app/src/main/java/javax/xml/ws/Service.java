package javax.xml.ws;

import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.Executor;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.spi.ServiceDelegate;

/* loaded from: rt.jar:javax/xml/ws/Service.class */
public class Service {
    private ServiceDelegate delegate;

    /* loaded from: rt.jar:javax/xml/ws/Service$Mode.class */
    public enum Mode {
        MESSAGE,
        PAYLOAD
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected Service(URL wsdlDocumentLocation, QName serviceName) {
        this.delegate = javax.xml.ws.spi.Provider.provider().createServiceDelegate(wsdlDocumentLocation, serviceName, getClass());
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected Service(URL wsdlDocumentLocation, QName serviceName, WebServiceFeature... features) {
        this.delegate = javax.xml.ws.spi.Provider.provider().createServiceDelegate(wsdlDocumentLocation, serviceName, getClass(), features);
    }

    public <T> T getPort(QName qName, Class<T> cls) {
        return (T) this.delegate.getPort(qName, cls);
    }

    public <T> T getPort(QName qName, Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        return (T) this.delegate.getPort(qName, cls, webServiceFeatureArr);
    }

    public <T> T getPort(Class<T> cls) {
        return (T) this.delegate.getPort(cls);
    }

    public <T> T getPort(Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        return (T) this.delegate.getPort(cls, webServiceFeatureArr);
    }

    public <T> T getPort(EndpointReference endpointReference, Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        return (T) this.delegate.getPort(endpointReference, cls, webServiceFeatureArr);
    }

    public void addPort(QName portName, String bindingId, String endpointAddress) {
        this.delegate.addPort(portName, bindingId, endpointAddress);
    }

    public <T> Dispatch<T> createDispatch(QName portName, Class<T> type, Mode mode) {
        return this.delegate.createDispatch(portName, type, mode);
    }

    public <T> Dispatch<T> createDispatch(QName portName, Class<T> type, Mode mode, WebServiceFeature... features) {
        return this.delegate.createDispatch(portName, type, mode, features);
    }

    public <T> Dispatch<T> createDispatch(EndpointReference endpointReference, Class<T> type, Mode mode, WebServiceFeature... features) {
        return this.delegate.createDispatch(endpointReference, type, mode, features);
    }

    public Dispatch<Object> createDispatch(QName portName, JAXBContext context, Mode mode) {
        return this.delegate.createDispatch(portName, context, mode);
    }

    public Dispatch<Object> createDispatch(QName portName, JAXBContext context, Mode mode, WebServiceFeature... features) {
        return this.delegate.createDispatch(portName, context, mode, features);
    }

    public Dispatch<Object> createDispatch(EndpointReference endpointReference, JAXBContext context, Mode mode, WebServiceFeature... features) {
        return this.delegate.createDispatch(endpointReference, context, mode, features);
    }

    public QName getServiceName() {
        return this.delegate.getServiceName();
    }

    public Iterator<QName> getPorts() {
        return this.delegate.getPorts();
    }

    public URL getWSDLDocumentLocation() {
        return this.delegate.getWSDLDocumentLocation();
    }

    public HandlerResolver getHandlerResolver() {
        return this.delegate.getHandlerResolver();
    }

    public void setHandlerResolver(HandlerResolver handlerResolver) {
        this.delegate.setHandlerResolver(handlerResolver);
    }

    public Executor getExecutor() {
        return this.delegate.getExecutor();
    }

    public void setExecutor(Executor executor) {
        this.delegate.setExecutor(executor);
    }

    public static Service create(URL wsdlDocumentLocation, QName serviceName) {
        return new Service(wsdlDocumentLocation, serviceName);
    }

    public static Service create(URL wsdlDocumentLocation, QName serviceName, WebServiceFeature... features) {
        return new Service(wsdlDocumentLocation, serviceName, features);
    }

    public static Service create(QName serviceName) {
        return new Service(null, serviceName);
    }

    public static Service create(QName serviceName, WebServiceFeature... features) {
        return new Service(null, serviceName, features);
    }
}
