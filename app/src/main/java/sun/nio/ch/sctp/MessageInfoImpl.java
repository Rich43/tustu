package sun.nio.ch.sctp;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import java.net.SocketAddress;

/* loaded from: rt.jar:sun/nio/ch/sctp/MessageInfoImpl.class */
public class MessageInfoImpl extends MessageInfo {
    private final SocketAddress address;
    private final int bytes;
    private Association association;
    private int assocId;
    private int streamNumber;
    private boolean complete;
    private boolean unordered;
    private long timeToLive;
    private int ppid;

    public MessageInfoImpl(Association association, SocketAddress socketAddress, int i2) {
        this.complete = true;
        this.association = association;
        this.address = socketAddress;
        this.streamNumber = i2;
        this.bytes = 0;
    }

    private MessageInfoImpl(int i2, SocketAddress socketAddress, int i3, int i4, boolean z2, boolean z3, int i5) {
        this.complete = true;
        this.assocId = i2;
        this.address = socketAddress;
        this.bytes = i3;
        this.streamNumber = i4;
        this.complete = z2;
        this.unordered = z3;
        this.ppid = i5;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public Association association() {
        return this.association;
    }

    void setAssociation(Association association) {
        this.association = association;
    }

    int associationID() {
        return this.assocId;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public SocketAddress address() {
        return this.address;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public int bytes() {
        return this.bytes;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public int streamNumber() {
        return this.streamNumber;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public MessageInfo streamNumber(int i2) {
        if (i2 < 0 || i2 > 65536) {
            throw new IllegalArgumentException("Invalid stream number");
        }
        this.streamNumber = i2;
        return this;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public int payloadProtocolID() {
        return this.ppid;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public MessageInfo payloadProtocolID(int i2) {
        this.ppid = i2;
        return this;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public boolean isComplete() {
        return this.complete;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public MessageInfo complete(boolean z2) {
        this.complete = z2;
        return this;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public boolean isUnordered() {
        return this.unordered;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public MessageInfo unordered(boolean z2) {
        this.unordered = z2;
        return this;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public long timeToLive() {
        return this.timeToLive;
    }

    @Override // com.sun.nio.sctp.MessageInfo
    public MessageInfo timeToLive(long j2) {
        this.timeToLive = j2;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("[Address: ").append((Object) this.address).append(", Association: ").append((Object) this.association).append(", Assoc ID: ").append(this.assocId).append(", Bytes: ").append(this.bytes).append(", Stream Number: ").append(this.streamNumber).append(", Complete: ").append(this.complete).append(", isUnordered: ").append(this.unordered).append("]");
        return sb.toString();
    }
}
