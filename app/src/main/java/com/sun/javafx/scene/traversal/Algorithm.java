package com.sun.javafx.scene.traversal;

import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/Algorithm.class */
public interface Algorithm {
    Node select(Node node, Direction direction, TraversalContext traversalContext);

    Node selectFirst(TraversalContext traversalContext);

    Node selectLast(TraversalContext traversalContext);
}
