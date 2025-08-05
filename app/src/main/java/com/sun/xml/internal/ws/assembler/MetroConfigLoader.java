package com.sun.xml.internal.ws.assembler;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.api.ResourceLoader;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.resources.TubelineassemblyMessages;
import com.sun.xml.internal.ws.runtime.config.MetroConfig;
import com.sun.xml.internal.ws.runtime.config.TubeFactoryList;
import com.sun.xml.internal.ws.runtime.config.TubelineDefinition;
import com.sun.xml.internal.ws.runtime.config.TubelineMapping;
import com.sun.xml.internal.ws.util.Constants;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.Iterator;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/MetroConfigLoader.class */
class MetroConfigLoader {
    private MetroConfigName defaultTubesConfigNames;
    private MetroConfig defaultConfig;
    private URL defaultConfigUrl;
    private MetroConfig appConfig;
    private URL appConfigUrl;
    private static final Logger LOGGER = Logger.getLogger(MetroConfigLoader.class);
    private static final TubeFactoryListResolver ENDPOINT_SIDE_RESOLVER = new TubeFactoryListResolver() { // from class: com.sun.xml.internal.ws.assembler.MetroConfigLoader.1
        @Override // com.sun.xml.internal.ws.assembler.MetroConfigLoader.TubeFactoryListResolver
        public TubeFactoryList getFactories(TubelineDefinition td) {
            if (td != null) {
                return td.getEndpointSide();
            }
            return null;
        }
    };
    private static final TubeFactoryListResolver CLIENT_SIDE_RESOLVER = new TubeFactoryListResolver() { // from class: com.sun.xml.internal.ws.assembler.MetroConfigLoader.2
        @Override // com.sun.xml.internal.ws.assembler.MetroConfigLoader.TubeFactoryListResolver
        public TubeFactoryList getFactories(TubelineDefinition td) {
            if (td != null) {
                return td.getClientSide();
            }
            return null;
        }
    };

    /* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/MetroConfigLoader$TubeFactoryListResolver.class */
    private interface TubeFactoryListResolver {
        TubeFactoryList getFactories(TubelineDefinition tubelineDefinition);
    }

    MetroConfigLoader(Container container, MetroConfigName defaultTubesConfigNames) {
        this.defaultTubesConfigNames = defaultTubesConfigNames;
        ResourceLoader spiResourceLoader = null;
        init(container, container != null ? (ResourceLoader) container.getSPI(ResourceLoader.class) : spiResourceLoader, new MetroConfigUrlLoader(container));
    }

    private void init(Container container, ResourceLoader... loaders) {
        MetroConfigName mcn;
        String appFileName = null;
        String defaultFileName = null;
        if (container != null && (mcn = (MetroConfigName) container.getSPI(MetroConfigName.class)) != null) {
            appFileName = mcn.getAppFileName();
            defaultFileName = mcn.getDefaultFileName();
        }
        if (appFileName == null) {
            appFileName = this.defaultTubesConfigNames.getAppFileName();
        }
        if (defaultFileName == null) {
            defaultFileName = this.defaultTubesConfigNames.getDefaultFileName();
        }
        this.defaultConfigUrl = locateResource(defaultFileName, loaders);
        if (this.defaultConfigUrl == null) {
            throw ((IllegalStateException) LOGGER.logSevereException(new IllegalStateException(TubelineassemblyMessages.MASM_0001_DEFAULT_CFG_FILE_NOT_FOUND(defaultFileName))));
        }
        LOGGER.config(TubelineassemblyMessages.MASM_0002_DEFAULT_CFG_FILE_LOCATED(defaultFileName, this.defaultConfigUrl));
        this.defaultConfig = loadMetroConfig(this.defaultConfigUrl);
        if (this.defaultConfig == null) {
            throw ((IllegalStateException) LOGGER.logSevereException(new IllegalStateException(TubelineassemblyMessages.MASM_0003_DEFAULT_CFG_FILE_NOT_LOADED(defaultFileName))));
        }
        if (this.defaultConfig.getTubelines() == null) {
            throw ((IllegalStateException) LOGGER.logSevereException(new IllegalStateException(TubelineassemblyMessages.MASM_0004_NO_TUBELINES_SECTION_IN_DEFAULT_CFG_FILE(defaultFileName))));
        }
        if (this.defaultConfig.getTubelines().getDefault() == null) {
            throw ((IllegalStateException) LOGGER.logSevereException(new IllegalStateException(TubelineassemblyMessages.MASM_0005_NO_DEFAULT_TUBELINE_IN_DEFAULT_CFG_FILE(defaultFileName))));
        }
        this.appConfigUrl = locateResource(appFileName, loaders);
        if (this.appConfigUrl != null) {
            LOGGER.config(TubelineassemblyMessages.MASM_0006_APP_CFG_FILE_LOCATED(this.appConfigUrl));
            this.appConfig = loadMetroConfig(this.appConfigUrl);
        } else {
            LOGGER.config(TubelineassemblyMessages.MASM_0007_APP_CFG_FILE_NOT_FOUND());
            this.appConfig = null;
        }
    }

