package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("operation")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Operation.class */
public interface Operation extends TypedXmlWriter, Documented {
    @XmlElement
    ParamType input();

    @XmlElement
    ParamType output();

    @XmlElement
    FaultType fault();

    @XmlAttribute
    Operation name(String str);

    @XmlAttribute
    Operation parameterOrder(String str);
}
