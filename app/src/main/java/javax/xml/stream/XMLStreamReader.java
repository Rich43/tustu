package javax.xml.stream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/stream/XMLStreamReader.class */
public interface XMLStreamReader extends XMLStreamConstants {
    Object getProperty(String str) throws IllegalArgumentException;

    int next() throws XMLStreamException;

    void require(int i2, String str, String str2) throws XMLStreamException;

    String getElementText() throws XMLStreamException;

    int nextTag() throws XMLStreamException;

    boolean hasNext() throws XMLStreamException;

    void close() throws XMLStreamException;

    String getNamespaceURI(String str);

    boolean isStartElement();

    boolean isEndElement();

    boolean isCharacters();

    boolean isWhiteSpace();

    String getAttributeValue(String str, String str2);

    int getAttributeCount();

    QName getAttributeName(int i2);

    String getAttributeNamespace(int i2);

    String getAttributeLocalName(int i2);

    String getAttributePrefix(int i2);

    String getAttributeType(int i2);

    String getAttributeValue(int i2);

    boolean isAttributeSpecified(int i2);

    int getNamespaceCount();

    String getNamespacePrefix(int i2);

    String getNamespaceURI(int i2);

    NamespaceContext getNamespaceContext();

    int getEventType();

    String getText();

    char[] getTextCharacters();

    int getTextCharacters(int i2, char[] cArr, int i3, int i4) throws XMLStreamException;

    int getTextStart();

    int getTextLength();

    String getEncoding();

    boolean hasText();

    Location getLocation();

    QName getName();

    String getLocalName();

    boolean hasName();

    String getNamespaceURI();

    String getPrefix();

    String getVersion();

    boolean isStandalone();

    boolean standaloneSet();

    String getCharacterEncodingScheme();

    String getPITarget();

    String getPIData();
}
