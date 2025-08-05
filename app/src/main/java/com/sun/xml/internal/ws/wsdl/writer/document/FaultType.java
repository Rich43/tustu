package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/FaultType.class */
public interface FaultType extends TypedXmlWriter, Documented {
    @XmlAttribute
    FaultType message(QName qName);

    @XmlAttribute
    FaultType name(String str);
}
