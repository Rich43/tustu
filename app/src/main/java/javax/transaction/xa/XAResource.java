package javax.transaction.xa;

/* loaded from: rt.jar:javax/transaction/xa/XAResource.class */
public interface XAResource {
    public static final int TMENDRSCAN = 8388608;
    public static final int TMFAIL = 536870912;
    public static final int TMJOIN = 2097152;
    public static final int TMNOFLAGS = 0;
    public static final int TMONEPHASE = 1073741824;
    public static final int TMRESUME = 134217728;
    public static final int TMSTARTRSCAN = 16777216;
    public static final int TMSUCCESS = 67108864;
    public static final int TMSUSPEND = 33554432;
    public static final int XA_RDONLY = 3;
    public static final int XA_OK = 0;

    void commit(Xid xid, boolean z2) throws XAException;

    void end(Xid xid, int i2) throws XAException;

    void forget(Xid xid) throws XAException;

    int getTransactionTimeout() throws XAException;

    boolean isSameRM(XAResource xAResource) throws XAException;

    int prepare(Xid xid) throws XAException;

    Xid[] recover(int i2) throws XAException;

    void rollback(Xid xid) throws XAException;

    boolean setTransactionTimeout(int i2) throws XAException;

    void start(Xid xid, int i2) throws XAException;
}
