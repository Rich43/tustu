package com.sun.xml.internal.messaging.saaj.util.transform;

import com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection;
import com.sun.xml.internal.messaging.saaj.util.XMLDeclarationParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/transform/EfficientStreamingTransformer.class */
public class EfficientStreamingTransformer extends Transformer {
    private final TransformerFactory transformerFactory = TransformerFactory.newInstance();
    private Transformer m_realTransformer = null;
    private Object m_fiDOMDocumentParser = null;
    private Object m_fiDOMDocumentSerializer = null;

    private EfficientStreamingTransformer() {
    }

    private void materialize() throws TransformerException {
        if (this.m_realTransformer == null) {
            this.m_realTransformer = this.transformerFactory.newTransformer();
        }
    }

    @Override // javax.xml.transform.Transformer
    public void clearParameters() {
        if (this.m_realTransformer != null) {
            this.m_realTransformer.clearParameters();
        }
    }

    @Override // javax.xml.transform.Transformer
    public ErrorListener getErrorListener() {
        try {
            materialize();
            return this.m_realTransformer.getErrorListener();
        } catch (TransformerException e2) {
            return null;
        }
    }

    @Override // javax.xml.transform.Transformer
    public Properties getOutputProperties() {
        try {
            materialize();
            return this.m_realTransformer.getOutputProperties();
        } catch (TransformerException e2) {
            return null;
        }
    }

    @Override // javax.xml.transform.Transformer
    public String getOutputProperty(String str) throws IllegalArgumentException {
        try {
            materialize();
            return this.m_realTransformer.getOutputProperty(str);
        } catch (TransformerException e2) {
            return null;
        }
    }

    @Override // javax.xml.transform.Transformer
    public Object getParameter(String str) {
        try {
            materialize();
            return this.m_realTransformer.getParameter(str);
        } catch (TransformerException e2) {
            return null;
        }
    }

    @Override // javax.xml.transform.Transformer
    public URIResolver getURIResolver() {
        try {
            materialize();
            return this.m_realTransformer.getURIResolver();
        } catch (TransformerException e2) {
            return null;
        }
    }

    @Override // javax.xml.transform.Transformer
    public void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException {
        try {
            materialize();
            this.m_realTransformer.setErrorListener(errorListener);
        } catch (TransformerException e2) {
        }
    }

    @Override // javax.xml.transform.Transformer
    public void setOutputProperties(Properties properties) throws IllegalArgumentException {
        try {
            materialize();
            this.m_realTransformer.setOutputProperties(properties);
        } catch (TransformerException e2) {
        }
    }

    @Override // javax.xml.transform.Transformer
    public void setOutputProperty(String str, String str1) throws IllegalArgumentException {
        try {
            materialize();
            this.m_realTransformer.setOutputProperty(str, str1);
        } catch (TransformerException e2) {
        }
    }

    @Override // javax.xml.transform.Transformer
    public void setParameter(String str, Object obj) {
        try {
            materialize();
            this.m_realTransformer.setParameter(str, obj);
        } catch (TransformerException e2) {
        }
    }

    @Override // javax.xml.transform.Transformer
    public void setURIResolver(URIResolver uRIResolver) {
        try {
            materialize();
            this.m_realTransformer.setURIResolver(uRIResolver);
        } catch (TransformerException e2) {
        }
    }

