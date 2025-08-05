package org.w3c.dom.traversal;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/w3c/dom/traversal/DocumentTraversal.class */
public interface DocumentTraversal {
    NodeIterator createNodeIterator(Node node, int i2, NodeFilter nodeFilter, boolean z2) throws DOMException;

    TreeWalker createTreeWalker(Node node, int i2, NodeFilter nodeFilter, boolean z2) throws DOMException;
}
