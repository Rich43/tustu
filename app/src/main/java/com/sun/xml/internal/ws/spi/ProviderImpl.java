package com.sun.xml.internal.ws.spi;

import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.ServiceSharedFeatureMarker;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
import com.sun.xml.internal.ws.api.server.BoundEndpoint;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import com.sun.xml.internal.ws.api.server.Module;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
import com.sun.xml.internal.ws.client.WSServiceDelegate;
import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
import com.sun.xml.internal.ws.resources.ProviderApiMessages;
import com.sun.xml.internal.ws.transport.http.server.EndpointImpl;
import com.sun.xml.internal.ws.util.ServiceFinder;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Endpoint;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.spi.Invoker;
import javax.xml.ws.spi.Provider;
import javax.xml.ws.spi.ServiceDelegate;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/ProviderImpl.class */
public class ProviderImpl extends Provider {
    private static final ContextClassloaderLocal<JAXBContext> eprjc = new ContextClassloaderLocal<JAXBContext>() { // from class: com.sun.xml.internal.ws.spi.ProviderImpl.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.spi.ContextClassloaderLocal
        public JAXBContext initialValue() throws Exception {
            return ProviderImpl.getEPRJaxbContext();
        }
    };
    public static final ProviderImpl INSTANCE = new ProviderImpl();

    @Override // javax.xml.ws.spi.Provider
    public Endpoint createEndpoint(String bindingId, Object implementor) {
        return new EndpointImpl(bindingId != null ? BindingID.parse(bindingId) : BindingID.parse(implementor.getClass()), implementor, new WebServiceFeature[0]);
    }

    @Override // javax.xml.ws.spi.Provider
    public ServiceDelegate createServiceDelegate(URL wsdlDocumentLocation, QName serviceName, Class serviceClass) {
        return new WSServiceDelegate(wsdlDocumentLocation, serviceName, (Class<? extends Service>) serviceClass, new WebServiceFeature[0]);
    }

    @Override // javax.xml.ws.spi.Provider
    public ServiceDelegate createServiceDelegate(URL wsdlDocumentLocation, QName serviceName, Class serviceClass, WebServiceFeature... features) {
        for (WebServiceFeature feature : features) {
            if (!(feature instanceof ServiceSharedFeatureMarker)) {
                throw new WebServiceException("Doesn't support any Service specific features");
            }
        }
        return new WSServiceDelegate(wsdlDocumentLocation, serviceName, (Class<? extends Service>) serviceClass, features);
    }

    public ServiceDelegate createServiceDelegate(Source wsdlSource, QName serviceName, Class serviceClass) {
        return new WSServiceDelegate(wsdlSource, serviceName, (Class<? extends Service>) serviceClass, new WebServiceFeature[0]);
    }

    @Override // javax.xml.ws.spi.Provider
    public Endpoint createAndPublishEndpoint(String address, Object implementor) {
        Endpoint endpoint = new EndpointImpl(BindingID.parse(implementor.getClass()), implementor, new WebServiceFeature[0]);
        endpoint.publish(address);
        return endpoint;
    }

    @Override // javax.xml.ws.spi.Provider
    public Endpoint createEndpoint(String bindingId, Object implementor, WebServiceFeature... features) {
        return new EndpointImpl(bindingId != null ? BindingID.parse(bindingId) : BindingID.parse(implementor.getClass()), implementor, features);
    }

    @Override // javax.xml.ws.spi.Provider
    public Endpoint createAndPublishEndpoint(String address, Object implementor, WebServiceFeature... features) {
        Endpoint endpoint = new EndpointImpl(BindingID.parse(implementor.getClass()), implementor, features);
        endpoint.publish(address);
        return endpoint;
    }

    @Override // javax.xml.ws.spi.Provider
    public Endpoint createEndpoint(String bindingId, Class implementorClass, Invoker invoker, WebServiceFeature... features) {
        return new EndpointImpl(bindingId != null ? BindingID.parse(bindingId) : BindingID.parse((Class<?>) implementorClass), implementorClass, invoker, features);
    }

    @Override // javax.xml.ws.spi.Provider
    public EndpointReference readEndpointReference(Source eprInfoset) {
        try {
            Unmarshaller unmarshaller = eprjc.get().createUnmarshaller();
            return (EndpointReference) unmarshaller.unmarshal(eprInfoset);
        } catch (JAXBException e2) {
            throw new WebServiceException("Error creating Marshaller or marshalling.", e2);
        }
    }