    private InputStream getInputStreamFromSource(StreamSource s2) throws TransformerException {
        InputStream stream = s2.getInputStream();
        if (stream != null) {
            return stream;
        }
        if (s2.getReader() != null) {
            return null;
        }
        String systemId = s2.getSystemId();
        if (systemId != null) {
            try {
                String fileURL = systemId;
                if (systemId.startsWith("file:///")) {
                    String absolutePath = systemId.substring(7);
                    boolean hasDriveDesignator = absolutePath.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) > 0;
                    if (hasDriveDesignator) {
                        String driveDesignatedPath = absolutePath.substring(1);
                        fileURL = driveDesignatedPath;
                    } else {
                        fileURL = absolutePath;
                    }
                }
                try {
                    return new FileInputStream(new File(new URI(fileURL)));
                } catch (URISyntaxException ex) {
                    throw new TransformerException(ex);
                }
            } catch (IOException e2) {
                throw new TransformerException(e2.toString());
            }
        }
        throw new TransformerException("Unexpected StreamSource object");
    }

    @Override // javax.xml.transform.Transformer
    public void transform(Source source, Result result) throws TransformerException {
        if ((source instanceof StreamSource) && (result instanceof StreamResult)) {
            try {
                StreamSource streamSource = (StreamSource) source;
                InputStream is = getInputStreamFromSource(streamSource);
                OutputStream os = ((StreamResult) result).getOutputStream();
                if (os == null) {
                    throw new TransformerException("Unexpected StreamResult object contains null OutputStream");
                }
                if (is != null) {
                    if (is.markSupported()) {
                        is.mark(Integer.MAX_VALUE);
                    }
                    byte[] b2 = new byte[8192];
                    while (true) {
                        int num = is.read(b2);
                        if (num == -1) {
                            break;
                        } else {
                            os.write(b2, 0, num);
                        }
                    }
                    if (is.markSupported()) {
                        is.reset();
                        return;
                    }
                    return;
                }
                Reader reader = streamSource.getReader();
                if (reader != null) {
                    if (reader.markSupported()) {
                        reader.mark(Integer.MAX_VALUE);
                    }
                    PushbackReader pushbackReader = new PushbackReader(reader, 4096);
                    XMLDeclarationParser ev = new XMLDeclarationParser(pushbackReader);
                    try {
                        ev.parse();
                        Writer writer = new OutputStreamWriter(os);
                        ev.writeTo(writer);
                        char[] ac2 = new char[8192];
                        while (true) {
                            int num2 = pushbackReader.read(ac2);
                            if (num2 == -1) {
                                break;
                            } else {
                                writer.write(ac2, 0, num2);
                            }
                        }
                        writer.flush();
                        if (reader.markSupported()) {
                            reader.reset();
                            return;
                        }
                        return;
                    } catch (Exception ex) {
                        throw new TransformerException("Unable to run the JAXP transformer on a stream " + ex.getMessage());
                    }
                }
                throw new TransformerException("Unexpected StreamSource object");
            } catch (IOException e2) {
                e2.printStackTrace();
                throw new TransformerException(e2.toString());
            }
        }
        if (FastInfosetReflection.isFastInfosetSource(source) && (result instanceof DOMResult)) {
            try {
                if (this.m_fiDOMDocumentParser == null) {
                    this.m_fiDOMDocumentParser = FastInfosetReflection.DOMDocumentParser_new();
                }
                FastInfosetReflection.DOMDocumentParser_parse(this.m_fiDOMDocumentParser, (Document) ((DOMResult) result).getNode(), FastInfosetReflection.FastInfosetSource_getInputStream(source));
                return;
            } catch (Exception e3) {
                throw new TransformerException(e3);
            }
        }
        if ((source instanceof DOMSource) && FastInfosetReflection.isFastInfosetResult(result)) {
            try {
                if (this.m_fiDOMDocumentSerializer == null) {
                    this.m_fiDOMDocumentSerializer = FastInfosetReflection.DOMDocumentSerializer_new();
                }
                FastInfosetReflection.DOMDocumentSerializer_setOutputStream(this.m_fiDOMDocumentSerializer, FastInfosetReflection.FastInfosetResult_getOutputStream(result));
                FastInfosetReflection.DOMDocumentSerializer_serialize(this.m_fiDOMDocumentSerializer, ((DOMSource) source).getNode());
                return;
            } catch (Exception e4) {
                throw new TransformerException(e4);
            }
        }
        materialize();
        this.m_realTransformer.transform(source, result);
    }

    public static Transformer newTransformer() {
        return new EfficientStreamingTransformer();
    }
}
