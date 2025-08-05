package com.sun.xml.internal.ws.wsdl.writer.document.http;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("address")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/http/Address.class */
public interface Address extends TypedXmlWriter {
    @XmlAttribute
    Address location(String str);
}
