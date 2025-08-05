package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformXPointer.class */
public class TransformXPointer extends TransformSpi {
    public static final String implementedTransformURI = "http://www.w3.org/TR/2001/WD-xptr-20010108";

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected String engineGetURI() {
        return "http://www.w3.org/TR/2001/WD-xptr-20010108";
    }

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws TransformationException {
        throw new TransformationException("signature.Transform.NotYetImplemented", new Object[]{"http://www.w3.org/TR/2001/WD-xptr-20010108"});
    }
}
