package com.sun.org.apache.xml.internal.security.signature;

import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/NodeFilter.class */
public interface NodeFilter {
    int isNodeInclude(Node node);

    int isNodeIncludeDO(Node node, int i2);
}
