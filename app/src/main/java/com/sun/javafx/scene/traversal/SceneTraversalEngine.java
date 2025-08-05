package com.sun.javafx.scene.traversal;

import javafx.scene.Parent;
import javafx.scene.Scene;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/SceneTraversalEngine.class */
public final class SceneTraversalEngine extends TopMostTraversalEngine {
    private final Scene scene;

    public SceneTraversalEngine(Scene scene) {
        this.scene = scene;
    }

    @Override // com.sun.javafx.scene.traversal.TraversalEngine
    protected Parent getRoot() {
        return this.scene.getRoot();
    }
}
