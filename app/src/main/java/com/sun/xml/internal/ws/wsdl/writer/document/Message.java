package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("message")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Message.class */
public interface Message extends TypedXmlWriter, Documented {
    @XmlAttribute
    Message name(String str);

    @XmlElement
    Part part();
}
