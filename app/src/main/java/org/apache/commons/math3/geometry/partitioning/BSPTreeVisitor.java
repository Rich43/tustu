package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/BSPTreeVisitor.class */
public interface BSPTreeVisitor<S extends Space> {

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/BSPTreeVisitor$Order.class */
    public enum Order {
        PLUS_MINUS_SUB,
        PLUS_SUB_MINUS,
        MINUS_PLUS_SUB,
        MINUS_SUB_PLUS,
        SUB_PLUS_MINUS,
        SUB_MINUS_PLUS
    }

    Order visitOrder(BSPTree<S> bSPTree);

    void visitInternalNode(BSPTree<S> bSPTree);

    void visitLeafNode(BSPTree<S> bSPTree);
}
