package com.sun.xml.internal.ws.wsdl.writer.document.soap12;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("headerFault")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/soap12/HeaderFault.class */
public interface HeaderFault extends TypedXmlWriter, BodyType {
    @XmlAttribute
    HeaderFault message(QName qName);
}
