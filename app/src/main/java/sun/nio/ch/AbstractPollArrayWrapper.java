package sun.nio.ch;

/* loaded from: rt.jar:sun/nio/ch/AbstractPollArrayWrapper.class */
public abstract class AbstractPollArrayWrapper {
    static final short SIZE_POLLFD = 8;
    static final short FD_OFFSET = 0;
    static final short EVENT_OFFSET = 4;
    static final short REVENT_OFFSET = 6;
    protected AllocatedNativeObject pollArray;
    protected int totalChannels = 0;
    protected long pollArrayAddress;

    int getEventOps(int i2) {
        return this.pollArray.getShort((8 * i2) + 4);
    }

    int getReventOps(int i2) {
        return this.pollArray.getShort((8 * i2) + 6);
    }

    int getDescriptor(int i2) {
        return this.pollArray.getInt((8 * i2) + 0);
    }

    void putEventOps(int i2, int i3) {
        this.pollArray.putShort((8 * i2) + 4, (short) i3);
    }

    void putReventOps(int i2, int i3) {
        this.pollArray.putShort((8 * i2) + 6, (short) i3);
    }

    void putDescriptor(int i2, int i3) {
        this.pollArray.putInt((8 * i2) + 0, i3);
    }
}
