package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TreeCellBehavior;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.property.DoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TreeCellSkin.class */
public class TreeCellSkin<T> extends CellSkinBase<TreeCell<T>, TreeCellBehavior<T>> {
    private static final Map<TreeView<?>, Double> maxDisclosureWidthMap = new WeakHashMap();
    private DoubleProperty indent;
    private boolean disclosureNodeDirty;
    private TreeItem<?> treeItem;
    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;
    private MultiplePropertyChangeListenerHandler treeItemListener;

    public final void setIndent(double value) {
        indentProperty().set(value);
    }

    public final double getIndent() {
        if (this.indent == null) {
            return 10.0d;
        }
        return this.indent.get();
    }

    public final DoubleProperty indentProperty() {
        if (this.indent == null) {
            this.indent = new StyleableDoubleProperty(10.0d) { // from class: com.sun.javafx.scene.control.skin.TreeCellSkin.1
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeCellSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "indent";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.INDENT;
                }
            };
        }
        return this.indent;
    }

    public TreeCellSkin(TreeCell<T> control) {
        super(control, new TreeCellBehavior(control));
        this.indent = null;
        this.disclosureNodeDirty = true;
        this.treeItemListener = new MultiplePropertyChangeListenerHandler(p2 -> {
            if ("EXPANDED".equals(p2)) {
                updateDisclosureNodeRotation(true);
                return null;
            }
            return null;
        });
        this.fixedCellSize = control.getTreeView().getFixedCellSize();
        this.fixedCellSizeEnabled = this.fixedCellSize > 0.0d;
        updateTreeItem();
        updateDisclosureNodeRotation(false);
        registerChangeListener(control.treeItemProperty(), "TREE_ITEM");
        registerChangeListener(control.textProperty(), "TEXT");
        registerChangeListener(control.getTreeView().fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("TREE_ITEM".equals(p2)) {
            updateTreeItem();
            this.disclosureNodeDirty = true;
            ((TreeCell) getSkinnable()).requestLayout();
        } else if ("TEXT".equals(p2)) {
            ((TreeCell) getSkinnable()).requestLayout();
        } else if ("FIXED_CELL_SIZE".equals(p2)) {
            this.fixedCellSize = ((TreeCell) getSkinnable()).getTreeView().getFixedCellSize();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0d;
        }
    }

    private void updateDisclosureNodeRotation(boolean animate) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateTreeItem() {
        if (this.treeItem != null) {
            this.treeItemListener.unregisterChangeListener(this.treeItem.expandedProperty());
        }
        this.treeItem = ((TreeCell) getSkinnable()).getTreeItem();
        if (this.treeItem != null) {
            this.treeItemListener.registerChangeListener(this.treeItem.expandedProperty(), "EXPANDED");
        }
        updateDisclosureNodeRotation(false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateDisclosureNode() {
        Node disclosureNode;
        if (((TreeCell) getSkinnable()).isEmpty() || (disclosureNode = ((TreeCell) getSkinnable()).getDisclosureNode()) == null) {
            return;
        }
        boolean disclosureVisible = (this.treeItem == null || this.treeItem.isLeaf()) ? false : true;
        disclosureNode.setVisible(disclosureVisible);
        if (!disclosureVisible) {
            getChildren().remove(disclosureNode);
        } else if (disclosureNode.getParent() == null) {
            getChildren().add(disclosureNode);
            disclosureNode.toFront();
        } else {
            disclosureNode.toBack();
        }
        if (disclosureNode.getScene() != null) {
            disclosureNode.applyCss();
        }
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase
    protected void updateChildren() {
        super.updateChildren();
        updateDisclosureNode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        TreeView<T> tree = ((TreeCell) getSkinnable()).getTreeView();
        if (tree == null) {
            return;
        }
        if (this.disclosureNodeDirty) {
            updateDisclosureNode();
            this.disclosureNodeDirty = false;
        }
        Node disclosureNode = ((TreeCell) getSkinnable()).getDisclosureNode();
        int level = tree.getTreeItemLevel(this.treeItem);
        if (!tree.isShowRoot()) {
            level--;
        }
        double leftMargin = getIndent() * level;
        double x3 = x2 + leftMargin;
        boolean disclosureVisible = (disclosureNode == null || this.treeItem == null || this.treeItem.isLeaf()) ? false : true;
        double defaultDisclosureWidth = maxDisclosureWidthMap.containsKey(tree) ? maxDisclosureWidthMap.get(tree).doubleValue() : 18.0d;
        double disclosureWidth = defaultDisclosureWidth;
        if (disclosureVisible) {
            if (disclosureNode == null || disclosureNode.getScene() == null) {
                updateChildren();
            }
            if (disclosureNode != null) {
                disclosureWidth = disclosureNode.prefWidth(h2);
                if (disclosureWidth > defaultDisclosureWidth) {
                    maxDisclosureWidthMap.put(tree, Double.valueOf(disclosureWidth));
                }
                double ph = disclosureNode.prefHeight(disclosureWidth);
                disclosureNode.resize(disclosureWidth, ph);
                positionInArea(disclosureNode, x3, y2, disclosureWidth, ph, 0.0d, HPos.CENTER, VPos.CENTER);
            }
        }
        int padding = (this.treeItem == null || this.treeItem.getGraphic() != null) ? 3 : 0;
        double x4 = x3 + disclosureWidth + padding;
        double w3 = w2 - ((leftMargin + disclosureWidth) + padding);
        Node graphic = ((TreeCell) getSkinnable()).getGraphic();
        if (graphic != null && !getChildren().contains(graphic)) {
            getChildren().add(graphic);
        }
        layoutLabelInArea(x4, y2, w3, h2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        double pref = super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
        Node d2 = ((TreeCell) getSkinnable()).getDisclosureNode();
        return d2 == null ? pref : Math.max(d2.minHeight(-1.0d), pref);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        TreeCell<T> cell = (TreeCell) getSkinnable();
        double pref = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        Node d2 = cell.getDisclosureNode();
        double prefHeight = d2 == null ? pref : Math.max(d2.prefHeight(-1.0d), pref);
        return snapSize(Math.max(cell.getMinHeight(), prefHeight));
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double labelWidth = super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        double pw = snappedLeftInset() + snappedRightInset();
        TreeView<T> tree = ((TreeCell) getSkinnable()).getTreeView();
        if (tree != null && this.treeItem != null) {
            int level = tree.getTreeItemLevel(this.treeItem);
            if (!tree.isShowRoot()) {
                level--;
            }
            double pw2 = labelWidth + (getIndent() * level);
            Node disclosureNode = ((TreeCell) getSkinnable()).getDisclosureNode();
            double disclosureNodePrefWidth = disclosureNode == null ? 0.0d : disclosureNode.prefWidth(-1.0d);
            double defaultDisclosureWidth = maxDisclosureWidthMap.containsKey(tree) ? maxDisclosureWidthMap.get(tree).doubleValue() : 0.0d;
            return pw2 + Math.max(defaultDisclosureWidth, disclosureNodePrefWidth);
        }
        return pw;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TreeCellSkin$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TreeCell<?>, Number> INDENT = new CssMetaData<TreeCell<?>, Number>("-fx-indent", SizeConverter.getInstance(), Double.valueOf(10.0d)) { // from class: com.sun.javafx.scene.control.skin.TreeCellSkin.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TreeCell<?> n2) {
                DoubleProperty p2 = ((TreeCellSkin) n2.getSkin()).indentProperty();
                return p2 == null || !p2.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TreeCell<?> n2) {
                TreeCellSkin<?> skin = (TreeCellSkin) n2.getSkin();
                return (StyleableProperty) skin.indentProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(CellSkinBase.getClassCssMetaData());
            styleables.add(INDENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // com.sun.javafx.scene.control.skin.CellSkinBase, javafx.scene.control.SkinBase
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}
