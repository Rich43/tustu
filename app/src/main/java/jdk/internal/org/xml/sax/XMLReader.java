package jdk.internal.org.xml.sax;

import java.io.IOException;

/* loaded from: rt.jar:jdk/internal/org/xml/sax/XMLReader.class */
public interface XMLReader {
    boolean getFeature(String str) throws SAXNotSupportedException, SAXNotRecognizedException;

    void setFeature(String str, boolean z2) throws SAXNotSupportedException, SAXNotRecognizedException;

    Object getProperty(String str) throws SAXNotSupportedException, SAXNotRecognizedException;

    void setProperty(String str, Object obj) throws SAXNotSupportedException, SAXNotRecognizedException;

    void setEntityResolver(EntityResolver entityResolver);

    EntityResolver getEntityResolver();

    void setDTDHandler(DTDHandler dTDHandler);

    DTDHandler getDTDHandler();

    void setContentHandler(ContentHandler contentHandler);

    ContentHandler getContentHandler();

    void setErrorHandler(ErrorHandler errorHandler);

    ErrorHandler getErrorHandler();

    void parse(InputSource inputSource) throws IOException, SAXException;

    void parse(String str) throws IOException, SAXException;
}
