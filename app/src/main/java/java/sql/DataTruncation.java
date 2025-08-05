package java.sql;

/* loaded from: rt.jar:java/sql/DataTruncation.class */
public class DataTruncation extends SQLWarning {
    private int index;
    private boolean parameter;
    private boolean read;
    private int dataSize;
    private int transferSize;
    private static final long serialVersionUID = 6464298989504059473L;

    public DataTruncation(int i2, boolean z2, boolean z3, int i3, int i4) {
        super("Data truncation", z3 ? "01004" : "22001");
        this.index = i2;
        this.parameter = z2;
        this.read = z3;
        this.dataSize = i3;
        this.transferSize = i4;
    }

    public DataTruncation(int i2, boolean z2, boolean z3, int i3, int i4, Throwable th) {
        super("Data truncation", z3 ? "01004" : "22001", th);
        this.index = i2;
        this.parameter = z2;
        this.read = z3;
        this.dataSize = i3;
        this.transferSize = i4;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean getParameter() {
        return this.parameter;
    }

    public boolean getRead() {
        return this.read;
    }

    public int getDataSize() {
        return this.dataSize;
    }

    public int getTransferSize() {
        return this.transferSize;
    }
}
