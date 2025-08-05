package com.sun.org.apache.xalan.internal.lib;

import com.sun.org.apache.xml.internal.utils.DOM2Helper;
import com.sun.org.apache.xpath.internal.NodeSet;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/lib/ExsltSets.class */
public class ExsltSets extends ExsltBase {
    public static NodeList leading(NodeList nl1, NodeList nl2) {
        if (nl2.getLength() == 0) {
            return nl1;
        }
        NodeSet ns1 = new NodeSet(nl1);
        NodeSet leadNodes = new NodeSet();
        Node endNode = nl2.item(0);
        if (!ns1.contains(endNode)) {
            return leadNodes;
        }
        for (int i2 = 0; i2 < nl1.getLength(); i2++) {
            Node testNode = nl1.item(i2);
            if (DOM2Helper.isNodeAfter(testNode, endNode) && !DOM2Helper.isNodeTheSame(testNode, endNode)) {
                leadNodes.addElement(testNode);
            }
        }
        return leadNodes;
    }

    public static NodeList trailing(NodeList nl1, NodeList nl2) {
        if (nl2.getLength() == 0) {
            return nl1;
        }
        NodeSet ns1 = new NodeSet(nl1);
        NodeSet trailNodes = new NodeSet();
        Node startNode = nl2.item(0);
        if (!ns1.contains(startNode)) {
            return trailNodes;
        }
        for (int i2 = 0; i2 < nl1.getLength(); i2++) {
            Node testNode = nl1.item(i2);
            if (DOM2Helper.isNodeAfter(startNode, testNode) && !DOM2Helper.isNodeTheSame(startNode, testNode)) {
                trailNodes.addElement(testNode);
            }
        }
        return trailNodes;
    }

    public static NodeList intersection(NodeList nl1, NodeList nl2) {
        NodeSet ns1 = new NodeSet(nl1);
        NodeSet ns2 = new NodeSet(nl2);
        NodeSet inter = new NodeSet();
        inter.setShouldCacheNodes(true);
        for (int i2 = 0; i2 < ns1.getLength(); i2++) {
            Node n2 = ns1.elementAt(i2);
            if (ns2.contains(n2)) {
                inter.addElement(n2);
            }
        }
        return inter;
    }

    public static NodeList difference(NodeList nl1, NodeList nl2) {
        NodeSet ns1 = new NodeSet(nl1);
        NodeSet ns2 = new NodeSet(nl2);
        NodeSet diff = new NodeSet();
        diff.setShouldCacheNodes(true);
        for (int i2 = 0; i2 < ns1.getLength(); i2++) {
            Node n2 = ns1.elementAt(i2);
            if (!ns2.contains(n2)) {
                diff.addElement(n2);
            }
        }
        return diff;
    }

    public static NodeList distinct(NodeList nl) {
        NodeSet dist = new NodeSet();
        dist.setShouldCacheNodes(true);
        Map<String, Node> stringTable = new HashMap<>();
        for (int i2 = 0; i2 < nl.getLength(); i2++) {
            Node currNode = nl.item(i2);
            String key = toString(currNode);
            if (key == null) {
                dist.addElement(currNode);
            } else if (!stringTable.containsKey(key)) {
                stringTable.put(key, currNode);
                dist.addElement(currNode);
            }
        }
        return dist;
    }

    public static boolean hasSameNode(NodeList nl1, NodeList nl2) {
        NodeSet ns1 = new NodeSet(nl1);
        NodeSet ns2 = new NodeSet(nl2);
        for (int i2 = 0; i2 < ns1.getLength(); i2++) {
            if (ns2.contains(ns1.elementAt(i2))) {
                return true;
            }
        }
        return false;
    }
}
