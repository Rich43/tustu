package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/SubHyperplane.class */
public interface SubHyperplane<S extends Space> {
    SubHyperplane<S> copySelf();

    Hyperplane<S> getHyperplane();

    boolean isEmpty();

    double getSize();

    @Deprecated
    Side side(Hyperplane<S> hyperplane);

    SplitSubHyperplane<S> split(Hyperplane<S> hyperplane);

    SubHyperplane<S> reunite(SubHyperplane<S> subHyperplane);

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/SubHyperplane$SplitSubHyperplane.class */
    public static class SplitSubHyperplane<U extends Space> {
        private final SubHyperplane<U> plus;
        private final SubHyperplane<U> minus;

        public SplitSubHyperplane(SubHyperplane<U> plus, SubHyperplane<U> minus) {
            this.plus = plus;
            this.minus = minus;
        }

        public SubHyperplane<U> getPlus() {
            return this.plus;
        }

        public SubHyperplane<U> getMinus() {
            return this.minus;
        }

        public Side getSide() {
            if (this.plus != null && !this.plus.isEmpty()) {
                if (this.minus != null && !this.minus.isEmpty()) {
                    return Side.BOTH;
                }
                return Side.PLUS;
            }
            if (this.minus != null && !this.minus.isEmpty()) {
                return Side.MINUS;
            }
            return Side.HYPER;
        }
    }
}
