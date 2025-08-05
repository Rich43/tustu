package com.sun.xml.internal.ws.db;

import com.oracle.webservices.internal.api.databinding.Databinding;
import com.oracle.webservices.internal.api.databinding.DatabindingModeFeature;
import com.oracle.webservices.internal.api.databinding.WSDLGenerator;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.databinding.DatabindingConfig;
import com.sun.xml.internal.ws.api.databinding.DatabindingFactory;
import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.spi.db.DatabindingProvider;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import org.xml.sax.EntityResolver;

/* loaded from: rt.jar:com/sun/xml/internal/ws/db/DatabindingFactoryImpl.class */
public class DatabindingFactoryImpl extends DatabindingFactory {
    static final String WsRuntimeFactoryDefaultImpl = "com.sun.xml.internal.ws.db.DatabindingProviderImpl";
    protected Map<String, Object> properties = new HashMap();
    protected DatabindingProvider defaultRuntimeFactory;
    protected List<DatabindingProvider> providers;

    private static List<DatabindingProvider> providers() {
        List<DatabindingProvider> factories = new ArrayList<>();
        Iterator it = ServiceFinder.find(DatabindingProvider.class).iterator();
        while (it.hasNext()) {
            DatabindingProvider p2 = (DatabindingProvider) it.next();
            factories.add(p2);
        }
        return factories;
    }

    @Override // com.sun.xml.internal.ws.api.databinding.DatabindingFactory, com.oracle.webservices.internal.api.databinding.DatabindingFactory
    public Map<String, Object> properties() {
        return this.properties;
    }

    <T> T property(Class<T> propType, String propName) {
        if (propName == null) {
            propName = propType.getName();
        }
        return propType.cast(this.properties.get(propName));
    }

    public DatabindingProvider provider(DatabindingConfig config) {
        String mode = databindingMode(config);
        if (this.providers == null) {
            this.providers = providers();
        }
        DatabindingProvider provider = null;
        if (this.providers != null) {
            for (DatabindingProvider p2 : this.providers) {
                if (p2.isFor(mode)) {
                    provider = p2;
                }
            }
        }
        if (provider == null) {
            provider = new DatabindingProviderImpl();
        }
        return provider;
    }

    @Override // com.sun.xml.internal.ws.api.databinding.DatabindingFactory
    public Databinding createRuntime(DatabindingConfig config) {
        DatabindingProvider provider = provider(config);
        return provider.create(config);
    }

    public WSDLGenerator createWsdlGen(DatabindingConfig config) {
        DatabindingProvider provider = provider(config);
        return provider.wsdlGen(config);
    }

    String databindingMode(DatabindingConfig config) {
        if (config.getMappingInfo() != null && config.getMappingInfo().getDatabindingMode() != null) {
            return config.getMappingInfo().getDatabindingMode();
        }
        if (config.getFeatures() != null) {
            for (WebServiceFeature f2 : config.getFeatures()) {
                if (f2 instanceof DatabindingModeFeature) {
                    DatabindingModeFeature dmf = (DatabindingModeFeature) f2;
                    config.properties().putAll(dmf.getProperties());
                    return dmf.getMode();
                }
            }
            return null;
        }
        return null;
    }

    ClassLoader classLoader() {
        ClassLoader classLoader = (ClassLoader) property(ClassLoader.class, null);
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        return classLoader;
    }

    Properties loadPropertiesFile(String fileName) {
        InputStream is;
        ClassLoader classLoader = classLoader();
        Properties p2 = new Properties();
        try {
            if (classLoader == null) {
                is = ClassLoader.getSystemResourceAsStream(fileName);
            } else {
                is = classLoader.getResourceAsStream(fileName);
            }
            if (is != null) {
                p2.load(is);
            }
            return p2;
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.oracle.webservices.internal.api.databinding.DatabindingFactory
    public Databinding.Builder createBuilder(Class<?> contractClass, Class<?> endpointClass) {
        return new ConfigBuilder(this, contractClass, endpointClass);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/db/DatabindingFactoryImpl$ConfigBuilder.class */
    static class ConfigBuilder implements Databinding.Builder {
        DatabindingConfig config = new DatabindingConfig();
        DatabindingFactoryImpl factory;

        ConfigBuilder(DatabindingFactoryImpl f2, Class<?> contractClass, Class<?> implBeanClass) {
            this.factory = f2;
            this.config.setContractClass(contractClass);
            this.config.setEndpointClass(implBeanClass);
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding.Builder targetNamespace(String targetNamespace) {
            this.config.getMappingInfo().setTargetNamespace(targetNamespace);
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding.Builder serviceName(QName serviceName) {
            this.config.getMappingInfo().setServiceName(serviceName);
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding.Builder portName(QName portName) {
            this.config.getMappingInfo().setPortName(portName);
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding.Builder wsdlURL(URL wsdlURL) {
            this.config.setWsdlURL(wsdlURL);
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding.Builder wsdlSource(Source wsdlSource) {
            this.config.setWsdlSource(wsdlSource);
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding.Builder entityResolver(EntityResolver entityResolver) {
            this.config.setEntityResolver(entityResolver);
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding.Builder classLoader(ClassLoader classLoader) {
            this.config.setClassLoader(classLoader);
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding.Builder feature(WebServiceFeature... f2) {
            this.config.setFeatures(f2);
            return this;
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding.Builder property(String name, Object value) {
            this.config.properties().put(name, value);
            if (isfor(BindingID.class, name, value)) {
                this.config.getMappingInfo().setBindingID((BindingID) value);
            }
            if (isfor(WSBinding.class, name, value)) {
                this.config.setWSBinding((WSBinding) value);
            }
            if (isfor(WSDLPort.class, name, value)) {
                this.config.setWsdlPort((WSDLPort) value);
            }
            if (isfor(MetadataReader.class, name, value)) {
                this.config.setMetadataReader((MetadataReader) value);
            }
            return this;
        }

        boolean isfor(Class<?> type, String name, Object value) {
            return type.getName().equals(name) && type.isInstance(value);
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public Databinding build() {
            return this.factory.createRuntime(this.config);
        }

        @Override // com.oracle.webservices.internal.api.databinding.Databinding.Builder
        public WSDLGenerator createWSDLGenerator() {
            return this.factory.createWsdlGen(this.config);
        }
    }
}
