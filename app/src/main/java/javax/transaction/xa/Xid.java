package javax.transaction.xa;

/* loaded from: rt.jar:javax/transaction/xa/Xid.class */
public interface Xid {
    public static final int MAXGTRIDSIZE = 64;
    public static final int MAXBQUALSIZE = 64;

    int getFormatId();

    byte[] getGlobalTransactionId();

    byte[] getBranchQualifier();
}
