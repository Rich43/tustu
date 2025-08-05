package org.bluecove.socket;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluecove/socket/Credentials.class */
public class Credentials {
    private final int pid;
    private final int uid;
    private final int gid;

    public Credentials(int pid, int uid, int gid) {
        this.pid = pid;
        this.uid = uid;
        this.gid = gid;
    }

    public int getPid() {
        return this.pid;
    }

    public int getUid() {
        return this.uid;
    }

    public int getGid() {
        return this.gid;
    }

    public boolean equals(Object o2) {
        return (o2 instanceof Credentials) && this.pid == ((Credentials) o2).pid && this.uid == ((Credentials) o2).uid && this.gid == ((Credentials) o2).gid;
    }
}
