package com.sun.xml.internal.stream.buffer.stax;

import com.sun.xml.internal.org.jvnet.staxex.Base64Data;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/StreamReaderBufferCreator.class */
public class StreamReaderBufferCreator extends StreamBufferCreator {
    private int _eventType;
    private boolean _storeInScopeNamespacesOnElementFragment;
    private Map<String, Integer> _inScopePrefixes;

    public StreamReaderBufferCreator() {
    }

    public StreamReaderBufferCreator(MutableXMLStreamBuffer buffer) {
        setBuffer(buffer);
    }

    public MutableXMLStreamBuffer create(XMLStreamReader reader) throws XMLStreamException {
        if (this._buffer == null) {
            createBuffer();
        }
        store(reader);
        return getXMLStreamBuffer();
    }

    public MutableXMLStreamBuffer createElementFragment(XMLStreamReader reader, boolean storeInScopeNamespaces) throws XMLStreamException {
        if (this._buffer == null) {
            createBuffer();
        }
        if (!reader.hasNext()) {
            return this._buffer;
        }
        this._storeInScopeNamespacesOnElementFragment = storeInScopeNamespaces;
        this._eventType = reader.getEventType();
        if (this._eventType != 1) {
            do {
                this._eventType = reader.next();
                if (this._eventType == 1) {
                    break;
                }
            } while (this._eventType != 8);
        }
        if (storeInScopeNamespaces) {
            this._inScopePrefixes = new HashMap();
        }
        storeElementAndChildren(reader);
        return getXMLStreamBuffer();
    }

    private void store(XMLStreamReader reader) throws XMLStreamException {
        if (!reader.hasNext()) {
            return;
        }
        this._eventType = reader.getEventType();
        switch (this._eventType) {
            case 1:
                storeElementAndChildren(reader);
                break;
            case 7:
                storeDocumentAndChildren(reader);
                break;
            default:
                throw new XMLStreamException("XMLStreamReader not positioned at a document or element");
        }
        increaseTreeCount();
    }

    private void storeDocumentAndChildren(XMLStreamReader reader) throws XMLStreamException {
        storeStructure(16);
        this._eventType = reader.next();
        while (this._eventType != 8) {
            switch (this._eventType) {
                case 1:
                    storeElementAndChildren(reader);
                    continue;
                case 3:
                    storeProcessingInstruction(reader);
                    break;
                case 5:
                    storeComment(reader);
                    break;
            }
            this._eventType = reader.next();
        }
        storeStructure(144);
    }

    private void storeElementAndChildren(XMLStreamReader reader) throws XMLStreamException {
        if (reader instanceof XMLStreamReaderEx) {
            storeElementAndChildrenEx((XMLStreamReaderEx) reader);
        } else {
            storeElementAndChildrenNoEx(reader);
        }
    }

    private void storeElementAndChildrenEx(XMLStreamReaderEx reader) throws XMLStreamException {
        int depth = 1;
        if (this._storeInScopeNamespacesOnElementFragment) {
            storeElementWithInScopeNamespaces(reader);
        } else {
            storeElement(reader);
        }
        while (depth > 0) {
            this._eventType = reader.next();
            switch (this._eventType) {
                case 1:
                    depth++;
                    storeElement(reader);
                    break;
                case 2:
                    depth--;
                    storeStructure(144);
                    break;
                case 3:
                    storeProcessingInstruction(reader);
                    break;
                case 4:
                case 6:
                case 12:
                    CharSequence c2 = reader.getPCDATA();
                    if (c2 instanceof Base64Data) {
                        storeStructure(92);
                        storeContentObject(c2);
                        break;
                    } else {
                        storeContentCharacters(80, reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
                        break;
                    }
                case 5:
                    storeComment(reader);
                    break;
                case 10:
                    storeAttributes(reader);
                    break;
                case 13:
                    storeNamespaceAttributes(reader);
                    break;
            }
        }
        this._eventType = reader.next();
    }

    private void storeElementAndChildrenNoEx(XMLStreamReader reader) throws XMLStreamException {
        int depth = 1;
        if (this._storeInScopeNamespacesOnElementFragment) {
            storeElementWithInScopeNamespaces(reader);
        } else {
            storeElement(reader);
        }
        while (depth > 0) {
            this._eventType = reader.next();
            switch (this._eventType) {
                case 1:
                    depth++;
                    storeElement(reader);
                    break;
                case 2:
                    depth--;
                    storeStructure(144);
                    break;
                case 3:
                    storeProcessingInstruction(reader);
                    break;
                case 4:
                case 6:
                case 12:
                    storeContentCharacters(80, reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
                    break;
                case 5:
                    storeComment(reader);
                    break;
                case 10:
                    storeAttributes(reader);
                    break;
                case 13:
                    storeNamespaceAttributes(reader);
                    break;
            }
        }
        this._eventType = reader.next();
    }

    private void storeElementWithInScopeNamespaces(XMLStreamReader reader) {
        storeQualifiedName(32, reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
        if (reader.getNamespaceCount() > 0) {
            storeNamespaceAttributes(reader);
        }
        if (reader.getAttributeCount() > 0) {
            storeAttributes(reader);
        }
    }

    private void storeElement(XMLStreamReader reader) {
        storeQualifiedName(32, reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
        if (reader.getNamespaceCount() > 0) {
            storeNamespaceAttributes(reader);
        }
        if (reader.getAttributeCount() > 0) {
            storeAttributes(reader);
        }
    }

    public void storeElement(String nsURI, String localName, String prefix, String[] ns) {
        storeQualifiedName(32, prefix, nsURI, localName);
        storeNamespaceAttributes(ns);
    }

    public void storeEndElement() {
        storeStructure(144);
    }

    private void storeNamespaceAttributes(XMLStreamReader reader) {
        int count = reader.getNamespaceCount();
        for (int i2 = 0; i2 < count; i2++) {
            storeNamespaceAttribute(reader.getNamespacePrefix(i2), reader.getNamespaceURI(i2));
        }
    }

    private void storeNamespaceAttributes(String[] ns) {
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < ns.length) {
                storeNamespaceAttribute(ns[i3], ns[i3 + 1]);
                i2 = i3 + 2;
            } else {
                return;
            }
        }
    }

    private void storeAttributes(XMLStreamReader reader) {
        int count = reader.getAttributeCount();
        for (int i2 = 0; i2 < count; i2++) {
            storeAttribute(reader.getAttributePrefix(i2), reader.getAttributeNamespace(i2), reader.getAttributeLocalName(i2), reader.getAttributeType(i2), reader.getAttributeValue(i2));
        }
    }

    private void storeComment(XMLStreamReader reader) {
        storeContentCharacters(96, reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
    }

    private void storeProcessingInstruction(XMLStreamReader reader) {
        storeProcessingInstruction(reader.getPITarget(), reader.getPIData());
    }
}
