package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/StAXExConnector.class */
final class StAXExConnector extends StAXStreamConnector {
    private final XMLStreamReaderEx in;

    public StAXExConnector(XMLStreamReaderEx in, XmlVisitor visitor) {
        super(in, visitor);
        this.in = in;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXStreamConnector
    protected void handleCharacters() throws XMLStreamException, SAXException {
        if (this.predictor.expectText()) {
            CharSequence pcdata = this.in.getPCDATA();
            if (pcdata instanceof com.sun.xml.internal.org.jvnet.staxex.Base64Data) {
                com.sun.xml.internal.org.jvnet.staxex.Base64Data bd2 = (com.sun.xml.internal.org.jvnet.staxex.Base64Data) pcdata;
                Base64Data binary = new Base64Data();
                if (!bd2.hasData()) {
                    binary.set(bd2.getDataHandler());
                } else {
                    binary.set(bd2.get(), bd2.getDataLen(), bd2.getMimeType());
                }
                this.visitor.text(binary);
                this.textReported = true;
                return;
            }
            this.buffer.append(pcdata);
        }
    }
}
