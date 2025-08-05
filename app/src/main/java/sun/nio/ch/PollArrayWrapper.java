package sun.nio.ch;

/* loaded from: rt.jar:sun/nio/ch/PollArrayWrapper.class */
class PollArrayWrapper {
    private AllocatedNativeObject pollArray;
    long pollArrayAddress;
    private static final short FD_OFFSET = 0;
    private static final short EVENT_OFFSET = 4;
    static short SIZE_POLLFD = 8;
    private int size;

    PollArrayWrapper(int i2) {
        this.pollArray = new AllocatedNativeObject(i2 * SIZE_POLLFD, true);
        this.pollArrayAddress = this.pollArray.address();
        this.size = i2;
    }

    void addEntry(int i2, SelectionKeyImpl selectionKeyImpl) {
        putDescriptor(i2, selectionKeyImpl.channel.getFDVal());
    }

    void replaceEntry(PollArrayWrapper pollArrayWrapper, int i2, PollArrayWrapper pollArrayWrapper2, int i3) {
        pollArrayWrapper2.putDescriptor(i3, pollArrayWrapper.getDescriptor(i2));
        pollArrayWrapper2.putEventOps(i3, pollArrayWrapper.getEventOps(i2));
    }

    void grow(int i2) {
        PollArrayWrapper pollArrayWrapper = new PollArrayWrapper(i2);
        for (int i3 = 0; i3 < this.size; i3++) {
            replaceEntry(this, i3, pollArrayWrapper, i3);
        }
        this.pollArray.free();
        this.pollArray = pollArrayWrapper.pollArray;
        this.size = pollArrayWrapper.size;
        this.pollArrayAddress = this.pollArray.address();
    }

    void free() {
        this.pollArray.free();
    }

    void putDescriptor(int i2, int i3) {
        this.pollArray.putInt((SIZE_POLLFD * i2) + 0, i3);
    }

    void putEventOps(int i2, int i3) {
        this.pollArray.putShort((SIZE_POLLFD * i2) + 4, (short) i3);
    }

    int getEventOps(int i2) {
        return this.pollArray.getShort((SIZE_POLLFD * i2) + 4);
    }

    int getDescriptor(int i2) {
        return this.pollArray.getInt((SIZE_POLLFD * i2) + 0);
    }

    void addWakeupSocket(int i2, int i3) {
        putDescriptor(i3, i2);
        putEventOps(i3, Net.POLLIN);
    }
}
