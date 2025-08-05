package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLEntityResolver.class */
public interface XMLEntityResolver {
    XMLInputSource resolveEntity(XMLResourceIdentifier xMLResourceIdentifier) throws IOException, XNIException;
}
