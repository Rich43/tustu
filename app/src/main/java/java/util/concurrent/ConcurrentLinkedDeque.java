package java.util.concurrent;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedDeque.class */
public class ConcurrentLinkedDeque<E> extends AbstractCollection<E> implements Deque<E>, Serializable {
    private static final long serialVersionUID = 876323262645176354L;
    private volatile transient Node<E> head;
    private volatile transient Node<E> tail;
    private static final Node<Object> PREV_TERMINATOR = new Node<>();
    private static final Node<Object> NEXT_TERMINATOR;
    private static final int HOPS = 2;
    private static final Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;

    Node<E> prevTerminator() {
        return (Node<E>) PREV_TERMINATOR;
    }

    Node<E> nextTerminator() {
        return (Node<E>) NEXT_TERMINATOR;
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedDeque$Node.class */
    static final class Node<E> {
        volatile Node<E> prev;
        volatile E item;
        volatile Node<E> next;
        private static final Unsafe UNSAFE;
        private static final long prevOffset;
        private static final long itemOffset;
        private static final long nextOffset;

        Node() {
        }

        Node(E e2) {
            UNSAFE.putObject(this, itemOffset, e2);
        }

        boolean casItem(E e2, E e3) {
            return UNSAFE.compareAndSwapObject(this, itemOffset, e2, e3);
        }

        void lazySetNext(Node<E> node) {
            UNSAFE.putOrderedObject(this, nextOffset, node);
        }

        boolean casNext(Node<E> node, Node<E> node2) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, node, node2);
        }

        void lazySetPrev(Node<E> node) {
            UNSAFE.putOrderedObject(this, prevOffset, node);
        }

        boolean casPrev(Node<E> node, Node<E> node2) {
            return UNSAFE.compareAndSwapObject(this, prevOffset, node, node2);
        }

        static {
            try {
                UNSAFE = Unsafe.getUnsafe();
                prevOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField("prev"));
                itemOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField(Constants.NEXT));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    private void linkFirst(E e2) {
        Node<E> node;
        Node<E> node2;
        checkNotNull(e2);
        Node<E> node3 = new Node<>(e2);
        loop0: while (true) {
            node = this.head;
            node2 = node;
            while (true) {
                Node<E> node4 = node2.prev;
                if (node4 != null) {
                    node2 = node4;
                    Node<E> node5 = node4.prev;
                    if (node5 != null) {
                        Node<E> node6 = node;
                        Node<E> node7 = this.head;
                        node = node7;
                        node2 = node6 != node7 ? node : node5;
                    }
                }
                if (node2.next == node2) {
                    break;
                }
                node3.lazySetNext(node2);
                if (node2.casPrev(null, node3)) {
                    break loop0;
                }
            }
        }
        if (node2 != node) {
            casHead(node, node3);
        }
    }

    private void linkLast(E e2) {
        Node<E> node;
        Node<E> node2;
        checkNotNull(e2);
        Node<E> node3 = new Node<>(e2);
        loop0: while (true) {
            node = this.tail;
            node2 = node;
            while (true) {
                Node<E> node4 = node2.next;
                if (node4 != null) {
                    node2 = node4;
                    Node<E> node5 = node4.next;
                    if (node5 != null) {
                        Node<E> node6 = node;
                        Node<E> node7 = this.tail;
                        node = node7;
                        node2 = node6 != node7 ? node : node5;
                    }
                }
                if (node2.prev == node2) {
                    break;
                }
                node3.lazySetPrev(node2);
                if (node2.casNext(null, node3)) {
                    break loop0;
                }
            }
        }
        if (node2 != node) {
            casTail(node, node3);
        }
    }

