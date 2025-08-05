package com.sun.xml.internal.ws.db;

import com.oracle.webservices.internal.api.databinding.WSDLGenerator;
import com.oracle.webservices.internal.api.databinding.WSDLResolver;
import com.sun.xml.internal.ws.api.databinding.Databinding;
import com.sun.xml.internal.ws.api.databinding.DatabindingConfig;
import com.sun.xml.internal.ws.api.databinding.WSDLGenInfo;
import com.sun.xml.internal.ws.spi.db.DatabindingProvider;
import java.io.File;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/db/DatabindingProviderImpl.class */
public class DatabindingProviderImpl implements DatabindingProvider {
    private static final String CachedDatabinding = "com.sun.xml.internal.ws.db.DatabindingProviderImpl";
    Map<String, Object> properties;

    @Override // com.sun.xml.internal.ws.spi.db.DatabindingProvider
    public void init(Map<String, Object> p2) {
        this.properties = p2;
    }

    DatabindingImpl getCachedDatabindingImpl(DatabindingConfig config) {
        Object object = config.properties().get(CachedDatabinding);
        if (object == null || !(object instanceof DatabindingImpl)) {
            return null;
        }
        return (DatabindingImpl) object;
    }

    @Override // com.sun.xml.internal.ws.spi.db.DatabindingProvider
    public Databinding create(DatabindingConfig config) {
        DatabindingImpl impl = getCachedDatabindingImpl(config);
        if (impl == null) {
            impl = new DatabindingImpl(this, config);
            config.properties().put(CachedDatabinding, impl);
        }
        return impl;
    }

    @Override // com.sun.xml.internal.ws.spi.db.DatabindingProvider
    public WSDLGenerator wsdlGen(DatabindingConfig config) {
        DatabindingImpl impl = (DatabindingImpl) create(config);
        return new JaxwsWsdlGen(impl);
    }

    @Override // com.sun.xml.internal.ws.spi.db.DatabindingProvider
    public boolean isFor(String databindingMode) {
        return true;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/db/DatabindingProviderImpl$JaxwsWsdlGen.class */
    public static class JaxwsWsdlGen implements WSDLGenerator {
        DatabindingImpl databinding;
        WSDLGenInfo wsdlGenInfo = new WSDLGenInfo();

        JaxwsWsdlGen(DatabindingImpl impl) {
            this.databinding = impl;
        }

        @Override // com.oracle.webservices.internal.api.databinding.WSDLGenerator
        public WSDLGenerator inlineSchema(boolean inline) {
            this.wsdlGenInfo.setInlineSchemas(inline);
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.WSDLGenerator
        public WSDLGenerator property(String name, Object value) {
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.WSDLGenerator
        public void generate(WSDLResolver wsdlResolver) {
            this.wsdlGenInfo.setWsdlResolver(wsdlResolver);
            this.databinding.generateWSDL(this.wsdlGenInfo);
        }

        @Override // com.oracle.webservices.internal.api.databinding.WSDLGenerator
        public void generate(File outputDir, String name) {
            this.databinding.generateWSDL(this.wsdlGenInfo);
        }
    }
}
