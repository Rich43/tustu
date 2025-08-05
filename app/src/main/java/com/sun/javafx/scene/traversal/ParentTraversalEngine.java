package com.sun.javafx.scene.traversal;

import javafx.scene.Parent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/ParentTraversalEngine.class */
public final class ParentTraversalEngine extends TraversalEngine {
    private final Parent root;
    private Boolean overridenTraversability;

    public ParentTraversalEngine(Parent root, Algorithm algorithm) {
        super(algorithm);
        this.root = root;
    }

    public ParentTraversalEngine(Parent root) {
        this.root = root;
    }

    public void setOverriddenFocusTraversability(Boolean value) {
        this.overridenTraversability = value;
    }

    @Override // com.sun.javafx.scene.traversal.TraversalEngine
    protected Parent getRoot() {
        return this.root;
    }

    public boolean isParentTraversable() {
        return this.overridenTraversability != null ? this.root.isFocusTraversable() && this.overridenTraversability.booleanValue() : this.root.isFocusTraversable();
    }
}
