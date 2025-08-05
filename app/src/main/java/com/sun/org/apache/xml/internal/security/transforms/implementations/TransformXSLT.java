package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/TransformXSLT.class */
public class TransformXSLT extends TransformSpi {
    public static final String implementedTransformURI = "http://www.w3.org/TR/1999/REC-xslt-19991116";
    static final String XSLTSpecNS = "http://www.w3.org/1999/XSL/Transform";
    static final String defaultXSLTSpecNSprefix = "xslt";
    static final String XSLTSTYLESHEET = "stylesheet";
    private static final Logger LOG = LoggerFactory.getLogger(TransformXSLT.class);

    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected String engineGetURI() {
        return "http://www.w3.org/TR/1999/REC-xslt-19991116";
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v32 */
    /* JADX WARN: Type inference failed for: r0v34 */
    /* JADX WARN: Type inference failed for: r0v35, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v36, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v37, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r15v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v2 */
    /* JADX WARN: Type inference failed for: r16v5, types: [java.lang.Throwable] */
    @Override // com.sun.org.apache.xml.internal.security.transforms.TransformSpi
    protected XMLSignatureInput enginePerformTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream, Transform transform) throws TransformerFactoryConfigurationError, TransformationException, IOException {
        Exception exc;
        try {
            Element element = transform.getElement();
            Element elementSelectNode = XMLUtils.selectNode(element.getFirstChild(), "http://www.w3.org/1999/XSL/Transform", "stylesheet", 0);
            if (elementSelectNode == null) {
                elementSelectNode = XMLUtils.selectNode(element.getFirstChild(), "http://www.w3.org/1999/XSL/Transform", Constants.ELEMNAME_TRANSFORM_STRING, 0);
            }
            if (elementSelectNode == null) {
                throw new TransformationException("xml.WrongContent", new Object[]{"xslt:stylesheet", com.sun.org.apache.xml.internal.security.utils.Constants._TAG_TRANSFORM});
            }
            TransformerFactory transformerFactoryNewInstance = TransformerFactory.newInstance();
            transformerFactoryNewInstance.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ?? r15 = 0;
            try {
                try {
                    Transformer transformerNewTransformer = transformerFactoryNewInstance.newTransformer();
                    transformerNewTransformer.transform(new DOMSource(elementSelectNode), new StreamResult(byteArrayOutputStream));
                    StreamSource streamSource = new StreamSource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                    Transformer transformer = transformerNewTransformer;
                    if (byteArrayOutputStream != null) {
                        if (0 != 0) {
                            try {
                                byteArrayOutputStream.close();
                                transformer = transformerNewTransformer;
                            } catch (Throwable th) {
                                r15.addSuppressed(th);
                                transformer = th;
                            }
                        } else {
                            byteArrayOutputStream.close();
                            transformer = transformerNewTransformer;
                        }
                    }
                    Transformer transformerNewTransformer2 = transformerFactoryNewInstance.newTransformer(streamSource);
                    try {
                        transformerNewTransformer2.setOutputProperty(OutputPropertiesFactory.S_KEY_LINE_SEPARATOR, "\n");
                        exc = r15;
                    } catch (Exception e2) {
                        LOG.warn("Unable to set Xalan line-separator property: " + e2.getMessage());
                        exc = e2;
                    }
                    try {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xMLSignatureInput.getBytes());
                        Throwable th2 = null;
                        StreamSource streamSource2 = new StreamSource(byteArrayInputStream);
                        if (outputStream == null) {
                            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                            Throwable th3 = null;
                            try {
                                try {
                                    transformerNewTransformer2.transform(streamSource2, new StreamResult(byteArrayOutputStream2));
                                    XMLSignatureInput xMLSignatureInput2 = new XMLSignatureInput(byteArrayOutputStream2.toByteArray());
                                    xMLSignatureInput2.setSecureValidation(this.secureValidation);
                                    if (byteArrayOutputStream2 != null) {
                                        if (0 != 0) {
                                            try {
                                                byteArrayOutputStream2.close();
                                            } catch (Throwable th4) {
                                                th3.addSuppressed(th4);
                                            }
                                        } else {
                                            byteArrayOutputStream2.close();
                                        }
                                    }
                                    if (byteArrayInputStream != null) {
                                        if (0 != 0) {
                                            try {
                                                byteArrayInputStream.close();
                                            } catch (Throwable th5) {
                                                th2.addSuppressed(th5);
                                            }
                                        } else {
                                            byteArrayInputStream.close();
                                        }
                                    }
                                    return xMLSignatureInput2;
                                } finally {
                                }
                            } finally {
                            }
                        }
                        transformerNewTransformer2.transform(streamSource2, new StreamResult(outputStream));
                        if (byteArrayInputStream != null) {
                            if (0 != 0) {
                                try {
                                    byteArrayInputStream.close();
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                }
                            } else {
                                byteArrayInputStream.close();
                            }
                        }
                        XMLSignatureInput xMLSignatureInput3 = new XMLSignatureInput((byte[]) null);
                        xMLSignatureInput3.setSecureValidation(this.secureValidation);
                        xMLSignatureInput3.setOutputStream(outputStream);
                        return xMLSignatureInput3;
                    } catch (Throwable th7) {
                        if (exc != false) {
                            if (transformer == true) {
                                try {
                                    exc.close();
                                } catch (Throwable th8) {
                                    transformer.addSuppressed(th8);
                                }
                            } else {
                                exc.close();
                            }
                        }
                        throw th7;
                    }
                } finally {
                }
            } finally {
            }
        } catch (XMLSecurityException e3) {
            throw new TransformationException(e3);
        } catch (TransformerConfigurationException e4) {
            throw new TransformationException(e4);
        } catch (TransformerException e5) {
            throw new TransformationException(e5);
        }
    }
}
