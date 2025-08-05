package com.sun.xml.internal.ws.wsdl.writer.document.soap12;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("fault")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/soap12/SOAPFault.class */
public interface SOAPFault extends TypedXmlWriter, BodyType {
    @XmlAttribute
    SOAPFault name(String str);
}
