package javafx.scene;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.CssFlags;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.LayoutFlags;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.TempState;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:javafx/scene/Parent.class */
public abstract class Parent extends Node {
    static final int DIRTY_CHILDREN_THRESHOLD = 10;
    private static final boolean warnOnAutoMove = PropertyHelper.getBooleanProperty("javafx.sg.warn");
    private static final int REMOVED_CHILDREN_THRESHOLD = 20;
    private List<Node> removed;
    private boolean geomChanged;
    private boolean childSetModified;
    private ObjectProperty<ParentTraversalEngine> impl_traversalEngine;
    private ReadOnlyBooleanWrapper needsLayout;
    LayoutFlags layoutFlag;
    private boolean cachedBoundsInvalid;
    private int dirtyChildrenCount;
    private ArrayList<Node> dirtyChildren;
    private Node top;
    private Node left;
    private Node bottom;
    private Node right;
    private Node near;
    private Node far;
    private Node currentlyProcessedChild;
    private boolean removedChildrenOptimizationDisabled = false;
    private final Set<Node> childSet = new HashSet();
    private int startIdx = 0;
    private int pgChildrenSize = 0;
    private boolean childrenTriggerPermutation = false;
    private final ObservableList<Node> children = new VetoableListDecorator<Node>(new TrackableObservableList<Node>() { // from class: javafx.scene.Parent.1
        /* JADX WARN: Code restructure failed: missing block: B:67:0x022f, code lost:
        
            r9 = r6.getFrom();
            r0 = r6.getTo();
         */
        /* JADX WARN: Code restructure failed: missing block: B:69:0x023f, code lost:
        
            if (r9 >= r0) goto L105;
         */
        /* JADX WARN: Code restructure failed: missing block: B:71:0x0256, code lost:
        
            if (((javafx.scene.Node) r5.this$0.children.get(r9)).isManaged() == false) goto L73;
         */
        /* JADX WARN: Code restructure failed: missing block: B:72:0x0259, code lost:
        
            r7 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:73:0x025e, code lost:
        
            r9 = r9 + 1;
         */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.javafx.collections.TrackableObservableList
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected void onChanged(javafx.collections.ListChangeListener.Change<javafx.scene.Node> r6) {
            /*
                Method dump skipped, instructions count: 699
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: javafx.scene.Parent.AnonymousClass1.onChanged(javafx.collections.ListChangeListener$Change):void");
        }
    }) { // from class: javafx.scene.Parent.2
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.javafx.collections.VetoableListDecorator
        protected void onProposedChange(List<Node> newNodes, int[] toBeRemoved) {
            Window w2;
            Scene scene = Parent.this.getScene();
            if (scene != null && (w2 = scene.getWindow()) != null && w2.impl_getPeer() != null) {
                Toolkit.getToolkit().checkFxUserThread();
            }
            Parent.this.geomChanged = false;
            long newLength = Parent.this.children.size() + newNodes.size();
            int removedLength = 0;
            for (int i2 = 0; i2 < toBeRemoved.length; i2 += 2) {
                removedLength += toBeRemoved[i2 + 1] - toBeRemoved[i2];
            }
            long newLength2 = newLength - removedLength;
            if (Parent.this.childrenTriggerPermutation) {
                Parent.this.childSetModified = false;
                return;
            }
            Parent.this.childSetModified = true;
            if (newLength2 == Parent.this.childSet.size()) {
                Parent.this.childSetModified = false;
                int i3 = newNodes.size() - 1;
                while (true) {
                    if (i3 < 0) {
                        break;
                    }
                    Node n2 = newNodes.get(i3);
                    if (!Parent.this.childSet.contains(n2)) {
                        Parent.this.childSetModified = true;
                        break;
                    }
                    i3--;
                }
            }
            for (int i4 = 0; i4 < toBeRemoved.length; i4 += 2) {
                for (int j2 = toBeRemoved[i4]; j2 < toBeRemoved[i4 + 1]; j2++) {
                    Parent.this.childSet.remove(Parent.this.children.get(j2));
                }
            }
            try {
                if (Parent.this.childSetModified) {
                    for (int i5 = newNodes.size() - 1; i5 >= 0; i5--) {
                        Node node = newNodes.get(i5);
                        if (node == null) {
                            throw new NullPointerException(constructExceptionMessage("child node is null", null));
                        }
                        if (node.getClipParent() != null) {
                            throw new IllegalArgumentException(constructExceptionMessage("node already used as a clip", node));
                        }
                        if (Parent.this.wouldCreateCycle(Parent.this, node)) {
                            throw new IllegalArgumentException(constructExceptionMessage("cycle detected", node));
                        }
                    }
                }
                Parent.this.childSet.addAll(newNodes);
                if (Parent.this.childSet.size() == newLength2) {
                    if (Parent.this.childSetModified) {
                        if (Parent.this.removed == null) {
                            Parent.this.removed = new ArrayList();
                        }
                        if (Parent.this.removed.size() + removedLength > 20 || !Parent.this.impl_isTreeVisible()) {
                            Parent.this.removedChildrenOptimizationDisabled = true;
                        }
                        for (int i6 = 0; i6 < toBeRemoved.length; i6 += 2) {
                            for (int j3 = toBeRemoved[i6]; j3 < toBeRemoved[i6 + 1]; j3++) {
                                Node old = (Node) Parent.this.children.get(j3);
                                Scene oldScene = old.getScene();
                                if (oldScene != null) {
                                    oldScene.generateMouseExited(old);
                                }
                                if (Parent.this.dirtyChildren != null) {
                                    Parent.this.dirtyChildren.remove(old);
                                }
                                if (old.isVisible()) {
                                    Parent.this.geomChanged = true;
                                    Parent.this.childExcluded(old);
                                }
                                if (old.getParent() == Parent.this) {
                                    old.setParent(null);
                                    old.setScenes(null, null);
                                }
                                if (scene != null && !Parent.this.removedChildrenOptimizationDisabled) {
                                    Parent.this.removed.add(old);
                                }
                            }
                        }
                        return;
                    }
                    return;
                }
                throw new IllegalArgumentException(constructExceptionMessage("duplicate children added", null));
            } catch (RuntimeException e2) {
                Parent.this.childSet.clear();
                Parent.this.childSet.addAll(Parent.this.children);
                throw e2;
            }
        }

        private String constructExceptionMessage(String cause, Node offendingNode) {
            StringBuilder sb = new StringBuilder("Children: ");
            sb.append(cause);
            sb.append(": parent = ").append((Object) Parent.this);
            if (offendingNode != null) {
                sb.append(", node = ").append((Object) offendingNode);
            }
            return sb.toString();
        }
    };
    private final ObservableList<Node> unmodifiableChildren = FXCollections.unmodifiableObservableList(this.children);
    private List<Node> unmodifiableManagedChildren = null;
    boolean performingLayout = false;
    private boolean sizeCacheClear = true;
    private double prefWidthCache = -1.0d;
    private double prefHeightCache = -1.0d;
    private double minWidthCache = -1.0d;
    private double minHeightCache = -1.0d;
    private boolean sceneRoot = false;
    boolean layoutRoot = false;
    private final ObservableList<String> stylesheets = new TrackableObservableList<String>() { // from class: javafx.scene.Parent.3
        @Override // com.sun.javafx.collections.TrackableObservableList
        protected void onChanged(ListChangeListener.Change<String> c2) {
            Scene scene = Parent.this.getScene();
            if (scene != null) {
                StyleManager.getInstance().stylesheetsChanged(Parent.this, c2);
                c2.reset();
                while (c2.next() && !c2.wasRemoved()) {
                }
                Parent.this.impl_reapplyCSS();
            }
        }
    };
    private BaseBounds tmp = new RectBounds();
    private BaseBounds cachedBounds = new RectBounds();
    private final int LEFT_INVALID = 1;
    private final int TOP_INVALID = 2;
    private final int NEAR_INVALID = 4;
    private final int RIGHT_INVALID = 8;
    private final int BOTTOM_INVALID = 16;
    private final int FAR_INVALID = 32;

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        NGGroup peer = (NGGroup) impl_getPeer();
        if (Utils.assertionEnabled()) {
            List<NGNode> pgnodes = peer.getChildren();
            if (pgnodes.size() != this.pgChildrenSize) {
                System.err.println("*** pgnodes.size() [" + pgnodes.size() + "] != pgChildrenSize [" + this.pgChildrenSize + "]");
            }
        }
        if (impl_isDirty(DirtyBits.PARENT_CHILDREN)) {
            peer.clearFrom(this.startIdx);
            for (int idx = this.startIdx; idx < this.children.size(); idx++) {
                peer.add(idx, this.children.get(idx).impl_getPeer());
            }
            if (this.removedChildrenOptimizationDisabled) {
                peer.markDirty();
                this.removedChildrenOptimizationDisabled = false;
            } else if (this.removed != null && !this.removed.isEmpty()) {
                for (int i2 = 0; i2 < this.removed.size(); i2++) {
                    peer.addToRemoved(this.removed.get(i2).impl_getPeer());
                }
            }
            if (this.removed != null) {
                this.removed.clear();
            }
            this.pgChildrenSize = this.children.size();
            this.startIdx = this.pgChildrenSize;
        }
        if (Utils.assertionEnabled()) {
            validatePG();
        }
    }

    void validatePG() {
        boolean assertionFailed = false;
        NGGroup peer = (NGGroup) impl_getPeer();
        List<NGNode> pgnodes = peer.getChildren();
        if (pgnodes.size() != this.children.size()) {
            System.err.println("*** pgnodes.size validatePG() [" + pgnodes.size() + "] != children.size() [" + this.children.size() + "]");
            assertionFailed = true;
        } else {
            for (int idx = 0; idx < this.children.size(); idx++) {
                Node n2 = this.children.get(idx);
                if (n2.getParent() != this) {
                    System.err.println("*** this=" + ((Object) this) + " validatePG children[" + idx + "].parent= " + ((Object) n2.getParent()));
                    assertionFailed = true;
                }
                if (n2.impl_getPeer() != pgnodes.get(idx)) {
                    System.err.println("*** pgnodes[" + idx + "] validatePG != children[" + idx + "]");
                    assertionFailed = true;
                }
            }
        }
        if (assertionFailed) {
            throw new AssertionError((Object) "validation of PGGroup children failed");
        }
    }

    void printSeq(String prefix, List<Node> nodes) {
        String str = prefix;
        for (Node nn : nodes) {
            str = str + ((Object) nn) + " ";
        }
        System.out.println(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ObservableList<Node> getChildren() {
        return this.children;
    }

    public ObservableList<Node> getChildrenUnmodifiable() {
        return this.unmodifiableChildren;
    }

    protected <E extends Node> List<E> getManagedChildren() {
        if (this.unmodifiableManagedChildren == null) {
            this.unmodifiableManagedChildren = new ArrayList();
            int size = this.children.size();
            for (int i2 = 0; i2 < size; i2++) {
                Node node = this.children.get(i2);
                if (node.isManaged()) {
                    this.unmodifiableManagedChildren.add(node);
                }
            }
        }
        return (List<E>) this.unmodifiableManagedChildren;
    }

    final void managedChildChanged() {
        requestLayout();
        this.unmodifiableManagedChildren = null;
    }

    final void impl_toFront(Node node) {
        if (Utils.assertionEnabled() && !this.childSet.contains(node)) {
            throw new AssertionError((Object) "specified node is not in the list of children");
        }
        if (this.children.get(this.children.size() - 1) != node) {
            this.childrenTriggerPermutation = true;
            try {
                this.children.remove(node);
                this.children.add(node);
            } finally {
                this.childrenTriggerPermutation = false;
            }
        }
    }

    final void impl_toBack(Node node) {
        if (Utils.assertionEnabled() && !this.childSet.contains(node)) {
            throw new AssertionError((Object) "specified node is not in the list of children");
        }
        if (this.children.get(0) != node) {
            this.childrenTriggerPermutation = true;
            try {
                this.children.remove(node);
                this.children.add(0, node);
            } finally {
                this.childrenTriggerPermutation = false;
            }
        }
    }

    @Override // javafx.scene.Node
    void scenesChanged(Scene newScene, SubScene newSubScene, Scene oldScene, SubScene oldSubScene) {
        if (oldScene != null && newScene == null) {
            StyleManager.getInstance().forget(this);
            if (this.removed != null) {
                this.removed.clear();
            }
        }
        for (int i2 = 0; i2 < this.children.size(); i2++) {
            this.children.get(i2).setScenes(newScene, newSubScene);
        }
        boolean awaitingLayout = this.layoutFlag != LayoutFlags.CLEAN;
        this.sceneRoot = (newSubScene != null && newSubScene.getRoot() == this) || (newScene != null && newScene.getRoot() == this);
        this.layoutRoot = !isManaged() || this.sceneRoot;
        if (awaitingLayout && newScene != null && this.layoutRoot && newSubScene != null) {
            newSubScene.setDirtyLayout(this);
        }
    }

    @Override // javafx.scene.Node
    void setDerivedDepthTest(boolean value) {
        super.setDerivedDepthTest(value);
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node node = this.children.get(i2);
            node.computeDerivedDepthTest();
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected void impl_pickNodeLocal(PickRay pickRay, PickResultChooser result) {
        double boundsDistance = impl_intersectsBounds(pickRay);
        if (!Double.isNaN(boundsDistance)) {
            for (int i2 = this.children.size() - 1; i2 >= 0; i2--) {
                this.children.get(i2).impl_pickNode(pickRay, result);
                if (result.isClosed()) {
                    return;
                }
            }
            if (isPickOnBounds()) {
                result.offer(this, boundsDistance, PickResultChooser.computePoint(pickRay, boundsDistance));
            }
        }
    }

    @Override // javafx.scene.Node
    boolean isConnected() {
        return super.isConnected() || this.sceneRoot;
    }

    @Override // javafx.scene.Node
    public Node lookup(String selector) {
        Node n2 = super.lookup(selector);
        if (n2 == null) {
            int max = this.children.size();
            for (int i2 = 0; i2 < max; i2++) {
                Node node = this.children.get(i2);
                n2 = node.lookup(selector);
                if (n2 != null) {
                    return n2;
                }
            }
        }
        return n2;
    }

    @Override // javafx.scene.Node
    List<Node> lookupAll(Selector selector, List<Node> results) {
        List<Node> results2 = super.lookupAll(selector, results);
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node node = this.children.get(i2);
            results2 = node.lookupAll(selector, results2);
        }
        return results2;
    }

    @Deprecated
    public final void setImpl_traversalEngine(ParentTraversalEngine value) {
        impl_traversalEngineProperty().set(value);
    }

    @Deprecated
    public final ParentTraversalEngine getImpl_traversalEngine() {
        if (this.impl_traversalEngine == null) {
            return null;
        }
        return this.impl_traversalEngine.get();
    }

    @Deprecated
    public final ObjectProperty<ParentTraversalEngine> impl_traversalEngineProperty() {
        if (this.impl_traversalEngine == null) {
            this.impl_traversalEngine = new SimpleObjectProperty(this, "impl_traversalEngine");
        }
        return this.impl_traversalEngine;
    }

    protected final void setNeedsLayout(boolean value) {
        if (value) {
            markDirtyLayout(true);
            return;
        }
        if (this.layoutFlag == LayoutFlags.NEEDS_LAYOUT) {
            boolean hasBranch = false;
            int i2 = 0;
            int max = this.children.size();
            while (true) {
                if (i2 >= max) {
                    break;
                }
                Node child = this.children.get(i2);
                if (!(child instanceof Parent) || ((Parent) child).layoutFlag == LayoutFlags.CLEAN) {
                    i2++;
                } else {
                    hasBranch = true;
                    break;
                }
            }
            setLayoutFlag(hasBranch ? LayoutFlags.DIRTY_BRANCH : LayoutFlags.CLEAN);
        }
    }

    public final boolean isNeedsLayout() {
        return this.layoutFlag == LayoutFlags.NEEDS_LAYOUT;
    }

    public final ReadOnlyBooleanProperty needsLayoutProperty() {
        if (this.needsLayout == null) {
            this.needsLayout = new ReadOnlyBooleanWrapper(this, "needsLayout", this.layoutFlag == LayoutFlags.NEEDS_LAYOUT);
        }
        return this.needsLayout;
    }

    void setLayoutFlag(LayoutFlags flag) {
        if (this.needsLayout != null) {
            this.needsLayout.set(flag == LayoutFlags.NEEDS_LAYOUT);
        }
        this.layoutFlag = flag;
    }

    private void markDirtyLayout(boolean local) {
        setLayoutFlag(LayoutFlags.NEEDS_LAYOUT);
        if (local || this.layoutRoot) {
            if (this.sceneRoot) {
                Toolkit.getToolkit().requestNextPulse();
                if (getSubScene() != null) {
                    getSubScene().setDirtyLayout(this);
                    return;
                }
                return;
            }
            markDirtyLayoutBranch();
            return;
        }
        requestParentLayout();
    }

    public void requestLayout() {
        clearSizeCache();
        markDirtyLayout(false);
    }

    protected final void requestParentLayout() {
        Parent parent;
        if (!this.layoutRoot && (parent = getParent()) != null && !parent.performingLayout) {
            parent.requestLayout();
        }
    }

    void clearSizeCache() {
        if (this.sizeCacheClear) {
            return;
        }
        this.sizeCacheClear = true;
        this.prefWidthCache = -1.0d;
        this.prefHeightCache = -1.0d;
        this.minWidthCache = -1.0d;
        this.minHeightCache = -1.0d;
    }

    @Override // javafx.scene.Node
    public double prefWidth(double height) {
        if (height == -1.0d) {
            if (this.prefWidthCache == -1.0d) {
                this.prefWidthCache = computePrefWidth(-1.0d);
                if (Double.isNaN(this.prefWidthCache) || this.prefWidthCache < 0.0d) {
                    this.prefWidthCache = 0.0d;
                }
                this.sizeCacheClear = false;
            }
            return this.prefWidthCache;
        }
        double result = computePrefWidth(height);
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Node
    public double prefHeight(double width) {
        if (width == -1.0d) {
            if (this.prefHeightCache == -1.0d) {
                this.prefHeightCache = computePrefHeight(-1.0d);
                if (Double.isNaN(this.prefHeightCache) || this.prefHeightCache < 0.0d) {
                    this.prefHeightCache = 0.0d;
                }
                this.sizeCacheClear = false;
            }
            return this.prefHeightCache;
        }
        double result = computePrefHeight(width);
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Node
    public double minWidth(double height) {
        if (height == -1.0d) {
            if (this.minWidthCache == -1.0d) {
                this.minWidthCache = computeMinWidth(-1.0d);
                if (Double.isNaN(this.minWidthCache) || this.minWidthCache < 0.0d) {
                    this.minWidthCache = 0.0d;
                }
                this.sizeCacheClear = false;
            }
            return this.minWidthCache;
        }
        double result = computeMinWidth(height);
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Node
    public double minHeight(double width) {
        if (width == -1.0d) {
            if (this.minHeightCache == -1.0d) {
                this.minHeightCache = computeMinHeight(-1.0d);
                if (Double.isNaN(this.minHeightCache) || this.minHeightCache < 0.0d) {
                    this.minHeightCache = 0.0d;
                }
                this.sizeCacheClear = false;
            }
            return this.minHeightCache;
        }
        double result = computeMinHeight(width);
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    protected double computePrefWidth(double height) {
        double minX = 0.0d;
        double maxX = 0.0d;
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node node = this.children.get(i2);
            if (node.isManaged()) {
                double x2 = node.getLayoutBounds().getMinX() + node.getLayoutX();
                minX = Math.min(minX, x2);
                maxX = Math.max(maxX, x2 + boundedSize(node.prefWidth(-1.0d), node.minWidth(-1.0d), node.maxWidth(-1.0d)));
            }
        }
        return maxX - minX;
    }

    protected double computePrefHeight(double width) {
        double minY = 0.0d;
        double maxY = 0.0d;
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node node = this.children.get(i2);
            if (node.isManaged()) {
                double y2 = node.getLayoutBounds().getMinY() + node.getLayoutY();
                minY = Math.min(minY, y2);
                maxY = Math.max(maxY, y2 + boundedSize(node.prefHeight(-1.0d), node.minHeight(-1.0d), node.maxHeight(-1.0d)));
            }
        }
        return maxY - minY;
    }

    protected double computeMinWidth(double height) {
        return prefWidth(height);
    }

    protected double computeMinHeight(double width) {
        return prefHeight(width);
    }

    @Override // javafx.scene.Node
    public double getBaselineOffset() {
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node child = this.children.get(i2);
            if (child.isManaged()) {
                double offset = child.getBaselineOffset();
                if (offset != Double.NEGATIVE_INFINITY) {
                    return child.getLayoutBounds().getMinY() + child.getLayoutY() + offset;
                }
            }
        }
        return super.getBaselineOffset();
    }

    public final void layout() {
        switch (this.layoutFlag) {
            case CLEAN:
            default:
                return;
            case NEEDS_LAYOUT:
                if (!this.performingLayout) {
                    this.performingLayout = true;
                    layoutChildren();
                    break;
                } else {
                    return;
                }
            case DIRTY_BRANCH:
                break;
        }
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node child = this.children.get(i2);
            if (child instanceof Parent) {
                ((Parent) child).layout();
            } else if (child instanceof SubScene) {
                ((SubScene) child).layoutPass();
            }
        }
        setLayoutFlag(LayoutFlags.CLEAN);
        this.performingLayout = false;
    }

    protected void layoutChildren() {
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node node = this.children.get(i2);
            if (node.isResizable() && node.isManaged()) {
                node.autosize();
            }
        }
    }

    @Override // javafx.scene.Node
    final void notifyManagedChanged() {
        this.layoutRoot = !isManaged() || this.sceneRoot;
    }

    final boolean isSceneRoot() {
        return this.sceneRoot;
    }

    public final ObservableList<String> getStylesheets() {
        return this.stylesheets;
    }

    @Deprecated
    public List<String> impl_getAllParentStylesheets() {
        List<String> list = null;
        Parent myParent = getParent();
        if (myParent != null) {
            list = myParent.impl_getAllParentStylesheets();
        }
        if (this.stylesheets != null && !this.stylesheets.isEmpty()) {
            if (list == null) {
                list = new ArrayList(this.stylesheets.size());
            }
            int nMax = this.stylesheets.size();
            for (int n2 = 0; n2 < nMax; n2++) {
                list.add(this.stylesheets.get(n2));
            }
        }
        return list;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected void impl_processCSS(WritableValue<Boolean> unused) {
        if (this.cssFlag == CssFlags.CLEAN) {
            return;
        }
        if (this.cssFlag == CssFlags.DIRTY_BRANCH) {
            super.processCSS();
            return;
        }
        super.impl_processCSS(unused);
        if (this.children.isEmpty()) {
            return;
        }
        Node[] childArray = (Node[]) this.children.toArray(new Node[this.children.size()]);
        for (Node child : childArray) {
            Parent childParent = child.getParent();
            if (childParent != null && childParent == this) {
                if (CssFlags.UPDATE.compareTo(child.cssFlag) > 0) {
                    child.cssFlag = CssFlags.UPDATE;
                }
                child.impl_processCSS(unused);
            }
        }
    }

    protected Parent() {
        this.layoutFlag = LayoutFlags.CLEAN;
        this.layoutFlag = LayoutFlags.NEEDS_LAYOUT;
        setAccessibleRole(AccessibleRole.PARENT);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGGroup();
    }

    @Override // javafx.scene.Node
    void nodeResolvedOrientationChanged() {
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            this.children.get(i2).parentResolvedOrientationInvalidated();
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        BaseBounds bounds2;
        if (this.children.isEmpty()) {
            return bounds.makeEmpty();
        }
        if (tx.isTranslateOrIdentity()) {
            if (this.cachedBoundsInvalid) {
                recomputeBounds();
                if (this.dirtyChildren != null) {
                    this.dirtyChildren.clear();
                }
                this.cachedBoundsInvalid = false;
                this.dirtyChildrenCount = 0;
            }
            if (!tx.isIdentity()) {
                bounds2 = bounds.deriveWithNewBounds((float) (this.cachedBounds.getMinX() + tx.getMxt()), (float) (this.cachedBounds.getMinY() + tx.getMyt()), (float) (this.cachedBounds.getMinZ() + tx.getMzt()), (float) (this.cachedBounds.getMaxX() + tx.getMxt()), (float) (this.cachedBounds.getMaxY() + tx.getMyt()), (float) (this.cachedBounds.getMaxZ() + tx.getMzt()));
            } else {
                bounds2 = bounds.deriveWithNewBounds(this.cachedBounds);
            }
            return bounds2;
        }
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        boolean first = true;
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node node = this.children.get(i2);
            if (node.isVisible()) {
                bounds = getChildTransformedBounds(node, tx, bounds);
                if (!bounds.isEmpty()) {
                    if (first) {
                        minX = bounds.getMinX();
                        minY = bounds.getMinY();
                        minZ = bounds.getMinZ();
                        maxX = bounds.getMaxX();
                        maxY = bounds.getMaxY();
                        maxZ = bounds.getMaxZ();
                        first = false;
                    } else {
                        minX = Math.min(bounds.getMinX(), minX);
                        minY = Math.min(bounds.getMinY(), minY);
                        minZ = Math.min(bounds.getMinZ(), minZ);
                        maxX = Math.max(bounds.getMaxX(), maxX);
                        maxY = Math.max(bounds.getMaxY(), maxY);
                        maxZ = Math.max(bounds.getMaxZ(), maxZ);
                    }
                }
            }
        }
        if (first) {
            bounds.makeEmpty();
        } else {
            bounds = bounds.deriveWithNewBounds((float) minX, (float) minY, (float) minZ, (float) maxX, (float) maxY, (float) maxZ);
        }
        return bounds;
    }

    private void setChildDirty(Node node, boolean dirty) {
        if (node.boundsChanged == dirty) {
            return;
        }
        node.boundsChanged = dirty;
        if (dirty) {
            if (this.dirtyChildren != null) {
                this.dirtyChildren.add(node);
            }
            this.dirtyChildrenCount++;
        } else {
            if (this.dirtyChildren != null) {
                this.dirtyChildren.remove(node);
            }
            this.dirtyChildrenCount--;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void childIncluded(Node node) {
        this.cachedBoundsInvalid = true;
        setChildDirty(node, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void childExcluded(Node node) {
        if (node == this.left) {
            this.left = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.top) {
            this.top = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.near) {
            this.near = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.right) {
            this.right = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.bottom) {
            this.bottom = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.far) {
            this.far = null;
            this.cachedBoundsInvalid = true;
        }
        setChildDirty(node, false);
    }

    private void recomputeBounds() {
        if (this.children.isEmpty()) {
            this.cachedBounds.makeEmpty();
            return;
        }
        if (this.children.size() == 1) {
            Node node = this.children.get(0);
            node.boundsChanged = false;
            if (node.isVisible()) {
                this.cachedBounds = getChildTransformedBounds(node, BaseTransform.IDENTITY_TRANSFORM, this.cachedBounds);
                this.far = node;
                this.near = node;
                this.right = node;
                this.bottom = node;
                this.left = node;
                this.top = node;
                return;
            }
            this.cachedBounds.makeEmpty();
            return;
        }
        if (this.dirtyChildrenCount != 0) {
            if (updateCachedBounds(this.dirtyChildren != null ? this.dirtyChildren : this.children, this.dirtyChildrenCount)) {
                return;
            }
        }
        createCachedBounds(this.children);
    }

    private boolean updateCachedBounds(List<Node> dirtyNodes, int remainingDirtyNodes) {
        if (this.cachedBounds.isEmpty()) {
            createCachedBounds(dirtyNodes);
            return true;
        }
        int invalidEdges = 0;
        if (this.left == null || this.left.boundsChanged) {
            invalidEdges = 0 | 1;
        }
        if (this.top == null || this.top.boundsChanged) {
            invalidEdges |= 2;
        }
        if (this.near == null || this.near.boundsChanged) {
            invalidEdges |= 4;
        }
        if (this.right == null || this.right.boundsChanged) {
            invalidEdges |= 8;
        }
        if (this.bottom == null || this.bottom.boundsChanged) {
            invalidEdges |= 16;
        }
        if (this.far == null || this.far.boundsChanged) {
            invalidEdges |= 32;
        }
        float minX = this.cachedBounds.getMinX();
        float minY = this.cachedBounds.getMinY();
        float minZ = this.cachedBounds.getMinZ();
        float maxX = this.cachedBounds.getMaxX();
        float maxY = this.cachedBounds.getMaxY();
        float maxZ = this.cachedBounds.getMaxZ();
        int i2 = dirtyNodes.size() - 1;
        while (remainingDirtyNodes > 0) {
            Node node = dirtyNodes.get(i2);
            if (node.boundsChanged) {
                node.boundsChanged = false;
                remainingDirtyNodes--;
                this.tmp = getChildTransformedBounds(node, BaseTransform.IDENTITY_TRANSFORM, this.tmp);
                if (!this.tmp.isEmpty()) {
                    float tmpx = this.tmp.getMinX();
                    float tmpy = this.tmp.getMinY();
                    float tmpz = this.tmp.getMinZ();
                    float tmpx2 = this.tmp.getMaxX();
                    float tmpy2 = this.tmp.getMaxY();
                    float tmpz2 = this.tmp.getMaxZ();
                    if (tmpx <= minX) {
                        minX = tmpx;
                        this.left = node;
                        invalidEdges &= -2;
                    }
                    if (tmpy <= minY) {
                        minY = tmpy;
                        this.top = node;
                        invalidEdges &= -3;
                    }
                    if (tmpz <= minZ) {
                        minZ = tmpz;
                        this.near = node;
                        invalidEdges &= -5;
                    }
                    if (tmpx2 >= maxX) {
                        maxX = tmpx2;
                        this.right = node;
                        invalidEdges &= -9;
                    }
                    if (tmpy2 >= maxY) {
                        maxY = tmpy2;
                        this.bottom = node;
                        invalidEdges &= -17;
                    }
                    if (tmpz2 >= maxZ) {
                        maxZ = tmpz2;
                        this.far = node;
                        invalidEdges &= -33;
                    }
                }
            }
            i2--;
        }
        if (invalidEdges != 0) {
            return false;
        }
        this.cachedBounds = this.cachedBounds.deriveWithNewBounds(minX, minY, minZ, maxX, maxY, maxZ);
        return true;
    }

    private void createCachedBounds(List<Node> fromNodes) {
        int nodeCount = fromNodes.size();
        int i2 = 0;
        while (true) {
            if (i2 >= nodeCount) {
                break;
            }
            Node node = fromNodes.get(i2);
            node.boundsChanged = false;
            if (node.isVisible()) {
                this.tmp = node.getTransformedBounds(this.tmp, BaseTransform.IDENTITY_TRANSFORM);
                if (!this.tmp.isEmpty()) {
                    this.far = node;
                    this.bottom = node;
                    this.right = node;
                    this.near = node;
                    this.top = node;
                    this.left = node;
                    break;
                }
            }
            i2++;
        }
        if (i2 == nodeCount) {
            this.far = null;
            this.bottom = null;
            this.right = null;
            this.near = null;
            this.top = null;
            this.left = null;
            this.cachedBounds.makeEmpty();
            return;
        }
        float minX = this.tmp.getMinX();
        float minY = this.tmp.getMinY();
        float minZ = this.tmp.getMinZ();
        float maxX = this.tmp.getMaxX();
        float maxY = this.tmp.getMaxY();
        float maxZ = this.tmp.getMaxZ();
        while (true) {
            i2++;
            if (i2 < nodeCount) {
                Node node2 = fromNodes.get(i2);
                node2.boundsChanged = false;
                if (node2.isVisible()) {
                    this.tmp = node2.getTransformedBounds(this.tmp, BaseTransform.IDENTITY_TRANSFORM);
                    if (!this.tmp.isEmpty()) {
                        float tmpx = this.tmp.getMinX();
                        float tmpy = this.tmp.getMinY();
                        float tmpz = this.tmp.getMinZ();
                        float tmpx2 = this.tmp.getMaxX();
                        float tmpy2 = this.tmp.getMaxY();
                        float tmpz2 = this.tmp.getMaxZ();
                        if (tmpx < minX) {
                            minX = tmpx;
                            this.left = node2;
                        }
                        if (tmpy < minY) {
                            minY = tmpy;
                            this.top = node2;
                        }
                        if (tmpz < minZ) {
                            minZ = tmpz;
                            this.near = node2;
                        }
                        if (tmpx2 > maxX) {
                            maxX = tmpx2;
                            this.right = node2;
                        }
                        if (tmpy2 > maxY) {
                            maxY = tmpy2;
                            this.bottom = node2;
                        }
                        if (tmpz2 > maxZ) {
                            maxZ = tmpz2;
                            this.far = node2;
                        }
                    }
                }
            } else {
                this.cachedBounds = this.cachedBounds.deriveWithNewBounds(minX, minY, minZ, maxX, maxY, maxZ);
                return;
            }
        }
    }

    protected void updateBounds() {
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            this.children.get(i2).updateBounds();
        }
        super.updateBounds();
    }

    private BaseBounds getChildTransformedBounds(Node node, BaseTransform tx, BaseBounds bounds) {
        this.currentlyProcessedChild = node;
        BaseBounds bounds2 = node.getTransformedBounds(bounds, tx);
        this.currentlyProcessedChild = null;
        return bounds2;
    }

    void childBoundsChanged(Node node) {
        if (node == this.currentlyProcessedChild) {
            return;
        }
        this.cachedBoundsInvalid = true;
        setChildDirty(node, true);
        impl_geomChanged();
    }

    void childVisibilityChanged(Node node) {
        if (node.isVisible()) {
            childIncluded(node);
        } else {
            childExcluded(node);
        }
        impl_geomChanged();
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        Point2D tempPt = TempState.getInstance().point;
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node node = this.children.get(i2);
            tempPt.f11907x = (float) localX;
            tempPt.f11908y = (float) localY;
            try {
                node.parentToLocal(tempPt);
            } catch (NoninvertibleTransformException e2) {
            }
            if (node.contains(tempPt.f11907x, tempPt.f11908y)) {
                return true;
            }
        }
        return false;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return alg.processContainerNode(this, ctx);
    }

    @Override // javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case CHILDREN:
                return getChildrenUnmodifiable();
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.Node
    void releaseAccessible() {
        int max = this.children.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node node = this.children.get(i2);
            node.releaseAccessible();
        }
        super.releaseAccessible();
    }

    List<Node> test_getRemoved() {
        return this.removed;
    }
}
