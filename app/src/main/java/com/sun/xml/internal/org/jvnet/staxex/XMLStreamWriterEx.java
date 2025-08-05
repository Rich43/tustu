package com.sun.xml.internal.org.jvnet.staxex;

import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/XMLStreamWriterEx.class */
public interface XMLStreamWriterEx extends XMLStreamWriter {
    void writeBinary(byte[] bArr, int i2, int i3, String str) throws XMLStreamException;

    void writeBinary(DataHandler dataHandler) throws XMLStreamException;

    OutputStream writeBinary(String str) throws XMLStreamException;

    void writePCDATA(CharSequence charSequence) throws XMLStreamException;

    @Override // javax.xml.stream.XMLStreamWriter
    NamespaceContextEx getNamespaceContext();
}
