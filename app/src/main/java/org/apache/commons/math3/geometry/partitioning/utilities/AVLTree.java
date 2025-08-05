package org.apache.commons.math3.geometry.partitioning.utilities;

import java.lang.Comparable;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/utilities/AVLTree.class */
public class AVLTree<T extends Comparable<T>> {
    private AVLTree<T>.Node top = null;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/utilities/AVLTree$Skew.class */
    private enum Skew {
        LEFT_HIGH,
        RIGHT_HIGH,
        BALANCED
    }

    public void insert(T element) {
        if (element != null) {
            if (this.top == null) {
                this.top = new Node(element, null);
            } else {
                this.top.insert(element);
            }
        }
    }

    public boolean delete(T element) {
        if (element != null) {
            AVLTree<T>.Node notSmaller = getNotSmaller(element);
            while (true) {
                AVLTree<T>.Node node = notSmaller;
                if (node == null) {
                    return false;
                }
                if (((Node) node).element == element) {
                    node.delete();
                    return true;
                }
                if (((Node) node).element.compareTo(element) <= 0) {
                    notSmaller = node.getNext();
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return this.top == null;
    }

    public int size() {
        if (this.top == null) {
            return 0;
        }
        return this.top.size();
    }

    public AVLTree<T>.Node getSmallest() {
        if (this.top == null) {
            return null;
        }
        return this.top.getSmallest();
    }

    public AVLTree<T>.Node getLargest() {
        if (this.top == null) {
            return null;
        }
        return this.top.getLargest();
    }

    public AVLTree<T>.Node getNotSmaller(T reference) {
        AVLTree<T>.Node candidate = null;
        AVLTree<T>.Node node = this.top;
        while (true) {
            AVLTree<T>.Node node2 = node;
            if (node2 == null) {
                return null;
            }
            if (((Node) node2).element.compareTo(reference) >= 0) {
                candidate = node2;
                if (((Node) node2).left == null) {
                    return candidate;
                }
                node = ((Node) node2).left;
            } else {
                if (((Node) node2).right == null) {
                    return candidate;
                }
                node = ((Node) node2).right;
            }
        }
    }

    public AVLTree<T>.Node getNotLarger(T reference) {
        AVLTree<T>.Node candidate = null;
        AVLTree<T>.Node node = this.top;
        while (true) {
            AVLTree<T>.Node node2 = node;
            if (node2 == null) {
                return null;
            }
            if (((Node) node2).element.compareTo(reference) <= 0) {
                candidate = node2;
                if (((Node) node2).right == null) {
                    return candidate;
                }
                node = ((Node) node2).right;
            } else {
                if (((Node) node2).left == null) {
                    return candidate;
                }
                node = ((Node) node2).left;
            }
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/utilities/AVLTree$Node.class */
    public class Node {
        private T element;
        private AVLTree<T>.Node parent;
        private AVLTree<T>.Node left = null;
        private AVLTree<T>.Node right = null;
        private Skew skew = Skew.BALANCED;

        Node(T element, AVLTree<T>.Node parent) {
            this.element = element;
            this.parent = parent;
        }

        public T getElement() {
            return this.element;
        }

        int size() {
            return 1 + (this.left == null ? 0 : this.left.size()) + (this.right == null ? 0 : this.right.size());
        }

        AVLTree<T>.Node getSmallest() {
            Node node = this;
            while (true) {
                Node node2 = node;
                if (node2.left != null) {
                    node = node2.left;
                } else {
                    return node2;
                }
            }
        }

        AVLTree<T>.Node getLargest() {
            Node node = this;
            while (true) {
                Node node2 = node;
                if (node2.right != null) {
                    node = node2.right;
                } else {
                    return node2;
                }
            }
        }

        public AVLTree<T>.Node getPrevious() {
            AVLTree<T>.Node node;
            if (this.left != null && (node = this.left.getLargest()) != null) {
                return node;
            }
            Node node2 = this;
            while (true) {
                Node node3 = node2;
                if (node3.parent != null) {
                    if (node3 == node3.parent.left) {
                        node2 = node3.parent;
                    } else {
                        return node3.parent;
                    }
                } else {
                    return null;
                }
            }
        }

        public AVLTree<T>.Node getNext() {
            AVLTree<T>.Node node;
            if (this.right != null && (node = this.right.getSmallest()) != null) {
                return node;
            }
            Node node2 = this;
            while (true) {
                Node node3 = node2;
                if (node3.parent != null) {
                    if (node3 == node3.parent.right) {
                        node2 = node3.parent;
                    } else {
                        return node3.parent;
                    }
                } else {
                    return null;
                }
            }
        }

        boolean insert(T newElement) {
            if (newElement.compareTo(this.element) < 0) {
                if (this.left == null) {
                    this.left = new Node(newElement, this);
                    return rebalanceLeftGrown();
                }
                if (this.left.insert(newElement)) {
                    return rebalanceLeftGrown();
                }
                return false;
            }
            if (this.right == null) {
                this.right = new Node(newElement, this);
                return rebalanceRightGrown();
            }
            if (this.right.insert(newElement)) {
                return rebalanceRightGrown();
            }
            return false;
        }

        public void delete() {
            Node largest;
            boolean leftShrunk;
            AVLTree<T>.Node child;
            if (this.parent == null && this.left == null && this.right == null) {
                this.element = null;
                AVLTree.this.top = null;
                return;
            }
            if (this.left == null && this.right == null) {
                largest = this;
                this.element = null;
                leftShrunk = largest == largest.parent.left;
                child = null;
            } else {
                largest = this.left != null ? this.left.getLargest() : this.right.getSmallest();
                this.element = largest.element;
                leftShrunk = largest == largest.parent.left;
                child = largest.left != null ? largest.left : largest.right;
            }
            AVLTree<T>.Node node = largest.parent;
            if (leftShrunk) {
                node.left = child;
            } else {
                node.right = child;
            }
            if (child != null) {
                child.parent = node;
            }
            while (true) {
                if (leftShrunk) {
                    if (!node.rebalanceLeftShrunk()) {
                        return;
                    }
                } else if (!node.rebalanceRightShrunk()) {
                    return;
                }
                if (node.parent == null) {
                    return;
                }
                leftShrunk = node == node.parent.left;
                node = node.parent;
            }
        }

        private boolean rebalanceLeftGrown() {
            switch (this.skew) {
                case LEFT_HIGH:
                    if (this.left.skew == Skew.LEFT_HIGH) {
                        rotateCW();
                        this.skew = Skew.BALANCED;
                        this.right.skew = Skew.BALANCED;
                        break;
                    } else {
                        Skew s2 = this.left.right.skew;
                        this.left.rotateCCW();
                        rotateCW();
                        switch (s2) {
                            case LEFT_HIGH:
                                this.left.skew = Skew.BALANCED;
                                this.right.skew = Skew.RIGHT_HIGH;
                                break;
                            case RIGHT_HIGH:
                                this.left.skew = Skew.LEFT_HIGH;
                                this.right.skew = Skew.BALANCED;
                                break;
                            default:
                                this.left.skew = Skew.BALANCED;
                                this.right.skew = Skew.BALANCED;
                                break;
                        }
                        this.skew = Skew.BALANCED;
                        break;
                    }
                case RIGHT_HIGH:
                    this.skew = Skew.BALANCED;
                    break;
                default:
                    this.skew = Skew.LEFT_HIGH;
                    break;
            }
            return false;
        }

        private boolean rebalanceRightGrown() {
            switch (this.skew) {
                case LEFT_HIGH:
                    this.skew = Skew.BALANCED;
                    break;
                case RIGHT_HIGH:
                    if (this.right.skew == Skew.RIGHT_HIGH) {
                        rotateCCW();
                        this.skew = Skew.BALANCED;
                        this.left.skew = Skew.BALANCED;
                        break;
                    } else {
                        Skew s2 = this.right.left.skew;
                        this.right.rotateCW();
                        rotateCCW();
                        switch (s2) {
                            case LEFT_HIGH:
                                this.left.skew = Skew.BALANCED;
                                this.right.skew = Skew.RIGHT_HIGH;
                                break;
                            case RIGHT_HIGH:
                                this.left.skew = Skew.LEFT_HIGH;
                                this.right.skew = Skew.BALANCED;
                                break;
                            default:
                                this.left.skew = Skew.BALANCED;
                                this.right.skew = Skew.BALANCED;
                                break;
                        }
                        this.skew = Skew.BALANCED;
                        break;
                    }
                default:
                    this.skew = Skew.RIGHT_HIGH;
                    break;
            }
            return false;
        }

        private boolean rebalanceLeftShrunk() {
            switch (this.skew) {
                case LEFT_HIGH:
                    this.skew = Skew.BALANCED;
                    break;
                case RIGHT_HIGH:
                    if (this.right.skew == Skew.RIGHT_HIGH) {
                        rotateCCW();
                        this.skew = Skew.BALANCED;
                        this.left.skew = Skew.BALANCED;
                        break;
                    } else if (this.right.skew == Skew.BALANCED) {
                        rotateCCW();
                        this.skew = Skew.LEFT_HIGH;
                        this.left.skew = Skew.RIGHT_HIGH;
                        break;
                    } else {
                        Skew s2 = this.right.left.skew;
                        this.right.rotateCW();
                        rotateCCW();
                        switch (s2) {
                            case LEFT_HIGH:
                                this.left.skew = Skew.BALANCED;
                                this.right.skew = Skew.RIGHT_HIGH;
                                break;
                            case RIGHT_HIGH:
                                this.left.skew = Skew.LEFT_HIGH;
                                this.right.skew = Skew.BALANCED;
                                break;
                            default:
                                this.left.skew = Skew.BALANCED;
                                this.right.skew = Skew.BALANCED;
                                break;
                        }
                        this.skew = Skew.BALANCED;
                        break;
                    }
                default:
                    this.skew = Skew.RIGHT_HIGH;
                    break;
            }
            return true;
        }

        private boolean rebalanceRightShrunk() {
            switch (this.skew) {
                case LEFT_HIGH:
                    if (this.left.skew == Skew.LEFT_HIGH) {
                        rotateCW();
                        this.skew = Skew.BALANCED;
                        this.right.skew = Skew.BALANCED;
                        break;
                    } else if (this.left.skew == Skew.BALANCED) {
                        rotateCW();
                        this.skew = Skew.RIGHT_HIGH;
                        this.right.skew = Skew.LEFT_HIGH;
                        break;
                    } else {
                        Skew s2 = this.left.right.skew;
                        this.left.rotateCCW();
                        rotateCW();
                        switch (s2) {
                            case LEFT_HIGH:
                                this.left.skew = Skew.BALANCED;
                                this.right.skew = Skew.RIGHT_HIGH;
                                break;
                            case RIGHT_HIGH:
                                this.left.skew = Skew.LEFT_HIGH;
                                this.right.skew = Skew.BALANCED;
                                break;
                            default:
                                this.left.skew = Skew.BALANCED;
                                this.right.skew = Skew.BALANCED;
                                break;
                        }
                        this.skew = Skew.BALANCED;
                        break;
                    }
                case RIGHT_HIGH:
                    this.skew = Skew.BALANCED;
                    break;
                default:
                    this.skew = Skew.LEFT_HIGH;
                    break;
            }
            return true;
        }

        private void rotateCW() {
            T tmpElt = this.element;
            this.element = this.left.element;
            this.left.element = tmpElt;
            AVLTree<T>.Node tmpNode = this.left;
            this.left = tmpNode.left;
            tmpNode.left = tmpNode.right;
            tmpNode.right = this.right;
            this.right = tmpNode;
            if (this.left != null) {
                this.left.parent = this;
            }
            if (this.right.right != null) {
                this.right.right.parent = this.right;
            }
        }

        private void rotateCCW() {
            T tmpElt = this.element;
            this.element = this.right.element;
            this.right.element = tmpElt;
            AVLTree<T>.Node tmpNode = this.right;
            this.right = tmpNode.right;
            tmpNode.right = tmpNode.left;
            tmpNode.left = this.left;
            this.left = tmpNode;
            if (this.right != null) {
                this.right.parent = this;
            }
            if (this.left.left != null) {
                this.left.left.parent = this.left;
            }
        }
    }
}
