package com.sun.org.apache.xml.internal.serialize;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/DOMSerializer.class */
public interface DOMSerializer {
    void serialize(Element element) throws IOException;

    void serialize(Document document) throws IOException;

    void serialize(DocumentFragment documentFragment) throws IOException;
}
