package com.sun.javafx.scene.traversal;

import javafx.scene.Parent;
import javafx.scene.SubScene;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/SubSceneTraversalEngine.class */
public final class SubSceneTraversalEngine extends TopMostTraversalEngine {
    private final SubScene subScene;

    public SubSceneTraversalEngine(SubScene scene) {
        this.subScene = scene;
    }

    @Override // com.sun.javafx.scene.traversal.TraversalEngine
    protected Parent getRoot() {
        return this.subScene.getRoot();
    }
}
