package jdk.internal.org.objectweb.asm.tree;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/InsnList.class */
public class InsnList {
    private int size;
    private AbstractInsnNode first;
    private AbstractInsnNode last;
    AbstractInsnNode[] cache;

    public int size() {
        return this.size;
    }

    public AbstractInsnNode getFirst() {
        return this.first;
    }

    public AbstractInsnNode getLast() {
        return this.last;
    }

    public AbstractInsnNode get(int i2) {
        if (i2 < 0 || i2 >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        if (this.cache == null) {
            this.cache = toArray();
        }
        return this.cache[i2];
    }

    public boolean contains(AbstractInsnNode abstractInsnNode) {
        AbstractInsnNode abstractInsnNode2;
        AbstractInsnNode abstractInsnNode3 = this.first;
        while (true) {
            abstractInsnNode2 = abstractInsnNode3;
            if (abstractInsnNode2 == null || abstractInsnNode2 == abstractInsnNode) {
                break;
            }
            abstractInsnNode3 = abstractInsnNode2.next;
        }
        return abstractInsnNode2 != null;
    }

    public int indexOf(AbstractInsnNode abstractInsnNode) {
        if (this.cache == null) {
            this.cache = toArray();
        }
        return abstractInsnNode.index;
    }

    public void accept(MethodVisitor methodVisitor) {
        AbstractInsnNode abstractInsnNode = this.first;
        while (true) {
            AbstractInsnNode abstractInsnNode2 = abstractInsnNode;
            if (abstractInsnNode2 != null) {
                abstractInsnNode2.accept(methodVisitor);
                abstractInsnNode = abstractInsnNode2.next;
            } else {
                return;
            }
        }
    }

    public ListIterator<AbstractInsnNode> iterator() {
        return iterator(0);
    }

    public ListIterator<AbstractInsnNode> iterator(int i2) {
        return new InsnListIterator(i2);
    }

    public AbstractInsnNode[] toArray() {
        int i2 = 0;
        AbstractInsnNode[] abstractInsnNodeArr = new AbstractInsnNode[this.size];
        for (AbstractInsnNode abstractInsnNode = this.first; abstractInsnNode != null; abstractInsnNode = abstractInsnNode.next) {
            abstractInsnNodeArr[i2] = abstractInsnNode;
            int i3 = i2;
            i2++;
            abstractInsnNode.index = i3;
        }
        return abstractInsnNodeArr;
    }

    public void set(AbstractInsnNode abstractInsnNode, AbstractInsnNode abstractInsnNode2) {
        AbstractInsnNode abstractInsnNode3 = abstractInsnNode.next;
        abstractInsnNode2.next = abstractInsnNode3;
        if (abstractInsnNode3 != null) {
            abstractInsnNode3.prev = abstractInsnNode2;
        } else {
            this.last = abstractInsnNode2;
        }
        AbstractInsnNode abstractInsnNode4 = abstractInsnNode.prev;
        abstractInsnNode2.prev = abstractInsnNode4;
        if (abstractInsnNode4 != null) {
            abstractInsnNode4.next = abstractInsnNode2;
        } else {
            this.first = abstractInsnNode2;
        }
        if (this.cache != null) {
            int i2 = abstractInsnNode.index;
            this.cache[i2] = abstractInsnNode2;
            abstractInsnNode2.index = i2;
        } else {
            abstractInsnNode2.index = 0;
        }
        abstractInsnNode.index = -1;
        abstractInsnNode.prev = null;
        abstractInsnNode.next = null;
    }

    public void add(AbstractInsnNode abstractInsnNode) {
        this.size++;
        if (this.last == null) {
            this.first = abstractInsnNode;
            this.last = abstractInsnNode;
        } else {
            this.last.next = abstractInsnNode;
            abstractInsnNode.prev = this.last;
        }
        this.last = abstractInsnNode;
        this.cache = null;
        abstractInsnNode.index = 0;
    }

    public void add(InsnList insnList) {
        if (insnList.size == 0) {
            return;
        }
        this.size += insnList.size;
        if (this.last == null) {
            this.first = insnList.first;
            this.last = insnList.last;
        } else {
            AbstractInsnNode abstractInsnNode = insnList.first;
            this.last.next = abstractInsnNode;
            abstractInsnNode.prev = this.last;
            this.last = insnList.last;
        }
        this.cache = null;
        insnList.removeAll(false);
    }

    public void insert(AbstractInsnNode abstractInsnNode) {
        this.size++;
        if (this.first == null) {
            this.first = abstractInsnNode;
            this.last = abstractInsnNode;
        } else {
            this.first.prev = abstractInsnNode;
            abstractInsnNode.next = this.first;
        }
        this.first = abstractInsnNode;
        this.cache = null;
        abstractInsnNode.index = 0;
    }

    public void insert(InsnList insnList) {
        if (insnList.size == 0) {
            return;
        }
        this.size += insnList.size;
        if (this.first == null) {
            this.first = insnList.first;
            this.last = insnList.last;
        } else {
            AbstractInsnNode abstractInsnNode = insnList.last;
            this.first.prev = abstractInsnNode;
            abstractInsnNode.next = this.first;
            this.first = insnList.first;
        }
        this.cache = null;
        insnList.removeAll(false);
    }

    public void insert(AbstractInsnNode abstractInsnNode, AbstractInsnNode abstractInsnNode2) {
        this.size++;
        AbstractInsnNode abstractInsnNode3 = abstractInsnNode.next;
        if (abstractInsnNode3 == null) {
            this.last = abstractInsnNode2;
        } else {
            abstractInsnNode3.prev = abstractInsnNode2;
        }
        abstractInsnNode.next = abstractInsnNode2;
        abstractInsnNode2.next = abstractInsnNode3;
        abstractInsnNode2.prev = abstractInsnNode;
        this.cache = null;
        abstractInsnNode2.index = 0;
    }

    public void insert(AbstractInsnNode abstractInsnNode, InsnList insnList) {
        if (insnList.size == 0) {
            return;
        }
        this.size += insnList.size;
        AbstractInsnNode abstractInsnNode2 = insnList.first;
        AbstractInsnNode abstractInsnNode3 = insnList.last;
        AbstractInsnNode abstractInsnNode4 = abstractInsnNode.next;
        if (abstractInsnNode4 == null) {
            this.last = abstractInsnNode3;
        } else {
            abstractInsnNode4.prev = abstractInsnNode3;
        }
        abstractInsnNode.next = abstractInsnNode2;
        abstractInsnNode3.next = abstractInsnNode4;
        abstractInsnNode2.prev = abstractInsnNode;
        this.cache = null;
        insnList.removeAll(false);
    }

    public void insertBefore(AbstractInsnNode abstractInsnNode, AbstractInsnNode abstractInsnNode2) {
        this.size++;
        AbstractInsnNode abstractInsnNode3 = abstractInsnNode.prev;
        if (abstractInsnNode3 == null) {
            this.first = abstractInsnNode2;
        } else {
            abstractInsnNode3.next = abstractInsnNode2;
        }
        abstractInsnNode.prev = abstractInsnNode2;
        abstractInsnNode2.next = abstractInsnNode;
        abstractInsnNode2.prev = abstractInsnNode3;
        this.cache = null;
        abstractInsnNode2.index = 0;
    }

    public void insertBefore(AbstractInsnNode abstractInsnNode, InsnList insnList) {
        if (insnList.size == 0) {
            return;
        }
        this.size += insnList.size;
        AbstractInsnNode abstractInsnNode2 = insnList.first;
        AbstractInsnNode abstractInsnNode3 = insnList.last;
        AbstractInsnNode abstractInsnNode4 = abstractInsnNode.prev;
        if (abstractInsnNode4 == null) {
            this.first = abstractInsnNode2;
        } else {
            abstractInsnNode4.next = abstractInsnNode2;
        }
        abstractInsnNode.prev = abstractInsnNode3;
        abstractInsnNode3.next = abstractInsnNode;
        abstractInsnNode2.prev = abstractInsnNode4;
        this.cache = null;
        insnList.removeAll(false);
    }

    public void remove(AbstractInsnNode abstractInsnNode) {
        this.size--;
        AbstractInsnNode abstractInsnNode2 = abstractInsnNode.next;
        AbstractInsnNode abstractInsnNode3 = abstractInsnNode.prev;
        if (abstractInsnNode2 == null) {
            if (abstractInsnNode3 == null) {
                this.first = null;
                this.last = null;
            } else {
                abstractInsnNode3.next = null;
                this.last = abstractInsnNode3;
            }
        } else if (abstractInsnNode3 == null) {
            this.first = abstractInsnNode2;
            abstractInsnNode2.prev = null;
        } else {
            abstractInsnNode3.next = abstractInsnNode2;
            abstractInsnNode2.prev = abstractInsnNode3;
        }
        this.cache = null;
        abstractInsnNode.index = -1;
        abstractInsnNode.prev = null;
        abstractInsnNode.next = null;
    }

    void removeAll(boolean z2) {
        if (z2) {
            AbstractInsnNode abstractInsnNode = this.first;
            while (true) {
                AbstractInsnNode abstractInsnNode2 = abstractInsnNode;
                if (abstractInsnNode2 == null) {
                    break;
                }
                AbstractInsnNode abstractInsnNode3 = abstractInsnNode2.next;
                abstractInsnNode2.index = -1;
                abstractInsnNode2.prev = null;
                abstractInsnNode2.next = null;
                abstractInsnNode = abstractInsnNode3;
            }
        }
        this.size = 0;
        this.first = null;
        this.last = null;
        this.cache = null;
    }

    public void clear() {
        removeAll(false);
    }

    public void resetLabels() {
        AbstractInsnNode abstractInsnNode = this.first;
        while (true) {
            AbstractInsnNode abstractInsnNode2 = abstractInsnNode;
            if (abstractInsnNode2 != null) {
                if (abstractInsnNode2 instanceof LabelNode) {
                    ((LabelNode) abstractInsnNode2).resetLabel();
                }
                abstractInsnNode = abstractInsnNode2.next;
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/InsnList$InsnListIterator.class */
    private final class InsnListIterator implements ListIterator {
        AbstractInsnNode next;
        AbstractInsnNode prev;
        AbstractInsnNode remove;

        InsnListIterator(int i2) {
            if (i2 == InsnList.this.size()) {
                this.next = null;
                this.prev = InsnList.this.getLast();
            } else {
                this.next = InsnList.this.get(i2);
                this.prev = this.next.prev;
            }
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public Object next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            AbstractInsnNode abstractInsnNode = this.next;
            this.prev = abstractInsnNode;
            this.next = abstractInsnNode.next;
            this.remove = abstractInsnNode;
            return abstractInsnNode;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            if (this.remove != null) {
                if (this.remove == this.next) {
                    this.next = this.next.next;
                } else {
                    this.prev = this.prev.prev;
                }
                InsnList.this.remove(this.remove);
                this.remove = null;
                return;
            }
            throw new IllegalStateException();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.prev != null;
        }

        @Override // java.util.ListIterator
        public Object previous() {
            AbstractInsnNode abstractInsnNode = this.prev;
            this.next = abstractInsnNode;
            this.prev = abstractInsnNode.prev;
            this.remove = abstractInsnNode;
            return abstractInsnNode;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            if (this.next == null) {
                return InsnList.this.size();
            }
            if (InsnList.this.cache == null) {
                InsnList.this.cache = InsnList.this.toArray();
            }
            return this.next.index;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            if (this.prev == null) {
                return -1;
            }
            if (InsnList.this.cache == null) {
                InsnList.this.cache = InsnList.this.toArray();
            }
            return this.prev.index;
        }

        @Override // java.util.ListIterator
        public void add(Object obj) {
            InsnList.this.insertBefore(this.next, (AbstractInsnNode) obj);
            this.prev = (AbstractInsnNode) obj;
            this.remove = null;
        }

        @Override // java.util.ListIterator
        public void set(Object obj) {
            InsnList.this.set(this.next.prev, (AbstractInsnNode) obj);
            this.prev = (AbstractInsnNode) obj;
        }
    }
}
