package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLDTDScanner.class */
public interface XMLDTDScanner extends XMLDTDSource, XMLDTDContentModelSource {
    void setInputSource(XMLInputSource xMLInputSource) throws IOException;

    boolean scanDTDInternalSubset(boolean z2, boolean z3, boolean z4) throws IOException, XNIException;

    boolean scanDTDExternalSubset(boolean z2) throws IOException, XNIException;

    boolean skipDTD(boolean z2) throws IOException;

    void setLimitAnalyzer(XMLLimitAnalyzer xMLLimitAnalyzer);
}
