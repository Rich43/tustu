package org.apache.commons.net.nntp;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/nntp/ThreadContainer.class */
class ThreadContainer {
    Threadable threadable;
    ThreadContainer parent;
    ThreadContainer next;
    ThreadContainer child;

    ThreadContainer() {
    }

    boolean findChild(ThreadContainer target) {
        if (this.child == null) {
            return false;
        }
        if (this.child == target) {
            return true;
        }
        return this.child.findChild(target);
    }

    void flush() {
        if (this.parent != null && this.threadable == null) {
            throw new RuntimeException("no threadable in " + toString());
        }
        this.parent = null;
        if (this.threadable != null) {
            this.threadable.setChild(this.child == null ? null : this.child.threadable);
        }
        if (this.child != null) {
            this.child.flush();
            this.child = null;
        }
        if (this.threadable != null) {
            this.threadable.setNext(this.next == null ? null : this.next.threadable);
        }
        if (this.next != null) {
            this.next.flush();
            this.next = null;
        }
        this.threadable = null;
    }

    void reverseChildren() {
        if (this.child != null) {
            ThreadContainer prev = null;
            ThreadContainer kid = this.child;
            ThreadContainer threadContainer = kid.next;
            while (true) {
                ThreadContainer rest = threadContainer;
                if (kid == null) {
                    break;
                }
                kid.next = prev;
                prev = kid;
                kid = rest;
                threadContainer = rest == null ? null : rest.next;
            }
            this.child = prev;
            ThreadContainer threadContainer2 = this.child;
            while (true) {
                ThreadContainer kid2 = threadContainer2;
                if (kid2 != null) {
                    kid2.reverseChildren();
                    threadContainer2 = kid2.next;
                } else {
                    return;
                }
            }
        }
    }
}
