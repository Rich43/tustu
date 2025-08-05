package com.sun.xml.internal.bind.v2.schemagen.episode;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/episode/Package.class */
public interface Package extends TypedXmlWriter {
    @XmlAttribute
    void name(String str);
}
