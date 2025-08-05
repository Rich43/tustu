package java.rmi.dgc;

import java.io.Serializable;

/* loaded from: rt.jar:java/rmi/dgc/Lease.class */
public final class Lease implements Serializable {
    private VMID vmid;
    private long value;
    private static final long serialVersionUID = -5713411624328831948L;

    public Lease(VMID vmid, long j2) {
        this.vmid = vmid;
        this.value = j2;
    }

    public VMID getVMID() {
        return this.vmid;
    }

    public long getValue() {
        return this.value;
    }
}
