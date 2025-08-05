package com.sun.xml.internal.org.jvnet.fastinfoset.stax;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/stax/LowLevelFastInfosetStreamWriter.class */
public interface LowLevelFastInfosetStreamWriter {
    void initiateLowLevelWriting() throws XMLStreamException;

    int getNextElementIndex();

    int getNextAttributeIndex();

    int getLocalNameIndex();

    int getNextLocalNameIndex();

    void writeLowLevelTerminationAndMark() throws IOException;

    void writeLowLevelStartElementIndexed(int i2, int i3) throws IOException;

    boolean writeLowLevelStartElement(int i2, String str, String str2, String str3) throws IOException;

    void writeLowLevelStartNamespaces() throws IOException;

    void writeLowLevelNamespace(String str, String str2) throws IOException;

    void writeLowLevelEndNamespaces() throws IOException;

    void writeLowLevelStartAttributes() throws IOException;

    void writeLowLevelAttributeIndexed(int i2) throws IOException;

    boolean writeLowLevelAttribute(String str, String str2, String str3) throws IOException;

    void writeLowLevelAttributeValue(String str) throws IOException;

    void writeLowLevelStartNameLiteral(int i2, String str, byte[] bArr, String str2) throws IOException;

    void writeLowLevelStartNameLiteral(int i2, String str, int i3, String str2) throws IOException;

    void writeLowLevelEndStartElement() throws IOException;

    void writeLowLevelEndElement() throws IOException;

    void writeLowLevelText(char[] cArr, int i2) throws IOException;

    void writeLowLevelText(String str) throws IOException;

    void writeLowLevelOctets(byte[] bArr, int i2) throws IOException;
}
