package java.sql;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/* loaded from: rt.jar:java/sql/BatchUpdateException.class */
public class BatchUpdateException extends SQLException {
    private int[] updateCounts;
    private long[] longUpdateCounts;
    private static final long serialVersionUID = 5977529877145521757L;

    public BatchUpdateException(String str, String str2, int i2, int[] iArr) {
        super(str, str2, i2);
        this.updateCounts = iArr == null ? null : Arrays.copyOf(iArr, iArr.length);
        this.longUpdateCounts = iArr == null ? null : copyUpdateCount(iArr);
    }

    public BatchUpdateException(String str, String str2, int[] iArr) {
        this(str, str2, 0, iArr);
    }

    public BatchUpdateException(String str, int[] iArr) {
        this(str, (String) null, 0, iArr);
    }

    public BatchUpdateException(int[] iArr) {
        this((String) null, (String) null, 0, iArr);
    }

    public BatchUpdateException() {
        this((String) null, (String) null, 0, (int[]) null);
    }

    public BatchUpdateException(Throwable th) {
        this(th == null ? null : th.toString(), (String) null, 0, (int[]) null, th);
    }

    public BatchUpdateException(int[] iArr, Throwable th) {
        this(th == null ? null : th.toString(), (String) null, 0, iArr, th);
    }

    public BatchUpdateException(String str, int[] iArr, Throwable th) {
        this(str, (String) null, 0, iArr, th);
    }

    public BatchUpdateException(String str, String str2, int[] iArr, Throwable th) {
        this(str, str2, 0, iArr, th);
    }

    public BatchUpdateException(String str, String str2, int i2, int[] iArr, Throwable th) {
        super(str, str2, i2, th);
        this.updateCounts = iArr == null ? null : Arrays.copyOf(iArr, iArr.length);
        this.longUpdateCounts = iArr == null ? null : copyUpdateCount(iArr);
    }

    public int[] getUpdateCounts() {
        if (this.updateCounts == null) {
            return null;
        }
        return Arrays.copyOf(this.updateCounts, this.updateCounts.length);
    }

    public BatchUpdateException(String str, String str2, int i2, long[] jArr, Throwable th) {
        super(str, str2, i2, th);
        this.longUpdateCounts = jArr == null ? null : Arrays.copyOf(jArr, jArr.length);
        this.updateCounts = this.longUpdateCounts == null ? null : copyUpdateCount(this.longUpdateCounts);
    }

    public long[] getLargeUpdateCounts() {
        if (this.longUpdateCounts == null) {
            return null;
        }
        return Arrays.copyOf(this.longUpdateCounts, this.longUpdateCounts.length);
    }

    private static long[] copyUpdateCount(int[] iArr) {
        long[] jArr = new long[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            jArr[i2] = iArr[i2];
        }
        return jArr;
    }

    private static int[] copyUpdateCount(long[] jArr) {
        int[] iArr = new int[jArr.length];
        for (int i2 = 0; i2 < jArr.length; i2++) {
            iArr[i2] = (int) jArr[i2];
        }
        return iArr;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        int[] iArr = (int[]) fields.get("updateCounts", (Object) null);
        long[] jArr = (long[]) fields.get("longUpdateCounts", (Object) null);
        if (iArr != null && jArr != null && iArr.length != jArr.length) {
            throw new InvalidObjectException("update counts are not the expected size");
        }
        if (iArr != null) {
            this.updateCounts = (int[]) iArr.clone();
        }
        if (jArr != null) {
            this.longUpdateCounts = (long[]) jArr.clone();
        }
        if (this.updateCounts == null && this.longUpdateCounts != null) {
            this.updateCounts = copyUpdateCount(this.longUpdateCounts);
        }
        if (this.longUpdateCounts == null && this.updateCounts != null) {
            this.longUpdateCounts = copyUpdateCount(this.updateCounts);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("updateCounts", this.updateCounts);
        putFieldPutFields.put("longUpdateCounts", this.longUpdateCounts);
        objectOutputStream.writeFields();
    }
}
