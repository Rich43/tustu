package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;

@XmlElement(DeploymentDescriptorParser.ATTR_SERVICE)
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Service.class */
public interface Service extends TypedXmlWriter, Documented {
    @XmlAttribute
    Service name(String str);

    @XmlElement
    Port port();
}
