package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformC14N11.class */
public class TransformC14N11 extends TransformSpi {
    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected String engineGetURI() {
        return "http://www.w3.org/2006/12/xml-c14n11";
    }

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws CanonicalizationException {
        Canonicalizer11_OmitComments canonicalizer11_OmitComments = new Canonicalizer11_OmitComments();
        canonicalizer11_OmitComments.setSecureValidation(this.secureValidation);
        if (outputStream != null) {
            canonicalizer11_OmitComments.setWriter(outputStream);
        }
        XMLSignatureInput xMLSignatureInput2 = new XMLSignatureInput(canonicalizer11_OmitComments.engineCanonicalize(xMLSignatureInput));
        xMLSignatureInput2.setSecureValidation(this.secureValidation);
        if (outputStream != null) {
            xMLSignatureInput2.setOutputStream(outputStream);
        }
        return xMLSignatureInput2;
    }
}