    void unlink(Node<E> node) {
        Node<E> node2;
        boolean z2;
        Node<E> node3;
        boolean z3;
        Node<E> node4 = node.prev;
        Node<E> node5 = node.next;
        if (node4 == null) {
            unlinkFirst(node, node5);
            return;
        }
        if (node5 == null) {
            unlinkLast(node, node4);
            return;
        }
        int i2 = 1;
        Node<E> node6 = node4;
        while (true) {
            if (node6.item != null) {
                node2 = node6;
                z2 = false;
                break;
            }
            Node<E> node7 = node6.prev;
            if (node7 == null) {
                if (node6.next == node6) {
                    return;
                }
                node2 = node6;
                z2 = true;
            } else {
                if (node6 == node7) {
                    return;
                }
                node6 = node7;
                i2++;
            }
        }
        Node<E> node8 = node5;
        while (true) {
            if (node8.item != null) {
                node3 = node8;
                z3 = false;
                break;
            }
            Node<E> node9 = node8.next;
            if (node9 == null) {
                if (node8.prev == node8) {
                    return;
                }
                node3 = node8;
                z3 = true;
            } else {
                if (node8 == node9) {
                    return;
                }
                node8 = node9;
                i2++;
            }
        }
        if (i2 < 2 && (z2 | z3)) {
            return;
        }
        skipDeletedSuccessors(node2);
        skipDeletedPredecessors(node3);
        if ((z2 | z3) && node2.next == node3 && node3.prev == node2) {
            if (z2) {
                if (node2.prev != null) {
                    return;
                }
            } else if (node2.item == null) {
                return;
            }
            if (z3) {
                if (node3.next != null) {
                    return;
                }
            } else if (node3.item == null) {
                return;
            }
            updateHead();
            updateTail();
            node.lazySetPrev(z2 ? prevTerminator() : node);
            node.lazySetNext(z3 ? nextTerminator() : node);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0023, code lost:
    
        if (r8.prev == r8) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x002d, code lost:
    
        if (r5.casNext(r6, r8) == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0030, code lost:
    
        skipDeletedPredecessors(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x003a, code lost:
    
        if (r5.prev != null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0042, code lost:
    
        if (r8.next == null) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004a, code lost:
    
        if (r8.item == null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0053, code lost:
    
        if (r8.prev != r5) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0056, code lost:
    
        updateHead();
        updateTail();
        r7.lazySetNext(r7);
        r7.lazySetPrev(prevTerminator());
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x006b, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0019, code lost:
    
        if (r7 == null) goto L30;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void unlinkFirst(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r5, java.util.concurrent.ConcurrentLinkedDeque.Node<E> r6) {
        /*
            r4 = this;
            r0 = 0
            r7 = r0
            r0 = r6
            r8 = r0
        L5:
            r0 = r8
            E r0 = r0.item
            if (r0 != 0) goto L18
            r0 = r8
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            r1 = r0
            r9 = r1
            if (r0 != 0) goto L6c
        L18:
            r0 = r7
            if (r0 == 0) goto L6b
            r0 = r8
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            r1 = r8
            if (r0 == r1) goto L6b
            r0 = r5
            r1 = r6
            r2 = r8
            boolean r0 = r0.casNext(r1, r2)
            if (r0 == 0) goto L6b
            r0 = r4
            r1 = r8
            r0.skipDeletedPredecessors(r1)
            r0 = r5
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            if (r0 != 0) goto L6b
            r0 = r8
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            if (r0 == 0) goto L4d
            r0 = r8
            E r0 = r0.item
            if (r0 == 0) goto L6b
        L4d:
            r0 = r8
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            r1 = r5
            if (r0 != r1) goto L6b
            r0 = r4
            r0.updateHead()
            r0 = r4
            r0.updateTail()
            r0 = r7
            r1 = r7
            r0.lazySetNext(r1)
            r0 = r7
            r1 = r4
            java.util.concurrent.ConcurrentLinkedDeque$Node r1 = r1.prevTerminator()
            r0.lazySetPrev(r1)
        L6b:
            return
        L6c:
            r0 = r8
            r1 = r9
            if (r0 != r1) goto L74
            return
        L74:
            r0 = r8
            r7 = r0
            r0 = r9
            r8 = r0
            goto L5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.unlinkFirst(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0023, code lost:
    
        if (r8.next == r8) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x002d, code lost:
    
        if (r5.casPrev(r6, r8) == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0030, code lost:
    
        skipDeletedSuccessors(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x003a, code lost:
    
        if (r5.next != null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0042, code lost:
    
        if (r8.prev == null) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004a, code lost:
    
        if (r8.item == null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0053, code lost:
    
        if (r8.next != r5) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0056, code lost:
    
        updateHead();
        updateTail();
        r7.lazySetPrev(r7);
        r7.lazySetNext(nextTerminator());
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x006b, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0019, code lost:
    
        if (r7 == null) goto L30;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void unlinkLast(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r5, java.util.concurrent.ConcurrentLinkedDeque.Node<E> r6) {
        /*
            r4 = this;
            r0 = 0
            r7 = r0
            r0 = r6
            r8 = r0
        L5:
            r0 = r8
            E r0 = r0.item
            if (r0 != 0) goto L18
            r0 = r8
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            r1 = r0
            r9 = r1
            if (r0 != 0) goto L6c
        L18:
            r0 = r7
            if (r0 == 0) goto L6b
            r0 = r8
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            r1 = r8
            if (r0 == r1) goto L6b
            r0 = r5
            r1 = r6
            r2 = r8
            boolean r0 = r0.casPrev(r1, r2)
            if (r0 == 0) goto L6b
            r0 = r4
            r1 = r8
            r0.skipDeletedSuccessors(r1)
            r0 = r5
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            if (r0 != 0) goto L6b
            r0 = r8
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            if (r0 == 0) goto L4d
            r0 = r8
            E r0 = r0.item
            if (r0 == 0) goto L6b
        L4d:
            r0 = r8
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            r1 = r5
            if (r0 != r1) goto L6b
            r0 = r4
            r0.updateHead()
            r0 = r4
            r0.updateTail()
            r0 = r7
            r1 = r7
            r0.lazySetPrev(r1)
            r0 = r7
            r1 = r4
            java.util.concurrent.ConcurrentLinkedDeque$Node r1 = r1.nextTerminator()
            r0.lazySetNext(r1)
        L6b:
            return
        L6c:
            r0 = r8
            r1 = r9
            if (r0 != r1) goto L74
            return
        L74:
            r0 = r8
            r7 = r0
            r0 = r9
            r8 = r0
            goto L5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.unlinkLast(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002f, code lost:
    
        if (casHead(r0, r6) == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0032, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void updateHead() {
        /*
            r4 = this;
        L0:
            r0 = r4
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.head
            r1 = r0
            r5 = r1
            E r0 = r0.item
            if (r0 != 0) goto L43
            r0 = r5
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            r1 = r0
            r6 = r1
            if (r0 == 0) goto L43
        L15:
            r0 = r6
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            r1 = r0
            r7 = r1
            if (r0 == 0) goto L29
            r0 = r7
            r1 = r0
            r6 = r1
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            r1 = r0
            r7 = r1
            if (r0 != 0) goto L33
        L29:
            r0 = r4
            r1 = r5
            r2 = r6
            boolean r0 = r0.casHead(r1, r2)
            if (r0 == 0) goto L0
            return
        L33:
            r0 = r5
            r1 = r4
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r1 = r1.head
            if (r0 == r1) goto L3e
            goto L0
        L3e:
            r0 = r7
            r6 = r0
            goto L15
        L43:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.updateHead():void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002f, code lost:
    
        if (casTail(r0, r6) == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0032, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void updateTail() {
        /*
            r4 = this;
        L0:
            r0 = r4
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.tail
            r1 = r0
            r5 = r1
            E r0 = r0.item
            if (r0 != 0) goto L43
            r0 = r5
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            r1 = r0
            r6 = r1
            if (r0 == 0) goto L43
        L15:
            r0 = r6
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            r1 = r0
            r7 = r1
            if (r0 == 0) goto L29
            r0 = r7
            r1 = r0
            r6 = r1
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            r1 = r0
            r7 = r1
            if (r0 != 0) goto L33
        L29:
            r0 = r4
            r1 = r5
            r2 = r6
            boolean r0 = r0.casTail(r1, r2)
            if (r0 == 0) goto L0
            return
        L33:
            r0 = r5
            r1 = r4
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r1 = r1.tail
            if (r0 == r1) goto L3e
            goto L0
        L3e:
            r0 = r7
            r6 = r0
            goto L15
        L43:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.updateTail():void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0038, code lost:
    
        if (r0 == r7) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0041, code lost:
    
        if (r5.casPrev(r0, r7) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0044, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void skipDeletedPredecessors(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r5) {
        /*
            r4 = this;
        L0:
            r0 = r5
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            r6 = r0
            r0 = r6
            r7 = r0
        L7:
            r0 = r7
            E r0 = r0.item
            if (r0 == 0) goto L11
            goto L36
        L11:
            r0 = r7
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            r8 = r0
            r0 = r8
            if (r0 != 0) goto L27
            r0 = r7
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            r1 = r7
            if (r0 != r1) goto L36
            goto L45
        L27:
            r0 = r7
            r1 = r8
            if (r0 != r1) goto L30
            goto L45
        L30:
            r0 = r8
            r7 = r0
            goto L7
        L36:
            r0 = r6
            r1 = r7
            if (r0 == r1) goto L44
            r0 = r5
            r1 = r6
            r2 = r7
            boolean r0 = r0.casPrev(r1, r2)
            if (r0 == 0) goto L45
        L44:
            return
        L45:
            r0 = r5
            E r0 = r0.item
            if (r0 != 0) goto L0
            r0 = r5
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            if (r0 == 0) goto L0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.skipDeletedPredecessors(java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0038, code lost:
    
        if (r0 == r7) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0041, code lost:
    
        if (r5.casNext(r0, r7) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0044, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void skipDeletedSuccessors(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r5) {
        /*
            r4 = this;
        L0:
            r0 = r5
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            r6 = r0
            r0 = r6
            r7 = r0
        L7:
            r0 = r7
            E r0 = r0.item
            if (r0 == 0) goto L11
            goto L36
        L11:
            r0 = r7
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.next
            r8 = r0
            r0 = r8
            if (r0 != 0) goto L27
            r0 = r7
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            r1 = r7
            if (r0 != r1) goto L36
            goto L45
        L27:
            r0 = r7
            r1 = r8
            if (r0 != r1) goto L30
            goto L45
        L30:
            r0 = r8
            r7 = r0
            goto L7
        L36:
            r0 = r6
            r1 = r7
            if (r0 == r1) goto L44
            r0 = r5
            r1 = r6
            r2 = r7
            boolean r0 = r0.casNext(r1, r2)
            if (r0 == 0) goto L45
        L44:
            return
        L45:
            r0 = r5
            E r0 = r0.item
            if (r0 != 0) goto L0
            r0 = r5
            java.util.concurrent.ConcurrentLinkedDeque$Node<E> r0 = r0.prev
            if (r0 == 0) goto L0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors(java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    final Node<E> succ(Node<E> node) {
        Node<E> node2 = node.next;
        return node == node2 ? first() : node2;
    }

    final Node<E> pred(Node<E> node) {
        Node<E> node2 = node.prev;
        return node == node2 ? last() : node2;
    }

    Node<E> first() {
        Node<E> node;
        Node<E> node2;
        do {
            node = this.head;
            Node<E> node3 = node;
            while (true) {
                node2 = node3;
                Node<E> node4 = node2.prev;
                if (node4 == null) {
                    break;
                }
                node2 = node4;
                Node<E> node5 = node4.prev;
                if (node5 == null) {
                    break;
                }
                Node<E> node6 = node;
                Node<E> node7 = this.head;
                node = node7;
                node3 = node6 != node7 ? node : node5;
            }
            if (node2 == node) {
                break;
            }
        } while (!casHead(node, node2));
        return node2;
    }

    Node<E> last() {
        Node<E> node;
        Node<E> node2;
        do {
            node = this.tail;
            Node<E> node3 = node;
            while (true) {
                node2 = node3;
                Node<E> node4 = node2.next;
                if (node4 == null) {
                    break;
                }
                node2 = node4;
                Node<E> node5 = node4.next;
                if (node5 == null) {
                    break;
                }
                Node<E> node6 = node;
                Node<E> node7 = this.tail;
                node = node7;
                node3 = node6 != node7 ? node : node5;
            }
            if (node2 == node) {
                break;
            }
        } while (!casTail(node, node2));
        return node2;
    }

    private static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    private E screenNullResult(E e2) {
        if (e2 == null) {
            throw new NoSuchElementException();
        }
        return e2;
    }

    private ArrayList<E> toArrayList() {
        ArrayList<E> arrayList = new ArrayList<>();
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node != null) {
                E e2 = node.item;
                if (e2 != null) {
                    arrayList.add(e2);
                }
                nodeFirst = succ(node);
            } else {
                return arrayList;
            }
        }
    }

    public ConcurrentLinkedDeque() {
        Node<E> node = new Node<>(null);
        this.tail = node;
        this.head = node;
    }

    public ConcurrentLinkedDeque(Collection<? extends E> collection) {
        Node<E> node = null;
        Node<E> node2 = null;
        for (E e2 : collection) {
            checkNotNull(e2);
            Node<E> node3 = new Node<>(e2);
            if (node == null) {
                node2 = node3;
                node = node3;
            } else {
                node2.lazySetNext(node3);
                node3.lazySetPrev(node2);
                node2 = node3;
            }
        }
        initHeadTail(node, node2);
    }

    private void initHeadTail(Node<E> node, Node<E> node2) {
        if (node == node2) {
            if (node == null) {
                Node<E> node3 = new Node<>(null);
                node2 = node3;
                node = node3;
            } else {
                Node<E> node4 = new Node<>(null);
                node2.lazySetNext(node4);
                node4.lazySetPrev(node2);
                node2 = node4;
            }
        }
        this.head = node;
        this.tail = node2;
    }

    @Override // java.util.Deque
    public void addFirst(E e2) {
        linkFirst(e2);
    }

    @Override // java.util.Deque
    public void addLast(E e2) {
        linkLast(e2);
    }

    @Override // java.util.Deque
    public boolean offerFirst(E e2) {
        linkFirst(e2);
        return true;
    }

    @Override // java.util.Deque
    public boolean offerLast(E e2) {
        linkLast(e2);
        return true;
    }

    @Override // java.util.Deque
    public E peekFirst() {
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node != null) {
                E e2 = node.item;
                if (e2 == null) {
                    nodeFirst = succ(node);
                } else {
                    return e2;
                }
            } else {
                return null;
            }
        }
    }

    @Override // java.util.Deque
    public E peekLast() {
        Node<E> nodeLast = last();
        while (true) {
            Node<E> node = nodeLast;
            if (node != null) {
                E e2 = node.item;
                if (e2 == null) {
                    nodeLast = pred(node);
                } else {
                    return e2;
                }
            } else {
                return null;
            }
        }
    }

    @Override // java.util.Deque
    public E getFirst() {
        return screenNullResult(peekFirst());
    }

    @Override // java.util.Deque
    public E getLast() {
        return screenNullResult(peekLast());
    }

    @Override // java.util.Deque
    public E pollFirst() {
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node != null) {
                E e2 = node.item;
                if (e2 == null || !node.casItem(e2, null)) {
                    nodeFirst = succ(node);
                } else {
                    unlink(node);
                    return e2;
                }
            } else {
                return null;
            }
        }
    }

    @Override // java.util.Deque
    public E pollLast() {
        Node<E> nodeLast = last();
        while (true) {
            Node<E> node = nodeLast;
            if (node != null) {
                E e2 = node.item;
                if (e2 == null || !node.casItem(e2, null)) {
                    nodeLast = pred(node);
                } else {
                    unlink(node);
                    return e2;
                }
            } else {
                return null;
            }
        }
    }

    @Override // java.util.Deque
    public E removeFirst() {
        return screenNullResult(pollFirst());
    }

    @Override // java.util.Deque
    public E removeLast() {
        return screenNullResult(pollLast());
    }

    @Override // java.util.Deque, java.util.Queue
    public boolean offer(E e2) {
        return offerLast(e2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return offerLast(e2);
    }

    @Override // java.util.Deque, java.util.Queue
    public E poll() {
        return pollFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E peek() {
        return peekFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E remove() {
        return removeFirst();
    }

    @Override // java.util.Deque
    public E pop() {
        return removeFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E element() {
        return getFirst();
    }

    @Override // java.util.Deque
    public void push(E e2) {
        addFirst(e2);
    }

    @Override // java.util.Deque
    public boolean removeFirstOccurrence(Object obj) {
        checkNotNull(obj);
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node != null) {
                E e2 = node.item;
                if (e2 == null || !obj.equals(e2) || !node.casItem(e2, null)) {
                    nodeFirst = succ(node);
                } else {
                    unlink(node);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.Deque
    public boolean removeLastOccurrence(Object obj) {
        checkNotNull(obj);
        Node<E> nodeLast = last();
        while (true) {
            Node<E> node = nodeLast;
            if (node != null) {
                E e2 = node.item;
                if (e2 == null || !obj.equals(e2) || !node.casItem(e2, null)) {
                    nodeLast = pred(node);
                } else {
                    unlink(node);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node != null) {
                E e2 = node.item;
                if (e2 == null || !obj.equals(e2)) {
                    nodeFirst = succ(node);
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return peekFirst() == null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        int i2 = 0;
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node == null) {
                break;
            }
            if (node.item != null) {
                i2++;
                if (i2 == Integer.MAX_VALUE) {
                    break;
                }
            }
            nodeFirst = succ(node);
        }
        return i2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return removeFirstOccurrence(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        Node<E> node;
        if (collection == this) {
            throw new IllegalArgumentException();
        }
        Node<E> node2 = null;
        Node<E> node3 = null;
        for (E e2 : collection) {
            checkNotNull(e2);
            Node<E> node4 = new Node<>(e2);
            if (node2 == null) {
                node3 = node4;
                node2 = node4;
            } else {
                node3.lazySetNext(node4);
                node4.lazySetPrev(node3);
                node3 = node4;
            }
        }
        if (node2 == null) {
            return false;
        }
        loop1: while (true) {
            node = this.tail;
            Node<E> node5 = node;
            while (true) {
                Node<E> node6 = node5.next;
                if (node6 != null) {
                    node5 = node6;
                    Node<E> node7 = node6.next;
                    if (node7 != null) {
                        Node<E> node8 = node;
                        Node<E> node9 = this.tail;
                        node = node9;
                        node5 = node8 != node9 ? node : node7;
                    }
                }
                if (node5.prev == node5) {
                    break;
                }
                node2.lazySetPrev(node5);
                if (node5.casNext(null, node2)) {
                    break loop1;
                }
            }
        }
        if (!casTail(node, node3)) {
            Node<E> node10 = this.tail;
            if (node3.next == null) {
                casTail(node10, node3);
                return true;
            }
            return true;
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        while (pollFirst() != null) {
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return toArrayList().toArray();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) toArrayList().toArray(tArr);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override // java.util.Deque
    public Iterator<E> descendingIterator() {
        return new DescendingItr();
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedDeque$AbstractItr.class */
    private abstract class AbstractItr implements Iterator<E> {
        private Node<E> nextNode;
        private E nextItem;
        private Node<E> lastRet;

        abstract Node<E> startNode();

        abstract Node<E> nextNode(Node<E> node);

        AbstractItr() {
            advance();
        }

        private void advance() {
            this.lastRet = this.nextNode;
            Node<E> nodeStartNode = this.nextNode == null ? startNode() : nextNode(this.nextNode);
            while (true) {
                Node<E> node = nodeStartNode;
                if (node == null) {
                    this.nextNode = null;
                    this.nextItem = null;
                    return;
                }
                E e2 = node.item;
                if (e2 == null) {
                    nodeStartNode = nextNode(node);
                } else {
                    this.nextNode = node;
                    this.nextItem = e2;
                    return;
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextItem != null;
        }

        @Override // java.util.Iterator
        public E next() {
            E e2 = this.nextItem;
            if (e2 == null) {
                throw new NoSuchElementException();
            }
            advance();
            return e2;
        }

        @Override // java.util.Iterator
        public void remove() {
            Node<E> node = this.lastRet;
            if (node == null) {
                throw new IllegalStateException();
            }
            node.item = null;
            ConcurrentLinkedDeque.this.unlink(node);
            this.lastRet = null;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedDeque$Itr.class */
    private class Itr extends ConcurrentLinkedDeque<E>.AbstractItr {
        private Itr() {
            super();
        }

        @Override // java.util.concurrent.ConcurrentLinkedDeque.AbstractItr
        Node<E> startNode() {
            return ConcurrentLinkedDeque.this.first();
        }

        @Override // java.util.concurrent.ConcurrentLinkedDeque.AbstractItr
        Node<E> nextNode(Node<E> node) {
            return ConcurrentLinkedDeque.this.succ(node);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedDeque$DescendingItr.class */
    private class DescendingItr extends ConcurrentLinkedDeque<E>.AbstractItr {
        private DescendingItr() {
            super();
        }

        @Override // java.util.concurrent.ConcurrentLinkedDeque.AbstractItr
        Node<E> startNode() {
            return ConcurrentLinkedDeque.this.last();
        }

        @Override // java.util.concurrent.ConcurrentLinkedDeque.AbstractItr
        Node<E> nextNode(Node<E> node) {
            return ConcurrentLinkedDeque.this.pred(node);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedDeque$CLDSpliterator.class */
    static final class CLDSpliterator<E> implements Spliterator<E> {
        static final int MAX_BATCH = 33554432;
        final ConcurrentLinkedDeque<E> queue;
        Node<E> current;
        int batch;
        boolean exhausted;

        CLDSpliterator(ConcurrentLinkedDeque<E> concurrentLinkedDeque) {
            this.queue = concurrentLinkedDeque;
        }

        @Override // java.util.Spliterator
        public Spliterator<E> trySplit() {
            ConcurrentLinkedDeque<E> concurrentLinkedDeque = this.queue;
            int i2 = this.batch;
            int i3 = i2 <= 0 ? 1 : i2 >= 33554432 ? 33554432 : i2 + 1;
            if (!this.exhausted) {
                Node<E> node = this.current;
                Node<E> nodeFirst = node;
                if (node == null) {
                    Node<E> nodeFirst2 = concurrentLinkedDeque.first();
                    nodeFirst = nodeFirst2;
                    if (nodeFirst2 == null) {
                        return null;
                    }
                }
                if (nodeFirst.item == null) {
                    Node<E> node2 = nodeFirst;
                    Node<E> node3 = nodeFirst.next;
                    nodeFirst = node3;
                    if (node2 == node3) {
                        Node<E> nodeFirst3 = concurrentLinkedDeque.first();
                        nodeFirst = nodeFirst3;
                        this.current = nodeFirst3;
                    }
                }
                if (nodeFirst != null && nodeFirst.next != null) {
                    Object[] objArr = new Object[i3];
                    int i4 = 0;
                    do {
                        E e2 = nodeFirst.item;
                        objArr[i4] = e2;
                        if (e2 != null) {
                            i4++;
                        }
                        Node<E> node4 = nodeFirst;
                        Node<E> node5 = nodeFirst.next;
                        nodeFirst = node5;
                        if (node4 == node5) {
                            nodeFirst = concurrentLinkedDeque.first();
                        }
                        if (nodeFirst == null) {
                            break;
                        }
                    } while (i4 < i3);
                    Node<E> node6 = nodeFirst;
                    this.current = node6;
                    if (node6 == null) {
                        this.exhausted = true;
                    }
                    if (i4 > 0) {
                        this.batch = i4;
                        return Spliterators.spliterator(objArr, 0, i4, 4368);
                    }
                    return null;
                }
                return null;
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            ConcurrentLinkedDeque<E> concurrentLinkedDeque = this.queue;
            if (!this.exhausted) {
                Node<E> node = this.current;
                Node<E> nodeFirst = node;
                if (node == null) {
                    Node<E> nodeFirst2 = concurrentLinkedDeque.first();
                    nodeFirst = nodeFirst2;
                    if (nodeFirst2 == null) {
                        return;
                    }
                }
                this.exhausted = true;
                do {
                    E e2 = nodeFirst.item;
                    Node<E> node2 = nodeFirst;
                    Node<E> node3 = nodeFirst.next;
                    nodeFirst = node3;
                    if (node2 == node3) {
                        nodeFirst = concurrentLinkedDeque.first();
                    }
                    if (e2 != null) {
                        consumer.accept(e2);
                    }
                } while (nodeFirst != null);
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            E e2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            ConcurrentLinkedDeque<E> concurrentLinkedDeque = this.queue;
            if (!this.exhausted) {
                Node<E> node = this.current;
                Node<E> nodeFirst = node;
                if (node == null) {
                    Node<E> nodeFirst2 = concurrentLinkedDeque.first();
                    nodeFirst = nodeFirst2;
                    if (nodeFirst2 == null) {
                        return false;
                    }
                }
                do {
                    e2 = nodeFirst.item;
                    Node<E> node2 = nodeFirst;
                    Node<E> node3 = nodeFirst.next;
                    nodeFirst = node3;
                    if (node2 == node3) {
                        nodeFirst = concurrentLinkedDeque.first();
                    }
                    if (e2 != null) {
                        break;
                    }
                } while (nodeFirst != null);
                Node<E> node4 = nodeFirst;
                this.current = node4;
                if (node4 == null) {
                    this.exhausted = true;
                }
                if (e2 != null) {
                    consumer.accept(e2);
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 4368;
        }
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return new CLDSpliterator(this);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node != null) {
                E e2 = node.item;
                if (e2 != null) {
                    objectOutputStream.writeObject(e2);
                }
                nodeFirst = succ(node);
            } else {
                objectOutputStream.writeObject(null);
                return;
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Node<E> node = null;
        Node<E> node2 = null;
        while (true) {
            Object object = objectInputStream.readObject();
            if (object != null) {
                Node<E> node3 = new Node<>(object);
                if (node == null) {
                    node2 = node3;
                    node = node3;
                } else {
                    node2.lazySetNext(node3);
                    node3.lazySetPrev(node2);
                    node2 = node3;
                }
            } else {
                initHeadTail(node, node2);
                return;
            }
        }
    }

    private boolean casHead(Node<E> node, Node<E> node2) {
        return UNSAFE.compareAndSwapObject(this, headOffset, node, node2);
    }

    private boolean casTail(Node<E> node, Node<E> node2) {
        return UNSAFE.compareAndSwapObject(this, tailOffset, node, node2);
    }

    static {
        PREV_TERMINATOR.next = (Node<E>) PREV_TERMINATOR;
        NEXT_TERMINATOR = new Node<>();
        NEXT_TERMINATOR.prev = (Node<E>) NEXT_TERMINATOR;
        try {
            UNSAFE = Unsafe.getUnsafe();
            headOffset = UNSAFE.objectFieldOffset(ConcurrentLinkedDeque.class.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset(ConcurrentLinkedDeque.class.getDeclaredField("tail"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }
}
