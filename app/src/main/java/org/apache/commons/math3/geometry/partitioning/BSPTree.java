package org.apache.commons.math3.geometry.partitioning;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/BSPTree.class */
public class BSPTree<S extends Space> {
    private SubHyperplane<S> cut;
    private BSPTree<S> plus;
    private BSPTree<S> minus;
    private BSPTree<S> parent;
    private Object attribute;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/BSPTree$LeafMerger.class */
    public interface LeafMerger<S extends Space> {
        BSPTree<S> merge(BSPTree<S> bSPTree, BSPTree<S> bSPTree2, BSPTree<S> bSPTree3, boolean z2, boolean z3);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/BSPTree$VanishingCutHandler.class */
    public interface VanishingCutHandler<S extends Space> {
        BSPTree<S> fixNode(BSPTree<S> bSPTree);
    }

    public BSPTree() {
        this.cut = null;
        this.plus = null;
        this.minus = null;
        this.parent = null;
        this.attribute = null;
    }

    public BSPTree(Object attribute) {
        this.cut = null;
        this.plus = null;
        this.minus = null;
        this.parent = null;
        this.attribute = attribute;
    }

    public BSPTree(SubHyperplane<S> cut, BSPTree<S> plus, BSPTree<S> minus, Object attribute) {
        this.cut = cut;
        this.plus = plus;
        this.minus = minus;
        this.parent = null;
        this.attribute = attribute;
        plus.parent = this;
        minus.parent = this;
    }

    public boolean insertCut(Hyperplane<S> hyperplane) {
        if (this.cut != null) {
            this.plus.parent = null;
            this.minus.parent = null;
        }
        SubHyperplane<S> chopped = fitToCell(hyperplane.wholeHyperplane());
        if (chopped == null || chopped.isEmpty()) {
            this.cut = null;
            this.plus = null;
            this.minus = null;
            return false;
        }
        this.cut = chopped;
        this.plus = new BSPTree<>();
        this.plus.parent = this;
        this.minus = new BSPTree<>();
        this.minus.parent = this;
        return true;
    }

    public BSPTree<S> copySelf() {
        if (this.cut == null) {
            return new BSPTree<>(this.attribute);
        }
        return new BSPTree<>(this.cut.copySelf(), this.plus.copySelf(), this.minus.copySelf(), this.attribute);
    }

    public SubHyperplane<S> getCut() {
        return this.cut;
    }

    public BSPTree<S> getPlus() {
        return this.plus;
    }

    public BSPTree<S> getMinus() {
        return this.minus;
    }

    public BSPTree<S> getParent() {
        return this.parent;
    }

    public void setAttribute(Object attribute) {
        this.attribute = attribute;
    }

    public Object getAttribute() {
        return this.attribute;
    }

    public void visit(BSPTreeVisitor<S> visitor) {
        if (this.cut == null) {
            visitor.visitLeafNode(this);
            return;
        }
        switch (visitor.visitOrder(this)) {
            case PLUS_MINUS_SUB:
                this.plus.visit(visitor);
                this.minus.visit(visitor);
                visitor.visitInternalNode(this);
                return;
            case PLUS_SUB_MINUS:
                this.plus.visit(visitor);
                visitor.visitInternalNode(this);
                this.minus.visit(visitor);
                return;
            case MINUS_PLUS_SUB:
                this.minus.visit(visitor);
                this.plus.visit(visitor);
                visitor.visitInternalNode(this);
                return;
            case MINUS_SUB_PLUS:
                this.minus.visit(visitor);
                visitor.visitInternalNode(this);
                this.plus.visit(visitor);
                return;
            case SUB_PLUS_MINUS:
                visitor.visitInternalNode(this);
                this.plus.visit(visitor);
                this.minus.visit(visitor);
                return;
            case SUB_MINUS_PLUS:
                visitor.visitInternalNode(this);
                this.minus.visit(visitor);
                this.plus.visit(visitor);
                return;
            default:
                throw new MathInternalError();
        }
    }

