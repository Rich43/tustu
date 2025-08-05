package com.sun.javafx.jmx;

import javafx.scene.Node;
import javafx.scene.Parent;

/* loaded from: jfxrt.jar:com/sun/javafx/jmx/MXNodeAlgorithm.class */
public interface MXNodeAlgorithm {
    Object processLeafNode(Node node, MXNodeAlgorithmContext mXNodeAlgorithmContext);

    Object processContainerNode(Parent parent, MXNodeAlgorithmContext mXNodeAlgorithmContext);
}
