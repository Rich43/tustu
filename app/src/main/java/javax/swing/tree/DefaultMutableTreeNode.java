package javax.swing.tree;

import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;

/* loaded from: rt.jar:javax/swing/tree/DefaultMutableTreeNode.class */
public class DefaultMutableTreeNode implements Cloneable, MutableTreeNode, Serializable {
    private static final long serialVersionUID = -4298474751201349152L;
    public static final Enumeration<TreeNode> EMPTY_ENUMERATION = Collections.emptyEnumeration();
    protected MutableTreeNode parent;
    protected Vector children;
    protected transient Object userObject;
    protected boolean allowsChildren;

    public DefaultMutableTreeNode() {
        this(null);
    }

    public DefaultMutableTreeNode(Object obj) {
        this(obj, true);
    }

    public DefaultMutableTreeNode(Object obj, boolean z2) {
        this.parent = null;
        this.allowsChildren = z2;
        this.userObject = obj;
    }

    @Override // javax.swing.tree.MutableTreeNode
    public void insert(MutableTreeNode mutableTreeNode, int i2) {
        if (!this.allowsChildren) {
            throw new IllegalStateException("node does not allow children");
        }
        if (mutableTreeNode == null) {
            throw new IllegalArgumentException("new child is null");
        }
        if (isNodeAncestor(mutableTreeNode)) {
            throw new IllegalArgumentException("new child is an ancestor");
        }
        MutableTreeNode mutableTreeNode2 = (MutableTreeNode) mutableTreeNode.getParent();
        if (mutableTreeNode2 != null) {
            mutableTreeNode2.remove(mutableTreeNode);
        }
        mutableTreeNode.setParent(this);
        if (this.children == null) {
            this.children = new Vector();
        }
        this.children.insertElementAt(mutableTreeNode, i2);
    }

    @Override // javax.swing.tree.MutableTreeNode
    public void remove(int i2) {
        MutableTreeNode mutableTreeNode = (MutableTreeNode) getChildAt(i2);
        this.children.removeElementAt(i2);
        mutableTreeNode.setParent(null);
    }

    @Override // javax.swing.tree.MutableTreeNode
    @Transient
    public void setParent(MutableTreeNode mutableTreeNode) {
        this.parent = mutableTreeNode;
    }

    @Override // javax.swing.tree.TreeNode
    public TreeNode getParent() {
        return this.parent;
    }