    @Override // javax.xml.ws.spi.Provider
    public <T> T getPort(EndpointReference endpointReference, Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        if (endpointReference == null) {
            throw new WebServiceException(ProviderApiMessages.NULL_EPR());
        }
        WSEndpointReference wSEndpointReference = new WSEndpointReference(endpointReference);
        WSEndpointReference.Metadata metaData = wSEndpointReference.getMetaData();
        if (metaData.getWsdlSource() == null) {
            throw new WebServiceException("WSDL metadata is missing in EPR");
        }
        return (T) ((WSService) createServiceDelegate(metaData.getWsdlSource(), metaData.getServiceName(), Service.class)).getPort(wSEndpointReference, cls, webServiceFeatureArr);
    }

    @Override // javax.xml.ws.spi.Provider
    public W3CEndpointReference createW3CEndpointReference(String address, QName serviceName, QName portName, List<Element> metadata, String wsdlDocumentLocation, List<Element> referenceParameters) {
        return createW3CEndpointReference(address, null, serviceName, portName, metadata, wsdlDocumentLocation, referenceParameters, null, null);
    }

    @Override // javax.xml.ws.spi.Provider
    public W3CEndpointReference createW3CEndpointReference(String address, QName interfaceName, QName serviceName, QName portName, List<Element> metadata, String wsdlDocumentLocation, List<Element> referenceParameters, List<Element> elements, Map<QName, String> attributes) {
        Container container = ContainerResolver.getInstance().getContainer();
        if (address == null) {
            if (serviceName == null || portName == null) {
                throw new IllegalStateException(ProviderApiMessages.NULL_ADDRESS_SERVICE_ENDPOINT());
            }
            Module module = (Module) container.getSPI(Module.class);
            if (module != null) {
                List<BoundEndpoint> beList = module.getBoundEndpoints();
                Iterator<BoundEndpoint> it = beList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    BoundEndpoint be2 = it.next();
                    WSEndpoint wse = be2.getEndpoint();
                    if (wse.getServiceName().equals(serviceName) && wse.getPortName().equals(portName)) {
                        try {
                            address = be2.getAddress().toString();
                            break;
                        } catch (WebServiceException e2) {
                        }
                    }
                }
            }
            if (address == null) {
                throw new IllegalStateException(ProviderApiMessages.NULL_ADDRESS());
            }
        }
        if (serviceName == null && portName != null) {
            throw new IllegalStateException(ProviderApiMessages.NULL_SERVICE());
        }
        String wsdlTargetNamespace = null;
        if (wsdlDocumentLocation != null) {
            try {
                EntityResolver er = XmlUtil.createDefaultCatalogResolver();
                URL wsdlLoc = new URL(wsdlDocumentLocation);
                WSDLModel wsdlDoc = RuntimeWSDLParser.parse(wsdlLoc, (Source) new StreamSource(wsdlLoc.toExternalForm()), er, true, container, (WSDLParserExtension[]) ServiceFinder.find(WSDLParserExtension.class).toArray());
                if (serviceName != null) {
                    WSDLService wsdlService = wsdlDoc.getService(serviceName);
                    if (wsdlService == null) {
                        throw new IllegalStateException(ProviderApiMessages.NOTFOUND_SERVICE_IN_WSDL(serviceName, wsdlDocumentLocation));
                    }
                    if (portName != null) {
                        WSDLPort wsdlPort = wsdlService.get(portName);
                        if (wsdlPort == null) {
                            throw new IllegalStateException(ProviderApiMessages.NOTFOUND_PORT_IN_WSDL(portName, serviceName, wsdlDocumentLocation));
                        }
                    }
                    wsdlTargetNamespace = serviceName.getNamespaceURI();
                } else {
                    QName firstService = wsdlDoc.getFirstServiceName();
                    wsdlTargetNamespace = firstService.getNamespaceURI();
                }
            } catch (Exception e3) {
                throw new IllegalStateException(ProviderApiMessages.ERROR_WSDL(wsdlDocumentLocation), e3);
            }
        }
        if (metadata != null && metadata.size() == 0) {
            metadata = null;
        }
        return (W3CEndpointReference) new WSEndpointReference(AddressingVersion.fromSpecClass(W3CEndpointReference.class), address, serviceName, portName, interfaceName, metadata, wsdlDocumentLocation, wsdlTargetNamespace, referenceParameters, elements, attributes).toSpec(W3CEndpointReference.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JAXBContext getEPRJaxbContext() {
        return (JAXBContext) AccessController.doPrivileged(new PrivilegedAction<JAXBContext>() { // from class: com.sun.xml.internal.ws.spi.ProviderImpl.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public JAXBContext run2() {
                try {
                    return JAXBContext.newInstance(MemberSubmissionEndpointReference.class, W3CEndpointReference.class);
                } catch (JAXBException e2) {
                    throw new WebServiceException("Error creating JAXBContext for W3CEndpointReference. ", e2);
                }
            }
        });
    }
}
