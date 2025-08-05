package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315ExclOmitComments;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformC14NExclusive.class */
public class TransformC14NExclusive extends TransformSpi {
    public static final String implementedTransformURI = "http://www.w3.org/2001/10/xml-exc-c14n#";

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected String engineGetURI() {
        return "http://www.w3.org/2001/10/xml-exc-c14n#";
    }

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws CanonicalizationException {
        try {
            String inclusiveNamespaces = null;
            if (transform.length("http://www.w3.org/2001/10/xml-exc-c14n#", InclusiveNamespaces._TAG_EC_INCLUSIVENAMESPACES) == 1) {
                inclusiveNamespaces = new InclusiveNamespaces(XMLUtils.selectNode(transform.getElement().getFirstChild(), "http://www.w3.org/2001/10/xml-exc-c14n#", InclusiveNamespaces._TAG_EC_INCLUSIVENAMESPACES, 0), transform.getBaseURI()).getInclusiveNamespaces();
            }
            Canonicalizer20010315ExclOmitComments canonicalizer20010315ExclOmitComments = new Canonicalizer20010315ExclOmitComments();
            canonicalizer20010315ExclOmitComments.setSecureValidation(this.secureValidation);
            if (outputStream != null) {
                canonicalizer20010315ExclOmitComments.setWriter(outputStream);
            }
            XMLSignatureInput xMLSignatureInput2 = new XMLSignatureInput(canonicalizer20010315ExclOmitComments.engineCanonicalize(xMLSignatureInput, inclusiveNamespaces));
            xMLSignatureInput2.setSecureValidation(this.secureValidation);
            if (outputStream != null) {
                xMLSignatureInput2.setOutputStream(outputStream);
            }
            return xMLSignatureInput2;
        } catch (XMLSecurityException e2) {
            throw new CanonicalizationException(e2);
        }
    }
}
