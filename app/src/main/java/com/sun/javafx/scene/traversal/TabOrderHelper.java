package com.sun.javafx.scene.traversal;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/traversal/TabOrderHelper.class */
final class TabOrderHelper {
    TabOrderHelper() {
    }

    private static Node findPreviousFocusableInList(List<Node> nodeList, int startIndex) {
        Node newNode;
        for (int i2 = startIndex; i2 >= 0; i2--) {
            Node prevNode = nodeList.get(i2);
            if (!isDisabledOrInvisible(prevNode)) {
                ParentTraversalEngine impl_traversalEngine = prevNode instanceof Parent ? ((Parent) prevNode).getImpl_traversalEngine() : null;
                if (prevNode instanceof Parent) {
                    if (impl_traversalEngine != null && impl_traversalEngine.canTraverse()) {
                        Node selected = impl_traversalEngine.selectLast();
                        if (selected != null) {
                            return selected;
                        }
                    } else {
                        List<Node> prevNodesList = ((Parent) prevNode).getChildrenUnmodifiable();
                        if (prevNodesList.size() > 0 && (newNode = findPreviousFocusableInList(prevNodesList, prevNodesList.size() - 1)) != null) {
                            return newNode;
                        }
                    }
                }
                if (impl_traversalEngine != null) {
                    if (impl_traversalEngine.isParentTraversable()) {
                        return prevNode;
                    }
                } else if (prevNode.isFocusTraversable()) {
                    return prevNode;
                }
            }
        }
        return null;
    }

