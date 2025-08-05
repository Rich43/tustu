package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/DOMSerializer.class */
public interface DOMSerializer {
    void serialize(Node node) throws IOException;
}
