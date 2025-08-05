package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.xml.internal.bind.marshaller.NoEscapeHandler;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx;
import javax.xml.stream.XMLStreamException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/StAXExStreamWriterOutput.class */
public final class StAXExStreamWriterOutput extends XMLStreamWriterOutput {
    private final XMLStreamWriterEx out;

    public StAXExStreamWriterOutput(XMLStreamWriterEx out) {
        super(out, NoEscapeHandler.theInstance);
        this.out = out;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void text(Pcdata value, boolean needsSeparatingWhitespace) throws XMLStreamException {
        if (needsSeparatingWhitespace) {
            this.out.writeCharacters(" ");
        }
        if (!(value instanceof Base64Data)) {
            this.out.writeCharacters(value.toString());
        } else {
            Base64Data v2 = (Base64Data) value;
            this.out.writeBinary(v2.getDataHandler());
        }
    }
}
