package com.sun.org.apache.xerces.internal.impl.xs.opti;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/NodeImpl.class */
public class NodeImpl extends DefaultNode {
    String prefix;
    String localpart;
    String rawname;
    String uri;
    short nodeType;
    boolean hidden;

    public NodeImpl() {
    }

    public NodeImpl(String prefix, String localpart, String rawname, String uri, short nodeType) {
        this.prefix = prefix;
        this.localpart = localpart;
        this.rawname = rawname;
        this.uri = uri;
        this.nodeType = nodeType;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public String getNodeName() {
        return this.rawname;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public String getNamespaceURI() {
        return this.uri;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public String getPrefix() {
        return this.prefix;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public String getLocalName() {
        return this.localpart;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, org.w3c.dom.Node
    public short getNodeType() {
        return this.nodeType;
    }

    public void setReadOnly(boolean hide, boolean deep) {
        this.hidden = hide;
    }

    public boolean getReadOnly() {
        return this.hidden;
    }
}
