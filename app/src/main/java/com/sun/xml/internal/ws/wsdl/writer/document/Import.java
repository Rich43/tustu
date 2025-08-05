package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("import")
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Import.class */
public interface Import extends TypedXmlWriter, Documented {
    @XmlAttribute
    Import location(String str);

    @XmlAttribute
    Import namespace(String str);
}
