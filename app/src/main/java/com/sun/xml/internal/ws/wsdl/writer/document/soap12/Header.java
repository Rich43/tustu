package com.sun.xml.internal.ws.wsdl.writer.document.soap12;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("header")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/soap12/Header.class */
public interface Header extends TypedXmlWriter, BodyType {
    @XmlAttribute
    Header message(QName qName);

    @XmlElement
    HeaderFault headerFault();

    @XmlAttribute
    BodyType part(String str);
}
