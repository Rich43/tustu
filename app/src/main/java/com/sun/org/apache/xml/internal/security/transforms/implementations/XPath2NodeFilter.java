package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* compiled from: TransformXPath2Filter.java */
/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/XPath2NodeFilter.class */
class XPath2NodeFilter implements NodeFilter {
    boolean hasUnionFilter;
    boolean hasSubtractFilter;
    boolean hasIntersectFilter;
    Set<Node> unionNodes;
    Set<Node> subtractNodes;
    Set<Node> intersectNodes;
    int inSubtract = -1;
    int inIntersect = -1;
    int inUnion = -1;

    XPath2NodeFilter(List<NodeList> list, List<NodeList> list2, List<NodeList> list3) {
        this.hasUnionFilter = !list.isEmpty();
        this.unionNodes = convertNodeListToSet(list);
        this.hasSubtractFilter = !list2.isEmpty();
        this.subtractNodes = convertNodeListToSet(list2);
        this.hasIntersectFilter = !list3.isEmpty();
        this.intersectNodes = convertNodeListToSet(list3);
    }

    @Override // com.sun.org.apache.xml.internal.security.signature.NodeFilter
    public int isNodeInclude(Node node) {
        int i2 = 1;
        if (this.hasSubtractFilter && rooted(node, this.subtractNodes)) {
            i2 = -1;
        } else if (this.hasIntersectFilter && !rooted(node, this.intersectNodes)) {
            i2 = 0;
        }
        if (i2 == 1) {
            return 1;
        }
        if (this.hasUnionFilter) {
            if (rooted(node, this.unionNodes)) {
                return 1;
            }
            i2 = 0;
        }
        return i2;
    }

    @Override // com.sun.org.apache.xml.internal.security.signature.NodeFilter
    public int isNodeIncludeDO(Node node, int i2) {
        int i3 = 1;
        if (this.hasSubtractFilter) {
            if (this.inSubtract == -1 || i2 <= this.inSubtract) {
                if (inList(node, this.subtractNodes)) {
                    this.inSubtract = i2;
                } else {
                    this.inSubtract = -1;
                }
            }
            if (this.inSubtract != -1) {
                i3 = -1;
            }
        }
        if (i3 != -1 && this.hasIntersectFilter && (this.inIntersect == -1 || i2 <= this.inIntersect)) {
            if (!inList(node, this.intersectNodes)) {
                this.inIntersect = -1;
                i3 = 0;
            } else {
                this.inIntersect = i2;
            }
        }
        if (i2 <= this.inUnion) {
            this.inUnion = -1;
        }
        if (i3 == 1) {
            return 1;
        }
        if (this.hasUnionFilter) {
            if (this.inUnion == -1 && inList(node, this.unionNodes)) {
                this.inUnion = i2;
            }
            if (this.inUnion != -1) {
                return 1;
            }
            i3 = 0;
        }
        return i3;
    }

    static boolean rooted(Node node, Set<Node> set) {
        if (set.isEmpty()) {
            return false;
        }
        if (set.contains(node)) {
            return true;
        }
        Iterator<Node> it = set.iterator();
        while (it.hasNext()) {
            if (XMLUtils.isDescendantOrSelf(it.next(), node)) {
                return true;
            }
        }
        return false;
    }

    static boolean inList(Node node, Set<Node> set) {
        return set.contains(node);
    }

    private static Set<Node> convertNodeListToSet(List<NodeList> list) {
        HashSet hashSet = new HashSet();
        for (NodeList nodeList : list) {
            int length = nodeList.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                hashSet.add(nodeList.item(i2));
            }
        }
        return hashSet;
    }
}
