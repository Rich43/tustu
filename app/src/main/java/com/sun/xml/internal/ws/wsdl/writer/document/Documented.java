package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlElement;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Documented.class */
public interface Documented extends TypedXmlWriter {
    @XmlElement
    Documented documentation(String str);
}
