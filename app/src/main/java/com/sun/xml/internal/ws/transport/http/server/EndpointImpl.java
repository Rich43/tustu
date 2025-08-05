package com.sun.xml.internal.ws.transport.http.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.net.httpserver.HttpContext;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.InstanceResolver;
import com.sun.xml.internal.ws.api.server.Invoker;
import com.sun.xml.internal.ws.api.server.SDDocumentSource;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.server.EndpointFactory;
import com.sun.xml.internal.ws.server.ServerRtException;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import com.sun.xml.internal.ws.transport.http.HttpAdapterList;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.ws.Binding;
import javax.xml.ws.Endpoint;
import javax.xml.ws.EndpointContext;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.WebServicePermission;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/EndpointImpl.class */
public class EndpointImpl extends Endpoint {
    private static final WebServicePermission ENDPOINT_PUBLISH_PERMISSION;
    private Object actualEndpoint;
    private final WSBinding binding;

    @Nullable
    private final Object implementor;
    private List<Source> metadata;
    private Executor executor;
    private Map<String, Object> properties;
    private boolean stopped;

    @Nullable
    private EndpointContext endpointContext;

    @NotNull
    private final Class<?> implClass;
    private final Invoker invoker;
    private Container container;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EndpointImpl.class.desiredAssertionStatus();
        ENDPOINT_PUBLISH_PERMISSION = new WebServicePermission("publishEndpoint");
    }

    public EndpointImpl(@NotNull BindingID bindingId, @NotNull Object impl, WebServiceFeature... features) {
        this(bindingId, impl, impl.getClass(), InstanceResolver.createSingleton(impl).createInvoker(), features);
    }

    public EndpointImpl(@NotNull BindingID bindingId, @NotNull Class implClass, javax.xml.ws.spi.Invoker invoker, WebServiceFeature... features) {
        this(bindingId, null, implClass, new InvokerImpl(invoker), features);
    }

    private EndpointImpl(@NotNull BindingID bindingId, Object impl, @NotNull Class implClass, Invoker invoker, WebServiceFeature... features) {
        this.properties = Collections.emptyMap();
        this.binding = BindingImpl.create(bindingId, features);
        this.implClass = implClass;
        this.invoker = invoker;
        this.implementor = impl;
    }

    public EndpointImpl(WSEndpoint wse, Object serverContext) {
        this(wse, serverContext, (EndpointContext) null);
    }

    public EndpointImpl(WSEndpoint wse, Object serverContext, EndpointContext ctxt) {
        this.properties = Collections.emptyMap();
        this.endpointContext = ctxt;
        this.actualEndpoint = new HttpEndpoint(null, getAdapter(wse, ""));
        ((HttpEndpoint) this.actualEndpoint).publish(serverContext);
        this.binding = wse.getBinding();
        this.implementor = null;
        this.implClass = null;
        this.invoker = null;
    }

    public EndpointImpl(WSEndpoint wse, String address) {
        this(wse, address, (EndpointContext) null);
    }

    public EndpointImpl(WSEndpoint wse, String address, EndpointContext ctxt) {
        this.properties = Collections.emptyMap();
        try {
            URL url = new URL(address);
            if (!url.getProtocol().equals("http")) {
                throw new IllegalArgumentException(url.getProtocol() + " protocol based address is not supported");
            }
            if (!url.getPath().startsWith("/")) {
                throw new IllegalArgumentException("Incorrect WebService address=" + address + ". The address's path should start with /");
            }
            this.endpointContext = ctxt;
            this.actualEndpoint = new HttpEndpoint(null, getAdapter(wse, url.getPath()));
            ((HttpEndpoint) this.actualEndpoint).publish(address);
            this.binding = wse.getBinding();
            this.implementor = null;
            this.implClass = null;
            this.invoker = null;
        } catch (MalformedURLException e2) {
            throw new IllegalArgumentException("Cannot create URL for this address " + address);
        }
    }

    @Override // javax.xml.ws.Endpoint
    public Binding getBinding() {
        return this.binding;
    }

    @Override // javax.xml.ws.Endpoint
    public Object getImplementor() {
        return this.implementor;
    }

    @Override // javax.xml.ws.Endpoint
    public void publish(String address) {
        canPublish();
        try {
            URL url = new URL(address);
            if (!url.getProtocol().equals("http")) {
                throw new IllegalArgumentException(url.getProtocol() + " protocol based address is not supported");
            }
            if (!url.getPath().startsWith("/")) {
                throw new IllegalArgumentException("Incorrect WebService address=" + address + ". The address's path should start with /");
            }
            createEndpoint(url.getPath());
            ((HttpEndpoint) this.actualEndpoint).publish(address);
        } catch (MalformedURLException e2) {
            throw new IllegalArgumentException("Cannot create URL for this address " + address);
        }
    }

    @Override // javax.xml.ws.Endpoint
    public void publish(Object serverContext) {
        canPublish();
        if (!HttpContext.class.isAssignableFrom(serverContext.getClass())) {
            throw new IllegalArgumentException(((Object) serverContext.getClass()) + " is not a supported context.");
        }
        createEndpoint(((HttpContext) serverContext).getPath());
        ((HttpEndpoint) this.actualEndpoint).publish(serverContext);
    }

    @Override // javax.xml.ws.Endpoint
    public void publish(javax.xml.ws.spi.http.HttpContext serverContext) {
        canPublish();
        createEndpoint(serverContext.getPath());
        ((HttpEndpoint) this.actualEndpoint).publish(serverContext);
    }

    @Override // javax.xml.ws.Endpoint
    public void stop() {
        if (isPublished()) {
            ((HttpEndpoint) this.actualEndpoint).stop();
            this.actualEndpoint = null;
            this.stopped = true;
        }
    }

    @Override // javax.xml.ws.Endpoint
    public boolean isPublished() {
        return this.actualEndpoint != null;
    }

    @Override // javax.xml.ws.Endpoint
    public List<Source> getMetadata() {
        return this.metadata;
    }

    @Override // javax.xml.ws.Endpoint
    public void setMetadata(List<Source> metadata) {
        if (isPublished()) {
            throw new IllegalStateException("Cannot set Metadata. Endpoint is already published");
        }
        this.metadata = metadata;
    }

    @Override // javax.xml.ws.Endpoint
    public Executor getExecutor() {
        return this.executor;
    }

    @Override // javax.xml.ws.Endpoint
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override // javax.xml.ws.Endpoint
    public Map<String, Object> getProperties() {
        return new HashMap(this.properties);
    }

    @Override // javax.xml.ws.Endpoint
    public void setProperties(Map<String, Object> map) {
        this.properties = new HashMap(map);
    }

    private void createEndpoint(String urlPattern) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(ENDPOINT_PUBLISH_PERMISSION);
        }
        try {
            Class.forName("com.sun.net.httpserver.HttpServer");
            this.container = getContainer();
            MetadataReader metadataReader = EndpointFactory.getExternalMetadatReader(this.implClass, this.binding);
            WSEndpoint wse = WSEndpoint.create(this.implClass, true, this.invoker, (QName) getProperty(QName.class, "javax.xml.ws.wsdl.service"), (QName) getProperty(QName.class, "javax.xml.ws.wsdl.port"), this.container, this.binding, getPrimaryWsdl(metadataReader), buildDocList(), (EntityResolver) null, false);
            this.actualEndpoint = new HttpEndpoint(this.executor, getAdapter(wse, urlPattern));
        } catch (Exception e2) {
            throw new UnsupportedOperationException("Couldn't load light weight http server", e2);
        }
    }

    private <T> T getProperty(Class<T> type, String key) {
        Object o2 = this.properties.get(key);
        if (o2 == null) {
            return null;
        }
        if (type.isInstance(o2)) {
            return type.cast(o2);
        }
        throw new IllegalArgumentException("Property " + key + " has to be of type " + ((Object) type));
    }

    private List<SDDocumentSource> buildDocList() {
        List<SDDocumentSource> r2 = new ArrayList<>();
        if (this.metadata != null) {
            for (Source source : this.metadata) {
                try {
                    XMLStreamBufferResult xsbr = (XMLStreamBufferResult) XmlUtil.identityTransform(source, new XMLStreamBufferResult());
                    String systemId = source.getSystemId();
                    r2.add(SDDocumentSource.create(new URL(systemId), xsbr.getXMLStreamBuffer()));
                } catch (IOException te) {
                    throw new ServerRtException("server.rt.err", te);
                } catch (ParserConfigurationException e2) {
                    throw new ServerRtException("server.rt.err", e2);
                } catch (TransformerException te2) {
                    throw new ServerRtException("server.rt.err", te2);
                } catch (SAXException e3) {
                    throw new ServerRtException("server.rt.err", e3);
                }
            }
        }
        return r2;
    }

    @Nullable
    private SDDocumentSource getPrimaryWsdl(MetadataReader metadataReader) {
        EndpointFactory.verifyImplementorClass(this.implClass, metadataReader);
        String wsdlLocation = EndpointFactory.getWsdlLocation(this.implClass, metadataReader);
        if (wsdlLocation != null) {
            ClassLoader cl = this.implClass.getClassLoader();
            URL url = cl.getResource(wsdlLocation);
            if (url != null) {
                return SDDocumentSource.create(url);
            }
            throw new ServerRtException("cannot.load.wsdl", wsdlLocation);
        }
        return null;
    }

    private void canPublish() {
        if (isPublished()) {
            throw new IllegalStateException("Cannot publish this endpoint. Endpoint has been already published.");
        }
        if (this.stopped) {
            throw new IllegalStateException("Cannot publish this endpoint. Endpoint has been already stopped.");
        }
    }

    @Override // javax.xml.ws.Endpoint
    public EndpointReference getEndpointReference(Element... referenceParameters) {
        return getEndpointReference(W3CEndpointReference.class, referenceParameters);
    }

    @Override // javax.xml.ws.Endpoint
    public <T extends EndpointReference> T getEndpointReference(Class<T> cls, Element... elementArr) {
        if (!isPublished()) {
            throw new WebServiceException("Endpoint is not published yet");
        }
        return (T) ((HttpEndpoint) this.actualEndpoint).getEndpointReference(cls, elementArr);
    }

    @Override // javax.xml.ws.Endpoint
    public void setEndpointContext(EndpointContext ctxt) {
        this.endpointContext = ctxt;
    }

    private HttpAdapter getAdapter(WSEndpoint endpoint, String urlPattern) {
        HttpAdapterList adapterList = null;
        if (this.endpointContext != null) {
            if (this.endpointContext instanceof Component) {
                adapterList = (HttpAdapterList) ((Component) this.endpointContext).getSPI(HttpAdapterList.class);
            }
            if (adapterList == null) {
                Iterator<Endpoint> it = this.endpointContext.getEndpoints().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Endpoint e2 = it.next();
                    if (e2.isPublished() && e2 != this) {
                        adapterList = ((HttpEndpoint) ((EndpointImpl) e2).actualEndpoint).getAdapterOwner();
                        if (!$assertionsDisabled && adapterList == null) {
                            throw new AssertionError();
                        }
                    }
                }
            }
        }
        if (adapterList == null) {
            adapterList = new ServerAdapterList();
        }
        return adapterList.createAdapter("", urlPattern, (WSEndpoint<?>) endpoint);
    }

    private Container getContainer() {
        Container c2;
        if (this.endpointContext != null) {
            if ((this.endpointContext instanceof Component) && (c2 = (Container) ((Component) this.endpointContext).getSPI(Container.class)) != null) {
                return c2;
            }
            for (Endpoint e2 : this.endpointContext.getEndpoints()) {
                if (e2.isPublished() && e2 != this) {
                    return ((EndpointImpl) e2).container;
                }
            }
        }
        return new ServerContainer();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/EndpointImpl$InvokerImpl.class */
    private static class InvokerImpl extends Invoker {
        private javax.xml.ws.spi.Invoker spiInvoker;

        InvokerImpl(javax.xml.ws.spi.Invoker spiInvoker) {
            this.spiInvoker = spiInvoker;
        }

        @Override // com.sun.xml.internal.ws.api.server.Invoker
        public void start(@NotNull WSWebServiceContext wsc, @NotNull WSEndpoint endpoint) throws IllegalArgumentException {
            try {
                this.spiInvoker.inject(wsc);
            } catch (IllegalAccessException e2) {
                throw new WebServiceException(e2);
            } catch (InvocationTargetException e3) {
                throw new WebServiceException(e3);
            }
        }

        @Override // com.sun.xml.internal.ws.server.sei.Invoker
        public Object invoke(@NotNull Packet p2, @NotNull Method m2, @NotNull Object... args) throws IllegalAccessException, InvocationTargetException {
            return this.spiInvoker.invoke(m2, args);
        }
    }
}
