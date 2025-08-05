package com.sun.xml.internal.ws.streaming;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/xml/internal/ws/streaming/XMLStreamReaderUtil.class */
public class XMLStreamReaderUtil {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XMLStreamReaderUtil.class.desiredAssertionStatus();
    }

    private XMLStreamReaderUtil() {
    }

    public static void close(XMLStreamReader reader) {
        try {
            reader.close();
        } catch (XMLStreamException e2) {
            throw wrapException(e2);
        }
    }

    public static void readRest(XMLStreamReader reader) {
        while (reader.getEventType() != 8) {
            try {
                reader.next();
            } catch (XMLStreamException e2) {
                throw wrapException(e2);
            }
        }
    }

    public static int next(XMLStreamReader reader) {
        int readerEvent;
        try {
            readerEvent = reader.next();
        } catch (XMLStreamException e2) {
            throw wrapException(e2);
        }
        while (readerEvent != 8) {
            switch (readerEvent) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 12:
                    return readerEvent;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    readerEvent = reader.next();
            }
            throw wrapException(e2);
        }
        return readerEvent;
    }

    public static int nextElementContent(XMLStreamReader reader) {
        int state = nextContent(reader);
        if (state == 4) {
            throw new XMLStreamReaderException("xmlreader.unexpectedCharacterContent", reader.getText());
        }
        return state;
    }

    public static void toNextTag(XMLStreamReader reader, QName name) {
        if (reader.getEventType() != 1 && reader.getEventType() != 2) {
            nextElementContent(reader);
        }
        if (reader.getEventType() == 2 && name.equals(reader.getName())) {
            nextElementContent(reader);
        }
    }

    public static String nextWhiteSpaceContent(XMLStreamReader reader) {
        next(reader);
        return currentWhiteSpaceContent(reader);
    }

    public static String currentWhiteSpaceContent(XMLStreamReader reader) {
        StringBuilder whiteSpaces = null;
        while (true) {
            switch (reader.getEventType()) {
                case 1:
                case 2:
                case 8:
                    if (whiteSpaces == null) {
                        return null;
                    }
                    return whiteSpaces.toString();
                case 4:
                    if (reader.isWhiteSpace()) {
                        if (whiteSpaces == null) {
                            whiteSpaces = new StringBuilder();
                        }
                        whiteSpaces.append(reader.getText());
                        break;
                    } else {
                        throw new XMLStreamReaderException("xmlreader.unexpectedCharacterContent", reader.getText());
                    }
            }
            next(reader);
        }
    }

    public static int nextContent(XMLStreamReader reader) {
        while (true) {
            int state = next(reader);
            switch (state) {
                case 1:
                case 2:
                case 8:
                    return state;
                case 4:
                    if (!reader.isWhiteSpace()) {
                        return 4;
                    }
                    break;
            }
        }
    }

    public static void skipElement(XMLStreamReader reader) {
        if (!$assertionsDisabled && reader.getEventType() != 1) {
            throw new AssertionError();
        }
        skipTags(reader, true);
        if (!$assertionsDisabled && reader.getEventType() != 2) {
            throw new AssertionError();
        }
    }

    public static void skipSiblings(XMLStreamReader reader, QName parent) {
        skipTags(reader, reader.getName().equals(parent));
        if (!$assertionsDisabled && reader.getEventType() != 2) {
            throw new AssertionError();
        }
    }

    private static void skipTags(XMLStreamReader reader, boolean exitCondition) {
        int tags = 0;
        while (true) {
            try {
                int state = reader.next();
                if (state != 8) {
                    if (state == 1) {
                        tags++;
                    } else if (state != 2) {
                        continue;
                    } else if (tags == 0 && exitCondition) {
                        return;
                    } else {
                        tags--;
                    }
                } else {
                    return;
                }
            } catch (XMLStreamException e2) {
                throw wrapException(e2);
            }
        }
    }

    public static String getElementText(XMLStreamReader reader) {
        try {
            return reader.getElementText();
        } catch (XMLStreamException e2) {
            throw wrapException(e2);
        }
    }

    public static QName getElementQName(XMLStreamReader reader) {
        try {
            String text = reader.getElementText().trim();
            String prefix = text.substring(0, text.indexOf(58));
            String namespaceURI = reader.getNamespaceContext().getNamespaceURI(prefix);
            if (namespaceURI == null) {
                namespaceURI = "";
            }
            String localPart = text.substring(text.indexOf(58) + 1, text.length());
            return new QName(namespaceURI, localPart);
        } catch (XMLStreamException e2) {
            throw wrapException(e2);
        }
    }

    public static Attributes getAttributes(XMLStreamReader reader) {
        if (reader.getEventType() == 1 || reader.getEventType() == 10) {
            return new AttributesImpl(reader);
        }
        return null;
    }

    public static void verifyReaderState(XMLStreamReader reader, int expectedState) {
        int state = reader.getEventType();
        if (state != expectedState) {
            throw new XMLStreamReaderException("xmlreader.unexpectedState", getStateName(expectedState), getStateName(state));
        }
    }

    public static void verifyTag(XMLStreamReader reader, String namespaceURI, String localName) {
        if (!localName.equals(reader.getLocalName()) || !namespaceURI.equals(reader.getNamespaceURI())) {
            throw new XMLStreamReaderException("xmlreader.unexpectedState.tag", VectorFormat.DEFAULT_PREFIX + namespaceURI + "}" + localName, VectorFormat.DEFAULT_PREFIX + reader.getNamespaceURI() + "}" + reader.getLocalName());
        }
    }

    public static void verifyTag(XMLStreamReader reader, QName name) {
        verifyTag(reader, name.getNamespaceURI(), name.getLocalPart());
    }

    public static String getStateName(XMLStreamReader reader) {
        return getStateName(reader.getEventType());
    }

    public static String getStateName(int state) {
        switch (state) {
            case 1:
                return "START_ELEMENT";
            case 2:
                return "END_ELEMENT";
            case 3:
                return "PROCESSING_INSTRUCTION";
            case 4:
                return "CHARACTERS";
            case 5:
                return "COMMENT";
            case 6:
                return "SPACE";
            case 7:
                return "START_DOCUMENT";
            case 8:
                return "END_DOCUMENT";
            case 9:
                return "ENTITY_REFERENCE";
            case 10:
                return "ATTRIBUTE";
            case 11:
                return "DTD";
            case 12:
                return "CDATA";
            case 13:
                return "NAMESPACE";
            case 14:
                return "NOTATION_DECLARATION";
            case 15:
                return "ENTITY_DECLARATION";
            default:
                return "UNKNOWN";
        }
    }

    private static XMLStreamReaderException wrapException(XMLStreamException e2) {
        return new XMLStreamReaderException("xmlreader.ioException", e2);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/streaming/XMLStreamReaderUtil$AttributesImpl.class */
    public static class AttributesImpl implements Attributes {
        static final String XMLNS_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";
        AttributeInfo[] atInfos;

        /* loaded from: rt.jar:com/sun/xml/internal/ws/streaming/XMLStreamReaderUtil$AttributesImpl$AttributeInfo.class */
        static class AttributeInfo {
            private QName name;
            private String value;

            public AttributeInfo(QName name, String value) {
                this.name = name;
                if (value == null) {
                    this.value = "";
                } else {
                    this.value = value;
                }
            }

            QName getName() {
                return this.name;
            }

            String getValue() {
                return this.value;
            }

            String getLocalName() {
                if (isNamespaceDeclaration()) {
                    if (this.name.getLocalPart().equals("")) {
                        return "xmlns";
                    }
                    return "xmlns:" + this.name.getLocalPart();
                }
                return this.name.getLocalPart();
            }

            boolean isNamespaceDeclaration() {
                return this.name.getNamespaceURI() == "http://www.w3.org/2000/xmlns/";
            }
        }

        public AttributesImpl(XMLStreamReader reader) {
            if (reader == null) {
                this.atInfos = new AttributeInfo[0];
                return;
            }
            int index = 0;
            int namespaceCount = reader.getNamespaceCount();
            int attributeCount = reader.getAttributeCount();
            this.atInfos = new AttributeInfo[namespaceCount + attributeCount];
            for (int i2 = 0; i2 < namespaceCount; i2++) {
                String namespacePrefix = reader.getNamespacePrefix(i2);
                if (namespacePrefix == null) {
                    namespacePrefix = "";
                }
                int i3 = index;
                index++;
                this.atInfos[i3] = new AttributeInfo(new QName("http://www.w3.org/2000/xmlns/", namespacePrefix, "xmlns"), reader.getNamespaceURI(i2));
            }
            for (int i4 = 0; i4 < attributeCount; i4++) {
                int i5 = index;
                index++;
                this.atInfos[i5] = new AttributeInfo(reader.getAttributeName(i4), reader.getAttributeValue(i4));
            }
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public int getLength() {
            return this.atInfos.length;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public String getLocalName(int index) {
            if (index >= 0 && index < this.atInfos.length) {
                return this.atInfos[index].getLocalName();
            }
            return null;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public QName getName(int index) {
            if (index >= 0 && index < this.atInfos.length) {
                return this.atInfos[index].getName();
            }
            return null;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public String getPrefix(int index) {
            if (index >= 0 && index < this.atInfos.length) {
                return this.atInfos[index].getName().getPrefix();
            }
            return null;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public String getURI(int index) {
            if (index >= 0 && index < this.atInfos.length) {
                return this.atInfos[index].getName().getNamespaceURI();
            }
            return null;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public String getValue(int index) {
            if (index >= 0 && index < this.atInfos.length) {
                return this.atInfos[index].getValue();
            }
            return null;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public String getValue(QName name) {
            int index = getIndex(name);
            if (index != -1) {
                return this.atInfos[index].getValue();
            }
            return null;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public String getValue(String localName) {
            int index = getIndex(localName);
            if (index != -1) {
                return this.atInfos[index].getValue();
            }
            return null;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public String getValue(String uri, String localName) {
            int index = getIndex(uri, localName);
            if (index != -1) {
                return this.atInfos[index].getValue();
            }
            return null;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public boolean isNamespaceDeclaration(int index) {
            if (index >= 0 && index < this.atInfos.length) {
                return this.atInfos[index].isNamespaceDeclaration();
            }
            return false;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public int getIndex(QName name) {
            for (int i2 = 0; i2 < this.atInfos.length; i2++) {
                if (this.atInfos[i2].getName().equals(name)) {
                    return i2;
                }
            }
            return -1;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public int getIndex(String localName) {
            for (int i2 = 0; i2 < this.atInfos.length; i2++) {
                if (this.atInfos[i2].getName().getLocalPart().equals(localName)) {
                    return i2;
                }
            }
            return -1;
        }

        @Override // com.sun.xml.internal.ws.streaming.Attributes
        public int getIndex(String uri, String localName) {
            for (int i2 = 0; i2 < this.atInfos.length; i2++) {
                QName qName = this.atInfos[i2].getName();
                if (qName.getNamespaceURI().equals(uri) && qName.getLocalPart().equals(localName)) {
                    return i2;
                }
            }
            return -1;
        }
    }
}