    private SubHyperplane<S> fitToCell(SubHyperplane<S> sub) {
        SubHyperplane<S> minus;
        SubHyperplane<S> s2 = sub;
        BSPTree<S> bSPTree = this;
        while (true) {
            BSPTree<S> tree = bSPTree;
            if (tree.parent == null || s2 == null) {
                break;
            }
            if (tree == tree.parent.plus) {
                minus = s2.split(tree.parent.cut.getHyperplane()).getPlus();
            } else {
                minus = s2.split(tree.parent.cut.getHyperplane()).getMinus();
            }
            s2 = minus;
            bSPTree = tree.parent;
        }
        return s2;
    }

    @Deprecated
    public BSPTree<S> getCell(Vector<S> point) {
        return getCell(point, 1.0E-10d);
    }

    public BSPTree<S> getCell(Point<S> point, double tolerance) {
        if (this.cut == null) {
            return this;
        }
        double offset = this.cut.getHyperplane().getOffset(point);
        if (FastMath.abs(offset) < tolerance) {
            return this;
        }
        if (offset <= 0.0d) {
            return this.minus.getCell(point, tolerance);
        }
        return this.plus.getCell(point, tolerance);
    }

    public List<BSPTree<S>> getCloseCuts(Point<S> point, double maxOffset) {
        List<BSPTree<S>> close = new ArrayList<>();
        recurseCloseCuts(point, maxOffset, close);
        return close;
    }

    private void recurseCloseCuts(Point<S> point, double maxOffset, List<BSPTree<S>> close) {
        if (this.cut != null) {
            double offset = this.cut.getHyperplane().getOffset(point);
            if (offset < (-maxOffset)) {
                this.minus.recurseCloseCuts(point, maxOffset, close);
            } else {
                if (offset > maxOffset) {
                    this.plus.recurseCloseCuts(point, maxOffset, close);
                    return;
                }
                close.add(this);
                this.minus.recurseCloseCuts(point, maxOffset, close);
                this.plus.recurseCloseCuts(point, maxOffset, close);
            }
        }
    }

    private void condense() {
        if (this.cut != null && this.plus.cut == null && this.minus.cut == null) {
            if ((this.plus.attribute == null && this.minus.attribute == null) || (this.plus.attribute != null && this.plus.attribute.equals(this.minus.attribute))) {
                this.attribute = this.plus.attribute == null ? this.minus.attribute : this.plus.attribute;
                this.cut = null;
                this.plus = null;
                this.minus = null;
            }
        }
    }

    public BSPTree<S> merge(BSPTree<S> tree, LeafMerger<S> leafMerger) {
        return merge(tree, leafMerger, null, false);
    }

