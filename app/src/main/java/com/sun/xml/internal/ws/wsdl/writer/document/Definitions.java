package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("definitions")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Definitions.class */
public interface Definitions extends TypedXmlWriter, Documented {
    @XmlAttribute
    Definitions name(String str);

    @XmlAttribute
    Definitions targetNamespace(String str);

    @XmlElement
    Service service();

    @XmlElement
    Binding binding();

    @XmlElement
    PortType portType();

    @XmlElement
    Message message();

    @XmlElement
    Types types();

    @XmlElement("import")
    Import _import();
}
