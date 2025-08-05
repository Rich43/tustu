package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverContext;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/resolver/implementations/ResolverLocalFilesystem.class */
public class ResolverLocalFilesystem extends ResourceResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(ResolverLocalFilesystem.class);

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public boolean engineIsThreadSafe() {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public XMLSignatureInput engineResolveURI(ResourceResolverContext resourceResolverContext) throws ResourceResolverException {
        try {
            URI newURI = getNewURI(resourceResolverContext.uriToResolve, resourceResolverContext.baseUri);
            XMLSignatureInput xMLSignatureInput = new XMLSignatureInput(Files.newInputStream(Paths.get(newURI), new OpenOption[0]));
            xMLSignatureInput.setSecureValidation(resourceResolverContext.secureValidation);
            xMLSignatureInput.setSourceURI(newURI.toString());
            return xMLSignatureInput;
        } catch (Exception e2) {
            throw new ResourceResolverException(e2, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri, "generic.EmptyMessage");
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public boolean engineCanResolveURI(ResourceResolverContext resourceResolverContext) {
        if (resourceResolverContext.uriToResolve == null || resourceResolverContext.uriToResolve.equals("") || resourceResolverContext.uriToResolve.charAt(0) == '#' || resourceResolverContext.uriToResolve.startsWith("http:")) {
            return false;
        }
        try {
            LOG.debug("I was asked whether I can resolve {}", resourceResolverContext.uriToResolve);
            if (resourceResolverContext.uriToResolve.startsWith("file:") || resourceResolverContext.baseUri.startsWith("file:")) {
                LOG.debug("I state that I can resolve {}", resourceResolverContext.uriToResolve);
                return true;
            }
        } catch (Exception e2) {
            LOG.debug(e2.getMessage(), e2);
        }
        LOG.debug("But I can't");
        return false;
    }

    private static URI getNewURI(String str, String str2) throws URISyntaxException {
        URI uri;
        if (str2 == null || "".equals(str2)) {
            uri = new URI(str);
        } else {
            uri = new URI(str2).resolve(str);
        }
        if (uri.getFragment() != null) {
            return new URI(uri.getScheme(), uri.getSchemeSpecificPart(), null);
        }
        return uri;
    }
}
