package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/Header.class */
public interface Header {
    boolean isIgnorable(@NotNull SOAPVersion sOAPVersion, @NotNull Set<String> set);

    @NotNull
    String getRole(@NotNull SOAPVersion sOAPVersion);

    boolean isRelay();

    @NotNull
    String getNamespaceURI();

    @NotNull
    String getLocalPart();

    @Nullable
    String getAttribute(@NotNull String str, @NotNull String str2);

    @Nullable
    String getAttribute(@NotNull QName qName);

    XMLStreamReader readHeader() throws XMLStreamException;

    <T> T readAsJAXB(Unmarshaller unmarshaller) throws JAXBException;

    <T> T readAsJAXB(Bridge<T> bridge) throws JAXBException;

    <T> T readAsJAXB(XMLBridge<T> xMLBridge) throws JAXBException;

    @NotNull
    WSEndpointReference readAsEPR(AddressingVersion addressingVersion) throws XMLStreamException;

    void writeTo(XMLStreamWriter xMLStreamWriter) throws XMLStreamException;

    void writeTo(SOAPMessage sOAPMessage) throws SOAPException;

    void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException;

    @NotNull
    String getStringContent();
}
