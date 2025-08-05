package com.sun.org.apache.xml.internal.security.utils.resolver;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverDirectHTTP;
import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverFragment;
import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverLocalFilesystem;
import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverXPointer;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import org.w3c.dom.Attr;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/resolver/ResourceResolver.class */
public class ResourceResolver {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceResolver.class);
    private static final List<ResourceResolver> resolverList = new ArrayList();
    private final ResourceResolverSpi resolverSpi;

    public ResourceResolver(ResourceResolverSpi resourceResolverSpi) {
        this.resolverSpi = resourceResolverSpi;
    }

    public static final ResourceResolver getInstance(Attr attr, String str, boolean z2) throws ResourceResolverException {
        return internalGetInstance(new ResourceResolverContext(attr, str, z2));
    }

    private static <N> ResourceResolver internalGetInstance(ResourceResolverContext resourceResolverContext) throws ResourceResolverException {
        synchronized (resolverList) {
            for (ResourceResolver resourceResolver : resolverList) {
                ResourceResolver resourceResolver2 = resourceResolver;
                if (!resourceResolver.resolverSpi.engineIsThreadSafe()) {
                    try {
                        resourceResolver2 = new ResourceResolver((ResourceResolverSpi) resourceResolver.resolverSpi.getClass().newInstance());
                    } catch (IllegalAccessException e2) {
                        throw new ResourceResolverException(e2, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri, "");
                    } catch (InstantiationException e3) {
                        throw new ResourceResolverException(e3, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri, "");
                    }
                }
                LOG.debug("check resolvability by class {}", resourceResolver2.getClass().getName());
                if (resourceResolver2.canResolve(resourceResolverContext)) {
                    if (resourceResolverContext.secureValidation && ((resourceResolver2.resolverSpi instanceof ResolverLocalFilesystem) || (resourceResolver2.resolverSpi instanceof ResolverDirectHTTP))) {
                        throw new ResourceResolverException("signature.Reference.ForbiddenResolver", new Object[]{resourceResolver2.resolverSpi.getClass().getName()}, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri);
                    }
                    return resourceResolver2;
                }
            }
            Object[] objArr = new Object[2];
            objArr[0] = resourceResolverContext.uriToResolve != null ? resourceResolverContext.uriToResolve : FXMLLoader.NULL_KEYWORD;
            objArr[1] = resourceResolverContext.baseUri;
            throw new ResourceResolverException("utils.resolver.noClass", objArr, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri);
        }
    }

    public static ResourceResolver getInstance(Attr attr, String str, List<ResourceResolver> list) throws ResourceResolverException {
        return getInstance(attr, str, list, true);
    }

    public static ResourceResolver getInstance(Attr attr, String str, List<ResourceResolver> list, boolean z2) throws ResourceResolverException {
        Logger logger = LOG;
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(list == null ? 0 : list.size());
        logger.debug("I was asked to create a ResourceResolver and got {}", objArr);
        ResourceResolverContext resourceResolverContext = new ResourceResolverContext(attr, str, z2);
        if (list != null) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                ResourceResolver resourceResolver = list.get(i2);
                if (resourceResolver != null) {
                    LOG.debug("check resolvability by class {}", resourceResolver.resolverSpi.getClass().getName());
                    if (resourceResolver.canResolve(resourceResolverContext)) {
                        return resourceResolver;
                    }
                }
            }
        }
        return internalGetInstance(resourceResolverContext);
    }

    public static void register(String str) {
        JavaUtils.checkRegisterPermission();
        try {
            register((Class<? extends ResourceResolverSpi>) ClassLoaderUtils.loadClass(str, ResourceResolver.class), false);
        } catch (ClassNotFoundException e2) {
            LOG.warn("Error loading resolver " + str + " disabling it");
        }
    }

    public static void registerAtStart(String str) {
        JavaUtils.checkRegisterPermission();
        try {
            register((Class<? extends ResourceResolverSpi>) ClassLoaderUtils.loadClass(str, ResourceResolver.class), true);
        } catch (ClassNotFoundException e2) {
            LOG.warn("Error loading resolver " + str + " disabling it");
        }
    }

    public static void register(Class<? extends ResourceResolverSpi> cls, boolean z2) {
        JavaUtils.checkRegisterPermission();
        try {
            register(cls.newInstance(), z2);
        } catch (IllegalAccessException e2) {
            LOG.warn("Error loading resolver " + ((Object) cls) + " disabling it");
        } catch (InstantiationException e3) {
            LOG.warn("Error loading resolver " + ((Object) cls) + " disabling it");
        }
    }

    public static void register(ResourceResolverSpi resourceResolverSpi, boolean z2) {
        JavaUtils.checkRegisterPermission();
        synchronized (resolverList) {
            if (z2) {
                resolverList.add(0, new ResourceResolver(resourceResolverSpi));
            } else {
                resolverList.add(new ResourceResolver(resourceResolverSpi));
            }
        }
        LOG.debug("Registered resolver: {}", resourceResolverSpi.toString());
    }

    public static void registerDefaultResolvers() {
        synchronized (resolverList) {
            resolverList.add(new ResourceResolver(new ResolverFragment()));
            resolverList.add(new ResourceResolver(new ResolverLocalFilesystem()));
            resolverList.add(new ResourceResolver(new ResolverXPointer()));
            resolverList.add(new ResourceResolver(new ResolverDirectHTTP()));
        }
    }

    public XMLSignatureInput resolve(Attr attr, String str, boolean z2) throws ResourceResolverException {
        return this.resolverSpi.engineResolveURI(new ResourceResolverContext(attr, str, z2));
    }

    public void setProperty(String str, String str2) {
        this.resolverSpi.engineSetProperty(str, str2);
    }

    public String getProperty(String str) {
        return this.resolverSpi.engineGetProperty(str);
    }

    public void addProperties(Map<String, String> map) {
        this.resolverSpi.engineAddProperies(map);
    }

    public String[] getPropertyKeys() {
        return this.resolverSpi.engineGetPropertyKeys();
    }

    public boolean understandsProperty(String str) {
        return this.resolverSpi.understandsProperty(str);
    }

    private boolean canResolve(ResourceResolverContext resourceResolverContext) {
        return this.resolverSpi.engineCanResolveURI(resourceResolverContext);
    }
}
