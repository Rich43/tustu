package com.sun.xml.internal.stream.buffer.stax;

import com.sun.xml.internal.org.jvnet.staxex.Base64Data;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx;
import com.sun.xml.internal.stream.buffer.AbstractProcessor;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/StreamWriterBufferProcessor.class */
public class StreamWriterBufferProcessor extends AbstractProcessor {
    public StreamWriterBufferProcessor() {
    }

    public StreamWriterBufferProcessor(XMLStreamBuffer buffer) {
        setXMLStreamBuffer(buffer, buffer.isFragment());
    }

    public StreamWriterBufferProcessor(XMLStreamBuffer buffer, boolean produceFragmentEvent) {
        setXMLStreamBuffer(buffer, produceFragmentEvent);
    }

    public final void process(XMLStreamBuffer buffer, XMLStreamWriter writer) throws XMLStreamException {
        setXMLStreamBuffer(buffer, buffer.isFragment());
        process(writer);
    }

    public void process(XMLStreamWriter writer) throws XMLStreamException {
        if (this._fragmentMode) {
            writeFragment(writer);
        } else {
            write(writer);
        }
    }

    public void setXMLStreamBuffer(XMLStreamBuffer buffer) {
        setBuffer(buffer);
    }

    public void setXMLStreamBuffer(XMLStreamBuffer buffer, boolean produceFragmentEvent) {
        setBuffer(buffer, produceFragmentEvent);
    }

    public void write(XMLStreamWriter writer) throws XMLStreamException {
        if (!this._fragmentMode) {
            if (this._treeCount > 1) {
                throw new IllegalStateException("forest cannot be written as a full infoset");
            }
            writer.writeStartDocument();
        }
        while (true) {
            int item = getEIIState(peekStructure());
            writer.flush();
            switch (item) {
                case 1:
                    readStructure();
                    break;
                case 2:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 15:
                default:
                    throw new XMLStreamException("Invalid State " + item);
                case 3:
                case 4:
                case 5:
                case 6:
                    writeFragment(writer);
                    break;
                case 12:
                    readStructure();
                    int length = readStructure();
                    int start = readContentCharactersBuffer(length);
                    String comment = new String(this._contentCharactersBuffer, start, length);
                    writer.writeComment(comment);
                    break;
                case 13:
                    readStructure();
                    int length2 = readStructure16();
                    int start2 = readContentCharactersBuffer(length2);
                    String comment2 = new String(this._contentCharactersBuffer, start2, length2);
                    writer.writeComment(comment2);
                    break;
                case 14:
                    readStructure();
                    char[] ch = readContentCharactersCopy();
                    writer.writeComment(new String(ch));
                    break;
                case 16:
                    readStructure();
                    writer.writeProcessingInstruction(readStructureString(), readStructureString());
                    break;
                case 17:
                    readStructure();
                    writer.writeEndDocument();
                    return;
            }
        }
    }

    public void writeFragment(XMLStreamWriter writer) throws XMLStreamException {
        if (writer instanceof XMLStreamWriterEx) {
            writeFragmentEx((XMLStreamWriterEx) writer);
        } else {
            writeFragmentNoEx(writer);
        }
    }

