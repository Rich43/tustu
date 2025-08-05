package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("fault")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Fault.class */
public interface Fault extends TypedXmlWriter, StartWithExtensionsType {
    @XmlAttribute
    Fault name(String str);
}
