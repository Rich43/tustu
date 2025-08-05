package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLDocumentScanner.class */
public interface XMLDocumentScanner extends XMLDocumentSource {
    void setInputSource(XMLInputSource xMLInputSource) throws IOException;

    boolean scanDocument(boolean z2) throws IOException, XNIException;

    int next() throws IOException, XNIException;
}
