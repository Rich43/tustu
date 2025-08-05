package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/DOMInputSource.class */
public final class DOMInputSource extends XMLInputSource {
    private Node fNode;

    public DOMInputSource() {
        this(null);
    }

    public DOMInputSource(Node node) {
        super(null, getSystemIdFromNode(node), null);
        this.fNode = node;
    }

    public DOMInputSource(Node node, String systemId) {
        super(null, systemId, null);
        this.fNode = node;
    }

    public Node getNode() {
        return this.fNode;
    }

    public void setNode(Node node) {
        this.fNode = node;
    }

    private static String getSystemIdFromNode(Node node) {
        if (node != null) {
            try {
                return node.getBaseURI();
            } catch (Exception e2) {
                return null;
            } catch (NoSuchMethodError e3) {
                return null;
            }
        }
        return null;
    }
}
