package javax.sql;

import java.util.EventObject;

/* loaded from: rt.jar:javax/sql/RowSetEvent.class */
public class RowSetEvent extends EventObject {
    static final long serialVersionUID = -1875450876546332005L;

    public RowSetEvent(RowSet rowSet) {
        super(rowSet);
    }
}
