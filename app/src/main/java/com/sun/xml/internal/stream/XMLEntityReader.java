package com.sun.xml.internal.stream;

import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/xml/internal/stream/XMLEntityReader.class */
public abstract class XMLEntityReader implements XMLLocator {
    public abstract void setEncoding(String str) throws IOException;

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public abstract String getEncoding();

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public abstract int getCharacterOffset();

    public abstract void setVersion(String str);

    public abstract String getVersion();

    public abstract boolean isExternal();

    public abstract int peekChar() throws IOException;

    public abstract int scanChar() throws IOException;

    public abstract String scanNmtoken() throws IOException;

    public abstract String scanName() throws IOException;

    public abstract boolean scanQName(QName qName) throws IOException;

    public abstract int scanContent(XMLString xMLString) throws IOException;

    public abstract int scanLiteral(int i2, XMLString xMLString) throws IOException;

    public abstract boolean scanData(String str, XMLStringBuffer xMLStringBuffer) throws IOException;

    public abstract boolean skipChar(int i2) throws IOException;

    public abstract boolean skipSpaces() throws IOException;

    public abstract boolean skipString(String str) throws IOException;

    public abstract void registerListener(XMLBufferListener xMLBufferListener);
}
