package com.sun.xml.internal.ws.client;

import com.oracle.webservices.internal.api.databinding.ExternalMetadataFeature;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.Closeable;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.ComponentFeature;
import com.sun.xml.internal.ws.api.ComponentsFeature;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.client.ServiceInterceptor;
import com.sun.xml.internal.ws.api.client.ServiceInterceptorFactory;
import com.sun.xml.internal.ws.api.databinding.DatabindingConfig;
import com.sun.xml.internal.ws.api.databinding.DatabindingFactory;
import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
import com.sun.xml.internal.ws.api.pipe.Stubs;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.client.HandlerConfigurator;
import com.sun.xml.internal.ws.client.sei.SEIStub;
import com.sun.xml.internal.ws.db.DatabindingImpl;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
import com.sun.xml.internal.ws.developer.UsesJAXBContextFeature;
import com.sun.xml.internal.ws.developer.WSBindingProvider;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import com.sun.xml.internal.ws.model.SOAPSEIModel;
import com.sun.xml.internal.ws.resources.ClientMessages;
import com.sun.xml.internal.ws.resources.DispatchMessages;
import com.sun.xml.internal.ws.resources.ProviderApiMessages;
import com.sun.xml.internal.ws.util.JAXWSUtils;
import com.sun.xml.internal.ws.util.ServiceConfigurationError;
import com.sun.xml.internal.ws.util.ServiceFinder;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.soap.AddressingFeature;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/WSServiceDelegate.class */
public class WSServiceDelegate extends WSService {
    private final Map<QName, PortInfo> ports;

    @NotNull
    private HandlerConfigurator handlerConfigurator;
    private final Class<? extends Service> serviceClass;
    private final WebServiceFeatureList features;

    @NotNull
    private final QName serviceName;
    private final Map<QName, SEIPortInfo> seiContext;
    private volatile Executor executor;

    @Nullable
    private WSDLService wsdlService;
    private final Container container;

    @NotNull
    final ServiceInterceptor serviceInterceptor;
    private URL wsdlURL;
    protected static final WebServiceFeature[] EMPTY_FEATURES = new WebServiceFeature[0];

    protected Map<QName, PortInfo> getQNameToPortInfoMap() {
        return this.ports;
    }

    public WSServiceDelegate(URL wsdlDocumentLocation, QName serviceName, Class<? extends Service> serviceClass, WebServiceFeature... features) {
        this(wsdlDocumentLocation, serviceName, serviceClass, new WebServiceFeatureList(features));
    }

    protected WSServiceDelegate(URL wsdlDocumentLocation, QName serviceName, Class<? extends Service> serviceClass, WebServiceFeatureList features) {
        this(wsdlDocumentLocation == null ? null : new StreamSource(wsdlDocumentLocation.toExternalForm()), serviceName, serviceClass, features);
        this.wsdlURL = wsdlDocumentLocation;
    }

    public WSServiceDelegate(@Nullable Source wsdl, @NotNull QName serviceName, @NotNull Class<? extends Service> serviceClass, WebServiceFeature... features) {
        this(wsdl, serviceName, serviceClass, new WebServiceFeatureList(features));
    }

    protected WSServiceDelegate(@Nullable Source wsdl, @NotNull QName serviceName, @NotNull Class<? extends Service> serviceClass, WebServiceFeatureList features) {
        this(wsdl, (WSDLService) null, serviceName, serviceClass, features);
    }

    public WSServiceDelegate(@Nullable Source wsdl, @Nullable WSDLService service, @NotNull QName serviceName, @NotNull Class<? extends Service> serviceClass, WebServiceFeature... features) {
        this(wsdl, service, serviceName, serviceClass, new WebServiceFeatureList(features));
    }

