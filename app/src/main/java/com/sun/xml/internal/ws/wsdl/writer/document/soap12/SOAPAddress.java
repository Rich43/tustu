package com.sun.xml.internal.ws.wsdl.writer.document.soap12;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("address")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/soap12/SOAPAddress.class */
public interface SOAPAddress extends TypedXmlWriter {
    @XmlAttribute
    SOAPAddress location(String str);
}
