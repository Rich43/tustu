package com.sun.jndi.dns;

import java.util.Hashtable;

/* loaded from: rt.jar:com/sun/jndi/dns/NameNode.class */
class NameNode {
    private String label;
    private Hashtable<String, NameNode> children = null;
    private boolean isZoneCut = false;
    private int depth = 0;

    NameNode(String str) {
        this.label = str;
    }

    protected NameNode newNameNode(String str) {
        return new NameNode(str);
    }

    String getLabel() {
        return this.label;
    }

    int depth() {
        return this.depth;
    }

    boolean isZoneCut() {
        return this.isZoneCut;
    }

    void setZoneCut(boolean z2) {
        this.isZoneCut = z2;
    }

    Hashtable<String, NameNode> getChildren() {
        return this.children;
    }

    NameNode get(String str) {
        if (this.children != null) {
            return this.children.get(str);
        }
        return null;
    }

    NameNode get(DnsName dnsName, int i2) {
        NameNode nameNode = this;
        for (int i3 = i2; i3 < dnsName.size() && nameNode != null; i3++) {
            nameNode = nameNode.get(dnsName.getKey(i3));
        }
        return nameNode;
    }

    NameNode add(DnsName dnsName, int i2) {
        NameNode nameNode = this;
        for (int i3 = i2; i3 < dnsName.size(); i3++) {
            String str = dnsName.get(i3);
            String key = dnsName.getKey(i3);
            NameNode nameNodeNewNameNode = null;
            if (nameNode.children == null) {
                nameNode.children = new Hashtable<>();
            } else {
                nameNodeNewNameNode = nameNode.children.get(key);
            }
            if (nameNodeNewNameNode == null) {
                nameNodeNewNameNode = newNameNode(str);
                nameNodeNewNameNode.depth = nameNode.depth + 1;
                nameNode.children.put(key, nameNodeNewNameNode);
            }
            nameNode = nameNodeNewNameNode;
        }
        return nameNode;
    }
}