    public void writeFragmentEx(XMLStreamWriterEx writer) throws XMLStreamException {
        int depth = 0;
        if (getEIIState(peekStructure()) == 1) {
            readStructure();
        }
        while (true) {
            int item = readEiiState();
            switch (item) {
                case 1:
                    throw new AssertionError();
                case 2:
                case 15:
                default:
                    throw new XMLStreamException("Invalid State " + item);
                case 3:
                    depth++;
                    String uri = readStructureString();
                    String localName = readStructureString();
                    String prefix = getPrefixFromQName(readStructureString());
                    writer.writeStartElement(prefix, localName, uri);
                    writeAttributes(writer, isInscope(depth));
                    break;
                case 4:
                    depth++;
                    String prefix2 = readStructureString();
                    String uri2 = readStructureString();
                    String localName2 = readStructureString();
                    writer.writeStartElement(prefix2, localName2, uri2);
                    writeAttributes(writer, isInscope(depth));
                    break;
                case 5:
                    depth++;
                    String uri3 = readStructureString();
                    String localName3 = readStructureString();
                    writer.writeStartElement("", localName3, uri3);
                    writeAttributes(writer, isInscope(depth));
                    break;
                case 6:
                    depth++;
                    String localName4 = readStructureString();
                    writer.writeStartElement(localName4);
                    writeAttributes(writer, isInscope(depth));
                    break;
                case 7:
                    int length = readStructure();
                    int start = readContentCharactersBuffer(length);
                    writer.writeCharacters(this._contentCharactersBuffer, start, length);
                    break;
                case 8:
                    int length2 = readStructure16();
                    int start2 = readContentCharactersBuffer(length2);
                    writer.writeCharacters(this._contentCharactersBuffer, start2, length2);
                    break;
                case 9:
                    char[] c2 = readContentCharactersCopy();
                    writer.writeCharacters(c2, 0, c2.length);
                    break;
                case 10:
                    String s2 = readContentString();
                    writer.writeCharacters(s2);
                    break;
                case 11:
                    writer.writePCDATA((CharSequence) readContentObject());
                    break;
                case 12:
                    int length3 = readStructure();
                    int start3 = readContentCharactersBuffer(length3);
                    String comment = new String(this._contentCharactersBuffer, start3, length3);
                    writer.writeComment(comment);
                    break;
                case 13:
                    int length4 = readStructure16();
                    int start4 = readContentCharactersBuffer(length4);
                    String comment2 = new String(this._contentCharactersBuffer, start4, length4);
                    writer.writeComment(comment2);
                    break;
                case 14:
                    char[] ch = readContentCharactersCopy();
                    writer.writeComment(new String(ch));
                    break;
                case 16:
                    writer.writeProcessingInstruction(readStructureString(), readStructureString());
                    break;
                case 17:
                    writer.writeEndElement();
                    depth--;
                    if (depth == 0) {
                        this._treeCount--;
                        break;
                    }
                    break;
            }
            if (depth <= 0 && this._treeCount <= 0) {
                return;
            }
        }
    }

    public void writeFragmentNoEx(XMLStreamWriter writer) throws XMLStreamException {
        int depth = 0;
        if (getEIIState(peekStructure()) == 1) {
            readStructure();
        }
        while (true) {
            int item = readEiiState();
            switch (item) {
                case 1:
                    throw new AssertionError();
                case 2:
                case 15:
                default:
                    throw new XMLStreamException("Invalid State " + item);
                case 3:
                    depth++;
                    String uri = readStructureString();
                    String localName = readStructureString();
                    String prefix = getPrefixFromQName(readStructureString());
                    writer.writeStartElement(prefix, localName, uri);
                    writeAttributes(writer, isInscope(depth));
                    break;
                case 4:
                    depth++;
                    String prefix2 = readStructureString();
                    String uri2 = readStructureString();
                    String localName2 = readStructureString();
                    writer.writeStartElement(prefix2, localName2, uri2);
                    writeAttributes(writer, isInscope(depth));
                    break;
                case 5:
                    depth++;
                    String uri3 = readStructureString();
                    String localName3 = readStructureString();
                    writer.writeStartElement("", localName3, uri3);
                    writeAttributes(writer, isInscope(depth));
                    break;
                case 6:
                    depth++;
                    String localName4 = readStructureString();
                    writer.writeStartElement(localName4);
                    writeAttributes(writer, isInscope(depth));
                    break;
                case 7:
                    int length = readStructure();
                    int start = readContentCharactersBuffer(length);
                    writer.writeCharacters(this._contentCharactersBuffer, start, length);
                    break;
                case 8:
                    int length2 = readStructure16();
                    int start2 = readContentCharactersBuffer(length2);
                    writer.writeCharacters(this._contentCharactersBuffer, start2, length2);
                    break;
                case 9:
                    char[] c2 = readContentCharactersCopy();
                    writer.writeCharacters(c2, 0, c2.length);
                    break;
                case 10:
                    String s2 = readContentString();
                    writer.writeCharacters(s2);
                    break;
                case 11:
                    CharSequence c3 = (CharSequence) readContentObject();
                    if (c3 instanceof Base64Data) {
                        try {
                            Base64Data bd2 = (Base64Data) c3;
                            bd2.writeTo(writer);
                            break;
                        } catch (IOException e2) {
                            throw new XMLStreamException(e2);
                        }
                    } else {
                        writer.writeCharacters(c3.toString());
                        break;
                    }
                case 12:
                    int length3 = readStructure();
                    int start3 = readContentCharactersBuffer(length3);
                    String comment = new String(this._contentCharactersBuffer, start3, length3);
                    writer.writeComment(comment);
                    break;
                case 13:
                    int length4 = readStructure16();
                    int start4 = readContentCharactersBuffer(length4);
                    String comment2 = new String(this._contentCharactersBuffer, start4, length4);
                    writer.writeComment(comment2);
                    break;
                case 14:
                    char[] ch = readContentCharactersCopy();
                    writer.writeComment(new String(ch));
                    break;
                case 16:
                    writer.writeProcessingInstruction(readStructureString(), readStructureString());
                    break;
                case 17:
                    writer.writeEndElement();
                    depth--;
                    if (depth == 0) {
                        this._treeCount--;
                        break;
                    }
                    break;
            }
            if (depth <= 0 && this._treeCount <= 0) {
                return;
            }
        }
    }