    private static boolean isDisabledOrInvisible(Node prevNode) {
        return prevNode.isDisabled() || !prevNode.impl_isTreeVisible();
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0071, code lost:
    
        r7 = r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0098 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static javafx.scene.Node findPreviousFocusablePeer(javafx.scene.Node r4, javafx.scene.Parent r5) {
        /*
            r0 = r4
            r6 = r0
            r0 = 0
            r7 = r0
            r0 = r6
            java.util.List r0 = findPeers(r0)
            r8 = r0
            r0 = r8
            if (r0 != 0) goto L27
            r0 = r4
            javafx.scene.Parent r0 = (javafx.scene.Parent) r0
            javafx.collections.ObservableList r0 = r0.getChildrenUnmodifiable()
            r9 = r0
            r0 = r9
            r1 = r9
            int r1 = r1.size()
            r2 = 1
            int r1 = r1 - r2
            javafx.scene.Node r0 = findPreviousFocusableInList(r0, r1)
            return r0
        L27:
            r0 = r8
            r1 = r6
            int r0 = r0.indexOf(r1)
            r9 = r0
            r0 = r8
            r1 = r9
            r2 = 1
            int r1 = r1 - r2
            javafx.scene.Node r0 = findPreviousFocusableInList(r0, r1)
            r7 = r0
        L3b:
            r0 = r7
            if (r0 != 0) goto L9e
            r0 = r6
            javafx.scene.Parent r0 = r0.getParent()
            r1 = r5
            if (r0 == r1) goto L9e
            r0 = r6
            javafx.scene.Parent r0 = r0.getParent()
            r12 = r0
            r0 = r12
            if (r0 == 0) goto L98
            r0 = r12
            com.sun.javafx.scene.traversal.ParentTraversalEngine r0 = r0.getImpl_traversalEngine()
            r13 = r0
            r0 = r13
            if (r0 == 0) goto L69
            r0 = r13
            boolean r0 = r0.isParentTraversable()
            if (r0 == 0) goto L77
            goto L71
        L69:
            r0 = r12
            boolean r0 = r0.isFocusTraversable()
            if (r0 == 0) goto L77
        L71:
            r0 = r12
            r7 = r0
            goto L98
        L77:
            r0 = r12
            java.util.List r0 = findPeers(r0)
            r10 = r0
            r0 = r10
            if (r0 == 0) goto L98
            r0 = r10
            r1 = r12
            int r0 = r0.indexOf(r1)
            r11 = r0
            r0 = r10
            r1 = r11
            r2 = 1
            int r1 = r1 - r2
            javafx.scene.Node r0 = findPreviousFocusableInList(r0, r1)
            r7 = r0
        L98:
            r0 = r12
            r6 = r0
            goto L3b
        L9e:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.traversal.TabOrderHelper.findPreviousFocusablePeer(javafx.scene.Node, javafx.scene.Parent):javafx.scene.Node");
    }

    private static List<Node> findPeers(Node node) {
        List<Node> parentNodes = null;
        Parent parent = node.getParent();
        if (parent != null) {
            parentNodes = parent.getChildrenUnmodifiable();
        }
        return parentNodes;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0094 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static javafx.scene.Node findNextFocusableInList(java.util.List<javafx.scene.Node> r3, int r4) {
        /*
            r0 = r4
            r5 = r0
        L2:
            r0 = r5
            r1 = r3
            int r1 = r1.size()
            if (r0 >= r1) goto L9a
            r0 = r3
            r1 = r5
            java.lang.Object r0 = r0.get(r1)
            javafx.scene.Node r0 = (javafx.scene.Node) r0
            r6 = r0
            r0 = r6
            boolean r0 = isDisabledOrInvisible(r0)
            if (r0 == 0) goto L21
            goto L94
        L21:
            r0 = r6
            boolean r0 = r0 instanceof javafx.scene.Parent
            if (r0 == 0) goto L32
            r0 = r6
            javafx.scene.Parent r0 = (javafx.scene.Parent) r0
            com.sun.javafx.scene.traversal.ParentTraversalEngine r0 = r0.getImpl_traversalEngine()
            goto L33
        L32:
            r0 = 0
        L33:
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L45
            r0 = r7
            boolean r0 = r0.isParentTraversable()
            if (r0 == 0) goto L4e
            goto L4c
        L45:
            r0 = r6
            boolean r0 = r0.isFocusTraversable()
            if (r0 == 0) goto L4e
        L4c:
            r0 = r6
            return r0
        L4e:
            r0 = r6
            boolean r0 = r0 instanceof javafx.scene.Parent
            if (r0 == 0) goto L94
            r0 = r7
            if (r0 == 0) goto L71
            r0 = r7
            boolean r0 = r0.canTraverse()
            if (r0 == 0) goto L71
            r0 = r7
            javafx.scene.Node r0 = r0.selectFirst()
            r8 = r0
            r0 = r8
            if (r0 == 0) goto L94
            r0 = r8
            return r0
        L71:
            r0 = r6
            javafx.scene.Parent r0 = (javafx.scene.Parent) r0
            javafx.collections.ObservableList r0 = r0.getChildrenUnmodifiable()
            r8 = r0
            r0 = r8
            int r0 = r0.size()
            if (r0 <= 0) goto L94
            r0 = r8
            r1 = 0
            javafx.scene.Node r0 = findNextFocusableInList(r0, r1)
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L94
            r0 = r9
            return r0
        L94:
            int r5 = r5 + 1
            goto L2
        L9a:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.traversal.TabOrderHelper.findNextFocusableInList(java.util.List, int):javafx.scene.Node");
    }

    public static Node findNextFocusablePeer(Node node, Parent root, boolean traverseIntoCurrent) {
        List<Node> peerNodes;
        Node startNode = node;
        Node newNode = null;
        if (traverseIntoCurrent && (node instanceof Parent)) {
            newNode = findNextFocusableInList(((Parent) node).getChildrenUnmodifiable(), 0);
        }
        if (newNode == null) {
            List<Node> parentNodes = findPeers(startNode);
            if (parentNodes == null) {
                return null;
            }
            int ourIndex = parentNodes.indexOf(startNode);
            newNode = findNextFocusableInList(parentNodes, ourIndex + 1);
        }
        while (newNode == null && startNode.getParent() != root) {
            Parent parent = startNode.getParent();
            if (parent != null && (peerNodes = findPeers(parent)) != null) {
                int parentIndex = peerNodes.indexOf(parent);
                newNode = findNextFocusableInList(peerNodes, parentIndex + 1);
            }
            startNode = parent;
        }
        return newNode;
    }

    public static Node getFirstTargetNode(Parent p2) {
        Node result;
        Node selected;
        if (p2 == null || isDisabledOrInvisible(p2)) {
            return null;
        }
        ParentTraversalEngine impl_traversalEngine = p2.getImpl_traversalEngine();
        if (impl_traversalEngine != null && impl_traversalEngine.canTraverse() && (selected = impl_traversalEngine.selectFirst()) != null) {
            return selected;
        }
        List<Node> parentsNodes = p2.getChildrenUnmodifiable();
        for (Node n2 : parentsNodes) {
            if (!isDisabledOrInvisible(n2)) {
                ParentTraversalEngine parentEngine = n2 instanceof Parent ? ((Parent) n2).getImpl_traversalEngine() : null;
                if (parentEngine != null) {
                    if (parentEngine.isParentTraversable()) {
                        return n2;
                    }
                    if ((n2 instanceof Parent) && (result = getFirstTargetNode((Parent) n2)) != null) {
                        return result;
                    }
                } else {
                    if (n2.isFocusTraversable()) {
                        return n2;
                    }
                    if (n2 instanceof Parent) {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public static Node getLastTargetNode(Parent p2) {
        Node result;
        Node selected;
        if (p2 == null || isDisabledOrInvisible(p2)) {
            return null;
        }
        ParentTraversalEngine impl_traversalEngine = p2.getImpl_traversalEngine();
        if (impl_traversalEngine != null && impl_traversalEngine.canTraverse() && (selected = impl_traversalEngine.selectLast()) != null) {
            return selected;
        }
        List<Node> parentsNodes = p2.getChildrenUnmodifiable();
        for (int i2 = parentsNodes.size() - 1; i2 >= 0; i2--) {
            Node n2 = parentsNodes.get(i2);
            if (!isDisabledOrInvisible(n2)) {
                if ((n2 instanceof Parent) && (result = getLastTargetNode((Parent) n2)) != null) {
                    return result;
                }
                ParentTraversalEngine parentEngine = n2 instanceof Parent ? ((Parent) n2).getImpl_traversalEngine() : null;
                if (parentEngine != null) {
                    if (parentEngine.isParentTraversable()) {
                        return n2;
                    }
                } else if (n2.isFocusTraversable()) {
                    return n2;
                }
            }
        }
        return null;
    }
}
