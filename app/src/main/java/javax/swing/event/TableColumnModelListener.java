package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/TableColumnModelListener.class */
public interface TableColumnModelListener extends EventListener {
    void columnAdded(TableColumnModelEvent tableColumnModelEvent);

    void columnRemoved(TableColumnModelEvent tableColumnModelEvent);

    void columnMoved(TableColumnModelEvent tableColumnModelEvent);

    void columnMarginChanged(ChangeEvent changeEvent);

    void columnSelectionChanged(ListSelectionEvent listSelectionEvent);
}
