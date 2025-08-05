package com.sun.xml.internal.ws.transport.http;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.server.PortAddressResolver;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpAdapterList.class */
public abstract class HttpAdapterList<T extends HttpAdapter> extends AbstractList<T> implements DeploymentDescriptorParser.AdapterFactory<T> {
    private final List<T> adapters = new ArrayList();
    private final Map<PortInfo, String> addressMap = new HashMap();

    protected abstract T createHttpAdapter(String str, String str2, WSEndpoint<?> wSEndpoint);

    @Override // com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser.AdapterFactory
    public /* bridge */ /* synthetic */ Object createAdapter(String str, String str2, WSEndpoint wSEndpoint) {
        return createAdapter(str, str2, (WSEndpoint<?>) wSEndpoint);
    }

    @Override // com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser.AdapterFactory
    public T createAdapter(String str, String str2, WSEndpoint<?> wSEndpoint) {
        T t2 = (T) createHttpAdapter(str, str2, wSEndpoint);
        this.adapters.add(t2);
        WSDLPort port = wSEndpoint.getPort();
        if (port != null) {
            this.addressMap.put(new PortInfo(port.getOwner().getName(), port.getName().getLocalPart(), wSEndpoint.getImplementationClass()), getValidPath(str2));
        }
        return t2;
    }

    private String getValidPath(@NotNull String urlPattern) {
        if (urlPattern.endsWith("/*")) {
            return urlPattern.substring(0, urlPattern.length() - 2);
        }
        return urlPattern;
    }

    public PortAddressResolver createPortAddressResolver(final String baseAddress, final Class<?> endpointImpl) {
        return new PortAddressResolver() { // from class: com.sun.xml.internal.ws.transport.http.HttpAdapterList.1
            @Override // com.sun.xml.internal.ws.api.server.PortAddressResolver
            public String getAddressFor(@NotNull QName serviceName, @NotNull String portName) {
                String urlPattern = (String) HttpAdapterList.this.addressMap.get(new PortInfo(serviceName, portName, endpointImpl));
                if (urlPattern == null) {
                    Iterator it = HttpAdapterList.this.addressMap.entrySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Map.Entry<PortInfo, String> e2 = (Map.Entry) it.next();
                        if (serviceName.equals(e2.getKey().serviceName) && portName.equals(e2.getKey().portName)) {
                            urlPattern = e2.getValue();
                            break;
                        }
                    }
                }
                if (urlPattern == null) {
                    return null;
                }
                return baseAddress + urlPattern;
            }
        };
    }

    @Override // java.util.AbstractList, java.util.List
    public T get(int index) {
        return this.adapters.get(index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.adapters.size();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpAdapterList$PortInfo.class */
    private static class PortInfo {
        private final QName serviceName;
        private final String portName;
        private final Class<?> implClass;

        PortInfo(@NotNull QName serviceName, @NotNull String portName, Class<?> implClass) {
            this.serviceName = serviceName;
            this.portName = portName;
            this.implClass = implClass;
        }

        public boolean equals(Object portInfo) {
            if (portInfo instanceof PortInfo) {
                PortInfo that = (PortInfo) portInfo;
                return this.implClass == null ? this.serviceName.equals(that.serviceName) && this.portName.equals(that.portName) && that.implClass == null : this.serviceName.equals(that.serviceName) && this.portName.equals(that.portName) && this.implClass.equals(that.implClass);
            }
            return false;
        }

        public int hashCode() {
            int retVal = this.serviceName.hashCode() + this.portName.hashCode();
            return this.implClass != null ? retVal + this.implClass.hashCode() : retVal;
        }
    }
}
