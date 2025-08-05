package javax.sql;

import java.util.EventListener;

/* loaded from: rt.jar:javax/sql/RowSetListener.class */
public interface RowSetListener extends EventListener {
    void rowSetChanged(RowSetEvent rowSetEvent);

    void rowChanged(RowSetEvent rowSetEvent);

    void cursorMoved(RowSetEvent rowSetEvent);
}
