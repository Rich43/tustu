package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverContext;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/resolver/implementations/ResolverXPointer.class */
public class ResolverXPointer extends ResourceResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(ResolverXPointer.class);
    private static final String XP = "#xpointer(id(";
    private static final int XP_LENGTH = XP.length();

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public boolean engineIsThreadSafe() {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public XMLSignatureInput engineResolveURI(ResourceResolverContext resourceResolverContext) throws ResourceResolverException {
        Document elementById = null;
        Document ownerDocument = resourceResolverContext.attr.getOwnerElement().getOwnerDocument();
        if (isXPointerSlash(resourceResolverContext.uriToResolve)) {
            elementById = ownerDocument;
        } else if (isXPointerId(resourceResolverContext.uriToResolve)) {
            String xPointerId = getXPointerId(resourceResolverContext.uriToResolve);
            elementById = ownerDocument.getElementById(xPointerId);
            if (resourceResolverContext.secureValidation && !XMLUtils.protectAgainstWrappingAttack(resourceResolverContext.attr.getOwnerDocument().getDocumentElement(), xPointerId)) {
                throw new ResourceResolverException("signature.Verification.MultipleIDs", new Object[]{xPointerId}, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri);
            }
            if (elementById == null) {
                throw new ResourceResolverException("signature.Verification.MissingID", new Object[]{xPointerId}, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri);
            }
        }
        XMLSignatureInput xMLSignatureInput = new XMLSignatureInput(elementById);
        xMLSignatureInput.setSecureValidation(resourceResolverContext.secureValidation);
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
            return false;
        }
        if (isXPointerSlash(resourceResolverContext.uriToResolve) || isXPointerId(resourceResolverContext.uriToResolve)) {
            return true;
        }
        return false;
    }

    private static boolean isXPointerSlash(String str) {
        if (str.equals("#xpointer(/)")) {
            return true;
        }
        return false;
    }

    private static boolean isXPointerId(String str) {
        if (str.startsWith(XP) && str.endsWith("))")) {
            String strSubstring = str.substring(XP_LENGTH, str.length() - 2);
            int length = strSubstring.length() - 1;
            if ((strSubstring.charAt(0) == '\"' && strSubstring.charAt(length) == '\"') || (strSubstring.charAt(0) == '\'' && strSubstring.charAt(length) == '\'')) {
                LOG.debug("Id = {}", strSubstring.substring(1, length));
                return true;
            }
            return false;
        }
        return false;
    }

    private static String getXPointerId(String str) {
        if (str.startsWith(XP) && str.endsWith("))")) {
            String strSubstring = str.substring(XP_LENGTH, str.length() - 2);
            int length = strSubstring.length() - 1;
            if ((strSubstring.charAt(0) == '\"' && strSubstring.charAt(length) == '\"') || (strSubstring.charAt(0) == '\'' && strSubstring.charAt(length) == '\'')) {
                return strSubstring.substring(1, length);
            }
            return null;
        }
        return null;
    }
}
