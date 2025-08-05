package com.sun.javafx.scene.traversal;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/TraversalContext.class */
public interface TraversalContext {
    List<Node> getAllTargetNodes();

    Bounds getSceneLayoutBounds(Node node);

    Parent getRoot();

    Node selectFirstInParent(Parent parent);

    Node selectLastInParent(Parent parent);

    Node selectInSubtree(Parent parent, Node node, Direction direction);
}