    private boolean isInscope(int depth) {
        return this._buffer.getInscopeNamespaces().size() > 0 && depth == 1;
    }

    private void writeAttributes(XMLStreamWriter writer, boolean inscope) throws XMLStreamException {
        Set<String> prefixSet = inscope ? new HashSet<>() : Collections.emptySet();
        int item = peekStructure();
        if ((item & 240) == 64) {
            item = writeNamespaceAttributes(item, writer, inscope, prefixSet);
        }
        if (inscope) {
            writeInscopeNamespaces(writer, prefixSet);
        }
        if ((item & 240) == 48) {
            writeAttributes(item, writer);
        }
    }

    private static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }

    private void writeInscopeNamespaces(XMLStreamWriter writer, Set<String> prefixSet) throws XMLStreamException {
        for (Map.Entry<String, String> e2 : this._buffer.getInscopeNamespaces().entrySet()) {
            String key = fixNull(e2.getKey());
            if (!prefixSet.contains(key)) {
                writer.writeNamespace(key, e2.getValue());
            }
        }
    }

    private int writeNamespaceAttributes(int item, XMLStreamWriter writer, boolean collectPrefixes, Set<String> prefixSet) throws XMLStreamException {
        do {
            switch (getNIIState(item)) {
                case 1:
                    writer.writeDefaultNamespace("");
                    if (collectPrefixes) {
                        prefixSet.add("");
                        break;
                    }
                    break;
                case 2:
                    String prefix = readStructureString();
                    writer.writeNamespace(prefix, "");
                    if (collectPrefixes) {
                        prefixSet.add(prefix);
                        break;
                    }
                    break;
                case 3:
                    String prefix2 = readStructureString();
                    writer.writeNamespace(prefix2, readStructureString());
                    if (collectPrefixes) {
                        prefixSet.add(prefix2);
                        break;
                    }
                    break;
                case 4:
                    writer.writeDefaultNamespace(readStructureString());
                    if (collectPrefixes) {
                        prefixSet.add("");
                        break;
                    }
                    break;
            }
            readStructure();
            item = peekStructure();
        } while ((item & 240) == 64);
        return item;
    }

    private void writeAttributes(int item, XMLStreamWriter writer) throws XMLStreamException {
        do {
            switch (getAIIState(item)) {
                case 1:
                    String uri = readStructureString();
                    String localName = readStructureString();
                    String prefix = getPrefixFromQName(readStructureString());
                    writer.writeAttribute(prefix, uri, localName, readContentString());
                    break;
                case 2:
                    writer.writeAttribute(readStructureString(), readStructureString(), readStructureString(), readContentString());
                    break;
                case 3:
                    writer.writeAttribute(readStructureString(), readStructureString(), readContentString());
                    break;
                case 4:
                    writer.writeAttribute(readStructureString(), readContentString());
                    break;
            }
            readStructureString();
            readStructure();
            item = peekStructure();
        } while ((item & 240) == 48);
    }
}
