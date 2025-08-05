package com.sun.org.apache.xml.internal.serializer;

import javax.xml.transform.Transformer;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/TransformStateSetter.class */
public interface TransformStateSetter {
    void setCurrentNode(Node node);

    void resetState(Transformer transformer);
}
