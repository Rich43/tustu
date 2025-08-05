package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315OmitComments;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformC14N.class */
public class TransformC14N extends TransformSpi {
    public static final String implementedTransformURI = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected String engineGetURI() {
        return "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
    }

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws CanonicalizationException {
        Canonicalizer20010315OmitComments canonicalizer20010315OmitComments = new Canonicalizer20010315OmitComments();
        canonicalizer20010315OmitComments.setSecureValidation(this.secureValidation);
        if (outputStream != null) {
            canonicalizer20010315OmitComments.setWriter(outputStream);
        }
        XMLSignatureInput xMLSignatureInput2 = new XMLSignatureInput(canonicalizer20010315OmitComments.engineCanonicalize(xMLSignatureInput));
        xMLSignatureInput2.setSecureValidation(this.secureValidation);
        if (outputStream != null) {
            xMLSignatureInput2.setOutputStream(outputStream);
        }
        return xMLSignatureInput2;
    }
}
