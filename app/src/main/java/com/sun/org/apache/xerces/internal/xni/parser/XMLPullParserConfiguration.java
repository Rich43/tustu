package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLPullParserConfiguration.class */
public interface XMLPullParserConfiguration extends XMLParserConfiguration {
    void setInputSource(XMLInputSource xMLInputSource) throws IOException, XMLConfigurationException;

    boolean parse(boolean z2) throws IOException, XNIException;

    void cleanup();
}
