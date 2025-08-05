package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
import java.net.URI;
import javax.xml.crypto.Data;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dom.DOMURIReference;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMURIDereferencer.class */
public final class DOMURIDereferencer implements URIDereferencer {
    static final URIDereferencer INSTANCE = new DOMURIDereferencer();

    private DOMURIDereferencer() {
        Init.init();
    }

    @Override // javax.xml.crypto.URIDereferencer
    public Data dereference(URIReference uRIReference, XMLCryptoContext xMLCryptoContext) throws URIReferenceException {
        if (uRIReference == null) {
            throw new NullPointerException("uriRef cannot be null");
        }
        if (xMLCryptoContext == null) {
            throw new NullPointerException("context cannot be null");
        }
        Attr attr = (Attr) ((DOMURIReference) uRIReference).getHere();
        String uri = uRIReference.getURI();
        DOMCryptoContext dOMCryptoContext = (DOMCryptoContext) xMLCryptoContext;
        String baseURI = xMLCryptoContext.getBaseURI();
        boolean zSecureValidation = Utils.secureValidation(xMLCryptoContext);
        if (zSecureValidation) {
            try {
                if (Policy.restrictReferenceUriScheme(uri)) {
                    throw new URIReferenceException("URI " + uri + " is forbidden when secure validation is enabled");
                }
                if (uri != null && !uri.isEmpty() && uri.charAt(0) != '#' && URI.create(uri).getScheme() == null) {
                    try {
                        if (Policy.restrictReferenceUriScheme(baseURI)) {
                            throw new URIReferenceException("Base URI " + baseURI + " is forbidden when secure validation is enabled");
                        }
                    } catch (IllegalArgumentException e2) {
                        throw new URIReferenceException("Invalid base URI " + baseURI);
                    }
                }
            } catch (IllegalArgumentException e3) {
                throw new URIReferenceException("Invalid URI " + uri);
            }
        }
        if (uri != null && uri.length() != 0 && uri.charAt(0) == '#') {
            String strSubstring = uri.substring(1);
            if (strSubstring.startsWith("xpointer(id(")) {
                int iIndexOf = strSubstring.indexOf(39);
                strSubstring = strSubstring.substring(iIndexOf + 1, strSubstring.indexOf(39, iIndexOf + 1));
            }
            Element elementById = attr.getOwnerDocument().getElementById(strSubstring);
            if (elementById == null) {
                elementById = dOMCryptoContext.getElementById(strSubstring);
            }
            if (elementById != null) {
                if (zSecureValidation && Policy.restrictDuplicateIds() && !XMLUtils.protectAgainstWrappingAttack(elementById.getOwnerDocument().getDocumentElement(), elementById, strSubstring)) {
                    throw new URIReferenceException("Multiple Elements with the same ID " + strSubstring + " detected when secure validation is enabled");
                }
                XMLSignatureInput xMLSignatureInput = new XMLSignatureInput(elementById);
                xMLSignatureInput.setSecureValidation(zSecureValidation);
                if (!uri.substring(1).startsWith("xpointer(id(")) {
                    xMLSignatureInput.setExcludeComments(true);
                }
                xMLSignatureInput.setMIMEType("text/xml");
                if (baseURI != null && baseURI.length() > 0) {
                    xMLSignatureInput.setSourceURI(baseURI.concat(attr.getNodeValue()));
                } else {
                    xMLSignatureInput.setSourceURI(attr.getNodeValue());
                }
                return new ApacheNodeSetData(xMLSignatureInput);
            }
        }
        try {
            XMLSignatureInput xMLSignatureInputResolve = ResourceResolver.getInstance(attr, baseURI, zSecureValidation).resolve(attr, baseURI, zSecureValidation);
            if (xMLSignatureInputResolve.isOctetStream()) {
                return new ApacheOctetStreamData(xMLSignatureInputResolve);
            }
            return new ApacheNodeSetData(xMLSignatureInputResolve);
        } catch (Exception e4) {
            throw new URIReferenceException(e4);
        }
    }
}
