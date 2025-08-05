package com.sun.xml.internal.ws.message.stream;

import com.sun.istack.internal.FinalArrayList;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.message.Util;
import com.sun.xml.internal.ws.message.stream.StreamHeader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/stream/StreamHeader12.class */
public class StreamHeader12 extends StreamHeader {
    protected static final String SOAP_1_2_MUST_UNDERSTAND = "mustUnderstand";
    protected static final String SOAP_1_2_ROLE = "role";
    protected static final String SOAP_1_2_RELAY = "relay";

    public StreamHeader12(XMLStreamReader reader, XMLStreamBuffer mark) {
        super(reader, mark);
    }

    public StreamHeader12(XMLStreamReader reader) throws XMLStreamException {
        super(reader);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamHeader
    protected final FinalArrayList<StreamHeader.Attribute> processHeaderAttributes(XMLStreamReader reader) {
        FinalArrayList<StreamHeader.Attribute> atts = null;
        this._role = "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver";
        for (int i2 = 0; i2 < reader.getAttributeCount(); i2++) {
            String localName = reader.getAttributeLocalName(i2);
            String namespaceURI = reader.getAttributeNamespace(i2);
            String value = reader.getAttributeValue(i2);
            if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI)) {
                if ("mustUnderstand".equals(localName)) {
                    this._isMustUnderstand = Util.parseBool(value);
                } else if ("role".equals(localName)) {
                    if (value != null && value.length() > 0) {
                        this._role = value;
                    }
                } else if (SOAP_1_2_RELAY.equals(localName)) {
                    this._isRelay = Util.parseBool(value);
                }
            }
            if (atts == null) {
                atts = new FinalArrayList<>();
            }
            atts.add(new StreamHeader.Attribute(namespaceURI, localName, value));
        }
        return atts;
    }
}
