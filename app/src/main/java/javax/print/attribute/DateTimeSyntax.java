package javax.print.attribute;

import java.io.Serializable;
import java.util.Date;

/* loaded from: rt.jar:javax/print/attribute/DateTimeSyntax.class */
public abstract class DateTimeSyntax implements Serializable, Cloneable {
    private static final long serialVersionUID = -1400819079791208582L;
    private Date value;

    protected DateTimeSyntax(Date date) {
        if (date == null) {
            throw new NullPointerException("value is null");
        }
        this.value = date;
    }

    public Date getValue() {
        return new Date(this.value.getTime());
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof DateTimeSyntax) && this.value.equals(((DateTimeSyntax) obj).value);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return "" + ((Object) this.value);
    }
}
