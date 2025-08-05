package com.sun.org.apache.xerces.internal.xni.grammars;

import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/grammars/XMLGrammarLoader.class */
public interface XMLGrammarLoader {
    String[] getRecognizedFeatures();

    boolean getFeature(String str) throws XMLConfigurationException;

    void setFeature(String str, boolean z2) throws XMLConfigurationException;

    String[] getRecognizedProperties();

    Object getProperty(String str) throws XMLConfigurationException;

    void setProperty(String str, Object obj) throws XMLConfigurationException;

    void setLocale(Locale locale);

    Locale getLocale();

    void setErrorHandler(XMLErrorHandler xMLErrorHandler);

    XMLErrorHandler getErrorHandler();

    void setEntityResolver(XMLEntityResolver xMLEntityResolver);

    XMLEntityResolver getEntityResolver();

    Grammar loadGrammar(XMLInputSource xMLInputSource) throws IOException, XNIException;
}
