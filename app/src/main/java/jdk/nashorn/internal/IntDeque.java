package jdk.nashorn.internal;

/* loaded from: nashorn.jar:jdk/nashorn/internal/IntDeque.class */
public class IntDeque {
    private int[] deque = new int[16];
    private int nextFree = 0;

    public void push(int value) {
        if (this.nextFree == this.deque.length) {
            int[] newDeque = new int[this.nextFree * 2];
            System.arraycopy(this.deque, 0, newDeque, 0, this.nextFree);
            this.deque = newDeque;
        }
        int[] iArr = this.deque;
        int i2 = this.nextFree;
        this.nextFree = i2 + 1;
        iArr[i2] = value;
    }

    public int pop() {
        int[] iArr = this.deque;
        int i2 = this.nextFree - 1;
        this.nextFree = i2;
        return iArr[i2];
    }

    public int peek() {
        return this.deque[this.nextFree - 1];
    }

    public int getAndIncrement() {
        int[] iArr = this.deque;
        int i2 = this.nextFree - 1;
        int i3 = iArr[i2];
        iArr[i2] = i3 + 1;
        return i3;
    }

    public int decrementAndGet() {
        int[] iArr = this.deque;
        int i2 = this.nextFree - 1;
        int i3 = iArr[i2] - 1;
        iArr[i2] = i3;
        return i3;
    }

    public boolean isEmpty() {
        return this.nextFree == 0;
    }
}
