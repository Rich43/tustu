package com.sun.corba.se.impl.orbutil.graph;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/graph/NodeData.class */
public class NodeData {
    private boolean visited;
    private boolean root;

    public NodeData() {
        clear();
    }

    public void clear() {
        this.visited = false;
        this.root = true;
    }

    boolean isVisited() {
        return this.visited;
    }

    void visited() {
        this.visited = true;
    }

    boolean isRoot() {
        return this.root;
    }

    void notRoot() {
        this.root = false;
    }
}
