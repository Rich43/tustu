package org.apache.commons.net.pop3;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/pop3/POP3MessageInfo.class */
public final class POP3MessageInfo {
    public int number;
    public int size;
    public String identifier;

    public POP3MessageInfo() {
        this(0, null, 0);
    }

    public POP3MessageInfo(int num, int octets) {
        this(num, null, octets);
    }

    public POP3MessageInfo(int num, String uid) {
        this(num, uid, -1);
    }

    private POP3MessageInfo(int num, String uid, int size) {
        this.number = num;
        this.size = size;
        this.identifier = uid;
    }

    public String toString() {
        return "Number: " + this.number + ". Size: " + this.size + ". Id: " + this.identifier;
    }
}
