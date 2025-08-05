package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.io.IOException;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration.class */
public interface XMLParserConfiguration extends XMLComponentManager {
    void parse(XMLInputSource xMLInputSource) throws IOException, XNIException;

    void addRecognizedFeatures(String[] strArr);

    void setFeature(String str, boolean z2) throws XMLConfigurationException;

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    boolean getFeature(String str) throws XMLConfigurationException;

    void addRecognizedProperties(String[] strArr);

    void setProperty(String str, Object obj) throws XMLConfigurationException;

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    Object getProperty(String str) throws XMLConfigurationException;

    void setErrorHandler(XMLErrorHandler xMLErrorHandler);

    XMLErrorHandler getErrorHandler();

    void setDocumentHandler(XMLDocumentHandler xMLDocumentHandler);

    XMLDocumentHandler getDocumentHandler();

    void setDTDHandler(XMLDTDHandler xMLDTDHandler);

    XMLDTDHandler getDTDHandler();

    void setDTDContentModelHandler(XMLDTDContentModelHandler xMLDTDContentModelHandler);

    XMLDTDContentModelHandler getDTDContentModelHandler();

    void setEntityResolver(XMLEntityResolver xMLEntityResolver);

    XMLEntityResolver getEntityResolver();

    void setLocale(Locale locale) throws XNIException;

    Locale getLocale();
}
