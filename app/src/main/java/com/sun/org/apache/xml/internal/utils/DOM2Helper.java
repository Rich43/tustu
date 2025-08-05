package com.sun.org.apache.xml.internal.utils;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/DOM2Helper.class */
public final class DOM2Helper {
    private DOM2Helper() {
    }

    public static String getLocalNameOfNode(Node n2) {
        String name = n2.getLocalName();
        return null == name ? getLocalNameOfNodeFallback(n2) : name;
    }

    private static String getLocalNameOfNodeFallback(Node n2) {
        String qname = n2.getNodeName();
        int index = qname.indexOf(58);
        return index < 0 ? qname : qname.substring(index + 1);
    }

    public static String getNamespaceOfNode(Node n2) {
        return n2.getNamespaceURI();
    }

    public static boolean isNodeAfter(Node node1, Node node2) {
        if (node1 == node2 || isNodeTheSame(node1, node2)) {
            return true;
        }
        boolean isNodeAfter = true;
        Node parent1 = getParentOfNode(node1);
        Node parent2 = getParentOfNode(node2);
        if (parent1 == parent2 || isNodeTheSame(parent1, parent2)) {
            if (null != parent1) {
                isNodeAfter = isNodeAfterSibling(parent1, node1, node2);
            }
        } else {
            int nParents1 = 2;
            int nParents2 = 2;
            while (parent1 != null) {
                nParents1++;
                parent1 = getParentOfNode(parent1);
            }
            while (parent2 != null) {
                nParents2++;
                parent2 = getParentOfNode(parent2);
            }
            Node startNode1 = node1;
            Node startNode2 = node2;
            if (nParents1 < nParents2) {
                int adjust = nParents2 - nParents1;
                for (int i2 = 0; i2 < adjust; i2++) {
                    startNode2 = getParentOfNode(startNode2);
                }
            } else if (nParents1 > nParents2) {
                int adjust2 = nParents1 - nParents2;
                for (int i3 = 0; i3 < adjust2; i3++) {
                    startNode1 = getParentOfNode(startNode1);
                }
            }
            Node prevChild1 = null;
            Node prevChild2 = null;
            while (null != startNode1) {
                if (startNode1 == startNode2 || isNodeTheSame(startNode1, startNode2)) {
                    isNodeAfter = null == prevChild1 ? nParents1 < nParents2 : isNodeAfterSibling(startNode1, prevChild1, prevChild2);
                } else {
                    prevChild1 = startNode1;
                    startNode1 = getParentOfNode(startNode1);
                    prevChild2 = startNode2;
                    startNode2 = getParentOfNode(startNode2);
                }
            }
        }
        return isNodeAfter;
    }

    public static boolean isNodeTheSame(Node node1, Node node2) {
        if ((node1 instanceof DTMNodeProxy) && (node2 instanceof DTMNodeProxy)) {
            return ((DTMNodeProxy) node1).equals(node2);
        }
        return node1 == node2;
    }

    public static Node getParentOfNode(Node node) {
        Node parent = node.getParentNode();
        if (parent == null && 2 == node.getNodeType()) {
            parent = ((Attr) node).getOwnerElement();
        }
        return parent;
    }

    private static boolean isNodeAfterSibling(Node parent, Node child1, Node child2) {
        boolean isNodeAfterSibling = false;
        short child1type = child1.getNodeType();
        short child2type = child2.getNodeType();
        if (2 != child1type && 2 == child2type) {
            isNodeAfterSibling = false;
        } else if (2 == child1type && 2 != child2type) {
            isNodeAfterSibling = true;
        } else if (2 == child1type) {
            NamedNodeMap children = parent.getAttributes();
            int nNodes = children.getLength();
            boolean found1 = false;
            boolean found2 = false;
            int i2 = 0;
            while (true) {
                if (i2 >= nNodes) {
                    break;
                }
                Node child = children.item(i2);
                if (child1 == child || isNodeTheSame(child1, child)) {
                    if (found2) {
                        isNodeAfterSibling = false;
                        break;
                    }
                    found1 = true;
                    i2++;
                } else {
                    if (child2 == child || isNodeTheSame(child2, child)) {
                        if (found1) {
                            isNodeAfterSibling = true;
                            break;
                        }
                        found2 = true;
                    }
                    i2++;
                }
            }
        } else {
            Node child3 = parent.getFirstChild();
            boolean found12 = false;
            boolean found22 = false;
            while (true) {
                if (null == child3) {
                    break;
                }
                if (child1 == child3 || isNodeTheSame(child1, child3)) {
                    if (found22) {
                        isNodeAfterSibling = false;
                        break;
                    }
                    found12 = true;
                    child3 = child3.getNextSibling();
                } else {
                    if (child2 == child3 || isNodeTheSame(child2, child3)) {
                        if (found12) {
                            isNodeAfterSibling = true;
                            break;
                        }
                        found22 = true;
                    }
                    child3 = child3.getNextSibling();
                }
            }
        }
        return isNodeAfterSibling;
    }
}
