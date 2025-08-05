package sun.management.counter.perf;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.UnsupportedEncodingException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:sun/management/counter/perf/PerfDataType.class */
class PerfDataType {
    private final String name;
    private final byte value;
    private final int size;
    public static final PerfDataType BOOLEAN = new PerfDataType("boolean", Constants.HASIDCALL_INDEX_SIG, 1);
    public static final PerfDataType CHAR = new PerfDataType("char", "C", 1);
    public static final PerfDataType FLOAT = new PerfDataType(SchemaSymbols.ATTVAL_FLOAT, PdfOps.F_TOKEN, 8);
    public static final PerfDataType DOUBLE = new PerfDataType(SchemaSymbols.ATTVAL_DOUBLE, PdfOps.D_TOKEN, 8);
    public static final PerfDataType BYTE = new PerfDataType(SchemaSymbols.ATTVAL_BYTE, PdfOps.B_TOKEN, 1);
    public static final PerfDataType SHORT = new PerfDataType(SchemaSymbols.ATTVAL_SHORT, PdfOps.S_TOKEN, 2);
    public static final PerfDataType INT = new PerfDataType("int", "I", 4);
    public static final PerfDataType LONG = new PerfDataType(SchemaSymbols.ATTVAL_LONG, "J", 8);
    public static final PerfDataType ILLEGAL = new PerfDataType("illegal", "X", 0);
    private static PerfDataType[] basicTypes = {LONG, BYTE, BOOLEAN, CHAR, FLOAT, DOUBLE, SHORT, INT};

    public String toString() {
        return this.name;
    }

    public byte byteValue() {
        return this.value;
    }

    public int size() {
        return this.size;
    }

    public static PerfDataType toPerfDataType(byte b2) {
        for (int i2 = 0; i2 < basicTypes.length; i2++) {
            if (basicTypes[i2].byteValue() == b2) {
                return basicTypes[i2];
            }
        }
        return ILLEGAL;
    }

    private PerfDataType(String str, String str2, int i2) {
        this.name = str;
        this.size = i2;
        try {
            this.value = str2.getBytes("UTF-8")[0];
        } catch (UnsupportedEncodingException e2) {
            throw new InternalError("Unknown encoding", e2);
        }
    }
}
