package com.sun.org.apache.xerces.internal.xinclude;

import com.sun.org.apache.xerces.internal.util.XML11Char;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/XInclude11TextReader.class */
public class XInclude11TextReader extends XIncludeTextReader {
    public XInclude11TextReader(XMLInputSource source, XIncludeHandler handler, int bufferSize) throws IOException {
        super(source, handler, bufferSize);
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XIncludeTextReader
    protected boolean isValid(int ch) {
        return XML11Char.isXML11Valid(ch);
    }
}