    public TreeNode getChildAt(int i2) {
        if (this.children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        return (TreeNode) this.children.elementAt(i2);
    }

    public int getChildCount() {
        if (this.children == null) {
            return 0;
        }
        return this.children.size();
    }

    @Override // javax.swing.tree.TreeNode
    public int getIndex(TreeNode treeNode) {
        if (treeNode == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (!isNodeChild(treeNode)) {
            return -1;
        }
        return this.children.indexOf(treeNode);
    }

    public Enumeration children() {
        if (this.children == null) {
            return EMPTY_ENUMERATION;
        }
        return this.children.elements();
    }

    public void setAllowsChildren(boolean z2) {
        if (z2 != this.allowsChildren) {
            this.allowsChildren = z2;
            if (!this.allowsChildren) {
                removeAllChildren();
            }
        }
    }

    @Override // javax.swing.tree.TreeNode
    public boolean getAllowsChildren() {
        return this.allowsChildren;
    }

    @Override // javax.swing.tree.MutableTreeNode
    public void setUserObject(Object obj) {
        this.userObject = obj;
    }

    public Object getUserObject() {
        return this.userObject;
    }

    @Override // javax.swing.tree.MutableTreeNode
    public void removeFromParent() {
        MutableTreeNode mutableTreeNode = (MutableTreeNode) getParent();
        if (mutableTreeNode != null) {
            mutableTreeNode.remove(this);
        }
    }

    @Override // javax.swing.tree.MutableTreeNode
    public void remove(MutableTreeNode mutableTreeNode) {
        if (mutableTreeNode == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (!isNodeChild(mutableTreeNode)) {
            throw new IllegalArgumentException("argument is not a child");
        }
        remove(getIndex(mutableTreeNode));
    }

    public void removeAllChildren() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            remove(childCount);
        }
    }

    public void add(MutableTreeNode mutableTreeNode) {
        if (mutableTreeNode != null && mutableTreeNode.getParent() == this) {
            insert(mutableTreeNode, getChildCount() - 1);
        } else {
            insert(mutableTreeNode, getChildCount());
        }
    }

    public boolean isNodeAncestor(TreeNode treeNode) {
        if (treeNode == null) {
            return false;
        }
        TreeNode treeNode2 = this;
        while (treeNode2 != treeNode) {
            TreeNode parent = treeNode2.getParent();
            treeNode2 = parent;
            if (parent == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isNodeDescendant(DefaultMutableTreeNode defaultMutableTreeNode) {
        if (defaultMutableTreeNode == null) {
            return false;
        }
        return defaultMutableTreeNode.isNodeAncestor(this);
    }

    public TreeNode getSharedAncestor(DefaultMutableTreeNode defaultMutableTreeNode) {
        int i2;
        DefaultMutableTreeNode parent;
        DefaultMutableTreeNode parent2;
        if (defaultMutableTreeNode == this) {
            return this;
        }
        if (defaultMutableTreeNode == null) {
            return null;
        }
        int level = getLevel();
        int level2 = defaultMutableTreeNode.getLevel();
        if (level2 > level) {
            i2 = level2 - level;
            parent = defaultMutableTreeNode;
            parent2 = this;
        } else {
            i2 = level - level2;
            parent = this;
            parent2 = defaultMutableTreeNode;
        }
        while (i2 > 0) {
            parent = parent.getParent();
            i2--;
        }
        while (parent != parent2) {
            parent = parent.getParent();
            parent2 = parent2.getParent();
            if (parent == null) {
                if (parent != null || parent2 != null) {
                    throw new Error("nodes should be null");
                }
                return null;
            }
        }
        return parent;
    }

    public boolean isNodeRelated(DefaultMutableTreeNode defaultMutableTreeNode) {
        return defaultMutableTreeNode != null && getRoot() == defaultMutableTreeNode.getRoot();
    }

    public int getDepth() {
        Object objNextElement = null;
        Enumeration enumerationBreadthFirstEnumeration = breadthFirstEnumeration();
        while (enumerationBreadthFirstEnumeration.hasMoreElements()) {
            objNextElement = enumerationBreadthFirstEnumeration.nextElement();
        }
        if (objNextElement == null) {
            throw new Error("nodes should be null");
        }
        return ((DefaultMutableTreeNode) objNextElement).getLevel() - getLevel();
    }

    public int getLevel() {
        int i2 = 0;
        DefaultMutableTreeNode defaultMutableTreeNode = this;
        while (true) {
            TreeNode parent = defaultMutableTreeNode.getParent();
            defaultMutableTreeNode = parent;
            if (parent != null) {
                i2++;
            } else {
                return i2;
            }
        }
    }

    public TreeNode[] getPath() {
        return getPathToRoot(this, 0);
    }

    protected TreeNode[] getPathToRoot(TreeNode treeNode, int i2) {
        TreeNode[] pathToRoot;
        if (treeNode == null) {
            if (i2 == 0) {
                return null;
            }
            pathToRoot = new TreeNode[i2];
        } else {
            int i3 = i2 + 1;
            pathToRoot = getPathToRoot(treeNode.getParent(), i3);
            pathToRoot[pathToRoot.length - i3] = treeNode;
        }
        return pathToRoot;
    }

    public Object[] getUserObjectPath() {
        TreeNode[] path = getPath();
        Object[] objArr = new Object[path.length];
        for (int i2 = 0; i2 < path.length; i2++) {
            objArr[i2] = ((DefaultMutableTreeNode) path[i2]).getUserObject();
        }
        return objArr;
    }

    public TreeNode getRoot() {
        DefaultMutableTreeNode defaultMutableTreeNode;
        DefaultMutableTreeNode parent = this;
        do {
            defaultMutableTreeNode = parent;
            parent = parent.getParent();
        } while (parent != null);
        return defaultMutableTreeNode;
    }

    public boolean isRoot() {
        return getParent() == null;
    }

    public DefaultMutableTreeNode getNextNode() {
        if (getChildCount() == 0) {
            DefaultMutableTreeNode nextSibling = getNextSibling();
            if (nextSibling == null) {
                TreeNode parent = getParent();
                while (true) {
                    DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) parent;
                    if (defaultMutableTreeNode == null) {
                        return null;
                    }
                    DefaultMutableTreeNode nextSibling2 = defaultMutableTreeNode.getNextSibling();
                    if (nextSibling2 != null) {
                        return nextSibling2;
                    }
                    parent = defaultMutableTreeNode.getParent();
                }
            } else {
                return nextSibling;
            }
        } else {
            return (DefaultMutableTreeNode) getChildAt(0);
        }
    }

    public DefaultMutableTreeNode getPreviousNode() {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) getParent();
        if (defaultMutableTreeNode == null) {
            return null;
        }
        DefaultMutableTreeNode previousSibling = getPreviousSibling();
        if (previousSibling != null) {
            if (previousSibling.getChildCount() == 0) {
                return previousSibling;
            }
            return previousSibling.getLastLeaf();
        }
        return defaultMutableTreeNode;
    }

    public Enumeration preorderEnumeration() {
        return new PreorderEnumeration(this);
    }

    public Enumeration postorderEnumeration() {
        return new PostorderEnumeration(this);
    }

    public Enumeration breadthFirstEnumeration() {
        return new BreadthFirstEnumeration(this);
    }

    public Enumeration depthFirstEnumeration() {
        return postorderEnumeration();
    }

    public Enumeration pathFromAncestorEnumeration(TreeNode treeNode) {
        return new PathBetweenNodesEnumeration(treeNode, this);
    }

    public boolean isNodeChild(TreeNode treeNode) {
        boolean z2;
        if (treeNode == null || getChildCount() == 0) {
            z2 = false;
        } else {
            z2 = treeNode.getParent() == this;
        }
        return z2;
    }

    public TreeNode getFirstChild() {
        if (getChildCount() == 0) {
            throw new NoSuchElementException("node has no children");
        }
        return getChildAt(0);
    }

    public TreeNode getLastChild() {
        if (getChildCount() == 0) {
            throw new NoSuchElementException("node has no children");
        }
        return getChildAt(getChildCount() - 1);
    }

    public TreeNode getChildAfter(TreeNode treeNode) {
        if (treeNode == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int index = getIndex(treeNode);
        if (index == -1) {
            throw new IllegalArgumentException("node is not a child");
        }
        if (index < getChildCount() - 1) {
            return getChildAt(index + 1);
        }
        return null;
    }

    public TreeNode getChildBefore(TreeNode treeNode) {
        if (treeNode == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int index = getIndex(treeNode);
        if (index == -1) {
            throw new IllegalArgumentException("argument is not a child");
        }
        if (index > 0) {
            return getChildAt(index - 1);
        }
        return null;
    }

    public boolean isNodeSibling(TreeNode treeNode) {
        boolean z2;
        if (treeNode == null) {
            z2 = false;
        } else if (treeNode == this) {
            z2 = true;
        } else {
            TreeNode parent = getParent();
            z2 = parent != null && parent == treeNode.getParent();
            if (z2 && !((DefaultMutableTreeNode) getParent()).isNodeChild(treeNode)) {
                throw new Error("sibling has different parent");
            }
        }
        return z2;
    }

    public int getSiblingCount() {
        TreeNode parent = getParent();
        if (parent == null) {
            return 1;
        }
        return parent.getChildCount();
    }

    public DefaultMutableTreeNode getNextSibling() {
        DefaultMutableTreeNode defaultMutableTreeNode;
        DefaultMutableTreeNode defaultMutableTreeNode2 = (DefaultMutableTreeNode) getParent();
        if (defaultMutableTreeNode2 == null) {
            defaultMutableTreeNode = null;
        } else {
            defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode2.getChildAfter(this);
        }
        if (defaultMutableTreeNode != null && !isNodeSibling(defaultMutableTreeNode)) {
            throw new Error("child of parent is not a sibling");
        }
        return defaultMutableTreeNode;
    }

    public DefaultMutableTreeNode getPreviousSibling() {
        DefaultMutableTreeNode defaultMutableTreeNode;
        DefaultMutableTreeNode defaultMutableTreeNode2 = (DefaultMutableTreeNode) getParent();
        if (defaultMutableTreeNode2 == null) {
            defaultMutableTreeNode = null;
        } else {
            defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode2.getChildBefore(this);
        }
        if (defaultMutableTreeNode != null && !isNodeSibling(defaultMutableTreeNode)) {
            throw new Error("child of parent is not a sibling");
        }
        return defaultMutableTreeNode;
    }

    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    public DefaultMutableTreeNode getFirstLeaf() {
        DefaultMutableTreeNode defaultMutableTreeNode = this;
        while (true) {
            DefaultMutableTreeNode defaultMutableTreeNode2 = defaultMutableTreeNode;
            if (!defaultMutableTreeNode2.isLeaf()) {
                defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode2.getFirstChild();
            } else {
                return defaultMutableTreeNode2;
            }
        }
    }

    public DefaultMutableTreeNode getLastLeaf() {
        DefaultMutableTreeNode defaultMutableTreeNode = this;
        while (true) {
            DefaultMutableTreeNode defaultMutableTreeNode2 = defaultMutableTreeNode;
            if (!defaultMutableTreeNode2.isLeaf()) {
                defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode2.getLastChild();
            } else {
                return defaultMutableTreeNode2;
            }
        }
    }

    public DefaultMutableTreeNode getNextLeaf() {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) getParent();
        if (defaultMutableTreeNode == null) {
            return null;
        }
        DefaultMutableTreeNode nextSibling = getNextSibling();
        if (nextSibling != null) {
            return nextSibling.getFirstLeaf();
        }
        return defaultMutableTreeNode.getNextLeaf();
    }

    public DefaultMutableTreeNode getPreviousLeaf() {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) getParent();
        if (defaultMutableTreeNode == null) {
            return null;
        }
        DefaultMutableTreeNode previousSibling = getPreviousSibling();
        if (previousSibling != null) {
            return previousSibling.getLastLeaf();
        }
        return defaultMutableTreeNode.getPreviousLeaf();
    }

    public int getLeafCount() {
        int i2 = 0;
        Enumeration enumerationBreadthFirstEnumeration = breadthFirstEnumeration();
        while (enumerationBreadthFirstEnumeration.hasMoreElements()) {
            if (((TreeNode) enumerationBreadthFirstEnumeration.nextElement()).isLeaf()) {
                i2++;
            }
        }
        if (i2 < 1) {
            throw new Error("tree has zero leaves");
        }
        return i2;
    }

    public String toString() {
        if (this.userObject == null) {
            return "";
        }
        return this.userObject.toString();
    }

    public Object clone() {
        try {
            DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) super.clone();
            defaultMutableTreeNode.children = null;
            defaultMutableTreeNode.parent = null;
            return defaultMutableTreeNode;
        } catch (CloneNotSupportedException e2) {
            throw new Error(e2.toString());
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Object[] objArr;
        objectOutputStream.defaultWriteObject();
        if (this.userObject != null && (this.userObject instanceof Serializable)) {
            objArr = new Object[]{"userObject", this.userObject};
        } else {
            objArr = new Object[0];
        }
        objectOutputStream.writeObject(objArr);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Object[] objArr = (Object[]) objectInputStream.readObject();
        if (objArr.length > 0 && objArr[0].equals("userObject")) {
            this.userObject = objArr[1];
        }
    }

    /* loaded from: rt.jar:javax/swing/tree/DefaultMutableTreeNode$PreorderEnumeration.class */
    private final class PreorderEnumeration implements Enumeration<TreeNode> {
        private final Stack<Enumeration> stack = new Stack<>();

        public PreorderEnumeration(TreeNode treeNode) {
            Vector vector = new Vector(1);
            vector.addElement(treeNode);
            this.stack.push(vector.elements());
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return !this.stack.empty() && this.stack.peek().hasMoreElements();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        public TreeNode nextElement() {
            Enumeration enumerationPeek = this.stack.peek();
            TreeNode treeNode = (TreeNode) enumerationPeek.nextElement();
            Enumeration enumerationChildren = treeNode.children();
            if (!enumerationPeek.hasMoreElements()) {
                this.stack.pop();
            }
            if (enumerationChildren.hasMoreElements()) {
                this.stack.push(enumerationChildren);
            }
            return treeNode;
        }
    }

    /* loaded from: rt.jar:javax/swing/tree/DefaultMutableTreeNode$PostorderEnumeration.class */
    final class PostorderEnumeration implements Enumeration<TreeNode> {
        protected TreeNode root;
        protected Enumeration<TreeNode> children;
        protected Enumeration<TreeNode> subtree = DefaultMutableTreeNode.EMPTY_ENUMERATION;

        public PostorderEnumeration(TreeNode treeNode) {
            this.root = treeNode;
            this.children = this.root.children();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.root != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        public TreeNode nextElement() {
            TreeNode treeNodeNextElement;
            if (this.subtree.hasMoreElements()) {
                treeNodeNextElement = this.subtree.nextElement();
            } else if (this.children.hasMoreElements()) {
                this.subtree = DefaultMutableTreeNode.this.new PostorderEnumeration(this.children.nextElement());
                treeNodeNextElement = this.subtree.nextElement();
            } else {
                treeNodeNextElement = this.root;
                this.root = null;
            }
            return treeNodeNextElement;
        }
    }

    /* loaded from: rt.jar:javax/swing/tree/DefaultMutableTreeNode$BreadthFirstEnumeration.class */
    final class BreadthFirstEnumeration implements Enumeration<TreeNode> {
        protected Queue queue;

        public BreadthFirstEnumeration(TreeNode treeNode) {
            Vector vector = new Vector(1);
            vector.addElement(treeNode);
            this.queue = new Queue();
            this.queue.enqueue(vector.elements());
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return !this.queue.isEmpty() && ((Enumeration) this.queue.firstObject()).hasMoreElements();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        public TreeNode nextElement() {
            Enumeration enumeration = (Enumeration) this.queue.firstObject();
            TreeNode treeNode = (TreeNode) enumeration.nextElement();
            Enumeration enumerationChildren = treeNode.children();
            if (!enumeration.hasMoreElements()) {
                this.queue.dequeue();
            }
            if (enumerationChildren.hasMoreElements()) {
                this.queue.enqueue(enumerationChildren);
            }
            return treeNode;
        }

        /* loaded from: rt.jar:javax/swing/tree/DefaultMutableTreeNode$BreadthFirstEnumeration$Queue.class */
        final class Queue {
            QNode head;
            QNode tail;

            Queue() {
            }

            /* loaded from: rt.jar:javax/swing/tree/DefaultMutableTreeNode$BreadthFirstEnumeration$Queue$QNode.class */
            final class QNode {
                public Object object;
                public QNode next;

                public QNode(Object obj, QNode qNode) {
                    this.object = obj;
                    this.next = qNode;
                }
            }

            public void enqueue(Object obj) {
                if (this.head == null) {
                    QNode qNode = new QNode(obj, null);
                    this.tail = qNode;
                    this.head = qNode;
                } else {
                    this.tail.next = new QNode(obj, null);
                    this.tail = this.tail.next;
                }
            }

            public Object dequeue() {
                if (this.head == null) {
                    throw new NoSuchElementException("No more elements");
                }
                Object obj = this.head.object;
                QNode qNode = this.head;
                this.head = this.head.next;
                if (this.head == null) {
                    this.tail = null;
                } else {
                    qNode.next = null;
                }
                return obj;
            }

            public Object firstObject() {
                if (this.head == null) {
                    throw new NoSuchElementException("No more elements");
                }
                return this.head.object;
            }

            public boolean isEmpty() {
                return this.head == null;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/tree/DefaultMutableTreeNode$PathBetweenNodesEnumeration.class */
    final class PathBetweenNodesEnumeration implements Enumeration<TreeNode> {
        protected Stack<TreeNode> stack;

        public PathBetweenNodesEnumeration(TreeNode treeNode, TreeNode treeNode2) {
            if (treeNode == null || treeNode2 == null) {
                throw new IllegalArgumentException("argument is null");
            }
            this.stack = new Stack<>();
            this.stack.push(treeNode2);
            TreeNode parent = treeNode2;
            while (parent != treeNode) {
                parent = parent.getParent();
                if (parent == null && treeNode2 != treeNode) {
                    throw new IllegalArgumentException("node " + ((Object) treeNode) + " is not an ancestor of " + ((Object) treeNode2));
                }
                this.stack.push(parent);
            }
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.stack.size() > 0;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        public TreeNode nextElement() {
            try {
                return this.stack.pop();
            } catch (EmptyStackException e2) {
                throw new NoSuchElementException("No more elements");
            }
        }
    }
}
