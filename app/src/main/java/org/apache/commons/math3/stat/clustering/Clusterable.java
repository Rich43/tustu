package org.apache.commons.math3.stat.clustering;

import java.util.Collection;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/clustering/Clusterable.class */
public interface Clusterable<T> {
    double distanceFrom(T t2);

    T centroidOf(Collection<T> collection);
}
