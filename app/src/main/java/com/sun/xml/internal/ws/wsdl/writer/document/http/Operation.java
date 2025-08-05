package com.sun.xml.internal.ws.wsdl.writer.document.http;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("operation")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/http/Operation.class */
public interface Operation extends TypedXmlWriter {
    @XmlAttribute
    Operation location(String str);
}
