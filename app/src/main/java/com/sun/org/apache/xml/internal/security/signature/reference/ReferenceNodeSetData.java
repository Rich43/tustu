package com.sun.org.apache.xml.internal.security.signature.reference;

import java.util.Iterator;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/reference/ReferenceNodeSetData.class */
public interface ReferenceNodeSetData extends ReferenceData {
    Iterator<Node> iterator();
}
