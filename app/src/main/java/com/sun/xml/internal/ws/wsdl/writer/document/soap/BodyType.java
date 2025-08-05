package com.sun.xml.internal.ws.wsdl.writer.document.soap;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/soap/BodyType.class */
public interface BodyType extends TypedXmlWriter {
    @XmlAttribute
    BodyType encodingStyle(String str);

    @XmlAttribute
    BodyType namespace(String str);

    @XmlAttribute
    BodyType use(String str);

    @XmlAttribute
    BodyType parts(String str);
}
