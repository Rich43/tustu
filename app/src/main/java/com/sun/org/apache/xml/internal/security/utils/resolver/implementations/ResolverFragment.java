package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverContext;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/resolver/implementations/ResolverFragment.class */
public class ResolverFragment extends ResourceResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(ResolverFragment.class);

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public boolean engineIsThreadSafe() {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public XMLSignatureInput engineResolveURI(ResourceResolverContext resourceResolverContext) throws ResourceResolverException {
        Document elementById;
        Document ownerDocument = resourceResolverContext.attr.getOwnerElement().getOwnerDocument();
        if (resourceResolverContext.uriToResolve.equals("")) {
            LOG.debug("ResolverFragment with empty URI (means complete document)");
            elementById = ownerDocument;
        } else {
            String strSubstring = resourceResolverContext.uriToResolve.substring(1);
            elementById = ownerDocument.getElementById(strSubstring);
            if (elementById == null) {
                throw new ResourceResolverException("signature.Verification.MissingID", new Object[]{strSubstring}, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri);
            }
            if (resourceResolverContext.secureValidation && !XMLUtils.protectAgainstWrappingAttack(resourceResolverContext.attr.getOwnerDocument().getDocumentElement(), strSubstring)) {
                throw new ResourceResolverException("signature.Verification.MultipleIDs", new Object[]{strSubstring}, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri);
            }
            LOG.debug("Try to catch an Element with ID {} and Element was {}", strSubstring, elementById);
        }
        XMLSignatureInput xMLSignatureInput = new XMLSignatureInput(elementById);
        xMLSignatureInput.setSecureValidation(resourceResolverContext.secureValidation);
        xMLSignatureInput.setExcludeComments(true);
        xMLSignatureInput.setMIMEType("text/xml");
        if (resourceResolverContext.baseUri != null && resourceResolverContext.baseUri.length() > 0) {
            xMLSignatureInput.setSourceURI(resourceResolverContext.baseUri.concat(resourceResolverContext.uriToResolve));
        } else {
            xMLSignatureInput.setSourceURI(resourceResolverContext.uriToResolve);
        }
        return xMLSignatureInput;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public boolean engineCanResolveURI(ResourceResolverContext resourceResolverContext) {
        if (resourceResolverContext.uriToResolve == null) {
            LOG.debug("Quick fail for null uri");
            return false;
        }
        if (resourceResolverContext.uriToResolve.equals("") || (resourceResolverContext.uriToResolve.charAt(0) == '#' && !resourceResolverContext.uriToResolve.startsWith("#xpointer("))) {
            LOG.debug("State I can resolve reference: \"{}\"", resourceResolverContext.uriToResolve);
            return true;
        }
        LOG.debug("Do not seem to be able to resolve reference: \"{}\"", resourceResolverContext.uriToResolve);
        return false;
    }
}
