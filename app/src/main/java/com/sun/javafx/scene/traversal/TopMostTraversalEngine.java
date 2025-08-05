package com.sun.javafx.scene.traversal;

import javafx.scene.Node;
import javafx.scene.Parent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/TopMostTraversalEngine.class */
public abstract class TopMostTraversalEngine extends TraversalEngine {
    protected TopMostTraversalEngine() {
        super(DEFAULT_ALGORITHM);
    }

    TopMostTraversalEngine(Algorithm algorithm) {
        super(algorithm);
    }

    public final Node trav(Node node, Direction dir) {
        Node newNode = null;
        Node traverseNode = node;
        for (Parent p2 = node.getParent(); p2 != null; p2 = p2.getParent()) {
            ParentTraversalEngine engine = p2.getImpl_traversalEngine();
            if (engine != null && engine.canTraverse()) {
                newNode = engine.select(node, dir);
                if (newNode != null) {
                    break;
                }
                traverseNode = p2;
                if (dir == Direction.NEXT) {
                    dir = Direction.NEXT_IN_LINE;
                }
            }
        }
        if (newNode == null) {
            newNode = select(traverseNode, dir);
        }
        if (newNode == null) {
            if (dir == Direction.NEXT || dir == Direction.NEXT_IN_LINE) {
                newNode = selectFirst();
            } else if (dir == Direction.PREVIOUS) {
                newNode = selectLast();
            }
        }
        if (newNode != null) {
            focusAndNotify(newNode);
        }
        return newNode;
    }

    private void focusAndNotify(Node newNode) {
        newNode.requestFocus();
        notifyTreeTraversedTo(newNode);
    }

    private void notifyTreeTraversedTo(Node newNode) {
        Parent parent = newNode.getParent();
        while (true) {
            Parent p2 = parent;
            if (p2 != null) {
                ParentTraversalEngine impl_traversalEngine = p2.getImpl_traversalEngine();
                if (impl_traversalEngine != null) {
                    impl_traversalEngine.notifyTraversedTo(newNode);
                }
                parent = p2.getParent();
            } else {
                notifyTraversedTo(newNode);
                return;
            }
        }
    }

    public final Node traverseToFirst() {
        Node n2 = selectFirst();
        if (n2 != null) {
            focusAndNotify(n2);
        }
        return n2;
    }

    public final Node traverseToLast() {
        Node n2 = selectLast();
        if (n2 != null) {
            focusAndNotify(n2);
        }
        return n2;
    }
}
