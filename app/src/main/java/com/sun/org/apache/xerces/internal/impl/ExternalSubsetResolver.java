package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLDTDDescription;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/ExternalSubsetResolver.class */
public interface ExternalSubsetResolver extends XMLEntityResolver {
    XMLInputSource getExternalSubset(XMLDTDDescription xMLDTDDescription) throws IOException, XNIException;
}
