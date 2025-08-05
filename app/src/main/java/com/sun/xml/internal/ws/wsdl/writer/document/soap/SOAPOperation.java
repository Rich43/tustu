package com.sun.xml.internal.ws.wsdl.writer.document.soap;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("operation")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/soap/SOAPOperation.class */
public interface SOAPOperation extends TypedXmlWriter {
    @XmlAttribute
    SOAPOperation soapAction(String str);

    @XmlAttribute
    SOAPOperation style(String str);
}
