package sun.nio.ch;

/* loaded from: rt.jar:sun/nio/ch/NativeThreadSet.class */
class NativeThreadSet {
    private long[] elts;
    private int used = 0;
    private boolean waitingToEmpty;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NativeThreadSet.class.desiredAssertionStatus();
    }

    NativeThreadSet(int i2) {
        this.elts = new long[i2];
    }

    int add() {
        long jCurrent = NativeThread.current();
        if (jCurrent == 0) {
            jCurrent = -1;
        }
        synchronized (this) {
            int i2 = 0;
            if (this.used >= this.elts.length) {
                int length = this.elts.length;
                long[] jArr = new long[length * 2];
                System.arraycopy(this.elts, 0, jArr, 0, length);
                this.elts = jArr;
                i2 = length;
            }
            for (int i3 = i2; i3 < this.elts.length; i3++) {
                if (this.elts[i3] == 0) {
                    this.elts[i3] = jCurrent;
                    this.used++;
                    return i3;
                }
            }
            if ($assertionsDisabled) {
                return -1;
            }
            throw new AssertionError();
        }
    }

    void remove(int i2) {
        synchronized (this) {
            this.elts[i2] = 0;
            this.used--;
            if (this.used == 0 && this.waitingToEmpty) {
                notifyAll();
            }
        }
    }

    synchronized void signalAndWait() {
        boolean z2 = false;
        while (this.used > 0) {
            int i2 = this.used;
            int length = this.elts.length;
            for (int i3 = 0; i3 < length; i3++) {
                long j2 = this.elts[i3];
                if (j2 != 0) {
                    if (j2 != -1) {
                        NativeThread.signal(j2);
                    }
                    i2--;
                    if (i2 == 0) {
                        break;
                    }
                }
            }
            this.waitingToEmpty = true;
            try {
                wait(50L);
                this.waitingToEmpty = false;
            } catch (InterruptedException e2) {
                z2 = true;
                this.waitingToEmpty = false;
            } catch (Throwable th) {
                this.waitingToEmpty = false;
                throw th;
            }
        }
        if (z2) {
            Thread.currentThread().interrupt();
        }
    }
}
