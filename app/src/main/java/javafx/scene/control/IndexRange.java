package javafx.scene.control;

import javafx.beans.NamedArg;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;

/* loaded from: jfxrt.jar:javafx/scene/control/IndexRange.class */
public final class IndexRange {
    private int start;
    private int end;
    public static final String VALUE_DELIMITER = ",";

    public IndexRange(@NamedArg("start") int start, @NamedArg(AsmConstants.END) int end) {
        if (end < start) {
            throw new IllegalArgumentException();
        }
        this.start = start;
        this.end = end;
    }

    public IndexRange(@NamedArg(AsmConstants.CODERANGE) IndexRange range) {
        this.start = range.start;
        this.end = range.end;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public int getLength() {
        return this.end - this.start;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof IndexRange) {
            IndexRange range = (IndexRange) object;
            return this.start == range.start && this.end == range.end;
        }
        return false;
    }

    public int hashCode() {
        return (31 * this.start) + this.end;
    }

    public String toString() {
        return this.start + ", " + this.end;
    }

    public static IndexRange normalize(int v1, int v2) {
        return new IndexRange(Math.min(v1, v2), Math.max(v1, v2));
    }

    public static IndexRange valueOf(String value) throws NumberFormatException {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        String[] values = value.split(",");
        if (values.length != 2) {
            throw new IllegalArgumentException();
        }
        int start = Integer.parseInt(values[0].trim());
        int end = Integer.parseInt(values[1].trim());
        return normalize(start, end);
    }
}
