package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/ParamType.class */
public interface ParamType extends TypedXmlWriter, Documented {
    @XmlAttribute
    ParamType message(QName qName);

    @XmlAttribute
    ParamType name(String str);
}
