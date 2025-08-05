package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("part")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Part.class */
public interface Part extends TypedXmlWriter, OpenAtts {
    @XmlAttribute
    Part element(QName qName);

    @XmlAttribute
    Part type(QName qName);

    @XmlAttribute
    Part name(String str);
}
