package com.sun.corba.se.impl.orbutil.graph;

import java.util.Set;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/graph/Graph.class */
public interface Graph extends Set {
    NodeData getNodeData(Node node);

    Set getRoots();
}
