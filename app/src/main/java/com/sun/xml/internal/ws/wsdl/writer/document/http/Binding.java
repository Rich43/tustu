package com.sun.xml.internal.ws.wsdl.writer.document.http;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;

@XmlElement(DeploymentDescriptorParser.ATTR_BINDING)
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/http/Binding.class */
public interface Binding extends TypedXmlWriter {
    @XmlAttribute
    Binding verb(String str);
}