    TubeFactoryList getEndpointSideTubeFactories(URI endpointReference) {
        return getTubeFactories(endpointReference, ENDPOINT_SIDE_RESOLVER);
    }

    TubeFactoryList getClientSideTubeFactories(URI endpointReference) {
        return getTubeFactories(endpointReference, CLIENT_SIDE_RESOLVER);
    }

    private TubeFactoryList getTubeFactories(URI endpointReference, TubeFactoryListResolver resolver) {
        TubeFactoryList list;
        if (this.appConfig != null && this.appConfig.getTubelines() != null) {
            Iterator<TubelineMapping> it = this.appConfig.getTubelines().getTubelineMappings().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                TubelineMapping mapping = it.next();
                if (mapping.getEndpointRef().equals(endpointReference.toString())) {
                    TubeFactoryList list2 = resolver.getFactories(getTubeline(this.appConfig, resolveReference(mapping.getTubelineRef())));
                    if (list2 != null) {
                        return list2;
                    }
                }
            }
            if (this.appConfig.getTubelines().getDefault() != null && (list = resolver.getFactories(getTubeline(this.appConfig, resolveReference(this.appConfig.getTubelines().getDefault())))) != null) {
                return list;
            }
        }
        Iterator<TubelineMapping> it2 = this.defaultConfig.getTubelines().getTubelineMappings().iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            TubelineMapping mapping2 = it2.next();
            if (mapping2.getEndpointRef().equals(endpointReference.toString())) {
                TubeFactoryList list3 = resolver.getFactories(getTubeline(this.defaultConfig, resolveReference(mapping2.getTubelineRef())));
                if (list3 != null) {
                    return list3;
                }
            }
        }
        return resolver.getFactories(getTubeline(this.defaultConfig, resolveReference(this.defaultConfig.getTubelines().getDefault())));
    }

    TubelineDefinition getTubeline(MetroConfig config, URI tubelineDefinitionUri) {
        if (config != null && config.getTubelines() != null) {
            for (TubelineDefinition td : config.getTubelines().getTubelineDefinitions()) {
                if (td.getName().equals(tubelineDefinitionUri.getFragment())) {
                    return td;
                }
            }
            return null;
        }
        return null;
    }

    private static URI resolveReference(String reference) {
        try {
            return new URI(reference);
        } catch (URISyntaxException ex) {
            throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(TubelineassemblyMessages.MASM_0008_INVALID_URI_REFERENCE(reference), ex)));
        }
    }

    private static URL locateResource(String resource, ResourceLoader loader) {
        if (loader == null) {
            return null;
        }
        try {
            return loader.getResource(resource);
        } catch (MalformedURLException ex) {
            LOGGER.severe(TubelineassemblyMessages.MASM_0009_CANNOT_FORM_VALID_URL(resource), ex);
            return null;
        }
    }

    private static URL locateResource(String resource, ResourceLoader[] loaders) {
        for (ResourceLoader loader : loaders) {
            URL url = locateResource(resource, loader);
            if (url != null) {
                return url;
            }
        }
        return null;
    }

    private static MetroConfig loadMetroConfig(@NotNull URL resourceUrl) throws FactoryConfigurationError {
        MetroConfig result = null;
        try {
            JAXBContext jaxbContext = createJAXBContext();
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory factory = XmlUtil.newXMLInputFactory(true);
            JAXBElement<MetroConfig> configElement = unmarshaller.unmarshal(factory.createXMLStreamReader(resourceUrl.openStream()), MetroConfig.class);
            result = configElement.getValue();
        } catch (Exception e2) {
            LOGGER.warning(TubelineassemblyMessages.MASM_0010_ERROR_READING_CFG_FILE_FROM_LOCATION(resourceUrl.toString()), e2);
        }
        return result;
    }

    private static JAXBContext createJAXBContext() throws Exception {
        if (isJDKInternal()) {
            return (JAXBContext) AccessController.doPrivileged(new PrivilegedExceptionAction<JAXBContext>() { // from class: com.sun.xml.internal.ws.assembler.MetroConfigLoader.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public JAXBContext run() throws Exception {
                    return JAXBContext.newInstance(MetroConfig.class.getPackage().getName());
                }
            }, createSecurityContext());
        }
        return JAXBContext.newInstance(MetroConfig.class.getPackage().getName());
    }

    private static AccessControlContext createSecurityContext() {
        PermissionCollection perms = new Permissions();
        perms.add(new RuntimePermission("accessClassInPackage.com.sun.xml.internal.ws.runtime.config"));
        perms.add(new ReflectPermission("suppressAccessChecks"));
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
    }

    private static boolean isJDKInternal() {
        return MetroConfigLoader.class.getName().startsWith(Constants.LoggingDomain);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/MetroConfigLoader$MetroConfigUrlLoader.class */
    private static class MetroConfigUrlLoader extends ResourceLoader {
        Container container;
        ResourceLoader parentLoader;

        MetroConfigUrlLoader(ResourceLoader parentLoader) {
            this.parentLoader = parentLoader;
        }

        MetroConfigUrlLoader(Container container) {
            this(container != null ? (ResourceLoader) container.getSPI(ResourceLoader.class) : null);
            this.container = container;
        }

        @Override // com.sun.xml.internal.ws.api.ResourceLoader
        public URL getResource(String resource) throws MalformedURLException {
            MetroConfigLoader.LOGGER.entering(resource);
            URL resourceUrl = null;
            try {
                if (this.parentLoader != null) {
                    if (MetroConfigLoader.LOGGER.isLoggable(Level.FINE)) {
                        MetroConfigLoader.LOGGER.fine(TubelineassemblyMessages.MASM_0011_LOADING_RESOURCE(resource, this.parentLoader));
                    }
                    resourceUrl = this.parentLoader.getResource(resource);
                }
                if (resourceUrl == null) {
                    resourceUrl = loadViaClassLoaders("com/sun/xml/internal/ws/assembler/" + resource);
                }
                if (resourceUrl == null && this.container != null) {
                    resourceUrl = loadFromServletContext(resource);
                }
                URL url = resourceUrl;
                MetroConfigLoader.LOGGER.exiting(resourceUrl);
                return url;
            } catch (Throwable th) {
                MetroConfigLoader.LOGGER.exiting(resourceUrl);
                throw th;
            }
        }

        private static URL loadViaClassLoaders(String resource) {
            URL resourceUrl = tryLoadFromClassLoader(resource, Thread.currentThread().getContextClassLoader());
            if (resourceUrl == null) {
                resourceUrl = tryLoadFromClassLoader(resource, MetroConfigLoader.class.getClassLoader());
                if (resourceUrl == null) {
                    return ClassLoader.getSystemResource(resource);
                }
            }
            return resourceUrl;
        }

        private static URL tryLoadFromClassLoader(String resource, ClassLoader loader) {
            if (loader != null) {
                return loader.getResource(resource);
            }
            return null;
        }

        private URL loadFromServletContext(String resource) throws RuntimeException {
            try {
                Class<?> contextClass = Class.forName("javax.servlet.ServletContext");
                Object context = this.container.getSPI(contextClass);
                if (context != null) {
                    if (MetroConfigLoader.LOGGER.isLoggable(Level.FINE)) {
                        MetroConfigLoader.LOGGER.fine(TubelineassemblyMessages.MASM_0012_LOADING_VIA_SERVLET_CONTEXT(resource, context));
                    }
                    try {
                        Method method = context.getClass().getMethod("getResource", String.class);
                        Object result = method.invoke(context, "/WEB-INF/" + resource);
                        return (URL) URL.class.cast(result);
                    } catch (Exception e2) {
                        throw ((RuntimeException) MetroConfigLoader.LOGGER.logSevereException((Logger) new RuntimeException(TubelineassemblyMessages.MASM_0013_ERROR_INVOKING_SERVLET_CONTEXT_METHOD("getResource()")), (Throwable) e2));
                    }
                }
                return null;
            } catch (ClassNotFoundException e3) {
                if (MetroConfigLoader.LOGGER.isLoggable(Level.FINE)) {
                    MetroConfigLoader.LOGGER.fine(TubelineassemblyMessages.MASM_0014_UNABLE_TO_LOAD_CLASS("javax.servlet.ServletContext"));
                    return null;
                }
                return null;
            }
        }
    }
}
