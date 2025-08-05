package com.sun.org.apache.xml.internal.dtm.ref;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/ExtendedType.class */
public final class ExtendedType {
    private int nodetype;
    private String namespace;
    private String localName;
    private int hash;

    public ExtendedType(int nodetype, String namespace, String localName) {
        this.nodetype = nodetype;
        this.namespace = namespace;
        this.localName = localName;
        this.hash = nodetype + namespace.hashCode() + localName.hashCode();
    }

    public ExtendedType(int nodetype, String namespace, String localName, int hash) {
        this.nodetype = nodetype;
        this.namespace = namespace;
        this.localName = localName;
        this.hash = hash;
    }

    protected void redefine(int nodetype, String namespace, String localName) {
        this.nodetype = nodetype;
        this.namespace = namespace;
        this.localName = localName;
        this.hash = nodetype + namespace.hashCode() + localName.hashCode();
    }

    protected void redefine(int nodetype, String namespace, String localName, int hash) {
        this.nodetype = nodetype;
        this.namespace = namespace;
        this.localName = localName;
        this.hash = hash;
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean equals(ExtendedType other) {
        try {
            if (other.nodetype == this.nodetype && other.localName.equals(this.localName)) {
                if (other.namespace.equals(this.namespace)) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public int getNodeType() {
        return this.nodetype;
    }

    public String getLocalName() {
        return this.localName;
    }

    public String getNamespace() {
        return this.namespace;
    }
}
