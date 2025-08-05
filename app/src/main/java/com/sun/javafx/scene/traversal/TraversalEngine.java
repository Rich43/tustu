package com.sun.javafx.scene.traversal;

import com.sun.javafx.application.PlatformImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/TraversalEngine.class */
public abstract class TraversalEngine {
    static final Algorithm DEFAULT_ALGORITHM;
    private final TraversalContext context;
    private final TempEngineContext tempEngineContext;
    protected final Algorithm algorithm;
    private final Bounds initialBounds;
    private final ArrayList<TraverseListener> listeners;

    protected abstract Parent getRoot();

    static {
        DEFAULT_ALGORITHM = PlatformImpl.isContextual2DNavigation() ? new Hueristic2D() : new ContainerTabOrder();
    }

    protected TraversalEngine(Algorithm algorithm) {
        this.context = new EngineContext();
        this.tempEngineContext = new TempEngineContext();
        this.initialBounds = new BoundingBox(0.0d, 0.0d, 1.0d, 1.0d);
        this.listeners = new ArrayList<>();
        this.algorithm = algorithm;
    }

    protected TraversalEngine() {
        this.context = new EngineContext();
        this.tempEngineContext = new TempEngineContext();
        this.initialBounds = new BoundingBox(0.0d, 0.0d, 1.0d, 1.0d);
        this.listeners = new ArrayList<>();
        this.algorithm = null;
    }

    public final void addTraverseListener(TraverseListener listener) {
        this.listeners.add(listener);
    }

    final void notifyTraversedTo(Node newNode) {
        Iterator<TraverseListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            TraverseListener l2 = it.next();
            l2.onTraverse(newNode, getLayoutBounds(newNode, getRoot()));
        }
    }

    public final Node select(Node from, Direction dir) {
        return this.algorithm.select(from, dir, this.context);
    }

    public final Node selectFirst() {
        return this.algorithm.selectFirst(this.context);
    }

    public final Node selectLast() {
        return this.algorithm.selectLast(this.context);
    }

    public final boolean canTraverse() {
        return this.algorithm != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bounds getLayoutBounds(Node n2, Parent forParent) {
        Bounds bounds;
        if (n2 != null) {
            if (forParent == null) {
                bounds = n2.localToScene(n2.getLayoutBounds());
            } else {
                bounds = forParent.sceneToLocal(n2.localToScene(n2.getLayoutBounds()));
            }
        } else {
            bounds = this.initialBounds;
        }
        return bounds;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/TraversalEngine$EngineContext.class */
    private final class EngineContext extends BaseEngineContext {
        private EngineContext() {
            super();
        }

        @Override // com.sun.javafx.scene.traversal.TraversalContext
        public Parent getRoot() {
            return TraversalEngine.this.getRoot();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/TraversalEngine$TempEngineContext.class */
    private final class TempEngineContext extends BaseEngineContext {
        private Parent root;

        private TempEngineContext() {
            super();
        }

        @Override // com.sun.javafx.scene.traversal.TraversalContext
        public Parent getRoot() {
            return this.root;
        }

        public void setRoot(Parent root) {
            this.root = root;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/TraversalEngine$BaseEngineContext.class */
    private abstract class BaseEngineContext implements TraversalContext {
        private BaseEngineContext() {
        }

        @Override // com.sun.javafx.scene.traversal.TraversalContext
        public List<Node> getAllTargetNodes() {
            List<Node> targetNodes = new ArrayList<>();
            addFocusableChildrenToList(targetNodes, getRoot());
            return targetNodes;
        }

        @Override // com.sun.javafx.scene.traversal.TraversalContext
        public Bounds getSceneLayoutBounds(Node n2) {
            return TraversalEngine.this.getLayoutBounds(n2, null);
        }

        private void addFocusableChildrenToList(List<Node> list, Parent parent) {
            List<Node> parentsNodes = parent.getChildrenUnmodifiable();
            for (Node n2 : parentsNodes) {
                if (n2.isFocusTraversable() && !n2.isFocused() && n2.impl_isTreeVisible() && !n2.isDisabled()) {
                    list.add(n2);
                }
                if (n2 instanceof Parent) {
                    addFocusableChildrenToList(list, (Parent) n2);
                }
            }
        }

        @Override // com.sun.javafx.scene.traversal.TraversalContext
        public Node selectFirstInParent(Parent parent) {
            TraversalEngine.this.tempEngineContext.setRoot(parent);
            return TraversalEngine.DEFAULT_ALGORITHM.selectFirst(TraversalEngine.this.tempEngineContext);
        }

        @Override // com.sun.javafx.scene.traversal.TraversalContext
        public Node selectLastInParent(Parent parent) {
            TraversalEngine.this.tempEngineContext.setRoot(parent);
            return TraversalEngine.DEFAULT_ALGORITHM.selectLast(TraversalEngine.this.tempEngineContext);
        }

        @Override // com.sun.javafx.scene.traversal.TraversalContext
        public Node selectInSubtree(Parent subTreeRoot, Node from, Direction dir) {
            TraversalEngine.this.tempEngineContext.setRoot(subTreeRoot);
            return TraversalEngine.DEFAULT_ALGORITHM.select(from, dir, TraversalEngine.this.tempEngineContext);
        }
    }
}