    private BSPTree<S> merge(BSPTree<S> tree, LeafMerger<S> leafMerger, BSPTree<S> parentTree, boolean isPlusChild) {
        if (this.cut == null) {
            return leafMerger.merge(this, tree, parentTree, isPlusChild, true);
        }
        if (tree.cut == null) {
            return leafMerger.merge(tree, this, parentTree, isPlusChild, false);
        }
        BSPTree<S> merged = tree.split(this.cut);
        if (parentTree != null) {
            merged.parent = parentTree;
            if (isPlusChild) {
                parentTree.plus = merged;
            } else {
                parentTree.minus = merged;
            }
        }
        this.plus.merge(merged.plus, leafMerger, merged, true);
        this.minus.merge(merged.minus, leafMerger, merged, false);
        merged.condense();
        if (merged.cut != null) {
            merged.cut = merged.fitToCell(merged.cut.getHyperplane().wholeHyperplane());
        }
        return merged;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public BSPTree<S> split(SubHyperplane<S> subHyperplane) {
        if (this.cut == null) {
            return new BSPTree<>(subHyperplane, copySelf(), new BSPTree(this.attribute), null);
        }
        Hyperplane<S> hyperplane = this.cut.getHyperplane();
        Hyperplane<S> hyperplane2 = subHyperplane.getHyperplane();
        SubHyperplane.SplitSubHyperplane<S> splitSubHyperplaneSplit = subHyperplane.split(hyperplane);
        switch (splitSubHyperplaneSplit.getSide()) {
            case PLUS:
                BSPTree<S> bSPTreeSplit = this.plus.split(subHyperplane);
                if (this.cut.split(hyperplane2).getSide() == Side.PLUS) {
                    bSPTreeSplit.plus = new BSPTree<>(this.cut.copySelf(), bSPTreeSplit.plus, this.minus.copySelf(), this.attribute);
                    bSPTreeSplit.plus.condense();
                    bSPTreeSplit.plus.parent = bSPTreeSplit;
                } else {
                    bSPTreeSplit.minus = new BSPTree<>(this.cut.copySelf(), bSPTreeSplit.minus, this.minus.copySelf(), this.attribute);
                    bSPTreeSplit.minus.condense();
                    bSPTreeSplit.minus.parent = bSPTreeSplit;
                }
                return bSPTreeSplit;
            case MINUS:
                BSPTree<S> bSPTreeSplit2 = this.minus.split(subHyperplane);
                if (this.cut.split(hyperplane2).getSide() == Side.PLUS) {
                    bSPTreeSplit2.plus = new BSPTree<>(this.cut.copySelf(), this.plus.copySelf(), bSPTreeSplit2.plus, this.attribute);
                    bSPTreeSplit2.plus.condense();
                    bSPTreeSplit2.plus.parent = bSPTreeSplit2;
                } else {
                    bSPTreeSplit2.minus = new BSPTree<>(this.cut.copySelf(), this.plus.copySelf(), bSPTreeSplit2.minus, this.attribute);
                    bSPTreeSplit2.minus.condense();
                    bSPTreeSplit2.minus.parent = bSPTreeSplit2;
                }
                return bSPTreeSplit2;
            case BOTH:
                SubHyperplane.SplitSubHyperplane<S> splitSubHyperplaneSplit2 = this.cut.split(hyperplane2);
                BSPTree<S> bSPTree = new BSPTree<>(subHyperplane, this.plus.split(splitSubHyperplaneSplit.getPlus()), this.minus.split(splitSubHyperplaneSplit.getMinus()), null);
                bSPTree.plus.cut = (SubHyperplane<S>) splitSubHyperplaneSplit2.getPlus();
                bSPTree.minus.cut = (SubHyperplane<S>) splitSubHyperplaneSplit2.getMinus();
                BSPTree<S> bSPTree2 = bSPTree.plus.minus;
                bSPTree.plus.minus = bSPTree.minus.plus;
                bSPTree.plus.minus.parent = bSPTree.plus;
                bSPTree.minus.plus = bSPTree2;
                bSPTree.minus.plus.parent = bSPTree.minus;
                bSPTree.plus.condense();
                bSPTree.minus.condense();
                return bSPTree;
            default:
                return hyperplane.sameOrientationAs(hyperplane2) ? new BSPTree<>(subHyperplane, this.plus.copySelf(), this.minus.copySelf(), this.attribute) : new BSPTree<>(subHyperplane, this.minus.copySelf(), this.plus.copySelf(), this.attribute);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public void insertInTree(BSPTree<S> parentTree, boolean isPlusChild) {
        insertInTree(parentTree, isPlusChild, new VanishingCutHandler<S>() { // from class: org.apache.commons.math3.geometry.partitioning.BSPTree.1
            @Override // org.apache.commons.math3.geometry.partitioning.BSPTree.VanishingCutHandler
            public BSPTree<S> fixNode(BSPTree<S> node) {
                throw new MathIllegalStateException(LocalizedFormats.NULL_NOT_ALLOWED, new Object[0]);
            }
        });
    }

    public void insertInTree(BSPTree<S> bSPTree, boolean z2, VanishingCutHandler<S> vanishingCutHandler) {
        this.parent = bSPTree;
        if (bSPTree != null) {
            if (z2) {
                bSPTree.plus = this;
            } else {
                bSPTree.minus = this;
            }
        }
        if (this.cut != null) {
            BSPTree<S> bSPTree2 = this;
            while (true) {
                BSPTree<S> bSPTree3 = bSPTree2;
                if (bSPTree3.parent == null) {
                    break;
                }
                Hyperplane<S> hyperplane = bSPTree3.parent.cut.getHyperplane();
                if (bSPTree3 == bSPTree3.parent.plus) {
                    this.cut = (SubHyperplane<S>) this.cut.split(hyperplane).getPlus();
                    this.plus.chopOffMinus(hyperplane, vanishingCutHandler);
                    this.minus.chopOffMinus(hyperplane, vanishingCutHandler);
                } else {
                    this.cut = (SubHyperplane<S>) this.cut.split(hyperplane).getMinus();
                    this.plus.chopOffPlus(hyperplane, vanishingCutHandler);
                    this.minus.chopOffPlus(hyperplane, vanishingCutHandler);
                }
                if (this.cut == null) {
                    BSPTree<S> bSPTreeFixNode = vanishingCutHandler.fixNode(this);
                    this.cut = bSPTreeFixNode.cut;
                    this.plus = bSPTreeFixNode.plus;
                    this.minus = bSPTreeFixNode.minus;
                    this.attribute = bSPTreeFixNode.attribute;
                    if (this.cut == null) {
                        break;
                    }
                }
                bSPTree2 = bSPTree3.parent;
            }
            condense();
        }
    }

    public BSPTree<S> pruneAroundConvexCell(Object cellAttribute, Object otherLeafsAttributes, Object internalAttributes) {
        BSPTree<S> bSPTree;
        BSPTree<S> tree = new BSPTree<>(cellAttribute);
        BSPTree<S> bSPTree2 = this;
        while (true) {
            BSPTree<S> current = bSPTree2;
            if (current.parent != null) {
                SubHyperplane<S> parentCut = current.parent.cut.copySelf();
                BSPTree<S> sibling = new BSPTree<>(otherLeafsAttributes);
                if (current == current.parent.plus) {
                    bSPTree = new BSPTree<>(parentCut, tree, sibling, internalAttributes);
                } else {
                    bSPTree = new BSPTree<>(parentCut, sibling, tree, internalAttributes);
                }
                tree = bSPTree;
                bSPTree2 = current.parent;
            } else {
                return tree;
            }
        }
    }

    private void chopOffMinus(Hyperplane<S> hyperplane, VanishingCutHandler<S> vanishingCutHandler) {
        if (this.cut != null) {
            this.cut = (SubHyperplane<S>) this.cut.split(hyperplane).getPlus();
            this.plus.chopOffMinus(hyperplane, vanishingCutHandler);
            this.minus.chopOffMinus(hyperplane, vanishingCutHandler);
            if (this.cut == null) {
                BSPTree<S> bSPTreeFixNode = vanishingCutHandler.fixNode(this);
                this.cut = bSPTreeFixNode.cut;
                this.plus = bSPTreeFixNode.plus;
                this.minus = bSPTreeFixNode.minus;
                this.attribute = bSPTreeFixNode.attribute;
            }
        }
    }

    private void chopOffPlus(Hyperplane<S> hyperplane, VanishingCutHandler<S> vanishingCutHandler) {
        if (this.cut != null) {
            this.cut = (SubHyperplane<S>) this.cut.split(hyperplane).getMinus();
            this.plus.chopOffPlus(hyperplane, vanishingCutHandler);
            this.minus.chopOffPlus(hyperplane, vanishingCutHandler);
            if (this.cut == null) {
                BSPTree<S> bSPTreeFixNode = vanishingCutHandler.fixNode(this);
                this.cut = bSPTreeFixNode.cut;
                this.plus = bSPTreeFixNode.plus;
                this.minus = bSPTreeFixNode.minus;
                this.attribute = bSPTreeFixNode.attribute;
            }
        }
    }
}
