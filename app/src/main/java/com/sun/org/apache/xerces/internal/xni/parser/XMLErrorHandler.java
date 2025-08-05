package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XNIException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLErrorHandler.class */
public interface XMLErrorHandler {
    void warning(String str, String str2, XMLParseException xMLParseException) throws XNIException;

    void error(String str, String str2, XMLParseException xMLParseException) throws XNIException;

    void fatalError(String str, String str2, XMLParseException xMLParseException) throws XNIException;
}
