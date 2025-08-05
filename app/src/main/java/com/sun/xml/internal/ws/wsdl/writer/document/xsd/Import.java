package com.sun.xml.internal.ws.wsdl.writer.document.xsd;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.sun.xml.internal.ws.wsdl.writer.document.Documented;

@XmlElement("import")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/xsd/Import.class */
public interface Import extends TypedXmlWriter, Documented {
    @XmlAttribute
    Import schemaLocation(String str);

    @XmlAttribute
    Import namespace(String str);
}
