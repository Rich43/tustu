package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/BoundaryBuilder.class */
class BoundaryBuilder<S extends Space> implements BSPTreeVisitor<S> {
    BoundaryBuilder() {
    }

    @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
    public BSPTreeVisitor.Order visitOrder(BSPTree<S> node) {
        return BSPTreeVisitor.Order.PLUS_MINUS_SUB;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
    public void visitInternalNode(BSPTree<S> node) {
        SubHyperplane<S> plusOutside = null;
        SubHyperplane<S> plusInside = null;
        NodesSet<S> splitters = null;
        Characterization<S> plusChar = new Characterization<>(node.getPlus(), node.getCut().copySelf());
        if (plusChar.touchOutside()) {
            Characterization<S> minusChar = new Characterization<>(node.getMinus(), plusChar.outsideTouching());
            if (minusChar.touchInside()) {
                plusOutside = minusChar.insideTouching();
                splitters = new NodesSet<>();
                splitters.addAll(minusChar.getInsideSplitters());
                splitters.addAll(plusChar.getOutsideSplitters());
            }
        }
        if (plusChar.touchInside()) {
            Characterization<S> minusChar2 = new Characterization<>(node.getMinus(), plusChar.insideTouching());
            if (minusChar2.touchOutside()) {
                plusInside = minusChar2.outsideTouching();
                if (splitters == null) {
                    splitters = new NodesSet<>();
                }
                splitters.addAll(minusChar2.getOutsideSplitters());
                splitters.addAll(plusChar.getInsideSplitters());
            }
        }
        if (splitters != null) {
            BSPTree<S> parent = node.getParent();
            while (true) {
                BSPTree<S> up = parent;
                if (up == null) {
                    break;
                }
                splitters.add(up);
                parent = up.getParent();
            }
        }
        node.setAttribute(new BoundaryAttribute(plusOutside, plusInside, splitters));
    }

    @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
    public void visitLeafNode(BSPTree<S> node) {
    }
}
