package org.ietf.jgss;

import java.net.InetAddress;
import java.util.Arrays;

/* loaded from: rt.jar:org/ietf/jgss/ChannelBinding.class */
public class ChannelBinding {
    private InetAddress initiator;
    private InetAddress acceptor;
    private byte[] appData;

    public ChannelBinding(InetAddress inetAddress, InetAddress inetAddress2, byte[] bArr) {
        this.initiator = inetAddress;
        this.acceptor = inetAddress2;
        if (bArr != null) {
            this.appData = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.appData, 0, bArr.length);
        }
    }

    public ChannelBinding(byte[] bArr) {
        this(null, null, bArr);
    }

    public InetAddress getInitiatorAddress() {
        return this.initiator;
    }

    public InetAddress getAcceptorAddress() {
        return this.acceptor;
    }

    public byte[] getApplicationData() {
        if (this.appData == null) {
            return null;
        }
        byte[] bArr = new byte[this.appData.length];
        System.arraycopy(this.appData, 0, bArr, 0, this.appData.length);
        return bArr;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChannelBinding)) {
            return false;
        }
        ChannelBinding channelBinding = (ChannelBinding) obj;
        if (this.initiator != null && channelBinding.initiator == null) {
            return false;
        }
        if (this.initiator == null && channelBinding.initiator != null) {
            return false;
        }
        if (this.initiator != null && !this.initiator.equals(channelBinding.initiator)) {
            return false;
        }
        if (this.acceptor != null && channelBinding.acceptor == null) {
            return false;
        }
        if (this.acceptor == null && channelBinding.acceptor != null) {
            return false;
        }
        if (this.acceptor != null && !this.acceptor.equals(channelBinding.acceptor)) {
            return false;
        }
        return Arrays.equals(this.appData, channelBinding.appData);
    }

    public int hashCode() {
        if (this.initiator != null) {
            return this.initiator.hashCode();
        }
        if (this.acceptor != null) {
            return this.acceptor.hashCode();
        }
        if (this.appData != null) {
            return new String(this.appData).hashCode();
        }
        return 1;
    }
}
