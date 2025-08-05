package com.sun.corba.se.impl.orbutil.graph;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/graph/GraphImpl.class */
public class GraphImpl extends AbstractSet implements Graph {
    private Map nodeToData;

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/graph/GraphImpl$NodeVisitor.class */
    interface NodeVisitor {
        void visit(Graph graph, Node node, NodeData nodeData);
    }

    public GraphImpl() {
        this.nodeToData = new HashMap();
    }

    public GraphImpl(Collection collection) {
        this();
        addAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Object obj) {
        if (!(obj instanceof Node)) {
            throw new IllegalArgumentException("Graphs must contain only Node instances");
        }
        Node node = (Node) obj;
        boolean zContains = this.nodeToData.keySet().contains(obj);
        if (!zContains) {
            this.nodeToData.put(node, new NodeData());
        }
        return !zContains;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator iterator() {
        return this.nodeToData.keySet().iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.nodeToData.keySet().size();
    }

    @Override // com.sun.corba.se.impl.orbutil.graph.Graph
    public NodeData getNodeData(Node node) {
        return (NodeData) this.nodeToData.get(node);
    }

    private void clearNodeData() {
        Iterator it = this.nodeToData.entrySet().iterator();
        while (it.hasNext()) {
            ((NodeData) ((Map.Entry) it.next()).getValue()).clear();
        }
    }

    void visitAll(NodeVisitor nodeVisitor) {
        boolean z2;
        do {
            z2 = true;
            for (Map.Entry entry : (Map.Entry[]) this.nodeToData.entrySet().toArray(new Map.Entry[0])) {
                Node node = (Node) entry.getKey();
                NodeData nodeData = (NodeData) entry.getValue();
                if (!nodeData.isVisited()) {
                    nodeData.visited();
                    z2 = false;
                    nodeVisitor.visit(this, node, nodeData);
                }
            }
        } while (!z2);
    }

    private void markNonRoots() {
        visitAll(new NodeVisitor() { // from class: com.sun.corba.se.impl.orbutil.graph.GraphImpl.1
            @Override // com.sun.corba.se.impl.orbutil.graph.GraphImpl.NodeVisitor
            public void visit(Graph graph, Node node, NodeData nodeData) {
                for (Node node2 : node.getChildren()) {
                    graph.add(node2);
                    graph.getNodeData(node2).notRoot();
                }
            }
        });
    }

    private Set collectRootSet() {
        HashSet hashSet = new HashSet();
        for (Map.Entry entry : this.nodeToData.entrySet()) {
            Node node = (Node) entry.getKey();
            if (((NodeData) entry.getValue()).isRoot()) {
                hashSet.add(node);
            }
        }
        return hashSet;
    }

    @Override // com.sun.corba.se.impl.orbutil.graph.Graph
    public Set getRoots() {
        clearNodeData();
        markNonRoots();
        return collectRootSet();
    }
}