    public WSServiceDelegate(@Nullable Source wsdl, @Nullable WSDLService service, @NotNull QName serviceName, @NotNull final Class<? extends Service> serviceClass, WebServiceFeatureList features) {
        this.ports = new HashMap();
        this.handlerConfigurator = new HandlerConfigurator.HandlerResolverImpl(null);
        this.seiContext = new HashMap();
        if (serviceName == null) {
            throw new WebServiceException(ClientMessages.INVALID_SERVICE_NAME_NULL(null));
        }
        this.features = features;
        WSService.InitParams initParams = INIT_PARAMS.get();
        INIT_PARAMS.set(null);
        initParams = initParams == null ? EMPTY_PARAMS : initParams;
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
        Container tContainer = initParams.getContainer() != null ? initParams.getContainer() : ContainerResolver.getInstance().getContainer();
        this.container = tContainer == Container.NONE ? new ClientContainer() : tContainer;
        ComponentFeature cf = (ComponentFeature) this.features.get(ComponentFeature.class);
        if (cf != null) {
            switch (cf.getTarget()) {
                case SERVICE:
                    getComponents().add(cf.getComponent());
                    break;
                case CONTAINER:
                    this.container.getComponents().add(cf.getComponent());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        ComponentsFeature csf = (ComponentsFeature) this.features.get(ComponentsFeature.class);
        if (csf != null) {
            for (ComponentFeature cfi : csf.getComponentFeatures()) {
                switch (cfi.getTarget()) {
                    case SERVICE:
                        getComponents().add(cfi.getComponent());
                        break;
                    case CONTAINER:
                        this.container.getComponents().add(cfi.getComponent());
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }
        ServiceInterceptor interceptor = ServiceInterceptorFactory.load(this, Thread.currentThread().getContextClassLoader());
        ServiceInterceptor si = (ServiceInterceptor) this.container.getSPI(ServiceInterceptor.class);
        this.serviceInterceptor = si != null ? ServiceInterceptor.aggregate(interceptor, si) : interceptor;
        if (service == null) {
            if (wsdl == null && serviceClass != Service.class) {
                WebServiceClient wsClient = (WebServiceClient) AccessController.doPrivileged(new PrivilegedAction<WebServiceClient>() { // from class: com.sun.xml.internal.ws.client.WSServiceDelegate.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public WebServiceClient run2() {
                        return (WebServiceClient) serviceClass.getAnnotation(WebServiceClient.class);
                    }
                });
                String wsdlLocation = wsClient.wsdlLocation();
                wsdl = new StreamSource(JAXWSUtils.absolutize(JAXWSUtils.getFileOrURLName(wsdlLocation)));
            }
            if (wsdl != null) {
                try {
                    URL url = wsdl.getSystemId() == null ? null : JAXWSUtils.getEncodedURL(wsdl.getSystemId());
                    WSDLModel model = parseWSDL(url, wsdl, serviceClass);
                    service = model.getService(this.serviceName);
                    if (service == null) {
                        throw new WebServiceException(ClientMessages.INVALID_SERVICE_NAME(this.serviceName, buildNameList(model.getServices().keySet())));
                    }
                    for (WSDLPort port : service.getPorts()) {
                        this.ports.put(port.getName(), new PortInfo(this, port));
                    }
                } catch (MalformedURLException e2) {
                    throw new WebServiceException(ClientMessages.INVALID_WSDL_URL(wsdl.getSystemId()));
                }
            }
        } else {
            for (WSDLPort port2 : service.getPorts()) {
                this.ports.put(port2.getName(), new PortInfo(this, port2));
            }
        }
        this.wsdlService = service;
        if (serviceClass != Service.class) {
            HandlerChain handlerChain = (HandlerChain) AccessController.doPrivileged(new PrivilegedAction<HandlerChain>() { // from class: com.sun.xml.internal.ws.client.WSServiceDelegate.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public HandlerChain run2() {
                    return (HandlerChain) serviceClass.getAnnotation(HandlerChain.class);
                }
            });
            if (handlerChain != null) {
                this.handlerConfigurator = new HandlerConfigurator.AnnotationConfigurator(this);
            }
        }
    }

    private WSDLModel parseWSDL(URL wsdlDocumentLocation, Source wsdlSource, Class serviceClass) {
        try {
            return RuntimeWSDLParser.parse(wsdlDocumentLocation, wsdlSource, createCatalogResolver(), true, getContainer(), serviceClass, (WSDLParserExtension[]) ServiceFinder.find(WSDLParserExtension.class).toArray());
        } catch (ServiceConfigurationError e2) {
            throw new WebServiceException(e2);
        } catch (IOException e3) {
            throw new WebServiceException(e3);
        } catch (XMLStreamException e4) {
            throw new WebServiceException(e4);
        } catch (SAXException e5) {
            throw new WebServiceException(e5);
        }
    }

    protected EntityResolver createCatalogResolver() {
        return XmlUtil.createDefaultCatalogResolver();
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public Executor getExecutor() {
        return this.executor;
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public HandlerResolver getHandlerResolver() {
        return this.handlerConfigurator.getResolver();
    }

    final HandlerConfigurator getHandlerConfigurator() {
        return this.handlerConfigurator;
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public void setHandlerResolver(HandlerResolver resolver) {
        this.handlerConfigurator = new HandlerConfigurator.HandlerResolverImpl(resolver);
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public <T> T getPort(QName qName, Class<T> cls) throws WebServiceException {
        return (T) getPort(qName, cls, EMPTY_FEATURES);
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public <T> T getPort(QName qName, Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        if (qName == null || cls == null) {
            throw new IllegalArgumentException();
        }
        WSDLService wSDLModelfromSEI = this.wsdlService;
        if (wSDLModelfromSEI == null) {
            wSDLModelfromSEI = getWSDLModelfromSEI(cls);
            if (wSDLModelfromSEI == null) {
                throw new WebServiceException(ProviderApiMessages.NO_WSDL_NO_PORT(cls.getName()));
            }
        }
        return (T) getPort(getPortModel(wSDLModelfromSEI, qName).getEPR(), qName, cls, new WebServiceFeatureList(webServiceFeatureArr));
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public <T> T getPort(EndpointReference endpointReference, Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        return (T) getPort(WSEndpointReference.create(endpointReference), cls, webServiceFeatureArr);
    }

    @Override // com.sun.xml.internal.ws.api.WSService
    public <T> T getPort(WSEndpointReference wSEndpointReference, Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        WebServiceFeatureList webServiceFeatureList = new WebServiceFeatureList(webServiceFeatureArr);
        return (T) getPort(wSEndpointReference, getPortNameFromEPR(wSEndpointReference, RuntimeModeler.getPortTypeName(cls, getMetadadaReader(webServiceFeatureList, cls.getClassLoader()))), cls, webServiceFeatureList);
    }

    protected <T> T getPort(WSEndpointReference wSEndpointReference, QName qName, Class<T> cls, WebServiceFeatureList webServiceFeatureList) {
        ComponentFeature componentFeature = (ComponentFeature) webServiceFeatureList.get(ComponentFeature.class);
        if (componentFeature != null && !ComponentFeature.Target.STUB.equals(componentFeature.getTarget())) {
            throw new IllegalArgumentException();
        }
        ComponentsFeature componentsFeature = (ComponentsFeature) webServiceFeatureList.get(ComponentsFeature.class);
        if (componentsFeature != null) {
            Iterator<ComponentFeature> it = componentsFeature.getComponentFeatures().iterator();
            while (it.hasNext()) {
                if (!ComponentFeature.Target.STUB.equals(it.next().getTarget())) {
                    throw new IllegalArgumentException();
                }
            }
        }
        webServiceFeatureList.addAll(this.features);
        return (T) createEndpointIFBaseProxy(wSEndpointReference, qName, cls, webServiceFeatureList, addSEI(qName, cls, webServiceFeatureList));
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public <T> T getPort(Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        QName portTypeName = RuntimeModeler.getPortTypeName(cls, getMetadadaReader(new WebServiceFeatureList(webServiceFeatureArr), cls.getClassLoader()));
        WSDLService wSDLModelfromSEI = this.wsdlService;
        if (wSDLModelfromSEI == null) {
            wSDLModelfromSEI = getWSDLModelfromSEI(cls);
            if (wSDLModelfromSEI == null) {
                throw new WebServiceException(ProviderApiMessages.NO_WSDL_NO_PORT(cls.getName()));
            }
        }
        WSDLPort matchingPort = wSDLModelfromSEI.getMatchingPort(portTypeName);
        if (matchingPort == null) {
            throw new WebServiceException(ClientMessages.UNDEFINED_PORT_TYPE(portTypeName));
        }
        return (T) getPort(matchingPort.getName(), cls, webServiceFeatureArr);
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public <T> T getPort(Class<T> cls) throws WebServiceException {
        return (T) getPort(cls, EMPTY_FEATURES);
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public void addPort(QName portName, String bindingId, String endpointAddress) throws WebServiceException {
        if (!this.ports.containsKey(portName)) {
            BindingID bid = bindingId == null ? BindingID.SOAP11_HTTP : BindingID.parse(bindingId);
            this.ports.put(portName, new PortInfo(this, endpointAddress == null ? null : EndpointAddress.create(endpointAddress), portName, bid));
            return;
        }
        throw new WebServiceException(DispatchMessages.DUPLICATE_PORT(portName.toString()));
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public <T> Dispatch<T> createDispatch(QName portName, Class<T> aClass, Service.Mode mode) throws WebServiceException {
        return createDispatch(portName, aClass, mode, EMPTY_FEATURES);
    }

    @Override // com.sun.xml.internal.ws.api.WSService
    public <T> Dispatch<T> createDispatch(QName portName, WSEndpointReference wsepr, Class<T> aClass, Service.Mode mode, WebServiceFeature... features) {
        return createDispatch(portName, wsepr, aClass, mode, new WebServiceFeatureList(features));
    }

    public <T> Dispatch<T> createDispatch(QName portName, WSEndpointReference wsepr, Class<T> aClass, Service.Mode mode, WebServiceFeatureList features) {
        PortInfo port = safeGetPort(portName);
        ComponentFeature cf = (ComponentFeature) features.get(ComponentFeature.class);
        if (cf != null && !ComponentFeature.Target.STUB.equals(cf.getTarget())) {
            throw new IllegalArgumentException();
        }
        ComponentsFeature csf = (ComponentsFeature) features.get(ComponentsFeature.class);
        if (csf != null) {
            for (ComponentFeature cfi : csf.getComponentFeatures()) {
                if (!ComponentFeature.Target.STUB.equals(cfi.getTarget())) {
                    throw new IllegalArgumentException();
                }
            }
        }
        features.addAll(this.features);
        BindingImpl binding = port.createBinding(features, null, null);
        binding.setMode(mode);
        Dispatch<T> dispatch = Stubs.createDispatch(port, this, binding, aClass, mode, wsepr);
        this.serviceInterceptor.postCreateDispatch((WSBindingProvider) dispatch);
        return dispatch;
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public <T> Dispatch<T> createDispatch(QName portName, Class<T> aClass, Service.Mode mode, WebServiceFeature... features) {
        return createDispatch(portName, aClass, mode, new WebServiceFeatureList(features));
    }

    public <T> Dispatch<T> createDispatch(QName portName, Class<T> aClass, Service.Mode mode, WebServiceFeatureList features) {
        WSEndpointReference wsepr = null;
        boolean isAddressingEnabled = false;
        AddressingFeature af2 = (AddressingFeature) features.get(AddressingFeature.class);
        if (af2 == null) {
            af2 = (AddressingFeature) this.features.get(AddressingFeature.class);
        }
        if (af2 != null && af2.isEnabled()) {
            isAddressingEnabled = true;
        }
        MemberSubmissionAddressingFeature msa = (MemberSubmissionAddressingFeature) features.get(MemberSubmissionAddressingFeature.class);
        if (msa == null) {
            msa = (MemberSubmissionAddressingFeature) this.features.get(MemberSubmissionAddressingFeature.class);
        }
        if (msa != null && msa.isEnabled()) {
            isAddressingEnabled = true;
        }
        if (isAddressingEnabled && this.wsdlService != null && this.wsdlService.get(portName) != null) {
            wsepr = this.wsdlService.get(portName).getEPR();
        }
        return createDispatch(portName, wsepr, aClass, mode, features);
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public <T> Dispatch<T> createDispatch(EndpointReference endpointReference, Class<T> type, Service.Mode mode, WebServiceFeature... features) {
        WSEndpointReference wsepr = new WSEndpointReference(endpointReference);
        QName portName = addPortEpr(wsepr);
        return createDispatch(portName, wsepr, type, mode, features);
    }

    @NotNull
    public PortInfo safeGetPort(QName portName) {
        PortInfo port = this.ports.get(portName);
        if (port == null) {
            throw new WebServiceException(ClientMessages.INVALID_PORT_NAME(portName, buildNameList(this.ports.keySet())));
        }
        return port;
    }

    private StringBuilder buildNameList(Collection<QName> names) {
        StringBuilder sb = new StringBuilder();
        for (QName qn : names) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append((Object) qn);
        }
        return sb;
    }

    public EndpointAddress getEndpointAddress(QName qName) {
        PortInfo p2 = this.ports.get(qName);
        if (p2 != null) {
            return p2.targetEndpoint;
        }
        return null;
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public Dispatch<Object> createDispatch(QName portName, JAXBContext jaxbContext, Service.Mode mode) throws WebServiceException {
        return createDispatch(portName, jaxbContext, mode, EMPTY_FEATURES);
    }

    @Override // com.sun.xml.internal.ws.api.WSService
    public Dispatch<Object> createDispatch(QName portName, WSEndpointReference wsepr, JAXBContext jaxbContext, Service.Mode mode, WebServiceFeature... features) {
        return createDispatch(portName, wsepr, jaxbContext, mode, new WebServiceFeatureList(features));
    }

    protected Dispatch<Object> createDispatch(QName portName, WSEndpointReference wsepr, JAXBContext jaxbContext, Service.Mode mode, WebServiceFeatureList features) {
        PortInfo port = safeGetPort(portName);
        ComponentFeature cf = (ComponentFeature) features.get(ComponentFeature.class);
        if (cf != null && !ComponentFeature.Target.STUB.equals(cf.getTarget())) {
            throw new IllegalArgumentException();
        }
        ComponentsFeature csf = (ComponentsFeature) features.get(ComponentsFeature.class);
        if (csf != null) {
            for (ComponentFeature cfi : csf.getComponentFeatures()) {
                if (!ComponentFeature.Target.STUB.equals(cfi.getTarget())) {
                    throw new IllegalArgumentException();
                }
            }
        }
        features.addAll(this.features);
        BindingImpl binding = port.createBinding(features, null, null);
        binding.setMode(mode);
        Dispatch<Object> dispatch = Stubs.createJAXBDispatch(port, binding, jaxbContext, mode, wsepr);
        this.serviceInterceptor.postCreateDispatch((WSBindingProvider) dispatch);
        return dispatch;
    }

    @Override // com.sun.xml.internal.ws.api.WSService
    @NotNull
    public Container getContainer() {
        return this.container;
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public Dispatch<Object> createDispatch(QName portName, JAXBContext jaxbContext, Service.Mode mode, WebServiceFeature... webServiceFeatures) {
        return createDispatch(portName, jaxbContext, mode, new WebServiceFeatureList(webServiceFeatures));
    }

    protected Dispatch<Object> createDispatch(QName portName, JAXBContext jaxbContext, Service.Mode mode, WebServiceFeatureList features) {
        WSEndpointReference wsepr = null;
        boolean isAddressingEnabled = false;
        AddressingFeature af2 = (AddressingFeature) features.get(AddressingFeature.class);
        if (af2 == null) {
            af2 = (AddressingFeature) this.features.get(AddressingFeature.class);
        }
        if (af2 != null && af2.isEnabled()) {
            isAddressingEnabled = true;
        }
        MemberSubmissionAddressingFeature msa = (MemberSubmissionAddressingFeature) features.get(MemberSubmissionAddressingFeature.class);
        if (msa == null) {
            msa = (MemberSubmissionAddressingFeature) this.features.get(MemberSubmissionAddressingFeature.class);
        }
        if (msa != null && msa.isEnabled()) {
            isAddressingEnabled = true;
        }
        if (isAddressingEnabled && this.wsdlService != null && this.wsdlService.get(portName) != null) {
            wsepr = this.wsdlService.get(portName).getEPR();
        }
        return createDispatch(portName, wsepr, jaxbContext, mode, features);
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public Dispatch<Object> createDispatch(EndpointReference endpointReference, JAXBContext context, Service.Mode mode, WebServiceFeature... features) {
        WSEndpointReference wsepr = new WSEndpointReference(endpointReference);
        QName portName = addPortEpr(wsepr);
        return createDispatch(portName, wsepr, context, mode, features);
    }

    private QName addPortEpr(WSEndpointReference wsepr) {
        if (wsepr == null) {
            throw new WebServiceException(ProviderApiMessages.NULL_EPR());
        }
        QName eprPortName = getPortNameFromEPR(wsepr, null);
        PortInfo portInfo = new PortInfo(this, wsepr.getAddress() == null ? null : EndpointAddress.create(wsepr.getAddress()), eprPortName, getPortModel(this.wsdlService, eprPortName).getBinding().getBindingId());
        if (!this.ports.containsKey(eprPortName)) {
            this.ports.put(eprPortName, portInfo);
        }
        return eprPortName;
    }

    private QName getPortNameFromEPR(@NotNull WSEndpointReference wsepr, @Nullable QName portTypeName) {
        WSEndpointReference.Metadata metadata = wsepr.getMetaData();
        QName eprServiceName = metadata.getServiceName();
        QName eprPortName = metadata.getPortName();
        if (eprServiceName != null && !eprServiceName.equals(this.serviceName)) {
            throw new WebServiceException("EndpointReference WSDL ServiceName differs from Service Instance WSDL Service QName.\n The two Service QNames must match");
        }
        if (this.wsdlService == null) {
            Source eprWsdlSource = metadata.getWsdlSource();
            if (eprWsdlSource == null) {
                throw new WebServiceException(ProviderApiMessages.NULL_WSDL());
            }
            try {
                WSDLModel eprWsdlMdl = parseWSDL(new URL(wsepr.getAddress()), eprWsdlSource, null);
                this.wsdlService = eprWsdlMdl.getService(this.serviceName);
                if (this.wsdlService == null) {
                    throw new WebServiceException(ClientMessages.INVALID_SERVICE_NAME(this.serviceName, buildNameList(eprWsdlMdl.getServices().keySet())));
                }
            } catch (MalformedURLException e2) {
                throw new WebServiceException(ClientMessages.INVALID_ADDRESS(wsepr.getAddress()));
            }
        }
        QName portName = eprPortName;
        if (portName == null && portTypeName != null) {
            WSDLPort port = this.wsdlService.getMatchingPort(portTypeName);
            if (port == null) {
                throw new WebServiceException(ClientMessages.UNDEFINED_PORT_TYPE(portTypeName));
            }
            portName = port.getName();
        }
        if (portName == null) {
            throw new WebServiceException(ProviderApiMessages.NULL_PORTNAME());
        }
        if (this.wsdlService.get(portName) == null) {
            throw new WebServiceException(ClientMessages.INVALID_EPR_PORT_NAME(portName, buildWsdlPortNames()));
        }
        return portName;
    }

    private <T> T createProxy(final Class<T> cls, final InvocationHandler invocationHandler) {
        final ClassLoader delegatingLoader = getDelegatingLoader(cls.getClassLoader(), WSServiceDelegate.class.getClassLoader());
        Permission runtimePermission = new RuntimePermission("accessClassInPackage.com.sun.xml.internal.*");
        PermissionCollection permissionCollectionNewPermissionCollection = runtimePermission.newPermissionCollection();
        permissionCollectionNewPermissionCollection.add(runtimePermission);
        return (T) AccessController.doPrivileged(new PrivilegedAction<T>() { // from class: com.sun.xml.internal.ws.client.WSServiceDelegate.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public T run2() {
                return (T) cls.cast(Proxy.newProxyInstance(delegatingLoader, new Class[]{cls, WSBindingProvider.class, Closeable.class}, invocationHandler));
            }
        }, new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissionCollectionNewPermissionCollection)}));
    }

    private WSDLService getWSDLModelfromSEI(final Class sei) {
        WebService ws = (WebService) AccessController.doPrivileged(new PrivilegedAction<WebService>() { // from class: com.sun.xml.internal.ws.client.WSServiceDelegate.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public WebService run2() {
                return (WebService) sei.getAnnotation(WebService.class);
            }
        });
        if (ws == null || ws.wsdlLocation().equals("")) {
            return null;
        }
        String wsdlLocation = ws.wsdlLocation();
        Source wsdl = new StreamSource(JAXWSUtils.absolutize(JAXWSUtils.getFileOrURLName(wsdlLocation)));
        try {
            URL url = wsdl.getSystemId() == null ? null : new URL(wsdl.getSystemId());
            WSDLModel model = parseWSDL(url, wsdl, sei);
            WSDLService service = model.getService(this.serviceName);
            if (service == null) {
                throw new WebServiceException(ClientMessages.INVALID_SERVICE_NAME(this.serviceName, buildNameList(model.getServices().keySet())));
            }
            return service;
        } catch (MalformedURLException e2) {
            throw new WebServiceException(ClientMessages.INVALID_WSDL_URL(wsdl.getSystemId()));
        }
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public QName getServiceName() {
        return this.serviceName;
    }

    public Class getServiceClass() {
        return this.serviceClass;
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public Iterator<QName> getPorts() throws WebServiceException {
        return this.ports.keySet().iterator();
    }

    @Override // javax.xml.ws.spi.ServiceDelegate
    public URL getWSDLDocumentLocation() {
        if (this.wsdlService == null) {
            return null;
        }
        try {
            return new URL(this.wsdlService.getParent().getLocation().getSystemId());
        } catch (MalformedURLException e2) {
            throw new AssertionError(e2);
        }
    }

    private <T> T createEndpointIFBaseProxy(@Nullable WSEndpointReference wSEndpointReference, QName qName, Class<T> cls, WebServiceFeatureList webServiceFeatureList, SEIPortInfo sEIPortInfo) {
        if (this.wsdlService == null) {
            throw new WebServiceException(ClientMessages.INVALID_SERVICE_NO_WSDL(this.serviceName));
        }
        if (this.wsdlService.get(qName) == null) {
            throw new WebServiceException(ClientMessages.INVALID_PORT_NAME(qName, buildWsdlPortNames()));
        }
        T t2 = (T) createProxy(cls, getStubHandler(sEIPortInfo.createBinding(webServiceFeatureList, (Class<?>) cls), sEIPortInfo, wSEndpointReference));
        if (this.serviceInterceptor != null) {
            this.serviceInterceptor.postCreateProxy((WSBindingProvider) t2, cls);
        }
        return t2;
    }

    protected InvocationHandler getStubHandler(BindingImpl binding, SEIPortInfo eif, @Nullable WSEndpointReference epr) {
        return new SEIStub(eif, binding, eif.model, epr);
    }

    private StringBuilder buildWsdlPortNames() {
        Set<QName> wsdlPortNames = new HashSet<>();
        for (WSDLPort port : this.wsdlService.getPorts()) {
            wsdlPortNames.add(port.getName());
        }
        return buildNameList(wsdlPortNames);
    }

    @NotNull
    public WSDLPort getPortModel(WSDLService wsdlService, QName portName) {
        WSDLPort port = wsdlService.get(portName);
        if (port == null) {
            throw new WebServiceException(ClientMessages.INVALID_PORT_NAME(portName, buildWsdlPortNames()));
        }
        return port;
    }

    private SEIPortInfo addSEI(QName portName, Class portInterface, WebServiceFeatureList features) throws WebServiceException {
        boolean ownModel = useOwnSEIModel(features);
        if (ownModel) {
            return createSEIPortInfo(portName, portInterface, features);
        }
        SEIPortInfo spi = this.seiContext.get(portName);
        if (spi == null) {
            spi = createSEIPortInfo(portName, portInterface, features);
            this.seiContext.put(spi.portName, spi);
            this.ports.put(spi.portName, spi);
        }
        return spi;
    }

    public SEIModel buildRuntimeModel(QName serviceName, QName portName, Class portInterface, WSDLPort wsdlPort, WebServiceFeatureList features) {
        DatabindingFactory fac = DatabindingFactory.newInstance();
        DatabindingConfig config = new DatabindingConfig();
        config.setContractClass(portInterface);
        config.getMappingInfo().setServiceName(serviceName);
        config.setWsdlPort(wsdlPort);
        config.setFeatures(features);
        config.setClassLoader(portInterface.getClassLoader());
        config.getMappingInfo().setPortName(portName);
        config.setWsdlURL(this.wsdlURL);
        config.setMetadataReader(getMetadadaReader(features, portInterface.getClassLoader()));
        DatabindingImpl rt = (DatabindingImpl) fac.createRuntime(config);
        return rt.getModel();
    }

    private MetadataReader getMetadadaReader(WebServiceFeatureList features, ClassLoader classLoader) {
        ExternalMetadataFeature ef;
        if (features != null && (ef = (ExternalMetadataFeature) features.get(ExternalMetadataFeature.class)) != null) {
            return ef.getMetadataReader(classLoader, false);
        }
        return null;
    }

    private SEIPortInfo createSEIPortInfo(QName portName, Class portInterface, WebServiceFeatureList features) {
        WSDLPort wsdlPort = getPortModel(this.wsdlService, portName);
        SEIModel model = buildRuntimeModel(this.serviceName, portName, portInterface, wsdlPort, features);
        return new SEIPortInfo(this, portInterface, (SOAPSEIModel) model, wsdlPort);
    }

    private boolean useOwnSEIModel(WebServiceFeatureList features) {
        return features.contains(UsesJAXBContextFeature.class);
    }

    public WSDLService getWsdlService() {
        return this.wsdlService;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/WSServiceDelegate$DaemonThreadFactory.class */
    static class DaemonThreadFactory implements ThreadFactory {
        DaemonThreadFactory() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r2) {
            Thread daemonThread = new Thread(r2);
            daemonThread.setDaemon(Boolean.TRUE.booleanValue());
            return daemonThread;
        }
    }

    private static ClassLoader getDelegatingLoader(ClassLoader loader1, ClassLoader loader2) {
        return loader1 == null ? loader2 : loader2 == null ? loader1 : new DelegatingLoader(loader1, loader2);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/WSServiceDelegate$DelegatingLoader.class */
    private static final class DelegatingLoader extends ClassLoader {
        private final ClassLoader loader;

        public int hashCode() {
            int result = (31 * 1) + (this.loader == null ? 0 : this.loader.hashCode());
            return (31 * result) + (getParent() == null ? 0 : getParent().hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            DelegatingLoader other = (DelegatingLoader) obj;
            if (this.loader == null) {
                if (other.loader != null) {
                    return false;
                }
            } else if (!this.loader.equals(other.loader)) {
                return false;
            }
            if (getParent() == null) {
                if (other.getParent() != null) {
                    return false;
                }
                return true;
            }
            if (!getParent().equals(other.getParent())) {
                return false;
            }
            return true;
        }

        DelegatingLoader(ClassLoader loader1, ClassLoader loader2) {
            super(loader2);
            this.loader = loader1;
        }

        @Override // java.lang.ClassLoader
        protected Class findClass(String name) throws ClassNotFoundException {
            return this.loader.loadClass(name);
        }

        @Override // java.lang.ClassLoader
        protected URL findResource(String name) {
            return this.loader.getResource(name);
        }
    }
}
