package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import javax.xml.namespace.QName;

@XmlElement(DeploymentDescriptorParser.ATTR_PORT)
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Port.class */
public interface Port extends TypedXmlWriter, Documented {
    @XmlAttribute
    Port name(String str);

    @XmlAttribute
    Port arrayType(String str);

    @XmlAttribute
    Port binding(QName qName);
}
