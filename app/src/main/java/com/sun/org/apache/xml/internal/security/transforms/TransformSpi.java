package com.sun.org.apache.xml.internal.security.transforms;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/TransformSpi.class */
public abstract class TransformSpi {
    protected boolean secureValidation;

    protected abstract String engineGetURI();

    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws CanonicalizationException, ParserConfigurationException, SAXException, IOException, InvalidCanonicalizerException, TransformationException {
        throw new UnsupportedOperationException();
    }

    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, Transform transform) throws CanonicalizationException, ParserConfigurationException, SAXException, IOException, InvalidCanonicalizerException, TransformationException {
        return enginePerformTransform(xMLSignatureInput, null, transform);
    }

    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException, ParserConfigurationException, SAXException, IOException, InvalidCanonicalizerException, TransformationException {
        return enginePerformTransform(xMLSignatureInput, null);
    }
}
